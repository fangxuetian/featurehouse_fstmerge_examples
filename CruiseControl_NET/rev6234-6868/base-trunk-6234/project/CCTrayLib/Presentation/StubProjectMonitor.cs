using System;
using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.CCTrayLib.Monitoring;
using ThoughtWorks.CruiseControl.Remote;
using System.Collections.Generic;
using ThoughtWorks.CruiseControl.Remote.Parameters;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Presentation
{
 public class StubProjectMonitor : IProjectMonitor, ISingleProjectDetail
 {
  private ProjectStatus projectStatus;
  private ProjectState projectState = ProjectState.NotConnected;
  private IntegrationStatus integrationStatus = IntegrationStatus.Unknown;
  private string projectName;
        private string category;
  private Exception connectException;
        private CCTrayProject _configuration;
  public StubProjectMonitor(string projectName)
  {
   this.projectName = projectName;
  }
        public string Category
        {
            get { return category; }
        }
  public string ProjectName
  {
   get { return projectName; }
  }
        public string ServerName
        {
            get { return string.Empty; }
        }
        public CCTrayProject Configuration
        {
            get { return _configuration; }
            set { _configuration = value; }
        }
  public ProjectStatus ProjectStatus
  {
   get { return projectStatus; }
   set { projectStatus = value; }
  }
  public ISingleProjectDetail Detail
  {
   get { return this; }
  }
  public string ProjectIntegratorState
  {
   get { return "Running"; }
  }
  public ProjectState ProjectState
  {
   get { return projectState; }
   set { projectState = value; }
  }
  public IntegrationStatus IntegrationStatus
  {
   get { return integrationStatus; }
   set { integrationStatus = value; }
  }
  public bool IsPending
  {
   get { return ProjectStatus.Activity.IsPending(); }
  }
  public bool IsConnected
  {
   get { return ProjectStatus != null; }
  }
  public ProjectActivity Activity
  {
   get { return ProjectStatus.Activity; }
  }
        public void AbortBuild()
        {
            throw new NotImplementedException();
        }
        public string LastBuildLabel
  {
   get { return ProjectStatus.LastBuildLabel; }
  }
  public DateTime LastBuildTime
  {
   get { return ProjectStatus.LastBuildDate; }
  }
  public DateTime NextBuildTime
  {
   get { return ProjectStatus.NextBuildTime; }
  }
        public string CurrentBuildStage
        {
            get { return ProjectStatus.BuildStage; }
        }
  public string WebURL
  {
   get { return ProjectStatus.WebURL; }
  }
  public string CurrentMessage
  {
   get { return ProjectStatus.CurrentMessage; }
  }
  public TimeSpan EstimatedTimeRemainingOnCurrentBuild
  {
   get { return TimeSpan.Zero; }
  }
  public void OnBuildOccurred(MonitorBuildOccurredEventArgs args)
  {
   if (BuildOccurred != null)
    BuildOccurred(this, args);
  }
  public void OnPolled(MonitorPolledEventArgs args)
  {
   if (Polled != null)
    Polled(this, args);
  }
  public void OnMessageReceived(string projectName, Message message )
  {
   if (MessageReceived != null)
    MessageReceived(projectName, message );
  }
  public event MonitorBuildOccurredEventHandler BuildOccurred;
  public event MonitorPolledEventHandler Polled;
  public event MessageEventHandler MessageReceived;
  public void Poll()
  {
   OnPolled(new MonitorPolledEventArgs(this));
  }
  public void OnPollStarting()
  {
  }
        public void ForceBuild(Dictionary<string, string> parameters)
  {
   throw new NotImplementedException();
  }
  public void FixBuild(string fixingUserName)
  {
   throw new NotImplementedException();
  }
  public void StopProject()
  {
   throw new NotImplementedException();
  }
  public void StartProject()
  {
   throw new NotImplementedException();
  }
  public void CancelPending()
  {
   throw new NotImplementedException();
  }
  public void SetUpAsIfExceptionOccurredOnConnect(Exception exception)
  {
   ProjectState = ProjectState.NotConnected;
   ProjectStatus = null;
   ConnectException = exception;
  }
  public Exception ConnectException
  {
   get { return connectException; }
   set { connectException = value; }
  }
  public string SummaryStatusString
  {
   get { throw new NotImplementedException(); }
  }
        public virtual ProjectStatusSnapshot RetrieveSnapshot()
        {
            throw new InvalidOperationException();
        }
        public virtual PackageDetails[] RetrievePackageList()
        {
            throw new InvalidOperationException();
        }
        public virtual IFileTransfer RetrieveFileTransfer(string fileName)
        {
            throw new InvalidOperationException();
        }
        public List<ParameterBase> ListBuildParameters()
        {
            return null;
        }
 }
}
