using System;
namespace ThoughtWorks.CruiseControl.Remote
{
    [Serializable]
    public class CruiseServerSnapshot
    {
        private readonly ProjectStatus[] projectStatuses;
        private readonly QueueSetSnapshot queueSetSnapshot;
        public CruiseServerSnapshot()
        {
            projectStatuses = new ProjectStatus[0];
            queueSetSnapshot = new QueueSetSnapshot();
        }
        public CruiseServerSnapshot(ProjectStatus[] projectStatuses, QueueSetSnapshot queueSetSnapshot)
        {
            this.projectStatuses = projectStatuses;
            this.queueSetSnapshot = queueSetSnapshot;
        }
        public ProjectStatus[] ProjectStatuses
        {
            get { return projectStatuses; }
        }
        public QueueSetSnapshot QueueSetSnapshot
        {
            get { return queueSetSnapshot; }
        }
        public bool IsQueueSetSnapshotChanged(QueueSetSnapshot queueSetSnapshotToCompare)
        {
            if (queueSetSnapshotToCompare == null)
            {
                if (queueSetSnapshot == null)
                    return false;
                else
                    return true;
            }
            if (queueSetSnapshot == null)
                return true;
            if (queueSetSnapshotToCompare.Queues.Count != queueSetSnapshot.Queues.Count)
                return true;
            for (int queueIndex = 0; queueIndex < queueSetSnapshot.Queues.Count; queueIndex++)
            {
                QueueSnapshot queueSnapshot = queueSetSnapshot.Queues[queueIndex];
                QueueSnapshot queueSnapshotToCompare = queueSetSnapshotToCompare.Queues[queueIndex];
                if (queueSnapshotToCompare.QueueName != queueSnapshot.QueueName)
                    return true;
                if (queueSnapshotToCompare.Requests.Count != queueSnapshot.Requests.Count)
                    return true;
                for (int requestIndex = 0; requestIndex < queueSnapshot.Requests.Count; requestIndex++)
                {
                    QueuedRequestSnapshot request = queueSnapshot.Requests[requestIndex];
                    QueuedRequestSnapshot requestToCompare = queueSnapshotToCompare.Requests[requestIndex];
                    if (requestToCompare.ProjectName != request.ProjectName)
                        return true;
                    if (requestToCompare.Activity != request.Activity)
                        return true;
                }
            }
            return false;
        }
  public ProjectStatus GetProjectStatus(string projectName)
  {
   foreach (ProjectStatus projectStatus in ProjectStatuses)
   {
    if (projectStatus.Name == projectName)
    {
     return projectStatus;
    }
   }
   return null;
  }
    }
}
