using System;
using NUnit.Framework;
namespace ThoughtWorks.CruiseControl.UnitTests
{
    [TestFixture]
    public class CCnetDeploymentTests
    {
        [Test]
        public void TestForAdminPackageOfWebDashboardIsEmpty()
        {
            string configFile = System.IO.Path.Combine(System.IO.Directory.GetCurrentDirectory(), @"..\..\..\Webdashboard\dashboard.config");
            Assert.IsTrue(System.IO.File.Exists(configFile), "Dashboard.config not found at {0}", configFile);
            System.Xml.XmlDocument xdoc = new System.Xml.XmlDocument();
            xdoc.Load(configFile);
            var adminPluginNode = xdoc.SelectSingleNode("/dashboard/plugins/farmPlugins/administrationPlugin");
            Assert.IsNotNull(adminPluginNode, "Admin package configuration not found in dashboard.config at {0}", configFile);
            var pwd = adminPluginNode.Attributes["password"];
            Assert.IsNotNull(pwd, "password attribute not defined in admin packackage in dashboard.config at {0}", configFile);
            Assert.AreEqual("", pwd.Value, "Password must be empty string, to force users to enter one. No default passwords allowed in distribution");
        }
    }
}
