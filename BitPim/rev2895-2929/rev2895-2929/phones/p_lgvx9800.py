"""Various descriptions of data specific to LG VX9800"""

from prototypes import *

from prototypeslg import *

from p_lg import *

from p_lgvx8100 import *

UINT=UINTlsb

BOOL=BOOLlsb

NUMPHONEBOOKENTRIES=1000

NUMEMAILS=2

NUMPHONENUMBERS=5

pb_file_name='pim/pbentry.dat'

wallpaper_id_file_name='pim/pbPictureIdSetAsPath.dat'

WALLPAPER_ID_PATH_MAX_LEN=80

EMPTY_WALLPAPER_ID_PATH='\xff'*WALLPAPER_ID_PATH_MAX_LEN

MEDIA_TYPE_RINGTONE=0x0201

MEDIA_TYPE_IMAGE=0x0100

MEDIA_TYPE_SOUND=0x0402

MEDIA_TYPE_SDIMAGE=0x0008

MEDIA_TYPE_SDSOUND=0x000C

MEDIA_TYPE_VIDEO=0x0304

MEDIA_RINGTONE_DEFAULT_ICON=1

MEDIA_IMAGE_DEFAULT_ICON=0

MEDIA_VIDEO_DEFAULT_ICON=0

NUMCALENDARENTRIES=300

SPEEDDIALINDEX=1

MAXCALENDARDESCRIPTION=32

SMS_CANNED_MAX_ITEMS=18

SMS_CANNED_MAX_LENGTH=101

BREW_FILE_SYSTEM=1

pl_dir='mmc1/my_mp3_playlist'

pl_dir_len=len(pl_dir)+1

pl_extension='.pl'

pl_extension_len=len(pl_extension)

mp3_dir='mmc1/my_mp3'

mp3_dir_len=len(mp3_dir)+1

mp3_index_file='dload/my_mp3.dat'

class  indexentry (BaseProtogenClass) :
	__fields=['index', 'type', 'filename', 'icon', 'date', 'dunno', 'size']
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

        self.__field_type.writetobuffer(buf)

        self.__field_filename.writetobuffer(buf)

        try: self.__field_icon

        except:

            self.__field_icon=UINT(**{'sizeinbytes': 4, 'default':0})

        self.__field_icon.writetobuffer(buf)

        try: self.__field_date

        except:

            self.__field_date=UINT(**{'sizeinbytes': 4, 'default': 0})

        self.__field_date.writetobuffer(buf)

        self.__field_dunno.writetobuffer(buf)

        try: self.__field_size

        except:

            self.__field_size=UINT(**{'sizeinbytes': 4, 'default': 0})

        self.__field_size.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index=UINT(**{'sizeinbytes': 2})

        self.__field_index.readfrombuffer(buf)

        self.__field_type=UINT(**{'sizeinbytes': 2})

        self.__field_type.readfrombuffer(buf)

        self.__field_filename=STRING(**{'sizeinbytes': 80, 'raiseonunterminatedread': False, 'raiseontruncate': False })

        self.__field_filename.readfrombuffer(buf)

        self.__field_icon=UINT(**{'sizeinbytes': 4, 'default':0})

        self.__field_icon.readfrombuffer(buf)

        self.__field_date=UINT(**{'sizeinbytes': 4, 'default': 0})

        self.__field_date.readfrombuffer(buf)

        self.__field_dunno=UINT(**{'sizeinbytes': 4})

        self.__field_dunno.readfrombuffer(buf)

        self.__field_size=UINT(**{'sizeinbytes': 4, 'default': 0})

        self.__field_size.readfrombuffer(buf)

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
	    def __getfield_type(self):

        return self.__field_type.getvalue()

	def __setfield_type(self, value):

        if isinstance(value,UINT):

            self.__field_type=value

        else:

            self.__field_type=UINT(value,**{'sizeinbytes': 2})

	def __delfield_type(self): del self.__field_type

	    type=property(__getfield_type, __setfield_type, __delfield_type, None)
	    def __getfield_filename(self):

        return self.__field_filename.getvalue()

	def __setfield_filename(self, value):

        if isinstance(value,STRING):

            self.__field_filename=value

        else:

            self.__field_filename=STRING(value,**{'sizeinbytes': 80, 'raiseonunterminatedread': False, 'raiseontruncate': False })

	def __delfield_filename(self): del self.__field_filename

	    filename=property(__getfield_filename, __setfield_filename, __delfield_filename, "includes full pathname")
	    def __getfield_icon(self):

        try: self.__field_icon

        except:

            self.__field_icon=UINT(**{'sizeinbytes': 4, 'default':0})

        return self.__field_icon.getvalue()

	def __setfield_icon(self, value):

        if isinstance(value,UINT):

            self.__field_icon=value

        else:

            self.__field_icon=UINT(value,**{'sizeinbytes': 4, 'default':0})

	def __delfield_icon(self): del self.__field_icon

	    icon=property(__getfield_icon, __setfield_icon, __delfield_icon, None)
	    def __getfield_date(self):

        try: self.__field_date

        except:

            self.__field_date=UINT(**{'sizeinbytes': 4, 'default': 0})

        return self.__field_date.getvalue()

	def __setfield_date(self, value):

        if isinstance(value,UINT):

            self.__field_date=value

        else:

            self.__field_date=UINT(value,**{'sizeinbytes': 4, 'default': 0})

	def __delfield_date(self): del self.__field_date

	    date=property(__getfield_date, __setfield_date, __delfield_date, "i think this is bitfield of the date")
	    def __getfield_dunno(self):

        return self.__field_dunno.getvalue()

	def __setfield_dunno(self, value):

        if isinstance(value,UINT):

            self.__field_dunno=value

        else:

            self.__field_dunno=UINT(value,**{'sizeinbytes': 4})

	def __delfield_dunno(self): del self.__field_dunno

	    dunno=property(__getfield_dunno, __setfield_dunno, __delfield_dunno, None)
	    def __getfield_size(self):

        try: self.__field_size

        except:

            self.__field_size=UINT(**{'sizeinbytes': 4, 'default': 0})

        return self.__field_size.getvalue()

	def __setfield_size(self, value):

        if isinstance(value,UINT):

            self.__field_size=value

        else:

            self.__field_size=UINT(value,**{'sizeinbytes': 4, 'default': 0})

	def __delfield_size(self): del self.__field_size

	    size=property(__getfield_size, __setfield_size, __delfield_size, "size of the file, can be set to zero")
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('index', self.__field_index, None)

        yield ('type', self.__field_type, None)

        yield ('filename', self.__field_filename, "includes full pathname")

        yield ('icon', self.__field_icon, None)

        yield ('date', self.__field_date, "i think this is bitfield of the date")

        yield ('dunno', self.__field_dunno, None)

        yield ('size', self.__field_size, "size of the file, can be set to zero")


class  indexfile (BaseProtogenClass) :
	"Used for tracking wallpaper and ringtones"
	    __fields=['items']
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

        if len(args):

            dict2={'elementclass': indexentry, 'createdefault': True}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_items=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_items

        except:

            self.__field_items=LIST(**{'elementclass': indexentry, 'createdefault': True})

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items=LIST(**{'elementclass': indexentry, 'createdefault': True})

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

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

        yield ('items', self.__field_items, None)

	"Used for tracking wallpaper and ringtones"

class  playlistentry (BaseProtogenClass) :
	__fields=['name', 'date', 'dunno1', 'dunno2', 'dunno3']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(playlistentry,self).__init__(**dict)

        if self.__class__ is playlistentry:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(playlistentry,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(playlistentry,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_name.writetobuffer(buf)

        try: self.__field_date

        except:

            self.__field_date=UINT(**{'sizeinbytes': 4,  'default': 0 })

        self.__field_date.writetobuffer(buf)

        try: self.__field_dunno1

        except:

            self.__field_dunno1=UINT(**{'sizeinbytes': 4,  'default': 0 })

        self.__field_dunno1.writetobuffer(buf)

        try: self.__field_dunno2

        except:

            self.__field_dunno2=UINT(**{'sizeinbytes': 4,  'default': 0 })

        self.__field_dunno2.writetobuffer(buf)

        try: self.__field_dunno3

        except:

            self.__field_dunno3=UINT(**{'sizeinbytes': 4,  'default': 1 })

        self.__field_dunno3.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_name=STRING(**{'sizeinbytes': 84,  'raiseonunterminatedread': False, 'raiseontruncate': False })

        self.__field_name.readfrombuffer(buf)

        self.__field_date=UINT(**{'sizeinbytes': 4,  'default': 0 })

        self.__field_date.readfrombuffer(buf)

        self.__field_dunno1=UINT(**{'sizeinbytes': 4,  'default': 0 })

        self.__field_dunno1.readfrombuffer(buf)

        self.__field_dunno2=UINT(**{'sizeinbytes': 4,  'default': 0 })

        self.__field_dunno2.readfrombuffer(buf)

        self.__field_dunno3=UINT(**{'sizeinbytes': 4,  'default': 1 })

        self.__field_dunno3.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 84,  'raiseonunterminatedread': False, 'raiseontruncate': False })

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def __getfield_date(self):

        try: self.__field_date

        except:

            self.__field_date=UINT(**{'sizeinbytes': 4,  'default': 0 })

        return self.__field_date.getvalue()

	def __setfield_date(self, value):

        if isinstance(value,UINT):

            self.__field_date=value

        else:

            self.__field_date=UINT(value,**{'sizeinbytes': 4,  'default': 0 })

	def __delfield_date(self): del self.__field_date

	    date=property(__getfield_date, __setfield_date, __delfield_date, None)
	    def __getfield_dunno1(self):

        try: self.__field_dunno1

        except:

            self.__field_dunno1=UINT(**{'sizeinbytes': 4,  'default': 0 })

        return self.__field_dunno1.getvalue()

	def __setfield_dunno1(self, value):

        if isinstance(value,UINT):

            self.__field_dunno1=value

        else:

            self.__field_dunno1=UINT(value,**{'sizeinbytes': 4,  'default': 0 })

	def __delfield_dunno1(self): del self.__field_dunno1

	    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
	    def __getfield_dunno2(self):

        try: self.__field_dunno2

        except:

            self.__field_dunno2=UINT(**{'sizeinbytes': 4,  'default': 0 })

        return self.__field_dunno2.getvalue()

	def __setfield_dunno2(self, value):

        if isinstance(value,UINT):

            self.__field_dunno2=value

        else:

            self.__field_dunno2=UINT(value,**{'sizeinbytes': 4,  'default': 0 })

	def __delfield_dunno2(self): del self.__field_dunno2

	    dunno2=property(__getfield_dunno2, __setfield_dunno2, __delfield_dunno2, None)
	    def __getfield_dunno3(self):

        try: self.__field_dunno3

        except:

            self.__field_dunno3=UINT(**{'sizeinbytes': 4,  'default': 1 })

        return self.__field_dunno3.getvalue()

	def __setfield_dunno3(self, value):

        if isinstance(value,UINT):

            self.__field_dunno3=value

        else:

            self.__field_dunno3=UINT(value,**{'sizeinbytes': 4,  'default': 1 })

	def __delfield_dunno3(self): del self.__field_dunno3

	    dunno3=property(__getfield_dunno3, __setfield_dunno3, __delfield_dunno3, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('name', self.__field_name, None)

        yield ('date', self.__field_date, None)

        yield ('dunno1', self.__field_dunno1, None)

        yield ('dunno2', self.__field_dunno2, None)

        yield ('dunno3', self.__field_dunno3, None)


class  playlistfile (BaseProtogenClass) :
	__fields=['items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(playlistfile,self).__init__(**dict)

        if self.__class__ is playlistfile:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(playlistfile,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(playlistfile,kwargs)

        if len(args):

            dict2={ 'elementclass': playlistentry }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_items=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_items

        except:

            self.__field_items=LIST(**{ 'elementclass': playlistentry })

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items=LIST(**{ 'elementclass': playlistentry })

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_items(self):

        try: self.__field_items

        except:

            self.__field_items=LIST(**{ 'elementclass': playlistentry })

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{ 'elementclass': playlistentry })

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('items', self.__field_items, None)


class  pbgroup (BaseProtogenClass) :
	"A single group"
	    __fields=['name']
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

        if len(args):

            dict2={'sizeinbytes': 23, 'raiseonunterminatedread': False, 'raiseontruncate': False }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_name=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_name.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_name=STRING(**{'sizeinbytes': 23, 'raiseonunterminatedread': False, 'raiseontruncate': False })

        self.__field_name.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_name(self):

        return self.__field_name.getvalue()

	def __setfield_name(self, value):

        if isinstance(value,STRING):

            self.__field_name=value

        else:

            self.__field_name=STRING(value,**{'sizeinbytes': 23, 'raiseonunterminatedread': False, 'raiseontruncate': False })

	def __delfield_name(self): del self.__field_name

	    name=property(__getfield_name, __setfield_name, __delfield_name, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

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

class  pbinforequest (BaseProtogenClass) :
	"Random information about the phone"
	    __fields=['header', 'pad']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbinforequest,self).__init__(**dict)

        if self.__class__ is pbinforequest:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbinforequest,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbinforequest,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_header

        except:

            self.__field_header=pbheader(**{'command': 0x15, 'flag': 0x01})

        self.__field_header.writetobuffer(buf)

        try: self.__field_pad

        except:

            self.__field_pad=UNKNOWN(**{'sizeinbytes': 6})

        self.__field_pad.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header=pbheader(**{'command': 0x15, 'flag': 0x01})

        self.__field_header.readfrombuffer(buf)

        self.__field_pad=UNKNOWN(**{'sizeinbytes': 6})

        self.__field_pad.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_header(self):

        try: self.__field_header

        except:

            self.__field_header=pbheader(**{'command': 0x15, 'flag': 0x01})

        return self.__field_header.getvalue()

	def __setfield_header(self, value):

        if isinstance(value,pbheader):

            self.__field_header=value

        else:

            self.__field_header=pbheader(value,**{'command': 0x15, 'flag': 0x01})

	def __delfield_header(self): del self.__field_header

	    header=property(__getfield_header, __setfield_header, __delfield_header, None)
	    def __getfield_pad(self):

        try: self.__field_pad

        except:

            self.__field_pad=UNKNOWN(**{'sizeinbytes': 6})

        return self.__field_pad.getvalue()

	def __setfield_pad(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_pad=value

        else:

            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 6})

	def __delfield_pad(self): del self.__field_pad

	    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('header', self.__field_header, None)

        yield ('pad', self.__field_pad, None)

	"Random information about the phone"

class  pbinforesponse (BaseProtogenClass) :
	__fields=['header', 'dunno1', 'firstentry', 'numentries', 'dunno2']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbinforesponse,self).__init__(**dict)

        if self.__class__ is pbinforesponse:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbinforesponse,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbinforesponse,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header.writetobuffer(buf)

        self.__field_dunno1.writetobuffer(buf)

        self.__field_firstentry.writetobuffer(buf)

        self.__field_numentries.writetobuffer(buf)

        self.__field_dunno2.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_header=pbheader()

        self.__field_header.readfrombuffer(buf)

        self.__field_dunno1=UNKNOWN(**{'sizeinbytes': 10})

        self.__field_dunno1.readfrombuffer(buf)

        self.__field_firstentry=UINT(**{'sizeinbytes': 4})

        self.__field_firstentry.readfrombuffer(buf)

        self.__field_numentries=UINT(**{'sizeinbytes': 2})

        self.__field_numentries.readfrombuffer(buf)

        self.__field_dunno2=UNKNOWN()

        self.__field_dunno2.readfrombuffer(buf)

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
	    def __getfield_dunno1(self):

        return self.__field_dunno1.getvalue()

	def __setfield_dunno1(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_dunno1=value

        else:

            self.__field_dunno1=UNKNOWN(value,**{'sizeinbytes': 10})

	def __delfield_dunno1(self): del self.__field_dunno1

	    dunno1=property(__getfield_dunno1, __setfield_dunno1, __delfield_dunno1, None)
	    def __getfield_firstentry(self):

        return self.__field_firstentry.getvalue()

	def __setfield_firstentry(self, value):

        if isinstance(value,UINT):

            self.__field_firstentry=value

        else:

            self.__field_firstentry=UINT(value,**{'sizeinbytes': 4})

	def __delfield_firstentry(self): del self.__field_firstentry

	    firstentry=property(__getfield_firstentry, __setfield_firstentry, __delfield_firstentry, None)
	    def __getfield_numentries(self):

        return self.__field_numentries.getvalue()

	def __setfield_numentries(self, value):

        if isinstance(value,UINT):

            self.__field_numentries=value

        else:

            self.__field_numentries=UINT(value,**{'sizeinbytes': 2})

	def __delfield_numentries(self): del self.__field_numentries

	    numentries=property(__getfield_numentries, __setfield_numentries, __delfield_numentries, None)
	    def __getfield_dunno2(self):

        return self.__field_dunno2.getvalue()

	def __setfield_dunno2(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_dunno2=value

        else:

            self.__field_dunno2=UNKNOWN(value,)

	def __delfield_dunno2(self): del self.__field_dunno2

	    dunno2=property(__getfield_dunno2, __setfield_dunno2, __delfield_dunno2, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('header', self.__field_header, None)

        yield ('dunno1', self.__field_dunno1, None)

        yield ('firstentry', self.__field_firstentry, None)

        yield ('numentries', self.__field_numentries, None)

        yield ('dunno2', self.__field_dunno2, None)


class  pbentry (BaseProtogenClass) :
	__fields=['serial1', 'entrysize', 'serial2', 'entrynumber', 'name', 'group', 'emails', 'ringtone', 'msgringtone', 'wallpaper', 'numbertypes', 'numbers', 'memo', 'unknown']
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

            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x01BE, 'constantexception': PhoneBookBusyException})

        self.__field_entrysize.writetobuffer(buf)

        self.__field_serial2.writetobuffer(buf)

        self.__field_entrynumber.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        try: self.__field_group

        except:

            self.__field_group=UINT(**{'sizeinbytes': 2,  'default': 0 })

        self.__field_group.writetobuffer(buf)

        try: self.__field_emails

        except:

            self.__field_emails=LIST(**{'elementclass': _gen_p_lgvx9800_123, 'length': NUMEMAILS})

        self.__field_emails.writetobuffer(buf)

        try: self.__field_ringtone

        except:

            self.__field_ringtone=UINT(**{'sizeinbytes': 2,  'default': 0xffff })

        self.__field_ringtone.writetobuffer(buf)

        try: self.__field_msgringtone

        except:

            self.__field_msgringtone=UINT(**{'sizeinbytes': 2,  'default': 0xffff })

        self.__field_msgringtone.writetobuffer(buf)

        try: self.__field_wallpaper

        except:

            self.__field_wallpaper=UINT(**{'sizeinbytes': 2,  'default': 0 })

        self.__field_wallpaper.writetobuffer(buf)

        try: self.__field_numbertypes

        except:

            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgvx9800_128, 'length': NUMPHONENUMBERS})

        self.__field_numbertypes.writetobuffer(buf)

        try: self.__field_numbers

        except:

            self.__field_numbers=LIST(**{'elementclass': _gen_p_lgvx9800_130, 'length': NUMPHONENUMBERS})

        self.__field_numbers.writetobuffer(buf)

        try: self.__field_memo

        except:

            self.__field_memo=STRING(**{'sizeinbytes': 61, 'default': '', 'raiseonunterminatedread': False})

        self.__field_memo.writetobuffer(buf)

        try: self.__field_unknown

        except:

            self.__field_unknown=UNKNOWN()

        self.__field_unknown.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1=UINT(**{'sizeinbytes': 4})

        self.__field_serial1.readfrombuffer(buf)

        self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x01BE, 'constantexception': PhoneBookBusyException})

        self.__field_entrysize.readfrombuffer(buf)

        self.__field_serial2=UINT(**{'sizeinbytes': 4})

        self.__field_serial2.readfrombuffer(buf)

        self.__field_entrynumber=UINT(**{'sizeinbytes': 2})

        self.__field_entrynumber.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 23, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self.__field_group=UINT(**{'sizeinbytes': 2,  'default': 0 })

        self.__field_group.readfrombuffer(buf)

        self.__field_emails=LIST(**{'elementclass': _gen_p_lgvx9800_123, 'length': NUMEMAILS})

        self.__field_emails.readfrombuffer(buf)

        self.__field_ringtone=UINT(**{'sizeinbytes': 2,  'default': 0xffff })

        self.__field_ringtone.readfrombuffer(buf)

        self.__field_msgringtone=UINT(**{'sizeinbytes': 2,  'default': 0xffff })

        self.__field_msgringtone.readfrombuffer(buf)

        self.__field_wallpaper=UINT(**{'sizeinbytes': 2,  'default': 0 })

        self.__field_wallpaper.readfrombuffer(buf)

        self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgvx9800_128, 'length': NUMPHONENUMBERS})

        self.__field_numbertypes.readfrombuffer(buf)

        self.__field_numbers=LIST(**{'elementclass': _gen_p_lgvx9800_130, 'length': NUMPHONENUMBERS})

        self.__field_numbers.readfrombuffer(buf)

        self.__field_memo=STRING(**{'sizeinbytes': 61, 'default': '', 'raiseonunterminatedread': False})

        self.__field_memo.readfrombuffer(buf)

        self.__field_unknown=UNKNOWN()

        self.__field_unknown.readfrombuffer(buf)

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

            self.__field_entrysize=UINT(**{'sizeinbytes': 2, 'constant': 0x01BE, 'constantexception': PhoneBookBusyException})

        return self.__field_entrysize.getvalue()

	def __setfield_entrysize(self, value):

        if isinstance(value,UINT):

            self.__field_entrysize=value

        else:

            self.__field_entrysize=UINT(value,**{'sizeinbytes': 2, 'constant': 0x01BE, 'constantexception': PhoneBookBusyException})

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

        try: self.__field_group

        except:

            self.__field_group=UINT(**{'sizeinbytes': 2,  'default': 0 })

        return self.__field_group.getvalue()

	def __setfield_group(self, value):

        if isinstance(value,UINT):

            self.__field_group=value

        else:

            self.__field_group=UINT(value,**{'sizeinbytes': 2,  'default': 0 })

	def __delfield_group(self): del self.__field_group

	    group=property(__getfield_group, __setfield_group, __delfield_group, None)
	    def __getfield_emails(self):

        try: self.__field_emails

        except:

            self.__field_emails=LIST(**{'elementclass': _gen_p_lgvx9800_123, 'length': NUMEMAILS})

        return self.__field_emails.getvalue()

	def __setfield_emails(self, value):

        if isinstance(value,LIST):

            self.__field_emails=value

        else:

            self.__field_emails=LIST(value,**{'elementclass': _gen_p_lgvx9800_123, 'length': NUMEMAILS})

	def __delfield_emails(self): del self.__field_emails

	    emails=property(__getfield_emails, __setfield_emails, __delfield_emails, None)
	    def __getfield_ringtone(self):

        try: self.__field_ringtone

        except:

            self.__field_ringtone=UINT(**{'sizeinbytes': 2,  'default': 0xffff })

        return self.__field_ringtone.getvalue()

	def __setfield_ringtone(self, value):

        if isinstance(value,UINT):

            self.__field_ringtone=value

        else:

            self.__field_ringtone=UINT(value,**{'sizeinbytes': 2,  'default': 0xffff })

	def __delfield_ringtone(self): del self.__field_ringtone

	    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, "ringtone index for a call")
	    def __getfield_msgringtone(self):

        try: self.__field_msgringtone

        except:

            self.__field_msgringtone=UINT(**{'sizeinbytes': 2,  'default': 0xffff })

        return self.__field_msgringtone.getvalue()

	def __setfield_msgringtone(self, value):

        if isinstance(value,UINT):

            self.__field_msgringtone=value

        else:

            self.__field_msgringtone=UINT(value,**{'sizeinbytes': 2,  'default': 0xffff })

	def __delfield_msgringtone(self): del self.__field_msgringtone

	    msgringtone=property(__getfield_msgringtone, __setfield_msgringtone, __delfield_msgringtone, "ringtone index for a text message")
	    def __getfield_wallpaper(self):

        try: self.__field_wallpaper

        except:

            self.__field_wallpaper=UINT(**{'sizeinbytes': 2,  'default': 0 })

        return self.__field_wallpaper.getvalue()

	def __setfield_wallpaper(self, value):

        if isinstance(value,UINT):

            self.__field_wallpaper=value

        else:

            self.__field_wallpaper=UINT(value,**{'sizeinbytes': 2,  'default': 0 })

	def __delfield_wallpaper(self): del self.__field_wallpaper

	    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
	    def __getfield_numbertypes(self):

        try: self.__field_numbertypes

        except:

            self.__field_numbertypes=LIST(**{'elementclass': _gen_p_lgvx9800_128, 'length': NUMPHONENUMBERS})

        return self.__field_numbertypes.getvalue()

	def __setfield_numbertypes(self, value):

        if isinstance(value,LIST):

            self.__field_numbertypes=value

        else:

            self.__field_numbertypes=LIST(value,**{'elementclass': _gen_p_lgvx9800_128, 'length': NUMPHONENUMBERS})

	def __delfield_numbertypes(self): del self.__field_numbertypes

	    numbertypes=property(__getfield_numbertypes, __setfield_numbertypes, __delfield_numbertypes, None)
	    def __getfield_numbers(self):

        try: self.__field_numbers

        except:

            self.__field_numbers=LIST(**{'elementclass': _gen_p_lgvx9800_130, 'length': NUMPHONENUMBERS})

        return self.__field_numbers.getvalue()

	def __setfield_numbers(self, value):

        if isinstance(value,LIST):

            self.__field_numbers=value

        else:

            self.__field_numbers=LIST(value,**{'elementclass': _gen_p_lgvx9800_130, 'length': NUMPHONENUMBERS})

	def __delfield_numbers(self): del self.__field_numbers

	    numbers=property(__getfield_numbers, __setfield_numbers, __delfield_numbers, None)
	    def __getfield_memo(self):

        try: self.__field_memo

        except:

            self.__field_memo=STRING(**{'sizeinbytes': 61, 'default': '', 'raiseonunterminatedread': False})

        return self.__field_memo.getvalue()

	def __setfield_memo(self, value):

        if isinstance(value,STRING):

            self.__field_memo=value

        else:

            self.__field_memo=STRING(value,**{'sizeinbytes': 61, 'default': '', 'raiseonunterminatedread': False})

	def __delfield_memo(self): del self.__field_memo

	    memo=property(__getfield_memo, __setfield_memo, __delfield_memo, None)
	    def __getfield_unknown(self):

        try: self.__field_unknown

        except:

            self.__field_unknown=UNKNOWN()

        return self.__field_unknown.getvalue()

	def __setfield_unknown(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_unknown=value

        else:

            self.__field_unknown=UNKNOWN(value,)

	def __delfield_unknown(self): del self.__field_unknown

	    unknown=property(__getfield_unknown, __setfield_unknown, __delfield_unknown, None)
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

        yield ('ringtone', self.__field_ringtone, "ringtone index for a call")

        yield ('msgringtone', self.__field_msgringtone, "ringtone index for a text message")

        yield ('wallpaper', self.__field_wallpaper, None)

        yield ('numbertypes', self.__field_numbertypes, None)

        yield ('numbers', self.__field_numbers, None)

        yield ('memo', self.__field_memo, None)

        yield ('unknown', self.__field_unknown, None)


class  _gen_p_lgvx9800_123 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['email']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_123,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_123:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_123,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_123,kwargs)

        if len(args):

            dict2={'sizeinbytes': 49, 'raiseonunterminatedread': False}

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

        self.__field_email=STRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

        self.__field_email.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_email(self):

        return self.__field_email.getvalue()

	def __setfield_email(self, value):

        if isinstance(value,STRING):

            self.__field_email=value

        else:

            self.__field_email=STRING(value,**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

	def __delfield_email(self): del self.__field_email

	    email=property(__getfield_email, __setfield_email, __delfield_email, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('email', self.__field_email, None)

	'Anonymous inner class'

class  _gen_p_lgvx9800_128 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['numbertype']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_128,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_128:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_128,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_128,kwargs)

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

class  _gen_p_lgvx9800_130 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['number']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_130,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_130:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_130,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_130,kwargs)

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

class  pbfileentry (BaseProtogenClass) :
	__fields=['serial1', 'entrynumber', 'data1', 'wallpaper', 'data2']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbfileentry,self).__init__(**dict)

        if self.__class__ is pbfileentry:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbfileentry,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbfileentry,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1.writetobuffer(buf)

        self.__field_entrynumber.writetobuffer(buf)

        self.__field_data1.writetobuffer(buf)

        self.__field_wallpaper.writetobuffer(buf)

        self.__field_data2.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_serial1=UINT(**{'sizeinbytes': 4})

        self.__field_serial1.readfrombuffer(buf)

        self.__field_entrynumber=UINT(**{'sizeinbytes': 2})

        self.__field_entrynumber.readfrombuffer(buf)

        self.__field_data1=UNKNOWN(**{'sizeinbytes': 127})

        self.__field_data1.readfrombuffer(buf)

        self.__field_wallpaper=UINT(**{'sizeinbytes': 2})

        self.__field_wallpaper.readfrombuffer(buf)

        self.__field_data2=UNKNOWN(**{'sizeinbytes': 76})

        self.__field_data2.readfrombuffer(buf)

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
	    def __getfield_entrynumber(self):

        return self.__field_entrynumber.getvalue()

	def __setfield_entrynumber(self, value):

        if isinstance(value,UINT):

            self.__field_entrynumber=value

        else:

            self.__field_entrynumber=UINT(value,**{'sizeinbytes': 2})

	def __delfield_entrynumber(self): del self.__field_entrynumber

	    entrynumber=property(__getfield_entrynumber, __setfield_entrynumber, __delfield_entrynumber, None)
	    def __getfield_data1(self):

        return self.__field_data1.getvalue()

	def __setfield_data1(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_data1=value

        else:

            self.__field_data1=UNKNOWN(value,**{'sizeinbytes': 127})

	def __delfield_data1(self): del self.__field_data1

	    data1=property(__getfield_data1, __setfield_data1, __delfield_data1, None)
	    def __getfield_wallpaper(self):

        return self.__field_wallpaper.getvalue()

	def __setfield_wallpaper(self, value):

        if isinstance(value,UINT):

            self.__field_wallpaper=value

        else:

            self.__field_wallpaper=UINT(value,**{'sizeinbytes': 2})

	def __delfield_wallpaper(self): del self.__field_wallpaper

	    wallpaper=property(__getfield_wallpaper, __setfield_wallpaper, __delfield_wallpaper, None)
	    def __getfield_data2(self):

        return self.__field_data2.getvalue()

	def __setfield_data2(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_data2=value

        else:

            self.__field_data2=UNKNOWN(value,**{'sizeinbytes': 76})

	def __delfield_data2(self): del self.__field_data2

	    data2=property(__getfield_data2, __setfield_data2, __delfield_data2, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('serial1', self.__field_serial1, None)

        yield ('entrynumber', self.__field_entrynumber, None)

        yield ('data1', self.__field_data1, None)

        yield ('wallpaper', self.__field_wallpaper, None)

        yield ('data2', self.__field_data2, None)


class  pbfile (BaseProtogenClass) :
	__fields=['items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(pbfile,self).__init__(**dict)

        if self.__class__ is pbfile:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(pbfile,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(pbfile,kwargs)

        if len(args):

            dict2={ 'elementclass': pbfileentry }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_items=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items=LIST(**{ 'elementclass': pbfileentry })

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_items(self):

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{ 'elementclass': pbfileentry })

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('items', self.__field_items, None)


class  wallpaper_id (BaseProtogenClass) :
	__fields=['path']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(wallpaper_id,self).__init__(**dict)

        if self.__class__ is wallpaper_id:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(wallpaper_id,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(wallpaper_id,kwargs)

        if len(args):

            dict2={'sizeinbytes': 80,  'terminator': None,                'default': EMPTY_WALLPAPER_ID_PATH }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_path=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_path

        except:

            self.__field_path=STRING(**{'sizeinbytes': 80,  'terminator': None,                'default': EMPTY_WALLPAPER_ID_PATH })

        self.__field_path.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_path=STRING(**{'sizeinbytes': 80,  'terminator': None,                'default': EMPTY_WALLPAPER_ID_PATH })

        self.__field_path.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_path(self):

        try: self.__field_path

        except:

            self.__field_path=STRING(**{'sizeinbytes': 80,  'terminator': None,                'default': EMPTY_WALLPAPER_ID_PATH })

        return self.__field_path.getvalue()

	def __setfield_path(self, value):

        if isinstance(value,STRING):

            self.__field_path=value

        else:

            self.__field_path=STRING(value,**{'sizeinbytes': 80,  'terminator': None,                'default': EMPTY_WALLPAPER_ID_PATH })

	def __delfield_path(self): del self.__field_path

	    path=property(__getfield_path, __setfield_path, __delfield_path, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('path', self.__field_path, None)


class  wallpaper_id_file (BaseProtogenClass) :
	__fields=['items']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(wallpaper_id_file,self).__init__(**dict)

        if self.__class__ is wallpaper_id_file:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(wallpaper_id_file,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(wallpaper_id_file,kwargs)

        if len(args):

            dict2={ 'length': NUMPHONEBOOKENTRIES,             'elementclass': wallpaper_id,             'createdefault': True }

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_items=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_items

        except:

            self.__field_items=LIST(**{ 'length': NUMPHONEBOOKENTRIES,             'elementclass': wallpaper_id,             'createdefault': True })

        self.__field_items.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_items=LIST(**{ 'length': NUMPHONEBOOKENTRIES,             'elementclass': wallpaper_id,             'createdefault': True })

        self.__field_items.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_items(self):

        try: self.__field_items

        except:

            self.__field_items=LIST(**{ 'length': NUMPHONEBOOKENTRIES,             'elementclass': wallpaper_id,             'createdefault': True })

        return self.__field_items.getvalue()

	def __setfield_items(self, value):

        if isinstance(value,LIST):

            self.__field_items=value

        else:

            self.__field_items=LIST(value,**{ 'length': NUMPHONEBOOKENTRIES,             'elementclass': wallpaper_id,             'createdefault': True })

	def __delfield_items(self): del self.__field_items

	    items=property(__getfield_items, __setfield_items, __delfield_items, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('items', self.__field_items, None)


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
	__fields=['pos', 'description', 'start', 'end', 'repeat', 'alarmindex_vibrate', 'ringtone', 'unknown1', 'alarmminutes', 'alarmhours', 'unknown2']
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

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_pos.writetobuffer(buf)

        self.__field_description.writetobuffer(buf)

        self.__field_start.writetobuffer(buf)

        self.__field_end.writetobuffer(buf)

        self.__field_repeat.writetobuffer(buf)

        self.__field_alarmindex_vibrate.writetobuffer(buf)

        self.__field_ringtone.writetobuffer(buf)

        self.__field_unknown1.writetobuffer(buf)

        self.__field_alarmminutes.writetobuffer(buf)

        self.__field_alarmhours.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_pos=UINT(**{'sizeinbytes': 4})

        self.__field_pos.readfrombuffer(buf)

        self.__field_description=STRING(**{'sizeinbytes': 33, 'raiseonunterminatedread': False, 'raiseontruncate': False })

        self.__field_description.readfrombuffer(buf)

        self.__field_start=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_start.readfrombuffer(buf)

        self.__field_end=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_end.readfrombuffer(buf)

        self.__field_repeat=LGCALREPEAT(**{'sizeinbytes': 4})

        self.__field_repeat.readfrombuffer(buf)

        self.__field_alarmindex_vibrate=UINT(**{'sizeinbytes': 1})

        self.__field_alarmindex_vibrate.readfrombuffer(buf)

        self.__field_ringtone=UINT(**{'sizeinbytes': 1})

        self.__field_ringtone.readfrombuffer(buf)

        self.__field_unknown1=UINT(**{'sizeinbytes': 1})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_alarmminutes=UINT(**{'sizeinbytes': 1})

        self.__field_alarmminutes.readfrombuffer(buf)

        self.__field_alarmhours=UINT(**{'sizeinbytes': 1})

        self.__field_alarmhours.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 1})

        self.__field_unknown2.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_pos(self):

        return self.__field_pos.getvalue()

	def __setfield_pos(self, value):

        if isinstance(value,UINT):

            self.__field_pos=value

        else:

            self.__field_pos=UINT(value,**{'sizeinbytes': 4})

	def __delfield_pos(self): del self.__field_pos

	    pos=property(__getfield_pos, __setfield_pos, __delfield_pos, "position within file, used as an event id")
	    def __getfield_description(self):

        return self.__field_description.getvalue()

	def __setfield_description(self, value):

        if isinstance(value,STRING):

            self.__field_description=value

        else:

            self.__field_description=STRING(value,**{'sizeinbytes': 33, 'raiseonunterminatedread': False, 'raiseontruncate': False })

	def __delfield_description(self): del self.__field_description

	    description=property(__getfield_description, __setfield_description, __delfield_description, None)
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

        if isinstance(value,LGCALREPEAT):

            self.__field_repeat=value

        else:

            self.__field_repeat=LGCALREPEAT(value,**{'sizeinbytes': 4})

	def __delfield_repeat(self): del self.__field_repeat

	    repeat=property(__getfield_repeat, __setfield_repeat, __delfield_repeat, None)
	    def __getfield_alarmindex_vibrate(self):

        return self.__field_alarmindex_vibrate.getvalue()

	def __setfield_alarmindex_vibrate(self, value):

        if isinstance(value,UINT):

            self.__field_alarmindex_vibrate=value

        else:

            self.__field_alarmindex_vibrate=UINT(value,**{'sizeinbytes': 1})

	def __delfield_alarmindex_vibrate(self): del self.__field_alarmindex_vibrate

	    alarmindex_vibrate=property(__getfield_alarmindex_vibrate, __setfield_alarmindex_vibrate, __delfield_alarmindex_vibrate, None)
	    def __getfield_ringtone(self):

        return self.__field_ringtone.getvalue()

	def __setfield_ringtone(self, value):

        if isinstance(value,UINT):

            self.__field_ringtone=value

        else:

            self.__field_ringtone=UINT(value,**{'sizeinbytes': 1})

	def __delfield_ringtone(self): del self.__field_ringtone

	    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
	    def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_alarmminutes(self):

        return self.__field_alarmminutes.getvalue()

	def __setfield_alarmminutes(self, value):

        if isinstance(value,UINT):

            self.__field_alarmminutes=value

        else:

            self.__field_alarmminutes=UINT(value,**{'sizeinbytes': 1})

	def __delfield_alarmminutes(self): del self.__field_alarmminutes

	    alarmminutes=property(__getfield_alarmminutes, __setfield_alarmminutes, __delfield_alarmminutes, "a value of 0xFF indicates not set")
	    def __getfield_alarmhours(self):

        return self.__field_alarmhours.getvalue()

	def __setfield_alarmhours(self, value):

        if isinstance(value,UINT):

            self.__field_alarmhours=value

        else:

            self.__field_alarmhours=UINT(value,**{'sizeinbytes': 1})

	def __delfield_alarmhours(self): del self.__field_alarmhours

	    alarmhours=property(__getfield_alarmhours, __setfield_alarmhours, __delfield_alarmhours, "a value of 0xFF indicates not set")
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('pos', self.__field_pos, "position within file, used as an event id")

        yield ('description', self.__field_description, None)

        yield ('start', self.__field_start, None)

        yield ('end', self.__field_end, None)

        yield ('repeat', self.__field_repeat, None)

        yield ('alarmindex_vibrate', self.__field_alarmindex_vibrate, None)

        yield ('ringtone', self.__field_ringtone, None)

        yield ('unknown1', self.__field_unknown1, None)

        yield ('alarmminutes', self.__field_alarmminutes, "a value of 0xFF indicates not set")

        yield ('alarmhours', self.__field_alarmhours, "a value of 0xFF indicates not set")

        yield ('unknown2', self.__field_unknown2, None)


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
	__fields=['GPStime', 'unknown2', 'duration', 'number', 'name', 'numberlength', 'pbnumbertype', 'unknown2', 'pbentrynum']
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

        self.__field_unknown2.writetobuffer(buf)

        self.__field_duration.writetobuffer(buf)

        self.__field_number.writetobuffer(buf)

        self.__field_name.writetobuffer(buf)

        self.__field_numberlength.writetobuffer(buf)

        self.__field_pbnumbertype.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_pbentrynum.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 4})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_duration=UINT(**{'sizeinbytes': 4})

        self.__field_duration.readfrombuffer(buf)

        self.__field_number=STRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})

        self.__field_number.readfrombuffer(buf)

        self.__field_name=STRING(**{'sizeinbytes': 36, 'raiseonunterminatedread': False})

        self.__field_name.readfrombuffer(buf)

        self.__field_numberlength=UINT(**{'sizeinbytes': 2})

        self.__field_numberlength.readfrombuffer(buf)

        self.__field_pbnumbertype=UINT(**{'sizeinbytes': 1})

        self.__field_pbnumbertype.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 3})

        self.__field_unknown2.readfrombuffer(buf)

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
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 4})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
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

            self.__field_numberlength=UINT(value,**{'sizeinbytes': 2})

	def __delfield_numberlength(self): del self.__field_numberlength

	    numberlength=property(__getfield_numberlength, __setfield_numberlength, __delfield_numberlength, None)
	    def __getfield_pbnumbertype(self):

        return self.__field_pbnumbertype.getvalue()

	def __setfield_pbnumbertype(self, value):

        if isinstance(value,UINT):

            self.__field_pbnumbertype=value

        else:

            self.__field_pbnumbertype=UINT(value,**{'sizeinbytes': 1})

	def __delfield_pbnumbertype(self): del self.__field_pbnumbertype

	    pbnumbertype=property(__getfield_pbnumbertype, __setfield_pbnumbertype, __delfield_pbnumbertype, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 3})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
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

        yield ('unknown2', self.__field_unknown2, None)

        yield ('duration', self.__field_duration, None)

        yield ('number', self.__field_number, None)

        yield ('name', self.__field_name, None)

        yield ('numberlength', self.__field_numberlength, None)

        yield ('pbnumbertype', self.__field_pbnumbertype, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('pbentrynum', self.__field_pbentrynum, None)


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


class  recipient_record (BaseProtogenClass) :
	__fields=['unknown1', 'number', 'status', 'timesent', 'timereceived', 'unknown2', 'unknown3']
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

        self.__field_number.writetobuffer(buf)

        self.__field_status.writetobuffer(buf)

        self.__field_timesent.writetobuffer(buf)

        self.__field_timereceived.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_unknown3.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_unknown1=DATA(**{'sizeinbytes': 45})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_number=STRING(**{'sizeinbytes': 49})

        self.__field_number.readfrombuffer(buf)

        self.__field_status=UINT(**{'sizeinbytes': 1})

        self.__field_status.readfrombuffer(buf)

        self.__field_timesent=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_timesent.readfrombuffer(buf)

        self.__field_timereceived=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_timereceived.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 1})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_unknown3=DATA(**{'sizeinbytes': 40})

        self.__field_unknown3.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,DATA):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=DATA(value,**{'sizeinbytes': 45})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_number(self):

        return self.__field_number.getvalue()

	def __setfield_number(self, value):

        if isinstance(value,STRING):

            self.__field_number=value

        else:

            self.__field_number=STRING(value,**{'sizeinbytes': 49})

	def __delfield_number(self): del self.__field_number

	    number=property(__getfield_number, __setfield_number, __delfield_number, None)
	    def __getfield_status(self):

        return self.__field_status.getvalue()

	def __setfield_status(self, value):

        if isinstance(value,UINT):

            self.__field_status=value

        else:

            self.__field_status=UINT(value,**{'sizeinbytes': 1})

	def __delfield_status(self): del self.__field_status

	    status=property(__getfield_status, __setfield_status, __delfield_status, None)
	    def __getfield_timesent(self):

        return self.__field_timesent.getvalue()

	def __setfield_timesent(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_timesent=value

        else:

            self.__field_timesent=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_timesent(self): del self.__field_timesent

	    timesent=property(__getfield_timesent, __setfield_timesent, __delfield_timesent, None)
	    def __getfield_timereceived(self):

        return self.__field_timereceived.getvalue()

	def __setfield_timereceived(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_timereceived=value

        else:

            self.__field_timereceived=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_timereceived(self): del self.__field_timereceived

	    timereceived=property(__getfield_timereceived, __setfield_timereceived, __delfield_timereceived, None)
	    def __getfield_unknown2(self):

        return self.__field_unknown2.getvalue()

	def __setfield_unknown2(self, value):

        if isinstance(value,UINT):

            self.__field_unknown2=value

        else:

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_unknown3(self):

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,DATA):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=DATA(value,**{'sizeinbytes': 40})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('unknown1', self.__field_unknown1, None)

        yield ('number', self.__field_number, None)

        yield ('status', self.__field_status, None)

        yield ('timesent', self.__field_timesent, None)

        yield ('timereceived', self.__field_timereceived, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('unknown3', self.__field_unknown3, None)


class  sms_saved (BaseProtogenClass) :
	__fields=['inboxmsg', 'GPStime', 'outbox', 'inbox']
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

        self.__field_inboxmsg.writetobuffer(buf)

        self.__field_GPStime.writetobuffer(buf)

        if not self.inboxmsg:

            self.__field_outbox.writetobuffer(buf)

        if self.inboxmsg:

            self.__field_inbox.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_inboxmsg=UINT(**{'sizeinbytes': 4})

        self.__field_inboxmsg.readfrombuffer(buf)

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        if not self.inboxmsg:

            self.__field_outbox=sms_out()

            self.__field_outbox.readfrombuffer(buf)

        if self.inboxmsg:

            self.__field_inbox=sms_in()

            self.__field_inbox.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_inboxmsg(self):

        return self.__field_inboxmsg.getvalue()

	def __setfield_inboxmsg(self, value):

        if isinstance(value,UINT):

            self.__field_inboxmsg=value

        else:

            self.__field_inboxmsg=UINT(value,**{'sizeinbytes': 4})

	def __delfield_inboxmsg(self): del self.__field_inboxmsg

	    inboxmsg=property(__getfield_inboxmsg, __setfield_inboxmsg, __delfield_inboxmsg, None)
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

        yield ('inboxmsg', self.__field_inboxmsg, None)

        yield ('GPStime', self.__field_GPStime, None)

        if not self.inboxmsg:

            yield ('outbox', self.__field_outbox, None)

        if self.inboxmsg:

            yield ('inbox', self.__field_inbox, None)


class  msg_record (BaseProtogenClass) :
	__fields=['binary', 'unknown3', 'unknown4', 'unknown6', 'length', 'msg']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(msg_record,self).__init__(**dict)

        if self.__class__ is msg_record:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(msg_record,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(msg_record,kwargs)

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_binary.writetobuffer(buf)

        self.__field_unknown3.writetobuffer(buf)

        self.__field_unknown4.writetobuffer(buf)

        self.__field_unknown6.writetobuffer(buf)

        self.__field_length.writetobuffer(buf)

        try: self.__field_msg

        except:

            self.__field_msg=LIST(**{'elementclass': _gen_p_lgvx9800_280, 'length': 219})

        self.__field_msg.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_binary=UINT(**{'sizeinbytes': 1})

        self.__field_binary.readfrombuffer(buf)

        self.__field_unknown3=UINT(**{'sizeinbytes': 1})

        self.__field_unknown3.readfrombuffer(buf)

        self.__field_unknown4=UINT(**{'sizeinbytes': 1})

        self.__field_unknown4.readfrombuffer(buf)

        self.__field_unknown6=UINT(**{'sizeinbytes': 1})

        self.__field_unknown6.readfrombuffer(buf)

        self.__field_length=UINT(**{'sizeinbytes': 1})

        self.__field_length.readfrombuffer(buf)

        self.__field_msg=LIST(**{'elementclass': _gen_p_lgvx9800_280, 'length': 219})

        self.__field_msg.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_binary(self):

        return self.__field_binary.getvalue()

	def __setfield_binary(self, value):

        if isinstance(value,UINT):

            self.__field_binary=value

        else:

            self.__field_binary=UINT(value,**{'sizeinbytes': 1})

	def __delfield_binary(self): del self.__field_binary

	    binary=property(__getfield_binary, __setfield_binary, __delfield_binary, None)
	    def __getfield_unknown3(self):

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,UINT):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def __getfield_unknown4(self):

        return self.__field_unknown4.getvalue()

	def __setfield_unknown4(self, value):

        if isinstance(value,UINT):

            self.__field_unknown4=value

        else:

            self.__field_unknown4=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown4(self): del self.__field_unknown4

	    unknown4=property(__getfield_unknown4, __setfield_unknown4, __delfield_unknown4, None)
	    def __getfield_unknown6(self):

        return self.__field_unknown6.getvalue()

	def __setfield_unknown6(self, value):

        if isinstance(value,UINT):

            self.__field_unknown6=value

        else:

            self.__field_unknown6=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown6(self): del self.__field_unknown6

	    unknown6=property(__getfield_unknown6, __setfield_unknown6, __delfield_unknown6, None)
	    def __getfield_length(self):

        return self.__field_length.getvalue()

	def __setfield_length(self, value):

        if isinstance(value,UINT):

            self.__field_length=value

        else:

            self.__field_length=UINT(value,**{'sizeinbytes': 1})

	def __delfield_length(self): del self.__field_length

	    length=property(__getfield_length, __setfield_length, __delfield_length, None)
	    def __getfield_msg(self):

        try: self.__field_msg

        except:

            self.__field_msg=LIST(**{'elementclass': _gen_p_lgvx9800_280, 'length': 219})

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,LIST):

            self.__field_msg=value

        else:

            self.__field_msg=LIST(value,**{'elementclass': _gen_p_lgvx9800_280, 'length': 219})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('binary', self.__field_binary, None)

        yield ('unknown3', self.__field_unknown3, None)

        yield ('unknown4', self.__field_unknown4, None)

        yield ('unknown6', self.__field_unknown6, None)

        yield ('length', self.__field_length, None)

        yield ('msg', self.__field_msg, None)


class  _gen_p_lgvx9800_280 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['byte']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_280,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_280:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_280,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_280,kwargs)

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

class  sms_out (BaseProtogenClass) :
	__fields=['index', 'unknown1', 'locked', 'timesent', 'unknown2', 'GPStime', 'subject', 'unknown4', 'num_msg_elements', 'unknown6', 'messages', 'priority', 'unknown7', 'unknown8', 'callback', 'recipients', 'unknown8']
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

        self.__field_unknown1.writetobuffer(buf)

        self.__field_locked.writetobuffer(buf)

        self.__field_timesent.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_GPStime.writetobuffer(buf)

        self.__field_subject.writetobuffer(buf)

        self.__field_unknown4.writetobuffer(buf)

        self.__field_num_msg_elements.writetobuffer(buf)

        self.__field_unknown6.writetobuffer(buf)

        try: self.__field_messages

        except:

            self.__field_messages=LIST(**{'elementclass': msg_record, 'length': 7})

        self.__field_messages.writetobuffer(buf)

        self.__field_priority.writetobuffer(buf)

        self.__field_unknown7.writetobuffer(buf)

        self.__field_unknown8.writetobuffer(buf)

        self.__field_callback.writetobuffer(buf)

        try: self.__field_recipients

        except:

            self.__field_recipients=LIST(**{'elementclass': recipient_record,'length': 10})

        self.__field_recipients.writetobuffer(buf)

        self.__field_unknown8.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_index=UINT(**{'sizeinbytes': 4})

        self.__field_index.readfrombuffer(buf)

        self.__field_unknown1=UINT(**{'sizeinbytes': 1})

        self.__field_unknown1.readfrombuffer(buf)

        self.__field_locked=UINT(**{'sizeinbytes': 1})

        self.__field_locked.readfrombuffer(buf)

        self.__field_timesent=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_timesent.readfrombuffer(buf)

        self.__field_unknown2=UINT(**{'sizeinbytes': 2})

        self.__field_unknown2.readfrombuffer(buf)

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        self.__field_subject=STRING(**{'sizeinbytes': 21})

        self.__field_subject.readfrombuffer(buf)

        self.__field_unknown4=UINT(**{'sizeinbytes': 1})

        self.__field_unknown4.readfrombuffer(buf)

        self.__field_num_msg_elements=UINT(**{'sizeinbytes': 1})

        self.__field_num_msg_elements.readfrombuffer(buf)

        self.__field_unknown6=UINT(**{'sizeinbytes': 1})

        self.__field_unknown6.readfrombuffer(buf)

        self.__field_messages=LIST(**{'elementclass': msg_record, 'length': 7})

        self.__field_messages.readfrombuffer(buf)

        self.__field_priority=UINT(**{'sizeinbytes': 1})

        self.__field_priority.readfrombuffer(buf)

        self.__field_unknown7=DATA(**{'sizeinbytes': 19})

        self.__field_unknown7.readfrombuffer(buf)

        self.__field_unknown8=DATA(**{'sizeinbytes': 3})

        self.__field_unknown8.readfrombuffer(buf)

        self.__field_callback=STRING(**{'sizeinbytes': 23})

        self.__field_callback.readfrombuffer(buf)

        self.__field_recipients=LIST(**{'elementclass': recipient_record,'length': 10})

        self.__field_recipients.readfrombuffer(buf)

        self.__field_unknown8=UNKNOWN()

        self.__field_unknown8.readfrombuffer(buf)

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
	    def __getfield_unknown1(self):

        return self.__field_unknown1.getvalue()

	def __setfield_unknown1(self, value):

        if isinstance(value,UINT):

            self.__field_unknown1=value

        else:

            self.__field_unknown1=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown1(self): del self.__field_unknown1

	    unknown1=property(__getfield_unknown1, __setfield_unknown1, __delfield_unknown1, None)
	    def __getfield_locked(self):

        return self.__field_locked.getvalue()

	def __setfield_locked(self, value):

        if isinstance(value,UINT):

            self.__field_locked=value

        else:

            self.__field_locked=UINT(value,**{'sizeinbytes': 1})

	def __delfield_locked(self): del self.__field_locked

	    locked=property(__getfield_locked, __setfield_locked, __delfield_locked, None)
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

            self.__field_unknown2=UINT(value,**{'sizeinbytes': 2})

	def __delfield_unknown2(self): del self.__field_unknown2

	    unknown2=property(__getfield_unknown2, __setfield_unknown2, __delfield_unknown2, None)
	    def __getfield_GPStime(self):

        return self.__field_GPStime.getvalue()

	def __setfield_GPStime(self, value):

        if isinstance(value,GPSDATE):

            self.__field_GPStime=value

        else:

            self.__field_GPStime=GPSDATE(value,**{'sizeinbytes': 4})

	def __delfield_GPStime(self): del self.__field_GPStime

	    GPStime=property(__getfield_GPStime, __setfield_GPStime, __delfield_GPStime, None)
	    def __getfield_subject(self):

        return self.__field_subject.getvalue()

	def __setfield_subject(self, value):

        if isinstance(value,STRING):

            self.__field_subject=value

        else:

            self.__field_subject=STRING(value,**{'sizeinbytes': 21})

	def __delfield_subject(self): del self.__field_subject

	    subject=property(__getfield_subject, __setfield_subject, __delfield_subject, None)
	    def __getfield_unknown4(self):

        return self.__field_unknown4.getvalue()

	def __setfield_unknown4(self, value):

        if isinstance(value,UINT):

            self.__field_unknown4=value

        else:

            self.__field_unknown4=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown4(self): del self.__field_unknown4

	    unknown4=property(__getfield_unknown4, __setfield_unknown4, __delfield_unknown4, None)
	    def __getfield_num_msg_elements(self):

        return self.__field_num_msg_elements.getvalue()

	def __setfield_num_msg_elements(self, value):

        if isinstance(value,UINT):

            self.__field_num_msg_elements=value

        else:

            self.__field_num_msg_elements=UINT(value,**{'sizeinbytes': 1})

	def __delfield_num_msg_elements(self): del self.__field_num_msg_elements

	    num_msg_elements=property(__getfield_num_msg_elements, __setfield_num_msg_elements, __delfield_num_msg_elements, None)
	    def __getfield_unknown6(self):

        return self.__field_unknown6.getvalue()

	def __setfield_unknown6(self, value):

        if isinstance(value,UINT):

            self.__field_unknown6=value

        else:

            self.__field_unknown6=UINT(value,**{'sizeinbytes': 1})

	def __delfield_unknown6(self): del self.__field_unknown6

	    unknown6=property(__getfield_unknown6, __setfield_unknown6, __delfield_unknown6, None)
	    def __getfield_messages(self):

        try: self.__field_messages

        except:

            self.__field_messages=LIST(**{'elementclass': msg_record, 'length': 7})

        return self.__field_messages.getvalue()

	def __setfield_messages(self, value):

        if isinstance(value,LIST):

            self.__field_messages=value

        else:

            self.__field_messages=LIST(value,**{'elementclass': msg_record, 'length': 7})

	def __delfield_messages(self): del self.__field_messages

	    messages=property(__getfield_messages, __setfield_messages, __delfield_messages, None)
	    def __getfield_priority(self):

        return self.__field_priority.getvalue()

	def __setfield_priority(self, value):

        if isinstance(value,UINT):

            self.__field_priority=value

        else:

            self.__field_priority=UINT(value,**{'sizeinbytes': 1})

	def __delfield_priority(self): del self.__field_priority

	    priority=property(__getfield_priority, __setfield_priority, __delfield_priority, None)
	    def __getfield_unknown7(self):

        return self.__field_unknown7.getvalue()

	def __setfield_unknown7(self, value):

        if isinstance(value,DATA):

            self.__field_unknown7=value

        else:

            self.__field_unknown7=DATA(value,**{'sizeinbytes': 19})

	def __delfield_unknown7(self): del self.__field_unknown7

	    unknown7=property(__getfield_unknown7, __setfield_unknown7, __delfield_unknown7, None)
	    def __getfield_unknown8(self):

        return self.__field_unknown8.getvalue()

	def __setfield_unknown8(self, value):

        if isinstance(value,DATA):

            self.__field_unknown8=value

        else:

            self.__field_unknown8=DATA(value,**{'sizeinbytes': 3})

	def __delfield_unknown8(self): del self.__field_unknown8

	    unknown8=property(__getfield_unknown8, __setfield_unknown8, __delfield_unknown8, None)
	    def __getfield_callback(self):

        return self.__field_callback.getvalue()

	def __setfield_callback(self, value):

        if isinstance(value,STRING):

            self.__field_callback=value

        else:

            self.__field_callback=STRING(value,**{'sizeinbytes': 23})

	def __delfield_callback(self): del self.__field_callback

	    callback=property(__getfield_callback, __setfield_callback, __delfield_callback, None)
	    def __getfield_recipients(self):

        try: self.__field_recipients

        except:

            self.__field_recipients=LIST(**{'elementclass': recipient_record,'length': 10})

        return self.__field_recipients.getvalue()

	def __setfield_recipients(self, value):

        if isinstance(value,LIST):

            self.__field_recipients=value

        else:

            self.__field_recipients=LIST(value,**{'elementclass': recipient_record,'length': 10})

	def __delfield_recipients(self): del self.__field_recipients

	    recipients=property(__getfield_recipients, __setfield_recipients, __delfield_recipients, None)
	    def __getfield_unknown8(self):

        return self.__field_unknown8.getvalue()

	def __setfield_unknown8(self, value):

        if isinstance(value,UNKNOWN):

            self.__field_unknown8=value

        else:

            self.__field_unknown8=UNKNOWN(value,)

	def __delfield_unknown8(self): del self.__field_unknown8

	    unknown8=property(__getfield_unknown8, __setfield_unknown8, __delfield_unknown8, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('index', self.__field_index, None)

        yield ('unknown1', self.__field_unknown1, None)

        yield ('locked', self.__field_locked, None)

        yield ('timesent', self.__field_timesent, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('GPStime', self.__field_GPStime, None)

        yield ('subject', self.__field_subject, None)

        yield ('unknown4', self.__field_unknown4, None)

        yield ('num_msg_elements', self.__field_num_msg_elements, None)

        yield ('unknown6', self.__field_unknown6, None)

        yield ('messages', self.__field_messages, None)

        yield ('priority', self.__field_priority, None)

        yield ('unknown7', self.__field_unknown7, None)

        yield ('unknown8', self.__field_unknown8, None)

        yield ('callback', self.__field_callback, None)

        yield ('recipients', self.__field_recipients, None)

        yield ('unknown8', self.__field_unknown8, None)


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

            dict2={'elementclass': _gen_p_lgvx9800_303, 'length': 181}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_msg=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_msg

        except:

            self.__field_msg=LIST(**{'elementclass': _gen_p_lgvx9800_303, 'length': 181})

        self.__field_msg.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msg=LIST(**{'elementclass': _gen_p_lgvx9800_303, 'length': 181})

        self.__field_msg.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_msg(self):

        try: self.__field_msg

        except:

            self.__field_msg=LIST(**{'elementclass': _gen_p_lgvx9800_303, 'length': 181})

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,LIST):

            self.__field_msg=value

        else:

            self.__field_msg=LIST(value,**{'elementclass': _gen_p_lgvx9800_303, 'length': 181})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('msg', self.__field_msg, None)


class  _gen_p_lgvx9800_303 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['byte']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_303,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_303:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_303,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_303,kwargs)

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
	__fields=['msg_index1', 'msg_index2', 'unknown2', 'timesent', 'unknown', 'callback_length', 'callback', 'sender_length', 'sender', 'unknown3', 'lg_time', 'unknown4', 'GPStime', 'unknown5', 'read', 'locked', 'unknown8', 'priority', 'unknown11', 'subject', 'bin_header1', 'bin_header2', 'unknown6', 'multipartID', 'unknown14', 'bin_header3', 'num_msg_elements', 'msglengths', 'msgs', 'unknown12', 'senders_name', 'unknown9']
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

        self.__field_msg_index1.writetobuffer(buf)

        self.__field_msg_index2.writetobuffer(buf)

        self.__field_unknown2.writetobuffer(buf)

        self.__field_timesent.writetobuffer(buf)

        self.__field_unknown.writetobuffer(buf)

        self.__field_callback_length.writetobuffer(buf)

        self.__field_callback.writetobuffer(buf)

        self.__field_sender_length.writetobuffer(buf)

        try: self.__field_sender

        except:

            self.__field_sender=LIST(**{'elementclass': _gen_p_lgvx9800_315, 'length': 38})

        self.__field_sender.writetobuffer(buf)

        self.__field_unknown3.writetobuffer(buf)

        self.__field_lg_time.writetobuffer(buf)

        self.__field_unknown4.writetobuffer(buf)

        self.__field_GPStime.writetobuffer(buf)

        self.__field_unknown5.writetobuffer(buf)

        self.__field_read.writetobuffer(buf)

        self.__field_locked.writetobuffer(buf)

        self.__field_unknown8.writetobuffer(buf)

        self.__field_priority.writetobuffer(buf)

        self.__field_unknown11.writetobuffer(buf)

        self.__field_subject.writetobuffer(buf)

        self.__field_bin_header1.writetobuffer(buf)

        self.__field_bin_header2.writetobuffer(buf)

        self.__field_unknown6.writetobuffer(buf)

        self.__field_multipartID.writetobuffer(buf)

        self.__field_unknown14.writetobuffer(buf)

        self.__field_bin_header3.writetobuffer(buf)

        self.__field_num_msg_elements.writetobuffer(buf)

        try: self.__field_msglengths

        except:

            self.__field_msglengths=LIST(**{'elementclass': _gen_p_lgvx9800_335, 'length': 20})

        self.__field_msglengths.writetobuffer(buf)

        try: self.__field_msgs

        except:

            self.__field_msgs=LIST(**{'length': 20, 'elementclass': SMSINBOXMSGFRAGMENT})

        self.__field_msgs.writetobuffer(buf)

        self.__field_unknown12.writetobuffer(buf)

        self.__field_senders_name.writetobuffer(buf)

        self.__field_unknown9.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msg_index1=UINT(**{'sizeinbytes': 4})

        self.__field_msg_index1.readfrombuffer(buf)

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

        self.__field_sender=LIST(**{'elementclass': _gen_p_lgvx9800_315, 'length': 38})

        self.__field_sender.readfrombuffer(buf)

        self.__field_unknown3=DATA(**{'sizeinbytes': 12})

        self.__field_unknown3.readfrombuffer(buf)

        self.__field_lg_time=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_lg_time.readfrombuffer(buf)

        self.__field_unknown4=UINT(**{'sizeinbytes': 3})

        self.__field_unknown4.readfrombuffer(buf)

        self.__field_GPStime=GPSDATE(**{'sizeinbytes': 4})

        self.__field_GPStime.readfrombuffer(buf)

        self.__field_unknown5=UINT(**{'sizeinbytes': 4})

        self.__field_unknown5.readfrombuffer(buf)

        self.__field_read=UINT(**{'sizeinbytes': 1})

        self.__field_read.readfrombuffer(buf)

        self.__field_locked=UINT(**{'sizeinbytes': 1})

        self.__field_locked.readfrombuffer(buf)

        self.__field_unknown8=UINT(**{'sizeinbytes': 2})

        self.__field_unknown8.readfrombuffer(buf)

        self.__field_priority=UINT(**{'sizeinbytes': 1})

        self.__field_priority.readfrombuffer(buf)

        self.__field_unknown11=DATA(**{'sizeinbytes': 6})

        self.__field_unknown11.readfrombuffer(buf)

        self.__field_subject=STRING(**{'sizeinbytes': 21})

        self.__field_subject.readfrombuffer(buf)

        self.__field_bin_header1=UINT(**{'sizeinbytes': 1})

        self.__field_bin_header1.readfrombuffer(buf)

        self.__field_bin_header2=UINT(**{'sizeinbytes': 1})

        self.__field_bin_header2.readfrombuffer(buf)

        self.__field_unknown6=UINT(**{'sizeinbytes': 2})

        self.__field_unknown6.readfrombuffer(buf)

        self.__field_multipartID=UINT(**{'sizeinbytes': 2})

        self.__field_multipartID.readfrombuffer(buf)

        self.__field_unknown14=UINT(**{'sizeinbytes': 2})

        self.__field_unknown14.readfrombuffer(buf)

        self.__field_bin_header3=UINT(**{'sizeinbytes': 1})

        self.__field_bin_header3.readfrombuffer(buf)

        self.__field_num_msg_elements=UINT(**{'sizeinbytes': 1})

        self.__field_num_msg_elements.readfrombuffer(buf)

        self.__field_msglengths=LIST(**{'elementclass': _gen_p_lgvx9800_335, 'length': 20})

        self.__field_msglengths.readfrombuffer(buf)

        self.__field_msgs=LIST(**{'length': 20, 'elementclass': SMSINBOXMSGFRAGMENT})

        self.__field_msgs.readfrombuffer(buf)

        self.__field_unknown12=DATA(**{'sizeinbytes': 101})

        self.__field_unknown12.readfrombuffer(buf)

        self.__field_senders_name=STRING(**{'sizeinbytes': 59})

        self.__field_senders_name.readfrombuffer(buf)

        self.__field_unknown9=DATA()

        self.__field_unknown9.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_msg_index1(self):

        return self.__field_msg_index1.getvalue()

	def __setfield_msg_index1(self, value):

        if isinstance(value,UINT):

            self.__field_msg_index1=value

        else:

            self.__field_msg_index1=UINT(value,**{'sizeinbytes': 4})

	def __delfield_msg_index1(self): del self.__field_msg_index1

	    msg_index1=property(__getfield_msg_index1, __setfield_msg_index1, __delfield_msg_index1, None)
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

            self.__field_sender=LIST(**{'elementclass': _gen_p_lgvx9800_315, 'length': 38})

        return self.__field_sender.getvalue()

	def __setfield_sender(self, value):

        if isinstance(value,LIST):

            self.__field_sender=value

        else:

            self.__field_sender=LIST(value,**{'elementclass': _gen_p_lgvx9800_315, 'length': 38})

	def __delfield_sender(self): del self.__field_sender

	    sender=property(__getfield_sender, __setfield_sender, __delfield_sender, None)
	    def __getfield_unknown3(self):

        return self.__field_unknown3.getvalue()

	def __setfield_unknown3(self, value):

        if isinstance(value,DATA):

            self.__field_unknown3=value

        else:

            self.__field_unknown3=DATA(value,**{'sizeinbytes': 12})

	def __delfield_unknown3(self): del self.__field_unknown3

	    unknown3=property(__getfield_unknown3, __setfield_unknown3, __delfield_unknown3, None)
	    def __getfield_lg_time(self):

        return self.__field_lg_time.getvalue()

	def __setfield_lg_time(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_lg_time=value

        else:

            self.__field_lg_time=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_lg_time(self): del self.__field_lg_time

	    lg_time=property(__getfield_lg_time, __setfield_lg_time, __delfield_lg_time, None)
	    def __getfield_unknown4(self):

        return self.__field_unknown4.getvalue()

	def __setfield_unknown4(self, value):

        if isinstance(value,UINT):

            self.__field_unknown4=value

        else:

            self.__field_unknown4=UINT(value,**{'sizeinbytes': 3})

	def __delfield_unknown4(self): del self.__field_unknown4

	    unknown4=property(__getfield_unknown4, __setfield_unknown4, __delfield_unknown4, None)
	    def __getfield_GPStime(self):

        return self.__field_GPStime.getvalue()

	def __setfield_GPStime(self, value):

        if isinstance(value,GPSDATE):

            self.__field_GPStime=value

        else:

            self.__field_GPStime=GPSDATE(value,**{'sizeinbytes': 4})

	def __delfield_GPStime(self): del self.__field_GPStime

	    GPStime=property(__getfield_GPStime, __setfield_GPStime, __delfield_GPStime, None)
	    def __getfield_unknown5(self):

        return self.__field_unknown5.getvalue()

	def __setfield_unknown5(self, value):

        if isinstance(value,UINT):

            self.__field_unknown5=value

        else:

            self.__field_unknown5=UINT(value,**{'sizeinbytes': 4})

	def __delfield_unknown5(self): del self.__field_unknown5

	    unknown5=property(__getfield_unknown5, __setfield_unknown5, __delfield_unknown5, None)
	    def __getfield_read(self):

        return self.__field_read.getvalue()

	def __setfield_read(self, value):

        if isinstance(value,UINT):

            self.__field_read=value

        else:

            self.__field_read=UINT(value,**{'sizeinbytes': 1})

	def __delfield_read(self): del self.__field_read

	    read=property(__getfield_read, __setfield_read, __delfield_read, None)
	    def __getfield_locked(self):

        return self.__field_locked.getvalue()

	def __setfield_locked(self, value):

        if isinstance(value,UINT):

            self.__field_locked=value

        else:

            self.__field_locked=UINT(value,**{'sizeinbytes': 1})

	def __delfield_locked(self): del self.__field_locked

	    locked=property(__getfield_locked, __setfield_locked, __delfield_locked, None)
	    def __getfield_unknown8(self):

        return self.__field_unknown8.getvalue()

	def __setfield_unknown8(self, value):

        if isinstance(value,UINT):

            self.__field_unknown8=value

        else:

            self.__field_unknown8=UINT(value,**{'sizeinbytes': 2})

	def __delfield_unknown8(self): del self.__field_unknown8

	    unknown8=property(__getfield_unknown8, __setfield_unknown8, __delfield_unknown8, None)
	    def __getfield_priority(self):

        return self.__field_priority.getvalue()

	def __setfield_priority(self, value):

        if isinstance(value,UINT):

            self.__field_priority=value

        else:

            self.__field_priority=UINT(value,**{'sizeinbytes': 1})

	def __delfield_priority(self): del self.__field_priority

	    priority=property(__getfield_priority, __setfield_priority, __delfield_priority, None)
	    def __getfield_unknown11(self):

        return self.__field_unknown11.getvalue()

	def __setfield_unknown11(self, value):

        if isinstance(value,DATA):

            self.__field_unknown11=value

        else:

            self.__field_unknown11=DATA(value,**{'sizeinbytes': 6})

	def __delfield_unknown11(self): del self.__field_unknown11

	    unknown11=property(__getfield_unknown11, __setfield_unknown11, __delfield_unknown11, None)
	    def __getfield_subject(self):

        return self.__field_subject.getvalue()

	def __setfield_subject(self, value):

        if isinstance(value,STRING):

            self.__field_subject=value

        else:

            self.__field_subject=STRING(value,**{'sizeinbytes': 21})

	def __delfield_subject(self): del self.__field_subject

	    subject=property(__getfield_subject, __setfield_subject, __delfield_subject, None)
	    def __getfield_bin_header1(self):

        return self.__field_bin_header1.getvalue()

	def __setfield_bin_header1(self, value):

        if isinstance(value,UINT):

            self.__field_bin_header1=value

        else:

            self.__field_bin_header1=UINT(value,**{'sizeinbytes': 1})

	def __delfield_bin_header1(self): del self.__field_bin_header1

	    bin_header1=property(__getfield_bin_header1, __setfield_bin_header1, __delfield_bin_header1, None)
	    def __getfield_bin_header2(self):

        return self.__field_bin_header2.getvalue()

	def __setfield_bin_header2(self, value):

        if isinstance(value,UINT):

            self.__field_bin_header2=value

        else:

            self.__field_bin_header2=UINT(value,**{'sizeinbytes': 1})

	def __delfield_bin_header2(self): del self.__field_bin_header2

	    bin_header2=property(__getfield_bin_header2, __setfield_bin_header2, __delfield_bin_header2, None)
	    def __getfield_unknown6(self):

        return self.__field_unknown6.getvalue()

	def __setfield_unknown6(self, value):

        if isinstance(value,UINT):

            self.__field_unknown6=value

        else:

            self.__field_unknown6=UINT(value,**{'sizeinbytes': 2})

	def __delfield_unknown6(self): del self.__field_unknown6

	    unknown6=property(__getfield_unknown6, __setfield_unknown6, __delfield_unknown6, None)
	    def __getfield_multipartID(self):

        return self.__field_multipartID.getvalue()

	def __setfield_multipartID(self, value):

        if isinstance(value,UINT):

            self.__field_multipartID=value

        else:

            self.__field_multipartID=UINT(value,**{'sizeinbytes': 2})

	def __delfield_multipartID(self): del self.__field_multipartID

	    multipartID=property(__getfield_multipartID, __setfield_multipartID, __delfield_multipartID, None)
	    def __getfield_unknown14(self):

        return self.__field_unknown14.getvalue()

	def __setfield_unknown14(self, value):

        if isinstance(value,UINT):

            self.__field_unknown14=value

        else:

            self.__field_unknown14=UINT(value,**{'sizeinbytes': 2})

	def __delfield_unknown14(self): del self.__field_unknown14

	    unknown14=property(__getfield_unknown14, __setfield_unknown14, __delfield_unknown14, None)
	    def __getfield_bin_header3(self):

        return self.__field_bin_header3.getvalue()

	def __setfield_bin_header3(self, value):

        if isinstance(value,UINT):

            self.__field_bin_header3=value

        else:

            self.__field_bin_header3=UINT(value,**{'sizeinbytes': 1})

	def __delfield_bin_header3(self): del self.__field_bin_header3

	    bin_header3=property(__getfield_bin_header3, __setfield_bin_header3, __delfield_bin_header3, None)
	    def __getfield_num_msg_elements(self):

        return self.__field_num_msg_elements.getvalue()

	def __setfield_num_msg_elements(self, value):

        if isinstance(value,UINT):

            self.__field_num_msg_elements=value

        else:

            self.__field_num_msg_elements=UINT(value,**{'sizeinbytes': 1})

	def __delfield_num_msg_elements(self): del self.__field_num_msg_elements

	    num_msg_elements=property(__getfield_num_msg_elements, __setfield_num_msg_elements, __delfield_num_msg_elements, None)
	    def __getfield_msglengths(self):

        try: self.__field_msglengths

        except:

            self.__field_msglengths=LIST(**{'elementclass': _gen_p_lgvx9800_335, 'length': 20})

        return self.__field_msglengths.getvalue()

	def __setfield_msglengths(self, value):

        if isinstance(value,LIST):

            self.__field_msglengths=value

        else:

            self.__field_msglengths=LIST(value,**{'elementclass': _gen_p_lgvx9800_335, 'length': 20})

	def __delfield_msglengths(self): del self.__field_msglengths

	    msglengths=property(__getfield_msglengths, __setfield_msglengths, __delfield_msglengths, None)
	    def __getfield_msgs(self):

        try: self.__field_msgs

        except:

            self.__field_msgs=LIST(**{'length': 20, 'elementclass': SMSINBOXMSGFRAGMENT})

        return self.__field_msgs.getvalue()

	def __setfield_msgs(self, value):

        if isinstance(value,LIST):

            self.__field_msgs=value

        else:

            self.__field_msgs=LIST(value,**{'length': 20, 'elementclass': SMSINBOXMSGFRAGMENT})

	def __delfield_msgs(self): del self.__field_msgs

	    msgs=property(__getfield_msgs, __setfield_msgs, __delfield_msgs, None)
	    def __getfield_unknown12(self):

        return self.__field_unknown12.getvalue()

	def __setfield_unknown12(self, value):

        if isinstance(value,DATA):

            self.__field_unknown12=value

        else:

            self.__field_unknown12=DATA(value,**{'sizeinbytes': 101})

	def __delfield_unknown12(self): del self.__field_unknown12

	    unknown12=property(__getfield_unknown12, __setfield_unknown12, __delfield_unknown12, None)
	    def __getfield_senders_name(self):

        return self.__field_senders_name.getvalue()

	def __setfield_senders_name(self, value):

        if isinstance(value,STRING):

            self.__field_senders_name=value

        else:

            self.__field_senders_name=STRING(value,**{'sizeinbytes': 59})

	def __delfield_senders_name(self): del self.__field_senders_name

	    senders_name=property(__getfield_senders_name, __setfield_senders_name, __delfield_senders_name, None)
	    def __getfield_unknown9(self):

        return self.__field_unknown9.getvalue()

	def __setfield_unknown9(self, value):

        if isinstance(value,DATA):

            self.__field_unknown9=value

        else:

            self.__field_unknown9=DATA(value,)

	def __delfield_unknown9(self): del self.__field_unknown9

	    unknown9=property(__getfield_unknown9, __setfield_unknown9, __delfield_unknown9, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('msg_index1', self.__field_msg_index1, None)

        yield ('msg_index2', self.__field_msg_index2, None)

        yield ('unknown2', self.__field_unknown2, None)

        yield ('timesent', self.__field_timesent, None)

        yield ('unknown', self.__field_unknown, None)

        yield ('callback_length', self.__field_callback_length, None)

        yield ('callback', self.__field_callback, None)

        yield ('sender_length', self.__field_sender_length, None)

        yield ('sender', self.__field_sender, None)

        yield ('unknown3', self.__field_unknown3, None)

        yield ('lg_time', self.__field_lg_time, None)

        yield ('unknown4', self.__field_unknown4, None)

        yield ('GPStime', self.__field_GPStime, None)

        yield ('unknown5', self.__field_unknown5, None)

        yield ('read', self.__field_read, None)

        yield ('locked', self.__field_locked, None)

        yield ('unknown8', self.__field_unknown8, None)

        yield ('priority', self.__field_priority, None)

        yield ('unknown11', self.__field_unknown11, None)

        yield ('subject', self.__field_subject, None)

        yield ('bin_header1', self.__field_bin_header1, None)

        yield ('bin_header2', self.__field_bin_header2, None)

        yield ('unknown6', self.__field_unknown6, None)

        yield ('multipartID', self.__field_multipartID, None)

        yield ('unknown14', self.__field_unknown14, None)

        yield ('bin_header3', self.__field_bin_header3, None)

        yield ('num_msg_elements', self.__field_num_msg_elements, None)

        yield ('msglengths', self.__field_msglengths, None)

        yield ('msgs', self.__field_msgs, None)

        yield ('unknown12', self.__field_unknown12, None)

        yield ('senders_name', self.__field_senders_name, None)

        yield ('unknown9', self.__field_unknown9, None)


class  _gen_p_lgvx9800_315 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['byte']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_315,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_315:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_315,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_315,kwargs)

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

class  _gen_p_lgvx9800_335 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['msglength']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_335,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_335:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_335,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_335,kwargs)

        if len(args):

            dict2={'sizeinbytes': 1}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_msglength=UINT(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msglength.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msglength=UINT(**{'sizeinbytes': 1})

        self.__field_msglength.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_msglength(self):

        return self.__field_msglength.getvalue()

	def __setfield_msglength(self, value):

        if isinstance(value,UINT):

            self.__field_msglength=value

        else:

            self.__field_msglength=UINT(value,**{'sizeinbytes': 1})

	def __delfield_msglength(self): del self.__field_msglength

	    msglength=property(__getfield_msglength, __setfield_msglength, __delfield_msglength, "lengths of individual messages in septets")
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('msglength', self.__field_msglength, "lengths of individual messages in septets")

	'Anonymous inner class'

class  sms_quick_text (BaseProtogenClass) :
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

            dict2={'elementclass': _gen_p_lgvx9800_349, 'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_msgs=LIST(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_msgs

        except:

            self.__field_msgs=LIST(**{'elementclass': _gen_p_lgvx9800_349, 'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True})

        self.__field_msgs.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msgs=LIST(**{'elementclass': _gen_p_lgvx9800_349, 'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True})

        self.__field_msgs.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_msgs(self):

        try: self.__field_msgs

        except:

            self.__field_msgs=LIST(**{'elementclass': _gen_p_lgvx9800_349, 'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True})

        return self.__field_msgs.getvalue()

	def __setfield_msgs(self, value):

        if isinstance(value,LIST):

            self.__field_msgs=value

        else:

            self.__field_msgs=LIST(value,**{'elementclass': _gen_p_lgvx9800_349, 'length': SMS_CANNED_MAX_ITEMS, 'createdefault': True})

	def __delfield_msgs(self): del self.__field_msgs

	    msgs=property(__getfield_msgs, __setfield_msgs, __delfield_msgs, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('msgs', self.__field_msgs, None)


class  _gen_p_lgvx9800_349 (BaseProtogenClass) :
	'Anonymous inner class'
	    __fields=['msg']
	    def __init__(self, *args, **kwargs):

        dict={}

        dict.update(kwargs)

        super(_gen_p_lgvx9800_349,self).__init__(**dict)

        if self.__class__ is _gen_p_lgvx9800_349:

            self._update(args,dict)

	def getfields(self):

        return self.__fields

	def _update(self, args, kwargs):

        super(_gen_p_lgvx9800_349,self)._update(args,kwargs)

        keys=kwargs.keys()

        for key in keys:

            if key in self.__fields:

                setattr(self, key, kwargs[key])

                del kwargs[key]

        if __debug__:

            self._complainaboutunusedargs(_gen_p_lgvx9800_349,kwargs)

        if len(args):

            dict2={'sizeinbytes': 101, 'default': ""}

            dict2.update(kwargs)

            kwargs=dict2

            self.__field_msg=STRING(*args,**dict2)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        try: self.__field_msg

        except:

            self.__field_msg=STRING(**{'sizeinbytes': 101, 'default': ""})

        self.__field_msg.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_msg=STRING(**{'sizeinbytes': 101, 'default': ""})

        self.__field_msg.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_msg(self):

        try: self.__field_msg

        except:

            self.__field_msg=STRING(**{'sizeinbytes': 101, 'default': ""})

        return self.__field_msg.getvalue()

	def __setfield_msg(self, value):

        if isinstance(value,STRING):

            self.__field_msg=value

        else:

            self.__field_msg=STRING(value,**{'sizeinbytes': 101, 'default': ""})

	def __delfield_msg(self): del self.__field_msg

	    msg=property(__getfield_msg, __setfield_msg, __delfield_msg, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('msg', self.__field_msg, None)

	'Anonymous inner class'

class  textmemo (BaseProtogenClass) :
	__fields=['text', 'memotime']
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

        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)

	def writetobuffer(self,buf):

        'Writes this packet to the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_text.writetobuffer(buf)

        self.__field_memotime.writetobuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def readfrombuffer(self,buf):

        'Reads this packet from the supplied buffer'

        self._bufferstartoffset=buf.getcurrentoffset()

        self.__field_text=STRING(**{'sizeinbytes': 301,  'raiseonunterminatedread': False, 'raiseontruncate': False })

        self.__field_text.readfrombuffer(buf)

        self.__field_memotime=LGCALDATE(**{'sizeinbytes': 4})

        self.__field_memotime.readfrombuffer(buf)

        self._bufferendoffset=buf.getcurrentoffset()

	def __getfield_text(self):

        return self.__field_text.getvalue()

	def __setfield_text(self, value):

        if isinstance(value,STRING):

            self.__field_text=value

        else:

            self.__field_text=STRING(value,**{'sizeinbytes': 301,  'raiseonunterminatedread': False, 'raiseontruncate': False })

	def __delfield_text(self): del self.__field_text

	    text=property(__getfield_text, __setfield_text, __delfield_text, None)
	    def __getfield_memotime(self):

        return self.__field_memotime.getvalue()

	def __setfield_memotime(self, value):

        if isinstance(value,LGCALDATE):

            self.__field_memotime=value

        else:

            self.__field_memotime=LGCALDATE(value,**{'sizeinbytes': 4})

	def __delfield_memotime(self): del self.__field_memotime

	    memotime=property(__getfield_memotime, __setfield_memotime, __delfield_memotime, None)
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('text', self.__field_text, None)

        yield ('memotime', self.__field_memotime, None)


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


class  firmwareresponse (BaseProtogenClass) :
	__fields=['command', 'date1', 'time1', 'date2', 'time2', 'firmware']
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
	    def iscontainer(self):

        return True

	def containerelements(self):

        yield ('command', self.__field_command, None)

        yield ('date1', self.__field_date1, None)

        yield ('time1', self.__field_time1, None)

        yield ('date2', self.__field_date2, None)

        yield ('time2', self.__field_time2, None)

        yield ('firmware', self.__field_firmware, None)


