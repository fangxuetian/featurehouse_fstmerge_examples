"""Communicate with the LG 6200 (Sprint) cell phone"""

import re

import time

import cStringIO

import sha

import p_lglg6200

import p_brew

import common

import commport

import com_brew

import com_phone

import com_lg

import com_lgvx4400

import com_lgpm225

import prototypes

import call_history

import sms

import fileinfo

import memo

class  Phone (com_lgpm225.Phone) :
	"Talk to the LG 6200 cell phone"
	    desc="LG 6200"
	    protocolclass=p_lglg6200
	    serialsname='lg6200'
	    imagelocations=(
        (0x600, "setas/dcamIndex.map", "Dcam/Wallet", "camera", 50, 6),
        )
	    ringtonelocations=(
        (0x1100, "setas/voicememoRingerIndex.map", "VoiceDB/All/Memos", "voice_memo", 50, 11),
        )
	    builtinimages=()
	    builtinringtones=( 'Ring 1', 'Ring 2', 'Ring 3', 'Ring 4', 'Ring 5', 'Ring 6',
                       'Alert 1', 'Alert 2', 'Alert 3', 'Alert 4', 'Alert 5', 'Alert 6',
                       'Jazztic', 'Rock & Roll', 'Grand waltz', 'Toccata and Fugue',
                       'Sunday afternoon', 'Bumble bee', 'Circus band', 'Cuckoo waltz',
                       'Latin', 'CanCan', 'Play tag', 'Eine kleine Nacht', 'Symphony No.25 in G Minor',
                       'Capriccio a minor', 'Moon light', 'A nameless girl', 'From the new world', 
                       'They called me Elvis')
	    def __init__(self, logtarget, commport):

        "Calls all the constructors and sets initial modes"

        com_phone.Phone.__init__(self, logtarget, commport)

        com_brew.BrewProtocol.__init__(self)

        com_lg.LGPhonebook.__init__(self)

        self.log("Attempting to contact phone")

        self._cal_has_voice_id=hasattr(self.protocolclass, 'cal_has_voice_id') \
                                and self.protocolclass.cal_has_voice_id

        self.mode=self.MODENONE

	brew_version_file='nvm/nvm/nvm_lg_param'
	    brew_version_txt_key='LG6200_version_data'
	    my_model='LG_LG6200'
	    def eval_detect_data(self, res):

        found=False

        if res.get(self.brew_version_txt_key, None) is not None:

            found=res[self.brew_version_txt_key][0x5b1:0x5b1+len(self.my_model)]==self.my_model

        if found:

            res['model']=self.my_model

            res['manufacturer']='LG Electronics Inc'

            s=res.get(self.esn_file_key, None)

            if s:

                res['esn']=self.get_esn(s)

	def getphoneinfo(self, phone_info):

        self.log('Getting Phone Info')

        try:

            s=self.getfilecontents(self.brew_version_file)

            if s[0x5b1:0x5b1+len(self.my_model)]==self.my_model:

                phone_info.append('Model:', self.my_model)

                phone_info.append('ESN:', self.get_brew_esn())

                req=p_brew.firmwarerequest()

                txt=self.getfilecontents("nvm/nvm/nvm_0000")[0x241:0x24b]

                phone_info.append('Phone Number:', txt)

        except:

            pass

        return

	"Talk to the LG 6200 cell phone"
parentprofile=com_lgpm225.Profile
class  Profile (parentprofile) :
	protocolclass=Phone.protocolclass
	    serialsname=Phone.serialsname
	    BP_Calendar_Version=3
	    phone_manufacturer='LG Electronics Inc'
	    phone_model='LG_LG6200'
	    brew_required=True
	    RINGTONE_LIMITS= {
        'MAXSIZE': 250000
    }
	    WALLPAPER_WIDTH=160
	    WALLPAPER_HEIGHT=120
	    MAX_WALLPAPER_BASENAME_LENGTH=30
	    WALLPAPER_FILENAME_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .-_"
	    WALLPAPER_CONVERT_FORMAT="jpg"
	    MAX_RINGTONE_BASENAME_LENGTH=30
	    RINGTONE_FILENAME_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .-_"
	    DIALSTRING_CHARS="[^0-9PT#*]"
	    imagetargets={}
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "wallpaper",
                                      {'width': 128, 'height': 160, 'format': "JPEG"}))
	    _supportedsyncs=(
        ('phonebook', 'read', None),  
        ('calendar', 'read', None),   
        ('wallpaper', 'read', None),  
        ('ringtone', 'read', None),   
        ('call_history', 'read', None),
        ('memo', 'read', None),        
        ('sms', 'read', None),         
        ('phonebook', 'write', 'OVERWRITE'),  
        ('calendar', 'write', 'OVERWRITE'),   
        ('wallpaper', 'write', 'MERGE'),      
        ('wallpaper', 'write', 'OVERWRITE'),
        ('ringtone', 'write', 'MERGE'),      
        ('ringtone', 'write', 'OVERWRITE'),
        ('memo', 'write', 'OVERWRITE'),       
        ('sms', 'write', 'OVERWRITE'),        
        )
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "wallpaper",
                                      {'width': 128, 'height': 160, 'format': "JPEG"}))

