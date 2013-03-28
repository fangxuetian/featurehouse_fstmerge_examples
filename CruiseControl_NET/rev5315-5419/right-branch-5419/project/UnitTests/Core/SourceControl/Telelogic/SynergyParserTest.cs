using System;
using System.Collections;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.Sourcecontrol.Telelogic;
namespace ThoughtWorks.CruiseControl.UnitTests.Core.Sourcecontrol.Telelogic
{
 [TestFixture]
 public sealed class SynergyParserTest
 {
  private SynergyConnectionInfo connection;
  private SynergyProjectInfo project;
  [TestFixtureSetUp]
  public void TestFixtureSetUp()
  {
   connection = new SynergyConnectionInfo();
   connection.Host = "localhost";
   connection.Database = @"\\server\share\mydb";
   connection.Delimiter = '-';
   project = new SynergyProjectInfo();
   project.ProjectSpecification = "MyProject-MyProject_Int";
  }
  [Test]
  public void ConnectionDefaults()
  {
   SynergyConnectionInfo actual = new SynergyConnectionInfo();
   Assert.IsNull(actual.Database);
   Assert.IsNull(actual.SessionId);
   Assert.AreEqual(3600, actual.Timeout);
   Assert.AreEqual("ccm.exe", actual.Executable);
   Assert.AreEqual(Environment.UserName, actual.Username);
   Assert.AreEqual("build_mgr", actual.Role);
   Assert.AreEqual('-', actual.Delimiter);
   Assert.AreEqual(Environment.ExpandEnvironmentVariables(@"%SystemDrive%\cmsynergy\%USERNAME%"), actual.HomeDirectory);
   Assert.AreEqual(Environment.ExpandEnvironmentVariables(@"%SystemDrive%\cmsynergy\u0000"), actual.ClientDatabaseDirectory);
   Assert.AreEqual(Environment.ExpandEnvironmentVariables(@"%ProgramFiles%\Telelogic\CM Synergy 6.3\bin"), actual.WorkingDirectory);
  }
  [Test]
  public void ProjectDefaults()
  {
   SynergyProjectInfo actual = new SynergyProjectInfo();
   Assert.IsNull(actual.Release);
   Assert.IsNull(actual.ProjectSpecification);
   Assert.AreEqual(0, actual.TaskFolder);
   Assert.AreEqual(DateTime.MinValue, actual.LastReconfigureTime);
   Assert.IsFalse(actual.BaseliningEnabled);
   Assert.AreEqual("Integration Testing", actual.Purpose);
  }
  [Test]
  public void CanParseNewTasks()
  {
   SynergyParser parser = new SynergyParser();
   Hashtable actual = parser.ParseTasks(SynergyMother.NewTaskInfo);
   Assert.IsNotNull(actual);
   Assert.AreEqual(6, actual.Count);
   foreach (DictionaryEntry comment in actual)
   {
    Assert.IsNotNull(comment);
    SynergyParser.SynergyTaskInfo info = (SynergyParser.SynergyTaskInfo) comment.Value;
    Assert.IsNotNull(info.TaskNumber);
    Assert.IsNotNull(info.TaskSynopsis);
    Assert.IsNotNull(info.Resolver);
   }
   if (null != actual["15"])
   {
    Assert.AreEqual("lorem ipsum dolerem ", ((SynergyParser.SynergyTaskInfo) actual["15"]).TaskSynopsis);
    Assert.AreEqual("Insulated Development projects for release PRODUCT/1.0", ((SynergyParser.SynergyTaskInfo) actual["22"]).TaskSynopsis);
    Assert.AreEqual("jdoe's Insulated Development projects", ((SynergyParser.SynergyTaskInfo) actual["21"]).TaskSynopsis);
    Assert.AreEqual("IGNORE THIS Sample Task ", ((SynergyParser.SynergyTaskInfo) actual["99"]).TaskSynopsis);
    Assert.AreEqual("the quick brown fox jumped over the lazy dog ", ((SynergyParser.SynergyTaskInfo) actual["17"]).TaskSynopsis);
    Assert.AreEqual("0123456789 ~!@#$%^&*()_=", ((SynergyParser.SynergyTaskInfo) actual["1"]).TaskSynopsis);
   }
   else
   {
    Assert.AreEqual("lorem ipsum dolerem ", ((SynergyParser.SynergyTaskInfo) actual["wwdev#15"]).TaskSynopsis);
    Assert.AreEqual("Insulated Development projects for release PRODUCT/1.0", ((SynergyParser.SynergyTaskInfo) actual["wwdev#22"]).TaskSynopsis);
    Assert.AreEqual("jdoe's Insulated Development projects", ((SynergyParser.SynergyTaskInfo) actual["wwdev#21"]).TaskSynopsis);
    Assert.AreEqual("IGNORE THIS Sample Task ", ((SynergyParser.SynergyTaskInfo) actual["wwdev#99"]).TaskSynopsis);
    Assert.AreEqual("the quick brown fox jumped over the lazy dog ", ((SynergyParser.SynergyTaskInfo) actual["wwdev#17"]).TaskSynopsis);
    Assert.AreEqual("0123456789 ~!@#$%^&*()_=", ((SynergyParser.SynergyTaskInfo) actual["wwdev#1"]).TaskSynopsis);
   }
   Assert.IsNull(actual["123456789"]);
  }
  [Test]
  public void ParseNewObjects()
  {
   ParseNewObjects(SynergyMother.NewTaskInfo, SynergyMother.NewObjects);
  }
  [Test]
  public void ParseDCMObjects()
  {
   ParseNewObjects(SynergyMother.NewDcmTaskInfo, SynergyMother.NewDCMObjects);
  }
  [Test]
  public void ParseWhenTasksAreEmpty()
  {
   SynergyParser parser = new SynergyParser();
   DateTime from_ = DateTime.Now.AddDays(-7L);
   Modification[] actual = parser.Parse(string.Empty, SynergyMother.NewObjects, from_);
   Assert.AreEqual(7, actual.Length);
   Assert.AreEqual(15, actual[0].ChangeNumber);
   Assert.AreEqual(9999, actual[6].ChangeNumber);
  }
  private void ParseNewObjects(string newTasks, string newObjects)
  {
   SynergyParser parser = new SynergyParser();
   DateTime from_ = DateTime.Now.AddDays(-7L);
   Modification[] actual = parser.Parse(newTasks, newObjects, from_);
   Assert.IsNotNull(actual);
   Assert.AreEqual(7, actual.Length);
   foreach (Modification modification in actual)
   {
    Assert.AreEqual("jdoe", modification.EmailAddress);
    Assert.AreEqual("jdoe", modification.UserName);
    Assert.IsNull(modification.Url);
   }
   Assert.AreEqual(15, actual[0].ChangeNumber);
   Assert.AreEqual(@"sourcecontrol-3", actual[0].FileName);
   Assert.AreEqual(@"$/MyProject/CruiseControl.NET/project/core", actual[0].FolderName);
   Assert.AreEqual(@"dir", actual[0].Type);
   Assert.AreEqual(@"lorem ipsum dolerem ", actual[0].Comment);
   Assert.AreEqual(21, actual[1].ChangeNumber);
   Assert.AreEqual(@"Synergy.cs-1", actual[1].FileName);
   Assert.AreEqual(@"$/MyProject/CruiseControl.NET/project/core/sourcecontrol", actual[1].FolderName);
   Assert.AreEqual(@"ms_cs", actual[1].Type);
   Assert.AreEqual("jdoe's Insulated Development projects", actual[1].Comment);
   Assert.AreEqual(22, actual[2].ChangeNumber);
   Assert.AreEqual(@"SynergyCommandBuilder.cs-1.1.1", actual[2].FileName);
   Assert.AreEqual(@"$/MyProject/CruiseControl.NET/project/core/sourcecontrol", actual[2].FolderName);
   Assert.AreEqual(@"ms_cs", actual[2].Type);
   Assert.AreEqual("Insulated Development projects for release PRODUCT/1.0", actual[2].Comment);
   Assert.AreEqual(22, actual[3].ChangeNumber);
   Assert.AreEqual(@"SynergyConnectionInfo.cs-2", actual[3].FileName);
   Assert.AreEqual(@"$/MyProject/CruiseControl.NET/project/core/sourcecontrol", actual[3].FolderName);
   Assert.AreEqual(@"ms_cs", actual[3].Type);
   Assert.AreEqual("Insulated Development projects for release PRODUCT/1.0", actual[3].Comment);
   Assert.AreEqual(1, actual[4].ChangeNumber);
   Assert.AreEqual(@"SynergyHistoryParser.cs-2.2.1", actual[4].FileName);
   Assert.AreEqual(@"$/MyProject/CruiseControl.NET/project/core/sourcecontrol", actual[4].FolderName);
   Assert.AreEqual(@"ms_cs", actual[4].Type);
   Assert.AreEqual(@"0123456789 ~!@#$%^&*()_=", actual[4].Comment);
   Assert.AreEqual(17, actual[5].ChangeNumber);
   Assert.AreEqual(@"SynergyProjectInfo.cs-1", actual[5].FileName);
   Assert.AreEqual(@"$/MyProject/CruiseControl.NET/project/core/sourcecontrol", actual[5].FolderName);
   Assert.AreEqual(@"ms_cs", actual[5].Type);
   Assert.AreEqual(@"the quick brown fox jumped over the lazy dog ", actual[5].Comment);
   Assert.AreEqual(9999, actual[6].ChangeNumber);
   Assert.AreEqual(@"NotUsed-10", actual[6].FileName);
   Assert.AreEqual(@"", actual[6].FolderName);
   Assert.AreEqual(@"dir", actual[6].Type);
   Assert.IsNull(actual[6].Comment);
  }
 }
}
