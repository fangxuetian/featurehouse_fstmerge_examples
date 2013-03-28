using System;
using System.Collections.Generic;
using System.Text;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core;
namespace ThoughtWorks.CruiseControl.Core.Publishers
{
    [ReflectorType("modificationHistory")]
    public class ModificationHistoryPublisher : ITask
    {
        public const string DataHistoryFileName = "HistoryData.xml";
        private bool onlyLogWhenChangesFound = false;
        [ReflectorProperty("onlyLogWhenChangesFound", Required = false)]
        public bool OnlyLogWhenChangesFound
        {
            get { return onlyLogWhenChangesFound; }
            set { onlyLogWhenChangesFound = value; }
        }
        [ReflectorProperty("description", Required = false)]
        public string Description = string.Empty;
        public void Run(IIntegrationResult result)
        {
            if ((OnlyLogWhenChangesFound) & (result.Modifications.Length == 0)) return;
            result.BuildProgressInformation.SignalStartRunTask(Description != string.Empty ? Description : "Saving modification history");
            string DataHistoryFile = System.IO.Path.Combine(result.ArtifactDirectory, DataHistoryFileName);
            WriteModifications(DataHistoryFile, result);
        }
        private void WriteModifications(string dataHistoryFile, IIntegrationResult result)
        {
            System.IO.FileStream fs = new System.IO.FileStream( dataHistoryFile, System.IO.FileMode.Append);
            fs.Seek(0, System.IO.SeekOrigin.End);
            System.IO.StreamWriter sw = new System.IO.StreamWriter(fs);
            System.Xml.XmlTextWriter CurrentBuildInfoWriter = new System.Xml.XmlTextWriter(sw);
            CurrentBuildInfoWriter.Formatting = System.Xml.Formatting.Indented;
            CurrentBuildInfoWriter.WriteStartElement("Build");
            WriteXMLAttributeAndValue(CurrentBuildInfoWriter, "BuildDate", Util.DateUtil.FormatDate(result.EndTime));
            WriteXMLAttributeAndValue(CurrentBuildInfoWriter, "Success", result.Succeeded.ToString());
            WriteXMLAttributeAndValue(CurrentBuildInfoWriter, "Label", result.Label);
            if (result.Modifications.Length > 0)
            {
                CurrentBuildInfoWriter.WriteStartElement("modifications");
                for (int i = 0; i < result.Modifications.Length; i++)
                {
                    result.Modifications[i].ToXml(CurrentBuildInfoWriter);
                }
                CurrentBuildInfoWriter.WriteEndElement();
            }
            CurrentBuildInfoWriter.WriteEndElement();
            sw.WriteLine();
            sw.Flush();
            fs.Flush();
            sw.Close();
            fs.Close();
        }
        private void WriteXMLAttributeAndValue(System.Xml.XmlTextWriter xmlWriter, string attributeName, string attributeValue)
        {
            xmlWriter.WriteStartAttribute(attributeName);
            xmlWriter.WriteValue(attributeValue);
        }
        public static string LoadHistory(string artifactDirectory)
        {
            string result = string.Empty;
            string documentLocation = System.IO.Path.Combine(artifactDirectory, DataHistoryFileName);
            if (System.IO.File.Exists(documentLocation))
            {
                System.IO.StreamReader sr = new System.IO.StreamReader(documentLocation);
                result = "<History>" + sr.ReadToEnd() + "</History>";
                sr.Close();
            }
            return result;
        }
    }
}
