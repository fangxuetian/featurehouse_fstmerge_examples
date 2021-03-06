using System;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Windows.Forms;
namespace IEControl
{
 public class DocHostUIHandler:
  Interop.IDocHostUIHandler,
  Interop.IOleClientSite,
  Interop.IOleContainer,
  Interop.IOleInPlaceSite,
  Interop.IAdviseSink
 {
  private HtmlControl hostControl;
  public DocHostUIHandler(HtmlControl hostControl)
  {
   if ((hostControl == null) || (hostControl.IsHandleCreated == false)) {
    throw new ArgumentNullException("hostControl");
   }
   this.hostControl = hostControl;
  }
  [DispId(-5512)]
  public int setFlags() {
   int flags = 0;
   if (!this.hostControl.ActiveXEnabled) {
    flags |= Interop.DLCTL_NO_DLACTIVEXCTLS | Interop.DLCTL_NO_RUNACTIVEXCTLS;
   }
   if (this.hostControl.SilentModeEnabled) {
    flags |= Interop.DLCTL_SILENT;
   }
   if (!this.hostControl.ScriptEnabled) {
    flags |= Interop.DLCTL_NO_SCRIPTS;
   }
   if (!this.hostControl.JavaEnabled) {
    flags |= Interop.DLCTL_NO_JAVA;
   }
   if (this.hostControl.ImagesDownloadEnabled) {
    flags |= Interop.DLCTL_DLIMAGES;
   }
   if (this.hostControl.BackroundSoundEnabled) {
    flags |= Interop.DLCTL_BGSOUNDS;
   }
   if (this.hostControl.VideoEnabled) {
    flags |= Interop.DLCTL_VIDEOS;
   }
   if (!this.hostControl.ClientPullEnabled) {
    flags |= Interop.DLCTL_NO_CLIENTPULL;
   }
   if (!this.hostControl.FrameDownloadEnabled) {
    flags |= Interop.DLCTL_NO_FRAMEDOWNLOAD;
   }
   if (!this.hostControl.BehaviorsExecuteEnabled) {
    flags |= Interop.DLCTL_NO_BEHAVIORS;
   }
   return flags;
  }
  public int ShowContextMenu(int dwID, Interop.POINT pt, object pcmdtReserved, object pdispReserved) {
   int ret = Interop.S_FALSE;
   Point location = hostControl.PointToClient(new Point(pt.x, pt.y));
   BrowserContextMenuCancelEventArgs e = new BrowserContextMenuCancelEventArgs(location, false);
   try {
    hostControl.RaiseOnShowContextMenu(e);
   }
   catch {}
   finally {
    if (e.Cancel)
     ret = Interop.S_OK;
   }
   return ret;
  }
  public int GetHostInfo(Interop.DOCHOSTUIINFO info) {
   info.dwDoubleClick = Interop.DOCHOSTUIDBLCLICK_DEFAULT;
   int flags = 0;
   if (hostControl.AllowInPlaceNavigation) {
    flags |= Interop.DOCHOSTUIFLAG_ENABLE_INPLACE_NAVIGATION;
   }
   if (!hostControl.Border3d) {
    flags |= Interop.DOCHOSTUIFLAG_NO3DBORDER;
   }
   if (!hostControl.ScriptEnabled) {
    flags |= Interop.DOCHOSTUIFLAG_DISABLE_SCRIPT_INACTIVE;
   }
   if (!hostControl.ScrollBarsEnabled) {
    flags |= Interop.DOCHOSTUIFLAG_SCROLL_NO;
   }
   if (hostControl.FlatScrollBars) {
    flags |= Interop.DOCHOSTUIFLAG_FLAT_SCROLLBAR;
   }
   info.dwFlags = flags;
   return Interop.S_OK;
  }
  public int ShowUI(int dwID, Interop.IOleInPlaceActiveObject activeObject, Interop.IOleCommandTarget commandTarget, Interop.IOleInPlaceFrame frame, Interop.IOleInPlaceUIWindow doc) {
   return Interop.S_OK;
  }
  public int HideUI() {
   return Interop.S_OK;
  }
  public int UpdateUI() {
   return Interop.S_OK;
  }
  public int EnableModeless(bool fEnable) {
   return Interop.S_OK;
  }
  public int OnDocWindowActivate(bool fActivate) {
   return Interop.E_NOTIMPL;
  }
  public int OnFrameWindowActivate(bool fActivate) {
   return Interop.E_NOTIMPL;
  }
  public int ResizeBorder(Interop.COMRECT rect, Interop.IOleInPlaceUIWindow doc, bool fFrameWindow) {
   return Interop.E_NOTIMPL;
  }
  public int TranslateAccelerator(Interop.COMMSG msg, ref System.Guid group, int nCmdID) {
   const int WM_KEYDOWN = 0x0100;
   const int VK_CONTROL = 0x11;
   if (msg.message != WM_KEYDOWN)
    return Interop.S_FALSE;
   Keys keyData = Keys.None;
   if (Interop.GetAsyncKeyState(VK_CONTROL) < 0)
    keyData |= Keys.Control;
   int key = msg.wParam.ToInt32();
   key &= 0xFF;
   keyData |= (Keys)key;
   KeyEventArgs kea = new KeyEventArgs(keyData);
   hostControl.RaiseOnTranslateAccelerator(kea);
   if (kea.Handled)
    return Interop.S_OK;
   return Interop.S_FALSE;
  }
  public int GetOptionKeyPath(string[] pbstrKey, int dw) {
   pbstrKey[0] = null;
   return Interop.S_OK;
  }
  public int GetDropTarget(Interop.IOleDropTarget pDropTarget, out Interop.IOleDropTarget ppDropTarget) {
   ppDropTarget = null;
   return Interop.S_OK;
  }
  public int GetExternal(out object ppDispatch) {
   ppDispatch = hostControl.ScriptObject;
   if (ppDispatch != null) {
    return Interop.S_OK;
   }
   else {
    return Interop.E_NOTIMPL;
   }
  }
  public int TranslateUrl(int dwTranslate, string strURLIn, out IntPtr pstrURLOut) {
   pstrURLOut = IntPtr.Zero;
   BrowserTranslateUrlEventArgs e = new BrowserTranslateUrlEventArgs(strURLIn);
   try {
    hostControl.RaiseOnTranslateUrl(e);
    pstrURLOut = Marshal.StringToCoTaskMemUni(e.TranslatedUrl);
   }
   catch {}
   return Interop.S_OK;
  }
  public int FilterDataObject(object pDO, out object ppDORet) {
   ppDORet = pDO;
   return Interop.S_OK;
  }
  public int SaveObject() {
   return Interop.S_OK;
  }
  public int GetMoniker(int dwAssign, int dwWhichMoniker, out object ppmk) {
   ppmk = null;
   return Interop.E_NOTIMPL;
  }
  public int GetContainer(out Interop.IOleContainer ppContainer) {
   ppContainer = this;
   return Interop.S_OK;
  }
  public int ShowObject() {
   return Interop.S_OK;
  }
  public int OnShowWindow(int fShow) {
   return Interop.S_OK;
  }
  public int RequestNewObjectLayout() {
   return Interop.S_OK;
  }
  public void ParseDisplayName(object pbc, string pszDisplayName, int[] pchEaten, object[] ppmkOut) {
   throw new COMException(String.Empty, Interop.E_NOTIMPL);
  }
  public void EnumObjects(int grfFlags, object[] ppenum) {
   throw new COMException(String.Empty, Interop.E_NOTIMPL);
  }
  public void LockContainer(int fLock) {
  }
  public IntPtr GetWindow() {
   return this.hostControl.Handle;
  }
  public void ContextSensitiveHelp(int fEnterMode) {
  }
  public int CanInPlaceActivate() {
   return 0;
  }
  public void OnInPlaceActivate() {
  }
  public void OnUIActivate() {
  }
  public void GetWindowContext(out Interop.IOleInPlaceFrame ppFrame, out Interop.IOleInPlaceUIWindow ppDoc, Interop.COMRECT lprcPosRect, Interop.COMRECT lprcClipRect, Interop.tagOIFI lpFrameInfo) {
   ppFrame = null;
   ppDoc = null;
  }
  public int Scroll(Interop.tagSIZE scrollExtent) {
   return 0;
  }
  public void OnUIDeactivate(int fUndoable) {
  }
  public void OnInPlaceDeactivate() {
  }
  public void DiscardUndoState() {
  }
  public void DeactivateAndUndo() {
  }
  public int OnPosRectChange(Interop.COMRECT lprcPosRect) {
   return 0;
  }
  public void OnDataChange(object pFormatetc, object pStgmed) {
  }
  public void OnViewChange(int dwAspect, int lindex) {
  }
  public void OnRename(UCOMIMoniker pmk) {
  }
  public void OnSave() {
  }
  public void OnClose() {
  }
 }
}
