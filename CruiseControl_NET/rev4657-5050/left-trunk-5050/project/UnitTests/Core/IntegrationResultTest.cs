using System;
using System.Collections;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.Tasks;
using ThoughtWorks.CruiseControl.Remote;
using ThoughtWorks.CruiseControl.UnitTests.Core.Sourcecontrol;
namespace ThoughtWorks.CruiseControl.UnitTests.Core
{
 [TestFixture]
 public class IntegrationResultTest : CustomAssertion
 {
  private IntegrationResult result;
  [SetUp]
  protected void CreateIntegrationResult()
  {
   result = new IntegrationResult();
  }
  [Test]
  public void LastModificationDate()
  {
   Modification earlierModification = new Modification();
   earlierModification.ModifiedTime = new DateTime(0);
   Modification laterModification = new Modification();
   laterModification.ModifiedTime = new DateTime(1);
   result.Modifications = new Modification[] {earlierModification, laterModification};
   Assert.AreEqual(laterModification.ModifiedTime, result.LastModificationDate);
  }
  [Test]
  public void LastModificationDateWhenThereAreNoModifications()
  {
   DateTime yesterday = DateTime.Now.AddDays(-1).Date;
   Assert.AreEqual(yesterday, result.LastModificationDate.Date);
  }
  [Test]
  public void VerifyInitialIntegrationResult()
  {
            IntegrationResult initial = IntegrationResult.CreateInitialIntegrationResult("project", @"c:\temp", @"c:\temp");
   Assert.AreEqual("project", initial.ProjectName);
   Assert.AreEqual(IntegrationStatus.Unknown, initial.LastIntegrationStatus, "last integration status is unknown because no previous integrations exist.");
   Assert.AreEqual(IntegrationStatus.Unknown, initial.Status, "status should be unknown as integration has not run yet.");
   Assert.AreEqual(DateTime.Now.AddDays(-1).Day, initial.StartTime.Day, "assume start date is yesterday in order to detect some modifications.");
   Assert.AreEqual(DateTime.Now.Day, initial.EndTime.Day, "assume end date is today in order to detect some modifications.");
   Assert.AreEqual(@"c:\temp", initial.WorkingDirectory);
   Assert.AreEqual(IntegrationResult.InitialLabel, initial.Label);
   Assert.IsTrue(initial.IsInitial());
  }
  [Test]
  public void ShouldReturnZeroAsLastChangeNumberIfNoModifications()
  {
   Assert.AreEqual(0, result.LastChangeNumber);
  }
  [Test]
  public void ShouldReturnTheMaximumChangeNumberFromAllModificationsForLastChangeNumber()
  {
   Modification mod1 = new Modification();
   mod1.ChangeNumber = 10;
   Modification mod2 = new Modification();
   mod2.ChangeNumber = 20;
   result.Modifications = new Modification[] {mod1};
   Assert.AreEqual(10, result.LastChangeNumber);
   result.Modifications = new Modification[] {mod1, mod2};
   Assert.AreEqual(20, result.LastChangeNumber);
   result.Modifications = new Modification[] {mod2, mod1};
   Assert.AreEqual(20, result.LastChangeNumber);
  }
  [Test]
  public void ShouldNotRunBuildIfThereAreNoModifications()
  {
   result.Modifications = new Modification[0];
   Assert.IsFalse(result.ShouldRunBuild());
  }
  [Test]
  public void ShouldRunBuildIfThereAreModifications()
  {
   Modification modification = ModificationMother.CreateModification("foo", DateTime.Now.AddSeconds(-2));
   result.Modifications = new Modification[] {modification};
   Assert.IsTrue(result.ShouldRunBuild());
  }
  [Test]
  public void ShouldRunBuildIfInForcedCondition()
  {
   result.BuildCondition = BuildCondition.ForceBuild;
   Assert.IsTrue(result.ShouldRunBuild());
  }
  [Test]
  public void TaskOutputShouldAggregateOutputOfTaskResults()
  {
   result.AddTaskResult("<foo/>");
   result.AddTaskResult("<bar/>");
   Assert.AreEqual("<foo/><bar/>", result.TaskOutput);
  }
  [Test]
  public void ShouldBaseRelativePathFromArtifactsDirectory()
  {
   result.ArtifactDirectory = @"c:\";
   Assert.AreEqual(@"c:\hello.bat", result.BaseFromArtifactsDirectory("hello.bat"));
  }
  [Test]
  public void ShouldNotReBaseRelativeToArtifactsDirectoryForAbsolutePath()
  {
   result.ArtifactDirectory = @"c:\";
   Assert.AreEqual(@"d:\hello.bat", result.BaseFromArtifactsDirectory(@"d:\hello.bat"));
  }
  [Test]
  public void ShouldBaseRelativePathFromWorkingDirectory()
  {
   result.WorkingDirectory = @"c:\";
   Assert.AreEqual(@"c:\hello.bat", result.BaseFromWorkingDirectory("hello.bat"));
  }
  [Test]
  public void ShouldNotReBaseRelativeToWorkingDirectoryForAbsolutePath()
  {
   result.WorkingDirectory = @"c:\";
   Assert.AreEqual(@"d:\hello.bat", result.BaseFromWorkingDirectory(@"d:\hello.bat"));
  }
  [Test]
  public void ShouldSucceedIfContainsOnlySuccessfulTaskResults()
  {
   result.AddTaskResult(new ProcessTaskResult(ProcessResultFixture.CreateSuccessfulResult()));
   Assert.IsTrue(result.Succeeded);
  }
  [Test]
  public void ShouldHaveFailedIfContainsFailedTaskResults()
  {
   result.AddTaskResult(new ProcessTaskResult(ProcessResultFixture.CreateNonZeroExitCodeResult()));
   result.AddTaskResult(new ProcessTaskResult(ProcessResultFixture.CreateSuccessfulResult()));
   Assert.IsTrue(result.Failed);
  }
  [Test]
  public void ShouldHaveExceptionStatusIfExceptionHasBeenThrown()
  {
   result.ExceptionResult = new Exception("build blew up");
   result.AddTaskResult(new ProcessTaskResult(ProcessResultFixture.CreateSuccessfulResult()));
   Assert.AreEqual(IntegrationStatus.Exception, result.Status);
  }
  [Test]
  public void MapIntegrationProperties()
  {
            result = new IntegrationResult("project", @"c:\workingdir\", @"c:\artifactdir\", new IntegrationRequest(BuildCondition.IfModificationExists, "myTrigger"), new IntegrationSummary(IntegrationStatus.Unknown, "foo", "foo", DateTime.MinValue));
   result.Label = "label23";
   result.ArtifactDirectory = @"c:\artifactdir\";
   result.StartTime = new DateTime(2005,06,06,08,45,00);
   result.ProjectUrl = "http:";
            result.FailureUsers.Add("user");
            Modification mods = new Modification();
            mods.UserName = "John";
            result.Modifications = new Modification[] { mods };
   Assert.AreEqual(15, result.IntegrationProperties.Count);
   Assert.AreEqual("project", result.IntegrationProperties[IntegrationPropertyNames.CCNetProject]);
   Assert.AreEqual("http://localhost/ccnet2", result.IntegrationProperties[IntegrationPropertyNames.CCNetProjectUrl]);
            Assert.AreEqual("label23", result.IntegrationProperties[IntegrationPropertyNames.CCNetLabel]);
            Assert.AreEqual(23, result.IntegrationProperties[IntegrationPropertyNames.CCNetNumericLabel]);
            Assert.AreEqual(@"c:\artifactdir\", result.IntegrationProperties[IntegrationPropertyNames.CCNetArtifactDirectory]);
            Assert.AreEqual(@"c:\workingdir\", result.IntegrationProperties[IntegrationPropertyNames.CCNetWorkingDirectory]);
            Assert.AreEqual("2005-06-06", result.IntegrationProperties[IntegrationPropertyNames.CCNetBuildDate]);
            Assert.AreEqual("08:45:00", result.IntegrationProperties[IntegrationPropertyNames.CCNetBuildTime]);
            Assert.AreEqual(BuildCondition.IfModificationExists, result.IntegrationProperties[IntegrationPropertyNames.CCNetBuildCondition]);
            Assert.AreEqual(IntegrationStatus.Unknown, result.IntegrationProperties[IntegrationPropertyNames.CCNetIntegrationStatus]);
            Assert.AreEqual(IntegrationStatus.Unknown, result.IntegrationProperties[IntegrationPropertyNames.CCNetLastIntegrationStatus]);
            Assert.AreEqual("myTrigger", result.IntegrationProperties[IntegrationPropertyNames.CCNetRequestSource]);
            Assert.AreEqual(@"c:\artifactdir\project_ListenFile.xml", result.IntegrationProperties[IntegrationPropertyNames.CCNetListenerFile]);
            ArrayList failureUsers = result.IntegrationProperties[IntegrationPropertyNames.CCNetFailureUsers] as ArrayList;
            Assert.IsNotNull(failureUsers);
            Assert.AreEqual(1, failureUsers.Count);
            Assert.AreEqual("user", failureUsers[0]);
            ArrayList Modifiers = result.IntegrationProperties[IntegrationPropertyNames.CCNetModifyingUsers] as ArrayList;
            Assert.IsNotNull(Modifiers);
            Assert.AreEqual(1, Modifiers.Count);
            Assert.AreEqual("John", Modifiers[0]);
  }
  [Test]
  public void VerifyIntegrationArtifactDir()
  {
   result = new IntegrationResult();
   result.ArtifactDirectory = @"c:\artifacts";
   result.Label = "1.2.3.4";
   Assert.AreEqual(@"c:\artifacts\1.2.3.4", result.IntegrationArtifactDirectory);
  }
  [Test]
  public void NumericLabel()
  {
   result = new IntegrationResult();
   result.Label = "23";
   Assert.AreEqual(23, result.NumericLabel);
  }
  [Test]
  public void NumericLabelWithPrefix()
  {
   result = new IntegrationResult();
   result.Label = "Prefix23";
   Assert.AreEqual(23, result.NumericLabel);
  }
  [Test]
  public void NumericLabelWithNumericPrefix()
  {
   result = new IntegrationResult();
   result.Label = "R3SX23";
   Assert.AreEqual(23, result.NumericLabel);
  }
  [Test]
  public void NumericLabelTextOnly()
  {
   result = new IntegrationResult();
   result.Label = "foo";
   Assert.AreEqual(0, result.NumericLabel);
  }
  [Test]
  public void CanGetPreviousState()
  {
   IntegrationSummary expectedSummary = new IntegrationSummary(IntegrationStatus.Exception, "foo", "foo", DateTime.MinValue);
            result = new IntegrationResult("project", "c:\\workingDir", @"c:\artifactdir\", IntegrationRequest.NullRequest, expectedSummary);
   Assert.AreEqual(new IntegrationSummary(IntegrationStatus.Exception, "foo", "foo", DateTime.MinValue), result.LastIntegration);
  }
        [Test]
        public void ShouldReturnPreviousLabelAsLastSuccessfulIntegrationLabelIfFailed()
        {
            result.AddTaskResult(new ProcessTaskResult(ProcessResultFixture.CreateNonZeroExitCodeResult()));
            result.LastSuccessfulIntegrationLabel = "1";
            result.Label = "2";
            Assert.AreEqual("1", result.LastSuccessfulIntegrationLabel);
        }
        [Test]
        public void ShouldReturnCurrentLabelAsLastSuccessfulIntegrationLabelIfSuccessful()
        {
            result.AddTaskResult(new ProcessTaskResult(ProcessResultFixture.CreateSuccessfulResult()));
            result.LastSuccessfulIntegrationLabel = "1";
            result.Label = "2";
            Assert.AreEqual("2", result.LastSuccessfulIntegrationLabel);
        }
 }
}
