using ThoughtWorks.CruiseControl.Core.Reporting.Dashboard.Navigation;
namespace ThoughtWorks.CruiseControl.Core.Reporting.Dashboard.Navigation
{
 public class DefaultUrlBuilder : IUrlBuilder
 {
  private string extension;
  public static readonly string DEFAULT_EXTENSION = "aspx";
  public DefaultUrlBuilder()
  {
   extension = DEFAULT_EXTENSION;
  }
  public string Extension
  {
   set { this.extension = value; }
   get { return extension; }
  }
  public string BuildUrl(string action)
  {
   return BuildUrl(action, null);
  }
  public string BuildUrl(string action, string queryString)
  {
   return BuildUrl(action, queryString, null);
  }
  public string BuildUrl(string action, string queryString, string path)
  {
   string url = string.Format("{0}{1}.{2}", CalculatePath(path), action, extension);
   if (queryString!= null && queryString != string.Empty)
   {
    url += string.Format("?{0}", queryString);
   }
   return url;
  }
  private string CalculatePath(string path)
  {
   if (path == null || path.Trim() == string.Empty)
   {
    return "";
   }
   else
   {
    return (path.EndsWith("/") ? path : path + "/");
   }
  }
 }
}
