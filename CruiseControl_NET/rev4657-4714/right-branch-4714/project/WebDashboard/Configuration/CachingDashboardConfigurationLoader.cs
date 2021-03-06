using System;
using System.Web;
using System.Web.Caching;
using Objection;
using Objection.NetReflectorPlugin;
using ThoughtWorks.CruiseControl.WebDashboard.Configuration;
using ThoughtWorks.CruiseControl.WebDashboard.IO;
namespace ThoughtWorks.CruiseControl.WebDashboard.Configuration
{
 internal class CachingDashboardConfigurationLoader : IDashboardConfiguration
 {
  private const string DashboardConfigurationKey = "DashboardConfiguration";
  private IDashboardConfiguration dashboardConfiguration;
  public CachingDashboardConfigurationLoader(ObjectSource objectSource, HttpContext context)
  {
   dashboardConfiguration = context.Cache[DashboardConfigurationKey] as IDashboardConfiguration;
   if (dashboardConfiguration == null)
   {
    dashboardConfiguration = new DashboardConfigurationLoader(new ObjectionNetReflectorInstantiator(objectSource), new HttpPathMapper(context));
    context.Cache.Add(DashboardConfigurationKey, dashboardConfiguration, null, DateTime.MaxValue, TimeSpan.Zero, CacheItemPriority.Normal, null);
   }
  }
  public IRemoteServicesConfiguration RemoteServices
  {
   get { return dashboardConfiguration.RemoteServices; }
  }
  public IPluginConfiguration PluginConfiguration
  {
   get { return dashboardConfiguration.PluginConfiguration; }
  }
 }
}
