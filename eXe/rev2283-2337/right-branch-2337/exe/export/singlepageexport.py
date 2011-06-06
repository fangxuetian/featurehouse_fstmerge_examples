"""
SinglePageExport will export a package as a website of HTML pages
"""
from cgi                      import escape
from exe.webui.blockfactory   import g_blockFactory
from exe.engine.error         import Error
from exe.engine.path          import Path
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
        if not self.outputDir.exists(): 
            self.outputDir.mkdir()
    def export(self, package):
        """ 
        Export web site
        Cleans up the previous packages pages and performs the export
        """
        self.style = package.style
        self.copyFiles(package)
        self.html  = self.renderHeader(package.name)
        self.html += u"<body>\n"
        self.html += u"<div id=\"content\">\n"
        self.html += u"<div id=\"header\">\n"
        self.html += escape(package.title)
        self.html += u"</div>\n"
        self.html += u"<div id=\"main\">\n"
        self.renderNode(package.root)
        self.html += u"</div>\n"
        self.html += u"</div>\n"
        self.html += u"</body></html>\n"
        self.save(self.outputDir/"index.html")
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
        self.templatesDir.copylist(('videoContainer.swf', 'magnifier.swf',
                                    'xspf_player.swf'),self.outputDir)
        (self.templatesDir/'fdl.html').copyfile(self.outputDir/'fdl.html')
    def renderHeader(self, name):
        """
        Returns an XHTML string for the header of this page.
        """
        html  = u"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        html += u'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 '
        html += u'Transitional//EN" '
        html += u'"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">\n'
        html += u"<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
        html += u"<head>\n"
        html += u"<style type=\"text/css\">\n"
        html += u"@import url(base.css);\n"
        html += u"@import url(content.css);\n"
        html += u"</style>"
        html += u"<title>"
        html += name
        html += "</title>\n"
        html += u"<meta http-equiv=\"Content-Type\" content=\"text/html; "
        html += u" charset=utf-8\" />\n";
        html += u'<script type="text/javascript" src="common.js"></script>\n'
        html += u"</head>\n"
        return html
    def renderNode(self, node):
        """
        Returns an XHTML string for this node and recurse for the children
        """
        self.html += '<div id=\"nodeDecoration\">'
        self.html += '<p id=\"nodeTitle\">'
        self.html += escape(node.titleLong)
        self.html += '</p></div>\n'
        for idevice in node.idevices:
            block = g_blockFactory.createBlock(None, idevice)
            if not block:
                log.critical("Unable to render iDevice.")
                raise Error("Unable to render iDevice.")
            if hasattr(idevice, "isQuiz"):
                self.html += block.renderJavascriptForWeb()
            self.html += block.renderView(self.style)
        for child in node.children:
            self.renderNode(child)
    def save(self, filename):
        """
        Save page to a file.  
        'outputDir' is the directory where the filenames will be saved
        (a 'path' instance)
        """
        outfile = open(filename, "w")
        outfile.write(self.html.encode('utf8'))
        outfile.close()
