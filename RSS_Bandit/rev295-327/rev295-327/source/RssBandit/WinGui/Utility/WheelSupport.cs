using System; 
using System.Drawing; 
using System.Reflection; 
using System.Windows.Forms; 
using System.Runtime.InteropServices; 
using Infragistics.Win.UltraWinTree; 
using RssBandit.WinGui.Controls; namespace  RssBandit.WinGui.Utility {
	
 public class  WheelSupport : NativeWindow, IMessageFilter {
		
  public delegate  Control  OnGetChildControlHandler (Control control);
		
  public  event OnGetChildControlHandler OnGetChildControl; 
  private  Form parent;
 
  private  WheelSupport() {}
 
  public  WheelSupport(Form f) {
   this.parent = f;
   this.parent.Activated += new EventHandler(this.OnParentActivated);
   this.parent.Deactivate += new EventHandler(this.OnParentDeactivate);
  }
 
  private  void OnParentActivated(object sender, EventArgs e) {
   Application.AddMessageFilter(this);
  }
 
  private  void OnParentDeactivate(object sender, EventArgs e) {
   Application.RemoveMessageFilter(this);
  }
 
  public virtual  bool PreFilterMessage(ref Message m) {
   switch (m.Msg){
    case WM_MOUSEWHEEL:
     if (Control.ModifierKeys != Keys.None)
      return false;
     Point screenPoint = new Point(m.LParam.ToInt32());
     Control child = GetTopmostChild(parent, screenPoint);
     if (child != null) {
      if (m.HWnd == child.Handle && child.Focused)
       return false;
      if (child is IEControl.HtmlControl) {
       return ScrollHtmlControl(child as IEControl.HtmlControl, m);
      }
      if (child is UltraTree) {
       UltraTree tree = child as UltraTree;
       int delta = SignedHIWORD(m.WParam);
       return TreeHelper.InvokeDoVerticalScroll(tree, delta);
      }
      if (m.HWnd != child.Handle) {
       PostMessage(child.Handle, WM_MOUSEWHEEL, m.WParam, m.LParam);
       return true;
      }
      return false;
     }
     break;
   }
   return false;
  }
 
  public  Control GetTopmostChild(Control ctrl, Point mousePosition) {
   if (this.OnGetChildControl != null) {
    Control childControl = this.OnGetChildControl(ctrl);
    if (childControl != null)
     ctrl = childControl;
   }
   if (ctrl.Controls.Count > 0) {
    Point p = ctrl.PointToClient(mousePosition);
    Control child = ctrl.GetChildAtPoint(p);
    if (child != null) {
     return GetTopmostChild(child, mousePosition);
    } else {
     return ctrl;
    }
   } else {
    return ctrl;
   }
  }
 
  private  bool ScrollHtmlControl(IEControl.HtmlControl control, Message m) {
   IntPtr hwnd;
   IEControl.Interop.IOleWindow oleWindow = null;
   try {
    oleWindow = control.Document2 as IEControl.Interop.IOleWindow;
   } catch (Exception){}
   if (oleWindow == null)
    return false;
   oleWindow.GetWindow(out hwnd);
   if (m.HWnd == hwnd) {
    return false;
   }
   PostMessage(hwnd, WM_MOUSEWHEEL, m.WParam, m.LParam);
   return true;
  }
 
  public static  int SignedHIWORD(int n) {
   return (short) ((n >> 0x10) & 0xffff);
  }
 
  public static  int SignedHIWORD(IntPtr n) {
   return SignedHIWORD((int) ((long) n));
  }
 
  [DllImport("user32.dll")]  private static extern 
   bool PostMessage( IntPtr hWnd, int wMsg, IntPtr wParam, IntPtr lParam );
 
  private  const int WM_MOUSEWHEEL = 0x20A;
	}

}
