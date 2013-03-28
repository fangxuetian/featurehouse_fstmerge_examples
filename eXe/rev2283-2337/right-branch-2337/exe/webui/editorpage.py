"""
The EditorPage is responsible for managing user created iDevices
"""
import logging
from twisted.web.resource      import Resource
from exe.webui                 import common
from exe.engine.genericidevice import GenericIdevice
from exe.webui.editorpane      import EditorPane
from exe.webui.renderable      import RenderableResource
log = logging.getLogger(__name__)
class EditorPage(RenderableResource):
    """
    The EditorPage is responsible for managing user created iDevices
    create / edit / delete
    """
    name = 'editor'
    def __init__(self, parent):
        """
        Initialize
        """
        RenderableResource.__init__(self, parent)
        self.editorPane   = EditorPane(self.webServer)
        self.url          = ""
        self.elements     = []
        self.isNewIdevice = True
        self.message      = ""
    def getChild(self, name, request):
        """
        Try and find the child for the name given
        """
        if name == "":
            return self
        else:
            return Resource.getChild(self, name, request)
    def process(self, request):
        """
        Process current package 
        """
        log.debug("process " + repr(request.args))
        self.editorPane.process(request,"old")
        if "action" in request.args:
            if request.args["action"][0] == "changeIdevice":
                genericIdevices = self.ideviceStore.generic
                if not self.isNewIdevice:
                    ideviceId = self.editorPane.idevice.id
                    for idevice in genericIdevices:
                        if idevice.id == ideviceId:
                            break
                    copyIdevice = self.editorPane.idevice.clone()
                    self.__saveChanges(idevice, copyIdevice)
                for idevice in genericIdevices:
                    if idevice.id == request.args["object"][0]:
                        break
                self.isNewIdevice = False
                self.editorPane.setIdevice(idevice)               
                self.editorPane.process(request, "new")
        if (("action" in request.args and 
             request.args["action"][0] == "newIdevice")
            or "new" in request.args):
            self.__createNewIdevice(request)
        if "delete" in request.args:
            self.ideviceStore.delGenericIdevice(self.editorPane.idevice)
            self.ideviceStore.save()
            self.__createNewIdevice(request) 
        if "add" in request.args:
            if self.editorPane.idevice.title == "":
                self.message = _("Please enter an idevice name.")
            else:
                newIdevice = self.editorPane.idevice.clone()
                newIdevice.id = self.ideviceStore.getNewIdeviceId()
                self.ideviceStore.addIdevice(newIdevice)
                self.editorPane.setIdevice(newIdevice)
                self.ideviceStore.save()
                self.isNewIdevice = False
        if "save" in request.args: 
            genericIdevices = self.ideviceStore.generic
            for idevice in genericIdevices:
                if idevice.id == self.editorPane.idevice.id:
                    break
            copyIdevice = self.editorPane.idevice.clone()
            self.__saveChanges(idevice, copyIdevice)
    def __createNewIdevice(self, request):
        """
        Create a new idevice and add to idevicestore
        """
        idevice = GenericIdevice("", "", "", "", "")
        idevice.icon = ""
        idevice.id = self.ideviceStore.getNewIdeviceId()
        self.editorPane.setIdevice(idevice)
        self.editorPane.process(request, "new")      
        self.isNewIdevice = True
    def __saveChanges(self, idevice, copyIdevice):
        """
        Save changes to generic idevice list.
        """
        idevice.title    = copyIdevice.title
        idevice.author   = copyIdevice.author
        idevice.purpose  = copyIdevice.purpose
        idevice.tip      = copyIdevice.tip
        idevice.fields   = copyIdevice.fields
        idevice.emphasis = copyIdevice.emphasis
        idevice.icon     = copyIdevice.icon
    def render_GET(self, request):
        """Called for all requests to this object"""
        log.debug("render_GET")
        self.process(request)
        html  = common.docType()
        html += "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
        html += "<head>\n"
        html += "<style type=\"text/css\">\n"
        html += "@import url(/css/exe.css);\n"
        html += '@import url(/style/base.css);\n'
        html += "@import url(/style/standardwhite/content.css);</style>\n"
        html += '<script type="text/javascript" src="/scripts/libot_drag.js">'
        html += '</script>\n'
        html += '<script type="text/javascript" src="/scripts/common.js">'
        html += '</script>\n'
        html += "<title>"+_("eXe : elearning XHTML editor")+"</title>\n"
        html += "<meta http-equiv=\"content-type\" content=\"text/html; "
        html += " charset=UTF-8\"></meta>\n";
        html += "</head>\n"
        html += "<body>\n"
        html += "<div id=\"main\"> \n"     
        html += "<form method=\"post\" action=\""+self.url+"\" "
        html += "id=\"contentForm\" >"  
        html += common.hiddenField("action")
        html += common.hiddenField("object")
        html += common.hiddenField("isChanged", "1") 
        html += "<font color=\"red\"<b>"+self.message+"</b></font>"
        html += "<div id=\"editorButtons\"> \n"     
        html += self.renderList()
        html += self.editorPane.renderButtons(request)
        if self.isNewIdevice:
            html += "<br/>" + common.submitButton("delete", _("Delete"), 
                                                        False)
            html += "<br/>" + common.submitButton("save", _("Save"), False)
        else:
            html += "<br/>" + common.submitButton("delete", _("Delete"))
            html += "<br/>" + common.submitButton("save", _("Save"))
        html += "<br/>" + common.submitButton("add", _("Save as"))
        html += "</fieldset>"
        html += "</div>\n"
        html += self.editorPane.renderIdevice(request)
        html += "</div>\n"
        html += "<br/></form>\n"
        html += "</body>\n"
        html += "</html>\n"
        return html.encode('utf8')
    render_POST = render_GET
    def renderList(self):
        """
        Render the list of generic iDevice
        """
        html  = "<fieldset><legend><b>" + _("Edit")+ "</b></legend>"
        html += '<select onchange="submitIdevice();" name="ideviceSelect">\n'
        html += "<option value = \"newIdevice\" "
        if self.isNewIdevice:
            html += "selected "
        html += ">"+ _("New iDevice") + "</option>"
        for prototype in self.ideviceStore.generic:
            html += "<option value=\""+prototype.id+"\" "
            if self.editorPane.idevice.id == prototype.id:
                html += "selected "
            title = prototype.title
            if len(title) > 16:
                title = title[:16] + "..."
            html += ">" + title + "</option>\n"
        html += "</select> \n"
        html += "</fieldset>\n"
        self.message = ""
        return html
