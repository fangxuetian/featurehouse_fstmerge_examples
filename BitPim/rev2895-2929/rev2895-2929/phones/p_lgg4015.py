"""Various descriptions of data specific to LG G4015"""

from prototypes import *

from prototypeslg import *

from p_gsm import *

from p_lg import *

UINT=UINTlsb

BOOL=BOOLlsb

MEDIA_RINGTONE=0

MEDIA_WALLPAPER=1

GROUP_INDEX_RANGE=xrange(8)

MIN_RINGTONE_INDEX=0

MAX_RINGTONE_INDEX=19

MIN_WALLPAPER_INDEX=0

MAX_WALLPAPER_INDEX=19

CHARSET_IRA='IRA'

CHARSET_BASE64='Base64'

CHARSET_GSM='GSM'

CHARSET_HEX='HEX'

CHARSET_PCCP437='PCCP437'

CHARSET_PCDN='PCDN'

CHARSET_8859_1='8859-1'

CHARSET_UCS2='UCS2'

CAL_TOTAL_ENTRIES=30

CAL_MIN_INDEX=0

CAL_MAX_INDEX=29

CAL_DESC_LEN=30

CAL_REP_NONE=0

CAL_REP_DAILY=1

CAL_REP_WEEKLY=2

CAL_REP_MONTHLY=3

CAL_REP_YEARLY=4

CAL_ALARM_NONE=0

CAL_ALARM_ONTIME=1

CAL_ALARM_15MIN=2

CAL_ALARM_30MIN=3

CAL_ALARM_1HR=4

CAL_ALARM_1DAY=5

CAL_ALARM_VALUE={
    CAL_ALARM_NONE: -1,
    CAL_ALARM_ONTIME: 0,
    CAL_ALARM_15MIN: 15,
    CAL_ALARM_30MIN: 30,
    CAL_ALARM_1HR: 60,
    CAL_ALARM_1DAY: 1440 }

CAL_ALARM_LIST=((1440, CAL_ALARM_1DAY), (60, CAL_ALARM_1HR),
                (30, CAL_ALARM_30MIN), (15, CAL_ALARM_15MIN),
                (0, CAL_ALARM_ONTIME), (-1, CAL_ALARM_NONE))

PB_MEMORY_SIM='AD'

PB_MEMORY_MAIN='ME'

PB_MEMORY_LAST_DIALED='LD'

PB_MEMORY_LAST_RECEIVED='LR'

PB_MEMORY_LAST_MISSED='LM'

PB_MAIN_TOTAL_ENTRIES=255

PB_MAIN_MIN_INDEX=0

PB_MAIN_MAX_INDEX=254

PB_SIM_TOTAL_ENTRIES=250

PB_SIM_MIN_INDEX=1

PB_SIM_MAX_INDEX=250

PB_NUMBER_LEN=40

PB_NAME_LEN=20

PB_EMAIL_LEN=40

PB_MEMO_LEN=50

PB_SIM_NAME_LEN=16

PB_LD_MIN_INDEX=1

PB_LD_MAX_INDEX=10

PB_LR_MIN_INDEX=1

PB_LR_MAX_INDEX=20

PB_LM_MIN_INDEX=1

PB_LM_MAX_INDEX=10

PB_CALL_HISTORY_INFO=(
    ('Getting Last Dialed Calls', PB_MEMORY_LAST_DIALED,
     PB_LD_MIN_INDEX, PB_LD_MAX_INDEX),
    ('Getting Last Received Calls', PB_MEMORY_LAST_RECEIVED,
     PB_LR_MIN_INDEX, PB_LR_MAX_INDEX),
    ('Getting Missed Calls', PB_MEMORY_LAST_MISSED,
     PB_LM_MIN_INDEX, PB_LM_MAX_INDEX))

MEMO_MIN_INDEX=0

MEMO_MAX_INDEX=19

MEMO_READ_CMD='+CMDR'

MEMO_WRITE_CMD='+CMDW'

SMS_MEMORY_PHONE='ME'

SMS_MEMORY_SIM='SM'

SMS_MEMORY_SELECT_CMD='+CPMS'

SMS_FORMAT_TEXT=1

SMS_FORMAT_PDU=0

SMS_FORMAT_CMD='+CMGF'

SMS_MSG_REC_UNREAD='REC UNREAD'

SMS_MSG_REC_READ='REC READ'

SMS_MSG_STO_UNSENT='STO UNSENT'

SMS_MSG_STO_SENT='STO SENT'

SMS_MSG_ALL='ALL'

SMS_MSG_LIST_CMD='+CMGL'

class  calendar_read_req (BaseProtogenClass) :
	__fields=['command', 'start_index', 'end_index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(calendar_read_req,self).__init__(**dict)

        if self.__class__ is calendar_read_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(calendar_read_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(calendar_read_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDR=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        self.__field_start_index.writetobuffer(buf)

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDR=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_start_index=CSVINT()

        self.__field_start_index.readfrombuffer(buf)

        self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDR=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CXDR=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_start_index(self):

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        return self.__field_start_index.getvalue()

	def __setfield_start_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_start_index=value

        else:

            self.__field_start_index=CSVINT(value,)

	def __delfield_start_index(self): del self.__field_start_index

	    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
	    def __getfield_end_index(self):

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        return self.__field_end_index.getvalue()

	def __setfield_end_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_end_index=value

        else:

            self.__field_end_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_end_index(self): del self.__field_end_index

	    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('start_index', self.__field_start_index, None)

        yield ('end_index', self.__field_end_index, None)


class  calendar_read_resp (BaseProtogenClass) :
	__fields=['command', 'index', 'repeat', 'alarm', 'date', 'time', 'description']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(calendar_read_resp,self).__init__(**dict)

        if self.__class__ is calendar_read_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(calendar_read_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(calendar_read_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_repeat.writetobuffer(buf)

        self.__field_alarm.writetobuffer(buf)

        self.__field_date.writetobuffer(buf)

        self.__field_time.writetobuffer(buf)

        self.__field_description.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': ord(' '), 'constant': '+CXDR:'})

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_repeat=CSVINT()

        self.__field_repeat.readfrombuffer(buf)

        self.__field_alarm=CSVINT()

        self.__field_alarm.readfrombuffer(buf)

        self.__field_date=GSMCALDATE()

        self.__field_date.readfrombuffer(buf)

        self.__field_time=GSMCALTIME()

        self.__field_time.readfrombuffer(buf)

        self.__field_description=CSVSTRING(**{ 'terminator': None })

        self.__field_description.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_command=value

        else:

            self.__field_command=CSVSTRING(value,**{'quotechar': None, 'terminator': ord(' '), 'constant': '+CXDR:'})

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_repeat(self):

        return self.__field_repeat.getvalue()

	def __setfield_repeat(self, value):

        if isinstance(value,CSVINT):

            self.__field_repeat=value

        else:

            self.__field_repeat=CSVINT(value,)

	def __delfield_repeat(self): del self.__field_repeat

	    repeat=property(__getfield_repeat, __setfield_repeat, __delfield_repeat, None)
	    def __getfield_alarm(self):

        return self.__field_alarm.getvalue()

	def __setfield_alarm(self, value):

        if isinstance(value,CSVINT):

            self.__field_alarm=value

        else:

            self.__field_alarm=CSVINT(value,)

	def __delfield_alarm(self): del self.__field_alarm

	    alarm=property(__getfield_alarm, __setfield_alarm, __delfield_alarm, None)
	    def __getfield_date(self):

        return self.__field_date.getvalue()

	def __setfield_date(self, value):

        if isinstance(value,GSMCALDATE):

            self.__field_date=value

        else:

            self.__field_date=GSMCALDATE(value,)

	def __delfield_date(self): del self.__field_date

	    date=property(__getfield_date, __setfield_date, __delfield_date, None)
	    def __getfield_time(self):

        return self.__field_time.getvalue()

	def __setfield_time(self, value):

        if isinstance(value,GSMCALTIME):

            self.__field_time=value

        else:

            self.__field_time=GSMCALTIME(value,)

	def __delfield_time(self): del self.__field_time

	    time=property(__getfield_time, __setfield_time, __delfield_time, None)
	    def __getfield_description(self):

        return self.__field_description.getvalue()

	def __setfield_description(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_description=value

        else:

            self.__field_description=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_description(self): del self.__field_description

	    description=property(__getfield_description, __setfield_description, __delfield_description, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('repeat', self.__field_repeat, None)

        yield ('alarm', self.__field_alarm, None)

        yield ('date', self.__field_date, None)

        yield ('time', self.__field_time, None)

        yield ('description', self.__field_description, None)


class  calendar_write_check_req (BaseProtogenClass) :
	__fields=['command']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(calendar_write_check_req,self).__init__(**dict)

        if self.__class__ is calendar_write_check_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(calendar_write_check_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(calendar_write_check_req,kwargs)

        if len(args):

            dict2={ 'terminator': None, 'default': '+CXDW' }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_command=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW' })

        self.__field_command.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW' })

        self.__field_command.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CXDW' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)


class  calendar_write_check_resp (BaseProtogenClass) :
	__fields=['command', 'index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(calendar_write_check_resp,self).__init__(**dict)

        if self.__class__ is calendar_write_check_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(calendar_write_check_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(calendar_write_check_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+CXDW:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+CXDW:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)


class  calendar_write_req (BaseProtogenClass) :
	__fields=['command', 'index', 'repeat', 'alarm', 'date', 'time', 'description']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(calendar_write_req,self).__init__(**dict)

        if self.__class__ is calendar_write_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(calendar_write_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(calendar_write_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_index

        except:

            self.__field_index=CSVINT()

        self.__field_index.writetobuffer(buf)

        try: self.__field_repeat

        except:

            self.__field_repeat=CSVINT()

        self.__field_repeat.writetobuffer(buf)

        try: self.__field_alarm

        except:

            self.__field_alarm=CSVINT()

        self.__field_alarm.writetobuffer(buf)

        try: self.__field_date

        except:

            self.__field_date=GSMCALDATE()

        self.__field_date.writetobuffer(buf)

        try: self.__field_time

        except:

            self.__field_time=GSMCALTIME()

        self.__field_time.writetobuffer(buf)

        try: self.__field_description

        except:

            self.__field_description=CSVSTRING(**{ 'terminator': None,                  'maxsizeinbytes': CAL_DESC_LEN,                  'raiseontruncate': False })

        self.__field_description.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_repeat=CSVINT()

        self.__field_repeat.readfrombuffer(buf)

        self.__field_alarm=CSVINT()

        self.__field_alarm.readfrombuffer(buf)

        self.__field_date=GSMCALDATE()

        self.__field_date.readfrombuffer(buf)

        self.__field_time=GSMCALTIME()

        self.__field_time.readfrombuffer(buf)

        self.__field_description=CSVSTRING(**{ 'terminator': None,                  'maxsizeinbytes': CAL_DESC_LEN,                  'raiseontruncate': False })

        self.__field_description.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CXDW=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        try: self.__field_index

        except:

            self.__field_index=CSVINT()

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_repeat(self):

        try: self.__field_repeat

        except:

            self.__field_repeat=CSVINT()

        return self.__field_repeat.getvalue()

	def __setfield_repeat(self, value):

        if isinstance(value,CSVINT):

            self.__field_repeat=value

        else:

            self.__field_repeat=CSVINT(value,)

	def __delfield_repeat(self): del self.__field_repeat

	    repeat=property(__getfield_repeat, __setfield_repeat, __delfield_repeat, None)
	    def __getfield_alarm(self):

        try: self.__field_alarm

        except:

            self.__field_alarm=CSVINT()

        return self.__field_alarm.getvalue()

	def __setfield_alarm(self, value):

        if isinstance(value,CSVINT):

            self.__field_alarm=value

        else:

            self.__field_alarm=CSVINT(value,)

	def __delfield_alarm(self): del self.__field_alarm

	    alarm=property(__getfield_alarm, __setfield_alarm, __delfield_alarm, None)
	    def __getfield_date(self):

        try: self.__field_date

        except:

            self.__field_date=GSMCALDATE()

        return self.__field_date.getvalue()

	def __setfield_date(self, value):

        if isinstance(value,GSMCALDATE):

            self.__field_date=value

        else:

            self.__field_date=GSMCALDATE(value,)

	def __delfield_date(self): del self.__field_date

	    date=property(__getfield_date, __setfield_date, __delfield_date, None)
	    def __getfield_time(self):

        try: self.__field_time

        except:

            self.__field_time=GSMCALTIME()

        return self.__field_time.getvalue()

	def __setfield_time(self, value):

        if isinstance(value,GSMCALTIME):

            self.__field_time=value

        else:

            self.__field_time=GSMCALTIME(value,)

	def __delfield_time(self): del self.__field_time

	    time=property(__getfield_time, __setfield_time, __delfield_time, None)
	    def __getfield_description(self):

        try: self.__field_description

        except:

            self.__field_description=CSVSTRING(**{ 'terminator': None,                  'maxsizeinbytes': CAL_DESC_LEN,                  'raiseontruncate': False })

        return self.__field_description.getvalue()

	def __setfield_description(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_description=value

        else:

            self.__field_description=CSVSTRING(value,**{ 'terminator': None,                  'maxsizeinbytes': CAL_DESC_LEN,                  'raiseontruncate': False })

	def __delfield_description(self): del self.__field_description

	    description=property(__getfield_description, __setfield_description, __delfield_description, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('repeat', self.__field_repeat, None)

        yield ('alarm', self.__field_alarm, None)

        yield ('date', self.__field_date, None)

        yield ('time', self.__field_time, None)

        yield ('description', self.__field_description, None)


class  calendar_del_req (BaseProtogenClass) :
	__fields=['command', 'index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(calendar_del_req,self).__init__(**dict)

        if self.__class__ is calendar_del_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(calendar_del_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(calendar_del_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_index

        except:

            self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CXDW=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CXDW=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        try: self.__field_index

        except:

            self.__field_index=CSVINT(**{ 'terminator': None })

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)


class  media_selector_req (BaseProtogenClass) :
	__fields=['command']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(media_selector_req,self).__init__(**dict)

        if self.__class__ is media_selector_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(media_selector_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(media_selector_req,kwargs)

        if len(args):

            dict2={ 'terminator': None, 'default': '+DDLS?' }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_command=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLS?' })

        self.__field_command.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLS?' })

        self.__field_command.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLS?' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+DDLS?' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)


class  media_selector_resp (BaseProtogenClass) :
	__fields=['command', 'media_type']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(media_selector_resp,self).__init__(**dict)

        if self.__class__ is media_selector_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(media_selector_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(media_selector_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_media_type.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+DDLS:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_media_type=CSVINT(**{ 'terminator': None })

        self.__field_media_type.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+DDLS:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_media_type(self):

        return self.__field_media_type.getvalue()

	def __setfield_media_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_media_type=value

        else:

            self.__field_media_type=CSVINT(value,**{ 'terminator': None })

	def __delfield_media_type(self): del self.__field_media_type

	    media_type=property(__getfield_media_type, __setfield_media_type, __delfield_media_type, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('media_type', self.__field_media_type, None)


class  media_selector_set (BaseProtogenClass) :
	__fields=['command', 'media_type']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(media_selector_set,self).__init__(**dict)

        if self.__class__ is media_selector_set:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(media_selector_set,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(media_selector_set,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLS=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_media_type

        except:

            self.__field_media_type=CSVINT(**{ 'terminator': None })

        self.__field_media_type.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLS=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_media_type=CSVINT(**{ 'terminator': None })

        self.__field_media_type.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLS=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+DDLS=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_media_type(self):

        try: self.__field_media_type

        except:

            self.__field_media_type=CSVINT(**{ 'terminator': None })

        return self.__field_media_type.getvalue()

	def __setfield_media_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_media_type=value

        else:

            self.__field_media_type=CSVINT(value,**{ 'terminator': None })

	def __delfield_media_type(self): del self.__field_media_type

	    media_type=property(__getfield_media_type, __setfield_media_type, __delfield_media_type, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('media_type', self.__field_media_type, None)


class  media_list_req (BaseProtogenClass) :
	__fields=['command', 'start_index', 'end_index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(media_list_req,self).__init__(**dict)

        if self.__class__ is media_list_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(media_list_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(media_list_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLR=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        self.__field_start_index.writetobuffer(buf)

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLR=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_start_index=CSVINT()

        self.__field_start_index.readfrombuffer(buf)

        self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLR=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+DDLR=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_start_index(self):

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        return self.__field_start_index.getvalue()

	def __setfield_start_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_start_index=value

        else:

            self.__field_start_index=CSVINT(value,)

	def __delfield_start_index(self): del self.__field_start_index

	    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
	    def __getfield_end_index(self):

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        return self.__field_end_index.getvalue()

	def __setfield_end_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_end_index=value

        else:

            self.__field_end_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_end_index(self): del self.__field_end_index

	    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('start_index', self.__field_start_index, None)

        yield ('end_index', self.__field_end_index, None)


class  media_list_resp (BaseProtogenClass) :
	__fields=['command', 'index', 'file_name', 'media_name']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(media_list_resp,self).__init__(**dict)

        if self.__class__ is media_list_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(media_list_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(media_list_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_file_name.writetobuffer(buf)

        self.__field_media_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': ord(' '), 'constant': '+DDLR:'})

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_file_name=CSVSTRING()

        self.__field_file_name.readfrombuffer(buf)

        self.__field_media_name=CSVSTRING(**{ 'terminator': None })

        self.__field_media_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_command=value

        else:

            self.__field_command=CSVSTRING(value,**{'quotechar': None, 'terminator': ord(' '), 'constant': '+DDLR:'})

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_file_name(self):

        return self.__field_file_name.getvalue()

	def __setfield_file_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_file_name=value

        else:

            self.__field_file_name=CSVSTRING(value,)

	def __delfield_file_name(self): del self.__field_file_name

	    file_name=property(__getfield_file_name, __setfield_file_name, __delfield_file_name, None)
	    def __getfield_media_name(self):

        return self.__field_media_name.getvalue()

	def __setfield_media_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_media_name=value

        else:

            self.__field_media_name=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_media_name(self): del self.__field_media_name

	    media_name=property(__getfield_media_name, __setfield_media_name, __delfield_media_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('file_name', self.__field_file_name, None)

        yield ('media_name', self.__field_media_name, None)


class  del_media_req (BaseProtogenClass) :
	__fields=['command', 'file_name']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(del_media_req,self).__init__(**dict)

        if self.__class__ is del_media_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(del_media_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(del_media_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLD=0,' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_file_name

        except:

            self.__field_file_name=CSVSTRING(**{ 'terminator': None })

        self.__field_file_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLD=0,' })

        self.__field_command.readfrombuffer(buf)

        self.__field_file_name=CSVSTRING(**{ 'terminator': None })

        self.__field_file_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLD=0,' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+DDLD=0,' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_file_name(self):

        try: self.__field_file_name

        except:

            self.__field_file_name=CSVSTRING(**{ 'terminator': None })

        return self.__field_file_name.getvalue()

	def __setfield_file_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_file_name=value

        else:

            self.__field_file_name=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_file_name(self): del self.__field_file_name

	    file_name=property(__getfield_file_name, __setfield_file_name, __delfield_file_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('file_name', self.__field_file_name, None)


class  write_media_req (BaseProtogenClass) :
	__fields=['command', 'index', 'file_name', 'media_name', 'data_len', 'media_type', 'dunno1', 'dunno2', 'dunno3', 'dunno4']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(write_media_req,self).__init__(**dict)

        if self.__class__ is write_media_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(write_media_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(write_media_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLW=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_index

        except:

            self.__field_index=CSVINT()

        self.__field_index.writetobuffer(buf)

        try: self.__field_file_name

        except:

            self.__field_file_name=CSVSTRING()

        self.__field_file_name.writetobuffer(buf)

        try: self.__field_media_name

        except:

            self.__field_media_name=CSVSTRING()

        self.__field_media_name.writetobuffer(buf)

        self.__field_data_len.writetobuffer(buf)

        self.__field_media_type.writetobuffer(buf)

        try: self.__field_dunno1

        except:

            self.__field_dunno1=CSVINT(**{ 'default': 0 })

        self.__field_dunno1.writetobuffer(buf)

        try: self.__field_dunno2

        except:

            self.__field_dunno2=CSVINT(**{ 'default': 0 })

        self.__field_dunno2.writetobuffer(buf)

        try: self.__field_dunno3

        except:

            self.__field_dunno3=CSVINT(**{ 'default': 0 })

        self.__field_dunno3.writetobuffer(buf)

        try: self.__field_dunno4

        except:

            self.__field_dunno4=CSVINT(**{ 'default': 0, 'terminator': ord('\r') })

        self.__field_dunno4.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLW=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_file_name=CSVSTRING()

        self.__field_file_name.readfrombuffer(buf)

        self.__field_media_name=CSVSTRING()

        self.__field_media_name.readfrombuffer(buf)

        self.__field_data_len=CSVINT()

        self.__field_data_len.readfrombuffer(buf)

        self.__field_media_type=CSVINT()

        self.__field_media_type.readfrombuffer(buf)

        self.__field_dunno1=CSVINT(**{ 'default': 0 })

        self.__field_dunno1.readfrombuffer(buf)

        self.__field_dunno2=CSVINT(**{ 'default': 0 })

        self.__field_dunno2.readfrombuffer(buf)

        self.__field_dunno3=CSVINT(**{ 'default': 0 })

        self.__field_dunno3.readfrombuffer(buf)

        self.__field_dunno4=CSVINT(**{ 'default': 0, 'terminator': ord('\r') })

        self.__field_dunno4.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+DDLW=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+DDLW=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        try: self.__field_index

        except:

            self.__field_index=CSVINT()

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_file_name(self):

        try: self.__field_file_name

        except:

            self.__field_file_name=CSVSTRING()

        return self.__field_file_name.getvalue()

	def __setfield_file_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_file_name=value

        else:

            self.__field_file_name=CSVSTRING(value,)

	def __delfield_file_name(self): del self.__field_file_name

	    file_name=property(__getfield_file_name, __setfield_file_name, __delfield_file_name, None)
	    def __getfield_media_name(self):

        try: self.__field_media_name

        except:

            self.__field_media_name=CSVSTRING()

        return self.__field_media_name.getvalue()

	def __setfield_media_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_media_name=value

        else:

            self.__field_media_name=CSVSTRING(value,)

	def __delfield_media_name(self): del self.__field_media_name

	    media_name=property(__getfield_media_name, __setfield_media_name, __delfield_media_name, None)
	    def __getfield_data_len(self):

        return self.__field_data_len.getvalue()

	def __setfield_data_len(self, value):

        if isinstance(value,CSVINT):

            self.__field_data_len=value

        else:

            self.__field_data_len=CSVINT(value,)

	def __delfield_data_len(self): del self.__field_data_len

	    data_len=property(__getfield_data_len, __setfield_data_len, __delfield_data_len, None)
	    def __getfield_media_type(self):

        return self.__field_media_type.getvalue()

	def __setfield_media_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_media_type=value

        else:

            self.__field_media_type=CSVINT(value,)

	def __delfield_media_type(self): del self.__field_media_type

	    media_type=property(__getfield_media_type, __setfield_media_type, __delfield_media_type, None)
	    def __getfield_dunno1(self):

        try: self.__field_dunno1

        except:

            self.__field_dunno1=CSVINT(**{ 'default': 0 })

        return self.__field_dunno1.getvalue()

	def __setfield_dunno1(self, value):

        if isinstance(value,CSVINT):

            self.__field_dunno1=value

        else:

            self.__field_dunno1=CSVINT(value,**{ 'default': 0 })

	def __delfield_dunno1(self): del self.__field_dunno1

	    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
	    def __getfield_dunno2(self):

        try: self.__field_dunno2

        except:

            self.__field_dunno2=CSVINT(**{ 'default': 0 })

        return self.__field_dunno2.getvalue()

	def __setfield_dunno2(self, value):

        if isinstance(value,CSVINT):

            self.__field_dunno2=value

        else:

            self.__field_dunno2=CSVINT(value,**{ 'default': 0 })

	def __delfield_dunno2(self): del self.__field_dunno2

	    dunno2=property(__getfield_dunno2, __setfield_dunno2, __delfield_dunno2, None)
	    def __getfield_dunno3(self):

        try: self.__field_dunno3

        except:

            self.__field_dunno3=CSVINT(**{ 'default': 0 })

        return self.__field_dunno3.getvalue()

	def __setfield_dunno3(self, value):

        if isinstance(value,CSVINT):

            self.__field_dunno3=value

        else:

            self.__field_dunno3=CSVINT(value,**{ 'default': 0 })

	def __delfield_dunno3(self): del self.__field_dunno3

	    dunno3=property(__getfield_dunno3, __setfield_dunno3, __delfield_dunno3, None)
	    def __getfield_dunno4(self):

        try: self.__field_dunno4

        except:

            self.__field_dunno4=CSVINT(**{ 'default': 0, 'terminator': ord('\r') })

        return self.__field_dunno4.getvalue()

	def __setfield_dunno4(self, value):

        if isinstance(value,CSVINT):

            self.__field_dunno4=value

        else:

            self.__field_dunno4=CSVINT(value,**{ 'default': 0, 'terminator': ord('\r') })

	def __delfield_dunno4(self): del self.__field_dunno4

	    dunno4=property(__getfield_dunno4, __setfield_dunno4, __delfield_dunno4, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('file_name', self.__field_file_name, None)

        yield ('media_name', self.__field_media_name, None)

        yield ('data_len', self.__field_data_len, None)

        yield ('media_type', self.__field_media_type, None)

        yield ('dunno1', self.__field_dunno1, None)

        yield ('dunno2', self.__field_dunno2, None)

        yield ('dunno3', self.__field_dunno3, None)

        yield ('dunno4', self.__field_dunno4, None)


class  list_group_req (BaseProtogenClass) :
	__fields=['command', 'start_index', 'end_index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(list_group_req,self).__init__(**dict)

        if self.__class__ is list_group_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(list_group_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(list_group_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPGR=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        self.__field_start_index.writetobuffer(buf)

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPGR=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_start_index=CSVINT()

        self.__field_start_index.readfrombuffer(buf)

        self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPGR=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CPGR=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_start_index(self):

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        return self.__field_start_index.getvalue()

	def __setfield_start_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_start_index=value

        else:

            self.__field_start_index=CSVINT(value,)

	def __delfield_start_index(self): del self.__field_start_index

	    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
	    def __getfield_end_index(self):

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        return self.__field_end_index.getvalue()

	def __setfield_end_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_end_index=value

        else:

            self.__field_end_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_end_index(self): del self.__field_end_index

	    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('start_index', self.__field_start_index, None)

        yield ('end_index', self.__field_end_index, None)


class  list_group_resp (BaseProtogenClass) :
	__fields=['command', 'index', 'group_name']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(list_group_resp,self).__init__(**dict)

        if self.__class__ is list_group_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(list_group_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(list_group_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_group_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+CPGR:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_group_name=CSVSTRING(**{ 'terminator': None })

        self.__field_group_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+CPGR:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_group_name(self):

        return self.__field_group_name.getvalue()

	def __setfield_group_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_group_name=value

        else:

            self.__field_group_name=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_group_name(self): del self.__field_group_name

	    group_name=property(__getfield_group_name, __setfield_group_name, __delfield_group_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('group_name', self.__field_group_name, None)


class  charset_set_req (BaseProtogenClass) :
	__fields=['command', 'charset']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(charset_set_req,self).__init__(**dict)

        if self.__class__ is charset_set_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(charset_set_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(charset_set_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CSCS=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_charset

        except:

            self.__field_charset=CSVSTRING(**{ 'terminator': None })

        self.__field_charset.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CSCS=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_charset=CSVSTRING(**{ 'terminator': None })

        self.__field_charset.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CSCS=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CSCS=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_charset(self):

        try: self.__field_charset

        except:

            self.__field_charset=CSVSTRING(**{ 'terminator': None })

        return self.__field_charset.getvalue()

	def __setfield_charset(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_charset=value

        else:

            self.__field_charset=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_charset(self): del self.__field_charset

	    charset=property(__getfield_charset, __setfield_charset, __delfield_charset, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('charset', self.__field_charset, None)


class  select_storage_req (BaseProtogenClass) :
	__fields=['command', 'storage']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(select_storage_req,self).__init__(**dict)

        if self.__class__ is select_storage_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(select_storage_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(select_storage_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBS=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_storage

        except:

            self.__field_storage=CSVSTRING(**{ 'terminator': None })

        self.__field_storage.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBS=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_storage=CSVSTRING(**{ 'terminator': None })

        self.__field_storage.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBS=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CPBS=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_storage(self):

        try: self.__field_storage

        except:

            self.__field_storage=CSVSTRING(**{ 'terminator': None })

        return self.__field_storage.getvalue()

	def __setfield_storage(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_storage=value

        else:

            self.__field_storage=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_storage(self): del self.__field_storage

	    storage=property(__getfield_storage, __setfield_storage, __delfield_storage, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('storage', self.__field_storage, None)


class  select_storage_resp (BaseProtogenClass) :
	__fields=['command', 'storage', 'used_slots_count', 'total_slots_count', 'dunno']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(select_storage_resp,self).__init__(**dict)

        if self.__class__ is select_storage_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(select_storage_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(select_storage_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_storage.writetobuffer(buf)

        self.__field_used_slots_count.writetobuffer(buf)

        self.__field_total_slots_count.writetobuffer(buf)

        self.__field_dunno.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+CPBS:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_storage=CSVSTRING()

        self.__field_storage.readfrombuffer(buf)

        self.__field_used_slots_count=CSVINT()

        self.__field_used_slots_count.readfrombuffer(buf)

        self.__field_total_slots_count=CSVINT()

        self.__field_total_slots_count.readfrombuffer(buf)

        self.__field_dunno=CSVINT(**{ 'terminator': None })

        self.__field_dunno.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+CPBS:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_storage(self):

        return self.__field_storage.getvalue()

	def __setfield_storage(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_storage=value

        else:

            self.__field_storage=CSVSTRING(value,)

	def __delfield_storage(self): del self.__field_storage

	    storage=property(__getfield_storage, __setfield_storage, __delfield_storage, None)
	    def __getfield_used_slots_count(self):

        return self.__field_used_slots_count.getvalue()

	def __setfield_used_slots_count(self, value):

        if isinstance(value,CSVINT):

            self.__field_used_slots_count=value

        else:

            self.__field_used_slots_count=CSVINT(value,)

	def __delfield_used_slots_count(self): del self.__field_used_slots_count

	    used_slots_count=property(__getfield_used_slots_count, __setfield_used_slots_count, __delfield_used_slots_count, None)
	    def __getfield_total_slots_count(self):

        return self.__field_total_slots_count.getvalue()

	def __setfield_total_slots_count(self, value):

        if isinstance(value,CSVINT):

            self.__field_total_slots_count=value

        else:

            self.__field_total_slots_count=CSVINT(value,)

	def __delfield_total_slots_count(self): del self.__field_total_slots_count

	    total_slots_count=property(__getfield_total_slots_count, __setfield_total_slots_count, __delfield_total_slots_count, None)
	    def __getfield_dunno(self):

        return self.__field_dunno.getvalue()

	def __setfield_dunno(self, value):

        if isinstance(value,CSVINT):

            self.__field_dunno=value

        else:

            self.__field_dunno=CSVINT(value,**{ 'terminator': None })

	def __delfield_dunno(self): del self.__field_dunno

	    dunno=property(__getfield_dunno, __setfield_dunno, __delfield_dunno, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('storage', self.__field_storage, None)

        yield ('used_slots_count', self.__field_used_slots_count, None)

        yield ('total_slots_count', self.__field_total_slots_count, None)

        yield ('dunno', self.__field_dunno, None)


class  read_phonebook_req (BaseProtogenClass) :
	__fields=['command', 'start_index', 'end_index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(read_phonebook_req,self).__init__(**dict)

        if self.__class__ is read_phonebook_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(read_phonebook_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(read_phonebook_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBR=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        self.__field_start_index.writetobuffer(buf)

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBR=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_start_index=CSVINT()

        self.__field_start_index.readfrombuffer(buf)

        self.__field_end_index=CSVINT(**{ 'terminator': None })

        self.__field_end_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBR=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CPBR=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_start_index(self):

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT()

        return self.__field_start_index.getvalue()

	def __setfield_start_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_start_index=value

        else:

            self.__field_start_index=CSVINT(value,)

	def __delfield_start_index(self): del self.__field_start_index

	    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
	    def __getfield_end_index(self):

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None })

        return self.__field_end_index.getvalue()

	def __setfield_end_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_end_index=value

        else:

            self.__field_end_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_end_index(self): del self.__field_end_index

	    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('start_index', self.__field_start_index, None)

        yield ('end_index', self.__field_end_index, None)


class  read_phonebook_resp (BaseProtogenClass) :
	__fields=['sim', 'command', 'index', 'group', 'mobile', 'mobile_type', 'home', 'home_type', 'office', 'office_type', 'name', 'email', 'memo']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(read_phonebook_resp,self).__init__(**dict)

        if self.__class__ is read_phonebook_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(read_phonebook_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(read_phonebook_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

        if getattr(self, '__field_sim', None) is None:

            self.__field_sim=BOOL(**{ 'default': False })

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_group.writetobuffer(buf)

        self.__field_mobile.writetobuffer(buf)

        self.__field_mobile_type.writetobuffer(buf)

        self.__field_home.writetobuffer(buf)

        self.__field_home_type.writetobuffer(buf)

        self.__field_office.writetobuffer(buf)

        self.__field_office_type.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_email.writetobuffer(buf)

        self.__field_memo.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+CPBR:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_group=CSVINT()

        self.__field_group.readfrombuffer(buf)

        self.__field_mobile=CSVSTRING()

        self.__field_mobile.readfrombuffer(buf)

        self.__field_mobile_type=CSVINT()

        self.__field_mobile_type.readfrombuffer(buf)

        self.__field_home=CSVSTRING()

        self.__field_home.readfrombuffer(buf)

        self.__field_home_type=CSVINT()

        self.__field_home_type.readfrombuffer(buf)

        self.__field_office=CSVSTRING()

        self.__field_office.readfrombuffer(buf)

        self.__field_office_type=CSVINT()

        self.__field_office_type.readfrombuffer(buf)

        self.__field_name=CSVSTRING()

        self.__field_name.readfrombuffer(buf)

        self.__field_email=CSVSTRING()

        self.__field_email.readfrombuffer(buf)

        self.__field_memo=CSVSTRING()

        self.__field_memo.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_sim(self):

        try: self.__field_sim

        except:

            self.__field_sim=BOOL(**{ 'default': False })

        return self.__field_sim.getvalue()

	def __setfield_sim(self, value):

        if isinstance(value,BOOL):

            self.__field_sim=value

        else:

            self.__field_sim=BOOL(value,**{ 'default': False })

	def __delfield_sim(self): del self.__field_sim

	    sim=property(__getfield_sim, __setfield_sim, __delfield_sim, None)
	    def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+CPBR:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_group(self):

        return self.__field_group.getvalue()

	def __setfield_group(self, value):

        if isinstance(value,CSVINT):

            self.__field_group=value

        else:

            self.__field_group=CSVINT(value,)

	def __delfield_group(self): del self.__field_group

	    group=property(__getfield_group, __setfield_group, __delfield_group, None)
	    def __getfield_mobile(self):

        return self.__field_mobile.getvalue()

	def __setfield_mobile(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_mobile=value

        else:

            self.__field_mobile=CSVSTRING(value,)

	def __delfield_mobile(self): del self.__field_mobile

	    mobile=property(__getfield_mobile, __setfield_mobile, __delfield_mobile, None)
	    def __getfield_mobile_type(self):

        return self.__field_mobile_type.getvalue()

	def __setfield_mobile_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_mobile_type=value

        else:

            self.__field_mobile_type=CSVINT(value,)

	def __delfield_mobile_type(self): del self.__field_mobile_type

	    mobile_type=property(__getfield_mobile_type, __setfield_mobile_type, __delfield_mobile_type, None)
	    def __getfield_home(self):

        return self.__field_home.getvalue()

	def __setfield_home(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_home=value

        else:

            self.__field_home=CSVSTRING(value,)

	def __delfield_home(self): del self.__field_home

	    home=property(__getfield_home, __setfield_home, __delfield_home, None)
	    def __getfield_home_type(self):

        return self.__field_home_type.getvalue()

	def __setfield_home_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_home_type=value

        else:

            self.__field_home_type=CSVINT(value,)

	def __delfield_home_type(self): del self.__field_home_type

	    home_type=property(__getfield_home_type, __setfield_home_type, __delfield_home_type, None)
	    def __getfield_office(self):

        return self.__field_office.getvalue()

	def __setfield_office(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_office=value

        else:

            self.__field_office=CSVSTRING(value,)

	def __delfield_office(self): del self.__field_office

	    office=property(__getfield_office, __setfield_office, __delfield_office, None)
	    def __getfield_office_type(self):

        return self.__field_office_type.getvalue()

	def __setfield_office_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_office_type=value

        else:

            self.__field_office_type=CSVINT(value,)

	def __delfield_office_type(self): del self.__field_office_type

	    office_type=property(__getfield_office_type, __setfield_office_type, __delfield_office_type, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_name=value

        else:

            self.__field_name=CSVSTRING(value,)

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_email(self):

        return self.__field_email.getvalue()

	def __setfield_email(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_email=value

        else:

            self.__field_email=CSVSTRING(value,)

	def __delfield_email(self): del self.__field_email

	    email=property(__getfield_email, __setfield_email, __delfield_email, None)
	    def __getfield_memo(self):

        return self.__field_memo.getvalue()

	def __setfield_memo(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_memo=value

        else:

            self.__field_memo=CSVSTRING(value,)

	def __delfield_memo(self): del self.__field_memo

	    memo=property(__getfield_memo, __setfield_memo, __delfield_memo, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('sim', self.__field_sim, None)

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('group', self.__field_group, None)

        yield ('mobile', self.__field_mobile, None)

        yield ('mobile_type', self.__field_mobile_type, None)

        yield ('home', self.__field_home, None)

        yield ('home_type', self.__field_home_type, None)

        yield ('office', self.__field_office, None)

        yield ('office_type', self.__field_office_type, None)

        yield ('name', self.__field_name, None)

        yield ('email', self.__field_email, None)

        yield ('memo', self.__field_memo, None)


class  read_sim_phonebook_resp (BaseProtogenClass) :
	__fields=['command', 'index', 'group', 'mobile', 'mobile_type', 'name', 'home', 'office', 'email', 'memo', 'sim']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(read_sim_phonebook_resp,self).__init__(**dict)

        if self.__class__ is read_sim_phonebook_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(read_sim_phonebook_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(read_sim_phonebook_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

        if getattr(self, '__field_home', None) is None:

            self.__field_home=STRING(**{ 'terminator': None, 'default': '' })

        if getattr(self, '__field_office', None) is None:

            self.__field_office=STRING(**{ 'terminator': None, 'default': '' })

        if getattr(self, '__field_email', None) is None:

            self.__field_email=STRING(**{ 'terminator': None, 'default': '' })

        if getattr(self, '__field_memo', None) is None:

            self.__field_memo=STRING(**{ 'terminator': None, 'default': '' })

        if getattr(self, '__field_sim', None) is None:

            self.__field_sim=BOOL(**{ 'default': True })

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_group.writetobuffer(buf)

        self.__field_mobile.writetobuffer(buf)

        self.__field_mobile_type.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+CPBR:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_group=CSVINT()

        self.__field_group.readfrombuffer(buf)

        self.__field_mobile=CSVSTRING()

        self.__field_mobile.readfrombuffer(buf)

        self.__field_mobile_type=CSVINT()

        self.__field_mobile_type.readfrombuffer(buf)

        self.__field_name=CSVSTRING()

        self.__field_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+CPBR:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_group(self):

        return self.__field_group.getvalue()

	def __setfield_group(self, value):

        if isinstance(value,CSVINT):

            self.__field_group=value

        else:

            self.__field_group=CSVINT(value,)

	def __delfield_group(self): del self.__field_group

	    group=property(__getfield_group, __setfield_group, __delfield_group, None)
	    def __getfield_mobile(self):

        return self.__field_mobile.getvalue()

	def __setfield_mobile(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_mobile=value

        else:

            self.__field_mobile=CSVSTRING(value,)

	def __delfield_mobile(self): del self.__field_mobile

	    mobile=property(__getfield_mobile, __setfield_mobile, __delfield_mobile, None)
	    def __getfield_mobile_type(self):

        return self.__field_mobile_type.getvalue()

	def __setfield_mobile_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_mobile_type=value

        else:

            self.__field_mobile_type=CSVINT(value,)

	def __delfield_mobile_type(self): del self.__field_mobile_type

	    mobile_type=property(__getfield_mobile_type, __setfield_mobile_type, __delfield_mobile_type, None)
	    def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_name=value

        else:

            self.__field_name=CSVSTRING(value,)

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_home(self):

        try: self.__field_home

        except:

            self.__field_home=STRING(**{ 'terminator': None, 'default': '' })

        return self.__field_home.getvalue()

	def __setfield_home(self, value):

        if isinstance(value,STRING):

            self.__field_home=value

        else:

            self.__field_home=STRING(value,**{ 'terminator': None, 'default': '' })

	def __delfield_home(self): del self.__field_home

	    home=property(__getfield_home, __setfield_home, __delfield_home, None)
	    def __getfield_office(self):

        try: self.__field_office

        except:

            self.__field_office=STRING(**{ 'terminator': None, 'default': '' })

        return self.__field_office.getvalue()

	def __setfield_office(self, value):

        if isinstance(value,STRING):

            self.__field_office=value

        else:

            self.__field_office=STRING(value,**{ 'terminator': None, 'default': '' })

	def __delfield_office(self): del self.__field_office

	    office=property(__getfield_office, __setfield_office, __delfield_office, None)
	    def __getfield_email(self):

        try: self.__field_email

        except:

            self.__field_email=STRING(**{ 'terminator': None, 'default': '' })

        return self.__field_email.getvalue()

	def __setfield_email(self, value):

        if isinstance(value,STRING):

            self.__field_email=value

        else:

            self.__field_email=STRING(value,**{ 'terminator': None, 'default': '' })

	def __delfield_email(self): del self.__field_email

	    email=property(__getfield_email, __setfield_email, __delfield_email, None)
	    def __getfield_memo(self):

        try: self.__field_memo

        except:

            self.__field_memo=STRING(**{ 'terminator': None, 'default': '' })

        return self.__field_memo.getvalue()

	def __setfield_memo(self, value):

        if isinstance(value,STRING):

            self.__field_memo=value

        else:

            self.__field_memo=STRING(value,**{ 'terminator': None, 'default': '' })

	def __delfield_memo(self): del self.__field_memo

	    memo=property(__getfield_memo, __setfield_memo, __delfield_memo, None)
	    def __getfield_sim(self):

        try: self.__field_sim

        except:

            self.__field_sim=BOOL(**{ 'default': True })

        return self.__field_sim.getvalue()

	def __setfield_sim(self, value):

        if isinstance(value,BOOL):

            self.__field_sim=value

        else:

            self.__field_sim=BOOL(value,**{ 'default': True })

	def __delfield_sim(self): del self.__field_sim

	    sim=property(__getfield_sim, __setfield_sim, __delfield_sim, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('group', self.__field_group, None)

        yield ('mobile', self.__field_mobile, None)

        yield ('mobile_type', self.__field_mobile_type, None)

        yield ('name', self.__field_name, None)

        yield ('home', self.__field_home, None)

        yield ('office', self.__field_office, None)

        yield ('email', self.__field_email, None)

        yield ('memo', self.__field_memo, None)

        yield ('sim', self.__field_sim, None)


class  del_phonebook_req (BaseProtogenClass) :
	__fields=['command', 'index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(del_phonebook_req,self).__init__(**dict)

        if self.__class__ is del_phonebook_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(del_phonebook_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(del_phonebook_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_index

        except:

            self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CPBW=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        try: self.__field_index

        except:

            self.__field_index=CSVINT(**{ 'terminator': None })

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)


class  update_phonebook_resp (BaseProtogenClass) :
	__fields=['command', 'index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(update_phonebook_resp,self).__init__(**dict)

        if self.__class__ is update_phonebook_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(update_phonebook_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(update_phonebook_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '), 'constant': '+CPBW:' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '), 'constant': '+CPBW:' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)


class  write_phonebook_req (BaseProtogenClass) :
	__fields=['command', 'group', 'mobile', 'mobile_type', 'home', 'home_type', 'office', 'office_type', 'name', 'email', 'memo']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(write_phonebook_req,self).__init__(**dict)

        if self.__class__ is write_phonebook_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(write_phonebook_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(write_phonebook_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=,' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_group

        except:

            self.__field_group=CSVINT()

        self.__field_group.writetobuffer(buf)

        try: self.__field_mobile

        except:

            self.__field_mobile=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_mobile.writetobuffer(buf)

        try: self.__field_mobile_type

        except:

            self.__field_mobile_type=CSVINT(**{ 'default': 255 })

        self.__field_mobile_type.writetobuffer(buf)

        try: self.__field_home

        except:

            self.__field_home=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_home.writetobuffer(buf)

        try: self.__field_home_type

        except:

            self.__field_home_type=CSVINT(**{ 'default': 255 })

        self.__field_home_type.writetobuffer(buf)

        try: self.__field_office

        except:

            self.__field_office=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_office.writetobuffer(buf)

        try: self.__field_office_type

        except:

            self.__field_office_type=CSVINT(**{ 'default': 255 })

        self.__field_office_type.writetobuffer(buf)

        try: self.__field_name

        except:

            self.__field_name=CSVSTRING(**{ 'maxsizeinbytes': PB_NAME_LEN,                  'raiseontruncate': False })

        self.__field_name.writetobuffer(buf)

        try: self.__field_email

        except:

            self.__field_email=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_EMAIL_LEN,                  'raiseontruncate': False })

        self.__field_email.writetobuffer(buf)

        try: self.__field_memo

        except:

            self.__field_memo=CSVSTRING(**{ 'terminator': None, 'default': '',                  'maxsizeinbytes': PB_MEMO_LEN,                  'raiseontruncate': False })

        self.__field_memo.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=,' })

        self.__field_command.readfrombuffer(buf)

        self.__field_group=CSVINT()

        self.__field_group.readfrombuffer(buf)

        self.__field_mobile=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_mobile.readfrombuffer(buf)

        self.__field_mobile_type=CSVINT(**{ 'default': 255 })

        self.__field_mobile_type.readfrombuffer(buf)

        self.__field_home=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_home.readfrombuffer(buf)

        self.__field_home_type=CSVINT(**{ 'default': 255 })

        self.__field_home_type.readfrombuffer(buf)

        self.__field_office=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_office.readfrombuffer(buf)

        self.__field_office_type=CSVINT(**{ 'default': 255 })

        self.__field_office_type.readfrombuffer(buf)

        self.__field_name=CSVSTRING(**{ 'maxsizeinbytes': PB_NAME_LEN,                  'raiseontruncate': False })

        self.__field_name.readfrombuffer(buf)

        self.__field_email=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_EMAIL_LEN,                  'raiseontruncate': False })

        self.__field_email.readfrombuffer(buf)

        self.__field_memo=CSVSTRING(**{ 'terminator': None, 'default': '',                  'maxsizeinbytes': PB_MEMO_LEN,                  'raiseontruncate': False })

        self.__field_memo.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=,' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CPBW=,' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_group(self):

        try: self.__field_group

        except:

            self.__field_group=CSVINT()

        return self.__field_group.getvalue()

	def __setfield_group(self, value):

        if isinstance(value,CSVINT):

            self.__field_group=value

        else:

            self.__field_group=CSVINT(value,)

	def __delfield_group(self): del self.__field_group

	    group=property(__getfield_group, __setfield_group, __delfield_group, None)
	    def __getfield_mobile(self):

        try: self.__field_mobile

        except:

            self.__field_mobile=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        return self.__field_mobile.getvalue()

	def __setfield_mobile(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_mobile=value

        else:

            self.__field_mobile=CSVSTRING(value,**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

	def __delfield_mobile(self): del self.__field_mobile

	    mobile=property(__getfield_mobile, __setfield_mobile, __delfield_mobile, None)
	    def __getfield_mobile_type(self):

        try: self.__field_mobile_type

        except:

            self.__field_mobile_type=CSVINT(**{ 'default': 255 })

        return self.__field_mobile_type.getvalue()

	def __setfield_mobile_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_mobile_type=value

        else:

            self.__field_mobile_type=CSVINT(value,**{ 'default': 255 })

	def __delfield_mobile_type(self): del self.__field_mobile_type

	    mobile_type=property(__getfield_mobile_type, __setfield_mobile_type, __delfield_mobile_type, None)
	    def __getfield_home(self):

        try: self.__field_home

        except:

            self.__field_home=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        return self.__field_home.getvalue()

	def __setfield_home(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_home=value

        else:

            self.__field_home=CSVSTRING(value,**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

	def __delfield_home(self): del self.__field_home

	    home=property(__getfield_home, __setfield_home, __delfield_home, None)
	    def __getfield_home_type(self):

        try: self.__field_home_type

        except:

            self.__field_home_type=CSVINT(**{ 'default': 255 })

        return self.__field_home_type.getvalue()

	def __setfield_home_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_home_type=value

        else:

            self.__field_home_type=CSVINT(value,**{ 'default': 255 })

	def __delfield_home_type(self): del self.__field_home_type

	    home_type=property(__getfield_home_type, __setfield_home_type, __delfield_home_type, None)
	    def __getfield_office(self):

        try: self.__field_office

        except:

            self.__field_office=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        return self.__field_office.getvalue()

	def __setfield_office(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_office=value

        else:

            self.__field_office=CSVSTRING(value,**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

	def __delfield_office(self): del self.__field_office

	    office=property(__getfield_office, __setfield_office, __delfield_office, None)
	    def __getfield_office_type(self):

        try: self.__field_office_type

        except:

            self.__field_office_type=CSVINT(**{ 'default': 255 })

        return self.__field_office_type.getvalue()

	def __setfield_office_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_office_type=value

        else:

            self.__field_office_type=CSVINT(value,**{ 'default': 255 })

	def __delfield_office_type(self): del self.__field_office_type

	    office_type=property(__getfield_office_type, __setfield_office_type, __delfield_office_type, None)
	    def __getfield_name(self):

        try: self.__field_name

        except:

            self.__field_name=CSVSTRING(**{ 'maxsizeinbytes': PB_NAME_LEN,                  'raiseontruncate': False })

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_name=value

        else:

            self.__field_name=CSVSTRING(value,**{ 'maxsizeinbytes': PB_NAME_LEN,                  'raiseontruncate': False })

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_email(self):

        try: self.__field_email

        except:

            self.__field_email=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_EMAIL_LEN,                  'raiseontruncate': False })

        return self.__field_email.getvalue()

	def __setfield_email(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_email=value

        else:

            self.__field_email=CSVSTRING(value,**{ 'default': '',                  'maxsizeinbytes': PB_EMAIL_LEN,                  'raiseontruncate': False })

	def __delfield_email(self): del self.__field_email

	    email=property(__getfield_email, __setfield_email, __delfield_email, None)
	    def __getfield_memo(self):

        try: self.__field_memo

        except:

            self.__field_memo=CSVSTRING(**{ 'terminator': None, 'default': '',                  'maxsizeinbytes': PB_MEMO_LEN,                  'raiseontruncate': False })

        return self.__field_memo.getvalue()

	def __setfield_memo(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_memo=value

        else:

            self.__field_memo=CSVSTRING(value,**{ 'terminator': None, 'default': '',                  'maxsizeinbytes': PB_MEMO_LEN,                  'raiseontruncate': False })

	def __delfield_memo(self): del self.__field_memo

	    memo=property(__getfield_memo, __setfield_memo, __delfield_memo, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('group', self.__field_group, None)

        yield ('mobile', self.__field_mobile, None)

        yield ('mobile_type', self.__field_mobile_type, None)

        yield ('home', self.__field_home, None)

        yield ('home_type', self.__field_home_type, None)

        yield ('office', self.__field_office, None)

        yield ('office_type', self.__field_office_type, None)

        yield ('name', self.__field_name, None)

        yield ('email', self.__field_email, None)

        yield ('memo', self.__field_memo, None)


class  write_sim_phonebook_req (BaseProtogenClass) :
	__fields=['command', 'group', 'number', 'number_type', 'name']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(write_sim_phonebook_req,self).__init__(**dict)

        if self.__class__ is write_sim_phonebook_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(write_sim_phonebook_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(write_sim_phonebook_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=,' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_group

        except:

            self.__field_group=CSVINT(**{ 'default': 0 })

        self.__field_group.writetobuffer(buf)

        try: self.__field_number

        except:

            self.__field_number=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_number.writetobuffer(buf)

        try: self.__field_number_type

        except:

            self.__field_number_type=CSVINT(**{ 'default': 255 })

        self.__field_number_type.writetobuffer(buf)

        try: self.__field_name

        except:

            self.__field_name=CSVSTRING(**{ 'terminator': None,                  'maxsizeinbytes': PB_SIM_NAME_LEN,                  'raiseontruncate': False })

        self.__field_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=,' })

        self.__field_command.readfrombuffer(buf)

        self.__field_group=CSVINT(**{ 'default': 0 })

        self.__field_group.readfrombuffer(buf)

        self.__field_number=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        self.__field_number.readfrombuffer(buf)

        self.__field_number_type=CSVINT(**{ 'default': 255 })

        self.__field_number_type.readfrombuffer(buf)

        self.__field_name=CSVSTRING(**{ 'terminator': None,                  'maxsizeinbytes': PB_SIM_NAME_LEN,                  'raiseontruncate': False })

        self.__field_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CPBW=,' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CPBW=,' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_group(self):

        try: self.__field_group

        except:

            self.__field_group=CSVINT(**{ 'default': 0 })

        return self.__field_group.getvalue()

	def __setfield_group(self, value):

        if isinstance(value,CSVINT):

            self.__field_group=value

        else:

            self.__field_group=CSVINT(value,**{ 'default': 0 })

	def __delfield_group(self): del self.__field_group

	    group=property(__getfield_group, __setfield_group, __delfield_group, None)
	    def __getfield_number(self):

        try: self.__field_number

        except:

            self.__field_number=CSVSTRING(**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

        return self.__field_number.getvalue()

	def __setfield_number(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_number=value

        else:

            self.__field_number=CSVSTRING(value,**{ 'default': '',                  'maxsizeinbytes': PB_NUMBER_LEN,                  'raiseontruncate': False })

	def __delfield_number(self): del self.__field_number

	    number=property(__getfield_number, __setfield_number, __delfield_number, None)
	    def __getfield_number_type(self):

        try: self.__field_number_type

        except:

            self.__field_number_type=CSVINT(**{ 'default': 255 })

        return self.__field_number_type.getvalue()

	def __setfield_number_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_number_type=value

        else:

            self.__field_number_type=CSVINT(value,**{ 'default': 255 })

	def __delfield_number_type(self): del self.__field_number_type

	    number_type=property(__getfield_number_type, __setfield_number_type, __delfield_number_type, None)
	    def __getfield_name(self):

        try: self.__field_name

        except:

            self.__field_name=CSVSTRING(**{ 'terminator': None,                  'maxsizeinbytes': PB_SIM_NAME_LEN,                  'raiseontruncate': False })

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_name=value

        else:

            self.__field_name=CSVSTRING(value,**{ 'terminator': None,                  'maxsizeinbytes': PB_SIM_NAME_LEN,                  'raiseontruncate': False })

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('group', self.__field_group, None)

        yield ('number', self.__field_number, None)

        yield ('number_type', self.__field_number_type, None)

        yield ('name', self.__field_name, None)


class  memo_read_req (BaseProtogenClass) :
	__fields=['command', 'start_index', 'end_index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(memo_read_req,self).__init__(**dict)

        if self.__class__ is memo_read_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(memo_read_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(memo_read_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_READ_CMD+'=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT(**{ 'default': MEMO_MIN_INDEX })

        self.__field_start_index.writetobuffer(buf)

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': MEMO_MAX_INDEX })

        self.__field_end_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_READ_CMD+'=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_start_index=CSVINT(**{ 'default': MEMO_MIN_INDEX })

        self.__field_start_index.readfrombuffer(buf)

        self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': MEMO_MAX_INDEX })

        self.__field_end_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_READ_CMD+'=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None,               'default': MEMO_READ_CMD+'=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_start_index(self):

        try: self.__field_start_index

        except:

            self.__field_start_index=CSVINT(**{ 'default': MEMO_MIN_INDEX })

        return self.__field_start_index.getvalue()

	def __setfield_start_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_start_index=value

        else:

            self.__field_start_index=CSVINT(value,**{ 'default': MEMO_MIN_INDEX })

	def __delfield_start_index(self): del self.__field_start_index

	    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
	    def __getfield_end_index(self):

        try: self.__field_end_index

        except:

            self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': MEMO_MAX_INDEX })

        return self.__field_end_index.getvalue()

	def __setfield_end_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_end_index=value

        else:

            self.__field_end_index=CSVINT(value,**{ 'terminator': None,               'default': MEMO_MAX_INDEX })

	def __delfield_end_index(self): del self.__field_end_index

	    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('start_index', self.__field_start_index, None)

        yield ('end_index', self.__field_end_index, None)


class  memo_read_resp (BaseProtogenClass) :
	__fields=['command', 'index', 'text']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(memo_read_resp,self).__init__(**dict)

        if self.__class__ is memo_read_resp:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(memo_read_resp,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(memo_read_resp,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_text.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '),               'constant': MEMO_READ_CMD+':' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_text=CSVSTRING(**{ 'terminator': None })

        self.__field_text.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '),               'constant': MEMO_READ_CMD+':' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_text(self):

        return self.__field_text.getvalue()

	def __setfield_text(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_text=value

        else:

            self.__field_text=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_text(self): del self.__field_text

	    text=property(__getfield_text, __setfield_text, __delfield_text, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('text', self.__field_text, None)


class  memo_write_req (BaseProtogenClass) :
	__fields=['command', 'text']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(memo_write_req,self).__init__(**dict)

        if self.__class__ is memo_write_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(memo_write_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(memo_write_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=,' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_text

        except:

            self.__field_text=CSVSTRING(**{ 'terminator': None })

        self.__field_text.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=,' })

        self.__field_command.readfrombuffer(buf)

        self.__field_text=CSVSTRING(**{ 'terminator': None })

        self.__field_text.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=,' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=,' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_text(self):

        try: self.__field_text

        except:

            self.__field_text=CSVSTRING(**{ 'terminator': None })

        return self.__field_text.getvalue()

	def __setfield_text(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_text=value

        else:

            self.__field_text=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_text(self): del self.__field_text

	    text=property(__getfield_text, __setfield_text, __delfield_text, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('text', self.__field_text, None)


class  memo_del_req (BaseProtogenClass) :
	__fields=['command', 'index']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(memo_del_req,self).__init__(**dict)

        if self.__class__ is memo_del_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(memo_del_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(memo_del_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_index

        except:

            self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT(**{ 'terminator': None })

        self.__field_index.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None,               'default': MEMO_WRITE_CMD+'=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        try: self.__field_index

        except:

            self.__field_index=CSVINT(**{ 'terminator': None })

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,**{ 'terminator': None })

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)


class  sms_format_req (BaseProtogenClass) :
	__fields=['command', 'format']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_format_req,self).__init__(**dict)

        if self.__class__ is sms_format_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_format_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_format_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_FORMAT_CMD+'=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_format

        except:

            self.__field_format=CSVINT(**{ 'terminator': None,               'default': SMS_FORMAT_TEXT })

        self.__field_format.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_FORMAT_CMD+'=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_format=CSVINT(**{ 'terminator': None,               'default': SMS_FORMAT_TEXT })

        self.__field_format.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_FORMAT_CMD+'=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None,               'default': SMS_FORMAT_CMD+'=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_format(self):

        try: self.__field_format

        except:

            self.__field_format=CSVINT(**{ 'terminator': None,               'default': SMS_FORMAT_TEXT })

        return self.__field_format.getvalue()

	def __setfield_format(self, value):

        if isinstance(value,CSVINT):

            self.__field_format=value

        else:

            self.__field_format=CSVINT(value,**{ 'terminator': None,               'default': SMS_FORMAT_TEXT })

	def __delfield_format(self): del self.__field_format

	    format=property(__getfield_format, __setfield_format, __delfield_format, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('format', self.__field_format, None)


class  sms_memory_select_req (BaseProtogenClass) :
	__fields=['command', 'list_memory']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_memory_select_req,self).__init__(**dict)

        if self.__class__ is sms_memory_select_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_memory_select_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_memory_select_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_MEMORY_SELECT_CMD+'=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_list_memory

        except:

            self.__field_list_memory=CSVSTRING(**{ 'terminator': None })

        self.__field_list_memory.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_MEMORY_SELECT_CMD+'=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_list_memory=CSVSTRING(**{ 'terminator': None })

        self.__field_list_memory.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_MEMORY_SELECT_CMD+'=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None,               'default': SMS_MEMORY_SELECT_CMD+'=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_list_memory(self):

        try: self.__field_list_memory

        except:

            self.__field_list_memory=CSVSTRING(**{ 'terminator': None })

        return self.__field_list_memory.getvalue()

	def __setfield_list_memory(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_list_memory=value

        else:

            self.__field_list_memory=CSVSTRING(value,**{ 'terminator': None })

	def __delfield_list_memory(self): del self.__field_list_memory

	    list_memory=property(__getfield_list_memory, __setfield_list_memory, __delfield_list_memory, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('list_memory', self.__field_list_memory, None)


class  sms_msg_list_req (BaseProtogenClass) :
	__fields=['command', 'msg_type']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_msg_list_req,self).__init__(**dict)

        if self.__class__ is sms_msg_list_req:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_msg_list_req,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_msg_list_req,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_MSG_LIST_CMD+'=' })

        self.__field_command.writetobuffer(buf)

        try: self.__field_msg_type

        except:

            self.__field_msg_type=CSVSTRING(**{ 'terminator': None,                  'default': SMS_MSG_ALL })

        self.__field_msg_type.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_MSG_LIST_CMD+'=' })

        self.__field_command.readfrombuffer(buf)

        self.__field_msg_type=CSVSTRING(**{ 'terminator': None,                  'default': SMS_MSG_ALL })

        self.__field_msg_type.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        try: self.__field_command

        except:

            self.__field_command=STRING(**{ 'terminator': None,               'default': SMS_MSG_LIST_CMD+'=' })

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': None,               'default': SMS_MSG_LIST_CMD+'=' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_msg_type(self):

        try: self.__field_msg_type

        except:

            self.__field_msg_type=CSVSTRING(**{ 'terminator': None,                  'default': SMS_MSG_ALL })

        return self.__field_msg_type.getvalue()

	def __setfield_msg_type(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_msg_type=value

        else:

            self.__field_msg_type=CSVSTRING(value,**{ 'terminator': None,                  'default': SMS_MSG_ALL })

	def __delfield_msg_type(self): del self.__field_msg_type

	    msg_type=property(__getfield_msg_type, __setfield_msg_type, __delfield_msg_type, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('msg_type', self.__field_msg_type, None)


class  sms_msg_list_header (BaseProtogenClass) :
	__fields=['command', 'index', 'msg_type', 'address', 'address_name', 'timestamp', 'address_type', 'data_len']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(sms_msg_list_header,self).__init__(**dict)

        if self.__class__ is sms_msg_list_header:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(sms_msg_list_header,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(sms_msg_list_header,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command.writetobuffer(buf)

        self.__field_index.writetobuffer(buf)

        self.__field_msg_type.writetobuffer(buf)

        self.__field_address.writetobuffer(buf)

        self.__field_address_name.writetobuffer(buf)

        self.__field_timestamp.writetobuffer(buf)

        self.__field_address_type.writetobuffer(buf)

        self.__field_data_len.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_command=STRING(**{ 'terminator': ord(' '),               'constant': SMS_MSG_LIST_CMD+':' })

        self.__field_command.readfrombuffer(buf)

        self.__field_index=CSVINT()

        self.__field_index.readfrombuffer(buf)

        self.__field_msg_type=CSVSTRING()

        self.__field_msg_type.readfrombuffer(buf)

        self.__field_address=CSVSTRING()

        self.__field_address.readfrombuffer(buf)

        self.__field_address_name=CSVSTRING()

        self.__field_address_name.readfrombuffer(buf)

        self.__field_timestamp=SMSDATETIME()

        self.__field_timestamp.readfrombuffer(buf)

        self.__field_address_type=CSVINT()

        self.__field_address_type.readfrombuffer(buf)

        self.__field_data_len=CSVINT(**{ 'terminator': None })

        self.__field_data_len.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_command(self):

        return self.__field_command.getvalue()

	def __setfield_command(self, value):

        if isinstance(value,STRING):

            self.__field_command=value

        else:

            self.__field_command=STRING(value,**{ 'terminator': ord(' '),               'constant': SMS_MSG_LIST_CMD+':' })

	def __delfield_command(self): del self.__field_command

	    command=property(__getfield_command, __setfield_command, __delfield_command, None)
	    def __getfield_index(self):

        return self.__field_index.getvalue()

	def __setfield_index(self, value):

        if isinstance(value,CSVINT):

            self.__field_index=value

        else:

            self.__field_index=CSVINT(value,)

	def __delfield_index(self): del self.__field_index

	    index=property(__getfield_index, __setfield_index, __delfield_index, None)
	    def __getfield_msg_type(self):

        return self.__field_msg_type.getvalue()

	def __setfield_msg_type(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_msg_type=value

        else:

            self.__field_msg_type=CSVSTRING(value,)

	def __delfield_msg_type(self): del self.__field_msg_type

	    msg_type=property(__getfield_msg_type, __setfield_msg_type, __delfield_msg_type, None)
	    def __getfield_address(self):

        return self.__field_address.getvalue()

	def __setfield_address(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_address=value

        else:

            self.__field_address=CSVSTRING(value,)

	def __delfield_address(self): del self.__field_address

	    address=property(__getfield_address, __setfield_address, __delfield_address, None)
	    def __getfield_address_name(self):

        return self.__field_address_name.getvalue()

	def __setfield_address_name(self, value):

        if isinstance(value,CSVSTRING):

            self.__field_address_name=value

        else:

            self.__field_address_name=CSVSTRING(value,)

	def __delfield_address_name(self): del self.__field_address_name

	    address_name=property(__getfield_address_name, __setfield_address_name, __delfield_address_name, None)
	    def __getfield_timestamp(self):

        return self.__field_timestamp.getvalue()

	def __setfield_timestamp(self, value):

        if isinstance(value,SMSDATETIME):

            self.__field_timestamp=value

        else:

            self.__field_timestamp=SMSDATETIME(value,)

	def __delfield_timestamp(self): del self.__field_timestamp

	    timestamp=property(__getfield_timestamp, __setfield_timestamp, __delfield_timestamp, None)
	    def __getfield_address_type(self):

        return self.__field_address_type.getvalue()

	def __setfield_address_type(self, value):

        if isinstance(value,CSVINT):

            self.__field_address_type=value

        else:

            self.__field_address_type=CSVINT(value,)

	def __delfield_address_type(self): del self.__field_address_type

	    address_type=property(__getfield_address_type, __setfield_address_type, __delfield_address_type, None)
	    def __getfield_data_len(self):

        return self.__field_data_len.getvalue()

	def __setfield_data_len(self, value):

        if isinstance(value,CSVINT):

            self.__field_data_len=value

        else:

            self.__field_data_len=CSVINT(value,**{ 'terminator': None })

	def __delfield_data_len(self): del self.__field_data_len

	    data_len=property(__getfield_data_len, __setfield_data_len, __delfield_data_len, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('index', self.__field_index, None)

        yield ('msg_type', self.__field_msg_type, None)

        yield ('address', self.__field_address, None)

        yield ('address_name', self.__field_address_name, None)

        yield ('timestamp', self.__field_timestamp, None)

        yield ('address_type', self.__field_address_type, None)

        yield ('data_len', self.__field_data_len, None)


