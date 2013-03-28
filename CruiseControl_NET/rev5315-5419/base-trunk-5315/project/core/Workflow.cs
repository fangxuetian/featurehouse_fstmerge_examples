using System;
using System.Collections;
using System.Xml;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Security;
using ThoughtWorks.CruiseControl.Remote;
using System.Collections.Generic;
namespace ThoughtWorks.CruiseControl.Core
{
 [ReflectorType("workflow")]
 public class Workflow : ProjectBase, IProject
 {
  private IList _tasks = new ArrayList();
  private WorkflowResult _currentIntegrationResult;
        private ProjectInitialState initialState = ProjectInitialState.Started;
        private ProjectStartupMode startupMode = ProjectStartupMode.UseLastState;
  [ReflectorCollection("tasks", InstanceType = typeof(ArrayList))]
  public IList Tasks
  {
   get { return _tasks; }
   set { _tasks = value; }
  }
  public IntegrationResult CurrentIntegration
  {
   get { return _currentIntegrationResult; }
  }
  public IIntegrationResult Integrate(IntegrationRequest request)
  {
   _currentIntegrationResult = new WorkflowResult();
   foreach (ITask task in Tasks)
   {
    try
    {
     RunTask(task);
    }
    catch (CruiseControlException ex)
    {
     _currentIntegrationResult.ExceptionResult = ex;
    }
   }
   return _currentIntegrationResult;
  }
  public void NotifyPendingState()
  {
   throw new NotImplementedException();
  }
  public void NotifySleepingState()
  {
   throw new NotImplementedException();
  }
  private void RunTask(ITask task)
  {
   task.Run(_currentIntegrationResult);
  }
  public IntegrationStatus LatestBuildStatus
  {
   get { return _currentIntegrationResult.Status; }
  }
  public void AbortRunningBuild()
  {
   throw new NotImplementedException();
  }
  public void Purge(bool purgeWorkingDirectory, bool purgeArtifactDirectory, bool purgeSourceControlEnvironment)
  {
   return;
  }
  public string Statistics
  {
   get { throw new NotImplementedException(); }
  }
        public string ModificationHistory
        {
            get { throw new NotImplementedException(); }
        }
        public string RSSFeed
        {
            get { throw new NotImplementedException(); }
        }
  public IIntegrationRepository IntegrationRepository
  {
   get { throw new NotImplementedException(); }
  }
  public string QueueName
  {
   get { throw new NotImplementedException(); }
   set { throw new NotImplementedException(); }
  }
  public int QueuePriority
  {
   get { throw new NotImplementedException(); }
   set { throw new NotImplementedException(); }
  }
  public void Initialize()
  {
   throw new NotImplementedException();
  }
  public ProjectStatus CreateProjectStatus(IProjectIntegrator integrator)
  {
   throw new NotImplementedException();
  }
        public ProjectActivity CurrentActivity
        {
            get { throw new NotImplementedException(); }
        }
  public void AddMessage(Message message)
  {
   throw new NotImplementedException();
  }
  public int MinimumSleepTimeMillis
  {
   get { return 0; }
  }
  public string WebURL
  {
   get { return ""; }
  }
        public IProjectAuthorisation Security
        {
            get { return null; }
        }
        public int MaxSourceControlRetries
        {
            get { throw new NotImplementedException(); }
        }
        [ReflectorProperty("initialState", Required = false)]
        public ProjectInitialState InitialState
        {
            get { return initialState; }
            set { initialState = value; }
        }
        [ReflectorProperty("startupMode", Required = false)]
        public ProjectStartupMode StartupMode
        {
            get { return startupMode; }
            set { startupMode = value; }
        }
        public bool stopProjectOnReachingMaxSourceControlRetries
        {
            get { throw new NotImplementedException(); }
        }
        public ThoughtWorks.CruiseControl.Core.Sourcecontrol.Common.SourceControlErrorHandlingPolicy SourceControlErrorHandling
        {
            get { throw new NotImplementedException(); }
        }
        public virtual List<PackageDetails> RetrievePackageList()
        {
            List<PackageDetails> packages = new List<PackageDetails>();
            return packages;
        }
        public virtual List<PackageDetails> RetrievePackageList(string buildLabel)
        {
            List<PackageDetails> packages = new List<PackageDetails>();
            return packages;
        }
    }
}
