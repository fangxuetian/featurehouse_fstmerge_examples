from profiledwirelessinterface import ProfiledWirelessInterface
from templatemanager import TemplateManager
class  EncryptableWirelessInterface (ProfiledWirelessInterface) :
	''' Adds wpa_supplicant support to the wireless interface.'''
	    def __init__(self, interface_name):
        ProfiledWirelessInterface.__init__(self, interface_name)
        self.template_manager = TemplateManager()
        self.template_manager.load_all_available_templates()


