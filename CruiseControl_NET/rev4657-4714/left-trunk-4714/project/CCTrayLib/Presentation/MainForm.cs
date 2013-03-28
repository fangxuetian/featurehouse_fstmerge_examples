using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Reflection;
using System.Windows.Forms;
using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.CCTrayLib.Monitoring;
using Message = System.Windows.Forms.Message;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Presentation
{
    public class MainForm : Form
    {
        private static int WM_QUERYENDSESSION = 0x0011;
        private MenuItem menuFile;
        private MenuItem menuFileExit;
        private ListView lvProjects;
        private ColumnHeader colProject;
        private ColumnHeader colServer;
        private ImageList iconList;
        private MainMenu mainMenu;
        private TrayIcon trayIcon;
        private ContextMenu projectContextMenu;
        private MenuItem mnuForce;
        private MenuItem mnuStart;
        private MenuItem mnuStop;
        private MenuItem mnuAbort;
        private MenuItem mnuWebPage;
        private MenuItem mnuViewIcons;
        private MenuItem mnuViewList;
        private MenuItem mnuViewDetails;
        private MenuItem mnuCopyBuildLabel;
        private ImageList largeIconList;
        private Button btnForceBuild;
        private ColumnHeader colLastBuildLabel;
        private ColumnHeader colActivity;
        private IContainer components;
        private MenuItem mnuFilePreferences;
        private MenuItem menuItem3;
        private ColumnHeader colDetail;
        private MainFormController controller;
        private MenuItem mnuView;
        private ContextMenu mnuTrayContextMenu;
        private MenuItem mnuTraySettings;
        private MenuItem mnuShow;
        private MenuItem mnuTrayExit;
        private MenuItem menuItem5;
        private ICCTrayMultiConfiguration configuration;
        private ColumnHeader colLastBuildTime;
        private bool systemShutdownInProgress;
        private MenuItem mnuFixBuild;
        private MenuItem mnuCancelPending;
        private Splitter splitterQueueView;
        private Button btnToggleQueueView;
        private Panel pnlButtons;
        private Panel pnlViewQueues;
        private QueueTreeView queueTreeView;
        private ContextMenu queueContextMenu;
        private ImageList queueIconList;
        private MenuItem mnuQueueCancelPending;
        private Button btnStartStopProject;
        private ColumnHeader colProjectStatus;
        private MenuItem mnuAbout;
        private ToolTip tltBuildStage;
        private PersistWindowState windowState;
        private bool queueViewPanelVisible;
        public MainForm(ICCTrayMultiConfiguration configuration)
        {
            InitializeComponent();
            this.configuration = configuration;
            HookPersistentWindowState();
        }
        private void HookPersistentWindowState()
        {
            windowState = new PersistWindowState();
            windowState.Parent = this;
            windowState.RegistryPath = @"Software\ThoughtWorks\CCTray";
            windowState.LoadState += OnLoadState;
            windowState.SaveState += OnSaveState;
        }
        private void OnLoadState(object sender, WindowStateEventArgs e)
        {
            colProject.Width = (int)e.Key.GetValue("ProjectColumnWidth", 160);
            colServer.Width = (int)e.Key.GetValue("ServerColumnWidth", 100);
            colActivity.Width = (int)e.Key.GetValue("ActivityColumnWidth", 132);
            colDetail.Width = (int)e.Key.GetValue("DetailColumnWidth", 250);
            colLastBuildLabel.Width = (int)e.Key.GetValue("LastBuildLabelColumnWidth", 120);
            colLastBuildTime.Width = (int)e.Key.GetValue("LastBuildTimeColumnWidth", 130);
            queueViewPanelVisible = bool.Parse(e.Key.GetValue("QueueViewPanelVisible", bool.FalseString).ToString());
            splitterQueueView.Visible = queueViewPanelVisible;
            pnlViewQueues.Visible = queueViewPanelVisible;
            queueTreeView.Visible = queueViewPanelVisible;
            UpdateViewQueuesButtonLabel();
            splitterQueueView.SplitPosition = (int)e.Key.GetValue("QueueViewSplitterPosition", 80);
            lvProjects.View = (View)Enum.Parse(typeof(View), (string)e.Key.GetValue("ProjectViewMode", View.Details.ToString()));
        }
        private void OnSaveState(object sender, WindowStateEventArgs e)
        {
            e.Key.SetValue("ProjectColumnWidth", colProject.Width);
            e.Key.SetValue("ServerColumnWidth", colServer.Width);
            e.Key.SetValue("ActivityColumnWidth", colActivity.Width);
            e.Key.SetValue("DetailColumnWidth", colDetail.Width);
            e.Key.SetValue("LastBuildLabelColumnWidth", colLastBuildLabel.Width);
            e.Key.SetValue("LastBuildTimeColumnWidth", colLastBuildTime.Width);
            e.Key.SetValue("QueueViewPanelVisible", queueViewPanelVisible);
            e.Key.SetValue("QueueViewSplitterPosition", splitterQueueView.SplitPosition);
            e.Key.SetValue("ProjectViewMode", lvProjects.View.ToString());
        }
        private void CreateController()
        {
            controller = new MainFormController(configuration, this);
            DataBindings.Add("Icon", controller.ProjectStateIconAdaptor, "Icon");
            controller.PopulateImageList(iconList);
            controller.PopulateImageList(largeIconList);
            controller.BindToTrayIcon(trayIcon);
            controller.BindToListView(lvProjects);
            controller.PopulateQueueImageList(queueIconList);
            controller.SetFormTopMost(this);
            controller.SetFormShowInTaskbar(this);
            if (queueViewPanelVisible)
                controller.BindToQueueTreeView(queueTreeView);
            foreach (IProjectMonitor mon in controller.Monitors)
                mon.Polled += mon_Polled;
            btnForceBuild.DataBindings.Add("Enabled", controller, "IsProjectSelected");
            btnStartStopProject.DataBindings.Add("Enabled", controller, "IsProjectSelected");
        }
        protected override void OnLoad(EventArgs e)
        {
            base.OnLoad(e);
            if (!this.DesignMode)
            {
                CreateController();
                controller.StartServerMonitoring();
            }
        }
        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                if (components != null)
                {
                    components.Dispose();
                }
            }
            base.Dispose(disposing);
        }
        private void InitializeComponent()
        {
   this.components = new System.ComponentModel.Container();
   System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
   this.lvProjects = new System.Windows.Forms.ListView();
   this.colProject = new System.Windows.Forms.ColumnHeader();
   this.colServer = new System.Windows.Forms.ColumnHeader();
   this.colActivity = new System.Windows.Forms.ColumnHeader();
   this.colDetail = new System.Windows.Forms.ColumnHeader();
   this.colLastBuildLabel = new System.Windows.Forms.ColumnHeader();
   this.colLastBuildTime = new System.Windows.Forms.ColumnHeader();
   this.colProjectStatus = new System.Windows.Forms.ColumnHeader();
   this.projectContextMenu = new System.Windows.Forms.ContextMenu();
   this.mnuForce = new System.Windows.Forms.MenuItem();
   this.mnuAbort = new System.Windows.Forms.MenuItem();
   this.mnuStart = new System.Windows.Forms.MenuItem();
   this.mnuStop = new System.Windows.Forms.MenuItem();
   this.mnuWebPage = new System.Windows.Forms.MenuItem();
   this.mnuCancelPending = new System.Windows.Forms.MenuItem();
   this.mnuFixBuild = new System.Windows.Forms.MenuItem();
   this.mnuCopyBuildLabel = new System.Windows.Forms.MenuItem();
   this.largeIconList = new System.Windows.Forms.ImageList(this.components);
   this.iconList = new System.Windows.Forms.ImageList(this.components);
   this.mainMenu = new System.Windows.Forms.MainMenu(this.components);
   this.menuFile = new System.Windows.Forms.MenuItem();
   this.mnuFilePreferences = new System.Windows.Forms.MenuItem();
   this.menuItem3 = new System.Windows.Forms.MenuItem();
   this.menuFileExit = new System.Windows.Forms.MenuItem();
   this.mnuView = new System.Windows.Forms.MenuItem();
   this.mnuViewIcons = new System.Windows.Forms.MenuItem();
   this.mnuViewList = new System.Windows.Forms.MenuItem();
   this.mnuViewDetails = new System.Windows.Forms.MenuItem();
   this.mnuAbout = new System.Windows.Forms.MenuItem();
   this.mnuTrayContextMenu = new System.Windows.Forms.ContextMenu();
   this.mnuTraySettings = new System.Windows.Forms.MenuItem();
   this.mnuShow = new System.Windows.Forms.MenuItem();
   this.menuItem5 = new System.Windows.Forms.MenuItem();
   this.mnuTrayExit = new System.Windows.Forms.MenuItem();
   this.pnlButtons = new System.Windows.Forms.Panel();
   this.btnToggleQueueView = new System.Windows.Forms.Button();
   this.btnForceBuild = new System.Windows.Forms.Button();
   this.btnStartStopProject = new System.Windows.Forms.Button();
   this.splitterQueueView = new System.Windows.Forms.Splitter();
   this.pnlViewQueues = new System.Windows.Forms.Panel();
   this.queueTreeView = new ThoughtWorks.CruiseControl.CCTrayLib.Presentation.QueueTreeView();
   this.queueIconList = new System.Windows.Forms.ImageList(this.components);
   this.queueContextMenu = new System.Windows.Forms.ContextMenu();
   this.mnuQueueCancelPending = new System.Windows.Forms.MenuItem();
   this.tltBuildStage = new System.Windows.Forms.ToolTip(this.components);
   this.trayIcon = new ThoughtWorks.CruiseControl.CCTrayLib.Presentation.TrayIcon();
   this.pnlButtons.SuspendLayout();
   this.pnlViewQueues.SuspendLayout();
   this.SuspendLayout();
   this.lvProjects.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.colProject,
            this.colServer,
            this.colActivity,
            this.colDetail,
            this.colLastBuildLabel,
            this.colLastBuildTime,
            this.colProjectStatus});
   this.lvProjects.ContextMenu = this.projectContextMenu;
   this.lvProjects.Dock = System.Windows.Forms.DockStyle.Fill;
   this.lvProjects.FullRowSelect = true;
   this.lvProjects.HideSelection = false;
   this.lvProjects.LargeImageList = this.largeIconList;
   this.lvProjects.Location = new System.Drawing.Point(203, 0);
   this.lvProjects.MultiSelect = false;
   this.lvProjects.Name = "lvProjects";
   this.lvProjects.Size = new System.Drawing.Size(689, 93);
   this.lvProjects.SmallImageList = this.iconList;
   this.lvProjects.TabIndex = 0;
   this.lvProjects.UseCompatibleStateImageBehavior = false;
   this.lvProjects.View = System.Windows.Forms.View.Details;
   this.lvProjects.SelectedIndexChanged += new System.EventHandler(this.lvProjects_SelectedIndexChanged);
   this.lvProjects.DoubleClick += new System.EventHandler(this.lvProjects_DoubleClick);
   this.lvProjects.ColumnClick += new System.Windows.Forms.ColumnClickEventHandler(this.lvProjects_ColumnClick);
   this.colProject.Text = "Project";
   this.colProject.Width = 160;
   this.colServer.Text = "Server";
   this.colServer.Width = 100;
   this.colActivity.Text = "Activity";
   this.colActivity.Width = 132;
   this.colDetail.Text = "Detail";
   this.colDetail.Width = 282;
   this.colLastBuildLabel.Text = "Last Build Label";
   this.colLastBuildLabel.Width = 192;
   this.colLastBuildTime.Text = "Last Build Time";
   this.colLastBuildTime.Width = 112;
   this.colProjectStatus.Text = "Project Status";
   this.projectContextMenu.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.mnuForce,
            this.mnuAbort,
            this.mnuStart,
            this.mnuStop,
            this.mnuWebPage,
            this.mnuCancelPending,
            this.mnuFixBuild,
            this.mnuCopyBuildLabel});
   this.projectContextMenu.Popup += new System.EventHandler(this.projectContextMenu_Popup);
   this.mnuForce.Index = 0;
   this.mnuForce.Text = "&Force Build";
   this.mnuForce.Click += new System.EventHandler(this.mnuForce_Click);
   this.mnuAbort.Index = 1;
   this.mnuAbort.Text = "&Abort Build";
   this.mnuAbort.Click += new System.EventHandler(this.mnuAbort_Click);
   this.mnuStart.Index = 2;
   this.mnuStart.Text = "&Start Project";
   this.mnuStart.Click += new System.EventHandler(this.mnuStart_Click);
   this.mnuStop.Index = 3;
   this.mnuStop.Text = "&Stop Project";
   this.mnuStop.Click += new System.EventHandler(this.mnuStop_Click);
   this.mnuWebPage.Index = 4;
   this.mnuWebPage.Text = "Display &Web Page";
   this.mnuWebPage.Click += new System.EventHandler(this.mnuWebPage_Click);
   this.mnuCancelPending.Index = 5;
   this.mnuCancelPending.Text = "&Cancel Pending";
   this.mnuCancelPending.Click += new System.EventHandler(this.mnuCancelPending_Click);
   this.mnuFixBuild.Index = 6;
   this.mnuFixBuild.Text = "&Volunteer to Fix Build";
   this.mnuFixBuild.Click += new System.EventHandler(this.mnuFixBuild_Click);
   this.mnuCopyBuildLabel.Index = 7;
   this.mnuCopyBuildLabel.Text = "Copy Build &Label";
   this.mnuCopyBuildLabel.Click += new System.EventHandler(this.mnuCopyBuildLabel_Click);
   this.largeIconList.ColorDepth = System.Windows.Forms.ColorDepth.Depth8Bit;
   this.largeIconList.ImageSize = new System.Drawing.Size(32, 32);
   this.largeIconList.TransparentColor = System.Drawing.Color.Transparent;
   this.iconList.ColorDepth = System.Windows.Forms.ColorDepth.Depth32Bit;
   this.iconList.ImageSize = new System.Drawing.Size(16, 16);
   this.iconList.TransparentColor = System.Drawing.Color.Transparent;
   this.mainMenu.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.menuFile,
            this.mnuView,
            this.mnuAbout});
   this.menuFile.Index = 0;
   this.menuFile.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.mnuFilePreferences,
            this.menuItem3,
            this.menuFileExit});
   this.menuFile.Text = "&File";
   this.mnuFilePreferences.Index = 0;
   this.mnuFilePreferences.Text = "&Settings...";
   this.mnuFilePreferences.Click += new System.EventHandler(this.mnuFilePreferences_Click);
   this.menuItem3.Index = 1;
   this.menuItem3.Text = "-";
   this.menuFileExit.Index = 2;
   this.menuFileExit.Text = "E&xit";
   this.menuFileExit.Click += new System.EventHandler(this.menuFileExit_Click);
   this.mnuView.Index = 1;
   this.mnuView.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.mnuViewIcons,
            this.mnuViewList,
            this.mnuViewDetails});
   this.mnuView.Text = "&View";
   this.mnuView.Popup += new System.EventHandler(this.mnuView_Popup);
   this.mnuViewIcons.Index = 0;
   this.mnuViewIcons.Text = "&Icons";
   this.mnuViewIcons.Click += new System.EventHandler(this.mnuViewIcons_Click);
   this.mnuViewList.Index = 1;
   this.mnuViewList.Text = "&List";
   this.mnuViewList.Click += new System.EventHandler(this.mnuViewList_Click);
   this.mnuViewDetails.Index = 2;
   this.mnuViewDetails.Text = "&Details";
   this.mnuViewDetails.Click += new System.EventHandler(this.mnuViewDetails_Click);
   this.mnuAbout.Index = 2;
   this.mnuAbout.Text = "&About";
   this.mnuAbout.Click += new System.EventHandler(this.mnuAbout_Click);
   this.mnuTrayContextMenu.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.mnuTraySettings,
            this.mnuShow,
            this.menuItem5,
            this.mnuTrayExit});
   this.mnuTraySettings.Index = 0;
   this.mnuTraySettings.Text = "&Settings...";
   this.mnuTraySettings.Click += new System.EventHandler(this.mnuFilePreferences_Click);
   this.mnuShow.DefaultItem = true;
   this.mnuShow.Index = 1;
   this.mnuShow.Text = "Show Status &Window";
   this.mnuShow.Click += new System.EventHandler(this.mnuShow_Click);
   this.menuItem5.Index = 2;
   this.menuItem5.Text = "-";
   this.mnuTrayExit.Index = 3;
   this.mnuTrayExit.Text = "&Exit";
   this.mnuTrayExit.Click += new System.EventHandler(this.menuFileExit_Click);
   this.pnlButtons.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
   this.pnlButtons.Controls.Add(this.btnToggleQueueView);
   this.pnlButtons.Controls.Add(this.btnForceBuild);
   this.pnlButtons.Controls.Add(this.btnStartStopProject);
   this.pnlButtons.Dock = System.Windows.Forms.DockStyle.Bottom;
   this.pnlButtons.Location = new System.Drawing.Point(0, 93);
   this.pnlButtons.Name = "pnlButtons";
   this.pnlButtons.Size = new System.Drawing.Size(892, 45);
   this.pnlButtons.TabIndex = 1;
   this.btnToggleQueueView.Location = new System.Drawing.Point(105, 10);
   this.btnToggleQueueView.Name = "btnToggleQueueView";
   this.btnToggleQueueView.Size = new System.Drawing.Size(85, 23);
   this.btnToggleQueueView.TabIndex = 1;
   this.btnToggleQueueView.Text = "Show &Queues";
   this.btnToggleQueueView.Click += new System.EventHandler(this.btnToggleQueueView_Click);
   this.btnForceBuild.Location = new System.Drawing.Point(10, 10);
   this.btnForceBuild.Name = "btnForceBuild";
   this.btnForceBuild.Size = new System.Drawing.Size(85, 23);
   this.btnForceBuild.TabIndex = 0;
   this.btnForceBuild.Text = "Force &Build";
   this.btnForceBuild.Click += new System.EventHandler(this.btnForceBuild_Click);
   this.btnStartStopProject.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
   this.btnStartStopProject.FlatStyle = System.Windows.Forms.FlatStyle.System;
   this.btnStartStopProject.Location = new System.Drawing.Point(793, 10);
   this.btnStartStopProject.Name = "btnStartStopProject";
   this.btnStartStopProject.Size = new System.Drawing.Size(85, 23);
   this.btnStartStopProject.TabIndex = 2;
   this.btnStartStopProject.Text = "&Stop Project";
   this.btnStartStopProject.UseVisualStyleBackColor = true;
   this.btnStartStopProject.Click += new System.EventHandler(this.btnStartStopProject_Click);
   this.splitterQueueView.Location = new System.Drawing.Point(200, 0);
   this.splitterQueueView.Name = "splitterQueueView";
   this.splitterQueueView.Size = new System.Drawing.Size(3, 93);
   this.splitterQueueView.TabIndex = 3;
   this.splitterQueueView.TabStop = false;
   this.splitterQueueView.Visible = false;
   this.pnlViewQueues.Controls.Add(this.queueTreeView);
   this.pnlViewQueues.Dock = System.Windows.Forms.DockStyle.Left;
   this.pnlViewQueues.Location = new System.Drawing.Point(0, 0);
   this.pnlViewQueues.Name = "pnlViewQueues";
   this.pnlViewQueues.Size = new System.Drawing.Size(200, 93);
   this.pnlViewQueues.TabIndex = 4;
   this.pnlViewQueues.Visible = false;
   this.queueTreeView.Dock = System.Windows.Forms.DockStyle.Fill;
   this.queueTreeView.ImageIndex = 0;
   this.queueTreeView.ImageList = this.queueIconList;
   this.queueTreeView.Location = new System.Drawing.Point(0, 0);
   this.queueTreeView.Name = "queueTreeView";
   this.queueTreeView.SelectedImageIndex = 0;
   this.queueTreeView.Size = new System.Drawing.Size(200, 93);
   this.queueTreeView.TabIndex = 2;
   this.queueTreeView.MouseUp += new System.Windows.Forms.MouseEventHandler(this.queueTreeView_MouseUp);
   this.queueIconList.ColorDepth = System.Windows.Forms.ColorDepth.Depth32Bit;
   this.queueIconList.ImageSize = new System.Drawing.Size(16, 16);
   this.queueIconList.TransparentColor = System.Drawing.Color.Transparent;
   this.queueContextMenu.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.mnuQueueCancelPending});
   this.mnuQueueCancelPending.Index = 0;
   this.mnuQueueCancelPending.Text = "&Cancel Pending";
   this.mnuQueueCancelPending.Click += new System.EventHandler(this.mnuQueueCancelPending_Click);
   this.trayIcon.ContextMenu = this.mnuTrayContextMenu;
   this.trayIcon.Icon = null;
   this.trayIcon.Text = "CruiseControl.NET";
   this.trayIcon.Visible = true;
   this.trayIcon.DoubleClick += new System.EventHandler(this.trayIcon_DoubleClick);
   this.trayIcon.Click += new System.EventHandler(this.trayIcon_Click);
   this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
   this.ClientSize = new System.Drawing.Size(892, 138);
   this.Controls.Add(this.lvProjects);
   this.Controls.Add(this.splitterQueueView);
   this.Controls.Add(this.pnlViewQueues);
   this.Controls.Add(this.pnlButtons);
   this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
   this.MaximizeBox = false;
   this.Menu = this.mainMenu;
   this.MinimizeBox = false;
   this.Name = "MainForm";
   this.StartPosition = System.Windows.Forms.FormStartPosition.Manual;
   this.Text = "CCTray ";
   this.Closing += new System.ComponentModel.CancelEventHandler(this.MainForm_Closing);
   this.pnlButtons.ResumeLayout(false);
   this.pnlViewQueues.ResumeLayout(false);
   this.ResumeLayout(false);
        }
        private void menuFileExit_Click(object sender, EventArgs e)
        {
            systemShutdownInProgress = true;
            Close();
        }
        private void lvProjects_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (lvProjects.SelectedItems.Count == 0)
            {
                controller.SelectedProject = null;
                tltBuildStage.RemoveAll();
            }
            else
            {
                controller.SelectedProject = (IProjectMonitor)lvProjects.SelectedItems[0].Tag;
                tltBuildStage.SetToolTip(this.lvProjects, GetBuildStage());
                System.Threading.Timer t =
                    new System.Threading.Timer(new System.Threading.TimerCallback(PollProject), controller.SelectedProject, 0,
                        System.Threading.Timeout.Infinite);
            }
        }
        private void PollProject(object obj)
        {
            IProjectMonitor projectMon = (IProjectMonitor)obj;
            projectMon.Poll();
        }
        private string GetBuildStage()
        {
            if (!controller.SelectedProject.Detail.IsConnected)
            { return ""; }
            if (controller.SelectedProject.ProjectState != ProjectState.Building &&
                controller.SelectedProject.ProjectState != ProjectState.BrokenAndBuilding)
            { return ""; }
            String currentBuildStage = controller.SelectedProject.Detail.CurrentBuildStage;
            if (currentBuildStage == null || currentBuildStage.Length == 0)
            { return ""; }
            System.Text.StringBuilder SB = new System.Text.StringBuilder();
            System.IO.StringWriter BuildStage = new System.IO.StringWriter(SB);
            try
            {
                System.Xml.XmlTextReader XReader;
                System.Xml.XmlDocument XDoc = new System.Xml.XmlDocument();
                XDoc.LoadXml(controller.SelectedProject.Detail.CurrentBuildStage);
                XReader = new System.Xml.XmlTextReader(XDoc.OuterXml, System.Xml.XmlNodeType.Document, null);
                XReader.WhitespaceHandling = System.Xml.WhitespaceHandling.None;
                while (XReader.Read())
                {
                    XReader.MoveToContent();
                    if (XReader.AttributeCount > 0)
                    {
                        BuildStage.WriteLine("{0}  {1}", XReader.GetAttribute("Time"), XReader.GetAttribute("Data"));
                    }
                }
            }
            catch
            {
                BuildStage = new System.IO.StringWriter();
            }
            return BuildStage.ToString();
        }
        private void mnuForce_Click(object sender, EventArgs e)
        {
            controller.ForceBuild();
        }
        private void mnuAbort_Click(object sender, EventArgs e)
        {
            controller.AbortBuild();
        }
        private void mnuCopyBuildLabel_Click(object sender, EventArgs e)
        {
            controller.CopyBuildLabel();
        }
        private void mnuCancelPending_Click(object sender, EventArgs e)
        {
            controller.CancelPending();
        }
        private void btnForceBuild_Click(object sender, EventArgs e)
        {
            if (!controller.IsProjectBuilding)
            {
                controller.ForceBuild();
            }
            else
            {
                controller.AbortBuild();
            }
        }
        private void UpdateForceAbortBuildButtonLabel()
        {
            btnForceBuild.Text = controller.IsProjectBuilding ? "Abort &Build" : "Force &Build";
            btnForceBuild.Enabled = ((controller.SelectedProject != null) && controller.SelectedProject.IsConnected);
        }
        private void mnuWebPage_Click(object sender, EventArgs e)
        {
            controller.DisplayWebPage();
        }
        private void lvProjects_DoubleClick(object sender, EventArgs e)
        {
            controller.DisplayWebPage();
        }
        private void mnuViewIcons_Click(object sender, EventArgs e)
        {
            lvProjects.View = View.LargeIcon;
        }
        private void mnuViewList_Click(object sender, EventArgs e)
        {
            lvProjects.View = View.List;
        }
        private void mnuViewDetails_Click(object sender, EventArgs e)
        {
            lvProjects.View = View.Details;
        }
        private void mnuAbout_Click(object sender, EventArgs e)
        {
            MessageBox.Show(string.Format("CCTray version {0}",
                                          Assembly.GetExecutingAssembly().GetName().Version
                                         ),
                            "CCTray Version"
                            );
        }
        private void projectContextMenu_Popup(object sender, EventArgs e)
        {
            mnuForce.Visible = controller.IsProjectSelected && !controller.IsProjectBuilding;
            mnuAbort.Visible = controller.IsProjectSelected && controller.IsProjectBuilding;
            mnuStart.Visible = controller.IsProjectSelected && !controller.IsProjectRunning;
            mnuStop.Visible = controller.IsProjectSelected && controller.IsProjectRunning;
            mnuWebPage.Enabled = controller.IsProjectSelected;
            mnuCancelPending.Visible = controller.CanCancelPending();
            mnuFixBuild.Visible = controller.CanFixBuild();
            mnuCopyBuildLabel.Visible = controller.IsProjectSelected;
        }
        private void mnuFilePreferences_Click(object sender, EventArgs e)
        {
            ShowPreferencesForm();
        }
        private void ShowPreferencesForm()
        {
            controller.StopServerMonitoring();
            try
            {
                if (new CCTrayMultiSettingsForm(configuration).ShowDialog() != DialogResult.OK)
                    return;
                configuration.Reload();
                lvProjects.Items.Clear();
                DataBindings.Clear();
                btnForceBuild.DataBindings.Clear();
                btnStartStopProject.DataBindings.Clear();
                controller.UnbindToQueueTreeView(queueTreeView);
                MainFormController oldController = controller;
                CreateController();
                oldController.ProjectStateIconProvider.Dispose();
            }
            finally
            {
                controller.StartServerMonitoring();
            }
        }
        private void mnuView_Popup(object sender, EventArgs e)
        {
            mnuViewIcons.Checked = (lvProjects.View == View.LargeIcon);
            mnuViewList.Checked = (lvProjects.View == View.List);
            mnuViewDetails.Checked = (lvProjects.View == View.Details);
        }
        private void mnuShow_Click(object sender, EventArgs e)
        {
            ShowStatusWindow();
        }
        private void trayIcon_Click(object sender, EventArgs e)
        {
            if (Visible)
                NativeMethods.SetForegroundWindow(Handle);
        }
        private void trayIcon_DoubleClick(object sender, EventArgs e)
        {
            if (!controller.OnDoubleClick())
            {
                ShowStatusWindow();
            }
        }
        private void ShowStatusWindow()
        {
            WindowState = FormWindowState.Normal;
            Show();
            NativeMethods.SetForegroundWindow(Handle);
        }
        protected override void WndProc(ref Message m)
        {
            if (m.Msg == WM_QUERYENDSESSION)
            {
                systemShutdownInProgress = true;
            }
            base.WndProc(ref m);
        }
        private void MainForm_Closing(object sender, CancelEventArgs e)
        {
            if (systemShutdownInProgress)
            {
                systemShutdownInProgress = false;
                e.Cancel = false;
            }
            else
            {
                e.Cancel = true;
                Hide();
            }
        }
        private void lvProjects_ColumnClick(object sender, ColumnClickEventArgs e)
        {
            ListViewItemComparer compare = lvProjects.ListViewItemSorter as ListViewItemComparer;
            if (compare == null)
            {
                lvProjects.ListViewItemSorter = new ListViewItemComparer(e.Column, true);
            }
            else
            {
                if (compare.SortColumn == e.Column)
                {
                    compare.SortAscending = !compare.SortAscending;
                }
                else
                {
                    compare.SortAscending = false;
                    compare.SortColumn = e.Column;
                }
            }
            lvProjects.Sort();
        }
        private void mnuFixBuild_Click(object sender, EventArgs e)
        {
            controller.VolunteerToFixBuild();
        }
        private void btnToggleQueueView_Click(object sender, EventArgs e)
        {
            queueViewPanelVisible = !queueViewPanelVisible;
            splitterQueueView.Visible = queueViewPanelVisible;
            pnlViewQueues.Visible = queueViewPanelVisible;
            queueTreeView.Visible = queueViewPanelVisible;
            UpdateViewQueuesButtonLabel();
            if (queueViewPanelVisible)
                controller.BindToQueueTreeView(queueTreeView);
            else
                controller.UnbindToQueueTreeView(queueTreeView);
        }
        private void queueTreeView_MouseUp(object sender, MouseEventArgs e)
        {
            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
                Point clickPoint = new Point(e.X, e.Y);
                TreeNode clickNode = queueTreeView.GetNodeAt(clickPoint);
                if (clickNode != null)
                {
                    queueTreeView.SelectedNode = clickNode;
                    IntegrationQueueTreeNodeTag tag = clickNode.Tag as IntegrationQueueTreeNodeTag;
                    if (tag != null && tag.IsQueuedItemNode)
                    {
                        mnuQueueCancelPending.Enabled = !tag.IsFirstItemOnQueue;
                        queueContextMenu.Show(queueTreeView, clickPoint);
                    }
                }
            }
        }
        private void mnuQueueCancelPending_Click(object sender, EventArgs e)
        {
            if (queueTreeView.SelectedNode == null)
                return;
            IntegrationQueueTreeNodeTag tag = queueTreeView.SelectedNode.Tag as IntegrationQueueTreeNodeTag;
            if (tag == null || tag.QueuedRequestSnapshot == null)
                return;
            controller.CancelPendingProjectByName(tag.QueuedRequestSnapshot.ProjectName);
        }
        private void UpdateViewQueuesButtonLabel()
        {
            btnToggleQueueView.Text = (queueViewPanelVisible) ? "Hide &Queues" : "Show &Queues";
        }
        private void mon_Polled(object sender, MonitorPolledEventArgs args)
        {
            UpdateForceAbortBuildButtonLabel();
            UpdateStartStopProjectButtonLabel();
        }
        private void btnStartStopProject_Click(object sender, EventArgs e)
        {
            bool projectRunning = controller.IsProjectRunning;
            if (projectRunning)
            {
                controller.StopProject();
            }
            else
            {
                controller.StartProject();
            }
        }
        private void mnuStart_Click(object sender, EventArgs e)
        {
            controller.StartProject();
        }
        private void mnuStop_Click(object sender, EventArgs e)
        {
            controller.StopProject();
        }
        private void UpdateStartStopProjectButtonLabel()
        {
            btnStartStopProject.Text = controller.IsProjectRunning ? "&Stop Project" : "&Start Project";
            btnStartStopProject.Enabled = ((controller.SelectedProject != null) && controller.SelectedProject.IsConnected);
        }
        private class ListViewItemComparer : IComparer
        {
            private static string[] _columnSortTypes = new string[] { "string", "string", "string", "string", "int", "datetime", "string" };
            private int col;
            private bool ascendingOrder;
            public int SortColumn
            {
                get { return col; }
                set { col = value; }
            }
            public bool SortAscending
            {
                get { return ascendingOrder; }
                set { ascendingOrder = value; }
            }
            public ListViewItemComparer()
                : this(0, true)
            {
            }
            public ListViewItemComparer(int column)
                : this(column, true)
            {
            }
            public ListViewItemComparer(int column, bool ascending)
            {
                SortColumn = column;
                SortAscending = ascending;
            }
            public int Compare(object x, object y)
            {
                int compare = 0;
                switch (_columnSortTypes[col])
                {
                    case "int":
                        int xValue = 0;
                        int yValue = 0;
                        if (int.TryParse(((ListViewItem)x).SubItems[SortColumn].Text, out xValue) && int.TryParse(((ListViewItem)y).SubItems[SortColumn].Text, out yValue))
                        {
                            if (xValue < yValue)
                            {
                                compare = -1;
                            }
                            else
                            {
                                if (xValue > yValue)
                                {
                                    compare = 1;
                                }
                                else
                                {
                                    compare = 0;
                                }
                            }
                        }
                        break;
                    case "datetime":
                        DateTime xDateTime = DateTime.MinValue;
                        DateTime yDateTime = DateTime.MinValue;
                        if (DateTime.TryParse(((ListViewItem)x).SubItems[SortColumn].Text, out xDateTime) && DateTime.TryParse(((ListViewItem)y).SubItems[SortColumn].Text, out yDateTime))
                        {
                            compare = DateTime.Compare(xDateTime, yDateTime);
                        }
                        break;
                    default:
                        compare = string.Compare(((ListViewItem)x).SubItems[SortColumn].Text, ((ListViewItem)y).SubItems[SortColumn].Text);
                        break;
                }
                if (!ascendingOrder)
                {
                    compare = -compare;
                }
                return compare;
            }
        }
    }
}
