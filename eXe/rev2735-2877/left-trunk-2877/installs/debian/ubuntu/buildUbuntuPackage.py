import sys, os
exeDir = '../../..'
sys.path.insert(0, exeDir)
from exe.engine.path import Path, TempDirPath
from getpass import getpass
exeDir = Path(exeDir).abspath()
usage = """
Build ubuntu and debian packages of exe script.
Usage: %s [build] [index] [sftp-edu|sftp-sf username]
build - Builds the package
index - Downloads cusomtized twisted package and generates Packages.gz file
sftp-edu - Uploads indexes and package to eduforge
sftp-sf - Uploads indexes and package to sourceforge
username and pasword for your eduforge/sourceforge account are needed for sftp and should
always be the last two parameters
Debugging options:
copy - Copies the index files, so you can use just copy instead of index next time instead of re-indexing.
       Ie. First time use "index copy". Next time just use "copy"
checkDirs - Checks that a nice directory structure exists on the server
"""
if len(sys.argv) == 1:
    print usage
    sys.exit(1)
server = None
if 'sftp-edu' in sys.argv or 'sftp-sf' in sys.argv:
    try:
        from paramiko import Transport
    except ImportError:
        print
        print 'To upload you need to install paramiko python library from:'
        print 'http://www.lag.net/paramiko',
        print 'or go: apt-get install python-paramiko'
        print 'Or remove "sftp-edu", "sft-sf" and username from command line'
        print
        sys.exit(2)
    if 'sftp-edu' in sys.argv:
        pos = sys.argv.index('sftp-edu')
    else:
        pos = sys.argv.index('sftp-sf')
    if len(sys.argv) != pos + 2:
        print
        print 'You passed "sftp-*" on the command line but it was not followed by a username'
        print usage
        sys.exit(1)
    if 'sftp-edu' in sys.argv:
        server = 'shell.eduforge.org'
        basedir = '/home/pub/exe/'
    elif 'sftp-sf' in sys.argv:
        server = 'ssh.sourceforge.net'
        basedir = '/home/groups/e/ex/exe/htdocs/'
    print 'Please enter password for %s@%s:' % (sys.argv[-1], server)
    password = getpass()
if 'build' in sys.argv:
    newDir = exeDir/'debian'
    if newDir.islink():
        print 'Removing old link'
        newDir.remove()
    elif newDir.exists():
        raise Exception('exe/debian directory/file already exists, aborting....')
    Path('debian').abspath().symlink(newDir)
    exeDir.chdir()
    ok = True
    print
    if not os.path.exists('/usr/bin/fakeroot'):
        ok = False
        print '"fakeroot" not found. Go: sudo apt-get install fakeroot'
    if not os.path.exists('/usr/bin/make'):
        ok = False
        print '"make" not found. Go: sudo apt-get install make'
    if not os.path.exists('/usr/bin/dh_testdir'):
        ok = False
        print '"dh_testdir" not found. Go: sudo apt-get install dh-make'
    if not os.path.exists('/usr/bin/ncftpget'):
        ok = False
        print '"ncftpget" not found. Go: sudo apt-get install ncftp'
    if not ok:
        sys.exit(1)
    os.system('fakeroot debian/rules binary')
packages = (exeDir/'..').glob('*.deb')
if not packages:
    print 'No packages found'
    sys.exit(1)
packages.sort()
package = packages[-1]
tmp = None
if 'index' in sys.argv:
    print 'Creating Index...'
    tmp = TempDirPath()
    pool = tmp/'pool'
    pool.mkdir()
    package.copyfile(pool/package.basename())
    pool.chdir()
    tmp.chdir()
    os.system('dpkg-scanpackages pool /dev/null | gzip -9c > pool/Packages.gz')
if 'copy' in sys.argv:
    if 'index' in sys.argv:
        print 'copying index file to', exeDir
        (pool/'Packages.gz').copy(exeDir)
    pool = exeDir
if server:
    print 'connecting to %s...' % server
    from socket import socket, gethostbyname
    s = socket()
    s.connect((gethostbyname(server), 22))
    t = Transport(s)
    t.connect()
    t.auth_password(sys.argv[-1], password)
    f = t.open_sftp_client()
    f.chdir(basedir)
    poolDir = 'ubuntu/pool'
    packageDirs = [
        'ubuntu/dists/current/main/binary-i386',
        'ubuntu/dists/current/main/binary-arm',
        'ubuntu/dists/current/main/binary-alpha',
        ]
    if 'checkDirs' in sys.argv:
        print 'Checking directory structure...'
        for fn in packageDirs + [poolDir]:
            for part in fn.split('/'):
                files = f.listdir()
                if part not in files:
                    print 'Creating Dir on server:', fn, ':', part
                    f.mkdir(part)
                f.chdir(part)
            f.chdir(basedir)
    def upFile(filename):
        rFilename = str(filename.basename())
        if rFilename in f.listdir():
            f.remove(rFilename)
        f.put(filename, rFilename)
        f.chmod(rFilename, 0x774)
    if server == 'shell.eduforge.org':
        print 'Uploading Package %s to main dir...' % package.basename()
        upFile(package)
    print 'Uploading Package %s to pool dir...' % package
    f.chdir(basedir + poolDir)
    upFile(package)
    print 'Uploading indexes...'
    for fn in packageDirs:
        print '  ', fn.split('/')[-1], '...'
        f.chdir(basedir+fn)
        upFile(pool/'Packages.gz')
    print 'done'
