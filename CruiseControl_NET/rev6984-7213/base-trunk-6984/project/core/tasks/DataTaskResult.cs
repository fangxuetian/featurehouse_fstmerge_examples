namespace ThoughtWorks.CruiseControl.Core.Tasks
{
 public class DataTaskResult : ITaskResult
 {
  private readonly string data;
  public DataTaskResult(string data)
  {
   this.data = data;
  }
  public string Data
  {
   get { return data; }
  }
        public bool CheckIfSuccess()
        {
            return true;
        }
    }
}
