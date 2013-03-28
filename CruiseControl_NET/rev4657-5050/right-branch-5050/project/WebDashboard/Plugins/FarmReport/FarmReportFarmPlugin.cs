using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.WebDashboard.Dashboard;
using ThoughtWorks.CruiseControl.WebDashboard.IO;
using ThoughtWorks.CruiseControl.WebDashboard.MVC;
using ThoughtWorks.CruiseControl.WebDashboard.MVC.Cruise;
using ThoughtWorks.CruiseControl.WebDashboard.MVC.View;
using System;
namespace ThoughtWorks.CruiseControl.WebDashboard.Plugins.FarmReport
{
 [ReflectorType("farmReportFarmPlugin")]
 public class FarmReportFarmPlugin : ICruiseAction, IPlugin
 {
  public static readonly string ACTION_NAME = "ViewFarmReport";
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
  public FarmReportFarmPlugin(IProjectGridAction projectGridAction)
  {
   this.projectGridAction = projectGridAction;
  }
  public IResponse Execute(ICruiseRequest request)
  {
            if (sortColumn.HasValue) projectGridAction.DefaultSortColumn = sortColumn.Value;
   return projectGridAction.Execute(ACTION_NAME, request);
  }
  public string LinkDescription
  {
   get { return "Farm Report"; }
  }
  public INamedAction[] NamedActions
  {
   get { return new INamedAction[] { new ImmutableNamedAction(ACTION_NAME, this) }; }
  }
 }
}
