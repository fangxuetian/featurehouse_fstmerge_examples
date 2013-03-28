namespace ThoughtWorks.CruiseControl.Core.Tasks
{
    using System;
    using System.Collections;
    using System.Diagnostics;
    using System.IO;
    using System.Text;
    using Exortech.NetReflector;
    using ThoughtWorks.CruiseControl.Core.Util;
    [ReflectorType("devenv")]
 public class DevenvTask
        : TaskBase
 {
  public const string VS2008_REGISTRY_PATH = @"Software\Microsoft\VisualStudio\9.0";
  public const string VS2005_REGISTRY_PATH = @"Software\Microsoft\VisualStudio\8.0";
  public const string VS2003_REGISTRY_PATH = @"Software\Microsoft\VisualStudio\7.1";
  public const string VS2002_REGISTRY_PATH = @"Software\Microsoft\VisualStudio\7.0";
  public const string VS_REGISTRY_KEY = @"InstallDir";
  public const string DEVENV_EXE = "devenv.com";
  public const int DEFAULT_BUILD_TIMEOUT = 600;
  public const string DEFAULT_BUILDTYPE = "rebuild";
  public const string DEFAULT_PROJECT = "";
        public const ProcessPriorityClass DEFAULT_PRIORITY = ProcessPriorityClass.Normal;
  private readonly IRegistry registry;
  private readonly ProcessExecutor executor;
  private string executable;
  private string version;
  public DevenvTask() :
   this(new Registry(), new ProcessExecutor()) { }
  public DevenvTask(IRegistry registry, ProcessExecutor executor)
  {
   this.registry = registry;
   this.executor = executor;
  }
  private readonly string[] ExpectedVisualStudioVersions =
   new string[]
    {
     "9.0", "8.0", "7.1", "7.0",
     "VS2008", "VS2005", "VS2003", "VS2002"
    };
  private readonly string[] RegistryScanOrder =
   new string[]
    {
     VS2008_REGISTRY_PATH, VS2005_REGISTRY_PATH, VS2003_REGISTRY_PATH, VS2002_REGISTRY_PATH
    };
  [ReflectorProperty("version", Required = false)]
  public string Version
  {
   get { return version; }
   set
   {
    if (Array.IndexOf(ExpectedVisualStudioVersions, value) == -1)
     throw new CruiseControlException("Invalid value for Version, expected one of: "+
      StringUtil.Join(", ", ExpectedVisualStudioVersions));
    version = value;
   }
  }
  [ReflectorProperty("executable", Required=false)]
  public string Executable
  {
   get
   {
    if (executable == null)
     executable = ReadDevenvExecutableFromRegistry();
    return executable;
   }
   set { executable = value; }
  }
  private string ReadDevenvExecutableFromRegistry()
  {
   if (Version == null)
    return Path.Combine(ScanForRegistryForVersion(), DEVENV_EXE);
   string path;
   switch (Version)
   {
    case "VS2008":
    case "9.0":
     path = registry.GetExpectedLocalMachineSubKeyValue(VS2008_REGISTRY_PATH, VS_REGISTRY_KEY);
     break;
    case "VS2005":
    case "8.0":
     path = registry.GetExpectedLocalMachineSubKeyValue(VS2005_REGISTRY_PATH, VS_REGISTRY_KEY);
     break;
    case "VS2003":
    case "7.1":
     path = registry.GetExpectedLocalMachineSubKeyValue(VS2003_REGISTRY_PATH, VS_REGISTRY_KEY);
     break;
    case "VS2002":
    case "7.0":
     path = registry.GetExpectedLocalMachineSubKeyValue(VS2002_REGISTRY_PATH, VS_REGISTRY_KEY);
     break;
    default:
     throw new Exception("Unknown version of Visual Studio.");
   }
   return Path.Combine(path, DEVENV_EXE);
  }
  private string ScanForRegistryForVersion()
  {
   foreach(string x in RegistryScanOrder)
   {
    string path = registry.GetLocalMachineSubKeyValue(x, VS_REGISTRY_KEY);
    if (path != null)
     return path;
   }
   throw new Exception("Unknown version of Visual Studio, or no version found.");
  }
  [ReflectorProperty("solutionfile")]
  public string SolutionFile;
        [ReflectorProperty("configuration")]
  public string Configuration;
        [ReflectorProperty("buildTimeoutSeconds", Required = false)]
  public int BuildTimeoutSeconds = DEFAULT_BUILD_TIMEOUT;
  [ReflectorProperty("buildtype", Required = false)]
  public string BuildType = DEFAULT_BUILDTYPE;
  [ReflectorProperty("project", Required = false)]
  public string Project = DEFAULT_PROJECT;
        [ReflectorProperty("priority", Required = false)]
        public ProcessPriorityClass Priority = DEFAULT_PRIORITY;
        protected override bool Execute(IIntegrationResult result)
  {
            result.BuildProgressInformation.SignalStartRunTask(!string.IsNullOrEmpty(Description) ? Description : string.Format("Executing Devenv :{0}", GetArguments()));
   ProcessResult processResult = TryToRun(result);
   result.AddTaskResult(new DevenvTaskResult(processResult));
   Log.Info("Devenv build complete.  Status: " + result.Status);
   if (processResult.TimedOut)
    throw new BuilderException(this, string.Format("Devenv process timed out after {0} seconds.", BuildTimeoutSeconds));
            return !processResult.Failed;
  }
  private ProcessResult TryToRun(IIntegrationResult result)
  {
   ProcessInfo processInfo = new ProcessInfo(Executable, GetArguments(), result.WorkingDirectory, Priority);
   processInfo.TimeOut = BuildTimeoutSeconds * 1000;
   IDictionary properties = result.IntegrationProperties;
   foreach (string key in properties.Keys)
   {
    processInfo.EnvironmentVariables[key] = StringUtil.IntegrationPropertyToString(properties[key]);
   }
   Log.Info(string.Format("Starting build: {0} {1}", processInfo.FileName, processInfo.PublicArguments));
   try
   {
    return executor.Execute(processInfo);
   }
   catch (IOException ex)
   {
    string message = string.Format("Unable to launch the devenv process.  Please verify that you can invoke this command from_ the command line: {0} {1}", processInfo.FileName, processInfo.PublicArguments);
    throw new BuilderException(this, message, ex);
   }
  }
  private string GetArguments()
  {
   StringBuilder sb = new StringBuilder();
   if (SolutionFile.StartsWith("\""))
    sb.Append(SolutionFile);
   else
    sb.AppendFormat("\"{0}\"", SolutionFile);
   sb.AppendFormat(" /{0}", BuildType);
   if (Configuration.StartsWith("\""))
    sb.AppendFormat(" {0}", Configuration);
   else
    sb.AppendFormat(" \"{0}\"", Configuration);
            if (!string.IsNullOrEmpty(Project))
   {
    if (Project.StartsWith("\""))
     sb.AppendFormat(" /project {0}", Project);
    else
     sb.AppendFormat(" /project \"{0}\"", Project);
   }
   return sb.ToString();
  }
 }
}
