using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;
namespace ThoughtWorks.CruiseControl.MigrationWizard
{
    public class MigrationEngine
    {
        private Stack<Action<string> > rollbackActions = new Stack<Action<string> >();
        private Version versionNumber;
        private readonly Version version140 = new Version(1, 4, 0);
        private readonly Version version141 = new Version(1, 4, 1);
        private readonly Version version142 = new Version(1, 4, 2);
        private readonly Version version143 = new Version(1, 4, 3);
        private readonly Version version144 = new Version(1, 4, 4);
        private readonly Version version150 = new Version(1, 5, 0);
        public MigrationOptions MigrationOptions { get; set; }
        public void Run()
        {
            FireMessage("Starting migration", MigrationEventType.Information);
            var isSuccessful = true;
            versionNumber = new Version(MigrationOptions.CurrentVersion);
            if (MigrationOptions.MigrateServer && isSuccessful)
            {
                isSuccessful = MigrateServerFiles();
            }
            if (MigrationOptions.MigrateConfiguration && isSuccessful)
            {
                isSuccessful = MigrateConfiguration();
            }
            if (MigrationOptions.MigrateWebDashboard && isSuccessful)
            {
                isSuccessful = MigrateWebDashboardFiles();
            }
            if (isSuccessful)
            {
                FireMessage("Migration has completed successfully", MigrationEventType.Information);
            }
            else
            {
                FireMessage("Migration has failed - rolling back changes", MigrationEventType.Warning);
                while (rollbackActions.Count > 0)
                {
                    var action = rollbackActions.Pop();
                    action(null);
                }
                FireMessage("Rollback completed", MigrationEventType.Information);
            }
        }
        public event EventHandler<MigrationEventArgs> Message;
        private void FireMessage(string message, MigrationEventType type)
        {
            if (Message != null)
            {
                Message(this, new MigrationEventArgs(message, type));
            }
        }
        private bool MigrateServerFiles()
        {
            var isSuccessful = false;
            try
            {
                FireMessage("Migrating server files", MigrationEventType.Information);
                if (versionNumber < version150)
                {
                    EnsureFolderExists(MigrationOptions.NewServerLocation, "server");
                }
                var document = new XmlDocument();
                document.Load(MigrationOptions.ConfigurationLocation);
                if (versionNumber < version150)
                {
                    var projectFoldersToMove = FindProjectFoldersToMove(document);
                    if (projectFoldersToMove.Count == 0)
                    {
                        FireMessage("No project folders found", MigrationEventType.Information);
                    }
                    else
                    {
                        FireMessage(string.Format("{0} project folder(s) found", projectFoldersToMove.Count), MigrationEventType.Information);
                        foreach (var projectName in projectFoldersToMove)
                        {
                            var oldProjectPath = Path.Combine(MigrationOptions.CurrentServerLocation, projectName);
                            var newProjectPath = Path.Combine(MigrationOptions.NewServerLocation, projectName);
                            MoveFolder(oldProjectPath,
                                newProjectPath,
                                string.Format("Project folder for '{0}' moved", projectName),
                                string.Format("Project folder for '{0}' restored", projectName));
                        }
                    }
                    var projectStatesToMove = FindProjectStatesToMove(document);
                    if (projectStatesToMove.Count == 0)
                    {
                        FireMessage("No project state files found", MigrationEventType.Information);
                    }
                    else
                    {
                        FireMessage(string.Format("{0} project state file(s) found", projectStatesToMove.Count), MigrationEventType.Information);
                        foreach (var projectName in projectStatesToMove)
                        {
                            var oldProjectPath = Path.Combine(MigrationOptions.CurrentServerLocation, projectName + ".state");
                            var newProjectPath = Path.Combine(MigrationOptions.NewServerLocation, projectName + ".state");
                            MoveFile(oldProjectPath,
                                newProjectPath,
                                string.Format("Project state file for '{0}' moved", projectName),
                                string.Format("Project state file for '{0}' restored", projectName));
                        }
                    }
                    var stateFile = Path.Combine(MigrationOptions.CurrentServerLocation, "ProjectsState.xml");
                    if (File.Exists(stateFile))
                    {
                        MoveFile(stateFile,
                            Path.Combine(MigrationOptions.NewServerLocation, "ProjectsState.xml"),
                            "Project state file moved",
                            "Project state file restored");
                    }
                }
                var logLocation = Path.Combine(MigrationOptions.NewServerLocation, "ccnet.log");
                UpdateServerConfigurationFile(logLocation, "ccnet.exe.config");
                UpdateServerConfigurationFile(logLocation, "ccservice.exe.config");
                if (versionNumber < version143)
                {
                    var statisticsFiles = FindStatisticsFiles(document);
                    if (statisticsFiles.Count == 0)
                    {
                        FireMessage("No statistics files found", MigrationEventType.Information);
                    }
                    else
                    {
                        FireMessage(string.Format("{0} statistics file(s) found", statisticsFiles.Count), MigrationEventType.Information);
                        foreach (var statisticsFile in statisticsFiles)
                        {
                            FireMessage(string.Format("Migrating {0}", statisticsFile), MigrationEventType.Information);
                            var lines = File.ReadAllLines(statisticsFile);
                            using (var output = File.CreateText(statisticsFile))
                            {
                                foreach (var line in lines)
                                {
                                    if (!string.Equals(line, "<statistics>") &&
                                        !string.Equals(line, "</statistics>"))
                                    {
                                        output.WriteLine(line);
                                    }
                                }
                                output.Flush();
                            }
                        }
                    }
                }
                FireMessage("Server files successfully migrated", MigrationEventType.Information);
                isSuccessful = true;
            }
            catch (Exception error)
            {
                FireMessage("An unexpected error has occurred while migrating server files: " + error.Message, MigrationEventType.Error);
            }
            FireMessage("Server migration completed " + (isSuccessful ? "without errors" : "with errors"), MigrationEventType.Status);
            return isSuccessful;
        }
        private void UpdateServerConfigurationFile(string logLocation, string configFileName)
        {
            FireMessage("Updating server configuration file: " + configFileName, MigrationEventType.Information);
            var changeCount = 0;
            var configFile = Path.Combine(MigrationOptions.CurrentServerLocation, configFileName);
            if (MigrationOptions.BackupServerConfiguration)
            {
                BackupFile(configFile);
            }
            var configDocument = new XmlDocument();
            configDocument.Load(configFile);
            if (versionNumber < version150)
            {
                changeCount += UpdateAttribute(configDocument, "/configuration/appSettings/add[@key='ServerLogFilePath']", "value", "ccnet.log", logLocation) ? 1 : 0;
                changeCount += UpdateAttribute(configDocument, "/configuration/log4net/appender[@name='RollingFileAppender']/file", "value", "ccnet.log", logLocation) ? 1 : 0;
            }
            if (changeCount > 0)
            {
                configDocument.Save(configFile);
                FireMessage("Server configuration file updated", MigrationEventType.Information);
            }
            else
            {
                FireMessage("Server configuration file not changed - up to date", MigrationEventType.Information);
            }
        }
        private bool UpdateAttribute(XmlDocument configDocument, string xpath, string attribute, string oldValue, string newValue)
        {
            var updated = false;
            var node = configDocument.SelectSingleNode(xpath) as XmlElement;
            if (node != null)
            {
                var nodeValue = node.GetAttribute(attribute);
                if (string.Equals(oldValue, nodeValue))
                {
                    node.SetAttribute(attribute, newValue);
                    updated = true;
                }
            }
            return updated;
        }
        private void BackupFile(string fileName)
        {
            FireMessage("Backing up " + fileName, MigrationEventType.Information);
            var loop = 0;
            var newFile = Path.Combine(Path.GetDirectoryName(fileName),
                Path.GetFileNameWithoutExtension(fileName) +
                "(Backup)" +
                Path.GetExtension(fileName));
            while (File.Exists(newFile))
            {
                newFile = Path.Combine(Path.GetDirectoryName(fileName),
                    Path.GetFileNameWithoutExtension(fileName) +
                    string.Format(" (Backup {0})", ++loop) +
                    Path.GetExtension(fileName));
            }
            File.Copy(fileName, newFile);
            FireMessage(
                string.Format("{0} backed up to {1}", Path.GetFileName(fileName), newFile),
                MigrationEventType.Information);
        }
        private XmlNode FindProjectNode(XmlNode itemNode)
        {
            var node = itemNode.ParentNode;
            while ((node != null) && (node.Name != "project"))
            {
                node = node.ParentNode;
            }
            return node;
        }
        private List<string> FindStatisticsFiles(XmlDocument document)
        {
            FireMessage("Searching for statistics publishers", MigrationEventType.Information);
            var statisticsFiles = new List<string>();
            foreach (XmlElement statisticsEl in document.SelectNodes("//statistics"))
            {
                var projectEl = FindProjectNode(statisticsEl) as XmlElement;
                var artefactFolder = RetrievePropertyValue(projectEl, "artifactDirectory");
                string fileName;
                if (!string.IsNullOrEmpty(artefactFolder))
                {
                    fileName = Path.Combine(artefactFolder, "report.xml");
                }
                else
                {
                    var projectName = RetrievePropertyValue(projectEl, "name");
                    fileName = Path.Combine(
                            Path.Combine(MigrationOptions.NewServerLocation, projectName),
                            Path.Combine("Artifacts", "report.xml"));
                }
                if (File.Exists(fileName))
                {
                    statisticsFiles.Add(fileName);
                }
            }
            return statisticsFiles;
        }
        private List<string> FindProjectFoldersToMove(XmlDocument document)
        {
            FireMessage("Searching for project folders to migrate", MigrationEventType.Information);
            var projectFoldersToMove = new List<string>();
            foreach (XmlElement projectEl in document.SelectNodes("/cruisecontrol/project"))
            {
                var hasWorking = !string.IsNullOrEmpty(projectEl.GetAttribute("workingDirectory")) ||
                    (projectEl.SelectSingleNode("workingDirectory") != null);
                var hasArtefact = !string.IsNullOrEmpty(projectEl.GetAttribute("artifactDirectory")) ||
                    (projectEl.SelectSingleNode("artifactDirectory") != null);
                if (!hasArtefact || !hasWorking)
                {
                    var projectName = RetrievePropertyValue(projectEl, "name");
                    if (!string.IsNullOrEmpty(projectName))
                    {
                        var projectPath = Path.Combine(MigrationOptions.CurrentServerLocation, projectName);
                        if (Directory.Exists(projectPath)) projectFoldersToMove.Add(projectName);
                    }
                }
            }
            return projectFoldersToMove;
        }
        private List<string> FindProjectStatesToMove(XmlDocument document)
        {
            FireMessage("Searching for project state files to migrate", MigrationEventType.Information);
            var projectStatesToMove = new List<string>();
            foreach (XmlElement projectEl in document.SelectNodes("/cruisecontrol/project"))
            {
                var projectName = RetrievePropertyValue(projectEl, "name");
                if (!string.IsNullOrEmpty(projectName))
                {
                    var projectPath = Path.Combine(MigrationOptions.CurrentServerLocation, projectName + ".state");
                    if (File.Exists(projectPath)) projectStatesToMove.Add(projectName);
                }
            }
            return projectStatesToMove;
        }
        private string RetrievePropertyValue(XmlElement parentEl, string property)
        {
            var value = parentEl.GetAttribute(property);
            if (string.IsNullOrEmpty(value))
            {
                var nameEl = parentEl.SelectSingleNode(property);
                if (nameEl != null) value = nameEl.InnerText;
            }
            return value;
        }
        private void MoveFolder(string oldPath, string newPath, string completedMessage, string rollbackMessage)
        {
            FireMessage(
                string.Format("Moving folder '{0}' to '{1}'", oldPath, newPath),
                MigrationEventType.Information);
            if (Directory.Exists(newPath))
            {
                FireMessage(
                    string.Format("Folder '{0}' already exists - unable to move", newPath),
                    MigrationEventType.Warning);
            }
            else
            {
                Directory.Move(oldPath, newPath);
                FireMessage(completedMessage, MigrationEventType.Information);
                rollbackActions.Push(s =>
                {
                    FireMessage(
                        string.Format("Moving folder '{0}' to '{1}'", newPath, oldPath),
                        MigrationEventType.Information);
                    Directory.Move(newPath, oldPath);
                    FireMessage(rollbackMessage, MigrationEventType.Information);
                });
            }
        }
        private void MoveFile(string oldPath, string newPath, string completedMessage, string rollbackMessage)
        {
            FireMessage(
                string.Format("Moving file '{0}' to '{1}'", oldPath, newPath),
                MigrationEventType.Information);
            if (File.Exists(newPath))
            {
                FireMessage(
                    string.Format("File '{0}' already exists - unable to move", newPath),
                    MigrationEventType.Warning);
            }
            else
            {
                File.Move(oldPath, newPath);
                FireMessage(completedMessage, MigrationEventType.Information);
                rollbackActions.Push(s =>
                {
                    FireMessage(
                        string.Format("Moving file '{0}' to '{1}'", newPath, oldPath),
                        MigrationEventType.Information);
                    File.Move(newPath, oldPath);
                    FireMessage(rollbackMessage, MigrationEventType.Information);
                });
            }
        }
        private void EnsureFolderExists(string folderPath, string name)
        {
            if (!Directory.Exists(folderPath))
            {
                FireMessage("Creating " + name + " folder: " + folderPath, MigrationEventType.Information);
                Directory.CreateDirectory(folderPath);
                rollbackActions.Push(s =>
                {
                    FireMessage("Deleting " + name + " folder: " + folderPath, MigrationEventType.Information);
                    Directory.Delete(folderPath, true);
                    FireMessage("Server folder deleted", MigrationEventType.Information);
                });
                FireMessage(CapitialiseName(name) + " folder created", MigrationEventType.Information);
            }
            else
            {
                FireMessage(CapitialiseName(name) + " folder already exists: " + folderPath, MigrationEventType.Information);
            }
        }
        private string CapitialiseName(string name)
        {
            return name.Substring(0, 1).ToUpper() + name.Substring(1);
        }
        private bool MigrateConfiguration()
        {
            var isSuccessful = false;
            try
            {
                FireMessage("Migrating configuration", MigrationEventType.Information);
                if (MigrationOptions.BackupConfiguration)
                {
                    BackupFile(MigrationOptions.ConfigurationLocation);
                }
                var configDoc = new XmlDocument();
                configDoc.Load(MigrationOptions.ConfigurationLocation);
                if (versionNumber < version150)
                {
                    FireMessage("Updating e-mail group configuration", MigrationEventType.Information);
                    var groups = configDoc.SelectNodes("//email/groups/group");
                    if (groups.Count == 0)
                    {
                        FireMessage("No e-mail groups found", MigrationEventType.Information);
                    }
                    else
                    {
                        foreach (XmlElement groupEl in groups)
                        {
                            var notification = RetrievePropertyValue(groupEl, "notification");
                            var notificationEl = groupEl.SelectSingleNode("notification");
                            if (notificationEl != null)
                            {
                                groupEl.RemoveChild(notificationEl);
                            }
                            else
                            {
                                groupEl.RemoveAttribute("notification");
                            }
                            var notificationsEl = configDoc.CreateElement("notifications");
                            groupEl.AppendChild(notificationsEl);
                            notificationEl = configDoc.CreateElement("notificationType");
                            notificationsEl.AppendChild(notificationEl);
                            if (!string.IsNullOrEmpty(notification))
                            {
                                notificationEl.InnerText = notification;
                            }
                            else
                            {
                                notificationEl.InnerText = "Always";
                            }
                        }
                        FireMessage(
                            string.Format("{0} e-mail groups found and converted", groups.Count),
                            MigrationEventType.Information);
                    }
                }
                configDoc.Save(MigrationOptions.ConfigurationLocation);
                FireMessage("Configuration files successfully migrated", MigrationEventType.Information);
                isSuccessful = true;
            }
            catch (Exception error)
            {
                FireMessage("An unexpected error has occurred while migrating configuration: " + error.Message, MigrationEventType.Error);
            }
            FireMessage("Configuration migration completed " + (isSuccessful ? "without errors" : "with errors"), MigrationEventType.Status);
            return isSuccessful;
        }
        private bool MigrateWebDashboardFiles()
        {
            var isSuccessful = false;
            try
            {
                FireMessage("Migrating web dashboard files", MigrationEventType.Information);
                if (versionNumber < version150)
                {
                    EnsureFolderExists(MigrationOptions.NewWebDashboardLocation, "web dashboard");
                }
                if (versionNumber < version150)
                {
                    var stateFile = Path.Combine(MigrationOptions.CurrentWebDashboardLocation, "dashboard.config");
                    if (File.Exists(stateFile))
                    {
                        MoveFile(stateFile,
                            Path.Combine(MigrationOptions.NewWebDashboardLocation, "dashboard.config"),
                            "Dashboard configuration moved",
                            "Dashboard configuration restored");
                    }
                    var packagesFolder = Path.Combine(MigrationOptions.CurrentWebDashboardLocation, "packages");
                    if (Directory.Exists(packagesFolder))
                    {
                        MoveFolder(packagesFolder,
                            Path.Combine(MigrationOptions.NewWebDashboardLocation, "packages"),
                            "Packages folder moved",
                            "Packages folder restored");
                    }
                }
                FireMessage("Web dashboard files successfully migrated", MigrationEventType.Information);
                isSuccessful = true;
            }
            catch (Exception error)
            {
                FireMessage("An unexpected error has occurred while migrating web dashboard files: " + error.Message, MigrationEventType.Error);
            }
            FireMessage("Web dashboard migration completed " + (isSuccessful ? "without errors" : "with errors"), MigrationEventType.Status);
            return isSuccessful;
        }
    }
}
