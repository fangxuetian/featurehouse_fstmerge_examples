import misc
from dhcpmanager import DhcpManager
from netutilsmanager import NetUtilsManager
from logfile import log
manager = NetUtilsManager()
netutils = manager.get_netutils()
class NetworkInterface(netutils.NetworkInterface):
    ''' Represents a network interface. '''
    def __init__(self, interface_name):
        netutils.NetworkInterface.__init__(self, interface_name)
        self.dhcp_manager = DhcpManager(interface_name)
    def do_ip_address(self, profile):
        if 'use_static_ip' in profile \
           and profile['use_static_ip']:
            log('do static ip')
            ip_addresses = ['static_ip', 'static_netmask', 'static_dns_1']
            optional = ['static_gateway', 'static_dns_2', 'static_dns_3']
            for field in ip_addresses + optional:
                if not field in profile:
                    raise misc.WicdError('Missing required profile item %s' %
                                         field)
            for field in ip_addresses:
                if not misc.is_valid_ip(profile[field]):
                    raise misc.WicdError('Invalid IP address: %s' 
                                         % network.profile[field])
            for field in optional:
                if not profile[field] is None:
                    if not misc.is_valid_ip(profile[field]):
                        raise misc.WicdError('Invalid IP address: %s' 
                                             % profile[field])
            self.set_ip(profile['static_ip'])
            self.set_netmask(profile['static_netmask'])
            if profile['static_gateway']:
                self.set_gateway(profile['static_gateway'])
            return True
        else:
            log('using dhcp')
            self.dhcp_manager.start()
            return self.dhcp_manager.status()
    def reset(self):
        self.set_ip('0.0.0.0')
        self.down()
