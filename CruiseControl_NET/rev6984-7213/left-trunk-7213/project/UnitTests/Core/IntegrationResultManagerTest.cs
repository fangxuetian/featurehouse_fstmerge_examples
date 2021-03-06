using NMock;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.State;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.UnitTests.Core
{
 [TestFixture]
 public class IntegrationResultManagerTest : IntegrationFixture
 {
  private IMock mockStateManager;
  private Project project;
  private IntegrationResultManager manager;
  [SetUp]
  public void SetUp()
  {
   mockStateManager = new DynamicMock(typeof(IStateManager));
   project = CreateProject();
   manager = new IntegrationResultManager(project);
  }
  [TearDown]
  public void Verify()
  {
   mockStateManager.Verify();
  }
  [Test]
  public void StartNewIntegrationShouldCreateNewIntegrationResultAndProperlyPopulate()
  {
   ExpectToLoadState(IntegrationResultMother.CreateSuccessful("success"));
   IIntegrationResult result = manager.StartNewIntegration(ForceBuildRequest());
   Assert.AreEqual("project", result.ProjectName);
   Assert.AreEqual(@"c:\temp", result.WorkingDirectory);
   Assert.AreEqual(BuildCondition.ForceBuild, result.BuildCondition);
   Assert.AreEqual("success", result.Label);
   Assert.AreEqual(project.ArtifactDirectory, result.ArtifactDirectory);
   Assert.AreEqual(project.WebURL, result.ProjectUrl);
   Assert.AreEqual("success", result.LastSuccessfulIntegrationLabel);
            Assert.AreEqual(Source, result.IntegrationRequest.Source);
  }
  [Test]
  public void LastIntegrationResultShouldBeLoadedOnlyOnceFromStateManager()
  {
   IntegrationResult expected = new IntegrationResult();
   ExpectToLoadState(expected);
   IIntegrationResult actual = manager.LastIntegrationResult;
   Assert.AreEqual(expected, actual);
   actual = manager.LastIntegrationResult;
   Assert.AreEqual(expected, actual);
  }
  [Test]
  public void SavingCurrentIntegrationShouldSetItToLastIntegrationResult()
  {
   IIntegrationResult lastResult = new IntegrationResult();
   ExpectToLoadState(lastResult);
   IIntegrationResult expected = manager.StartNewIntegration(ModificationExistRequest());
   Assert.AreEqual(lastResult, manager.LastIntegrationResult);
   mockStateManager.Expect("SaveState", expected);
   manager.FinishIntegration();
   Assert.AreEqual(expected, manager.LastIntegrationResult);
  }
     [Test]
     public void InitialBuildShouldBeForced()
     {
            mockStateManager.ExpectAndReturn("HasPreviousState", false, "project");
            IIntegrationResult expected = manager.StartNewIntegration(ModificationExistRequest());
         Assert.AreEqual(BuildCondition.ForceBuild, expected.BuildCondition);
        }
        [Test]
        public void FailedIntegrationShouldAddModificationUsersToFailedUsers()
        {
            IIntegrationResult lastResult = IntegrationResultMother.CreateFailed();
            lastResult.FailureUsers.Add("user1");
            ExpectToLoadState(lastResult);
            IIntegrationResult newResult = manager.StartNewIntegration(ModificationExistRequest());
            Assert.AreEqual(1, newResult.FailureUsers.Count, "Mismatched count of inherited FailureUsers");
            Modification modification = new Modification();
            modification.UserName = "user";
            newResult.Modifications = new Modification[] { modification };
            newResult.Status = IntegrationStatus.Failure;
            mockStateManager.Expect("SaveState", newResult);
            manager.FinishIntegration();
            Assert.AreEqual(2, newResult.FailureUsers.Count, "Mismatched count of resulting FailureUsers");
        }
        [Test]
        public void SuccessfulIntegrationShouldClearFailedUsersOnNextIntegration()
        {
            IIntegrationResult result1 = IntegrationResultMother.CreateFailed();
            result1.FailureUsers.Add("user1");
            ExpectToLoadState(result1);
            IIntegrationResult result2 = manager.StartNewIntegration(ModificationExistRequest());
            Assert.AreEqual(1, result2.FailureUsers.Count);
            Modification modification = new Modification();
            modification.UserName = "user";
            result2.Modifications = new Modification[] { modification };
            result2.Status = IntegrationStatus.Success;
            mockStateManager.Expect("SaveState", result2);
            manager.FinishIntegration();
            Assert.AreEqual(1, result2.FailureUsers.Count);
            IIntegrationResult result3 = manager.StartNewIntegration(ModificationExistRequest());
            Assert.AreEqual(0, result3.FailureUsers.Count);
        }
  private void ExpectToLoadState(IIntegrationResult result)
  {
   mockStateManager.ExpectAndReturn("HasPreviousState", true, "project");
   mockStateManager.ExpectAndReturn("LoadState", result, "project");
  }
  private Project CreateProject()
  {
   project = new Project();
   project.Name = "project";
   project.ConfiguredWorkingDirectory = @"c:\temp";
   project.StateManager = (IStateManager) mockStateManager.MockInstance;
   project.ConfiguredArtifactDirectory = project.ConfiguredWorkingDirectory;
   return project;
  }
 }
}
