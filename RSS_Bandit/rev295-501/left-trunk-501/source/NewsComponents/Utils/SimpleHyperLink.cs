namespace NewsComponents.Utils
{
 public class SimpleHyperLink
 {
  private string _navigateUrl;
  private string _text;
  private string _imageUrl;
  public SimpleHyperLink(string navigateUrl):
   this(navigateUrl, null, null) {}
  public SimpleHyperLink(string navigateUrl, string text):
   this(navigateUrl, text, null) {}
  public SimpleHyperLink(string navigateUrl, string text, string imageUrl)
  {
   _navigateUrl = navigateUrl;
   _text = text;
   _imageUrl = imageUrl;
  }
  public string ImageUrl {
   get { return _imageUrl; }
   set { _imageUrl = value; }
  }
  public string Text {
   get { return _text; }
   set { _text = value; }
  }
  public string NavigateUrl {
   get { return _navigateUrl; }
   set { _navigateUrl = value; }
  }
  public override string ToString() {
   return NavigateUrl;
  }
 }
}
