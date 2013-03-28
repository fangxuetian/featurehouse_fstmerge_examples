"""
Gallery Idevice. Enables you to easily manage a bunch of images and thumbnails
"""
import Image, ImageDraw
from twisted.persisted.styles import requireUpgrade
from copy import copy, deepcopy
import logging
from exe.engine.idevice   import Idevice
from exe.engine.field     import TextField
from exe.engine.path      import Path, TempDirPath, toUnicode
from exe.engine.persist   import Persistable
from nevow                import tags as T
from nevow.stan           import raw
from nevow.flat           import flatten
from exe.engine.resource  import Resource
from exe.engine.translate import lateTranslate
log = logging.getLogger(__name__)
class _ShowsResources(Persistable):
    """
    Base class for gallery and gallery image.
    In Preview mode, resourcesUrl = 'resources/' but in export mode, resourcesUrl = None.
    Doing it this way, we don't need seperate functions for preview and view/export.
    The ideal solution would be to not have the URL to resources change ever.
    """
    resourcesUrl = 'resources/'
    @staticmethod
    def export():
        """
        Turns on export mode.
        """
        _ShowsResources.resourcesUrl = ''
    @staticmethod
    def preview():
        """
        Turns on preview mode.
        """
        _ShowsResources.resourcesUrl = 'resources/'
class GalleryImage(_ShowsResources):
    """
    Holds a gallery image and its caption. Can produce a thumbnail
    and a preview, popup window.
    """
    persistenceVersion = 2
    _parent       = None
    _caption       = None
    _id           = None
    thumbnailSize = (128, 128)
    size          = thumbnailSize
    bgColour      = 0x808080
    def __init__(self, parent, caption, originalImagePath):
        """
        'parent' is a GalleryIdevice instance
        'caption' is some text that will be displayed with the image
        'originalImagePath' is the local path to the image
        """
        self.parent             = parent
        self._caption            = TextField(caption)
        self._imageResource     = None
        self._thumbnailResource = None
        self._saveFiles(originalImagePath)
    def _saveFiles(self, originalImagePath=None):
        """
        Copies the image file and saves the thumbnail file
        'originalImagePath' is a Path instance
        setting 'originalImagePath' to None, will just recreate the
        thumbnail resources from the existing image resource.
        """
        package = self.parent.parentNode.package
        if originalImagePath is not None:
            originalImagePath = Path(originalImagePath)
            self._imageResource = Resource(self.parent, originalImagePath)
        try:
            image = Image.open(toUnicode(self._imageResource.path))
        except Exception, e:
            log.error("Couldn't load image: %s\nBecause: %s" % (self._imageResource.path, str(e)))
            image = Image.new('RGBA', self.thumbnailSize, (0xFF, 0, 0, 0))
            self._msgImage(image, _(u"No Thumbnail Available. Could not load original image."))
        self.size = image.size
        try:
            image.thumbnail(self.thumbnailSize, Image.ANTIALIAS)
        except Exception, e:
            log.error("Couldn't shrink image: %s\nBecause: %s" % (self._imageResource.path, str(e)))
            image = Image.new('RGBA', self.thumbnailSize, (0xFF, 0, 0, 0))
            self._msgImage(image, _(u"No Thumbnail Available. Could not shrink original image."))
        image2 = Image.new('RGBA', self.thumbnailSize, (0xFF, 0, 0, 0))
        width1, height1 = image.size
        width2, height2 = image2.size
        left = int(round((width2 - width1) / 2.))
        top = int(round((height2 - height1) / 2.))
        try:
            image2.paste(image, (left, top))
        except IOError:
            self._defaultThumbnail(image2)
        tmpDir = TempDirPath()
        thumbnailPath = Path(tmpDir/self._imageResource.path.namebase + "Thumbnail.png").unique()
        try:
            image2.save(thumbnailPath)
            self._thumbnailResource = Resource(self.parent, thumbnailPath)
        finally:
            thumbnailPath.remove()
    def _defaultThumbnail(self, image):
        """
        Draws a nice default thumbnail on 'image'
        """
        self._msgImage(image,
            _(u"No Thumbnail Available. Could not shrink image."))
    def _msgImage(self, image, msg):
        """
        Puts a message in an image
        """
        draw = ImageDraw.Draw(image)
        draw.rectangle([(0, 0), self.thumbnailSize], fill="black")
        words = msg.split(' ')
        size = draw.textsize(msg)
        top = 1
        while words:
            if top > self.thumbnailSize[1]:
                break
            for ln in range(len(words), -1, -1):
                size = draw.textsize(' '.join(words[:ln]))
                if size[0] <= self.thumbnailSize[0]: 
                    break
            draw.text((1, top), ' '.join(words[:ln]))
            words = words[ln:]
            top += size[1]
    def delete(self):
        """
        Removes our files from resources and removes us from our parent's list
        """
        if self._imageResource:
            self._imageResource.delete()
        if self._thumbnailResource:
            self._thumbnailResource.delete()
        self.parent = None # This also removes our self from our parent's list
    def set_parent(self, parent):
        """
        Used for changing the parent attribute, handles adding and removing from
        parents images list
        """
        if self._parent is not parent:
            if self._parent:
                self._parent.images.remove(self)
                self._id = None
            if parent:
                parent.images.append(self)
                self._id = parent.genImageId()
            self._parent = parent
    def get_imageFilename(self):
        """
        Returns the full path to the image
        """
        return self._imageResource.path
    def set_imageFilename(self, filename):
        """
        Totally changes the image to point to a new filename
        """
        if self._imageResource:
            self._imageResource.delete()
            self._thumbnailResource.delete()
        self._saveFiles(filename)
    def get_thumbnailFilename(self):
        """
        Returns the full path to the thumbnail
        """
        return self._thumbnailResource.path
    def set_caption(self, value):
        """
        Set's the text of our caption
        """
        self._caption.content = value
    imageFilename = property(get_imageFilename, set_imageFilename)
    thumbnailFilename = property(get_thumbnailFilename)
    caption = property(lambda self: self._caption.content, set_caption)
    parent  = property(lambda self: self._parent, set_parent)
    imageSrc = property(lambda self: '%s%s' % (self.resourcesUrl , self._imageResource.storageName))
    thumbnailSrc = property(lambda self: '%s%s' % (self.resourcesUrl, self._thumbnailResource.storageName))
    id = property(lambda self: self._id)
    index = property(lambda self: self.parent.images.index(self))
    def upgradeToVersion1(self):
        """
        Called to upgrade from 0.10 to 0.11
        """
        self._htmlFilename = Path(self._imageFilename).namebase + '.html'
    def _upgradeImageToVersion2(self):
        """
        Upgrades to exe v0.12
        """
        requireUpgrade(self)
        self._imageResource = Resource(self.parent, Path(self._imageFilename))
        self._thumbnailResource = Resource(self.parent, Path(self._thumbnailFilename))
        self._htmlResource = Resource(self.parent, Path(self._htmlFilename))
        del self._imageFilename
        del self._thumbnailFilename
        del self._htmlFilename
    def _deleteHTMLResource(self):
        """
        Delete our HTML Resource
        """
        if hasattr(self, '_htmlResource') and self._htmlResource:
            self._htmlResource.delete()
            del self._htmlResource
    def upgradeToVersion2(self):
        """
        Upgrades to verison 0.20
        """
        package = self.parent.parentNode.package
        package.afterUpgradeHandlers.append(self._deleteHTMLResource)
class GalleryImages(Persistable, list):
    """
    Allows easy access to gallery images
    """
    def __init__(self, idevice):
        """
        Just takes the idevice who it is working for
        """
        list.__init__(self)
        self.idevice = idevice
    def __getstate__(self):
        """
        Enables jellying of our list items
        """
        result = Persistable.__getstate__(self)
        result['.listitems'] = list(self)
        return result
    def __setstate__(self, state):
        """
        Enables jellying of our list items
        """
        for item in state['.listitems']:
            self.append(item)
        del state['.listitems']
        Persistable.__setstate__(self, state)
    def __getitem__(self, index):
        """
        Allows one to retrieve an image by index or id
        """
        if isinstance(index, int) or isinstance(index, slice):
            return list.__getitem__(self, index)
        else:
            for image in self:
                if image.id == index:
                    return image
            else:
                raise KeyError(index)
    def __delitem__(self, index):
        """
        Cleanly removes the image and its filens
        """
        self[index].delete()
    def __deepcopy__(self, memo):
        """
        Makes sure deepcopy doesn't double the entries in our list
        """
        result = GalleryImages(deepcopy(self.idevice, memo))
        memo[id(self)] = result
        for image in self:
            result.append(deepcopy(image, memo))
        return result
class GalleryIdevice(_ShowsResources, Idevice):
    """
    Gallery Idevice. Enables you to easily manage a bunch of images and
    thumbnails.
    """
    persistenceVersion = 7
    previewSize        = (320.0, 240.0)
    _htmlResource      = None
    def __init__(self, parentNode=None):
        """
        Sets up the idevice title and instructions etc
        """
        Idevice.__init__(self, 
                         x_(u"Image Gallery"), 
                         x_(u"eXe Project"), 
                         x_(u"""<p>Where you have a number of images that relate 
to each other or to a particular learning exercise you may wish to display 
these in a gallery context rather then individually.</p>"""),
                         x_(u"Use this Idevice if you have a lot of images to "
                             "show."),
                             "gallery",
                             parentNode)
        self.emphasis          = Idevice.SomeEmphasis
        self.nextImageId       = 0
        self.images            = GalleryImages(self)
        self.currentImageIndex = 0
        self.systemResources  += ["stock-insert-image.png"]
        self._titleInstruc     = x_(u'Enter a title for the gallery')
        self._addImageInstr    = x_(u"Click on the Add images button to select "
                                    u"an image file. The image will appear "
                                    u"below where you will be able to label "
                                    u"it. It's always good practice to put "
                                    u"the file size in the label.")
    addImageInstr = lateTranslate('addImageInstr')
    titleInstruc = lateTranslate('titleInstruc')
    htmlSrc = property(lambda self: '%s%s' % (self.resourcesUrl, self._htmlResource.storageName))
    def genImageId(self):
        """Generate a unique id for an image.
        Called by 'GalleryImage'"""
        self.nextImageId += 1
        return '%s.%s' % (self.id, self.nextImageId - 1)
    def addImage(self, imagePath):
        """
        Adds a new image to the last taking an image path.
        Generates the thumbnail and the image in
        the resources directory.
        """
        return GalleryImage(self, '', imagePath)
    def recreateResources(self):
        """
        Recreates all the thumbnails and html pages from the original image
        resources.
        """
        if len(self.images) > 0:
            self._createHTMLPopupFile()
        for image in self.images:
            image._saveFiles()
    def _killBadImages(self):
        """
        Kills images that have somehow gotten corrupted and lost their
        reference to their resource (See #601)
        """
        for i in range(len(self.images)-1, -1, -1):
            image = self.images[i]
            if hasattr(image, '_htmlResource'): 
                if image._imageResource is None or image._htmlResource is None:
                    del self.images[i]
    def _createHTMLPopupFile(self):
        """
        Renders an HTML page that show's the image
        (Only realy needed for stupid IE)
        """
        _ShowsResources.export()
        try:
            if self.parentNode:
                styleDir = self.parentNode.package.style 
            else:
                styleDir = 'default'
            img = self.images[0]
            data = flatten(
               T.html[
                 T.head[
                   T.title[self.title],
                   T.style(type="text/css")[
                    '@import url(/style/base.css);'],
                   T.style(type="text/css")[
                    '@import url(/style/%s/content.css);' % styleDir],
                   T.style(type="text/css")[
                    '@import url(base.css);'],
                   T.style(type="text/css")[
                    '@import url(content.css);'],
                   T.script[
                     raw(
                         '\n'.join([
                            '  var maxWidth = %s;' % self.previewSize[0],
                            '  var maxHeight = %s;' % self.previewSize[1],
                            '  var thWidth = maxWidth; var thHeight = maxHeight;',
                            '  var images = %s;' % [img.imageSrc.encode('utf-8') for img in self.images],
                            '  var titles = %s;' % [img.caption.encode('utf-8') for img in self.images],
                            '  var imageIdx = 0;',
                            '  var p = window.location.href.search(/=(\d+)$/);',
                            '  if (p >= 0) {',
                            '    imageIdx = parseInt(window.location.href.substr(p+1));',
                            '    if ((imageIdx < 0) || (imageIdx > (images.length - 1))) { imageIdx = 0; }',
                            '  }',
                            '  var imageExpanded = false;',
                            '  var imgObj = new Image();',
                            '  imgObj.onload = function () { getShrinkMod(); toggleZoom(); }',
                            '  imgObj.src = images[imageIdx];',
                            '',
                            'function getShrinkMod() {',
                            '  thWidth = imgObj.width;',
                            '  thHeight = imgObj.height;',
                            '  if (imgObj.width > maxWidth) {',
                            '    thHeight = imgObj.height * maxWidth / imgObj.width;',
                            '    thWidth = maxWidth;',
                            '  }',
                            '  if (thHeight > maxHeight) {',
                            '    thWidth = thWidth * maxHeight / thHeight;',
                            '    thHeight = maxHeight;',
                            '  }',
                            '}',
                            'function toggleZoom() {',
                            '  var imgEle = document.getElementById("the_image");',
                            '  if (imageExpanded) {',
                            '    imgEle.width = thWidth; imgEle.height = thHeight;',
                            '  } else {',
                            '    imgEle.width = imgObj.width; imgEle.height = imgObj.height;',
                            '  }',
                            '  imageExpanded = !imageExpanded;',
                            '}',
                            '',
                            '// Goes one image forward (if possible), then updates the screen',
                            'function next() {',
                            '    if (imageIdx < images.length - 1) {',
                            '        imageIdx++;',
                            '        imageExpanded = true;',
                            '        updateWindow();',
                            '    }',
                            '}',
                            '',
                            '// Goes one image back (if possible), then updates the screen',
                            'function prev() {',
                            '    if (imageIdx > 0) {',
                            '        imageIdx--;',
                            '        imageExpanded = true;',
                            '        updateWindow();',
                            '    }',
                            '}',
                            '',
                            '// Updates the screen',
                            'function updateWindow() {',
                            '    // Show/hide previous button',
                            '    var btnPrev = document.getElementById("btnPrev");',
                            '    if (imageIdx > 0) {',
                            '        btnPrev.style.display = "block";',
                            '    } else {',
                            '        btnPrev.style.display = "none";',
                            '    }',
                            '    // Show/hide next button',
                            '    var btnNext = document.getElementById("btnNext");',
                            '    if (imageIdx < images.length - 1) {',
                            '        btnNext.style.display = "block";',
                            '    } else {',
                            '        btnNext.style.display = "none";',
                            '    }',
                            '    // Update image',
                            '    var imgEle = document.getElementById("the_image");',
                            '    imgObj.src = images[imageIdx];',
                            '    imgEle.src = images[imageIdx];',
                            '    // Update title',
                            '    var title = document.getElementById("nodeTitle");',
                            '    title.innerHTML = titles[imageIdx];',
                            '}', ])),
                 ],
                 T.body(onLoad="updateWindow()")[
                   T.h1(id='nodeTitle')[img.caption],
                   T.p(align='center') [
                     T.table(width="100%")[
                       T.tr[
                         T.td(align="right", width="33%")[
                           T.a(href='javascript:prev()', id='btnPrev')[_('Previous')]
                         ],
                         T.td(align="center", width="33%")[
                           T.a(href='javascript:window.close()')[
                             _('Close')
                           ],
                         ],
                         T.td(align="left", width="33%")[
                            T.a(href='javascript:next()', id='btnNext')[_('Next')]
                         ]
                       ],
                       T.tr[
                         T.td(width="100%", align="center", colspan=3)[
                             T.a(href="javascript:toggleZoom()")[
                                 T.img(id='the_image',
                                       src=unicode(img.imageSrc),
                                       width=min(img.size[0], self.previewSize[0]),
                                       height=min(img.size[1], self.previewSize[1]))
                             ]
                         ]
                       ]
                     ]
                   ]
                 ]
               ]
             ]
           )
        finally:
            _ShowsResources.preview()
        tmpDir = TempDirPath()
        htmlPath = Path(tmpDir/'galleryPopup.html')
        log.debug("_createHTMLPopupFile htmlPath=%s" % htmlPath)
        try:
            htmlFile = open(htmlPath, 'wb')
            htmlFile.write(data)
            htmlFile.close()
            self._htmlResource = Resource(self, htmlPath)
        finally:
            htmlPath.remove()
    def upgradeToVersion1(self):
        """
        Upgrades the node to exe version 0.7
        """
        self.lastIdevice = False
    def upgradeToVersion2(self):
        """
        Upgrades exe to v0.10
        """
        self._upgradeIdeviceToVersion1()
    def upgradeToVersion3(self):
        """
        Upgrades to v0.12
        """
        self._upgradeIdeviceToVersion2()
        for image in self.images:
            image._upgradeImageToVersion2()
    def upgradeToVersion4(self):
        """
        Upgrades to v0.13
        """
        package = self.parentNode.package
        package.afterUpgradeHandlers.append(self.recreateResources)
    def upgradeToVersion5(self):
        """
        Upgrades to v0.19
        Some old resources had no storageName.
        """
        self.userResources = [res for res in self.userResources if res.storageName is not None]
    def upgradeToVersion6(self):
        """
        Upgrades to exe version 0.20.alpha (nightlies)
        Some packages were corrupted (See #601).
        """
        package = self.parentNode.package
        package.afterUpgradeHandlers.append(self._killBadImages)
    def upgradeToVersion7(self):
        """
        Upgrades to Version 0.20
        """
        package = self.parentNode.package
        if not hasattr(package, 'resources'):
            package.resources = {}
        self.recreateResources()
