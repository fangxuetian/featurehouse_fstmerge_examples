import os, sys
import misc
class DhcpManager(object):
    def __init__(self, interface_name):
        self.interface_name = interface_name
        path = os.path.dirname(os.path.realpath(__file__))
        self.dhcp_dir = os.path.join(path, 'dhcp')
        self.load_all_available_clients()
    def get_available_client_files(self):
        client_list = []
        for f in os.listdir(self.dhcp_dir):
            if self._valid_client_file(f):
                client_list.append(f)
        return client_list
    def _valid_client_file(self,template):
        ''' Checks if a file is a valid template. '''
        isntbasedhcppy = not (template == 'basedhcp.py')
        endswithpy = template.endswith('.py')
        return isntbasedhcppy and endswithpy
    def load_all_available_clients(self):
        self.clear_all()
        for client in self.get_available_client_files():
            print 'loading dhcp client template %s' % client
            self.load_client(client)
    def load_client(self, client_file):
        if self._valid_client_file(client_file):
            shortname = client_file[:-3]
            sys.path.insert(0, self.dhcp_dir)
            client = __import__(shortname)
            sys.path.remove(self.dhcp_dir)
            for item in dir(client):
                if item.startswith('Dhcp'):
                    client_class = getattr(client, item)
                    if client_class.check():
                        self._clients[item[4:]] = client_class
    def clear_all(self):
        self._clients = {}
    def get_template(self, name):
        return self._clients.get(name)
    def start(self):
        dhcp_client = self._clients.items()[0][1]
        self.dhcp_client = dhcp_client(self.interface_name)
        self.dhcp_client.start()
    def stop(self):
        if hasattr(self, 'dhcp_client'): self.dhcp_client.stop()
    def status(self):
        if not hasattr(self, 'dhcp_client'):
            raise misc.WicdError('Must run start() before status()')
        return self.dhcp_client.status()
    def __copy__(self): return self
    def __deepcopy__(self, memo): return self
