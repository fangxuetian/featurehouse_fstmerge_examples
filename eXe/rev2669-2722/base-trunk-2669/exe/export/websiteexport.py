"""
WebsiteExport will export a package as a website of HTML pages
"""
import logging
import re
import imp
from cgi                      import escape
from exe.webui.blockfactory   import g_blockFactory
from exe.engine.error         import Error
from exe.engine.path          import Path, TempDirPath
from exe.export.pages         import uniquifyNames
from exe.export.websitepage   import WebsitePage
from zipfile                  import ZipFile, ZIP_DEFLATED
log = logging.getLogger(__name__)
class WebsiteExport(object):
    """
    WebsiteExport will export a package as a website of HTML pages
    """
    def __init__(self, config, styleDir, filename):
        """
        'stylesDir' is the directory where we can copy the stylesheets from
        'outputDir' is the directory that will be [over]written
        with the website
        """
        self.config       = config
        self.imagesDir    = config.webDir/"images"
        self.scriptsDir   = config.webDir/"scripts"
        self.templatesDir = config.webDir/"templates"
        self.stylesDir    = Path(styleDir)
        self.filename     = Path(filename)
        self.pages        = []
    def exportZip(self, package):
        """ 
        Export web site
        Cleans up the previous packages pages and performs the export
        """
        outputDir = TempDirPath()
        if (self.stylesDir/"websitepage.py").exists():
            global WebsitePage
            module = imp.load_source("websitepage", 
                                     self.stylesDir/"websitepage.py")
            WebsitePage = module.WebsitePage
        self.pages = [ WebsitePage("index", 1, package.root) ]
        self.generatePages(package.root, 1)
        uniquifyNames(self.pages)
        prevPage = None
        thisPage = self.pages[0]
        for nextPage in self.pages[1:]:
            thisPage.save(outputDir, prevPage, nextPage, self.pages)
            prevPage = thisPage
            thisPage = nextPage
        thisPage.save(outputDir, prevPage, None, self.pages)
        self.copyFiles(package, outputDir)
        self.filename.safeSave(self.doZip, _('EXPORT FAILED!\nLast succesful export is %s.'), outputDir)
        outputDir.rmtree()
    def doZip(self, fileObj, outputDir):
        """
        Actually saves the zip data. Called by 'Path.safeSave'
        """
        zipped = ZipFile(fileObj, "w")
        for scormFile in outputDir.files():
            zipped.write(scormFile, scormFile.basename().encode('utf8'), ZIP_DEFLATED)
        zipped.close()
    def export(self, package):
        """ 
        Export web site
        Cleans up the previous packages pages and performs the export
        """
        outputDir = self.filename
        if not outputDir.exists(): 
            outputDir.mkdir()
        if (self.stylesDir/"websitepage.py").exists():
            global WebsitePage
            module = imp.load_source("websitepage", 
                                     self.stylesDir/"websitepage.py")
            WebsitePage = module.WebsitePage
        self.pages = [ WebsitePage("index", 1, package.root) ]
        self.generatePages(package.root, 1)
        uniquifyNames(self.pages)
        prevPage = None
        thisPage = self.pages[0]
        for nextPage in self.pages[1:]:
            thisPage.save(outputDir, prevPage, nextPage, self.pages)
            prevPage = thisPage
            thisPage = nextPage
        thisPage.save(outputDir, prevPage, None, self.pages)
        self.copyFiles(package, outputDir)
    def copyFiles(self, package, outputDir):
        """
        Copy all the files used by the website.
        """
        styleFiles  = [self.stylesDir/'..'/'base.css']
        styleFiles += [self.stylesDir/'..'/'popup_bg.gif']
        styleFiles += self.stylesDir.files("*.css")
        styleFiles += self.stylesDir.files("*.jpg")
        styleFiles += self.stylesDir.files("*.gif")
        styleFiles += self.stylesDir.files("*.png")
        styleFiles += self.stylesDir.files("*.js")
        styleFiles += self.stylesDir.files("*.html")
        self.stylesDir.copylist(styleFiles, outputDir)
        package.resourceDir.copyfiles(outputDir)
        self.scriptsDir.copylist(('libot_drag.js', 'common.js'), 
                                  outputDir)
        hasVideoContainer = False
        hasMagnifier      = False
        hasXspfplayer     = False
        isBreak           = False
        for page in self.pages:
            if isBreak:
                break
            for idevice in page.node.idevices:
                if (hasVideoContainer and hasMagnifier and hasXspfplayer):
                    isBreak = True
                    break
                if not hasVideoContainer:
                    if 'videoContainer.swf' in idevice.systemResources:
                        hasVideoContainer = True
                if not hasMagnifier:
                    if 'magnifier.swf' in idevice.systemResources:
                        hasMagnifier = True
                if not hasXspfplayer:
                    if 'xspf_player.swf' in idevice.systemResources:
                        hasXspfplayer = True
        if hasVideoContainer:
            videofile = (self.templatesDir/'videoContainer.swf')
            videofile.copyfile(outputDir/'videoContainer.swf')
        if hasMagnifier:
            videofile = (self.templatesDir/'magnifier.swf')
            videofile.copyfile(outputDir/'magnifier.swf')
        if hasXspfplayer:
            videofile = (self.templatesDir/'xspf_player.swf')
            videofile.copyfile(outputDir/'xspf_player.swf')
        (self.templatesDir/'fdl.html').copyfile(outputDir/'fdl.html')
    def generatePages(self, node, depth):
        """
        Recursively generate pages and store in pages member variable
        for retrieving later
        """           
        for child in node.children:
            pageName = child.titleShort.lower().replace(" ", "_")
            pageName = re.sub(r"\W", "", pageName)
            if not pageName:
                pageName = "__"
            self.pages.append(WebsitePage(pageName, depth, child))
            self.generatePages(child, depth + 1)
