"""
A ImageMagnifier Idevice is one built up from an image and free text.
"""
import logging
from exe.engine.idevice   import Idevice
from exe.engine.field     import TextAreaField, MultimediaField
from exe.engine.translate import lateTranslate
log = logging.getLogger(__name__)
class MultimediaIdevice(Idevice):
    """
    A Multimedia Idevice is one built up from an Multimedia file and free text.
    """
    persistenceVersion = 2
    def __init__(self, defaultMedia = None):
        Idevice.__init__(self, 
                         x_(u"MP3"), 
                         x_(u"Auckland University of Technology"), 
                         x_(u"The MP3 iDevice allows you to attach an MP3 " 
                            "media file to your content along with relevant textual"
                            "learning instructions."),
                         u"", 
                         u"")
        self.emphasis                    = Idevice.NoEmphasis
        self.media                       = MultimediaField(
                                           x_(u"Choose an MP3 file"),
                                           x_(u""
            "<ol>"
            "  <li>Click &lt;Select an MP3&gt; and browse to the MP3 "
            "      file you wish to insert</li>"
            " <li>Click on the dropdown menu to select the position "
            "       that you want the file displayed on screen.</li>"
            "  <li>Enter an optional caption for your file.</li>"
            " <li>Associate any relevant text to the MP3 file.</li>"
            " <li>Choose the type of style you would like the iDevice to"
            "       display e.g. 'Some emphasis' "
            "applies a border and icon to the iDevice content displayed.</li>"
            "</ol>"
            ))
        self.media.idevice               = self
        self.text                        = TextAreaField(x_(u"Text"),
                                           x_("""Enter the text you wish to 
associate with the file."""))
        self.text.idevice                = self
        self.float                       = u"left"
        self.caption                     = u""
        self.icon                        = u"multimedia"
        self._captionInstruc             = x_(u"""Provide a caption for the 
MP3 file. This will appear in the players title bar as well.""")
        self._alignInstruc               = x_(u"""Alignment allows you to 
choose where on the screen the media player will be positioned.""")
        self.systemResources += ['xspf_player.swf']
    captionInstruc     = lateTranslate('captionInstruc')
    alignInstruc       = lateTranslate('alignInstruc')
    def upgradeToVersion2(self):
        """
        (We skipped version 1 by accident)
        Upgrades to 0.22
        """
        self.systemResources += ['xspf_player.swf']
