"Deals with vcard calendar import stuff"
import copy
import datetime
import wx
import bpcalendar
import bptime
import common_calendar
import helpids
import vcard
module_debug=False
class vCalendarFile(object):
    def __init__(self, file_name=None):
        self._data=[]
        self._file_name=file_name
    def read(self, file_name=None):
        self._data=[]
        if file_name is not None:
            self._file_name=file_name
        if self._file_name is None:
            return
        try:
            f=open(self._file_name)
            vfile=vcard.VFile(f)
            has_data=False
            for n,l in vfile:
                if n[0]=='BEGIN' and l=='VEVENT':
                    d={}
                    has_data=True
                elif n[0]=='END' and l=='VEVENT':
                    self._data.append(d)
                    d={}
                    has_data=False
                elif has_data:
                    d[n[0]]={ 'value': l }
            f.close()
        except:
            pass
    def _get_data(self):
        return copy.deepcopy(self._data)
    data=property(fget=_get_data)
class VCalendarImportData(object):
    _default_filter={
        'start': None,
        'end': None,
        'categories': None,
        'rpt_events': False,
        'no_alarm': False,
        'ringtone': None,
        'alarm_override':False,
        'vibrate':False,
        'alarm_value':0
        }
    _rrule_dow={
        'SU': 0x01, 'MO': 0x02, 'TU': 0x04, 'WE': 0x08, 'TH': 0x10,
        'FR': 0x20, 'SA': 0x40 }
    _rrule_weekday=_rrule_dow['MO']|_rrule_dow['TU']|\
                  _rrule_dow['WE']|_rrule_dow['TH']|\
                  _rrule_dow['FR']
    def __init__(self, file_name=None):
        self._file_name=file_name
        self._data=[]
        self._filter=self._default_filter
        self.read()
    def _accept(self, entry):
        if self._filter['start'] is not None and \
           entry['start'][:3]<self._filter['start'][:3]:
            return False
        if self._filter['end'] is not None and \
           entry['end'][:3]>self._filter['end'][:3] and \
           entry['end'][:3]!=common_calendar.no_end_date[:3]:
            return False
        c=self._filter['categories']
        if c is None or not len(c):
            return True
        if len([x for x in entry['categories'] if x in c]):
            return True
        return False
    def _populate_repeat_entry(self, e, ce):
        if not e.get('repeat', False) or e.get('repeat_type', None) is None:
            return
        rp=bpcalendar.RepeatEntry()
        rp_type=e['repeat_type']
        rp_interval=e.get('repeat_interval', 1)
        rp_interval2=e.get('repeat_interval2', 1)
        rp_end=e.get('repeat_end', None)
        rp_num=e.get('repeat_num', None)
        rp_dow=e.get('repeat_dow', 0)
        if rp_type==rp.daily:
            rp.repeat_type=rp.daily
            rp.interval=rp_interval
        elif rp_type==rp.weekly or rp_type==rp.monthly:
            rp.repeat_type=rp_type
            rp.interval=rp_interval
            rp.interval2=rp_interval2
            rp.dow=rp_dow
        elif rp_type==rp.yearly:
            rp.repeat_type=rp.yearly
        else:
            return
        if rp_end is not None:
            ce.end=rp_end[:3]+ce.end[3:]
        elif rp_num is not None and rp_num:
            if rp_type=='daily':
                bp_t=bptime.BPTime(ce.start)+ \
                      datetime.timedelta(rp_interval*(rp_num-1))
                ce.end=bp_t.get()[:3]+ce.end[3:]
            elif rp_type=='weekly':
                bp_t=bptime.BPTime(ce.start)+ \
                      datetime.timedelta(7*rp_interval*(rp_num-1))
                ce.end=bp_t.get()[:3]+ce.end[3:]
            elif rp_type=='monthly':
                bp_t=bptime.BPTime(ce.start)+ \
                      datetime.timedelta(30*(rp_num-1))
                ce.end=bp_t.get()[:2]+ce.end[2:]
            else:                    
                bp_t=bptime.BPTime(ce.start)+ \
                      datetime.timedelta(365*(rp_num-1))
                ce.end=bp_t.get()[:1]+ce.end[1:]
        else:
            ce.end=common_calendar.no_end_date[:3]+ce.end[3:]
        for k in e.get('exceptions', []):
            rp.add_suppressed(*k[:3])
        ce.repeat=rp
    def _populate_entry(self, e, ce):
        ce.description=e.get('description', None)
        ce.location=e.get('location', None)
        v=e.get('priority', None)
        if v is not None:
            ce.priority=v
        if not self._filter.get('no_alarm', False) and \
               not self._filter.get('alarm_override', False) and \
               e.get('alarm', False):
            ce.alarm=e.get('alarm_value', 0)
            ce.ringtone=self._filter.get('ringtone', "")
            ce.vibrate=self._filter.get('vibrate', False)
        elif not self._filter.get('no_alarm', False) and \
               self._filter.get('alarm_override', False):
            ce.alarm=self._filter.get('alarm_value', 0)
            ce.ringtone=self._filter.get('ringtone', "")
            ce.vibrate=self._filter.get('vibrate', False)
        ce_start=e.get('start', None)
        ce_end=e.get('end', None)
        if ce_start is None and ce_end is None:
            raise ValueError, "No start or end datetime"
        if ce_start is not None:
            ce.start=ce_start
        if ce_end is not None:
            ce.end=ce_end
        if ce_start is None:
            ce.start=ce.end
        elif ce_end is None:
            ce.end=ce.start
        ce.notes=e.get('notes', None)
        v=[]
        for k in e.get('categories', []):
            v.append({ 'category': k })
        ce.categories=v
        self._populate_repeat_entry(e, ce)
    def _generate_repeat_events(self, e):
        ce=bpcalendar.CalendarEntry()
        self._populate_entry(e, ce)
        l=[]
        new_e=e.copy()
        new_e['repeat']=False
        for k in ('repeat_type', 'repeat_interval', 'repeat_dow'):
            if new_e.has_key(k):
                del new_e[k]
        s_date=datetime.datetime(*self._filter['start'])
        e_date=datetime.datetime(*self._filter['end'])
        one_day=datetime.timedelta(1)
        this_date=s_date
        while this_date<=e_date:
            date_l=(this_date.year, this_date.month, this_date.day)
            if ce.is_active(*date_l):
                new_e['start']=date_l+new_e['start'][3:]
                new_e['end']=date_l+new_e['end'][3:]
                l.append(new_e.copy())
            this_date+=one_day
        return l
    def get(self):
        res={}
        single_rpt=self._filter.get('rpt_events', False)
        for k in self._data:
            try:
                if self._accept(k):
                    if k.get('repeat', False) and single_rpt:
                        d=self._generate_repeat_events(k)
                    else:
                        d=[k]
                    for n in d:
                        ce=bpcalendar.CalendarEntry()
                        self._populate_entry(n, ce)
                        res[ce.id]=ce
            except:
                if module_debug:
                    raise
        return res
    def get_category_list(self):
        l=[]
        for e in self._data:
            l+=[x for x in e.get('categories', []) if x not in l]
        return l
    def set_filter(self, filter):
        self._filter=filter
    def get_filter(self):
        return self._filter
    def _conv_cat(self, v, _):
        return [x.strip() for x in v['value'].split(",") if len(x)]
    def _conv_alarm(self, v, dd):
        try:
            alarm_date=bptime.BPTime(v['value'].split(';')[0])
            start_date=bptime.BPTime(dd['start'])
            if alarm_date.get()<start_date.get():
                dd['alarm_value']=(start_date-alarm_date).seconds/60
                return True
            return False
        except:
            return False
    def _conv_date(self, v, _):
        return bptime.BPTime(v['value']).get()
    def _conv_priority(self, v, _):
        try:
            return int(v['value'])
        except:
            return None
    def _process_daily_rule(self, v, dd):
        s=v['value'].split(' ')
        dd['repeat_interval']=int(s[0][1:])
        if len(s)==1:
            return True
        if s[1][0]=='#':
            dd['repeat_num']=int(s[1][1:])
        else:
            dd['repeat_end']=bptime.BPTime(s[1]).get()
        dd['repeat_type']='daily'
        return True
    def _process_weekly_rule(self, v, dd):
        s=v['value'].split(' ')
        dd['repeat_interval']=int(s[0][1:])
        dow=0
        for i in range(1, len(s)):
            n=s[i]
            if n[0].isdigit():
                dd['repeat_end']=bptime.BPTime(n).get()
            elif n[0]=='#':
                dd['repeat_num']=int(n[1:])
            else:
                dow=dow|self._rrule_dow.get(n, 0)
        if dow:
            dd['repeat_dow']=dow
        dd['repeat_type']='weekly'
        return True
    def _process_monthly_rule(self, v, dd):
        try:
            s=v['value'].split(' ')
            if s[0][:2]!='MD' and s[0][:2]!='MP':
                return False
            dd['repeat_interval2']=int(s[0][2:])
            if s[0][:2]=='MP':
                n=s[1]
                if n in ['1+', '2+', '3+', '4+', '1-']:
                    if n[1]=='-':
                        dd['repeat_interval']=5
                    else:
                        dd['repeat_interval']=int(n[0])
                else:
                    return False
                dd['repeat_dow']=self._rrule_dow.get(s[2], 0)
            else:
                dd['repeat_interval']=dd['repeat_dow']=0
            dd['repeat_type']='monthly'
            n=s[-1]
            if len(n)>7 and n[:8].isdigit():
                dd['repeat_end']=bptime.BPTime(n).get()
            elif n[0]=='#':
                dd['repeat_num']=int(n[1:])
            return True
        except:
            if module_debug: raise
            return False
    def _process_yearly_rule(self, v, dd):
        try:
            s=v['value'].split(' ')
            if s[0]!='YM1':
                return False
            n=s[-1]
            if len(n)>7 and n[:8].isdigit():
                dd['repeat_end']=bptime.BPTime(n).get()
            elif n[0]=='#':
                dd['repeat_num']=int(n[1:])
            dd['repeat_type']='yearly'
            return True
        except:
            if module_debug: raise
            return False
    def _conv_repeat(self, v, dd):
        func_dict={
            'D': self._process_daily_rule,
            'W': self._process_weekly_rule,
            'M': self._process_monthly_rule,
            'Y': self._process_yearly_rule
            }
        c=v['value'][0]
        return func_dict.get(c, lambda *arg: False)(v, dd)
    def _conv_exceptions(self, v, _):
        try:
            l=v['value'].split(';')
            r=[]
            for n in l:
                r.append(bptime.BPTime(n).get())
            return r
        except:
            return []
    _calendar_keys=[
        ('CATEGORIES', 'categories', _conv_cat),
        ('DESCRIPTION', 'notes', None),
        ('DTEND', 'end', _conv_date),
        ('LOCATION', 'location', None),
        ('PRIORITY', 'priority', _conv_priority),
        ('DTSTART', 'start', _conv_date),
        ('SUMMARY', 'description', None),
        ('AALARM', 'alarm', _conv_alarm),
        ('DALARM', 'alarm', _conv_alarm),
        ('RRULE', 'repeat', _conv_repeat),
        ('EXDATE', 'exceptions', _conv_exceptions),
        ]
    def _convert(self, vcal, d):
        for i in vcal:
            try:
                dd={'start': None, 'end': None }
                for j in self._calendar_keys:
                    if i.has_key(j[0]):
                        k=i[j[0]]
                        if j[2] is not None:
                            dd[j[1]]=j[2](self, k, dd)
                        else:
                            dd[j[1]]=k['value']
                if dd['start'] is None and dd['end'] is None:
                    continue
                if dd['start'] is None:
                    dd['start']=dd['end']
                elif dd['end'] is None:
                    dd['end']=dd['start']
                if module_debug: print dd
                d.append(dd)
            except:
                if module_debug: raise
    def get_display_data(self):
        cnt=0
        res={}
        single_rpt=self._filter.get('rpt_events', False)
        for k in self._data:
            if self._accept(k):
                if k.get('repeat', False) and single_rpt:
                    d=self._generate_repeat_events(k)
                else:
                    d=[k.copy()]
                for n in d:
                    if self._filter.get('no_alarm', False):
                        n['alarm']=False
                    res[cnt]=n
                    cnt+=1
        return res
    def get_file_name(self):
        if self._file_name is not None:
            return self._file_name
        return ''
    def read(self, file_name=None):
        if file_name is not None:
            self._file_name=file_name
        if self._file_name is None:
            return
        v=vCalendarFile(self._file_name)
        v.read()
        self._convert(v.data, self._data)
class VcalImportCalDialog(common_calendar.PreviewDialog):
    _column_labels=[
        ('description', 'Description', 400, None),
        ('start', 'Start', 150, common_calendar.bp_date_str),
        ('end', 'End', 150, common_calendar.bp_date_str),
        ('repeat_type', 'Repeat', 80, common_calendar.bp_repeat_str),
        ('alarm', 'Alarm', 80, common_calendar.bp_alarm_str),
        ('categories', 'Category', 150, common_calendar.category_str)
        ]
    ID_ADD=wx.NewId()
    def __init__(self, parent, id, title):
        self._oc=VCalendarImportData()
        common_calendar.PreviewDialog.__init__(self, parent, id, title,
                               self._column_labels,
                               self._oc.get_display_data(),
                               config_name='import/calendar/vcaldialog')
    def getcontrols(self, main_bs):
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        hbs.Add(wx.StaticText(self, -1, "VCalendar File:"), 0, wx.ALL|wx.ALIGN_CENTRE, 2)
        self.folderctrl=wx.TextCtrl(self, -1, "", style=wx.TE_READONLY)
        self.folderctrl.SetValue(self._oc.get_file_name())
        hbs.Add(self.folderctrl, 1, wx.EXPAND|wx.ALL, 2)
        id_browse=wx.NewId()
        hbs.Add(wx.Button(self, id_browse, 'Browse ...'), 0, wx.EXPAND|wx.ALL, 2)
        main_bs.Add(hbs, 0, wx.EXPAND|wx.ALL, 5)
        main_bs.Add(wx.StaticLine(self, -1), 0, wx.EXPAND|wx.TOP|wx.BOTTOM, 5)
        wx.EVT_BUTTON(self, id_browse, self.OnBrowseFolder)
    def getpostcontrols(self, main_bs):
        main_bs.Add(wx.StaticLine(self, -1), 0, wx.EXPAND|wx.TOP|wx.BOTTOM, 5)
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        id_import=wx.NewId()
        hbs.Add(wx.Button(self, id_import, 'Import'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        hbs.Add(wx.Button(self, wx.ID_OK, 'Replace All'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        hbs.Add(wx.Button(self, self.ID_ADD, 'Add'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        hbs.Add(wx.Button(self, wx.ID_CANCEL, 'Cancel'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        id_filter=wx.NewId()
        hbs.Add(wx.Button(self, id_filter, 'Filter'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)       
        hbs.Add(wx.Button(self, wx.ID_HELP, 'Help'), 0,  wx.ALIGN_CENTRE|wx.ALL, 5)
        main_bs.Add(hbs, 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        wx.EVT_BUTTON(self, id_import, self.OnImport)
        wx.EVT_BUTTON(self, id_filter, self.OnFilter)
        wx.EVT_BUTTON(self, self.ID_ADD, self.OnAdd)
        wx.EVT_BUTTON(self, wx.ID_HELP, lambda *_: wx.GetApp().displayhelpid(helpids.ID_DLG_CALENDAR_IMPORT))
    def OnImport(self, evt):
        wx.BeginBusyCursor()
        dlg=wx.ProgressDialog('VCalendar Import',
                              'Importing vCalendar Data, please wait ...',
                              parent=self)
        self._oc.read(self.folderctrl.GetValue())
        self.populate(self._oc.get_display_data())
        dlg.Destroy()
        wx.EndBusyCursor()
    def OnBrowseFolder(self, evt):
        dlg=wx.FileDialog(self, "Pick a VCalendar File",
                          wildcard='*.vcs;*.ics')
        id=dlg.ShowModal()
        if id==wx.ID_CANCEL:
            dlg.Destroy()
            return
        self.folderctrl.SetValue(dlg.GetPath())
        dlg.Destroy()
    def OnFilter(self, evt):
        cat_list=self._oc.get_category_list()
        dlg=common_calendar.FilterDialog(self, -1, 'Filtering Parameters', cat_list)
        if dlg.ShowModal()==wx.ID_OK:
            self._oc.set_filter(dlg.get())
            self.populate(self._oc.get_display_data())
    def OnAdd(self, evt):
        self.EndModal(self.ID_ADD)
    def get(self):
        return self._oc.get()
    def get_categories(self):
        return self._oc.get_category_list()
def ImportCal(folder, filters):
    _oc=VCalendarImportData(folder)
    _oc.set_filter(filters)
    _oc.read()
    res={ 'calendar':_oc.get() }
    return res
class VCalAutoConfCalDialog(wx.Dialog):
    def __init__(self, parent, id, title, folder, filters,
                 style=wx.CAPTION|wx.MAXIMIZE_BOX| \
                 wx.SYSTEM_MENU|wx.DEFAULT_DIALOG_STYLE|wx.RESIZE_BORDER):
        self._oc=VCalendarImportData()
        self._oc.set_filter(filters)
        self._read=False
        wx.Dialog.__init__(self, parent, id=id, title=title, style=style)
        main_bs=wx.BoxSizer(wx.VERTICAL)
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        hbs.Add(wx.StaticText(self, -1, "VCalendar File:"), 0, wx.ALL|wx.ALIGN_CENTRE, 2)
        self.folderctrl=wx.TextCtrl(self, -1, "", style=wx.TE_READONLY)
        self.folderctrl.SetValue(folder)
        hbs.Add(self.folderctrl, 1, wx.EXPAND|wx.ALL, 2)
        id_browse=wx.NewId()
        hbs.Add(wx.Button(self, id_browse, 'Browse ...'), 0, wx.EXPAND|wx.ALL, 2)
        main_bs.Add(hbs, 0, wx.EXPAND|wx.ALL, 5)
        main_bs.Add(wx.StaticLine(self, -1), 0, wx.EXPAND|wx.TOP|wx.BOTTOM, 5)
        wx.EVT_BUTTON(self, id_browse, self.OnBrowseFolder)
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        hbs.Add(wx.Button(self, wx.ID_OK, 'OK'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        hbs.Add(wx.Button(self, wx.ID_CANCEL, 'Cancel'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        id_filter=wx.NewId()
        hbs.Add(wx.Button(self, id_filter, 'Filter'), 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        hbs.Add(wx.Button(self, wx.ID_HELP, 'Help'), 0,  wx.ALIGN_CENTRE|wx.ALL, 5)
        main_bs.Add(hbs, 0, wx.ALIGN_CENTRE|wx.ALL, 5)
        wx.EVT_BUTTON(self, id_filter, self.OnFilter)
        wx.EVT_BUTTON(self, wx.ID_HELP, lambda *_: wx.GetApp().displayhelpid(helpids.ID_DLG_CALENDAR_IMPORT))
        self.SetSizer(main_bs)
        self.SetAutoLayout(True)
        main_bs.Fit(self)
    def OnBrowseFolder(self, evt):
        dlg=wx.FileDialog(self, "Pick a VCalendar File", wildcard='*.vcs')
        id=dlg.ShowModal()
        if id==wx.ID_CANCEL:
            dlg.Destroy()
            return
        self.folderctrl.SetValue(dlg.GetPath())
        self._read=False
        dlg.Destroy()
    def OnFilter(self, evt):
        if not self._read:
            self._oc.read(self.folderctrl.GetValue())
            self._read=True
        cat_list=self._oc.get_category_list()
        dlg=common_calendar.AutoSyncFilterDialog(self, -1, 'Filtering Parameters', cat_list)
        dlg.set(self._oc.get_filter())
        if dlg.ShowModal()==wx.ID_OK:
            self._oc.set_filter(dlg.get())
    def GetFolder(self):
        return self.folderctrl.GetValue()
    def GetFilter(self):
        return self._oc.get_filter()
