using System;
using System.Drawing;
using System.Windows.Forms;
using System.ComponentModel;
using System.ComponentModel.Design;
using System.Runtime.InteropServices;
namespace WeifenLuo.WinFormsUI.Docking
{
    partial class DockPanel
    {
        private class MdiClientController : NativeWindow, IComponent, IDisposable
        {
            private bool m_autoScroll = true;
            private BorderStyle m_borderStyle = BorderStyle.Fixed3D;
            private MdiClient m_mdiClient = null;
            private Form m_parentForm = null;
            private ISite m_site = null;
            public MdiClientController()
            {
            }
            public void Dispose()
            {
                Dispose(true);
                GC.SuppressFinalize(this);
            }
            protected virtual void Dispose(bool disposing)
            {
                if (disposing)
                {
                    lock (this)
                    {
                        if (Site != null && Site.Container != null)
                            Site.Container.Remove(this);
                        if (Disposed != null)
                            Disposed(this, EventArgs.Empty);
                    }
                }
            }
            public bool AutoScroll
            {
                get { return m_autoScroll; }
                set
                {
                    m_autoScroll = value;
                    if (MdiClient != null)
                        UpdateStyles();
                }
            }
            public BorderStyle BorderStyle
            {
                set
                {
                    if (!Enum.IsDefined(typeof(BorderStyle), value))
                        throw new InvalidEnumArgumentException();
                    m_borderStyle = value;
                    if (MdiClient == null)
                        return;
                    if (Site != null && Site.DesignMode)
                        return;
                    int style = NativeMethods.GetWindowLong(MdiClient.Handle, (int)Win32.GetWindowLongIndex.GWL_STYLE);
                    int exStyle = NativeMethods.GetWindowLong(MdiClient.Handle, (int)Win32.GetWindowLongIndex.GWL_EXSTYLE);
                    switch (m_borderStyle)
                    {
                        case BorderStyle.Fixed3D:
                            exStyle |= (int)Win32.WindowExStyles.WS_EX_CLIENTEDGE;
                            style &= ~((int)Win32.WindowStyles.WS_BORDER);
                            break;
                        case BorderStyle.FixedSingle:
                            exStyle &= ~((int)Win32.WindowExStyles.WS_EX_CLIENTEDGE);
                            style |= (int)Win32.WindowStyles.WS_BORDER;
                            break;
                        case BorderStyle.None:
                            style &= ~((int)Win32.WindowStyles.WS_BORDER);
                            exStyle &= ~((int)Win32.WindowExStyles.WS_EX_CLIENTEDGE);
                            break;
                    }
                    NativeMethods.SetWindowLong(MdiClient.Handle, (int)Win32.GetWindowLongIndex.GWL_STYLE, style);
                    NativeMethods.SetWindowLong(MdiClient.Handle, (int)Win32.GetWindowLongIndex.GWL_EXSTYLE, exStyle);
                    UpdateStyles();
                }
            }
            public MdiClient MdiClient
            {
                get { return m_mdiClient; }
            }
            [Browsable(false)]
            public Form ParentForm
            {
                get { return m_parentForm; }
                set
                {
                    if (m_parentForm != null)
                    {
                        m_parentForm.HandleCreated -= new EventHandler(ParentFormHandleCreated);
                        m_parentForm.MdiChildActivate -= new EventHandler(ParentFormMdiChildActivate);
                    }
                    m_parentForm = value;
                    if (m_parentForm == null)
                        return;
                    if (m_parentForm.IsHandleCreated)
                    {
                        InitializeMdiClient();
                        RefreshProperties();
                    }
                    else
                        m_parentForm.HandleCreated += new EventHandler(ParentFormHandleCreated);
                    m_parentForm.MdiChildActivate += new EventHandler(ParentFormMdiChildActivate);
                }
            }
            public ISite Site
            {
                get { return m_site; }
                set
                {
                    m_site = value;
                    if (m_site == null)
                        return;
                    IDesignerHost host = (value.GetService(typeof(IDesignerHost)) as IDesignerHost);
                    if (host != null)
                    {
                        Form parent = host.RootComponent as Form;
                        if (parent != null)
                            ParentForm = parent;
                    }
                }
            }
            public void RenewMdiClient()
            {
                InitializeMdiClient();
                RefreshProperties();
            }
            public event EventHandler Disposed;
            public event EventHandler HandleAssigned;
            public event EventHandler MdiChildActivate;
            public event LayoutEventHandler Layout;
            protected virtual void OnHandleAssigned(EventArgs e)
            {
                if (HandleAssigned != null)
                    HandleAssigned(this, e);
            }
            protected virtual void OnMdiChildActivate(EventArgs e)
            {
                if (MdiChildActivate != null)
                    MdiChildActivate(this, e);
            }
            protected virtual void OnLayout(LayoutEventArgs e)
            {
                if (Layout != null)
                    Layout(this, e);
            }
            public event PaintEventHandler Paint;
            protected virtual void OnPaint(PaintEventArgs e)
            {
                if (Paint != null)
                    Paint(this, e);
            }
            protected override void WndProc(ref Message m)
            {
                switch (m.Msg)
                {
                    case (int)Win32.Msgs.WM_NCCALCSIZE:
                        if (!AutoScroll)
                            NativeMethods.ShowScrollBar(m.HWnd, (int)Win32.ScrollBars.SB_BOTH, 0 );
                        break;
                }
                base.WndProc(ref m);
            }
            private void ParentFormHandleCreated(object sender, EventArgs e)
            {
                this.m_parentForm.HandleCreated -= new EventHandler(ParentFormHandleCreated);
                InitializeMdiClient();
                RefreshProperties();
            }
            private void ParentFormMdiChildActivate(object sender, EventArgs e)
            {
                OnMdiChildActivate(e);
            }
            private void MdiClientLayout(object sender, LayoutEventArgs e)
            {
                OnLayout(e);
            }
            private void MdiClientHandleDestroyed(object sender, EventArgs e)
            {
                if (m_mdiClient != null)
                {
                    m_mdiClient.HandleDestroyed -= new EventHandler(MdiClientHandleDestroyed);
                    m_mdiClient = null;
                }
                ReleaseHandle();
            }
            private void InitializeMdiClient()
            {
                if (MdiClient != null)
                {
                    MdiClient.HandleDestroyed -= new EventHandler(MdiClientHandleDestroyed);
                    MdiClient.Layout -= new LayoutEventHandler(MdiClientLayout);
                }
                if (ParentForm == null)
                    return;
                foreach (Control control in ParentForm.Controls)
                {
                    m_mdiClient = control as MdiClient;
                    if (m_mdiClient == null)
                        continue;
                    ReleaseHandle();
                    AssignHandle(MdiClient.Handle);
                    OnHandleAssigned(EventArgs.Empty);
                    MdiClient.HandleDestroyed += new EventHandler(MdiClientHandleDestroyed);
                    MdiClient.Layout += new LayoutEventHandler(MdiClientLayout);
                    break;
                }
            }
            private void RefreshProperties()
            {
                BorderStyle = m_borderStyle;
                AutoScroll = m_autoScroll;
            }
            private void UpdateStyles()
            {
                NativeMethods.SetWindowPos(MdiClient.Handle, IntPtr.Zero, 0, 0, 0, 0,
                    Win32.FlagsSetWindowPos.SWP_NOACTIVATE |
                    Win32.FlagsSetWindowPos.SWP_NOMOVE |
                    Win32.FlagsSetWindowPos.SWP_NOSIZE |
                    Win32.FlagsSetWindowPos.SWP_NOZORDER |
                    Win32.FlagsSetWindowPos.SWP_NOOWNERZORDER |
                    Win32.FlagsSetWindowPos.SWP_FRAMECHANGED);
            }
        }
        private MdiClientController m_mdiClientController = null;
        private MdiClientController GetMdiClientController()
        {
            if (m_mdiClientController == null)
            {
                m_mdiClientController = new MdiClientController();
                m_mdiClientController.HandleAssigned += new EventHandler(MdiClientHandleAssigned);
                m_mdiClientController.MdiChildActivate += new EventHandler(ParentFormMdiChildActivate);
                m_mdiClientController.Layout += new LayoutEventHandler(MdiClient_Layout);
            }
            return m_mdiClientController;
        }
        private void ParentFormMdiChildActivate(object sender, EventArgs e)
        {
            if (GetMdiClientController().ParentForm == null)
                return;
            IDockContent content = GetMdiClientController().ParentForm.ActiveMdiChild as IDockContent;
            if (content == null)
                return;
            if (content.DockHandler.DockPanel == this && content.DockHandler.Pane != null)
                content.DockHandler.Pane.ActiveContent = content;
        }
        private bool MdiClientExists
        {
            get { return GetMdiClientController().MdiClient != null; }
        }
        private void SetMdiClientBounds(Rectangle bounds)
        {
            GetMdiClientController().MdiClient.Bounds = bounds;
        }
        private void SuspendMdiClientLayout()
        {
            if (GetMdiClientController().MdiClient != null)
                GetMdiClientController().MdiClient.PerformLayout();
        }
        private void ResumeMdiClientLayout(bool perform)
        {
            if (GetMdiClientController().MdiClient != null)
                GetMdiClientController().MdiClient.ResumeLayout(perform);
        }
        private void PerformMdiClientLayout()
        {
            if (GetMdiClientController().MdiClient != null)
                GetMdiClientController().MdiClient.PerformLayout();
        }
        private void SetMdiClient()
        {
            MdiClientController controller = GetMdiClientController();
            if (this.DocumentStyle == DocumentStyle.DockingMdi)
            {
                controller.AutoScroll = false;
                controller.BorderStyle = BorderStyle.None;
                if (MdiClientExists)
                    controller.MdiClient.Dock = DockStyle.Fill;
            }
            else if (DocumentStyle == DocumentStyle.DockingSdi || DocumentStyle == DocumentStyle.DockingWindow)
            {
                controller.AutoScroll = true;
                controller.BorderStyle = BorderStyle.Fixed3D;
                if (MdiClientExists)
                    controller.MdiClient.Dock = DockStyle.Fill;
            }
            else if (this.DocumentStyle == DocumentStyle.SystemMdi)
            {
                controller.AutoScroll = true;
                controller.BorderStyle = BorderStyle.Fixed3D;
                if (controller.MdiClient != null)
                {
                    controller.MdiClient.Dock = DockStyle.None;
                    controller.MdiClient.Bounds = SystemMdiClientBounds;
                }
            }
        }
        internal Rectangle RectangleToMdiClient(Rectangle rect)
        {
            if (MdiClientExists)
                return GetMdiClientController().MdiClient.RectangleToClient(rect);
            else
                return Rectangle.Empty;
        }
    }
}
