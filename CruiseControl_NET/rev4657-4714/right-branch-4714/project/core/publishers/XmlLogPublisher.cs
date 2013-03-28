using System.IO;
using System.Xml;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.Core.Publishers
{
    [ReflectorType("xmllogger")]
    public class XmlLogPublisher : ITask
    {
  public static readonly string DEFAULT_LOG_SUBDIRECTORY = "buildlogs";
        [ReflectorProperty("logDir", Required = false)]
  public string ConfiguredLogDirectory;
  public string LogDirectory(string artifactDirectory)
  {
   if (StringUtil.IsBlank(ConfiguredLogDirectory))
   {
    return Path.Combine(artifactDirectory, DEFAULT_LOG_SUBDIRECTORY);
   }
   else if (Path.IsPathRooted(ConfiguredLogDirectory))
   {
    return ConfiguredLogDirectory;
   }
   else
   {
    return Path.Combine(artifactDirectory, ConfiguredLogDirectory);
   }
  }
  public void Run(IIntegrationResult result)
        {
            if (result.Status == IntegrationStatus.Unknown)
                return;
            using (XmlIntegrationResultWriter integrationWriter = new XmlIntegrationResultWriter(CreateWriter(LogDirectory(result.ArtifactDirectory), GetFilename(result))))
            {
    integrationWriter.Formatting = Formatting.Indented;
    integrationWriter.Write(result);
            }
            result.BuildLogDirectory = LogDirectory(result.ArtifactDirectory);
        }
        private TextWriter CreateWriter(string dirname, string filename)
        {
            if (!Directory.Exists(dirname))
                Directory.CreateDirectory(dirname);
            string path = Path.Combine(dirname, filename);
   return new StreamWriter(path);
        }
        private string GetFilename(IIntegrationResult result)
        {
            return Util.StringUtil.RemoveInvalidCharactersFromFileName(new LogFile(result).Filename);
        }
    }
}
