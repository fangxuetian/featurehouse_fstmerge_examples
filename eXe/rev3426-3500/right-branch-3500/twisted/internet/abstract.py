"""Support for generic select()able objects.
API Stability: stable
Maintainer: U{Itamar Shtull-Trauring<mailto:twisted@itamarst.org>}
"""
import types, string
from zope.interface import implements
from twisted.python import log, reflect, components, failure
from twisted.persisted import styles
import interfaces, main
class FileDescriptor(log.Logger, styles.Ephemeral, object):
    """An object which can be operated on by select().
    This is an abstract superclass of all objects which may be notified when
    they are readable or writable; e.g. they have a file-descriptor that is
    valid to be passed to select(2).
    """
    connected = 0
    producerPaused = 0
    streamingProducer = 0
    producer = None
    disconnected = 0
    disconnecting = 0
    _writeDisconnecting = False
    _writeDisconnected = False
    dataBuffer = ""
    offset = 0
    SEND_LIMIT = 128*1024
    implements(interfaces.IProducer, interfaces.IReadWriteDescriptor,
               interfaces.IConsumer, interfaces.ITransport, interfaces.IHalfCloseableDescriptor)
    def __init__(self, reactor=None):
        if not reactor:
            from twisted.internet import reactor
        self.reactor = reactor
        self._tempDataBuffer = [] # will be added to dataBuffer in doWrite
        self._tempDataLen = 0
    def connectionLost(self, reason):
        """The connection was lost.
        This is called when the connection on a selectable object has been
        lost.  It will be called whether the connection was closed explicitly,
        an exception occurred in an event handler, or the other end of the
        connection closed it first.
        Clean up state here, but make sure to call back up to FileDescriptor.
        """
        self.disconnected = 1
        self.connected = 0
        if self.producer is not None:
            self.producer.stopProducing()
            self.producer = None
        self.stopReading()
        self.stopWriting()
    def writeSomeData(self, data):
        """Write as much as possible of the given data, immediately.
        This is called to invoke the lower-level writing functionality, such as
        a socket's send() method, or a file's write(); this method returns an
        integer.  If positive, it is the number of bytes written; if negative,
        it indicates the connection was lost.
        """
        raise NotImplementedError("%s does not implement writeSomeData" %
                                  reflect.qual(self.__class__))
    def doRead(self):
        raise NotImplementedError("%s does not implement doRead" %
                                  reflect.qual(self.__class__))
    def doWrite(self):
        """Called when data is available for writing.
        A result that is true (which will be a negative number) implies the
        connection was lost. A false result implies the connection is still
        there; a result of 0 implies no write was done, and a result of None
        indicates that a write was done.
        """
        if len(self.dataBuffer) - self.offset < self.SEND_LIMIT:
            self.dataBuffer = buffer(self.dataBuffer, self.offset) + "".join(self._tempDataBuffer)
            self.offset = 0
            self._tempDataBuffer = []
            self._tempDataLen = 0
        if self.offset:
            l = self.writeSomeData(buffer(self.dataBuffer, self.offset))
        else:
            l = self.writeSomeData(self.dataBuffer)
        if l < 0 or isinstance(l, Exception):
            return l
        if l == 0 and self.dataBuffer:
            result = 0
        else:
            result = None
        self.offset += l
        if self.offset == len(self.dataBuffer) and not self._tempDataLen:
            self.dataBuffer = ""
            self.offset = 0
            self.stopWriting()
            if self.producer is not None and ((not self.streamingProducer)
                                              or self.producerPaused):
                self.producerPaused = 0
                self.producer.resumeProducing()
            elif self.disconnecting:
                return self._postLoseConnection()
            elif self._writeDisconnecting:
                result = self._closeWriteConnection()
                self._writeDisconnected = True
                return result
        return result
    def _postLoseConnection(self):
        """Called after a loseConnection(), when all data has been written.
        Whatever this returns is then returned by doWrite.
        """
        return main.CONNECTION_DONE
    def _closeWriteConnection(self):
        pass
    def writeConnectionLost(self, reason):
        self.connectionLost(reason)
    def readConnectionLost(self, reason):
        self.connectionLost(reason)
    def write(self, data):
        """Reliably write some data.
        If there is no buffered data this tries to write this data immediately,
        otherwise this adds data to be written the next time this file descriptor is
        ready for writing.
        """
        if isinstance(data, unicode): # no, really, I mean it
            raise TypeError("Data must not be unicode")
        if not self.connected or self._writeDisconnected:
            return
        if data:
            self._tempDataBuffer.append(data)
            self._tempDataLen += len(data)
            if self.producer is not None:
                if len(self.dataBuffer) + self._tempDataLen > self.bufferSize:
                    self.producerPaused = 1
                    self.producer.pauseProducing()
            self.startWriting()
    def writeSequence(self, iovec):
        if not self.connected or not iovec or self._writeDisconnected:
            return
        self._tempDataBuffer.extend(iovec)
        for i in iovec:
            self._tempDataLen += len(i)
        if self.producer is not None:
            if len(self.dataBuffer) + self._tempDataLen > self.bufferSize:
                self.producerPaused = 1
                self.producer.pauseProducing()
        self.startWriting()
    def loseConnection(self, _connDone=failure.Failure(main.CONNECTION_DONE)):
        """Close the connection at the next available opportunity.
        Call this to cause this FileDescriptor to lose its connection.  It will
        first write any data that it has buffered.
        If there is data buffered yet to be written, this method will cause the
        transport to lose its connection as soon as it's done flushing its
        write buffer.  If you have a producer registered, the connection won't
        be closed until the producer is finished. Therefore, make sure you
        unregister your producer when it's finished, or the connection will
        never close.
        """
        if self.connected and not self.disconnecting:
            if self._writeDisconnected:
                self.stopReading()
                self.stopWriting()
                self.connectionLost(_connDone)
            else:
                self.stopReading()
                self.startWriting()
                self.disconnecting = 1
    def loseWriteConnection(self):
        self._writeDisconnecting = True
        self.startWriting()
    def stopReading(self):
        """Stop waiting for read availability.
        Call this to remove this selectable from being notified when it is
        ready for reading.
        """
        self.reactor.removeReader(self)
    def stopWriting(self):
        """Stop waiting for write availability.
        Call this to remove this selectable from being notified when it is ready
        for writing.
        """
        self.reactor.removeWriter(self)
    def startReading(self):
        """Start waiting for read availability.
        """
        self.reactor.addReader(self)
    def startWriting(self):
        """Start waiting for write availability.
        Call this to have this FileDescriptor be notified whenever it is ready for
        writing.
        """
        self.reactor.addWriter(self)
    producer = None
    bufferSize = 2**2**2**2
    def registerProducer(self, producer, streaming):
        """Register to receive data from a producer.
        This sets this selectable to be a consumer for a producer.  When this
        selectable runs out of data on a write() call, it will ask the producer
        to resumeProducing(). A producer should implement the IProducer
        interface.
        FileDescriptor provides some infrastructure for producer methods.
        """
        if self.producer is not None:
            raise RuntimeError("Cannot register producer %s, because producer %s was never unregistered." % (producer, self.producer))
        if self.disconnected:
            producer.stopProducing()
        else:
            self.producer = producer
            self.streamingProducer = streaming
            if not streaming:
                producer.resumeProducing()
    def unregisterProducer(self):
        """Stop consuming data from a producer, without disconnecting.
        """
        self.producer = None
    def stopConsuming(self):
        """Stop consuming data.
        This is called when a producer has lost its connection, to tell the
        consumer to go lose its connection (and break potential circular
        references).
        """
        self.unregisterProducer()
        self.loseConnection()
    def resumeProducing(self):
        assert self.connected and not self.disconnecting
        self.startReading()
    def pauseProducing(self):
        self.stopReading()
    def stopProducing(self):
        self.loseConnection()
    def fileno(self):
        """File Descriptor number for select().
        This method must be overridden or assigned in subclasses to
        indicate a valid file descriptor for the operating system.
        """
        return -1
components.backwardsCompatImplements(FileDescriptor)
def isIPAddress(addr):
    parts = string.split(addr, '.')
    if len(parts) == 4:
        try:
            for part in map(int, parts):
                if not (0<=part<256):
                    break
            else:
                return 1
        except ValueError:
                pass
    return 0
__all__ = ["FileDescriptor"]
