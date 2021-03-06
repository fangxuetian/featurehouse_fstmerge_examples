import re
import misc
from profilednetutils import ProfiledNetworkInterface, NetworkProfile
import logging
class WirelessRegexPatterns:
    essid       = re.compile('.*ESSID:"(.*?)"\n', re.I | re.M  | re.S)
    ap_mac      = re.compile('.*Address: (.*?)\n', re.I | re.M  | re.S)
    channel     = re.compile('.*Channel:? ?(\d\d?)', re.I | re.M  | re.S)
    strength    = re.compile('.*Quality:?=? ?(\d+)\s*/?\s*(\d*)', re.I | re.M  | re.S)
    altstrength = re.compile('.*Signal level:?=? ?(\d\d*)', re.I | re.M | re.S)
    signaldbm   = re.compile('.*Signal level:?=? ?(-\d\d*)', re.I | re.M | re.S)
    mode        = re.compile('.*Mode:(.*?)\n', re.I | re.M  | re.S)
    freq        = re.compile('.*Frequency:(.*?)\n', re.I | re.M  | re.S)
    bssid       = re.compile('.*Access Point: (([0-9A-Z]{2}:){5}[0-9A-Z]{2})', re.I | re.M | re.S)
    wep         = re.compile('.*Encryption key:(.*?)\n', re.I | re.M  | re.S)
    altwpa      = re.compile('(wpa_ie)', re.I | re.M | re.S)
    wpa1        = re.compile('(WPA Version 1)', re.I | re.M  | re.S)
    wpa2        = re.compile('(WPA2)', re.I | re.M  | re.S)
    auth        = re.compile('.*wpa_state=(.*?)\n', re.I | re.M  | re.S)
class WirelessNetwork(object): pass
class WirelessInterface(ProfiledNetworkInterface):
    ''' Represents a hardware wireless interface. '''
    def __init__(self, interface_name):
        ProfiledNetworkInterface.__init__(self, interface_name)
    def scan(self):
        """ Updates the current wireless network list. """
        self.save_profiles()
        self.current_network = None
        self.networks = self._do_scan()
        for network in self.networks:
            if self.config_manager.has_section(network.bssid):
                logging.debug('%s has profile', network.bssid)
                settings = self.config_manager.items(network.bssid)
                network.profile = NetworkProfile(dict(settings))
            else:
                logging.debug('%s missing profile', network.bssid)
                network.profile = NetworkProfile()
        return self.networks
    def save_profiles(self):
        if hasattr(self, 'networks'):
            for network in self.networks:
                network.profile['bssid'] = network.bssid
                network.profile['essid'] = network.essid
            self._save_config(
                [ network.profile for network in self.networks ],
                'bssid'
            )
    def _do_scan(self):
        self.up()
        cmd = [misc.find_program_in_path('iwlist'),
                              self.interface_name,
                              'scan']
        results = misc.run(cmd)
        networks = results.split( '   Cell ' )
        access_points = []
        for cell in networks:
            if 'ESSID:' in cell:
                entry = self._parse_access_point(cell)
                if entry is not None:
                    access_points.append(entry)
        return access_points
    def _parse_access_point(self, cell):
        """ Parse a single cell from the output of iwlist.
        Keyword arguments:
        cell -- string containing the cell information
        Returns:
        A dictionary containing the cell networks properties.
        """
        ap = WirelessNetwork()
        ap.essid = misc.run_regex(WirelessRegexPatterns.essid, cell)
        try:
            ap.essid = misc.to_unicode(ap.essid)
        except (UnicodeDecodeError, UnicodeEncodeError):
            logging.debug('Unicode problem with current network essid, ignoring!!')
            return None
        if ap.essid in ['<hidden>', ""]:
            ap.essid = 'Hidden'
            ap.hidden = True
        else:
            ap.hidden = False
        ap.channel = misc.run_regex(WirelessRegexPatterns.channel, cell)
        if ap.channel == None:
            freq = misc.run_regex(WirelessRegexPatterns.freq, cell)
            ap.channel = self._freq_to_channel(freq)
        ap.bssid = misc.run_regex(WirelessRegexPatterns.ap_mac, cell)
        ap.mode = misc.run_regex(WirelessRegexPatterns.mode, cell)
        if (WirelessRegexPatterns.strength.match(cell)):
            [(strength, max_strength)] = WirelessRegexPatterns.strength.findall(cell)
            if max_strength:
                ap.quality = 100 * int(strength) // int(max_strength)
            else:
                ap.quality = int(strength)
        elif misc.run_regex(WirelessRegexPatterns.altstrength,cell):
            ap.quality = misc.run_regex(WirelessRegexPatterns.altstrength, cell)
        else:
            ap.quality = -1
        if misc.run_regex(WirelessRegexPatterns.signaldbm, cell):
            ap.strength = misc.run_regex(WirelessRegexPatterns.signaldbm, cell)
        return ap
    def _freq_to_channel(self, freq):
        """ Translate the specified frequency to a channel.
        Note: This function is simply a lookup dict and therefore the
        freq argument must be in the dict to provide a valid channel.
        Keyword arguments:
        freq -- string containing the specified frequency
        Returns:
        The channel number, or None if not found.
        """
        ret = None
        freq_dict = {'2.412 GHz': 1, '2.417 GHz': 2, '2.422 GHz': 3,
                         '2.427 GHz': 4, '2.432 GHz': 5, '2.437 GHz': 6,
                         '2.442 GHz': 7, '2.447 GHz': 8, '2.452 GHz': 9,
                         '2.457 GHz': 10, '2.462 GHz': 11, '2.467 GHz': 12,
                         '2.472 GHz': 13, '2.484 GHz': 14 }
        try:
            ret = freq_dict[freq]
        except KeyError:
            logging.error("couldn't determine channel number for frequency: %s",
                          freq)
        return ret
