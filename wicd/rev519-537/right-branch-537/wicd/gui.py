""" Wicd GUI module.
Module containg all the code (other than the tray icon) related to the
Wicd user interface.
"""
import os
import sys
import time
import gobject
import pango
import gtk
import gtk.glade
from dbus import Dictionary as DBusDictionary
from dbus import DBusException
from dbus import version as dbus_version
from wicd import wpath
from dbusmanager import DBusManager
from xmlui import simpleinterfacegtk as simpleinterface
from wicd import misc
misc.RenameProcess('wicd-gui')
dbus_manager = DBusManager()
dbus_manager.connect_to_dbus()
dbus_ifaces = dbus_manager.get_dbus_ifaces()
daemon = dbus_ifaces['daemon']
interface = dbus_ifaces['interface']
ui = dbus_ifaces['ui']
if __name__ == '__main__':
    wpath.chdir(__file__)
try:
    import pygtk
    pygtk.require("2.0")
except:
    pass
if not dbus_version or (dbus_version < (0, 80, 0)):
    import dbus.glib
else:
    from dbus.mainloop.glib import DBusGMainLoop
    DBusGMainLoop(set_as_default=True)
def error(parent, message):
    """ Shows an error dialog """
    dialog = gtk.MessageDialog(parent, gtk.DIALOG_MODAL, gtk.MESSAGE_ERROR,
                               gtk.BUTTONS_OK)
    dialog.set_markup(message)
    dialog.run()
    dialog.destroy()
class CallbackProxy(object):
    def __init__(self, interface_name):
        print 'init CallbackProxy', interface_name
        self.interface_name = interface_name
    def __getattribute__(self, name):
        """ Returns an attribute that already exists, or makes one up. """
        try:
            return object.__getattribute__(self, name)
        except:
            pass
        def catch_all_callback(self, callback_object):
            value_dict = {}
            for key, value in self.controls.iteritems():
                if hasattr(value, 'get_value'):
                    value_dict[key] = value.get_value()
            if value_dict == {}:
                value_dict = { "nothing" : "nothing" }
            return ui.CallCallbackHandler(self.interface_name, name, value_dict)
        return catch_all_callback
class appGui(object):
    """ The main wicd GUI class. """
    def __init__(self, dbus_man=None, standalone=False):
        """ Initializes everything needed for the GUI. """
        gladefile = wpath.share + "wicd.glade"
        self.wTree = gtk.glade.XML(gladefile)
        dic = { "refresh_clicked" : self.refresh,
                "quit_clicked" : self.exit, }
        self.wTree.signal_autoconnect(dic)
        if os.path.exists(wpath.etc + "wicd.png"):
            self.window.set_icon_from_file(wpath.etc + "wicd.png")
        self.window = self.wTree.get_widget("window1")
        self.network_list = self.wTree.get_widget("network_list_vbox")
        self.status_area = self.wTree.get_widget("connecting_hbox")
        self.status_bar = self.wTree.get_widget("statusbar")
        self.status_area.hide_all()
    def exit(self, button=None):
        sys.exit()
    def refresh(self, button=None):
        print 'Refreshing...'
        ui = dbus_ifaces['interface'].GetInterfaceData('wired1', 'user_interface')
        cbh = CallbackProxy('wired1')
        gtkinterface, controls = simpleinterface.generate_interface(ui, cbh)
        cbh.controls = controls
        print gtkinterface
        self.network_list.pack_start(gtkinterface.control)
        self.network_list.show_all()
if __name__ == '__main__':
    app = appGui(standalone=True)
    gtk.main()
