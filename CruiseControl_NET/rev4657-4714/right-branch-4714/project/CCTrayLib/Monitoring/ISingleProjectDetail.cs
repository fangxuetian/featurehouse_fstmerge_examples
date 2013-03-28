using System;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Monitoring
{
 public interface ISingleProjectDetail
 {
  string ProjectName { get; }
  ProjectState ProjectState { get; }
  bool IsConnected { get; }
  ProjectActivity Activity { get; }
  string LastBuildLabel { get; }
  DateTime LastBuildTime { get; }
  DateTime NextBuildTime { get; }
  string ProjectIntegratorState { get; }
  string WebURL { get; }
  string CurrentMessage { get; }
        string CurrentBuildStage { get; }
  TimeSpan EstimatedTimeRemainingOnCurrentBuild { get; }
  Exception ConnectException { get; }
 }
}
