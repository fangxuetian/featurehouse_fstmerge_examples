"""Old method of wxPython support for Twisted.
twisted.internet.wxreactor is probably a better choice.
To use::
    | # given a wxApp instance called myWxAppInstance:
    | from twisted.internet import wxsupport
    | wxsupport.install(myWxAppInstance)
Use Twisted's APIs for running and stopping the event loop, don't use
wxPython's methods.
On Windows the Twisted event loop might block when dialogs are open
or menus are selected.
API Stability: stable
Maintainer: U{Itamar Shtull-Trauring<mailto:twisted@itamarst.org>}
"""
import warnings
warnings.warn("wxsupport is not fully functional on Windows, wxreactor is better.")
from wxPython.wx import wxApp
from twisted.internet import reactor
from twisted.python.runtime import platformType
class wxRunner:
    """Make sure GUI events are handled."""
    def __init__(self, app):
        self.app = app
    def run(self):
        """
        Execute pending WX events followed by WX idle events and
        reschedule.
        """
        while self.app.Pending():
            self.app.Dispatch()
        self.app.ProcessIdle()
        reactor.callLater(0.02, self.run)
def install(app):
    """Install the wxPython support, given a wxApp instance"""
    runner = wxRunner(app)
    reactor.callLater(0.02, runner.run)
__all__ = ["install"]
