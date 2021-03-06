"""Communicate with the LG VX6000 cell phone
The VX6000 is substantially similar to the VX4400 except that it supports more
image formats, has wallpapers in no less than 5 locations and puts things in
slightly different directories.
The code in this file mainly inherits from VX4400 code and then extends where
the 6000 has extra functionality
"""

import time

import cStringIO

import sha

import common

import copy

import p_lgvx6000

import com_lgvx4400

import com_brew

import com_phone

import com_lg

import prototypes

class  Phone (com_lgvx4400.Phone) :
	"Talk to the LG VX6000 cell phone"
	    desc="LG-VX6000"
	    protocolclass=p_lgvx6000
	    serialsname='lgvx6000'
	    imagelocations=(
        ( 10, "download/dloadindex/brewImageIndex.map", "brew/shared", "images", 30) ,
        ( 0xc8, "download/dloadindex/mmsImageIndex.map", "brew/shared/mms", "mms", 20),
        ( 0xdc, "download/dloadindex/mmsDrmImageIndex.map", "brew/shared/mms/d", "drm", 20), 
        ( 0x82, None, None, "camera", 20) 
        )
	    ringtonelocations=(
        ( 50, "download/dloadindex/brewRingerIndex.map", "user/sound/ringer", "ringers", 30),
        ( 150, "download/dloadindex/mmsRingerIndex.map", "mms/sound", "mms", 20),
        ( 180, "download/dloadindex/mmsDrmRingerIndex.map", "mms/sound/drm", "drm", 20)
        )
	    builtinimages= ('Beach Ball', 'Towerbridge', 'Sunflower', 'Beach',
                    'Fish', 'Sea', 'Snowman')
	    builtinringtones= ('Ring 1', 'Ring 2', 'Ring 3', 'Ring 4', 'Ring 5', 'Ring 6',
                       'Annen Polka', 'Leichte Kavallerie Overture', 'CanCan',
                       'Paganini', 'Bubble', 'Fugue',
                       'Polka', 'Mozart Symphony No. 40', 'Cuckoo Waltz', 'Rodetzky',
                       'Funicula', 'Hallelujah', 'Trumpets', 'Trepak', 'Prelude', 'Mozart Aria',
                       'William Tell overture', 'Spring', 'Slavonic', 'Fantasy')
	    def __init__(self, logtarget, commport):

        com_lgvx4400.Phone.__init__(self,logtarget,commport)

        self.mode=self.MODENONE

	def getcameraindex(self):

        buf=prototypes.buffer(self.getfilecontents("cam/pics.dat"))

        index={}

        g=self.protocolclass.campicsdat()

        g.readfrombuffer(buf)

        for i in g.items:

            if len(i.name):

                index[i.index]={'name': "pic%02d.jpg"%(i.index,), 'date': i.taken, 'origin': 'camera' }

        return index

	my_model='VX6000'
	"Talk to the LG VX6000 cell phone"
parentprofile=com_lgvx4400.Profile
class  Profile (parentprofile) :
	protocolclass=Phone.protocolclass
	    serialsname=Phone.serialsname
	    phone_manufacturer='LG Electronics Inc'
	    phone_model='VX6000'
	    WALLPAPER_WIDTH=120
	    WALLPAPER_HEIGHT=131
	    MAX_WALLPAPER_BASENAME_LENGTH=32
	    WALLPAPER_FILENAME_CHARS="_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ."
	    WALLPAPER_CONVERT_FORMAT="bmp"
	    MAX_RINGTONE_BASENAME_LENGTH=32
	    RINGTONE_FILENAME_CHARS="_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ."
	    imageorigins={}
	    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "images"))
	    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "mms"))
	    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "drm"))
	    def GetImageOrigins(self):

        return self.imageorigins

	imagetargets={}
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "wallpaper",
                                      {'width': 120, 'height': 131, 'format': "BMP"}))
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "pictureid",
                                      {'width': 120, 'height': 131, 'format': "BMP"}))
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "fullscreen",
                                      {'width': 120, 'height': 160, 'format': "BMP"}))
	    def GetTargetsForImageOrigin(self, origin):

        return self.imagetargets

	_supportedsyncs=(
        ('phonebook', 'read', None),  
        ('calendar', 'read', None),   
        ('wallpaper', 'read', None),  
        ('ringtone', 'read', None),   
        ('phonebook', 'write', 'OVERWRITE'),  
        ('calendar', 'write', 'OVERWRITE'),   
        ('wallpaper', 'write', 'MERGE'),      
        ('wallpaper', 'write', 'OVERWRITE'),
        ('ringtone', 'write', 'MERGE'),      
        ('ringtone', 'write', 'OVERWRITE'),
        ('memo', 'read', None),     
        ('memo', 'write', 'OVERWRITE'),  
        ('call_history', 'read', None),
        ('sms', 'read', None),
        ('sms', 'write', 'OVERWRITE'),
       )
	    def __init__(self):

        parentprofile.__init__(self)

	    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "images"))
	    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "mms"))
	    imageorigins.update(common.getkv(parentprofile.stockimageorigins, "drm"))
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "wallpaper",
                                      {'width': 120, 'height': 131, 'format': "BMP"}))
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "pictureid",
                                      {'width': 120, 'height': 131, 'format': "BMP"}))
	    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "fullscreen",
                                      {'width': 120, 'height': 160, 'format': "BMP"}))

