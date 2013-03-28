using Exortech.NetReflector;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Security
{
    [ReflectorType("fileBasedCache")]
    public class FileBasedSessionCache
        : SessionCacheBase
    {
  private readonly IFileSystem fileSystem;
  private readonly IExecutionEnvironment executionEnvironment;
        private string storeLocation;
  public FileBasedSessionCache() : this(new SystemIoFileSystem(), new ExecutionEnvironment())
  { }
  public FileBasedSessionCache(IFileSystem fileSystem, IExecutionEnvironment executionEnvironment)
  {
   this.fileSystem = fileSystem;
   this.executionEnvironment = executionEnvironment;
   storeLocation = Path.Combine(this.executionEnvironment.GetDefaultProgramDataFolder(ApplicationType.Server), "sessions");
   fileSystem.EnsureFolderExists(storeLocation);
  }
        [ReflectorProperty("location", Required = false)]
        public virtual string StoreLocation
        {
            get { return storeLocation; }
            set
            {
    string path = executionEnvironment.EnsurePathIsRooted(value);
    if (!string.IsNullOrEmpty(path))
     fileSystem.EnsureFolderExists(path);
             storeLocation = path;
            }
        }
        public override void Initialise()
        {
            base.Initialise();
            DirectoryInfo storeDirectory = new DirectoryInfo(storeLocation);
            if (storeDirectory.Exists)
            {
                FileInfo[] sessions = storeDirectory.GetFiles("*.session");
                XmlDocument sessionXml = new XmlDocument();
                foreach (FileInfo sessionFile in sessions)
                {
                    try
                    {
                        sessionXml.Load(sessionFile.FullName);
                        string sessionToken = sessionXml.DocumentElement.GetAttribute("token");
                        string userName = sessionXml.DocumentElement.GetAttribute("userName");
                        string expiryTime = sessionXml.DocumentElement.GetAttribute("expiry");
                        SessionDetails session = new SessionDetails(userName, DateTime.Parse(expiryTime));
                        foreach (XmlElement value in sessionXml.SelectNodes("//value"))
                        {
                            string valueKey = value.GetAttribute("key");
                            session.Values[valueKey] = value.InnerText;
                        }
                        AddToCacheInternal(sessionToken, session);
                    }
                    catch { }
                }
            }
            else
            {
                storeDirectory.Create();
            }
        }
        public override string AddToCache(string userName)
        {
            string sessionToken = base.AddToCache(userName);
            SaveSession(sessionToken);
            return sessionToken;
        }
        public override void RemoveFromCache(string sessionToken)
        {
            base.RemoveFromCache(sessionToken);
            string sessionFile = GenerateFileName(sessionToken);
            if (File.Exists(sessionFile)) File.Delete(sessionFile);
        }
        public override void StoreSessionValue(string sessionToken, string key, object value)
        {
            base.StoreSessionValue(sessionToken, key, value);
            SaveSession(sessionToken);
        }
        protected virtual void SaveSession(string sessionToken)
        {
            SessionDetails details = RetrieveSessionDetails(sessionToken);
            string sessionFile = GenerateFileName(sessionToken);
            XmlDocument sessionXml = new XmlDocument();
            XmlElement sessionRoot;
            XmlElement valuesNode;
            if (File.Exists(sessionFile))
            {
                sessionXml.Load(sessionFile);
                sessionRoot = sessionXml.DocumentElement;
            }
            else
            {
                sessionRoot = sessionXml.CreateElement("session");
                sessionRoot.SetAttribute("token", sessionToken);
                sessionXml.AppendChild(sessionRoot);
            }
            sessionRoot.SetAttribute("userName", details.UserName);
            sessionRoot.SetAttribute("expiry", details.ExpiryTime.ToString("o"));
            valuesNode = sessionXml.SelectSingleNode("values") as XmlElement;
            if (valuesNode != null) valuesNode.ParentNode.RemoveChild(valuesNode);
            valuesNode = sessionXml.CreateElement("values");
            sessionRoot.AppendChild(valuesNode);
            foreach (string key in details.Values.Keys)
            {
                XmlElement valueNode = sessionXml.CreateElement("value");
                valueNode.SetAttribute("key", key);
                object keyValue = details.Values[key];
                valueNode.SetAttribute("type", keyValue.GetType().Name);
                valueNode.InnerText = keyValue.ToString();
                valuesNode.AppendChild(valueNode);
            }
            sessionXml.Save(sessionFile);
        }
        protected virtual string GenerateFileName(string sessionToken)
        {
            string sessionFile = Path.Combine(storeLocation, sessionToken + ".session");
            return sessionFile;
        }
    }
}
