using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_3_0.wms
{
 public class LegendURLType : Altova.Xml.Node
 {
  public LegendURLType() : base() { SetCollectionParents(); }
  public LegendURLType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public LegendURLType(XmlNode node) : base(node) { SetCollectionParents(); }
  public LegendURLType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "width"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "width", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "height"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "height", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Format"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Format", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource", i);
    InternalAdjustPrefix(DOMNode, true);
    new OnlineResourceType(DOMNode).AdjustPrefix();
   }
  }
  public int GetwidthMinCount()
  {
   return 0;
  }
  public int widthMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetwidthMaxCount()
  {
   return 1;
  }
  public int widthMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetwidthCount()
  {
   return DomChildCount(NodeType.Attribute, "", "width");
  }
  public int widthCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "width");
   }
  }
  public bool Haswidth()
  {
   return HasDomChild(NodeType.Attribute, "", "width");
  }
  public SchemaLong GetwidthAt(int index)
  {
   return new SchemaLong(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "width", index)));
  }
  public SchemaLong Getwidth()
  {
   return GetwidthAt(0);
  }
  public SchemaLong width
  {
   get
   {
    return GetwidthAt(0);
   }
  }
  public void RemovewidthAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "width", index);
  }
  public void Removewidth()
  {
   while (Haswidth())
    RemovewidthAt(0);
  }
  public void Addwidth(SchemaLong newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "width", newValue.ToString());
  }
  public void InsertwidthAt(SchemaLong newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "width", index, newValue.ToString());
  }
  public void ReplacewidthAt(SchemaLong newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "width", index, newValue.ToString());
  }
        public widthCollection Mywidths = new widthCollection( );
        public class widthCollection: IEnumerable
        {
            LegendURLType parent;
            public LegendURLType Parent
   {
    set
    {
     parent = value;
    }
   }
   public widthEnumerator GetEnumerator()
   {
    return new widthEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class widthEnumerator: IEnumerator
        {
   int nIndex;
   LegendURLType parent;
   public widthEnumerator(LegendURLType par)
   {
    parent = par;
    nIndex = -1;
   }
   public void Reset()
   {
    nIndex = -1;
   }
   public bool MoveNext()
   {
    nIndex++;
    return(nIndex < parent.widthCount );
   }
   public SchemaLong Current
   {
    get
    {
     return(parent.GetwidthAt(nIndex));
    }
   }
   object IEnumerator.Current
   {
    get
    {
     return(Current);
    }
   }
     }
  public int GetheightMinCount()
  {
   return 0;
  }
  public int heightMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetheightMaxCount()
  {
   return 1;
  }
  public int heightMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetheightCount()
  {
   return DomChildCount(NodeType.Attribute, "", "height");
  }
  public int heightCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "height");
   }
  }
  public bool Hasheight()
  {
   return HasDomChild(NodeType.Attribute, "", "height");
  }
  public SchemaLong GetheightAt(int index)
  {
   return new SchemaLong(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "height", index)));
  }
  public SchemaLong Getheight()
  {
   return GetheightAt(0);
  }
  public SchemaLong height
  {
   get
   {
    return GetheightAt(0);
   }
  }
  public void RemoveheightAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "height", index);
  }
  public void Removeheight()
  {
   while (Hasheight())
    RemoveheightAt(0);
  }
  public void Addheight(SchemaLong newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "height", newValue.ToString());
  }
  public void InsertheightAt(SchemaLong newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "height", index, newValue.ToString());
  }
  public void ReplaceheightAt(SchemaLong newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "height", index, newValue.ToString());
  }
        public heightCollection Myheights = new heightCollection( );
        public class heightCollection: IEnumerable
        {
            LegendURLType parent;
            public LegendURLType Parent
   {
    set
    {
     parent = value;
    }
   }
   public heightEnumerator GetEnumerator()
   {
    return new heightEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class heightEnumerator: IEnumerator
        {
   int nIndex;
   LegendURLType parent;
   public heightEnumerator(LegendURLType par)
   {
    parent = par;
    nIndex = -1;
   }
   public void Reset()
   {
    nIndex = -1;
   }
   public bool MoveNext()
   {
    nIndex++;
    return(nIndex < parent.heightCount );
   }
   public SchemaLong Current
   {
    get
    {
     return(parent.GetheightAt(nIndex));
    }
   }
   object IEnumerator.Current
   {
    get
    {
     return(Current);
    }
   }
     }
  public int GetFormatMinCount()
  {
   return 1;
  }
  public int FormatMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetFormatMaxCount()
  {
   return 1;
  }
  public int FormatMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetFormatCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Format");
  }
  public int FormatCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Format");
   }
  }
  public bool HasFormat()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Format");
  }
  public SchemaString GetFormatAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Format", index)));
  }
  public SchemaString GetFormat()
  {
   return GetFormatAt(0);
  }
  public SchemaString Format
  {
   get
   {
    return GetFormatAt(0);
   }
  }
  public void RemoveFormatAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Format", index);
  }
  public void RemoveFormat()
  {
   while (HasFormat())
    RemoveFormatAt(0);
  }
  public void AddFormat(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "Format", newValue.ToString());
  }
  public void InsertFormatAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Format", index, newValue.ToString());
  }
  public void ReplaceFormatAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Format", index, newValue.ToString());
  }
        public FormatCollection MyFormats = new FormatCollection( );
        public class FormatCollection: IEnumerable
        {
            LegendURLType parent;
            public LegendURLType Parent
   {
    set
    {
     parent = value;
    }
   }
   public FormatEnumerator GetEnumerator()
   {
    return new FormatEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class FormatEnumerator: IEnumerator
        {
   int nIndex;
   LegendURLType parent;
   public FormatEnumerator(LegendURLType par)
   {
    parent = par;
    nIndex = -1;
   }
   public void Reset()
   {
    nIndex = -1;
   }
   public bool MoveNext()
   {
    nIndex++;
    return(nIndex < parent.FormatCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetFormatAt(nIndex));
    }
   }
   object IEnumerator.Current
   {
    get
    {
     return(Current);
    }
   }
     }
  public int GetOnlineResourceMinCount()
  {
   return 1;
  }
  public int OnlineResourceMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetOnlineResourceMaxCount()
  {
   return 1;
  }
  public int OnlineResourceMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetOnlineResourceCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource");
  }
  public int OnlineResourceCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource");
   }
  }
  public bool HasOnlineResource()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource");
  }
  public OnlineResourceType GetOnlineResourceAt(int index)
  {
   return new OnlineResourceType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource", index));
  }
  public OnlineResourceType GetOnlineResource()
  {
   return GetOnlineResourceAt(0);
  }
  public OnlineResourceType OnlineResource
  {
   get
   {
    return GetOnlineResourceAt(0);
   }
  }
  public void RemoveOnlineResourceAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "OnlineResource", index);
  }
  public void RemoveOnlineResource()
  {
   while (HasOnlineResource())
    RemoveOnlineResourceAt(0);
  }
  public void AddOnlineResource(OnlineResourceType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "OnlineResource", newValue);
  }
  public void InsertOnlineResourceAt(OnlineResourceType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "OnlineResource", index, newValue);
  }
  public void ReplaceOnlineResourceAt(OnlineResourceType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "OnlineResource", index, newValue);
  }
        public OnlineResourceCollection MyOnlineResources = new OnlineResourceCollection( );
        public class OnlineResourceCollection: IEnumerable
        {
            LegendURLType parent;
            public LegendURLType Parent
   {
    set
    {
     parent = value;
    }
   }
   public OnlineResourceEnumerator GetEnumerator()
   {
    return new OnlineResourceEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class OnlineResourceEnumerator: IEnumerator
        {
   int nIndex;
   LegendURLType parent;
   public OnlineResourceEnumerator(LegendURLType par)
   {
    parent = par;
    nIndex = -1;
   }
   public void Reset()
   {
    nIndex = -1;
   }
   public bool MoveNext()
   {
    nIndex++;
    return(nIndex < parent.OnlineResourceCount );
   }
   public OnlineResourceType Current
   {
    get
    {
     return(parent.GetOnlineResourceAt(nIndex));
    }
   }
   object IEnumerator.Current
   {
    get
    {
     return(Current);
    }
   }
     }
        private void SetCollectionParents()
        {
            Mywidths.Parent = this;
            Myheights.Parent = this;
            MyFormats.Parent = this;
            MyOnlineResources.Parent = this;
 }
}
}
