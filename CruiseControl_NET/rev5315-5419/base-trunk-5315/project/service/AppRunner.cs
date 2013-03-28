using System;
using System.Collections.Generic;
using System.Text;
using ThoughtWorks.CruiseControl.Core.Config;
using System.IO;
using ThoughtWorks.CruiseControl.Core.Util;
using System.Reflection;
using ThoughtWorks.CruiseControl.Remote;
using ThoughtWorks.CruiseControl.Core;
using System.Configuration;
using System.Globalization;
namespace ThoughtWorks.CruiseControl.Service
{
    public class AppRunner
        : MarshalByRefObject
    {
        private const string DefaultConfigFileName = "ccnet.config";
        private readonly string DefaultDirectory = AppDomain.CurrentDomain.BaseDirectory;
        private ICruiseServer server;
        private bool isStopping = false;
        private object lockObject = new object();
        private string ConfigFilename
        {
            get
            {
                string configFilename = ConfigurationManager.AppSettings["ccnet.config"];
                return StringUtil.IsBlank(configFilename) ? DefaultConfigFilePath() : configFilename;
            }
        }
        private static string Remoting
        {
            get { return ConfigurationManager.AppSettings["remoting"]; }
        }
        public void Run()
        {
            Directory.SetCurrentDirectory(DefaultDirectory);
            Log.Info(string.Format("CruiseControl.NET Server {0} -- .NET Continuous Integration Server", Assembly.GetExecutingAssembly().GetName().Version));
            AssemblyCopyrightAttribute[] copyrightAttributes = (AssemblyCopyrightAttribute[])Assembly.GetExecutingAssembly().GetCustomAttributes(typeof(AssemblyCopyrightAttribute), false);
            if (copyrightAttributes.Length > 0)
            {
                Log.Info(string.Format("{0}  All Rights Reserved.", copyrightAttributes[0].Copyright));
            }
            Log.Info(string.Format(".NET Runtime Version: {0}{2}\tImage Runtime Version: {1}", Environment.Version, Assembly.GetExecutingAssembly().ImageRuntimeVersion, GetRuntime()));
            Log.Info(string.Format("OS Version: {0}\tServer locale: {1}", Environment.OSVersion, CultureInfo.CurrentUICulture.NativeName));
            VerifyConfigFileExists();
            CreateAndStartCruiseServer();
        }
        public void Stop(string reason)
        {
            bool stopRunner = false;
            lock (lockObject)
            {
                if (!isStopping)
                {
                    stopRunner = true;
                    isStopping = true;
                }
            }
            if (stopRunner)
            {
                Log.Info("Stopping service: " + reason);
                server.Stop();
                server.WaitForExit();
            }
        }
        private string DefaultConfigFilePath()
        {
            return Path.Combine(DefaultDirectory, DefaultConfigFileName);
        }
        private void VerifyConfigFileExists()
        {
            FileInfo configFileInfo = new FileInfo(ConfigFilename);
            if (!configFileInfo.Exists)
            {
                throw new Exception(
                    string.Format("CruiseControl.NET configuration file {0} does not exist.",
                        configFileInfo.FullName));
            }
        }
        private void CreateAndStartCruiseServer()
        {
            server = new CruiseServerFactory().Create(UseRemoting(), ConfigFilename);
            server.Start();
        }
        private static bool UseRemoting()
        {
            return (Remoting != null && Remoting.Trim().ToLower() == "on");
        }
        private static string GetRuntime()
        {
            if (Type.GetType("Mono.Runtime") != null)
                return " [Mono]";
            return string.Empty;
        }
    }
}
