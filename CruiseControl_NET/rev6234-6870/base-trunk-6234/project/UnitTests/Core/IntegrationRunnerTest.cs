using System;
using System.IO;
using NMock;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.Sourcecontrol;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
using ThoughtWorks.CruiseControl.UnitTests.UnitTestUtils;
using System.Collections.Generic;
namespace ThoughtWorks.CruiseControl.UnitTests.Core
{
    [TestFixture]
    public class IntegrationRunnerTest : IntegrationFixture
    {
        private IntegrationRunner runner;
        private IMock resultManagerMock;
        private IMock targetMock;
        private IMock resultMock;
        private IMock lastResultMock;
        private IMock sourceControlMock;
        private IMock quietPeriodMock;
        private IIntegrationResult result;
        private IIntegrationResult lastResult;
        private Modification[] modifications;
        private DateTime endTime;
        private IntegrationRequest request;
        private Mockery mockery;
        [SetUp]
        public void Setup()
        {
            mockery = new Mockery();
            targetMock = mockery.NewDynamicMock(typeof(IIntegrationRunnerTarget));
            resultManagerMock = mockery.NewDynamicMock(typeof(IIntegrationResultManager));
            quietPeriodMock = mockery.NewDynamicMock(typeof(IQuietPeriod));
            runner = new IntegrationRunner((IIntegrationResultManager)resultManagerMock.MockInstance,
                                           (IIntegrationRunnerTarget)targetMock.MockInstance,
                                           (IQuietPeriod)quietPeriodMock.MockInstance);
            request = ModificationExistRequest();
            endTime = new DateTime(2005, 2, 1);
            modifications = new Modification[] { new Modification() };
            resultMock = mockery.NewDynamicMock(typeof(IIntegrationResult));
            resultMock.SetupResult("WorkingDirectory", TempFileUtil.GetTempPath("workingDir"));
            resultMock.SetupResult("ArtifactDirectory", TempFileUtil.GetTempPath("artifactDir"));
            resultMock.SetupResult("BuildProgressInformation", new ThoughtWorks.CruiseControl.Core.Util.BuildProgressInformation("",string.Empty));
            resultMock.SetupResult("IntegrationProperties", new Dictionary<string, string>());
            result = (IIntegrationResult)resultMock.MockInstance;
            lastResultMock = mockery.NewDynamicMock(typeof(IIntegrationResult));
            lastResult = (IIntegrationResult)lastResultMock.MockInstance;
        }
        [TearDown]
        public void Teardown()
        {
            TempFileUtil.DeleteTempDir("workingDir");
        }
        [Test]
        public void ShouldNotRunBuildIfResultShouldNotBuild()
        {
            SetupPreambleExpections();
            resultMock.ExpectAndReturn("ShouldRunBuild", false);
            targetMock.Expect("Activity", ProjectActivity.Sleeping);
            IIntegrationResult returnedResult = runner.Integrate(request);
            Assert.AreEqual(result, returnedResult);
            Assert.IsTrue(Directory.Exists(result.WorkingDirectory));
            Assert.IsTrue(Directory.Exists(result.ArtifactDirectory));
            mockery.Verify();
        }
        [Test]
        public void ShouldRunBuildIfResultShouldBuild()
        {
            SetupPreambleExpections();
            SetupShouldBuildExpectations();
            SetupBuildPassExpectations();
            IIntegrationResult returnedResult = runner.Integrate(request);
            Assert.AreEqual(result, returnedResult);
            mockery.Verify();
        }
        [Test]
        public void ShouldStillPublishResultsIfPrebuildThrowsException()
        {
            SetupPreambleExpections();
            resultMock.ExpectAndReturn("ShouldRunBuild", true);
            resultMock.ExpectAndReturn("LastIntegrationStatus", IntegrationStatus.Success);
            targetMock.Expect("Activity", ProjectActivity.Building);
            sourceControlMock.Expect("GetSource", result);
            targetMock.ExpectAndThrow("Prebuild", new CruiseControlException("expected exception"), result);
            resultMock.Expect("ExceptionResult");
            resultMock.Expect("MarkEndTime");
            targetMock.Expect("Activity", ProjectActivity.Sleeping);
            resultMock.ExpectAndReturn("EndTime", endTime);
            resultMock.ExpectAndReturn("Status", IntegrationStatus.Exception);
            targetMock.Expect("PublishResults", result);
            resultManagerMock.Expect("FinishIntegration");
            runner.Integrate(ModificationExistRequest());
            mockery.Verify();
        }
        private void SetupPreambleExpections()
        {
            targetMock.Expect("Activity", ProjectActivity.CheckingModifications);
            resultManagerMock.ExpectAndReturn("StartNewIntegration", result, request);
            resultMock.Expect("MarkStartTime");
            resultManagerMock.ExpectAndReturn("LastIntegrationResult", lastResult);
            sourceControlMock = new DynamicMock(typeof(ISourceControl));
            sourceControlMock.Strict = true;
            targetMock.SetupResult("SourceControl", sourceControlMock.MockInstance);
            quietPeriodMock.ExpectAndReturn("GetModifications", modifications, sourceControlMock.MockInstance, lastResult, result);
            resultMock.ExpectAndReturn("Modifications", modifications);
        }
        private void SetupShouldBuildExpectations()
        {
            resultMock.ExpectAndReturn("ShouldRunBuild", true);
            resultMock.ExpectAndReturn("LastIntegrationStatus",IntegrationStatus.Success);
            targetMock.Expect("Activity", ProjectActivity.Building);
            sourceControlMock.Expect("GetSource", result);
            targetMock.Expect("Prebuild", result);
            resultMock.ExpectAndReturn("Failed", false);
            targetMock.Expect("Run", result);
            resultMock.Expect("MarkEndTime");
            targetMock.Expect("Activity", ProjectActivity.Sleeping);
            resultMock.ExpectAndReturn("EndTime", endTime);
        }
        private void SetupBuildPassExpectations()
        {
            resultMock.ExpectAndReturn("Status", IntegrationStatus.Success);
            sourceControlMock.Expect("LabelSourceControl", result);
            targetMock.Expect("PublishResults", result);
            resultManagerMock.Expect("FinishIntegration");
        }
    }
}
