using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Xml.Serialization;
using ThoughtWorks.CruiseControl.Core.Tasks;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.Core
{
    [Serializable]
    public class IntegrationResult : IIntegrationResult
    {
        public const string InitialLabel = "UNKNOWN";
        private string projectName;
        private string projectUrl;
        private string workingDirectory;
        private string artifactDirectory = "";
        private IntegrationRequest request = IntegrationRequest.NullRequest;
        private IntegrationSummary lastIntegration = IntegrationSummary.Initial;
        private string buildLogDirectory;
        private IntegrationStatus status = IntegrationStatus.Unknown;
        private ArrayList failureUsers = new ArrayList();
        private string label = InitialLabel;
        private DateTime startTime;
        private DateTime endTime;
        private Modification[] modifications = new Modification[0];
        private Exception exception;
        private readonly List<ITaskResult> taskResults = new List<ITaskResult>();
        private readonly BuildProgressInformation buildProgressInformation = new BuildProgressInformation("", "");
        private bool sourceControlErrorOccured = false;
        [XmlIgnore]
        public BuildProgressInformation BuildProgressInformation
        {
            get { return buildProgressInformation; }
        }
        public IntegrationResult()
        {
        }
        public IntegrationResult(string projectName, string workingDirectory, string artifactDirectory, IntegrationRequest request, IntegrationSummary lastIntegration)
        {
            ProjectName = projectName;
            WorkingDirectory = workingDirectory;
            ArtifactDirectory = artifactDirectory;
            this.request = (lastIntegration.IsInitial()) ? new IntegrationRequest(BuildCondition.ForceBuild, request.Source) : request;
            this.lastIntegration = lastIntegration;
            if ((lastIntegration.Status == IntegrationStatus.Exception)
                || (lastIntegration.Status == IntegrationStatus.Failure))
                failureUsers = lastIntegration.FailureUsers;
            buildProgressInformation = new BuildProgressInformation(artifactDirectory, projectName);
            this.label = this.LastIntegration.Label;
        }
        public string ProjectName
        {
            get { return projectName; }
            set { projectName = value; }
        }
        public string ProjectUrl
        {
            get { return projectUrl; }
            set { projectUrl = value; }
        }
        public BuildCondition BuildCondition
        {
            get { return request.BuildCondition; }
            set { request = new IntegrationRequest(value, "reloaded from_ state file"); }
        }
        public string Label
        {
            get { return label; }
            set { label = value; }
        }
        public int NumericLabel
        {
            get
            {
                try
                {
                    string tempNumericLabel = Regex.Replace(Label, @".*?(\d+$)", "$1");
                    return int.Parse(tempNumericLabel);
                }
                catch (FormatException)
                {
                    return 0;
                }
            }
        }
        public string WorkingDirectory
        {
            get { return workingDirectory; }
            set { workingDirectory = value; }
        }
        public string ArtifactDirectory
        {
            get { return artifactDirectory; }
            set { artifactDirectory = value; }
        }
        public string BuildLogDirectory
        {
            get { return buildLogDirectory; }
            set { buildLogDirectory = value; }
        }
        public string IntegrationArtifactDirectory
        {
            get { return Path.Combine(ArtifactDirectory, Label); }
        }
        public string ListenerFile
        {
            get
            {
                return Path.Combine(artifactDirectory,
                  StringUtil.RemoveInvalidCharactersFromFileName(projectName) + "_ListenFile.xml");
            }
        }
        public IntegrationStatus Status
        {
            get { return status; }
            set { status = value; }
        }
        public DateTime StartTime
        {
            get { return startTime; }
            set { startTime = value; }
        }
        public DateTime EndTime
        {
            get { return endTime; }
            set { endTime = value; }
        }
        [XmlIgnore]
        public virtual Modification[] Modifications
        {
            get { return modifications; }
            set { modifications = value; }
        }
        public DateTime LastModificationDate
        {
            get
            {
                if (Modifications.Length == 0)
                {
                    return DateTime.Now.AddDays(-1.0);
                }
                DateTime latestDate = DateTime.MinValue;
                foreach (Modification modification in Modifications)
                {
                    latestDate = DateUtil.MaxDate(modification.ModifiedTime, latestDate);
                }
                return latestDate;
            }
        }
        public int LastChangeNumber
        {
            get
            {
                return Modification.GetLastChangeNumber(modifications);
            }
        }
        public bool IsInitial()
        {
            return Label == InitialLabel;
        }
        public bool Succeeded
        {
            get { return Status == IntegrationStatus.Success; }
        }
        public bool Failed
        {
            get { return Status == IntegrationStatus.Failure; }
        }
        public bool Fixed
        {
            get { return Succeeded && LastIntegrationStatus == IntegrationStatus.Failure; }
        }
        public TimeSpan TotalIntegrationTime
        {
            get { return EndTime - StartTime; }
        }
        [XmlIgnore]
        public Exception ExceptionResult
        {
            get { return exception; }
            set
            {
                exception = value;
                if (exception != null)
                    Status = IntegrationStatus.Exception;
            }
        }
        [XmlIgnore]
        public IList TaskResults
        {
            get { return taskResults; }
        }
        public void AddTaskResult(string result)
        {
            AddTaskResult(new DataTaskResult(result));
        }
        public void AddTaskResult(ITaskResult result)
        {
            taskResults.Add(result);
            if (Failed || Status == IntegrationStatus.Exception)
                return;
            Status = result.Succeeded() ? IntegrationStatus.Success : IntegrationStatus.Failure;
        }
        public void MarkStartTime()
        {
            StartTime = DateTime.Now;
        }
        public void MarkEndTime()
        {
            EndTime = DateTime.Now;
        }
        public bool HasModifications()
        {
            return Modifications.Length > 0;
        }
        public static IntegrationResult CreateInitialIntegrationResult(string project, string workingDirectory, string artifactDirectory)
        {
            IntegrationRequest initialRequest = new IntegrationRequest(BuildCondition.ForceBuild, "Initial Build");
            IntegrationResult result = new IntegrationResult(project, workingDirectory, artifactDirectory, initialRequest, IntegrationSummary.Initial);
            result.StartTime = DateTime.Now.AddDays(-1);
            result.EndTime = DateTime.Now;
            return result;
        }
        public bool ShouldRunBuild()
        {
            return BuildCondition.ForceBuild == BuildCondition || HasModifications();
        }
        public string BaseFromArtifactsDirectory(string pathToBase)
        {
            return string.IsNullOrEmpty(pathToBase) ? ArtifactDirectory : Path.Combine(ArtifactDirectory, pathToBase);
        }
        public string BaseFromWorkingDirectory(string pathToBase)
        {
            return string.IsNullOrEmpty(pathToBase) ? WorkingDirectory : Path.Combine(WorkingDirectory, pathToBase);
        }
        [XmlIgnore]
        public string TaskOutput
        {
            get
            {
                StringBuilder sb = new StringBuilder();
                foreach (ITaskResult result in taskResults)
                    sb.Append(result.Data);
                return sb.ToString();
            }
        }
        [XmlIgnore]
        public IntegrationSummary LastIntegration
        {
            get { return lastIntegration; }
        }
        public IntegrationStatus LastIntegrationStatus
        {
            get { return lastIntegration.Status; }
            set { lastIntegration = new IntegrationSummary(value, lastIntegration.Label, LastSuccessfulIntegrationLabel, lastIntegration.StartTime); }
        }
        public string LastSuccessfulIntegrationLabel
        {
            get { return (Succeeded || lastIntegration.LastSuccessfulIntegrationLabel == null) ? Label : lastIntegration.LastSuccessfulIntegrationLabel; }
            set { lastIntegration = new IntegrationSummary(lastIntegration.Status, lastIntegration.Label, value, lastIntegration.StartTime); }
        }
        public ArrayList FailureUsers
        {
            get { return failureUsers; }
            set { failureUsers = value; }
        }
        [XmlIgnore]
        public IntegrationRequest IntegrationRequest
        {
            get { return request; }
        }
        [XmlIgnore]
        public IDictionary IntegrationProperties
        {
            get
            {
                IDictionary fullProps = new SortedList();
                fullProps[IntegrationPropertyNames.CCNetProject] = projectName;
                if (projectUrl != null) fullProps[IntegrationPropertyNames.CCNetProjectUrl] = projectUrl;
                fullProps[IntegrationPropertyNames.CCNetWorkingDirectory] = workingDirectory;
                fullProps[IntegrationPropertyNames.CCNetArtifactDirectory] = artifactDirectory;
                fullProps[IntegrationPropertyNames.CCNetIntegrationStatus] = Status;
                fullProps[IntegrationPropertyNames.CCNetLabel] = Label;
                fullProps[IntegrationPropertyNames.CCNetBuildCondition] = BuildCondition;
                fullProps[IntegrationPropertyNames.CCNetNumericLabel] = NumericLabel;
                fullProps[IntegrationPropertyNames.CCNetBuildDate] = StartTime.ToString("yyyy-MM-dd", null);
                fullProps[IntegrationPropertyNames.CCNetBuildTime] = StartTime.ToString("HH:mm:ss", null);
                fullProps[IntegrationPropertyNames.CCNetLastIntegrationStatus] = LastIntegrationStatus;
                fullProps[IntegrationPropertyNames.CCNetListenerFile] = BuildProgressInformation.ListenerFile;
                fullProps[IntegrationPropertyNames.CCNetFailureUsers] = FailureUsers;
                fullProps[IntegrationPropertyNames.CCNetModifyingUsers] = GetModifiers();
                if (IntegrationRequest != null) fullProps[IntegrationPropertyNames.CCNetRequestSource] = IntegrationRequest.Source;
                return fullProps;
            }
        }
        public override bool Equals(object obj)
        {
            IntegrationResult other = obj as IntegrationResult;
            if (other == null)
                return false;
            return ProjectName == other.ProjectName &&
                   Status == other.Status &&
                   Label == other.Label &&
                   StartTime == other.StartTime;
        }
        public override int GetHashCode()
        {
            return (ProjectName + Label + StartTime.Ticks).GetHashCode();
        }
        public override string ToString()
        {
            return string.Format("Project: {0}, Status: {1}, Label: {2}, StartTime: {3}", ProjectName, Status, Label, StartTime);
        }
        public bool SourceControlErrorOccured
        {
            get
            {
                return sourceControlErrorOccured;
            }
            set
            {
                sourceControlErrorOccured = value;
            }
        }
        private ArrayList GetModifiers()
        {
            ArrayList Result = new ArrayList();
            foreach (Modification mod in Modifications)
            {
                if (!Result.Contains(mod.UserName))
                {
                    Result.Add(mod.UserName);
                }
            }
            return Result;
        }
    }
}
