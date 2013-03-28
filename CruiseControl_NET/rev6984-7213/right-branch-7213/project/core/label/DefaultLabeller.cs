using System.Text.RegularExpressions;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.Core.Label
{
    [ReflectorType("defaultlabeller")]
    public class DefaultLabeller
        : LabellerBase
    {
        public const int INITIAL_LABEL = 1;
        [ReflectorProperty("prefix", Required = false)]
        public string LabelPrefix = string.Empty;
        [ReflectorProperty("postfix", Required = false)]
        public string LabelPostfix = string.Empty;
  [ReflectorProperty("initialBuildLabel", Required = false)]
  public int InitialBuildLabel = INITIAL_LABEL;
        [ReflectorProperty("incrementOnFailure", Required = false)]
        public bool IncrementOnFailed = false;
        [ReflectorProperty("labelFormat", Required = false)]
        public string LabelFormat = "0";
        public override string Generate(IIntegrationResult integrationResult)
        {
            IntegrationSummary lastIntegration = integrationResult.LastIntegration;
            if (integrationResult == null || lastIntegration.IsInitial())
            {
    return LabelPrefix + InitialBuildLabel.ToString(LabelFormat) + LabelPostfix;
            }
            else if (ShouldIncrementLabel(lastIntegration))
            {
                return LabelPrefix + IncrementLabel(lastIntegration.Label) + LabelPostfix;
            }
            else
            {
                return integrationResult.LastIntegration.Label;
            }
        }
        private bool ShouldIncrementLabel(IntegrationSummary previousResult)
        {
            return previousResult.Status == IntegrationStatus.Success || IncrementOnFailed;
        }
        private string IncrementLabel(string label)
        {
            if (LabelPostfix.Length == 0 && LabelPrefix.Length > 0)
            {
                string numericLabel = Regex.Replace(label, @".*?(\d+$)", "$1");
                int newLabel = int.Parse(numericLabel);
                newLabel++;
                return newLabel.ToString(LabelFormat);
            }
            if (LabelPrefix.Length == 0 && LabelPostfix.Length >= 0)
            {
                string numericLabel = Regex.Replace(label, @"\D*?(\d{1,9}).*", "$1");
                int newLabel = int.Parse(numericLabel);
                newLabel++;
                return newLabel.ToString(LabelFormat);
            }
            MatchCollection NumericParts = Regex.Matches(label, @"\D*?(\d{1,9})\D*");
            if (NumericParts.Count == 1)
            {
                int newLabel = int.Parse(NumericParts[0].ToString());
                newLabel++;
                return newLabel.ToString(LabelFormat);
            }
            label = label.Replace(LabelPrefix, string.Empty);
            label = label.Replace(LabelPostfix, string.Empty);
            NumericParts = Regex.Matches(label, @"\D*?(\d{1,9})\D*");
            if (NumericParts.Count == 1)
            {
                int newLabel = int.Parse(NumericParts[0].ToString());
                newLabel++;
                return newLabel.ToString(LabelFormat);
            }
            throw new CruiseControlException("Unable to determine numeric part in label, pre and postfix may not contain multiple numeric parts");
        }
    }
}
