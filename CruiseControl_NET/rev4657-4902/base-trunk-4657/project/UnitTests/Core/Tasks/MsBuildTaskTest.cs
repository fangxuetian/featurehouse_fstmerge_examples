using System.IO;
using Exortech.NetReflector;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.Tasks;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.UnitTests.Core.Tasks
{
 [TestFixture]
 public class MsBuildTaskTest : ProcessExecutorTestFixtureBase
 {
  private string logfile;
  private IIntegrationResult result;
  private MsBuildTask task;
  [SetUp]
  protected void SetUp()
  {
   CreateProcessExecutorMock(MsBuildTask.defaultExecutable);
   result = IntegrationResult();
   result.Label = "1.0";
   result.ArtifactDirectory = Path.GetTempPath();
   logfile = Path.Combine(result.ArtifactDirectory, MsBuildTask.LogFilename);
   TempFileUtil.DeleteTempFile(logfile);
   task = new MsBuildTask((ProcessExecutor) mockProcessExecutor.MockInstance);
  }
  [TearDown]
  protected void TearDown()
  {
   Verify();
  }
  [Test]
  public void ExecuteSpecifiedProject()
  {
   string args = "/nologo /t:target1;target2 " + IntegrationProperties() + " /p:Configuration=Release myproject.sln" + DefaultLogger();
   ExpectToExecuteArguments(args);
   task.ProjectFile = "myproject.sln";
   task.Targets = "target1;target2";
   task.BuildArgs = "/p:Configuration=Release";
   task.Timeout = 600;
   task.Run(result);
   Assert.AreEqual(1, result.TaskResults.Count);
   Assert.AreEqual(IntegrationStatus.Success, result.Status);
   Assert.AreEqual(ProcessResultOutput, result.TaskOutput);
  }
  [Test]
  public void AddQuotesAroundProjectsWithSpacesAndHandleNoSpecifiedTargets()
  {
   ExpectToExecuteArguments(@"/nologo " + IntegrationProperties() + @" ""my project.proj""" + DefaultLogger());
   task.ProjectFile = "my project.proj";
   task.Run(result);
  }
  [Test]
  public void AddQuotesAroundTargetsWithSpaces()
  {
   ExpectToExecuteArguments(@"/nologo /t:first;""next task"" " + IntegrationProperties() + DefaultLogger());
   task.Targets = "first;next task";
   task.Run(result);
  }
  [Test]
  public void AddQuotesAroundPropertiesWithSpaces()
  {
            string expectedProperties = string.Format(@"/p:CCNetArtifactDirectory={2};CCNetBuildCondition=IfModificationExists;CCNetBuildDate={0};CCNetBuildTime={1};CCNetFailureUsers=;CCNetIntegrationStatus=Success;CCNetLabel=""My Label"";CCNetLastIntegrationStatus=Success;CCNetListenerFile={3};CCNetModifyingUsers=;CCNetNumericLabel=0;CCNetProject=test;CCNetRequestSource=foo;CCNetWorkingDirectory=c:\source\", testDateString, testTimeString, StringUtil.AutoDoubleQuoteString(result.ArtifactDirectory), StringUtil.AutoDoubleQuoteString(Path.GetTempPath() + "test_ListenFile.xml"));
            ExpectToExecuteArguments(@"/nologo " + expectedProperties + DefaultLogger());
   result.Label = @"My Label";
   task.Run(result);
  }
  [Test]
  public void DoNotAddQuotesAroundBuildArgs()
  {
   ExpectToExecuteArguments(@"/nologo " + IntegrationProperties() + @" /noconsolelogger /p:Configuration=Debug" + DefaultLogger());
   task.BuildArgs = "/noconsolelogger /p:Configuration=Debug";
   task.Run(result);
  }
  [Test]
  public void RebaseFromWorkingDirectory()
  {
   ProcessInfo info = NewProcessInfo("/nologo " + IntegrationProperties() + DefaultLogger());
   info.WorkingDirectory = Path.Combine(DefaultWorkingDirectory, "src");
   ExpectToExecute(info);
   task.WorkingDirectory = "src";
   task.Run(result);
  }
  [Test]
  [ExpectedException(typeof(BuilderException))]
  public void TimedOutExecutionShouldCauseBuilderException()
  {
   ExpectToExecuteAndReturn(TimedOutProcessResult());
   task.Run(result);
  }
  [Test]
  public void TimedOutExecutionShouldFailBuild()
  {
   try
   {
    ExpectToExecuteAndReturn(TimedOutProcessResult());
    task.Run(result);
   }
   catch (BuilderException)
   {
   }
   Assert.AreEqual(IntegrationStatus.Failure, result.Status);
  }
  [Test]
  public void ShouldAutomaticallyMergeTheBuildOutputFile()
  {
   TempFileUtil.CreateTempXmlFile(logfile, "<output/>");
   ExpectToExecuteAndReturn(SuccessfulProcessResult());
   task.Run(result);
   Assert.AreEqual(2, result.TaskResults.Count);
   Assert.AreEqual("<output/>" + ProcessResultOutput, result.TaskOutput);
   Assert.IsTrue(result.Succeeded);
  }
  [Test]
  public void ShouldFailOnFailedProcessResult()
  {
   TempFileUtil.CreateTempXmlFile(logfile, "<output/>");
   ExpectToExecuteAndReturn(FailedProcessResult());
   task.Run(result);
   Assert.AreEqual(2, result.TaskResults.Count);
   Assert.AreEqual("<output/>" + ProcessResultOutput, result.TaskOutput);
   Assert.IsTrue(result.Failed);
  }
  [Test]
  public void PopulateFromConfiguration()
  {
   const string xml = @"<msbuild>
 <executable>C:\WINDOWS\Microsoft.NET\Framework\v2.0.50215\MSBuild.exe</executable>
 <workingDirectory>C:\dev\ccnet</workingDirectory>
 <projectFile>CCNet.sln</projectFile>
 <buildArgs>/p:Configuration=Debug /v:diag</buildArgs>
 <targets>Build;Test</targets>
 <timeout>15</timeout>
 <logger>Kobush.Build.Logging.XmlLogger,Kobush.MSBuild.dll;buildresult.xml</logger>
</msbuild>";
   task = (MsBuildTask) NetReflector.Read(xml);
   Assert.AreEqual(@"C:\WINDOWS\Microsoft.NET\Framework\v2.0.50215\MSBuild.exe", task.Executable);
   Assert.AreEqual(@"C:\dev\ccnet", task.WorkingDirectory);
   Assert.AreEqual("CCNet.sln", task.ProjectFile);
   Assert.AreEqual("Build;Test", task.Targets);
   Assert.AreEqual("/p:Configuration=Debug /v:diag", task.BuildArgs);
   Assert.AreEqual(15, task.Timeout);
   Assert.AreEqual("Kobush.Build.Logging.XmlLogger,Kobush.MSBuild.dll;buildresult.xml", task.Logger);
  }
  [Test]
  public void PopulateFromMinimalConfiguration()
  {
   task = (MsBuildTask) NetReflector.Read("<msbuild />");
   Assert.AreEqual(defaultExecutable, task.Executable);
   Assert.AreEqual(MsBuildTask.DefaultTimeout, task.Timeout);
   Assert.AreEqual(MsBuildTask.DefaultLogger, task.Logger);
  }
  private string DefaultLogger()
  {
   string defaultLogger;
   if (MsBuildTask.DefaultLogger == string.Empty)
    defaultLogger = StringUtil.AutoDoubleQuoteString(string.Format("{0}{1}ThoughtWorks.CruiseControl.MsBuild.dll", Path.GetDirectoryName(System.Reflection.Assembly.GetAssembly(typeof(MsBuildTask)).Location), Path.DirectorySeparatorChar));
   else
    defaultLogger = MsBuildTask.DefaultLogger;
   return string.Format(@" /l:{0};{1}", StringUtil.AutoDoubleQuoteString(defaultLogger), StringUtil.AutoDoubleQuoteString(logfile));
  }
  private string IntegrationProperties()
  {
            return string.Format(@"/p:CCNetArtifactDirectory={3};CCNetBuildCondition=IfModificationExists;CCNetBuildDate={1};CCNetBuildTime={2};CCNetFailureUsers=;CCNetIntegrationStatus=Success;CCNetLabel=1.0;CCNetLastIntegrationStatus=Success;CCNetListenerFile={4};CCNetModifyingUsers=;CCNetNumericLabel=0;CCNetProject=test;CCNetRequestSource=foo;CCNetWorkingDirectory={0}", StringUtil.AutoDoubleQuoteString(DefaultWorkingDirectory), testDateString, testTimeString, StringUtil.AutoDoubleQuoteString(result.ArtifactDirectory), StringUtil.AutoDoubleQuoteString(Path.GetTempPath() + "test_ListenFile.xml"));
  }
 }
}
