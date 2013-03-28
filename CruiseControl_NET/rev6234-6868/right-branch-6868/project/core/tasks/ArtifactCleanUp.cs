using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Publishers;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Tasks
{
 [ReflectorType("artifactcleanup")]
    internal class ArtifactCleanUpTask
        : TaskBase
 {
  public enum CleanUpMethod
  {
   KeepLastXBuilds,
   DeleteBuildsOlderThanXDays,
   KeepMaximumXHistoryDataEntries,
   DeleteSubDirsOlderThanXDays,
   KeepLastXSubDirs
  }
  private CleanUpMethod cleanUpMethod;
  private int cleanUpValue;
  [ReflectorProperty("cleanUpMethod", Required = true)]
  public CleanUpMethod CleaningUpMethod
  {
   get { return cleanUpMethod; }
   set { cleanUpMethod = value; }
  }
  [ReflectorProperty("cleanUpValue", Required = true)]
  public int CleaningUpValue
  {
   get { return cleanUpValue; }
   set { cleanUpValue = value; }
  }
        protected override bool Execute(IIntegrationResult result)
  {
            result.BuildProgressInformation.SignalStartRunTask(!string.IsNullOrEmpty(Description) ? Description : "Cleaning up");
   switch (cleanUpMethod)
   {
    case CleanUpMethod.KeepLastXBuilds:
     if (BuildLogFolderSet(result))
      KeepLastXBuilds(result.BuildLogDirectory, CleaningUpValue);
     break;
    case CleanUpMethod.DeleteBuildsOlderThanXDays:
     if (BuildLogFolderSet(result))
      DeleteBuildsOlderThanXDays(result.BuildLogDirectory, CleaningUpValue);
     break;
    case CleanUpMethod.KeepMaximumXHistoryDataEntries:
     KeepMaximumXHistoryDataEntries(result, cleanUpValue);
     break;
    case CleanUpMethod.DeleteSubDirsOlderThanXDays:
     DeleteSubDirsOlderThanXDays(result, cleanUpValue);
     break;
    case CleanUpMethod.KeepLastXSubDirs:
     KeepLastXSubDirs(result, cleanUpValue);
     break;
    default:
     throw new NotImplementedException("Unmapped cleaning up method used");
   }
            return true;
  }
  private void KeepLastXSubDirs(IIntegrationResult result, int amountToKeep)
  {
   List<string> sortNames = new List<string>();
   const string dateFormat = "yyyyMMddHHmmssffffff";
            Util.Log.Debug("Deleting Subdirs of {0}", result.ArtifactDirectory);
   foreach (string folder in Directory.GetDirectories(result.ArtifactDirectory))
   {
    if (folder != result.BuildLogDirectory)
     sortNames.Add(Directory.GetCreationTime(folder).ToString(dateFormat) + folder);
   }
   sortNames.Sort();
   int amountToDelete = sortNames.Count - amountToKeep;
   for (int i = 0; i < amountToDelete; i++)
   {
    DeleteFolder(sortNames[0].Substring(dateFormat.Length));
    sortNames.RemoveAt(0);
   }
  }
  private void DeleteSubDirsOlderThanXDays(IIntegrationResult result, int daysToKeep)
  {
            Util.Log.Debug("Deleting Subdirs of {0}", result.ArtifactDirectory);
            DateTime cutoffDate = DateTime.Now.Date.AddDays(-daysToKeep);
   foreach (string folder in Directory.GetDirectories(result.ArtifactDirectory))
   {
    if ((Directory.GetCreationTime(folder).Date < cutoffDate) &&
        (folder != result.BuildLogDirectory))
     DeleteFolder(folder);
   }
  }
  private void DeleteFolder(string folderName)
  {
            Util.Log.Debug("Deleting {0}", folderName);
   SetFilesToNormalAttributeAndDelete(folderName);
   Directory.Delete(folderName);
  }
  private void SetFilesToNormalAttributeAndDelete(string folderName)
  {
   foreach (string file in Directory.GetFiles(folderName))
   {
    File.SetAttributes(file, FileAttributes.Normal);
    File.Delete(file);
   }
   foreach (string subFolder in Directory.GetDirectories(folderName))
   {
    DeleteFolder(subFolder);
   }
  }
        private void SetFilesToNormalAttribute(string folderName)
        {
            foreach (string file in Directory.GetFiles(folderName))
            {
                File.SetAttributes(file, FileAttributes.Normal);
            }
        }
  private bool BuildLogFolderSet(IIntegrationResult result)
  {
   string BuildLogFolder = result.BuildLogDirectory;
   if (string.IsNullOrEmpty(BuildLogFolder))
   {
    Log.Debug(
     "Cleaning up the artifact folder not possible because the buildlog folder is NULL. \n Check that the XML Log publisher is before the Artifacts Cleanup publisher in the config.");
    return false;
   }
   return true;
  }
  private void DeleteBuildsOlderThanXDays(string buildLogFolder, int daysToKeep)
  {
            SetFilesToNormalAttribute(buildLogFolder);
   foreach (string filename in Directory.GetFiles(buildLogFolder))
   {
    if (File.GetCreationTime(filename).Date < DateTime.Now.Date.AddDays(-daysToKeep))
     File.Delete(filename);
   }
  }
  private void KeepLastXBuilds(string buildLogFolder, int buildToKeep)
  {
            SetFilesToNormalAttribute(buildLogFolder);
   List<string> buildLogs = new List<string>(Directory.GetFiles(buildLogFolder));
   buildLogs.Sort();
   while (buildLogs.Count > buildToKeep)
   {
    File.Delete(buildLogs[0]);
    buildLogs.RemoveAt(0);
   }
  }
  private void KeepMaximumXHistoryDataEntries(IIntegrationResult result, int entriesToKeep)
  {
   string historyXml = ModificationHistoryPublisher.LoadHistory(result.ArtifactDirectory);
   if (historyXml.Length == 0)
    return;
   XmlDocument doc = new XmlDocument();
   doc.LoadXml(historyXml);
            int nodeCount = doc.FirstChild.ChildNodes.Count;
            if (nodeCount <= entriesToKeep)
    return;
   StringWriter cleanedHistory = new StringWriter();
   for (int i = nodeCount - entriesToKeep; i < nodeCount ; i++)
   {
    cleanedHistory.WriteLine(doc.FirstChild.ChildNodes[i].OuterXml);
   }
   StreamWriter historyWriter = new StreamWriter(
    Path.Combine(result.ArtifactDirectory,
                 ModificationHistoryPublisher.DataHistoryFileName));
   historyWriter.WriteLine(cleanedHistory.ToString());
   historyWriter.Close();
  }
 }
}
