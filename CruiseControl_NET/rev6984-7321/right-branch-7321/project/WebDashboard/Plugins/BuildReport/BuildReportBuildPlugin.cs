using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.WebDashboard.Dashboard;
using ThoughtWorks.CruiseControl.WebDashboard.Dashboard.Actions;
using ThoughtWorks.CruiseControl.WebDashboard.Dashboard.GenericPlugins;
namespace ThoughtWorks.CruiseControl.WebDashboard.Plugins.BuildReport
{
 [ReflectorType("buildReportBuildPlugin")]
 public class BuildReportBuildPlugin : ProjectConfigurableBuildPlugin
 {
  public static readonly string ACTION_NAME = "ViewBuildReport";
  private readonly IActionInstantiator actionInstantiator;
  public BuildReportBuildPlugin(IActionInstantiator actionInstantiator)
  {
   this.actionInstantiator = actionInstantiator;
  }
  public override string LinkDescription
  {
   get { return "Build Report"; }
  }
        [ReflectorProperty("xslFileNames", typeof(BuildReportXslFilenameSerialiserFactory))]
        public BuildReportXslFilename[] XslFileNames { get; set; }
  public override INamedAction[] NamedActions
  {
   get
   {
    MultipleXslReportBuildAction buildAction = (MultipleXslReportBuildAction) actionInstantiator.InstantiateAction(typeof (MultipleXslReportBuildAction));
    buildAction.XslFileNames = this.XslFileNames;
    return new INamedAction[] {new ImmutableNamedAction(ACTION_NAME, buildAction)};
   }
  }
 }
}
