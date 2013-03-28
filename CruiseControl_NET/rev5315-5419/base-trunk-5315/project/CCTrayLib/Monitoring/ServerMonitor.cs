using System;
using System.Diagnostics;
using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Monitoring
{
    public class ServerMonitor : ISingleServerMonitor
 {
  public event MonitorServerPolledEventHandler Polled;
  public event MonitorServerQueueChangedEventHandler QueueChanged;
        private CruiseServerSnapshot lastCruiseServerSnapshot;
  private readonly ICruiseServerManager cruiseServerManager;
  private Exception connectException;
  public ServerMonitor(ICruiseServerManager cruiseServerManager)
  {
   this.cruiseServerManager = cruiseServerManager;
  }
  public void CancelPendingRequest(string projectName)
  {
   cruiseServerManager.CancelPendingRequest(projectName);
  }
        public CruiseServerSnapshot CruiseServerSnapshot
  {
            get { return lastCruiseServerSnapshot; }
  }
        public string SessionToken
        {
            get { return cruiseServerManager.SessionToken; }
        }
        public ProjectStatus GetProjectStatus(string projectName)
        {
            if (lastCruiseServerSnapshot == null || lastCruiseServerSnapshot.ProjectStatuses == null)
            {
                return null;
            }
            foreach (ProjectStatus status in lastCruiseServerSnapshot.ProjectStatuses)
            {
                if (status.Name == projectName)
                    return status;
            }
            throw new ApplicationException("Project '" + projectName + "' not found on server");
        }
  public void Poll()
  {
   try
   {
       CruiseServerSnapshot cruiseServerSnapshot = cruiseServerManager.GetCruiseServerSnapshot();
                if ((lastCruiseServerSnapshot == null)
                    || (cruiseServerSnapshot == null)
                    || lastCruiseServerSnapshot.IsQueueSetSnapshotChanged(cruiseServerSnapshot.QueueSetSnapshot))
                {
                    OnQueueChanged(new MonitorServerQueueChangedEventArgs(this));
                }
                lastCruiseServerSnapshot = cruiseServerSnapshot;
   }
   catch (Exception ex)
   {
    Trace.WriteLine("ServerMonitorPoll Exception: " + ex);
                lastCruiseServerSnapshot = null;
    connectException = ex;
                OnQueueChanged(new MonitorServerQueueChangedEventArgs(this));
            }
   OnPolled(new MonitorServerPolledEventArgs(this));
  }
  public void OnPollStarting()
  {
   if (cruiseServerManager is ICache)
    ((ICache)cruiseServerManager).InvalidateCache();
  }
  public string ServerUrl
  {
   get { return cruiseServerManager.Configuration.Url; }
  }
  public string DisplayName
  {
   get { return cruiseServerManager.DisplayName; }
  }
  public BuildServerTransport Transport
  {
   get { return cruiseServerManager.Configuration.Transport; }
  }
  public bool IsConnected
  {
   get { return lastCruiseServerSnapshot != null; }
  }
  public Exception ConnectException
  {
   get { return connectException; }
  }
  protected void OnPolled(MonitorServerPolledEventArgs args)
  {
   if (Polled != null) Polled(this, args);
  }
  protected void OnQueueChanged(MonitorServerQueueChangedEventArgs args)
  {
   if (QueueChanged != null) QueueChanged(this, args);
  }
        public void Start()
        {
            cruiseServerManager.Login();
        }
        public void Stop()
        {
            cruiseServerManager.Logout();
        }
        public bool RefreshSession()
        {
            cruiseServerManager.Logout();
            return cruiseServerManager.Login();
        }
 }
}
