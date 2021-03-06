import sys
import time
import types
import socket
import thread
import imaplib
import unittest
import asyncore
import sb_test_support
sb_test_support.fix_sys_path()
from spambayes import Dibbler
from spambayes.Options import options
from spambayes.classifier import Classifier
from sb_imapfilter import BadIMAPResponseError
from spambayes.message import message_from_string
from sb_imapfilter import IMAPSession, IMAPMessage, IMAPFolder, IMAPFilter
IMAP_PORT = 8143
IMAP_USERNAME = "testu"
IMAP_PASSWORD = "testp"
IMAP_FOLDER_LIST = ["INBOX", "unsure", "ham_to_train", "spam",
                    "spam_to_train"]
SB_ID_1 = "test@spambayes.invalid"
SB_ID_2 = "14102004"
IMAP_MESSAGES = {
    101 : """Subject: Test\r
Message-ID: <%s>\r
\r
Body test.""" % (SB_ID_1,),
    102 : """Subject: Test2\r
Message-ID: <%s>\r
%s: %s\r
\r
Another body test.""" % (SB_ID_1, options["Headers", "mailid_header_name"],
                         SB_ID_2),
    103 : """Received: from noisy-2-82-67-182-141.fbx.proxad.net(82.67.182.141)
 via SMTP by mx1.example.com, id smtpdAAAzMayUR; Tue Apr 27 18:56:48 2004
Return-Path: " Freeman" <XLUPSYGSHLBAPN@runbox.com>
Received: from  rly-xn05.mx.aol.com (rly-xn05.mail.aol.com [172.20.83.138]) by air-xn02.mail.aol.com (v98.10) with ESMTP id MAILINXN22-6504043449c151; Tue, 27 Apr 2004 16:57:46 -0300
Received: from 132.16.224.107 by 82.67.182.141; Tue, 27 Apr 2004 14:54:46 -0500
From: " Gilliam" <.@doramail.com>
To: To: user@example.com
Subject: Your Source For Online Prescriptions....Soma-Watson..VALIUM-Roche    .		
Date: Wed, 28 Apr 2004 00:52:46 +0500
Mime-Version: 1.0
Content-Type: multipart/alternative;
        boundary=""
X-Mailer: AOL 7.0 for Windows US sub 118
X-AOL-IP: 114.204.176.98
X-AOL-SCOLL-SCORE: 1:XXX:XX
X-AOL-SCOLL-URL_COUNT: 2
Message-ID: <@XLUPSYGSHLBAPN@runbox.com>
--
Content-Type: text/html;
        charset="iso-8859-1"
Content-Transfer-Encoding: quoted-printable
<strong><a href=3D"http://www.ibshels454drugs.biz/c39/">ENTER HERE</a> to
ORDER MEDS Online, such as XANAX..VALIUM..SOMA..Much MORE SHIPPED
OVERNIGHT,to US and INTERNATIONAL</strong>
---
""",
    104 : """Subject: Test2\r
\r
Yet another body test.""",
    }
IMAP_UIDS = {1 : 101, 2: 102, 3:103, 4:104}
UNDELETED_IDS = (1,2)
class TestListener(Dibbler.Listener):
    """Listener for TestIMAP4Server."""
    def __init__(self, socketMap=asyncore.socket_map):
        Dibbler.Listener.__init__(self, IMAP_PORT, TestIMAP4Server,
                                  (socketMap,), socketMap=socketMap)
FAIL_NEXT = False
class TestIMAP4Server(Dibbler.BrighterAsyncChat):
    """Minimal IMAP4 server, for testing purposes.  Accepts a limited
    subset of commands, and also a KILL command, to terminate."""
    def __init__(self, clientSocket, socketMap):
        Dibbler.BrighterAsyncChat.__init__(self)
        Dibbler.BrighterAsyncChat.set_socket(self, clientSocket, socketMap)
        self.set_terminator('\r\n')
        self.okCommands = ['NOOP', 'LOGOUT', 'CAPABILITY', 'KILL']
        self.handlers = {'LIST' : self.onList,
                         'LOGIN' : self.onLogin,
                         'SELECT' : self.onSelect,
                         'FETCH' : self.onFetch,
                         'SEARCH' : self.onSearch,
                         'UID' : self.onUID,
                         'APPEND' : self.onAppend,
                         'STORE' : self.onStore,
                         }
        self.push("* OK [CAPABILITY IMAP4REV1 AUTH=LOGIN] " \
                  "localhost IMAP4rev1\r\n")
        self.request = ''
        self.next_id = 0
        self.in_literal = (0, None)
    def collect_incoming_data(self, data):
        """Asynchat override."""
        if self.in_literal[0] > 0:
            self.request = "%s\r\n%s" % (self.request, data)
        else:
            self.request = self.request + data
    def found_terminator(self):
        """Asynchat override."""
        global FAIL_NEXT
        if self.in_literal[0] > 0:
            if len(self.request) >= self.in_literal[0]:
                self.push(self.in_literal[1](self.request,
                                             *self.in_literal[2]))
                self.in_literal = (0, None)
                self.request = ''
            return
        id, command = self.request.split(None, 1)
        if FAIL_NEXT:
            FAIL_NEXT = False
            self.push("%s NO Was told to fail.\r\n" % (id,))
        if ' ' in command:
            command, args = command.split(None, 1)
        else:
            args = ''
        command = command.upper()
        if command in self.okCommands:
            self.push("%s OK (we hope)\r\n" % (id,))
            if command == 'LOGOUT':
                self.close_when_done()
            if command == 'KILL':
                self.socket.shutdown(2)
                self.close()
                raise SystemExit()
        else:
            handler = self.handlers.get(command, self.onUnknown)
            self.push(handler(id, command, args, False))  # Or push_slowly for testing
        self.request = ''
    def push_slowly(self, response):
        """Useful for testing."""
        for c in response:
            self.push(c)
            time.sleep(0.02)
    def onLogin(self, id, command, args, uid=False):
        """Log in to server."""
        username, password = args.split(None, 1)
        username = username.strip('"')
        password = password.strip('"')
        if username == IMAP_USERNAME and password == IMAP_PASSWORD:
            return "%s OK [CAPABILITY IMAP4REV1] User %s " \
                   "authenticated.\r\n" % (id, username)
        return "%s NO LOGIN failed\r\n" % (id,)
    def onList(self, id, command, args, uid=False):
        """Return list of folders."""
        base = '\r\n* LIST (\\NoInferiors \\UnMarked) "/" '
        return "%s%s\r\n%s OK LIST completed\r\n" % \
               (base[2:], base.join(IMAP_FOLDER_LIST), id)
    def onStore(self, id, command, args, uid=False):
        return "%s OK STORE completed\r\n" % (id,)
    def onSelect(self, id, command, args, uid=False):
        exists = "* %d EXISTS" % (len(IMAP_MESSAGES),)
        recent = "* 0 RECENT"
        uidv = "* OK [UIDVALIDITY 1091599302] UID validity status"
        next_uid = "* OK [UIDNEXT 23] Predicted next UID"
        flags = "* FLAGS (\Answered \Flagged \Deleted \Draft \Seen)"
        perm_flags = "* OK [PERMANENTFLAGS (\* \Answered \Flagged " \
                     "\Deleted \Draft \Seen)] Permanent flags"
        complete = "%s OK [READ-WRITE] SELECT completed" % (id,)
        return "%s\r\n" % ("\r\n".join([exists, recent, uidv, next_uid,
                                        flags, perm_flags, complete]),)
    def onAppend(self, id, command, args, uid=False):
        folder, args = args.split(None, 1)
        if ')' in args:
            flags, args = args.split(')', 1)
            flags = flags[1:]
        unused, date, args = args.split('"', 2)
        if '{' in args:
            size = int(args[2:-1])
            self.in_literal = (size, self.appendLiteral, (id,))
            return "+ Ready for argument\r\n"
        return self.appendLiteral(args[1:], id)
    def appendLiteral(self, message, command_id):
        while True:
            id = self.next_id
            self.next_id += 1
            if id not in IMAP_MESSAGES:
                break
        IMAP_MESSAGES[id] = message
        return "* APPEND %s\r\n%s OK APPEND succeeded\r\n" % \
               (id, command_id)
    def onSearch(self, id, command, args, uid=False):
        args = args.upper()
        results = ()
        if args.find("UNDELETED") != -1:
            for msg_id in UNDELETED_IDS:
                if uid:
                    results += (IMAP_UIDS[msg_id],)
                else:
                    results += (msg_id,)
        if uid:
            command_string = "UID " + command
        else:
            command_string = command
        return "%s\r\n%s OK %s completed\r\n" % \
               ("* SEARCH " + ' '.join([str(r) for r in results]), id,
                command_string)
    def onFetch(self, id, command, args, uid=False):
        msg_nums, msg_parts = args.split(None, 1)
        msg_nums = msg_nums.split()
        response = {}
        for msg in msg_nums:
            response[msg] = []
        if msg_parts.find("UID") != -1:
            if uid:
                for msg in msg_nums:
                    response[msg].append("FETCH (UID %s)" % (msg,))
            else:
                for msg in msg_nums:
                    response[msg].append("FETCH (UID %s)" %
                                         (IMAP_UIDS[int(msg)]))
        if msg_parts.find("BODY.PEEK[]") != -1:
            for msg in msg_nums:
                if uid:
                    msg_uid = int(msg)
                else:
                    msg_uid = IMAP_UIDS[int(msg)]
                response[msg].append(("FETCH (BODY[] {%s}" %
                                     (len(IMAP_MESSAGES[msg_uid])),
                                     IMAP_MESSAGES[msg_uid]))
        if msg_parts.find("RFC822.HEADER") != -1:
            for msg in msg_nums:
                if uid:
                    msg_uid = int(msg)
                else:
                    msg_uid = IMAP_UIDS[int(msg)]
                msg_text = IMAP_MESSAGES[msg_uid]
                headers, unused = msg_text.split('\r\n\r\n', 1)
                response[msg].append(("FETCH (RFC822.HEADER {%s}" %
                                      (len(headers),), headers))
        if msg_parts.find("FLAGS INTERNALDATE") != -1:
            for msg in msg_nums:
                response[msg].append('FETCH (FLAGS (\Seen \Deleted) '
                                     'INTERNALDATE "27-Jul-2004 13:1'
                                     '1:56 +1200')
        for msg in msg_nums:
            try:
                simple = " ".join(response[msg])
            except TypeError:
                simple = []
                for part in response[msg]:
                    if isinstance(part, types.StringTypes):
                        simple.append(part)
                    else:
                        simple.append('%s\r\n%s)' % (part[0], part[1]))
                simple = " ".join(simple)
            response[msg] = "* %s %s" % (msg, simple)
        response_text = "\r\n".join(response.values())
        return "%s\r\n%s OK FETCH completed\r\n" % (response_text, id)
    def onUID(self, id, command, args, uid=False):
        actual_command, args = args.split(None, 1)
        handler = self.handlers.get(actual_command, self.onUnknown)
        return handler(id, actual_command, args, uid=True)
    def onUnknown(self, id, command, args, uid=False):
        """Unknown IMAP4 command."""
        return "%s BAD Command unrecognised: %s\r\n" % (id, repr(command))
class BaseIMAPFilterTest(unittest.TestCase):
    def setUp(self):
        self.imap = IMAPSession("localhost", IMAP_PORT)
    def tearDown(self):
        try:
            self.imap.logout()
        except imaplib.error:
            pass
class IMAPSessionTest(BaseIMAPFilterTest):
    def testConnection(self):
        self.assert_(self.imap.connected)
    def testGoodLogin(self):
        self.imap.login(IMAP_USERNAME, IMAP_PASSWORD)
        self.assert_(self.imap.logged_in)
    def testBadLogin(self):
        print "\nYou should see a message indicating that login failed."
        self.assertRaises(SystemExit, self.imap.login, IMAP_USERNAME,
                          "wrong password")
    def test_check_response(self):
        test_data = "IMAP response data"
        response = ("OK", test_data)
        data = self.imap.check_response("", response)
        self.assertEqual(data, test_data)
        response = ("NO", test_data)
        self.assertRaises(BadIMAPResponseError, self.imap.check_response,
                          "", response)
    def testSelectFolder(self):
        self.imap.login(IMAP_USERNAME, IMAP_PASSWORD)
        self.assertRaises(BadIMAPResponseError, self.imap.SelectFolder, "")
        self.imap.SelectFolder("Inbox")
        response = self.imap.response('OK')
        self.assertEquals(response[0], "OK")
        self.assert_(response[1] != [None])
        self.imap.SelectFolder("Inbox")
        response = self.imap.response('OK')
        self.assertEquals(response[0], "OK")
        self.assertEquals(response[1], [None])
    def test_folder_list(self):
        global FAIL_NEXT
        self.imap.login(IMAP_USERNAME, IMAP_PASSWORD)
        folders = self.imap.folder_list()
        correct = IMAP_FOLDER_LIST[:]
        correct.sort()
        self.assertEqual(folders, correct)
        print "\nYou should see a message indicating that getting the " \
              "folder list failed."
        FAIL_NEXT = True
        self.assertEqual(self.imap.folder_list(), [])
    def test_extract_fetch_data(self):
        response = "bad response"
        self.assertRaises(BadIMAPResponseError,
                          self.imap.extract_fetch_data, response)
        message_number = "123"
        uid = "5432"
        response = "%s (UID %s)" % (message_number, uid)
        data = self.imap.extract_fetch_data(response)
        self.assertEqual(data["message_number"], message_number)
        self.assertEqual(data["UID"], uid)
        flags = r"(\Seen \Deleted)"
        date = '"27-Jul-2004 13:11:56 +1200"'
        response = "%s (FLAGS %s INTERNALDATE %s)" % \
                   (message_number, flags, date)
        data = self.imap.extract_fetch_data(response)
        self.assertEqual(data["FLAGS"], flags)
        self.assertEqual(data["INTERNALDATE"], date)
        rfc = "Subject: Test\r\n\r\nThis is a test message."
        response = ("%s (RFC822 {%s}" % (message_number, len(rfc)), rfc)
        data = self.imap.extract_fetch_data(response)
        self.assertEqual(data["message_number"], message_number)
        self.assertEqual(data["RFC822"], rfc)
        headers = "Subject: Foo\r\nX-SpamBayes-ID: 1231-1\r\n"
        response = ("%s (RFC822.HEADER {%s}" % (message_number,
                                                len(headers)), headers)
        data = self.imap.extract_fetch_data(response)
        self.assertEqual(data["RFC822.HEADER"], headers)
        peek = "Subject: Test2\r\n\r\nThis is another test message."
        response = ("%s (BODY[] {%s}" % (message_number, len(peek)),
                    peek)
        data = self.imap.extract_fetch_data(response)
        self.assertEqual(data["BODY[]"], peek)
class IMAPMessageTest(BaseIMAPFilterTest):
    def setUp(self):
        BaseIMAPFilterTest.setUp(self)
        self.msg = IMAPMessage()
        self.msg.imap_server = self.imap
    def test_extract_time_no_date(self):
        date = self.msg.extractTime()
        self.assertEqual(date, imaplib.Time2Internaldate(time.time()))
    def test_extract_time_date(self):
        self.msg["Date"] = "Wed, 19 May 2004 20:05:15 +1200"
        date = self.msg.extractTime()
        self.assertEqual(date, '"19-May-2004 20:05:15 +1200"')
    def test_extract_time_bad_date(self):
        self.msg["Date"] = "Mon, 06 May 0102 10:51:16 -0100"
        date = self.msg.extractTime()
        self.assertEqual(date, imaplib.Time2Internaldate(time.time()))
    def test_as_string_invalid(self):
        content = "This is example content.\nThis is more\r\n"
        self.msg.invalid = True
        self.msg.invalid_content = content
        as_string = self.msg.as_string()
        self.assertEqual(self.msg._force_CRLF(content), as_string)
    def testMoveTo(self):
        fol1 = "Folder1"
        fol2 = "Folder2"
        self.msg.MoveTo(fol1)
        self.assertEqual(self.msg.folder, fol1)
        self.msg.MoveTo(fol2)
        self.assertEqual(self.msg.previous_folder, fol1)
        self.assertEqual(self.msg.folder, fol2)
    def test_get_full_message(self):
        self.assertRaises(AssertionError, self.msg.get_full_message)
        self.msg.id = "unittest"
        self.assertRaises(AttributeError, self.msg.get_full_message)
        self.msg.imap_server.login(IMAP_USERNAME, IMAP_PASSWORD)
        self.msg.imap_server.select()
        response = self.msg.imap_server.fetch(1, "UID")
        self.assertEqual(response[0], "OK")
        self.msg.uid = response[1][0][7:-1]
        self.msg.folder = IMAPFolder("Inbox", self.msg.imap_server)
        new_msg = self.msg.get_full_message()
        self.assertEqual(new_msg.folder, self.msg.folder)
        self.assertEqual(new_msg.previous_folder, self.msg.previous_folder)
        self.assertEqual(new_msg.uid, self.msg.uid)
        self.assertEqual(new_msg.id, self.msg.id)
        self.assertEqual(new_msg.rfc822_key, self.msg.rfc822_key)
        self.assertEqual(new_msg.rfc822_command, self.msg.rfc822_command)
        self.assertEqual(new_msg.imap_server, self.msg.imap_server)
        id_header = options["Headers", "mailid_header_name"]
        self.assertEqual(new_msg[id_header], self.msg.id)
        new_msg2 = new_msg.get_full_message()
        self.assert_(new_msg is new_msg2)
    def test_get_bad_message(self):
        self.msg.id = "unittest"
        self.msg.imap_server.login(IMAP_USERNAME, IMAP_PASSWORD)
        self.msg.imap_server.select()
        self.msg.uid = 103 # id of malformed message in dummy server
        self.msg.folder = IMAPFolder("Inbox", self.msg.imap_server)
        print "\nWith email package versions less than 3.0, you should " \
              "see an error parsing the message."
        new_msg = self.msg.get_full_message()
        has_header = new_msg.as_string().find("X-Spambayes-Exception: ") != -1
        has_defect = hasattr(new_msg, "defects") and len(new_msg.defects) > 0
        self.assert_(has_header or has_defect)
    def test_get_memory_error_message(self):
        pass
    def test_Save(self):
        pass
class IMAPFolderTest(BaseIMAPFilterTest):
    def setUp(self):
        BaseIMAPFilterTest.setUp(self)
        self.imap.login(IMAP_USERNAME, IMAP_PASSWORD)
        self.folder = IMAPFolder("testfolder", self.imap)
    def test_cmp(self):
        folder2 = IMAPFolder("testfolder", self.imap)
        folder3 = IMAPFolder("testfolder2", self.imap)
        self.assertEqual(self.folder, folder2)
        self.assertNotEqual(self.folder, folder3)
    def test_iter(self):
        keys = self.folder.keys()
        for msg in self.folder:
            msg = msg.get_full_message()
            msg_correct = message_from_string(IMAP_MESSAGES[int(keys[0])])
            id_header_name = options["Headers", "mailid_header_name"]
            if msg_correct[id_header_name] is None:
                msg_correct[id_header_name] = msg.id
            self.assertEqual(msg.as_string(), msg_correct.as_string())
            keys = keys[1:]
    def test_keys(self):
        keys = self.folder.keys()
        correct_keys = [str(IMAP_UIDS[id]) for id in UNDELETED_IDS]
        self.assertEqual(keys, correct_keys)
    def test_getitem_new_style(self):
        id_header_name = options["Headers", "mailid_header_name"]
        msg1 = self.folder[101]
        self.assertEqual(msg1.id, SB_ID_1)
        msg1 = msg1.get_full_message()
        msg1_correct = message_from_string(IMAP_MESSAGES[101])
        self.assertNotEqual(msg1[id_header_name], None)
        msg1_correct[id_header_name] = SB_ID_1
        self.assertEqual(msg1.as_string(), msg1_correct.as_string())
    def test_getitem_old_style(self):
        id_header_name = options["Headers", "mailid_header_name"]
        msg2 = self.folder[102]
        self.assertEqual(msg2.id, SB_ID_2)
        msg2 = msg2.get_full_message()
        self.assertNotEqual(msg2[id_header_name], None)
        self.assertEqual(msg2.as_string(), IMAP_MESSAGES[102])
    def test_getitem_new_id(self):
        id_header_name = options["Headers", "mailid_header_name"]
        msg3 = self.folder[104]
        self.assertNotEqual(msg3[id_header_name], None)
        msg_correct = message_from_string(IMAP_MESSAGES[104])
        msg_correct[id_header_name] = msg3.id
        self.assertEqual(msg3.as_string(), msg_correct.as_string())
    def test_generate_id(self):
        print "\nThis test takes slightly over a second."
        id1 = self.folder._generate_id()
        id2 = self.folder._generate_id()
        id3 = self.folder._generate_id()
        time.sleep(1)
        id4 = self.folder._generate_id()
        self.assertEqual(id2, id1 + "-2")
        self.assertEqual(id3, id1 + "-3")
        self.assertNotEqual(id1, id4)
        self.assertNotEqual(id2, id4)
        self.assertNotEqual(id3, id4)
        self.assert_('-' not in id4)
    def test_Train(self):
        pass
    def test_Filter(self):
        pass
class IMAPFilterTest(BaseIMAPFilterTest):
    def setUp(self):
        BaseIMAPFilterTest.setUp(self)
        self.imap.login(IMAP_USERNAME, IMAP_PASSWORD)
        classifier = Classifier()
        self.filter = IMAPFilter(classifier)
        options["imap", "ham_train_folders"] = ("ham_to_train",)
        options["imap", "spam_train_folders"] = ("spam_to_train",)
    def test_Train(self):
        pass
    def test_Filter(self):
        pass
def suite():
    suite = unittest.TestSuite()
    for cls in (IMAPSessionTest,
                IMAPMessageTest,
                IMAPFolderTest,
                IMAPFilterTest,
               ):
        suite.addTest(unittest.makeSuite(cls))
    return suite
if __name__=='__main__':
    def runTestServer():
        TestListener()
        asyncore.loop()
    thread.start_new_thread(runTestServer, ())
    sb_test_support.unittest_main(argv=sys.argv + ['suite'])
