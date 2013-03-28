using System.Collections.Generic;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Tasks;
using ThoughtWorks.CruiseControl.Remote.Parameters;
using System.Xml;
namespace ThoughtWorks.CruiseControl.Core.Sourcecontrol
{
 [ReflectorType("multi")]
 public class MultiSourceControl
        : SourceControlBase
 {
  private ISourceControl[] _sourceControls;
  [ReflectorProperty("requireChangesFromAll", Required=false)]
  public bool RequireChangesFromAll = false;
  [ReflectorArray("sourceControls", Required=true)]
  public ISourceControl[] SourceControls
  {
   get
   {
    if (_sourceControls == null)
     _sourceControls = new ISourceControl[0];
    return _sourceControls;
   }
   set { _sourceControls = value; }
  }
        public override Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
  {
            var modificationSet = new Dictionary<Modification, bool>();
            foreach (ISourceControl sourceControl in SourceControls)
            {
                Modification[] mods = sourceControl.GetModifications(from_, to);
                if (mods != null && mods.Length > 0)
                {
                    foreach (var mod in mods)
                    {
                        modificationSet[mod] = true;
                    }
                }
                else if (RequireChangesFromAll)
                {
                    modificationSet.Clear();
                    break;
                }
            }
            var modArray = new Modification[modificationSet.Count];
            modificationSet.Keys.CopyTo(modArray, 0);
            return modArray;
  }
        public override void LabelSourceControl(IIntegrationResult result)
  {
   foreach (ISourceControl sourceControl in SourceControls)
   {
    sourceControl.LabelSourceControl(result);
   }
  }
        public override void GetSource(IIntegrationResult result)
  {
   foreach (ISourceControl sourceControl in SourceControls)
   {
    sourceControl.GetSource(result);
   }
  }
        public override void Initialize(IProject project)
  {
  }
        public override void Purge(IProject project)
  {
  }
        public override void ApplyParameters(Dictionary<string, string> parameters, IEnumerable<ParameterBase> parameterDefinitions)
        {
            base.ApplyParameters(parameters, parameterDefinitions);
            foreach (var child in SourceControls)
            {
                var dynamicChild = child as IParamatisedItem;
                if (dynamicChild != null) dynamicChild.ApplyParameters(parameters, parameterDefinitions);
            }
        }
        [ReflectionPreprocessor]
        public override XmlNode PreprocessParameters(NetReflectorTypeTable typeTable, XmlNode inputNode)
        {
            return DynamicValueUtility.ConvertXmlToDynamicValues(typeTable, inputNode, "sourceControls");
        }
    }
}
