"""Communicate with the LG VX5200 cell phone
The code in this file mainly inherits from VX8100 code and then extends where
the 5200 has different functionality
"""
import time
import cStringIO
import sha
import common
import commport
import copy
import com_lgvx4400
import p_brew
import p_lgvx5200
import com_lgvx8100
import com_brew
import com_phone
import com_lg
import prototypes
import bpcalendar
import call_history
import sms
import memo
class Phone(com_lgvx8100.Phone):
    "Talk to the LG VX5200 cell phone"
    desc="LG-VX5200"
    protocolclass=p_lgvx5200
    serialsname='lgvx5200'
    builtinringtones= ('Low Beep Once', 'Low Beeps', 'Loud Beep Once', 'Loud Beeps', 'VZW Default Ringtone') + \
                      tuple(['Ringtone '+`n` for n in range(1,11)]) + \
                      ('No Ring',)
    ringtonelocations= (
        ( 'ringers', 'dload/ringtone.dat', '', 'user/sound/ringer', 100, 150, 0x201, 1, 0),
        ( 'sounds', 'dload/sound.dat', 'dload/soundsize.dat', 'dload/snd', 100, 150, 0x402, 0, 151),
        )
    calendarlocation="sch/schedule.dat"
    calendarexceptionlocation="sch/schexception.dat"
    calenderrequiresreboot=1
    memolocation="sch/memo.dat"
    builtinwallpapers = () # none
    wallpaperlocations= (
        ( 'images', 'dload/image.dat', 'dload/imagesize.dat', 'dload/img', 100, 50, 0, 0, 0),
        )
    def __init__(self, logtarget, commport):
        com_lgvx4400.Phone.__init__(self,logtarget,commport)
        self.mode=self.MODENONE
    my_model='VX5200'
    def getmedia(self, maps, results, key):
        return com_lg.LGNewIndexedMedia2.getmedia(self, maps, results, key)
    def savemedia(self, mediakey, mediaindexkey, maps, results, merge, reindexfunction):
        return com_lg.LGNewIndexedMedia2.savemedia(self, mediakey, mediaindexkey, maps, results, merge, reindexfunction)
parentprofile=com_lgvx8100.Profile
class Profile(parentprofile):
    protocolclass=Phone.protocolclass
    serialsname=Phone.serialsname
    BP_Calendar_Version=3
    phone_manufacturer='LG Electronics Inc'
    phone_model='VX5200'
    WALLPAPER_WIDTH=275
    WALLPAPER_HEIGHT=175
    MAX_WALLPAPER_BASENAME_LENGTH=32
    WALLPAPER_FILENAME_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_() ."
    WALLPAPER_CONVERT_FORMAT="jpg"
    DIALSTRING_CHARS="[^0-9PW#*]"
    MAX_RINGTONE_BASENAME_LENGTH=32
    RINGTONE_FILENAME_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_() ."
    imageorigins={}
    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "images"))
    def GetImageOrigins(self):
        return self.imageorigins
    imagetargets={}
    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "wallpaper",
                                      {'width': 275, 'height': 175, 'format': "JPEG"}))
    def GetTargetsForImageOrigin(self, origin):
        return self.imagetargets
    def __init__(self):
        parentprofile.__init__(self)
    _supportedsyncs=(
        ('phonebook', 'read', None),  # all phonebook reading
        ('calendar', 'read', None),   # all calendar reading
        ('wallpaper', 'read', None),  # all wallpaper reading
        ('ringtone', 'read', None),   # all ringtone reading
        ('call_history', 'read', None),# all call history list reading
        ('sms', 'read', None),         # all SMS list reading
        ('memo', 'read', None),        # all memo list reading
        ('phonebook', 'write', 'OVERWRITE'),  # only overwriting phonebook
        ('calendar', 'write', 'OVERWRITE'),   # only overwriting calendar
        ('wallpaper', 'write', 'MERGE'),      # merge and overwrite wallpaper
        ('wallpaper', 'write', 'OVERWRITE'),
        ('ringtone', 'write', 'MERGE'),      # merge and overwrite ringtone
        ('ringtone', 'write', 'OVERWRITE'),
        ('sms', 'write', 'OVERWRITE'),        # all SMS list writing
        ('memo', 'write', 'OVERWRITE'),       # all memo list writing
        )
