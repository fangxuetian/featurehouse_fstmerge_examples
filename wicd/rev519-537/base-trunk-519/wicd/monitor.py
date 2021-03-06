""" monitor -- connection monitoring process
This process is spawned as a child of the daemon, and is responsible
for monitoring connection status and initiating autoreconnection
when appropriate.
"""
import dbus
import gobject
import time
import sys
if getattr(dbus, 'version', (0, 0, 0)) < (0, 80, 0):
    import dbus.glib
else:
    from dbus.mainloop.glib import DBusGMainLoop
    DBusGMainLoop(set_as_default=True)
import wicd.wpath as wpath
import wicd.misc as misc
misc.RenameProcess("wicd-monitor")
if __name__ == '__main__':
    wpath.chdir(__file__)
proxy_obj = dbus.SystemBus().get_object('org.wicd.daemon', '/org/wicd/daemon')
daemon = dbus.Interface(proxy_obj, 'org.wicd.daemon')
wired = dbus.Interface(proxy_obj, 'org.wicd.daemon.wired')
wireless = dbus.Interface(proxy_obj, 'org.wicd.daemon.wireless')
class ConnectionStatus(object):
    """ Class for monitoring the computer's connection status. """
    def __init__(self):
        """ Initialize variables needed for the connection status methods. """
        self.last_strength = -2
        self.displayed_strength = -1
        self.still_wired = False
        self.network = ''
        self.tried_reconnect = False
        self.connection_lost_counter = 0
        self.dbus_error_count = 0
        self.last_state = misc.NOT_CONNECTED
        self.reconnecting = False
        self.reconnect_tries = 0
        self.last_reconnect_time = time.time()
        self.signal_changed = False
        self.iwconfig = ""
    def check_for_wired_connection(self, wired_ip):
        """ Checks for an active wired connection.
        Checks for and updates the tray icon for an active wired connection
        Returns True if wired connection is active, false if inactive.
        """
        if wired_ip and wired.CheckPluggedIn():
            if not self.still_wired:
                daemon.SetCurrentInterface(daemon.GetWiredInterface())
                self.still_wired = True
            return True
        self.still_wired = False
        return False
    def check_for_wireless_connection(self, wireless_ip):
        """ Checks for an active wireless connection.
        Checks for and updates the tray icon for an active
        wireless connection.  Returns True if wireless connection
        is active, and False otherwise.
        """
        if wireless_ip is None:
            return False
        self.tried_reconnect = False
        try:
            if daemon.GetSignalDisplayType() == 0:
                wifi_signal = int(wireless.GetCurrentSignalStrength(self.iwconfig))
            else:
                wifi_signal = int(wireless.GetCurrentDBMStrength(self.iwconfig))
        except:
            wifi_signal = 0
        if wifi_signal == 0:
            self.connection_lost_counter += 1
            if self.connection_lost_counter >= 4:
                print "Connection lost for too long, setting status to disconnected."
                self.connection_lost_counter = 0
                return False
        else:
            self.connection_lost_counter = 0
        if (wifi_signal != self.last_strength or
            self.network != wireless.GetCurrentNetwork(self.iwconfig)):
            self.last_strength = wifi_signal
            self.signal_changed = True
            daemon.SetCurrentInterface(daemon.GetWirelessInterface())
        return True
    def update_connection_status(self):
        """ Updates the tray icon and current connection status.
        Determines the current connection state and sends a dbus signal
        announcing when the status changes.  Also starts the automatic
        reconnection process if necessary.
        """
        wired_ip = None
        wifi_ip = None
        try:
            if daemon.GetSuspend():
                print "Suspended."
                state = misc.SUSPENDED
                self.update_state(state)
                return True
            if daemon.CheckIfConnecting():
                state = misc.CONNECTING
                self.update_state(state)
                return True
            wired_ip = wired.GetWiredIP()
            wired_found = self.check_for_wired_connection(wired_ip)
            if wired_found:
                self.update_state(misc.WIRED, wired_ip=wired_ip)
                return True
            wifi_ip = wireless.GetWirelessIP()
            self.iwconfig = wireless.GetIwconfig()
            self.signal_changed = False
            wireless_found = self.check_for_wireless_connection(wifi_ip)
            if wireless_found:
                self.update_state(misc.WIRELESS, wifi_ip=wifi_ip)
                return True
            state = misc.NOT_CONNECTED
            if self.last_state == misc.WIRELESS:
                from_wireless = True
            else:
                from_wireless = False
            self.auto_reconnect(from_wireless)
            self.update_state(state)
            self.dbus_error_count = 0
        except dbus.exceptions.DBusException, e:
            print 'Ignoring DBus Error: ' + str(e)
            self.dbus_error_count += 1
            if self.dbus_error_count > 5:
                raise dbus.exceptions.DBusException(e)
        finally:
            return True
    def update_state(self, state, wired_ip=None, wifi_ip=None):
        """ Set the current connection state. """
        if state == misc.NOT_CONNECTED:
            info = [""]
        elif state == misc.SUSPENDED:
            info = [""]
        elif state == misc.CONNECTING:
            if wired.CheckIfWiredConnecting():
                info = ["wired"]
            else:
                info = ["wireless", str(wireless.GetCurrentNetwork(self.iwconfig))]
        elif state == misc.WIRELESS:
            self.reconnect_tries = 0
            info = [str(wifi_ip), str(wireless.GetCurrentNetwork(self.iwconfig)),
                    str(wireless.GetPrintableSignalStrength(self.iwconfig)),
                    str(wireless.GetCurrentNetworkID(self.iwconfig))]
        elif state == misc.WIRED:
            self.reconnect_tries = 0
            info = [str(wired_ip)]
        else:
            print 'ERROR: Invalid state!'
            return True
        daemon.SetConnectionStatus(state, info)
        if state != self.last_state or \
           (state == misc.WIRELESS and self.signal_changed):
            daemon.EmitStatusChanged(state, info)
        self.last_state = state
        return True
    def auto_reconnect(self, from_wireless=None):
        """ Automatically reconnects to a network if needed.
        If automatic reconnection is turned on, this method will
        attempt to first reconnect to the last used wireless network, and
        should that fail will simply run AutoConnect()
        """
        if self.reconnecting:
            return
        if self.reconnect_tries > 2 and \
           time.time() - self.last_reconnect_time < 30:
            return
        self.reconnecting = True
        daemon.SetCurrentInterface('')
        if daemon.ShouldAutoReconnect():
            print 'Starting automatic reconnect process'
            self.last_reconnect_time = time.time()
            self.reconnect_tries += 1
            cur_net_id = wireless.GetCurrentNetworkID(self.iwconfig)
            if from_wireless and cur_net_id > -1:
                print 'Trying to reconnect to last used wireless ' + \
                       'network'
                wireless.ConnectWireless(cur_net_id)
            else:
                daemon.AutoConnect(True, reply_handler=reply_handle,
                                   error_handler=err_handle)
        self.reconnecting = False
    def rescan_networks(self):
        """ Calls a wireless scan. """
        try:
            if daemon.GetSuspend() or daemon.CheckIfConnecting():
                return True
            wireless.Scan()
        except dbus.exceptions.DBusException:
            print 'dbus exception while attempting rescan'
        finally:
            return True
def reply_handle():
    """ Just a dummy function needed for asynchronous dbus calls. """
    pass
def err_handle(error):
    """ Just a dummy function needed for asynchronous dbus calls. """
    pass
def main(args):
    """ Starts the connection monitor.
    Starts a ConnectionStatus instance, sets the status to update
    every two seconds, and sets a wireless scan to be called every
    two minutes.
    """
    monitor = ConnectionStatus()
    try:
        gobject.timeout_add_seconds(4, monitor.update_connection_status)
    except:
        gobject.timeout_add(4000, monitor.update_connection_status)
    mainloop = gobject.MainLoop()
    mainloop.run()
if __name__ == '__main__':
    main(sys.argv[1:])
