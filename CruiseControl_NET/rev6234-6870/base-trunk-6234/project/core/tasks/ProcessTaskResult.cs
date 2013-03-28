using System;
using System.Xml;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Tasks
{
 public class ProcessTaskResult : ITaskResult
 {
  protected readonly ProcessResult result;
     protected bool ignoreStandardOutputOnSuccess;
        public ProcessTaskResult(ProcessResult result)
            : this(result, false)
        {
        }
     public ProcessTaskResult(ProcessResult result, bool ignoreStandardOutputOnSuccess)
  {
   this.result = result;
         this.ignoreStandardOutputOnSuccess = ignoreStandardOutputOnSuccess;
   if (this.CheckIfSuccess())
   {
    Log.Info("Task execution failed");
    Log.Info("Task output: " + result.StandardOutput);
    string input = result.StandardError;
    if (!string.IsNullOrEmpty(input))
     Log.Info("Task error: " + result.StandardError);
   }
  }
  public virtual string Data
  {
   get
            {
                if (!ignoreStandardOutputOnSuccess || result.Failed)
                {
                    return StringUtil.Join(Environment.NewLine, result.StandardOutput, result.StandardError);
                }
                else
                {
                    return result.StandardError;
                }
            }
  }
  public virtual void WriteTo(XmlWriter writer)
  {
   writer.WriteStartElement("task");
   if (result.Failed)
    writer.WriteAttributeString("failed", true.ToString());
   if (result.TimedOut)
    writer.WriteAttributeString("timedout", true.ToString());
            if (!ignoreStandardOutputOnSuccess || result.Failed)
                writer.WriteElementString("standardOutput", result.StandardOutput);
   writer.WriteElementString("standardError", result.StandardError);
   writer.WriteEndElement();
  }
        public bool CheckIfSuccess()
        {
            return !result.Failed;
        }
 }
}
