"""
GenericBlock can render and process GenericIdevices as XHTML
"""
import logging
from exe.webui.block            import Block
from exe.webui.elementfactory   import g_elementFactory
from exe.webui                  import common
log = logging.getLogger(__name__)
class GenericBlock(Block):
    """
    GenericBlock can render and process GenericIdevices as XHTML
    """
    def __init__(self, parent, idevice):
        Block.__init__(self, parent, idevice)
        self.elements = []
        for field in self.idevice:
            self.elements.append(g_elementFactory.createElement(field))
    def process(self, request):
        """
        Process the request arguments from the web server
        """
        Block.process(self, request)
        if (u"action" not in request.args or
            request.args[u"action"][0] != u"delete"):
            for element in self.elements:
                element.process(request)
        if "title"+self.id in request.args:
            self.idevice.title = request.args["title"+self.id][0]
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form element for editing this block
        """
        html  = u'<div><div class="block">\n'
        html += common.textInput("title"+self.id, self.idevice.title) 
        html += u"</div>\n"
        for element in self.elements:
            html += element.renderEdit() + "<br/>"
        html += self.renderEditButtons()
        html += u"</div>\n"
        return html
    def renderPreview(self, style):
        """
        Returns an XHTML string for previewing this block during editing
        """
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += u"ondblclick=\"submitLink('edit', "+self.id+", 0);\">\n"
        if self.idevice.icon:
            html += u'<img alt="%s" ' % _(u'IDevice Icon')
            html += u'     class="iDevice_icon" '
	    html += u"src=\"/style/"+style
            html += u"/icon_"+self.idevice.icon+".gif\"/>\n"
	if self.idevice.emphasis > 0:
	    html += u"<span class=\"iDeviceTitle\">"
	    html += self.idevice.title
	    html += u"</span>\n"
        html += u"<div class=\"iDevice_inner\">\n"
        for element in self.elements:
            html += element.renderPreview()
            html += u"<br/>\n"
        html += self.renderViewButtons()
        html += u"</div>\n"
        html += u"</div>\n"
        return html
    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block, 
        i.e. when exported as a webpage or SCORM package
        """
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        if self.idevice.icon:
            html += u'<img alt="%s" ' % _(u'IDevice Icon')
            html += u'     class="iDevice_icon" '
            html += u"src=\"icon_"+self.idevice.icon+".gif\"/>\n"
        html += u"<span class=\"iDeviceTitle\">"
        html += self.idevice.title
        html += u"</span>\n"
        html += u"<div class=\"iDevice_inner\">\n"
        for element in self.elements:
            html += element.renderView()
            html += u"<br/>\n"
        html += u"</div>\n"
        html += u"</div>\n"
        return html
from exe.engine.genericidevice import GenericIdevice
from exe.webui.blockfactory    import g_blockFactory
g_blockFactory.registerBlockType(GenericBlock, GenericIdevice)    
