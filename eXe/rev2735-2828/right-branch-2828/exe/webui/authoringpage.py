"""
AuthoringPage is responsible for creating the XHTML for the authoring
area of the eXe web user interface.  
"""
import logging
from twisted.web.resource    import Resource
from exe.webui               import common
from cgi                     import escape
import exe.webui.builtinblocks
from exe.webui.blockfactory  import g_blockFactory
from exe.engine.error        import Error
from exe.webui.renderable    import RenderableResource
log = logging.getLogger(__name__)
class AuthoringPage(RenderableResource):
    """
    AuthoringPage is responsible for creating the XHTML for the authoring
    area of the eXe web user interface.  
    """
    name = u'authoring'
    def __init__(self, parent):
        """
        Initialize
        'parent' is our MainPage instance that created us
        """
        RenderableResource.__init__(self, parent)
        self.blocks  = []
    def getChild(self, name, request):
        """
        Try and find the child for the name given
        """
        if name == "":
            return self
        else:
            return Resource.getChild(self, name, request)
    def _process(self, request):
        """
        Delegates processing of args to blocks
        """  
        log.debug(u"r3m0 DEBUG: start of _process(), just before calling parent's process of request..")
        log.debug(repr(request)) # also a r3m0 debug, to be removed...
        self.parent.process(request)
        log.debug(u"r3m0 DEBUG: in _process(), just after calling parent's process of request..")
        if ("action" in request.args and 
            request.args["action"][0] == u"saveChange"):
            log.debug(u"process savachange:::::")
            self.package.save()
            log.debug(u"package name: " + self.package.name)
        for block in self.blocks:
            log.debug(u"r3m0 DEBUG: in for-block of _process(), just before calling block's process of request..")
            block.process(request)
        log.debug(u"After authoringPage process" + repr(request.args))
    def render_GET(self, request=None):
        """
        Returns an XHTML string for viewing this page
        if 'request' is not passed, will generate psedo/debug html
        """
        log.debug(u"render_GET "+repr(request))
        if request is not None:
            for key, value in request.args.items():
                request.args[key] = [unicode(value[0], 'utf8')]
            self._process(request)
        topNode     = self.package.currentNode
        self.blocks = []
        self.__addBlocks(topNode)
        html  = self.__renderHeader()
        html += u'<body onload="onLoadHandler();">\n'
        html += u"<form method=\"post\" "
        if request is None:
            html += u'action="NO_ACTION"'
        else:
            html += u"action=\""+request.path+"#currentBlock\""
        html += u" id=\"contentForm\">"
        html += u'<div id="main">\n'
        html += common.hiddenField(u"action")
        html += common.hiddenField(u"object")
        html += common.hiddenField(u"isChanged", u"0")
        html += u'<!-- start authoring page -->\n'
        html += u'<div id="nodeDecoration">\n'
        html += u'<p id="nodeTitle">\n'
        html += escape(topNode.titleLong)
        html += u'</p>\n'
        html += u'</div>\n'
        for block in self.blocks:
            html += block.render(self.package.style)
        html += u'</div>\n'
        html += common.footer()
        html = html.encode('utf8')
        return html
    render_POST = render_GET
    def __renderHeader(self):
        """Generates the header for AuthoringPage"""
        html  = common.docType()
        html += u'<html xmlns="http://www.w3.org/1999/xhtml">\n'
        html += u'<head>\n'
        html += u'<style type="text/css">\n'
        html += u'@import url(/css/exe.css);\n'
        html += u'@import url(/style/base.css);\n'
        html += u'@import url(/style/%s/content.css);\n' % self.package.style
        html += u'</style>\n'
        html += u'<script type="text/javascript" src="/scripts/common.js">'
        html += u'</script>\n'
        html += u'<script type="text/javascript" '
        html += u'src="/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js">'
        html += u'</script>\n'
        html += u'<script type="text/javascript">\n'
        html += u'<!--\n'
        html += u"tinyMCE.init({   " 
        html += u"content_css : \"/css/extra.css\", \n"
        html += u"valid_elements : \"*[*]\",\n"
        html += u"verify_html : false, \n"
        html += u"apply_source_formatting : true, \n"
        html += u"gecko_spellcheck : true, \n"
        html += u" mode : \"textareas\",\n"
        html += u" editor_selector : \"mceEditor\",\n"
        html += u" plugins : \"table,save,advhr,advimage,advlink,emotions,media,"
        html += u" contextmenu,paste,directionality\","
        html += u" theme : \"advanced\",\n"
        html += u" theme_advanced_layout_manager : \"SimpleLayout\",\n"
        html += u"theme_advanced_toolbar_location : \"top\",\n"  
        html += u" theme_advanced_buttons1 : \"newdocument,separator,"
        html += u"bold,italic,underline,fontsizeselect,forecolor,separator,sub,sup,separator,"
        html += u"justifyleft,justifycenter,justifyright,justifyfull,"
        html += u"separator,bullist,numlist,indent,outdent,separator,"
        html += u"cut,copy,paste,pastetext,pasteword\",\n"
        html += u" theme_advanced_buttons2 : \"image,tablecontrols,separator,"
        html += u"link,unlink,separator,undo,redo,separator,"
        html += u" charmap,removeformat,cleanup,code,help\",\n"
        html += u"file_browser_callback : \"chooseImage_viaTinyMCE\",\n"
        html += u" theme_advanced_buttons3 : \"\",\n"
        html += u"theme_advanced_statusbar_location : \"bottom\",\n"
        html += u"    theme_advanced_resize_horizontal : false,\n"
        html += u"    theme_advanced_resizing : true\n"
        html += u" });\n"
        html += u"//-->\n"
        html += u"</script>\n"
        html += u'<script type="text/javascript" src="/scripts/libot_drag.js">'
        html += u'</script>\n'
        html += u'<title>"+_("eXe : elearning XHTML editor")+"</title>\n'
        html += u'<meta http-equiv="content-type" content="text/html; '
        html += u' charset=UTF-8" />\n'
        html += u'</head>\n'
        return html
    def __addBlocks(self, node):
        """
        Add All the blocks for the currently selected node
        """
        for idevice in node.idevices:
            block = g_blockFactory.createBlock(self, idevice)
            if not block:
                log.critical(u"Unable to render iDevice.")
                raise Error(u"Unable to render iDevice.")
            self.blocks.append(block)
