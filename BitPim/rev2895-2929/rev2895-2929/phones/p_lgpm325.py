"""Various descriptions of data specific to LG PM325 (Sprint)"""

import re

from prototypes import *

from prototypeslg import *

from p_lg import *

UINT=UINTlsb

BOOL=BOOLlsb

NUMSPEEDDIALS=99

FIRSTSPEEDDIAL=1

LASTSPEEDDIAL=99

NUMPHONEBOOKENTRIES=200

MEMOLENGTH=33

NUMEMAILS=3

NUMPHONENUMBERS=5

NORINGTONE=0

NOMSGRINGTONE=0

NOWALLPAPER=0

numbertypetab=( 'cell', 'home', 'office', 'fax', 'pager' )

media_directory='ams'

ringerindex='setas/amsRingerIndex.map'

imageindex='setas/amsImageIndex.map'

ringerconst=2

imageconst=3

max_ringers=100

max_images=100

phonebook_media='pim/pbookcontact.dat'

NUMCALENDARENTRIES=300

CAL_REP_NONE=0x10

CAL_REP_DAILY=0x11

CAL_REP_MONFRI=0x12

CAL_REP_WEEKLY=0x13

CAL_REP_MONTHLY=0x14

CAL_REP_YEARLY=0x15

CAL_DOW_SUN=0x0800

CAL_DOW_MON=0x0400

CAL_DOW_TUE=0x0200

CAL_DOW_WED=0x0100

CAL_DOW_THU=0x0080

CAL_DOW_FRI=0x0040

CAL_DOW_SAT=0x0020

CAL_DOW_EXCEPTIONS=0x0010

CAL_REMINDER_NONE=0

CAL_REMINDER_ONTIME=1

CAL_REMINDER_5MIN=2

CAL_REMINDER_10MIN=3

CAL_REMINDER_1HOUR=4

CAL_REMINDER_1DAY=5

CAL_REMINDER_2DAYS=6

CAL_REPEAT_DATE=(2100, 12, 31)

cal_dir='sch'

cal_data_file_name='sch/schedule.dat'

cal_exception_file_name='sch/schexception.dat'

cal_has_voice_id=False

SMS_CANNED_MAX_ITEMS=40

SMS_CANNED_MAX_LENGTH=104

SMS_CANNED_FILENAME="sms/canned_msg.dat"

SMS_PATTERNS={'Inbox': re.compile(r"^.*/inbox[0-9][0-9][0-9]\.dat$"),
             'Sent': re.compile(r"^.*/outbox[0-9][0-9][0-9]\.dat$"),
             'Saved': re.compile(r"^.*/sf[0-9][0-9]\.dat$"),
             }

text_memo_file='sch/memo.dat'

content_file_name='ams/contentInfo'

content_count_file_name='ams/realContent'

class  firmwareresponse (BaseProtogenClass) :
	__fields=['command', 'date1', 'time1', 'date2', 'time2', 'firmware', 'dunno']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(firmwareresponse,self).__init__(**dict)

        if self.__class__ is firmwareresponse:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(firmwareresponse,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(firmwareresponse,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_date1.writetobuffer(buf)

        self.__field_time1.writetobuffer(buf)

        self.__field_date2.writetobuffer(buf)

        self.__field_time2.writetobuffer(buf)

        self.__field_firmware.writetobuffer(buf)

        self.__field_dunno.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=UINT(**{'sizeinbytes': 1})

        self.__field_command.readfrombuffer(buf)

        self.__field_date1=STRING(**{'sizeinbytes': 11, 'terminator': None})

        self.__field_date1.readfrombuffer(buf)

        self.__field_time1=STRING(**{'sizeinbytes': 8, 'terminator': None})

        self.__field_time1.readfrombuffer(buf)

        self.__field_date2=STRING(**{'sizeinbytes': 11, 'terminator': None})

        self.__field_date2.readfrombuffer(buf)

        self.__field_time2=STRING(**{'sizeinbytes': 8, 'terminator': None})

        self.__field_time2.readfrombuffer(buf)

        self.__field_firmware=STRING(**{'sizeinbytes': 8, 'terminator': None})

        self.__field_firmware.readfrombuffer(buf)

        self.__field_dunno=DATA()

        self.__field_dunno.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,UINT):

            self.__field_command=value

        else:

            self.__field_command=UINT(value,**{'sizeinbytes': 1})

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_date1(self):

        return self.__field_date1.getvalue()

	def __setfield_date1(self, value):

        if isinstance(value,STRING):

            self.__field_date1=value

        else:

            self.__field_date1=STRING(value,**{'sizeinbytes': 11, 'terminator': None})

	def __delfield_date1(self): del self.__field_date1

	    date1=property(__getfield_date1, __setfield_date1, __delfield_date1, None)
	    def __getfield_time1(self):

        return self.__field_time1.getvalue()

	def __setfield_time1(self, value):

        if isinstance(value,STRING):

            self.__field_time1=value

        else:

            self.__field_time1=STRING(value,**{'sizeinbytes': 8, 'terminator': None})

	def __delfield_time1(self): del self.__field_time1

	    time1=property(__getfield_time1, __setfield_time1, __delfield_time1, None)
	    def __getfield_date2(self):

        return self.__field_date2.getvalue()

	def __setfield_date2(self, value):

        if isinstance(value,STRING):

            self.__field_date2=value

        else:

            self.__field_date2=STRING(value,**{'sizeinbytes': 11, 'terminator': None})

	def __delfield_date2(self): del self.__field_date2

	    date2=property(__getfield_date2, __setfield_date2, __delfield_date2, None)
	    def __getfield_time2(self):

        return self.__field_time2.getvalue()

	def __setfield_time2(self, value):

        if isinstance(value,STRING):

            self.__field_time2=value

        else:

            self.__field_time2=STRING(value,**{'sizeinbytes': 8, 'terminator': None})

	def __delfield_time2(self): del self.__field_time2

	    time2=property(__getfield_time2, __setfield_time2, __delfield_time2, None)
	    def __getfield_firmware(self):

        return self.__field_firmware.getvalue()

	def __setfield_firmware(self, value):

        if isinstance(value,STRING):

            self.__field_firmware=value

        else:

            self.__field_firmware=STRING(value,**{'sizeinbytes': 8, 'terminator': None})

	def __delfield_firmware(self): del self.__field_firmware

	    firmware=property(__getfield_firmware, __setfield_firmware, __delfield_firmware, None)
	    def __getfield_dunno(self):

        return self.__field_dunno.getvalue()

	def __setfield_dunno(self, value):

        if isinstance(value,DATA):

            self.__field_dunno=value

        else:

            self.__field_dunno=DATA(value,)

	def __delfield_dunno(self): del self.__field_dunno

	    dunno=property(__getfield_dunno, __setfield_dunno, __delfield_dunno, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('date1', self.__field_date1, None)

        yield ('time1', self.__field_time1, None)

        yield ('date2', self.__field_date2, None)

        yield ('time2', self.__field_time2, None)

        yield ('firmware', self.__field_firmware, None)

        yield ('dunno', self.__field_dunno, None)


class  speeddial (BaseProtogenClass) :
	__fields=['entry', 'number']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(speeddial,self).__init__(**dict)

        if self.__class__ is speeddial:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(speeddial,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(speeddial,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_entry

        except:

            self.__field_entry=UINT(**{'sizeinbytes': 1, 'default': 0xff})

        self.__field_entry.writetobuffer(buf)

        try: self.__field_number

        except:

            self.__field_number=UINT(**{'sizeinbytes': 1, 'default': 0xff})

        self.__field_number.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_entry=UINT(**{'sizeinbytes': 1, 'default': 0xff})

        self.__field_entry.readfrombuffer(buf)

        self.__field_number=UINT(**{'sizeinbytes': 1, 'default': 0xff})

        self.__field_number.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_entry(self):

        try: self.__field_entry

        except:

            self.__field_entry=UINT(**{'sizeinbytes': 1, 'default': 0xff})

        return self.__field_entry.getvalue()

	def __setfield_entry(self, value):

        if isinstance(value,UINT):

            self.__field_entry=value

        else:

            self.__field_entry=UINT(value,**{'sizeinbytes': 1, 'default': 0xff})

	def __delfield_entry(self): del self.__field_entry

	    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
	    def __getfield_number(self):

        try: self.__field_number

        except:

            self.__field_number=UINT(**{'sizeinbytes': 1, 'default': 0xff})

        return self.__field_number.getvalue()

	def __setfield_number(self, value):

        if isinstance(value,UINT):

            self.__field_number=value

        else:

            self.__field_number=UINT(value,**{'sizeinbytes': 1, 'default': 0xff})

	def __delfield_number(self): del self.__field_number

	    number=property(__getfield_number, __setfield_number, __delfield_number, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('entry', self.__field_entry, None)

        yield ('number', self.__field_number, None)


class  speeddials (BaseProtogenClass) :
	__fields=['speeddials']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(speeddials,self).__init__(**dict)

        if self.__class__ is speeddials:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(speeddials,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(speeddials,kwargs)

        if len(args):

            dict2={'length': NUMSPEEDDIALS, 'elementclass': speeddial}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_speeddials=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_speeddials

        except:

            self.__field_speeddials=LIST(**{'length': NUMSPEEDDIALS, 'elementclass': speeddial})

        self.__field_speeddials.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_speeddials=LIST(**{'length': NUMSPEEDDIALS, 'elementclass': speeddial})

        self.__field_speeddials.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_speeddials(self):

        try: self.__field_speeddials

        except:

            self.__field_speeddials=LIST(**{'length': NUMSPEEDDIALS, 'elementclass': speeddial})

        return self.__field_speeddials.getvalue()

	def __setfield_speeddials(self, value):

        if isinstance(value,LIST):

            self.__field_speeddials=value

        else:

            self.__field_speeddials=LIST(value,**{'length': NUMSPEEDDIALS, 'elementclass': speeddial})

	def __delfield_speeddials(self): del self.__field_speeddials

	    speeddials=property(__getfield_speeddials, __setfield_speeddials, __delfield_speeddials, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('speeddials', self.__field_speeddials, None)


class  pbreadentryresponse (BaseProtogenClass) :
	"Results of reading one entry"
	    __fields=['header', 'entry']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbreadentryresponse,self).__init__(**dict)

        if self.__class__ is pbreadentryresponse:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbreadentryresponse,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbreadentryresponse,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header.writetobuffer(buf)

        self.__field_entry.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header=pbheader()

        self.__field_header.readfrombuffer(buf)

        self.__field_entry=pbentry()

        self.__field_entry.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_header(self):

        return self.__field_header.getvalue()

	def __setfield_header(self, value):

        if isinstance(value,pbheader):

            self.__field_header=value

        else:

            self.__field_header=pbheader(value,)

	def __delfield_header(self): del self.__field_header

	    header=property(__getfield_header, __setfield_header, __delfield_header, None)
	    def __getfield_entry(self):

        return self.__field_entry.getvalue()

	def __setfield_entry(self, value):

        if isinstance(value,pbentry):

            self.__field_entry=value

        else:

            self.__field_entry=pbentry(value,)

	def __delfield_entry(self): del self.__field_entry

	    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('header', self.__field_header, None)

        yield ('entry', self.__field_entry, None)

	"Results of reading one entry"

class  pbupdateentryrequest (BaseProtogenClass) :
	__fields=['header', 'entry']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbupdateentryrequest,self).__init__(**dict)

        if self.__class__ is pbupdateentryrequest:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbupdateentryrequest,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbupdateentryrequest,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_header

        except:

            self.__field_header=pbheader(**{'command': 0x04, 'flag': 0x01})

        self.__field_header.writetobuffer(buf)

        self.__field_entry.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header=pbheader(**{'command': 0x04, 'flag': 0x01})

        self.__field_header.readfrombuffer(buf)

        self.__field_entry=pbentry()

        self.__field_entry.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_header(self):

        try: self.__field_header

        except:

            self.__field_header=pbheader(**{'command': 0x04, 'flag': 0x01})

        return self.__field_header.getvalue()

	def __setfield_header(self, value):

        if isinstance(value,pbheader):

            self.__field_header=value

        else:

            self.__field_header=pbheader(value,**{'command': 0x04, 'flag': 0x01})

	def __delfield_header(self): del self.__field_header

	    header=property(__getfield_header, __setfield_header, __delfield_header, None)
	    def __getfield_entry(self):

        return self.__field_entry.getvalue()

	def __setfield_entry(self, value):

        if isinstance(value,pbentry):

            self.__field_entry=value

        else:

            self.__field_entry=pbentry(value,)

	def __delfield_entry(self): del self.__field_entry

	    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('header', self.__field_header, None)

        yield ('entry', self.__field_entry, None)


class  pbappendentryrequest (BaseProtogenClass) :
	__fields=['header', 'entry']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbappendentryrequest,self).__init__(**dict)

        if self.__class__ is pbappendentryrequest:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbappendentryrequest,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbappendentryrequest,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_header

        except:

            self.__field_header=pbheader(**{'command': 0x03, 'flag': 0x01})

        self.__field_header.writetobuffer(buf)

        self.__field_entry.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header=pbheader(**{'command': 0x03, 'flag': 0x01})

        self.__field_header.readfrombuffer(buf)

        self.__field_entry=pbentry()

        self.__field_entry.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_header(self):

        try: self.__field_header

        except:

            self.__field_header=pbheader(**{'command': 0x03, 'flag': 0x01})

        return self.__field_header.getvalue()

	def __setfield_header(self, value):

        if isinstance(value,pbheader):

            self.__field_header=value

        else:

            self.__field_header=pbheader(value,**{'command': 0x03, 'flag': 0x01})

	def __delfield_header(self): del self.__field_header

	    header=property(__getfield_header, __setfield_header, __delfield_header, None)
	    def __getfield_entry(self):

        return self.__field_entry.getvalue()

	def __setfield_entry(self, value):

        if isinstance(value,pbentry):

            self.__field_entry=value

        else:

            self.__field_entry=pbentry(value,)

	def __delfield_entry(self): del self.__field_entry

	    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('header', self.__field_header, None)

        yield ('entry', self.__field_entry, None)


class  pbentry (BaseProtogenClass) :
	__fields=['serial1', 'entrysize', 'entrynumber', 'unknown1', 'name', 'group', 'ringtone', 'wallpaper', 'secret', 'memo', 'emails', 'url', 'numberspeeds', 'numbertypes', 'numbers', 'EndOfRecord']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbentry,self).__init__(**dict)

        if self.__class__ is pbentry:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbentry,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbentry,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1.writetobuffer(buf)

        try: self.__field_entrysize

        except:

            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x026E})

        self.__field_entrysize.writetobuffer(buf)

        self.__field_entrynumber.writetobuffer(buf)

        try: self.__field_unknown1

        except:

            self.__field_unknown1=UNKNOWN(**{'sizeinbytes': 2})

        self.__field_unknown1.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_group.writetobuffer(buf)

        self.__field_ringtone.writetobuffer(buf)

        self.__field_wallpaper.writetobuffer(buf)

        self.__field_secret.writetobuffer(buf)

        self.__field_memo.writetobuffer(buf)

        try: self.__field_emails

        except:

            self.__field_emails=LIST(**{'elementclass': _gen_p_lgpm325_146, 'length': NUMEMAILS})

        self.__field_emails.writetobuffer(buf)

        self.__field_url.writetobuffer(buf)

        try: self.__field_numberspeeds

        except:

            self.__field_numberspeeds=LIST(**{'elementclass': _gen_p_lgpm325_149, 'length': NUMPHONENUMBERS})

        self.__field_numberspeeds.writetobuffer(buf)

        try: self.__field_numbertypes

        except:

            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgpm325_151, 'length': NUMPHONENUMBERS})

        self.__field_numbertypes.writetobuffer(buf)

        try: self.__field_numbers

        except:

            self.__field_numbers=LIST(**{'elementclass': _gen_p_lgpm325_153, 'length': NUMPHONENUMBERS})

        self.__field_numbers.writetobuffer(buf)

        try: self.__field_EndOfRecord

        except:

            self.__field_EndOfRecord=UINT(**{'sizeinbytes': 1, 'constant': 0x78})

        self.__field_EndOfRecord.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1=UINT(**{'sizeinbytes': 4})

        self.__field_serial1.readfrombuffer(buf)

        self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x026E})

        self.__field_entrysize.readfrombuffer(buf)

        self.__field_entrynumber=UINT(**{'sizeinbytes': 2})

        self.__field_entrynumber.readfrombuffer(buf)

        self.__field_unknown1=UNKNOWN(**{'sizeinbytes': 2})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 33, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self.__field_group=UINT(**{'sizeinbytes': 2})

        self.__field_group.readfrombuffer(buf)

        self.__field_ringtone=UINT(**{'sizeinbytes': 1})

        self.__field_ringtone.readfrombuffer(buf)

        self.__field_wallpaper=UINT(**{'sizeinbytes': 1})

        self.__field_wallpaper.readfrombuffer(buf)

        self.__field_secret=BOOL(**{'sizeinbytes': 1})

        self.__field_secret.readfrombuffer(buf)

        self.__field_memo=STRING(**{'raiseonunterminatedread': False, 'sizeinbytes': MEMOLENGTH})

        self.__field_memo.readfrombuffer(buf)

        self.__field_emails=LIST(**{'elementclass': _gen_p_lgpm325_146, 'length': NUMEMAILS})

        self.__field_emails.readfrombuffer(buf)

        self.__field_url=STRING(**{'sizeinbytes': 73, 'raiseonunterminatedread': False})

        self.__field_url.readfrombuffer(buf)

        self.__field_numberspeeds=LIST(**{'elementclass': _gen_p_lgpm325_149, 'length': NUMPHONENUMBERS})

        self.__field_numberspeeds.readfrombuffer(buf)

        self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgpm325_151, 'length': NUMPHONENUMBERS})

        self.__field_numbertypes.readfrombuffer(buf)

        self.__field_numbers=LIST(**{'elementclass': _gen_p_lgpm325_153, 'length': NUMPHONENUMBERS})

        self.__field_numbers.readfrombuffer(buf)

        self.__field_EndOfRecord=UINT(**{'sizeinbytes': 1, 'constant': 0x78})

        self.__field_EndOfRecord.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_serial1(self):

        return self.__field_serial1.getvalue()

	def __setfield_serial1(self, value):

        if isinstance(value,UINT):

            self.__field_serial1=value

        else:

            self.__field_serial1=UINT(value,**{'sizeinbytes': 4})

	def __delfield_serial1(self): del self.__field_serial1

	    serial1=property(__getfield_serial1, __setfield_serial1, __delfield_serial1, None)
	    def __getfield_entrysize(self):

        try: self.__field_entrysize

        except:

            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x026E})

        return self.__field_entrysize.getvalue()

	def __setfield_entrysize(self, value):

        if isinstance(value,UINT):

            self.__field_entrysize=value

        else:

            self.__field_entrysize=UINT(value,**{'sizeinbytes': 2, 'constant': 0x026E})

	def __delfield_entrysize(self): del self.__field_entrysize

	    entrysize=property(__getfield_entrysize, __setfield_entrysize, __delfield_entrysize, None)
	    def __getfield_entrynumber(self):

        return self.__field_entrynumber.getvalue()

	def __setfield_entrynumber(self, value):

        if isinstance(value,UINT):

            self.__field_entrynumber=value

        else:

            self.__field_entrynumber=UINT(value,**{'sizeinbytes': 2})

	def __delfield_entrynumber(self): del self.__field_entrynumber

	    entrynumber=property(__getfield_entrynumber, __setfield_entrynumber, __delfield_entrynumber, None)
	    def __getfield_unknown1(self):

        try: self.__field_unknown1

        except:

            self.__field_unknown1=UNKNOWN(**{'sizeinbytes': 2})

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UNKNOWN(value,**{'sizeinbytes': 2})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 33, 'raiseonunterminatedread': False})

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_group(self):

        return self.__field_group.getvalue()

	def __setfield_group(self, value):

        if isinstance(value,UINT):

            self.__field_group=value

        else:

            self.__field_group=UINT(value,**{'sizeinbytes': 2})

	def __delfield_group(self): del self.__field_group

	    group=property(__getfield_group, __setfield_group, __delfield_group, None)
	    def __getfield_ringtone(self):

        return self.__field_ringtone.getvalue()

	def __setfield_ringtone(self, value):

        if isinstance(value,UINT):

            self.__field_ringtone=value

        else:

            self.__field_ringtone=UINT(value,**{'sizeinbytes': 1})

	def __delfield_ringtone(self): del self.__field_ringtone

	    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
	    def __getfield_wallpaper(self):

        return self.__field_wallpaper.getvalue()

	def __setfield_wallpaper(self, value):

        if isinstance(value,UINT):

            self.__field_wallpaper=value

        else:

            self.__field_wallpaper=UINT(value,**{'sizeinbytes': 1})

	def __delfield_wallpaper(self): del self.__field_wallpaper

	    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
	    def __getfield_secret(self):

        return self.__field_secret.getvalue()

	def __setfield_secret(self, value):

        if isinstance(value,BOOL):

            self.__field_secret=value

        else:

            self.__field_secret=BOOL(value,**{'sizeinbytes': 1})

	def __delfield_secret(self): del self.__field_secret

	    secret=property(__getfield_secret, __setfield_secret, __delfield_secret, None)
	    def __getfield_memo(self):

        return self.__field_memo.getvalue()

	def __setfield_memo(self, value):

        if isinstance(value,STRING):

            self.__field_memo=value

        else:

            self.__field_memo=STRING(value,**{'raiseonunterminatedread': False, 'sizeinbytes': MEMOLENGTH})

	def __delfield_memo(self): del self.__field_memo

	    memo=property(__getfield_memo, __setfield_memo, __delfield_memo, None)
	    def __getfield_emails(self):

        try: self.__field_emails

        except:

            self.__field_emails=LIST(**{'elementclass': _gen_p_lgpm325_146, 'length': NUMEMAILS})

        return self.__field_emails.getvalue()

	def __setfield_emails(self, value):

        if isinstance(value,LIST):

            self.__field_emails=value

        else:

            self.__field_emails=LIST(value,**{'elementclass': _gen_p_lgpm325_146, 'length': NUMEMAILS})

	def __delfield_emails(self): del self.__field_emails

	    emails=property(__getfield_emails, __setfield_emails, __delfield_emails, None)
	    def __getfield_url(self):

        return self.__field_url.getvalue()

	def __setfield_url(self, value):

        if isinstance(value,STRING):

            self.__field_url=value

        else:

            self.__field_url=STRING(value,**{'sizeinbytes': 73, 'raiseonunterminatedread': False})

	def __delfield_url(self): del self.__field_url

	    url=property(__getfield_url, __setfield_url, __delfield_url, None)
	    def __getfield_numberspeeds(self):

        try: self.__field_numberspeeds

        except:

            self.__field_numberspeeds=LIST(**{'elementclass': _gen_p_lgpm325_149, 'length': NUMPHONENUMBERS})

        return self.__field_numberspeeds.getvalue()

	def __setfield_numberspeeds(self, value):

        if isinstance(value,LIST):

            self.__field_numberspeeds=value

        else:

            self.__field_numberspeeds=LIST(value,**{'elementclass': _gen_p_lgpm325_149, 'length': NUMPHONENUMBERS})

	def __delfield_numberspeeds(self): del self.__field_numberspeeds

	    numberspeeds=property(__getfield_numberspeeds, __setfield_numberspeeds, __delfield_numberspeeds, None)
	    def __getfield_numbertypes(self):

        try: self.__field_numbertypes

        except:

            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgpm325_151, 'length': NUMPHONENUMBERS})

        return self.__field_numbertypes.getvalue()

	def __setfield_numbertypes(self, value):

        if isinstance(value,LIST):

            self.__field_numbertypes=value

        else:

            self.__field_numbertypes=LIST(value,**{'elementclass': _gen_p_lgpm325_151, 'length': NUMPHONENUMBERS})

	def __delfield_numbertypes(self): del self.__field_numbertypes

	    numbertypes=property(__getfield_numbertypes, __setfield_numbertypes, __delfield_numbertypes, None)
	    def __getfield_numbers(self):

        try: self.__field_numbers

        except:

            self.__field_numbers=LIST(**{'elementclass': _gen_p_lgpm325_153, 'length': NUMPHONENUMBERS})

        return self.__field_numbers.getvalue()

	def __setfield_numbers(self, value):

        if isinstance(value,LIST):

            self.__field_numbers=value

        else:

            self.__field_numbers=LIST(value,**{'elementclass': _gen_p_lgpm325_153, 'length': NUMPHONENUMBERS})

	def __delfield_numbers(self): del self.__field_numbers

	    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
	    def __getfield_EndOfRecord(self):

        try: self.__field_EndOfRecord

        except:

            self.__field_EndOfRecord=UINT(**{'sizeinbytes': 1, 'constant': 0x78})

        return self.__field_EndOfRecord.getvalue()

	def __setfield_EndOfRecord(self, value):

        if isinstance(value,UINT):

            self.__field_EndOfRecord=value

        else:

            self.__field_EndOfRecord=UINT(value,**{'sizeinbytes': 1, 'constant': 0x78})

	def __delfield_EndOfRecord(self): del self.__field_EndOfRecord

	    EndOfRecord=property(__getfield_EndOfRecord, __setfield_EndOfRecord, __delfield_EndOfRecord, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('serial1', self.__field_serial1, None)

        yield ('entrysize', self.__field_entrysize, None)

        yield ('entrynumber', self.__field_entrynumber, None)

        yield ('unknown1', self.__field_unknown1, None)

        yield ('name', self.__field_name, None)

        yield ('group', self.__field_group, None)

        yield ('ringtone', self.__field_ringtone, None)

        yield ('wallpaper', self.__field_wallpaper, None)

        yield ('secret', self.__field_secret, None)

        yield ('memo', self.__field_memo, None)

        yield ('emails', self.__field_emails, None)

        yield ('url', self.__field_url, None)

        yield ('numberspeeds', self.__field_numberspeeds, None)

        yield ('numbertypes', self.__field_numbertypes, None)

        yield ('numbers', self.__field_numbers, None)

        yield ('EndOfRecord', self.__field_EndOfRecord, None)


class  _gen_p_lgpm325_146 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['email']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgpm325_146,self).__init__(**dict)

        if self.__class__ is _gen_p_lgpm325_146:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgpm325_146,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgpm325_146,kwargs)

        if len(args):

            dict2={'sizeinbytes': 73, 'raiseonunterminatedread': False}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_email=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_email.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_email=STRING(**{'sizeinbytes': 73, 'raiseonunterminatedread': False})

        self.__field_email.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_email(self):

        return self.__field_email.getvalue()

	def __setfield_email(self, value):

        if isinstance(value,STRING):

            self.__field_email=value

        else:

            self.__field_email=STRING(value,**{'sizeinbytes': 73, 'raiseonunterminatedread': False})

	def __delfield_email(self): del self.__field_email

	    email=property(__getfield_email, __setfield_email, __delfield_email, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('email', self.__field_email, None)

	'Anonymous inner class'

class  _gen_p_lgpm325_149 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['numbertype']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgpm325_149,self).__init__(**dict)

        if self.__class__ is _gen_p_lgpm325_149:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgpm325_149,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgpm325_149,kwargs)

        if len(args):

            dict2={'sizeinbytes': 1}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_numbertype=UINT(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numbertype.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numbertype=UINT(**{'sizeinbytes': 1})

        self.__field_numbertype.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_numbertype(self):

        return self.__field_numbertype.getvalue()

	def __setfield_numbertype(self, value):

        if isinstance(value,UINT):

            self.__field_numbertype=value

        else:

            self.__field_numbertype=UINT(value,**{'sizeinbytes': 1})

	def __delfield_numbertype(self): del self.__field_numbertype

	    numbertype=property(__getfield_numbertype, __setfield_numbertype, __delfield_numbertype, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('numbertype', self.__field_numbertype, None)

	'Anonymous inner class'

class  _gen_p_lgpm325_151 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['numbertype']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgpm325_151,self).__init__(**dict)

        if self.__class__ is _gen_p_lgpm325_151:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgpm325_151,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgpm325_151,kwargs)

        if len(args):

            dict2={'sizeinbytes': 1}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_numbertype=UINT(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numbertype.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numbertype=UINT(**{'sizeinbytes': 1})

        self.__field_numbertype.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_numbertype(self):

        return self.__field_numbertype.getvalue()

	def __setfield_numbertype(self, value):

        if isinstance(value,UINT):

            self.__field_numbertype=value

        else:

            self.__field_numbertype=UINT(value,**{'sizeinbytes': 1})

	def __delfield_numbertype(self): del self.__field_numbertype

	    numbertype=property(__getfield_numbertype, __setfield_numbertype, __delfield_numbertype, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('numbertype', self.__field_numbertype, None)

	'Anonymous inner class'

class  _gen_p_lgpm325_153 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['number']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgpm325_153,self).__init__(**dict)

        if self.__class__ is _gen_p_lgpm325_153:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgpm325_153,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgpm325_153,kwargs)

        if len(args):

            dict2={'sizeinbytes': 49, 'raiseonunterminatedread': False}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_number=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_number.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_number=STRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

        self.__field_number.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_number(self):

        return self.__field_number.getvalue()

	def __setfield_number(self, value):

        if isinstance(value,STRING):

            self.__field_number=value

        else:

            self.__field_number=STRING(value,**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

	def __delfield_number(self): del self.__field_number

	    number=property(__getfield_number, __setfield_number, __delfield_number, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('number', self.__field_number, None)

	'Anonymous inner class'

class  pbgroup (BaseProtogenClass) :
	"A single group"
	    __fields=['group_id', 'rectype', 'unknown2', 'unknown3', 'name']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbgroup,self).__init__(**dict)

        if self.__class__ is pbgroup:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbgroup,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbgroup,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_group_id.writetobuffer(buf)

        self.__field_rectype.writetobuffer(buf)

        try: self.__field_unknown2

        except:

            self.__field_unknown2=UNKNOWN(**{'sizeinbytes': 3})

        self.__field_unknown2.writetobuffer(buf)

        try: self.__field_unknown3

        except:

            self.__field_unknown3=UNKNOWN(**{'sizeinbytes': 3})

        self.__field_unknown3.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_group_id=UINT(**{'sizeinbytes': 1})

        self.__field_group_id.readfrombuffer(buf)

        self.__field_rectype=UINT(**{'sizeinbytes': 1})

        self.__field_rectype.readfrombuffer(buf)

        self.__field_unknown2=UNKNOWN(**{'sizeinbytes': 3})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_unknown3=UNKNOWN(**{'sizeinbytes': 3})

        self.__field_unknown3.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 33, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_group_id(self):

        return self.__field_group_id.getvalue()

	def __setfield_group_id(self, value):

        if isinstance(value,UINT):

            self.__field_group_id=value

        else:

            self.__field_group_id=UINT(value,**{'sizeinbytes': 1})

	def __delfield_group_id(self): del self.__field_group_id

	    group_id=property(__getfield_group_id, __setfield_group_id, __delfield_group_id, None)
	    def __getfield_rectype(self):

        return self.__field_rectype.getvalue()

	def __setfield_rectype(self, value):

        if isinstance(value,UINT):

            self.__field_rectype=value

        else:

            self.__field_rectype=UINT(value,**{'sizeinbytes': 1})

	def __delfield_rectype(self): del self.__field_rectype

	    rectype=property(__getfield_rectype, __setfield_rectype, __delfield_rectype, None)
	    def __getfield_unknown2(self):

        try: self.__field_unknown2

        except:

            self.__field_unknown2=UNKNOWN(**{'sizeinbytes': 3})

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UNKNOWN(value,**{'sizeinbytes': 3})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_unknown3(self):

        try: self.__field_unknown3

        except:

            self.__field_unknown3=UNKNOWN(**{'sizeinbytes': 3})

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=UNKNOWN(value,**{'sizeinbytes': 3})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 33, 'raiseonunterminatedread': False})

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('group_id', self.__field_group_id, None)

        yield ('rectype', self.__field_rectype, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('unknown3', self.__field_unknown3, None)

        yield ('name', self.__field_name, None)

	"A single group"

class  pbgroups (BaseProtogenClass) :
	"Phonebook groups"
	    __fields=['groups']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbgroups,self).__init__(**dict)

        if self.__class__ is pbgroups:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbgroups,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbgroups,kwargs)

        if len(args):

            dict2={'elementclass': pbgroup}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_groups=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_groups

        except:

            self.__field_groups=LIST(**{'elementclass': pbgroup})

        self.__field_groups.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_groups=LIST(**{'elementclass': pbgroup})

        self.__field_groups.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_groups(self):

        try: self.__field_groups

        except:

            self.__field_groups=LIST(**{'elementclass': pbgroup})

        return self.__field_groups.getvalue()

	def __setfield_groups(self, value):

        if isinstance(value,LIST):

            self.__field_groups=value

        else:

            self.__field_groups=LIST(value,**{'elementclass': pbgroup})

	def __delfield_groups(self): del self.__field_groups

	    groups=property(__getfield_groups, __setfield_groups, __delfield_groups, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('groups', self.__field_groups, None)

	"Phonebook groups"

class  pb_contact_media_entry (BaseProtogenClass) :
	"""Reads the wallpaper/ringer info for each contact on the phone"""
	    __fields=['index', 'dont_care1', 'ringer', 'name', 'dont_care2', 'wallpaper', 'dont_care3']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pb_contact_media_entry,self).__init__(**dict)

        if self.__class__ is pb_contact_media_entry:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pb_contact_media_entry,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pb_contact_media_entry,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index.writetobuffer(buf)

        self.__field_dont_care1.writetobuffer(buf)

        self.__field_ringer.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_dont_care2.writetobuffer(buf)

        self.__field_wallpaper.writetobuffer(buf)

        self.__field_dont_care3.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index=UINT(**{'sizeinbytes': 2})

        self.__field_index.readfrombuffer(buf)

        self.__field_dont_care1=DATA(**{'sizeinbytes': 18})

        self.__field_dont_care1.readfrombuffer(buf)

        self.__field_ringer=UINT(**{'sizeinbytes': 2})

        self.__field_ringer.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 33})

        self.__field_name.readfrombuffer(buf)

        self.__field_dont_care2=DATA(**{'sizeinbytes': 182})

        self.__field_dont_care2.readfrombuffer(buf)

        self.__field_wallpaper=UINT(**{'sizeinbytes': 2})

        self.__field_wallpaper.readfrombuffer(buf)

        self.__field_dont_care3=DATA(**{'sizeinbytes': 4})

        self.__field_dont_care3.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,UINT):

            self.__field_index=value

        else:

            self.__field_index=UINT(value,**{'sizeinbytes': 2})

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_dont_care1(self):

        return self.__field_dont_care1.getvalue()

	def __setfield_dont_care1(self, value):

        if isinstance(value,DATA):

            self.__field_dont_care1=value

        else:

            self.__field_dont_care1=DATA(value,**{'sizeinbytes': 18})

	def __delfield_dont_care1(self): del self.__field_dont_care1

	    dont_care1=property(__getfield_dont_care1, __setfield_dont_care1, __delfield_dont_care1, None)
	    def __getfield_ringer(self):

        return self.__field_ringer.getvalue()

	def __setfield_ringer(self, value):

        if isinstance(value,UINT):

            self.__field_ringer=value

        else:

            self.__field_ringer=UINT(value,**{'sizeinbytes': 2})

	def __delfield_ringer(self): del self.__field_ringer

	    ringer=property(__getfield_ringer, __setfield_ringer, __delfield_ringer, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 33})

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_dont_care2(self):

        return self.__field_dont_care2.getvalue()

	def __setfield_dont_care2(self, value):

        if isinstance(value,DATA):

            self.__field_dont_care2=value

        else:

            self.__field_dont_care2=DATA(value,**{'sizeinbytes': 182})

	def __delfield_dont_care2(self): del self.__field_dont_care2

	    dont_care2=property(__getfield_dont_care2, __setfield_dont_care2, __delfield_dont_care2, None)
	    def __getfield_wallpaper(self):

        return self.__field_wallpaper.getvalue()

	def __setfield_wallpaper(self, value):

        if isinstance(value,UINT):

            self.__field_wallpaper=value

        else:

            self.__field_wallpaper=UINT(value,**{'sizeinbytes': 2})

	def __delfield_wallpaper(self): del self.__field_wallpaper

	    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
	    def __getfield_dont_care3(self):

        return self.__field_dont_care3.getvalue()

	def __setfield_dont_care3(self, value):

        if isinstance(value,DATA):

            self.__field_dont_care3=value

        else:

            self.__field_dont_care3=DATA(value,**{'sizeinbytes': 4})

	def __delfield_dont_care3(self): del self.__field_dont_care3

	    dont_care3=property(__getfield_dont_care3, __setfield_dont_care3, __delfield_dont_care3, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('index', self.__field_index, None)

        yield ('dont_care1', self.__field_dont_care1, None)

        yield ('ringer', self.__field_ringer, None)

        yield ('name', self.__field_name, None)

        yield ('dont_care2', self.__field_dont_care2, None)

        yield ('wallpaper', self.__field_wallpaper, None)

        yield ('dont_care3', self.__field_dont_care3, None)

	"""Reads the wallpaper/ringer info for each contact on the phone"""

class  pb_contact_media_file (BaseProtogenClass) :
	__fields=['contacts']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pb_contact_media_file,self).__init__(**dict)

        if self.__class__ is pb_contact_media_file:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pb_contact_media_file,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pb_contact_media_file,kwargs)

        if len(args):

            dict2={'elementclass': pb_contact_media_entry}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_contacts=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_contacts

        except:

            self.__field_contacts=LIST(**{'elementclass': pb_contact_media_entry})

        self.__field_contacts.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_contacts=LIST(**{'elementclass': pb_contact_media_entry})

        self.__field_contacts.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_contacts(self):

        try: self.__field_contacts

        except:

            self.__field_contacts=LIST(**{'elementclass': pb_contact_media_entry})

        return self.__field_contacts.getvalue()

	def __setfield_contacts(self, value):

        if isinstance(value,LIST):

            self.__field_contacts=value

        else:

            self.__field_contacts=LIST(value,**{'elementclass': pb_contact_media_entry})

	def __delfield_contacts(self): del self.__field_contacts

	    contacts=property(__getfield_contacts, __setfield_contacts, __delfield_contacts, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('contacts', self.__field_contacts, None)


class  scheduleexception (BaseProtogenClass) :
	__fields=['pos', 'day', 'month', 'year']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(scheduleexception,self).__init__(**dict)

        if self.__class__ is scheduleexception:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(scheduleexception,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(scheduleexception,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_pos.writetobuffer(buf)

        self.__field_day.writetobuffer(buf)

        self.__field_month.writetobuffer(buf)

        self.__field_year.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_pos=UINT(**{'sizeinbytes': 4})

        self.__field_pos.readfrombuffer(buf)

        self.__field_day=UINT(**{'sizeinbytes': 1})

        self.__field_day.readfrombuffer(buf)

        self.__field_month=UINT(**{'sizeinbytes': 1})

        self.__field_month.readfrombuffer(buf)

        self.__field_year=UINT(**{'sizeinbytes': 2})

        self.__field_year.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_pos(self):

        return self.__field_pos.getvalue()

	def __setfield_pos(self, value):

        if isinstance(value,UINT):

            self.__field_pos=value

        else:

            self.__field_pos=UINT(value,**{'sizeinbytes': 4})

	def __delfield_pos(self): del self.__field_pos

	    pos=property(__getfield_pos, __setfield_pos, __delfield_pos, "Refers to event id (position in schedule file) that this suppresses")
	    def __getfield_day(self):

        return self.__field_day.getvalue()

	def __setfield_day(self, value):

        if isinstance(value,UINT):

            self.__field_day=value

        else:

            self.__field_day=UINT(value,**{'sizeinbytes': 1})

	def __delfield_day(self): del self.__field_day

	    day=property(__getfield_day, __setfield_day, __delfield_day, None)
	    def __getfield_month(self):

        return self.__field_month.getvalue()

	def __setfield_month(self, value):

        if isinstance(value,UINT):

            self.__field_month=value

        else:

            self.__field_month=UINT(value,**{'sizeinbytes': 1})

	def __delfield_month(self): del self.__field_month

	    month=property(__getfield_month, __setfield_month, __delfield_month, None)
	    def __getfield_year(self):

        return self.__field_year.getvalue()

	def __setfield_year(self, value):

        if isinstance(value,UINT):

            self.__field_year=value

        else:

            self.__field_year=UINT(value,**{'sizeinbytes': 2})

	def __delfield_year(self): del self.__field_year

	    year=property(__getfield_year, __setfield_year, __delfield_year, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('pos', self.__field_pos, "Refers to event id (position in schedule file) that this suppresses")

        yield ('day', self.__field_day, None)

        yield ('month', self.__field_month, None)

        yield ('year', self.__field_year, None)


class  scheduleexceptionfile (BaseProtogenClass) :
	__fields=['items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(scheduleexceptionfile,self).__init__(**dict)

        if self.__class__ is scheduleexceptionfile:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(scheduleexceptionfile,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(scheduleexceptionfile,kwargs)

        if len(args):

            dict2={'elementclass': scheduleexception}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_items=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'elementclass': scheduleexception})

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items=LIST(**{'elementclass': scheduleexception})

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_items(self):

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'elementclass': scheduleexception})

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{'elementclass': scheduleexception})

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('items', self.__field_items, None)


class  scheduleevent (BaseProtogenClass) :
	__fields=['packet_size', 'pos', 'start', 'end', 'repeat', 'daybitmap', 'pad2', 'alarmminutes', 'alarmhours', 'alarmtype', 'snoozedelay', 'ringtone', 'pad3', 'description']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(scheduleevent,self).__init__(**dict)

        if self.__class__ is scheduleevent:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(scheduleevent,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(scheduleevent,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

        if getattr(self, '__field_packet_size', None) is None:

            self.__field_packet_size=UINT(**{ 'constant': 64 })

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_pos.writetobuffer(buf)

        self.__field_start.writetobuffer(buf)

        self.__field_end.writetobuffer(buf)

        self.__field_repeat.writetobuffer(buf)

        self.__field_daybitmap.writetobuffer(buf)

        try: self.__field_pad2

        except:

            self.__field_pad2=UINT(**{'sizeinbytes': 1,  'default': 0 })

        self.__field_pad2.writetobuffer(buf)

        self.__field_alarmminutes.writetobuffer(buf)

        self.__field_alarmhours.writetobuffer(buf)

        self.__field_alarmtype.writetobuffer(buf)

        try: self.__field_snoozedelay

        except:

            self.__field_snoozedelay=UINT(**{'sizeinbytes': 1,  'default': 0 })

        self.__field_snoozedelay.writetobuffer(buf)

        self.__field_ringtone.writetobuffer(buf)

        try: self.__field_pad3

        except:

            self.__field_pad3=UINT(**{'sizeinbytes': 1,  'default': 0 })

        self.__field_pad3.writetobuffer(buf)

        self.__field_description.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_pos=UINT(**{'sizeinbytes': 4})

        self.__field_pos.readfrombuffer(buf)

        self.__field_start=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_start.readfrombuffer(buf)

        self.__field_end=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_end.readfrombuffer(buf)

        self.__field_repeat=UINT(**{'sizeinbytes': 1})

        self.__field_repeat.readfrombuffer(buf)

        self.__field_daybitmap=UINT(**{'sizeinbytes': 2})

        self.__field_daybitmap.readfrombuffer(buf)

        self.__field_pad2=UINT(**{'sizeinbytes': 1,  'default': 0 })

        self.__field_pad2.readfrombuffer(buf)

        self.__field_alarmminutes=UINT(**{'sizeinbytes': 1})

        self.__field_alarmminutes.readfrombuffer(buf)

        self.__field_alarmhours=UINT(**{'sizeinbytes': 1})

        self.__field_alarmhours.readfrombuffer(buf)

        self.__field_alarmtype=UINT(**{'sizeinbytes': 1})

        self.__field_alarmtype.readfrombuffer(buf)

        self.__field_snoozedelay=UINT(**{'sizeinbytes': 1,  'default': 0 })

        self.__field_snoozedelay.readfrombuffer(buf)

        self.__field_ringtone=UINT(**{'sizeinbytes': 1})

        self.__field_ringtone.readfrombuffer(buf)

        self.__field_pad3=UINT(**{'sizeinbytes': 1,  'default': 0 })

        self.__field_pad3.readfrombuffer(buf)

        self.__field_description=STRING(**{'sizeinbytes': 42, 'raiseontruncate': False,               'raiseonunterminatedread': False })

        self.__field_description.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_packet_size(self):

        return self.__field_packet_size.getvalue()

	def __setfield_packet_size(self, value):

        if isinstance(value,UINT):

            self.__field_packet_size=value

        else:

            self.__field_packet_size=UINT(value,**{ 'constant': 64 })

	def __delfield_packet_size(self): del self.__field_packet_size

	    packet_size=property(__getfield_packet_size, __setfield_packet_size, __delfield_packet_size, "Faster than packetsize()")
	    def __getfield_pos(self):

        return self.__field_pos.getvalue()

	def __setfield_pos(self, value):

        if isinstance(value,UINT):

            self.__field_pos=value

        else:

            self.__field_pos=UINT(value,**{'sizeinbytes': 4})

	def __delfield_pos(self): del self.__field_pos

	    pos=property(__getfield_pos, __setfield_pos, __delfield_pos, "position within file, used as an event id")
	    def __getfield_start(self):

        return self.__field_start.getvalue()

	def __setfield_start(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_start=value

        else:

            self.__field_start=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_start(self): del self.__field_start

	    start=property(__getfield_start, __setfield_start, __delfield_start, None)
	    def __getfield_end(self):

        return self.__field_end.getvalue()

	def __setfield_end(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_end=value

        else:

            self.__field_end=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_end(self): del self.__field_end

	    end=property(__getfield_end, __setfield_end, __delfield_end, None)
	    def __getfield_repeat(self):

        return self.__field_repeat.getvalue()

	def __setfield_repeat(self, value):

        if isinstance(value,UINT):

            self.__field_repeat=value

        else:

            self.__field_repeat=UINT(value,**{'sizeinbytes': 1})

	def __delfield_repeat(self): del self.__field_repeat

	    repeat=property(__getfield_repeat, __setfield_repeat, __delfield_repeat, None)
	    def __getfield_daybitmap(self):

        return self.__field_daybitmap.getvalue()

	def __setfield_daybitmap(self, value):

        if isinstance(value,UINT):

            self.__field_daybitmap=value

        else:

            self.__field_daybitmap=UINT(value,**{'sizeinbytes': 2})

	def __delfield_daybitmap(self): del self.__field_daybitmap

	    daybitmap=property(__getfield_daybitmap, __setfield_daybitmap, __delfield_daybitmap, "which days a weekly repeat event happens on")
	    def __getfield_pad2(self):

        try: self.__field_pad2

        except:

            self.__field_pad2=UINT(**{'sizeinbytes': 1,  'default': 0 })

        return self.__field_pad2.getvalue()

	def __setfield_pad2(self, value):

        if isinstance(value,UINT):

            self.__field_pad2=value

        else:

            self.__field_pad2=UINT(value,**{'sizeinbytes': 1,  'default': 0 })

	def __delfield_pad2(self): del self.__field_pad2

	    pad2=property(__getfield_pad2, __setfield_pad2, __delfield_pad2, None)
	    def __getfield_alarmminutes(self):

        return self.__field_alarmminutes.getvalue()

	def __setfield_alarmminutes(self, value):

        if isinstance(value,UINT):

            self.__field_alarmminutes=value

        else:

            self.__field_alarmminutes=UINT(value,**{'sizeinbytes': 1})

	def __delfield_alarmminutes(self): del self.__field_alarmminutes

	    alarmminutes=property(__getfield_alarmminutes, __setfield_alarmminutes, __delfield_alarmminutes, "a value of 100 indicates not set")
	    def __getfield_alarmhours(self):

        return self.__field_alarmhours.getvalue()

	def __setfield_alarmhours(self, value):

        if isinstance(value,UINT):

            self.__field_alarmhours=value

        else:

            self.__field_alarmhours=UINT(value,**{'sizeinbytes': 1})

	def __delfield_alarmhours(self): del self.__field_alarmhours

	    alarmhours=property(__getfield_alarmhours, __setfield_alarmhours, __delfield_alarmhours, "a value of 100 indicates not set")
	    def __getfield_alarmtype(self):

        return self.__field_alarmtype.getvalue()

	def __setfield_alarmtype(self, value):

        if isinstance(value,UINT):

            self.__field_alarmtype=value

        else:

            self.__field_alarmtype=UINT(value,**{'sizeinbytes': 1})

	def __delfield_alarmtype(self): del self.__field_alarmtype

	    alarmtype=property(__getfield_alarmtype, __setfield_alarmtype, __delfield_alarmtype, "preset alarm reminder type")
	    def __getfield_snoozedelay(self):

        try: self.__field_snoozedelay

        except:

            self.__field_snoozedelay=UINT(**{'sizeinbytes': 1,  'default': 0 })

        return self.__field_snoozedelay.getvalue()

	def __setfield_snoozedelay(self, value):

        if isinstance(value,UINT):

            self.__field_snoozedelay=value

        else:

            self.__field_snoozedelay=UINT(value,**{'sizeinbytes': 1,  'default': 0 })

	def __delfield_snoozedelay(self): del self.__field_snoozedelay

	    snoozedelay=property(__getfield_snoozedelay, __setfield_snoozedelay, __delfield_snoozedelay, "in minutes, not for this phone")
	    def __getfield_ringtone(self):

        return self.__field_ringtone.getvalue()

	def __setfield_ringtone(self, value):

        if isinstance(value,UINT):

            self.__field_ringtone=value

        else:

            self.__field_ringtone=UINT(value,**{'sizeinbytes': 1})

	def __delfield_ringtone(self): del self.__field_ringtone

	    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
	    def __getfield_pad3(self):

        try: self.__field_pad3

        except:

            self.__field_pad3=UINT(**{'sizeinbytes': 1,  'default': 0 })

        return self.__field_pad3.getvalue()

	def __setfield_pad3(self, value):

        if isinstance(value,UINT):

            self.__field_pad3=value

        else:

            self.__field_pad3=UINT(value,**{'sizeinbytes': 1,  'default': 0 })

	def __delfield_pad3(self): del self.__field_pad3

	    pad3=property(__getfield_pad3, __setfield_pad3, __delfield_pad3, None)
	    def __getfield_description(self):

        return self.__field_description.getvalue()

	def __setfield_description(self, value):

        if isinstance(value,STRING):

            self.__field_description=value

        else:

            self.__field_description=STRING(value,**{'sizeinbytes': 42, 'raiseontruncate': False,               'raiseonunterminatedread': False })

	def __delfield_description(self): del self.__field_description

	    description=property(__getfield_description, __setfield_description, __delfield_description, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('packet_size', self.__field_packet_size, "Faster than packetsize()")

        yield ('pos', self.__field_pos, "position within file, used as an event id")

        yield ('start', self.__field_start, None)

        yield ('end', self.__field_end, None)

        yield ('repeat', self.__field_repeat, None)

        yield ('daybitmap', self.__field_daybitmap, "which days a weekly repeat event happens on")

        yield ('pad2', self.__field_pad2, None)

        yield ('alarmminutes', self.__field_alarmminutes, "a value of 100 indicates not set")

        yield ('alarmhours', self.__field_alarmhours, "a value of 100 indicates not set")

        yield ('alarmtype', self.__field_alarmtype, "preset alarm reminder type")

        yield ('snoozedelay', self.__field_snoozedelay, "in minutes, not for this phone")

        yield ('ringtone', self.__field_ringtone, None)

        yield ('pad3', self.__field_pad3, None)

        yield ('description', self.__field_description, None)


class  schedulefile (BaseProtogenClass) :
	__fields=['numactiveitems', 'events']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(schedulefile,self).__init__(**dict)

        if self.__class__ is schedulefile:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(schedulefile,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(schedulefile,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numactiveitems.writetobuffer(buf)

        try: self.__field_events

        except:

            self.__field_events=LIST(**{'elementclass': scheduleevent})

        self.__field_events.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numactiveitems=UINT(**{'sizeinbytes': 2})

        self.__field_numactiveitems.readfrombuffer(buf)

        self.__field_events=LIST(**{'elementclass': scheduleevent})

        self.__field_events.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_numactiveitems(self):

        return self.__field_numactiveitems.getvalue()

	def __setfield_numactiveitems(self, value):

        if isinstance(value,UINT):

            self.__field_numactiveitems=value

        else:

            self.__field_numactiveitems=UINT(value,**{'sizeinbytes': 2})

	def __delfield_numactiveitems(self): del self.__field_numactiveitems

	    numactiveitems=property(__getfield_numactiveitems, __setfield_numactiveitems, __delfield_numactiveitems, None)
	    def __getfield_events(self):

        try: self.__field_events

        except:

            self.__field_events=LIST(**{'elementclass': scheduleevent})

        return self.__field_events.getvalue()

	def __setfield_events(self, value):

        if isinstance(value,LIST):

            self.__field_events=value

        else:

            self.__field_events=LIST(value,**{'elementclass': scheduleevent})

	def __delfield_events(self): del self.__field_events

	    events=property(__getfield_events, __setfield_events, __delfield_events, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('numactiveitems', self.__field_numactiveitems, None)

        yield ('events', self.__field_events, None)


class  call (BaseProtogenClass) :
	__fields=['GPStime', 'unknown1', 'duration', 'number', 'name', 'numberlength', 'unknown2', 'pbnumbertype', 'unknown3']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(call,self).__init__(**dict)

        if self.__class__ is call:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(call,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(call,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_GPStime.writetobuffer(buf)

        self.__field_unknown1.writetobuffer(buf)

        self.__field_duration.writetobuffer(buf)

        self.__field_number.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_numberlength.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_pbnumbertype.writetobuffer(buf)

        self.__field_unknown3.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        self.__field_unknown1=UINT(**{'sizeinbytes': 4})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_duration=UINT(**{'sizeinbytes': 4})

        self.__field_duration.readfrombuffer(buf)

        self.__field_number=STRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

        self.__field_number.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 36, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self.__field_numberlength=UINT(**{'sizeinbytes': 1})

        self.__field_numberlength.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 1})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_pbnumbertype=UINT(**{'sizeinbytes': 1})

        self.__field_pbnumbertype.readfrombuffer(buf)

        self.__field_unknown3=UINT(**{'sizeinbytes': 5})

        self.__field_unknown3.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_GPStime(self):

        return self.__field_GPStime.getvalue()

	def __setfield_GPStime(self, value):

        if isinstance(value,GPSDATE):

            self.__field_GPStime=value

        else:

            self.__field_GPStime=GPSDATE(value,**{'sizeinbytes': 4})

	def __delfield_GPStime(self): del self.__field_GPStime

	    GPStime=property(__getfield_GPStime, __setfield_GPStime, __delfield_GPStime, None)
	    def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 4})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_duration(self):

        return self.__field_duration.getvalue()

	def __setfield_duration(self, value):

        if isinstance(value,UINT):

            self.__field_duration=value

        else:

            self.__field_duration=UINT(value,**{'sizeinbytes': 4})

	def __delfield_duration(self): del self.__field_duration

	    duration=property(__getfield_duration, __setfield_duration, __delfield_duration, None)
	    def __getfield_number(self):

        return self.__field_number.getvalue()

	def __setfield_number(self, value):

        if isinstance(value,STRING):

            self.__field_number=value

        else:

            self.__field_number=STRING(value,**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

	def __delfield_number(self): del self.__field_number

	    number=property(__getfield_number, __setfield_number, __delfield_number, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 36, 'raiseonunterminatedread': False})

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_numberlength(self):

        return self.__field_numberlength.getvalue()

	def __setfield_numberlength(self, value):

        if isinstance(value,UINT):

            self.__field_numberlength=value

        else:

            self.__field_numberlength=UINT(value,**{'sizeinbytes': 1})

	def __delfield_numberlength(self): del self.__field_numberlength

	    numberlength=property(__getfield_numberlength, __setfield_numberlength, __delfield_numberlength, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_pbnumbertype(self):

        return self.__field_pbnumbertype.getvalue()

	def __setfield_pbnumbertype(self, value):

        if isinstance(value,UINT):

            self.__field_pbnumbertype=value

        else:

            self.__field_pbnumbertype=UINT(value,**{'sizeinbytes': 1})

	def __delfield_pbnumbertype(self): del self.__field_pbnumbertype

	    pbnumbertype=property(__getfield_pbnumbertype, __setfield_pbnumbertype, __delfield_pbnumbertype, None)
	    def __getfield_unknown3(self):

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,UINT):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=UINT(value,**{'sizeinbytes': 5})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('GPStime', self.__field_GPStime, None)

        yield ('unknown1', self.__field_unknown1, None)

        yield ('duration', self.__field_duration, None)

        yield ('number', self.__field_number, None)

        yield ('name', self.__field_name, None)

        yield ('numberlength', self.__field_numberlength, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('pbnumbertype', self.__field_pbnumbertype, None)

        yield ('unknown3', self.__field_unknown3, None)


class  callhistory (BaseProtogenClass) :
	__fields=['numcalls', 'unknown1', 'calls']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(callhistory,self).__init__(**dict)

        if self.__class__ is callhistory:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(callhistory,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(callhistory,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numcalls.writetobuffer(buf)

        self.__field_unknown1.writetobuffer(buf)

        try: self.__field_calls

        except:

            self.__field_calls=LIST(**{'elementclass': call})

        self.__field_calls.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numcalls=UINT(**{'sizeinbytes': 4})

        self.__field_numcalls.readfrombuffer(buf)

        self.__field_unknown1=UINT(**{'sizeinbytes': 1})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_calls=LIST(**{'elementclass': call})

        self.__field_calls.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_numcalls(self):

        return self.__field_numcalls.getvalue()

	def __setfield_numcalls(self, value):

        if isinstance(value,UINT):

            self.__field_numcalls=value

        else:

            self.__field_numcalls=UINT(value,**{'sizeinbytes': 4})

	def __delfield_numcalls(self): del self.__field_numcalls

	    numcalls=property(__getfield_numcalls, __setfield_numcalls, __delfield_numcalls, None)
	    def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_calls(self):

        try: self.__field_calls

        except:

            self.__field_calls=LIST(**{'elementclass': call})

        return self.__field_calls.getvalue()

	def __setfield_calls(self, value):

        if isinstance(value,LIST):

            self.__field_calls=value

        else:

            self.__field_calls=LIST(value,**{'elementclass': call})

	def __delfield_calls(self): del self.__field_calls

	    calls=property(__getfield_calls, __setfield_calls, __delfield_calls, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('numcalls', self.__field_numcalls, None)

        yield ('unknown1', self.__field_unknown1, None)

        yield ('calls', self.__field_calls, None)


class  indexentry (BaseProtogenClass) :
	__fields=['index', 'name']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(indexentry,self).__init__(**dict)

        if self.__class__ is indexentry:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(indexentry,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(indexentry,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_index

        except:

            self.__field_index=UINT(**{'sizeinbytes': 2, 'default': 0xffff})

        self.__field_index.writetobuffer(buf)

        try: self.__field_name

        except:

            self.__field_name=STRING(**{'sizeinbytes': 40, 'default': ""})

        self.__field_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index=UINT(**{'sizeinbytes': 2, 'default': 0xffff})

        self.__field_index.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 40, 'default': ""})

        self.__field_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_index(self):

        try: self.__field_index

        except:

            self.__field_index=UINT(**{'sizeinbytes': 2, 'default': 0xffff})

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,UINT):

            self.__field_index=value

        else:

            self.__field_index=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_name(self):

        try: self.__field_name

        except:

            self.__field_name=STRING(**{'sizeinbytes': 40, 'default': ""})

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 40, 'default': ""})

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('index', self.__field_index, None)

        yield ('name', self.__field_name, None)


class  indexfile (BaseProtogenClass) :
	"Used for tracking wallpaper and ringtones"
	    __fields=['maxitems', 'numactiveitems', 'items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(indexfile,self).__init__(**dict)

        if self.__class__ is indexfile:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(indexfile,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(indexfile,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

        if getattr(self, '__field_maxitems', None) is None:

            self.__field_maxitems=UINT(**{'constant': 30})

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numactiveitems.writetobuffer(buf)

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': indexentry, 'createdefault': True})

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_numactiveitems=UINT(**{'sizeinbytes': 2})

        self.__field_numactiveitems.readfrombuffer(buf)

        self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': indexentry, 'createdefault': True})

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_maxitems(self):

        return self.__field_maxitems.getvalue()

	def __setfield_maxitems(self, value):

        if isinstance(value,UINT):

            self.__field_maxitems=value

        else:

            self.__field_maxitems=UINT(value,**{'constant': 30})

	def __delfield_maxitems(self): del self.__field_maxitems

	    maxitems=property(__getfield_maxitems, __setfield_maxitems, __delfield_maxitems, None)
	    def __getfield_numactiveitems(self):

        return self.__field_numactiveitems.getvalue()

	def __setfield_numactiveitems(self, value):

        if isinstance(value,UINT):

            self.__field_numactiveitems=value

        else:

            self.__field_numactiveitems=UINT(value,**{'sizeinbytes': 2})

	def __delfield_numactiveitems(self): del self.__field_numactiveitems

	    numactiveitems=property(__getfield_numactiveitems, __setfield_numactiveitems, __delfield_numactiveitems, None)
	    def __getfield_items(self):

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': indexentry, 'createdefault': True})

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{'length': self.maxitems, 'elementclass': indexentry, 'createdefault': True})

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('maxitems', self.__field_maxitems, None)

        yield ('numactiveitems', self.__field_numactiveitems, None)

        yield ('items', self.__field_items, None)

	"Used for tracking wallpaper and ringtones"

class  content_entry (BaseProtogenClass) :
	__fields=['type', 'index1', 'name1', 'unknown1', 'unknown2', 'mime_type', 'content_type', 'url', 'unknown_int1', 'unknown3', 'unknown_int2', 'unknown4', 'unknown5', 'size', 'location_maybe', 'index2', 'name2', 'unknown6']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(content_entry,self).__init__(**dict)

        if self.__class__ is content_entry:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(content_entry,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(content_entry,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_type.writetobuffer(buf)

        if self.type=='!C':

            self.__field_index1.writetobuffer(buf)

            self.__field_name1.writetobuffer(buf)

            try: self.__field_unknown1

            except:

                self.__field_unknown1=STRING(**{'terminator': 0xA, 'default': '-1'})

            self.__field_unknown1.writetobuffer(buf)

            try: self.__field_unknown2

            except:

                self.__field_unknown2=UINT(**{'sizeinbytes': 8, 'default' :0})

            self.__field_unknown2.writetobuffer(buf)

            self.__field_mime_type.writetobuffer(buf)

            self.__field_content_type.writetobuffer(buf)

            try: self.__field_url

            except:

                self.__field_url=STRING(**{'terminator': 0xA, 'default':'bitpim.org'})

            self.__field_url.writetobuffer(buf)

            try: self.__field_unknown_int1

            except:

                self.__field_unknown_int1=UINT(**{'sizeinbytes': 1, 'default':0x14})

            self.__field_unknown_int1.writetobuffer(buf)

            try: self.__field_unknown3

            except:

                self.__field_unknown3=STRING(**{'terminator': 0xA, 'default':''})

            self.__field_unknown3.writetobuffer(buf)

            try: self.__field_unknown_int2

            except:

                self.__field_unknown_int2=UINT(**{'sizeinbytes': 1, 'default':0x14})

            self.__field_unknown_int2.writetobuffer(buf)

            try: self.__field_unknown4

            except:

                self.__field_unknown4=STRING(**{'terminator': 0xA, 'default':''})

            self.__field_unknown4.writetobuffer(buf)

            try: self.__field_unknown5

            except:

                self.__field_unknown5=STRING(**{'terminator': 0xA, 'default':'0'})

            self.__field_unknown5.writetobuffer(buf)

            self.__field_size.writetobuffer(buf)

        if self.type=='!E':

            try: self.__field_location_maybe

            except:

                self.__field_location_maybe=STRING(**{'terminator': 0xA, 'default':'ams:'})

            self.__field_location_maybe.writetobuffer(buf)

            self.__field_index2.writetobuffer(buf)

            self.__field_name2.writetobuffer(buf)

            try: self.__field_unknown6

            except:

                self.__field_unknown6=STRING(**{'terminator': 0xA, 'default':''})

            self.__field_unknown6.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_type=STRING(**{'sizeinbytes': 3, 'terminator': 0xA})

        self.__field_type.readfrombuffer(buf)

        if self.type=='!C':

            self.__field_index1=STRING(**{'terminator': 0xA})

            self.__field_index1.readfrombuffer(buf)

            self.__field_name1=STRING(**{'terminator': 0xA})

            self.__field_name1.readfrombuffer(buf)

            self.__field_unknown1=STRING(**{'terminator': 0xA, 'default': '-1'})

            self.__field_unknown1.readfrombuffer(buf)

            self.__field_unknown2=UINT(**{'sizeinbytes': 8, 'default' :0})

            self.__field_unknown2.readfrombuffer(buf)

            self.__field_mime_type=STRING(**{'terminator': 0xA})

            self.__field_mime_type.readfrombuffer(buf)

            self.__field_content_type=STRING(**{'terminator': 0xA})

            self.__field_content_type.readfrombuffer(buf)

            self.__field_url=STRING(**{'terminator': 0xA, 'default':'bitpim.org'})

            self.__field_url.readfrombuffer(buf)

            self.__field_unknown_int1=UINT(**{'sizeinbytes': 1, 'default':0x14})

            self.__field_unknown_int1.readfrombuffer(buf)

            self.__field_unknown3=STRING(**{'terminator': 0xA, 'default':''})

            self.__field_unknown3.readfrombuffer(buf)

            self.__field_unknown_int2=UINT(**{'sizeinbytes': 1, 'default':0x14})

            self.__field_unknown_int2.readfrombuffer(buf)

            self.__field_unknown4=STRING(**{'terminator': 0xA, 'default':''})

            self.__field_unknown4.readfrombuffer(buf)

            self.__field_unknown5=STRING(**{'terminator': 0xA, 'default':'0'})

            self.__field_unknown5.readfrombuffer(buf)

            self.__field_size=STRING(**{'terminator': 0xA})

            self.__field_size.readfrombuffer(buf)

        if self.type=='!E':

            self.__field_location_maybe=STRING(**{'terminator': 0xA, 'default':'ams:'})

            self.__field_location_maybe.readfrombuffer(buf)

            self.__field_index2=STRING(**{'terminator': 0xA})

            self.__field_index2.readfrombuffer(buf)

            self.__field_name2=STRING(**{'terminator': 0xA})

            self.__field_name2.readfrombuffer(buf)

            self.__field_unknown6=STRING(**{'terminator': 0xA, 'default':''})

            self.__field_unknown6.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_type(self):

        return self.__field_type.getvalue()

	def __setfield_type(self, value):

        if isinstance(value,STRING):

            self.__field_type=value

        else:

            self.__field_type=STRING(value,**{'sizeinbytes': 3, 'terminator': 0xA})

	def __delfield_type(self): del self.__field_type

	    type=property(__getfield_type, __setfield_type, __delfield_type, None)
	    def __getfield_index1(self):

        return self.__field_index1.getvalue()

	def __setfield_index1(self, value):

        if isinstance(value,STRING):

            self.__field_index1=value

        else:

            self.__field_index1=STRING(value,**{'terminator': 0xA})

	def __delfield_index1(self): del self.__field_index1

	    index1=property(__getfield_index1, __setfield_index1, __delfield_index1, None)
	    def __getfield_name1(self):

        return self.__field_name1.getvalue()

	def __setfield_name1(self, value):

        if isinstance(value,STRING):

            self.__field_name1=value

        else:

            self.__field_name1=STRING(value,**{'terminator': 0xA})

	def __delfield_name1(self): del self.__field_name1

	    name1=property(__getfield_name1, __setfield_name1, __delfield_name1, None)
	    def __getfield_unknown1(self):

        try: self.__field_unknown1

        except:

            self.__field_unknown1=STRING(**{'terminator': 0xA, 'default': '-1'})

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,STRING):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=STRING(value,**{'terminator': 0xA, 'default': '-1'})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_unknown2(self):

        try: self.__field_unknown2

        except:

            self.__field_unknown2=UINT(**{'sizeinbytes': 8, 'default' :0})

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 8, 'default' :0})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_mime_type(self):

        return self.__field_mime_type.getvalue()

	def __setfield_mime_type(self, value):

        if isinstance(value,STRING):

            self.__field_mime_type=value

        else:

            self.__field_mime_type=STRING(value,**{'terminator': 0xA})

	def __delfield_mime_type(self): del self.__field_mime_type

	    mime_type=property(__getfield_mime_type, __setfield_mime_type, __delfield_mime_type, None)
	    def __getfield_content_type(self):

        return self.__field_content_type.getvalue()

	def __setfield_content_type(self, value):

        if isinstance(value,STRING):

            self.__field_content_type=value

        else:

            self.__field_content_type=STRING(value,**{'terminator': 0xA})

	def __delfield_content_type(self): del self.__field_content_type

	    content_type=property(__getfield_content_type, __setfield_content_type, __delfield_content_type, None)
	    def __getfield_url(self):

        try: self.__field_url

        except:

            self.__field_url=STRING(**{'terminator': 0xA, 'default':'bitpim.org'})

        return self.__field_url.getvalue()

	def __setfield_url(self, value):

        if isinstance(value,STRING):

            self.__field_url=value

        else:

            self.__field_url=STRING(value,**{'terminator': 0xA, 'default':'bitpim.org'})

	def __delfield_url(self): del self.__field_url

	    url=property(__getfield_url, __setfield_url, __delfield_url, None)
	    def __getfield_unknown_int1(self):

        try: self.__field_unknown_int1

        except:

            self.__field_unknown_int1=UINT(**{'sizeinbytes': 1, 'default':0x14})

        return self.__field_unknown_int1.getvalue()

	def __setfield_unknown_int1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown_int1=value

        else:

            self.__field_unknown_int1=UINT(value,**{'sizeinbytes': 1, 'default':0x14})

	def __delfield_unknown_int1(self): del self.__field_unknown_int1

	    unknown_int1=property(__getfield_unknown_int1, __setfield_unknown_int1, __delfield_unknown_int1, None)
	    def __getfield_unknown3(self):

        try: self.__field_unknown3

        except:

            self.__field_unknown3=STRING(**{'terminator': 0xA, 'default':''})

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,STRING):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=STRING(value,**{'terminator': 0xA, 'default':''})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def __getfield_unknown_int2(self):

        try: self.__field_unknown_int2

        except:

            self.__field_unknown_int2=UINT(**{'sizeinbytes': 1, 'default':0x14})

        return self.__field_unknown_int2.getvalue()

	def __setfield_unknown_int2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown_int2=value

        else:

            self.__field_unknown_int2=UINT(value,**{'sizeinbytes': 1, 'default':0x14})

	def __delfield_unknown_int2(self): del self.__field_unknown_int2

	    unknown_int2=property(__getfield_unknown_int2, __setfield_unknown_int2, __delfield_unknown_int2, None)
	    def __getfield_unknown4(self):

        try: self.__field_unknown4

        except:

            self.__field_unknown4=STRING(**{'terminator': 0xA, 'default':''})

        return self.__field_unknown4.getvalue()

	def __setfield_unknown4(self, value):

        if isinstance(value,STRING):

            self.__field_unknown4=value

        else:

            self.__field_unknown4=STRING(value,**{'terminator': 0xA, 'default':''})

	def __delfield_unknown4(self): del self.__field_unknown4

	    unknown4=property(__getfield_unknown4, __setfield_unknown4, __delfield_unknown4, None)
	    def __getfield_unknown5(self):

        try: self.__field_unknown5

        except:

            self.__field_unknown5=STRING(**{'terminator': 0xA, 'default':'0'})

        return self.__field_unknown5.getvalue()

	def __setfield_unknown5(self, value):

        if isinstance(value,STRING):

            self.__field_unknown5=value

        else:

            self.__field_unknown5=STRING(value,**{'terminator': 0xA, 'default':'0'})

	def __delfield_unknown5(self): del self.__field_unknown5

	    unknown5=property(__getfield_unknown5, __setfield_unknown5, __delfield_unknown5, None)
	    def __getfield_size(self):

        return self.__field_size.getvalue()

	def __setfield_size(self, value):

        if isinstance(value,STRING):

            self.__field_size=value

        else:

            self.__field_size=STRING(value,**{'terminator': 0xA})

	def __delfield_size(self): del self.__field_size

	    size=property(__getfield_size, __setfield_size, __delfield_size, None)
	    def __getfield_location_maybe(self):

        try: self.__field_location_maybe

        except:

            self.__field_location_maybe=STRING(**{'terminator': 0xA, 'default':'ams:'})

        return self.__field_location_maybe.getvalue()

	def __setfield_location_maybe(self, value):

        if isinstance(value,STRING):

            self.__field_location_maybe=value

        else:

            self.__field_location_maybe=STRING(value,**{'terminator': 0xA, 'default':'ams:'})

	def __delfield_location_maybe(self): del self.__field_location_maybe

	    location_maybe=property(__getfield_location_maybe, __setfield_location_maybe, __delfield_location_maybe, None)
	    def __getfield_index2(self):

        return self.__field_index2.getvalue()

	def __setfield_index2(self, value):

        if isinstance(value,STRING):

            self.__field_index2=value

        else:

            self.__field_index2=STRING(value,**{'terminator': 0xA})

	def __delfield_index2(self): del self.__field_index2

	    index2=property(__getfield_index2, __setfield_index2, __delfield_index2, None)
	    def __getfield_name2(self):

        return self.__field_name2.getvalue()

	def __setfield_name2(self, value):

        if isinstance(value,STRING):

            self.__field_name2=value

        else:

            self.__field_name2=STRING(value,**{'terminator': 0xA})

	def __delfield_name2(self): del self.__field_name2

	    name2=property(__getfield_name2, __setfield_name2, __delfield_name2, None)
	    def __getfield_unknown6(self):

        try: self.__field_unknown6

        except:

            self.__field_unknown6=STRING(**{'terminator': 0xA, 'default':''})

        return self.__field_unknown6.getvalue()

	def __setfield_unknown6(self, value):

        if isinstance(value,STRING):

            self.__field_unknown6=value

        else:

            self.__field_unknown6=STRING(value,**{'terminator': 0xA, 'default':''})

	def __delfield_unknown6(self): del self.__field_unknown6

	    unknown6=property(__getfield_unknown6, __setfield_unknown6, __delfield_unknown6, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('type', self.__field_type, None)

        if self.type=='!C':

            yield ('index1', self.__field_index1, None)

            yield ('name1', self.__field_name1, None)

            yield ('unknown1', self.__field_unknown1, None)

            yield ('unknown2', self.__field_unknown2, None)

            yield ('mime_type', self.__field_mime_type, None)

            yield ('content_type', self.__field_content_type, None)

            yield ('url', self.__field_url, None)

            yield ('unknown_int1', self.__field_unknown_int1, None)

            yield ('unknown3', self.__field_unknown3, None)

            yield ('unknown_int2', self.__field_unknown_int2, None)

            yield ('unknown4', self.__field_unknown4, None)

            yield ('unknown5', self.__field_unknown5, None)

            yield ('size', self.__field_size, None)

        if self.type=='!E':

            yield ('location_maybe', self.__field_location_maybe, None)

            yield ('index2', self.__field_index2, None)

            yield ('name2', self.__field_name2, None)

            yield ('unknown6', self.__field_unknown6, None)


class  content_file (BaseProtogenClass) :
	"Used to store all content on the phone, apps, ringers and images (with the exception of the camera)"
	    __fields=['items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(content_file,self).__init__(**dict)

        if self.__class__ is content_file:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(content_file,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(content_file,kwargs)

        if len(args):

            dict2={'elementclass': content_entry, 'createdefault': True}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_items=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'elementclass': content_entry, 'createdefault': True})

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items=LIST(**{'elementclass': content_entry, 'createdefault': True})

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_items(self):

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'elementclass': content_entry, 'createdefault': True})

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{'elementclass': content_entry, 'createdefault': True})

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('items', self.__field_items, None)

	"Used to store all content on the phone, apps, ringers and images (with the exception of the camera)"

class  content_count (BaseProtogenClass) :
	"Stores the number of items in the content file"
	    __fields=['count']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(content_count,self).__init__(**dict)

        if self.__class__ is content_count:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(content_count,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(content_count,kwargs)

        if len(args):

            dict2={'terminator': None}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_count=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_count.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_count=STRING(**{'terminator': None})

        self.__field_count.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_count(self):

        return self.__field_count.getvalue()

	def __setfield_count(self, value):

        if isinstance(value,STRING):

            self.__field_count=value

        else:

            self.__field_count=STRING(value,**{'terminator': None})

	def __delfield_count(self): del self.__field_count

	    count=property(__getfield_count, __setfield_count, __delfield_count, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('count', self.__field_count, None)

	"Stores the number of items in the content file"

class  qcp_media_header (BaseProtogenClass) :
	"Start of a qcp format file, used to determine if a file is qcp or mp3 format"
	    __fields=['riff', 'riff_size', 'qcp_format', 'stuff']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(qcp_media_header,self).__init__(**dict)

        if self.__class__ is qcp_media_header:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(qcp_media_header,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(qcp_media_header,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_riff.writetobuffer(buf)

        self.__field_riff_size.writetobuffer(buf)

        self.__field_qcp_format.writetobuffer(buf)

        self.__field_stuff.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_riff=STRING(**{'sizeinbytes': 4, 'constant': 'RIFF', 'terminator': None})

        self.__field_riff.readfrombuffer(buf)

        self.__field_riff_size=UINT(**{'sizeinbytes': 4})

        self.__field_riff_size.readfrombuffer(buf)

        self.__field_qcp_format=STRING(**{'sizeinbytes': 8, 'constant': 'QLCMfmt ', 'terminator': None})

        self.__field_qcp_format.readfrombuffer(buf)

        self.__field_stuff=DATA()

        self.__field_stuff.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_riff(self):

        return self.__field_riff.getvalue()

	def __setfield_riff(self, value):

        if isinstance(value,STRING):

            self.__field_riff=value

        else:

            self.__field_riff=STRING(value,**{'sizeinbytes': 4, 'constant': 'RIFF', 'terminator': None})

	def __delfield_riff(self): del self.__field_riff

	    riff=property(__getfield_riff, __setfield_riff, __delfield_riff, None)
	    def __getfield_riff_size(self):

        return self.__field_riff_size.getvalue()

	def __setfield_riff_size(self, value):

        if isinstance(value,UINT):

            self.__field_riff_size=value

        else:

            self.__field_riff_size=UINT(value,**{'sizeinbytes': 4})

	def __delfield_riff_size(self): del self.__field_riff_size

	    riff_size=property(__getfield_riff_size, __setfield_riff_size, __delfield_riff_size, None)
	    def __getfield_qcp_format(self):

        return self.__field_qcp_format.getvalue()

	def __setfield_qcp_format(self, value):

        if isinstance(value,STRING):

            self.__field_qcp_format=value

        else:

            self.__field_qcp_format=STRING(value,**{'sizeinbytes': 8, 'constant': 'QLCMfmt ', 'terminator': None})

	def __delfield_qcp_format(self): del self.__field_qcp_format

	    qcp_format=property(__getfield_qcp_format, __setfield_qcp_format, __delfield_qcp_format, None)
	    def __getfield_stuff(self):

        return self.__field_stuff.getvalue()

	def __setfield_stuff(self, value):

        if isinstance(value,DATA):

            self.__field_stuff=value

        else:

            self.__field_stuff=DATA(value,)

	def __delfield_stuff(self): del self.__field_stuff

	    stuff=property(__getfield_stuff, __setfield_stuff, __delfield_stuff, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('riff', self.__field_riff, None)

        yield ('riff_size', self.__field_riff_size, None)

        yield ('qcp_format', self.__field_qcp_format, None)

        yield ('stuff', self.__field_stuff, None)

	"Start of a qcp format file, used to determine if a file is qcp or mp3 format"

class  textmemo (BaseProtogenClass) :
	__fields=['text']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(textmemo,self).__init__(**dict)

        if self.__class__ is textmemo:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(textmemo,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(textmemo,kwargs)

        if len(args):

            dict2={'sizeinbytes': 151,  'raiseonunterminatedread': False, 'raiseontruncate': False }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_text=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_text.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_text=STRING(**{'sizeinbytes': 151,  'raiseonunterminatedread': False, 'raiseontruncate': False })

        self.__field_text.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_text(self):

        return self.__field_text.getvalue()

	def __setfield_text(self, value):

        if isinstance(value,STRING):

            self.__field_text=value

        else:

            self.__field_text=STRING(value,**{'sizeinbytes': 151,  'raiseonunterminatedread': False, 'raiseontruncate': False })

	def __delfield_text(self): del self.__field_text

	    text=property(__getfield_text, __setfield_text, __delfield_text, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('text', self.__field_text, None)


class  textmemofile (BaseProtogenClass) :
	__fields=['itemcount', 'items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(textmemofile,self).__init__(**dict)

        if self.__class__ is textmemofile:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(textmemofile,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(textmemofile,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_itemcount.writetobuffer(buf)

        try: self.__field_items

        except:

            self.__field_items=LIST(**{ 'elementclass': textmemo })

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_itemcount=UINT(**{'sizeinbytes': 4})

        self.__field_itemcount.readfrombuffer(buf)

        self.__field_items=LIST(**{ 'elementclass': textmemo })

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_itemcount(self):

        return self.__field_itemcount.getvalue()

	def __setfield_itemcount(self, value):

        if isinstance(value,UINT):

            self.__field_itemcount=value

        else:

            self.__field_itemcount=UINT(value,**{'sizeinbytes': 4})

	def __delfield_itemcount(self): del self.__field_itemcount

	    itemcount=property(__getfield_itemcount, __setfield_itemcount, __delfield_itemcount, None)
	    def __getfield_items(self):

        try: self.__field_items

        except:

            self.__field_items=LIST(**{ 'elementclass': textmemo })

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{ 'elementclass': textmemo })

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('itemcount', self.__field_itemcount, None)

        yield ('items', self.__field_items, None)


class  recipient_record (BaseProtogenClass) :
	__fields=['unknown1', 'name', 'number', 'unknown2', 'status', 'unknown3', 'time', 'unknown2']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(recipient_record,self).__init__(**dict)

        if self.__class__ is recipient_record:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(recipient_record,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(recipient_record,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_unknown1.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_number.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_status.writetobuffer(buf)

        self.__field_unknown3.writetobuffer(buf)

        self.__field_time.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_unknown1=UINT(**{'sizeinbytes': 8})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 33, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self.__field_number=STRING(**{'sizeinbytes': 49})

        self.__field_number.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 24})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_status=UINT(**{'sizeinbytes': 1})

        self.__field_status.readfrombuffer(buf)

        self.__field_unknown3=UINT(**{'sizeinbytes': 1})

        self.__field_unknown3.readfrombuffer(buf)

        self.__field_time=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_time.readfrombuffer(buf)

        self.__field_unknown2=DATA(**{'sizeinbytes': 12})

        self.__field_unknown2.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 8})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 33, 'raiseonunterminatedread': False})

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_number(self):

        return self.__field_number.getvalue()

	def __setfield_number(self, value):

        if isinstance(value,STRING):

            self.__field_number=value

        else:

            self.__field_number=STRING(value,**{'sizeinbytes': 49})

	def __delfield_number(self): del self.__field_number

	    number=property(__getfield_number, __setfield_number, __delfield_number, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 24})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_status(self):

        return self.__field_status.getvalue()

	def __setfield_status(self, value):

        if isinstance(value,UINT):

            self.__field_status=value

        else:

            self.__field_status=UINT(value,**{'sizeinbytes': 1})

	def __delfield_status(self): del self.__field_status

	    status=property(__getfield_status, __setfield_status, __delfield_status, None)
	    def __getfield_unknown3(self):

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,UINT):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def __getfield_time(self):

        return self.__field_time.getvalue()

	def __setfield_time(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_time=value

        else:

            self.__field_time=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_time(self): del self.__field_time

	    time=property(__getfield_time, __setfield_time, __delfield_time, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,DATA):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=DATA(value,**{'sizeinbytes': 12})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('unknown1', self.__field_unknown1, None)

        yield ('name', self.__field_name, None)

        yield ('number', self.__field_number, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('status', self.__field_status, None)

        yield ('unknown3', self.__field_unknown3, None)

        yield ('time', self.__field_time, None)

        yield ('unknown2', self.__field_unknown2, None)


class  sms_saved (BaseProtogenClass) :
	__fields=['outboxmsg', 'GPStime', 'outbox', 'inbox']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_saved,self).__init__(**dict)

        if self.__class__ is sms_saved:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_saved,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_saved,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_outboxmsg.writetobuffer(buf)

        self.__field_GPStime.writetobuffer(buf)

        if self.outboxmsg:

            self.__field_outbox.writetobuffer(buf)

        if not self.outboxmsg:

            self.__field_inbox.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_outboxmsg=UINT(**{'sizeinbytes': 4})

        self.__field_outboxmsg.readfrombuffer(buf)

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        if self.outboxmsg:

            self.__field_outbox=sms_out()

            self.__field_outbox.readfrombuffer(buf)

        if not self.outboxmsg:

            self.__field_inbox=sms_in()

            self.__field_inbox.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_outboxmsg(self):

        return self.__field_outboxmsg.getvalue()

	def __setfield_outboxmsg(self, value):

        if isinstance(value,UINT):

            self.__field_outboxmsg=value

        else:

            self.__field_outboxmsg=UINT(value,**{'sizeinbytes': 4})

	def __delfield_outboxmsg(self): del self.__field_outboxmsg

	    outboxmsg=property(__getfield_outboxmsg, __setfield_outboxmsg, __delfield_outboxmsg, None)
	    def __getfield_GPStime(self):

        return self.__field_GPStime.getvalue()

	def __setfield_GPStime(self, value):

        if isinstance(value,GPSDATE):

            self.__field_GPStime=value

        else:

            self.__field_GPStime=GPSDATE(value,**{'sizeinbytes': 4})

	def __delfield_GPStime(self): del self.__field_GPStime

	    GPStime=property(__getfield_GPStime, __setfield_GPStime, __delfield_GPStime, None)
	    def __getfield_outbox(self):

        return self.__field_outbox.getvalue()

	def __setfield_outbox(self, value):

        if isinstance(value,sms_out):

            self.__field_outbox=value

        else:

            self.__field_outbox=sms_out(value,)

	def __delfield_outbox(self): del self.__field_outbox

	    outbox=property(__getfield_outbox, __setfield_outbox, __delfield_outbox, None)
	    def __getfield_inbox(self):

        return self.__field_inbox.getvalue()

	def __setfield_inbox(self, value):

        if isinstance(value,sms_in):

            self.__field_inbox=value

        else:

            self.__field_inbox=sms_in(value,)

	def __delfield_inbox(self): del self.__field_inbox

	    inbox=property(__getfield_inbox, __setfield_inbox, __delfield_inbox, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('outboxmsg', self.__field_outboxmsg, None)

        yield ('GPStime', self.__field_GPStime, None)

        if self.outboxmsg:

            yield ('outbox', self.__field_outbox, None)

        if not self.outboxmsg:

            yield ('inbox', self.__field_inbox, None)


class  sms_out (BaseProtogenClass) :
	__fields=['index', 'locked', 'unknown1', 'timesent', 'unknown2', 'msg', 'unknown3', 'callback', 'recipients']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_out,self).__init__(**dict)

        if self.__class__ is sms_out:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_out,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_out,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index.writetobuffer(buf)

        self.__field_locked.writetobuffer(buf)

        self.__field_unknown1.writetobuffer(buf)

        self.__field_timesent.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_msg.writetobuffer(buf)

        self.__field_unknown3.writetobuffer(buf)

        self.__field_callback.writetobuffer(buf)

        try: self.__field_recipients

        except:

            self.__field_recipients=LIST(**{'elementclass': recipient_record, 'length': 10})

        self.__field_recipients.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index=UINT(**{'sizeinbytes': 4})

        self.__field_index.readfrombuffer(buf)

        self.__field_locked=UINT(**{'sizeinbytes': 1})

        self.__field_locked.readfrombuffer(buf)

        self.__field_unknown1=UINT(**{'sizeinbytes': 3})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_timesent=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_timesent.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 1})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_msg=STRING(**{'sizeinbytes': 178})

        self.__field_msg.readfrombuffer(buf)

        self.__field_unknown3=UINT(**{'sizeinbytes': 1})

        self.__field_unknown3.readfrombuffer(buf)

        self.__field_callback=STRING(**{'sizeinbytes': 16})

        self.__field_callback.readfrombuffer(buf)

        self.__field_recipients=LIST(**{'elementclass': recipient_record, 'length': 10})

        self.__field_recipients.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,UINT):

            self.__field_index=value

        else:

            self.__field_index=UINT(value,**{'sizeinbytes': 4})

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_locked(self):

        return self.__field_locked.getvalue()

	def __setfield_locked(self, value):

        if isinstance(value,UINT):

            self.__field_locked=value

        else:

            self.__field_locked=UINT(value,**{'sizeinbytes': 1})

	def __delfield_locked(self): del self.__field_locked

	    locked=property(__getfield_locked, __setfield_locked, __delfield_locked, None)
	    def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 3})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_timesent(self):

        return self.__field_timesent.getvalue()

	def __setfield_timesent(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_timesent=value

        else:

            self.__field_timesent=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_timesent(self): del self.__field_timesent

	    timesent=property(__getfield_timesent, __setfield_timesent, __delfield_timesent, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_msg(self):

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,STRING):

            self.__field_msg=value

        else:

            self.__field_msg=STRING(value,**{'sizeinbytes': 178})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def __getfield_unknown3(self):

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,UINT):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def __getfield_callback(self):

        return self.__field_callback.getvalue()

	def __setfield_callback(self, value):

        if isinstance(value,STRING):

            self.__field_callback=value

        else:

            self.__field_callback=STRING(value,**{'sizeinbytes': 16})

	def __delfield_callback(self): del self.__field_callback

	    callback=property(__getfield_callback, __setfield_callback, __delfield_callback, None)
	    def __getfield_recipients(self):

        try: self.__field_recipients

        except:

            self.__field_recipients=LIST(**{'elementclass': recipient_record, 'length': 10})

        return self.__field_recipients.getvalue()

	def __setfield_recipients(self, value):

        if isinstance(value,LIST):

            self.__field_recipients=value

        else:

            self.__field_recipients=LIST(value,**{'elementclass': recipient_record, 'length': 10})

	def __delfield_recipients(self): del self.__field_recipients

	    recipients=property(__getfield_recipients, __setfield_recipients, __delfield_recipients, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('index', self.__field_index, None)

        yield ('locked', self.__field_locked, None)

        yield ('unknown1', self.__field_unknown1, None)

        yield ('timesent', self.__field_timesent, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('msg', self.__field_msg, None)

        yield ('unknown3', self.__field_unknown3, None)

        yield ('callback', self.__field_callback, None)

        yield ('recipients', self.__field_recipients, None)


class  SMSINBOXMSGFRAGMENT (BaseProtogenClass) :
	__fields=['msg']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(SMSINBOXMSGFRAGMENT,self).__init__(**dict)

        if self.__class__ is SMSINBOXMSGFRAGMENT:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(SMSINBOXMSGFRAGMENT,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(SMSINBOXMSGFRAGMENT,kwargs)

        if len(args):

            dict2={'elementclass': _gen_p_lgpm325_363, 'length': 181}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_msg=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_msg

        except:

            self.__field_msg=LIST(**{'elementclass': _gen_p_lgpm325_363, 'length': 181})

        self.__field_msg.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msg=LIST(**{'elementclass': _gen_p_lgpm325_363, 'length': 181})

        self.__field_msg.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_msg(self):

        try: self.__field_msg

        except:

            self.__field_msg=LIST(**{'elementclass': _gen_p_lgpm325_363, 'length': 181})

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,LIST):

            self.__field_msg=value

        else:

            self.__field_msg=LIST(value,**{'elementclass': _gen_p_lgpm325_363, 'length': 181})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('msg', self.__field_msg, None)


class  _gen_p_lgpm325_363 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['byte']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgpm325_363,self).__init__(**dict)

        if self.__class__ is _gen_p_lgpm325_363:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgpm325_363,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgpm325_363,kwargs)

        if len(args):

            dict2={'sizeinbytes': 1}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_byte=UINT(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_byte.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_byte=UINT(**{'sizeinbytes': 1})

        self.__field_byte.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_byte(self):

        return self.__field_byte.getvalue()

	def __setfield_byte(self, value):

        if isinstance(value,UINT):

            self.__field_byte=value

        else:

            self.__field_byte=UINT(value,**{'sizeinbytes': 1})

	def __delfield_byte(self): del self.__field_byte

	    byte=property(__getfield_byte, __setfield_byte, __delfield_byte, "individual byte of message")
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('byte', self.__field_byte, "individual byte of message")

	'Anonymous inner class'

class  sms_in (BaseProtogenClass) :
	__fields=['unknown1', 'msg_index2', 'unknown2', 'timesent', 'unknown', 'callback_length', 'callback', 'sender_length', 'sender', 'unknown4', 'lg_time', 'GPStime', 'read', 'unknown5', 'subject', 'msglength', 'msg', 'unknown8']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_in,self).__init__(**dict)

        if self.__class__ is sms_in:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_in,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_in,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_unknown1.writetobuffer(buf)

        self.__field_msg_index2.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_timesent.writetobuffer(buf)

        self.__field_unknown.writetobuffer(buf)

        self.__field_callback_length.writetobuffer(buf)

        self.__field_callback.writetobuffer(buf)

        self.__field_sender_length.writetobuffer(buf)

        try: self.__field_sender

        except:

            self.__field_sender=LIST(**{'elementclass': _gen_p_lgpm325_375, 'length': 38})

        self.__field_sender.writetobuffer(buf)

        self.__field_unknown4.writetobuffer(buf)

        self.__field_lg_time.writetobuffer(buf)

        self.__field_GPStime.writetobuffer(buf)

        self.__field_read.writetobuffer(buf)

        self.__field_unknown5.writetobuffer(buf)

        self.__field_subject.writetobuffer(buf)

        self.__field_msglength.writetobuffer(buf)

        self.__field_msg.writetobuffer(buf)

        self.__field_unknown8.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_unknown1=UINT(**{'sizeinbytes': 4})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_msg_index2=UINT(**{'sizeinbytes': 4})

        self.__field_msg_index2.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 2})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_timesent=SMSDATE(**{'sizeinbytes': 6})

        self.__field_timesent.readfrombuffer(buf)

        self.__field_unknown=UINT(**{'sizeinbytes': 3})

        self.__field_unknown.readfrombuffer(buf)

        self.__field_callback_length=UINT(**{'sizeinbytes': 1})

        self.__field_callback_length.readfrombuffer(buf)

        self.__field_callback=STRING(**{'sizeinbytes': 38})

        self.__field_callback.readfrombuffer(buf)

        self.__field_sender_length=UINT(**{'sizeinbytes': 1})

        self.__field_sender_length.readfrombuffer(buf)

        self.__field_sender=LIST(**{'elementclass': _gen_p_lgpm325_375, 'length': 38})

        self.__field_sender.readfrombuffer(buf)

        self.__field_unknown4=DATA(**{'sizeinbytes': 15})

        self.__field_unknown4.readfrombuffer(buf)

        self.__field_lg_time=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_lg_time.readfrombuffer(buf)

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        self.__field_read=UINT(**{'sizeinbytes': 2})

        self.__field_read.readfrombuffer(buf)

        self.__field_unknown5=UINT(**{'sizeinbytes': 9})

        self.__field_unknown5.readfrombuffer(buf)

        self.__field_subject=STRING(**{'sizeinbytes': 73})

        self.__field_subject.readfrombuffer(buf)

        self.__field_msglength=UINT(**{'sizeinbytes': 2})

        self.__field_msglength.readfrombuffer(buf)

        self.__field_msg=STRING(**{'sizeinbytes': 200})

        self.__field_msg.readfrombuffer(buf)

        self.__field_unknown8=DATA()

        self.__field_unknown8.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 4})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_msg_index2(self):

        return self.__field_msg_index2.getvalue()

	def __setfield_msg_index2(self, value):

        if isinstance(value,UINT):

            self.__field_msg_index2=value

        else:

            self.__field_msg_index2=UINT(value,**{'sizeinbytes': 4})

	def __delfield_msg_index2(self): del self.__field_msg_index2

	    msg_index2=property(__getfield_msg_index2, __setfield_msg_index2, __delfield_msg_index2, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 2})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_timesent(self):

        return self.__field_timesent.getvalue()

	def __setfield_timesent(self, value):

        if isinstance(value,SMSDATE):

            self.__field_timesent=value

        else:

            self.__field_timesent=SMSDATE(value,**{'sizeinbytes': 6})

	def __delfield_timesent(self): del self.__field_timesent

	    timesent=property(__getfield_timesent, __setfield_timesent, __delfield_timesent, None)
	    def __getfield_unknown(self):

        return self.__field_unknown.getvalue()

	def __setfield_unknown(self, value):

        if isinstance(value,UINT):

            self.__field_unknown=value

        else:

            self.__field_unknown=UINT(value,**{'sizeinbytes': 3})

	def __delfield_unknown(self): del self.__field_unknown

	    unknown=property(__getfield_unknown, __setfield_unknown, __delfield_unknown, None)
	    def __getfield_callback_length(self):

        return self.__field_callback_length.getvalue()

	def __setfield_callback_length(self, value):

        if isinstance(value,UINT):

            self.__field_callback_length=value

        else:

            self.__field_callback_length=UINT(value,**{'sizeinbytes': 1})

	def __delfield_callback_length(self): del self.__field_callback_length

	    callback_length=property(__getfield_callback_length, __setfield_callback_length, __delfield_callback_length, None)
	    def __getfield_callback(self):

        return self.__field_callback.getvalue()

	def __setfield_callback(self, value):

        if isinstance(value,STRING):

            self.__field_callback=value

        else:

            self.__field_callback=STRING(value,**{'sizeinbytes': 38})

	def __delfield_callback(self): del self.__field_callback

	    callback=property(__getfield_callback, __setfield_callback, __delfield_callback, None)
	    def __getfield_sender_length(self):

        return self.__field_sender_length.getvalue()

	def __setfield_sender_length(self, value):

        if isinstance(value,UINT):

            self.__field_sender_length=value

        else:

            self.__field_sender_length=UINT(value,**{'sizeinbytes': 1})

	def __delfield_sender_length(self): del self.__field_sender_length

	    sender_length=property(__getfield_sender_length, __setfield_sender_length, __delfield_sender_length, None)
	    def __getfield_sender(self):

        try: self.__field_sender

        except:

            self.__field_sender=LIST(**{'elementclass': _gen_p_lgpm325_375, 'length': 38})

        return self.__field_sender.getvalue()

	def __setfield_sender(self, value):

        if isinstance(value,LIST):

            self.__field_sender=value

        else:

            self.__field_sender=LIST(value,**{'elementclass': _gen_p_lgpm325_375, 'length': 38})

	def __delfield_sender(self): del self.__field_sender

	    sender=property(__getfield_sender, __setfield_sender, __delfield_sender, None)
	    def __getfield_unknown4(self):

        return self.__field_unknown4.getvalue()

	def __setfield_unknown4(self, value):

        if isinstance(value,DATA):

            self.__field_unknown4=value

        else:

            self.__field_unknown4=DATA(value,**{'sizeinbytes': 15})

	def __delfield_unknown4(self): del self.__field_unknown4

	    unknown4=property(__getfield_unknown4, __setfield_unknown4, __delfield_unknown4, None)
	    def __getfield_lg_time(self):

        return self.__field_lg_time.getvalue()

	def __setfield_lg_time(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_lg_time=value

        else:

            self.__field_lg_time=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_lg_time(self): del self.__field_lg_time

	    lg_time=property(__getfield_lg_time, __setfield_lg_time, __delfield_lg_time, None)
	    def __getfield_GPStime(self):

        return self.__field_GPStime.getvalue()

	def __setfield_GPStime(self, value):

        if isinstance(value,GPSDATE):

            self.__field_GPStime=value

        else:

            self.__field_GPStime=GPSDATE(value,**{'sizeinbytes': 4})

	def __delfield_GPStime(self): del self.__field_GPStime

	    GPStime=property(__getfield_GPStime, __setfield_GPStime, __delfield_GPStime, None)
	    def __getfield_read(self):

        return self.__field_read.getvalue()

	def __setfield_read(self, value):

        if isinstance(value,UINT):

            self.__field_read=value

        else:

            self.__field_read=UINT(value,**{'sizeinbytes': 2})

	def __delfield_read(self): del self.__field_read

	    read=property(__getfield_read, __setfield_read, __delfield_read, None)
	    def __getfield_unknown5(self):

        return self.__field_unknown5.getvalue()

	def __setfield_unknown5(self, value):

        if isinstance(value,UINT):

            self.__field_unknown5=value

        else:

            self.__field_unknown5=UINT(value,**{'sizeinbytes': 9})

	def __delfield_unknown5(self): del self.__field_unknown5

	    unknown5=property(__getfield_unknown5, __setfield_unknown5, __delfield_unknown5, None)
	    def __getfield_subject(self):

        return self.__field_subject.getvalue()

	def __setfield_subject(self, value):

        if isinstance(value,STRING):

            self.__field_subject=value

        else:

            self.__field_subject=STRING(value,**{'sizeinbytes': 73})

	def __delfield_subject(self): del self.__field_subject

	    subject=property(__getfield_subject, __setfield_subject, __delfield_subject, None)
	    def __getfield_msglength(self):

        return self.__field_msglength.getvalue()

	def __setfield_msglength(self, value):

        if isinstance(value,UINT):

            self.__field_msglength=value

        else:

            self.__field_msglength=UINT(value,**{'sizeinbytes': 2})

	def __delfield_msglength(self): del self.__field_msglength

	    msglength=property(__getfield_msglength, __setfield_msglength, __delfield_msglength, None)
	    def __getfield_msg(self):

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,STRING):

            self.__field_msg=value

        else:

            self.__field_msg=STRING(value,**{'sizeinbytes': 200})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def __getfield_unknown8(self):

        return self.__field_unknown8.getvalue()

	def __setfield_unknown8(self, value):

        if isinstance(value,DATA):

            self.__field_unknown8=value

        else:

            self.__field_unknown8=DATA(value,)

	def __delfield_unknown8(self): del self.__field_unknown8

	    unknown8=property(__getfield_unknown8, __setfield_unknown8, __delfield_unknown8, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('unknown1', self.__field_unknown1, None)

        yield ('msg_index2', self.__field_msg_index2, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('timesent', self.__field_timesent, None)

        yield ('unknown', self.__field_unknown, None)

        yield ('callback_length', self.__field_callback_length, None)

        yield ('callback', self.__field_callback, None)

        yield ('sender_length', self.__field_sender_length, None)

        yield ('sender', self.__field_sender, None)

        yield ('unknown4', self.__field_unknown4, None)

        yield ('lg_time', self.__field_lg_time, None)

        yield ('GPStime', self.__field_GPStime, None)

        yield ('read', self.__field_read, None)

        yield ('unknown5', self.__field_unknown5, None)

        yield ('subject', self.__field_subject, None)

        yield ('msglength', self.__field_msglength, None)

        yield ('msg', self.__field_msg, None)

        yield ('unknown8', self.__field_unknown8, None)


class  _gen_p_lgpm325_375 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['byte']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgpm325_375,self).__init__(**dict)

        if self.__class__ is _gen_p_lgpm325_375:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgpm325_375,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgpm325_375,kwargs)

        if len(args):

            dict2={'sizeinbytes': 1}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_byte=UINT(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_byte.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_byte=UINT(**{'sizeinbytes': 1})

        self.__field_byte.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_byte(self):

        return self.__field_byte.getvalue()

	def __setfield_byte(self, value):

        if isinstance(value,UINT):

            self.__field_byte=value

        else:

            self.__field_byte=UINT(value,**{'sizeinbytes': 1})

	def __delfield_byte(self): del self.__field_byte

	    byte=property(__getfield_byte, __setfield_byte, __delfield_byte, "individual byte of senders phone number")
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('byte', self.__field_byte, "individual byte of senders phone number")

	'Anonymous inner class'

class  sms_quick_text (BaseProtogenClass) :
	__fields=['dunno', 'msg']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_quick_text,self).__init__(**dict)

        if self.__class__ is sms_quick_text:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_quick_text,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_quick_text,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_dunno

        except:

            self.__field_dunno=UINT(**{'sizeinbytes': 4, 'default': 0})

        self.__field_dunno.writetobuffer(buf)

        try: self.__field_msg

        except:

            self.__field_msg=STRING(**{'sizeinbytes': 104, 'default': ""})

        self.__field_msg.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_dunno=UINT(**{'sizeinbytes': 4, 'default': 0})

        self.__field_dunno.readfrombuffer(buf)

        self.__field_msg=STRING(**{'sizeinbytes': 104, 'default': ""})

        self.__field_msg.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_dunno(self):

        try: self.__field_dunno

        except:

            self.__field_dunno=UINT(**{'sizeinbytes': 4, 'default': 0})

        return self.__field_dunno.getvalue()

	def __setfield_dunno(self, value):

        if isinstance(value,UINT):

            self.__field_dunno=value

        else:

            self.__field_dunno=UINT(value,**{'sizeinbytes': 4, 'default': 0})

	def __delfield_dunno(self): del self.__field_dunno

	    dunno=property(__getfield_dunno, __setfield_dunno, __delfield_dunno, None)
	    def __getfield_msg(self):

        try: self.__field_msg

        except:

            self.__field_msg=STRING(**{'sizeinbytes': 104, 'default': ""})

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,STRING):

            self.__field_msg=value

        else:

            self.__field_msg=STRING(value,**{'sizeinbytes': 104, 'default': ""})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('dunno', self.__field_dunno, None)

        yield ('msg', self.__field_msg, None)


class  sms_canned_file (BaseProtogenClass) :
	__fields=['num_active', 'msgs']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_canned_file,self).__init__(**dict)

        if self.__class__ is sms_canned_file:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_canned_file,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_canned_file,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_num_active.writetobuffer(buf)

        try: self.__field_msgs

        except:

            self.__field_msgs=LIST(**{'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True, 'elementclass': sms_quick_text})

        self.__field_msgs.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_num_active=UINT(**{'sizeinbytes': 4})

        self.__field_num_active.readfrombuffer(buf)

        self.__field_msgs=LIST(**{'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True, 'elementclass': sms_quick_text})

        self.__field_msgs.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_num_active(self):

        return self.__field_num_active.getvalue()

	def __setfield_num_active(self, value):

        if isinstance(value,UINT):

            self.__field_num_active=value

        else:

            self.__field_num_active=UINT(value,**{'sizeinbytes': 4})

	def __delfield_num_active(self): del self.__field_num_active

	    num_active=property(__getfield_num_active, __setfield_num_active, __delfield_num_active, None)
	    def __getfield_msgs(self):

        try: self.__field_msgs

        except:

            self.__field_msgs=LIST(**{'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True, 'elementclass': sms_quick_text})

        return self.__field_msgs.getvalue()

	def __setfield_msgs(self, value):

        if isinstance(value,LIST):

            self.__field_msgs=value

        else:

            self.__field_msgs=LIST(value,**{'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True, 'elementclass': sms_quick_text})

	def __delfield_msgs(self): del self.__field_msgs

	    msgs=property(__getfield_msgs, __setfield_msgs, __delfield_msgs, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('num_active', self.__field_num_active, None)

        yield ('msgs', self.__field_msgs, None)


