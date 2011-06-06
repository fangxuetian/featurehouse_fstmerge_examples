"""
Package represents the collection of resources the user is editing
i.e. the "package".
"""
import logging
import time
import zipfile 
import re
from xml.dom                   import minidom
from exe.engine.path           import Path, TempDirPath, toUnicode
from exe.engine.node           import Node
from exe.engine.genericidevice import GenericIdevice
from exe.engine.persist        import Persistable, encodeObject, \
                                      decodeObject, decodeObjectRaw
from exe                       import globals as G
from exe.engine.resource       import Resource
from twisted.persisted.styles  import Versioned, doUpgrade
from twisted.spread.jelly      import Jellyable, Unjellyable
from exe.engine.beautifulsoup  import BeautifulSoup
log = logging.getLogger(__name__)
def clonePrototypeIdevice(title):
    idevice = None
    for prototype in G.application.ideviceStore.getIdevices(): 
        if prototype.get_title() == title:
            log.debug('have prototype of:' + prototype.get_title()) 
            idevice = prototype.clone() 
            idevice.edit = False
            break 
    return idevice
def burstIdevice(idev_type, i, node): 
    idevice = clonePrototypeIdevice(idev_type)
    if idevice is None:
        log.warn("unable to clone " + idev_type + " idevice")
        freetext_idevice = clonePrototypeIdevice('Free Text')
        if freetext_idevice is None:
            log.error("unable to clone Free Text for " + idev_type 
                    + " idevice")
            return
        idevice = freetext_idevice
    node.addIdevice(idevice)
    idevice.burstHTML(i)
    return idevice
def loadNodesIdevices(node, s):
    soup = BeautifulSoup(s)
    body = soup.find('body')
    if body:
        idevices = body.findAll(name='div', 
                attrs={'class' : re.compile('Idevice$') })
        if len(idevices) > 0:
            for i in idevices: 
                if i.attrMap['class']=="activityIdevice":
                    idevice = burstIdevice('Activity', i, node)
                elif i.attrMap['class']=="objectivesIdevice":
                    idevice = burstIdevice('Objectives', i, node)
                elif i.attrMap['class']=="preknowledgeIdevice":
                    idevice = burstIdevice('Preknowledge', i, node)
                elif i.attrMap['class']=="readingIdevice":
                    idevice = burstIdevice('Reading Activity', i, node)
                elif i.attrMap['class']=="RssIdevice":
                    idevice = burstIdevice('RSS', i, node)
                elif i.attrMap['class']=="WikipediaIdevice":
                    idevice = burstIdevice('Wiki Article', i, node)
                elif i.attrMap['class']=="ReflectionIdevice":
                    idevice = burstIdevice('Reflection', i, node)
                elif i.attrMap['class']=="GalleryIdevice":
                    idevice = burstIdevice('Image Gallery', i, node)
                elif i.attrMap['class']=="ImageMagnifierIdevice":
                    idevice = burstIdevice('Image Magnifier', i, node)
                elif i.attrMap['class']=="AppletIdevice":
                    idevice = burstIdevice('Java Applet', i, node)
                elif i.attrMap['class']=="ExternalUrlIdevice":
                    idevice = burstIdevice('External Web Site', i, node)
                elif i.attrMap['class']=="ClozeIdevice":
                    idevice = burstIdevice('Cloze Activity', i, node)
                elif i.attrMap['class']=="FreeTextIdevice":
                    idevice = burstIdevice('Free Text', i, node)
                elif i.attrMap['class']=="CasestudyIdevice":
                    idevice = burstIdevice('Case Study', i, node)
                elif i.attrMap['class']=="MultichoiceIdevice":
                    idevice = burstIdevice('Multi-choice', i, node)
                elif i.attrMap['class']=="MultiSelectIdevice":
                    idevice = burstIdevice('Multi-select', i, node)
                elif i.attrMap['class']=="QuizTestIdevice":
                    idevice = burstIdevice('SCORM Quiz', i, node)
                elif i.attrMap['class']=="TrueFalseIdevice":
                    idevice = burstIdevice('True-False Question', i, node)
                else:
                    log.warn("unburstable idevice " + i.attrMap['class'] + 
                            "; bursting into Free Text")
                    idevice = burstIdevice('Free Text', i, node)
        else:
            log.warn("no idevices found on this node, bursting into Free Text.")
            idevice = burstIdevice('Free Text', i, node)
    else:
        log.warn("unable to read the body of this node.")
def test_for_node(html_content):
    exe_string = u"<!-- Created using eXe: http://exelearning.org -->"
    if html_content.decode('utf-8').find(exe_string) >= 0:
        return True
    else:
        return False
def loadNode(pass_num, resourceDir, zippedFile, node, doc, item, level):
    titles = item.getElementsByTagName('title')
    node.setTitle(titles[0].firstChild.data)
    node_resource = item.attributes['identifierref'].value
    log.debug('*' * level + ' ' + titles[0].firstChild.data + '->' + item.attributes['identifierref'].value)
    for resource in doc.getElementsByTagName('resource'):
        if resource.attributes['identifier'].value == node_resource:
            for file in resource.childNodes:
                if file.nodeName == 'file':
                    filename = file.attributes['href'].value
                    is_exe_node_html = False
                    if filename.endswith('.html') \
                    and filename != "fdl.html" \
                    and not filename.startswith("galleryPopup"):
                        is_exe_node_html = \
                                test_for_node(zippedFile.read(filename))
                    if is_exe_node_html:
                        if pass_num == 1:
                            log.debug('loading idevices from node: ' + filename)
                            loadNodesIdevices(node, zippedFile.read(filename))
                    elif filename == "fdl.html" or \
                    filename.startswith("galleryPopup."):
                        if pass_num == 0:
                            log.debug('ignoring resource file: '+ filename)
                    else:
                        if pass_num == 0:
                            try:
                                zipinfo = zippedFile.getinfo(filename)
                                log.debug('unzipping resource file: '
                                        + resourceDir/filename )
                                outFile = open(resourceDir/filename, "wb") 
                                outFile.write(zippedFile.read(filename)) 
                                outFile.flush() 
                                outFile.close()
                            except:
                                log.warn('error unzipping resource file: '
                                        + resourceDir/filename )
            break
    for subitem in item.childNodes:
        if subitem.nodeName == 'item': 
            next_node = node
            if pass_num == 1:
                next_node = node.createChild()
            loadNode(pass_num, resourceDir, zippedFile, next_node,
                    doc, subitem, level+1)
def loadCC(zippedFile, filename):
    """
    Load an IMS Common Cartridge or Content Package from filename
    """
    package = Package(Path(filename).namebase)
    xmldoc = minidom.parseString( zippedFile.read('imsmanifest.xml')) 
    organizations_list = xmldoc.getElementsByTagName('organizations')
    level = 0
    for pass_num in range(2):
        for organizations in organizations_list:
            organization_list = organizations.getElementsByTagName(
                    'organization')
            for organization in organization_list:
                for item in organization.childNodes:
                    if item.nodeName == 'item':
                        loadNode(pass_num, package.resourceDir, zippedFile, 
                                package.root, xmldoc, item, level)
    return package
class DublinCore(Jellyable, Unjellyable):
    """
    Holds dublin core info
    """
    def __init__(self):
        self.title = ''
        self.creator = ''
        self.subject = ''
        self.description = ''
        self.publisher = ''
        self.contributors = ''
        self.date = ''
        self.type = ''
        self.format = ''
        self.identifier = ''
        self.source = ''
        self.language = ''
        self.relation = ''
        self.coverage = ''
        self.rights = ''
    def __setattr__(self, name, value):
        self.__dict__[name] = toUnicode(value)
class Package(Persistable):
    """
    Package represents the collection of resources the user is editing
    i.e. the "package".
    """
    persistenceVersion = 9
    nonpersistant      = ['resourceDir', 'filename']
    _name              = '' 
    tempFile           = False # This is set when the package is saved as a temp copy file
    _title             = '' 
    _author            = ''
    _description       = ''
    _backgroundImg     = ''
    defaultLevelNames  = [x_(u"Topic"), x_(u"Section"), x_(u"Unit")]
    def __init__(self, name):
        """
        Initialize 
        """
        log.debug(u"init " + repr(name))
        self._nextIdeviceId = 0
        self._nextNodeId    = 0
        self._nodeIdDict    = {} 
        self._levelNames    = self.defaultLevelNames[:]
        self.name           = name
        self._title         = u''
        self._backgroundImg = u''
        self.backgroundImgTile = False
        self.filename      = u''
        self.root          = Node(self, None, _(u"Home"))
        self.currentNode   = self.root
        self.style         = u"default"
        self.isChanged     = False
        self.idevices      = []
        self.dublinCore    = DublinCore()
        self.scolinks      = False
        self.license       = "None"
        self.footer        = ""
        self.resourceDir = TempDirPath()
        self.resources = {} # Checksum-[_Resource(),..]
    def set_name(self, value):
        self._name = toUnicode(value)
    def set_title(self, value):
        self._title = toUnicode(value)
    def set_author(self, value):
        self._author = toUnicode(value)
    def set_description(self, value):
        self._description = toUnicode(value)
    def get_backgroundImg(self):
        """Get the background image for this package"""
        if self._backgroundImg:
            return "file://" + self._backgroundImg.path
        else:
            return ""
    def set_backgroundImg(self, value):
        """Set the background image for this package"""
        if self._backgroundImg:
            self._backgroundImg.delete()
        if value:
            if value.startswith("file://"):
                value = value[7:]
            imgFile = Path(value)
            self._backgroundImg = Resource(self, Path(imgFile))
        else:
            self._backgroundImg = u''
    def get_level1(self):
        return self.levelName(0)
    def set_level1(self, value):
        if value != '':
            self._levelNames[0] = value 
        else:
            self._levelNames[0] = self.defaultLevelNames[0]
    def get_level2(self):
        return self.levelName(1)
    def set_level2(self, value):
        if value != '':
            self._levelNames[1] = value 
        else:
            self._levelNames[1] = self.defaultLevelNames[1]
    def get_level3(self):
        return self.levelName(2)
    def set_level3(self, value):
        if value != '':
            self._levelNames[2] = value 
        else:
            self._levelNames[2] = self.defaultLevelNames[2]
    name          = property(lambda self:self._name, set_name)
    title         = property(lambda self:self._title, set_title)
    author        = property(lambda self:self._author, set_author)
    description   = property(lambda self:self._description, set_description)
    backgroundImg = property(get_backgroundImg, set_backgroundImg)
    level1 = property(get_level1, set_level1)
    level2 = property(get_level2, set_level2)
    level3 = property(get_level3, set_level3)
    def findNode(self, nodeId):
        """
        Finds a node from its nodeId
        (nodeId can be a string or a list/tuple)
        """
        log.debug(u"findNode" + repr(nodeId))
        node = self._nodeIdDict.get(nodeId)
        if node and node.package is self:
            return node
        else: 
            return None
    def levelName(self, level):
        """
        Return the level name
        """
        if level < len(self._levelNames):
            return _(self._levelNames[level])
        else:
            return _(u"?????")
    def save(self, filename=None, tempFile=False):
        """
        Save package to disk
        pass an optional filename
        """
        self.tempFile = tempFile
        if filename:
            filename = Path(filename)
            name = filename.splitpath()[1]
            if not tempFile:
                self.name = name.basename().splitext()[0]
        elif self.filename:
            filename = Path(self.filename)
        else:
            raise AssertionError(u'No name passed when saving a new package')
        log.debug(u"Will save %s to: %s" % (self.name, filename))
        if tempFile:
            self.nonpersistant.remove('filename')
            oldFilename, self.filename = self.filename, unicode(self.filename)
            try:
                filename.safeSave(self.doSave, _('SAVE FAILED!\nLast succesful save is %s.'))
            finally:
                self.nonpersistant.append('filename')
                self.filename = oldFilename
        else:
            self.filename = filename
            filename.safeSave(self.doSave, _('SAVE FAILED!\nLast succesful save is %s.'))
            self.isChanged = False
            self.updateRecentDocuments(filename)
    def updateRecentDocuments(self, filename):
        """
        Updates the list of recent documents
        """
        genericData = G.application.config.configDir/'idevices'/'generic.data'
        if genericData.isfile() or genericData.islink():
            if Path(filename).samefile(genericData):
                return
        recentProjects = G.application.config.recentProjects
        if filename in recentProjects:
            if recentProjects[0] == filename:
                return
            recentProjects.remove(filename)
        recentProjects.insert(0, filename)
        del recentProjects[5:] # Delete any older names from the list
        G.application.config.configParser.write() # Save the settings
    def doSave(self, fileObj):
        """
        Actually performs the save to 'fileObj'.
        """
        zippedFile = zipfile.ZipFile(fileObj, "w", zipfile.ZIP_DEFLATED)
        try:
            for resourceFile in self.resourceDir.files():
                zippedFile.write(unicode(resourceFile.normpath()),
                        resourceFile.name.encode('utf8'), zipfile.ZIP_DEFLATED)
            zinfo = zipfile.ZipInfo(filename='content.data',
                    date_time=time.localtime()[0:6])
            zinfo.external_attr = 0100644<<16L
            zippedFile.writestr(zinfo, encodeObject(self))
        finally:
            zippedFile.close()
    def extractNode(self):
        """
        Clones and extracts the currently selected node into a new package.
        """
        newPackage = Package('NoName') # Name will be set once it is saved..
        newPackage.title  = self.currentNode.title
        newPackage.style  = self.style
        newPackage.author = self.author
        newPackage._nextNodeId = self._nextNodeId
        self.currentNode.copyToPackage(newPackage)
        return newPackage
    @staticmethod
    def load(filename, newLoad=True, destinationPackage=None):
        """
        Load package from disk, returns a package.
        """
        if not zipfile.is_zipfile(filename):
            return None
        zippedFile = zipfile.ZipFile(filename, "r")
        try:
            toDecode   = zippedFile.read(u"content.data")
        except KeyError:
            log.info("no content.data, trying Common Cartridge/Content Package")
            newPackage = loadCC(zippedFile, filename)
            newPackage.tempFile = False
            newPackage.isChanged = False
            newPackage.filename = Path(filename)
            return newPackage
        resourceDir = TempDirPath()
        for fn in zippedFile.namelist():
            if unicode(fn, 'utf8') != u"content.data":
                outFile = open(resourceDir/fn, "wb")
                outFile.write(zippedFile.read(fn))
                outFile.flush()
                outFile.close()
        try:
            newPackage = decodeObjectRaw(toDecode)
            G.application.afterUpgradeHandlers = []
            newPackage.resourceDir = resourceDir
            G.application.afterUpgradeZombies2Delete = []
            if newLoad: 
                log.debug("load() about to doUpgrade newPackage \"" 
                        + newPackage._name + "\" " + repr(newPackage) )
                if hasattr(newPackage, 'resourceDir'):
                    log.debug("newPackage resourceDir = "
                            + newPackage.resourceDir)
                else:
                    log.error("newPackage resourceDir has NO resourceDir!")
                doUpgrade(newPackage)
            else: 
                log.debug("load() about to merge doUpgrade newPackage \"" 
                        + newPackage._name + "\" " + repr(newPackage)
                        + " INTO destinationPackage \"" 
                        + destinationPackage._name + "\" " 
                        + repr(destinationPackage))
                log.debug("using their resourceDirs:")
                if hasattr(newPackage, 'resourceDir'):
                    log.debug("   newPackage resourceDir = " 
                            + newPackage.resourceDir)
                else:
                    log.error("newPackage has NO resourceDir!")
                if hasattr(destinationPackage, 'resourceDir'):
                    log.debug("   destinationPackage resourceDir = " 
                            + destinationPackage.resourceDir)
                else:
                    log.error("destinationPackage has NO resourceDir!")
                doUpgrade(destinationPackage, 
                        isMerge=True, preMergePackage=newPackage)
        except:
            import traceback
            traceback.print_exc()
            raise
        if newPackage.tempFile:
            newPackage.tempFile = False
        else:
            newPackage.filename = Path(filename)
        for handler_priority in range(3):
          for handler in G.application.afterUpgradeHandlers:
            if handler_priority == 0 and \
            repr(handler.im_class)=="<class 'exe.engine.resource.Resource'>":
                handler()
            elif handler_priority == 1 and \
            repr(handler.im_class)=="<class 'exe.engine.package.Package'>":
                if handler.im_self == newPackage: 
                    handler()
                else:
                    log.warn("Extra package object found, " \
                       + "ignoring its afterUpgradeHandler: " \
                       + repr(handler))
            elif handler_priority == 2 and \
            repr(handler.im_class)!="<class 'exe.engine.resource.Resource'>" \
            and \
            repr(handler.im_class)!="<class 'exe.engine.package.Package'>":
                handler()
        G.application.afterUpgradeHandlers = []
        num_zombies = len(G.application.afterUpgradeZombies2Delete)
        for i in range(num_zombies-1, -1, -1):
            zombie = G.application.afterUpgradeZombies2Delete[i]
            zombie_is_node = False
            if isinstance(zombie, Node):
                zombie_is_node = True
            if zombie_is_node: 
                zombie.delete(pruningZombies=True) 
            else:
                zombie.delete() 
            del zombie
        G.application.afterUpgradeZombies2Delete = []
        newPackage.updateRecentDocuments(newPackage.filename)
        newPackage.isChanged = False
        return newPackage
    def cleanUpResources(self):
        """
        Removes duplicate resource files
        """
        if not hasattr(self,'resourceDir'):
            log.warn("cleanUpResources called on a redundant package")
            return
        existingFiles = set([fn.basename() for fn in self.resourceDir.files()])
        usedFiles = set([reses[0].storageName for reses in self.resources.values()])
        for fn in existingFiles - usedFiles:
            (self.resourceDir/fn).remove()
    def findResourceByName(self, queryName):
        """
        Support for merging, and anywhere else that unique names might be
        checked before actually comparing against the files (as will be 
        done by the resource class itself in its _addOurselvesToPackage() )
        """
        foundResource = None
        queryResources = self.resources
        for this_checksum in queryResources:
            for this_resource in queryResources[this_checksum]:
                if queryName == this_resource.storageName:
                    foundResource = this_resource
                    return foundResource
        return foundResource
    def upgradeToVersion1(self):
        """
        Called to upgrade from 0.3 release
        """
        self._nextNodeId = 0
        self._nodeIdDict = {}
        draft = getattr(self, 'draft')
        draft._id = self._regNewNode(draft)
        draft._package = self
        setattr(self, 'editor', Node(self, None, _(u"iDevice Editor")))
        idevice = GenericIdevice("", "", "", "", "")
        editor = getattr(self, 'editor')
        idevice.parentNode = editor
        editor.addIdevice(idevice)
        def superReg(node):
            """Registers all our nodes
            because in v0 they were not registered
            in this way"""
            node._id = self._regNewNode(node)
            node._package = self
            for child in node.children:
                superReg(child)
        superReg(self.root)
    def _regNewNode(self, node):
        """
        Called only by nodes, 
        stores the node in our id lookup dict
        returns a new unique id
        """
        id_ = unicode(self._nextNodeId)
        self._nextNodeId += 1
        self._nodeIdDict[id_] = node
        return id_
    def getNewIdeviceId(self):
        """
        Returns an iDevice Id which is unique for this package.
        """
        id_ = unicode(self._nextIdeviceId)
        self._nextIdeviceId += 1
        return id_
    def upgradeToVersion2(self):
        """
        Called to upgrade from 0.4 release
        """
        getattr(self, 'draft').delete()
        getattr(self, 'editor').delete()
        delattr(self, 'draft')
        delattr(self, 'editor')
        self._nextNodeId = 0
        def renumberNode(node):
            """
            Gives the old node a number
            """
            node._id = self._regNewNode(node)
            for child in node.children:
                renumberNode(child)
        renumberNode(self.root)
    def upgradeToVersion3(self):
        """
        Also called to upgrade from 0.4 release
        """
        self._nextIdeviceId = 0
    def upgradeToVersion4(self):
        """
        Puts properties in their place
        Also called to upgrade from 0.8 release
        """
        self._name = toUnicode(self.__dict__['name'])
        self._author = toUnicode(self.__dict__['author'])
        self._description = toUnicode(self.__dict__['description'])
    def upgradeToVersion5(self):
        """
        For version 0.11
        """
        self._levelNames = self.levelNames
        del self.levelNames
    def upgradeToVersion6(self):
        """
        For version 0.14
        """
        self.dublinCore = DublinCore()
        self.title = self.root.title
        self.dublinCore.title = self.root.title
        self.dublinCore.creator = self._author
        self.dublinCore.description = self._description
        self.scolinks = False
    def upgradeToVersion7(self):
        """
        For version 0.15
        """
        self._backgroundImg = ''
        self.backgroundImgTile = False
    def upgradeToVersion8(self):
        """
        For version 0.20, alpha, for nightlies r2469
        """
        self.license = 'None'
        self.footer = ""
        self.idevices = []
    def upgradeToVersion9(self):
        """
        For version >= 0.20.4
        """
        if not hasattr(self, 'resources'):
            self.resources = {}
        G.application.afterUpgradeHandlers.append(self.cleanUpResources)
