using ThoughtWorks.CruiseControl.CCTrayLib.Monitoring;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Configuration
{
 public interface ICCTrayMultiConfiguration
 {
  ISingleServerMonitor[] GetServerMonitors();
  IProjectMonitor[] GetProjectStatusMonitors(ISingleServerMonitor[] serverMonitors);
  ICruiseServerManagerFactory CruiseServerManagerFactory { get; }
  ICruiseProjectManagerFactory CruiseProjectManagerFactory { get; }
  BuildServer[] GetUniqueBuildServerList();
  CCTrayProject[] Projects { get; set; }
  bool ShouldShowBalloonOnBuildTransition { get; set; }
  int PollPeriodSeconds { get; set; }
  AudioFiles Audio { get; }
  TrayIconDoubleClickAction TrayIconDoubleClickAction { get; set; }
  BalloonMessages BalloonMessages { get; }
        NotifyInfoFlags MinimumNotificationLevel { get; set; }
  Icons Icons { get; }
  X10Configuration X10 { get; }
  SpeechConfiguration Speech { get; }
        bool AlwaysOnTop { get; set; }
  bool ShowInTaskbar { get; set; }
        string FixUserName { get; set; }
  void Reload();
  void Persist();
  ICCTrayMultiConfiguration Clone();
 }
}
