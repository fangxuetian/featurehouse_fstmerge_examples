"""Proposed descriptions of data usign AT commands"""
from prototypes import *
from p_samsung_packet import *
UINT=UINTlsb
BOOL=BOOLlsb
NUMPHONEBOOKENTRIES=239
NUMPHONENUMBERS=6
NUMCALENDAREVENTS=70
MAXNUMBERLEN=32
NUMTODOENTRIES=20
NUMGROUPS=4
class pbentry(BaseProtogenClass):
    __fields=['url', 'birthday', 'slot', 'name', 'dunno1', 'numbers']
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
        if getattr(self, '__field_url', None) is None:
            self.__field_url=STRING(**{'default': ""})
        if getattr(self, '__field_birthday', None) is None:
            self.__field_birthday=CSVDATE(**{'default': ""})
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        try: self.__field_dunno1
        except:
            self.__field_dunno1=CSVINT(**{'default': 255})
        self.__field_dunno1.writetobuffer(buf)
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'elementclass': phonenumber})
        self.__field_numbers.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot=CSVINT()
        self.__field_slot.readfrombuffer(buf)
        self.__field_name=CSVSTRING()
        self.__field_name.readfrombuffer(buf)
        self.__field_dunno1=CSVINT(**{'default': 255})
        self.__field_dunno1.readfrombuffer(buf)
        self.__field_numbers=LIST(**{'elementclass': phonenumber})
        self.__field_numbers.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_url(self):
        try: self.__field_url
        except:
            self.__field_url=STRING(**{'default': ""})
        return self.__field_url.getvalue()
    def __setfield_url(self, value):
        if isinstance(value,STRING):
            self.__field_url=value
        else:
            self.__field_url=STRING(value,**{'default': ""})
    def __delfield_url(self): del self.__field_url
    url=property(__getfield_url, __setfield_url, __delfield_url, None)
    def __getfield_birthday(self):
        try: self.__field_birthday
        except:
            self.__field_birthday=CSVDATE(**{'default': ""})
        return self.__field_birthday.getvalue()
    def __setfield_birthday(self, value):
        if isinstance(value,CSVDATE):
            self.__field_birthday=value
        else:
            self.__field_birthday=CSVDATE(value,**{'default': ""})
    def __delfield_birthday(self): del self.__field_birthday
    birthday=property(__getfield_birthday, __setfield_birthday, __delfield_birthday, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,CSVINT):
            self.__field_slot=value
        else:
            self.__field_slot=CSVINT(value,)
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, "Internal Slot")
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_name=value
        else:
            self.__field_name=CSVSTRING(value,)
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_dunno1(self):
        try: self.__field_dunno1
        except:
            self.__field_dunno1=CSVINT(**{'default': 255})
        return self.__field_dunno1.getvalue()
    def __setfield_dunno1(self, value):
        if isinstance(value,CSVINT):
            self.__field_dunno1=value
        else:
            self.__field_dunno1=CSVINT(value,**{'default': 255})
    def __delfield_dunno1(self): del self.__field_dunno1
    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
    def __getfield_numbers(self):
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'elementclass': phonenumber})
        return self.__field_numbers.getvalue()
    def __setfield_numbers(self, value):
        if isinstance(value,LIST):
            self.__field_numbers=value
        else:
            self.__field_numbers=LIST(value,**{'elementclass': phonenumber})
    def __delfield_numbers(self): del self.__field_numbers
    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('url', self.__field_url, None)
        yield ('birthday', self.__field_birthday, None)
        yield ('slot', self.__field_slot, "Internal Slot")
        yield ('name', self.__field_name, None)
        yield ('dunno1', self.__field_dunno1, None)
        yield ('numbers', self.__field_numbers, None)
class phonenumber(BaseProtogenClass):
    __fields=['last_number', 'numbertype', 'number', 'number']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(phonenumber,self).__init__(**dict)
        if self.__class__ is phonenumber:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(phonenumber,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(phonenumber,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
        if getattr(self, '__field_last_number', None) is None:
            self.__field_last_number=BOOL(**{'default': True})
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_numbertype
        except:
            self.__field_numbertype=CSVINT()
        self.__field_numbertype.writetobuffer(buf)
        if not self.last_number:
            try: self.__field_number
            except:
                self.__field_number=CSVSTRING(**{'quotechar': None, 'default': ""})
            self.__field_number.writetobuffer(buf)
        if self.last_number:
            try: self.__field_number
            except:
                self.__field_number=CSVSTRING(**{'quotechar': None, 'default': "", 'terminator': None})
            self.__field_number.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numbertype=CSVINT()
        self.__field_numbertype.readfrombuffer(buf)
        if not self.last_number:
            self.__field_number=CSVSTRING(**{'quotechar': None, 'default': ""})
            self.__field_number.readfrombuffer(buf)
        if self.last_number:
            self.__field_number=CSVSTRING(**{'quotechar': None, 'default': "", 'terminator': None})
            self.__field_number.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_last_number(self):
        try: self.__field_last_number
        except:
            self.__field_last_number=BOOL(**{'default': True})
        return self.__field_last_number.getvalue()
    def __setfield_last_number(self, value):
        if isinstance(value,BOOL):
            self.__field_last_number=value
        else:
            self.__field_last_number=BOOL(value,**{'default': True})
    def __delfield_last_number(self): del self.__field_last_number
    last_number=property(__getfield_last_number, __setfield_last_number, __delfield_last_number, None)
    def __getfield_numbertype(self):
        try: self.__field_numbertype
        except:
            self.__field_numbertype=CSVINT()
        return self.__field_numbertype.getvalue()
    def __setfield_numbertype(self, value):
        if isinstance(value,CSVINT):
            self.__field_numbertype=value
        else:
            self.__field_numbertype=CSVINT(value,)
    def __delfield_numbertype(self): del self.__field_numbertype
    numbertype=property(__getfield_numbertype, __setfield_numbertype, __delfield_numbertype, None)
    def __getfield_number(self):
        try: self.__field_number
        except:
            self.__field_number=CSVSTRING(**{'quotechar': None, 'default': ""})
        return self.__field_number.getvalue()
    def __setfield_number(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_number=value
        else:
            self.__field_number=CSVSTRING(value,**{'quotechar': None, 'default': ""})
    def __delfield_number(self): del self.__field_number
    number=property(__getfield_number, __setfield_number, __delfield_number, None)
    def __getfield_number(self):
        try: self.__field_number
        except:
            self.__field_number=CSVSTRING(**{'quotechar': None, 'default': "", 'terminator': None})
        return self.__field_number.getvalue()
    def __setfield_number(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_number=value
        else:
            self.__field_number=CSVSTRING(value,**{'quotechar': None, 'default': "", 'terminator': None})
    def __delfield_number(self): del self.__field_number
    number=property(__getfield_number, __setfield_number, __delfield_number, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('last_number', self.__field_last_number, None)
        yield ('numbertype', self.__field_numbertype, None)
        if not self.last_number:
            yield ('number', self.__field_number, None)
        if self.last_number:
            yield ('number', self.__field_number, None)
class phonebookslotrequest(BaseProtogenClass):
    __fields=['command', 'slot']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(phonebookslotrequest,self).__init__(**dict)
        if self.__class__ is phonebookslotrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(phonebookslotrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(phonebookslotrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': None, 'default': '#PBOKR='})
        self.__field_command.writetobuffer(buf)
        try: self.__field_slot
        except:
            self.__field_slot=CSVINT(**{'terminator': None})
        self.__field_slot.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': None, 'default': '#PBOKR='})
        self.__field_command.readfrombuffer(buf)
        self.__field_slot=CSVINT(**{'terminator': None})
        self.__field_slot.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': None, 'default': '#PBOKR='})
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{'quotechar': None, 'terminator': None, 'default': '#PBOKR='})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_slot(self):
        try: self.__field_slot
        except:
            self.__field_slot=CSVINT(**{'terminator': None})
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,CSVINT):
            self.__field_slot=value
        else:
            self.__field_slot=CSVINT(value,**{'terminator': None})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, "Internal Slot")
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('slot', self.__field_slot, "Internal Slot")
class phonebookslotresponse(BaseProtogenClass):
    __fields=['command', 'entry']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(phonebookslotresponse,self).__init__(**dict)
        if self.__class__ is phonebookslotresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(phonebookslotresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(phonebookslotresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': ord(' '), 'constant': '#PBOKR:'})
        self.__field_command.readfrombuffer(buf)
        self.__field_entry=pbentry()
        self.__field_entry.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{'quotechar': None, 'terminator': ord(' '), 'constant': '#PBOKR:'})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
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
        yield ('command', self.__field_command, None)
        yield ('entry', self.__field_entry, None)
class phonebookslotupdaterequest(BaseProtogenClass):
    __fields=['command', 'entry']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(phonebookslotupdaterequest,self).__init__(**dict)
        if self.__class__ is phonebookslotupdaterequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(phonebookslotupdaterequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(phonebookslotupdaterequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': None, 'default': '#PBOKW='})
        self.__field_command.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': None, 'default': '#PBOKW='})
        self.__field_command.readfrombuffer(buf)
        self.__field_entry=pbentry()
        self.__field_entry.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': None, 'default': '#PBOKW='})
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{'quotechar': None, 'terminator': None, 'default': '#PBOKW='})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
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
        yield ('command', self.__field_command, None)
        yield ('entry', self.__field_entry, None)
