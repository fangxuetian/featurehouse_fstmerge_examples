"""
Gallery Idevice. Enables you to easily manage a bunch of images and thumbnails
"""
import Image, ImageDraw
from twisted.persisted.styles import requireUpgrade
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
class GalleryImage(Persistable):
    """
    Holds a gallery image and its caption. Can produce a thumbnail
    and a preview, popup window.
    """
    persistenceVersion = 1
    resourcesUrl  = 'resources/'
    _parent       = None
    _caption      = None
    _id           = None
    thumbnailSize = (128, 128)
    previewSize   = (320.0, 240.0)
    size          = thumbnailSize
    bgColour      = 0x808080
    def __init__(self, parent, caption, originalImagePath):
        """
        'parent' is a GalleryIdevice instance
        'caption' is some text that will be displayed with the image
        'originalImagePath' is the local path to the image
        """
        self.parent             = parent
        self._caption           = TextField(caption)
        self._imageResource     = None
        self._thumbnailResource = None
        self._htmlResource      = None
        self._saveFiles(originalImagePath)
    def _saveFiles(self, originalImagePath=None):
        """
        Copies the image file and saves the thumbna il file
        'originalImagePath' is a Path instance
        setting 'originalImagePath' to None, will just recreate the html and
        thumbnail resources from the existing image resource.
        """
        package = self.parent.parentNode.package
        if originalImagePath is not None:
            originalImagePath = Path(originalImagePath)
            self._imageResource = Resource(self.parent, originalImagePath)
        tmpDir = TempDirPath()
        htmlPath = (package.resourceDir/self._imageResource.path.namebase + ".html")
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
        thumbnailPath = (package.resourceDir/self._imageResource.path.namebase + "Thumbnail.png")
        image2.save(thumbnailPath)
        self._thumbnailResource = Resource(self.parent, thumbnailPath)
        htmlPath = (package.resourceDir/self._imageResource.path.namebase + ".html")
        self._createHTMLPopupFile(htmlPath)
        self._htmlResource = Resource(self.parent, htmlPath)
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
    def _createHTMLPopupFile(self, htmlPath):
        """
        Renders an HTML page that show's the image
        (Only realy needed for stupid IE)
        """
        log.debug("_createHTMLPopupFile htmlPath=%s" % htmlPath)
        if self.parent.parentNode:
            styleDir = self.parent.parentNode.package.style 
        else:
            styleDir = 'default'
        if htmlPath.ext == ".html":
            index = self.parent.images.index(self)
            if index == 0:
                backStan = raw('&nbsp;')
            else:
                prevUrl = self.parent.images[index - 1]._htmlResource.storageName
                backStan = T.a(href=prevUrl)[_('Previous')]
            if index == len(self.parent.images) - 1:
                nextStan = raw('&nbsp;')
            else:
                nextUrl = self.parent.images[index + 1]._htmlResource.storageName
                nextStan = T.a(href=nextUrl)[_('Next')]
            data = flatten(
               T.html[
                 T.head[
                   T.title[self.caption],
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
                             '  imgObj = new Image();',
                             '  imgObj.src = "%s";' % \
                                self._imageResource.storageName, 
                             '  multiplier = 1;',
                             '  imageExpanded = false;',
                             '  imageCanExpand = false;',
                             '  maxWidth = %s;' % self.previewSize[0],
                             '  maxHeight = %s;' % self.previewSize[1],
                             '',
                             '// Returns the multiplier to shink the Image',
                             'function getShrinkMod() {',
                             '  if (imgObj.width > imgObj.height) {',
                             '    if (imgObj.width > maxWidth) {',
                             '      imageCanExpand = true;'
                             '      return maxWidth / imgObj.width;',
                             '    } else {', 
                             '       return 1;',
                             '    }', 
                             '  } else {', 
                             '    if (imgObj.height > maxHeight) {',
                             '      imageCanExpand = true;'
                             '      return maxHeight / imgObj.height;',
                             '    } else {', 
                             '       return 1;',
                             '    }', 
                             '  }', 
                             '}', 
                             '',
                             '// Resize the image',
                             'function onLoad() {',
                             '  var imgEle = document.getElementById("the_image");',
                             '  multiplier = getShrinkMod();',
                             '  if (imageCanExpand) {',
                             '      // Shrink the Image',
                             '      imgEle.width = imgObj.width * multiplier;',
                             '      imgEle.height = imgObj.height * multiplier;',
                             '  }', 
                             '}', 
                             '',
                             'function growImage() {',
                             '  var imgEle = document.getElementById("the_image");',
                             '  if (imageCanExpand) {',
                             '    if (imageExpanded) {',
                             '        // Shrink Image',
                             '        imgEle.width = imgObj.width * multiplier;',
                             '        imgEle.height = imgObj.height * multiplier;',
                             '    } else {',
                             '        // Grow Image',
                             '        imgEle.width = imgObj.width;',
                             '        imgEle.height = imgObj.height;',
                             '    }',
                             '    imageExpanded = !imageExpanded;',
                             '  }',
                             '}'])),
                     ]
                 ],
                 T.body(onLoad="onLoad()")[
                   T.h1(id='nodeTitle')[self.caption],
                   T.p(align='center') [
                     T.table(width="100%")[
                       T.tr[
                         T.td(width="100%", align="center", colspan=3)[
                             T.a(href="javascript:growImage()")[
                                 T.img(id='the_image',
                                       src=unicode(self._imageResource.storageName),
                                       width=min(self.size[0],
                                                 self.previewSize[0]),
                                       height=min(self.size[1],
                                                  self.previewSize[1]))
                             ]
                         ]
                       ],
                       T.tr[
                         T.td(align="right", width="33%")[
                           backStan
                         ],
                         T.td(align="center", width="33%")[
                           T.a(href='javascript:window.close()')[
                             _('Close')
                           ],
                         ],
                         T.td(align="left", width="33%")[
                            nextStan
                         ]
                       ]
                     ]
                   ]
                 ]
               ])
            htmlFile = open(htmlPath, 'wb')
            htmlFile.write(data)
            htmlFile.close()
    def delete(self):
        """
        Removes our files from resources and removes us from our parent's list
        """
        self._imageResource.delete()
        self._thumbnailResource.delete()
        self._htmlResource.delete()
        self.parent = None
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
    def set_caption(self, value):
        """
        Lets you set our caption as a string
        """
        self._caption.content = value
        if self._htmlResource:
            self._createHTMLPopupFile(self._htmlResource.path)
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
            self._htmlResource.delete()
        self._saveFiles(filename)
    def get_thumbnailFilename(self):
        """
        Returns the full path to the thumbnail
        """
        return self._thumbnailResource.path
    imageFilename = property(get_imageFilename, set_imageFilename)
    thumbnailFilename = property(get_thumbnailFilename)
    parent  = property(lambda self: self._parent, set_parent)
    caption = property(lambda self: unicode(self._caption.content), set_caption)
    imageSrc = property(lambda self: '%s%s' % 
                   (self.resourcesUrl , self._imageResource.storageName))
    thumbnailSrc = property(lambda self: '%s%s' %
                            (self.resourcesUrl, 
                             self._thumbnailResource.storageName))
    htmlSrc = property(lambda self: '%s%s' %
                            (self.resourcesUrl, 
                             self._htmlResource.storageName))
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
class GalleryIdevice(Idevice):
    """
    Gallery Idevice. Enables you to easily manage a bunch of images and
    thumbnails.
    """
    persistenceVersion = 5
    def __init__(self, parentNode=None):
        """
        Sets up the idevice title and instructions etc
        """
        Idevice.__init__(self, 
                         x_(u"Image Gallery"), 
                         x_(u"University of Auckland"), 
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
    def onResourceNamesChanged(self, resourceNamesChanged):
        """
        Called when the iDevice's resources need their names changed
        """
        for oldName, newName in resourceNamesChanged:
            for image in self.images:
                if image._imageResource.storageName == newName:
                    htmlPath = self.parentNode.package.resourceDir/newName
                    image._createHTMLPopupFile(htmlPath)
    def recreateResources(self):
        """
        Recreates all the thumbnails and html pages from the original image
        resources.
        """
        for image in self.images:
            image._saveFiles()
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
