using System.IO;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Tasks
{
 [ReflectorType("merge")]
 public class MergeFilesTask
        : TaskBase
 {
        [ReflectorProperty("target", Required = false)]
        public string TargetFolder { get; set; }
        [ReflectorProperty("files", typeof(MergeFileSerialiserFactory))]
        public MergeFileInfo[] MergeFiles = new MergeFileInfo[0];
        public IFileSystem FileSystem { get; set; }
        public ILogger Logger { get; set; }
        protected override bool Execute(IIntegrationResult result)
  {
            result.BuildProgressInformation.SignalStartRunTask(!string.IsNullOrEmpty(Description) ? Description : "Merging Files");
            var actualFileSystem = FileSystem ?? new SystemIoFileSystem();
            var actualLogger = Logger ?? new DefaultLogger();
            var targetFolder = TargetFolder;
            if (!string.IsNullOrEmpty(targetFolder))
            {
                if (!Path.IsPathRooted(targetFolder))
                {
                    targetFolder = Path.Combine(
                        Path.Combine(result.ArtifactDirectory, result.Label),
                        targetFolder);
                }
            }
            else
            {
                targetFolder = Path.Combine(result.ArtifactDirectory, result.Label);
            }
   foreach (var mergeFile in MergeFiles)
   {
    string fullMergeFile = mergeFile.FileName;
                if (!Path.IsPathRooted(fullMergeFile))
                {
                    fullMergeFile = Path.Combine(result.WorkingDirectory, fullMergeFile);
                }
    WildCardPath path = new WildCardPath(fullMergeFile);
                foreach (var fileInfo in path.GetFiles())
                {
                    if (actualFileSystem.FileExists(fileInfo.FullName))
                    {
                        switch (mergeFile.MergeAction)
                        {
                            case MergeFileInfo.MergeActionType.Merge:
                            case MergeFileInfo.MergeActionType.CData:
                                actualLogger.Info("Merging file '{0}'", fileInfo);
                                result.BuildProgressInformation.AddTaskInformation(string.Format("Merging file '{0}'", fileInfo));
                                var useCData = (mergeFile.MergeAction == MergeFileInfo.MergeActionType.CData);
                                result.AddTaskResultFromFile(fileInfo.FullName, useCData);
                                break;
                            case MergeFileInfo.MergeActionType.Copy:
                                actualFileSystem.EnsureFolderExists(targetFolder);
                                actualLogger.Info("Copying file '{0}' to '{1}'", fileInfo.Name, targetFolder);
                                result.BuildProgressInformation.AddTaskInformation(string.Format("Copying file '{0}' to '{1}'", fileInfo.Name, targetFolder));
                                actualFileSystem.Copy(fileInfo.FullName, Path.Combine(targetFolder, fileInfo.Name));
                                break;
                            default:
                                throw new CruiseControlException(
                                    string.Format("Unknown file merge action '{0}'", mergeFile.MergeAction));
                        }
                    }
                    else
                    {
                        actualLogger.Warning("File not found '{0}", fileInfo);
                    }
                }
   }
            return true;
  }
 }
}
