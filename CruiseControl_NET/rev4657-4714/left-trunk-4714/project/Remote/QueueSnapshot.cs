using System;
namespace ThoughtWorks.CruiseControl.Remote
{
 [Serializable]
 public class QueueSnapshot
 {
  private string queueName;
  private QueuedRequestSnapshotList _requests;
  public QueueSnapshot(string queueName)
  {
   this.queueName = queueName;
   _requests = new QueuedRequestSnapshotList();
  }
  public string QueueName
  {
   get { return queueName; }
  }
  public QueuedRequestSnapshotList Requests
  {
   get { return _requests; }
  }
        public bool IsEmpty
        {
            get { return _requests.Count == 0; }
        }
 }
}
