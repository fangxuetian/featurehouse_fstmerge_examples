from prototypes import *
max_ringtone_entries=40
max_image_entries=30
ringtone_index_file_name='nvm/nvm/brew_melody'
ringtone_file_path='brew/ringer'
image_index_file_name='nvm/nvm/brew_image'
image_file_path='brew/shared'
cam_pix_file_path='digital_cam'
mms_image_path='mms_image'
UINT=UINTlsb
BOOL=BOOLlsb
class ringtone(BaseProtogenClass):
    __fields=['c0', 'index', 'c1', 'assignment', 'c2', 'name', 'name_len', 'file_name', 'file_name_len', 'c3']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(ringtone,self).__init__(**dict)
        if self.__class__ is ringtone:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(ringtone,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(ringtone,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_c0.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_c1.writetobuffer(buf)
        self.__field_assignment.writetobuffer(buf)
        self.__field_c2.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        self.__field_name_len.writetobuffer(buf)
        self.__field_file_name.writetobuffer(buf)
        self.__field_file_name_len.writetobuffer(buf)
        self.__field_c3.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_c0=UINT(**{'sizeinbytes': 1})
        self.__field_c0.readfrombuffer(buf)
        self.__field_index=UINT(**{'sizeinbytes': 1})
        self.__field_index.readfrombuffer(buf)
        self.__field_c1=UINT(**{'sizeinbytes': 1})
        self.__field_c1.readfrombuffer(buf)
        self.__field_assignment=UINT(**{'sizeinbytes': 1})
        self.__field_assignment.readfrombuffer(buf)
        self.__field_c2=UINT(**{'sizeinbytes': 1})
        self.__field_c2.readfrombuffer(buf)
        self.__field_name=STRING(**{'sizeinbytes': 17,  'raiseonunterminatedread': False })
        self.__field_name.readfrombuffer(buf)
        self.__field_name_len=UINT(**{'sizeinbytes': 1})
        self.__field_name_len.readfrombuffer(buf)
        self.__field_file_name=STRING(**{'sizeinbytes': 51,  'raiseonunterminatedread': False })
        self.__field_file_name.readfrombuffer(buf)
        self.__field_file_name_len=UINT(**{'sizeinbytes': 1})
        self.__field_file_name_len.readfrombuffer(buf)
        self.__field_c3=UINT(**{'sizeinbytes': 2})
        self.__field_c3.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_c0(self):
        return self.__field_c0.getvalue()
    def __setfield_c0(self, value):
        if isinstance(value,UINT):
            self.__field_c0=value
        else:
            self.__field_c0=UINT(value,**{'sizeinbytes': 1})
    def __delfield_c0(self): del self.__field_c0
    c0=property(__getfield_c0, __setfield_c0, __delfield_c0, None)
    def __getfield_index(self):
        return self.__field_index.getvalue()
    def __setfield_index(self, value):
        if isinstance(value,UINT):
            self.__field_index=value
        else:
            self.__field_index=UINT(value,**{'sizeinbytes': 1})
    def __delfield_index(self): del self.__field_index
    index=property(__getfield_index, __setfield_index, __delfield_index, None)
    def __getfield_c1(self):
        return self.__field_c1.getvalue()
    def __setfield_c1(self, value):
        if isinstance(value,UINT):
            self.__field_c1=value
        else:
            self.__field_c1=UINT(value,**{'sizeinbytes': 1})
    def __delfield_c1(self): del self.__field_c1
    c1=property(__getfield_c1, __setfield_c1, __delfield_c1, None)
    def __getfield_assignment(self):
        return self.__field_assignment.getvalue()
    def __setfield_assignment(self, value):
        if isinstance(value,UINT):
            self.__field_assignment=value
        else:
            self.__field_assignment=UINT(value,**{'sizeinbytes': 1})
    def __delfield_assignment(self): del self.__field_assignment
    assignment=property(__getfield_assignment, __setfield_assignment, __delfield_assignment, None)
    def __getfield_c2(self):
        return self.__field_c2.getvalue()
    def __setfield_c2(self, value):
        if isinstance(value,UINT):
            self.__field_c2=value
        else:
            self.__field_c2=UINT(value,**{'sizeinbytes': 1})
    def __delfield_c2(self): del self.__field_c2
    c2=property(__getfield_c2, __setfield_c2, __delfield_c2, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,STRING):
            self.__field_name=value
        else:
            self.__field_name=STRING(value,**{'sizeinbytes': 17,  'raiseonunterminatedread': False })
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_name_len(self):
        return self.__field_name_len.getvalue()
    def __setfield_name_len(self, value):
        if isinstance(value,UINT):
            self.__field_name_len=value
        else:
            self.__field_name_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_name_len(self): del self.__field_name_len
    name_len=property(__getfield_name_len, __setfield_name_len, __delfield_name_len, None)
    def __getfield_file_name(self):
        return self.__field_file_name.getvalue()
    def __setfield_file_name(self, value):
        if isinstance(value,STRING):
            self.__field_file_name=value
        else:
            self.__field_file_name=STRING(value,**{'sizeinbytes': 51,  'raiseonunterminatedread': False })
    def __delfield_file_name(self): del self.__field_file_name
    file_name=property(__getfield_file_name, __setfield_file_name, __delfield_file_name, None)
    def __getfield_file_name_len(self):
        return self.__field_file_name_len.getvalue()
    def __setfield_file_name_len(self, value):
        if isinstance(value,UINT):
            self.__field_file_name_len=value
        else:
            self.__field_file_name_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_file_name_len(self): del self.__field_file_name_len
    file_name_len=property(__getfield_file_name_len, __setfield_file_name_len, __delfield_file_name_len, None)
    def __getfield_c3(self):
        return self.__field_c3.getvalue()
    def __setfield_c3(self, value):
        if isinstance(value,UINT):
            self.__field_c3=value
        else:
            self.__field_c3=UINT(value,**{'sizeinbytes': 2})
    def __delfield_c3(self): del self.__field_c3
    c3=property(__getfield_c3, __setfield_c3, __delfield_c3, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('c0', self.__field_c0, None)
        yield ('index', self.__field_index, None)
        yield ('c1', self.__field_c1, None)
        yield ('assignment', self.__field_assignment, None)
        yield ('c2', self.__field_c2, None)
        yield ('name', self.__field_name, None)
        yield ('name_len', self.__field_name_len, None)
        yield ('file_name', self.__field_file_name, None)
        yield ('file_name_len', self.__field_file_name_len, None)
        yield ('c3', self.__field_c3, None)
class ringtones(BaseProtogenClass):
    __fields=['entry']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(ringtones,self).__init__(**dict)
        if self.__class__ is ringtones:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(ringtones,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(ringtones,kwargs)
        if len(args):
            dict2={ 'length': max_ringtone_entries, 'elementclass': ringtone }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_entry=LIST(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_entry=LIST(**{ 'length': max_ringtone_entries, 'elementclass': ringtone })
        self.__field_entry.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,LIST):
            self.__field_entry=value
        else:
            self.__field_entry=LIST(value,**{ 'length': max_ringtone_entries, 'elementclass': ringtone })
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('entry', self.__field_entry, None)
class image(BaseProtogenClass):
    __fields=['c0', 'index', 'c1', 'assignment', 'name', 'name_len', 'file_name', 'file_name_len', 'c2']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(image,self).__init__(**dict)
        if self.__class__ is image:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(image,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(image,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_c0.writetobuffer(buf)
        self.__field_index.writetobuffer(buf)
        self.__field_c1.writetobuffer(buf)
        self.__field_assignment.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        self.__field_name_len.writetobuffer(buf)
        self.__field_file_name.writetobuffer(buf)
        self.__field_file_name_len.writetobuffer(buf)
        self.__field_c2.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_c0=UINT(**{'sizeinbytes': 1})
        self.__field_c0.readfrombuffer(buf)
        self.__field_index=UINT(**{'sizeinbytes': 1})
        self.__field_index.readfrombuffer(buf)
        self.__field_c1=UINT(**{'sizeinbytes': 1})
        self.__field_c1.readfrombuffer(buf)
        self.__field_assignment=UINT(**{'sizeinbytes': 1})
        self.__field_assignment.readfrombuffer(buf)
        self.__field_name=STRING(**{'sizeinbytes': 17,  'raiseonunterminatedread': False })
        self.__field_name.readfrombuffer(buf)
        self.__field_name_len=UINT(**{'sizeinbytes': 1})
        self.__field_name_len.readfrombuffer(buf)
        self.__field_file_name=STRING(**{'sizeinbytes': 51,  'raiseonunterminatedread': False })
        self.__field_file_name.readfrombuffer(buf)
        self.__field_file_name_len=UINT(**{'sizeinbytes': 1})
        self.__field_file_name_len.readfrombuffer(buf)
        self.__field_c2=UINT(**{'sizeinbytes': 2})
        self.__field_c2.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_c0(self):
        return self.__field_c0.getvalue()
    def __setfield_c0(self, value):
        if isinstance(value,UINT):
            self.__field_c0=value
        else:
            self.__field_c0=UINT(value,**{'sizeinbytes': 1})
    def __delfield_c0(self): del self.__field_c0
    c0=property(__getfield_c0, __setfield_c0, __delfield_c0, None)
    def __getfield_index(self):
        return self.__field_index.getvalue()
    def __setfield_index(self, value):
        if isinstance(value,UINT):
            self.__field_index=value
        else:
            self.__field_index=UINT(value,**{'sizeinbytes': 1})
    def __delfield_index(self): del self.__field_index
    index=property(__getfield_index, __setfield_index, __delfield_index, None)
    def __getfield_c1(self):
        return self.__field_c1.getvalue()
    def __setfield_c1(self, value):
        if isinstance(value,UINT):
            self.__field_c1=value
        else:
            self.__field_c1=UINT(value,**{'sizeinbytes': 1})
    def __delfield_c1(self): del self.__field_c1
    c1=property(__getfield_c1, __setfield_c1, __delfield_c1, None)
    def __getfield_assignment(self):
        return self.__field_assignment.getvalue()
    def __setfield_assignment(self, value):
        if isinstance(value,UINT):
            self.__field_assignment=value
        else:
            self.__field_assignment=UINT(value,**{'sizeinbytes': 1})
    def __delfield_assignment(self): del self.__field_assignment
    assignment=property(__getfield_assignment, __setfield_assignment, __delfield_assignment, None)
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,STRING):
            self.__field_name=value
        else:
            self.__field_name=STRING(value,**{'sizeinbytes': 17,  'raiseonunterminatedread': False })
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_name_len(self):
        return self.__field_name_len.getvalue()
    def __setfield_name_len(self, value):
        if isinstance(value,UINT):
            self.__field_name_len=value
        else:
            self.__field_name_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_name_len(self): del self.__field_name_len
    name_len=property(__getfield_name_len, __setfield_name_len, __delfield_name_len, None)
    def __getfield_file_name(self):
        return self.__field_file_name.getvalue()
    def __setfield_file_name(self, value):
        if isinstance(value,STRING):
            self.__field_file_name=value
        else:
            self.__field_file_name=STRING(value,**{'sizeinbytes': 51,  'raiseonunterminatedread': False })
    def __delfield_file_name(self): del self.__field_file_name
    file_name=property(__getfield_file_name, __setfield_file_name, __delfield_file_name, None)
    def __getfield_file_name_len(self):
        return self.__field_file_name_len.getvalue()
    def __setfield_file_name_len(self, value):
        if isinstance(value,UINT):
            self.__field_file_name_len=value
        else:
            self.__field_file_name_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_file_name_len(self): del self.__field_file_name_len
    file_name_len=property(__getfield_file_name_len, __setfield_file_name_len, __delfield_file_name_len, None)
    def __getfield_c2(self):
        return self.__field_c2.getvalue()
    def __setfield_c2(self, value):
        if isinstance(value,UINT):
            self.__field_c2=value
        else:
            self.__field_c2=UINT(value,**{'sizeinbytes': 2})
    def __delfield_c2(self): del self.__field_c2
    c2=property(__getfield_c2, __setfield_c2, __delfield_c2, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('c0', self.__field_c0, None)
        yield ('index', self.__field_index, None)
        yield ('c1', self.__field_c1, None)
        yield ('assignment', self.__field_assignment, None)
        yield ('name', self.__field_name, None)
        yield ('name_len', self.__field_name_len, None)
        yield ('file_name', self.__field_file_name, None)
        yield ('file_name_len', self.__field_file_name_len, None)
        yield ('c2', self.__field_c2, None)
class images(BaseProtogenClass):
    __fields=['entry']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(images,self).__init__(**dict)
        if self.__class__ is images:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(images,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(images,kwargs)
        if len(args):
            dict2={ 'length': max_image_entries, 'elementclass': image }
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_entry=LIST(*args,**dict2)
    def writetobuffer(self,buf,autolog=True,logtitle="<written data>"):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_entry.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologwrite(buf, logtitle=logtitle)
    def readfrombuffer(self,buf,autolog=True,logtitle="<read data>"):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        if autolog and self._bufferstartoffset==0: self.autologread(buf, logtitle=logtitle)
        self.__field_entry=LIST(**{ 'length': max_image_entries, 'elementclass': image })
        self.__field_entry.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,LIST):
            self.__field_entry=value
        else:
            self.__field_entry=LIST(value,**{ 'length': max_image_entries, 'elementclass': image })
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('entry', self.__field_entry, None)
