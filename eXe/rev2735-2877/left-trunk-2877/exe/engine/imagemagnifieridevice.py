"""
A ImageMagnifier Idevice is one built up from an image and free text.
"""
import logging
from exe.engine.idevice   import Idevice
from exe.engine.field     import TextAreaField, MagnifierField
from exe.engine.translate import lateTranslate
log = logging.getLogger(__name__)
class ImageMagnifierIdevice(Idevice):
    """
    A ImageMagnifier Idevice is one built up from an image and free text.
    """
    persistenceVersion = 2
    def __init__(self, defaultImage = None):
        Idevice.__init__(self, 
                         x_(u"Image Magnifier"), 
                         x_(u"University of Auckland"), 
                         x_(u"""The image magnifier is a magnifying tool enabling
 learners to magnify the view of the image they have been given. Moving the 
magnifying glass over the image allows larger detail to be studied."""), 
                         u"", u"")
        self.emphasis                    = Idevice.NoEmphasis
        self.imageMagnifier              = MagnifierField(
                                           x_(u"Choose an Image"), x_(u"""Click 
on the picture below or the "Add Image" button to select an image file to be 
magnified."""))
        self.imageMagnifier.idevice      = self
        self.imageMagnifier.defaultImage = defaultImage
        self.text                        = TextAreaField(x_(u"Text"),
                                           x_("""Enter the text you wish to 
associate with the file."""))
        self.text.idevice                = self
        self.float                       = u"left"
        self.caption                     = u""
        self._captionInstruc             = x_(u"""Provide a caption for the 
image to be magnified.""")
        self._dimensionInstruc           = x_(u"""Choose the size you want 
your image to display at. The measurements are in pixels. Generally, 100 
pixels equals approximately 3cm. Leave both fields blank if you want the 
image to display at its original size.""")
        self._alignInstruc               = x_(u"""Alignment allows you to 
choose where on the screen the image will be positioned.""")
        self._initialZoomInstruc         = x_(u"""Set the initial level of zoom 
when the IDevice loads, as a percentage of the original image size""")
        self._maxZoomInstruc             = x_(u"""Set the maximum level of zoom, 
as a percentage of the original image size""")
        self._glassSizeInstruc           = x_(u"""Select the size of the magnifying glass""")
        self.systemResources            += ['magnifier.swf']
    captionInstruc     = lateTranslate('captionInstruc')
    dimensionInstruc   = lateTranslate('dimensionInstruc')
    alignInstruc       = lateTranslate('alignInstruc')
    initialZoomInstruc = lateTranslate('initialZoomInstruc')
    maxZoomInstruc     = lateTranslate('maxZoomInstruc')
    glassSizeInstruc   = lateTranslate('glassSizeInstruc')
    def upgradeToVersion1(self):
        """
        Upgrades to v0.14
        """
        self._alignInstruc               = x_(u"""Alignment allows you to 
choose where on the screen the image will be positioned.""")
        self._initialZoomInstruc         = x_(u"""Set the initial level of zoom 
when the IDevice loads, as a percentage of the original image size""")
        self._maxZoomInstruc             = x_(u"""Set the maximum level of zoom, 
as a percentage of the original image size""")
        self._glassSizeInstruc           = x_(u"""This chooses the initial size 
of the magnifying glass""")
    def upgradeToVersion2(self):
        """
        Upgrades to v0.24
        """
        self.imageMagnifier.isDefaultImage = False
