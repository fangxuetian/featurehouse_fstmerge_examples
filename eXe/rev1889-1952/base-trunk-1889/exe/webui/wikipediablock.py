"""
WikipediaBlock can render and process WikipediaIdevices as XHTML
"""
import re
import logging
from exe.webui.block   import Block
from exe.webui         import common
from exe.webui.element import TextAreaElement
from exe.engine.idevice   import Idevice
log = logging.getLogger(__name__)
class WikipediaBlock(Block):
    """
    WikipediaBlock can render and process WikipediaIdevices as XHTML
    """
    def __init__(self, parent, idevice):
        """
        Initialize
        """
        Block.__init__(self, parent, idevice)
        self.articleElement = TextAreaElement(idevice.article)
        self.articleElement.height = 300
    def process(self, request):
        """
        Process the request arguments from the web server to see if any
        apply to this block
        """
        log.debug("process " + repr(request.args))
        if u"emphasis"+self.id in request.args:
            self.idevice.emphasis = int(request.args["emphasis"+self.id][0])
        if u"loadWikipedia"+self.id in request.args:
            self.idevice.site = request.args["site"][0]
            self.idevice.loadArticle(request.args["article"][0])
        else:
            Block.process(self, request)
            if (u"action" not in request.args or
                request.args[u"action"][0] != u"delete"):
                self.articleElement.process(request)
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form elements for editing this block
        """
        log.debug("renderEdit")
        html  = u"<div class=\"iDevice\"><br/>\n"
        html += common.textInput("title"+self.id, self.idevice.title)
        sites = [(_(u"English Wikipedia Article"), "http://en.wikipedia.org/"),
                 (_(u"Chinese Wikipedia Article"), "http://zh.wikipedia.org/"),
                 (_(u"German Wikipedia Article"),  "http://de.wikipedia.org/"),
                 (_(u"French Wikipedia Article"),  "http://fr.wikipedia.org/"),
                 (_(u"Japanese Wikipedia Article"),"http://ja.wikipedia.org/"),
                 (_(u"Italian Wikipedia Article"), "http://il.wikipedia.org/"),
                 (_(u"Polish Wikipedia Article"),  "http://pl.wikipedia.org/"),
                 (_(u"Dutch Wikipedia Article"),   "http://nl.wikipedia.org/"),
                 (_(u"Portugese Wikipedia Article"),
                                                   "http://pt.wikipedia.org/"),
                 (_(u"Spanish Wikipedia Article"), "http://es.wikipedia.org/"),
                 (_(u"Swedish Wikipedia Article"), "http://sv.wikipedia.org/"),
                 (_(u"Wikibooks Article"),         "http://en.wikibooks.org/"),
                 (_(u"Wikieducator Content"),      "http://wikieducator.org/")]
        html += common.formField('select', _('Site'),
                                 'site', self.id,
                                 self.idevice.langInstruc,
                                 sites,
                                 self.idevice.site)
        html += common.textInput("article", self.idevice.articleName)
        html += common.elementInstruc(self.idevice.searchInstruc)
        html += common.submitButton(u"loadWikipedia"+self.id, _(u"Load"))
        html += u"<br/>\n"
        html += self.articleElement.renderEdit()
        emphasisValues = [(_(u"No emphasis"),     Idevice.NoEmphasis),
                          (_(u"Some emphasis"),   Idevice.SomeEmphasis),
                          (_(u"Strong emphasis"), Idevice.StrongEmphasis)]
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
        html += self.articleElement.renderPreview()
        html += u"<br/>\n"
        html += u"This article is licensed under the "
        html += u'<span style="text-decoration: underline;">GNU Free Documentation License</span>. It uses material '
        html += u'from the <span style="text-decoration: underline;">article '
        html += u'"%s"</span>.<br/>\n' % self.idevice.articleName
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
        content = self.articleElement.renderView()
        content = re.sub(r'src="/.*?/resources/', 'src="', content)
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        if self.idevice.emphasis != Idevice.NoEmphasis:
            if self.idevice.icon:
                html += u'<img alt="iDevice icon" class="iDevice_icon" '
                html += u" src=\"icon_"+self.idevice.icon+".gif\"/>\n"
            html += u"<span class=\"iDeviceTitle\">"
            html += self.idevice.title
            html += u"</span>\n"
            html += u"<div class=\"iDevice_inner\">\n"
        html += content
        html += u"<br/>\n"
        html += _(u"This article is licensed under the ")
        html += u"<a "
        html += u"href=\"javascript:window.open('fdl.html')\">"
        html += u"%s</a>. " % _(u"GNU Free Documentation License")
        html += _(u"It uses material from the ")
        html += u"<a href=\""+self.idevice.site+u"wiki/"
        html += self.idevice.articleName+u"\">"
        html += _(u"article ") + u"\""+self.idevice.articleName+u"\"</a>.<br/>\n"
        if self.idevice.emphasis != Idevice.NoEmphasis:
            html += u"</div></div>\n"
        else:
            html += u"</div>\n"
        return html
from exe.engine.wikipediaidevice import WikipediaIdevice
from exe.webui.blockfactory      import g_blockFactory
g_blockFactory.registerBlockType(WikipediaBlock, WikipediaIdevice)    
