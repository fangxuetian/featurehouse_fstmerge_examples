"""SOCKS unit tests."""
from twisted.trial import unittest
from twisted.test import proto_helpers
import struct, socket
from twisted.internet import defer, address
from twisted.protocols import socks
class StringTCPTransport(proto_helpers.StringTransport):
    stringTCPTransport_closing = False
    peer = None
    def getPeer(self):
        return self.peer
    def getHost(self):
        return address.IPv4Address('TCP', '2.3.4.5', 42)
    def loseConnection(self):
        self.stringTCPTransport_closing = True
class SOCKSv4Driver(socks.SOCKSv4):
    driver_outgoing = None
    driver_listen = None
    def connectClass(self, host, port, klass, *args):
        proto = klass(*args)
        proto.transport = StringTCPTransport()
        proto.transport.peer = address.IPv4Address('TCP', host, port)
        proto.connectionMade()
        self.driver_outgoing = proto
        return defer.succeed(proto)
    def listenClass(self, port, klass, *args):
        factory = klass(*args)
        self.driver_listen = factory
        if port == 0:
            port = 1234
        return defer.succeed(('6.7.8.9', port))
class Connect(unittest.TestCase):
    def setUp(self):
        self.sock = SOCKSv4Driver()
        self.sock.transport = StringTCPTransport()
        self.sock.connectionMade()
    def tearDown(self):
        outgoing = self.sock.driver_outgoing
        if outgoing is not None:
            self.assert_(outgoing.transport.stringTCPTransport_closing,
                         "Outgoing SOCKS connections need to be closed.")
    def test_simple(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 1, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.assertEqual(sent,
                         struct.pack('!BBH', 0, 90, 34)
                         + socket.inet_aton('1.2.3.4'))
        self.assert_(not self.sock.transport.stringTCPTransport_closing)
        self.assert_(self.sock.driver_outgoing is not None)
        self.sock.dataReceived('hello, world')
        self.assertEqual(self.sock.driver_outgoing.transport.value(),
                         'hello, world')
        self.sock.driver_outgoing.dataReceived('hi there')
        self.assertEqual(self.sock.transport.value(), 'hi there')
        self.sock.connectionLost('fake reason')
    def test_access_denied(self):
        self.sock.authorize = lambda code, server, port, user: 0
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 1, 4242)
            + socket.inet_aton('10.2.3.4')
            + 'fooBAR'
            + '\0')
        self.assertEqual(self.sock.transport.value(),
                         struct.pack('!BBH', 0, 91, 0)
                         + socket.inet_aton('0.0.0.0'))
        self.assert_(self.sock.transport.stringTCPTransport_closing)
        self.assertIdentical(self.sock.driver_outgoing, None)
    def test_eof_remote(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 1, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.sock.dataReceived('hello, world')
        self.assertEqual(self.sock.driver_outgoing.transport.value(),
                         'hello, world')
        self.sock.driver_outgoing.transport.loseConnection()
        self.sock.driver_outgoing.connectionLost('fake reason')
    def test_eof_local(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 1, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.sock.dataReceived('hello, world')
        self.assertEqual(self.sock.driver_outgoing.transport.value(),
                         'hello, world')
        self.sock.connectionLost('fake reason')
class Bind(unittest.TestCase):
    def setUp(self):
        self.sock = SOCKSv4Driver()
        self.sock.transport = StringTCPTransport()
        self.sock.connectionMade()
    def test_simple(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 2, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.assertEqual(sent,
                         struct.pack('!BBH', 0, 90, 1234)
                         + socket.inet_aton('6.7.8.9'))
        self.assert_(not self.sock.transport.stringTCPTransport_closing)
        self.assert_(self.sock.driver_listen is not None)
        incoming = self.sock.driver_listen.buildProtocol(('1.2.3.4', 5345))
        self.assertNotIdentical(incoming, None)
        incoming.transport = StringTCPTransport()
        incoming.connectionMade()
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.assertEqual(sent,
                         struct.pack('!BBH', 0, 90, 0)
                         + socket.inet_aton('0.0.0.0'))
        self.assert_(not self.sock.transport.stringTCPTransport_closing)
        self.sock.dataReceived('hello, world')
        self.assertEqual(incoming.transport.value(),
                         'hello, world')
        incoming.dataReceived('hi there')
        self.assertEqual(self.sock.transport.value(), 'hi there')
        self.sock.connectionLost('fake reason')
    def test_access_denied(self):
        self.sock.authorize = lambda code, server, port, user: 0
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 2, 4242)
            + socket.inet_aton('10.2.3.4')
            + 'fooBAR'
            + '\0')
        self.assertEqual(self.sock.transport.value(),
                         struct.pack('!BBH', 0, 91, 0)
                         + socket.inet_aton('0.0.0.0'))
        self.assert_(self.sock.transport.stringTCPTransport_closing)
        self.assertIdentical(self.sock.driver_listen, None)
    def test_eof_remote(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 2, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        incoming = self.sock.driver_listen.buildProtocol(('1.2.3.4', 5345))
        self.assertNotIdentical(incoming, None)
        incoming.transport = StringTCPTransport()
        incoming.connectionMade()
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.assertEqual(sent,
                         struct.pack('!BBH', 0, 90, 0)
                         + socket.inet_aton('0.0.0.0'))
        self.assert_(not self.sock.transport.stringTCPTransport_closing)
        self.sock.dataReceived('hello, world')
        self.assertEqual(incoming.transport.value(),
                         'hello, world')
        incoming.transport.loseConnection()
        incoming.connectionLost('fake reason')
    def test_eof_local(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 2, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        incoming = self.sock.driver_listen.buildProtocol(('1.2.3.4', 5345))
        self.assertNotIdentical(incoming, None)
        incoming.transport = StringTCPTransport()
        incoming.connectionMade()
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.assertEqual(sent,
                         struct.pack('!BBH', 0, 90, 0)
                         + socket.inet_aton('0.0.0.0'))
        self.assert_(not self.sock.transport.stringTCPTransport_closing)
        self.sock.dataReceived('hello, world')
        self.assertEqual(incoming.transport.value(),
                         'hello, world')
        self.sock.connectionLost('fake reason')
    def test_bad_source(self):
        self.sock.dataReceived(
            struct.pack('!BBH', 4, 2, 34)
            + socket.inet_aton('1.2.3.4')
            + 'fooBAR'
            + '\0')
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        incoming = self.sock.driver_listen.buildProtocol(('1.6.6.6', 666))
        self.assertIdentical(incoming, None)
        sent = self.sock.transport.value()
        self.sock.transport.clear()
        self.assertEqual(sent,
                         struct.pack('!BBH', 0, 91, 0)
                         + socket.inet_aton('0.0.0.0'))
        self.assert_(self.sock.transport.stringTCPTransport_closing)
