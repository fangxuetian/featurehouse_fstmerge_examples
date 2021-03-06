using System;
using System.Windows.Forms.Design;
using System.Windows.Forms;
namespace RssBandit.WinGui.Controls
{
 public class DirectoryBrowser: FolderNameEditor {
  private string returnPath = String.Empty;
  private FolderBrowser fb = new FolderBrowser();
  public string ReturnPath {
   get { return returnPath; }
  }
  public string Description {
   get { return fb.Description; }
   set { fb.Description = value; }
  }
  public DirectoryBrowser() { ;}
  private DialogResult RunDialog() {
   fb.StartLocation = FolderBrowserFolder.MyComputer;
   fb.Style = FolderBrowserStyles.ShowTextBox;
   DialogResult r = fb.ShowDialog();
   if (r == DialogResult.OK)
    returnPath = fb.DirectoryPath;
   else
    returnPath = String.Empty;
   return r;
  }
  public DialogResult ShowDialog() {
   return RunDialog();
  }
 }
}
