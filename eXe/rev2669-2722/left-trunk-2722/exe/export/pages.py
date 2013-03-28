"""
Export Pages functions
"""
import logging
log = logging.getLogger(__name__)
class Page(object):
    """
    This is an abstraction for a page containing a node
    e.g. in a SCORM package or Website
    """
    def __init__(self, name, depth, node):
        """
        Initialize
        """
        self.name  = name
        self.depth = depth
        self.node  = node
    def renderLicense(self):
        """
        Returns an XHTML string rendering the license.
        """
        licenses = {"GNU Free Documentation License":
                     "http://www.gnu.org/copyleft/fdl.html", 
                     "Creative Commons Attribution 2.5 License":
                     "http://creativecommons.org/licenses/by/2.5/",
                     "Creative Commons Attribution-ShareAlike 2.5 License":
                     "http://creativecommons.org/licenses/by-sa/2.5/",
                     "Creative Commons Attribution-NoDerivs 2.5 License":
                     "http://creativecommons.org/licenses/by-nd/2.5/",
                     "Creative Commons Attribution-NonCommercial 2.5 License":
                     "http://creativecommons.org/licenses/by-nc/2.5/",
                     "Creative Commons Attribution-NonCommercial-ShareAlike 2.5 License":
                     "http://creativecommons.org/licenses/by-nc-sa/2.5/",
                     "Creative Commons Attribution-NonCommercial-NoDerivs 2.5 License":
                     "http://creativecommons.org/licenses/by-nc-nd/2.5/",
                     "Developing Nations 2.0":
                     "http://creativecommons.org/licenses/devnations/2.0/"}
        html = ""
        license = self.node.package.license
        if license <> "None":
            html += '<p align="center">'
            html += _("Licensed under the ")
            html += '<a href="%s">%s</a></p>' % (licenses[license], license)
        return html
    def renderFooter(self):
        """
        Returns an XHTML string rendering the footer.
        """
        html = ""
        if self.node.package.footer <> "":
            html += '<p align="center">'
            html += self.node.package.footer + "</p>"
        return html
def uniquifyNames(pages):
    """
    Make sure all the page names are unique
    """
    pageNames = {}
    for page in pages:
        if page.name in pageNames:
            pageNames[page.name] = 1
        else:
            pageNames[page.name] = 0
    for page in pages:
        uniquifier = pageNames[page.name]
        if uniquifier:
            pageNames[page.name] = uniquifier + 1
            page.name += unicode(uniquifier)
