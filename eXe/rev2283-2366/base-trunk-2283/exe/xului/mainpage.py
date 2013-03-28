"""
This is the main XUL page.
"""
import os
import sys
import logging
import traceback
from twisted.internet            import reactor
from twisted.web                 import static
from nevow                       import loaders, inevow, stan
from nevow.livepage              import handler, js
from exe.xului.idevicepane       import IdevicePane
from exe.xului.outlinepane       import OutlinePane
from exe.xului.stylemenu         import StyleMenu
from exe.webui.renderable        import RenderableLivePage
from exe.xului.propertiespage    import PropertiesPage
from exe.webui.authoringpage     import AuthoringPage
from exe.export.websiteexport    import WebsiteExport
from exe.export.textexport       import TextExport
from exe.export.singlepageexport import SinglePageExport
from exe.export.scormexport      import ScormExport
from exe.export.imsexport        import IMSExport
from exe.engine.path             import Path
from exe.engine.package          import Package
log = logging.getLogger(__name__)
class MainPage(RenderableLivePage):
    """
    This is the main XUL page.  Responsible for handling URLs.
    """
    _templateFileName = 'mainpage.xul'
    name = 'to_be_defined'
    def __init__(self, parent, package):
        """
        Initialize a new XUL page
        'package' is the package that we look after
        """
        self.name = package.name
        RenderableLivePage.__init__(self, parent, package)
        self.putChild("resources", static.File(package.resourceDir))
        mainxul = Path(self.config.xulDir).joinpath('templates', 'mainpage.xul')
        self.docFactory  = loaders.xmlfile(mainxul)
        self.outlinePane = OutlinePane(self)
        self.idevicePane = IdevicePane(self)
        self.styleMenu   = StyleMenu(self)
        self.authoringPage  = AuthoringPage(self)
        self.propertiesPage = PropertiesPage(self)
    def getChild(self, name, request):
        """
        Try and find the child for the name given
        """
        if name == '':
            return self
        else:
            return super(self, self.__class__).getChild(self, name, request)
    def goingLive(self, ctx, client):
        """Called each time the page is served/refreshed"""
        inevow.IRequest(ctx).setHeader('content-type',
                                       'application/vnd.mozilla.xul+xml')
        def setUpHandler(func, name, *args, **kwargs):
            """
            Convience function link funcs to hander ids
            and store them
            """
            kwargs['identifier'] = name
            hndlr = handler(func, *args, **kwargs)
            hndlr(ctx, client) # Stores it
        setUpHandler(self.handleIsPackageDirty,  'isPackageDirty')
        setUpHandler(self.handlePackageFileName, 'getPackageFileName')
        setUpHandler(self.handleSavePackage,     'savePackage')
        setUpHandler(self.handleLoadPackage,     'loadPackage')
        setUpHandler(self.handleExport,          'exportPackage')
        setUpHandler(self.handleQuit,            'quit')
        setUpHandler(self.handleRegister,        'register')
        setUpHandler(self.handleReportIssue,     'reportIssue')
        setUpHandler(self.handleInsertPackage,   'insertPackage')
        setUpHandler(self.handleExtractPackage,  'extractPackage')
        setUpHandler(self.outlinePane.handleSetTreeSelection,  
                                                 'setTreeSelection')
        self.idevicePane.client = client
    def render_mainMenu(self, ctx, data):
        """Mac menubars are not shown
        so make it a toolbar"""
        if sys.platform[:6] == "darwin":
            ctx.tag.tagName = 'toolbar'
        return ctx.tag
    def render_addChild(self, ctx, data):
        """Fills in the oncommand handler for the 
        add child button and short cut key"""
        return ctx.tag(oncommand=handler(self.outlinePane.handleAddChild,
                       js('currentOutlineId()')))
    def render_delNode(self, ctx, data):
        """Fills in the oncommand handler for the 
        delete child button and short cut key"""
        return ctx.tag(oncommand=handler(self.outlinePane.handleDelNode,
                       js("confirmDelete()"),
                       js('currentOutlineId()')))
    def render_renNode(self, ctx, data):
        """Fills in the oncommand handler for the 
        rename node button and short cut key"""
        return ctx.tag(oncommand=handler(self.outlinePane.handleRenNode,
                       js('currentOutlineId()'),
                       js('askNodeName()'), bubble=True))
    def render_prePath(self, ctx, data):
        """Fills in the package name to certain urls in the xul"""
        request = inevow.IRequest(ctx)
        return ctx.tag(src=self.package.name + '/' + ctx.tag.attributes['src'])
    def _passHandle(self, ctx, name):
        """Ties up a handler for the promote, demote,
        up and down buttons. (Called by below funcs)"""
        attr = getattr(self.outlinePane, 'handle%s' % name)
        return ctx.tag(oncommand=handler(attr, js('currentOutlineId()')))
    def render_promote(self, ctx, data):
        """Fills in the oncommand handler for the 
        Promote button and shortcut key"""
        return self._passHandle(ctx, 'Promote')
    def render_demote(self, ctx, data):
        """Fills in the oncommand handler for the 
        Demote button and shortcut key"""
        return self._passHandle(ctx, 'Demote')
    def render_up(self, ctx, data):
        """Fills in the oncommand handler for the 
        Up button and shortcut key"""
        return self._passHandle(ctx, 'Up')
    def render_down(self, ctx, data):
        """Fills in the oncommand handler for the 
        Down button and shortcut key"""
        return self._passHandle(ctx, 'Down')
    def render_debugInfo(self, ctx, data):
        """Renders debug info to the top
        of the screen if logging is set to debug level
        """
        if log.getEffectiveLevel() == logging.DEBUG:
            request = inevow.IRequest(ctx)
            return stan.xml(('<hbox id="header">\n'
                             '    <label>%s</label>\n'
                             '    <label>%s</label>\n'
                             '</hbox>\n' %
                             (request.prepath, self.package.name)))
        else:
            return ''
    def handleIsPackageDirty(self, client, ifClean, ifDirty):
        """
        Called by js to know if the package is dirty or not.
        ifClean is JavaScript to be evaled on the client if the package has
        been changed 
        ifDirty is JavaScript to be evaled on the client if the package has not
        been changed
        """
        if self.package.isChanged:
            client.sendScript(ifDirty)
        else:
            client.sendScript(ifClean)
    def handlePackageFileName(self, client, onDone, onDoneParam):
        """
        Calls the javascript func named by 'onDone' passing as the
        only parameter the filename of our package. If the package
        has never been saved or loaded, it passes an empty string
        'onDoneParam' will be passed to onDone as a param after the
        filename
        """
        client.call(onDone, unicode(self.package.filename), onDoneParam)
    def handleSavePackage(self, client, filename=None, onDone=None):
        """
        Save the current package
        'filename' is the filename to save the package to
        'onDone' will be evaled after saving instead or redirecting
        to the new location (in cases of package name changes).
        (This is used where the user goes file|open when their 
        package is changed and needs saving)
        """
        filename = Path(filename)
        saveDir  = filename.dirname()
        if saveDir and not saveDir.exists():
            client.alert(_(u'Cannot access directory named ') +
                         unicode(saveDir) +
                         _(u'. Please use ASCII names.'))
            return
        oldName = self.package.name
        if not filename:
            filename = self.package.filename
            assert (filename, 
                    ('Somehow save was called without a filename '
                     'on a package that has no default filename.'))
        if not filename.lower().endswith('.elp'):
            filename += '.elp'
            if Path(filename).exists():
                client.alert(_(u'SAVE FAILED.\n"%s" already exists.\n'
                                'Please try again with '
                                'a different filename') % filename)
                return
        self.package.save(filename) # This can change the package name
        client.alert(_(u'Package saved to: %s' % filename))
        if onDone:
            client.sendScript(onDone)
        elif self.package.name != oldName:
            self.webServer.root.putChild(self.package.name, self)
            log.info('Package saved, redirecting client to /%s'
                     % self.package.name)
            client.sendScript('top.location = "/%s"' % \
                              self.package.name.encode('utf8'))
    def handleLoadPackage(self, client, filename):
        """Load the package named 'filename'"""
        package = self._loadPackage(client, filename)
        packageStore = self.webServer.application.packageStore
        packageStore.addPackage(package)
        self.root.bindNewPackage(package)
        client.sendScript((u'top.location = "/%s"' % \
                          package.name).encode('utf8'))
    def handleExport(self, client, exportType, filename):
        """
        Called by js. 
        Exports the current package to one of the above formats
        'exportType' can be one of 'singlePage' 'webSite' 'zipFile'
                     'textFile' or 'scorm'
        'filename' is a file for scorm pages, and a directory for websites
        """ 
        webDir     = Path(self.config.webDir)
        stylesDir  = webDir.joinpath('style', self.package.style)
        exportDir  = Path(filename).dirname()
        if exportDir and not exportDir.exists():
            client.alert(_(u'Cannot access directory named ') +
                         unicode(exportDir) +
                         _(u'. Please use ASCII names.'))
            return
        if exportType == 'singlePage':
            self.exportSinglePage(client, filename, webDir, stylesDir)
        elif exportType == 'webSite':
            self.exportWebSite(client, filename, stylesDir)
        elif exportType == 'zipFile':
            self.exportWebZip(client, filename, stylesDir)
        elif exportType == 'textFile':
            self.exportText(client, filename)
        elif exportType == "scorm":
            self.exportScorm(client, filename, stylesDir, "scorm1.2")
        elif exportType == "scorm2004":
            self.exportScorm(client, filename, stylesDir, "scorm2004")
        else:
            self.exportIMS(client, filename, stylesDir)
    def handleQuit(self, client):
        """
        Stops the server
        """
        reactor.stop()
    def handleRegister(self, client):
        """Go to the exelearning.org/register.php site"""
        if hasattr(os, 'startfile'):
            os.startfile("http://exelearning.org/register.php")
        elif sys.platform[:6] == "darwin":
            import webbrowser
            webbrowser.open("http://exelearning.org/register.php", new=True)
        else:
            os.system("firefox http://exelearning.org/register.php&")
    def handleReportIssue(self, client):
        """Go to the exelearning.org issue tracker"""
        if hasattr(os, 'startfile'):
            os.startfile("http://exelearning.org/issue.php")
        elif sys.platform[:6] == "darwin":
            import webbrowser
            webbrowser.open("http://exelearning.org/issue.php", new=True)
        else:
            os.system("firefox http://exelearning.org/issue.php&")
    def handleInsertPackage(self, client, filename):
        """
        Load the package and insert in current node
        """
        package = self._loadPackage(client, filename)
        insertNode = package.root
        insertNode.mergeIntoPackage(self.package)
        insertNode.move(self.package.currentNode)
        client.sendScript((u'top.location = "/%s"' % \
                          self.package.name).encode('utf8'))
    def handleExtractPackage(self, client, filename):
        """
        Create a new package consisting of the current node and export
        """
        filename  = Path(filename)
        saveDir = filename.dirname()
        if saveDir and not saveDir.exists():
            client.alert(_(u'Cannot access directory named ') +
                         unicode(saveDir) +
                         _(u'. Please use ASCII names.'))
            return
        if not filename.lower().endswith('.elp'):
            filename += '.elp'
        if Path(filename).exists():
            client.alert(_(u'EXPORT FAILED.\n"%s" already exists.\n'
                            'Please try again with '
                            'a different filename') % filename)
            return
        package = Package(filename.namebase)
        package.style  = self.package.style
        package.author = self.package.author
        extractNode  = self.package.currentNode.clone()
        extractNode.mergeIntoPackage(package)
        package.root = package.currentNode = extractNode
        package.save(filename)
        client.alert(_(u'Package saved to: %s' % filename))
    def exportSinglePage(self, client, filename, webDir, stylesDir):
        """
        Export 'client' to a single web page,
        'webDir' is just read from config.webDir
        'stylesDir' is where to copy the style sheet information from
        """
        imagesDir    = webDir.joinpath('images')
        scriptsDir   = webDir.joinpath('scripts')
        templatesDir = webDir.joinpath('templates')
        filename = Path(filename)
        if filename.basename() != self.package.name:
            filename /= self.package.name
        if not filename.exists():
            filename.makedirs()
        elif not filename.isdir():
            client.alert(_(u'Filename %s is a file, cannot replace it') % 
                         filename)
            log.error("Couldn't export web page: "+
                      "Filename %s is a file, cannot replace it" % filename)
            return
        else:
            try:
                filename.rmtree()
                filename.mkdir()
            except Exception, e:
                client.alert(_('There was an error in the export:\n%s') % str(e))
                return
        singlePageExport = SinglePageExport(stylesDir, filename, 
                                            imagesDir, scriptsDir, templatesDir)
        singlePageExport.export(self.package)
        self._startFile(filename)
    def exportWebSite(self, client, filename, stylesDir):
        """
        Export 'client' to a web site,
        'webDir' is just read from config.webDir
        'stylesDir' is where to copy the style sheet information from
        """
        filename = Path(filename)
        if filename.basename() != self.package.name:
            filename /= self.package.name
        if not filename.exists():
            filename.makedirs()
        elif not filename.isdir():
            client.alert(_(u'Filename %s is a file, cannot replace it') % 
                         filename)
            log.error("Couldn't export web page: "+
                      "Filename %s is a file, cannot replace it" % filename)
            return
        else:
            try:
                filename.rmtree()
                filename.mkdir()
            except Exception, e:
                client.alert(_('There was an error in the export:\n%s') % str(e))
                return
        websiteExport = WebsiteExport(self.config, stylesDir, filename)
        websiteExport.export(self.package)
        self._startFile(filename)
    def exportWebZip(self, client, filename, stylesDir):
        filename = Path(filename)
        log.debug(u"exportWebsite, filename=%s" % filename)
        if not filename.ext.lower() == '.zip': 
            filename += '.zip'
        if filename.exists(): 
            filename.remove()
        websiteExport = WebsiteExport(self.config, stylesDir, filename)
        websiteExport.exportZip(self.package)
        client.alert(_(u'Exported to %s') % filename)
    def exportText(self, client, filename):
        filename = Path(filename)
        log.debug(u"exportWebsite, filename=%s" % filename)
        if not filename.ext.lower() == '.txt': 
            filename += '.txt'
        if filename.exists(): 
            filename.remove()
        textExport = TextExport(filename)
        textExport.export(self.package)
        client.alert(_(u'Exported to %s') % filename)
    def exportScorm(self, client, filename, stylesDir, scormType):
        """
        Exports this package to a scorm package file
        """
        filename = Path(filename)
        log.debug(u"exportScorm, filename=%s" % filename)
        if not filename.ext.lower() == '.zip': 
            filename += '.zip'
        if filename.exists(): 
            filename.remove()
        scormExport = ScormExport(self.config, stylesDir, filename, scormType)
        scormExport.export(self.package)
        client.alert(_(u'Exported to %s') % filename)
    def exportIMS(self, client, filename, stylesDir):
        """
        Exports this package to a ims package file
        """
        log.debug(u"exportIMS")
        if not filename.lower().endswith('.zip'): 
            filename += '.zip'
        filename = Path(filename)
        if filename.exists(): 
            filename.remove()
        imsExport = IMSExport(self.config, stylesDir, filename)
        imsExport.export(self.package)
        client.alert(_(u'Exported to %s' % filename))
    def _startFile(self, filename):
        """
        Launches an exported web site or page
        """
        if hasattr(os, 'startfile'):
            try:
                os.startfile(filename)
            except UnicodeEncodeError:
                os.startfile(filename.encode(Path.fileSystemEncoding))
        elif sys.platform[:6] == "darwin":
            import webbrowser
            filename /= 'index.html'
            webbrowser.open('file://'+filename)
        else:
            filename /= 'index.html'
            log.debug(u"firefox file://"+filename+"&")
            os.system("firefox file://"+filename+"&")
    def _loadPackage(self, client, filename):
        """Load the package named 'filename'"""
        try:
            encoding = sys.getfilesystemencoding()
            if encoding is None:
                encoding = 'utf-8'
            filename2 = unicode(filename, encoding)
            log.debug("filename and path" + filename2)
            package = Package.load(filename2)
            if package is None:
                filename2 = unicode(filename, 'utf-8')
                package = Package.load(filename2)
                if package is None:
                    raise Exception(_("Couldn't load file, please email file to bugs@exelearning.org"))
        except Exception, exc:
            if log.getEffectiveLevel() == logging.DEBUG:
                client.alert(_(u'Sorry, wrong file format:\n%s') % unicode(exc))
            else:
                client.alert(_(u'Sorry, wrong file format'))
            log.error(_(u'Error loading package "%s": %s') % \
                      (filename2, unicode(exc)))
            log.error((u'Traceback:\n%s' % traceback.format_exc()))
            raise
        return package
