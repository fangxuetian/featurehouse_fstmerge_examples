"""Various descriptions of data specific to Sanyo RL-4930"""
from prototypes import *
from p_sanyo import *
from p_sanyomedia import *
from p_sanyonewer import *
UINT=UINTlsb
BOOL=BOOLlsb
_NUMPBSLOTS=500
_NUMSPEEDDIALS=8
_NUMLONGNUMBERS=5
_LONGPHONENUMBERLEN=30
_NUMEVENTSLOTS=100
_NUMCALLALARMSLOTS=15
_NUMCALLHISTORY=20
_MAXNUMBERLEN=32
_MAXEMAILLEN=96
HASRINGPICBUF=0
class qcpheader(BaseProtogenClass):
    __fields=['readwrite', 'command', 'packettype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update({'readwrite': 0x26})
        dict.update(kwargs)
        super(qcpheader,self).__init__(**dict)
        if self.__class__ is qcpheader:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(qcpheader,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(qcpheader,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_readwrite.writetobuffer(buf)
        self.__field_command.writetobuffer(buf)
        self.__field_packettype.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_readwrite=UINT(**{'sizeinbytes': 1})
        self.__field_readwrite.readfrombuffer(buf)
        self.__field_command=UINT(**{'sizeinbytes': 1})
        self.__field_command.readfrombuffer(buf)
        self.__field_packettype=UINT(**{'sizeinbytes': 1})
        self.__field_packettype.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_readwrite(self):
        return self.__field_readwrite.getvalue()
    def __setfield_readwrite(self, value):
        if isinstance(value,UINT):
            self.__field_readwrite=value
        else:
            self.__field_readwrite=UINT(value,**{'sizeinbytes': 1})
    def __delfield_readwrite(self): del self.__field_readwrite
    readwrite=property(__getfield_readwrite, __setfield_readwrite, __delfield_readwrite, None)
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,UINT):
            self.__field_command=value
        else:
            self.__field_command=UINT(value,**{'sizeinbytes': 1})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_packettype(self):
        return self.__field_packettype.getvalue()
    def __setfield_packettype(self, value):
        if isinstance(value,UINT):
            self.__field_packettype=value
        else:
            self.__field_packettype=UINT(value,**{'sizeinbytes': 1})
    def __delfield_packettype(self): del self.__field_packettype
    packettype=property(__getfield_packettype, __setfield_packettype, __delfield_packettype, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('readwrite', self.__field_readwrite, None)
        yield ('command', self.__field_command, None)
        yield ('packettype', self.__field_packettype, None)
class qcpwriteheader(BaseProtogenClass):
    __fields=['readwrite', 'command', 'packettype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update({'readwrite': 0x27})
        dict.update(kwargs)
        super(qcpwriteheader,self).__init__(**dict)
        if self.__class__ is qcpwriteheader:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(qcpwriteheader,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(qcpwriteheader,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_readwrite.writetobuffer(buf)
        self.__field_command.writetobuffer(buf)
        self.__field_packettype.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_readwrite=UINT(**{'sizeinbytes': 1})
        self.__field_readwrite.readfrombuffer(buf)
        self.__field_command=UINT(**{'sizeinbytes': 1})
        self.__field_command.readfrombuffer(buf)
        self.__field_packettype=UINT(**{'sizeinbytes': 1})
        self.__field_packettype.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_readwrite(self):
        return self.__field_readwrite.getvalue()
    def __setfield_readwrite(self, value):
        if isinstance(value,UINT):
            self.__field_readwrite=value
        else:
            self.__field_readwrite=UINT(value,**{'sizeinbytes': 1})
    def __delfield_readwrite(self): del self.__field_readwrite
    readwrite=property(__getfield_readwrite, __setfield_readwrite, __delfield_readwrite, None)
    def __getfield_command(self):
        return self.__field_command.getvalue()
    def __setfield_command(self, value):
        if isinstance(value,UINT):
            self.__field_command=value
        else:
            self.__field_command=UINT(value,**{'sizeinbytes': 1})
    def __delfield_command(self): del self.__field_command
    command=property(__getfield_command, __setfield_command, __delfield_command, None)
    def __getfield_packettype(self):
        return self.__field_packettype.getvalue()
    def __setfield_packettype(self, value):
        if isinstance(value,UINT):
            self.__field_packettype=value
        else:
            self.__field_packettype=UINT(value,**{'sizeinbytes': 1})
    def __delfield_packettype(self): del self.__field_packettype
    packettype=property(__getfield_packettype, __setfield_packettype, __delfield_packettype, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('readwrite', self.__field_readwrite, None)
        yield ('command', self.__field_command, None)
        yield ('packettype', self.__field_packettype, None)
class pbsortbuffer(BaseProtogenClass):
    "Various arrays for sorting the phone book, speed dial, determining which"
    __fields=['startcommand', 'bufsize', 'comment', 'usedflags', 'slotsused', 'slotsused2', 'numemail', 'numurl', 'firsttypes', 'sortorder', 'pbfirstletters', 'sortorder2', 'speeddialindex', 'longnumbersindex', 'emails', 'emailfirstletters', 'urls', 'urlfirstletters', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(pbsortbuffer,self).__init__(**dict)
        if self.__class__ is pbsortbuffer:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(pbsortbuffer,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(pbsortbuffer,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
        if getattr(self, '__field_startcommand', None) is None:
            self.__field_startcommand=UINT(**{'constant': 0x3c})
        if getattr(self, '__field_bufsize', None) is None:
            self.__field_bufsize=UINT(**{'constant': 7168})
        if getattr(self, '__field_comment', None) is None:
            self.__field_comment=STRING(**{'default': "sort buffer"})
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_usedflags
        except:
            self.__field_usedflags=LIST(**{'elementclass': _gen_p_sanyo4930_57, 'length': _NUMPBSLOTS, 'createdefault': True})
        self.__field_usedflags.writetobuffer(buf)
        self.__field_slotsused.writetobuffer(buf)
        self.__field_slotsused2.writetobuffer(buf)
        self.__field_numemail.writetobuffer(buf)
        self.__field_numurl.writetobuffer(buf)
        try: self.__field_firsttypes
        except:
            self.__field_firsttypes=LIST(**{'elementclass': _gen_p_sanyo4930_63, 'length': _NUMPBSLOTS})
        self.__field_firsttypes.writetobuffer(buf)
        try: self.__field_sortorder
        except:
            self.__field_sortorder=LIST(**{'elementclass': _gen_p_sanyo4930_65, 'length': _NUMPBSLOTS})
        self.__field_sortorder.writetobuffer(buf)
        self.__field_pbfirstletters.writetobuffer(buf)
        try: self.__field_sortorder2
        except:
            self.__field_sortorder2=LIST(**{'elementclass': _gen_p_sanyo4930_68, 'length': _NUMPBSLOTS})
        self.__field_sortorder2.writetobuffer(buf)
        try: self.__field_speeddialindex
        except:
            self.__field_speeddialindex=LIST(**{'elementclass': _gen_p_sanyo4930_70, 'length': _NUMSPEEDDIALS})
        self.__field_speeddialindex.writetobuffer(buf)
        try: self.__field_longnumbersindex
        except:
            self.__field_longnumbersindex=LIST(**{'elementclass': _gen_p_sanyo4930_72, 'length': _NUMLONGNUMBERS})
        self.__field_longnumbersindex.writetobuffer(buf)
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'elementclass': _gen_p_sanyo4930_74, 'length': _NUMPBSLOTS})
        self.__field_emails.writetobuffer(buf)
        self.__field_emailfirstletters.writetobuffer(buf)
        try: self.__field_urls
        except:
            self.__field_urls=LIST(**{'elementclass': _gen_p_sanyo4930_77, 'length': _NUMPBSLOTS})
        self.__field_urls.writetobuffer(buf)
        self.__field_urlfirstletters.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN()
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_usedflags=LIST(**{'elementclass': _gen_p_sanyo4930_57, 'length': _NUMPBSLOTS, 'createdefault': True})
        self.__field_usedflags.readfrombuffer(buf)
        self.__field_slotsused=UINT(**{'sizeinbytes': 2})
        self.__field_slotsused.readfrombuffer(buf)
        self.__field_slotsused2=UINT(**{'sizeinbytes': 2})
        self.__field_slotsused2.readfrombuffer(buf)
        self.__field_numemail=UINT(**{'sizeinbytes': 2})
        self.__field_numemail.readfrombuffer(buf)
        self.__field_numurl=UINT(**{'sizeinbytes': 2})
        self.__field_numurl.readfrombuffer(buf)
        self.__field_firsttypes=LIST(**{'elementclass': _gen_p_sanyo4930_63, 'length': _NUMPBSLOTS})
        self.__field_firsttypes.readfrombuffer(buf)
        self.__field_sortorder=LIST(**{'elementclass': _gen_p_sanyo4930_65, 'length': _NUMPBSLOTS})
        self.__field_sortorder.readfrombuffer(buf)
        self.__field_pbfirstletters=STRING(**{'terminator': None, 'sizeinbytes': _NUMPBSLOTS})
        self.__field_pbfirstletters.readfrombuffer(buf)
        self.__field_sortorder2=LIST(**{'elementclass': _gen_p_sanyo4930_68, 'length': _NUMPBSLOTS})
        self.__field_sortorder2.readfrombuffer(buf)
        self.__field_speeddialindex=LIST(**{'elementclass': _gen_p_sanyo4930_70, 'length': _NUMSPEEDDIALS})
        self.__field_speeddialindex.readfrombuffer(buf)
        self.__field_longnumbersindex=LIST(**{'elementclass': _gen_p_sanyo4930_72, 'length': _NUMLONGNUMBERS})
        self.__field_longnumbersindex.readfrombuffer(buf)
        self.__field_emails=LIST(**{'elementclass': _gen_p_sanyo4930_74, 'length': _NUMPBSLOTS})
        self.__field_emails.readfrombuffer(buf)
        self.__field_emailfirstletters=STRING(**{'terminator': None, 'sizeinbytes': _NUMPBSLOTS})
        self.__field_emailfirstletters.readfrombuffer(buf)
        self.__field_urls=LIST(**{'elementclass': _gen_p_sanyo4930_77, 'length': _NUMPBSLOTS})
        self.__field_urls.readfrombuffer(buf)
        self.__field_urlfirstletters=STRING(**{'terminator': None, 'sizeinbytes': _NUMPBSLOTS})
        self.__field_urlfirstletters.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_startcommand(self):
        return self.__field_startcommand.getvalue()
    def __setfield_startcommand(self, value):
        if isinstance(value,UINT):
            self.__field_startcommand=value
        else:
            self.__field_startcommand=UINT(value,**{'constant': 0x3c})
    def __delfield_startcommand(self): del self.__field_startcommand
    startcommand=property(__getfield_startcommand, __setfield_startcommand, __delfield_startcommand, "Starting command for R/W buf parts")
    def __getfield_bufsize(self):
        return self.__field_bufsize.getvalue()
    def __setfield_bufsize(self, value):
        if isinstance(value,UINT):
            self.__field_bufsize=value
        else:
            self.__field_bufsize=UINT(value,**{'constant': 7168})
    def __delfield_bufsize(self): del self.__field_bufsize
    bufsize=property(__getfield_bufsize, __setfield_bufsize, __delfield_bufsize, None)
    def __getfield_comment(self):
        try: self.__field_comment
        except:
            self.__field_comment=STRING(**{'default': "sort buffer"})
        return self.__field_comment.getvalue()
    def __setfield_comment(self, value):
        if isinstance(value,STRING):
            self.__field_comment=value
        else:
            self.__field_comment=STRING(value,**{'default': "sort buffer"})
    def __delfield_comment(self): del self.__field_comment
    comment=property(__getfield_comment, __setfield_comment, __delfield_comment, None)
    def __getfield_usedflags(self):
        try: self.__field_usedflags
        except:
            self.__field_usedflags=LIST(**{'elementclass': _gen_p_sanyo4930_57, 'length': _NUMPBSLOTS, 'createdefault': True})
        return self.__field_usedflags.getvalue()
    def __setfield_usedflags(self, value):
        if isinstance(value,LIST):
            self.__field_usedflags=value
        else:
            self.__field_usedflags=LIST(value,**{'elementclass': _gen_p_sanyo4930_57, 'length': _NUMPBSLOTS, 'createdefault': True})
    def __delfield_usedflags(self): del self.__field_usedflags
    usedflags=property(__getfield_usedflags, __setfield_usedflags, __delfield_usedflags, None)
    def __getfield_slotsused(self):
        return self.__field_slotsused.getvalue()
    def __setfield_slotsused(self, value):
        if isinstance(value,UINT):
            self.__field_slotsused=value
        else:
            self.__field_slotsused=UINT(value,**{'sizeinbytes': 2})
    def __delfield_slotsused(self): del self.__field_slotsused
    slotsused=property(__getfield_slotsused, __setfield_slotsused, __delfield_slotsused, None)
    def __getfield_slotsused2(self):
        return self.__field_slotsused2.getvalue()
    def __setfield_slotsused2(self, value):
        if isinstance(value,UINT):
            self.__field_slotsused2=value
        else:
            self.__field_slotsused2=UINT(value,**{'sizeinbytes': 2})
    def __delfield_slotsused2(self): del self.__field_slotsused2
    slotsused2=property(__getfield_slotsused2, __setfield_slotsused2, __delfield_slotsused2, "Always seems to be the same.  Why duplicated?")
    def __getfield_numemail(self):
        return self.__field_numemail.getvalue()
    def __setfield_numemail(self, value):
        if isinstance(value,UINT):
            self.__field_numemail=value
        else:
            self.__field_numemail=UINT(value,**{'sizeinbytes': 2})
    def __delfield_numemail(self): del self.__field_numemail
    numemail=property(__getfield_numemail, __setfield_numemail, __delfield_numemail, "Num of slots with email")
    def __getfield_numurl(self):
        return self.__field_numurl.getvalue()
    def __setfield_numurl(self, value):
        if isinstance(value,UINT):
            self.__field_numurl=value
        else:
            self.__field_numurl=UINT(value,**{'sizeinbytes': 2})
    def __delfield_numurl(self): del self.__field_numurl
    numurl=property(__getfield_numurl, __setfield_numurl, __delfield_numurl, "Num of slots with URL")
    def __getfield_firsttypes(self):
        try: self.__field_firsttypes
        except:
            self.__field_firsttypes=LIST(**{'elementclass': _gen_p_sanyo4930_63, 'length': _NUMPBSLOTS})
        return self.__field_firsttypes.getvalue()
    def __setfield_firsttypes(self, value):
        if isinstance(value,LIST):
            self.__field_firsttypes=value
        else:
            self.__field_firsttypes=LIST(value,**{'elementclass': _gen_p_sanyo4930_63, 'length': _NUMPBSLOTS})
    def __delfield_firsttypes(self): del self.__field_firsttypes
    firsttypes=property(__getfield_firsttypes, __setfield_firsttypes, __delfield_firsttypes, None)
    def __getfield_sortorder(self):
        try: self.__field_sortorder
        except:
            self.__field_sortorder=LIST(**{'elementclass': _gen_p_sanyo4930_65, 'length': _NUMPBSLOTS})
        return self.__field_sortorder.getvalue()
    def __setfield_sortorder(self, value):
        if isinstance(value,LIST):
            self.__field_sortorder=value
        else:
            self.__field_sortorder=LIST(value,**{'elementclass': _gen_p_sanyo4930_65, 'length': _NUMPBSLOTS})
    def __delfield_sortorder(self): del self.__field_sortorder
    sortorder=property(__getfield_sortorder, __setfield_sortorder, __delfield_sortorder, None)
    def __getfield_pbfirstletters(self):
        return self.__field_pbfirstletters.getvalue()
    def __setfield_pbfirstletters(self, value):
        if isinstance(value,STRING):
            self.__field_pbfirstletters=value
        else:
            self.__field_pbfirstletters=STRING(value,**{'terminator': None, 'sizeinbytes': _NUMPBSLOTS})
    def __delfield_pbfirstletters(self): del self.__field_pbfirstletters
    pbfirstletters=property(__getfield_pbfirstletters, __setfield_pbfirstletters, __delfield_pbfirstletters, None)
    def __getfield_sortorder2(self):
        try: self.__field_sortorder2
        except:
            self.__field_sortorder2=LIST(**{'elementclass': _gen_p_sanyo4930_68, 'length': _NUMPBSLOTS})
        return self.__field_sortorder2.getvalue()
    def __setfield_sortorder2(self, value):
        if isinstance(value,LIST):
            self.__field_sortorder2=value
        else:
            self.__field_sortorder2=LIST(value,**{'elementclass': _gen_p_sanyo4930_68, 'length': _NUMPBSLOTS})
    def __delfield_sortorder2(self): del self.__field_sortorder2
    sortorder2=property(__getfield_sortorder2, __setfield_sortorder2, __delfield_sortorder2, "Is this the same")
    def __getfield_speeddialindex(self):
        try: self.__field_speeddialindex
        except:
            self.__field_speeddialindex=LIST(**{'elementclass': _gen_p_sanyo4930_70, 'length': _NUMSPEEDDIALS})
        return self.__field_speeddialindex.getvalue()
    def __setfield_speeddialindex(self, value):
        if isinstance(value,LIST):
            self.__field_speeddialindex=value
        else:
            self.__field_speeddialindex=LIST(value,**{'elementclass': _gen_p_sanyo4930_70, 'length': _NUMSPEEDDIALS})
    def __delfield_speeddialindex(self): del self.__field_speeddialindex
    speeddialindex=property(__getfield_speeddialindex, __setfield_speeddialindex, __delfield_speeddialindex, None)
    def __getfield_longnumbersindex(self):
        try: self.__field_longnumbersindex
        except:
            self.__field_longnumbersindex=LIST(**{'elementclass': _gen_p_sanyo4930_72, 'length': _NUMLONGNUMBERS})
        return self.__field_longnumbersindex.getvalue()
    def __setfield_longnumbersindex(self, value):
        if isinstance(value,LIST):
            self.__field_longnumbersindex=value
        else:
            self.__field_longnumbersindex=LIST(value,**{'elementclass': _gen_p_sanyo4930_72, 'length': _NUMLONGNUMBERS})
    def __delfield_longnumbersindex(self): del self.__field_longnumbersindex
    longnumbersindex=property(__getfield_longnumbersindex, __setfield_longnumbersindex, __delfield_longnumbersindex, None)
    def __getfield_emails(self):
        try: self.__field_emails
        except:
            self.__field_emails=LIST(**{'elementclass': _gen_p_sanyo4930_74, 'length': _NUMPBSLOTS})
        return self.__field_emails.getvalue()
    def __setfield_emails(self, value):
        if isinstance(value,LIST):
            self.__field_emails=value
        else:
            self.__field_emails=LIST(value,**{'elementclass': _gen_p_sanyo4930_74, 'length': _NUMPBSLOTS})
    def __delfield_emails(self): del self.__field_emails
    emails=property(__getfield_emails, __setfield_emails, __delfield_emails, "Sorted list of slots with Email")
    def __getfield_emailfirstletters(self):
        return self.__field_emailfirstletters.getvalue()
    def __setfield_emailfirstletters(self, value):
        if isinstance(value,STRING):
            self.__field_emailfirstletters=value
        else:
            self.__field_emailfirstletters=STRING(value,**{'terminator': None, 'sizeinbytes': _NUMPBSLOTS})
    def __delfield_emailfirstletters(self): del self.__field_emailfirstletters
    emailfirstletters=property(__getfield_emailfirstletters, __setfield_emailfirstletters, __delfield_emailfirstletters, "First letters in sort order")
    def __getfield_urls(self):
        try: self.__field_urls
        except:
            self.__field_urls=LIST(**{'elementclass': _gen_p_sanyo4930_77, 'length': _NUMPBSLOTS})
        return self.__field_urls.getvalue()
    def __setfield_urls(self, value):
        if isinstance(value,LIST):
            self.__field_urls=value
        else:
            self.__field_urls=LIST(value,**{'elementclass': _gen_p_sanyo4930_77, 'length': _NUMPBSLOTS})
    def __delfield_urls(self): del self.__field_urls
    urls=property(__getfield_urls, __setfield_urls, __delfield_urls, "Sorted list of slots with a URL")
    def __getfield_urlfirstletters(self):
        return self.__field_urlfirstletters.getvalue()
    def __setfield_urlfirstletters(self, value):
        if isinstance(value,STRING):
            self.__field_urlfirstletters=value
        else:
            self.__field_urlfirstletters=STRING(value,**{'terminator': None, 'sizeinbytes': _NUMPBSLOTS})
    def __delfield_urlfirstletters(self): del self.__field_urlfirstletters
    urlfirstletters=property(__getfield_urlfirstletters, __setfield_urlfirstletters, __delfield_urlfirstletters, "First letters in sort order")
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN()
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('startcommand', self.__field_startcommand, "Starting command for R/W buf parts")
        yield ('bufsize', self.__field_bufsize, None)
        yield ('comment', self.__field_comment, None)
        yield ('usedflags', self.__field_usedflags, None)
        yield ('slotsused', self.__field_slotsused, None)
        yield ('slotsused2', self.__field_slotsused2, "Always seems to be the same.  Why duplicated?")
        yield ('numemail', self.__field_numemail, "Num of slots with email")
        yield ('numurl', self.__field_numurl, "Num of slots with URL")
        yield ('firsttypes', self.__field_firsttypes, None)
        yield ('sortorder', self.__field_sortorder, None)
        yield ('pbfirstletters', self.__field_pbfirstletters, None)
        yield ('sortorder2', self.__field_sortorder2, "Is this the same")
        yield ('speeddialindex', self.__field_speeddialindex, None)
        yield ('longnumbersindex', self.__field_longnumbersindex, None)
        yield ('emails', self.__field_emails, "Sorted list of slots with Email")
        yield ('emailfirstletters', self.__field_emailfirstletters, "First letters in sort order")
        yield ('urls', self.__field_urls, "Sorted list of slots with a URL")
        yield ('urlfirstletters', self.__field_urlfirstletters, "First letters in sort order")
        yield ('pad', self.__field_pad, None)
class _gen_p_sanyo4930_57(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['used']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_57,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_57:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_57,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_57,kwargs)
        if len(args):
            dict2={'sizeinbytes': 1}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_used=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_used.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_used=UINT(**{'sizeinbytes': 1})
        self.__field_used.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_used(self):
        return self.__field_used.getvalue()
    def __setfield_used(self, value):
        if isinstance(value,UINT):
            self.__field_used=value
        else:
            self.__field_used=UINT(value,**{'sizeinbytes': 1})
    def __delfield_used(self): del self.__field_used
    used=property(__getfield_used, __setfield_used, __delfield_used, "1 if slot in use")
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('used', self.__field_used, "1 if slot in use")
class _gen_p_sanyo4930_63(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['firsttype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_63,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_63:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_63,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_63,kwargs)
        if len(args):
            dict2={'sizeinbytes': 1}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_firsttype=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_firsttype.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_firsttype=UINT(**{'sizeinbytes': 1})
        self.__field_firsttype.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_firsttype(self):
        return self.__field_firsttype.getvalue()
    def __setfield_firsttype(self, value):
        if isinstance(value,UINT):
            self.__field_firsttype=value
        else:
            self.__field_firsttype=UINT(value,**{'sizeinbytes': 1})
    def __delfield_firsttype(self): del self.__field_firsttype
    firsttype=property(__getfield_firsttype, __setfield_firsttype, __delfield_firsttype, "First phone number type in each slot")
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('firsttype', self.__field_firsttype, "First phone number type in each slot")
class _gen_p_sanyo4930_65(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['pbslot']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_65,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_65:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_65,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_65,kwargs)
        if len(args):
            dict2={'sizeinbytes': 2, 'default': 0xffff}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_pbslot=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_pbslot.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_pbslot(self):
        return self.__field_pbslot.getvalue()
    def __setfield_pbslot(self, value):
        if isinstance(value,UINT):
            self.__field_pbslot=value
        else:
            self.__field_pbslot=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
    def __delfield_pbslot(self): del self.__field_pbslot
    pbslot=property(__getfield_pbslot, __setfield_pbslot, __delfield_pbslot, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('pbslot', self.__field_pbslot, None)
class _gen_p_sanyo4930_68(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['pbslot']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_68,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_68:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_68,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_68,kwargs)
        if len(args):
            dict2={'sizeinbytes': 2, 'default': 0xffff}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_pbslot=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_pbslot.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_pbslot(self):
        return self.__field_pbslot.getvalue()
    def __setfield_pbslot(self, value):
        if isinstance(value,UINT):
            self.__field_pbslot=value
        else:
            self.__field_pbslot=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
    def __delfield_pbslot(self): del self.__field_pbslot
    pbslot=property(__getfield_pbslot, __setfield_pbslot, __delfield_pbslot, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('pbslot', self.__field_pbslot, None)
class _gen_p_sanyo4930_70(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['pbslotandtype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_70,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_70:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_70,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_70,kwargs)
        if len(args):
            dict2={'sizeinbytes': 2, 'default': 0xffff}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_pbslotandtype=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslotandtype.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslotandtype=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_pbslotandtype.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_pbslotandtype(self):
        return self.__field_pbslotandtype.getvalue()
    def __setfield_pbslotandtype(self, value):
        if isinstance(value,UINT):
            self.__field_pbslotandtype=value
        else:
            self.__field_pbslotandtype=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
    def __delfield_pbslotandtype(self): del self.__field_pbslotandtype
    pbslotandtype=property(__getfield_pbslotandtype, __setfield_pbslotandtype, __delfield_pbslotandtype, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('pbslotandtype', self.__field_pbslotandtype, None)
class _gen_p_sanyo4930_72(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['pbslotandtype']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_72,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_72:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_72,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_72,kwargs)
        if len(args):
            dict2={'sizeinbytes': 2, 'default': 0xffff}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_pbslotandtype=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslotandtype.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslotandtype=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_pbslotandtype.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_pbslotandtype(self):
        return self.__field_pbslotandtype.getvalue()
    def __setfield_pbslotandtype(self, value):
        if isinstance(value,UINT):
            self.__field_pbslotandtype=value
        else:
            self.__field_pbslotandtype=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
    def __delfield_pbslotandtype(self): del self.__field_pbslotandtype
    pbslotandtype=property(__getfield_pbslotandtype, __setfield_pbslotandtype, __delfield_pbslotandtype, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('pbslotandtype', self.__field_pbslotandtype, None)
class _gen_p_sanyo4930_74(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['pbslot']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_74,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_74:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_74,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_74,kwargs)
        if len(args):
            dict2={'sizeinbytes': 2, 'default': 0xffff}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_pbslot=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_pbslot.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_pbslot(self):
        return self.__field_pbslot.getvalue()
    def __setfield_pbslot(self, value):
        if isinstance(value,UINT):
            self.__field_pbslot=value
        else:
            self.__field_pbslot=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
    def __delfield_pbslot(self): del self.__field_pbslot
    pbslot=property(__getfield_pbslot, __setfield_pbslot, __delfield_pbslot, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('pbslot', self.__field_pbslot, None)
class _gen_p_sanyo4930_77(BaseProtogenClass):
    'Anonymous inner class'
    __fields=['pbslot']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(_gen_p_sanyo4930_77,self).__init__(**dict)
        if self.__class__ is _gen_p_sanyo4930_77:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(_gen_p_sanyo4930_77,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(_gen_p_sanyo4930_77,kwargs)
        if len(args):
            dict2={'sizeinbytes': 2, 'default': 0xffff}
            dict2.update(kwargs)
            kwargs=dict2
            self.__field_pbslot=UINT(*args,**dict2)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_pbslot=UINT(**{'sizeinbytes': 2, 'default': 0xffff})
        self.__field_pbslot.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_pbslot(self):
        return self.__field_pbslot.getvalue()
    def __setfield_pbslot(self, value):
        if isinstance(value,UINT):
            self.__field_pbslot=value
        else:
            self.__field_pbslot=UINT(value,**{'sizeinbytes': 2, 'default': 0xffff})
    def __delfield_pbslot(self): del self.__field_pbslot
    pbslot=property(__getfield_pbslot, __setfield_pbslot, __delfield_pbslot, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('pbslot', self.__field_pbslot, None)
class calleridbuffer(BaseProtogenClass):
    "Index so that phone can show a name instead of number"
    __fields=['maxentries', 'startcommand', 'bufsize', 'comment', 'numentries', 'items', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(calleridbuffer,self).__init__(**dict)
        if self.__class__ is calleridbuffer:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(calleridbuffer,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(calleridbuffer,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
        if getattr(self, '__field_maxentries', None) is None:
            self.__field_maxentries=UINT(**{'constant': 700})
        if getattr(self, '__field_startcommand', None) is None:
            self.__field_startcommand=UINT(**{'constant': 0x46})
        if getattr(self, '__field_bufsize', None) is None:
            self.__field_bufsize=UINT(**{'constant': 9216})
        if getattr(self, '__field_comment', None) is None:
            self.__field_comment=STRING(**{'default': "callerid"})
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numentries.writetobuffer(buf)
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'length': self.maxentries, 'elementclass': calleridentry, 'createdefault': True})
        self.__field_items.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN()
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_numentries=UINT(**{'sizeinbytes': 2})
        self.__field_numentries.readfrombuffer(buf)
        self.__field_items=LIST(**{'length': self.maxentries, 'elementclass': calleridentry, 'createdefault': True})
        self.__field_items.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_maxentries(self):
        return self.__field_maxentries.getvalue()
    def __setfield_maxentries(self, value):
        if isinstance(value,UINT):
            self.__field_maxentries=value
        else:
            self.__field_maxentries=UINT(value,**{'constant': 700})
    def __delfield_maxentries(self): del self.__field_maxentries
    maxentries=property(__getfield_maxentries, __setfield_maxentries, __delfield_maxentries, None)
    def __getfield_startcommand(self):
        return self.__field_startcommand.getvalue()
    def __setfield_startcommand(self, value):
        if isinstance(value,UINT):
            self.__field_startcommand=value
        else:
            self.__field_startcommand=UINT(value,**{'constant': 0x46})
    def __delfield_startcommand(self): del self.__field_startcommand
    startcommand=property(__getfield_startcommand, __setfield_startcommand, __delfield_startcommand, "Starting command for R/W buf parts")
    def __getfield_bufsize(self):
        return self.__field_bufsize.getvalue()
    def __setfield_bufsize(self, value):
        if isinstance(value,UINT):
            self.__field_bufsize=value
        else:
            self.__field_bufsize=UINT(value,**{'constant': 9216})
    def __delfield_bufsize(self): del self.__field_bufsize
    bufsize=property(__getfield_bufsize, __setfield_bufsize, __delfield_bufsize, None)
    def __getfield_comment(self):
        try: self.__field_comment
        except:
            self.__field_comment=STRING(**{'default': "callerid"})
        return self.__field_comment.getvalue()
    def __setfield_comment(self, value):
        if isinstance(value,STRING):
            self.__field_comment=value
        else:
            self.__field_comment=STRING(value,**{'default': "callerid"})
    def __delfield_comment(self): del self.__field_comment
    comment=property(__getfield_comment, __setfield_comment, __delfield_comment, None)
    def __getfield_numentries(self):
        return self.__field_numentries.getvalue()
    def __setfield_numentries(self, value):
        if isinstance(value,UINT):
            self.__field_numentries=value
        else:
            self.__field_numentries=UINT(value,**{'sizeinbytes': 2})
    def __delfield_numentries(self): del self.__field_numentries
    numentries=property(__getfield_numentries, __setfield_numentries, __delfield_numentries, "Number phone numbers")
    def __getfield_items(self):
        try: self.__field_items
        except:
            self.__field_items=LIST(**{'length': self.maxentries, 'elementclass': calleridentry, 'createdefault': True})
        return self.__field_items.getvalue()
    def __setfield_items(self, value):
        if isinstance(value,LIST):
            self.__field_items=value
        else:
            self.__field_items=LIST(value,**{'length': self.maxentries, 'elementclass': calleridentry, 'createdefault': True})
    def __delfield_items(self): del self.__field_items
    items=property(__getfield_items, __setfield_items, __delfield_items, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN()
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('maxentries', self.__field_maxentries, None)
        yield ('startcommand', self.__field_startcommand, "Starting command for R/W buf parts")
        yield ('bufsize', self.__field_bufsize, None)
        yield ('comment', self.__field_comment, None)
        yield ('numentries', self.__field_numentries, "Number phone numbers")
        yield ('items', self.__field_items, None)
        yield ('pad', self.__field_pad, None)
class eventresponse(BaseProtogenClass):
    __fields=['header', 'entry', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventresponse,self).__init__(**dict)
        if self.__class__ is eventresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=sanyoheader()
        self.__field_header.readfrombuffer(buf)
        self.__field_entry=evententry()
        self.__field_entry.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,sanyoheader):
            self.__field_header=value
        else:
            self.__field_header=sanyoheader(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,evententry):
            self.__field_entry=value
        else:
            self.__field_entry=evententry(value,)
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def __getfield_pad(self):
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('entry', self.__field_entry, None)
        yield ('pad', self.__field_pad, None)
class eventrequest(BaseProtogenClass):
    __fields=['header', 'slot', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventrequest,self).__init__(**dict)
        if self.__class__ is eventrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'packettype': 0x0c, 'command': 0x23})
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader(**{'packettype': 0x0c, 'command': 0x23})
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'packettype': 0x0c, 'command': 0x23})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,**{'packettype': 0x0c, 'command': 0x23})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 129})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('pad', self.__field_pad, None)
class eventslotinuserequest(BaseProtogenClass):
    __fields=['header', 'slot', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventslotinuserequest,self).__init__(**dict)
        if self.__class__ is eventslotinuserequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventslotinuserequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventslotinuserequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'readwrite': 0x26, 'packettype': 0x0d, 'command': 0x74})
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader(**{'readwrite': 0x26, 'packettype': 0x0d, 'command': 0x74})
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'readwrite': 0x26, 'packettype': 0x0d, 'command': 0x74})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,**{'readwrite': 0x26, 'packettype': 0x0d, 'command': 0x74})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 129})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('pad', self.__field_pad, None)
class evententry(BaseProtogenClass):
    __fields=['slot', 'eventname', 'pad1', 'eventname_len', 'start', 'end', 'location', 'pad2', 'location_len', 'alarmdiff', 'period', 'dom', 'alarm', 'pad3', 'serial', 'pad4', 'ringtone']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(evententry,self).__init__(**dict)
        if self.__class__ is evententry:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(evententry,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(evententry,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot.writetobuffer(buf)
        self.__field_eventname.writetobuffer(buf)
        try: self.__field_pad1
        except:
            self.__field_pad1=UNKNOWN(**{'sizeinbytes': 7})
        self.__field_pad1.writetobuffer(buf)
        self.__field_eventname_len.writetobuffer(buf)
        self.__field_start.writetobuffer(buf)
        self.__field_end.writetobuffer(buf)
        self.__field_location.writetobuffer(buf)
        try: self.__field_pad2
        except:
            self.__field_pad2=UNKNOWN(**{'sizeinbytes': 7})
        self.__field_pad2.writetobuffer(buf)
        self.__field_location_len.writetobuffer(buf)
        self.__field_alarmdiff.writetobuffer(buf)
        self.__field_period.writetobuffer(buf)
        self.__field_dom.writetobuffer(buf)
        self.__field_alarm.writetobuffer(buf)
        try: self.__field_pad3
        except:
            self.__field_pad3=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad3.writetobuffer(buf)
        try: self.__field_serial
        except:
            self.__field_serial=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_serial.writetobuffer(buf)
        try: self.__field_pad4
        except:
            self.__field_pad4=UNKNOWN(**{'sizeinbytes': 3})
        self.__field_pad4.writetobuffer(buf)
        self.__field_ringtone.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_eventname=STRING(**{'sizeinbytes': 14, 'raiseonunterminatedread': False, 'raiseontruncate': False, 'terminator': None})
        self.__field_eventname.readfrombuffer(buf)
        self.__field_pad1=UNKNOWN(**{'sizeinbytes': 7})
        self.__field_pad1.readfrombuffer(buf)
        self.__field_eventname_len=UINT(**{'sizeinbytes': 1})
        self.__field_eventname_len.readfrombuffer(buf)
        self.__field_start=UINT(**{'sizeinbytes': 4})
        self.__field_start.readfrombuffer(buf)
        self.__field_end=UINT(**{'sizeinbytes': 4})
        self.__field_end.readfrombuffer(buf)
        self.__field_location=STRING(**{'sizeinbytes': 14, 'raiseonunterminatedread': False, 'raiseontruncate': False, 'terminator': None})
        self.__field_location.readfrombuffer(buf)
        self.__field_pad2=UNKNOWN(**{'sizeinbytes': 7})
        self.__field_pad2.readfrombuffer(buf)
        self.__field_location_len=UINT(**{'sizeinbytes': 1})
        self.__field_location_len.readfrombuffer(buf)
        self.__field_alarmdiff=UINT(**{'sizeinbytes': 4})
        self.__field_alarmdiff.readfrombuffer(buf)
        self.__field_period=UINT(**{'sizeinbytes': 1})
        self.__field_period.readfrombuffer(buf)
        self.__field_dom=UINT(**{'sizeinbytes': 1})
        self.__field_dom.readfrombuffer(buf)
        self.__field_alarm=UINT(**{'sizeinbytes': 4})
        self.__field_alarm.readfrombuffer(buf)
        self.__field_pad3=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad3.readfrombuffer(buf)
        self.__field_serial=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_serial.readfrombuffer(buf)
        self.__field_pad4=UNKNOWN(**{'sizeinbytes': 3})
        self.__field_pad4.readfrombuffer(buf)
        self.__field_ringtone=UINT(**{'sizeinbytes': 2})
        self.__field_ringtone.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_eventname(self):
        return self.__field_eventname.getvalue()
    def __setfield_eventname(self, value):
        if isinstance(value,STRING):
            self.__field_eventname=value
        else:
            self.__field_eventname=STRING(value,**{'sizeinbytes': 14, 'raiseonunterminatedread': False, 'raiseontruncate': False, 'terminator': None})
    def __delfield_eventname(self): del self.__field_eventname
    eventname=property(__getfield_eventname, __setfield_eventname, __delfield_eventname, None)
    def __getfield_pad1(self):
        try: self.__field_pad1
        except:
            self.__field_pad1=UNKNOWN(**{'sizeinbytes': 7})
        return self.__field_pad1.getvalue()
    def __setfield_pad1(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad1=value
        else:
            self.__field_pad1=UNKNOWN(value,**{'sizeinbytes': 7})
    def __delfield_pad1(self): del self.__field_pad1
    pad1=property(__getfield_pad1, __setfield_pad1, __delfield_pad1, None)
    def __getfield_eventname_len(self):
        return self.__field_eventname_len.getvalue()
    def __setfield_eventname_len(self, value):
        if isinstance(value,UINT):
            self.__field_eventname_len=value
        else:
            self.__field_eventname_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_eventname_len(self): del self.__field_eventname_len
    eventname_len=property(__getfield_eventname_len, __setfield_eventname_len, __delfield_eventname_len, None)
    def __getfield_start(self):
        return self.__field_start.getvalue()
    def __setfield_start(self, value):
        if isinstance(value,UINT):
            self.__field_start=value
        else:
            self.__field_start=UINT(value,**{'sizeinbytes': 4})
    def __delfield_start(self): del self.__field_start
    start=property(__getfield_start, __setfield_start, __delfield_start, "# seconds since Jan 1, 1980 approximately")
    def __getfield_end(self):
        return self.__field_end.getvalue()
    def __setfield_end(self, value):
        if isinstance(value,UINT):
            self.__field_end=value
        else:
            self.__field_end=UINT(value,**{'sizeinbytes': 4})
    def __delfield_end(self): del self.__field_end
    end=property(__getfield_end, __setfield_end, __delfield_end, None)
    def __getfield_location(self):
        return self.__field_location.getvalue()
    def __setfield_location(self, value):
        if isinstance(value,STRING):
            self.__field_location=value
        else:
            self.__field_location=STRING(value,**{'sizeinbytes': 14, 'raiseonunterminatedread': False, 'raiseontruncate': False, 'terminator': None})
    def __delfield_location(self): del self.__field_location
    location=property(__getfield_location, __setfield_location, __delfield_location, None)
    def __getfield_pad2(self):
        try: self.__field_pad2
        except:
            self.__field_pad2=UNKNOWN(**{'sizeinbytes': 7})
        return self.__field_pad2.getvalue()
    def __setfield_pad2(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad2=value
        else:
            self.__field_pad2=UNKNOWN(value,**{'sizeinbytes': 7})
    def __delfield_pad2(self): del self.__field_pad2
    pad2=property(__getfield_pad2, __setfield_pad2, __delfield_pad2, None)
    def __getfield_location_len(self):
        return self.__field_location_len.getvalue()
    def __setfield_location_len(self, value):
        if isinstance(value,UINT):
            self.__field_location_len=value
        else:
            self.__field_location_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_location_len(self): del self.__field_location_len
    location_len=property(__getfield_location_len, __setfield_location_len, __delfield_location_len, None)
    def __getfield_alarmdiff(self):
        return self.__field_alarmdiff.getvalue()
    def __setfield_alarmdiff(self, value):
        if isinstance(value,UINT):
            self.__field_alarmdiff=value
        else:
            self.__field_alarmdiff=UINT(value,**{'sizeinbytes': 4})
    def __delfield_alarmdiff(self): del self.__field_alarmdiff
    alarmdiff=property(__getfield_alarmdiff, __setfield_alarmdiff, __delfield_alarmdiff, "Displayed alarm time")
    def __getfield_period(self):
        return self.__field_period.getvalue()
    def __setfield_period(self, value):
        if isinstance(value,UINT):
            self.__field_period=value
        else:
            self.__field_period=UINT(value,**{'sizeinbytes': 1})
    def __delfield_period(self): del self.__field_period
    period=property(__getfield_period, __setfield_period, __delfield_period, "No, Daily, Weekly, Monthly, Yearly")
    def __getfield_dom(self):
        return self.__field_dom.getvalue()
    def __setfield_dom(self, value):
        if isinstance(value,UINT):
            self.__field_dom=value
        else:
            self.__field_dom=UINT(value,**{'sizeinbytes': 1})
    def __delfield_dom(self): del self.__field_dom
    dom=property(__getfield_dom, __setfield_dom, __delfield_dom, "Day of month for the event")
    def __getfield_alarm(self):
        return self.__field_alarm.getvalue()
    def __setfield_alarm(self, value):
        if isinstance(value,UINT):
            self.__field_alarm=value
        else:
            self.__field_alarm=UINT(value,**{'sizeinbytes': 4})
    def __delfield_alarm(self): del self.__field_alarm
    alarm=property(__getfield_alarm, __setfield_alarm, __delfield_alarm, None)
    def __getfield_pad3(self):
        try: self.__field_pad3
        except:
            self.__field_pad3=UNKNOWN(**{'sizeinbytes': 1})
        return self.__field_pad3.getvalue()
    def __setfield_pad3(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad3=value
        else:
            self.__field_pad3=UNKNOWN(value,**{'sizeinbytes': 1})
    def __delfield_pad3(self): del self.__field_pad3
    pad3=property(__getfield_pad3, __setfield_pad3, __delfield_pad3, None)
    def __getfield_serial(self):
        try: self.__field_serial
        except:
            self.__field_serial=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_serial.getvalue()
    def __setfield_serial(self, value):
        if isinstance(value,UINT):
            self.__field_serial=value
        else:
            self.__field_serial=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_serial(self): del self.__field_serial
    serial=property(__getfield_serial, __setfield_serial, __delfield_serial, "Some kind of serial number")
    def __getfield_pad4(self):
        try: self.__field_pad4
        except:
            self.__field_pad4=UNKNOWN(**{'sizeinbytes': 3})
        return self.__field_pad4.getvalue()
    def __setfield_pad4(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad4=value
        else:
            self.__field_pad4=UNKNOWN(value,**{'sizeinbytes': 3})
    def __delfield_pad4(self): del self.__field_pad4
    pad4=property(__getfield_pad4, __setfield_pad4, __delfield_pad4, None)
    def __getfield_ringtone(self):
        return self.__field_ringtone.getvalue()
    def __setfield_ringtone(self, value):
        if isinstance(value,UINT):
            self.__field_ringtone=value
        else:
            self.__field_ringtone=UINT(value,**{'sizeinbytes': 2})
    def __delfield_ringtone(self): del self.__field_ringtone
    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('slot', self.__field_slot, None)
        yield ('eventname', self.__field_eventname, None)
        yield ('pad1', self.__field_pad1, None)
        yield ('eventname_len', self.__field_eventname_len, None)
        yield ('start', self.__field_start, "# seconds since Jan 1, 1980 approximately")
        yield ('end', self.__field_end, None)
        yield ('location', self.__field_location, None)
        yield ('pad2', self.__field_pad2, None)
        yield ('location_len', self.__field_location_len, None)
        yield ('alarmdiff', self.__field_alarmdiff, "Displayed alarm time")
        yield ('period', self.__field_period, "No, Daily, Weekly, Monthly, Yearly")
        yield ('dom', self.__field_dom, "Day of month for the event")
        yield ('alarm', self.__field_alarm, None)
        yield ('pad3', self.__field_pad3, None)
        yield ('serial', self.__field_serial, "Some kind of serial number")
        yield ('pad4', self.__field_pad4, None)
        yield ('ringtone', self.__field_ringtone, None)
class eventresponse(BaseProtogenClass):
    __fields=['header', 'entry', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventresponse,self).__init__(**dict)
        if self.__class__ is eventresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader()
        self.__field_header.readfrombuffer(buf)
        self.__field_entry=evententry()
        self.__field_entry.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,evententry):
            self.__field_entry=value
        else:
            self.__field_entry=evententry(value,)
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def __getfield_pad(self):
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('entry', self.__field_entry, None)
        yield ('pad', self.__field_pad, None)
class eventslotinuseresponse(BaseProtogenClass):
    __fields=['header', 'slot', 'flag', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventslotinuseresponse,self).__init__(**dict)
        if self.__class__ is eventslotinuseresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventslotinuseresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventslotinuseresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        self.__field_flag.writetobuffer(buf)
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader()
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_flag=UINT(**{'sizeinbytes': 1})
        self.__field_flag.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
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
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('flag', self.__field_flag, None)
        yield ('pad', self.__field_pad, None)
class eventslotinuseupdaterequest(BaseProtogenClass):
    __fields=['header', 'slot', 'flag', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventslotinuseupdaterequest,self).__init__(**dict)
        if self.__class__ is eventslotinuseupdaterequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventslotinuseupdaterequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventslotinuseupdaterequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpwriteheader(**{'packettype': 0x0d, 'command': 0x74})
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        self.__field_flag.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 124})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpwriteheader(**{'packettype': 0x0d, 'command': 0x74})
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_flag=UINT(**{'sizeinbytes': 1})
        self.__field_flag.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 124})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpwriteheader(**{'packettype': 0x0d, 'command': 0x74})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpwriteheader):
            self.__field_header=value
        else:
            self.__field_header=qcpwriteheader(value,**{'packettype': 0x0d, 'command': 0x74})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
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
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 124})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 124})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('flag', self.__field_flag, None)
        yield ('pad', self.__field_pad, None)
class eventupdaterequest(BaseProtogenClass):
    __fields=['header', 'entry', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(eventupdaterequest,self).__init__(**dict)
        if self.__class__ is eventupdaterequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(eventupdaterequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(eventupdaterequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpwriteheader(**{'packettype': 0x0c, 'command':0x23})
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 56})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpwriteheader(**{'packettype': 0x0c, 'command':0x23})
        self.__field_header.readfrombuffer(buf)
        self.__field_entry=evententry()
        self.__field_entry.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 56})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpwriteheader(**{'packettype': 0x0c, 'command':0x23})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpwriteheader):
            self.__field_header=value
        else:
            self.__field_header=qcpwriteheader(value,**{'packettype': 0x0c, 'command':0x23})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,evententry):
            self.__field_entry=value
        else:
            self.__field_entry=evententry(value,)
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 56})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 56})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('entry', self.__field_entry, None)
        yield ('pad', self.__field_pad, None)
class callalarmrequest(BaseProtogenClass):
    __fields=['header', 'slot', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(callalarmrequest,self).__init__(**dict)
        if self.__class__ is callalarmrequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(callalarmrequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(callalarmrequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'packettype': 0x0c, 'command': 0x24})
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader(**{'packettype': 0x0c, 'command': 0x24})
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'packettype': 0x0c, 'command': 0x24})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,**{'packettype': 0x0c, 'command': 0x24})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 129})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('pad', self.__field_pad, None)
class callalarmresponse(BaseProtogenClass):
    __fields=['header', 'entry', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(callalarmresponse,self).__init__(**dict)
        if self.__class__ is callalarmresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(callalarmresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(callalarmresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader()
        self.__field_header.readfrombuffer(buf)
        self.__field_entry=callalarmentry()
        self.__field_entry.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,callalarmentry):
            self.__field_entry=value
        else:
            self.__field_entry=callalarmentry(value,)
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def __getfield_pad(self):
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('entry', self.__field_entry, None)
        yield ('pad', self.__field_pad, None)
class callalarmupdaterequest(BaseProtogenClass):
    __fields=['header', 'entry', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(callalarmupdaterequest,self).__init__(**dict)
        if self.__class__ is callalarmupdaterequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(callalarmupdaterequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(callalarmupdaterequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpwriteheader(**{'packettype': 0x0c, 'command':0x24})
        self.__field_header.writetobuffer(buf)
        self.__field_entry.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 40})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpwriteheader(**{'packettype': 0x0c, 'command':0x24})
        self.__field_header.readfrombuffer(buf)
        self.__field_entry=callalarmentry()
        self.__field_entry.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 40})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpwriteheader(**{'packettype': 0x0c, 'command':0x24})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpwriteheader):
            self.__field_header=value
        else:
            self.__field_header=qcpwriteheader(value,**{'packettype': 0x0c, 'command':0x24})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_entry(self):
        return self.__field_entry.getvalue()
    def __setfield_entry(self, value):
        if isinstance(value,callalarmentry):
            self.__field_entry=value
        else:
            self.__field_entry=callalarmentry(value,)
    def __delfield_entry(self): del self.__field_entry
    entry=property(__getfield_entry, __setfield_entry, __delfield_entry, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 40})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 40})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('entry', self.__field_entry, None)
        yield ('pad', self.__field_pad, None)
class callalarmslotinuserequest(BaseProtogenClass):
    __fields=['header', 'slot', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(callalarmslotinuserequest,self).__init__(**dict)
        if self.__class__ is callalarmslotinuserequest:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(callalarmslotinuserequest,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(callalarmslotinuserequest,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'packettype': 0x0d, 'command': 0x76})
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader(**{'packettype': 0x0d, 'command': 0x76})
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        try: self.__field_header
        except:
            self.__field_header=qcpheader(**{'packettype': 0x0d, 'command': 0x76})
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,**{'packettype': 0x0d, 'command': 0x76})
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_pad(self):
        try: self.__field_pad
        except:
            self.__field_pad=UNKNOWN(**{'sizeinbytes': 129})
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,**{'sizeinbytes': 129})
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('pad', self.__field_pad, None)
class callalarmslotinuseresponse(BaseProtogenClass):
    __fields=['header', 'slot', 'flag', 'pad']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(callalarmslotinuseresponse,self).__init__(**dict)
        if self.__class__ is callalarmslotinuseresponse:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(callalarmslotinuseresponse,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(callalarmslotinuseresponse,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header.writetobuffer(buf)
        self.__field_slot.writetobuffer(buf)
        self.__field_flag.writetobuffer(buf)
        self.__field_pad.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_header=qcpheader()
        self.__field_header.readfrombuffer(buf)
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_flag=UINT(**{'sizeinbytes': 1})
        self.__field_flag.readfrombuffer(buf)
        self.__field_pad=UNKNOWN()
        self.__field_pad.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_header(self):
        return self.__field_header.getvalue()
    def __setfield_header(self, value):
        if isinstance(value,qcpheader):
            self.__field_header=value
        else:
            self.__field_header=qcpheader(value,)
    def __delfield_header(self): del self.__field_header
    header=property(__getfield_header, __setfield_header, __delfield_header, None)
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
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
        return self.__field_pad.getvalue()
    def __setfield_pad(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad=value
        else:
            self.__field_pad=UNKNOWN(value,)
    def __delfield_pad(self): del self.__field_pad
    pad=property(__getfield_pad, __setfield_pad, __delfield_pad, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('header', self.__field_header, None)
        yield ('slot', self.__field_slot, None)
        yield ('flag', self.__field_flag, None)
        yield ('pad', self.__field_pad, None)
class callalarmentry(BaseProtogenClass):
    __fields=['slot', 'pad0', 'phonenum', 'phonenum_len', 'date', 'period', 'dom', 'datedup', 'name', 'pad1', 'name_len', 'phonenumbertype', 'phonenumberslot', 'pad2', 'serial', 'pad3', 'ringtone', 'pad4', 'flag']
    def __init__(self, *args, **kwargs):
        dict={}
        dict.update(kwargs)
        super(callalarmentry,self).__init__(**dict)
        if self.__class__ is callalarmentry:
            self._update(args,dict)
    def getfields(self):
        return self.__fields
    def _update(self, args, kwargs):
        super(callalarmentry,self)._update(args,kwargs)
        keys=kwargs.keys()
        for key in keys:
            if key in self.__fields:
                setattr(self, key, kwargs[key])
                del kwargs[key]
        if __debug__:
            self._complainaboutunusedargs(callalarmentry,kwargs)
        if len(args): raise TypeError('Unexpected arguments supplied: '+`args`)
    def writetobuffer(self,buf):
        'Writes this packet to the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot.writetobuffer(buf)
        try: self.__field_pad0
        except:
            self.__field_pad0=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad0.writetobuffer(buf)
        self.__field_phonenum.writetobuffer(buf)
        self.__field_phonenum_len.writetobuffer(buf)
        self.__field_date.writetobuffer(buf)
        self.__field_period.writetobuffer(buf)
        self.__field_dom.writetobuffer(buf)
        self.__field_datedup.writetobuffer(buf)
        self.__field_name.writetobuffer(buf)
        try: self.__field_pad1
        except:
            self.__field_pad1=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad1.writetobuffer(buf)
        self.__field_name_len.writetobuffer(buf)
        self.__field_phonenumbertype.writetobuffer(buf)
        self.__field_phonenumberslot.writetobuffer(buf)
        try: self.__field_pad2
        except:
            self.__field_pad2=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad2.writetobuffer(buf)
        try: self.__field_serial
        except:
            self.__field_serial=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_serial.writetobuffer(buf)
        try: self.__field_pad3
        except:
            self.__field_pad3=UNKNOWN(**{'sizeinbytes': 2})
        self.__field_pad3.writetobuffer(buf)
        try: self.__field_ringtone
        except:
            self.__field_ringtone=UINT(**{'sizeinbytes': 1, 'default': 0xfc})
        self.__field_ringtone.writetobuffer(buf)
        try: self.__field_pad4
        except:
            self.__field_pad4=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad4.writetobuffer(buf)
        try: self.__field_flag
        except:
            self.__field_flag=UINT(**{'sizeinbytes': 1})
        self.__field_flag.writetobuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def readfrombuffer(self,buf):
        'Reads this packet from the supplied buffer'
        self._bufferstartoffset=buf.getcurrentoffset()
        self.__field_slot=UINT(**{'sizeinbytes': 1})
        self.__field_slot.readfrombuffer(buf)
        self.__field_pad0=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad0.readfrombuffer(buf)
        self.__field_phonenum=STRING(**{'sizeinbytes': 49, 'raiseonunterminatedread': False})
        self.__field_phonenum.readfrombuffer(buf)
        self.__field_phonenum_len=UINT(**{'sizeinbytes': 1})
        self.__field_phonenum_len.readfrombuffer(buf)
        self.__field_date=UINT(**{'sizeinbytes': 4})
        self.__field_date.readfrombuffer(buf)
        self.__field_period=UINT(**{'sizeinbytes': 1})
        self.__field_period.readfrombuffer(buf)
        self.__field_dom=UINT(**{'sizeinbytes': 1})
        self.__field_dom.readfrombuffer(buf)
        self.__field_datedup=UINT(**{'sizeinbytes': 4})
        self.__field_datedup.readfrombuffer(buf)
        self.__field_name=STRING(**{'sizeinbytes': 16, 'raiseonunterminatedread': False, 'raiseontruncate': False, 'terminator': None})
        self.__field_name.readfrombuffer(buf)
        self.__field_pad1=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad1.readfrombuffer(buf)
        self.__field_name_len=UINT(**{'sizeinbytes': 1})
        self.__field_name_len.readfrombuffer(buf)
        self.__field_phonenumbertype=UINT(**{'sizeinbytes': 1})
        self.__field_phonenumbertype.readfrombuffer(buf)
        self.__field_phonenumberslot=UINT(**{'sizeinbytes': 2})
        self.__field_phonenumberslot.readfrombuffer(buf)
        self.__field_pad2=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad2.readfrombuffer(buf)
        self.__field_serial=UINT(**{'sizeinbytes': 1, 'default': 0})
        self.__field_serial.readfrombuffer(buf)
        self.__field_pad3=UNKNOWN(**{'sizeinbytes': 2})
        self.__field_pad3.readfrombuffer(buf)
        self.__field_ringtone=UINT(**{'sizeinbytes': 1, 'default': 0xfc})
        self.__field_ringtone.readfrombuffer(buf)
        self.__field_pad4=UNKNOWN(**{'sizeinbytes': 1})
        self.__field_pad4.readfrombuffer(buf)
        self.__field_flag=UINT(**{'sizeinbytes': 1})
        self.__field_flag.readfrombuffer(buf)
        self._bufferendoffset=buf.getcurrentoffset()
    def __getfield_slot(self):
        return self.__field_slot.getvalue()
    def __setfield_slot(self, value):
        if isinstance(value,UINT):
            self.__field_slot=value
        else:
            self.__field_slot=UINT(value,**{'sizeinbytes': 1})
    def __delfield_slot(self): del self.__field_slot
    slot=property(__getfield_slot, __setfield_slot, __delfield_slot, None)
    def __getfield_pad0(self):
        try: self.__field_pad0
        except:
            self.__field_pad0=UNKNOWN(**{'sizeinbytes': 1})
        return self.__field_pad0.getvalue()
    def __setfield_pad0(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad0=value
        else:
            self.__field_pad0=UNKNOWN(value,**{'sizeinbytes': 1})
    def __delfield_pad0(self): del self.__field_pad0
    pad0=property(__getfield_pad0, __setfield_pad0, __delfield_pad0, "Not the flag?")
    def __getfield_phonenum(self):
        return self.__field_phonenum.getvalue()
    def __setfield_phonenum(self, value):
        if isinstance(value,STRING):
            self.__field_phonenum=value
        else:
            self.__field_phonenum=STRING(value,**{'sizeinbytes': 49, 'raiseonunterminatedread': False})
    def __delfield_phonenum(self): del self.__field_phonenum
    phonenum=property(__getfield_phonenum, __setfield_phonenum, __delfield_phonenum, None)
    def __getfield_phonenum_len(self):
        return self.__field_phonenum_len.getvalue()
    def __setfield_phonenum_len(self, value):
        if isinstance(value,UINT):
            self.__field_phonenum_len=value
        else:
            self.__field_phonenum_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_phonenum_len(self): del self.__field_phonenum_len
    phonenum_len=property(__getfield_phonenum_len, __setfield_phonenum_len, __delfield_phonenum_len, None)
    def __getfield_date(self):
        return self.__field_date.getvalue()
    def __setfield_date(self, value):
        if isinstance(value,UINT):
            self.__field_date=value
        else:
            self.__field_date=UINT(value,**{'sizeinbytes': 4})
    def __delfield_date(self): del self.__field_date
    date=property(__getfield_date, __setfield_date, __delfield_date, "# seconds since Jan 1, 1980 approximately")
    def __getfield_period(self):
        return self.__field_period.getvalue()
    def __setfield_period(self, value):
        if isinstance(value,UINT):
            self.__field_period=value
        else:
            self.__field_period=UINT(value,**{'sizeinbytes': 1})
    def __delfield_period(self): del self.__field_period
    period=property(__getfield_period, __setfield_period, __delfield_period, "No, Daily, Weekly, Monthly, Yearly")
    def __getfield_dom(self):
        return self.__field_dom.getvalue()
    def __setfield_dom(self, value):
        if isinstance(value,UINT):
            self.__field_dom=value
        else:
            self.__field_dom=UINT(value,**{'sizeinbytes': 1})
    def __delfield_dom(self): del self.__field_dom
    dom=property(__getfield_dom, __setfield_dom, __delfield_dom, "Day of month for the event")
    def __getfield_datedup(self):
        return self.__field_datedup.getvalue()
    def __setfield_datedup(self, value):
        if isinstance(value,UINT):
            self.__field_datedup=value
        else:
            self.__field_datedup=UINT(value,**{'sizeinbytes': 4})
    def __delfield_datedup(self): del self.__field_datedup
    datedup=property(__getfield_datedup, __setfield_datedup, __delfield_datedup, "Copy of the date.  Always the same???")
    def __getfield_name(self):
        return self.__field_name.getvalue()
    def __setfield_name(self, value):
        if isinstance(value,STRING):
            self.__field_name=value
        else:
            self.__field_name=STRING(value,**{'sizeinbytes': 16, 'raiseonunterminatedread': False, 'raiseontruncate': False, 'terminator': None})
    def __delfield_name(self): del self.__field_name
    name=property(__getfield_name, __setfield_name, __delfield_name, None)
    def __getfield_pad1(self):
        try: self.__field_pad1
        except:
            self.__field_pad1=UNKNOWN(**{'sizeinbytes': 1})
        return self.__field_pad1.getvalue()
    def __setfield_pad1(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad1=value
        else:
            self.__field_pad1=UNKNOWN(value,**{'sizeinbytes': 1})
    def __delfield_pad1(self): del self.__field_pad1
    pad1=property(__getfield_pad1, __setfield_pad1, __delfield_pad1, None)
    def __getfield_name_len(self):
        return self.__field_name_len.getvalue()
    def __setfield_name_len(self, value):
        if isinstance(value,UINT):
            self.__field_name_len=value
        else:
            self.__field_name_len=UINT(value,**{'sizeinbytes': 1})
    def __delfield_name_len(self): del self.__field_name_len
    name_len=property(__getfield_name_len, __setfield_name_len, __delfield_name_len, None)
    def __getfield_phonenumbertype(self):
        return self.__field_phonenumbertype.getvalue()
    def __setfield_phonenumbertype(self, value):
        if isinstance(value,UINT):
            self.__field_phonenumbertype=value
        else:
            self.__field_phonenumbertype=UINT(value,**{'sizeinbytes': 1})
    def __delfield_phonenumbertype(self): del self.__field_phonenumbertype
    phonenumbertype=property(__getfield_phonenumbertype, __setfield_phonenumbertype, __delfield_phonenumbertype, "1: Home, 2: Work, ...")
    def __getfield_phonenumberslot(self):
        return self.__field_phonenumberslot.getvalue()
    def __setfield_phonenumberslot(self, value):
        if isinstance(value,UINT):
            self.__field_phonenumberslot=value
        else:
            self.__field_phonenumberslot=UINT(value,**{'sizeinbytes': 2})
    def __delfield_phonenumberslot(self): del self.__field_phonenumberslot
    phonenumberslot=property(__getfield_phonenumberslot, __setfield_phonenumberslot, __delfield_phonenumberslot, None)
    def __getfield_pad2(self):
        try: self.__field_pad2
        except:
            self.__field_pad2=UNKNOWN(**{'sizeinbytes': 1})
        return self.__field_pad2.getvalue()
    def __setfield_pad2(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad2=value
        else:
            self.__field_pad2=UNKNOWN(value,**{'sizeinbytes': 1})
    def __delfield_pad2(self): del self.__field_pad2
    pad2=property(__getfield_pad2, __setfield_pad2, __delfield_pad2, None)
    def __getfield_serial(self):
        try: self.__field_serial
        except:
            self.__field_serial=UINT(**{'sizeinbytes': 1, 'default': 0})
        return self.__field_serial.getvalue()
    def __setfield_serial(self, value):
        if isinstance(value,UINT):
            self.__field_serial=value
        else:
            self.__field_serial=UINT(value,**{'sizeinbytes': 1, 'default': 0})
    def __delfield_serial(self): del self.__field_serial
    serial=property(__getfield_serial, __setfield_serial, __delfield_serial, None)
    def __getfield_pad3(self):
        try: self.__field_pad3
        except:
            self.__field_pad3=UNKNOWN(**{'sizeinbytes': 2})
        return self.__field_pad3.getvalue()
    def __setfield_pad3(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad3=value
        else:
            self.__field_pad3=UNKNOWN(value,**{'sizeinbytes': 2})
    def __delfield_pad3(self): del self.__field_pad3
    pad3=property(__getfield_pad3, __setfield_pad3, __delfield_pad3, None)
    def __getfield_ringtone(self):
        try: self.__field_ringtone
        except:
            self.__field_ringtone=UINT(**{'sizeinbytes': 1, 'default': 0xfc})
        return self.__field_ringtone.getvalue()
    def __setfield_ringtone(self, value):
        if isinstance(value,UINT):
            self.__field_ringtone=value
        else:
            self.__field_ringtone=UINT(value,**{'sizeinbytes': 1, 'default': 0xfc})
    def __delfield_ringtone(self): del self.__field_ringtone
    ringtone=property(__getfield_ringtone, __setfield_ringtone, __delfield_ringtone, None)
    def __getfield_pad4(self):
        try: self.__field_pad4
        except:
            self.__field_pad4=UNKNOWN(**{'sizeinbytes': 1})
        return self.__field_pad4.getvalue()
    def __setfield_pad4(self, value):
        if isinstance(value,UNKNOWN):
            self.__field_pad4=value
        else:
            self.__field_pad4=UNKNOWN(value,**{'sizeinbytes': 1})
    def __delfield_pad4(self): del self.__field_pad4
    pad4=property(__getfield_pad4, __setfield_pad4, __delfield_pad4, " This may be the ringtone.  Need to understand ")
    def __getfield_flag(self):
        try: self.__field_flag
        except:
            self.__field_flag=UINT(**{'sizeinbytes': 1})
        return self.__field_flag.getvalue()
    def __setfield_flag(self, value):
        if isinstance(value,UINT):
            self.__field_flag=value
        else:
            self.__field_flag=UINT(value,**{'sizeinbytes': 1})
    def __delfield_flag(self): del self.__field_flag
    flag=property(__getfield_flag, __setfield_flag, __delfield_flag, None)
    def iscontainer(self):
        return True
    def containerelements(self):
        yield ('slot', self.__field_slot, None)
        yield ('pad0', self.__field_pad0, "Not the flag?")
        yield ('phonenum', self.__field_phonenum, None)
        yield ('phonenum_len', self.__field_phonenum_len, None)
        yield ('date', self.__field_date, "# seconds since Jan 1, 1980 approximately")
        yield ('period', self.__field_period, "No, Daily, Weekly, Monthly, Yearly")
        yield ('dom', self.__field_dom, "Day of month for the event")
        yield ('datedup', self.__field_datedup, "Copy of the date.  Always the same???")
        yield ('name', self.__field_name, None)
        yield ('pad1', self.__field_pad1, None)
        yield ('name_len', self.__field_name_len, None)
        yield ('phonenumbertype', self.__field_phonenumbertype, "1: Home, 2: Work, ...")
        yield ('phonenumberslot', self.__field_phonenumberslot, None)
        yield ('pad2', self.__field_pad2, None)
        yield ('serial', self.__field_serial, None)
        yield ('pad3', self.__field_pad3, None)
        yield ('ringtone', self.__field_ringtone, None)
        yield ('pad4', self.__field_pad4, " This may be the ringtone.  Need to understand ")
        yield ('flag', self.__field_flag, None)
