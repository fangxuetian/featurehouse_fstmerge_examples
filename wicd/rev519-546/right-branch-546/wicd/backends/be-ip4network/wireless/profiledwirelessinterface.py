import wpath, misc
import externalwirelessutils, configmanager
class WirelessNetworkProfile(object):
    def __init__(self, settings={}):
        self.__values = {}
        if settings:
            for key, value in settings.iteritems():
                self[key] = value
    def __setitem__(self, key, value):
        self.__values[key] = misc.smart_type(value)
    def __getitem__(self, key):
        return self.__values[key]
    def __contains__(self, item):
        return item in self.__values
    def __delitem__(self, item):
        del self.__values[item]
    def __iter__(self):
        return self.__values.iteritems()
    def __len__(self):
        return len(self.__values)
class ProfiledWirelessInterface(externalwirelessutils.WirelessInterface):
    ''' Adds network profiles to the wireless interface. '''
    def __init__(self, interface_name):
        externalwirelessutils.WirelessInterface.__init__(self,
                                                         interface_name)
        self.config_manager = configmanager.ConfigManager(wpath.etc + 
                                                          'wireless-%s-profiles.conf'
                                                          % interface_name)
    def scan(self):
        """ Updates the current wireless network list. """
        self.save_profiles()
        externalwirelessutils.WirelessInterface.scan(self)
        for network in self.networks:
            if self.config_manager.has_section(network.bssid):
                print network.bssid,'has profile'
                settings = self.config_manager.items(network.bssid)
                network.profile = WirelessNetworkProfile(dict(settings))
            else:
                print network.bssid,'missing profile'
                network.profile = WirelessNetworkProfile()
        return self.networks
    def save_profiles(self):
        """ Saves the profiles of all networks. """
        if not hasattr(self, 'networks'): return
        for network in self.networks:
            for key, value in network.profile:
                self.config_manager.set_option(network.bssid, key, value)
        self.config_manager.write()
