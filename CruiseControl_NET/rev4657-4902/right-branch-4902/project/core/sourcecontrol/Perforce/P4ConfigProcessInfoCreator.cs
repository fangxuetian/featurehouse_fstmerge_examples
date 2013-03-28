using System.Text;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Sourcecontrol.Perforce
{
 public class P4ConfigProcessInfoCreator : IP4ProcessInfoCreator
 {
  public ProcessInfo CreateProcessInfo(P4 p4, string extraArguments)
  {
   ProcessInfo processInfo = new ProcessInfo(p4.Executable, BuildCommonArguments(p4) + extraArguments);
   processInfo.TimeOut = 0;
   return processInfo;
  }
  private string BuildCommonArguments(P4 p4)
  {
   StringBuilder args = new StringBuilder();
   args.Append("-s ");
   if (! StringUtil.IsBlank(p4.Client))
   {
    args.Append("-c " + p4.Client + " ");
   }
   if (! StringUtil.IsBlank(p4.Port))
   {
    args.Append("-p " + p4.Port + " ");
   }
   if (! StringUtil.IsBlank(p4.User))
   {
    args.Append("-u " + p4.User + " ");
   }
   if (! StringUtil.IsBlank(p4.Password))
   {
    args.Append("-P " + p4.Password + " ");
   }
   return args.ToString();
  }
 }
}
