using Exortech.NetReflector;
namespace ThoughtWorks.CruiseControl.Core.Sourcecontrol
{
 [ReflectorType("nullSourceControl")]
 public class NullSourceControl : ISourceControl
 {
  public Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
  {
   return new Modification[0];
  }
  public void LabelSourceControl(IIntegrationResult result)
  {
  }
  public void GetSource(IIntegrationResult result)
  {
  }
  public void Initialize(IProject project)
  {
  }
  public void Purge(IProject project)
  {
  }
 }
}
