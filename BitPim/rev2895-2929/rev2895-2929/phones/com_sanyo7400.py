"""Talk to the Sanyo MM-7400 cell phone"""

import common

import p_sanyo7400

import com_brew

import com_phone

import com_sanyo

import com_sanyomedia

import com_sanyonewer

import prototypes

numbertypetab=( 'cell', 'home', 'office', 'pager',
                    'fax', 'data', 'none' )

class  Phone (com_sanyonewer.Phone) :
	"Talk to the Sanyo MM-7400 cell phone"
	    desc="MM-7400"
	    protocolclass=p_sanyo7400
	    serialsname='mm7400'
	    FIRST_MEDIA_DIRECTORY=1
	    LAST_MEDIA_DIRECTORY=3
	    imagelocations=(
        )
	    builtinringtones=( 'None', 'Vibrate', 'Ringer & Voice', '', '', '', '', '', '', 
                       'Tone 1', 'Tone 2', 'Tone 3', 'Tone 4', 'Tone 5',
                       'Tone 6', 'Tone 7', 'Tone 8', '', '', '', '', '',
                       '', '', '', '', '', '', '',
                       'Tschaik.Swanlake', 'Satie Gymnop.#1',
                       'Hungarian Dance', 'Beethoven Sym.5', 'Greensleeves',
                       'Foster Ky. Home', 'The Moment', 'Asian Jingle',
                       'Disco')
	    calendar_defaultringtone=0
	    calendar_defaultcaringtone=0
	    def __init__(self, logtarget, commport):

        com_sanyonewer.Phone.__init__(self, logtarget, commport)

        self.mode=self.MODENONE

        self.numbertypetab=numbertypetab

	"Talk to the Sanyo MM-7400 cell phone"

class  Profile (com_sanyonewer.Profile) :
	protocolclass=Phone.protocolclass
	    serialsname=Phone.serialsname
	    phone_manufacturer='SANYO'
	    phone_model='SCP-7400/US'
	    WALLPAPER_WIDTH=176
	    WALLPAPER_HEIGHT=220
	    _supportedsyncs=(
        ('phonebook', 'read', None),  
        ('calendar', 'read', None),   
        ('phonebook', 'write', 'OVERWRITE'),  
        ('calendar', 'write', 'OVERWRITE'),   
        ('wallpaper', 'write', 'MERGE'),
        ('ringtone', 'write', 'MERGE'),
        ('wallpaper', 'read', None),  
        ('ringtone', 'read', None),   
    )
	    def __init__(self):

        com_sanyonewer.Profile.__init__(self)

        self.numbertypetab=numbertypetab


