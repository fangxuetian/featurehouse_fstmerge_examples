""" Misc - miscellaneous functions for wicd """
import os
import locale
import gettext
import sys
from subprocess import Popen, STDOUT, PIPE, call
import subprocess
import commands
import wpath
from logfile import log
from globalconfig import global_config
if __name__ == '__main__':
    wpath.chdir(__file__)
NOT_CONNECTED = 0
CONNECTING = 1
WIRELESS = 2
WIRED = 3
SUSPENDED = 4
AUTO = 0
DHCLIENT = 1
DHCPCD = 2
PUMP = 3
ETHTOOL = 1
MIITOOL = 2
IP = 1
ROUTE = 2
class WicdError(Exception): pass
class WicdSecurityException(WicdError): pass
def Run(cmd, include_stderr=False, return_pipe=False, return_fileobject=False):
    """ Run a command.
    Runs the given command, returning either the output
    of the program, or a pipe to read output from.
    keyword arguments --
    cmd - The command to execute
    include_std_err - Boolean specifying if stderr should
                      be included in the pipe to the cmd.
    return_pipe - Boolean specifying if a pipe to the
                  command should be returned.  If it is
                  false, all that will be returned is
                  one output string from the command.
    """
    raise NotImplementedError('Please use misc.run instead of this function')
def run(cmd, include_stderr=False, return_pipe=False, return_fileobject=False):
    if type(cmd) is not list:
        cmd = to_unicode(str(cmd))
        cmd = cmd.split()
    if global_config.get_option('misc', 'printcommands', True):
        log(cmd)
    if include_stderr:
        err = STDOUT
        fds = True
    else:
        err = None
        fds = False
    tmpenv = os.environ.copy()
    tmpenv["LC_ALL"] = "C"
    tmpenv["LANG"] = "C"
    f = Popen(cmd, shell=False, stdout=PIPE, stderr=err, close_fds=fds, cwd='/',
              env=tmpenv)
    if return_fileobject:
        return f
    if return_pipe:
        return f.stdout
    else:
        return f.communicate()[0]
def convert_string_to_proper(self, string):
    if string in ['None', None]:
        return None
    elif string in ['True', 'true', True]:
        return True
    elif string in ['False', 'false', False]:
        return False
    else:
        return string
def smart_type(value):
    try:
        value = int(value)
    except (ValueError, TypeError):
        if value in ['True', 'true', True]:
            value = True
        elif value in ['False', 'false', False]:
            value = False
        else:
            value = stringToNone(value)
    return value
def LaunchAndWait(cmd):
    """ Launches the given program with the given arguments, then blocks.
    cmd : A list contained the program name and its arguments.
    """
    call(cmd)
def find_program_in_path(program):
    """ Determines the full path for the given program.
    Searches a hardcoded list of paths for a given program name.
    Keyword arguments:
    program -- The name of the program to search for
    Returns:
    The full path of the program or None
    """
    try:
        paths = os.environ['PATH'].split(':')
    except:
        log( 'using $PATH failed, using preselected locations')
        paths = ['/sbin/', '/usr/sbin/', '/bin/', '/usr/bin/',
                 '/usr/local/sbin/', '/usr/local/bin/']
    for path in paths:
        if os.access(os.path.join(path, program), os.F_OK):
            return os.path.join(path, program)
    return None
def is_valid_ip(ip):
    """ Make sure an entered IP is valid. """
    if ip != None:
        if ip.count('.') == 3:
            ipNumbers = ip.split('.')
            for number in ipNumbers:
                if not number.isdigit() or int(number) > 255:
                    return False
            return ipNumbers
    return False
def run_regex(regex, string):
    """ runs a regex search on a string """
    m = regex.search(string)
    if m:
        return m.groups()[0]
    else:
        return None
def ExecuteScript(script):
    """ Execute a command and send its output to the bit bucket. """
    os.system("%s > /dev/null 2>&1" % script)
def to_bool(var):
    """ Convert a string to type bool, but make "False"/"0" become False. """
    if var in ("False", "0"):
        var = False
    else:
        var = bool(var)
    return var
def Noneify(variable):
    """ Convert string types to either None or booleans"""
    if variable in ("None", "", None):
        return None
    if variable in ("False", "0"):
        return False
    if variable in ("True", "1"):
        return True
    return variable
def noneToString(text):
    """ Convert None, "None", or "" to string type "None"
    Used for putting text in a text box.  If the value to put in is 'None',
    the box will be blank.
    """
    if text in (None, ""):
        return "None"
    else:
        return str(text)
def get_gettext():
    """ Set up gettext for translations. """
    local_path = os.path.realpath(os.path.dirname(sys.argv[0])) + \
                 '/translations'
    langs = []
    lc, encoding = locale.getdefaultlocale()
    if (lc):
        langs = [lc]
    osLanguage = os.environ.get('LANGUAGE', None)
    if (osLanguage):
        langs += osLanguage.split(":")
    langs += ["en_US"]
    lang = gettext.translation('wicd', local_path, languages=langs, 
                               fallback=True)
    _ = lang.gettext
    return _
def to_unicode(x):
    """ Attempts to convert a string to utf-8. """
    try: 
        encoding = locale.getpreferredencoding()
    except:
        encoding = 'utf-8'
    ret = x.decode(encoding, 'replace').encode('utf-8')
    return ret
def RenameProcess(new_name):
    if sys.platform != 'linux2':
        log( 'Unsupported platform')
        return False
    try:
        import ctypes
        is_64 = os.path.exists('/lib64/libc.so.6')
        if is_64:
            libc = ctypes.CDLL('/lib64/libc.so.6')
        else:
            libc = ctypes.CDLL('/lib/libc.so.6')
        libc.prctl(15, new_name, 0, 0, 0)
        return True
    except:
        return False
def get_language_list_gui():
    """ Returns a dict of translatable strings used by the GUI.
    translations are done at http://wicd.net/translator. Please 
    translate if you can.
    """
    _ = get_gettext()
    language = {}
    language['connect'] = _("Connect")
    language['ip'] = _("IP")
    language['netmask'] = _("Netmask")
    language['gateway'] = _('Gateway')
    language['dns'] = _('DNS')
    language['use_static_ip'] = _('Use Static IPs')
    language['use_static_dns'] = _('Use Static DNS')
    language['use_encryption'] = _('Use Encryption')
    language['advanced_settings'] = _('Advanced Settings')
    language['wired_network'] = _('Wired Network')
    language['wired_network_instructions'] = _('To connect to a wired network,'
    ' you must create a network profile. To create a network profile, type a'
    ' name that describes this network, and press Add.')
    language['automatic_connect'] = _('Automatically connect to this network')
    language['secured'] = _('Secured')
    language['unsecured'] = _('Unsecured')
    language['channel'] = _('Channel')
    language['preferences'] = _('Preferences')
    language['wpa_supplicant_driver'] = _('WPA Supplicant Driver')
    language['wireless_interface'] = _('Wireless Interface')
    language['wired_interface'] = _('Wired Interface')
    language['hidden_network'] = _('Hidden Network')
    language['hidden_network_essid'] = _('Hidden Network ESSID')
    language['connected_to_wireless'] = _('Connected to $A at $B (IP: $C)')
    language['connected_to_wired'] = _('Connected to wired network (IP: $A)')
    language['not_connected'] = _('Not connected')
    language['no_wireless_networks_found'] = _('No wireless networks found.')
    language['killswitch_enabled'] = _('Wireless Kill Switch Enabled')
    language['key'] = _('Key')
    language['username'] = _('Username')
    language['password'] = _('Password')
    language['anonymous_identity'] = _('Anonymous Identity')
    language['identity'] = _('Identity')
    language['authentication'] = _('Authentication')
    language['path_to_pac_file'] = _('Path to PAC File')
    language['select_a_network'] = _('Choose from the networks below:')
    language['connecting'] = _('Connecting...')
    language['wired_always_on'] = _('Always show wired interface')
    language['auto_reconnect'] = _('Automatically reconnect on connection loss')
    language['create_adhoc_network'] = _('Create an Ad-Hoc Network')
    language['essid'] = _('ESSID')
    language['use_wep_encryption'] = _('Use Encryption (WEP only)')
    language['before_script'] = _('Run script before connect')
    language['after_script'] = _('Run script after connect')
    language['disconnect_script'] = _('Run disconnect script')
    language['script_settings'] = _('Scripts')
    language['use_ics'] = _('Activate Internet Connection Sharing')
    language['madwifi_for_adhoc'] = _('Check if using madwifi/atheros drivers')
    language['default_wired'] = _('Use as default profile (overwrites any previous default)')
    language['use_debug_mode'] = _('Enable debug mode')
    language['use_global_dns'] = _('Use global DNS servers')
    language['use_default_profile'] = _('Use default profile on wired autoconnect')
    language['show_wired_list'] = _('Prompt for profile on wired autoconnect')
    language['use_last_used_profile'] = _('Use last used profile on wired autoconnect')
    language['choose_wired_profile'] = _('Select or create a wired profile to connect with')
    language['wired_network_found'] = _('Wired connection detected')
    language['stop_showing_chooser'] = _('Stop Showing Autoconnect pop-up temporarily')
    language['display_type_dialog'] = _('Use dBm to measure signal strength')
    language['scripts'] = _('Scripts')
    language['invalid_address'] = _('Invalid address in $A entry.')
    language['global_settings'] = _('Use these settings for all networks sharing this essid')
    language['encrypt_info_missing'] = _('Required encryption information is missing.')
    language['enable_encryption'] = _('This network requires encryption to be enabled.')
    language['wicd_auto_config'] = _('Automatic (recommended)')
    language["gen_settings"] = _("General Settings")
    language["ext_programs"] = _("External Programs")
    language["dhcp_client"] = _("DHCP Client")
    language["wired_detect"] = _("Wired Link Detection")
    language["route_flush"] = _("Route Table Flushing")
    language["backend"] = _("Backend")
    language["backend_alert"] = _("Changes to your backend won't occur until the daemon is restarted.")
    language['0'] = _('0')
    language['1'] = _('1')
    language['2'] = _('2')
    language['3'] = _('3')
    language['4'] = _('4')
    language['5'] = _('5')
    language['6'] = _('6')
    language['7'] = _('7')
    language['8'] = _('8')
    language['9'] = _('9')
    language['interface_down'] = _('Putting interface down...')
    language['resetting_ip_address'] = _('Resetting IP address...')
    language['interface_up'] = _('Putting interface up...')
    language['setting_encryption_info'] = _('Setting encryption info')
    language['removing_old_connection'] = _('Removing old connection...')
    language['generating_psk'] = _('Generating PSK...')
    language['generating_wpa_config'] = _('Generating WPA configuration file...')
    language['flushing_routing_table'] = _('Flushing the routing table...')
    language['configuring_interface'] = _('Configuring wireless interface...')
    language['validating_authentication'] = _('Validating authentication...')
    language['setting_broadcast_address'] = _('Setting broadcast address...')
    language['setting_static_dns'] = _('Setting static DNS servers...')
    language['setting_static_ip'] = _('Setting static IP addresses...')
    language['running_dhcp'] = _('Obtaining IP address...')
    language['dhcp_failed'] = _('Connection Failed: Unable to Get IP Address')
    language['aborted'] = _('Connection Cancelled')
    language['bad_pass'] = _('Connection Failed: Bad password')
    language['done'] = _('Done connecting...')
    return language
def get_language_list_tray():
    _ = get_gettext()
    language = {}
    language['connected_to_wireless'] = _('Connected to $A at $B (IP: $C)')
    language['connected_to_wired'] = _('Connected to wired network (IP: $A)')
    language['not_connected'] = _('Not connected')
    language['killswitch_enabled'] = _('Wireless Kill Switch Enabled')
    language['connecting'] = _('Connecting')
    language['wired'] = _('Wired Network')
    language['scanning'] = _('Scanning')
    language['no_wireless_networks_found'] = _('No wireless networks found.')
    return language
def noneToBlankString(text):
    """ Converts NoneType or "None" to a blank string. """
    if text in (None, "None"):
        return ""
    else:
        return str(text)
def stringToNone(text):
    """ Performs opposite function of noneToString. """
    if text in ("", None, "None"):
        return None
    else:
        return str(text)
def stringToBoolean(text):
    """ Turns a string representation of a bool to a boolean if needed. """
    if text in ("True", "1"):
        return True
    if text in ("False", "0"):
        return False
    return text
