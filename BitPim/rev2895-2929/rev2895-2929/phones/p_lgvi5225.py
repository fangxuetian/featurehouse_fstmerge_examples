"""Various descriptions of data specific to LG VI5225"""

from prototypes import *

from prototypeslg import *

from p_lg import *

from p_lgvx4400 import *

UINT=UINTlsb

BOOL=BOOLlsb

NUMSPEEDDIALS=100

FIRSTSPEEDDIAL=2

LASTSPEEDDIAL=99

NUMPHONEBOOKENTRIES=500

MAXCALENDARDESCRIPTION=38

NUMEMAILS=1

NUMPHONENUMBERS=5

MEMOLENGTH=48

pb_file_name='pim/pbentry.dat'

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

class  ffpacket (BaseProtogenClass) :
	__fields=['header', 'command', 'dunno1', 'dunno2', 'pad', 'dunno3', 'dunno4']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(ffpacket,self).__init__(**dict)

        if self.__class__ is ffpacket:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(ffpacket,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(ffpacket,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_header

        except:

            self.__field_header=UINT(**{'sizeinbytes': 1, 'constant': 0xff})

        self.__field_header.writetobuffer(buf)

        self.__field_command.writetobuffer(buf)

        self.__field_dunno1.writetobuffer(buf)

        self.__field_dunno2.writetobuffer(buf)

        self.__field_pad.writetobuffer(buf)

        self.__field_dunno3.writetobuffer(buf)

        self.__field_dunno4.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header=UINT(**{'sizeinbytes': 1, 'constant': 0xff})

        self.__field_header.readfrombuffer(buf)

        self.__field_command=UINT(**{'sizeinbytes': 1})

        self.__field_command.readfrombuffer(buf)

        self.__field_dunno1=UINT(**{'sizeinbytes': 1})

        self.__field_dunno1.readfrombuffer(buf)

        self.__field_dunno2=UINT(**{'sizeinbytes': 1})

        self.__field_dunno2.readfrombuffer(buf)

        self.__field_pad=UKNOWN(**{'sizeinbytes': 4})

        self.__field_pad.readfrombuffer(buf)

        self.__field_dunno3=UINT(**{'sizeinbytes': 1})

        self.__field_dunno3.readfrombuffer(buf)

        self.__field_dunno4=UINT(**{'sizeinbytes': 1})

        self.__field_dunno4.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_header(self):

        try: self.__field_header

        except:

            self.__field_header=UINT(**{'sizeinbytes': 1, 'constant': 0xff})

        return self.__field_header.getvalue()

	def __setfield_header(self, value):

        if isinstance(value,UINT):

            self.__field_header=value

        else:

            self.__field_header=UINT(value,**{'sizeinbytes': 1, 'constant': 0xff})

	def __delfield_header(self): del self.__field_header

	    header=property(__getfield_header, __setfield_header, __delfield_header, None)
	    def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,UINT):

            self.__field_command=value

        else:

            self.__field_command=UINT(value,**{'sizeinbytes': 1})

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_dunno1(self):

        return self.__field_dunno1.getvalue()

	def __setfield_dunno1(self, value):

        if isinstance(value,UINT):

            self.__field_dunno1=value

        else:

            self.__field_dunno1=UINT(value,**{'sizeinbytes': 1})

	def __delfield_dunno1(self): del self.__field_dunno1

	    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
	    def __getfield_dunno2(self):

        return self.__field_dunno2.getvalue()

	def __setfield_dunno2(self, value):

        if isinstance(value,UINT):

            self.__field_dunno2=value

        else:

            self.__field_dunno2=UINT(value,**{'sizeinbytes': 1})

	def __delfield_dunno2(self): del self.__field_dunno2

	    dunno2=property(__getfield_dunno2, __setfield_dunno2, __delfield_dunno2, None)
	    def __getfield_pad(self):

        return self.__field_pad.getvalue()

	def __setfield_pad(self, value):

        if isinstance(value,UKNOWN):

            self.__field_pad=value

        else:

            self.__field_pad=UKNOWN(value,**{'sizeinbytes': 4})

	def __delfield_pad(self): del self.__field_pad

	    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
	    def __getfield_dunno3(self):

        return self.__field_dunno3.getvalue()

	def __setfield_dunno3(self, value):

        if isinstance(value,UINT):

            self.__field_dunno3=value

        else:

            self.__field_dunno3=UINT(value,**{'sizeinbytes': 1})

	def __delfield_dunno3(self): del self.__field_dunno3

	    dunno3=property(__getfield_dunno3, __setfield_dunno3, __delfield_dunno3, None)
	    def __getfield_dunno4(self):

        return self.__field_dunno4.getvalue()

	def __setfield_dunno4(self, value):

        if isinstance(value,UINT):

            self.__field_dunno4=value

        else:

            self.__field_dunno4=UINT(value,**{'sizeinbytes': 1})

	def __delfield_dunno4(self): del self.__field_dunno4

	    dunno4=property(__getfield_dunno4, __setfield_dunno4, __delfield_dunno4, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('header', self.__field_header, None)

        yield ('command', self.__field_command, None)

        yield ('dunno1', self.__field_dunno1, None)

        yield ('dunno2', self.__field_dunno2, None)

        yield ('pad', self.__field_pad, None)

        yield ('dunno3', self.__field_dunno3, None)

        yield ('dunno4', self.__field_dunno4, None)


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
	__fields=['wallpaper', 'serial1', 'entrysize', 'serial2', 'entrynumber', 'name', 'group', 'emails', 'url', 'ringtone', 'secret', 'memo', 'dunno1', 'dunno2', 'numbertypes', 'numbers']
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

        if getattr(self, '__field_wallpaper', None) is None:

            self.__field_wallpaper=UINT(**{'constant': NOWALLPAPER})

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1.writetobuffer(buf)

        try: self.__field_entrysize

        except:

            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x01E0})

        self.__field_entrysize.writetobuffer(buf)

        self.__field_serial2.writetobuffer(buf)

        self.__field_entrynumber.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_group.writetobuffer(buf)

        try: self.__field_emails

        except:

            self.__field_emails=LIST(**{'elementclass': _gen_p_lgvi5225_116, 'length': NUMEMAILS})

        self.__field_emails.writetobuffer(buf)

        self.__field_url.writetobuffer(buf)

        self.__field_ringtone.writetobuffer(buf)

        self.__field_secret.writetobuffer(buf)

        self.__field_memo.writetobuffer(buf)

        try: self.__field_dunno1

        except:

            self.__field_dunno1=UINT(**{'sizeinbytes': 1, 'default': 0})

        self.__field_dunno1.writetobuffer(buf)

        try: self.__field_dunno2

        except:

            self.__field_dunno2=UINT(**{'sizeinbytes': 2, 'default': 0})

        self.__field_dunno2.writetobuffer(buf)

        try: self.__field_numbertypes

        except:

            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgvi5225_124, 'length': NUMPHONENUMBERS})

        self.__field_numbertypes.writetobuffer(buf)

        try: self.__field_numbers

        except:

            self.__field_numbers=LIST(**{'elementclass': _gen_p_lgvi5225_126, 'length': NUMPHONENUMBERS})

        self.__field_numbers.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1=UINT(**{'sizeinbytes': 4})

        self.__field_serial1.readfrombuffer(buf)

        self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x01E0})

        self.__field_entrysize.readfrombuffer(buf)

        self.__field_serial2=UINT(**{'sizeinbytes': 4})

        self.__field_serial2.readfrombuffer(buf)

        self.__field_entrynumber=UINT(**{'sizeinbytes': 2})

        self.__field_entrynumber.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 23, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self.__field_group=UINT(**{'sizeinbytes': 2})

        self.__field_group.readfrombuffer(buf)

        self.__field_emails=LIST(**{'elementclass': _gen_p_lgvi5225_116, 'length': NUMEMAILS})

        self.__field_emails.readfrombuffer(buf)

        self.__field_url=STRING(**{'sizeinbytes': 73, 'raiseonunterminatedread': False})

        self.__field_url.readfrombuffer(buf)

        self.__field_ringtone=UINT(**{'sizeinbytes': 1})

        self.__field_ringtone.readfrombuffer(buf)

        self.__field_secret=UINT(**{'sizeinbytes': 1})

        self.__field_secret.readfrombuffer(buf)

        self.__field_memo=STRING(**{'raiseonunterminatedread': False, 'sizeinbytes': MEMOLENGTH})

        self.__field_memo.readfrombuffer(buf)

        self.__field_dunno1=UINT(**{'sizeinbytes': 1, 'default': 0})

        self.__field_dunno1.readfrombuffer(buf)

        self.__field_dunno2=UINT(**{'sizeinbytes': 2, 'default': 0})

        self.__field_dunno2.readfrombuffer(buf)

        self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgvi5225_124, 'length': NUMPHONENUMBERS})

        self.__field_numbertypes.readfrombuffer(buf)

        self.__field_numbers=LIST(**{'elementclass': _gen_p_lgvi5225_126, 'length': NUMPHONENUMBERS})

        self.__field_numbers.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_wallpaper(self):

        return self.__field_wallpaper.getvalue()

	def __setfield_wallpaper(self, value):

        if isinstance(value,UINT):

            self.__field_wallpaper=value

        else:

            self.__field_wallpaper=UINT(value,**{'constant': NOWALLPAPER})

	def __delfield_wallpaper(self): del self.__field_wallpaper

	    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
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

            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x01E0})

        return self.__field_entrysize.getvalue()

	def __setfield_entrysize(self, value):

        if isinstance(value,UINT):

            self.__field_entrysize=value

        else:

            self.__field_entrysize=UINT(value,**{'sizeinbytes': 2, 'constant': 0x01E0})

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

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 23, 'raiseonunterminatedread': False})

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

            self.__field_emails=LIST(**{'elementclass': _gen_p_lgvi5225_116, 'length': NUMEMAILS})

        return self.__field_emails.getvalue()

	def __setfield_emails(self, value):

        if isinstance(value,LIST):

            self.__field_emails=value

        else:

            self.__field_emails=LIST(value,**{'elementclass': _gen_p_lgvi5225_116, 'length': NUMEMAILS})

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
	    def __getfield_ringtone(self):

        return self.__field_ringtone.getvalue()

	def __setfield_ringtone(self, value):

        if isinstance(value,UINT):

            self.__field_ringtone=value

        else:

            self.__field_ringtone=UINT(value,**{'sizeinbytes': 1})

	def __delfield_ringtone(self): del self.__field_ringtone

	    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, "ringtone index for a call")
	    def __getfield_secret(self):

        return self.__field_secret.getvalue()

	def __setfield_secret(self, value):

        if isinstance(value,UINT):

            self.__field_secret=value

        else:

            self.__field_secret=UINT(value,**{'sizeinbytes': 1})

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
	    def __getfield_dunno1(self):

        try: self.__field_dunno1

        except:

            self.__field_dunno1=UINT(**{'sizeinbytes': 1, 'default': 0})

        return self.__field_dunno1.getvalue()

	def __setfield_dunno1(self, value):

        if isinstance(value,UINT):

            self.__field_dunno1=value

        else:

            self.__field_dunno1=UINT(value,**{'sizeinbytes': 1, 'default': 0})

	def __delfield_dunno1(self): del self.__field_dunno1

	    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
	    def __getfield_dunno2(self):

        try: self.__field_dunno2

        except:

            self.__field_dunno2=UINT(**{'sizeinbytes': 2, 'default': 0})

        return self.__field_dunno2.getvalue()

	def __setfield_dunno2(self, value):

        if isinstance(value,UINT):

            self.__field_dunno2=value

        else:

            self.__field_dunno2=UINT(value,**{'sizeinbytes': 2, 'default': 0})

	def __delfield_dunno2(self): del self.__field_dunno2

	    dunno2=property(__getfield_dunno2, __setfield_dunno2, __delfield_dunno2, None)
	    def __getfield_numbertypes(self):

        try: self.__field_numbertypes

        except:

            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgvi5225_124, 'length': NUMPHONENUMBERS})

        return self.__field_numbertypes.getvalue()

	def __setfield_numbertypes(self, value):

        if isinstance(value,LIST):

            self.__field_numbertypes=value

        else:

            self.__field_numbertypes=LIST(value,**{'elementclass': _gen_p_lgvi5225_124, 'length': NUMPHONENUMBERS})

	def __delfield_numbertypes(self): del self.__field_numbertypes

	    numbertypes=property(__getfield_numbertypes, __setfield_numbertypes, __delfield_numbertypes, None)
	    def __getfield_numbers(self):

        try: self.__field_numbers

        except:

            self.__field_numbers=LIST(**{'elementclass': _gen_p_lgvi5225_126, 'length': NUMPHONENUMBERS})

        return self.__field_numbers.getvalue()

	def __setfield_numbers(self, value):

        if isinstance(value,LIST):

            self.__field_numbers=value

        else:

            self.__field_numbers=LIST(value,**{'elementclass': _gen_p_lgvi5225_126, 'length': NUMPHONENUMBERS})

	def __delfield_numbers(self): del self.__field_numbers

	    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('wallpaper', self.__field_wallpaper, None)

        yield ('serial1', self.__field_serial1, None)

        yield ('entrysize', self.__field_entrysize, None)

        yield ('serial2', self.__field_serial2, None)

        yield ('entrynumber', self.__field_entrynumber, None)

        yield ('name', self.__field_name, None)

        yield ('group', self.__field_group, None)

        yield ('emails', self.__field_emails, None)

        yield ('url', self.__field_url, None)

        yield ('ringtone', self.__field_ringtone, "ringtone index for a call")

        yield ('secret', self.__field_secret, None)

        yield ('memo', self.__field_memo, None)

        yield ('dunno1', self.__field_dunno1, None)

        yield ('dunno2', self.__field_dunno2, None)

        yield ('numbertypes', self.__field_numbertypes, None)

        yield ('numbers', self.__field_numbers, None)


class  _gen_p_lgvi5225_116 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['email']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvi5225_116,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvi5225_116:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvi5225_116,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvi5225_116,kwargs)

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

class  _gen_p_lgvi5225_124 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['numbertype']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvi5225_124,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvi5225_124:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvi5225_124,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvi5225_124,kwargs)

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

class  _gen_p_lgvi5225_126 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['number']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvi5225_126,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvi5225_126:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvi5225_126,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvi5225_126,kwargs)

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

