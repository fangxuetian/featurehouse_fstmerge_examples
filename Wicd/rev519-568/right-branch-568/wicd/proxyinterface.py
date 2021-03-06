from dbusmanager import set, get, do, prepare_data_for_dbus
import logging
class ProxyInterface(object):
    def __init__(self, interface_name):
        self.interface_name = interface_name
    def __str__(self):
        return '<Proxy Interface %s>' % self.interface_name
    def __getattr__(self, name):
        access_method = None
        method_name = None
        methods = {
            'do' : do,
            'set' : set,
            'get' : get
            }
        if name.startswith('get_') or name.startswith('set_') \
           or name.startswith('do_'):
            access_method = methods[name[:name.index('_')]]
            method_name = name[name.index('_')+1:]
        else:
            raise AttributeError('No such method: %s' % name)
        def proxy_method(*args):
            logging.debug('%s %s %s %s' % (method, self.interface_name, method_name, args))
            return access_method(self.interface_name, method_name, prepare_data_for_dbus(args))
        method = proxy_method
        method.func_name = name
        return method
