using System;
using System.Collections.Generic;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
using ThoughtWorks.CruiseControl.Remote.Security;
namespace ThoughtWorks.CruiseControl.Core
{
 public class RemoteCruiseServer : ICruiseServer
 {
  public const string URI = "CruiseManager.rem";
  public const string DefaultUri = "tcp://localhost:21234/" + URI;
  private ICruiseServer server;
  private bool disposed;
  private IExecutionEnvironment environment = new ExecutionEnvironment();
  public RemoteCruiseServer(ICruiseServer server, string remotingConfigurationFile)
  {
   this.server = server;
   RemotingConfiguration.Configure(remotingConfigurationFile, false);
   RegisterForRemoting();
  }
  public void Start()
  {
   server.Start();
  }
  public void Stop()
  {
   server.Stop();
  }
  public void Abort()
  {
   server.Abort();
  }
  public void WaitForExit()
  {
   server.WaitForExit();
  }
        public void Start(string sessionToken, string project)
  {
   server.Start(sessionToken, project);
  }
        public void Stop(string sessionToken, string project)
  {
   server.Stop(sessionToken, project);
  }
  public ICruiseManager CruiseManager
  {
   get { return server.CruiseManager; }
  }
  public ProjectStatus[] GetProjectStatus()
  {
   return server.GetProjectStatus();
  }
        public void ForceBuild(string sessionToken, string projectName, string enforcerName)
  {
            server.ForceBuild(sessionToken, projectName, enforcerName);
  }
  public void AbortBuild(string sessionToken, string projectName, string enforcerName)
  {
   server.AbortBuild(sessionToken, projectName, enforcerName);
  }
        public void Request(string sessionToken, string projectName, IntegrationRequest request)
  {
   server.Request(sessionToken, projectName, request);
  }
  public void WaitForExit(string projectName)
  {
   server.WaitForExit(projectName);
  }
        public void CancelPendingRequest(string sessionToken, string projectName)
  {
   server.CancelPendingRequest(sessionToken, projectName);
  }
        public CruiseServerSnapshot GetCruiseServerSnapshot()
  {
   return server.GetCruiseServerSnapshot();
  }
  public string GetLatestBuildName(string projectName)
  {
   return server.GetLatestBuildName(projectName);
  }
  public string[] GetBuildNames(string projectName)
  {
   return server.GetBuildNames(projectName);
  }
  public string GetVersion()
  {
   return server.GetVersion();
  }
  public string[] GetMostRecentBuildNames(string projectName, int buildCount)
  {
   return server.GetMostRecentBuildNames(projectName, buildCount);
  }
  public string GetLog(string projectName, string buildName)
  {
   return server.GetLog(projectName, buildName);
  }
  public string GetServerLog()
  {
   return server.GetServerLog();
  }
  public string GetServerLog(string projectName)
  {
   return server.GetServerLog(projectName);
  }
  public void AddProject(string serializedProject)
  {
   server.AddProject(serializedProject);
  }
  public void DeleteProject(string projectName, bool purgeWorkingDirectory, bool purgeArtifactDirectory, bool purgeSourceControlEnvironment)
  {
   server.DeleteProject(projectName, purgeWorkingDirectory, purgeArtifactDirectory, purgeSourceControlEnvironment);
  }
  public string GetProject(string name)
  {
   return server.GetProject(name);
  }
  public void UpdateProject(string projectName, string serializedProject)
  {
   server.UpdateProject(projectName, serializedProject);
  }
  public ExternalLink[] GetExternalLinks(string projectName)
  {
   return server.GetExternalLinks(projectName);
  }
        public void SendMessage(string sessionToken, string projectName, Message message)
  {
   server.SendMessage(sessionToken, projectName, message);
  }
  public string GetArtifactDirectory(string projectName)
  {
   return server.GetArtifactDirectory(projectName);
  }
  public string GetStatisticsDocument(string projectName)
  {
            return server.GetStatisticsDocument(projectName);
  }
        public string GetModificationHistoryDocument(string projectName)
        {
            return server.GetModificationHistoryDocument(projectName);
        }
        public string GetRSSFeed(string projectName)
        {
            return server.GetRSSFeed(projectName);
        }
  private void RegisterForRemoting()
  {
   MarshalByRefObject marshalByRef = (MarshalByRefObject) server.CruiseManager;
   RemotingServices.Marshal(marshalByRef, URI);
   foreach (IChannel channel in ChannelServices.RegisteredChannels)
   {
    Log.Info("Registered channel: " + channel.ChannelName);
    if (environment.IsRunningOnWindows)
    {
     if (channel is IChannelReceiver)
     {
      foreach (string url in ((IChannelReceiver) channel).GetUrlsForUri(URI))
      {
       Log.Info("CruiseManager: Listening on url: " + url);
      }
     }
    }
   }
  }
  void IDisposable.Dispose()
  {
   lock (this)
   {
    if (disposed) return;
    disposed = true;
   }
   Log.Info("Disconnecting remote server: ");
   RemotingServices.Disconnect((MarshalByRefObject) server.CruiseManager);
   foreach (IChannel channel in ChannelServices.RegisteredChannels)
   {
    Log.Info("Unregistering channel: " + channel.ChannelName);
    ChannelServices.UnregisterChannel(channel);
   }
   server.Dispose();
  }
        public string Login(ISecurityCredentials credentials)
        {
            return server.Login(credentials);
 }
        public void Logout(string sesionToken)
        {
            server.Logout(sesionToken);
        }
        public virtual string GetSecurityConfiguration(string sessionToken)
        {
            return server.GetSecurityConfiguration(sessionToken);
        }
        public virtual List<UserDetails> ListAllUsers(string sessionToken)
        {
            return server.ListAllUsers(sessionToken);
        }
        public virtual List<SecurityCheckDiagnostics> DiagnoseSecurityPermissions(string sessionToken, string userName, params string[] projectNames)
        {
            return server.DiagnoseSecurityPermissions(sessionToken, userName, projectNames);
        }
        public virtual List<AuditRecord> ReadAuditRecords(string sessionToken, int startPosition, int numberOfRecords)
        {
            return server.ReadAuditRecords(sessionToken, startPosition, numberOfRecords);
        }
        public virtual List<AuditRecord> ReadAuditRecords(string sessionToken, int startPosition, int numberOfRecords, IAuditFilter filter)
        {
            return server.ReadAuditRecords(sessionToken, startPosition, numberOfRecords, filter);
        }
        public virtual void ChangePassword(string sessionToken, string oldPassword, string newPassword)
        {
            server.ChangePassword(sessionToken, oldPassword, newPassword);
        }
        public virtual void ResetPassword(string sessionToken, string userName, string newPassword)
        {
            server.ResetPassword(sessionToken, userName, newPassword);
        }
 }
}
