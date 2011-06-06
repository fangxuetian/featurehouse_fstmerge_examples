"""
SinglePageExport will export a package as a website of HTML pages
"""
from cgi                      import escape
from exe.webui.blockfactory   import g_blockFactory
from exe.engine.error         import Error
from exe.engine.path          import Path
from exe.export.singlepage    import SinglePage
import logging
log = logging.getLogger(__name__)
class SinglePageExport(object):
    """
    SinglePageExport will export a package as a website of HTML pages
    """
    def __init__(self, stylesDir, outputDir, imagesDir, scriptsDir, templatesDir):
        """
        'stylesDir' is the directory where we can copy the stylesheets from
        'outputDir' is the directory that will be [over]written
        with the website
        """
        self.html         = ""
        self.style        = None
        self.name         = None
        self.stylesDir    = Path(stylesDir)
        self.outputDir    = Path(outputDir)
        self.imagesDir    = Path(imagesDir)
        self.scriptsDir   = Path(scriptsDir)
        self.templatesDir = Path(templatesDir)
	self.page         = None
        if not self.outputDir.exists(): 
            self.outputDir.mkdir()
    def export(self, package, for_print=0):
        """ 
        Export web site
        Cleans up the previous packages pages and performs the export
        """
        self.style = package.style       
	
	self.page = SinglePage("index", 1, package.root)
        self.page.save(self.outputDir/"index.html", for_print)
	
	self.copyFiles(package)
    def copyFiles(self, package):
        """
        Copy all the files used by the website.
        """
        styleFiles  = [self.stylesDir/'..'/'base.css']
	styleFiles += [self.stylesDir/'..'/'popup_bg.gif']
        styleFiles += self.stylesDir.files("*.css")
        if "nav.css" in styleFiles:
            styleFiles.remove("nav.css")
        styleFiles += self.stylesDir.files("*.jpg")
        styleFiles += self.stylesDir.files("*.gif")
        styleFiles += self.stylesDir.files("*.png")
        styleFiles += self.stylesDir.files("*.js")
        styleFiles += self.stylesDir.files("*.html")
        self.stylesDir.copylist(styleFiles, self.outputDir)
        package.resourceDir.copyfiles(self.outputDir)
        self.scriptsDir.copylist(('libot_drag.js', 'common.js'), 
                                     self.outputDir)
	
        hasVideoContainer = False
        hasMagnifier      = False
        hasXspfplayer     = False
	for idevice in self.page.node.idevices:
	    if (hasVideoContainer and hasMagnifier and hasXspfplayer):
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
            videofile.copyfile(self.outputDir/'videoContainer.swf')
        if hasMagnifier:
            videofile = (self.templatesDir/'magnifier.swf')
            videofile.copyfile(self.outputDir/'magnifier.swf')
        if hasXspfplayer:
            videofile = (self.templatesDir/'xspf_player.swf')
            videofile.copyfile(self.outputDir/'xspf_player.swf')
        (self.templatesDir/'fdl.html').copyfile(self.outputDir/'fdl.html')
