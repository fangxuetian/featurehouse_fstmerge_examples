"""Various descriptions of data specific to GSM phones"""
from prototypes import *
from prototypeslg import *
UINT=UINTlsb
BOOL=BOOLlsb
class echo_off(BaseProtogenClass):
    __fields=['command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(echo_off,self).__init__(**dict)
        if self.__class__ is echo_off:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(echo_off,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(echo_off,kwargs)
        if len(args):
            dict2={ 'terminator': None, 'default': 'E0V1' }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_command=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': 'E0V1' })
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=STRING(**{ 'terminator': None, 'default': 'E0V1' })
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': 'E0V1' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,STRING):
            self.__field_command=value
        else:
            self.__field_command=STRING(value,**{ 'terminator': None, 'default': 'E0V1' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
class esnrequest(BaseProtogenClass):
    __fields=['command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(esnrequest,self).__init__(**dict)
        if self.__class__ is esnrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(esnrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(esnrequest,kwargs)
        if len(args):
            dict2={ 'terminator': None, 'default': '+GSN' }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_command=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GSN' })
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=STRING(**{ 'terminator': None, 'default': '+GSN' })
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GSN' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,STRING):
            self.__field_command=value
        else:
            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+GSN' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
class esnresponse(BaseProtogenClass):
    __fields=['command', 'esn']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(esnresponse,self).__init__(**dict)
        if self.__class__ is esnresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(esnresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(esnresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_command.writetobuffer(buf)
        self.__field_esn.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=CSVSTRING(**{'quotechar': None, 'terminator': ord(' '), 'default': '+GSN'})
        self.__field_command.readfrombuffer(buf)
        self.__field_esn=CSVSTRING(**{'quotechar': None, 'terminator': None})
        self.__field_esn.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_command=value
        else:
            self.__field_command=CSVSTRING(value,**{'quotechar': None, 'terminator': ord(' '), 'default': '+GSN'})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_esn(self):
        return self.__field_esn.getvalue()
    def __setfield_esn(self, value):
        if isinstance(value,CSVSTRING):
            self.__field_esn=value
        else:
            self.__field_esn=CSVSTRING(value,**{'quotechar': None, 'terminator': None})
    def __delfield_esn(self): del self.__field_esn
    esn=property(__getfield_esn, __setfield_esn, __delfield_esn, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
        yield ('esn', self.__field_esn, None)
class SIM_ID_Req(BaseProtogenClass):
    __fields=['command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(SIM_ID_Req,self).__init__(**dict)
        if self.__class__ is SIM_ID_Req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(SIM_ID_Req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(SIM_ID_Req,kwargs)
        if len(args):
            dict2={ 'terminator': None, 'default': '+CIMI' }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_command=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CIMI' })
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=STRING(**{ 'terminator': None, 'default': '+CIMI' })
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+CIMI' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,STRING):
            self.__field_command=value
        else:
            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+CIMI' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
class single_value_resp(BaseProtogenClass):
    __fields=['value']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(single_value_resp,self).__init__(**dict)
        if self.__class__ is single_value_resp:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(single_value_resp,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(single_value_resp,kwargs)
        if len(args):
            dict2={ 'terminator': None }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_value=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_value.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_value=STRING(**{ 'terminator': None })
        self.__field_value.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_value(self):
        return self.__field_value.getvalue()
    def __setfield_value(self, value):
        if isinstance(value,STRING):
            self.__field_value=value
        else:
            self.__field_value=STRING(value,**{ 'terminator': None })
    def __delfield_value(self): del self.__field_value
    value=property(__getfield_value, __setfield_value, __delfield_value, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('value', self.__field_value, None)
class manufacturer_id_req(BaseProtogenClass):
    __fields=['command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(manufacturer_id_req,self).__init__(**dict)
        if self.__class__ is manufacturer_id_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(manufacturer_id_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(manufacturer_id_req,kwargs)
        if len(args):
            dict2={ 'terminator': None, 'default': '+GMI'}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_command=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMI'})
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMI'})
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMI'})
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,STRING):
            self.__field_command=value
        else:
            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+GMI'})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
class model_id_req(BaseProtogenClass):
    __fields=['command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(model_id_req,self).__init__(**dict)
        if self.__class__ is model_id_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(model_id_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(model_id_req,kwargs)
        if len(args):
            dict2={ 'terminator': None, 'default': '+GMM' }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_command=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMM' })
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMM' })
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMM' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,STRING):
            self.__field_command=value
        else:
            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+GMM' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
class firmware_version_req(BaseProtogenClass):
    __fields=['command']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(firmware_version_req,self).__init__(**dict)
        if self.__class__ is firmware_version_req:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(firmware_version_req,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(firmware_version_req,kwargs)
        if len(args):
            dict2={ 'terminator': None, 'default': '+GMR' }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_command=STRING(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMR' })
        self.__field_command.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMR' })
        self.__field_command.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_command(self):
        try: self.__field_command
        except:
            self.__field_command=STRING(**{ 'terminator': None, 'default': '+GMR' })
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,STRING):
            self.__field_command=value
        else:
            self.__field_command=STRING(value,**{ 'terminator': None, 'default': '+GMR' })
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('command', self.__field_command, None)
