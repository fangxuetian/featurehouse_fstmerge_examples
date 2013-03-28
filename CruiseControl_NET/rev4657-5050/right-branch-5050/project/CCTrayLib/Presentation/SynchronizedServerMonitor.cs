using System;
using System.ComponentModel;
using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.CCTrayLib.Monitoring;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Presentation
{
 public class SynchronizedServerMonitor : ISingleServerMonitor
 {
  public event MonitorServerQueueChangedEventHandler QueueChanged;
  public event MonitorServerPolledEventHandler Polled;
  private readonly ISingleServerMonitor serverMonitor;
  private readonly ISynchronizeInvoke synchronizeInvoke;
  public SynchronizedServerMonitor(ISingleServerMonitor serverMonitor, ISynchronizeInvoke synchronizeInvoke)
  {
   this.serverMonitor = serverMonitor;
   this.synchronizeInvoke = synchronizeInvoke;
   serverMonitor.Polled += new MonitorServerPolledEventHandler(ServerMonitor_Polled);
   serverMonitor.QueueChanged += new MonitorServerQueueChangedEventHandler(ServerMonitor_QueueChanged);
  }
  public string ServerUrl
  {
   get { return serverMonitor.ServerUrl; }
  }
  public string DisplayName
  {
   get { return serverMonitor.DisplayName; }
  }
  public BuildServerTransport Transport
  {
   get { return serverMonitor.Transport; }
  }
  public void CancelPendingRequest(string projectName)
  {
   serverMonitor.CancelPendingRequest(projectName);
  }
        public CruiseServerSnapshot CruiseServerSnapshot
  {
            get { return serverMonitor.CruiseServerSnapshot; }
  }
        public ProjectStatus GetProjectStatus(string projectName)
        {
            return serverMonitor.GetProjectStatus(projectName);
        }
  public bool IsConnected
  {
   get { return serverMonitor.IsConnected; }
  }
  public Exception ConnectException
  {
   get { return serverMonitor.ConnectException; }
  }
  public void Poll()
  {
   serverMonitor.Poll();
  }
  public void OnPollStarting()
  {
   serverMonitor.OnPollStarting();
  }
  private void ServerMonitor_Polled(object sender, MonitorServerPolledEventArgs args)
  {
   if (Polled != null) synchronizeInvoke.BeginInvoke(Polled, new object[] {sender, args});
  }
  private void ServerMonitor_QueueChanged(object sender, MonitorServerQueueChangedEventArgs args)
  {
   if (QueueChanged != null) synchronizeInvoke.BeginInvoke(QueueChanged, new object[] {sender, args});
  }
 }
}
