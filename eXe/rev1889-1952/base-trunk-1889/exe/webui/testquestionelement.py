"""
TestQuestionElement is responsible for a block of question.  
Used by QuizTestBlock
"""
import logging
from exe.webui.testoptionelement   import TestoptionElement
from exe.webui                     import common
log = logging.getLogger(__name__)
class TestquestionElement(object):
    """
    TestQuestionElement is responsible for a block of question.  
    Used by QuizTestBlock
    """
    def __init__(self, index, idevice, question):
        """
        Initialize
        """
        self.index      = index
        self.id         = unicode(index) + "b" + idevice.id        
        self.idevice    = idevice
        self.question   = question
        self.options    = []
        self.keyId      = "key" + self.id
        i = 0
        for option in question.options:
            self.options.append(TestoptionElement(i,
                                                  question, 
                                                  self.id, 
                                                  option,
                                                  idevice))
            i += 1
    def process(self, request):
        """
        Process the request arguments from the web server
        """
        log.info("process " + repr(request.args))
        questionId = "question"+self.id
        if questionId in request.args:
            self.question.question = request.args[questionId][0]
        if ("addOption"+unicode(self.id)) in request.args: 
            self.question.addOption()
            self.idevice.edit = True
        if "action" in request.args and request.args["action"][0] == self.id:
            self.idevice.questions.remove(self.question)
        for element in self.options:
            element.process(request)
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this element
        """
        html  = u"<div class=\"iDevice\">\n"
        html += u"<b>" + _("Question:") + " </b>" 
        html += common.elementInstruc(self.question.questionInstruc)
        html += u" " + common.submitImage(self.id, self.idevice.id, 
                                   "/images/stock-cancel.png",
                                   _("Delete question"))
        html += common.richTextArea("question"+self.id, 
                                    self.question.question)
        html += u"<table width =\"100%%\">"
        html += u"<thead>"
        html += u"<tr>"
        html += u"<th>%s " % _("Options")
        html += common.elementInstruc(self.question.optionInstruc)
        html += u"</th><th align=\"left\">%s "  % _("Correct")
        html += common.elementInstruc(self.question.correctAnswerInstruc)
        html += u"<br/>" + _("Option")
        html += u"</th>"
        html += u"</tr>"
        html += u"</thead>"
        html += u"<tbody>"
        for element in self.options:
            html += element.renderEdit() 
        html += u"</tbody>"
        html += u"</table>\n"
        value = _(u"Add another Option")    
        html += common.submitButton("addOption"+unicode(self.id), value)
        html += u"<br />"
        html += u"</div>\n"
        return html
    def renderView(self):
        """
        Returns an XHTML string for viewing this element
        """
        html  = "<b>" + self.question.question +"</b><br/>\n"
        html += "<table>"
        for element in self.options:
            html += element.renderView()      
        html += "</table>"   
        return html
    def getCorrectAns(self):
        """
        return the correct answer for the question
        """
        return self.question.correctAns
    def getNumOption(self):
        """
        return the number of options
        """
        return len(self.question.options)
