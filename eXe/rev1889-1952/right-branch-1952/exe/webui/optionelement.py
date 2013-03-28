"""
OptionElement is responsible for a block of option.  Used by MultichoiceBlock
"""
import logging
from exe.webui import common
log = logging.getLogger(__name__)
class OptionElement(object):
    """
    OptionElement is responsible for a block of option.  Used by
    MultichoiceBlock 
    """
    def __init__(self, index, idevice, option):
        """
        Initialize
        """
        self.index      = index
        self.id         = "a" + unicode(index) + "b" + idevice.id
        self.idevice    = idevice
        self.option     = option
        self.answerId   = "optionAnswer"+ unicode(index) + "b" + idevice.id
        self.keyId      = "optionKey" + idevice.id
        self.feedbackId = "optionFeedback" + unicode(index) + "b" + idevice.id
    def process(self, request):
        """
        Process arguments from the webserver.  Return any which apply to this 
        element.
        """
        log.debug("process " + repr(request.args))
        if self.answerId in request.args:
            self.option.answer = request.args[self.answerId][0]
        if self.keyId in request.args:
            if request.args[self.keyId][0] == self.id:
                self.option.isCorrect = True 
                log.debug("option " + repr(self.option.isCorrect))
            else:
                self.option.isCorrect = False
        if self.feedbackId in request.args:
            self.option.feedback = request.args[self.feedbackId][0]
        if "action" in request.args and request.args["action"][0] == self.id:
            self.idevice.options.remove(self.option)
    def renderEdit(self):
        """
        Returns an XHTML string for editing this option element
        """
        html  = u"<tr><td align=\"center\"><b>%s</b>" % _("Option")
        html += common.elementInstruc(self.idevice.answerInstruc)
        header = ""
        if self.index == 0:
            header = _("Correct") + "<br/>" + _("Option")
        html += u"</td><td align=\"center\"><b>%s</b>\n" % header
        html += common.elementInstruc(self.idevice.keyInstruc)
        html += "</td><td></td></tr><tr><td>\n" 
        html += common.richTextArea(self.answerId, self.option.answer)
        html += "</td><td align=\"center\">\n"
        html += common.option(self.keyId, self.option.isCorrect, self.id)   
        html += "</td><td>\n"
        html += common.submitImage(self.id, self.idevice.id,
                                   "/images/stock-cancel.png",
                                   _(u"Delete option"))
        html += "</td></tr><tr><td align=\"center\"><b>%s</b>" % _("Feedback")
        html += common.elementInstruc(self.idevice.feedbackInstruc)
        html += "</td><td></td><td></td></tr><tr><td>\n" 
        html += common.richTextArea(self.feedbackId, self.option.feedback)
        html += "</td><td></td><td></td></tr>\n"
        return html
    def renderAnswerView(self):
        """
        Returns an XHTML string for viewing and previewing this option element
        """
        log.debug("renderView called")
        length = len(self.idevice.options)
        html  = '<tr><td>'
        html += '<input type="radio" name="option%s" ' % self.idevice.id
        html += 'id="i%s" ' % self.id
        html += 'onclick="getFeedback(%d,%d,\'%s\',\'multi\')"/>' % (self.index, 
                                                length, self.idevice.id)
        html += '</td><td>\n'
        html += self.option.answer + "</td></tr>\n"
        return html
    def renderFeedbackView(self):
        """
        return xhtml string for display this option's feedback
        """
        feedbackStr = ""
        if self.option.feedback != "":
            feedbackStr = self.option.feedback
        else:
            if self.option.isCorrect:
                feedbackStr = _("Correct")
            else:
                feedbackStr = _("Wrong")
        html  = '<div id="s%s" style="color: rgb(0, 51, 204);' % self.id
        html += 'display: none;">' 
        html += feedbackStr + '</div>\n'
        return html
