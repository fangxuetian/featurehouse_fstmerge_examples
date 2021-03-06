import os, re
from baseinterface import BaseInterface, needsidle
from threadedwirelessinterface import ThreadedWirelessInterface as WirelessInterface
from threadedwirelessinterface import AsyncError
from misc import WicdError
class  BackendWirelessInterface (BaseInterface) :
	''' A Wireless Interface backend for Wicd. '''
	    @staticmethod
    def get_type():
        ''' Returns type of network interface. '''
        return 'wireless'
    
	@staticmethod
    def find_available_interfaces():
        ''' Returns list of strings of valid network interface names. '''
        dev_dir = '/sys/class/net/'
        ifnames = [iface for iface in os.listdir(dev_dir) \
                   if os.path.isdir(dev_dir + iface)                   and 'wireless' in os.listdir(dev_dir + iface)]
        return ifnames
    
	def __init__(self, interface_name, status_change_callback):
        ''' Instantiates the BackendWirelessInterface. '''
        BaseInterface.__init__(self, interface_name, status_change_callback)
        if not interface_name in self.find_available_interfaces():
            raise self.CannotCreateInterfaceException()
        self.name = 'Wireless Interface %s' % self.interface_name
        self.interface = WirelessInterface(self.interface_name)
        self._status_change('idle')
    
	@needsidle
    def do_update(self):
        """ Updates interface status. """
        self._status_change('updating')
        self._status_change('idle')
    
	@needsidle
    def do_scan(self):
        ''' Scans for new networks. '''
        def finished_callback():
            ''' Sets the current interface state. '''
            self._status_change('idle')
        self.interface.scan(finished_callback)
        self._status_change('scanning')
    
	def get_status(self):
        ''' Returns interface status. '''
        return "Wireless Interface. Connected %s" % \
               self.get_connected_to_network()
    
	@needsidle
    def get_networks(self):
        """ Returns a dictionary of network BSSIDs with the ESSIDs. """
        networks = dict( [ ( network.bssid, network.essid ) for network in self.interface.networks ] )
        return networks
    
	def _get_network(self, the_property, value):
        ''' Returns the first network where property == value. '''
        for network in self.interface.networks:
            if hasattr(network, the_property):
                if getattr(network, the_property) == value:
                    return network
    
	@needsidle
    def set_current_network(self, network_bssid):
        ''' Sets the current network to the one with the specified BSSID. '''
        try:
            networks = self.get_networks()
        except AttributeError:
            raise WicdError('Must run do_scan() before set_current_network()')
        self.interface.current_network = self._get_network('bssid', network_bssid)
    
	def set_network_property(self, name, value):
        setattr(self.interface.current_network.profile, name, value)
    
	def get_network_property(self, name):
        if not hasattr(self.interface.current_network.profile, name):
            raise WicdError('Current network has no property %s' % name)
        return getattr(self.interface.current_network.profile, name)
    
	@needsidle
    def do_connect(self):
        ''' Connects to the wireless network set with set_current_network(). '''
        if not self.interface.current_network:
            raise WicdError('Must run set_current_network() before do_connect()')
        def finished_callback():
            ''' Sets the current interface state. '''
            self.connected = True
            self._status_change('idle')
        self._status_change('connecting')
        self.interface.connect(finished_callback)
    
	def do_cancel_connect(self):
        ''' Cancels the current connection attempt. '''
        self.interface.cancel_connection_attempt()
    
	@needsidle
    def do_save(self):
        ''' Saves the wireless profiles. '''
        self.interface.save_profiles()


