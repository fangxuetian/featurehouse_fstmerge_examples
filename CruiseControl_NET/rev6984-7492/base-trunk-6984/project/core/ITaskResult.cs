namespace ThoughtWorks.CruiseControl.Core
{
    public interface ITaskResult
    {
        string Data { get; }
        bool CheckIfSuccess();
    }
}
