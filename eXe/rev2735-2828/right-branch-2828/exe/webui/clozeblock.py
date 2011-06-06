"""
Renders a paragraph where the content creator can choose which words the student
must fill in.
"""
import logging
from exe.webui.block   import Block
from exe.webui         import common
from exe.webui.element import ClozeElement, TextAreaElement
log = logging.getLogger(__name__)
class ClozeBlock(Block):
    """
    Renders a paragraph where the content creator can choose which words the
    student must fill in.
    """
    def __init__(self, parent, idevice):
        """
        Pre-create our field ids
        """
        Block.__init__(self, parent, idevice)
        self.instructionElement = \
            TextAreaElement(idevice.instructionsForLearners)
        self.clozeElement = ClozeElement(idevice.content)
        self.feedbackElement = \
            TextAreaElement(idevice.feedback)
        self.previewing        = False # In view or preview render
    def process(self, request):
        """
        Handles changes in the paragraph text from editing
        """
        if "title"+self.id in request.args:
            self.idevice.title = request.args["title"+self.id][0]
        object = request.args.get('object', [''])[0]
        action = request.args.get('action', [''])[0]
        self.instructionElement.process(request)
        self.clozeElement.process(request)
        self.feedbackElement.process(request)
        Block.process(self, request)
    def renderEdit(self, style):
        """
        Renders a screen that allows the user to enter paragraph text and choose
        which words are hidden.
        """
        html = [
            u'<div class="iDevice">',
            u'<div class="block">',
            common.textInput("title"+self.id, self.idevice.title),
            u'</div>',
            self.instructionElement.renderEdit(),
            self.clozeElement.renderEdit(),
            self.feedbackElement.renderEdit(),
            self.renderEditButtons(),
            u'</div>'
            ]
        return u'\n    '.join(html)
    def renderPreview(self, style):
        """ 
        Remembers if we're previewing or not, 
        then implicitly calls self.renderViewContent (via Block.renderPreview) 
        """ 
        self.previewing = True 
        return Block.renderPreview(self, style)
    def renderView(self, style):
        """ 
        Remembers if we're previewing or not, 
        then implicitly calls self.renderViewContent (via Block.renderPreview) 
        """ 
        self.previewing = False 
        return Block.renderView(self, style)
    def renderViewContent(self):
        """
        Returns an XHTML string for this block
        """
        if self.feedbackElement.field.content:
            if self.previewing: 
                clozeContent = self.clozeElement.renderPreview(self.feedbackElement.id)
            else: 
                clozeContent = self.clozeElement.renderView(self.feedbackElement.id)
        else:
            if self.previewing: 
                clozeContent = self.clozeElement.renderView()
            else:
                clozeContent = self.clozeElement.renderPreview()
        instruction_html = ""
        if self.previewing: 
            instruction_html = self.instructionElement.renderPreview()
        else:
            instruction_html = self.instructionElement.renderView()
        html = [
            u'<script type="text/javascript" src="common.js"></script>\n',
            u'<div class="iDevice_inner">\n',
            instruction_html,
            clozeContent]
        if self.feedbackElement.field.content: 
            if self.previewing: 
                html.append(self.feedbackElement.renderPreview(False, class_="feedback"))
            else:
                html.append(self.feedbackElement.renderView(False, class_="feedback"))
        html += [
            u'</div>\n',
            ]
        return u'\n    '.join(html)
    def renderText(self): 
        """
        Returns an XHTML string for text file export.
        """
        if self.previewing: 
            html = '<p>' +  self.instructionElement.renderPreview() +'</p>'
        else:
            html = '<p>' +  self.instructionElement.renderView() +'</p>'
        html += '<p>' + self.clozeElement.renderText() + '</p>'
        if self.feedbackElement.field.content:
            html += '<p>%s:</P>' % _(u"Feedback") 
            if self.previewing: 
                html += '<p>' +self.feedbackElement.renderPreview(False, class_="feedback") +'</p>'
            else:
                html += '<p>' +self.feedbackElement.renderView(False, class_="feedback") +'</p>'
        html += self.clozeElement.renderAnswers()
        return html
from exe.engine.clozeidevice import ClozeIdevice
from exe.webui.blockfactory  import g_blockFactory
g_blockFactory.registerBlockType(ClozeBlock, ClozeIdevice)    
