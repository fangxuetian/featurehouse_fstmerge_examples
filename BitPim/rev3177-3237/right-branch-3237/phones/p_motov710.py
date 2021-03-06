"""Various descriptions of data specific to Motorola phones"""
from prototypes import *
from prototypes_moto import *
from p_gsm import *
from p_moto import *
UINT=UINTlsb
BOOL=BOOLlsb
PB_TOTAL_GROUP=30
PB_GROUP_RANGE=xrange(1, PB_TOTAL_GROUP+1)
PB_GROUP_NAME_LEN=24
RT_BUILTIN=0x0C
RT_CUSTOM=0x0D
RT_INDEX_FILE='/MyToneDB.db'
RT_PATH='motorola/shared/audio'
WP_PATH='motorola/shared/picture'
CAL_TOTAL_ENTRIES=500
CAL_MAX_ENTRY=499
CAL_TOTAL_ENTRY_EXCEPTIONS=8
CAL_TITLE_LEN=64
CAL_REP_NONE=0
CAL_REP_DAILY=1
CAL_REP_WEEKLY=2
CAL_REP_MONTHLY=3
CAL_REP_MONTHLY_NTH=4
CAL_REP_YEARLY=5
CAL_ALARM_NOTIME='00:00'
CAL_ALARM_NODATE='00-00-2000'
class read_group_req(BaseProtogenClass):
    __fields=['command', 'start_index', 'end_index']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(read_group_req,self).__init__(**dict)
        if self.__class__ is read_group_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(read_group_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(read_group_req,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGR=' })
        self.__field_command.writetobuffer(buf)
        try: self.__field_start_index
        except:
            self.__field_start_index=CSVINT(**{ 'default': 1 })
        self.__field_start_index.writetobuffer(buf)
        try: self.__field_end_index
        except:
            self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': PB_TOTAL_GROUP })
        self.__field_end_index.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGR=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_start_index=CSVINT(**{ 'default': 1 })
        self.__field_start_index.readfrombuffer(buf)
        self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': PB_TOTAL_GROUP })
        self.__field_end_index.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGR=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGR=' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_start_index(self):
        try: self.__field_start_index
        except:
            self.__field_start_index=CSVINT(**{ 'default': 1 })
        return self.__field_start_index.getvalue()
    def __setfield_start_index(self, value):
        if isinstance(value,CSVINT):
            self.__field_start_index=value
        else:
            self.__field_start_index=CSVINT(value,**{ 'default': 1 })
    def __delfield_start_index(self): del self.__field_start_index
    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
    def __getfield_end_index(self):
        try: self.__field_end_index
        except:
            self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': PB_TOTAL_GROUP })
        return self.__field_end_index.getvalue()
    def __setfield_end_index(self, value):
        if isinstance(value,CSVINT):
            self.__field_end_index=value
        else:
            self.__field_end_index=CSVINT(value,**{ 'terminator': None,               'default': PB_TOTAL_GROUP })
    def __delfield_end_index(self): del self.__field_end_index
    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('start_index', self.__field_start_index, None)
        yield ('end_index', self.__field_end_index, None)
class read_group_resp(BaseProtogenClass):
    __fields=['command', 'index', 'name', 'ringtone', 'dunno']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(read_group_resp,self).__init__(**dict)
        if self.__class__ is read_group_resp:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(read_group_resp,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(read_group_resp,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        self.__field_ringtone.writetobuffer(buf)
        self.__field_dunno.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None, 'terminator': ord(' '),                  'default': '+MPGR:' })
        self.__field_command.readfrombuffer(buf)
        self.__field_index=CSVINT()
        self.__field_index.readfrombuffer(buf)
        self.__field_name=CSVSTRING()
        self.__field_name.readfrombuffer(buf)
        self.__field_ringtone=CSVINT()
        self.__field_ringtone.readfrombuffer(buf)
        self.__field_dunno=DATA()
        self.__field_dunno.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None, 'terminator': ord(' '),                  'default': '+MPGR:' })
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
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_name=value
        else:
            self.__field_name=CSVSTRING(value,)
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_ringtone(self):
        return self.__field_ringtone.getvalue()
    def __setfield_ringtone(self, value):
        if isinstance(value,CSVINT):
            self.__field_ringtone=value
        else:
            self.__field_ringtone=CSVINT(value,)
    def __delfield_ringtone(self): del self.__field_ringtone
    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
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
        yield ('index', self.__field_index, None)
        yield ('name', self.__field_name, None)
        yield ('ringtone', self.__field_ringtone, None)
        yield ('dunno', self.__field_dunno, None)
class del_group_req(BaseProtogenClass):
    __fields=['command', 'index']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(del_group_req,self).__init__(**dict)
        if self.__class__ is del_group_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(del_group_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(del_group_req,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
        self.__field_command.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_index=CSVINT(**{ 'terminator': None })
        self.__field_index.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
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
class write_group_req(BaseProtogenClass):
    __fields=['command', 'index', 'name', 'ringtone']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(write_group_req,self).__init__(**dict)
        if self.__class__ is write_group_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(write_group_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(write_group_req,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
        self.__field_command.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        try: self.__field_ringtone
        except:
            self.__field_ringtone=CSVINT(**{ 'terminator': None, 'default': 255 })
        self.__field_ringtone.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_index=CSVINT()
        self.__field_index.readfrombuffer(buf)
        self.__field_name=CSVSTRING(**{ 'maxsizeinbytes': PB_GROUP_NAME_LEN,                  'raiseontruncate': False })
        self.__field_name.readfrombuffer(buf)
        self.__field_ringtone=CSVINT(**{ 'terminator': None, 'default': 255 })
        self.__field_ringtone.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None, 'default': '+MPGW=' })
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
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_name=value
        else:
            self.__field_name=CSVSTRING(value,**{ 'maxsizeinbytes': PB_GROUP_NAME_LEN,                  'raiseontruncate': False })
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_ringtone(self):
        try: self.__field_ringtone
        except:
            self.__field_ringtone=CSVINT(**{ 'terminator': None, 'default': 255 })
        return self.__field_ringtone.getvalue()
    def __setfield_ringtone(self, value):
        if isinstance(value,CSVINT):
            self.__field_ringtone=value
        else:
            self.__field_ringtone=CSVINT(value,**{ 'terminator': None, 'default': 255 })
    def __delfield_ringtone(self): del self.__field_ringtone
    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('index', self.__field_index, None)
        yield ('name', self.__field_name, None)
        yield ('ringtone', self.__field_ringtone, None)
class ringtone_index_entry(BaseProtogenClass):
    __fields=['read_mode', 'name', 'name', 'index', 'ringtone_type', 'dunno']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(ringtone_index_entry,self).__init__(**dict)
        if self.__class__ is ringtone_index_entry:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(ringtone_index_entry,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(ringtone_index_entry,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
        if getattr(self, '__field_read_mode', None) is None:
            self.__field_read_mode=BOOL(**{ 'default': True })
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if self.read_mode:
            self.__field_name.writetobuffer(buf)
        if not self.read_mode:
            self.__field_name.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_ringtone_type.writetobuffer(buf)
        try: self.__field_dunno
        except:
            self.__field_dunno=DATA(**{'sizeinbytes': 6,  'default': '' })
        self.__field_dunno.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        if self.read_mode:
            self.__field_name=DATA(**{'sizeinbytes': 508,  'pad': None })
            self.__field_name.readfrombuffer(buf)
        if not self.read_mode:
            self.__field_name=DATA(**{'sizeinbytes': 508})
            self.__field_name.readfrombuffer(buf)
        self.__field_index=UINT(**{'sizeinbytes': 1})
        self.__field_index.readfrombuffer(buf)
        self.__field_ringtone_type=UINT(**{'sizeinbytes': 1})
        self.__field_ringtone_type.readfrombuffer(buf)
        self.__field_dunno=DATA(**{'sizeinbytes': 6,  'default': '' })
        self.__field_dunno.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_read_mode(self):
        try: self.__field_read_mode
        except:
            self.__field_read_mode=BOOL(**{ 'default': True })
        return self.__field_read_mode.getvalue()
    def __setfield_read_mode(self, value):
        if isinstance(value,BOOL):
            self.__field_read_mode=value
        else:
            self.__field_read_mode=BOOL(value,**{ 'default': True })
    def __delfield_read_mode(self): del self.__field_read_mode
    read_mode=property(__getfield_read_mode, __setfield_read_mode, __delfield_read_mode, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,DATA):
            self.__field_name=value
        else:
            self.__field_name=DATA(value,**{'sizeinbytes': 508,  'pad': None })
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,DATA):
            self.__field_name=value
        else:
            self.__field_name=DATA(value,**{'sizeinbytes': 508})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_index(self):
        return self.__field_index.getvalue()
    def __setfield_index(self, value):
        if isinstance(value,UINT):
            self.__field_index=value
        else:
            self.__field_index=UINT(value,**{'sizeinbytes': 1})
    def __delfield_index(self): del self.__field_index
    index=property(__getfield_index, __setfield_index, __delfield_index, None)
    def __getfield_ringtone_type(self):
        return self.__field_ringtone_type.getvalue()
    def __setfield_ringtone_type(self, value):
        if isinstance(value,UINT):
            self.__field_ringtone_type=value
        else:
            self.__field_ringtone_type=UINT(value,**{'sizeinbytes': 1})
    def __delfield_ringtone_type(self): del self.__field_ringtone_type
    ringtone_type=property(__getfield_ringtone_type, __setfield_ringtone_type, __delfield_ringtone_type, None)
    def __getfield_dunno(self):
        try: self.__field_dunno
        except:
            self.__field_dunno=DATA(**{'sizeinbytes': 6,  'default': '' })
        return self.__field_dunno.getvalue()
    def __setfield_dunno(self, value):
        if isinstance(value,DATA):
            self.__field_dunno=value
        else:
            self.__field_dunno=DATA(value,**{'sizeinbytes': 6,  'default': '' })
    def __delfield_dunno(self): del self.__field_dunno
    dunno=property(__getfield_dunno, __setfield_dunno, __delfield_dunno, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('read_mode', self.__field_read_mode, None)
        if self.read_mode:
            yield ('name', self.__field_name, None)
        if not self.read_mode:
            yield ('name', self.__field_name, None)
        yield ('index', self.__field_index, None)
        yield ('ringtone_type', self.__field_ringtone_type, None)
        yield ('dunno', self.__field_dunno, None)
class ringtone_index_file(BaseProtogenClass):
    __fields=['items']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(ringtone_index_file,self).__init__(**dict)
        if self.__class__ is ringtone_index_file:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(ringtone_index_file,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(ringtone_index_file,kwargs)
        if len(args):
            dict2={ 'elementclass': ringtone_index_entry,             'createdefault': True}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_items=LIST(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_items
        except:
            self.__field_items=LIST(**{ 'elementclass': ringtone_index_entry,             'createdefault': True})
        self.__field_items.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_items=LIST(**{ 'elementclass': ringtone_index_entry,             'createdefault': True})
        self.__field_items.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_items(self):
        try: self.__field_items
        except:
            self.__field_items=LIST(**{ 'elementclass': ringtone_index_entry,             'createdefault': True})
        return self.__field_items.getvalue()
    def __setfield_items(self, value):
        if isinstance(value,LIST):
            self.__field_items=value
        else:
            self.__field_items=LIST(value,**{ 'elementclass': ringtone_index_entry,             'createdefault': True})
    def __delfield_items(self): del self.__field_items
    items=property(__getfield_items, __setfield_items, __delfield_items, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('items', self.__field_items, None)
class calendar_lock_req(BaseProtogenClass):
    __fields=['command', 'lock']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(calendar_lock_req,self).__init__(**dict)
        if self.__class__ is calendar_lock_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(calendar_lock_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(calendar_lock_req,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBL=' })
        self.__field_command.writetobuffer(buf)
        self.__field_lock.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBL=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_lock=CSVINT(**{ 'terminator': None })
        self.__field_lock.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBL=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBL=' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_lock(self):
        return self.__field_lock.getvalue()
    def __setfield_lock(self, value):
        if isinstance(value,CSVINT):
            self.__field_lock=value
        else:
            self.__field_lock=CSVINT(value,**{ 'terminator': None })
    def __delfield_lock(self): del self.__field_lock
    lock=property(__getfield_lock, __setfield_lock, __delfield_lock, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('lock', self.__field_lock, None)
class calendar_read_req(BaseProtogenClass):
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBR=' })
        self.__field_command.writetobuffer(buf)
        try: self.__field_start_index
        except:
            self.__field_start_index=CSVINT(**{ 'default': 0 })
        self.__field_start_index.writetobuffer(buf)
        try: self.__field_end_index
        except:
            self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': CAL_MAX_ENTRY })
        self.__field_end_index.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBR=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_start_index=CSVINT(**{ 'default': 0 })
        self.__field_start_index.readfrombuffer(buf)
        self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': CAL_MAX_ENTRY })
        self.__field_end_index.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBR=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBR=' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_start_index(self):
        try: self.__field_start_index
        except:
            self.__field_start_index=CSVINT(**{ 'default': 0 })
        return self.__field_start_index.getvalue()
    def __setfield_start_index(self, value):
        if isinstance(value,CSVINT):
            self.__field_start_index=value
        else:
            self.__field_start_index=CSVINT(value,**{ 'default': 0 })
    def __delfield_start_index(self): del self.__field_start_index
    start_index=property(__getfield_start_index, __setfield_start_index, __delfield_start_index, None)
    def __getfield_end_index(self):
        try: self.__field_end_index
        except:
            self.__field_end_index=CSVINT(**{ 'terminator': None,               'default': CAL_MAX_ENTRY })
        return self.__field_end_index.getvalue()
    def __setfield_end_index(self, value):
        if isinstance(value,CSVINT):
            self.__field_end_index=value
        else:
            self.__field_end_index=CSVINT(value,**{ 'terminator': None,               'default': CAL_MAX_ENTRY })
    def __delfield_end_index(self): del self.__field_end_index
    end_index=property(__getfield_end_index, __setfield_end_index, __delfield_end_index, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('start_index', self.__field_start_index, None)
        yield ('end_index', self.__field_end_index, None)
class calendar_req_resp(BaseProtogenClass):
    __fields=['command', 'index', 'title', 'alarm_timed', 'alarm_enabled', 'start_time', 'start_date', 'duration', 'alarm_time', 'alarm_date', 'repeat_type', 'ex_event', 'ex_event_flag']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(calendar_req_resp,self).__init__(**dict)
        if self.__class__ is calendar_req_resp:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(calendar_req_resp,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(calendar_req_resp,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        if self.command=='+MDBR:':
            self.__field_title.writetobuffer(buf)
            self.__field_alarm_timed.writetobuffer(buf)
            self.__field_alarm_enabled.writetobuffer(buf)
            self.__field_start_time.writetobuffer(buf)
            self.__field_start_date.writetobuffer(buf)
            self.__field_duration.writetobuffer(buf)
            self.__field_alarm_time.writetobuffer(buf)
            self.__field_alarm_date.writetobuffer(buf)
            self.__field_repeat_type.writetobuffer(buf)
        if self.command=='+MDBRE:':
            self.__field_ex_event.writetobuffer(buf)
            self.__field_ex_event_flag.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': ord(' '),                  'default': '+MDBR:' })
        self.__field_command.readfrombuffer(buf)
        self.__field_index=CSVINT()
        self.__field_index.readfrombuffer(buf)
        if self.command=='+MDBR:':
            self.__field_title=CSVSTRING()
            self.__field_title.readfrombuffer(buf)
            self.__field_alarm_timed=CSVINT()
            self.__field_alarm_timed.readfrombuffer(buf)
            self.__field_alarm_enabled=CSVINT()
            self.__field_alarm_enabled.readfrombuffer(buf)
            self.__field_start_time=CAL_TIME()
            self.__field_start_time.readfrombuffer(buf)
            self.__field_start_date=CAL_DATE()
            self.__field_start_date.readfrombuffer(buf)
            self.__field_duration=CSVINT()
            self.__field_duration.readfrombuffer(buf)
            self.__field_alarm_time=CAL_TIME()
            self.__field_alarm_time.readfrombuffer(buf)
            self.__field_alarm_date=CAL_DATE()
            self.__field_alarm_date.readfrombuffer(buf)
            self.__field_repeat_type=CSVINT(**{ 'terminator': None })
            self.__field_repeat_type.readfrombuffer(buf)
        if self.command=='+MDBRE:':
            self.__field_ex_event=CSVINT()
            self.__field_ex_event.readfrombuffer(buf)
            self.__field_ex_event_flag=CSVINT(**{ 'terminator': None })
            self.__field_ex_event_flag.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': ord(' '),                  'default': '+MDBR:' })
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
    def __getfield_title(self):
        return self.__field_title.getvalue()
    def __setfield_title(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_title=value
        else:
            self.__field_title=CSVSTRING(value,)
    def __delfield_title(self): del self.__field_title
    title=property(__getfield_title, __setfield_title, __delfield_title, None)
    def __getfield_alarm_timed(self):
        return self.__field_alarm_timed.getvalue()
    def __setfield_alarm_timed(self, value):
        if isinstance(value,CSVINT):
            self.__field_alarm_timed=value
        else:
            self.__field_alarm_timed=CSVINT(value,)
    def __delfield_alarm_timed(self): del self.__field_alarm_timed
    alarm_timed=property(__getfield_alarm_timed, __setfield_alarm_timed, __delfield_alarm_timed, None)
    def __getfield_alarm_enabled(self):
        return self.__field_alarm_enabled.getvalue()
    def __setfield_alarm_enabled(self, value):
        if isinstance(value,CSVINT):
            self.__field_alarm_enabled=value
        else:
            self.__field_alarm_enabled=CSVINT(value,)
    def __delfield_alarm_enabled(self): del self.__field_alarm_enabled
    alarm_enabled=property(__getfield_alarm_enabled, __setfield_alarm_enabled, __delfield_alarm_enabled, None)
    def __getfield_start_time(self):
        return self.__field_start_time.getvalue()
    def __setfield_start_time(self, value):
        if isinstance(value,CAL_TIME):
            self.__field_start_time=value
        else:
            self.__field_start_time=CAL_TIME(value,)
    def __delfield_start_time(self): del self.__field_start_time
    start_time=property(__getfield_start_time, __setfield_start_time, __delfield_start_time, None)
    def __getfield_start_date(self):
        return self.__field_start_date.getvalue()
    def __setfield_start_date(self, value):
        if isinstance(value,CAL_DATE):
            self.__field_start_date=value
        else:
            self.__field_start_date=CAL_DATE(value,)
    def __delfield_start_date(self): del self.__field_start_date
    start_date=property(__getfield_start_date, __setfield_start_date, __delfield_start_date, None)
    def __getfield_duration(self):
        return self.__field_duration.getvalue()
    def __setfield_duration(self, value):
        if isinstance(value,CSVINT):
            self.__field_duration=value
        else:
            self.__field_duration=CSVINT(value,)
    def __delfield_duration(self): del self.__field_duration
    duration=property(__getfield_duration, __setfield_duration, __delfield_duration, None)
    def __getfield_alarm_time(self):
        return self.__field_alarm_time.getvalue()
    def __setfield_alarm_time(self, value):
        if isinstance(value,CAL_TIME):
            self.__field_alarm_time=value
        else:
            self.__field_alarm_time=CAL_TIME(value,)
    def __delfield_alarm_time(self): del self.__field_alarm_time
    alarm_time=property(__getfield_alarm_time, __setfield_alarm_time, __delfield_alarm_time, None)
    def __getfield_alarm_date(self):
        return self.__field_alarm_date.getvalue()
    def __setfield_alarm_date(self, value):
        if isinstance(value,CAL_DATE):
            self.__field_alarm_date=value
        else:
            self.__field_alarm_date=CAL_DATE(value,)
    def __delfield_alarm_date(self): del self.__field_alarm_date
    alarm_date=property(__getfield_alarm_date, __setfield_alarm_date, __delfield_alarm_date, None)
    def __getfield_repeat_type(self):
        return self.__field_repeat_type.getvalue()
    def __setfield_repeat_type(self, value):
        if isinstance(value,CSVINT):
            self.__field_repeat_type=value
        else:
            self.__field_repeat_type=CSVINT(value,**{ 'terminator': None })
    def __delfield_repeat_type(self): del self.__field_repeat_type
    repeat_type=property(__getfield_repeat_type, __setfield_repeat_type, __delfield_repeat_type, None)
    def __getfield_ex_event(self):
        return self.__field_ex_event.getvalue()
    def __setfield_ex_event(self, value):
        if isinstance(value,CSVINT):
            self.__field_ex_event=value
        else:
            self.__field_ex_event=CSVINT(value,)
    def __delfield_ex_event(self): del self.__field_ex_event
    ex_event=property(__getfield_ex_event, __setfield_ex_event, __delfield_ex_event, None)
    def __getfield_ex_event_flag(self):
        return self.__field_ex_event_flag.getvalue()
    def __setfield_ex_event_flag(self, value):
        if isinstance(value,CSVINT):
            self.__field_ex_event_flag=value
        else:
            self.__field_ex_event_flag=CSVINT(value,**{ 'terminator': None })
    def __delfield_ex_event_flag(self): del self.__field_ex_event_flag
    ex_event_flag=property(__getfield_ex_event_flag, __setfield_ex_event_flag, __delfield_ex_event_flag, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('index', self.__field_index, None)
        if self.command=='+MDBR:':
            yield ('title', self.__field_title, None)
            yield ('alarm_timed', self.__field_alarm_timed, None)
            yield ('alarm_enabled', self.__field_alarm_enabled, None)
            yield ('start_time', self.__field_start_time, None)
            yield ('start_date', self.__field_start_date, None)
            yield ('duration', self.__field_duration, None)
            yield ('alarm_time', self.__field_alarm_time, None)
            yield ('alarm_date', self.__field_alarm_date, None)
            yield ('repeat_type', self.__field_repeat_type, None)
        if self.command=='+MDBRE:':
            yield ('ex_event', self.__field_ex_event, None)
            yield ('ex_event_flag', self.__field_ex_event_flag, None)
class calendar_write_req(BaseProtogenClass):
    __fields=['command', 'index', 'title', 'alarm_timed', 'alarm_enabled', 'start_time', 'start_date', 'duration', 'alarm_time', 'alarm_date', 'repeat_type']
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
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBW=' })
        self.__field_command.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_title.writetobuffer(buf)
        try: self.__field_alarm_timed
        except:
            self.__field_alarm_timed=CSVINT(**{ 'default': 0 })
        self.__field_alarm_timed.writetobuffer(buf)
        try: self.__field_alarm_enabled
        except:
            self.__field_alarm_enabled=CSVINT(**{ 'default': 0 })
        self.__field_alarm_enabled.writetobuffer(buf)
        self.__field_start_time.writetobuffer(buf)
        self.__field_start_date.writetobuffer(buf)
        self.__field_duration.writetobuffer(buf)
        self.__field_alarm_time.writetobuffer(buf)
        self.__field_alarm_date.writetobuffer(buf)
        try: self.__field_repeat_type
        except:
            self.__field_repeat_type=CSVINT(**{ 'terminator': None,               'default': 0 })
        self.__field_repeat_type.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBW=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_index=CSVINT()
        self.__field_index.readfrombuffer(buf)
        self.__field_title=CSVSTRING(**{'maxsizeinbytes': CAL_TITLE_LEN })
        self.__field_title.readfrombuffer(buf)
        self.__field_alarm_timed=CSVINT(**{ 'default': 0 })
        self.__field_alarm_timed.readfrombuffer(buf)
        self.__field_alarm_enabled=CSVINT(**{ 'default': 0 })
        self.__field_alarm_enabled.readfrombuffer(buf)
        self.__field_start_time=CAL_TIME()
        self.__field_start_time.readfrombuffer(buf)
        self.__field_start_date=CAL_DATE()
        self.__field_start_date.readfrombuffer(buf)
        self.__field_duration=CSVINT()
        self.__field_duration.readfrombuffer(buf)
        self.__field_alarm_time=CAL_TIME()
        self.__field_alarm_time.readfrombuffer(buf)
        self.__field_alarm_date=CAL_DATE()
        self.__field_alarm_date.readfrombuffer(buf)
        self.__field_repeat_type=CSVINT(**{ 'terminator': None,               'default': 0 })
        self.__field_repeat_type.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBW=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBW=' })
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
    def __getfield_title(self):
        return self.__field_title.getvalue()
    def __setfield_title(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_title=value
        else:
            self.__field_title=CSVSTRING(value,**{'maxsizeinbytes': CAL_TITLE_LEN })
    def __delfield_title(self): del self.__field_title
    title=property(__getfield_title, __setfield_title, __delfield_title, None)
    def __getfield_alarm_timed(self):
        try: self.__field_alarm_timed
        except:
            self.__field_alarm_timed=CSVINT(**{ 'default': 0 })
        return self.__field_alarm_timed.getvalue()
    def __setfield_alarm_timed(self, value):
        if isinstance(value,CSVINT):
            self.__field_alarm_timed=value
        else:
            self.__field_alarm_timed=CSVINT(value,**{ 'default': 0 })
    def __delfield_alarm_timed(self): del self.__field_alarm_timed
    alarm_timed=property(__getfield_alarm_timed, __setfield_alarm_timed, __delfield_alarm_timed, None)
    def __getfield_alarm_enabled(self):
        try: self.__field_alarm_enabled
        except:
            self.__field_alarm_enabled=CSVINT(**{ 'default': 0 })
        return self.__field_alarm_enabled.getvalue()
    def __setfield_alarm_enabled(self, value):
        if isinstance(value,CSVINT):
            self.__field_alarm_enabled=value
        else:
            self.__field_alarm_enabled=CSVINT(value,**{ 'default': 0 })
    def __delfield_alarm_enabled(self): del self.__field_alarm_enabled
    alarm_enabled=property(__getfield_alarm_enabled, __setfield_alarm_enabled, __delfield_alarm_enabled, None)
    def __getfield_start_time(self):
        return self.__field_start_time.getvalue()
    def __setfield_start_time(self, value):
        if isinstance(value,CAL_TIME):
            self.__field_start_time=value
        else:
            self.__field_start_time=CAL_TIME(value,)
    def __delfield_start_time(self): del self.__field_start_time
    start_time=property(__getfield_start_time, __setfield_start_time, __delfield_start_time, None)
    def __getfield_start_date(self):
        return self.__field_start_date.getvalue()
    def __setfield_start_date(self, value):
        if isinstance(value,CAL_DATE):
            self.__field_start_date=value
        else:
            self.__field_start_date=CAL_DATE(value,)
    def __delfield_start_date(self): del self.__field_start_date
    start_date=property(__getfield_start_date, __setfield_start_date, __delfield_start_date, None)
    def __getfield_duration(self):
        return self.__field_duration.getvalue()
    def __setfield_duration(self, value):
        if isinstance(value,CSVINT):
            self.__field_duration=value
        else:
            self.__field_duration=CSVINT(value,)
    def __delfield_duration(self): del self.__field_duration
    duration=property(__getfield_duration, __setfield_duration, __delfield_duration, None)
    def __getfield_alarm_time(self):
        return self.__field_alarm_time.getvalue()
    def __setfield_alarm_time(self, value):
        if isinstance(value,CAL_TIME):
            self.__field_alarm_time=value
        else:
            self.__field_alarm_time=CAL_TIME(value,)
    def __delfield_alarm_time(self): del self.__field_alarm_time
    alarm_time=property(__getfield_alarm_time, __setfield_alarm_time, __delfield_alarm_time, None)
    def __getfield_alarm_date(self):
        return self.__field_alarm_date.getvalue()
    def __setfield_alarm_date(self, value):
        if isinstance(value,CAL_DATE):
            self.__field_alarm_date=value
        else:
            self.__field_alarm_date=CAL_DATE(value,)
    def __delfield_alarm_date(self): del self.__field_alarm_date
    alarm_date=property(__getfield_alarm_date, __setfield_alarm_date, __delfield_alarm_date, None)
    def __getfield_repeat_type(self):
        try: self.__field_repeat_type
        except:
            self.__field_repeat_type=CSVINT(**{ 'terminator': None,               'default': 0 })
        return self.__field_repeat_type.getvalue()
    def __setfield_repeat_type(self, value):
        if isinstance(value,CSVINT):
            self.__field_repeat_type=value
        else:
            self.__field_repeat_type=CSVINT(value,**{ 'terminator': None,               'default': 0 })
    def __delfield_repeat_type(self): del self.__field_repeat_type
    repeat_type=property(__getfield_repeat_type, __setfield_repeat_type, __delfield_repeat_type, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('index', self.__field_index, None)
        yield ('title', self.__field_title, None)
        yield ('alarm_timed', self.__field_alarm_timed, None)
        yield ('alarm_enabled', self.__field_alarm_enabled, None)
        yield ('start_time', self.__field_start_time, None)
        yield ('start_date', self.__field_start_date, None)
        yield ('duration', self.__field_duration, None)
        yield ('alarm_time', self.__field_alarm_time, None)
        yield ('alarm_date', self.__field_alarm_date, None)
        yield ('repeat_type', self.__field_repeat_type, None)
class calendar_write_ex_req(BaseProtogenClass):
    __fields=['command', 'index', 'nth_event', 'ex_event_flag']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(calendar_write_ex_req,self).__init__(**dict)
        if self.__class__ is calendar_write_ex_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(calendar_write_ex_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(calendar_write_ex_req,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBWE=' })
        self.__field_command.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_nth_event.writetobuffer(buf)
        try: self.__field_ex_event_flag
        except:
            self.__field_ex_event_flag=CSVINT(**{ 'terminator': None,               'default': 1 })
        self.__field_ex_event_flag.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBWE=' })
        self.__field_command.readfrombuffer(buf)
        self.__field_index=CSVINT()
        self.__field_index.readfrombuffer(buf)
        self.__field_nth_event=CSVINT()
        self.__field_nth_event.readfrombuffer(buf)
        self.__field_ex_event_flag=CSVINT(**{ 'terminator': None,               'default': 1 })
        self.__field_ex_event_flag.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBWE=' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{ 'quotechar': None,                  'terminator': None,                  'default': '+MDBWE=' })
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
    def __getfield_nth_event(self):
        return self.__field_nth_event.getvalue()
    def __setfield_nth_event(self, value):
        if isinstance(value,CSVINT):
            self.__field_nth_event=value
        else:
            self.__field_nth_event=CSVINT(value,)
    def __delfield_nth_event(self): del self.__field_nth_event
    nth_event=property(__getfield_nth_event, __setfield_nth_event, __delfield_nth_event, None)
    def __getfield_ex_event_flag(self):
        try: self.__field_ex_event_flag
        except:
            self.__field_ex_event_flag=CSVINT(**{ 'terminator': None,               'default': 1 })
        return self.__field_ex_event_flag.getvalue()
    def __setfield_ex_event_flag(self, value):
        if isinstance(value,CSVINT):
            self.__field_ex_event_flag=value
        else:
            self.__field_ex_event_flag=CSVINT(value,**{ 'terminator': None,               'default': 1 })
    def __delfield_ex_event_flag(self): del self.__field_ex_event_flag
    ex_event_flag=property(__getfield_ex_event_flag, __setfield_ex_event_flag, __delfield_ex_event_flag, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('index', self.__field_index, None)
        yield ('nth_event', self.__field_nth_event, None)
        yield ('ex_event_flag', self.__field_ex_event_flag, None)
