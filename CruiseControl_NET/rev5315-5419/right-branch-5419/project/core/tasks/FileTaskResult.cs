using System;
using System.IO;
namespace ThoughtWorks.CruiseControl.Core.Tasks
{
 public class FileTaskResult : ITaskResult
 {
  private readonly string data;
  public FileTaskResult(string filename) :
   this(new FileInfo(filename)) {}
  public FileTaskResult(FileInfo file)
  {
   data = ReadFileContents(file);
  }
  public string Data
  {
   get { return data; }
  }
  public bool Succeeded()
  {
   return true;
  }
  public bool Failed()
  {
   return false;
  }
  private static string ReadFileContents(FileInfo file)
  {
   try
   {
    using (StreamReader reader = file.OpenText())
    {
     return reader.ReadToEnd();
    }
   }
   catch (Exception ex)
   {
    throw new CruiseControlException("Unable to read the contents of the file: " + file.FullName, ex);
   }
  }
 }
}
