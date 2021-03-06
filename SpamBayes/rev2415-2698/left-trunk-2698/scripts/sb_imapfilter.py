"""An IMAP filter.  An IMAP message box is scanned and all non-scored
messages are scored and (where necessary) filtered.
The original filter design owed much to isbg by Roger Binns
(http://www.rogerbinns.com/isbg).
Usage:
    sb_imapfilter [options]
        note: option values with spaces in them must be enclosed
              in double quotes
        options:
            -p  dbname  : pickled training database filename
            -d  dbname  : dbm training database filename
            -t          : train contents of spam folder and ham folder
            -c          : classify inbox
            -h          : display this message
            -v          : verbose mode
            -P          : security option to prompt for imap password,
                          rather than look in options["imap", "password"]
            -e y/n      : expunge/purge messages on exit (y) or not (n)
            -i debuglvl : a somewhat mysterious imaplib debugging level
                          (4 is a good level, and suitable for bug reports)
            -l minutes  : period of time between filtering operations
            -b          : Launch a web browser showing the user interface.
            -o section:option:value :
                          set [section, option] in the options database
                          to value
Examples:
    Classify inbox, with dbm database
        sb_imapfilter -c -d bayes.db
    Train Spam and Ham, then classify inbox, with dbm database
        sb_imapfilter -t -c -d bayes.db
    Train Spam and Ham only, with pickled database
        sb_imapfilter -t -p bayes.db
Warnings:
    o This is beta software.  While we are reasonably confident that it
      should work as designed, there is a lot of difference between IMAP
      servers, and we can only test with a few.  We never delete mail,
      unless you use the -e/purge option, but we do mark a lot as deleted,
      and your mail client might remove that for you.  We try to only mark
      as deleted once the moved/altered message is correctly saved, but
      things might go wrong.  We *strongly* recommend that you try this
      script out on mail that you can recover from somewhere else, at least
      at first.
"""
todo = """
    o IMAP supports authentication via other methods than the plain-text
      password method that we are using at the moment.  Neither of the
      servers I have access to offer any alternative method, however.  If
      someone's does, then it would be nice to offer this.
    o Usernames should be able to be literals as well as quoted strings.
      This might help if the username/password has special characters like
      accented characters.
    o Suggestions?
"""
__author__ = "Tony Meyer <ta-meyer@ihug.co.nz>, Tim Stone"
__credits__ = "All the SpamBayes folk."
from __future__ import generators
try:
    True, False
except NameError:
    True, False = 1, 0
import socket
import os
import re
import time
import sys
import getopt
import types
import thread
import traceback
import email
import email.Parser
from getpass import getpass
from email.Utils import parsedate
try:
    import cStringIO as StringIO
except ImportError:
    import StringIO
from spambayes.Options import options, get_pathname_option
from spambayes import tokenizer, storage, message, Dibbler
from spambayes.UserInterface import UserInterfaceServer
from spambayes.ImapUI import IMAPUserInterface
from spambayes.Version import get_version_string
from imaplib import IMAP4
from imaplib import Time2Internaldate
try:
    if options["imap", "use_ssl"]:
        from imaplib import IMAP4_SSL as BaseIMAP
    else:
        from imaplib import IMAP4 as BaseIMAP
except ImportError:
    from imaplib import IMAP4 as BaseIMAP
class BadIMAPResponseError(Exception):
    """An IMAP command returned a non-"OK" response."""
    def __init__(self, command, response):
        self.command = command
        self.response = response
    def __str__(self):
        return "The command '%s' failed to give an OK response.\n%s" % \
               (self.command, self.response)
class IMAPSession(BaseIMAP):
    '''A class extending the IMAP4 class, with a few optimizations'''
    timeout = 60 # seconds
    def __init__(self, server, port, debug=0, do_expunge=False):
        if not hasattr(self, "ssl"):
            readline = self.readline
            self.readline = self.readline_timeout
        try:
            BaseIMAP.__init__(self, server, port)
        except (BaseIMAP.error, socket.gaierror, socket.error):
            print "Cannot connect to server %s on port %s" % (server, port)
            if not hasattr(self, "ssl"):
                print "If you are connecting to an SSL server, please " \
                      "ensure that you have the 'Use SSL' option enabled."
            self.connected = False
        else:
            self.connected = True
        if not hasattr(self, "ssl"):
            self.readline = readline
        self.debug = debug
        self.do_expunge = do_expunge
        self.logged_in = False
        self.current_folder = None
    def readline_timeout(self):
        """Read line from remote, possibly timing out."""
        st_time = time.time()
        self.sock.setblocking(False)
        buffer = []
        while True:
            if (time.time() - st_time) > self.timeout:
                if options["globals", "verbose"]:
                    print >> sys.stderr, "IMAP Timing out"
                break
            try:
                data = self.sock.recv(1)
            except socket.error, e:
                if e[0] == 10035:
                    continue
                raise
            if not data:
                break
            if data == '\n':
                break
            buffer.append(data)
        self.sock.setblocking(True)
        return "".join(buffer)
    def login(self, username, pwd):
        """Log in to the IMAP server, catching invalid username/password."""
        assert self.connected, "Must be connected before logging in."
        try:
            BaseIMAP.login(self, username, pwd)  # superclass login
        except BaseIMAP.error, e:
            msg = "There was an error logging in to the IMAP server." \
                  " The username (%s) and/or password may " \
                  "be incorrect." % (username,)
            print msg
            sys.exit()
        self.logged_in = True
    def logout(self):
        """Log off from the IMAP server, possibly expunging.
        Note that most, if not all, of the expunging is probably done in
        SelectFolder, rather than here, for purposes of speed."""
        if self.connected and self.logged_in and self.do_expunge:
            for fol in ["spam_folder",
                        "unsure_folder",
                        "ham_folder"]:
                self.select(options["imap", fol])
                self.expunge()
            for fol_list in ["ham_train_folders",
                             "spam_train_folders",]:
                for fol in options["imap", fol_list]:
                    self.select(fol)
                    self.expunge()
        BaseIMAP.logout(self)  # superclass logout
    def check_response(self, command, IMAP_response):
        """A utility function to check the response from IMAP commands.
        Raises BadIMAPResponseError if the response is not OK.  Returns
        the data segment of the response otherwise."""
        response, data = IMAP_response
        if response != "OK":
            raise BadIMAPResponseError(command, IMAP_response)
        return data
    def SelectFolder(self, folder):
        """A method to point ensuing IMAP operations at a target folder.
        This is essentially a wrapper around the IMAP select command, which
        ignores the command if the folder is already selected."""
        if self.current_folder != folder:
            if self.current_folder != None and self.do_expunge:
                self.close()
            if folder == "":
                raise BadIMAPResponseError("Cannot have empty string as " \
                                           "folder name in select", "")
            response = self.select(folder, None)
            data = self.check_response("select %s" % (folder,), response)
            self.current_folder = folder
            return data
    number_re = re.compile(r"{\d+}")
    folder_re = re.compile(r"\(([\w\\ ]*)\) ")
    def folder_list(self):
        """Return a alphabetical list of all folders available on the
        server."""
        response = self.list()
        try:
            all_folders = self.check_response("list", response)
        except BadIMAPResponseError:
            print "Could not retrieve folder list."
            return []
        folders = []
        for fol in all_folders:
            if isinstance(fol, types.TupleType):
                m = self.number_re.search(fol[0])
                if not m:
                    continue
                fol = '%s"%s"' % (fol[0][:m.start()], fol[1])
            m = self.folder_re.search(fol)
            if not m:
                continue
            name_attributes = fol[:m.end()-1]
            self.folder_delimiter = fol[m.end()+1:m.end()+2]
            if self.folder_delimiter == ',':
                print "WARNING: Your imap server uses a comma as the " \
                      "folder delimiter.  This may cause unpredictable " \
                      "errors."
            folders.append(fol[m.end()+4:].strip('"'))
        folders.sort()
        return folders
    FLAG_CHARS = ""
    for i in range(32, 127):
        if not chr(i) in ['(', ')', '{', ' ', '%', '*', '"', '\\']:
            FLAG_CHARS += chr(i)
    FLAG = r"\\?[" + re.escape(FLAG_CHARS) + r"]+"
    FLAGS_RE = re.compile(r"(FLAGS) (\((" + FLAG + r" )*(" + FLAG + r")\))")
    INTERNALDATE_RE = re.compile(r"(INTERNALDATE) (\"\d{1,2}\-[A-Za-z]{3,3}\-" +
                                 r"\d{2,4} \d{2,2}\:\d{2,2}\:\d{2,2} " +
                                 r"[\+\-]\d{4,4}\")")
    RFC822_RE = re.compile(r"(RFC822) (\{[\d]+\})")
    BODY_PEEK_RE = re.compile(r"(BODY\[\]) (\{[\d]+\})")
    RFC822_HEADER_RE = re.compile(r"(RFC822.HEADER) (\{[\d]+\})")
    UID_RE = re.compile(r"(UID) ([\d]+)")
    FETCH_RESPONSE_RE = re.compile(r"([0-9]+) \(([" + \
                                   re.escape(FLAG_CHARS) + r"\"\{\}\(\)\\ ]*)\)?")
    LITERAL_RE = re.compile(r"^\{[\d]+\}$")
    def _extract_fetch_data(self, response):
        """This does the real work of extracting the data, for each message
        number.
        """
        if isinstance(response, types.StringTypes):
            response = (response,)
        data = {}
        expected_literal = None
        for part in response:
            if part == ')':
                continue
            if expected_literal:
                key, expected_size = expected_literal
                data[key] = part
                expected_literal = None
                continue
            mo = self.FETCH_RESPONSE_RE.match(part)
            if mo:
                data["message_number"] = mo.group(1)
                rest = mo.group(2)
            else:
                raise BadIMAPResponseError("FETCH response", response)
            for r in [self.FLAGS_RE, self.INTERNALDATE_RE, self.RFC822_RE,
                      self.UID_RE, self.RFC822_HEADER_RE, self.BODY_PEEK_RE]:
                mo = r.search(rest)
                if mo is not None:
                    if self.LITERAL_RE.match(mo.group(2)):
                        expected_literal = (mo.group(1),
                                            int(mo.group(2)[1:-1]))
                    else:
                        data[mo.group(1)] = mo.group(2)
        return data
    def extract_fetch_data(self, response):
        """Extract data from the response given to an IMAP FETCH command.
        The data is put into a dictionary, which is returned, where the
        keys are the fetch items.
        """
        if isinstance(response, types.StringTypes):
            response = (response,)
        data = {}
        for msg in response:
            msg_data = self._extract_fetch_data(msg)
            if msg_data:
                num = msg_data["message_number"]
                if num in data:
                    data[num].update(msg_data)
                else:
                    data[num] = msg_data
        return data
class IMAPMessage(message.SBHeaderMessage):
    def __init__(self):
        message.Message.__init__(self)
        self.folder = None
        self.previous_folder = None
        self.rfc822_command = "(BODY.PEEK[])"
        self.rfc822_key = "BODY[]"
        self.got_substance = False
        self.invalid = False
        self.could_not_retrieve = False
        self.imap_server = None
    def extractTime(self):
        """When we create a new copy of a message, we need to specify
        a timestamp for the message, if we can't get the information
        from the IMAP server itself.  If the message has a valid date
        header we use that.  Otherwise, we use the current time."""
        message_date = self["Date"]
        if message_date is not None:
            parsed_date = parsedate(message_date)
            if parsed_date is not None:
                try:
                    return Time2Internaldate(time.mktime(parsed_date))
                except ValueError:
                    pass
                except OverflowError:
                    pass
        return Time2Internaldate(time.time())
    def get_full_message(self):
        """Retrieve the RFC822 message from the IMAP server and return a
        new IMAPMessage object that has the same details as this message,
        but also has the substance."""
        if self.got_substance:
            return self
        assert self.id, "Cannot get substance of message without an id"
        assert self.uid, "Cannot get substance of message without an UID"
        assert self.imap_server, "Cannot do anything without IMAP connection"
        try:
            self.imap_server.SelectFolder(self.folder.name)
        except BadIMAPResponseError:
            self.could_not_retrieve = True
            print >>sys.stderr, "Could not select folder %s for message " \
                  "%s (uid %s)" % (self.folder.name, self.id, self.uid)
            return self
        try:
            response = self.imap_server.uid("FETCH", self.uid,
                                            self.rfc822_command)
        except MemoryError:
            self.could_not_retrieve = True
            print >>sys.stderr, "MemoryError with message %s (uid %s)" % \
                  (self.id, self.uid)
            return self
        command = "uid fetch %s" % (self.uid,)
        response_data = self.imap_server.check_response(command, response)
        data = self.imap_server.extract_fetch_data(response_data)
        rfc822_data = None
        for msg_data in data.itervalues():
            if self.rfc822_key in msg_data:
                rfc822_data = msg_data[self.rfc822_key]
                break
        if rfc822_data is None:
            raise BadIMAPResponseError("FETCH response", response_data)
        try:
            new_msg = email.message_from_string(rfc822_data, IMAPMessage)
        except:
            self.invalid = True
            text, details = message.insert_exception_header(
                rfc822_data, self.id)
            self.invalid_content = text
            self.got_substance = True
            print >>sys.stderr, details
            return self            
        new_msg.folder = self.folder
        new_msg.previous_folder = self.previous_folder
        new_msg.rfc822_command = self.rfc822_command
        new_msg.rfc822_key = self.rfc822_key
        new_msg.imap_server = self.imap_server
        new_msg.uid = self.uid
        new_msg.setId(self.id)
        new_msg.got_substance = True
        if not new_msg.has_key(options["Headers", "mailid_header_name"]):
            new_msg[options["Headers", "mailid_header_name"]] = self.id
        if options["globals", "verbose"]:
            sys.stdout.write(chr(8) + "*")
        return new_msg
    def MoveTo(self, dest):
        '''Note that message should move to another folder.  No move is
        carried out until Save() is called, for efficiency.'''
        if self.previous_folder is None:
            self.previous_folder = self.folder
        self.folder = dest
    def as_string(self, unixfrom=False):
        if self.invalid:
            return self._force_CRLF(self.invalid_content)
        else:
            return message.SBHeaderMessage.as_string(self, unixfrom)
    recent_re = re.compile(r"\\Recent ?| ?\\Recent")
    def Save(self):
        """Save message to IMAP server.
        We can't actually update the message with IMAP, so what we do is
        create a new message and delete the old one."""
        assert self.folder is not None,\
               "Can't save a message that doesn't have a folder."
        assert self.id, "Can't save a message that doesn't have an id."
        assert self.imap_server, "Can't do anything without IMAP connection."
        response = self.imap_server.uid("FETCH", self.uid,
                                        "(FLAGS INTERNALDATE)")
        command = "fetch %s (flags internaldate)" % (self.uid,)
        response_data = self.imap_server.check_response(command, response)
        data = self.imap_server.extract_fetch_data(response_data)
        msg_time = self.extractTime()
        flags = None
        for msg_data in data.itervalues():
            if "INTERNALDATE" in msg_data:
                msg_time = msg_data["INTERNALDATE"]
            if "FLAGS" in msg_data:
                flags = msg_data["FLAGS"]
                flags = self.recent_re.sub("", flags)
        for flgs, tme in [(flags, msg_time),
                          (None, msg_time),
                          (flags, Time2Internaldate(time.time())),
                          (None, Time2Internaldate(time.time()))]:
            response = self.imap_server.append(self.folder.name, flgs, tme,
                                               self.as_string())
            try:
                self.imap_server.check_response("", response)
            except BadIMAPResponseError:
                pass
            else:
                break
        else:
            command = "append %s %s %s %s" % (self.folder.name, flgs, tme,
                                              self.as_string)
            raise BadIMAPResponseError(command)
        if self.previous_folder is None:
            self.imap_server.SelectFolder(self.folder.name)
        else:
            self.imap_server.SelectFolder(self.previous_folder.name)
            self.previous_folder = None
        response = self.imap_server.uid("STORE", self.uid, "+FLAGS.SILENT",
                                        "(\\Deleted \\Seen)")
        command = "set %s to be deleted and seen" % (self.uid,)
        self.imap_server.check_response(command, response)
        for i in xrange(100):
            response = self.imap_server.recent()
            data = self.imap_server.check_response("recent", response)
            if data[0] is not None:
                break
        else:
            raise BadIMAPResponseError("Cannot find saved message", "")
        self.imap_server.SelectFolder(self.folder.name)
        search_string = "(UNDELETED HEADER %s \"%s\")" % \
                        (options["Headers", "mailid_header_name"],
                         self.id.replace('\\',r'\\').replace('"',r'\"'))
        response = self.imap_server.uid("SEARCH", search_string)
        data = self.imap_server.check_response("search " + search_string,
                                               response)
        new_id = data[0]
        multiple_ids = new_id.split()
        for id_to_remove in multiple_ids[:-1]:
            response = self.imap_server.uid("STORE", id_to_remove,
                                            "+FLAGS.SILENT",
                                            "(\\Deleted \\Seen)")
            command = "silently delete and make seen %s" % (id_to_remove,)
            self.imap_server.check_response(command, response)
        if multiple_ids:
            new_id = multiple_ids[-1]
        else:
            response = self.imap_server.uid("SEARCH", "RECENT")
            data = self.imap_server.check_response("search recent",
                                                   response)
            new_id = data[0]
            if new_id.find(' ') > -1:
                ids = new_id.split(' ')
                new_id = ids[-1]
            if new_id == "":
                response = self.imap_server.uid("SEARCH", "ALL")
                data = self.imap_server.check_response("search all",
                                                       response)
                new_id = data[0]
                if new_id.find(' ') > -1:
                    ids = new_id.split(' ')
                    new_id = ids[-1]
        self.uid = new_id
class IMAPFolder(object):
    def __init__(self, folder_name, imap_server):
        self.name = folder_name
        self.imap_server = imap_server
        self.lastBaseMessageName = ''
        self.uniquifier = 2
    def __cmp__(self, obj):
        """Two folders are equal if their names are equal."""
        if obj is None:
            return False
        return cmp(self.name, obj.name)
    def __iter__(self):
        """Iterate through the messages in this IMAP folder."""
        for key in self.keys():
            yield self[key]
    def keys(self):
        '''Returns *uids* for all the messages in the folder not
        marked as deleted.'''
        self.imap_server.SelectFolder(self.name)
        response = self.imap_server.uid("SEARCH", "UNDELETED")
        data = self.imap_server.check_response("search undeleted", response)
        if data[0]:
            return data[0].split(' ')
        else:
            return []
    custom_header_id_re = re.compile(re.escape(\
        options["Headers", "mailid_header_name"]) + "\:\s*(\d+(?:\-\d)?)",
                                     re.IGNORECASE)
    message_id_re = re.compile("Message-ID\: ?\<([^\n\>]+)\>",
                               re.IGNORECASE)
    def __getitem__(self, key):
        """Return message matching the given *uid*.
        The messages returned have no substance (so this should be
        reasonably quick, even with large messages).  You need to call
        get_full_message() on the returned message to get the substance of
        the message from the server."""
        self.imap_server.SelectFolder(self.name)
        response = self.imap_server.uid("FETCH", key, "RFC822.HEADER")
        response_data = self.imap_server.check_response(\
            "fetch %s rfc822.header" % (key,), response)
        data = self.imap_server.extract_fetch_data(response_data)
        headers = None
        for msg_data in data.itervalues():
            if "RFC822.HEADER" in msg_data:
                headers = msg_data["RFC822.HEADER"]
                break
        if headers is None:
            raise BadIMAPResponseError("FETCH response", response_data)
        msg = IMAPMessage()
        msg.folder = self
        msg.uid = key
        msg.imap_server = self.imap_server
        for id_header_re in [self.custom_header_id_re, self.message_id_re]:
            mo = id_header_re.search(headers)
            if mo:
                msg.setId(mo.group(1))
                break
        else:
            msg.setId(self._generate_id())
            msg = msg.get_full_message()
            msg.Save()
        if options["globals", "verbose"]:
            sys.stdout.write(".")
        return msg
    def _generate_id(self):
        messageName = "%10.10d" % long(time.time())
        if messageName == self.lastBaseMessageName:
            messageName = "%s-%d" % (messageName, self.uniquifier)
            self.uniquifier += 1
        else:
            self.lastBaseMessageName = messageName
            self.uniquifier = 2
        return messageName
    def Train(self, classifier, isSpam):
        """Train folder as spam/ham."""
        num_trained = 0
        for msg in self:
            if msg.GetTrained() == (not isSpam):
                msg = msg.get_full_message()
                if msg.could_not_retrieve:
                    continue
                msg.delSBHeaders()
                classifier.unlearn(msg.tokenize(), not isSpam)
                msg.RememberTrained(None)
            if msg.GetTrained() is None:
                msg = msg.get_full_message()
                if msg.could_not_retrieve:
                    continue
                saved_headers = msg.currentSBHeaders()
                msg.delSBHeaders()
                classifier.learn(msg.tokenize(), isSpam)
                num_trained += 1
                msg.RememberTrained(isSpam)
                if isSpam:
                    move_opt_name = "move_trained_spam_to_folder"
                else:
                    move_opt_name = "move_trained_ham_to_folder"
                if options["imap", move_opt_name] != "":
                    for header, value in saved_headers.items():
                        msg[header] = value
                    msg.MoveTo(IMAPFolder(options["imap", move_opt_name],
                                           self.imap_server))
                    msg.Save()
        return num_trained
    def Filter(self, classifier, spamfolder, unsurefolder, hamfolder):
        count = {}
        count["ham"] = 0
        count["spam"] = 0
        count["unsure"] = 0
        for msg in self:
            if msg.GetClassification() is None:
                msg = msg.get_full_message()
                if msg.could_not_retrieve:
                    continue
                (prob, clues) = classifier.spamprob(msg.tokenize(),
                                                    evidence=True)
                msg.delSBHeaders()
                msg.addSBHeaders(prob, clues)
                cls = msg.GetClassification()
                if cls == options["Headers", "header_ham_string"]:
                    if hamfolder:
                        msg.MoveTo(hamfolder)
                    count["ham"] += 1
                elif cls == options["Headers", "header_spam_string"]:
                    msg.MoveTo(spamfolder)
                    count["spam"] += 1
                else:
                    msg.MoveTo(unsurefolder)
                    count["unsure"] += 1
                msg.Save()
        return count
class IMAPFilter(object):
    def __init__(self, classifier):
        self.spam_folder = None
        self.unsure_folder = None
        self.ham_folder = None
        self.classifier = classifier
        self.imap_server = None
    def Train(self):
        assert self.imap_server, "Cannot do anything without IMAP server."
        if options["globals", "verbose"]:
            t = time.time()
        total_trained = 0
        for is_spam, option_name in [(False, "ham_train_folders"),
                                     (True, "spam_train_folders")]:
            training_folders = options["imap", option_name]
            for fol in training_folders:
                try:
                    self.imap_server.SelectFolder(fol)
                except BadIMAPResponseError:
                    print "Skipping %s, as it cannot be selected." % (fol,)
                    continue
                if options['globals', 'verbose']:
                    print "   Training %s folder %s" % \
                          (["ham", "spam"][is_spam], fol)
                folder = IMAPFolder(fol, self.imap_server)
                num_trained = folder.Train(self.classifier, is_spam)
                total_trained += num_trained
                if options['globals', 'verbose']:
                    print "\n       %s trained." % (num_trained,)
        if total_trained:
            self.classifier.store()
        if options["globals", "verbose"]:
            print "Training took %.4f seconds, %s messages were trained." \
                  % (time.time() - t, total_trained)
    def Filter(self):
        assert self.imap_server, "Cannot do anything without IMAP server."
        if not self.spam_folder:
            self.spam_folder = IMAPFolder(options["imap", "spam_folder"],
                                          self.imap_server)
        if not self.unsure_folder:
            self.unsure_folder = IMAPFolder(options["imap",
                                                    "unsure_folder"],
                                            self.imap_server)
        ham_folder_name = options["imap", "ham_folder"]
        if ham_folder_name and not self.ham_folder:
            self.ham_folder = IMAPFolder(ham_folder_name, self.imap_server)
        if options["globals", "verbose"]:
            t = time.time()
        count = {}
        count["ham"] = 0
        count["spam"] = 0
        count["unsure"] = 0
        try:
            self.imap_server.SelectFolder(self.spam_folder.name)
        except BadIMAPResponseError:
            print "Cannot select spam folder.  Please check configuration."
            sys.exit(-1)
        try:
            self.imap_server.SelectFolder(self.unsure_folder.name)
        except BadIMAPResponseError:
            print "Cannot select spam folder.  Please check configuration."
            sys.exit(-1)
        if self.ham_folder:
            try:
                self.imap_server.SelectFolder(self.ham_folder.name)
            except BadIMAPResponseError:
                print "Cannot select ham folder.  Please check configuration."
                sys.exit(-1)
        for filter_folder in options["imap", "filter_folders"]:
            try:
                self.imap_server.SelectFolder(filter_folder)
            except BadIMAPResponseError:
                print "Cannot select %s, skipping." % (filter_folder,)
                continue
            folder = IMAPFolder(filter_folder, self.imap_server)
            subcount = folder.Filter(self.classifier, self.spam_folder,
                                     self.unsure_folder, self.ham_folder)
            for key in count.keys():
                count[key] += subcount.get(key, 0)
        if options["globals", "verbose"]:
            if count is not None:
                print "\nClassified %s ham, %s spam, and %s unsure." % \
                      (count["ham"], count["spam"], count["unsure"])
            print "Classifying took %.4f seconds." % (time.time() - t,)
def run():
    try:
        opts, args = getopt.getopt(sys.argv[1:], 'hbPtcvl:e:i:d:p:o:')
    except getopt.error, msg:
        print >>sys.stderr, str(msg) + '\n\n' + __doc__
        sys.exit()
    doTrain = False
    doClassify = False
    doExpunge = options["imap", "expunge"]
    imapDebug = 0
    sleepTime = 0
    promptForPass = False
    launchUI = False
    server = ""
    username = ""
    for opt, arg in opts:
        if opt == '-h':
            print >>sys.stderr, __doc__
            sys.exit()
        elif opt == "-b":
            launchUI = True
        elif opt == '-t':
            doTrain = True
        elif opt == '-P':
            promptForPass = True
        elif opt == '-c':
            doClassify = True
        elif opt == '-v':
            options["globals", "verbose"] = True
        elif opt == '-e':
            if arg == 'y':
                doExpunge = True
            else:
                doExpunge = False
        elif opt == '-i':
            imapDebug = int(arg)
        elif opt == '-l':
            sleepTime = int(arg) * 60
        elif opt == '-o':
            options.set_from_cmdline(arg, sys.stderr)
    bdbname, useDBM = storage.database_type(opts)
    print get_version_string("IMAP Filter")
    print "and engine %s.\n" % (get_version_string(),)
    if options["globals", "verbose"]:
        print "Loading database %s..." % (bdbname),
    classifier = storage.open_storage(bdbname, useDBM)
    if options["globals", "verbose"]:
        print "Done."
    if options["imap", "server"]:
        server = options["imap", "server"]
        if len(server) > 0:
            server = server[0]
        username = options["imap", "username"]
        if len(username) > 0:
            username = username[0]
        if not promptForPass:
            pwd = options["imap", "password"]
            if len(pwd) > 0:
                pwd = pwd[0]
    else:
        pwd = None
        if not launchUI:
            print "You need to specify both a server and a username."
            sys.exit()
    if promptForPass:
        pwd = getpass()
    if server.find(':') > -1:
        server, port = server.split(':', 1)
        port = int(port)
    else:
        if options["imap", "use_ssl"]:
            port = 993
        else:
            port = 143
    imap_filter = IMAPFilter(classifier)
    if sleepTime or not (doClassify or doTrain):
        if server == "":
            imap = None
        else:
            imap = IMAPSession(server, port, imapDebug, doExpunge)
        httpServer = UserInterfaceServer(options["html_ui", "port"])
        httpServer.register(IMAPUserInterface(classifier, imap, pwd,
                                              IMAPSession))
        launchBrowser=launchUI or options["html_ui", "launch_browser"]
        if sleepTime:
            thread.start_new_thread(Dibbler.run, (),
                                    {"launchBrowser":launchBrowser})
        else:
            Dibbler.run(launchBrowser=launchBrowser)
    if doClassify or doTrain:
        while True:
            imap = IMAPSession(server, port, imapDebug, doExpunge)
            if imap.connected:
                imap.login(username, pwd)
                imap_filter.imap_server = imap
                if doTrain:
                    if options["globals", "verbose"]:
                        print "Training"
                    imap_filter.Train()
                if doClassify:
                    if options["globals", "verbose"]:
                        print "Classifying"
                    imap_filter.Filter()
                imap.logout()
            else:
                pass
            if sleepTime:
                time.sleep(sleepTime)
            else:
                break
if __name__ == '__main__':
    run()
