"""
EditorElement is responsible for a block of field. Used by iDevice Editor
"""
import logging
from exe.webui import common
from exe.webui.element import Element
log = logging.getLogger(__name__)
class EditorElement(Element):
    """
    EditorElement is responsible for a block of field.  Used by iDevice Editor
    """
    def __init__(self, field):
        """
        Initialize
        """
        Element.__init__(self, field)
    def process(self, request):
        """
        Process arguments from the webserver.  Return any which apply to this 
        element.
        """
        log.debug(u'process ' + repr(request.args))
        if "name"+self.id in request.args:
            self.field.name = unicode(request.args["name"+self.id][0], 'utf8')
        if "instruc"+self.id in request.args:
            self.field.instruc = unicode(request.args["instruc"+self.id][0], 
                                         'utf8')
        if "object" in request.args and request.args["object"][0] == self.id:
            if request.args["action"][0] == "deleteField":
                self.field.idevice.fields.remove(self.field)
        if "btnCaption"+self.id in request.args:
            self.field.buttonCaption = unicode(request.args["btnCaption"+self.id][0],
                                         'utf8')
class TextEditorElement(EditorElement):
    """ 
    TextElement is a single line of text
    """
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this field
        """
        html  = common.textInput("name"+self.id, self.field.name, 25)
        html += common.submitImage("deleteField", self.id, 
                                   "/images/stock-cancel.png", 
                                   _("Delete"), 1)
        html += "<br/>\n"
        html += common.richTextArea("instruc"+self.id, self.field.instruc)
        html += "<br/>"
        return html
    def renderPreview(self):
        """
        Returns an XHTML string with the form element for previewing this field
        """
        html  = "<b>" + self.field.name + "</b> "
        if self.field.instruc != "":
            html += common.elementInstruc(self.field.instruc)
        html += "<br/>\n"  
        html += common.textInput(self.id, self.field.content)
        html += "<br/>\n"
        return html
class TextAreaEditorElement(EditorElement):
    """ 
    TextElement is a single line of text
    """
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this field
        """
        html  = common.textInput("name"+self.id, self.field.name, 25)
        html += common.submitImage("deleteField", self.id, 
                                   "/images/stock-cancel.png", 
                                   _("Delete"), 1)
        html += "<br/>\n"
        html += common.formField('richTextArea', '','instruc',
                                 self.id, '',
                                 self.field.instruc)
        html += "<br/>"
        return html
    def renderPreview(self):
        """
        Returns an XHTML string with the form element for previewing this field
        """
        html  = "<b>" + self.field.name + "</b> "
        if self.field.instruc != "":
            html += common.elementInstruc(self.field.instruc)
        html += "<br/>\n" 
        html += common.textArea(self.id, self.field.content)
        html += "<br/>\n"
        return html
class FeedbackEditorElement(EditorElement):
    """ 
    FeedbackElement is a feedback text which can be show or hide
    """
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this field
        """
        html = "<b>%s </b>" % _("Button Caption")
        html += common.textInput("btnCaption"+self.id, 
                                 self.field.buttonCaption, 25)
        html += "<br/><br/>"
        html += common.textInput("name"+self.id, self.field.name, 25)
        html += common.submitImage("deleteField", self.id, 
                                   "/images/stock-cancel.png", 
                                   _("Delete"), 1)
        html += "<br/>\n"
        html += common.formField('richTextArea', '','instruc',
                                 self.id, '',
                                 self.field.instruc)
        html += "<br/>"
        return html
    def renderPreview(self):
        """
        Returns an XHTML string with the form element for previewing this field
        """
        html  = "<b>" + self.field.name + "</b> "
        if self.field.instruc != "":
            html += common.elementInstruc(self.field.instruc)
        html += "<br/>\n" 
        html += common.textArea(self.id, self.field.feedback)
        html += "<br/>\n"
        return html
class ImageEditorElement(EditorElement):
    """ 
    ImageElement is an image
    """
    DefaultImage = "sunflowers.jpg"
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this field
        """
        html  = common.textInput("name"+self.id, self.field.name, 25)
        html += common.submitImage("deleteField", self.id, 
                                   "/images/stock-cancel.png", 
                                   _("Delete"), 1)
        html += "<br/>\n"
        html += common.image("img"+self.id, 
                             "/images/"+ImageEditorElement.DefaultImage,
                             self.field.width,
                             self.field.height)
        html += "<br/>\n"
        html += common.formField('richTextArea', '','instruc',
                                 self.id, '',
                                 self.field.instruc)
        return html
    def renderPreview(self):
        """
        Returns an XHTML string with the form element for previewing this field
        """
        html  = "<b>" + self.field.name + "</b> "
        if self.field.instruc != "":
            html += common.elementInstruc(self.field.instruc)
        html += "<br/>" 
        html += common.image("img"+self.id, 
                             "/images/"+ImageEditorElement.DefaultImage,
                             self.field.width,
                             self.field.height)
        floatArr    = [[_(u'Left'), 'left'],
                      [_(u'Right'), 'right'],
                      [_(u'None'),  'none']]
        html += common.formField('select', _("Align:"),
                                 "float" + self.id, '',
                                 '',
                                 floatArr, '')
        html += "<br/>\n"
        return html
class FlashEditorElement(EditorElement):
    """ 
    FlashElement is an flash video
    """
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this field
        """
        html  = common.textInput("name"+self.id, self.field.name, 25)
        html += common.submitImage("deleteField", self.id, 
                                   "/images/stock-cancel.png", 
                                   _("Delete"), 1)
        html += "<br/>\n"
        html += common.formField('richTextArea', '','instruc',
                                 self.id, '',
                                 self.field.instruc)
        return html
    def renderPreview(self):
        """
        Returns an XHTML string with the form element for previewing this field
        """
        html  = "<b>" + self.field.name + "</b> "
        html += "<br/>" 
        html += common.textInput("path"+self.id, "", 50)
        html += u'<input type="button" '
        html += u' value="%s" />' % _(u"Select Flash Object")
        if self.field.instruc != "":
            html += common.elementInstruc(self.field.instruc)
        html += "<br/>\n"
        return html
class MultimediaEditorElement(EditorElement):
    """ 
    MultimediaElement is a mp3
    """
    def renderEdit(self):
        """
        Returns an XHTML string with the form element for editing this field
        """
        html  = common.textInput("name"+self.id, self.field.name, 25)
        html += common.submitImage("deleteField", self.id, 
                                   "/images/stock-cancel.png", 
                                   _("Delete"), 1)
        html += "<br/>\n"
        html += common.formField('richTextArea', '','instruc',
                                 self.id, '',
                                 self.field.instruc)
        return html
    def renderPreview(self):
        """
        Returns an XHTML string with the form element for previewing this field
        """
        html  = "<b>" + self.field.name + "</b> "
        html += "<br/>" 
        html += common.textInput("path"+self.id, "", 50)
        html += u'<input type="button" ' 
        html += u' value="%s" />' % _(u"Select MP3 file")
        if self.field.instruc != "":
            html += common.elementInstruc(self.field.instruc)
        html += '<br/><b>%s</b><br/>' % _(u"Caption:")
        html += common.textInput("", "")
        html += common.elementInstruc(self.field.captionInstruc)
        html += "<br/>\n"
        return html
