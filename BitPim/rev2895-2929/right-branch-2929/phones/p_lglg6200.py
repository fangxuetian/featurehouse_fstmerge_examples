"""Various descriptions of data specific to LG 6200 (Sprint)"""
import re
from prototypes import *
from prototypeslg import *
from p_lg import *
from p_lgpm225 import *
UINT=UINTlsb
BOOL=BOOLlsb
NUMSPEEDDIALS=99
FIRSTSPEEDDIAL=1
LASTSPEEDDIAL=99
NUMPHONEBOOKENTRIES=200
MEMOLENGTH=33
NORINGTONE=0
NOMSGRINGTONE=0
NOWALLPAPER=0
NUMEMAILS=3
NUMPHONENUMBERS=5
SMS_CANNED_MAX_ITEMS=40
SMS_CANNED_MAX_LENGTH=104
SMS_CANNED_FILENAME="sms/canned_msg.dat"
SMS_PATTERNS={'Inbox': re.compile(r"^.*/inbox[0-9][0-9][0-9]\.dat$"),
             'Sent': re.compile(r"^.*/outbox[0-9][0-9][0-9]\.dat$"),
             'Saved': re.compile(r"^.*/sf[0-9][0-9]\.dat$"),
             }
numbertypetab=( 'cell', 'home', 'office', 'fax', 'pager' )
text_memo_file='sch/memo.dat'
content_file_name='ams/contentInfo'
content_count_file_name='ams/realContent'
media_directory='ams'
ringerindex='setas/amsRingerIndex.map'
imageindex='setas/amsImageIndex.map'
ringerconst=2
imageconst=3
max_ringers=100
max_images=100
phonebook_media='pim/pbookcontact.dat'
NUMCALENDARENTRIES=300  # ?? for VX4400
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
class pbreadentryresponse(BaseProtogenClass):
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
class pbupdateentryrequest(BaseProtogenClass):
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
class pbappendentryrequest(BaseProtogenClass):
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
class pbentry(BaseProtogenClass):
    __fields=['serial1', 'entrysize', 'entrynumber', 'unknown1', 'name', 'group', 'unknown2', 'secret', 'memo', 'emails', 'url', 'numberspeeds', 'numbertypes', 'numbers', 'EndOfRecord', 'ringtone', 'wallpaper']
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
        if getattr(self, '__field_ringtone', None) is None:
            self.__field_ringtone=UINT(**{'default': 0x600})
        if getattr(self, '__field_wallpaper', None) is None:
            self.__field_wallpaper=UINT(**{'default': 0x100})
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_serial1.writetobuffer(buf)
        try: self.__field_entrysize
        except:
            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x026e})
        self.__field_entrysize.writetobuffer(buf)
        self.__field_entrynumber.writetobuffer(buf)
        try: self.__field_unknown1
        except:
            self.__field_unknown1=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_unknown1.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        self.__field_group.writetobuffer(buf)
        try: self.__field_unknown2
        except:
            self.__field_unknown2=UINT(**{'sizeinbytes': 2, 'default': 0x10})
        self.__field_unknown2.writetobuffer(buf)
        self.__field_secret.writetobuffer(buf)
        self.__field_memo.writetobuffer(buf)
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'elementclass': _gen_p_lglg6200_128, 'length': NUMEMAILS})
        self.__field_emails.writetobuffer(buf)
        self.__field_url.writetobuffer(buf)
        try: self.__field_numberspeeds
        except:
            self.__field_numberspeeds=LIST(**{'elementclass': _gen_p_lglg6200_131, 'length': NUMPHONENUMBERS})
        self.__field_numberspeeds.writetobuffer(buf)
        try: self.__field_numbertypes
        except:
            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lglg6200_133, 'length': NUMPHONENUMBERS})
        self.__field_numbertypes.writetobuffer(buf)
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'elementclass': _gen_p_lglg6200_135, 'length': NUMPHONENUMBERS})
        self.__field_numbers.writetobuffer(buf)
        try: self.__field_EndOfRecord
        except:
            self.__field_EndOfRecord=UINT(**{'sizeinbytes': 2, 'constant': 0x0278})
        self.__field_EndOfRecord.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_serial1=UINT(**{'sizeinbytes': 4})
        self.__field_serial1.readfrombuffer(buf)
        self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x026e})
        self.__field_entrysize.readfrombuffer(buf)
        self.__field_entrynumber=UINT(**{'sizeinbytes': 2})
        self.__field_entrynumber.readfrombuffer(buf)
        self.__field_unknown1=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_name=STRING(**{'sizeinbytes': 33, 'raiseonunterminatedread': False})
        self.__field_name.readfrombuffer(buf)
        self.__field_group=UINT(**{'sizeinbytes': 2})
        self.__field_group.readfrombuffer(buf)
        self.__field_unknown2=UINT(**{'sizeinbytes': 2, 'default': 0x10})
        self.__field_unknown2.readfrombuffer(buf)
        self.__field_secret=BOOL(**{'sizeinbytes': 1})
        self.__field_secret.readfrombuffer(buf)
        self.__field_memo=STRING(**{'raiseonunterminatedread': False, 'sizeinbytes': MEMOLENGTH})
        self.__field_memo.readfrombuffer(buf)
        self.__field_emails=LIST(**{'elementclass': _gen_p_lglg6200_128, 'length': NUMEMAILS})
        self.__field_emails.readfrombuffer(buf)
        self.__field_url=STRING(**{'sizeinbytes': 73, 'raiseonunterminatedread': False})
        self.__field_url.readfrombuffer(buf)
        self.__field_numberspeeds=LIST(**{'elementclass': _gen_p_lglg6200_131, 'length': NUMPHONENUMBERS})
        self.__field_numberspeeds.readfrombuffer(buf)
        self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lglg6200_133, 'length': NUMPHONENUMBERS})
        self.__field_numbertypes.readfrombuffer(buf)
        self.__field_numbers=LIST(**{'elementclass': _gen_p_lglg6200_135, 'length': NUMPHONENUMBERS})
        self.__field_numbers.readfrombuffer(buf)
        self.__field_EndOfRecord=UINT(**{'sizeinbytes': 2, 'constant': 0x0278})
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
            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x026e})
        return self.__field_entrysize.getvalue()
    def __setfield_entrysize(self, value):
        if isinstance(value,UINT):
            self.__field_entrysize=value
        else:
            self.__field_entrysize=UINT(value,**{'sizeinbytes': 2, 'constant': 0x026e})
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
            self.__field_unknown1=UINT(**{'sizeinbytes': 2, 'default': 0})
        return self.__field_unknown1.getvalue()
    def __setfield_unknown1(self, value):
        if isinstance(value,UINT):
            self.__field_unknown1=value
        else:
            self.__field_unknown1=UINT(value,**{'sizeinbytes': 2, 'default': 0})
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
    def __getfield_unknown2(self):
        try: self.__field_unknown2
        except:
            self.__field_unknown2=UINT(**{'sizeinbytes': 2, 'default': 0x10})
        return self.__field_unknown2.getvalue()
    def __setfield_unknown2(self, value):
        if isinstance(value,UINT):
            self.__field_unknown2=value
        else:
            self.__field_unknown2=UINT(value,**{'sizeinbytes': 2, 'default': 0x10})
    def __delfield_unknown2(self): del self.__field_unknown2
    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
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
            self.__field_emails=LIST(**{'elementclass': _gen_p_lglg6200_128, 'length': NUMEMAILS})
        return self.__field_emails.getvalue()
    def __setfield_emails(self, value):
        if isinstance(value,LIST):
            self.__field_emails=value
        else:
            self.__field_emails=LIST(value,**{'elementclass': _gen_p_lglg6200_128, 'length': NUMEMAILS})
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
            self.__field_numberspeeds=LIST(**{'elementclass': _gen_p_lglg6200_131, 'length': NUMPHONENUMBERS})
        return self.__field_numberspeeds.getvalue()
    def __setfield_numberspeeds(self, value):
        if isinstance(value,LIST):
            self.__field_numberspeeds=value
        else:
            self.__field_numberspeeds=LIST(value,**{'elementclass': _gen_p_lglg6200_131, 'length': NUMPHONENUMBERS})
    def __delfield_numberspeeds(self): del self.__field_numberspeeds
    numberspeeds=property(__getfield_numberspeeds, __setfield_numberspeeds, __delfield_numberspeeds, None)
    def __getfield_numbertypes(self):
        try: self.__field_numbertypes
        except:
            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lglg6200_133, 'length': NUMPHONENUMBERS})
        return self.__field_numbertypes.getvalue()
    def __setfield_numbertypes(self, value):
        if isinstance(value,LIST):
            self.__field_numbertypes=value
        else:
            self.__field_numbertypes=LIST(value,**{'elementclass': _gen_p_lglg6200_133, 'length': NUMPHONENUMBERS})
    def __delfield_numbertypes(self): del self.__field_numbertypes
    numbertypes=property(__getfield_numbertypes, __setfield_numbertypes, __delfield_numbertypes, None)
    def __getfield_numbers(self):
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'elementclass': _gen_p_lglg6200_135, 'length': NUMPHONENUMBERS})
        return self.__field_numbers.getvalue()
    def __setfield_numbers(self, value):
        if isinstance(value,LIST):
            self.__field_numbers=value
        else:
            self.__field_numbers=LIST(value,**{'elementclass': _gen_p_lglg6200_135, 'length': NUMPHONENUMBERS})
    def __delfield_numbers(self): del self.__field_numbers
    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
    def __getfield_EndOfRecord(self):
        try: self.__field_EndOfRecord
        except:
            self.__field_EndOfRecord=UINT(**{'sizeinbytes': 2, 'constant': 0x0278})
        return self.__field_EndOfRecord.getvalue()
    def __setfield_EndOfRecord(self, value):
        if isinstance(value,UINT):
            self.__field_EndOfRecord=value
        else:
            self.__field_EndOfRecord=UINT(value,**{'sizeinbytes': 2, 'constant': 0x0278})
    def __delfield_EndOfRecord(self): del self.__field_EndOfRecord
    EndOfRecord=property(__getfield_EndOfRecord, __setfield_EndOfRecord, __delfield_EndOfRecord, None)
    def __getfield_ringtone(self):
        try: self.__field_ringtone
        except:
            self.__field_ringtone=UINT(**{'default': 0x600})
        return self.__field_ringtone.getvalue()
    def __setfield_ringtone(self, value):
        if isinstance(value,UINT):
            self.__field_ringtone=value
        else:
            self.__field_ringtone=UINT(value,**{'default': 0x600})
    def __delfield_ringtone(self): del self.__field_ringtone
    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
    def __getfield_wallpaper(self):
        try: self.__field_wallpaper
        except:
            self.__field_wallpaper=UINT(**{'default': 0x100})
        return self.__field_wallpaper.getvalue()
    def __setfield_wallpaper(self, value):
        if isinstance(value,UINT):
            self.__field_wallpaper=value
        else:
            self.__field_wallpaper=UINT(value,**{'default': 0x100})
    def __delfield_wallpaper(self): del self.__field_wallpaper
    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('serial1', self.__field_serial1, None)
        yield ('entrysize', self.__field_entrysize, None)
        yield ('entrynumber', self.__field_entrynumber, None)
        yield ('unknown1', self.__field_unknown1, None)
        yield ('name', self.__field_name, None)
        yield ('group', self.__field_group, None)
        yield ('unknown2', self.__field_unknown2, None)
        yield ('secret', self.__field_secret, None)
        yield ('memo', self.__field_memo, None)
        yield ('emails', self.__field_emails, None)
        yield ('url', self.__field_url, None)
        yield ('numberspeeds', self.__field_numberspeeds, None)
        yield ('numbertypes', self.__field_numbertypes, None)
        yield ('numbers', self.__field_numbers, None)
        yield ('EndOfRecord', self.__field_EndOfRecord, None)
        yield ('ringtone', self.__field_ringtone, None)
        yield ('wallpaper', self.__field_wallpaper, None)
class _gen_p_lglg6200_128(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['email']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6200_128,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6200_128:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6200_128,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6200_128,kwargs)
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
class _gen_p_lglg6200_131(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['numberspeed']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6200_131,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6200_131:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6200_131,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6200_131,kwargs)
        if len(args):
            dict2={'sizeinbytes': 1}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_numberspeed=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numberspeed.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numberspeed=UINT(**{'sizeinbytes': 1})
        self.__field_numberspeed.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_numberspeed(self):
        return self.__field_numberspeed.getvalue()
    def __setfield_numberspeed(self, value):
        if isinstance(value,UINT):
            self.__field_numberspeed=value
        else:
            self.__field_numberspeed=UINT(value,**{'sizeinbytes': 1})
    def __delfield_numberspeed(self): del self.__field_numberspeed
    numberspeed=property(__getfield_numberspeed, __setfield_numberspeed, __delfield_numberspeed, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('numberspeed', self.__field_numberspeed, None)
class _gen_p_lglg6200_133(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['numbertype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6200_133,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6200_133:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6200_133,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6200_133,kwargs)
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
class _gen_p_lglg6200_135(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['number']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6200_135,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6200_135:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6200_135,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6200_135,kwargs)
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
class pbgroup(BaseProtogenClass):
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
class pbgroups(BaseProtogenClass):
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
class pb_contact_media_entry(BaseProtogenClass):
    """Reads the wallpaper/ringer info for each 
    contact on the phone"""
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
class pb_contact_media_file(BaseProtogenClass):
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
class indexentry(BaseProtogenClass):
    __fields=['index', 'const', 'name']
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
        self.__field_index.writetobuffer(buf)
        self.__field_const.writetobuffer(buf)
        try: self.__field_name
        except:
            self.__field_name=STRING(**{'sizeinbytes': 80, 'default': ""})
        self.__field_name.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_index=UINT(**{'sizeinbytes': 1})
        self.__field_index.readfrombuffer(buf)
        self.__field_const=UINT(**{'sizeinbytes': 1})
        self.__field_const.readfrombuffer(buf)
        self.__field_name=STRING(**{'sizeinbytes': 80, 'default': ""})
        self.__field_name.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_index(self):
        return self.__field_index.getvalue()
    def __setfield_index(self, value):
        if isinstance(value,UINT):
            self.__field_index=value
        else:
            self.__field_index=UINT(value,**{'sizeinbytes': 1})
    def __delfield_index(self): del self.__field_index
    index=property(__getfield_index, __setfield_index, __delfield_index, None)
    def __getfield_const(self):
        return self.__field_const.getvalue()
    def __setfield_const(self, value):
        if isinstance(value,UINT):
            self.__field_const=value
        else:
            self.__field_const=UINT(value,**{'sizeinbytes': 1})
    def __delfield_const(self): del self.__field_const
    const=property(__getfield_const, __setfield_const, __delfield_const, None)
    def __getfield_name(self):
        try: self.__field_name
        except:
            self.__field_name=STRING(**{'sizeinbytes': 80, 'default': ""})
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,STRING):
            self.__field_name=value
        else:
            self.__field_name=STRING(value,**{'sizeinbytes': 80, 'default': ""})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('index', self.__field_index, None)
        yield ('const', self.__field_const, None)
        yield ('name', self.__field_name, None)
class indexfile(BaseProtogenClass):
    "Used for tracking wallpaper and ringtones"
    __fields=['numactiveitems', 'items']
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
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numactiveitems.writetobuffer(buf)
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'elementclass': indexentry, 'createdefault': True})
        self.__field_items.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numactiveitems=UINT(**{'sizeinbytes': 2})
        self.__field_numactiveitems.readfrombuffer(buf)
        self.__field_items=LIST(**{'elementclass': indexentry, 'createdefault': True})
        self.__field_items.readfrombuffer(buf)
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
    def __getfield_items(self):
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'elementclass': indexentry, 'createdefault': True})
        return self.__field_items.getvalue()
    def __setfield_items(self, value):
        if isinstance(value,LIST):
            self.__field_items=value
        else:
            self.__field_items=LIST(value,**{'elementclass': indexentry, 'createdefault': True})
    def __delfield_items(self): del self.__field_items
    items=property(__getfield_items, __setfield_items, __delfield_items, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('numactiveitems', self.__field_numactiveitems, None)
        yield ('items', self.__field_items, None)
class content_entry(BaseProtogenClass):
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
                self.__field_unknown_int1=UINT(**{'sizeinbytes': 2, 'default':0x08AA})
            self.__field_unknown_int1.writetobuffer(buf)
            try: self.__field_unknown3
            except:
                self.__field_unknown3=STRING(**{'terminator': 0xA, 'default':''})
            self.__field_unknown3.writetobuffer(buf)
            try: self.__field_unknown_int2
            except:
                self.__field_unknown_int2=UINT(**{'sizeinbytes': 2, 'default':0x08AA})
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
            self.__field_unknown_int1=UINT(**{'sizeinbytes': 2, 'default':0x08AA})
            self.__field_unknown_int1.readfrombuffer(buf)
            self.__field_unknown3=STRING(**{'terminator': 0xA, 'default':''})
            self.__field_unknown3.readfrombuffer(buf)
            self.__field_unknown_int2=UINT(**{'sizeinbytes': 2, 'default':0x08AA})
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
            self.__field_unknown_int1=UINT(**{'sizeinbytes': 2, 'default':0x08AA})
        return self.__field_unknown_int1.getvalue()
    def __setfield_unknown_int1(self, value):
        if isinstance(value,UINT):
            self.__field_unknown_int1=value
        else:
            self.__field_unknown_int1=UINT(value,**{'sizeinbytes': 2, 'default':0x08AA})
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
            self.__field_unknown_int2=UINT(**{'sizeinbytes': 2, 'default':0x08AA})
        return self.__field_unknown_int2.getvalue()
    def __setfield_unknown_int2(self, value):
        if isinstance(value,UINT):
            self.__field_unknown_int2=value
        else:
            self.__field_unknown_int2=UINT(value,**{'sizeinbytes': 2, 'default':0x08AA})
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
class content_file(BaseProtogenClass):
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
class content_count(BaseProtogenClass):
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
class textmemo(BaseProtogenClass):
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
class textmemofile(BaseProtogenClass):
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
