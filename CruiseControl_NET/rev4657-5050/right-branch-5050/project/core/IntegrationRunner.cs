using System;
using System.IO;
using ThoughtWorks.CruiseControl.Core.Sourcecontrol;
using ThoughtWorks.CruiseControl.Core.Util;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.Core
{
    public class IntegrationRunner : IIntegratable
    {
        public IIntegrationRunnerTarget target;
        private readonly IIntegrationResultManager resultManager;
        private readonly IQuietPeriod quietPeriod;
        public IntegrationRunner(IIntegrationResultManager resultManager, IIntegrationRunnerTarget target, IQuietPeriod quietPeriod)
        {
            this.target = target;
            this.quietPeriod = quietPeriod;
            this.resultManager = resultManager;
        }
        public IIntegrationResult Integrate(IntegrationRequest request)
        {
            IIntegrationResult result = resultManager.StartNewIntegration(request);
            IIntegrationResult lastResult = resultManager.LastIntegrationResult;
            CreateDirectoryIfItDoesntExist(result.WorkingDirectory);
            CreateDirectoryIfItDoesntExist(result.ArtifactDirectory);
            result.MarkStartTime();
            bool RunBuild = false;
            try
            {
                result.Modifications = GetModifications(lastResult, result);
            }
            catch (Exception error)
            {
                result.SourceControlError = error;
                Log.Warning(string.Format("Source control failure (GetModifications): {0}", error.Message));
                if (request.PublishOnSourceControlException)
                {
                    result.ExceptionResult = error;
                    CompleteIntegration(result);
                }
            }
            try
            {
                RunBuild = (result.SourceControlError == null) && result.ShouldRunBuild();
                if (RunBuild)
                {
                    Log.Info("Building: " + request);
                    if (result.LastIntegrationStatus == IntegrationStatus.Exception)
                    {
                        IntegrationSummary isExceptionFix = new IntegrationSummary(IntegrationStatus.Success, result.LastIntegration.Label, result.LastIntegration.LastSuccessfulIntegrationLabel , result.LastIntegration.StartTime);
                        IIntegrationResult irExceptionFix = new IntegrationResult(result.ProjectName, result.WorkingDirectory, result.ArtifactDirectory, result.IntegrationRequest, isExceptionFix);
                        target.CreateLabel(irExceptionFix);
                        result.Label = irExceptionFix.Label;
                    }
                    else
                    {
                        target.CreateLabel(result);
                    }
                    Build(result);
                }
            }
            catch (Exception ex)
            {
                result.ExceptionResult = ex;
            }
            finally
            {
                if (RunBuild)
                {
                    CompleteIntegration(result);
                }
            }
            target.Activity = ProjectActivity.Sleeping;
            return result;
        }
        private void CompleteIntegration(IIntegrationResult result)
        {
            result.MarkEndTime();
            PostBuild(result);
            Log.Info(string.Format("Integration complete: {0} - {1}", result.Status, result.EndTime));
        }
        private Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
        {
            target.Activity = ProjectActivity.CheckingModifications;
            to.BuildProgressInformation.SignalStartRunTask("Getting source ... ");
            return quietPeriod.GetModifications(target.SourceControl, from_, to);
        }
        private void Build(IIntegrationResult result)
        {
            target.Activity = ProjectActivity.Building;
            target.Prebuild(result);
            if (!result.Failed)
            {
                target.SourceControl.GetSource(result);
                target.Run(result);
                target.SourceControl.LabelSourceControl(result);
            }
        }
        private void PostBuild(IIntegrationResult result)
        {
            resultManager.FinishIntegration();
            target.PublishResults(result);
        }
        private static void CreateDirectoryIfItDoesntExist(string directory)
        {
            if (!Directory.Exists(directory))
                Directory.CreateDirectory(directory);
        }
    }
}
