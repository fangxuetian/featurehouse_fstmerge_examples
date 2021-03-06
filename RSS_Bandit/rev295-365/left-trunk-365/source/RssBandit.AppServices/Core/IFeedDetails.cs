using System;
using System.Collections;
using System.Collections.Generic;
using System.Xml;
namespace NewsComponents
{
 public interface IFeedDetails: ICloneable{
  string Language{ get; }
  string Title{ get; }
  string Link{ get; }
  string Description{ get; }
        Dictionary<XmlQualifiedName, string> OptionalElements { get; }
  FeedType Type { get; }
        List<INewsItem> ItemsList { get; set; }
        string Id { get; set; }
        void WriteTo(XmlWriter writer);
 }
}
