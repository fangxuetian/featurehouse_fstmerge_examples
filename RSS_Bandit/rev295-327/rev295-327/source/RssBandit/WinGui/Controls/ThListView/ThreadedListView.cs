using System; 
using System.Collections; 
using System.ComponentModel; 
using System.ComponentModel.Design; 
using System.Drawing; 
using System.Drawing.Design; 
using System.Drawing.Drawing2D; 
using System.Threading; 
using System.Windows.Forms; 
using System.Runtime.InteropServices; 
using System.Diagnostics; 
using System.Windows.Forms.ThListView.Sorting; 
using THLV = System.Windows.Forms.ThListView; 
using NewsComponents.Feed; 
using RssBandit.Resources; 
using System.Collections.Generic; 
using RssBandit; namespace  System.Windows.Forms.ThListView {
	
    public class  ThreadedListView  : ListView {
		
        private  ImageList imageListStates;
 
        private  Bitmap upBM, downBM;
 
        private  bool _isWinXP, _headerImageListLoaded;
 
        private  ImageList _headerImageList;
 
        private  ThreadedListViewSorter _sorter;
 
        private  ThreadedListViewColumnHeader _autoGroupCol = null;
 
        private readonly  List<string> _autoGroupList = new List<string>();
 
        private readonly  ThreadedListViewItemCollection _items;
 
        private readonly  ThreadedListViewColumnHeaderCollection _columns;
 
        private readonly  ThreadedListViewGroupCollection _groups;
 
        private  FeedColumnLayout _layout;
 
        private  bool _showInGroups = false;
 
        private  bool _autoGroup = false;
 
        private  bool _threadedView = true;
 
        private  string _emptyAutoGroupText = String.Empty;
 
        private  ThreadedListViewItem _noChildsPlaceHolder;
 
        public delegate  void  OnBeforeListLayoutChangeCancelEventHandler (object sender, ListLayoutCancelEventArgs e);
		
        public  event OnBeforeListLayoutChangeCancelEventHandler BeforeListLayoutChange; 
        public delegate  void  OnListLayoutChangedEventHandler (object sender, ListLayoutEventArgs e);
		
        public  event OnListLayoutChangedEventHandler ListLayoutChanged; 
        public delegate  void  OnListLayoutModifiedEventHandler (object sender, ListLayoutEventArgs e);
		
        public  event OnListLayoutModifiedEventHandler ListLayoutModified; 
        public delegate  void  OnBeforeExpandThreadCancelEventHandler (object sender, ThreadCancelEventArgs e);
		
        public  event OnBeforeExpandThreadCancelEventHandler BeforeExpandThread; 
        public delegate  void  OnBeforeCollapseThreadCancelEventHandler (object sender, ThreadCancelEventArgs e);
		
        public  event OnBeforeCollapseThreadCancelEventHandler BeforeCollapseThread; 
        public delegate  void  OnExpandThreadEventHandler (object sender, ThreadEventArgs e);
		
        public  event OnExpandThreadEventHandler ExpandThread; 
        public delegate  void  OnAfterExpandThreadEventHandler (object sender, ThreadEventArgs e);
		
        public  event OnAfterExpandThreadEventHandler AfterExpandThread; 
        public delegate  void  OnCollapseThreadEventHandler (object sender, ThreadEventArgs e);
		
        public  event OnCollapseThreadEventHandler CollapseThread; 
        public delegate  void  InsertItemsForPlaceHolderHandler (
            string placeHolderTicket, ThreadedListViewItem[] newChildItems, bool sortOnInsert);
		
        private  IContainer components;
 
        public  ThreadedListView()
        {
            _items = new ThreadedListViewItemCollection(this);
            _groups = new ThreadedListViewGroupCollection(this);
            _columns = new ThreadedListViewColumnHeaderCollection(this);
            InitializeComponent();
            InitListView();
        }
 
        internal  void SetColumnOrderArray(int[] orderArray)
        {
            Win32.API.SetColumnOrderArray(Handle, orderArray);
        }
 
        internal  int[] GetColumnOrderArray()
        {
            return Win32.API.GetColumnOrderArray(Handle, Columns.Count);
        }
 
        [Browsable(false), EditorBrowsable(EditorBrowsableState.Advanced)] 
        public new  bool CheckBoxes
        {
            get
            {
                return base.CheckBoxes;
            }
            set
            {
                base.CheckBoxes = value;
            }
        }
 
        [Browsable(false), EditorBrowsable(EditorBrowsableState.Advanced)] 
        public new  ImageList StateImageList
        {
            get
            {
                return base.StateImageList;
            }
            set
            {
                base.StateImageList = value;
            }
        }
 
        private  void InitListView()
        {
            _headerImageListLoaded = false;
            _isWinXP = RssBandit.Win32.IsOSAtLeastWindowsXP;
            ShowAsThreads = true;
            View = View.Details;
            _sorter = new ThreadedListViewSorter(this);
            _sorter.BeforeSort += OnBeforeSort;
            _sorter.AfterSort += OnAfterSort;
            HandleCreated += OnListviewHandleCreated;
            MouseDown += OnListviewMouseDown;
        }
 
        protected override  void Dispose(bool disposing)
        {
            if (disposing)
            {
                if (components != null)
                    components.Dispose();
            }
            base.Dispose(disposing);
        }
 
        private  void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.Resources.ResourceManager resources = new System.Resources.ResourceManager(typeof (ThreadedListView));
            this.imageListStates = new System.Windows.Forms.ImageList(this.components);
            this.imageListStates.ImageSize = new System.Drawing.Size(16, 16);
            this.imageListStates.ImageStream =
                ((System.Windows.Forms.ImageListStreamer) (resources.GetObject("imageListStates.ImageStream")));
            this.imageListStates.TransparentColor = System.Drawing.Color.Transparent;
        }
 
        private  void OnListviewHandleCreated(object sender, EventArgs e)
        {
            SetExtendedStyles();
        }
 
        private  void OnListviewMouseDown(object sender, MouseEventArgs e)
        {
            if (base.Items.Count <= 0)
                return;
            if (e.Button != MouseButtons.Left)
                return;
            ThreadedListViewItem lvi = GetItemAt(e.X, e.Y) as ThreadedListViewItem;
            if (lvi != null && lvi.StateImageHitTest(new Point(e.X, e.Y)))
            {
                if (lvi.HasChilds)
                {
                    if (lvi.Expanded == false)
                        ExpandListViewItem(lvi, false);
                    else if (lvi.Expanded)
                        CollapseListViewItem(lvi);
                }
            }
            else if (lvi != null && e.Clicks > 1)
            {
                if (lvi.HasChilds)
                {
                    if (lvi.Expanded == false)
                    {
                        ExpandListViewItem(lvi, true);
                    }
                    else if (lvi.Expanded)
                    {
                        CollapseListViewItem(lvi);
                    }
                }
            }
        }
 
        [DesignerSerializationVisibility(DesignerSerializationVisibility.Content),
         Description("the items collection of this view"),
         Editor(typeof (ThreadedListViewItemCollectionEditor), typeof (UITypeEditor)),
         Category("Behavior")] 
        public new  ThreadedListViewItemCollection Items
        {
            get
            {
                return _items;
            }
        }
 
        [DesignerSerializationVisibility(DesignerSerializationVisibility.Content),
         Description("the header columns collection of this view"),
         Category("Behavior")] 
        public new  ThreadedListViewColumnHeaderCollection Columns
        {
            get
            {
                return _columns;
            }
        }
 
        [DesignerSerializationVisibility(DesignerSerializationVisibility.Content),
         Description("collection of available groups (manually added)"),
         Editor(typeof (CollectionEditor), typeof (UITypeEditor)),
         Category("Grouping")] 
  public  ThreadedListViewGroupCollection Groups{
            get
            {
                return _groups;
            }
        }
 
        [Category("Grouping"),
         Description("flag if the grouping view is active"),
         DefaultValue(false)] 
        public  bool ShowInGroups
        {
            get
            {
                return _showInGroups;
            }
            set
            {
                if (_showInGroups != value)
                {
                    _showInGroups = value;
                    if (_showInGroups)
                        ShowAsThreads = false;
                    if (_autoGroup && value == false)
                    {
                        _autoGroup = false;
                        _autoGroupCol = null;
                        _autoGroupList.Clear();
                    }
                    APIEnableGrouping(value);
                }
            }
        }
 
        [Category("Grouping"),
         Description("flag if the autogroup mode is active"),
         DefaultValue(false)] 
        public  bool AutoGroupMode
        {
            get
            {
                return _autoGroup;
            }
            set
            {
                _autoGroup = value;
                if (_autoGroup)
                    ShowAsThreads = false;
                if (_autoGroupCol != null)
                {
                    AutoGroupByColumn(_autoGroupCol.Index);
                }
            }
        }
 
        [Category("Grouping"),
         Description("column by with values the listiew is automatically grouped"),
         DefaultValue(typeof (ColumnHeader), ""),
         DesignerSerializationVisibility(DesignerSerializationVisibility.Visible)] 
        public  ThreadedListViewColumnHeader AutoGroupColumn
        {
            get
            {
                return _autoGroupCol;
            }
            set
            {
                _autoGroupCol = value;
                if (_autoGroupCol != null)
                {
                    AutoGroupByColumn(_autoGroupCol.Index);
                }
            }
        }
 
        [Category("Grouping"),
         Description("the text that is displayed instead of an empty auto group text"),
         DefaultValue("")] 
        public  string EmptyAutoGroupText
        {
            get
            {
                return _emptyAutoGroupText;
            }
            set
            {
                _emptyAutoGroupText = value;
                if (_autoGroupCol != null)
                {
                    AutoGroupByColumn(_autoGroupCol.Index);
                }
            }
        }
 
        [Browsable(false),
         Description("readonly array of all automatically created groups"),
         Category("Grouping")] 
        public  string[] Autogroups
        {
            get
            {
                return _autoGroupList.ToArray();
            }
        }
 
        public  bool AutoGroupByColumn(int columnID)
        {
            if (columnID >= Columns.Count || columnID < 0)
            {
                return false;
            }
            try
            {
                _autoGroupList.Clear();
                foreach (ThreadedListViewItem itm in Items)
                {
                    if (
                        !_autoGroupList.Contains(itm.SubItems[columnID].Text == String.Empty
                                                     ? _emptyAutoGroupText
                                                     : itm.SubItems[columnID].Text))
                    {
                        _autoGroupList.Add(itm.SubItems[columnID].Text == String.Empty
                                               ? EmptyAutoGroupText
                                               : itm.SubItems[columnID].Text);
                    }
                }
                _autoGroupList.Sort();
                Win32.API.ClearListViewGroup(Handle);
                foreach (string text in _autoGroupList)
                {
                    Win32.API.AddListViewGroup(Handle, text, _autoGroupList.IndexOf(text));
                }
                foreach (ThreadedListViewItem itm in Items)
                {
                    int index =
                        _autoGroupList.IndexOf(itm.SubItems[columnID].Text == ""
                                                   ? _emptyAutoGroupText
                                                   : itm.SubItems[columnID].Text);
                    Win32.API.AddItemToGroup(Handle, itm.Index, index);
                }
                APIEnableGrouping(true);
                _showInGroups = true;
                _autoGroup = true;
                _autoGroupCol = Columns[columnID];
                Refresh();
                return true;
            }
            catch (Exception ex)
            {
                throw new SystemException("Error in ThreadedListView.AutoGroupByColumn: " + ex.Message);
            }
        }
 
        public  ThreadedListViewItem NoThreadChildsPlaceHolder
        {
            get
            {
                return _noChildsPlaceHolder;
            }
            set
            {
                _noChildsPlaceHolder = value;
            }
        }
 
        public  bool Regroup()
        {
            try
            {
                Win32.API.ClearListViewGroup(Handle);
                foreach (ThreadedListViewGroup grp in Groups)
                {
                    Win32.API.AddListViewGroup(Handle, grp.GroupText, grp.GroupIndex);
                }
                foreach (ThreadedListViewItem itm in Items)
                {
                    Win32.API.AddItemToGroup(Handle, itm.Index, itm.GroupIndex);
                }
                APIEnableGrouping(true);
                _showInGroups = true;
                _autoGroup = false;
                _autoGroupCol = null;
                _autoGroupList.Clear();
                return true;
            }
            catch (Exception ex)
            {
                throw new SystemException("Error in ThreadedListView.Regroup: " + ex.Message);
            }
        }
 
        public  ThreadedListViewSorter SortManager
        {
            get
            {
                return _sorter;
            }
        }
 
        [DesignerSerializationVisibility(DesignerSerializationVisibility.Hidden)] 
        public  FeedColumnLayout FeedColumnLayout
        {
            get
            {
                return FeedColumnLayoutFromCurrentSettings();
            }
            set
            {
                if (value != null)
                {
                    FeedColumnLayout newLayout = value;
                    if (!RaiseBeforeFeedColumnLayoutChangeEventCancel(newLayout))
                    {
                        _layout = newLayout;
                        RaiseFeedColumnLayoutChangedEvent(newLayout);
                        if (newLayout.SortOrder != NewsComponents.SortOrder.None)
                            _sorter.Sort(Columns.GetIndexByKey(newLayout.SortByColumn),
                                         ConvertSortOrder(newLayout.SortOrder));
                    }
                }
            }
        }
 
        [Category("Behavior"),
         Description("flag if the threaded view mode is active"),
         DefaultValue(true)] 
        public  bool ShowAsThreads
        {
            get
            {
                return _threadedView;
            }
            set
            {
                _threadedView = value;
                if (_threadedView)
                {
                    ShowInGroups = false;
                    AutoGroupMode = false;
                    CheckBoxes = false;
                    StateImageList = imageListStates;
                    ReApplyItemStates();
                }
                else
                {
                    StateImageList = null;
                }
            }
        }
 
        internal  void RedrawItems()
        {
            Win32.API.RedrawItems(this, true);
            ArrangeIcons();
        }
 
        internal  void UpdateItems()
        {
            Win32.API.UpdateItems(this);
        }
 
        public  void SetColumnStyle(int column, Font font, Color foreColor, Color backColor)
        {
            SuspendLayout();
            foreach (ThreadedListViewItem itm in Items)
            {
                if (itm.SubItems.Count > column)
                {
                    itm.SubItems[column].Font = font;
                    itm.SubItems[column].BackColor = backColor;
                    itm.SubItems[column].ForeColor = foreColor;
                }
            }
            ResumeLayout();
        }
 
        public  void SetColumnStyle(int column, Font font, Color foreColor)
        {
            SetColumnStyle(column, font, foreColor, BackColor);
        }
 
        public  void SetColumnStyle(int column, Font font)
        {
            SetColumnStyle(column, font, ForeColor, BackColor);
        }
 
        public  void ResetColumnStyle(int column)
        {
            SuspendLayout();
            foreach (ThreadedListViewItem itm in Items)
            {
                if (itm.SubItems.Count > column)
                {
                    itm.SubItems[column].ResetStyle();
                }
            }
            ResumeLayout();
        }
 
        public  void SetBackgroundImage(string imagePath, ImagePosition position)
        {
            Win32.API.SetListViewImage(Handle, imagePath, position);
        }
 
        private  void ReApplyItemStates()
        {
            if (base.Items.Count == 0)
                return;
            foreach (ThreadedListViewItem lv in Items)
            {
                if (lv.HasChilds)
                    lv.Expanded = !lv.Collapsed;
            }
        }
 
        internal  void CollapseListViewItem(ThreadedListViewItem lvItem)
        {
            if (lvItem != null && lvItem.Expanded)
            {
                if (RaiseBeforeCollapseEventCancel(lvItem))
                {
                    lvItem.StateImageIndex = 0;
                    return;
                }
                int focusedItemIndex;
                if (FocusedItem == null)
                    focusedItemIndex = lvItem.Index;
                else
                    focusedItemIndex = FocusedItem.Index;
                BeginUpdate();
                try
                {
                    lvItem.SetThreadState(false);
                    int paramItemIndex = lvItem.Index;
                    int currentIndent = lvItem.IndentLevel;
                    int nextIndex = paramItemIndex + 1;
                    lock (Items)
                    {
                        while (nextIndex < base.Items.Count &&
                               ((ThreadedListViewItem) base.Items[nextIndex]).IndentLevel > currentIndent)
                        {
                            Items[nextIndex].Parent = null;
                            Items.RemoveAt(nextIndex);
                            if (nextIndex < focusedItemIndex)
                                focusedItemIndex = focusedItemIndex - 1;
                            else if (nextIndex == focusedItemIndex)
                                focusedItemIndex = paramItemIndex;
                        }
                    }
                    RaiseCollapseEvent(lvItem);
                }
                finally
                {
                    EndUpdate();
                }
                if (focusedItemIndex >= 0)
                {
                    ListViewItem lvi = base.Items[focusedItemIndex];
                    lvi.Focused = true;
                    lvi.Selected = true;
                }
            }
        }
 
        internal  void ExpandListViewItem(ThreadedListViewItem lvItem, bool activate)
        {
            int selIdxsCount = SelectedIndices.Count;
            int[] selIdxs = new int[selIdxsCount];
            SelectedIndices.CopyTo(selIdxs, 0);
            ThreadedListViewItem[] newItems;
            if (lvItem != null && lvItem.Collapsed)
            {
                if (RaiseBeforeExpandEventCancel(lvItem))
                {
                    lvItem.StateImageIndex = 0;
                    return;
                }
                int paramItemIndex = lvItem.Index;
                int currentIndent = lvItem.IndentLevel;
                newItems = RaiseExpandEvent(lvItem);
                if (newItems == null)
                {
                    ThreadedListViewItem item = _noChildsPlaceHolder;
                    if (item == null)
                    {
                        item = new ThreadedListViewItemPlaceHolder(SR.FeedListNoChildsMessage);
                        item.Font = new Font(Font.FontFamily, Font.Size, FontStyle.Regular);
                    }
                    newItems = new ThreadedListViewItem[] {item};
                }
                if (newItems.Length > 1 && ListViewItemSorter != null)
                {
                    Array.Sort(newItems, ListViewItemSorter);
                }
                if (_showInGroups)
                    APIEnableGrouping(false);
                BeginUpdate();
                try
                {
                    lvItem.SetThreadState(true);
                    lock (Items)
                    {
                        foreach (ThreadedListViewItem newListItem in newItems)
                        {
                            while (newListItem.SubItems.Count < Columns.Count)
                            {
                                newListItem.SubItems.Add(String.Empty);
                            }
                            newListItem.Parent = lvItem;
                            Items.Insert(paramItemIndex + 1, newListItem);
                            newListItem.IndentLevel = currentIndent + 1;
                            paramItemIndex++;
                        }
                    }
                }
                finally
                {
                    EndUpdate();
                }
                RedrawItems();
                if (_showInGroups)
                    APIEnableGrouping(true);
                try
                {
                    EnsureVisible(paramItemIndex - 1);
                    EnsureVisible(lvItem.Index);
                }
                catch
                {
                }
                if (activate)
                {
                    SelectedItems.Clear();
                    lvItem.Selected = true;
                    lvItem.Focused = true;
                }
                else if (selIdxsCount > 0)
                {
                }
                RaiseAfterExpandEvent(lvItem, newItems);
            }
        }
 
        public  void CheckForLayoutModifications()
        {
            FeedColumnLayout layout = _layout;
            if (layout != null)
            {
                GuiInvoker.InvokeAsync(this, delegate
                                                 {
                                                     FeedColumnLayout current = FeedColumnLayoutFromCurrentSettings();
                                                     if (!layout.Equals(current))
                                                     {
                                                         RaiseFeedColumnLayoutModifiedEvent(current);
                                                     }
                                                 });
            }
        }
 
        public  void ApplyLayoutModifications()
        {
            _layout = FeedColumnLayoutFromCurrentSettings();
        }
 
        private  FeedColumnLayout FeedColumnLayoutFromCurrentSettings()
        {
            try
            {
                FeedColumnLayout layout = new FeedColumnLayout();
                lock (Columns)
                {
                    if (_sorter.SortColumnIndex >= 0 && _sorter.SortColumnIndex < Columns.Count)
                    {
                        layout.SortByColumn = Columns[_sorter.SortColumnIndex].Key;
                        layout.SortOrder = ConvertSortOrder(_sorter.SortOrder);
                    }
                    int[] colOrder = GetColumnOrderArray();
                    List<string> aCols = new List<string>(Columns.Count);
                    List<int> aColWidths = new List<int>(Columns.Count);
                    for (int i = 0; i < Columns.Count; i++)
                    {
                        aCols.Add(Columns[colOrder[i]].Key);
                        aColWidths.Add(Columns[colOrder[i]].Width);
                    }
                    layout.Columns = aCols;
                    layout.ColumnWidths = aColWidths;
                }
                return layout;
            }
            catch
            {
                return null;
            }
        }
 
        private  bool RaiseBeforeExpandEventCancel(ThreadedListViewItem tlv)
        {
            bool cancel = false;
            if (BeforeExpandThread != null)
            {
                ThreadCancelEventArgs e = new ThreadCancelEventArgs(tlv, cancel);
                try
                {
                    BeforeExpandThread(this, e);
                    cancel = e.Cancel;
                }
                catch
                {
                }
            }
            return cancel;
        }
 
        private  ThreadedListViewItem[] RaiseExpandEvent(ThreadedListViewItem tlv)
        {
            if (ExpandThread != null)
            {
                ThreadEventArgs tea = new ThreadEventArgs(tlv);
                try
                {
                    ExpandThread(this, tea);
                }
                catch
                {
                }
                if (tea.ChildItems != null)
                {
                    return tea.ChildItems;
                }
            }
            return new ThreadedListViewItem[] {};
        }
 
        private  void RaiseAfterExpandEvent(ThreadedListViewItem parent, ThreadedListViewItem[] newItems)
        {
            if (AfterExpandThread != null)
            {
                ThreadEventArgs tea = new ThreadEventArgs(parent);
                tea.ChildItems = newItems;
                try
                {
                    AfterExpandThread(this, tea);
                }
                catch
                {
                }
            }
        }
 
        private  bool RaiseBeforeCollapseEventCancel(ThreadedListViewItem tlv)
        {
            bool cancel = false;
            if (BeforeCollapseThread != null)
            {
                ThreadCancelEventArgs e = new ThreadCancelEventArgs(tlv, cancel);
                try
                {
                    BeforeCollapseThread(this, e);
                    cancel = e.Cancel;
                }
                catch
                {
                }
            }
            return cancel;
        }
 
        private  void RaiseCollapseEvent(ThreadedListViewItem tlv)
        {
            if (CollapseThread != null)
            {
                ThreadEventArgs tea = new ThreadEventArgs(tlv);
                try
                {
                    CollapseThread(this, tea);
                }
                catch
                {
                }
            }
        }
 
        private  bool RaiseBeforeFeedColumnLayoutChangeEventCancel(FeedColumnLayout newLayout)
        {
            bool cancel = false;
            if (BeforeListLayoutChange != null)
            {
                ListLayoutCancelEventArgs e = new ListLayoutCancelEventArgs(newLayout, cancel);
                try
                {
                    BeforeListLayoutChange(this, e);
                    cancel = e.Cancel;
                }
                catch
                {
                }
            }
            return cancel;
        }
 
        private  void RaiseFeedColumnLayoutChangedEvent(FeedColumnLayout layout)
        {
            if (ListLayoutChanged != null)
            {
                try
                {
                    ListLayoutChanged(this, new ListLayoutEventArgs(layout));
                }
                catch
                {
                }
            }
        }
 
        private  void RaiseFeedColumnLayoutModifiedEvent(FeedColumnLayout layout)
        {
            if (ListLayoutModified != null)
            {
                try
                {
                    ListLayoutModified(this, new ListLayoutEventArgs(layout));
                }
                catch
                {
                }
            }
        }
 
        private  void SetExtendedStyles()
        {
            Win32.LVS_EX ex_styles =
                (Win32.LVS_EX) Win32.API.SendMessage(Handle, Win32.W32_LVM.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, IntPtr.Zero);
            ex_styles |= Win32.LVS_EX.LVS_EX_DOUBLEBUFFER | Win32.LVS_EX.LVS_EX_INFOTIP |
                         Win32.LVS_EX.LVS_EX_SUBITEMIMAGES;
            Win32.API.SendMessage(Handle, Win32.W32_LVM.LVM_SETEXTENDEDLISTVIEWSTYLE, 0, new IntPtr((int) ex_styles));
        }
 
        public  void InsertItemsForPlaceHolder(string placeHolderTicket, ThreadedListViewItem[] newChildItems,
                                              bool sortOnInsert)
        {
            if (placeHolderTicket == null || placeHolderTicket.Length == 0)
                throw new ArgumentNullException("placeHolderTicket");
            ThreadedListViewItemPlaceHolder placeHolder = null;
            lock (Items)
            {
                for (int i = 0; i < Items.Count; i++)
                {
                    ThreadedListViewItemPlaceHolder p = Items[i] as ThreadedListViewItemPlaceHolder;
                    if (p != null)
                    {
                        if (p.InsertionPointTicket.Equals(placeHolderTicket))
                        {
                            placeHolder = p;
                            break;
                        }
                    }
                }
                if (placeHolder == null)
                    return;
                int parentItemIndex = placeHolder.Index;
                int parentIndentLevel = placeHolder.IndentLevel;
                try
                {
                    BeginUpdate();
                    Items.RemoveAt(parentItemIndex);
                    if (newChildItems == null || newChildItems.Length == 0)
                    {
                        ThreadedListViewItem item = _noChildsPlaceHolder;
                        if (item == null)
                        {
                            item = new ThreadedListViewItemPlaceHolder(SR.FeedListNoChildsMessage);
                            item.Font = new Font(Font.FontFamily, Font.Size, FontStyle.Regular);
                        }
                        newChildItems = new ThreadedListViewItem[] {item};
                    }
                    if (newChildItems.Length > 1 && _sorter.SortOrder != SortOrder.None && sortOnInsert)
                    {
                        Array.Sort(newChildItems, _sorter.GetComparer());
                    }
                    for (int i = 0; i < newChildItems.Length; i++)
                    {
                        ThreadedListViewItem newListItem = newChildItems[i];
                        newListItem.Parent = placeHolder.Parent;
                        while (newListItem.SubItems.Count < Columns.Count)
                        {
                            newListItem.SubItems.Add(String.Empty);
                        }
                        if (parentItemIndex < Items.Count)
                        {
                            Items.Insert(parentItemIndex, newListItem);
                        }
                        else
                        {
                            Items.Add(newListItem);
                        }
                        newListItem.IndentLevel = parentIndentLevel;
                        parentItemIndex++;
                    }
                }
                finally
                {
                    EndUpdate();
                }
            }
            UpdateItems();
        }
 
        private  void APIEnableGrouping(bool value)
        {
            IntPtr param = IntPtr.Zero;
            int onOff = (value ? 1 : 0);
            Win32.API.SendMessage(Handle, Win32.W32_LVM.LVM_ENABLEGROUPVIEW, onOff, ref param);
        }
 
        internal  void RefreshSortMarks(int sortedColumnIndex, SortOrder sortOrder)
        {
            if (!IsHandleCreated)
            {
                return;
            }
            IntPtr hHeader = Win32.API.SendMessage(Handle,
                                                   Win32.W32_LVM.LVM_GETHEADER, IntPtr.Zero, IntPtr.Zero);
            if (!hHeader.Equals(IntPtr.Zero))
            {
                if (upBM == null)
                {
                    upBM = GetBitmap(true);
                    downBM = GetBitmap(false);
                }
                try
                {
                    lock (Columns)
                    {
                        for (int i = 0; i < Columns.Count; i++)
                        {
                            Win32.HDITEM item = new Win32.HDITEM();
                            item.cchTextMax = 255;
                            item.mask =
                                (int)
                                (Win32.HeaderItemMask.HDI_FORMAT | Win32.HeaderItemMask.HDI_TEXT |
                                 Win32.HeaderItemMask.HDI_BITMAP);
                            IntPtr result =
                                Win32.API.SendMessage(hHeader, Win32.HeaderControlMessages.HDM_GETITEM, new IntPtr(i),
                                                      item);
                            if (result.ToInt32() > 0)
                            {
                                ColumnHeader colHdr = Columns[i];
                                HorizontalAlignment align = colHdr.TextAlign;
                                string txt = (colHdr.Text ?? String.Empty);
                                item.pszText = Marshal.StringToHGlobalAuto(txt);
                                item.mask = (int) (Win32.HeaderItemMask.HDI_FORMAT | Win32.HeaderItemMask.HDI_TEXT);
                                item.fmt = (int) (Win32.HeaderItemFlags.HDF_STRING);
                                item.mask |= (int) (Win32.HeaderItemMask.HDI_BITMAP);
                                item.hbm = IntPtr.Zero;
                                if (i == sortedColumnIndex && sortOrder != SortOrder.None)
                                {
                                    item.fmt |= (int) (Win32.HeaderItemFlags.HDF_BITMAP);
                                    if (align != HorizontalAlignment.Right)
                                        item.fmt |= (int) (Win32.HeaderItemFlags.HDF_BITMAP_ON_RIGHT);
                                    if (sortOrder == SortOrder.Ascending)
                                        item.hbm = upBM.GetHbitmap();
                                    else
                                        item.hbm = downBM.GetHbitmap();
                                }
                                Win32.API.SendMessage(hHeader, Win32.HeaderControlMessages.HDM_SETITEM, new IntPtr(i),
                                                      item);
                                Marshal.FreeHGlobal(item.pszText);
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    Trace.WriteLine("RefreshSortMarks() error: " + ex.Message);
                }
            }
        }
 
        private  void SetHeaderImageList()
        {
            if (! _headerImageListLoaded)
            {
                _headerImageList = new ImageList(components);
                _headerImageList.ImageSize = (_isWinXP ? new Size(9, 9) : new Size(8, 8));
                _headerImageList.TransparentColor = Color.Magenta;
                _headerImageList.Images.Add(GetBitmap(true));
                _headerImageList.Images.Add(GetBitmap(false));
                IntPtr hHeader = Win32.API.SendMessage(Handle,
                                                       Win32.W32_LVM.LVM_GETHEADER, IntPtr.Zero, IntPtr.Zero);
                Win32.API.SendMessage(hHeader, Win32.HeaderControlMessages.HDM_SETIMAGELIST, IntPtr.Zero,
                                      _headerImageList.Handle);
                _headerImageListLoaded = true;
            }
        }
 
        private  Bitmap GetBitmap(bool ascending)
        {
            if (_isWinXP)
            {
                Bitmap bm = new Bitmap(9, 9);
                Graphics gfx = Graphics.FromImage(bm);
                Brush fillBrush = SystemBrushes.ControlDark;
                Brush backBrush = new SolidBrush(Color.FromArgb(235, 234, 219));
                gfx.FillRectangle(backBrush , 0, 0, 9, 9);
                backBrush.Dispose();
                GraphicsPath path = new GraphicsPath();
                if (ascending)
                {
                    path.AddLine(4, 1, -1, 7);
                    path.AddLine(-1, 7, 9, 7);
                    path.AddLine(9, 7, 4, 1);
                    gfx.FillPath(fillBrush, path);
                }
                else
                {
                    path.AddLine(0, 2, 9, 2);
                    path.AddLine(9, 2, 4, 7);
                    path.AddLine(4, 7, 0, 2);
                    gfx.FillPath(fillBrush, path);
                }
                path.Dispose();
                gfx.Dispose();
                return bm;
            }
            else
            {
                Bitmap bm = new Bitmap(8, 8);
                Graphics gfx = Graphics.FromImage(bm);
                Pen lightPen = SystemPens.ControlLightLight;
                Pen shadowPen = SystemPens.ControlDark;
                gfx.FillRectangle(SystemBrushes.ControlLight, 0, 0, 8, 8);
                if (ascending)
                {
                    gfx.DrawLine(lightPen, 0, 7, 7, 7);
                    gfx.DrawLine(lightPen, 7, 7, 4, 0);
                    gfx.DrawLine(shadowPen, 3, 0, 0, 7);
                }
                else
                {
                    gfx.DrawLine(lightPen, 4, 7, 7, 0);
                    gfx.DrawLine(shadowPen, 3, 7, 0, 0);
                    gfx.DrawLine(shadowPen, 0, 0, 7, 0);
                }
                gfx.Dispose();
                return bm;
            }
        }
 
        private  void OnBeforeSort(object sender, EventArgs e)
        {
            SetHeaderImageList();
            if (_showInGroups)
            {
                APIEnableGrouping(false);
            }
        }
 
        private  void OnAfterSort(object sender, EventArgs e)
        {
            if (_showInGroups)
            {
                APIEnableGrouping(true);
                if (_autoGroup)
                {
                    AutoGroupByColumn(_autoGroupCol.Index);
                }
                else
                {
                    Regroup();
                }
            }
        }
 
        private static  SortOrder ConvertSortOrder(NewsComponents.SortOrder sortOrder)
        {
            if (sortOrder == NewsComponents.SortOrder.Ascending)
            {
                return SortOrder.Ascending;
            }
            else if (sortOrder == NewsComponents.SortOrder.Descending)
            {
                return SortOrder.Descending;
            }
            else
            {
                return SortOrder.None;
            }
        }
 
        private static  NewsComponents.SortOrder ConvertSortOrder(SortOrder sortOrder)
        {
            if (sortOrder == SortOrder.Ascending)
            {
                return NewsComponents.SortOrder.Ascending;
            }
            else if (sortOrder == SortOrder.Descending)
            {
                return NewsComponents.SortOrder.Descending;
            }
            else
            {
                return NewsComponents.SortOrder.None;
            }
        }

	}

}
