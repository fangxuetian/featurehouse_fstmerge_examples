"""
Renders a paragraph where the content creator can choose which words the student
must fill in.
"""
import logging
import urllib
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
    def process(self, request):
        """
        Handles changes in the paragraph text from editing
        """
        if "title"+self.id in request.args:
            self.idevice.title = request.args["title"+self.id][0]
        object = request.args.get('object', [''])[0]
        action = request.args.get('action', [''])[0]
        self.instructionElement.process(request)
        if object == self.id:
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
    def renderViewContent(self):
        """
        Returns an XHTML string for this block
        """
        if self.feedbackElement.field.content:
            clozeContent = self.clozeElement.renderView(self.feedbackElement.id)
        else:
            clozeContent = self.clozeElement.renderView()
        html = [
            u'<script type="text/javascript" src="common.js"></script>\n',
            u'<div class="iDevice_inner">\n',
            self.instructionElement.renderView(),
            clozeContent]
        if self.feedbackElement.field.content:
            html.append(self.feedbackElement.renderView(False, class_="feedback"))
        html += [
            u'</div>\n',
            ]
        return u'\n    '.join(html)
from exe.engine.clozeidevice import ClozeIdevice
from exe.webui.blockfactory  import g_blockFactory
g_blockFactory.registerBlockType(ClozeBlock, ClozeIdevice)    
