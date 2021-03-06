"""Various descriptions of data specific to Audiovox CDM8900"""
from prototypes import *
UINT=UINTlsb
BOOL=BOOLlsb
NUMSLOTS=300
MAXPHONENUMBERLEN=32
MAXPHONENUMBERS=5
MAXNAMELEN=16
MAXEMAILLEN=48
MAXMEMOLEN=48
MAXEMAILS=3
numbertypetab=( 'phone', 'home', 'office','cell', 'pager', 'fax' )
class pbnumber(BaseProtogenClass):
    __fields=['valid', 'type', 'ringer_group', 'pad0', 'ringer_index', 'pad1', 'secret', 'number', 'pad2']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(pbnumber,self).__init__(**dict)
        if self.__class__ is pbnumber:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(pbnumber,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(pbnumber,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_valid
        except:
            self.__field_valid=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_valid.writetobuffer(buf)
        try: self.__field_type
        except:
            self.__field_type=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_type.writetobuffer(buf)
        try: self.__field_ringer_group
        except:
            self.__field_ringer_group=UINT(**{'sizeinbytes': 1, 'default': 5})
        self.__field_ringer_group.writetobuffer(buf)
        try: self.__field_pad0
        except:
            self.__field_pad0=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_pad0.writetobuffer(buf)
        try: self.__field_ringer_index
        except:
            self.__field_ringer_index=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_ringer_index.writetobuffer(buf)
        try: self.__field_pad1
        except:
            self.__field_pad1=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_pad1.writetobuffer(buf)
        try: self.__field_secret
        except:
            self.__field_secret=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_secret.writetobuffer(buf)
        try: self.__field_number
        except:
            self.__field_number=STRING(**{'sizeinbytes': 33, 'terminator': None, 'pascal': True, 'default': ""})
        self.__field_number.writetobuffer(buf)
        try: self.__field_pad2
        except:
            self.__field_pad2=UNKNOWN(**{'sizeinbytes': 48})
        self.__field_pad2.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_valid=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_valid.readfrombuffer(buf)
        self.__field_type=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_type.readfrombuffer(buf)
        self.__field_ringer_group=UINT(**{'sizeinbytes': 1, 'default': 5})
        self.__field_ringer_group.readfrombuffer(buf)
        self.__field_pad0=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_pad0.readfrombuffer(buf)
        self.__field_ringer_index=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_ringer_index.readfrombuffer(buf)
        self.__field_pad1=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_pad1.readfrombuffer(buf)
        self.__field_secret=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_secret.readfrombuffer(buf)
        self.__field_number=STRING(**{'sizeinbytes': 33, 'terminator': None, 'pascal': True, 'default': ""})
        self.__field_number.readfrombuffer(buf)
        self.__field_pad2=UNKNOWN(**{'sizeinbytes': 48})
        self.__field_pad2.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_valid(self):
        try: self.__field_valid
        except:
            self.__field_valid=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_valid.getvalue()
    def __setfield_valid(self, value):
        if isinstance(value,UINT):
            self.__field_valid=value
        else:
            self.__field_valid=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_valid(self): del self.__field_valid
    valid=property(__getfield_valid, __setfield_valid, __delfield_valid, None)
    def __getfield_type(self):
        try: self.__field_type
        except:
            self.__field_type=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_type.getvalue()
    def __setfield_type(self, value):
        if isinstance(value,UINT):
            self.__field_type=value
        else:
            self.__field_type=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_type(self): del self.__field_type
    type=property(__getfield_type, __setfield_type, __delfield_type, None)
    def __getfield_ringer_group(self):
        try: self.__field_ringer_group
        except:
            self.__field_ringer_group=UINT(**{'sizeinbytes': 1, 'default': 5})
        return self.__field_ringer_group.getvalue()
    def __setfield_ringer_group(self, value):
        if isinstance(value,UINT):
            self.__field_ringer_group=value
        else:
            self.__field_ringer_group=UINT(value,**{'sizeinbytes': 1, 'default': 5})
    def __delfield_ringer_group(self): del self.__field_ringer_group
    ringer_group=property(__getfield_ringer_group, __setfield_ringer_group, __delfield_ringer_group, None)
    def __getfield_pad0(self):
        try: self.__field_pad0
        except:
            self.__field_pad0=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_pad0.getvalue()
    def __setfield_pad0(self, value):
        if isinstance(value,UINT):
            self.__field_pad0=value
        else:
            self.__field_pad0=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_pad0(self): del self.__field_pad0
    pad0=property(__getfield_pad0, __setfield_pad0, __delfield_pad0, None)
    def __getfield_ringer_index(self):
        try: self.__field_ringer_index
        except:
            self.__field_ringer_index=UINT(**{'sizeinbytes': 2, 'default': 0})
        return self.__field_ringer_index.getvalue()
    def __setfield_ringer_index(self, value):
        if isinstance(value,UINT):
            self.__field_ringer_index=value
        else:
            self.__field_ringer_index=UINT(value,**{'sizeinbytes': 2, 'default': 0})
    def __delfield_ringer_index(self): del self.__field_ringer_index
    ringer_index=property(__getfield_ringer_index, __setfield_ringer_index, __delfield_ringer_index, None)
    def __getfield_pad1(self):
        try: self.__field_pad1
        except:
            self.__field_pad1=UINT(**{'sizeinbytes': 2, 'default': 0})
        return self.__field_pad1.getvalue()
    def __setfield_pad1(self, value):
        if isinstance(value,UINT):
            self.__field_pad1=value
        else:
            self.__field_pad1=UINT(value,**{'sizeinbytes': 2, 'default': 0})
    def __delfield_pad1(self): del self.__field_pad1
    pad1=property(__getfield_pad1, __setfield_pad1, __delfield_pad1, None)
    def __getfield_secret(self):
        try: self.__field_secret
        except:
            self.__field_secret=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_secret.getvalue()
    def __setfield_secret(self, value):
        if isinstance(value,UINT):
            self.__field_secret=value
        else:
            self.__field_secret=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_secret(self): del self.__field_secret
    secret=property(__getfield_secret, __setfield_secret, __delfield_secret, None)
    def __getfield_number(self):
        try: self.__field_number
        except:
            self.__field_number=STRING(**{'sizeinbytes': 33, 'terminator': None, 'pascal': True, 'default': ""})
        return self.__field_number.getvalue()
    def __setfield_number(self, value):
        if isinstance(value,STRING):
            self.__field_number=value
        else:
            self.__field_number=STRING(value,**{'sizeinbytes': 33, 'terminator': None, 'pascal': True, 'default': ""})
    def __delfield_number(self): del self.__field_number
    number=property(__getfield_number, __setfield_number, __delfield_number, None)
    def __getfield_pad2(self):
        try: self.__field_pad2
        except:
            self.__field_pad2=UNKNOWN(**{'sizeinbytes': 48})
        return self.__field_pad2.getvalue()
    def __setfield_pad2(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad2=value
        else:
            self.__field_pad2=UNKNOWN(value,**{'sizeinbytes': 48})
    def __delfield_pad2(self): del self.__field_pad2
    pad2=property(__getfield_pad2, __setfield_pad2, __delfield_pad2, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('valid', self.__field_valid, None)
        yield ('type', self.__field_type, None)
        yield ('ringer_group', self.__field_ringer_group, None)
        yield ('pad0', self.__field_pad0, None)
        yield ('ringer_index', self.__field_ringer_index, None)
        yield ('pad1', self.__field_pad1, None)
        yield ('secret', self.__field_secret, None)
        yield ('number', self.__field_number, None)
        yield ('pad2', self.__field_pad2, None)
class pbemail(BaseProtogenClass):
    __fields=['valid', 'dunno1', 'dunno2', 'dunno3', 'dunno4', 'email']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(pbemail,self).__init__(**dict)
        if self.__class__ is pbemail:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(pbemail,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(pbemail,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_valid
        except:
            self.__field_valid=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_valid.writetobuffer(buf)
        try: self.__field_dunno1
        except:
            self.__field_dunno1=UINT(**{'sizeinbytes': 1, 'default': 1})
        self.__field_dunno1.writetobuffer(buf)
        try: self.__field_dunno2
        except:
            self.__field_dunno2=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_dunno2.writetobuffer(buf)
        try: self.__field_dunno3
        except:
            self.__field_dunno3=UINT(**{'sizeinbytes': 1, 'default': 5})
        self.__field_dunno3.writetobuffer(buf)
        try: self.__field_dunno4
        except:
            self.__field_dunno4=UINT(**{'sizeinbytes': 4, 'default': 0})
        self.__field_dunno4.writetobuffer(buf)
        try: self.__field_email
        except:
            self.__field_email=STRING(**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
        self.__field_email.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_valid=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_valid.readfrombuffer(buf)
        self.__field_dunno1=UINT(**{'sizeinbytes': 1, 'default': 1})
        self.__field_dunno1.readfrombuffer(buf)
        self.__field_dunno2=UINT(**{'sizeinbytes': 2, 'default': 0})
        self.__field_dunno2.readfrombuffer(buf)
        self.__field_dunno3=UINT(**{'sizeinbytes': 1, 'default': 5})
        self.__field_dunno3.readfrombuffer(buf)
        self.__field_dunno4=UINT(**{'sizeinbytes': 4, 'default': 0})
        self.__field_dunno4.readfrombuffer(buf)
        self.__field_email=STRING(**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
        self.__field_email.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_valid(self):
        try: self.__field_valid
        except:
            self.__field_valid=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_valid.getvalue()
    def __setfield_valid(self, value):
        if isinstance(value,UINT):
            self.__field_valid=value
        else:
            self.__field_valid=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_valid(self): del self.__field_valid
    valid=property(__getfield_valid, __setfield_valid, __delfield_valid, None)
    def __getfield_dunno1(self):
        try: self.__field_dunno1
        except:
            self.__field_dunno1=UINT(**{'sizeinbytes': 1, 'default': 1})
        return self.__field_dunno1.getvalue()
    def __setfield_dunno1(self, value):
        if isinstance(value,UINT):
            self.__field_dunno1=value
        else:
            self.__field_dunno1=UINT(value,**{'sizeinbytes': 1, 'default': 1})
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
    def __getfield_dunno3(self):
        try: self.__field_dunno3
        except:
            self.__field_dunno3=UINT(**{'sizeinbytes': 1, 'default': 5})
        return self.__field_dunno3.getvalue()
    def __setfield_dunno3(self, value):
        if isinstance(value,UINT):
            self.__field_dunno3=value
        else:
            self.__field_dunno3=UINT(value,**{'sizeinbytes': 1, 'default': 5})
    def __delfield_dunno3(self): del self.__field_dunno3
    dunno3=property(__getfield_dunno3, __setfield_dunno3, __delfield_dunno3, None)
    def __getfield_dunno4(self):
        try: self.__field_dunno4
        except:
            self.__field_dunno4=UINT(**{'sizeinbytes': 4, 'default': 0})
        return self.__field_dunno4.getvalue()
    def __setfield_dunno4(self, value):
        if isinstance(value,UINT):
            self.__field_dunno4=value
        else:
            self.__field_dunno4=UINT(value,**{'sizeinbytes': 4, 'default': 0})
    def __delfield_dunno4(self): del self.__field_dunno4
    dunno4=property(__getfield_dunno4, __setfield_dunno4, __delfield_dunno4, None)
    def __getfield_email(self):
        try: self.__field_email
        except:
            self.__field_email=STRING(**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
        return self.__field_email.getvalue()
    def __setfield_email(self, value):
        if isinstance(value,STRING):
            self.__field_email=value
        else:
            self.__field_email=STRING(value,**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
    def __delfield_email(self): del self.__field_email
    email=property(__getfield_email, __setfield_email, __delfield_email, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('valid', self.__field_valid, None)
        yield ('dunno1', self.__field_dunno1, None)
        yield ('dunno2', self.__field_dunno2, None)
        yield ('dunno3', self.__field_dunno3, None)
        yield ('dunno4', self.__field_dunno4, None)
        yield ('email', self.__field_email, None)
class pbentry(BaseProtogenClass):
    __fields=['slot', 'pad2', 'pad3', 'name', 'numbers', 'emails', 'dunno', 'web_page', 'pad5']
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
        self.__field_slot.writetobuffer(buf)
        try: self.__field_pad2
        except:
            self.__field_pad2=UINT(**{'sizeinbytes': 2, 'default': 0x0101})
        self.__field_pad2.writetobuffer(buf)
        try: self.__field_pad3
        except:
            self.__field_pad3=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_pad3.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'length': MAXPHONENUMBERS, 'elementclass': pbnumber, 'createdefault': True})
        self.__field_numbers.writetobuffer(buf)
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'length': MAXEMAILS, 'elementclass': pbemail, 'createdefault': True})
        self.__field_emails.writetobuffer(buf)
        try: self.__field_dunno
        except:
            self.__field_dunno=UINT(**{'sizeinbytes': 2, 'default': 0x0001})
        self.__field_dunno.writetobuffer(buf)
        try: self.__field_web_page
        except:
            self.__field_web_page=STRING(**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
        self.__field_web_page.writetobuffer(buf)
        try: self.__field_pad5
        except:
            self.__field_pad5=UNKNOWN(**{'sizeinbytes': 81})
        self.__field_pad5.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot=UINT(**{'sizeinbytes': 2})
        self.__field_slot.readfrombuffer(buf)
        self.__field_pad2=UINT(**{'sizeinbytes': 2, 'default': 0x0101})
        self.__field_pad2.readfrombuffer(buf)
        self.__field_pad3=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_pad3.readfrombuffer(buf)
        self.__field_name=STRING(**{'sizeinbytes': 37, 'terminator': None, 'pascal': True})
        self.__field_name.readfrombuffer(buf)
        self.__field_numbers=LIST(**{'length': MAXPHONENUMBERS, 'elementclass': pbnumber, 'createdefault': True})
        self.__field_numbers.readfrombuffer(buf)
        self.__field_emails=LIST(**{'length': MAXEMAILS, 'elementclass': pbemail, 'createdefault': True})
        self.__field_emails.readfrombuffer(buf)
        self.__field_dunno=UINT(**{'sizeinbytes': 2, 'default': 0x0001})
        self.__field_dunno.readfrombuffer(buf)
        self.__field_web_page=STRING(**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
        self.__field_web_page.readfrombuffer(buf)
        self.__field_pad5=UNKNOWN(**{'sizeinbytes': 81})
        self.__field_pad5.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 2})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_pad2(self):
        try: self.__field_pad2
        except:
            self.__field_pad2=UINT(**{'sizeinbytes': 2, 'default': 0x0101})
        return self.__field_pad2.getvalue()
    def __setfield_pad2(self, value):
        if isinstance(value,UINT):
            self.__field_pad2=value
        else:
            self.__field_pad2=UINT(value,**{'sizeinbytes': 2, 'default': 0x0101})
    def __delfield_pad2(self): del self.__field_pad2
    pad2=property(__getfield_pad2, __setfield_pad2, __delfield_pad2, None)
    def __getfield_pad3(self):
        try: self.__field_pad3
        except:
            self.__field_pad3=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_pad3.getvalue()
    def __setfield_pad3(self, value):
        if isinstance(value,UINT):
            self.__field_pad3=value
        else:
            self.__field_pad3=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_pad3(self): del self.__field_pad3
    pad3=property(__getfield_pad3, __setfield_pad3, __delfield_pad3, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,STRING):
            self.__field_name=value
        else:
            self.__field_name=STRING(value,**{'sizeinbytes': 37, 'terminator': None, 'pascal': True})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_numbers(self):
        try: self.__field_numbers
        except:
            self.__field_numbers=LIST(**{'length': MAXPHONENUMBERS, 'elementclass': pbnumber, 'createdefault': True})
        return self.__field_numbers.getvalue()
    def __setfield_numbers(self, value):
        if isinstance(value,LIST):
            self.__field_numbers=value
        else:
            self.__field_numbers=LIST(value,**{'length': MAXPHONENUMBERS, 'elementclass': pbnumber, 'createdefault': True})
    def __delfield_numbers(self): del self.__field_numbers
    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
    def __getfield_emails(self):
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'length': MAXEMAILS, 'elementclass': pbemail, 'createdefault': True})
        return self.__field_emails.getvalue()
    def __setfield_emails(self, value):
        if isinstance(value,LIST):
            self.__field_emails=value
        else:
            self.__field_emails=LIST(value,**{'length': MAXEMAILS, 'elementclass': pbemail, 'createdefault': True})
    def __delfield_emails(self): del self.__field_emails
    emails=property(__getfield_emails, __setfield_emails, __delfield_emails, None)
    def __getfield_dunno(self):
        try: self.__field_dunno
        except:
            self.__field_dunno=UINT(**{'sizeinbytes': 2, 'default': 0x0001})
        return self.__field_dunno.getvalue()
    def __setfield_dunno(self, value):
        if isinstance(value,UINT):
            self.__field_dunno=value
        else:
            self.__field_dunno=UINT(value,**{'sizeinbytes': 2, 'default': 0x0001})
    def __delfield_dunno(self): del self.__field_dunno
    dunno=property(__getfield_dunno, __setfield_dunno, __delfield_dunno, None)
    def __getfield_web_page(self):
        try: self.__field_web_page
        except:
            self.__field_web_page=STRING(**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
        return self.__field_web_page.getvalue()
    def __setfield_web_page(self, value):
        if isinstance(value,STRING):
            self.__field_web_page=value
        else:
            self.__field_web_page=STRING(value,**{'sizeinbytes': 49, 'terminator': None, 'pascal': True, 'default': ""})
    def __delfield_web_page(self): del self.__field_web_page
    web_page=property(__getfield_web_page, __setfield_web_page, __delfield_web_page, None)
    def __getfield_pad5(self):
        try: self.__field_pad5
        except:
            self.__field_pad5=UNKNOWN(**{'sizeinbytes': 81})
        return self.__field_pad5.getvalue()
    def __setfield_pad5(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad5=value
        else:
            self.__field_pad5=UNKNOWN(value,**{'sizeinbytes': 81})
    def __delfield_pad5(self): del self.__field_pad5
    pad5=property(__getfield_pad5, __setfield_pad5, __delfield_pad5, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('slot', self.__field_slot, None)
        yield ('pad2', self.__field_pad2, None)
        yield ('pad3', self.__field_pad3, None)
        yield ('name', self.__field_name, None)
        yield ('numbers', self.__field_numbers, None)
        yield ('emails', self.__field_emails, None)
        yield ('dunno', self.__field_dunno, None)
        yield ('web_page', self.__field_web_page, None)
        yield ('pad5', self.__field_pad5, None)
class setphoneattribrequest(BaseProtogenClass):
    __fields=['cmd', 'cmd2', 'cmd3', 'flag', 'data']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(setphoneattribrequest,self).__init__(**dict)
        if self.__class__ is setphoneattribrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(setphoneattribrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(setphoneattribrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x27})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0xF0})
        self.__field_cmd2.writetobuffer(buf)
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x7F})
        self.__field_cmd3.writetobuffer(buf)
        try: self.__field_flag
        except:
            self.__field_flag=UINT(**{'sizeinbytes': 1, 'constant': 0x01})
        self.__field_flag.writetobuffer(buf)
        try: self.__field_data
        except:
            self.__field_data=UINT(**{'sizeinbytes': 129, 'constant': 0x00})
        self.__field_data.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x27})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0xF0})
        self.__field_cmd2.readfrombuffer(buf)
        self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x7F})
        self.__field_cmd3.readfrombuffer(buf)
        self.__field_flag=UINT(**{'sizeinbytes': 1, 'constant': 0x01})
        self.__field_flag.readfrombuffer(buf)
        self.__field_data=UINT(**{'sizeinbytes': 129, 'constant': 0x00})
        self.__field_data.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x27})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0x27})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_cmd2(self):
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0xF0})
        return self.__field_cmd2.getvalue()
    def __setfield_cmd2(self, value):
        if isinstance(value,UINT):
            self.__field_cmd2=value
        else:
            self.__field_cmd2=UINT(value,**{'sizeinbytes': 1, 'constant': 0xF0})
    def __delfield_cmd2(self): del self.__field_cmd2
    cmd2=property(__getfield_cmd2, __setfield_cmd2, __delfield_cmd2, None)
    def __getfield_cmd3(self):
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x7F})
        return self.__field_cmd3.getvalue()
    def __setfield_cmd3(self, value):
        if isinstance(value,UINT):
            self.__field_cmd3=value
        else:
            self.__field_cmd3=UINT(value,**{'sizeinbytes': 1, 'constant': 0x7F})
    def __delfield_cmd3(self): del self.__field_cmd3
    cmd3=property(__getfield_cmd3, __setfield_cmd3, __delfield_cmd3, None)
    def __getfield_flag(self):
        try: self.__field_flag
        except:
            self.__field_flag=UINT(**{'sizeinbytes': 1, 'constant': 0x01})
        return self.__field_flag.getvalue()
    def __setfield_flag(self, value):
        if isinstance(value,UINT):
            self.__field_flag=value
        else:
            self.__field_flag=UINT(value,**{'sizeinbytes': 1, 'constant': 0x01})
    def __delfield_flag(self): del self.__field_flag
    flag=property(__getfield_flag, __setfield_flag, __delfield_flag, None)
    def __getfield_data(self):
        try: self.__field_data
        except:
            self.__field_data=UINT(**{'sizeinbytes': 129, 'constant': 0x00})
        return self.__field_data.getvalue()
    def __setfield_data(self, value):
        if isinstance(value,UINT):
            self.__field_data=value
        else:
            self.__field_data=UINT(value,**{'sizeinbytes': 129, 'constant': 0x00})
    def __delfield_data(self): del self.__field_data
    data=property(__getfield_data, __setfield_data, __delfield_data, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('cmd', self.__field_cmd, None)
        yield ('cmd2', self.__field_cmd2, None)
        yield ('cmd3', self.__field_cmd3, None)
        yield ('flag', self.__field_flag, None)
        yield ('data', self.__field_data, None)
class setphoneattribresponse(BaseProtogenClass):
    __fields=['cmd', 'cmd2', 'cmd3', 'flag', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(setphoneattribresponse,self).__init__(**dict)
        if self.__class__ is setphoneattribresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(setphoneattribresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(setphoneattribresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x27})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0xF0})
        self.__field_cmd2.writetobuffer(buf)
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x7F})
        self.__field_cmd3.writetobuffer(buf)
        self.__field_flag.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=DATA()
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x27})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0xF0})
        self.__field_cmd2.readfrombuffer(buf)
        self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x7F})
        self.__field_cmd3.readfrombuffer(buf)
        self.__field_flag=UINT(**{'sizeinbytes': 1})
        self.__field_flag.readfrombuffer(buf)
        self.__field_pad=DATA()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x27})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0x27})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_cmd2(self):
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0xF0})
        return self.__field_cmd2.getvalue()
    def __setfield_cmd2(self, value):
        if isinstance(value,UINT):
            self.__field_cmd2=value
        else:
            self.__field_cmd2=UINT(value,**{'sizeinbytes': 1, 'constant': 0xF0})
    def __delfield_cmd2(self): del self.__field_cmd2
    cmd2=property(__getfield_cmd2, __setfield_cmd2, __delfield_cmd2, None)
    def __getfield_cmd3(self):
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x7F})
        return self.__field_cmd3.getvalue()
    def __setfield_cmd3(self, value):
        if isinstance(value,UINT):
            self.__field_cmd3=value
        else:
            self.__field_cmd3=UINT(value,**{'sizeinbytes': 1, 'constant': 0x7F})
    def __delfield_cmd3(self): del self.__field_cmd3
    cmd3=property(__getfield_cmd3, __setfield_cmd3, __delfield_cmd3, None)
    def __getfield_flag(self):
        return self.__field_flag.getvalue()
    def __setfield_flag(self, value):
        if isinstance(value,UINT):
            self.__field_flag=value
        else:
            self.__field_flag=UINT(value,**{'sizeinbytes': 1})
    def __delfield_flag(self): del self.__field_flag
    flag=property(__getfield_flag, __setfield_flag, __delfield_flag, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=DATA()
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,DATA):
            self.__field_pad=value
        else:
            self.__field_pad=DATA(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('cmd', self.__field_cmd, None)
        yield ('cmd2', self.__field_cmd2, None)
        yield ('cmd3', self.__field_cmd3, None)
        yield ('flag', self.__field_flag, None)
        yield ('pad', self.__field_pad, None)
class tosh_swapheaderrequest(BaseProtogenClass):
    "The bit in front on all toshiba request packets"
    __fields=['cmd', 'cmd2', 'command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_swapheaderrequest,self).__init__(**dict)
        if self.__class__ is tosh_swapheaderrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_swapheaderrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_swapheaderrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0xF1})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0x0F})
        self.__field_cmd2.writetobuffer(buf)
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0xF1})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0x0F})
        self.__field_cmd2.readfrombuffer(buf)
        self.__field_command=UINT(**{'sizeinbytes': 1})
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0xF1})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0xF1})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_cmd2(self):
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0x0F})
        return self.__field_cmd2.getvalue()
    def __setfield_cmd2(self, value):
        if isinstance(value,UINT):
            self.__field_cmd2=value
        else:
            self.__field_cmd2=UINT(value,**{'sizeinbytes': 1, 'constant': 0x0F})
    def __delfield_cmd2(self): del self.__field_cmd2
    cmd2=property(__getfield_cmd2, __setfield_cmd2, __delfield_cmd2, None)
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,UINT):
            self.__field_command=value
        else:
            self.__field_command=UINT(value,**{'sizeinbytes': 1})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('cmd', self.__field_cmd, None)
        yield ('cmd2', self.__field_cmd2, None)
        yield ('command', self.__field_command, None)
class tosh_swapheaderresponse(BaseProtogenClass):
    "The bit in front on all toshiba request packets"
    __fields=['cmd', 'cmd2']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_swapheaderresponse,self).__init__(**dict)
        if self.__class__ is tosh_swapheaderresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_swapheaderresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_swapheaderresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0xF1})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0x0F})
        self.__field_cmd2.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0xF1})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0x0F})
        self.__field_cmd2.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0xF1})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0xF1})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_cmd2(self):
        try: self.__field_cmd2
        except:
            self.__field_cmd2=UINT(**{'sizeinbytes': 1, 'constant': 0x0F})
        return self.__field_cmd2.getvalue()
    def __setfield_cmd2(self, value):
        if isinstance(value,UINT):
            self.__field_cmd2=value
        else:
            self.__field_cmd2=UINT(value,**{'sizeinbytes': 1, 'constant': 0x0F})
    def __delfield_cmd2(self): del self.__field_cmd2
    cmd2=property(__getfield_cmd2, __setfield_cmd2, __delfield_cmd2, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('cmd', self.__field_cmd, None)
        yield ('cmd2', self.__field_cmd2, None)
class tosh_getpbentryrequest(BaseProtogenClass):
    """
    Read an entry from a slot
    """
    __fields=['header', 'cmd', 'pad', 'data_type', 'entry_index', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_getpbentryrequest,self).__init__(**dict)
        if self.__class__ is tosh_getpbentryrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_getpbentryrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_getpbentryrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.writetobuffer(buf)
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.writetobuffer(buf)
        self.__field_entry_index.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.readfrombuffer(buf)
        self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.readfrombuffer(buf)
        self.__field_entry_index=UINT(**{'sizeinbytes': 2})
        self.__field_entry_index.readfrombuffer(buf)
        self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderrequest):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderrequest(value,**{'command': 0x02})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 2, 'constant': 0x03})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UINT):
            self.__field_pad=value
        else:
            self.__field_pad=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def __getfield_data_type(self):
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        return self.__field_data_type.getvalue()
    def __setfield_data_type(self, value):
        if isinstance(value,UINT):
            self.__field_data_type=value
        else:
            self.__field_data_type=UINT(value,**{'sizeinbytes': 2, 'constant': 0x04})
    def __delfield_data_type(self): del self.__field_data_type
    data_type=property(__getfield_data_type, __setfield_data_type, __delfield_data_type, None)
    def __getfield_entry_index(self):
        return self.__field_entry_index.getvalue()
    def __setfield_entry_index(self, value):
        if isinstance(value,UINT):
            self.__field_entry_index=value
        else:
            self.__field_entry_index=UINT(value,**{'sizeinbytes': 2})
    def __delfield_entry_index(self): del self.__field_entry_index
    entry_index=property(__getfield_entry_index, __setfield_entry_index, __delfield_entry_index, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UINT):
            self.__field_pad=value
        else:
            self.__field_pad=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('pad', self.__field_pad, None)
        yield ('data_type', self.__field_data_type, None)
        yield ('entry_index', self.__field_entry_index, None)
        yield ('pad', self.__field_pad, None)
class tosh_getpbentryresponse(BaseProtogenClass):
    __fields=['header', 'cmd', 'read', 'data_type', 'swap_ok']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_getpbentryresponse,self).__init__(**dict)
        if self.__class__ is tosh_getpbentryresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_getpbentryresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_getpbentryresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_read
        except:
            self.__field_read=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_read.writetobuffer(buf)
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.writetobuffer(buf)
        self.__field_swap_ok.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderresponse()
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_read=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_read.readfrombuffer(buf)
        self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.readfrombuffer(buf)
        self.__field_swap_ok=UINT(**{'sizeinbytes': 4})
        self.__field_swap_ok.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderresponse):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderresponse(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0x02})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_read(self):
        try: self.__field_read
        except:
            self.__field_read=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_read.getvalue()
    def __setfield_read(self, value):
        if isinstance(value,UINT):
            self.__field_read=value
        else:
            self.__field_read=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_read(self): del self.__field_read
    read=property(__getfield_read, __setfield_read, __delfield_read, None)
    def __getfield_data_type(self):
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        return self.__field_data_type.getvalue()
    def __setfield_data_type(self, value):
        if isinstance(value,UINT):
            self.__field_data_type=value
        else:
            self.__field_data_type=UINT(value,**{'sizeinbytes': 2, 'constant': 0x04})
    def __delfield_data_type(self): del self.__field_data_type
    data_type=property(__getfield_data_type, __setfield_data_type, __delfield_data_type, None)
    def __getfield_swap_ok(self):
        return self.__field_swap_ok.getvalue()
    def __setfield_swap_ok(self, value):
        if isinstance(value,UINT):
            self.__field_swap_ok=value
        else:
            self.__field_swap_ok=UINT(value,**{'sizeinbytes': 4})
    def __delfield_swap_ok(self): del self.__field_swap_ok
    swap_ok=property(__getfield_swap_ok, __setfield_swap_ok, __delfield_swap_ok, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('read', self.__field_read, None)
        yield ('data_type', self.__field_data_type, None)
        yield ('swap_ok', self.__field_swap_ok, None)
class tosh_setpbentryrequest(BaseProtogenClass):
    """
    Inserts a new entry into an empty slot
    """
    __fields=['header', 'cmd', 'write', 'data_type', 'entry_index', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_setpbentryrequest,self).__init__(**dict)
        if self.__class__ is tosh_setpbentryrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_setpbentryrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_setpbentryrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_write
        except:
            self.__field_write=UINT(**{'sizeinbytes': 2, 'constant': 0x100})
        self.__field_write.writetobuffer(buf)
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.writetobuffer(buf)
        self.__field_entry_index.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_write=UINT(**{'sizeinbytes': 2, 'constant': 0x100})
        self.__field_write.readfrombuffer(buf)
        self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.readfrombuffer(buf)
        self.__field_entry_index=UINT(**{'sizeinbytes': 2})
        self.__field_entry_index.readfrombuffer(buf)
        self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderrequest):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderrequest(value,**{'command': 0x02})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 2, 'constant': 0x03})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_write(self):
        try: self.__field_write
        except:
            self.__field_write=UINT(**{'sizeinbytes': 2, 'constant': 0x100})
        return self.__field_write.getvalue()
    def __setfield_write(self, value):
        if isinstance(value,UINT):
            self.__field_write=value
        else:
            self.__field_write=UINT(value,**{'sizeinbytes': 2, 'constant': 0x100})
    def __delfield_write(self): del self.__field_write
    write=property(__getfield_write, __setfield_write, __delfield_write, None)
    def __getfield_data_type(self):
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        return self.__field_data_type.getvalue()
    def __setfield_data_type(self, value):
        if isinstance(value,UINT):
            self.__field_data_type=value
        else:
            self.__field_data_type=UINT(value,**{'sizeinbytes': 2, 'constant': 0x04})
    def __delfield_data_type(self): del self.__field_data_type
    data_type=property(__getfield_data_type, __setfield_data_type, __delfield_data_type, None)
    def __getfield_entry_index(self):
        return self.__field_entry_index.getvalue()
    def __setfield_entry_index(self, value):
        if isinstance(value,UINT):
            self.__field_entry_index=value
        else:
            self.__field_entry_index=UINT(value,**{'sizeinbytes': 2})
    def __delfield_entry_index(self): del self.__field_entry_index
    entry_index=property(__getfield_entry_index, __setfield_entry_index, __delfield_entry_index, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UINT):
            self.__field_pad=value
        else:
            self.__field_pad=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('write', self.__field_write, None)
        yield ('data_type', self.__field_data_type, None)
        yield ('entry_index', self.__field_entry_index, None)
        yield ('pad', self.__field_pad, None)
class tosh_setpbentryresponse(BaseProtogenClass):
    __fields=['header', 'cmd', 'swap_ok']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_setpbentryresponse,self).__init__(**dict)
        if self.__class__ is tosh_setpbentryresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_setpbentryresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_setpbentryresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        self.__field_cmd.writetobuffer(buf)
        self.__field_swap_ok.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderresponse()
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_swap_ok=UINT(**{'sizeinbytes': 4})
        self.__field_swap_ok.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderresponse):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderresponse(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0x02})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_swap_ok(self):
        return self.__field_swap_ok.getvalue()
    def __setfield_swap_ok(self, value):
        if isinstance(value,UINT):
            self.__field_swap_ok=value
        else:
            self.__field_swap_ok=UINT(value,**{'sizeinbytes': 4})
    def __delfield_swap_ok(self): del self.__field_swap_ok
    swap_ok=property(__getfield_swap_ok, __setfield_swap_ok, __delfield_swap_ok, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('swap_ok', self.__field_swap_ok, None)
class tosh_modifypbentryrequest(BaseProtogenClass):
    """
    Modifies/deletes an existing entry
    delete occurs if the swap file does not exist when this command
    is issued
    """
    __fields=['header', 'cmd', 'write', 'data_type', 'entry_index', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_modifypbentryrequest,self).__init__(**dict)
        if self.__class__ is tosh_modifypbentryrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_modifypbentryrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_modifypbentryrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        self.__field_cmd.writetobuffer(buf)
        try: self.__field_write
        except:
            self.__field_write=UINT(**{'sizeinbytes': 2, 'constant': 0x200})
        self.__field_write.writetobuffer(buf)
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.writetobuffer(buf)
        self.__field_entry_index.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_write=UINT(**{'sizeinbytes': 2, 'constant': 0x200})
        self.__field_write.readfrombuffer(buf)
        self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        self.__field_data_type.readfrombuffer(buf)
        self.__field_entry_index=UINT(**{'sizeinbytes': 2})
        self.__field_entry_index.readfrombuffer(buf)
        self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderrequest):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderrequest(value,**{'command': 0x02})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x03})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 2, 'constant': 0x03})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_write(self):
        try: self.__field_write
        except:
            self.__field_write=UINT(**{'sizeinbytes': 2, 'constant': 0x200})
        return self.__field_write.getvalue()
    def __setfield_write(self, value):
        if isinstance(value,UINT):
            self.__field_write=value
        else:
            self.__field_write=UINT(value,**{'sizeinbytes': 2, 'constant': 0x200})
    def __delfield_write(self): del self.__field_write
    write=property(__getfield_write, __setfield_write, __delfield_write, None)
    def __getfield_data_type(self):
        try: self.__field_data_type
        except:
            self.__field_data_type=UINT(**{'sizeinbytes': 2, 'constant': 0x04})
        return self.__field_data_type.getvalue()
    def __setfield_data_type(self, value):
        if isinstance(value,UINT):
            self.__field_data_type=value
        else:
            self.__field_data_type=UINT(value,**{'sizeinbytes': 2, 'constant': 0x04})
    def __delfield_data_type(self): del self.__field_data_type
    data_type=property(__getfield_data_type, __setfield_data_type, __delfield_data_type, None)
    def __getfield_entry_index(self):
        return self.__field_entry_index.getvalue()
    def __setfield_entry_index(self, value):
        if isinstance(value,UINT):
            self.__field_entry_index=value
        else:
            self.__field_entry_index=UINT(value,**{'sizeinbytes': 2})
    def __delfield_entry_index(self): del self.__field_entry_index
    entry_index=property(__getfield_entry_index, __setfield_entry_index, __delfield_entry_index, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UINT):
            self.__field_pad=value
        else:
            self.__field_pad=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('write', self.__field_write, None)
        yield ('data_type', self.__field_data_type, None)
        yield ('entry_index', self.__field_entry_index, None)
        yield ('pad', self.__field_pad, None)
class tosh_modifypbentryresponse(BaseProtogenClass):
    __fields=['header', 'cmd', 'swap_ok']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_modifypbentryresponse,self).__init__(**dict)
        if self.__class__ is tosh_modifypbentryresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_modifypbentryresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_modifypbentryresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        self.__field_cmd.writetobuffer(buf)
        self.__field_swap_ok.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderresponse()
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_swap_ok=UINT(**{'sizeinbytes': 4})
        self.__field_swap_ok.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderresponse):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderresponse(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 1, 'constant': 0x02})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 1, 'constant': 0x02})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_swap_ok(self):
        return self.__field_swap_ok.getvalue()
    def __setfield_swap_ok(self, value):
        if isinstance(value,UINT):
            self.__field_swap_ok=value
        else:
            self.__field_swap_ok=UINT(value,**{'sizeinbytes': 4})
    def __delfield_swap_ok(self): del self.__field_swap_ok
    swap_ok=property(__getfield_swap_ok, __setfield_swap_ok, __delfield_swap_ok, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('swap_ok', self.__field_swap_ok, None)
class tosh_enableswapdatarequest(BaseProtogenClass):
    __fields=['header']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_enableswapdatarequest,self).__init__(**dict)
        if self.__class__ is tosh_enableswapdatarequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_enableswapdatarequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_enableswapdatarequest,kwargs)
        if len(args):
            dict2={'command': 0x00}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_header=tosh_swapheaderrequest(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x00})
        self.__field_header.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderrequest(**{'command': 0x00})
        self.__field_header.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x00})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderrequest):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderrequest(value,**{'command': 0x00})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
class tosh_enableswapdataresponse(BaseProtogenClass):
    __fields=['header', 'cmd3', 'cmd4']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_enableswapdataresponse,self).__init__(**dict)
        if self.__class__ is tosh_enableswapdataresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_enableswapdataresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_enableswapdataresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x00})
        self.__field_cmd3.writetobuffer(buf)
        try: self.__field_cmd4
        except:
            self.__field_cmd4=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_cmd4.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderresponse()
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x00})
        self.__field_cmd3.readfrombuffer(buf)
        self.__field_cmd4=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_cmd4.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderresponse):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderresponse(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd3(self):
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x00})
        return self.__field_cmd3.getvalue()
    def __setfield_cmd3(self, value):
        if isinstance(value,UINT):
            self.__field_cmd3=value
        else:
            self.__field_cmd3=UINT(value,**{'sizeinbytes': 1, 'constant': 0x00})
    def __delfield_cmd3(self): del self.__field_cmd3
    cmd3=property(__getfield_cmd3, __setfield_cmd3, __delfield_cmd3, None)
    def __getfield_cmd4(self):
        try: self.__field_cmd4
        except:
            self.__field_cmd4=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_cmd4.getvalue()
    def __setfield_cmd4(self, value):
        if isinstance(value,UINT):
            self.__field_cmd4=value
        else:
            self.__field_cmd4=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_cmd4(self): del self.__field_cmd4
    cmd4=property(__getfield_cmd4, __setfield_cmd4, __delfield_cmd4, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd3', self.__field_cmd3, None)
        yield ('cmd4', self.__field_cmd4, None)
class tosh_disableswapdatarequest(BaseProtogenClass):
    __fields=['header']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_disableswapdatarequest,self).__init__(**dict)
        if self.__class__ is tosh_disableswapdatarequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_disableswapdatarequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_disableswapdatarequest,kwargs)
        if len(args):
            dict2={'command': 0x01}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_header=tosh_swapheaderrequest(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x01})
        self.__field_header.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderrequest(**{'command': 0x01})
        self.__field_header.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x01})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderrequest):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderrequest(value,**{'command': 0x01})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
class tosh_disableswapdataresponse(BaseProtogenClass):
    __fields=['header', 'cmd3', 'cmd4']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_disableswapdataresponse,self).__init__(**dict)
        if self.__class__ is tosh_disableswapdataresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_disableswapdataresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_disableswapdataresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        self.__field_header.writetobuffer(buf)
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x01})
        self.__field_cmd3.writetobuffer(buf)
        try: self.__field_cmd4
        except:
            self.__field_cmd4=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_cmd4.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderresponse()
        self.__field_header.readfrombuffer(buf)
        self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x01})
        self.__field_cmd3.readfrombuffer(buf)
        self.__field_cmd4=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_cmd4.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderresponse):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderresponse(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_cmd3(self):
        try: self.__field_cmd3
        except:
            self.__field_cmd3=UINT(**{'sizeinbytes': 1, 'constant': 0x01})
        return self.__field_cmd3.getvalue()
    def __setfield_cmd3(self, value):
        if isinstance(value,UINT):
            self.__field_cmd3=value
        else:
            self.__field_cmd3=UINT(value,**{'sizeinbytes': 1, 'constant': 0x01})
    def __delfield_cmd3(self): del self.__field_cmd3
    cmd3=property(__getfield_cmd3, __setfield_cmd3, __delfield_cmd3, None)
    def __getfield_cmd4(self):
        try: self.__field_cmd4
        except:
            self.__field_cmd4=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_cmd4.getvalue()
    def __setfield_cmd4(self, value):
        if isinstance(value,UINT):
            self.__field_cmd4=value
        else:
            self.__field_cmd4=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_cmd4(self): del self.__field_cmd4
    cmd4=property(__getfield_cmd4, __setfield_cmd4, __delfield_cmd4, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('cmd3', self.__field_cmd3, None)
        yield ('cmd4', self.__field_cmd4, None)
class tosh_getunknownrecordrequest(BaseProtogenClass):
    __fields=['header', 'data_type', 'pad', 'cmd', 'data_index', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_getunknownrecordrequest,self).__init__(**dict)
        if self.__class__ is tosh_getunknownrecordrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_getunknownrecordrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_getunknownrecordrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.writetobuffer(buf)
        self.__field_data_type.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.writetobuffer(buf)
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_cmd.writetobuffer(buf)
        self.__field_data_index.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        self.__field_header.readfrombuffer(buf)
        self.__field_data_type=UINT(**{'sizeinbytes': 2})
        self.__field_data_type.readfrombuffer(buf)
        self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.readfrombuffer(buf)
        self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_cmd.readfrombuffer(buf)
        self.__field_data_index=UINT(**{'sizeinbytes': 2})
        self.__field_data_index.readfrombuffer(buf)
        self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderrequest(**{'command': 0x02})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderrequest):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderrequest(value,**{'command': 0x02})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_data_type(self):
        return self.__field_data_type.getvalue()
    def __setfield_data_type(self, value):
        if isinstance(value,UINT):
            self.__field_data_type=value
        else:
            self.__field_data_type=UINT(value,**{'sizeinbytes': 2})
    def __delfield_data_type(self): del self.__field_data_type
    data_type=property(__getfield_data_type, __setfield_data_type, __delfield_data_type, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UINT):
            self.__field_pad=value
        else:
            self.__field_pad=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def __getfield_cmd(self):
        try: self.__field_cmd
        except:
            self.__field_cmd=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_cmd.getvalue()
    def __setfield_cmd(self, value):
        if isinstance(value,UINT):
            self.__field_cmd=value
        else:
            self.__field_cmd=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_cmd(self): del self.__field_cmd
    cmd=property(__getfield_cmd, __setfield_cmd, __delfield_cmd, None)
    def __getfield_data_index(self):
        return self.__field_data_index.getvalue()
    def __setfield_data_index(self, value):
        if isinstance(value,UINT):
            self.__field_data_index=value
        else:
            self.__field_data_index=UINT(value,**{'sizeinbytes': 2})
    def __delfield_data_index(self): del self.__field_data_index
    data_index=property(__getfield_data_index, __setfield_data_index, __delfield_data_index, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UINT(**{'sizeinbytes': 2, 'constant': 0x00})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UINT):
            self.__field_pad=value
        else:
            self.__field_pad=UINT(value,**{'sizeinbytes': 2, 'constant': 0x00})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('data_type', self.__field_data_type, None)
        yield ('pad', self.__field_pad, None)
        yield ('cmd', self.__field_cmd, None)
        yield ('data_index', self.__field_data_index, None)
        yield ('pad', self.__field_pad, None)
class tosh_getunknownrecordresponse(BaseProtogenClass):
    __fields=['header', 'data']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(tosh_getunknownrecordresponse,self).__init__(**dict)
        if self.__class__ is tosh_getunknownrecordresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(tosh_getunknownrecordresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(tosh_getunknownrecordresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        self.__field_header.writetobuffer(buf)
        try: self.__field_data
        except:
            self.__field_data=DATA()
        self.__field_data.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=tosh_swapheaderresponse()
        self.__field_header.readfrombuffer(buf)
        self.__field_data=DATA()
        self.__field_data.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=tosh_swapheaderresponse()
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,tosh_swapheaderresponse):
            self.__field_header=value
        else:
            self.__field_header=tosh_swapheaderresponse(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_data(self):
        try: self.__field_data
        except:
            self.__field_data=DATA()
        return self.__field_data.getvalue()
    def __setfield_data(self, value):
        if isinstance(value,DATA):
            self.__field_data=value
        else:
            self.__field_data=DATA(value,)
    def __delfield_data(self): del self.__field_data
    data=property(__getfield_data, __setfield_data, __delfield_data, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('data', self.__field_data, None)
