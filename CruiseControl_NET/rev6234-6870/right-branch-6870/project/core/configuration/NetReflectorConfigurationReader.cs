using System;
using System.IO;
using System.Xml;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Security;
using System.Collections.Generic;
using System.Configuration;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Config
{
 public class NetReflectorConfigurationReader
        : INetReflectorConfigurationReader
 {
  private const string ROOT_ELEMENT = "cruisecontrol";
  private const string CONFIG_ASSEMBLY_PATTERN = "ccnet.*.plugin.dll";
  private readonly NetReflectorTypeTable typeTable;
  private NetReflectorReader reader;
  public NetReflectorConfigurationReader()
  {
   typeTable = new NetReflectorTypeTable();
   typeTable.Add(AppDomain.CurrentDomain);
            string pluginLocation = ConfigurationManager.AppSettings["PluginLocation"];
            if (!string.IsNullOrEmpty(pluginLocation))
            {
                if (Directory.Exists(pluginLocation))
                {
                    typeTable.Add(pluginLocation, CONFIG_ASSEMBLY_PATTERN);
                }
                else
                {
                    throw new CruiseControlException("Unable to find plugin directory: " + pluginLocation);
                }
            }
   typeTable.Add(Directory.GetCurrentDirectory(), CONFIG_ASSEMBLY_PATTERN);
   reader = new NetReflectorReader(typeTable);
  }
        public IConfiguration Read(XmlDocument document,
            IConfigurationErrorProcesser errorProcesser)
  {
            Configuration configuration = new Configuration();
            string ConflictingXMLNode = string.Empty;
            List<string> projectNames = new List<string>();
            VerifyDocumentHasValidRootElement(document);
            var actualErrorProcesser = errorProcesser ?? new DefaultErrorProcesser();
            InvalidNodeEventHandler invalidNodeHandler = (args) =>
            {
                if (!actualErrorProcesser.ProcessUnhandledNode(args.Node))
                {
                    actualErrorProcesser.ProcessError(args.Message);
                }
            };
            typeTable.InvalidNode += invalidNodeHandler;
            try
            {
                foreach (XmlNode node in document.DocumentElement)
                {
                    ConflictingXMLNode = string.Empty;
                    if (!(node is XmlComment))
                    {
                        ConflictingXMLNode = "Conflicting project data : " + node.OuterXml;
                        object loadedItem = reader.Read(node);
                        if (loadedItem is IProject)
                        {
                            LoadAndValidateProject(actualErrorProcesser, projectNames, configuration, loadedItem);
                        }
                        else if (loadedItem is IQueueConfiguration)
                        {
                            LoadAndValidateQueue(configuration, loadedItem);
                        }
                        else if (loadedItem is ISecurityManager)
                        {
                            LoadAndValidateSecurityManager(configuration, loadedItem);
                        }
                        else
                        {
                            actualErrorProcesser.ProcessError(
                                new ConfigurationException(
                                    "\nUnknown configuration item found\n" + node.OuterXml));
                        }
                    }
                }
                ValidateConfiguration(configuration, actualErrorProcesser);
            }
            catch (NetReflectorException ex)
            {
                actualErrorProcesser.ProcessError(new ConfigurationException("\nUnable to instantiate CruiseControl projects from_ configuration document." +
                    "\nConfiguration document is likely missing Xml nodes required for properly populating CruiseControl configuration.\n"
                    + ex.Message +
                    "\n " + ConflictingXMLNode, ex));
            }
            finally
            {
                typeTable.InvalidNode -= invalidNodeHandler;
            }
            return configuration;
        }
        private void LoadAndValidateSecurityManager(Configuration configuration, object loadedItem)
        {
            ISecurityManager securityManager = loadedItem as ISecurityManager;
            configuration.SecurityManager = securityManager as ISecurityManager;
        }
        private void LoadAndValidateQueue(Configuration configuration, object loadedItem)
        {
            IQueueConfiguration queueConfig = loadedItem as IQueueConfiguration;
            configuration.QueueConfigurations.Add(queueConfig);
        }
        private void LoadAndValidateProject(IConfigurationErrorProcesser errorProcesser,
            List<string> projectNames, Configuration configuration, object loadedItem)
        {
            IProject project = loadedItem as IProject;
            string projectName = project.Name.ToLowerInvariant();
            if (projectNames.Contains(projectName))
            {
                errorProcesser.ProcessError(
                    new CruiseControlException(
                        string.Format(
                            "A duplicate project name ({0})has been found - projects must be unique per server",
                            projectName)));
            }
            else
            {
                projectNames.Add(projectName);
            }
            configuration.AddProject(project);
        }
  private void VerifyDocumentHasValidRootElement(XmlDocument configXml)
  {
   if (configXml.DocumentElement == null || configXml.DocumentElement.Name != ROOT_ELEMENT)
   {
    throw new ConfigurationException("The configuration document has an invalid root element.  Expected <cruisecontrol>.");
   }
  }
        private void ValidateConfiguration(Configuration value, IConfigurationErrorProcesser errorProcesser)
        {
            if (value.SecurityManager is IConfigurationValidation)
            {
                (value.SecurityManager as IConfigurationValidation).Validate(value, null, errorProcesser);
            }
            foreach (IProject project in value.Projects)
            {
                if (project is IConfigurationValidation)
                {
                    (project as IConfigurationValidation).Validate(value, null, errorProcesser);
                }
            }
            foreach (IQueueConfiguration queue in value.QueueConfigurations)
            {
                if (queue is IConfigurationValidation)
                {
                    (queue as IConfigurationValidation).Validate(value, null, errorProcesser);
                }
            }
        }
        private class DefaultErrorProcesser
            : IConfigurationErrorProcesser
        {
            public void ProcessError(string message)
            {
                throw new ConfigurationException(message);
            }
            public void ProcessError(Exception error)
            {
                throw error;
            }
            public void ProcessWarning(string message)
            {
                Log.Warning(message);
            }
            public bool ProcessUnhandledNode(XmlNode node)
            {
                return false;
            }
            public void ProcessError(string message, params object[] args)
            {
                throw new ConfigurationException(string.Format(message,args)) ;
            }
            public void ProcessWarning(string message, params object[] args)
            {
                Log.Warning(message, args) ;
            }
        }
 }
}
