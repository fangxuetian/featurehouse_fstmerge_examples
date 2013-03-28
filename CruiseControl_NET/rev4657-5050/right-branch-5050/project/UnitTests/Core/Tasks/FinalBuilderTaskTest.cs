using System;
using System.IO;
using Exortech.NetReflector;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.Tasks;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
using NMock;
namespace ThoughtWorks.CruiseControl.UnitTests.Core.Tasks
{
 [TestFixture]
 public class FinalBuilderTaskTest : ProcessExecutorTestFixtureBase
 {
  private IIntegrationResult _result;
  private FinalBuilderTask _task;
  private IMock _mockRegistry;
  [SetUp]
  protected void SetUp()
  {
   _mockRegistry = new DynamicMock(typeof (IRegistry));
   CreateProcessExecutorMock(@"C:\Dummy\FBCmd.exe");
   DefaultWorkingDirectory = @"C:\Dummy";
   _result = IntegrationResult();
   _result.Label = "1.0";
   _result.ArtifactDirectory = Path.GetTempPath();
   _task = new FinalBuilderTask((IRegistry) _mockRegistry.MockInstance, (ProcessExecutor) mockProcessExecutor.MockInstance);
  }
  [TearDown]
  protected void TearDown()
  {
   Verify();
   _mockRegistry.Verify();
  }
  [Test]
  public void BuildCommandLine()
  {
   const string expectedArgs = @"/B /S /Vvar1=value1;var2=""value 2"" /PC:\Dummy\TestProject.fbz5";
   ExpectToExecuteArguments(expectedArgs);
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder5.exe", @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
   _task.FBVariables = new FinalBuilderTask.FBVariable[2];
   _task.FBVariables[0] = new FinalBuilderTask.FBVariable("var1", "value1");
   _task.FBVariables[1] = new FinalBuilderTask.FBVariable("var2", "value 2");
   _task.ProjectFile = @"C:\Dummy\TestProject.fbz5";
   _task.ShowBanner = false;
   _task.DontWriteToLog = true;
   _task.Timeout = 600;
   _task.Run(_result);
   Assert.AreEqual(1, _result.TaskResults.Count);
   Assert.AreEqual(IntegrationStatus.Success, _result.Status);
   Assert.AreEqual(ProcessResultOutput, _result.TaskOutput);
  }
  [Test]
  public void DoubleQuoteSpacesinPaths()
  {
   const string expectedArgs = @"/P""C:\Dummy\Another Directory\TestProject.fbz5""";
   DefaultWorkingDirectory = @"C:\Dummy\Another Directory";
   ExpectToExecuteArguments(expectedArgs);
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder5.exe", @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
   _task.ShowBanner = true;
   _task.ProjectFile = @"C:\Dummy\Another Directory\TestProject.fbz5";
   _task.Timeout = 600;
   _task.Run(_result);
   Assert.AreEqual(1, _result.TaskResults.Count);
   Assert.AreEqual(IntegrationStatus.Success, _result.Status);
   Assert.AreEqual(ProcessResultOutput, _result.TaskOutput);
  }
  [Test]
  public void PopulateFromCompleteConfiguration()
  {
   const string xmlConfig = @"<FinalBuilder>
    <ProjectFile>C:\Dummy\Project.fbz3</ProjectFile>
     <ShowBanner>false</ShowBanner>
     <FBVariables>
      <FBVariable name=""MyVariable"" value=""SomeValue"" />
     </FBVariables>
    <FBVersion>3</FBVersion>
     <FBCMDPath>C:\Program Files\MyFinalBuilderPath\FBCMD.EXE</FBCMDPath>
     <DontWriteToLog>true</DontWriteToLog>
     <Timeout>100</Timeout>
    </FinalBuilder>";
   NetReflector.Read(xmlConfig, _task);
   Assert.AreEqual(@"C:\Dummy\Project.fbz3", _task.ProjectFile);
   Assert.AreEqual(false, _task.ShowBanner);
   Assert.AreEqual(1, _task.FBVariables.Length);
   Assert.AreEqual("MyVariable", _task.FBVariables[0].Name);
   Assert.AreEqual("SomeValue", _task.FBVariables[0].Value);
   Assert.AreEqual(3, _task.FBVersion);
   Assert.AreEqual(@"C:\Program Files\MyFinalBuilderPath\FBCMD.EXE", _task.FBCMDPath);
   Assert.AreEqual(100, _task.Timeout);
  }
  [Test]
  public void PopulateFromMinimalConfiguration()
  {
   const string xmlConfig = @"<FinalBuilder>
        <ProjectFile>C:\Dummy\Project.fbz5</ProjectFile>
        </FinalBuilder>";
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder5.exe", @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
   NetReflector.Read(xmlConfig, _task);
   Assert.AreEqual(@"C:\Dummy\Project.fbz5", _task.ProjectFile);
   Assert.AreEqual(5, _task.FBVersion);
   Assert.AreEqual(@"C:\Dummy\FBCmd.exe", _task.FBCMDPath);
  }
  [Test]
  public void AutodetectFB5Path()
  {
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder5.exe", @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
   _task.ProjectFile = @"C:\Dummy\Project.fbz5";
   Assert.AreEqual(5, _task.FBVersion);
   Assert.AreEqual(@"C:\Dummy\FBCmd.exe", _task.FBCMDPath);
  }
  [Test]
  public void AutodetectFB4Path()
  {
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder4.exe", @"SOFTWARE\VSoft\FinalBuilder\4.0", "Location");
   _task.ProjectFile = @"C:\Dummy\Project.fbz4";
   Assert.AreEqual(4, _task.FBVersion);
   Assert.AreEqual(@"C:\Dummy\FBCmd.exe", _task.FBCMDPath);
  }
  [Test]
  public void AutodetectFB3Path()
  {
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder3.exe", @"SOFTWARE\VSoft\FinalBuilder\3.0", "Location");
   _task.ProjectFile = @"C:\Dummy\Project.fbz3";
   Assert.AreEqual(3, _task.FBVersion);
   Assert.AreEqual(@"C:\Dummy\FB3Cmd.exe", _task.FBCMDPath);
  }
  [Test]
  [ExpectedException(typeof(BuilderException),"Finalbuilder version could not be autodetected from_ project file name.")]
  public void InvalidProjectFileName()
  {
   ExpectThatExecuteWillNotBeCalled();
   _mockRegistry.ExpectNoCall("GetLocalMachineSubKeyValue", new Type[] { typeof(string), typeof(string) });
   NetReflector.Read(@"<FinalBuilder>
    <ProjectFile>C:\Dummy\Project.txt</ProjectFile>
   </FinalBuilder>", _task);
   _task.Run(_result);
  }
  [Test]
  [ExpectedException(typeof(BuilderException),"Path to Finalbuilder 5 command line executable could not be found.")]
  public void FinalBuilderIsNotInstalled()
  {
   ExpectThatExecuteWillNotBeCalled();
   _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", null, @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
   _task.ProjectFile = @"C:\Dummy\Project.fbz5";
   _task.Run(_result);
  }
  [Test]
  [ExpectedException(typeof(NetReflectorException))]
  public void RequiredPropertiesNotProvided()
  {
   NetReflector.Read(@"<FinalBuilder />", _task);
  }
        [Test]
        public void TemporaryLogFile()
        {
            const string expectedArgs = @"/B /TL /PC:\Dummy\TestProject.fbz5";
            ExpectToExecuteArguments(expectedArgs);
            _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder5.exe", @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
            _task.ProjectFile = @"C:\Dummy\TestProject.fbz5";
            _task.UseTemporaryLogFile = true;
            _task.Timeout = 600;
            _task.Run(_result);
            Assert.AreEqual(1, _result.TaskResults.Count);
            Assert.AreEqual(IntegrationStatus.Success, _result.Status);
            Assert.AreEqual(ProcessResultOutput, _result.TaskOutput);
        }
        [Test]
        public void TemporaryLogFileOverridesDontLogToOutput()
        {
            const string expectedArgs = @"/B /TL /PC:\Dummy\TestProject.fbz5";
            ExpectToExecuteArguments(expectedArgs);
            _mockRegistry.ExpectAndReturn("GetLocalMachineSubKeyValue", @"C:\Dummy\FinalBuilder5.exe", @"SOFTWARE\VSoft\FinalBuilder\5.0", "Location");
            _task.ProjectFile = @"C:\Dummy\TestProject.fbz5";
            _task.UseTemporaryLogFile = true;
            _task.DontWriteToLog = true;
            _task.Timeout = 600;
            _task.Run(_result);
            Assert.AreEqual(1, _result.TaskResults.Count);
            Assert.AreEqual(IntegrationStatus.Success, _result.Status);
            Assert.AreEqual(ProcessResultOutput, _result.TaskOutput);
        }
 }
}
