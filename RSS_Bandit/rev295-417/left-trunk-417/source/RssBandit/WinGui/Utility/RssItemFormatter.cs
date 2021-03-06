using System;
using System.Threading;
using System.Xml;
using System.Xml.XPath;
using System.Xml.Xsl;
using System.IO;
using System.Windows.Forms;
using System.Collections.Specialized;
using NewsComponents;
using NewsComponents.Feed;
using RssBandit.Exceptions;
using RssBandit.Resources;
namespace RssBandit.WinGui.Utility
{
 public class NewsItemFormatter {
  public event EventHandler<FeedExceptionEventArgs> TransformError;
  public event EventHandler<ExceptionEventArgs> StylesheetError;
  public event EventHandler<ExceptionEventArgs> StylesheetValidationError;
  static private string _defaultTmpl = null;
  static private string _searchTmpl = null;
  public const string SearchTemplateId = ":*<>?";
  private ListDictionary stylesheetTable = new ListDictionary();
  static NewsItemFormatter() {
   using (Stream xsltStream = Resource.GetStream("Resources.DefaultTemplate.xslt")) {
    _defaultTmpl = new StreamReader(xsltStream).ReadToEnd();
   }
   using (Stream xsltStream = Resource.GetStream("Resources.SearchResultsTemplate.xslt")) {
    _searchTmpl = new StreamReader(xsltStream).ReadToEnd();
   }
  }
  public NewsItemFormatter():this(String.Empty, _defaultTmpl) {
   this.AddXslStyleSheet(SearchTemplateId, _searchTmpl);
  }
  public NewsItemFormatter(string xslStyleSheetName, string xslStyleSheet) {
   this.AddXslStyleSheet(xslStyleSheetName, xslStyleSheet);
  }
  public bool ContainsXslStyleSheet(string name){
   return this.stylesheetTable.Contains(name);
  }
  public void AddXslStyleSheet(string name, string stylesheet){
            XslCompiledTransform transform = new XslCompiledTransform();
   try {
    if(name == null){
     name = String.Empty;
    }
    if (stylesheet == null || stylesheet.Length == 0) {
     stylesheet = DefaultNewsItemTemplate;
    }
    stylesheet = stylesheet.Replace("$IMAGEDIR$", Path.Combine(Application.StartupPath,@"templates\images\")); 
    transform.Load(new XmlTextReader(new StringReader(stylesheet)));
    if(this.stylesheetTable.Contains(name)){
     this.stylesheetTable.Remove(name);
    }
    this.stylesheetTable.Add(name, transform);
   }catch (XsltCompileException e){
    stylesheet = DefaultNewsItemTemplate;
    this.OnStylesheetError(this, new ExceptionEventArgs(e, SR.ExceptionNewsItemFormatterStylesheetCompile));
    if(!(e.InnerException is ThreadAbortException)){
     RssBanditApplication.PublishException(new BanditApplicationException("Error in AddXslStyleSheet()", e));
    }
   }catch (XmlException e){
    stylesheet = DefaultNewsItemTemplate;
    this.OnStylesheetError(this, new ExceptionEventArgs(e, SR.ExceptionNewsItemFormatterStylesheetCompile));
    if(!(e.InnerException is ThreadAbortException)){
     RssBanditApplication.PublishException(new BanditApplicationException("Error in AddXslStyleSheet()", e));
    }
   }
  }
  public virtual string ToHtml(string stylesheet, object transformTarget, XsltArgumentList xslArgs) {
   NewsItemSerializationFormat format = NewsItemSerializationFormat.NewsPaper;
   string link = String.Empty, content = String.Empty;
   if (transformTarget == null)
    return "<html><head><title>empty</title></head><body></body></html>";
   StringWriter swr = new StringWriter();
   try {
    XPathDocument doc = null;
    if(transformTarget is INewsItem){
     INewsItem item = (INewsItem) transformTarget;
     link = item.FeedLink;
     content = item.Content;
     doc = new XPathDocument(new XmlTextReader(new StringReader(item.ToString(format, false))), XmlSpace.Preserve);
                }else if (transformTarget is IFeedDetails){
     IFeedDetails feed = (IFeedDetails) transformTarget;
     link = feed.Link;
     doc = new XPathDocument(new XmlTextReader(new StringReader(feed.ToString(format, false))), XmlSpace.Preserve);
    }else if(transformTarget is FeedInfoList){
     FeedInfoList feeds = (FeedInfoList) transformTarget;
     doc = new XPathDocument(new XmlTextReader(new StringReader(feeds.ToString())), XmlSpace.Preserve);
    }else{
     throw new ArgumentException("transformTarget");
    }
    XslCompiledTransform transform = null;
    if(this.stylesheetTable.Contains(stylesheet)){
                    transform = (XslCompiledTransform)this.stylesheetTable[stylesheet];
    }else{
                    transform = (XslCompiledTransform)this.stylesheetTable[String.Empty];
    }
    xslArgs.AddExtensionObject("urn:localization-extension", new LocalizerExtensionObject());
    transform.Transform(doc, xslArgs, swr);
   } catch (ThreadAbortException) {
   } catch (Exception e) {
    this.OnTransformationError(this, new FeedExceptionEventArgs(e, link, SR.ExceptionNewsItemTransformation));
    return content;
   }
   return swr.ToString();
  }
  public static string DefaultNewsItemTemplate {
   get { return _defaultTmpl; }
  }
  protected void OnTransformationError(object sender, FeedExceptionEventArgs e) {
   if (TransformError != null)
    foreach (EventHandler<FeedExceptionEventArgs> eh in TransformError.GetInvocationList())
     eh.BeginInvoke(sender, e, null, null);
  }
  protected void OnStylesheetError(object sender, ExceptionEventArgs e) {
   if (StylesheetError != null)
    foreach (EventHandler<ExceptionEventArgs> eh in StylesheetError.GetInvocationList())
     eh.BeginInvoke(this, e, null, null);
  }
  protected void OnStylesheetValidationError(object sender, ExceptionEventArgs e) {
   if (StylesheetValidationError != null)
    foreach (EventHandler<ExceptionEventArgs> eh in StylesheetValidationError.GetInvocationList())
     eh.BeginInvoke(this, e, null, null);
  }
  public class LocalizerExtensionObject
  {
   public string RelatedLinksText(){
    return SR.XsltDefaultTemplate_RelatedLinks;
   }
   public string PreviousPageText(){
    return SR.XsltDefaultTemplate_Previous;
   }
   public string NextPageText(){
    return SR.XsltDefaultTemplate_Next;
   }
   public string DisplayingPageText(){
    return SR.XsltDefaultTemplate_Displaying_page;
   }
   public string PageOfText(){
    return SR.XsltDefaultTemplate_of;
   }
   public string ItemPublisherText() {
    return SR.XsltDefaultTemplate_ItemPublisher;
   }
   public string ItemAuthorText() {
    return SR.XsltDefaultTemplate_ItemAuthor;
   }
   public string ItemDateText() {
    return SR.XsltDefaultTemplate_ItemDate;
   }
   public string ItemEnclosureText() {
    return SR.XsltDefaultTemplate_ItemEnclosure;
   }
   public string ToggleFlagStateText() {
    return SR.XsltDefaultTemplate_ToggleFlagState;
   }
   public string ToggleReadStateText() {
    return SR.XsltDefaultTemplate_ToggleReadState;
   }
   public string ToggleWatchStateText() {
    return SR.XsltDefaultTemplate_ToggleWatchState;
   }
  }
 }
}
