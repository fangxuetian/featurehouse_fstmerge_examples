using System;
using NUnit.Framework;
using CCNet = ThoughtWorks.CruiseControl;
namespace ThoughtWorks.CruiseControl.UnitTests.IntegrationTests
{
    [TestFixture]
    [Category("Integration")]
    public class CCNet_1902
    {
        [TestFixtureSetUp]
        public void fixLog4Net()
        {
            log4net.Config.XmlConfigurator.Configure(new System.IO.FileInfo("test.config"));
        }
        private System.Collections.Generic.Dictionary<string, bool> IntegrationCompleted;
        [Test]
        [Timeout(120000)]
        public void ForceBuildShouldNotWorkWhenProjectIsStopped()
        {
            const string projectName1 = "test02";
            IntegrationCompleted = new System.Collections.Generic.Dictionary<string, bool>();
            string IntegrationFolder = System.IO.Path.Combine("scenarioTests", projectName1);
            string CCNetConfigFile = System.IO.Path.Combine("IntegrationScenarios", "Simple.xml");
            string Project1StateFile = new System.IO.FileInfo(projectName1 + ".state").FullName;
            IntegrationCompleted.Add(projectName1, false);
            Log("Clear existing state file, to simulate first run : " + Project1StateFile);
            System.IO.File.Delete(Project1StateFile);
            Log("Clear integration folder to simulate first run");
            if (System.IO.Directory.Exists(IntegrationFolder)) System.IO.Directory.Delete(IntegrationFolder, true);
            CCNet.Remote.Messages.ProjectStatusResponse psr;
            CCNet.Remote.Messages.ProjectRequest pr1 = new CCNet.Remote.Messages.ProjectRequest(null, projectName1);
            Log("Making CruiseServerFactory");
            CCNet.Core.CruiseServerFactory csf = new CCNet.Core.CruiseServerFactory();
            bool ErrorOccured = false;
            Log("Making cruiseServer with config from_ :" + CCNetConfigFile);
            using (var cruiseServer = csf.Create(true, CCNetConfigFile))
            {
                cruiseServer.ProjectStarting += new EventHandler<ThoughtWorks.CruiseControl.Remote.Events.CancelProjectEventArgs>(cruiseServer_ProjectStarting);
                Log("Starting cruiseServer");
                cruiseServer.Start();
                cruiseServer.ProjectStarting -= new EventHandler<ThoughtWorks.CruiseControl.Remote.Events.CancelProjectEventArgs>(cruiseServer_ProjectStarting);
                Log("Stopping project " + projectName1);
                cruiseServer.Stop(pr1);
                System.Threading.Thread.Sleep(250);
                Log("Forcing build on project " + projectName1);
                try
                {
                    CheckResponse(cruiseServer.ForceBuild(pr1));
                }
                catch (Exception e)
                {
                    ErrorOccured = true;
                    Assert.AreEqual(e.Message, "Project is stopping / stopped - unable to start integration");
                }
                Assert.IsTrue(ErrorOccured, "Force build should raise exception when forcing build and project is stopping or stopped");
            }
        }
        void cruiseServer_ProjectStarting(object sender, ThoughtWorks.CruiseControl.Remote.Events.CancelProjectEventArgs e)
        {
            Log("starting project " + e.ProjectName);
        }
        private void Log(string message)
        {
            System.Diagnostics.Debug.WriteLine(string.Format("--> {0} {1}", DateTime.Now.ToLongTimeString(), message));
        }
        private void CheckResponse(ThoughtWorks.CruiseControl.Remote.Messages.Response value)
        {
            if (value.Result == ThoughtWorks.CruiseControl.Remote.Messages.ResponseResult.Failure)
            {
                throw new CCNet.Core.CruiseControlException(value.ConcatenateErrors());
            }
        }
    }
}
