"""
MultimediaBlock can render and process MultimediaIdevices as XHTML
"""
import logging
from exe.webui.block    import Block
from exe.webui.element  import TextAreaElement, MultimediaElement
from exe.webui          import common
from exe.engine.idevice import Idevice
log = logging.getLogger(__name__)
class MultimediaBlock(Block):
    """
    ImageWithTextBlock can render and process ImageWithTextIdevices as XHTML
    """
    name = 'MultimediaWithText'
    def __init__(self, parent, idevice):
        """
        Initialize
        """
        Block.__init__(self, parent, idevice)
        self.mediaElement = MultimediaElement(idevice.media)
        self.textElement  = TextAreaElement(idevice.text)
    def process(self, request):
        """
        Process the request arguments from the web server to see if any
        apply to this block
        """
        log.debug("process " + repr(request.args))
        Block.process(self, request)
        if (u"action" not in request.args or
            request.args[u"action"][0] != u"delete"):
            self.mediaElement.process(request)
            self.textElement.process(request)
        if 'emphasis'+self.id in request.args:
            self.idevice.emphasis = int(request.args['emphasis'+self.id][0])
        if "float"+self.id in request.args:
            self.idevice.float = request.args["float"+self.id][0]
        if "title"+self.id in request.args:
            self.idevice.title = request.args["title"+self.id][0]
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form elements for editing this block
        """
        log.debug("renderEdit")
        html  = u"<div class=\"iDevice\">\n"
        html += common.textInput("title"+self.id, self.idevice.title) + '<br/><br/>'
        html += self.mediaElement.renderEdit()       
        floatArr    = [[_(u'Left'), 'left'],
                      [_(u'Right'), 'right'],
                      [_(u'None'),  'none']]
        html += common.formField('select', _("Align:"),
                                 "float" + self.id, '',
                                 self.idevice.alignInstruc,
                                 floatArr, self.idevice.float)
        html += "<br/>" + self.textElement.renderEdit()
        emphasisValues = [(_(u"No emphasis"),     Idevice.NoEmphasis),
                          (_(u"Some emphasis"),   Idevice.SomeEmphasis)]
        html += common.formField('select', _('Emphasis'),
                                 'emphasis', self.id, 
                                 '', # TODO: Instructions
                                 emphasisValues,
                                 self.idevice.emphasis)
        html += self.renderEditButtons()
        html += u"</div>\n"
        return html
    def renderPreview(self, style):
        """
        Returns an XHTML string for previewing this block
        """
        log.debug("renderPreview")
        html  = u"\n<!-- MP3 iDevice -->\n"
        html += u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += "ondblclick=\"submitLink('edit',"+self.id+", 0);\">\n"
        if self.idevice.emphasis != Idevice.NoEmphasis:
            if self.idevice.icon:
                html += u'<img alt="idevice icon" class="iDevice_icon" '
                html += u" src=\"/style/"+style
                html += "/icon_"+self.idevice.icon+".gif\"/>\n"
            html += u"<span class=\"iDeviceTitle\">"
            html += self.idevice.title
            html += u"</span>\n"
        html += u"<div class=\"iDevice_inner\"> "
        html += u"<div class=\"media\">\n"
        html += self.mediaElement.renderPreview()+ "</div>\n"
        html += self.textElement.renderPreview()
        html += u"<br/>\n"        
        html += u"<div style=\"clear:both;\">"
        html += u"</div></div>\n"
        html += self.renderViewButtons()
        html += u"</div>\n"
        return html
    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block
        """        
        log.debug("renderView")
        html  = u"\n<!-- MP3 iDevice -->\n"
        html += u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        if self.idevice.emphasis != Idevice.NoEmphasis:
            if self.idevice.icon:
                html += u'<img alt="idevice icon" class="iDevice_icon" '
                html += u' src="icon_'+self.idevice.icon+'.gif"/>\n'
            html += u"<span class=\"iDeviceTitle\">"
            html += self.idevice.title
            html += u"</span>\n"
        html += u"<div class=\"iDevice_inner\"> "
        html += u"<div class=\"media\">\n" + "</div>"
        html += self.mediaElement.renderView()
        html += self.textElement.renderView()
        html += u"<div style=\"clear:both;\">"
        html += u"</div></div>\n"
        html += u"</div>\n"
        return html
from exe.engine.multimediaidevice import MultimediaIdevice
from exe.webui.blockfactory       import g_blockFactory
g_blockFactory.registerBlockType(MultimediaBlock, MultimediaIdevice)    
