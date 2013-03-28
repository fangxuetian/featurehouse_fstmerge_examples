using ThoughtWorks.CruiseControl.CCTrayLib.Configuration;
using ThoughtWorks.CruiseControl.Remote;
using System;
using ThoughtWorks.CruiseControl.Remote.Messages;
namespace ThoughtWorks.CruiseControl.CCTrayLib.Monitoring
{
 public class RemotingCruiseServerManager : ICruiseServerManager
 {
        private readonly CruiseServerClientBase manager;
  private readonly BuildServer configuration;
  private readonly string displayName;
        public RemotingCruiseServerManager(CruiseServerClientBase manager, BuildServer buildServer)
  {
   this.manager = manager;
   this.displayName = buildServer.DisplayName;
            this.configuration = buildServer;
  }
        public BuildServer Configuration
        {
            get { return configuration; }
        }
        public string SessionToken
        {
            get { return manager.SessionToken; }
        }
  public string DisplayName
  {
   get { return displayName; }
  }
  public void CancelPendingRequest(string projectName)
  {
            manager.CancelPendingRequest(projectName);
  }
        public CruiseServerSnapshot GetCruiseServerSnapshot()
  {
            var snapshot = manager.GetCruiseServerSnapshot();
            return snapshot;
  }
        public bool Login()
        {
            if (configuration.SecurityType != null)
            {
                Logout();
                IAuthenticationMode authentication = ExtensionHelpers.RetrieveAuthenticationMode(configuration.SecurityType);
                authentication.Settings = configuration.SecuritySettings;
                var isValid = manager.Login(authentication.GenerateCredentials().Credentials);
                return isValid;
            }
            else
            {
                return true;
            }
        }
        public void Logout()
        {
            manager.Logout();
        }
 }
}
