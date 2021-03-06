using System; 
using System.Collections; 
using System.Xml.XPath; namespace  NewsComponents {
	
 public interface  INewsItem :  ICloneable, IXPathNavigable {
		
  string FeedLink { get; } 
  string Link { get; } 
  DateTime Date { get; } 
  string Id { get; } 
  string ParentId { get; } 
  string Content { get; } 
  bool HasContent { get; } 
  void SetContent(string newContent, ContentType contentType) ; 
  ContentType ContentType { get; set; } 
  bool BeenRead { get; set; } 
  IFeedDetails FeedDetails { get; set; } 
  string Author { get; set; } 
  string Title { get; } 
  string Subject { get; } 
  int CommentCount { get; set; } 
  string CommentUrl { get; } 
  string CommentRssUrl { get; } 
  Hashtable OptionalElements { get; set; } 
  String ToString(); 
  int GetHashCode(); 
  bool Equals(object obj);
	}

}
