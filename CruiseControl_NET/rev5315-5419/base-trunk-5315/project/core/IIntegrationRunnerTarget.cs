using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.Core
{
 public interface IIntegrationRunnerTarget : ITask
 {
  ISourceControl SourceControl { get; }
  void Prebuild(IIntegrationResult result);
  void PublishResults(IIntegrationResult result);
        ProjectActivity Activity { set; get;}
        void CreateLabel(IIntegrationResult result);
        void RecordSourceControlOperation(SourceControlOperation operation, ItemBuildStatus status);
 }
}
