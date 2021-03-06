"""Communicate with the LG 6190 (bell mobility) cell phone"""
import re
import time
import cStringIO
import sha
import p_lglg6190
import p_brew
import common
import commport
import com_brew
import com_phone
import com_lg
import com_lgvx4400
import prototypes
import call_history
import sms
import fileinfo
import memo
class Phone(com_lgvx4400.Phone):
    "Talk to the LG 6190 cell phone"
    desc="LG 6190"
    protocolclass=p_lglg6190
    serialsname='lg6190'
    wallpaperindexfilename="download/dloadindex/brewImageIndex.map"
    ringerindexfilename="download/dloadindex/brewRingerIndex.map"
    imagelocations=(
        ( 10, "download/dloadindex/brewImageIndex.map", "usr/Wallpaper", "images", 30),
        ( 50, "cam/pics.dat",                           "cam",           "camera", 60),
        )
    ringtonelocations=(
        ( 50, "download/dloadindex/brewRingerIndex.map", "usr/Ringtone", "ringers", 30),
        )
    builtinimages=()
    builtinringtones=( 'Ring 1', 'Ring 2', 'Ring 3', 'Ring 4', 'Ring 5',
                       'Default tone', 'Fearwell', 'Arabesque', 'Piano Sonata', 'Latin',
                       'When the Saints', 'Bach Cello Suite', 'Speedy Way', 'Cancan', 'String', 
                       'Toccata and Fuge', 'Mozart Symphony 40', 'Nutcracker March', 'Funiculi',
                       'Ploka', 'Hallelujah', 'Mozart Aria', 'Leichte', 'Spring', 'Slavonic', 'Fantasy')
    def __init__(self, logtarget, commport):
        "Calls all the constructors and sets initial modes"
        com_phone.Phone.__init__(self, logtarget, commport)
        com_brew.BrewProtocol.__init__(self)
        com_lg.LGPhonebook.__init__(self)
        self.log("Attempting to contact phone")
        self._cal_has_voice_id=hasattr(self.protocolclass, 'cal_has_voice_id') \
                                and self.protocolclass.cal_has_voice_id
        self.mode=self.MODENONE
    def getmediaindex(self, builtins, maps, results, key):
        """Gets the media (wallpaper/ringtone) index
        @param builtins: the builtin list on the phone
        @param results: places results in this dict
        @param maps: the list of index files and locations
        @param key: key to place results in
        """
        self.log("Reading "+key)
        media={}
        c=1
        for name in builtins:
            media[c]={'name': name, 'origin': 'builtin' }
            c+=1
        type=None
        for offset,indexfile,location,type,maxentries in maps:
            if type=='camera':
                index=self.getcamindex(indexfile, location)
            else:
                index=self.getindex(indexfile, location)
            for i in index:
                media[i+offset]={'name': index[i], 'origin': type}
        results[key]=media
        return media
    def getcamindex(self, indexfile, location, getmedia=False):
        "Read an index file"
        index={}
        try:
            buf=prototypes.buffer(self.getfilecontents(indexfile))
        except com_brew.BrewNoSuchFileException:
            return index
        g=self.protocolclass.camindexfile()
        g.readfrombuffer(buf, logtitle="Camera index file")
        self.log("Cam index file read")
        for i in g.items:
            if len(i.name):
                filename="pic%02d.jpg" % i.index
                if not getmedia:
                    index[i.index]=filename
                else:
                    try:
                        contents=self.getfilecontents(location+"/"+filename+"/body")
                    except (com_brew.BrewNoSuchFileException,com_brew.BrewNoSuchDirectoryException,com_brew.BrewNameTooLongException):
                        self.log("Can't find the actual content in "+location+"/"+filename+"/body")
                        continue
                    index[filename]=contents
        return index
    def getindex(self, indexfile, location, getmedia=False):
        "Read an index file"
        index={}
        try:
            buf=prototypes.buffer(self.getfilecontents(indexfile))
        except com_brew.BrewNoSuchFileException:
            return index
        g=self.protocolclass.indexfile()
        g.readfrombuffer(buf, logtitle="Index file %s read" % (indexfile,))
        for i in g.items:
            if i.index!=0xffff and len(i.name):
                try:
                    buf=prototypes.buffer(self.getfilecontents(location+"/"+i.name+"/.desc"))
                except com_brew.BrewNoSuchFileException:
                    self.log("No .desc file in "+location+"/"+i.name+" - ignoring directory")
                    continue
                desc=self.protocolclass.mediadesc()
                desc.readfrombuffer(buf, logtitle=".desc file %s/.desc read" % (location+"/"+i.name,))
                filename=self._createnamewithmimetype(i.name, desc.mimetype)
                if not getmedia:
                    index[i.index]=filename
                else:
                    try:
                        contents=self.getfilecontents(location+"/"+i.name+"/"+desc.filename)
                    except (com_brew.BrewNoSuchFileException,com_brew.BrewNoSuchDirectoryException):
                        try:
                            contents=self.getfilecontents(location+"/"+i.name+"/body")
                        except (com_brew.BrewNoSuchFileException,com_brew.BrewNoSuchDirectoryException,com_brew.BrewNameTooLongException):
                            self.log("Can't find the actual content in "+location+"/"+i.name+"/body")
                            continue
                    index[filename]=contents
        return index
    def _createnamewithmimetype(self, name, mt):
        name=common.basename(name)
        if mt=="image/jpeg":
            mt="image/jpg"
        try:
            return name+self.__mimetoextensionmapping[mt]
        except KeyError:
            self.log("Unable to figure out extension for mime type "+mt)
            return name
    __mimetoextensionmapping={
        'image/jpg': '.jpg',
        'image/bmp': '.bmp',
        'image/png': '.png',
        'image/gif': '.gif',
        'image/bci': '.bci',
        'audio/mpeg': '.mp3',
        'audio/midi': '.mid',
        'audio/qcp': '.qcp'
        }
    def _getmimetype(self, name):
        ext=common.getext(name.lower())
        if len(ext): ext="."+ext
        if ext==".jpeg":
            return "image/jpg" # special case
        for mt,extension in self.__mimetoextensionmapping.items():
            if ext==extension:
                return mt
        self.log("Unable to figure out a mime type for "+name)
        assert False, "No idea what type "+ext+" is"
        return "x-unknown/x-unknown"
    def getmedia(self, maps, result, key):
        """Returns the contents of media as a dict where the key is a name as returned
        by getindex, and the value is the contents of the media"""
        media={}
        for offset,indexfile,location,origin,maxentries in maps:
            if origin=='camera':
                media.update(self.getcamindex(indexfile, location, getmedia=True))
            else:
                media.update(self.getindex(indexfile, location, getmedia=True))
        result[key]=media
        return result
    def savemedia(self, mediakey, mediaindexkey, maps, results, merge, reindexfunction):
        """Actually saves out the media
        @param mediakey: key of the media (eg 'wallpapers' or 'ringtones')
        @param mediaindexkey:  index key (eg 'wallpaper-index')
        @param maps: list index files and locations
        @param results: results dict
        @param merge: are we merging or overwriting what is there?
        @param reindexfunction: the media is re-indexed at the end.  this function is called to do it
        """
        print results.keys()
        wp=results[mediakey].copy()
        wpi=results[mediaindexkey].copy()
        for k in wpi.keys():
            if wpi[k]['origin']=='builtin':
                del wpi[k]
        init={}
        for offset,indexfile,location,type,maxentries in maps:
            if type=='camera':
                continue
            init[type]={}
            for k in wpi.keys():
                if wpi[k]['origin']==type:
                    index=k-offset
                    name=wpi[k]['name']
                    data=None
                    del wpi[k]
                    for w in wp.keys():
                        if wp[w]['name']==name:
                            data=wp[w]['data']
                            del wp[w]
                    if not merge and data is None:
                        continue
                    init[type][index]={'name': name, 'data': data}
        print init.keys()
        for w in wp.keys():
            o=wp[w].get("origin", "")
            if o is not None and len(o) and o in init:
                idx=-1
                while idx in init[o]:
                    idx-=1
                init[o][idx]=wp[w]
                del wp[w]
        for offset,indexfile,location,type,maxentries in maps:
            if type=='camera':
                continue
            index=init[type]
            try:
                dirlisting=self.getfilesystem(location)
            except com_brew.BrewNoSuchDirectoryException:
                self.mkdirs(location)
                dirlisting={}
            for i in dirlisting.keys():
                if len(i)>len(location) and i[len(location)]==location:
                    dirlisting[i[len(location)+1:]]=dirlisting[i]
                    del dirlisting[i]
            dellist=[]
            if not merge:
                wpi=results[mediaindexkey]
                for i in wpi:
                    entry=wpi[i]
                    if entry['origin']==type:
                        delit=True
                        for idx in index:
                            if index[idx]['name']==entry['name']:
                                delit=False
                                break
                        if delit:
                            if common.stripext(entry['name']) in dirlisting:
                                dellist.append(common.stripext(entry['name']))
                            else:
                                self.log("%s in %s index but not filesystem" % (entry['name'], type))
            print "deleting",dellist
            for f in dellist:
                self.rmdirs(location+"/"+f)
            while len(index)<maxentries and len(wp):
                idx=-1
                while idx in index:
                    idx-=1
                k=wp.keys()[0]
                index[idx]=wp[k]
                del wp[k]
            index=self._normaliseindices(index)  # hey look, I called a function!
            if len(index)>maxentries:
                keys=index.keys()
                keys.sort()
                for k in keys[maxentries:]:
                    idx=-1
                    while idx in wp:
                        idx-=1
                    wp[idx]=index[k]
                    del index[k]
            keys=index.keys()
            keys.sort()
            ifile=self.protocolclass.indexfile()
            ifile.numactiveitems=len(keys)
            for k in keys:
                entry=self.protocolclass.indexentry()
                entry.index=k
                entry.name=common.stripext(index[k]['name'])
                ifile.items.append(entry)
            while len(ifile.items)<maxentries:
                ifile.items.append(self.protocolclass.indexentry())
            buffer=prototypes.buffer()
            ifile.writetobuffer(buffer, logtitle="Updated index file "+indexfile)
            self.writefile(indexfile, buffer.getvalue())
            for k in keys:
                entry=index[k]
                data=entry.get("data", None)
                dirname=common.stripext(entry['name'])
                if data is None:
                    if dirname not in dirlisting:
                        self.log("Index error.  I have no data for "+dirname+" and it isn't already in the filesystem")
                    continue
                if dirname in dirlisting:
                    try:
                        stat=self.statfile(location+"/"+dirname+"/body")
                        if stat['size']==len(data):
                            stat=self.statfile(location+"/"+dirname+"/.desc")
                            if stat['size']==152:
                                self.log("Skipping writing %s/%s/body as there is already a file of the same length" % (location,dirname))
                                continue
                    except com_brew.BrewNoSuchFileException:
                        pass
                elif dirname not in dirlisting:
                    try:
                        self.mkdir(location+"/"+dirname)
                    except com_brew.BrewDirectoryExistsException:
                        pass
                self.writefile(location+"/"+dirname+"/body", data)
                mimetype=self._getmimetype(entry['name'])
                desc=self.protocolclass.mediadesc()
                desc.mimetype=mimetype
                desc.totalsize=0
                desc.totalsize=desc.packetsize()+len(data)
                buf=prototypes.buffer()
                descfile="%s/%s/.desc" % (location, dirname)
                desc.writetobuffer(buf, logtitle="Desc file at "+descfile)
                self.writefile(descfile, buf.getvalue())
        if len(wp):
            for k in wp:
                self.log("Unable to put %s on the phone as there weren't any spare index entries" % (wp[k]['name'],))
        del results[mediakey] # done with it
        reindexfunction(results)
        return results
    def _getinboxmessage(self, sf):
        entry=sms.SMSEntry()
        entry.folder=entry.Folder_Inbox
        entry.datetime="%d%02d%02dT%02d%02d%02d" % (sf.GPStime)
        entry._from=self._getsender(sf.sender, sf.sender_length)
        entry.subject=sf.subject
        entry.read=sf.read
        entry.text=sf.msg
        entry.callback=sf.callback
        return entry
    def _getoutboxmessage(self, sf):
        entry=sms.SMSEntry()
        entry.folder=entry.Folder_Sent
        entry.datetime="%d%02d%02dT%02d%02d00" % ((sf.timesent))
        for r in sf.recipients:
            if r.number:
                confirmed=(r.status==2)
                confirmed_date=None
                if confirmed:
                    confirmed_date="%d%02d%02dT%02d%02d00" % r.time
                entry.add_recipient(r.number, confirmed, confirmed_date)
        entry.subject=sf.msg[:28]
        entry.text=sf.msg
        entry.callback=sf.callback
        return entry
    brew_version_file='OWS/paramtable1.fil'
    brew_version_txt_key='LG6190_version_data'
    my_model='lg6190' 
    def eval_detect_data(self, res):
        found=False
        if res.get(self.brew_version_txt_key, None) is not None:
            found=res[self.brew_version_txt_key][0x1ae:0x1ae+len(self.my_model)]==self.my_model
        if found:
            res['model']=self.my_model
            res['manufacturer']='LG Electronics Inc'
            s=res.get(self.esn_file_key, None)
            if s:
                res['esn']=self.get_esn(s)
    def getphoneinfo(self, phone_info):
        self.log('Getting Phone Info')
        try:
            s=self.getfilecontents(self.brew_version_file)
            if s[0x1ae:0x1ae+len(self.my_model)]==self.my_model:
                phone_info.append('Model:', self.my_model)
                phone_info.append('ESN:', self.get_brew_esn())
                req=p_brew.firmwarerequest()
                txt=self.getfilecontents("nvm/nvm/nvm_0000")[0x241:0x24b]
                phone_info.append('Phone Number:', txt)
        except:
            pass
        return
parentprofile=com_lgvx4400.Profile
class Profile(parentprofile):
    protocolclass=Phone.protocolclass
    serialsname=Phone.serialsname
    BP_Calendar_Version=3
    phone_manufacturer='LG Electronics Inc'
    phone_model='lg6190'      # from Bell Mobility
    brew_required=True
    RINGTONE_LIMITS= {
        'MAXSIZE': 250000
    }
    WALLPAPER_WIDTH=160
    WALLPAPER_HEIGHT=120
    MAX_WALLPAPER_BASENAME_LENGTH=30
    WALLPAPER_FILENAME_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789."
    WALLPAPER_CONVERT_FORMAT="jpg"
    MAX_RINGTONE_BASENAME_LENGTH=30
    RINGTONE_FILENAME_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789."
    DIALSTRING_CHARS="[^0-9PT#*]"
    imagetargets={}
    imagetargets.update(common.getkv(parentprofile.stockimagetargets, "wallpaper",
                                      {'width': 128, 'height': 160, 'format': "JPEG"}))
    _supportedsyncs=(
        ('phonebook', 'read', None),  # all phonebook reading
        ('calendar', 'read', None),   # all calendar reading
        ('wallpaper', 'read', None),  # all wallpaper reading
        ('ringtone', 'read', None),   # all ringtone reading
        ('call_history', 'read', None),# all call history list reading
        ('memo', 'read', None),        # all memo list reading
        ('sms', 'read', None),         # all SMS list reading
        ('phonebook', 'write', 'OVERWRITE'),  # only overwriting phonebook
        ('calendar', 'write', 'OVERWRITE'),   # only overwriting calendar
        ('wallpaper', 'write', 'MERGE'),      # merge and overwrite wallpaper
        ('wallpaper', 'write', 'OVERWRITE'),
        ('ringtone', 'write', 'MERGE'),      # merge and overwrite ringtone
        ('ringtone', 'write', 'OVERWRITE'),
        ('memo', 'write', 'OVERWRITE'),       # all memo list writing
        ('sms', 'write', 'OVERWRITE'),        # all SMS list writing
        )
    field_color_data={
        'phonebook': {
            'name': {
                'first': 0, 'middle': 0, 'last': 0, 'full': 1,
                'nickname': 0, 'details': 1 },
            'number': {
                'type': 5, 'speeddial': 5, 'number': 5, 'details': 5 },
            'email': 3,
            'address': {
                'type': 0, 'company': 0, 'street': 0, 'street2': 0,
                'city': 0, 'state': 0, 'postalcode': 0, 'country': 0,
                'details': 0 },
            'url': 1,
            'memo': 1,
            'category': 1,
            'wallpaper': 0,
            'ringtone': 1,
            'storage': 0,
            },
        'calendar': {
            'description': True, 'location': False, 'allday': True,
            'start': True, 'end': True, 'priority': False,
            'alarm': True, 'vibrate': False,
            'repeat': True,
            'memo': False,
            'category': False,
            'wallpaper': False,
            'ringtone': True,
            },
        'memo': {
            'subject': True,
            'date': False,
            'secret': False,
            'category': False,
            'memo': True,
            },
        'todo': {
            'summary': False,
            'status': False,
            'due_date': False,
            'percent_complete': False,
            'completion_date': False,
            'private': False,
            'priority': False,
            'category': False,
            'memo': False,
            },
        }
