"""
FreeTextBlock can render and process FreeTextIdevices as XHTML
"""
import logging
from exe.webui.block            import Block
from exe.webui.element          import TextAreaElement
from exe.webui                     import common
log = logging.getLogger(__name__)
class FreeTextBlock(Block):
    """
    FreeTextBlock can render and process FreeTextIdevices as XHTML
    GenericBlock will replace it..... one day
    """
    def __init__(self, parent, idevice):
        Block.__init__(self, parent, idevice)
        if idevice.content.idevice is None: 
            idevice.content.idevice = idevice
        self.contentElement = TextAreaElement(idevice.content)
        self.contentElement.height = 250
        if not hasattr(self.idevice,'undo'): 
            self.idevice.undo = True
    def process(self, request):
        """
        Process the request arguments from the web server to see if any
        apply to this block
        """
        is_cancel = common.requestHasCancel(request)
        if is_cancel:
            self.idevice.edit = False
            if not hasattr(self.idevice.content, 'content_w_resourcePaths'):
                self.idevice.content.content_w_resourcePaths = ""
            if not hasattr(self.idevice.content, 'content_wo_resourcePaths'):
                self.idevice.content.content_wo_resourcePaths = ""
            return
        Block.process(self, request)
        if (u"action" not in request.args or 
            request.args[u"action"][0] != u"delete"): 
            content = self.contentElement.process(request) 
            if content: 
                self.idevice.content = content
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form element for editing this block
        """
        html  = u"<div>\n"
        html += self.contentElement.renderEdit()
        html += self.renderEditButtons()
        html += u"</div>\n"
        return html
    def renderPreview(self, style):
        """
        Returns an XHTML string for previewing this block
        """
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += u"ondblclick=\"submitLink('edit',"+self.id+", 0);\">\n"
        html += self.contentElement.renderPreview()
        html += self.renderViewButtons()
        html += "</div>\n"
        return html
    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block
        """
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        html += self.contentElement.renderView()
        html += u"</div>\n"
        return html
from exe.engine.freetextidevice import FreeTextIdevice
from exe.webui.blockfactory     import g_blockFactory
g_blockFactory.registerBlockType(FreeTextBlock, FreeTextIdevice)    
