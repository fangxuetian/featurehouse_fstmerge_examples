using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Globalization;
using System.IO;
using System.Text;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Sourcecontrol
{
    [ReflectorType("pvcs")]
 public class Pvcs : ProcessSourceControl
 {
  private const string DELETE_LABEL_TEMPLATE =
   @"run -y -xe""{0}"" -xo""{1}"" DeleteLabel -pr""{2}"" {3} {4} {5} {6}";
  private const string APPLY_LABEL_TEMPLATE =
   @"Vcs -q -xo""{0}"" -xe""{1}"" {2} -v""{3}"" ""@{4}""";
  private const string VLOG_INSTRUCTIONS_TEMPLATE =
   @"run -xe""{0}"" -xo""{1}"" -q vlog -pr""{2}"" {3} {4} -ds""{5}"" -de""{6}"" {7}";
  private const string VLOG_LABEL_INSTRUCTIONS_TEMPLATE =
   @"run -xe""{0}"" -xo""{1}"" -q vlog -pr""{2}"" {3} {4} -r""{5}"" {6}";
  private const string GET_INSTRUCTIONS_TEMPLATE =
   @"run -y -xe""{0}"" -xo""{1}"" -q Get -pr""{2}"" {3} {4} -sp""{5}"" {6} {7} ";
  private const string INDIVIDUAL_GET_REVISION_TEMPLATE =
   @"-r{0} ""{1}{2}\{3}""(""{4}"") ";
  private const string INDIVIDUAL_LABEL_REVISION_TEMPLATE =
   @"{0} ""{1}{2}\{3}"" ";
  private TimeZone currentTimeZone = TimeZone.CurrentTimeZone;
  private string baseLabelName =string.Empty;
  private Modification[] modifications = null;
  private Modification[] baseModifications = null;
  private string errorFile =string.Empty;
  private string logFile =string.Empty;
  private string tempFile =string.Empty;
  private string tempLabel =string.Empty;
  public Pvcs() : this(new PvcsHistoryParser(), new ProcessExecutor())
  {}
  public Pvcs(IHistoryParser parser, ProcessExecutor executor) : base(parser, executor)
  {}
  [ReflectorProperty("executable")]
  public string Executable = "pcli.exe";
        [ReflectorProperty("project")]
  public string Project;
        [ReflectorProperty("subproject")]
  public string Subproject;
        [ReflectorProperty("username", Required = false)]
  public string Username =string.Empty;
        [ReflectorProperty("password", Required = false)]
  public string Password =string.Empty;
        [ReflectorProperty("workingdirectory", Required = false)]
  public string WorkingDirectory =string.Empty;
        [ReflectorProperty("workspace", Required = false)]
  public string Workspace = "/@/RootWorkspace";
        [ReflectorProperty("recursive", Required = false)]
  public bool Recursive = true;
        [ReflectorProperty("labelOnSuccess", Required = false)]
  public bool LabelOnSuccess = false;
        [ReflectorProperty("autoGetSource", Required = false)]
  public bool AutoGetSource = true;
        [ReflectorProperty("manuallyAdjustForDaylightSavings", Required = false)]
  public bool ManuallyAdjustForDaylightSavings = false;
  public bool IsPromotionGroup = false;
        [ReflectorProperty("labelOrPromotionName", Required = false)]
  public string LabelOrPromotionName
  {
   get { return baseLabelName; }
   set
   {
    baseLabelName = value;
                LabelOnSuccess = !string.IsNullOrEmpty(baseLabelName);
   }
  }
  public TimeZone CurrentTimeZone
  {
   set { currentTimeZone = value; }
  }
  public string ErrorFile
  {
   get { return errorFile = TempFileNameIfBlank(errorFile); }
  }
  public string LogFile
  {
   get { return logFile = TempFileNameIfBlank(logFile); }
  }
  public string TempFile
  {
   get { return tempFile = TempFileNameIfBlank(tempFile); }
  }
  public string LabelOrPromotionInput(string label)
  {
   return (label.Length == 0) ?string.Empty : (IsPromotionGroup == false ? "-v" : "-g") + label;
  }
  public override Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
  {
   baseModifications = null;
   using (TextReader reader = ExecuteVLog(from_.StartTime, to.StartTime))
   {
    modifications = ParseModifications(reader, from_.StartTime, to.StartTime);
   }
            base.FillIssueUrl(modifications);
   return modifications;
  }
  private string GetRecursiveValue()
  {
   return Recursive ? "-z" : string.Empty;
  }
  private TextReader ExecuteVLog(DateTime from_, DateTime to)
  {
   if (ManuallyAdjustForDaylightSavings)
   {
    from_ = AdjustForDayLightSavingsBug(from_);
    to = AdjustForDayLightSavingsBug(to);
   }
   Execute(CreatePcliContentsForCreatingVLog(GetDateString(from_), GetDateString(to)));
   return GetTextReader(LogFile);
  }
  public string GetLogin(bool doubleQuotes)
  {
            if (Username.Length == 0)
                return string.Empty;
            string quotes = doubleQuotes ? "\"\"" : string.Empty;
            if (Password.Length == 0)
    return string.Format(" {1}-id\"{0}\"{1} ", Username, quotes);
            return string.Format(" {2}-id\"{0}\":\"{1}\"{2} ", Username, Password, quotes);
        }
  private void ExecuteNonPvcsFunction(string content)
  {
   string filename = Path.GetTempFileName();
   filename = filename.Substring(0, filename.Length - 3) + "cmd";
   try
   {
    CreatePVCSInstructionFile(filename, content);
    string arguments = string.Format(@"/c ""{0}""", filename);
    Execute(CreatePVCSProcessInfo("cmd.exe", arguments));
   }
   finally
   {
    if (File.Exists(filename))
     File.Delete(filename);
   }
  }
  private void Execute(string pcliContent)
  {
   Execute(CreatePVCSProcessInfo(Executable, pcliContent));
  }
  private void CreatePVCSInstructionFile(string filename, string content)
  {
   using (StreamWriter stream = File.CreateText(filename))
   {
    stream.Write(content);
   }
  }
  private ProcessInfo CreatePVCSProcessInfo(string executable, string arguments)
  {
   return new ProcessInfo(executable, arguments);
  }
  public string CreatePcliContentsForGet()
  {
   return string.Format(GET_INSTRUCTIONS_TEMPLATE, ErrorFile, LogFile, Project, GetLogin(false), GetRecursiveValue(), Workspace, LabelOrPromotionInput(tempLabel), Subproject);
  }
  public string CreatePcliContentsForCreatingVLog(string beforedate, string afterdate)
  {
   return string.Format(VLOG_INSTRUCTIONS_TEMPLATE, ErrorFile, LogFile, Project, GetLogin(false), GetRecursiveValue(), beforedate, afterdate, Subproject);
  }
  public string CreatePcliContentsForCreatingVlogByLabel(string label)
  {
   return string.Format(VLOG_LABEL_INSTRUCTIONS_TEMPLATE, ErrorFile, LogFile, Project, GetLogin(false), GetRecursiveValue(), label, Subproject);
  }
  public string CreatePcliContentsForDeletingLabel(string label)
  {
   return string.Format(DELETE_LABEL_TEMPLATE, ErrorFile, LogFile, Project, GetLogin(false), GetRecursiveValue(), LabelOrPromotionInput(label), Subproject);
  }
  public string CreatePcliContentsForLabeling(string label)
  {
   return string.Format(APPLY_LABEL_TEMPLATE, LogFile, ErrorFile, GetLogin(false), label, TempFile);
  }
  public string CreateIndividualLabelString(Modification mod, string label)
  {
   return string.Format(INDIVIDUAL_LABEL_REVISION_TEMPLATE, GetVersion(mod, label), GetUncPathPrefix(mod), mod.FolderName, mod.FileName);
  }
  public string CreateIndividualGetString(Modification mod, string fileLocation)
  {
   return string.Format(INDIVIDUAL_GET_REVISION_TEMPLATE, GetVersion(mod,string.Empty), GetUncPathPrefix(mod), mod.FolderName, mod.FileName, fileLocation);
  }
  private string GetUncPathPrefix(Modification mod)
  {
   return mod.FolderName.StartsWith("\\") ? @"\" :string.Empty;
  }
  private string GetVersion(Modification mod, string label)
  {
   return ((label == null || label.Length == 0) ? (mod.Version == null ? "1.0" : mod.Version) : (LabelOrPromotionInput(label)));
  }
  private TextReader GetTextReader(string path)
  {
   FileStream stream = new FileStream(path, FileMode.Open, FileAccess.Read, FileShare.Read);
   return new StreamReader(stream);
  }
  public DateTime AdjustForDayLightSavingsBug(DateTime date)
  {
   if (currentTimeZone.IsDaylightSavingTime(DateTime.Now))
   {
    TimeSpan anHour = new TimeSpan(1, 0, 0);
    return date.Subtract(anHour);
   }
   return date;
  }
  private string TempFileNameIfBlank(string file)
  {
            return string.IsNullOrEmpty(file) ? Path.GetTempFileName() : file;
  }
  public override void GetSource(IIntegrationResult result)
  {
            result.BuildProgressInformation.SignalStartRunTask("Getting source from_ Pvcs");
   if (result.Modifications.Length < 1 || !AutoGetSource)
    return;
   WorkingDirectory = (WorkingDirectory.Length < 3) ? result.WorkingDirectory : WorkingDirectory;
   modifications = result.Modifications;
   if (LabelOnSuccess && LabelOrPromotionName.Length > 0)
    DetermineMaxRevisions(LabelOrPromotionName);
   StringDictionary createFolders = new StringDictionary();
   using (TextWriter stream = File.CreateText(TempFile))
   {
    foreach (Modification mod in modifications)
    {
     string fileLoc = DetermineFileLocation(mod.FolderName);
     if (!createFolders.ContainsKey(fileLoc))
      createFolders.Add(fileLoc, fileLoc);
     stream.WriteLine(CreateIndividualGetString(mod, fileLoc));
    }
   }
   ExecutePvcsGet(createFolders);
  }
  private string DetermineFileLocation(string folderName)
  {
   string folder = Path.GetFullPath(folderName).ToLower();
   string archive = Project.ToLower() + @"\archives";
   if (folder.IndexOf(archive) < 0)
   {
    return WorkingDirectory + folder;
   }
   else
   {
    return folder.Replace(archive, WorkingDirectory);
   }
  }
  private void ExecutePvcsGet(StringDictionary folders)
  {
   StringBuilder content = new StringBuilder();
   content.Append("@echo off \r\necho Create all necessary folders first\r\n");
   foreach (string key in folders.Keys)
   {
    content.AppendFormat("IF NOT EXIST \"{0}\" mkdir \"{0}\\\" >NUL \r\n", folders[key]);
   }
   content.Append("\r\necho Get all of the files by version number and archive location \r\n");
   content.AppendFormat("\"{0}\" -W -Y -xo\"{1}\" -xe\"{2}\" @\"{3}\"\r{4}", GetExeFilename(), LogFile, ErrorFile, TempFile, Environment.NewLine);
   ExecuteNonPvcsFunction(content.ToString());
  }
  public string GetExeFilename()
  {
   string dir = Path.GetDirectoryName(Executable);
   return Path.Combine(dir, "Get.exe");
  }
  public override void LabelSourceControl(IIntegrationResult result)
  {
   if (result.Modifications.Length < 1 || LabelOnSuccess == false || ! result.Succeeded)
    return;
   modifications = result.Modifications;
   if (LabelOrPromotionName.Length > 0)
    LabelSourceControl(string.Empty, LabelOrPromotionName, result.ProjectName);
   if (result.Label != LabelOrPromotionName)
    LabelSourceControl(LabelOrPromotionName, result.Label, result.ProjectName);
  }
  private void LabelSourceControl(string oldLabel, string newLabel, string project)
  {
   if (oldLabel.Length > 0)
   {
    Log.Info("Copying PVCS Label " + oldLabel + " to " + newLabel);
    DetermineMaxRevisions(oldLabel);
   }
   using (TextWriter stream = File.CreateText(TempFile))
   {
    foreach (Modification mod in modifications)
    {
     stream.WriteLine(CreateIndividualLabelString(mod, (oldLabel.Length > 0 ? newLabel :string.Empty)));
    }
   }
   Log.Info("Applying PVCS Label " + newLabel + " on Project " + project);
   ExecuteNonPvcsFunction(CreatePcliContentsForLabeling(newLabel));
  }
  private void DetermineMaxRevisions(string oldLabel)
  {
   if (baseModifications == null)
   {
    Log.Info("Determine Revisions based on Promotion Group/Label : " + oldLabel);
    Execute(CreatePcliContentsForCreatingVlogByLabel(oldLabel));
    using (TextReader reader = GetTextReader(LogFile))
    {
     baseModifications = historyParser.Parse(reader, DateTime.Now, DateTime.Now);
    }
   }
            var allMods = new List<Modification>();
   foreach (Modification mod in baseModifications)
   {
    allMods.Add(mod);
   }
   foreach (Modification mod in modifications)
   {
    allMods.Add(mod);
   }
   modifications = PvcsHistoryParser.AnalyzeModifications(allMods);
  }
  public static string GetDateString(DateTime dateToConvert)
  {
   return GetDateString(dateToConvert, CultureInfo.CurrentCulture.DateTimeFormat);
  }
  public static string GetDateString(DateTime dateToConvert, DateTimeFormatInfo format)
  {
   string pattern = String.Format("{0} {1}", format.ShortDatePattern, format.ShortTimePattern);
   return dateToConvert.ToString(pattern, format);
  }
  public static DateTime GetDate(string dateToParse)
  {
   return GetDate(dateToParse, CultureInfo.CurrentCulture);
  }
  public static DateTime GetDate(string dateToParse, IFormatProvider format)
  {
   try
   {
    return DateTime.Parse(dateToParse, format);
   }
   catch (Exception ex)
   {
    throw new CruiseControlException("Unable to parse: " + dateToParse, ex);
   }
  }
 }
}
