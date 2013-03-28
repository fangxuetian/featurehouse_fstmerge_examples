using System;
using System.Diagnostics;
using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Monitoring
{
 public class ProjectMonitor : IProjectMonitor, ISingleProjectDetail
 {
  private readonly ICruiseProjectManager cruiseProjectManager;
  private ProjectStatus lastProjectStatus;
  private Exception connectException;
  private readonly BuildDurationTracker buildDurationTracker;
        private readonly IProjectStatusRetriever projectStatusRetriever;
        private readonly CCTrayProject _configuration;
        public ProjectMonitor(CCTrayProject configuration, ICruiseProjectManager cruiseProjectManager, IProjectStatusRetriever projectStatusRetriever)
   : this(configuration, cruiseProjectManager, projectStatusRetriever, new DateTimeProvider())
  {
  }
        public ProjectMonitor(CCTrayProject configuration, ICruiseProjectManager cruiseProjectManager, IProjectStatusRetriever projectStatusRetriever, DateTimeProvider dateTimeProvider)
  {
   buildDurationTracker = new BuildDurationTracker(dateTimeProvider);
   this.cruiseProjectManager = cruiseProjectManager;
            this.projectStatusRetriever = projectStatusRetriever;
            this._configuration = configuration;
        }
  public ProjectStatus ProjectStatus
  {
   get { return lastProjectStatus; }
  }
  public bool IsConnected
  {
   get { return lastProjectStatus != null; }
  }
  public ProjectActivity Activity
  {
   get
   {
    if (IsConnected)
    {
     return lastProjectStatus.Activity;
    }
    return new ProjectActivity("");
   }
  }
  public string LastBuildLabel
  {
   get
   {
    if (IsConnected)
    {
     return lastProjectStatus.LastBuildLabel;
    }
    return string.Empty;
   }
  }
        public string ServerName
        {
            get
            {
                if (IsConnected)
                {
                    return lastProjectStatus.ServerName;
                }
                return string.Empty;
            }
        }
  public DateTime LastBuildTime
  {
   get
   {
    if (IsConnected)
    {
     return lastProjectStatus.LastBuildDate;
    }
    return DateTime.MinValue;
   }
  }
  public DateTime NextBuildTime
  {
   get
   {
    if (IsConnected)
    {
     return lastProjectStatus.NextBuildTime;
    }
    return DateTime.MinValue;
   }
  }
  public string WebURL
  {
   get
   {
    if (IsConnected)
    {
     return lastProjectStatus.WebURL;
    }
    return string.Empty;
   }
  }
        public string CurrentBuildStage
        {
            get
   {
    if (IsConnected)
    {
     return lastProjectStatus.BuildStage;
    }
    return string.Empty;
   }
        }
  public string CurrentMessage
  {
   get
   {
    if (IsConnected)
    {
     return lastProjectStatus.CurrentMessage;
    }
    return string.Empty;
   }
  }
  public string ProjectName
  {
   get { return cruiseProjectManager.ProjectName; }
  }
        public CCTrayProject Configuration
        {
            get { return this._configuration; }
        }
  public Exception ConnectException
  {
   get { return connectException; }
  }
  public ProjectState ProjectState
  {
   get
   {
    ProjectStatus status = ProjectStatus;
    if (status == null)
     return ProjectState.NotConnected;
    if (status.Activity.IsBuilding())
     return (status.BuildStatus == IntegrationStatus.Success) ? ProjectState.Building : ProjectState.BrokenAndBuilding;
    if (status.BuildStatus == IntegrationStatus.Success)
     return ProjectState.Success;
    return ProjectState.Broken;
   }
  }
  public IntegrationStatus IntegrationStatus
  {
   get
   {
    if (lastProjectStatus == null)
     return IntegrationStatus.Unknown;
    return lastProjectStatus.BuildStatus;
   }
  }
  public string ProjectIntegratorState
  {
            get
            {
                if (lastProjectStatus == null)
                {
                    return "Unknown";
                }
                else
                {
                    return lastProjectStatus.Status.ToString();
                }
            }
  }
  public bool IsPending
  {
   get { return ProjectStatus != null && ProjectStatus.Activity.IsPending(); }
  }
  public ISingleProjectDetail Detail
  {
   get { return this; }
  }
  public void ForceBuild()
  {
   cruiseProjectManager.ForceBuild();
  }
  public void AbortBuild()
  {
   cruiseProjectManager.AbortBuild();
  }
  public void FixBuild(string fixingUserName)
  {
            cruiseProjectManager.FixBuild(fixingUserName);
  }
  public void StopProject()
  {
   cruiseProjectManager.StopProject();
  }
  public void StartProject()
  {
   cruiseProjectManager.StartProject();
  }
  public void CancelPending()
  {
   cruiseProjectManager.CancelPendingRequest();
  }
  public void OnPollStarting()
  {
  }
  public void Poll()
  {
   try
   {
    ProjectStatus newProjectStatus = projectStatusRetriever.GetProjectStatus(ProjectName);
    if (lastProjectStatus != null && newProjectStatus != null)
    {
     PollIntervalReporter duringInterval = new PollIntervalReporter(lastProjectStatus, newProjectStatus);
     if (duringInterval.IsAnotherBuildComplete && duringInterval.WasLatestBuildSuccessful) buildDurationTracker.OnSuccessfulBuild();
     if (duringInterval.IsAnotherBuildComplete) OnBuildOccurred(new MonitorBuildOccurredEventArgs(this, duringInterval.BuildTransition));
     if (duringInterval.HasNewBuildStarted) buildDurationTracker.OnBuildStart();
     if (duringInterval.WasNewStatusMessagesReceived) OnMessageReceived(duringInterval.LatestStatusMessage);
    }
    lastProjectStatus = newProjectStatus;
   }
   catch (Exception ex)
   {
    Debug.WriteLine("Exception during poll: " + ex);
    lastProjectStatus = null;
    connectException = ex;
   }
   OnPolled(new MonitorPolledEventArgs(this));
  }
  public event MonitorBuildOccurredEventHandler BuildOccurred;
  public event MonitorPolledEventHandler Polled;
  public event MessageEventHandler MessageReceived;
  protected void OnBuildOccurred(MonitorBuildOccurredEventArgs args)
  {
   if (BuildOccurred != null) BuildOccurred(this, args);
  }
  protected void OnPolled(MonitorPolledEventArgs args)
  {
   if (Polled != null) Polled(this, args);
  }
  private void OnMessageReceived(Message message)
  {
   if (MessageReceived != null) MessageReceived(message);
  }
  public string SummaryStatusString
  {
   get
   {
    ProjectState state = ProjectState;
    if (state == ProjectState.Success)
     return String.Empty;
    return ProjectName + ": " + state;
   }
  }
  public TimeSpan EstimatedTimeRemainingOnCurrentBuild
  {
   get { return buildDurationTracker.EstimatedTimeRemainingOnCurrentBuild; }
  }
 }
 public delegate void MessageEventHandler(Message message);
}
