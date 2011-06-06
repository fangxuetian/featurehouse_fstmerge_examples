"""
RssBlock can render and process RssIdevices as XHTML
"""
import re
import logging
from exe.webui.block    import Block
from exe.webui          import common
from exe.webui.element  import TextAreaElement
from exe.engine.idevice import Idevice
log = logging.getLogger(__name__)
class RssBlock(Block):
    """
    RssBlock can render and process RssIdevices as XHTML
    """
    def __init__(self, parent, idevice):
        """
        Initialize
        """
        Block.__init__(self, parent, idevice)
        if idevice.rss.idevice is None: 
            idevice.rss.idevice = idevice
        self.rssElement = TextAreaElement(idevice.rss)
        self.rssElement.height = 300
    def process(self, request):
        """
        Process the request arguments from the web server to see if any
        apply to this block
        """
        log.debug("process " + repr(request.args))
        if 'emphasis'+self.id in request.args:
            self.idevice.emphasis = int(request.args['emphasis'+self.id][0])
        if 'site'+self.id in request.args:
            self.idevice.site = request.args['site'+self.id][0]
        if 'title'+self.id in request.args:
            self.idevice.title = request.args['title'+self.id][0]
        if "url" + self.id in request.args:
            self.idevice.url = request.args['url'+ self.id][0]
        if 'loadRss'+self.id in request.args:
            self.idevice.loadRss(request.args['url'+ self.id][0])
        else:
            Block.process(self, request)
            if (u"action" not in request.args or
                request.args[u"action"][0] != u"delete"):
                self.rssElement.process(request)
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form elements for editing this block
        """
        log.debug("renderEdit")
        html  = u"<div class=\"iDevice\"><br/>\n"
        html += common.textInput("title" + self.id, self.idevice.title)
        html += '<br/><br/>RSS URL ' + common.textInput("url" + self.id,
                                                        self.idevice.url)
        html += common.submitButton(u"loadRss"+self.id, _(u"Load"))
        html += common.elementInstruc(self.idevice.urlInstruc)
        html += u"<br/>\n"
        html += self.rssElement.renderEdit()
        emphasisValues = [(_(u"No emphasis"),     Idevice.NoEmphasis),
                          (_(u"Some emphasis"),   Idevice.SomeEmphasis)]
        this_package = None
        if self.idevice is not None and self.idevice.parentNode is not None:
            this_package = self.idevice.parentNode.package
        html += common.formField('select', this_package, _('Emphasis'),
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
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += u"ondblclick=\"submitLink('edit',"+self.id+", 0);\">\n"
        if self.idevice.emphasis != Idevice.NoEmphasis:
            if self.idevice.icon:
                html += u'<img alt="idevice icon" class="iDevice_icon" '
                html += u" src=\"/style/"+style
                html += "/icon_"+self.idevice.icon+".gif\"/>\n"
            html += u"<span class=\"iDeviceTitle\">"
            html += self.idevice.title
            html += u"</span>\n"
            html += u"<div class=\"iDevice_inner\">\n"
        html += self.rssElement.renderPreview()
        html += self.renderViewButtons()
        if self.idevice.emphasis != Idevice.NoEmphasis:
            html += u"</div></div>\n"
        else:
            html += u"</div>\n"
        return html
    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block
        """        
        log.debug("renderView")
        content = self.rssElement.renderView()
        content = re.sub(r'src="/.*?/resources/', 'src="', content)
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        html += u"<div class=\"rss_url\" value=\"" + self.idevice.url \
                + "\"></div>"
        if self.idevice.emphasis != Idevice.NoEmphasis:
            if self.idevice.icon:
                html += u'<img alt="iDevice icon" class="iDevice_icon" '
                html += u" src=\"icon_"+self.idevice.icon+".gif\"/>\n"
            html += u"<span class=\"iDeviceTitle\">"
            html += self.idevice.title
            html += u"</span>\n"
            html += u"<div class=\"iDevice_inner\">\n"
        html += content
        if self.idevice.emphasis != Idevice.NoEmphasis:
            html += u"</div></div>\n"
        else:
            html += u"</div>\n"
        return html
from exe.engine.rssidevice  import RssIdevice
from exe.webui.blockfactory import g_blockFactory
g_blockFactory.registerBlockType(RssBlock, RssIdevice)    
