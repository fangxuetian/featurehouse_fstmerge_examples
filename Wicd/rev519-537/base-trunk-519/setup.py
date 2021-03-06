from distutils.core import setup, Command
import os
import shutil
import sys
VERSION_NUM = '1.5.3'
class configure(Command):
    description = "configure the paths that Wicd will be installed to"
    user_options = [
        ('lib=', None, 'set the lib directory'),
        ('share=', None, 'set the share directory'),
        ('etc=', None, 'set the etc directory'),
        ('images=', None, 'set the image directory'),
        ('encryption=', None, 'set the encryption template directory'),
        ('bin=', None, 'set the bin directory'),
        ('sbin=', None, 'set the sbin directory'),
        ('networks=', None, 'set the encryption configuration directory'),
        ('log=', None, 'set the log directory'),
        ('resume=', None, 'set the directory the resume from suspend script is stored in'),
        ('suspend=', None, 'set the directory the  suspend script is stored in'),
        ('pmutils=', None, 'set the directory the  pm-utils hooks are stored in'),
        ('dbus=', None, 'set the directory the dbus config file is stored in'),
        ('desktop=', None, 'set the directory the .desktop file is stored in'),
        ('icons=', None, "set the base directory for the .desktop file's icons"),
        ('translations=', None, 'set the directory translations are stored in'),
        ('autostart=', None, 'set the directory that will be autostarted on desktop login'),
        ('init=', None, 'set the directory for the init file'),
        ('docdir=', None, 'set the directory for the documentation'),
        ('mandir=', None, 'set the directory for the man pages'),
        ('kdedir=', None, 'set the kde autostart directory'),
        ('python=', None, 'set the path to the Python executable'),
        ('pidfile=', None, 'set the pid file'),
        ('initfile=', None, 'set the init file to use'),
        ('initfilename=', None, "set the name of the init file (don't use)"),
        ('no-install-init', None, "do not install the init file"),
        ('no-install-man', None, 'do not install the man file'),
        ('no-install-kde', None, 'do not install the kde autostart file'),
        ('no-install-acpi', None, 'do not install the suspend.d and resume.d acpi scripts'),
        ('no-install-pmutils', None, 'do not install the pm-utils hooks'),
        ('no-install-docs', None, 'do not install the auxiliary documentation')
        ]
    def initialize_options(self):
        self.lib = '/usr/lib/wicd/'
        self.share = '/usr/share/wicd/'
        self.etc = '/etc/wicd/'
        self.icons = '/usr/share/icons/hicolor/'
        self.images = '/usr/share/pixmaps/wicd/'
        self.encryption = self.etc + 'encryption/templates/'
        self.bin = '/usr/bin/'
        self.sbin = '/usr/sbin/'
        self.networks = '/var/lib/wicd/configurations/'
        self.log = '/var/log/wicd/'
        self.resume = '/etc/acpi/resume.d/'
        self.suspend = '/etc/acpi/suspend.d/'
        self.pmutils = '/usr/lib/pm-utils/sleep.d/'
        self.dbus = '/etc/dbus-1/system.d/'
        self.desktop = '/usr/share/applications/'
        self.translations = '/usr/share/locale/'
        self.autostart = '/etc/xdg/autostart/'
        self.docdir = '/usr/share/doc/wicd/'
        self.mandir = '/usr/share/man/'
        self.kdedir = '/usr/share/autostart/'
        self.no_install_init = False
        self.no_install_man = False
        self.no_install_kde = False
        self.no_install_acpi = False
        self.no_install_pmutils = False
        self.no_install_docs = False
        self.distro_detect_failed = False
        self.initfile = 'init/default/wicd'
        if os.access('/etc/redhat-release', os.F_OK):
            self.init = '/etc/rc.d/init.d/'
            self.initfile = 'init/redhat/wicd'
        elif os.access('/etc/SuSE-release', os.F_OK):
            self.init = '/etc/init.d/'
            self.initfile = 'init/suse/wicd'
        elif os.access('/etc/fedora-release', os.F_OK):
            self.init = '/etc/rc.d/init.d/'
            self.initfile = 'init/redhat/wicd'
        elif os.access('/etc/gentoo-release', os.F_OK):
            self.init = '/etc/init.d/'
            self.initfile = 'init/gentoo/wicd'
        elif os.access('/etc/debian_version', os.F_OK):
            self.init = '/etc/init.d/'
            self.initfile = 'init/debian/wicd'
        elif os.access('/etc/arch-release', os.F_OK):
            self.init = '/etc/rc.d/'
            self.initfile = 'init/arch/wicd'
        elif os.access('/etc/slackware-version', os.F_OK) or \
             os.access('/etc/slamd64-version', os.F_OK):
            self.init = '/etc/rc.d/'
            self.initfile = 'init/slackware/rc.wicd'
            self.docdir = '/usr/doc/wicd-%s' % VERSION_NUM
            self.mandir = '/usr/man/'
            self.no_install_acpi = True
            self.no_install_pmutils = True
        elif os.access('/etc/pld-release', os.F_OK):
            self.init = '/etc/rc.d/init.d/'
            self.initfile = 'init/pld/wicd'
        else:
            self.init = 'FAIL'
            self.initfile = 'FAIL'
            self.no_install_init = True
            self.distro_detect_failed = True
            print 'WARNING: Unable to detect the distribution in use.  ' + \
                  'If you have specified --init and --initfile, configure will continue.  ' + \
                  'Please report this warning, along with the name of your ' + \
                  'distribution, to the wicd developers.'
        self.python = '/usr/bin/python'
        self.pidfile = '/var/run/wicd/wicd.pid'
        self.initfilename = os.path.basename(self.initfile)
    def finalize_options(self):
        if self.distro_detect_failed == True:
            if not self.no_install_init:
                if self.init == 'FAIL' or self.initfile == 'FAIL':
                    print 'ERROR: Failed to detect distro. Configure cannot continue.  ' + \
                          'Please specify --init and --initfile to continue with configuration.'
        for argument in self.user_options:
            argument_name = argument[0][:-1]
            value = getattr(self, argument_name)
            if not argument[0][:-1] == "python":
                if not value.endswith("/"):
                    setattr(self, argument_name, value + "/")
            else:
                return
    def run(self):
        values = list()
        for argument in self.user_options:
            if argument[0].endswith('='):
                print argument[0][:-1],'is',
                print getattr(self, argument[0][:-1])
                values.append((argument[0][:-1], getattr(self, argument[0][:-1].replace('-','_'))))
            else:
                print "Found switch",argument,getattr(self, argument[0].replace('-','_'))
                values.append((argument[0], bool(getattr(self, argument[0].replace('-','_')))))
        print 'Replacing values in template files...'
        for item in os.listdir('in'):
            if item.endswith('.in'):
                print 'Replacing values in',item,
                original_name = os.path.join('in',item)
                item_in = open(original_name, 'r')
                final_name = item[:-3].replace('=','/')
                print final_name
                item_out = open(final_name, 'w')
                for line in item_in.readlines():
                    for item, value in values:
                        line = line.replace('%' + str(item.upper().replace('-','_')) + '%', str(value))
                    item_out.write(line)
                item_out.close()
                item_in.close()
                shutil.copymode(original_name, final_name)
class get_translations(Command):
    description = "download the translations from the online translator"
    user_options = []
    def initialize_options(self):
        pass
    def finalize_options(self):
        pass
    def run(self):
        import urllib, shutil
        shutil.rmtree('translations/')
        os.makedirs('translations')
        filename, headers = urllib.urlretrieve('http://wicd.net/translator/get_id_list.php')
        id_file = open(filename, 'r')
        lines = id_file.readlines()
        lines = [ x.strip() for x in lines if not x.strip() is '' ]
        for id in lines:
            pofile, poheaders = urllib.urlretrieve('http://wicd.net/translator/download_po.php?language='+str(id))
            lang_identifier = open(pofile,'r').readlines()[1].strip()[2:]
            shutil.move(pofile, lang_identifier+'.po')
            print 'Got',lang_identifier
            os.makedirs('translations/'+lang_identifier+'/LC_MESSAGES')
            os.system('msgfmt --output-file=translations/'+lang_identifier+'/LC_MESSAGES/wicd.mo '+lang_identifier+'.po')
            os.remove(lang_identifier+'.po')
class uninstall(Command):
    description = "remove Wicd using uninstall.sh and install.log"
    user_options = []
    def initialize_options(self):
        pass
    def finalize_options(self):
        pass
    def run(self):
        os.system("./uninstall.sh")
try:
    import wpath
except:
    print '''Error importing wpath.py. You can safely ignore this
message. It is probably because you haven't run python setup.py
configure yet or you are running it for the first time.'''
data = []
try:
    print "Using init file",(wpath.init, wpath.initfile)
    data = [
    (wpath.dbus, ['other/wicd.conf']),
    (wpath.desktop, ['other/wicd.desktop']),
    (wpath.log, []), 
    (wpath.etc, []),
    (wpath.icons + 'scalable/apps/', ['icons/scalable/wicd-client.svg']),
    (wpath.icons + '192x192/apps/', ['icons/192px/wicd-client.png']),
    (wpath.icons + '128x128/apps/', ['icons/128px/wicd-client.png']),
    (wpath.icons + '96x96/apps/', ['icons/96px/wicd-client.png']),
    (wpath.icons + '72x72/apps/', ['icons/72px/wicd-client.png']),
    (wpath.icons + '64x64/apps/', ['icons/64px/wicd-client.png']),
    (wpath.icons + '48x48/apps/', ['icons/48px/wicd-client.png']),
    (wpath.icons + '36x36/apps/', ['icons/36px/wicd-client.png']),
    (wpath.icons + '32x32/apps/', ['icons/32px/wicd-client.png']),
    (wpath.icons + '24x24/apps/', ['icons/24px/wicd-client.png']),
    (wpath.icons + '22x22/apps/', ['icons/22px/wicd-client.png']),
    (wpath.icons + '16x16/apps/', ['icons/16px/wicd-client.png']),
    (wpath.images, [('images/' + b) for b in os.listdir('images') if not b.startswith('.')]),
    (wpath.encryption, [('encryption/templates/' + b) for b in os.listdir('encryption/templates') if not b.startswith('.')]),
    (wpath.networks, []),
    (wpath.bin, ['scripts/wicd-client', ]), 
    (wpath.sbin,  ['scripts/wicd', ]),  
    (wpath.share, ['data/wicd.glade', ]),
    (wpath.lib, ['wicd/wicd-client.py', 'wicd/monitor.py', 'wicd/wicd-daemon.py', 'wicd/configscript.py', 'wicd/suspend.py', 'wicd/autoconnect.py']), #'wicd/wicd-gui.py', 
    (wpath.autostart, ['other/wicd-tray.desktop', ]),
    ]
    piddir = os.path.dirname(wpath.pidfile)
    if not piddir.endswith('/'):
        piddir += '/'
    data.append (( piddir, [] ))
    if not wpath.no_install_docs:
        data.append(( wpath.docdir, [ 'INSTALL', 'LICENSE', 'AUTHORS', 'README' ]))
    if not wpath.no_install_kde:
        data.append(( wpath.kdedir, [ 'other/wicd-tray.desktop' ]))
    if not wpath.no_install_init:
        data.append(( wpath.init, [ wpath.initfile ]))
    if not wpath.no_install_man:
        data.append(( wpath.mandir + 'man8/', [ 'man/wicd.8' ]))
        data.append(( wpath.mandir + 'man5/', [ 'man/wicd-manager-settings.conf.5' ]))
        data.append(( wpath.mandir + 'man5/', [ 'man/wicd-wired-settings.conf.5' ]))
        data.append(( wpath.mandir + 'man5/', [ 'man/wicd-wireless-settings.conf.5' ]))
        data.append(( wpath.mandir + 'man1/', [ 'man/wicd-client.1' ]))
    if not wpath.no_install_acpi:
        data.append(( wpath.resume, ['other/80-wicd-connect.sh' ]))
        data.append(( wpath.suspend, ['other/50-wicd-suspend.sh' ]))
    if not wpath.no_install_pmutils:
        data.append(( wpath.pmutils, ['other/55-wicd' ]))
    print 'Creating pid path', os.path.basename(wpath.pidfile)
    print 'Language support for',
    for language in os.listdir('translations/'):
        if not language.startswith('.'):
            codes = language.split('_')
            short_language = language
            if codes[0].lower() == codes[1].lower():
                short_language = codes[0].lower()
            print short_language,
            data.append((wpath.translations + short_language + '/LC_MESSAGES/', ['translations/' + language + '/LC_MESSAGES/wicd.mo']))
    print
except:
    print '''Error setting up data array. This is normal if 
python setup.py configure has not yet been run.'''
setup(cmdclass={'configure' : configure, 'get_translations' : get_translations, 'uninstall' : uninstall},
      name="Wicd",
      version=VERSION_NUM,
      description="A wireless and wired network manager",
      long_description="""A complete network connection manager
Wicd supports wired and wireless networks, and capable of
creating and tracking profiles for both.  It has a 
template-based wireless encryption system, which allows the user
to easily add encryption methods used.  It ships with some common
encryption types, such as WPA and WEP. Wicd will automatically
connect at startup to any preferred network within range.
""",
      author="Adam Blackburn, Dan O'Reilly",
      author_email="compwiz18@users.sourceforge.net, imdano@users.sourceforge.net",
      url="http://wicd.net",
      license="http://www.gnu.org/licenses/old-licenses/gpl-2.0.html",
      py_modules=['wicd.networking', 'wicd.misc', 'wicd.gui', 'wicd.wnettools', 'wicd.wpath'],
      data_files=data
      )
