"""
Functions for handling persistance in eXe
"""
import logging
import cStringIO
from twisted.persisted.styles import Versioned, doUpgrade
from twisted.spread  import jelly
from twisted.spread  import banana
log = logging.getLogger(__name__)
try:
    from twisted.spread import cBanana
    banana.cBanana = cBanana
    Banana = banana.Canana
    log.info('Using cBanana')
except ImportError:
    Banana = banana.Banana
    log.info('Using pyBanana')
class Persistable(object, jelly.Jellyable, jelly.Unjellyable, Versioned):
    """
    Base class for persistent classes
    """
    nonpersistant = []
    def getStateFor(self, jellier):
        """
        Call Versioned.__getstate__ to store
        persistenceVersion etc...
        """
        return self.__getstate__()
    def __getstate__(self):
        """
        Return which variables we should persist
        """
        toPersist = dict([(key, value) for key, value in self.__dict__.items()
                          if key not in self.nonpersistant])
        return Versioned.__getstate__(self, toPersist)
    def afterUpgrade(self):
        """
        Called after all items bieng loaded have been loaded
        and upgraded.
        """
def encodeObject(toEncode):
    """
    Take a object and turn it into an string
    """
    log.debug(u"encodeObject")
    encoder = Banana()
    encoder.connectionMade()
    encoder._selectDialect(u"none")
    strBuffer = cStringIO.StringIO()
    encoder.transport = strBuffer
    encoder.sendEncoded(jelly.jelly(toEncode))
    return strBuffer.getvalue()
def decodeToList(toDecode):
    """
    Decodes an object to a list of jelly strings, but doesn't unjelly them
    """
    log.debug(u"decodeObjectRaw starting decodeToList")
    decoder = Banana()
    decoder.connectionMade()
    decoder._selectDialect(u"none")
    jellyData = []
    decoder.expressionReceived = jellyData.append
    decoder.dataReceived(toDecode)
    log.debug(u"decodeObjectRaw ending decodeToList")
    return jellyData
def fixDataForMovedObjects(jellyData):
    """
    Fixes the jelly data so that IDevices that have moved can still be loaded.
    Removes 'exe.engine' from the module path, so that you can add their 'dir'
    to 'sys.path' and then jelly will just load them
    """
    for i, element in enumerate(jellyData):
        if isinstance(element, list):
            fixDataForMovedObjects(element)
        elif isinstance(element, str):
            if element in ('flashmovieidevice.FlashMovieIdevice',
                           'quiztestidevice.QuizTestIdevice',
                           'quiztestidevice.TestQuestion',
                           'quiztestidevice.AnswerOption',
                           'appletidevice.AppletIdevice'):
                mod, cls = element.split('.')
                jellyData[i] = 'exe.engine.%s.%s' % (mod, cls) 
def decodeObjectRaw(toDecode):
    """
    Decodes the object the same as decodeObject but doesn't upgrade it.
    """
    jellyData = decodeToList(toDecode)
    fixDataForMovedObjects(jellyData)
    decoded = jelly.unjelly(jellyData[0])
    return decoded
def decodeObject(toDecode):
    """
    Take a string and turn it into an object
    """
    decoded = decodeObjectRaw(toDecode)
    doUpgrade()
    return decoded
