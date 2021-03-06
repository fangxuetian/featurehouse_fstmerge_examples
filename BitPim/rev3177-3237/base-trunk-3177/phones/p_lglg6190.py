"""Various descriptions of data specific to LG 6190 (Sprint)"""
import re
from prototypes import *
from prototypeslg import *
from p_lg import *
from p_lgvx4400 import *
UINT=UINTlsb
BOOL=BOOLlsb
NUMSPEEDDIALS=100
FIRSTSPEEDDIAL=1
LASTSPEEDDIAL=99
NUMPHONEBOOKENTRIES=500
MEMOLENGTH=65
NORINGTONE=0
NOMSGRINGTONE=0
NOWALLPAPER=0
NUMEMAILS=3
NUMPHONENUMBERS=5
SMS_CANNED_MAX_ITEMS=18
SMS_CANNED_MAX_LENGTH=101
SMS_CANNED_FILENAME="sms/mediacan000.dat"
SMS_PATTERNS={'Inbox': re.compile(r"^.*/inbox[0-9][0-9][0-9]\.dat$"),
             'Sent': re.compile(r"^.*/outbox[0-9][0-9][0-9]\.dat$"),
             'Saved': re.compile(r"^.*/sf[0-9][0-9]\.dat$"),
             }
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
CAL_NO_VOICE=0xffff
CAL_REPEAT_DATE=(2999, 12, 31)
cal_has_voice_id=True
cal_voice_id_ofs=0x11
cal_voice_ext='.qcp'      # full name='sche000.qcp'
cal_dir='sch'
cal_data_file_name='sch/schedule.dat'
cal_exception_file_name='sch/schexception.dat'
PHONE_ENCODING='iso-8859-1'
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=pbheader(**{'command': 0x04, 'flag': 0x01})
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=pbheader(**{'command': 0x03, 'flag': 0x01})
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class speeddial(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_entry
        except:
            self.__field_entry=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_entry.writetobuffer(buf)
        try: self.__field_number
        except:
            self.__field_number=UINT(**{'sizeinbytes': 1, 'default': 0xff})
        self.__field_number.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_entry=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_entry.readfrombuffer(buf)
        self.__field_number=UINT(**{'sizeinbytes': 1, 'default': 0xff})
        self.__field_number.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_entry(self):
        try: self.__field_entry
        except:
            self.__field_entry=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,UINT):
            self.__field_entry=value
        else:
            self.__field_entry=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
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
class speeddials(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_speeddials
        except:
            self.__field_speeddials=LIST(**{'length': NUMSPEEDDIALS, 'elementclass': speeddial})
        self.__field_speeddials.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class pbentry(BaseProtogenClass):
    __fields=['serial1', 'entrysize', 'serial2', 'entrynumber', 'name', 'group', 'emails', 'url', 'ringtone', 'msgringtone', 'secret', 'memo', 'wallpaper', 'numbertypes', 'numbers', 'unknown20c']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_serial1.writetobuffer(buf)
        try: self.__field_entrysize
        except:
            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x0222})
        self.__field_entrysize.writetobuffer(buf)
        self.__field_serial2.writetobuffer(buf)
        self.__field_entrynumber.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        self.__field_group.writetobuffer(buf)
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'elementclass': _gen_p_lglg6190_137, 'length': NUMEMAILS})
        self.__field_emails.writetobuffer(buf)
        self.__field_url.writetobuffer(buf)
        self.__field_ringtone.writetobuffer(buf)
        self.__field_msgringtone.writetobuffer(buf)
        self.__field_secret.writetobuffer(buf)
        self.__field_memo.writetobuffer(buf)
        self.__field_wallpaper.writetobuffer(buf)
        try: self.__field_numbertypes
        except:
            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lglg6190_145, 'length': NUMPHONENUMBERS})
        self.__field_numbertypes.writetobuffer(buf)
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'elementclass': _gen_p_lglg6190_147, 'length': NUMPHONENUMBERS})
        self.__field_numbers.writetobuffer(buf)
        try: self.__field_unknown20c
        except:
            self.__field_unknown20c=UNKNOWN()
        self.__field_unknown20c.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_serial1=UINT(**{'sizeinbytes': 4})
        self.__field_serial1.readfrombuffer(buf)
        self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x0222})
        self.__field_entrysize.readfrombuffer(buf)
        self.__field_serial2=UINT(**{'sizeinbytes': 4})
        self.__field_serial2.readfrombuffer(buf)
        self.__field_entrynumber=UINT(**{'sizeinbytes': 2})
        self.__field_entrynumber.readfrombuffer(buf)
        self.__field_name=USTRING(**{'sizeinbytes': 23, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
        self.__field_name.readfrombuffer(buf)
        self.__field_group=UINT(**{'sizeinbytes': 2})
        self.__field_group.readfrombuffer(buf)
        self.__field_emails=LIST(**{'elementclass': _gen_p_lglg6190_137, 'length': NUMEMAILS})
        self.__field_emails.readfrombuffer(buf)
        self.__field_url=USTRING(**{'sizeinbytes': 49, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
        self.__field_url.readfrombuffer(buf)
        self.__field_ringtone=UINT(**{'sizeinbytes': 1})
        self.__field_ringtone.readfrombuffer(buf)
        self.__field_msgringtone=UINT(**{'sizeinbytes': 1})
        self.__field_msgringtone.readfrombuffer(buf)
        self.__field_secret=BOOL(**{'sizeinbytes': 1})
        self.__field_secret.readfrombuffer(buf)
        self.__field_memo=USTRING(**{'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False, 'sizeinbytes': MEMOLENGTH})
        self.__field_memo.readfrombuffer(buf)
        self.__field_wallpaper=UINT(**{'sizeinbytes': 1})
        self.__field_wallpaper.readfrombuffer(buf)
        self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lglg6190_145, 'length': NUMPHONENUMBERS})
        self.__field_numbertypes.readfrombuffer(buf)
        self.__field_numbers=LIST(**{'elementclass': _gen_p_lglg6190_147, 'length': NUMPHONENUMBERS})
        self.__field_numbers.readfrombuffer(buf)
        self.__field_unknown20c=UNKNOWN()
        self.__field_unknown20c.readfrombuffer(buf)
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
            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x0222})
        return self.__field_entrysize.getvalue()
    def __setfield_entrysize(self, value):
        if isinstance(value,UINT):
            self.__field_entrysize=value
        else:
            self.__field_entrysize=UINT(value,**{'sizeinbytes': 2, 'constant': 0x0222})
    def __delfield_entrysize(self): del self.__field_entrysize
    entrysize=property(__getfield_entrysize, __setfield_entrysize, __delfield_entrysize, None)
    def __getfield_serial2(self):
        return self.__field_serial2.getvalue()
    def __setfield_serial2(self, value):
        if isinstance(value,UINT):
            self.__field_serial2=value
        else:
            self.__field_serial2=UINT(value,**{'sizeinbytes': 4})
    def __delfield_serial2(self): del self.__field_serial2
    serial2=property(__getfield_serial2, __setfield_serial2, __delfield_serial2, None)
    def __getfield_entrynumber(self):
        return self.__field_entrynumber.getvalue()
    def __setfield_entrynumber(self, value):
        if isinstance(value,UINT):
            self.__field_entrynumber=value
        else:
            self.__field_entrynumber=UINT(value,**{'sizeinbytes': 2})
    def __delfield_entrynumber(self): del self.__field_entrynumber
    entrynumber=property(__getfield_entrynumber, __setfield_entrynumber, __delfield_entrynumber, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,USTRING):
            self.__field_name=value
        else:
            self.__field_name=USTRING(value,**{'sizeinbytes': 23, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
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
    def __getfield_emails(self):
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'elementclass': _gen_p_lglg6190_137, 'length': NUMEMAILS})
        return self.__field_emails.getvalue()
    def __setfield_emails(self, value):
        if isinstance(value,LIST):
            self.__field_emails=value
        else:
            self.__field_emails=LIST(value,**{'elementclass': _gen_p_lglg6190_137, 'length': NUMEMAILS})
    def __delfield_emails(self): del self.__field_emails
    emails=property(__getfield_emails, __setfield_emails, __delfield_emails, None)
    def __getfield_url(self):
        return self.__field_url.getvalue()
    def __setfield_url(self, value):
        if isinstance(value,USTRING):
            self.__field_url=value
        else:
            self.__field_url=USTRING(value,**{'sizeinbytes': 49, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
    def __delfield_url(self): del self.__field_url
    url=property(__getfield_url, __setfield_url, __delfield_url, None)
    def __getfield_ringtone(self):
        return self.__field_ringtone.getvalue()
    def __setfield_ringtone(self, value):
        if isinstance(value,UINT):
            self.__field_ringtone=value
        else:
            self.__field_ringtone=UINT(value,**{'sizeinbytes': 1})
    def __delfield_ringtone(self): del self.__field_ringtone
    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, "ringtone index for a call")
    def __getfield_msgringtone(self):
        return self.__field_msgringtone.getvalue()
    def __setfield_msgringtone(self, value):
        if isinstance(value,UINT):
            self.__field_msgringtone=value
        else:
            self.__field_msgringtone=UINT(value,**{'sizeinbytes': 1})
    def __delfield_msgringtone(self): del self.__field_msgringtone
    msgringtone=property(__getfield_msgringtone, __setfield_msgringtone, __delfield_msgringtone, "ringtone index for a text message")
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
        if isinstance(value,USTRING):
            self.__field_memo=value
        else:
            self.__field_memo=USTRING(value,**{'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False, 'sizeinbytes': MEMOLENGTH})
    def __delfield_memo(self): del self.__field_memo
    memo=property(__getfield_memo, __setfield_memo, __delfield_memo, None)
    def __getfield_wallpaper(self):
        return self.__field_wallpaper.getvalue()
    def __setfield_wallpaper(self, value):
        if isinstance(value,UINT):
            self.__field_wallpaper=value
        else:
            self.__field_wallpaper=UINT(value,**{'sizeinbytes': 1})
    def __delfield_wallpaper(self): del self.__field_wallpaper
    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
    def __getfield_numbertypes(self):
        try: self.__field_numbertypes
        except:
            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lglg6190_145, 'length': NUMPHONENUMBERS})
        return self.__field_numbertypes.getvalue()
    def __setfield_numbertypes(self, value):
        if isinstance(value,LIST):
            self.__field_numbertypes=value
        else:
            self.__field_numbertypes=LIST(value,**{'elementclass': _gen_p_lglg6190_145, 'length': NUMPHONENUMBERS})
    def __delfield_numbertypes(self): del self.__field_numbertypes
    numbertypes=property(__getfield_numbertypes, __setfield_numbertypes, __delfield_numbertypes, None)
    def __getfield_numbers(self):
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'elementclass': _gen_p_lglg6190_147, 'length': NUMPHONENUMBERS})
        return self.__field_numbers.getvalue()
    def __setfield_numbers(self, value):
        if isinstance(value,LIST):
            self.__field_numbers=value
        else:
            self.__field_numbers=LIST(value,**{'elementclass': _gen_p_lglg6190_147, 'length': NUMPHONENUMBERS})
    def __delfield_numbers(self): del self.__field_numbers
    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
    def __getfield_unknown20c(self):
        try: self.__field_unknown20c
        except:
            self.__field_unknown20c=UNKNOWN()
        return self.__field_unknown20c.getvalue()
    def __setfield_unknown20c(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_unknown20c=value
        else:
            self.__field_unknown20c=UNKNOWN(value,)
    def __delfield_unknown20c(self): del self.__field_unknown20c
    unknown20c=property(__getfield_unknown20c, __setfield_unknown20c, __delfield_unknown20c, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('serial1', self.__field_serial1, None)
        yield ('entrysize', self.__field_entrysize, None)
        yield ('serial2', self.__field_serial2, None)
        yield ('entrynumber', self.__field_entrynumber, None)
        yield ('name', self.__field_name, None)
        yield ('group', self.__field_group, None)
        yield ('emails', self.__field_emails, None)
        yield ('url', self.__field_url, None)
        yield ('ringtone', self.__field_ringtone, "ringtone index for a call")
        yield ('msgringtone', self.__field_msgringtone, "ringtone index for a text message")
        yield ('secret', self.__field_secret, None)
        yield ('memo', self.__field_memo, None)
        yield ('wallpaper', self.__field_wallpaper, None)
        yield ('numbertypes', self.__field_numbertypes, None)
        yield ('numbers', self.__field_numbers, None)
        yield ('unknown20c', self.__field_unknown20c, None)
class _gen_p_lglg6190_137(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['email']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6190_137,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6190_137:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6190_137,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6190_137,kwargs)
        if len(args):
            dict2={'sizeinbytes': 49, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_email=USTRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_email.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_email=USTRING(**{'sizeinbytes': 49, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
        self.__field_email.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_email(self):
        return self.__field_email.getvalue()
    def __setfield_email(self, value):
        if isinstance(value,USTRING):
            self.__field_email=value
        else:
            self.__field_email=USTRING(value,**{'sizeinbytes': 49, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
    def __delfield_email(self): del self.__field_email
    email=property(__getfield_email, __setfield_email, __delfield_email, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('email', self.__field_email, None)
class _gen_p_lglg6190_145(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['numbertype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6190_145,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6190_145:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6190_145,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6190_145,kwargs)
        if len(args):
            dict2={'sizeinbytes': 1}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_numbertype=UINT(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numbertype.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class _gen_p_lglg6190_147(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['number']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6190_147,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6190_147:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6190_147,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6190_147,kwargs)
        if len(args):
            dict2={'sizeinbytes': 49, 'raiseonunterminatedread': False}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_number=USTRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_number.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_number=USTRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})
        self.__field_number.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_number(self):
        return self.__field_number.getvalue()
    def __setfield_number(self, value):
        if isinstance(value,USTRING):
            self.__field_number=value
        else:
            self.__field_number=USTRING(value,**{'sizeinbytes': 49, 'raiseonunterminatedread': False})
    def __delfield_number(self): del self.__field_number
    number=property(__getfield_number, __setfield_number, __delfield_number, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('number', self.__field_number, None)
class pbgroup(BaseProtogenClass):
    "A single group"
    __fields=['icon', 'name']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_icon.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_icon=UINT(**{'sizeinbytes': 1})
        self.__field_icon.readfrombuffer(buf)
        self.__field_name=USTRING(**{'sizeinbytes': 23, 'encoding': PHONE_ENCODING})
        self.__field_name.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_icon(self):
        return self.__field_icon.getvalue()
    def __setfield_icon(self, value):
        if isinstance(value,UINT):
            self.__field_icon=value
        else:
            self.__field_icon=UINT(value,**{'sizeinbytes': 1})
    def __delfield_icon(self): del self.__field_icon
    icon=property(__getfield_icon, __setfield_icon, __delfield_icon, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,USTRING):
            self.__field_name=value
        else:
            self.__field_name=USTRING(value,**{'sizeinbytes': 23, 'encoding': PHONE_ENCODING})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('icon', self.__field_icon, None)
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_groups
        except:
            self.__field_groups=LIST(**{'elementclass': pbgroup})
        self.__field_groups.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class call(BaseProtogenClass):
    __fields=['GPStime', 'unknown1', 'duration', 'number', 'name', 'numberlength', 'unknown2', 'pbnumbertype', 'unknown3', 'pbentrynum']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
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
        self.__field_pbentrynum.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})
        self.__field_GPStime.readfrombuffer(buf)
        self.__field_unknown1=UINT(**{'sizeinbytes': 4})
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_duration=UINT(**{'sizeinbytes': 4})
        self.__field_duration.readfrombuffer(buf)
        self.__field_number=USTRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})
        self.__field_number.readfrombuffer(buf)
        self.__field_name=USTRING(**{'sizeinbytes': 36, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
        self.__field_name.readfrombuffer(buf)
        self.__field_numberlength=UINT(**{'sizeinbytes': 1})
        self.__field_numberlength.readfrombuffer(buf)
        self.__field_unknown2=UINT(**{'sizeinbytes': 1})
        self.__field_unknown2.readfrombuffer(buf)
        self.__field_pbnumbertype=UINT(**{'sizeinbytes': 1})
        self.__field_pbnumbertype.readfrombuffer(buf)
        self.__field_unknown3=UINT(**{'sizeinbytes': 2})
        self.__field_unknown3.readfrombuffer(buf)
        self.__field_pbentrynum=UINT(**{'sizeinbytes': 2})
        self.__field_pbentrynum.readfrombuffer(buf)
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
        if isinstance(value,USTRING):
            self.__field_number=value
        else:
            self.__field_number=USTRING(value,**{'sizeinbytes': 49, 'raiseonunterminatedread': False})
    def __delfield_number(self): del self.__field_number
    number=property(__getfield_number, __setfield_number, __delfield_number, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,USTRING):
            self.__field_name=value
        else:
            self.__field_name=USTRING(value,**{'sizeinbytes': 36, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
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
            self.__field_unknown3=UINT(value,**{'sizeinbytes': 2})
    def __delfield_unknown3(self): del self.__field_unknown3
    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
    def __getfield_pbentrynum(self):
        return self.__field_pbentrynum.getvalue()
    def __setfield_pbentrynum(self, value):
        if isinstance(value,UINT):
            self.__field_pbentrynum=value
        else:
            self.__field_pbentrynum=UINT(value,**{'sizeinbytes': 2})
    def __delfield_pbentrynum(self): del self.__field_pbentrynum
    pbentrynum=property(__getfield_pbentrynum, __setfield_pbentrynum, __delfield_pbentrynum, None)
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
        yield ('pbentrynum', self.__field_pbentrynum, None)
class callhistory(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numcalls.writetobuffer(buf)
        self.__field_unknown1.writetobuffer(buf)
        try: self.__field_calls
        except:
            self.__field_calls=LIST(**{'elementclass': call})
        self.__field_calls.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class indexentry(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_index
        except:
            self.__field_index=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_index.writetobuffer(buf)
        try: self.__field_name
        except:
            self.__field_name=USTRING(**{'sizeinbytes': 50, 'default': ""})
        self.__field_name.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_index=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_index.readfrombuffer(buf)
        self.__field_name=USTRING(**{'sizeinbytes': 50, 'default': ""})
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
            self.__field_name=USTRING(**{'sizeinbytes': 50, 'default': ""})
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,USTRING):
            self.__field_name=value
        else:
            self.__field_name=USTRING(value,**{'sizeinbytes': 50, 'default': ""})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('index', self.__field_index, None)
        yield ('name', self.__field_name, None)
class indexfile(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numactiveitems.writetobuffer(buf)
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': indexentry, 'createdefault': True})
        self.__field_items.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class camindexentry(BaseProtogenClass):
    __fields=['index', 'unknown1', 'name', 'taken', 'unkown2']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(camindexentry,self).__init__(**dict)
        if self.__class__ is camindexentry:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(camindexentry,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(camindexentry,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_index.writetobuffer(buf)
        try: self.__field_unknown1
        except:
            self.__field_unknown1=UINT(**{'sizeinbytes': 1, 'default' : 80})
        self.__field_unknown1.writetobuffer(buf)
        try: self.__field_name
        except:
            self.__field_name=USTRING(**{'sizeinbytes': 10, 'default': ""})
        self.__field_name.writetobuffer(buf)
        self.__field_taken.writetobuffer(buf)
        self.__field_unkown2.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_index=UINT(**{'sizeinbytes': 1})
        self.__field_index.readfrombuffer(buf)
        self.__field_unknown1=UINT(**{'sizeinbytes': 1, 'default' : 80})
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_name=USTRING(**{'sizeinbytes': 10, 'default': ""})
        self.__field_name.readfrombuffer(buf)
        self.__field_taken=LGCALDATE(**{'sizeinbytes': 4})
        self.__field_taken.readfrombuffer(buf)
        self.__field_unkown2=UINT(**{'sizeinbytes': 4})
        self.__field_unkown2.readfrombuffer(buf)
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
    def __getfield_unknown1(self):
        try: self.__field_unknown1
        except:
            self.__field_unknown1=UINT(**{'sizeinbytes': 1, 'default' : 80})
        return self.__field_unknown1.getvalue()
    def __setfield_unknown1(self, value):
        if isinstance(value,UINT):
            self.__field_unknown1=value
        else:
            self.__field_unknown1=UINT(value,**{'sizeinbytes': 1, 'default' : 80})
    def __delfield_unknown1(self): del self.__field_unknown1
    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
    def __getfield_name(self):
        try: self.__field_name
        except:
            self.__field_name=USTRING(**{'sizeinbytes': 10, 'default': ""})
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,USTRING):
            self.__field_name=value
        else:
            self.__field_name=USTRING(value,**{'sizeinbytes': 10, 'default': ""})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_taken(self):
        return self.__field_taken.getvalue()
    def __setfield_taken(self, value):
        if isinstance(value,LGCALDATE):
            self.__field_taken=value
        else:
            self.__field_taken=LGCALDATE(value,**{'sizeinbytes': 4})
    def __delfield_taken(self): del self.__field_taken
    taken=property(__getfield_taken, __setfield_taken, __delfield_taken, None)
    def __getfield_unkown2(self):
        return self.__field_unkown2.getvalue()
    def __setfield_unkown2(self, value):
        if isinstance(value,UINT):
            self.__field_unkown2=value
        else:
            self.__field_unkown2=UINT(value,**{'sizeinbytes': 4})
    def __delfield_unkown2(self): del self.__field_unkown2
    unkown2=property(__getfield_unkown2, __setfield_unkown2, __delfield_unkown2, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('index', self.__field_index, None)
        yield ('unknown1', self.__field_unknown1, None)
        yield ('name', self.__field_name, None)
        yield ('taken', self.__field_taken, None)
        yield ('unkown2', self.__field_unkown2, None)
class camindexfile(BaseProtogenClass):
    "Used for tracking wallpaper and ringtones"
    __fields=['maxitems', 'items']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(camindexfile,self).__init__(**dict)
        if self.__class__ is camindexfile:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(camindexfile,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(camindexfile,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
        if getattr(self, '__field_maxitems', None) is None:
            self.__field_maxitems=UINT(**{'constant': 60})
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': camindexentry, 'createdefault': True})
        self.__field_items.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': camindexentry, 'createdefault': True})
        self.__field_items.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_maxitems(self):
        return self.__field_maxitems.getvalue()
    def __setfield_maxitems(self, value):
        if isinstance(value,UINT):
            self.__field_maxitems=value
        else:
            self.__field_maxitems=UINT(value,**{'constant': 60})
    def __delfield_maxitems(self): del self.__field_maxitems
    maxitems=property(__getfield_maxitems, __setfield_maxitems, __delfield_maxitems, None)
    def __getfield_items(self):
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'length': self.maxitems, 'elementclass': camindexentry, 'createdefault': True})
        return self.__field_items.getvalue()
    def __setfield_items(self, value):
        if isinstance(value,LIST):
            self.__field_items=value
        else:
            self.__field_items=LIST(value,**{'length': self.maxitems, 'elementclass': camindexentry, 'createdefault': True})
    def __delfield_items(self): del self.__field_items
    items=property(__getfield_items, __setfield_items, __delfield_items, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('maxitems', self.__field_maxitems, None)
        yield ('items', self.__field_items, None)
class mediadesc(BaseProtogenClass):
    __fields=['totalsize', 'dunno1', 'magic1', 'magic2', 'magic3', 'dunno2', 'filename', 'whoknows', 'mimetype', 'whoknows2']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(mediadesc,self).__init__(**dict)
        if self.__class__ is mediadesc:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(mediadesc,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(mediadesc,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_totalsize.writetobuffer(buf)
        try: self.__field_dunno1
        except:
            self.__field_dunno1=UINT(**{'sizeinbytes': 4, 'constant': 0})
        self.__field_dunno1.writetobuffer(buf)
        try: self.__field_magic1
        except:
            self.__field_magic1=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        self.__field_magic1.writetobuffer(buf)
        try: self.__field_magic2
        except:
            self.__field_magic2=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        self.__field_magic2.writetobuffer(buf)
        try: self.__field_magic3
        except:
            self.__field_magic3=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        self.__field_magic3.writetobuffer(buf)
        try: self.__field_dunno2
        except:
            self.__field_dunno2=UINT(**{'sizeinbytes': 4, 'constant': 0})
        self.__field_dunno2.writetobuffer(buf)
        try: self.__field_filename
        except:
            self.__field_filename=USTRING(**{'sizeinbytes': 32, 'default': 'body'})
        self.__field_filename.writetobuffer(buf)
        try: self.__field_whoknows
        except:
            self.__field_whoknows=USTRING(**{'sizeinbytes': 32, 'default': 'identity'})
        self.__field_whoknows.writetobuffer(buf)
        self.__field_mimetype.writetobuffer(buf)
        try: self.__field_whoknows2
        except:
            self.__field_whoknows2=USTRING(**{'sizeinbytes': 32, 'default': ""})
        self.__field_whoknows2.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_totalsize=UINT(**{'sizeinbytes': 4})
        self.__field_totalsize.readfrombuffer(buf)
        self.__field_dunno1=UINT(**{'sizeinbytes': 4, 'constant': 0})
        self.__field_dunno1.readfrombuffer(buf)
        self.__field_magic1=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        self.__field_magic1.readfrombuffer(buf)
        self.__field_magic2=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        self.__field_magic2.readfrombuffer(buf)
        self.__field_magic3=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        self.__field_magic3.readfrombuffer(buf)
        self.__field_dunno2=UINT(**{'sizeinbytes': 4, 'constant': 0})
        self.__field_dunno2.readfrombuffer(buf)
        self.__field_filename=USTRING(**{'sizeinbytes': 32, 'default': 'body'})
        self.__field_filename.readfrombuffer(buf)
        self.__field_whoknows=USTRING(**{'sizeinbytes': 32, 'default': 'identity'})
        self.__field_whoknows.readfrombuffer(buf)
        self.__field_mimetype=USTRING(**{'sizeinbytes': 32})
        self.__field_mimetype.readfrombuffer(buf)
        self.__field_whoknows2=USTRING(**{'sizeinbytes': 32, 'default': ""})
        self.__field_whoknows2.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_totalsize(self):
        return self.__field_totalsize.getvalue()
    def __setfield_totalsize(self, value):
        if isinstance(value,UINT):
            self.__field_totalsize=value
        else:
            self.__field_totalsize=UINT(value,**{'sizeinbytes': 4})
    def __delfield_totalsize(self): del self.__field_totalsize
    totalsize=property(__getfield_totalsize, __setfield_totalsize, __delfield_totalsize, "media file size with size of this file (152 bytes) added")
    def __getfield_dunno1(self):
        try: self.__field_dunno1
        except:
            self.__field_dunno1=UINT(**{'sizeinbytes': 4, 'constant': 0})
        return self.__field_dunno1.getvalue()
    def __setfield_dunno1(self, value):
        if isinstance(value,UINT):
            self.__field_dunno1=value
        else:
            self.__field_dunno1=UINT(value,**{'sizeinbytes': 4, 'constant': 0})
    def __delfield_dunno1(self): del self.__field_dunno1
    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
    def __getfield_magic1(self):
        try: self.__field_magic1
        except:
            self.__field_magic1=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        return self.__field_magic1.getvalue()
    def __setfield_magic1(self, value):
        if isinstance(value,UINT):
            self.__field_magic1=value
        else:
            self.__field_magic1=UINT(value,**{'sizeinbytes': 4, 'default': 0x7824c97a})
    def __delfield_magic1(self): del self.__field_magic1
    magic1=property(__getfield_magic1, __setfield_magic1, __delfield_magic1, "probably the file date (created)")
    def __getfield_magic2(self):
        try: self.__field_magic2
        except:
            self.__field_magic2=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        return self.__field_magic2.getvalue()
    def __setfield_magic2(self, value):
        if isinstance(value,UINT):
            self.__field_magic2=value
        else:
            self.__field_magic2=UINT(value,**{'sizeinbytes': 4, 'default': 0x7824c97a})
    def __delfield_magic2(self): del self.__field_magic2
    magic2=property(__getfield_magic2, __setfield_magic2, __delfield_magic2, "probably the file date (accessed)")
    def __getfield_magic3(self):
        try: self.__field_magic3
        except:
            self.__field_magic3=UINT(**{'sizeinbytes': 4, 'default': 0x7824c97a})
        return self.__field_magic3.getvalue()
    def __setfield_magic3(self, value):
        if isinstance(value,UINT):
            self.__field_magic3=value
        else:
            self.__field_magic3=UINT(value,**{'sizeinbytes': 4, 'default': 0x7824c97a})
    def __delfield_magic3(self): del self.__field_magic3
    magic3=property(__getfield_magic3, __setfield_magic3, __delfield_magic3, "probably the file date (modified)")
    def __getfield_dunno2(self):
        try: self.__field_dunno2
        except:
            self.__field_dunno2=UINT(**{'sizeinbytes': 4, 'constant': 0})
        return self.__field_dunno2.getvalue()
    def __setfield_dunno2(self, value):
        if isinstance(value,UINT):
            self.__field_dunno2=value
        else:
            self.__field_dunno2=UINT(value,**{'sizeinbytes': 4, 'constant': 0})
    def __delfield_dunno2(self): del self.__field_dunno2
    dunno2=property(__getfield_dunno2, __setfield_dunno2, __delfield_dunno2, None)
    def __getfield_filename(self):
        try: self.__field_filename
        except:
            self.__field_filename=USTRING(**{'sizeinbytes': 32, 'default': 'body'})
        return self.__field_filename.getvalue()
    def __setfield_filename(self, value):
        if isinstance(value,USTRING):
            self.__field_filename=value
        else:
            self.__field_filename=USTRING(value,**{'sizeinbytes': 32, 'default': 'body'})
    def __delfield_filename(self): del self.__field_filename
    filename=property(__getfield_filename, __setfield_filename, __delfield_filename, None)
    def __getfield_whoknows(self):
        try: self.__field_whoknows
        except:
            self.__field_whoknows=USTRING(**{'sizeinbytes': 32, 'default': 'identity'})
        return self.__field_whoknows.getvalue()
    def __setfield_whoknows(self, value):
        if isinstance(value,USTRING):
            self.__field_whoknows=value
        else:
            self.__field_whoknows=USTRING(value,**{'sizeinbytes': 32, 'default': 'identity'})
    def __delfield_whoknows(self): del self.__field_whoknows
    whoknows=property(__getfield_whoknows, __setfield_whoknows, __delfield_whoknows, "set to iso-8859-1 in some cases??")
    def __getfield_mimetype(self):
        return self.__field_mimetype.getvalue()
    def __setfield_mimetype(self, value):
        if isinstance(value,USTRING):
            self.__field_mimetype=value
        else:
            self.__field_mimetype=USTRING(value,**{'sizeinbytes': 32})
    def __delfield_mimetype(self): del self.__field_mimetype
    mimetype=property(__getfield_mimetype, __setfield_mimetype, __delfield_mimetype, None)
    def __getfield_whoknows2(self):
        try: self.__field_whoknows2
        except:
            self.__field_whoknows2=USTRING(**{'sizeinbytes': 32, 'default': ""})
        return self.__field_whoknows2.getvalue()
    def __setfield_whoknows2(self, value):
        if isinstance(value,USTRING):
            self.__field_whoknows2=value
        else:
            self.__field_whoknows2=USTRING(value,**{'sizeinbytes': 32, 'default': ""})
    def __delfield_whoknows2(self): del self.__field_whoknows2
    whoknows2=property(__getfield_whoknows2, __setfield_whoknows2, __delfield_whoknows2, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('totalsize', self.__field_totalsize, "media file size with size of this file (152 bytes) added")
        yield ('dunno1', self.__field_dunno1, None)
        yield ('magic1', self.__field_magic1, "probably the file date (created)")
        yield ('magic2', self.__field_magic2, "probably the file date (accessed)")
        yield ('magic3', self.__field_magic3, "probably the file date (modified)")
        yield ('dunno2', self.__field_dunno2, None)
        yield ('filename', self.__field_filename, None)
        yield ('whoknows', self.__field_whoknows, "set to iso-8859-1 in some cases??")
        yield ('mimetype', self.__field_mimetype, None)
        yield ('whoknows2', self.__field_whoknows2, None)
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
            self.__field_text=USTRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_text.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_text=USTRING(**{'sizeinbytes': 151,  'raiseonunterminatedread': False, 'raiseontruncate': False })
        self.__field_text.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_text(self):
        return self.__field_text.getvalue()
    def __setfield_text(self, value):
        if isinstance(value,USTRING):
            self.__field_text=value
        else:
            self.__field_text=USTRING(value,**{'sizeinbytes': 151,  'raiseonunterminatedread': False, 'raiseontruncate': False })
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_itemcount.writetobuffer(buf)
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'elementclass': textmemo })
        self.__field_items.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_itemcount=UINT(**{'sizeinbytes': 4})
        self.__field_itemcount.readfrombuffer(buf)
        self.__field_items=LIST(**{'elementclass': textmemo })
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
            self.__field_items=LIST(**{'elementclass': textmemo })
        return self.__field_items.getvalue()
    def __setfield_items(self, value):
        if isinstance(value,LIST):
            self.__field_items=value
        else:
            self.__field_items=LIST(value,**{'elementclass': textmemo })
    def __delfield_items(self): del self.__field_items
    items=property(__getfield_items, __setfield_items, __delfield_items, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('itemcount', self.__field_itemcount, None)
        yield ('items', self.__field_items, None)
class scheduleexception(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pos.writetobuffer(buf)
        self.__field_day.writetobuffer(buf)
        self.__field_month.writetobuffer(buf)
        self.__field_year.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class scheduleexceptionfile(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'elementclass': scheduleexception})
        self.__field_items.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class scheduleevent(BaseProtogenClass):
    __fields=['packet_size', 'pos', 'start', 'end', 'repeat', 'daybitmap', 'pad2', 'alarmminutes', 'alarmhours', 'alarmtype', 'snoozedelay', 'ringtone', 'description', 'unknown1', 'hasvoice', 'voiceid', 'unknown2']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
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
        self.__field_description.writetobuffer(buf)
        try: self.__field_unknown1
        except:
            self.__field_unknown1=UINT(**{'sizeinbytes': 2,  'default': 0 })
        self.__field_unknown1.writetobuffer(buf)
        self.__field_hasvoice.writetobuffer(buf)
        self.__field_voiceid.writetobuffer(buf)
        try: self.__field_unknown2
        except:
            self.__field_unknown2=UINT(**{'sizeinbytes': 2,  'default': 0 })
        self.__field_unknown2.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
        self.__field_description=USTRING(**{'sizeinbytes': 35, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False, 'raiseontruncate': False })
        self.__field_description.readfrombuffer(buf)
        self.__field_unknown1=UINT(**{'sizeinbytes': 2,  'default': 0 })
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_hasvoice=UINT(**{'sizeinbytes': 2})
        self.__field_hasvoice.readfrombuffer(buf)
        self.__field_voiceid=UINT(**{'sizeinbytes': 2})
        self.__field_voiceid.readfrombuffer(buf)
        self.__field_unknown2=UINT(**{'sizeinbytes': 2,  'default': 0 })
        self.__field_unknown2.readfrombuffer(buf)
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
    def __getfield_description(self):
        return self.__field_description.getvalue()
    def __setfield_description(self, value):
        if isinstance(value,USTRING):
            self.__field_description=value
        else:
            self.__field_description=USTRING(value,**{'sizeinbytes': 35, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False, 'raiseontruncate': False })
    def __delfield_description(self): del self.__field_description
    description=property(__getfield_description, __setfield_description, __delfield_description, None)
    def __getfield_unknown1(self):
        try: self.__field_unknown1
        except:
            self.__field_unknown1=UINT(**{'sizeinbytes': 2,  'default': 0 })
        return self.__field_unknown1.getvalue()
    def __setfield_unknown1(self, value):
        if isinstance(value,UINT):
            self.__field_unknown1=value
        else:
            self.__field_unknown1=UINT(value,**{'sizeinbytes': 2,  'default': 0 })
    def __delfield_unknown1(self): del self.__field_unknown1
    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, "This seems to always be two zeros")
    def __getfield_hasvoice(self):
        return self.__field_hasvoice.getvalue()
    def __setfield_hasvoice(self, value):
        if isinstance(value,UINT):
            self.__field_hasvoice=value
        else:
            self.__field_hasvoice=UINT(value,**{'sizeinbytes': 2})
    def __delfield_hasvoice(self): del self.__field_hasvoice
    hasvoice=property(__getfield_hasvoice, __setfield_hasvoice, __delfield_hasvoice, "This event has an associated voice memo if 1")
    def __getfield_voiceid(self):
        return self.__field_voiceid.getvalue()
    def __setfield_voiceid(self, value):
        if isinstance(value,UINT):
            self.__field_voiceid=value
        else:
            self.__field_voiceid=UINT(value,**{'sizeinbytes': 2})
    def __delfield_voiceid(self): del self.__field_voiceid
    voiceid=property(__getfield_voiceid, __setfield_voiceid, __delfield_voiceid, "sch/schexxx.qcp is the voice memo (xxx = voiceid - 0x0f)")
    def __getfield_unknown2(self):
        try: self.__field_unknown2
        except:
            self.__field_unknown2=UINT(**{'sizeinbytes': 2,  'default': 0 })
        return self.__field_unknown2.getvalue()
    def __setfield_unknown2(self, value):
        if isinstance(value,UINT):
            self.__field_unknown2=value
        else:
            self.__field_unknown2=UINT(value,**{'sizeinbytes': 2,  'default': 0 })
    def __delfield_unknown2(self): del self.__field_unknown2
    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, "This seems to always be yet two more zeros")
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
        yield ('description', self.__field_description, None)
        yield ('unknown1', self.__field_unknown1, "This seems to always be two zeros")
        yield ('hasvoice', self.__field_hasvoice, "This event has an associated voice memo if 1")
        yield ('voiceid', self.__field_voiceid, "sch/schexxx.qcp is the voice memo (xxx = voiceid - 0x0f)")
        yield ('unknown2', self.__field_unknown2, "This seems to always be yet two more zeros")
class schedulefile(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numactiveitems.writetobuffer(buf)
        try: self.__field_events
        except:
            self.__field_events=LIST(**{'elementclass': scheduleevent})
        self.__field_events.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class recipient_record(BaseProtogenClass):
    __fields=['unknown1', 'name', 'number', 'unknown2', 'status', 'time']
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
        if getattr(self, '__field_name', None) is None:
            self.__field_name=USTRING(**{'encoding': PHONE_ENCODING, 'default':'', 'raiseonunterminatedread': False})
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_unknown1.writetobuffer(buf)
        self.__field_number.writetobuffer(buf)
        self.__field_unknown2.writetobuffer(buf)
        self.__field_status.writetobuffer(buf)
        self.__field_time.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_unknown1=UINT(**{'sizeinbytes': 20})
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_number=USTRING(**{'sizeinbytes': 49})
        self.__field_number.readfrombuffer(buf)
        self.__field_unknown2=UINT(**{'sizeinbytes': 24})
        self.__field_unknown2.readfrombuffer(buf)
        self.__field_status=UINT(**{'sizeinbytes': 1})
        self.__field_status.readfrombuffer(buf)
        self.__field_time=LGCALDATE(**{'sizeinbytes': 4})
        self.__field_time.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_unknown1(self):
        return self.__field_unknown1.getvalue()
    def __setfield_unknown1(self, value):
        if isinstance(value,UINT):
            self.__field_unknown1=value
        else:
            self.__field_unknown1=UINT(value,**{'sizeinbytes': 20})
    def __delfield_unknown1(self): del self.__field_unknown1
    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,USTRING):
            self.__field_name=value
        else:
            self.__field_name=USTRING(value,**{'encoding': PHONE_ENCODING, 'default':'', 'raiseonunterminatedread': False})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_number(self):
        return self.__field_number.getvalue()
    def __setfield_number(self, value):
        if isinstance(value,USTRING):
            self.__field_number=value
        else:
            self.__field_number=USTRING(value,**{'sizeinbytes': 49})
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
    def __getfield_time(self):
        return self.__field_time.getvalue()
    def __setfield_time(self, value):
        if isinstance(value,LGCALDATE):
            self.__field_time=value
        else:
            self.__field_time=LGCALDATE(value,**{'sizeinbytes': 4})
    def __delfield_time(self): del self.__field_time
    time=property(__getfield_time, __setfield_time, __delfield_time, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('unknown1', self.__field_unknown1, None)
        yield ('name', self.__field_name, None)
        yield ('number', self.__field_number, None)
        yield ('unknown2', self.__field_unknown2, None)
        yield ('status', self.__field_status, None)
        yield ('time', self.__field_time, None)
class sms_saved(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_outboxmsg.writetobuffer(buf)
        self.__field_GPStime.writetobuffer(buf)
        if self.outboxmsg:
            self.__field_outbox.writetobuffer(buf)
        if not self.outboxmsg:
            self.__field_inbox.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class sms_out(BaseProtogenClass):
    __fields=['index', 'locked', 'unknown1', 'timesent', 'msg', 'unknown2', 'callback', 'recipients']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_index.writetobuffer(buf)
        self.__field_locked.writetobuffer(buf)
        self.__field_unknown1.writetobuffer(buf)
        self.__field_timesent.writetobuffer(buf)
        self.__field_msg.writetobuffer(buf)
        self.__field_unknown2.writetobuffer(buf)
        self.__field_callback.writetobuffer(buf)
        try: self.__field_recipients
        except:
            self.__field_recipients=LIST(**{'elementclass': recipient_record, 'length': 10})
        self.__field_recipients.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_index=UINT(**{'sizeinbytes': 4})
        self.__field_index.readfrombuffer(buf)
        self.__field_locked=UINT(**{'sizeinbytes': 1})
        self.__field_locked.readfrombuffer(buf)
        self.__field_unknown1=UINT(**{'sizeinbytes': 3})
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_timesent=LGCALDATE(**{'sizeinbytes': 4})
        self.__field_timesent.readfrombuffer(buf)
        self.__field_msg=USTRING(**{'sizeinbytes': 500, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
        self.__field_msg.readfrombuffer(buf)
        self.__field_unknown2=DATA(**{'sizeinbytes': 1250})
        self.__field_unknown2.readfrombuffer(buf)
        self.__field_callback=USTRING(**{'sizeinbytes': 16})
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
    def __getfield_msg(self):
        return self.__field_msg.getvalue()
    def __setfield_msg(self, value):
        if isinstance(value,USTRING):
            self.__field_msg=value
        else:
            self.__field_msg=USTRING(value,**{'sizeinbytes': 500, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
    def __delfield_msg(self): del self.__field_msg
    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
    def __getfield_unknown2(self):
        return self.__field_unknown2.getvalue()
    def __setfield_unknown2(self, value):
        if isinstance(value,DATA):
            self.__field_unknown2=value
        else:
            self.__field_unknown2=DATA(value,**{'sizeinbytes': 1250})
    def __delfield_unknown2(self): del self.__field_unknown2
    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
    def __getfield_callback(self):
        return self.__field_callback.getvalue()
    def __setfield_callback(self, value):
        if isinstance(value,USTRING):
            self.__field_callback=value
        else:
            self.__field_callback=USTRING(value,**{'sizeinbytes': 16})
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
        yield ('msg', self.__field_msg, None)
        yield ('unknown2', self.__field_unknown2, None)
        yield ('callback', self.__field_callback, None)
        yield ('recipients', self.__field_recipients, None)
class SMSINBOXMSGFRAGMENT(BaseProtogenClass):
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
            dict2={'elementclass': _gen_p_lglg6190_321, 'length': 181}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_msg=LIST(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_msg
        except:
            self.__field_msg=LIST(**{'elementclass': _gen_p_lglg6190_321, 'length': 181})
        self.__field_msg.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_msg=LIST(**{'elementclass': _gen_p_lglg6190_321, 'length': 181})
        self.__field_msg.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_msg(self):
        try: self.__field_msg
        except:
            self.__field_msg=LIST(**{'elementclass': _gen_p_lglg6190_321, 'length': 181})
        return self.__field_msg.getvalue()
    def __setfield_msg(self, value):
        if isinstance(value,LIST):
            self.__field_msg=value
        else:
            self.__field_msg=LIST(value,**{'elementclass': _gen_p_lglg6190_321, 'length': 181})
    def __delfield_msg(self): del self.__field_msg
    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('msg', self.__field_msg, None)
class _gen_p_lglg6190_321(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['byte']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6190_321,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6190_321:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6190_321,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6190_321,kwargs)
        if len(args):
            dict2={'sizeinbytes': 1}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_byte=UINT(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_byte.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class sms_in(BaseProtogenClass):
    __fields=['unknown1', 'msg_index2', 'unknown2', 'timesent', 'unknown', 'callback_length', 'callback', 'sender_length', 'sender', 'unknown4', 'lg_time', 'GPStime', 'unknown41', 'read', 'unknown5', 'subject', 'unknown6', 'msglength', 'unknown7', 'msg', 'unknown8']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
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
            self.__field_sender=LIST(**{'elementclass': _gen_p_lglg6190_333, 'length': 38})
        self.__field_sender.writetobuffer(buf)
        self.__field_unknown4.writetobuffer(buf)
        self.__field_lg_time.writetobuffer(buf)
        self.__field_GPStime.writetobuffer(buf)
        self.__field_unknown41.writetobuffer(buf)
        self.__field_read.writetobuffer(buf)
        self.__field_unknown5.writetobuffer(buf)
        self.__field_subject.writetobuffer(buf)
        self.__field_unknown6.writetobuffer(buf)
        self.__field_msglength.writetobuffer(buf)
        self.__field_unknown7.writetobuffer(buf)
        self.__field_msg.writetobuffer(buf)
        self.__field_unknown8.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_unknown1=UINT(**{'sizeinbytes': 4})
        self.__field_unknown1.readfrombuffer(buf)
        self.__field_msg_index2=UINT(**{'sizeinbytes': 4})
        self.__field_msg_index2.readfrombuffer(buf)
        self.__field_unknown2=UINT(**{'sizeinbytes': 6})
        self.__field_unknown2.readfrombuffer(buf)
        self.__field_timesent=SMSDATE(**{'sizeinbytes': 6})
        self.__field_timesent.readfrombuffer(buf)
        self.__field_unknown=UINT(**{'sizeinbytes': 3})
        self.__field_unknown.readfrombuffer(buf)
        self.__field_callback_length=UINT(**{'sizeinbytes': 1})
        self.__field_callback_length.readfrombuffer(buf)
        self.__field_callback=USTRING(**{'sizeinbytes': 38})
        self.__field_callback.readfrombuffer(buf)
        self.__field_sender_length=UINT(**{'sizeinbytes': 1})
        self.__field_sender_length.readfrombuffer(buf)
        self.__field_sender=LIST(**{'elementclass': _gen_p_lglg6190_333, 'length': 38})
        self.__field_sender.readfrombuffer(buf)
        self.__field_unknown4=DATA(**{'sizeinbytes': 15})
        self.__field_unknown4.readfrombuffer(buf)
        self.__field_lg_time=LGCALDATE(**{'sizeinbytes': 4})
        self.__field_lg_time.readfrombuffer(buf)
        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})
        self.__field_GPStime.readfrombuffer(buf)
        self.__field_unknown41=UINT(**{'sizeinbytes': 2})
        self.__field_unknown41.readfrombuffer(buf)
        self.__field_read=UINT(**{'sizeinbytes': 2})
        self.__field_read.readfrombuffer(buf)
        self.__field_unknown5=UINT(**{'sizeinbytes': 9})
        self.__field_unknown5.readfrombuffer(buf)
        self.__field_subject=USTRING(**{'sizeinbytes': 21, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
        self.__field_subject.readfrombuffer(buf)
        self.__field_unknown6=UINT(**{'sizeinbytes': 8})
        self.__field_unknown6.readfrombuffer(buf)
        self.__field_msglength=UINT(**{'sizeinbytes': 2})
        self.__field_msglength.readfrombuffer(buf)
        self.__field_unknown7=UINT(**{'sizeinbytes': 18})
        self.__field_unknown7.readfrombuffer(buf)
        self.__field_msg=USTRING(**{'sizeinbytes': 200, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
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
            self.__field_unknown2=UINT(value,**{'sizeinbytes': 6})
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
        if isinstance(value,USTRING):
            self.__field_callback=value
        else:
            self.__field_callback=USTRING(value,**{'sizeinbytes': 38})
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
            self.__field_sender=LIST(**{'elementclass': _gen_p_lglg6190_333, 'length': 38})
        return self.__field_sender.getvalue()
    def __setfield_sender(self, value):
        if isinstance(value,LIST):
            self.__field_sender=value
        else:
            self.__field_sender=LIST(value,**{'elementclass': _gen_p_lglg6190_333, 'length': 38})
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
    def __getfield_unknown41(self):
        return self.__field_unknown41.getvalue()
    def __setfield_unknown41(self, value):
        if isinstance(value,UINT):
            self.__field_unknown41=value
        else:
            self.__field_unknown41=UINT(value,**{'sizeinbytes': 2})
    def __delfield_unknown41(self): del self.__field_unknown41
    unknown41=property(__getfield_unknown41, __setfield_unknown41, __delfield_unknown41, None)
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
        if isinstance(value,USTRING):
            self.__field_subject=value
        else:
            self.__field_subject=USTRING(value,**{'sizeinbytes': 21, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
    def __delfield_subject(self): del self.__field_subject
    subject=property(__getfield_subject, __setfield_subject, __delfield_subject, None)
    def __getfield_unknown6(self):
        return self.__field_unknown6.getvalue()
    def __setfield_unknown6(self, value):
        if isinstance(value,UINT):
            self.__field_unknown6=value
        else:
            self.__field_unknown6=UINT(value,**{'sizeinbytes': 8})
    def __delfield_unknown6(self): del self.__field_unknown6
    unknown6=property(__getfield_unknown6, __setfield_unknown6, __delfield_unknown6, None)
    def __getfield_msglength(self):
        return self.__field_msglength.getvalue()
    def __setfield_msglength(self, value):
        if isinstance(value,UINT):
            self.__field_msglength=value
        else:
            self.__field_msglength=UINT(value,**{'sizeinbytes': 2})
    def __delfield_msglength(self): del self.__field_msglength
    msglength=property(__getfield_msglength, __setfield_msglength, __delfield_msglength, None)
    def __getfield_unknown7(self):
        return self.__field_unknown7.getvalue()
    def __setfield_unknown7(self, value):
        if isinstance(value,UINT):
            self.__field_unknown7=value
        else:
            self.__field_unknown7=UINT(value,**{'sizeinbytes': 18})
    def __delfield_unknown7(self): del self.__field_unknown7
    unknown7=property(__getfield_unknown7, __setfield_unknown7, __delfield_unknown7, None)
    def __getfield_msg(self):
        return self.__field_msg.getvalue()
    def __setfield_msg(self, value):
        if isinstance(value,USTRING):
            self.__field_msg=value
        else:
            self.__field_msg=USTRING(value,**{'sizeinbytes': 200, 'encoding': PHONE_ENCODING, 'raiseonunterminatedread': False})
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
        yield ('unknown41', self.__field_unknown41, None)
        yield ('read', self.__field_read, None)
        yield ('unknown5', self.__field_unknown5, None)
        yield ('subject', self.__field_subject, None)
        yield ('unknown6', self.__field_unknown6, None)
        yield ('msglength', self.__field_msglength, None)
        yield ('unknown7', self.__field_unknown7, None)
        yield ('msg', self.__field_msg, None)
        yield ('unknown8', self.__field_unknown8, None)
class _gen_p_lglg6190_333(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['byte']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6190_333,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6190_333:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6190_333,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6190_333,kwargs)
        if len(args):
            dict2={'sizeinbytes': 1}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_byte=UINT(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_byte.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
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
class sms_quick_text(BaseProtogenClass):
    __fields=['msgs']
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
        if len(args):
            dict2={'elementclass': _gen_p_lglg6190_354, }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_msgs=LIST(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_msgs
        except:
            self.__field_msgs=LIST(**{'elementclass': _gen_p_lglg6190_354, })
        self.__field_msgs.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_msgs=LIST(**{'elementclass': _gen_p_lglg6190_354, })
        self.__field_msgs.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_msgs(self):
        try: self.__field_msgs
        except:
            self.__field_msgs=LIST(**{'elementclass': _gen_p_lglg6190_354, })
        return self.__field_msgs.getvalue()
    def __setfield_msgs(self, value):
        if isinstance(value,LIST):
            self.__field_msgs=value
        else:
            self.__field_msgs=LIST(value,**{'elementclass': _gen_p_lglg6190_354, })
    def __delfield_msgs(self): del self.__field_msgs
    msgs=property(__getfield_msgs, __setfield_msgs, __delfield_msgs, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('msgs', self.__field_msgs, None)
class _gen_p_lglg6190_354(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['msg']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_lglg6190_354,self).__init__(**dict)
        if self.__class__ is _gen_p_lglg6190_354:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_lglg6190_354,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_lglg6190_354,kwargs)
        if len(args):
            dict2={'encoding': PHONE_ENCODING}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_msg=USTRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_msg.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_msg=USTRING(**{'encoding': PHONE_ENCODING})
        self.__field_msg.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_msg(self):
        return self.__field_msg.getvalue()
    def __setfield_msg(self, value):
        if isinstance(value,USTRING):
            self.__field_msg=value
        else:
            self.__field_msg=USTRING(value,**{'encoding': PHONE_ENCODING})
    def __delfield_msg(self): del self.__field_msg
    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('msg', self.__field_msg, None)
