using System;
using System.Collections.Generic;
using System.IO;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Sourcecontrol
{
    [ReflectorType("ftpSourceControl")]
    public class FtpSourceControl
        : SourceControlBase
    {
        private FtpLib ftp;
        [ReflectorProperty("serverName", Required = true)]
        public string ServerName = string.Empty;
        [ReflectorProperty("userName", Required = true)]
        public string UserName = string.Empty;
        [ReflectorProperty("password", Required = true)]
        public string Password = string.Empty;
        [ReflectorProperty("useActiveConnectionMode", Required = false)]
        public bool UseActiveConnectionMode = true;
        [ReflectorProperty("ftpFolderName", Required = true)]
        public string FtpFolderName = string.Empty;
        [ReflectorProperty("localFolderName", Required = true)]
        public string LocalFolderName = string.Empty;
        [ReflectorProperty("recursiveCopy", Required = true)]
        public bool RecursiveCopy = true;
        public override Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
        {
            ftp = new FtpLib(to.BuildProgressInformation);
            string remoteFolder = FtpFolderName;
            ftp.LogIn(ServerName,UserName,Password,UseActiveConnectionMode);
            if (!FtpFolderName.StartsWith("/"))
            {
                remoteFolder = System.IO.Path.Combine(ftp.CurrentWorkingFolder(), FtpFolderName);
            }
            Modification[] mods = ftp.ListNewOrUpdatedFilesAtFtpSite(LocalFolderName, remoteFolder, RecursiveCopy);
            ftp.DisConnect();
            return mods;
        }
        public override void LabelSourceControl(IIntegrationResult result)
        {
        }
        public override void GetSource(IIntegrationResult result)
        {
            Util.Log.Info(result.HasModifications().ToString());
            ftp = new FtpLib(result.BuildProgressInformation);
            string remoteFolder = FtpFolderName;
            ftp.LogIn(ServerName, UserName, Password, UseActiveConnectionMode);
            if (!FtpFolderName.StartsWith("/"))
            {
                remoteFolder = System.IO.Path.Combine(ftp.CurrentWorkingFolder(), FtpFolderName);
            }
            ftp.DownloadFolder( LocalFolderName, remoteFolder, RecursiveCopy);
            ftp.DisConnect();
        }
        public override void Initialize(IProject project)
        {
        }
        public override void Purge(IProject project)
        {
        }
    }
}
