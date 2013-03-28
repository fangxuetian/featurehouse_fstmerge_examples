using NMock;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core.Reporting.Dashboard.Navigation;
using ThoughtWorks.CruiseControl.Remote;
using ThoughtWorks.CruiseControl.WebDashboard.Configuration;
using ThoughtWorks.CruiseControl.WebDashboard.ServerConnection;
using System.Collections.Generic;
namespace ThoughtWorks.CruiseControl.UnitTests.WebDashboard.ServerConnection
{
 [TestFixture]
 public class ServerAggregatingCruiseManagerWrapperTest
 {
  private DynamicMock configurationMock;
  private DynamicMock cruiseManagerFactoryMock;
  private DynamicMock cruiseManagerMock;
  private ServerAggregatingCruiseManagerWrapper managerWrapper;
  private DefaultServerSpecifier serverSpecifier;
  private IProjectSpecifier projectSpecifier;
  private DefaultBuildSpecifier buildSpecifier;
  private DefaultBuildSpecifier buildSpecifierForUnknownServer;
  private ServerLocation serverLocation;
  private ServerLocation otherServerLocation;
  [SetUp]
  public void Setup()
  {
   configurationMock = new DynamicMock(typeof (IRemoteServicesConfiguration));
   cruiseManagerFactoryMock = new DynamicMock(typeof (ICruiseManagerFactory));
   cruiseManagerMock = new DynamicMock(typeof (ICruiseManager));
   serverSpecifier = new DefaultServerSpecifier("myserver");
   projectSpecifier = new DefaultProjectSpecifier(serverSpecifier, "myproject");
   buildSpecifier = new DefaultBuildSpecifier(projectSpecifier, "mybuild");
   buildSpecifierForUnknownServer = new DefaultBuildSpecifier(new DefaultProjectSpecifier(new DefaultServerSpecifier("unknownServer"), "myProject"), "myBuild");
   managerWrapper = new ServerAggregatingCruiseManagerWrapper(
    (IRemoteServicesConfiguration) configurationMock.MockInstance,
    (ICruiseManagerFactory) cruiseManagerFactoryMock.MockInstance
    );
   serverLocation = new ServerLocation();
   serverLocation.Name = "myserver";
   serverLocation.Url = "http://myurl";
   serverLocation.AllowForceBuild = true;
   otherServerLocation = new ServerLocation();
   otherServerLocation.Name = "myotherserver";
   otherServerLocation.Url = "http://myotherurl";
  }
  private void VerifyAll()
  {
   configurationMock.Verify();
   cruiseManagerFactoryMock.Verify();
   cruiseManagerMock.Verify();
  }
  [Test]
  public void ThrowsCorrectExceptionIfServerNotKnown()
  {
   configurationMock.ExpectAndReturn("Servers", new ServerLocation[] {serverLocation});
   try
   {
    managerWrapper.GetLog(buildSpecifierForUnknownServer);
    Assert.Fail("Should throw exception");
   }
   catch (UnknownServerException e)
   {
    Assert.AreEqual("unknownServer", e.RequestedServer);
   }
   configurationMock.ExpectAndReturn("Servers", new ServerLocation[] {serverLocation});
   try
   {
    managerWrapper.GetLatestBuildSpecifier(buildSpecifierForUnknownServer.ProjectSpecifier);
    Assert.Fail("Should throw exception");
   }
   catch (UnknownServerException e)
   {
    Assert.AreEqual("unknownServer", e.RequestedServer);
   }
   VerifyAll();
  }
  [Test]
  public void ThrowsCorrectExceptionIfProjectNotRunningOnServer()
  {
   configurationMock.ExpectAndReturn("Servers", new ServerLocation[] {serverLocation});
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndThrow("GetLatestBuildName", new NoSuchProjectException("myproject"), "myproject");
   try
   {
    managerWrapper.GetLatestBuildSpecifier(projectSpecifier);
    Assert.Fail("Should throw exception");
   }
   catch (NoSuchProjectException e)
   {
    Assert.AreEqual("myproject", e.RequestedProject);
   }
   VerifyAll();
  }
  [Test]
  public void ReturnsLatestLogNameFromCorrectProjectOnCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   DefaultProjectSpecifier myProjectMyServer = new DefaultProjectSpecifier(new DefaultServerSpecifier("myserver"), "myproject");
   DefaultProjectSpecifier myOtherProjectMyServer = new DefaultProjectSpecifier(new DefaultServerSpecifier("myserver"), "myotherproject");
   DefaultProjectSpecifier myProjectMyOtherServer = new DefaultProjectSpecifier(new DefaultServerSpecifier("myotherserver"), "myproject");
   DefaultProjectSpecifier myOtherProjectMyOtherServer = new DefaultProjectSpecifier(new DefaultServerSpecifier("myotherserver"), "myotherproject");
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetLatestBuildName", "mylogformyserverformyproject", "myproject");
   Assert.AreEqual(new DefaultBuildSpecifier(myProjectMyServer, "mylogformyserverformyproject"), managerWrapper.GetLatestBuildSpecifier(myProjectMyServer));
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetLatestBuildName", "mylogformyserverformyotherproject", "myotherproject");
   Assert.AreEqual(new DefaultBuildSpecifier(myOtherProjectMyServer, "mylogformyserverformyotherproject"), managerWrapper.GetLatestBuildSpecifier(myOtherProjectMyServer));
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myotherurl");
   cruiseManagerMock.ExpectAndReturn("GetLatestBuildName", "mylogformyotherserverformyproject", "myproject");
   Assert.AreEqual(new DefaultBuildSpecifier(myProjectMyOtherServer, "mylogformyotherserverformyproject"), managerWrapper.GetLatestBuildSpecifier(myProjectMyOtherServer));
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myotherurl");
   cruiseManagerMock.ExpectAndReturn("GetLatestBuildName", "mylogformyotherserverformyotherproject", "myotherproject");
   Assert.AreEqual(new DefaultBuildSpecifier(myOtherProjectMyOtherServer, "mylogformyotherserverformyotherproject"), managerWrapper.GetLatestBuildSpecifier(myOtherProjectMyOtherServer));
   VerifyAll();
  }
  [Test]
  public void ReturnsCorrectLogFromCorrectProjectOnCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetLog", "log\r\ncontents", "myproject", "mybuild");
   Assert.AreEqual("log\r\ncontents", managerWrapper.GetLog(buildSpecifier));
   VerifyAll();
  }
  [Test]
  public void ReturnsCorrectLogNamesFromCorrectProjectOnCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetBuildNames", new string[] {"log1", "log2"}, "myproject");
   Assert.AreEqual(new DefaultBuildSpecifier(projectSpecifier, "log1"), managerWrapper.GetBuildSpecifiers(projectSpecifier)[0]);
   VerifyAll();
  }
  [Test]
  public void ReturnCorrectArtifactDirectoryFromCorrectProjectFromCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetArtifactDirectory", @"c:\ArtifactDirectory", "myproject");
   Assert.AreEqual(@"c:\ArtifactDirectory", managerWrapper.GetArtifactDirectory(projectSpecifier));
  }
  [Test]
  public void ReturnsCorrectBuildSpecifiersFromCorrectProjectOnCorrectServerWhenNumberOfBuildsSpecified()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetMostRecentBuildNames", new string[] {"log1", "log2"}, "myproject", 99);
   IBuildSpecifier[] returnedBuildSpecifiers = managerWrapper.GetMostRecentBuildSpecifiers(projectSpecifier, 99);
   Assert.AreEqual("log1", returnedBuildSpecifiers[0].BuildName);
   Assert.AreEqual(projectSpecifier, returnedBuildSpecifiers[0].ProjectSpecifier);
   Assert.AreEqual(2, returnedBuildSpecifiers.Length);
   VerifyAll();
  }
  [Test]
  public void AddsProjectToCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   string serializedProject = "myproject---";
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.Expect("AddProject", serializedProject);
   managerWrapper.AddProject(serverSpecifier, serializedProject);
   VerifyAll();
  }
  [Test]
  public void DeletesProjectOnCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.Expect("DeleteProject", "myproject", false, true, false);
   managerWrapper.DeleteProject(projectSpecifier, false, true, false);
   VerifyAll();
  }
  [Test]
  public void GetsProjectFromCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   string serializedProject = "a serialized project";
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetProject", serializedProject, "myproject");
   string returnedProject = managerWrapper.GetProject(projectSpecifier);
   VerifyAll();
   Assert.AreEqual(serializedProject, returnedProject);
  }
  [Test]
  public void UpdatesProjectOnCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   string serializedProject = "myproject---";
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.Expect("UpdateProject", "myproject", serializedProject);
   managerWrapper.UpdateProject(projectSpecifier, serializedProject);
   VerifyAll();
  }
  [Test]
  public void ReturnsServerLogFromCorrectServer()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetServerLog", "a server log");
   Assert.AreEqual("a server log", managerWrapper.GetServerLog(serverSpecifier));
   VerifyAll();
  }
  [Test]
  public void ReturnsServerLogFromCorrectServerForCorrectProject()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetServerLog", "a server log", "myproject");
   Assert.AreEqual("a server log", managerWrapper.GetServerLog(projectSpecifier));
   VerifyAll();
  }
  [Test]
  public void ReturnsServerNames()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   IServerSpecifier[] serverSpecifiers = managerWrapper.GetServerSpecifiers();
   Assert.AreEqual(2, serverSpecifiers.Length);
   Assert.AreEqual("myserver", serverSpecifiers[0].ServerName);
   Assert.AreEqual("myotherserver", serverSpecifiers[1].ServerName);
   VerifyAll();
  }
  [Test]
  public void ForcesBuild()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
            var parameters = new Dictionary<string, string>();
   cruiseManagerMock.Expect("ForceBuild", (string)null, "myproject","BuildForcer", parameters);
            managerWrapper.ForceBuild(projectSpecifier, null, "BuildForcer", parameters);
   VerifyAll();
  }
  [Test]
  public void ShouldMatchServersInCaseInsensitiveWay()
  {
   configurationMock.ExpectAndReturn("Servers", new ServerLocation[] {serverLocation, otherServerLocation});
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   cruiseManagerMock.ExpectAndReturn("GetServerVersion", "1.1");
   managerWrapper.GetServerVersion(new DefaultServerSpecifier(serverLocation.Name.ToUpper()));
   VerifyAll();
  }
  [Test]
  public void GetsExternalLinks()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
   ExternalLink[] links = new ExternalLink[] {new ExternalLink("1", "2"), new ExternalLink("3", "4")};
   cruiseManagerMock.ExpectAndReturn("GetExternalLinks", links, "myproject");
   Assert.AreEqual(links, managerWrapper.GetExternalLinks(projectSpecifier));
   VerifyAll();
  }
  [Test]
  public void ReturnsServerConfiguration()
  {
   ServerLocation[] servers = new ServerLocation[] {serverLocation, otherServerLocation};
   configurationMock.ExpectAndReturn("Servers", servers);
   IServerSpecifier specifier = managerWrapper.GetServerConfiguration("myserver");
   Assert.AreEqual(true, specifier.AllowForceBuild);
   VerifyAll();
  }
        [Test]
        public void ReturnsServerSecurityFromCorrectServer()
        {
            ServerLocation[] servers = new ServerLocation[] { serverLocation, otherServerLocation };
            configurationMock.ExpectAndReturn("Servers", servers);
            cruiseManagerFactoryMock.ExpectAndReturn("GetCruiseManager", cruiseManagerMock.MockInstance, "http://myurl");
            string expected = "security configuration";
            cruiseManagerMock.ExpectAndReturn("GetSecurityConfiguration", expected, string.Empty);
            string actual = managerWrapper.GetServerSecurity(serverSpecifier, string.Empty);
            Assert.AreEqual(expected, actual);
            VerifyAll();
        }
 }
}
