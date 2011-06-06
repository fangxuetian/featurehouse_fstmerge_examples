"""
A true false idevice is one built up from question and options
"""
import logging
from exe.engine.persist   import Persistable
from exe.engine.idevice   import Idevice
from exe.engine.translate import lateTranslate
from exe.engine.field     import TextAreaField
log = logging.getLogger(__name__)
class TrueFalseQuestion(Persistable):
    """
    A TrueFalse iDevice is built up of questions.  Each question can
    be rendered as an XHTML element
    """
    def __init__(self, question="", isCorrect=False, feedback="", hint=""):
        """
        Initialize 
        """
        self.question  = question
        self.isCorrect = isCorrect
        self.feedback  = feedback
        self.hint      = hint
class TrueFalseIdevice(Idevice):
    """
    A TrueFalse Idevice is one built up from question and options
    """
    persistenceVersion = 8
    def __init__(self):
        """
        Initialize 
        """
        Idevice.__init__(self,
                         x_(u"True-False Question"),
                         x_(u"University of Auckland"),
                         x_(u"""True/false questions present a statement where 
the learner must decide if the statement is true. This type of question works 
well for factual information and information that lends itself to either/or 
responses."""), u"", u"question")
        self.emphasis         = Idevice.SomeEmphasis
        self._hintInstruc     = x_(u"""A hint may be provided to assist the 
learner in answering the question.""")
        self.questions        = []
        self._questionInstruc = x_(u"""Type the question stem. The question 
should be clear and unambiguous. Avoid negative premises as these can tend to 
be ambiguous.""")
        self._keyInstruc      = ""
        self._feedbackInstruc = x_(u"""Enter any feedback you wish to provide 
to the learner. This field may be left blank. if this field is left blank 
default feedback will be provided.""")
        self.questions.append(TrueFalseQuestion())
        self.systemResources += ["common.js", "libot_drag.js",
                                 "panel-amusements.png", "stock-stop.png"]
        self.instructionsForLearners = TextAreaField(
            x_(u'Instructions'),
            x_(u"""Provide instruction on how the True/False Question should be 
completed."""),
            u'')
        self.instructionsForLearners.idevice = self
    hintInstruc     = lateTranslate('hintInstruc')
    questionInstruc = lateTranslate('questionInstruc')
    keyInstruc      = lateTranslate('keyInstruc')
    feedbackInstruc = lateTranslate('feedbackInstruc')
    def addQuestion(self):
        """
        Add a new question to this iDevice. 
        """
        self.questions.append(TrueFalseQuestion())
    def upgradeToVersion1(self):
        """
        Upgrades the node from version 0 to 1.
        Old packages will loose their icons, but they will load.
        """
        log.debug(u"Upgrading iDevice")
        self.icon = u"multichoice"
    def upgradeToVersion2(self):
        """
        Upgrades the node from 1 (v0.5) to 2 (v0.6).
        Old packages will loose their icons, but they will load.
        """
        log.debug(u"Upgrading iDevice")
        self.emphasis = Idevice.SomeEmphasis
    def upgradeToVersion3(self):
        """
        Upgrades the node from 1 (v0.6) to 2 (v0.7).
        Change icon from 'multichoice' to 'question'
        """
        log.debug(u"Upgrading iDevice icon")
        self.icon = "question"
    def upgradeToVersion4(self):
        """
        Upgrades v0.6 to v0.7.
        """
        self.lastIdevice = False
    def upgradeToVersion5(self):
        """
        Upgrades exe to v0.10
        """
        self._upgradeIdeviceToVersion1()
        self._hintInstruc = self.__dict__['hintInstruc']
        self._questionInstruc = self.__dict__['questionInstruc']
        self._keyInstruc = self.__dict__['keyInstruc']
    def upgradeToVersion6(self):
        """
        Upgrades exe to v0.11
        """
        self._feedbackInstruc = x_(u"""Type in the feedback that you want the 
student to see when selecting the particular question. If you don't complete
this box, eXe will automatically provide default feedback as follows: 
"Correct answer" as indicated by the selection for the correct answer; or 
"Wrong answer" for the other alternatives.""")
    def upgradeToVersion7(self):
        """
        Upgrades to v0.12
        """
        self._upgradeIdeviceToVersion2()        
        self.systemResources += ["common.js", "libot_drag.js",
                                 "panel-amusements.png", "stock-stop.png"]
    def upgradeToVersion8(self):
        """
        Upgrades to v0.15
        """
        self.instructionsForLearners = TextAreaField(
            x_(u'Instructions'),
            x_(u"""Provide instruction on how the True/False Question should be 
completed."""),
            x_(u'Read the paragraph below and '
                'fill in the missing words.'))
        self.instructionsForLearners.idevice = self
