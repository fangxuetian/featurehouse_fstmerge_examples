"""
ReflectionBlock can render and process ReflectionIdevices as XHTML
"""
import logging
from exe.webui.block               import Block
from exe.webui                     import common
log = logging.getLogger(__name__)
class ReflectionBlock(Block):
    """
    ReflectionBlock can render and process ReflectionIdevices as XHTML
    """
    def __init__(self, parent, idevice):
        """
        Initialize a new Block object
        """
        Block.__init__(self, parent, idevice)
        self.activity        = idevice.activity 
        self.answer          = idevice.answer
        self.activityInstruc = idevice.activityInstruc
        self.answerInstruc   = idevice.answerInstruc
    def process(self, request):
        """
        Process the request arguments from the web server
        """
        Block.process(self, request)
        if "activity"+self.id in request.args:
            self.idevice.activity = request.args["activity"+self.id][0]
        if "answer"+self.id in request.args:
            self.idevice.answer = request.args["answer"+self.id][0]
        if "title"+self.id in request.args:
            self.idevice.title = request.args["title"+self.id][0]
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form element for editing this block
        """
        html  = "<div class=\"iDevice\"><br/>\n"
        html += common.textInput("title"+self.id, self.idevice.title)
        html += common.formField('richTextArea',_(u'Reflective question:'),
                                 'activity', self.id, self.activityInstruc,
                                 self.activity)
        html += common.formField('richTextArea',_(u'Feedback:'),
                                 'answer', self.id, self.answerInstruc,
                                 self.answer)
        html += "<br/>" + self.renderEditButtons()
        html += "</div>\n"
        return html
    def renderViewContent(self):
        """
        Returns an XHTML string for this block
        """
        html  = u'<script type="text/javascript" src="common.js"></script>\n'
        html += u'<div class="iDevice_inner">\n'
        html += self.activity   
        html += '<div id="view%s" style="display:block;">' % self.id
        html += common.feedbackButton("btnshow"+self.id, _(u"Click here"),
                    onclick="showAnswer('%s',1)" % self.id)
        html += '</div>\n' 
        html += '<div id="hide%s" style="display:none;">' % self.id
        html += common.feedbackButton("btnshow"+self.id, _(u"Hide"),
                    onclick="showAnswer('%s',0)" % self.id)
        html += '</div>\n'
        html += '<div id="s%s" class="feedback" style=" ' % self.id
        html += 'display: none;">'
        html += self.answer
        html += "</div>\n"
        html += "</div>\n"
        return html
from exe.engine.reflectionidevice  import ReflectionIdevice
from exe.webui.blockfactory        import g_blockFactory
g_blockFactory.registerBlockType(ReflectionBlock, ReflectionIdevice)    
