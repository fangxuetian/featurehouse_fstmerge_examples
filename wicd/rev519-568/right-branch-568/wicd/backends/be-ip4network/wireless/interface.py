import os, re
from baseinterface import BaseInterface, needsidle
from threadedwirelessinterface import ThreadedWirelessInterface as WirelessInterface
from threadedwirelessinterface import AsyncError
from misc import WicdError
import logging
class BackendWirelessInterface(BaseInterface):
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
                   if os.path.isdir(dev_dir + iface)
                   and 'wireless' in os.listdir(dev_dir + iface)]
        return ifnames
    def __init__(self, interface_name, status_change_callback):
        ''' Instantiates the BackendWirelessInterface. '''
        BaseInterface.__init__(self, interface_name, status_change_callback)
        if not interface_name in self.find_available_interfaces():
            raise self.CannotCreateInterfaceException(
                'No wireless interface named %s exists.' % interface_name)
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
    @needsidle
    def get_networks(self):
        """ Returns a dictionary of network BSSIDs with the ESSIDs. """
        try:
            networks = self.interface.networks
        except AttributeError:
            raise WicdError('Must run do_scan() before get_networks()')
        networks = dict( [ ( network.bssid, network.essid ) for network in networks ] )
        return networks
    def _get_network(self, the_property, value):
        ''' Returns the first network where property == value. '''
        if not hasattr(self.interface, 'networks'):
            raise WicdError('Must run do_scan() before _get_networks()')
        for network in self.interface.networks:
            if hasattr(network, the_property):
                if getattr(network, the_property) == value:
                    return network
        raise WicdError('No network with the specified %s exists.' % the_property)
    @needsidle
    def set_current_network(self, network_bssid):
        ''' Sets the current network to the one with the specified BSSID. '''
        self.interface.current_network = self._get_network('bssid', network_bssid)
    @needsidle
    def set_network_property(self, name, value):
        self.interface.current_network.profile[name] = value
    def get_network_property(self, name):
        if not name in self.interface.current_network.profile:
            raise WicdError('Current network has no property %s' % name)
        return self.interface.current_network.profile[name]
    @needsidle
    def do_connect(self):
        ''' Connects to the wireless network set with set_current_network(). '''
        if not self.interface.current_network:
            raise WicdError('Must run set_current_network() before do_connect()')
        def finished_callback(result):
            ''' Sets the current interface state. '''
            self._state_change(result)
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
    @needsidle
    def do_autoconnect(self, value):
        logging.debug('%s: do_autoconnect %s', self.interface.interface_name, value)
        for network in self.interface.networks:
            logging.debug('found network %s', network.essid)
            if 'autoconnect' in network.profile \
               and network.profile['autoconnect']:
                logging.debug('autoconnecting to %s', network.essid)
                self.set_current_network(network.bssid)
                self.do_connect()
                return
