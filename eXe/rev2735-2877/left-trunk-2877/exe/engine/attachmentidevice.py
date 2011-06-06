"""
An Attachment Idevice allows a file to be attached to a package.
"""
from exe.engine.idevice   import Idevice
from exe.engine.path      import Path
from exe.engine.translate import lateTranslate
from exe.engine.resource  import Resource
import logging
log = logging.getLogger(__name__)
class AttachmentIdevice(Idevice):
    """
    An Attachment Idevice allows a file to be attached to a package.
    """
    persistenceVersion = 3
    def __init__(self):
        Idevice.__init__(self, 
                         x_(u"Attachment"), 
                         x_(u"University of Auckland"), 
                         x_(u"The attachment iDevice is used to attach "
                             "existing files to your .elp content. For example, "
                             "you might have a PDF file or a PPT presentation "
                             "file that you wish the learners to have access "
                             "to, these can be attached and labeled to indicate "
                             "what the attachment is and how large the file is. "
                             "Learners can click on the attachment link and can "
                             "download the attachment."), u"", u"")
        self.emphasis           = Idevice.NoEmphasis
        self.label              = u''
        self.description        = u''
        self._filenameInstruc   = x_(u'Click <strong>Select a file</strong>, '
                                    'browse to the file you want '
                                    'to attach and select it.')
        self._labelInstruc      = x_(u"<p>"
                                    "Assign a label for the attachment. It "
                                    "is useful to include the type of file. "
                                    "Eg. pdf, ppt, etc."
                                    "</p>"
                                    "<p>"
                                    "Including the size is also recommended so "
                                    "that after your package is exported "
                                    "to a web site, people will have an idea "
                                    "how long it would take to download this "
                                    "attachment."
                                    "</p>"
                                    "<p>"
                                    "For example: "
                                    "<code>Sales Forecast.doc (500kb)</code>"
                                    "</p>")
        self._descriptionInstruc = x_(u"Enter the text you wish to associate "
                                      u"with the downloaded file. You might "
                                      u"want to provide instructions on what "
                                      u"you require the learner to do once "
                                      u"the file is downloaded or how the "
                                      u"material should be used.")
    filenameInstruc = lateTranslate('filenameInstruc')
    labelInstruc = lateTranslate('labelInstruc')
    descriptionInstruc = lateTranslate('descriptionInstruc')
    def setAttachment(self, attachmentPath):
        """
        Store the attachment in the package
        Needs to be in a package to work.
        """ 
        log.debug(u"setAttachment "+unicode(attachmentPath))
        resourceFile = Path(attachmentPath)
        assert(self.parentNode,
               _('Attachment %s has no parentNode') % self.id)
        assert(self.parentNode.package,
               _('iDevice %s has no package') % self.parentNode.id)
        if resourceFile.isfile():
            if self.userResources:
                while self.userResources:
                    self.userResources[0].delete()
            Resource(self, resourceFile)
        else:
            log.error('File %s is not a file' % resourceFile)
    def upgradeToVersion1(self):
        """
        Upgrades v0.6 to v0.7.
        """
        self.lastIdevice = False
    def upgradeToVersion2(self):
        """
        Upgrades to v0.10
        """
        self._upgradeIdeviceToVersion1()
        self._filenameInstruc    = self.__dict__.get('filenameInstruc', '')
        self._labelInstruc       = self.__dict__.get('labelInstruc', '')
        self._descriptionInstruc = self.__dict__.get('descriptionInstruc', '')
    def upgradeToVersion3(self):
        """
        Upgrades to v0.12
        """
        self._upgradeIdeviceToVersion2()
        if self.filename and self.parentNode:
            Resource(self, Path(self.filename))
        del self.filename
