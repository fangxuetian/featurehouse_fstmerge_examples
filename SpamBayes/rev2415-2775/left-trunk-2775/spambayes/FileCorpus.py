"""FileCorpus.py - Corpus composed of file system artifacts
Classes:
    FileCorpus - an observable dictionary of FileMessages
    ExpiryFileCorpus - a FileCorpus of young files
    FileMessage - a subject of Spambayes training
    FileMessageFactory - a factory to create FileMessage objects
    GzipFileMessage - A FileMessage zipped for less storage
    GzipFileMessageFactory - factory to create GzipFileMessage objects
Abstract:
    These classes are concrete implementations of the Corpus framework.
    FileCorpus is designed to manage corpora that are directories of
    message files.
    ExpiryFileCorpus is an ExpiryCorpus of file messages.
    FileMessage manages messages that are files in the file system.
    FileMessageFactory is responsible for the creation of FileMessages,
    in response to requests to a corpus for messages.
    GzipFileMessage and GzipFileMessageFactory are used to persist messages
    as zipped files.  This can save a bit of persistent storage, though the
    ability of the compresser to do very much deflation is limited due to the
    relatively small size of the average textual message.  Still, for a large
    corpus, this could amount to a significant space savings.
    See Corpus.__doc__ for more information.
Test harness:
    FileCorpus [options]
        options:
            -h : show this message
            -v : execute in verbose mode, useful for general understanding
                 and debugging purposes
            -g : use GzipFileMessage and GzipFileMessageFactory
            -s : setup self test, useful for seeing what is going into the
                 test
            -t : setup and execute a self test.
            -c : clean up file system after self test
    Please note that running with -s or -t will create file system artifacts
    in the current directory.  Be sure this doesn't stomp something of
    yours...  The artifacts created are:
        fctestmisc.bayes
        fctestclass.bayes
        fctestspamcorpus/MSG00001
        fctestspamcorpus/MSG00002
        fctestunsurecorpus/MSG00003
        fctestunsurecorpus/MSG00004
        fctestunsurecorpus/MSG00005
        fctestunsurecorpus/MSG00006
        fctesthamcorpus/
    After the test has executed, the following file system artifacts
    (should) will exist:
        fctestmisc.bayes
        fctestclass.bayes
        fctestspamcorpus/MSG00001
        fctestspamcorpus/MSG00004
        fctesthamcorpus/MSG00002
        fctesthamcorpus/MSG00005
        fctesthamcorpus/MSG00006
        fctestunsurecorpus/
To Do:
    o Suggestions?
"""
__author__ = "Tim Stone <tim@fourstonesExpressions.com>"
__credits__ = "Richie Hindle, Tim Peters, all the spambayes contributors."
from __future__ import generators
import email
from spambayes import Corpus
from spambayes import message
from spambayes import storage
import sys, os, gzip, fnmatch, getopt, time, stat
from spambayes.Options import options
class FileCorpus(Corpus.Corpus):
    def __init__(self, factory, directory, filter='*', cacheSize=250):
        '''Constructor(FileMessageFactory, corpus directory name, fnmatch
filter'''
        Corpus.Corpus.__init__(self, factory, cacheSize)
        self.directory = directory
        self.filter = filter
        for filename in os.listdir(directory):
            if fnmatch.fnmatch(filename, filter):
                self.msgs[filename] = None
    def makeMessage(self, key, content=None):
        '''Ask our factory to make a Message'''
        msg = self.factory.create(key, self.directory, content)
        return msg
    def addMessage(self, message):
        '''Add a Message to this corpus'''
        if not fnmatch.fnmatch(message.key(), self.filter):
            raise ValueError
        if options["globals", "verbose"]:
            print 'adding',message.key(),'to corpus'
        message.directory = self.directory
        message.store()
        Corpus.Corpus.addMessage(self, message)
    def removeMessage(self, message, observer_flags=0):
        '''Remove a Message from this corpus'''
        if options["globals", "verbose"]:
            print 'removing',message.key(),'from corpus'
        message.remove()
        Corpus.Corpus.removeMessage(self, message, observer_flags)
    def __repr__(self):
        '''Instance as a representative string'''
        nummsgs = len(self.msgs)
        if nummsgs != 1:
            s = 's'
        else:
            s = ''
        if options["globals", "verbose"] and nummsgs > 0:
            lst = ', ' + '%s' % (self.keys())
        else:
            lst = ''
        return "<%s object at %8.8x, directory: %s, %s message%s%s>" % \
            (self.__class__.__name__, \
            id(self), \
            self.directory, \
            nummsgs, s, lst)
class ExpiryFileCorpus(Corpus.ExpiryCorpus, FileCorpus):
    '''FileCorpus of "young" file system artifacts'''
    def __init__(self, expireBefore, factory, directory, filter='*', cacheSize=250):
        '''Constructor(FileMessageFactory, corpus directory name, fnmatch
filter'''
        Corpus.ExpiryCorpus.__init__(self, expireBefore)
        FileCorpus.__init__(self, factory, directory, filter, cacheSize)
class FileMessage(message.SBHeaderMessage):
    '''Message that persists as a file system artifact.'''
    def __init__(self, file_name=None, directory=None):
        '''Constructor(message file name, corpus directory name)'''
        message.SBHeaderMessage.__init__(self)
        self.file_name = file_name
        self.directory = directory
        self.loaded = False
    def as_string(self):
        self.load() # ensure that the substance is loaded
        return message.SBHeaderMessage.as_string(self)
    def pathname(self):
        '''Derive the pathname of the message file'''
        assert self.file_name is not None, \
               "Must set filename before using FileMessage instances."
        assert self.directory is not None, \
               "Must set directory before using FileMessage instances."
        return os.path.join(self.directory, self.file_name)
    def load(self):
        '''Read the Message substance from the file'''
        if self.loaded:
            return
        assert self.file_name is not None, \
               "Must set filename before using FileMessage instances."
        if options["globals", "verbose"]:
            print 'loading', self.file_name
        pn = self.pathname()
        fp = gzip.open(pn, 'rb')
        try:
            self.setPayload(fp.read())
        except IOError, e:
            if str(e) == 'Not a gzipped file':
                fp.close()
                fp = open(self.pathname(), 'rb')
                self.setPayload(fp.read())
                fp.close()
        else:
            fp.close()
        self.loaded = True
    def store(self):
        '''Write the Message substance to the file'''
        assert self.file_name is not None, \
               "Must set filename before using FileMessage instances."
        if options["globals", "verbose"]:
            print 'storing', self.file_name
        fp = open(self.pathname(), 'wb')
        fp.write(self.as_string())
        fp.close()
    def setPayload(self, payload):
        self.loaded = True
        msg = email.message_from_string(payload)
        self.set_payload(msg.get_payload())
        self.set_unixfrom(msg.get_unixfrom())
        self.set_charset(msg.get_charset())
        for name, value in msg.items():
            del self[name]
            self[name] = value
    def remove(self):
        '''Message hara-kiri'''
        if options["globals", "verbose"]:
            print 'physically deleting file',self.pathname()
        try:
            os.unlink(self.pathname())
        except OSError:
            if options["globals", "verbose"]:
                print 'file', self.pathname(), 'can not be deleted'
    def name(self):
        '''A unique name for the message'''
        assert self.file_name is not None, \
               "Must set filename before using FileMessage instances."
        return self.file_name
    def key(self):
        '''The key of this message in the msgs dictionary'''
        assert self.file_name is not None, \
               "Must set filename before using FileMessage instances."
        return self.file_name
    def __repr__(self):
        '''Instance as a representative string'''
        sub = self.as_string()
        if not options["globals", "verbose"]:
            if len(sub) > 20:
                if len(sub) > 40:
                    sub = sub[:20] + '...' + sub[-20:]
                else:
                    sub = sub[:20]
        return "<%s object at %8.8x, file: %s, %s>" % \
            (self.__class__.__name__, \
            id(self), self.pathname(), sub)
    def __str__(self):
        '''Instance as a printable string'''
        return self.__repr__()
    def createTimestamp(self):
        '''Return the create timestamp for the file'''
        try:
            stats = os.stat(self.pathname())
        except OSError:
            ctime = time.time()
        else:
            ctime = stats[stat.ST_CTIME]
        return ctime
class FileMessageFactory(Corpus.MessageFactory):
    '''MessageFactory for FileMessage objects'''
    def create(self, key, directory, content=None):
        '''Create a message object from a filename in a directory'''
        if content:
            msg = email.message_from_string(content, _class=FileMessage)
            msg.file_name = key
            msg.directory = directory
            msg.loaded = True
            return msg
        return FileMessage(key, directory)
class GzipFileMessage(FileMessage):
    '''Message that persists as a zipped file system artifact.'''
    def store(self):
        '''Write the Message substance to the file'''
        assert self.file_name is not None, \
               "Must set filename before using FileMessage instances."
        if options["globals", "verbose"]:
            print 'storing', self.file_name
        pn = self.pathname()
        gz = gzip.open(pn, 'wb')
        gz.write(self.as_string())
        gz.flush()
        gz.close()
class GzipFileMessageFactory(FileMessageFactory):
    '''MessageFactory for FileMessage objects'''
    def create(self, key, directory, content=None):
        '''Create a message object from a filename in a directory'''
        if content:
            msg = email.message_from_string(content,
                                            _class=GzipFileMessage)
            msg.file_name = key
            msg.directory = directory
            msg.loaded = True
            return msg
        return GzipFileMessage(key, directory)
def runTest(useGzip):
    print 'Executing Test'
    if useGzip:
        fmFact = GzipFileMessageFactory()
        print 'Executing with Gzipped files'
    else:
        fmFact =  FileMessageFactory()
        print 'Executing with uncompressed files'
    print '\n\nCreating two Classifier databases'
    miscbayes = storage.PickledClassifier('fctestmisc.bayes')
    classbayes = storage.DBDictClassifier('fctestclass.bayes')
    print '\n\nSetting up spam corpus'
    spamcorpus = FileCorpus(fmFact, 'fctestspamcorpus')
    spamtrainer = storage.SpamTrainer(miscbayes)
    spamcorpus.addObserver(spamtrainer)
    anotherspamtrainer = storage.SpamTrainer(classbayes, storage.UPDATEPROBS)
    spamcorpus.addObserver(anotherspamtrainer)
    keys = spamcorpus.keys()
    keys.sort()
    for key in keys:                          # iterate the list of keys
        msg = spamcorpus[key]                 # corpus is a dictionary
        spamtrainer.train(msg)
        anotherspamtrainer.train(msg)
    print '\n\nSetting up ham corpus'
    hamcorpus = FileCorpus(fmFact, \
                          'fctesthamcorpus', \
                          'MSG*')
    hamtrainer = storage.HamTrainer(miscbayes)
    hamcorpus.addObserver(hamtrainer)
    hamtrainer.trainAll(hamcorpus)
    print '\n\nA couple of message related tests'
    if useGzip:
        fmFactory = GzipFileMessageFactory()
    else:
        fmFactory = FileMessageFactory()
    m1 = fmFactory.create('XMG00001', 'fctestspamcorpus', testmsg2())
    print '\n\nAdd a message to hamcorpus that does not match the filter'
    try:
        hamcorpus.addMessage(m1)
    except ValueError:
        print 'Add failed, test passed'
    else:
        print 'Add passed, test failed'
    print '\n\nThis is the hamcorpus'
    print hamcorpus
    print '\n\nThis is the spamcorpus'
    print spamcorpus
    print '\n\nSetting up unsure corpus'
    unsurecorpus = ExpiryFileCorpus(5, fmFact, \
                                    'fctestunsurecorpus', 'MSG*', 2)
    unsurecorpus.removeExpiredMessages()
    print '\n\nIterate the unsure corpus twice, to make sure cache size \
is managed correctly, and to make sure iteration is repeatable. \
We should not see MSG00003 in this iteration.'
    for msg in unsurecorpus:
        print msg.key()    # don't print msg, too much information
    print '...and again'
    for msg in unsurecorpus:
        print msg.key()    # don't print msg, too much information
    print '\n\nRemoving expired messages from unsure corpus.'
    unsurecorpus.removeExpiredMessages()
    print '\n\nTrain with an individual message'
    anotherhamtrainer = storage.HamTrainer(classbayes)
    anotherhamtrainer.train(unsurecorpus['MSG00005'])
    print '\n\nMoving msg00002 from spamcorpus to hamcorpus'
    hamcorpus.takeMessage('MSG00002', spamcorpus)   # Oops. made a mistake...
    print "\n\nLet's test printing a message"
    msg = spamcorpus['MSG00001']
    print msg
    print '\n\nClassifying messages in unsure corpus'
    for msg in unsurecorpus:
        prob = classbayes.spamprob(msg.tokenize())
        print 'Message %s spam probability is %f' % (msg.key(), prob)
        if prob < options["Categorization", "ham_cutoff"]:
            print 'Moving %s from unsurecorpus to hamcorpus, \
based on prob of %f' % (msg.key(), prob)
            hamcorpus.takeMessage(msg.key(), unsurecorpus)
        elif prob > options["Categorization", "spam_cutoff"]:
            print 'Moving %s from unsurecorpus to spamcorpus, \
based on prob of %f' % (msg.key(), prob)
            spamcorpus.takeMessage(msg.key(), unsurecorpus)
    print '\n\nThis is the new hamcorpus'
    print hamcorpus
    print '\n\nThis is the new spamcorpus'
    print spamcorpus
    print '\n\nThis is the new unsurecorpus'
    print unsurecorpus
    print 'unsurecorpus cache contains', unsurecorpus.keysInMemory
    print 'unsurecorpus msgs dict contains', unsurecorpus.msgs
    print '\n\nStoring bayes databases'
    miscbayes.store()
    classbayes.store()
def cleanupTest():
    print 'Cleaning up'
    cleanupDirectory('fctestspamcorpus')
    cleanupDirectory('fctesthamcorpus')
    cleanupDirectory('fctestunsurecorpus')
    if not useExistingDB:
        try:
            os.unlink('fctestmisc.bayes')
        except OSError, e:
            if e.errno != 2:     # errno.<WHAT>
                raise
        try:
            os.unlink('fctestclass.bayes')
        except OSError, e:
            if e.errno != 2:     # errno.<WHAT>
                raise
def cleanupDirectory(dirname):
    try:
        flist = os.listdir(dirname)
    except OSError, e:
        if e.errno != 3:     # errno.<WHAT>
            raise
    else:
        for filename in flist:
            fn = os.path.join(dirname, filename)
            os.unlink(fn)
    try:
        os.rmdir(dirname)
    except OSError, e:
        if e.errno != 2:     # errno.<WHAT>
            raise
def setupTest(useGzip):
    cleanupTest()
    print 'Setting up test'
    os.mkdir('fctestspamcorpus')
    os.mkdir('fctesthamcorpus')
    os.mkdir('fctestunsurecorpus')
    tm1 = testmsg1()
    tm2 = testmsg2()
    if useGzip:
        fmFactory = GzipFileMessageFactory()
    else:
        fmFactory = FileMessageFactory()
    m1 = fmFactory.create('MSG00001', 'fctestspamcorpus', tm1)
    m1.store()
    m2 = fmFactory.create('MSG00002', 'fctestspamcorpus', tm2)
    m2.store()
    m3 = fmFactory.create('MSG00003', 'fctestunsurecorpus', tm1)
    m3.store()
    for x in range(11):
        time.sleep(1)    # make sure MSG00003 has expired
        if 10-x == 1:
            s = ''
        else:
            s = 's'
        print 'wait',10-x,'more second%s' % (s)
    m4 = fmFactory.create('MSG00004', 'fctestunsurecorpus', tm1)
    m4.store()
    m5 = fmFactory.create('MSG00005', 'fctestunsurecorpus', tm2)
    m5.store()
    m6 = fmFactory.create('MSG00006', 'fctestunsurecorpus', tm2)
    m6.store()
def testmsg1():
    return """
X-Hd:skip@pobox.com Mon Nov 04 10:50:49 2002
Received:by mail.powweb.com (mbox timstone) (with Cubic Circle's cucipop (v1.31
1998/05/13) Mon Nov 4 08:50:58 2002)
X-From_:skip@mojam.com Mon Nov 4 08:49:03 2002
Return-Path:<skip@mojam.com>
Delivered-To:timstone@mail.powweb.com
Received:from manatee.mojam.com (manatee.mojam.com [199.249.165.175]) by
mail.powweb.com (Postfix) with ESMTP id DC95A1BB1D0 for
<tim@fourstonesExpressions.com>; Mon, 4 Nov 2002 08:49:02 -0800 (PST)
Received:from montanaro.dyndns.org (12-248-11-90.client.attbi.com
[12.248.11.90]) by manatee.mojam.com (8.12.1/8.12.1) with ESMTP id
gA4Gn0oY029655 for <tim@fourstonesExpressions.com>; Mon, 4 Nov 2002 10:49:00
-0600
Received:from montanaro.dyndns.org (localhost [127.0.0.1]) by
montanaro.dyndns.org (8.12.2/8.12.2) with ESMTP id gA4Gn3cP015572 for
<tim@fourstonesExpressions.com>; Mon, 4 Nov 2002 10:49:03 -0600 (CST)
Received:(from skip@localhost) by montanaro.dyndns.org (8.12.2/8.12.2/Submit)
id gA4Gn37l015569; Mon, 4 Nov 2002 10:49:03 -0600 (CST)
From:Skip Montanaro <skip@pobox.com>
MIME-Version:1.0
Content-Type:text/plain; charset=us-ascii
Content- Transfer- Encoding:7bit
Message-ID:<15814.42238.882013.702030@montanaro.dyndns.org>
Date:Mon, 4 Nov 2002 10:49:02 -0600
To:Four Stones Expressions <tim@fourstonesExpressions.com>
Subject:Reformat mail to 80 columns?
In-Reply-To:<QOIDLHRPNK62FBRPA9SM54US7504UR65.3dc5eed1@riven>
References:<8285NLPL5YTTQJGXTAXU3WA8OB2.3dc5e3cc@riven>
<QOIDLHRPNK62FBRPA9SM54US7504UR65.3dc5eed1@riven>
X-Mailer:VM 7.07 under 21.5 (beta9) "brussels sprouts" XEmacs Lucid
Reply-To:skip@pobox.com
X-Hammie- Disposition:Unsure
11/4/2002 10:49:02 AM, Skip Montanaro <skip@pobox.com> wrote:
>(off-list)
>
>Tim,
>
>Any chance you can easily generate messages to the spambayes list which wrap
>at something between 70 and 78 columns?  I find I have to always edit your
>messages to read them easily.
>
>Thanks,
>
>--
>Skip Montanaro - skip@pobox.com
>http://www.mojam.com/
>http://www.musi-cal.com/
>
>
- Tim
www.fourstonesExpressions.com """
def testmsg2():
    return """
X-Hd:richie@entrian.com Wed Nov 06 12:05:41 2002
Received:by mail.powweb.com (mbox timstone) (with Cubic Circle's cucipop (v1.31
1998/05/13) Wed Nov 6 10:05:45 2002)
X-From_:richie@entrian.com Wed Nov 6 10:05:33 2002
Return-Path:<richie@entrian.com>
Delivered-To:timstone@mail.powweb.com
Received:from anchor-post-31.mail.demon.net (anchor-post-31.mail.demon.net
[194.217.242.89]) by mail.powweb.com (Postfix) with ESMTP id 3DC431BB06A for
<tim@fourstonesexpressions.com>; Wed, 6 Nov 2002 10:05:33 -0800 (PST)
Received:from sundog.demon.co.uk ([158.152.226.183]) by
anchor-post-31.mail.demon.net with smtp (Exim 3.35 #1) id 189UYP-000IAw-0V for
tim@fourstonesExpressions.com; Wed, 06 Nov 2002 18:05:25 +0000
From:Richie Hindle <richie@entrian.com>
To:tim@fourstonesExpressions.com
Subject:Re: What to call this training stuff
Date:Wed, 06 Nov 2002 18:05:56 +0000
Organization:entrian.com
Reply-To:richie@entrian.com
Message-ID:<d0hisugn3nau4m704kotgpd4jlt33rvrda@4ax.com>
References:<IFWRHE041VTXW72JGDBD0RTS04YTGE.3dc933a1@riven>
In-Reply-To:<IFWRHE041VTXW72JGDBD0RTS04YTGE.3dc933a1@riven>
X-Mailer:Forte Agent 1.7/32.534
MIME-Version:1.0
Content-Type:text/plain; charset=us-ascii
Content- Transfer- Encoding:7bit
X-Hammie- Disposition:Unsure
Hi Tim,
> Richie, I think we should package these classes I've been writing as
> 'corpusManagement.py'  What we're really doing here is creating a set of
tools
> that can be used to manage corpi (?) corpusses (?)  corpae (?)  whatever...
of
> messages.
Good plan.  Minor point of style: mixed-case module names (like class
names) tend to have an initial capital: CorpusManagement.py
On the name... sorry to disagree about names again, but what does the word
'management' add?  This is a module for manipulating corpuses, so I reckon
it should be called Corpus.  Like Cookie, gzip, zipfile, locale, mailbox...
see what I mean?
--
Richie Hindle
richie@entrian.com"""
if __name__ == '__main__':
    try:
        opts, args = getopt.getopt(sys.argv[1:], 'estgvhcu')
    except getopt.error, msg:
        print >>sys.stderr, str(msg) + '\n\n' + __doc__
        sys.exit()
    options["globals", "verbose"] = False
    runTestServer = False
    setupTestServer = False
    cleanupTestServer = False
    useGzip = False
    useExistingDB = False
    for opt, arg in opts:
        if opt == '-h':
            print >>sys.stderr, __doc__
            sys.exit()
        elif opt == '-s':
            setupTestServer = True
        elif opt == '-e':
            runTestServer = True
        elif opt == '-t':
            setupTestServer = True
            runTestServer = True
        elif opt == '-c':
            cleanupTestServer = True
        elif opt == '-v':
            options["globals", "verbose"] = True
        elif opt == '-g':
            useGzip = True
        elif opt == '-u':
            useExistingDB = True
    if setupTestServer:
        setupTest(useGzip)
    if runTestServer:
        runTest(useGzip)
    elif cleanupTestServer:
        cleanupTest()
    else:
        print >>sys.stderr, __doc__
