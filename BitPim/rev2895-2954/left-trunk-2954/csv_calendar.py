"Deals with CSV calendar import/export stuff"
import csv
import datetime
import wx
import bpcalendar
import common_calendar
import helpids
module_debug=False
class ExportCSVDialog(wx.Dialog):
    def __init__(self, parent, title):
        super(ExportCSVDialog, self).__init__(parent, -1, title)
        vbs=wx.BoxSizer(wx.VERTICAL)
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        hbs.Add(wx.StaticText(self, -1, "File"), 0, wx.ALL|wx.ALIGN_CENTRE, 5)
        self.filenamectrl=wx.TextCtrl(self, -1, "calendar.csv")
        hbs.Add(self.filenamectrl, 1, wx.ALL|wx.EXPAND, 5)
        self.browsectrl=wx.Button(self, wx.NewId(), "Browse...")
        hbs.Add(self.browsectrl, 0, wx.ALL|wx.EXPAND, 5)
        vbs.Add(hbs, 0, wx.EXPAND|wx.ALL, 5)
        vbs.Add(self.GetSelectionGui(self), 5, wx.EXPAND|wx.ALL, 5)
        vbs.Add(wx.StaticLine(self, -1, style=wx.LI_HORIZONTAL), 0, wx.EXPAND|wx.ALL,5)
        vbs.Add(self.CreateButtonSizer(wx.OK|wx.CANCEL|wx.HELP), 0, wx.ALIGN_CENTER|wx.ALL, 5)
        wx.EVT_BUTTON(self, self.browsectrl.GetId(), self.OnBrowse)
        wx.EVT_BUTTON(self, wx.ID_OK, self.OnOk)
        self.SetSizer(vbs)
        self.SetAutoLayout(True)
        vbs.Fit(self)
    def GetSelectionGui(self, parent):
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        self.__selection=wx.RadioBox(parent, wx.NewId(), 'Events Selection',
                                     choices=['All', 'Date Range'],
                                     style=wx.RA_SPECIFY_ROWS)
        hbs.Add(self.__selection, 0, wx.EXPAND|wx.ALL, 5)
        sbs=wx.StaticBoxSizer(wx.StaticBox(parent, -1, 'Date Range'), wx.VERTICAL)
        gs=wx.FlexGridSizer(-1, 2, 5, 5)
        gs.AddGrowableCol(1)
        gs.Add(wx.StaticText(self, -1, 'Start:'), 0, wx.ALL, 0)
        self.__start_date=wx.DatePickerCtrl(self, style=wx.DP_DROPDOWN | wx.DP_SHOWCENTURY)
        gs.Add(self.__start_date, 0, wx.ALL, 0)
        gs.Add(wx.StaticText(self, -1, 'End:'), 0, wx.ALL, 0)
        self.__end_date=wx.DatePickerCtrl(self, style=wx.DP_DROPDOWN | wx.DP_SHOWCENTURY)
        gs.Add(self.__end_date, 0, wx.ALL, 0)
        sbs.Add(gs, 1, wx.EXPAND|wx.ALL, 5)
        hbs.Add(sbs, 0, wx.EXPAND|wx.ALL, 5)
        return hbs
    def OnBrowse(self, _):
        dlg=wx.FileDialog(self, defaultFile=self.filenamectrl.GetValue(),
                          wildcard="CSV files (*.cvs)|*.csv", style=wx.SAVE|wx.CHANGE_DIR)
        if dlg.ShowModal()==wx.ID_OK:
            self.filenamectrl.SetValue(dlg.GetPath())
        dlg.Destroy()
    def __get_str(self, entry, field):
        s=getattr(entry, field, '')
        if s is None:
            s=''
        if isinstance(s, unicode):
            return s.encode('ascii', 'ignore')
        else:
            return str(s)
    def OnOk(self, _):
        filename=self.filenamectrl.GetValue()
        csv_event_template=(
            ('Start', 'start_str', None),
            ('End', 'end_str', None),
            ('Description', 'description', None),
            ('Location', 'location', None),
            ('Priority', 'priority', None),
            ('Alarm', 'alarm', None),
            ('All-Day', 'allday', None),
            ('Notes', 'notes', None),
            ('Categories', 'categories_str', None),
            ('Ringtone', 'ringtone', None),
            ('Wallpaper', 'wallpaper', None))
        csv_repeat_template=(
            ('Repeat Type', 'repeat_type', None),
            ('Repeat Interval', 'interval', None),
            ('Repeat Interval2', 'interval2', None),
            ('Day-of-Week', 'dow_str', None),
            ('Excluded Dates', 'suppressed_str', None))
        try:
            f=file(filename, 'wt')
        except:
            f=None
        if f is None:
            dlg=wx.MessageDialog(self, 'Failed to open file ['+filename+']',
                             'Export Error')
            dlg.ShowModal()
            dlg.Destroy()
            self.EndModal(wx.ID_OK)
        s=['"'+x[0]+'"' for x in csv_event_template]+\
           ['"'+x[0]+'"' for x in csv_repeat_template]
        f.write(','.join(s)+'\n')
        all_items=self.__selection.GetSelection()==0
        dt=self.__start_date.GetValue()
        range_start=(dt.GetYear(), dt.GetMonth()+1, dt.GetDay())
        dt=self.__end_date.GetValue()
        range_end=(dt.GetYear(), dt.GetMonth()+1, dt.GetDay())
        def __write_rec(f, cal_dict):
            for k,e in cal_dict.items():
                if not all_items and \
                   (e.end < range_start or e.start>range_end):
                    continue
                l=[]
                for field in csv_event_template:
                    if field[2] is None:
                        s=self.__get_str(e, field[1])
                    else:
                        s=field[2](e, field[1])
                    l+=['"'+s.replace('"', '')+'"']
                rpt=e.repeat
                if rpt is None:
                    l+=['']*len(csv_repeat_template)
                else:
                    for field in csv_repeat_template:
                        if field[2] is None:
                            s=self.__get_str(rpt, field[1])
                        else:
                            s=field[2](rpt, field[1])
                        l+=['"'+s.replace('"', '')+'"']
                f.write(','.join(l)+'\n')
        cal_dict=self.GetParent().GetCalendarData()
        __write_rec(f, cal_dict)
        f.close()
        self.EndModal(wx.ID_OK)
class CSVCalendarImportData(object):
    __default_filter={
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
    def __init__(self, file_name=None):
        self.__calendar_keys=(
            ('Start', 'start', self.__set_datetime),
            ('End', 'end', self.__set_datetime),
            ('Description', 'description', self.__set_str),
            ('Location', 'location', self.__set_str),
            ('Priority', 'priority', self.__set_priority),
            ('Alarm', 'alarm_value',self.__set_alarm),
            ('All-Day', 'allday', self.__set_bool),
            ('Notes', 'notes', self.__set_str),
            ('Categories', 'categories', self.__set_categories),
            ('Ringtone', 'ringtone', self.__set_str),
            ('Wallpaper', 'wallpaper', self.__set_str),
            ('Repeat Type', 'repeat_type', self.__set_repeat_type),
            ('Repeat Interval', 'repeat_interval', self.__set_int),
            ('Repeat Interval2', 'repeat_interval2', self.__set_int),
            ('Day-of-Week', 'repeat_dow', self.__set_dow),
            ('Excluded Dates', 'exceptions', self.__set_exceptions)
            )
        self.__file_name=file_name
        self.__data=[]
        self.__filter=self.__default_filter
        self.read()
    def __accept(self, entry):
        if self.__filter['start'] is not None and \
           entry['start'][:3]<self.__filter['start'][:3]:
            return False
        if self.__filter['end'] is not None and \
           entry['end'][:3]>self.__filter['end'][:3] and \
           entry['end'][:3]!=common_calendar.no_end_date[:3]:
            return False
        c=self.__filter['categories']
        if c is None or not len(c):
            return True
        if len([x for x in entry['categories'] if x in c]):
            return True
        return False
    def get(self):
        res={}
        single_rpt=self.__filter.get('rpt_events', False)
        for k in self.__data:
            try:
                if self.__accept(k):
                    if k.get('repeat', False) and single_rpt:
                        d=self.__generate_repeat_events(k)
                    else:
                        d=[k]
                    for n in d:
                        ce=bpcalendar.CalendarEntry()
                        self.__populate_entry(n, ce)
                        res[ce.id]=ce
            except:
                if module_debug:
                    raise
        return res
    def get_category_list(self):
        l=[]
        for e in self.__data:
            l+=[x for x in e.get('categories', []) if x not in l]
        return l
    def set_filter(self, filter):
        self.__filter=filter
    def get_filter(self):
        return self.__filter
    def get_display_data(self):
        cnt=0
        res={}
        single_rpt=self.__filter.get('rpt_events', False)
        for k in self.__data:
            if self.__accept(k):
                if k.get('repeat', False) and single_rpt:
                    d=self.__generate_repeat_events(k)
                else:
                    d=[k.copy()]
                for n in d:
                    if self.__filter.get('no_alarm', False):
                        n['alarm']=False
                    res[cnt]=n
                    cnt+=1
        return res
    def get_file_name(self):
        if self.__file_name is not None:
            return self.__file_name
        return ''
    def read(self, file_name=None):
        if file_name is not None:
            self.__file_name=file_name
        if self.__file_name is None:
            return
        try:
            csv_file=file(self.__file_name, 'rb')
        except:
            return
        reader=csv.reader(csv_file)
        h=reader.next()
        header_keys=[]
        for e in h:
            k=None
            for x in self.__calendar_keys:
                if e==x[0]:
                    k=x
                    break
            header_keys.append(k)
        self.__data=[]
        for row in reader:
            d={}
            for i,e in enumerate(row):
                if header_keys[i] is None:
                    continue
                elif header_keys[i][2] is None:
                    self.__set_str(e, d, header_keys[i][1])
                else:
                    header_keys[i][2](e, d, header_keys[i][1])
            self.__data.append(d)
        csv_file.close()
    def __populate_repeat_entry(self, e, ce):
        if not e.get('repeat', False) or e.get('repeat_type', None) is None:
            return
        rp=bpcalendar.RepeatEntry()
        rp_type=e['repeat_type']
        rp_interval=e.get('repeat_interval', 1)
        rp_interval2=e.get('repeat_interval2', 1)
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
        for k in e.get('exceptions', []):
            rp.add_suppressed(*k[:3])
        ce.repeat=rp
    def __populate_entry(self, e, ce):
        ce.description=e.get('description', None)
        ce.location=e.get('location', None)
        v=e.get('priority', None)
        if v is not None:
            ce.priority=v
        if not self.__filter.get('no_alarm', False) and \
               not self.__filter.get('alarm_override', False) and \
               e.get('alarm', False):
            ce.alarm=e.get('alarm_value', 0)
            ce.ringtone=self.__filter.get('ringtone', "")
            ce.vibrate=self.__filter.get('vibrate', False)
        elif not self.__filter.get('no_alarm', False) and \
               self.__filter.get('alarm_override', False):
            ce.alarm=self.__filter.get('alarm_value', 0)
            ce.ringtone=self.__filter.get('ringtone', "")
            ce.vibrate=self.__filter.get('vibrate', False)
        ce.allday=e.get('allday', False)
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
        self.__populate_repeat_entry(e, ce)
    def __generate_repeat_events(self, e):
        ce=bpcalendar.CalendarEntry()
        self.__populate_entry(e, ce)
        l=[]
        new_e=e.copy()
        new_e['repeat']=False
        for k in ('repeat_type', 'repeat_interval', 'repeat_dow'):
            if new_e.has_key(k):
                del new_e[k]
        s_date=datetime.datetime(*self.__filter['start'])
        e_date=datetime.datetime(*self.__filter['end'])
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
    def __set_str(self, v, d, key):
        d[key]=str(v)
    def __set_datetime(self, v, d, key):
        if v[4]!='-' or v[7]!='-' or v[10]!= ' ' or v[13]!=':':
            return
        d[key]=(int(v[:4]), int(v[5:7]), int(v[8:10]), int(v[11:13]),
                int(v[14:16]))
    def __set_priority(self, v, d, key):
        if len(v):
            d[key]=int(v)
        else:
            d[key]=None
    def __set_alarm(self, v, d, key):
        if len(v):
            d[key]=int(v)
            d['alarm']=d[key]!=-1
        else:
            d[key]=None
            d['alarm']=False
    def __set_int(self, v, d, key):
        if not len(v):
            d[key]=None
        else:
            d[key]=int(v)
    def __set_bool(self, v, d, key):
        d[key]=v.upper()=='TRUE'
    def __set_categories(self, v, d, key):
        if v is None or not len(v):
            d[key]=[]
        else:
            d[key]=v.split(';')
    def __set_repeat_type(self, v, d, key):
        if len(v):
            d[key]=str(v)
            d['repeat']=True
        else:
            d['repeat']=False
    def __set_dow(self, v, d, key):
        dow=0
        for e in v.split(';'):
            dow|=bpcalendar.RepeatEntry.dow_names.get(e, 0)
        d[key]=dow
    def __set_exceptions(self, v, d, key):
        l=[]
        for e in v.split(';'):
            if len(e):
                if e[4]=='-' and e[7]=='-':
                    l.append( (int(e[:4]), int(e[5:7]), int(e[8:10])) )
        d[key]=l
class CSVImportDialog(common_calendar.PreviewDialog):
    __column_labels=[
        ('description', 'Description', 400, None),
        ('start', 'Start', 150, common_calendar.bp_date_str),
        ('end', 'End', 150, common_calendar.bp_date_str),
        ('repeat_type', 'Repeat', 80, common_calendar.bp_repeat_str),
        ('alarm', 'Alarm', 80, common_calendar.bp_alarm_str),
        ('categories', 'Category', 150, common_calendar.category_str)
        ]
    ID_ADD=wx.NewId()
    def __init__(self, parent, id, title):
        self.__oc=CSVCalendarImportData()
        common_calendar.PreviewDialog.__init__(self, parent, id, title,
                               self.__column_labels,
                               self.__oc.get_display_data(),
                               config_name='import/calendar/csvdialog')
    def getcontrols(self, main_bs):
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        hbs.Add(wx.StaticText(self, -1, "CSV File:"), 0, wx.ALL|wx.ALIGN_CENTRE, 2)
        self.folderctrl=wx.TextCtrl(self, -1, "", style=wx.TE_READONLY)
        self.folderctrl.SetValue(self.__oc.get_file_name())
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
        dlg=wx.ProgressDialog('CSV Calendar Import',
                              'Importing CSV Calendar Data, please wait ...',
                              parent=self)
        self.__oc.read(self.folderctrl.GetValue())
        self.populate(self.__oc.get_display_data())
        dlg.Destroy()
        wx.EndBusyCursor()
    def OnBrowseFolder(self, evt):
        dlg=wx.FileDialog(self, "Pick a CSV Calendar File", wildcard='*.csv')
        id=dlg.ShowModal()
        if id==wx.ID_CANCEL:
            dlg.Destroy()
            return
        self.folderctrl.SetValue(dlg.GetPath())
        dlg.Destroy()
    def OnFilter(self, evt):
        cat_list=self.__oc.get_category_list()
        dlg=common_calendar.FilterDialog(self, -1, 'Filtering Parameters', cat_list)
        if dlg.ShowModal()==wx.ID_OK:
            self.__oc.set_filter(dlg.get())
            self.populate(self.__oc.get_display_data())
    def OnAdd(self, evt):
        self.EndModal(self.ID_ADD)
    def get(self):
        return self.__oc.get()
    def get_categories(self):
        return self.__oc.get_category_list()
def ImportCal(folder, filters):
    _oc=CSVCalendarImportData(folder)
    _oc.set_filter(filters)
    _oc.read()
    res={ 'calendar':_oc.get() }
    return res
class CSVAutoConfCalDialog(wx.Dialog):
    def __init__(self, parent, id, title, folder, filters,
                 style=wx.CAPTION|wx.MAXIMIZE_BOX| \
                 wx.SYSTEM_MENU|wx.DEFAULT_DIALOG_STYLE|wx.RESIZE_BORDER):
        self._oc=CSVCalendarImportData()
        self._oc.set_filter(filters)
        self.__read=False
        wx.Dialog.__init__(self, parent, id=id, title=title, style=style)
        main_bs=wx.BoxSizer(wx.VERTICAL)
        hbs=wx.BoxSizer(wx.HORIZONTAL)
        hbs.Add(wx.StaticText(self, -1, "CSV Calendar File:"), 0, wx.ALL|wx.ALIGN_CENTRE, 2)
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
        dlg=wx.FileDialog(self, "Pick a CSV Calendar File", wildcard='*.csv')
        id=dlg.ShowModal()
        if id==wx.ID_CANCEL:
            dlg.Destroy()
            return
        self.folderctrl.SetValue(dlg.GetPath())
        self.__read=False
        dlg.Destroy()
    def OnFilter(self, evt):
        if not self.__read:
            self._oc.read(self.folderctrl.GetValue())
            self.__read=True
        cat_list=self._oc.get_category_list()
        dlg=common_calendar.AutoSyncFilterDialog(self, -1, 'Filtering Parameters', cat_list)
        dlg.set(self._oc.get_filter())
        if dlg.ShowModal()==wx.ID_OK:
            self._oc.set_filter(dlg.get())
    def GetFolder(self):
        return self.folderctrl.GetValue()
    def GetFilter(self):
        return self._oc.get_filter()
