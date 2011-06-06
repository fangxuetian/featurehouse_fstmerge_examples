"""
ImageMagnifierBlock can render and process ImageMagnifierIdevices as XHTML
"""
import logging
from exe.webui.block   import Block
from exe.webui.element import TextAreaElement, MagnifierElement
from exe.webui         import common
log = logging.getLogger(__name__)
class ImageMagnifierBlock(Block):
    """
    ImageMagnifierBlock can render and process ImageMagnifierIdevices as XHTML
    """
    name = 'imageManifier'
    def __init__(self, parent, idevice):
        """
        Initialize
        """
        Block.__init__(self, parent, idevice)
        self.imageMagnifierElement = MagnifierElement(idevice.imageMagnifier)
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
            self.imageMagnifierElement.process(request)
            self.textElement.process(request)
        if "float"+self.id in request.args:
            self.idevice.float = request.args["float"+self.id][0]
        if "caption"+self.id in request.args:
            self.idevice.caption = request.args["caption"+self.id][0]
        if "glass"+self.id in request.args:
            self.idevice.imageMagnifier.glassSize = \
                request.args["glass"+self.id][0]
        if "initial"+self.id in request.args:
            if request.args["initial"+self.id][0] == "yes":
                self.idevice.imageMagnifier.initialZSize = "120"
            else:
                self.idevice.imageMagnifier.initialZSize ="100"
        if "maxZoom"+self.id in request.args:
            self.idevice.imageMagnifier.maxZSize = \
                request.args["maxZoom"+self.id][0] 
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form elements for editing this block
        """
        log.debug("renderEdit")
        floatArr        = [[_(u'Left'), 'left'],
                          [_(u'Right'), 'right'],
                          [_(u'None'),  'none']]
        glassSizeArr    = [[_(u'Small'), '1'],
                          [_(u'Medium'),'2'],
                          [_(u'Large'),'3'],
                          [_(u'Extra large'),'4'],]
        html  = u"<div class=\"iDevice\">\n"
        html += self.imageMagnifierElement.renderEdit()       
        html += common.formField('select', _("Align:"),
                                 "float" + self.id, '',
                                 self.idevice.dimensionInstruc,
                                 floatArr, self.idevice.float)
        html += '<div class="block">'
        html += u"<b>%s </b>" % _(u"Caption:")
        html += common.textInput("caption" + self.id, self.idevice.caption)
        html += common.elementInstruc(self.idevice.captionInstruc)
        html += '</div>'
        html += '<div class="block">'
        html += self.textElement.renderEdit()
        html += '</div>'
        html += common.formField('select', _(u"Size of magnifying glass: "),
                                 "glass" + self.id,
                                 options = glassSizeArr, 
                                 selection = \
                                 self.idevice.imageMagnifier.glassSize)
        zoomOpts = []
        selected = ''
        for i in range(10, 21):
            if str(i)+ "0" == self.idevice.imageMagnifier.maxZSize:
                selected = str(i)
            zoomOpts.append((str(i), str(i)))
        html += common.formField('select', _(u"Maximum zoom"),
                                 "maxZoom" + self.id,
                                 options = zoomOpts, 
                                 selection = selected)
        html += '<div class="block">'
        html += "<b>" +_(u"Magnify initial zoom? ")  + "</b>"
        html += _(u"Yes")
        if self.idevice.imageMagnifier.initialZSize == "100":
            html += common.option("initial"+self.id, 0, "yes")
            html += _(u"No")
            html += common.option("initial"+self.id, 1, "no")
        else:
            html += common.option("initial"+self.id, 1, "yes")
            html += _(u"No")
            html += common.option("initial"+self.id, 0, "no")
        html += common.formField('select', _(u"Maximum zoom"),
                                 "maxZoom", self.id,
                                 '',
                                 glassSizeArr, 
                                 self.idevice.imageMagnifier.glassSize)
        html += "<b>" +_(u"Magnify initial zoom? ")  + "</b>"
        html += _(u"Yes")
        if self.idevice.imageMagnifier.initialZSize == "100":
            html += common.option("initial"+self.id, 0, "yes")
            html += _(u"No")
            html += common.option("initial"+self.id, 1, "no")
        else:
            html += common.option("initial"+self.id, 1, "yes")
            html += _(u"No")
            html += common.option("initial"+self.id, 0, "no")
        html += '</div>'
        html += self.renderEditButtons()
        html += u"</div>\n"
        return html
    def renderPreview(self, style):
        """
        Returns an XHTML string for previewing this block
        """
        log.debug("renderPreview")
        html  = u"\n<!-- image with text iDevice -->\n"
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += "ondblclick=\"submitLink('edit',"+self.id+", 0);\">\n"
        html += u"  <div class=\"image_text\" style=\""
        html += u"width:" + str(self.idevice.imageMagnifier.width) + "px; "
        html += u"float:%s;\">\n" % self.idevice.float
        html += u"    <div class=\"image\">\n"
        html += self.imageMagnifierElement.renderPreview()
        html += u"" + self.idevice.caption
        html += u"    </div> <!-- class=\"image\" -->\n" 
        html += u"  </div> <!-- class=\"image_text\" -->\n" 
        text = self.textElement.renderPreview()
        if text:
            html += text
        else:
            html += '&nbsp;'
        html += u'\n<div style="clear:both;height:1px;overflow:hidden;"></div>\n'
        html += self.renderViewButtons()
        html += u"</div> <!-- class=\"iDevice emphasisX\" -->\n" 
        return html
    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block
        """        
        log.debug("renderView")
        html  = u"\n<!-- image with text iDevice -->\n"
        html += u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        html += u"  <div class=\"image_text\" style=\""
        html += u"width:" + str(self.idevice.imageMagnifier.width) + "px; "
        html += u"float:%s;\">\n" % self.idevice.float
        html += u"    <div class=\"image\">\n"
        html += self.imageMagnifierElement.renderView()
        html += u"    <br/>" + self.idevice.caption
        html += u"    </div> <!-- class=\"image\" -->\n" 
        html += u"  </div> <!-- class=\"image_text\" -->\n" 
        text = self.textElement.renderView()
        if text:
            html += text
        else:
            html += '&nbsp;'
        html += u'\n<div style="clear:both;height:1px;overflow:hidden;"></div>\n'
        html += u'\n<div style="clear:both;"></div>\n'
        html += u"</div> <!-- class=\"iDevice emphasisX\" -->\n" 
        return html
from exe.engine.imagemagnifieridevice import ImageMagnifierIdevice
from exe.webui.blockfactory           import g_blockFactory
g_blockFactory.registerBlockType(ImageMagnifierBlock, ImageMagnifierIdevice)    
