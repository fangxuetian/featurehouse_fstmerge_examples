using System;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Sourcecontrol
{
 [ReflectorType("surround")]
 public class Surround : ProcessSourceControl
 {
  public const string TO_SSCM_DATE_FORMAT = "yyyyMMddHHmmss";
  private const string DefaultServerConnection = "127.0.0.1:4900";
  private const string DefaultServerLogin = "Administrator:";
  public Surround() : base(new SurroundHistoryParser(), new ProcessExecutor())
  {}
  [ReflectorProperty("executable")]
  public string Executable = "sscm";
  [ReflectorProperty("branch")]
  public string Branch;
  [ReflectorProperty("repository")]
  public string Repository;
  [ReflectorProperty("file", Required=false)]
  public string File;
  [ReflectorProperty("workingDirectory")]
  public string WorkingDirectory;
  [ReflectorProperty("serverconnect", Required=false)]
  public string ServerConnect = DefaultServerConnection;
  [ReflectorProperty("serverlogin", Required=false)]
  public string ServerLogin = DefaultServerLogin;
  [ReflectorProperty("searchregexp", Required=false)]
  public int SearchRegExp = 0;
  [ReflectorProperty("recursive", Required=false)]
  public int Recursive = 0;
  public override Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
  {
   string command = String.Format("cc {0} -d{1}:{2} {3} -b{4} -p{5} {6} -z{7} -y{8}",
                                  File,
                                  from_.StartTime.ToString(TO_SSCM_DATE_FORMAT),
                                  to.StartTime.ToString(TO_SSCM_DATE_FORMAT),
                                  (Recursive == 0) ? "" : "-r",
                                  Branch,
                                  Repository,
                                  (SearchRegExp == 0) ? "-x-" : "-x",
                                  ServerConnect,
                                  ServerLogin);
            Modification[] modifications = GetModifications(CreateSSCMProcessInfo(command), from_.StartTime, to.StartTime);
            base.FillIssueUrl(modifications);
            return modifications;
        }
  private ProcessInfo CreateSSCMProcessInfo(string command)
  {
   return new ProcessInfo(Executable, command);
  }
  public override void LabelSourceControl(IIntegrationResult result)
  {}
  public override void Initialize(IProject project)
  {
   Execute(CreateSSCMProcessInfo("workdir " + WorkingDirectory + " " + Repository + " -z" + ServerConnect + " -y" + ServerLogin));
  }
  public override void GetSource(IIntegrationResult result)
  {
   Log.Info("Getting source from_ Surround SCM");
            result.BuildProgressInformation.SignalStartRunTask("Getting source from_ Surround SCM");
   string command = String.Format("get * -q -tcheckin -wreplace {0} -d{1} -b{2} -p{3} -z{4} -y{5}",
                                  (Recursive == 0) ? "" : "-r",
                                  WorkingDirectory,
             Branch,
                                  Repository,
                                  ServerConnect,
                                  ServerLogin);
   Execute(CreateSSCMProcessInfo(command));
  }
 }
}
