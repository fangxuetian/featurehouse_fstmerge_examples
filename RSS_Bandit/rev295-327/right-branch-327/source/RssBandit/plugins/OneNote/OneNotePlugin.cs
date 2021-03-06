using System;
using System.Diagnostics;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using System.Xml.XPath;
using Syndication.Extensibility;
using Microsoft.Office.OneNote;
namespace BlogExtension.OneNote
{
 public class OneNotePlugin: IBlogExtension
 {
  string configFile;
  OneNotePluginConfig configInfo;
  XmlSerializer serializer;
  public OneNotePlugin()
  {
   string assemblyUri = this.GetType().Assembly.CodeBase;
   string assemblyPath = new Uri(assemblyUri).LocalPath;
   string assemblyDir = Path.GetDirectoryName(assemblyPath);
   configFile = Path.Combine(assemblyDir, "OneNotePlugin.config.xml");
   serializer = new XmlSerializer(typeof(OneNotePluginConfig));
  }
  public void BlogItem(IXPathNavigable rssFragment, bool edited) {
   LoadConfig();
   DoExportItem(rssFragment.CreateNavigator());
  }
  public bool HasEditingGUI {
   get {
    return false;
   }
  }
  public string DisplayName {
   get {
    return Resource.Manager["RES_MenuSendToOneNoteCaption"];
   }
  }
  public void Configure(System.Windows.Forms.IWin32Window parent) {
   LoadConfig();
  }
  public bool HasConfiguration {
   get {
    return false;
   }
  }
  private void DoExportItem(XPathNavigator nav) {
   string title = StripAndDecode(nav.Evaluate("string(//item/title/text())").ToString());
   Page p = new Page(configInfo.Page, title);
   p.Date = DateTime.Parse(nav.Evaluate("string(//item/pubDate/text())").ToString());
   OutlineObject outline = new OutlineObject();
            if (configInfo.ItemLinkBelowContent)
            {
                outline.AddContent(new HtmlContent(String.Format(configInfo.ItemContentTemplate, nav.Evaluate("string(//item/description/text())").ToString())));
                outline.AddContent(new HtmlContent(String.Format(configInfo.ItemLinkTemplate, nav.Evaluate("string(//item/link/text())").ToString())));
            }
            else
            {
                outline.AddContent(new HtmlContent(String.Format(configInfo.ItemLinkTemplate, nav.Evaluate("string(//item/link/text())").ToString())));
                outline.AddContent(new HtmlContent(String.Format(configInfo.ItemContentTemplate, nav.Evaluate("string(//item/description/text())").ToString())));
            }
   p.AddObject(outline);
   p.Commit();
   p.NavigateTo();
  }
  private string StripAndDecode(string s) {
   string t = HtmlHelper.StripAnyTags(s);
   if (t.IndexOf("&") >= 0 && t.IndexOf(";") >= 0) {
    t = HtmlHelper.HtmlDecode(t);
   }
   return t;
  }
  private void LoadConfig()
        {
            if (File.Exists(configFile))
            {
                try
                {
                    using (XmlTextReader reader = new XmlTextReader(configFile))
                    {
                        configInfo = (OneNotePluginConfig)serializer.Deserialize(reader);
                    }
                } catch (Exception loadEx ) {
                    string ex = loadEx.Message;
                    if (loadEx.InnerException != null)
                        ex += " " + loadEx.InnerException.Message;
                    string msg = Resource.Manager.FormatMessage("RES_ExceptionOneNoteBadConfigurationFileFormat", configFile, ex);
                    if (msg == null)
                        msg = String.Format("There was an error loading configuration from '{0}': {1}", configFile, ex);
                    throw new BlogExtensionException(msg, loadEx);
                }
            }
            else
            {
                try
                {
                    configInfo = new OneNotePluginConfig();
                    using (Stream f = new FileStream(configFile, FileMode.Create, FileAccess.Write, FileShare.None, 4*1024)) {
         serializer.Serialize(f, configInfo);
        }
                } catch (Exception saveEx ) {
                    string ex = saveEx.Message;
                    if (saveEx.InnerException != null)
                        ex += " " + saveEx.InnerException.Message;
                    string msg = Resource.Manager.FormatMessage("RES_ExceptionOneNoteWritingConfigurationFile", configFile, ex);
                    if (msg == null)
                        msg = String.Format("There was an error writing configuration file '{0}': {1}", configFile, ex);
                    throw new BlogExtensionException(msg, saveEx);
                }
            }
  }
 }
 [System.Xml.Serialization.XmlRootAttribute("onenote-plugin", Namespace="", IsNullable=false)]
 public class OneNotePluginConfig {
  public OneNotePluginConfig() {
   this.Page = "General.one";
   this.ItemLinkTemplate = "Original Web Location: <a href=\"{0}\">click here</a>";
   this.ItemContentTemplate = "{0}";
            ItemLinkBelowContent = false;
  }
  [System.Xml.Serialization.XmlElementAttribute("target-note-page")]
  public string Page;
  [System.Xml.Serialization.XmlElementAttribute("item-link-template")]
  public string ItemLinkTemplate;
  [System.Xml.Serialization.XmlElementAttribute("item-content-template")]
  public string ItemContentTemplate;
        [System.Xml.Serialization.XmlElementAttribute("item-link-below-content")]
        public bool ItemLinkBelowContent;
 }
}
