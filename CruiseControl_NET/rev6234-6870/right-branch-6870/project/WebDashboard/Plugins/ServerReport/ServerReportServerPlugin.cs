using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.WebDashboard.Dashboard;
using ThoughtWorks.CruiseControl.WebDashboard.IO;
using ThoughtWorks.CruiseControl.WebDashboard.MVC;
using ThoughtWorks.CruiseControl.WebDashboard.MVC.Cruise;
using ThoughtWorks.CruiseControl.WebDashboard.MVC.View;
using System;
namespace ThoughtWorks.CruiseControl.WebDashboard.Plugins.ServerReport
{
 [ReflectorType("serverReportServerPlugin")]
 public class ServerReportServerPlugin : ICruiseAction, IPlugin
 {
  public static readonly string ACTION_NAME = "ViewServerReport";
  private readonly IProjectGridAction projectGridAction;
        private ProjectGridSortColumn? sortColumn;
        [ReflectorProperty("defaultSort", Required = false)]
        public string DefaultSortColumn
        {
            get { return sortColumn.GetValueOrDefault(ProjectGridSortColumn.Name).ToString(); }
            set
            {
                if (string.IsNullOrEmpty(value))
                {
                    sortColumn = null;
                }
                else
                {
                    sortColumn = (ProjectGridSortColumn)Enum.Parse(typeof(ProjectGridSortColumn), value);
                }
            }
        }
  public ServerReportServerPlugin(IProjectGridAction projectGridAction)
  {
   this.projectGridAction = projectGridAction;
  }
  public IResponse Execute(ICruiseRequest request)
  {
            if (sortColumn.HasValue) projectGridAction.DefaultSortColumn = sortColumn.Value;
            return projectGridAction.Execute(ACTION_NAME, request.ServerSpecifier, request);
  }
  public string LinkDescription
  {
   get { return "Server Report"; }
  }
  public INamedAction[] NamedActions
  {
   get { return new INamedAction[] { new ImmutableNamedAction(ACTION_NAME, this) }; }
  }
 }
}
