using System;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.Core
{
 public class CruiseManager : MarshalByRefObject, ICruiseManager
 {
  private readonly ICruiseServer cruiseServer;
  public CruiseManager(ICruiseServer cruiseServer)
  {
   this.cruiseServer = cruiseServer;
  }
  public ProjectStatus[] GetProjectStatus()
  {
   return cruiseServer.GetProjectStatus();
  }
  public void ForceBuild(string project, string enforcerName)
  {
   cruiseServer.ForceBuild(project, enforcerName);
  }
  public void AbortBuild(string project, string enforcerName)
  {
   cruiseServer.AbortBuild(project, enforcerName);
  }
  public void Request(string projectName, IntegrationRequest integrationRequest)
  {
   cruiseServer.Request(projectName, integrationRequest);
  }
  public void Start(string project)
  {
   cruiseServer.Start(project);
  }
  public void Stop(string project)
  {
   cruiseServer.Stop(project);
  }
  public void SendMessage(string projectName, Message message)
  {
   cruiseServer.SendMessage(projectName, message);
  }
  public void WaitForExit(string project)
  {
   cruiseServer.WaitForExit(project);
  }
  public void CancelPendingRequest(string projectName)
  {
   cruiseServer.CancelPendingRequest(projectName);
  }
        public CruiseServerSnapshot GetCruiseServerSnapshot()
  {
   return cruiseServer.GetCruiseServerSnapshot();
  }
  public string GetLatestBuildName(string projectName)
  {
   return cruiseServer.GetLatestBuildName(projectName);
  }
  public string[] GetBuildNames(string projectName)
  {
   return cruiseServer.GetBuildNames(projectName);
  }
  public string[] GetMostRecentBuildNames(string projectName, int buildCount)
  {
   try
   {
    return cruiseServer.GetMostRecentBuildNames(projectName, buildCount);
   }
   catch (Exception e)
   {
    Log.Error(e);
    throw new CruiseControlException("Unexpected exception caught on server", e);
   }
  }
  public string GetLog(string projectName, string buildName)
  {
   return cruiseServer.GetLog(projectName, buildName);
  }
  public string GetServerLog()
  {
   return cruiseServer.GetServerLog();
  }
  public string GetServerLog(string projectName)
  {
   return cruiseServer.GetServerLog(projectName);
  }
  public void AddProject(string serializedProject)
  {
   cruiseServer.AddProject(serializedProject);
  }
  public void DeleteProject(string projectName, bool purgeWorkingDirectory, bool purgeArtifactDirectory, bool purgeSourceControlEnvironment)
  {
   cruiseServer.DeleteProject(projectName, purgeWorkingDirectory, purgeArtifactDirectory, purgeSourceControlEnvironment);
  }
  public string GetProject(string projectName)
  {
   return cruiseServer.GetProject(projectName);
  }
  public void UpdateProject(string projectName, string serializedProject)
  {
   cruiseServer.UpdateProject(projectName, serializedProject);
  }
  public ExternalLink[] GetExternalLinks(string projectName)
  {
   return cruiseServer.GetExternalLinks(projectName);
  }
  public string GetArtifactDirectory(string projectName)
  {
   return cruiseServer.GetArtifactDirectory(projectName);
  }
  public string GetStatisticsDocument(string projectName)
  {
   return cruiseServer.GetStatisticsDocument(projectName);
  }
        public string GetModificationHistoryDocument(string projectName)
        {
            return cruiseServer.GetModificationHistoryDocument(projectName);
        }
        public string GetRSSFeed(string projectName)
        {
            return cruiseServer.GetRSSFeed(projectName);
        }
  public override object InitializeLifetimeService()
  {
   return null;
  }
  public string GetServerVersion()
  {
   return cruiseServer.GetVersion();
  }
        public long GetFreeDiskSpace()
        {
            return cruiseServer.GetFreeDiskSpace();
        }
        public virtual RemotingFileTransfer RetrieveFileTransfer(string project, string fileName, FileTransferSource source)
        {
            return cruiseServer.RetrieveFileTransfer(project, fileName, source);
        }
 }
}
