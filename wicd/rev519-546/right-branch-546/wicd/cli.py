from dbusmanager import DBusManager
import optparse
dbus_manager = DBusManager()
dbus_manager.connect_to_dbus()
dbus_ifaces = dbus_manager.get_dbus_ifaces()
p = optparse.OptionParser()
p.add_option('--load-configuration', '', action='store_true')
p.add_option('--save-configuration', '', action='store_true')
p.add_option('--create-interface', '', action='store_true')
p.add_option('--update', '', action='store_true')
p.add_option('--get-status', '', action='store_true')
p.add_option('--get-data', '-g', default='')
p.add_option('--set-data', '-s', default='')
p.add_option('--do-action', '-p', default='')
p.add_option('--data', '-d', default=('dbusdoesntallowemptytuples',))
p.add_option('--interface-name', '-i', default='')
p.add_option('--interface-type', '-t', default='')
options, arguments = p.parse_args()
if not options.data == ('dbusdoesntallowemptytuples',):
    split = options.data.split('|')
    options.data = tuple(split)
if options.load_configuration:
    dbus_ifaces['daemon'].LoadConfiguration()
elif options.save_configuration:
    dbus_ifaces['daemon'].SaveConfiguration()
elif options.create_interface:
    dbus_ifaces['interface'].CreateNewInterface(options.interface_type,
                                            options.interface_name)
elif options.update:
    dbus_ifaces['interface'].UpdateInterfaces()
elif options.get_status:
    for interface_name in dbus_ifaces['interface'].GetInterfaces():
        print interface_name, ':', dbus_ifaces['interface'].GetInterfaceData(interface_name,
                                        'status')
elif options.get_data:
    print dbus_ifaces['interface'].GetInterfaceData(options.interface_name,
                                                       options.get_data,
                                                       options.data)
elif options.set_data:
    dbus_ifaces['interface'].SetInterfaceData(options.interface_name,
                                                       options.set_data,
                                                       options.data)
elif options.do_action:
    dbus_ifaces['interface'].DoInterfaceAction(options.interface_name,
                                                       options.do_action,
                                                       options.data)
