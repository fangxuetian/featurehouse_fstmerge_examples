using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Monitoring
{
 public class RemotingCruiseServerManager : ICruiseServerManager
 {
  private readonly ICruiseManager manager;
  private readonly string serverUrl;
  private readonly string displayName;
  private readonly BuildServerTransport transport;
  public RemotingCruiseServerManager(ICruiseManager manager, BuildServer buildServer)
  {
   this.manager = manager;
   this.serverUrl = buildServer.Url;
   this.displayName = buildServer.DisplayName;
   this.transport = buildServer.Transport;
  }
  public string ServerUrl
  {
   get { return serverUrl; }
  }
  public string DisplayName
  {
   get { return displayName; }
  }
  public BuildServerTransport Transport
  {
   get { return transport; }
  }
  public void CancelPendingRequest(string projectName)
  {
   manager.CancelPendingRequest(projectName);
  }
        public CruiseServerSnapshot GetCruiseServerSnapshot()
  {
   return manager.GetCruiseServerSnapshot();
  }
 }
}
