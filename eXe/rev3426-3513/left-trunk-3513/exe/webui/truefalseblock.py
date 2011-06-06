"""
TrueFalseBlock can render and process TrueFalseIdevices as XHTML
"""
import logging
from exe.webui.block               import Block
from exe.webui.truefalseelement    import TrueFalseElement
from exe.webui                     import common
from exe.webui.element             import TextAreaElement
log = logging.getLogger(__name__)
class TrueFalseBlock(Block):
    """
    TrueFalseBlock can render and process TrueFalseIdevices as XHTML
    """
    def __init__(self, parent, idevice):
        """
        Initialize a new Block object
        """
        Block.__init__(self, parent, idevice)
        self.idevice         = idevice
        self.questionElements  = []
        self.questionInstruc = idevice.questionInstruc
        self.keyInstruc      = idevice.keyInstruc
        self.feedbackInstruc = idevice.feedbackInstruc
        self.hintInstruc     = idevice.hintInstruc 
        if idevice.instructionsForLearners.idevice is None: 
            idevice.instructionsForLearners.idevice = idevice
        self.instructionElement = \
            TextAreaElement(idevice.instructionsForLearners)
        if not hasattr(self.idevice,'undo'):
            self.idevice.undo = True
        i = 0
        for question in idevice.questions:
            self.questionElements.append(TrueFalseElement(i, idevice, 
                                                           question))
            i += 1
    def process(self, request):
        """
        Process the request arguments from the web server
        """
        Block.process(self, request)
        is_cancel = common.requestHasCancel(request)
        self.instructionElement.process(request)
        if ("addQuestion"+unicode(self.id)) in request.args: 
            self.idevice.addQuestion()
            self.idevice.edit = True
            self.idevice.undo = False
        if "title"+self.id in request.args \
        and not is_cancel:
            self.idevice.title = request.args["title"+self.id][0]
        for element in self.questionElements:
            element.process(request)
        if ("action" in request.args and request.args["action"][0] == "done" 
        or not self.idevice.edit): 
            if hasattr(self.idevice,'undo'): 
                del self.idevice.undo
    def renderEdit(self, style):
        """
        Returns an XHTML string with the form element for editing this block
        """
        html  = u"<div class=\"iDevice\"><br/>\n"
        html += common.textInput("title"+self.id, self.idevice.title)
        html += u"<br/><br/>\n"
        html += self.instructionElement.renderEdit()
        for element in self.questionElements:
            html += element.renderEdit() 
        value = _(u"Add another question")    
        html += common.submitButton("addQuestion"+unicode(self.id), value)
        html += u"<br /><br />" + self.renderEditButtons(undo=self.idevice.undo)
        html += u"</div>\n"
        return html
    def renderPreview(self, style):
        """
        Returns an XHTML string for previewing this block
        """
        html  = u"<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\" "
        html += u"ondblclick=\"submitLink('edit',"+self.id+", 0);\">\n"
        html += u'<img alt="" class="iDevice_icon" '
        html += u"src=\"/style/"+style+"/icon_"+self.idevice.icon+".gif\" />\n"
        html += u"<span class=\"iDeviceTitle\">"       
        html += self.idevice.title+"</span><br/>\n"     
        html += u"<div class=\"iDevice_inner\">\n"
        html += self.instructionElement.renderPreview()
        for element in self.questionElements:
            html += element.renderQuestionPreview()
            html += element.renderFeedbackPreview()
        html += "</div>\n"    
        html += self.renderViewButtons()
        html += "</div>\n"
        return html
    def renderView(self, style):
        """
        Returns an XHTML string for viewing this block
        """
        html  = u'<script type="text/javascript" src="common.js"></script>\n'
        html += u'<script type="text/javascript" src="libot_drag.js"></script>'
        html += u"\n<div class=\"iDevice "
        html += u"emphasis"+unicode(self.idevice.emphasis)+"\">\n"
        html += u'<img alt="" class="iDevice_icon" '
        html += u"src=\"icon_"+self.idevice.icon+".gif\" />\n"
        html += u"<span class=\"iDeviceTitle\">"       
        html += self.idevice.title+"</span><br/>\n"
        html += u"<div class=\"iDevice_inner\">\n"
        html += self.instructionElement.renderView()
        for element in self.questionElements:
            html += element.renderQuestionView()
            html += element.renderFeedbackView()
        html += u"</div>\n"    
        html += u"</div>\n"
        return html
from exe.engine.truefalseidevice   import TrueFalseIdevice
from exe.webui.blockfactory        import g_blockFactory
g_blockFactory.registerBlockType(TrueFalseBlock, TrueFalseIdevice)    
