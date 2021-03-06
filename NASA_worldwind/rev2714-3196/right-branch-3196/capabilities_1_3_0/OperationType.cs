using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_3_0.wms
{
 public class OperationType : Altova.Xml.Node
 {
  public OperationType() : base() { SetCollectionParents(); }
  public OperationType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public OperationType(XmlNode node) : base(node) { SetCollectionParents(); }
  public OperationType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Format"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Format", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "DCPType"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "DCPType", i);
    InternalAdjustPrefix(DOMNode, true);
    new DCPTypeType(DOMNode).AdjustPrefix();
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
   return Int32.MaxValue;
  }
  public int FormatMaxCount
  {
   get
   {
    return Int32.MaxValue;
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
            OperationType parent;
            public OperationType Parent
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
   OperationType parent;
   public FormatEnumerator(OperationType par)
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
  public int GetDCPTypeMinCount()
  {
   return 1;
  }
  public int DCPTypeMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetDCPTypeMaxCount()
  {
   return Int32.MaxValue;
  }
  public int DCPTypeMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetDCPTypeCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "DCPType");
  }
  public int DCPTypeCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "DCPType");
   }
  }
  public bool HasDCPType()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "DCPType");
  }
  public DCPTypeType GetDCPTypeAt(int index)
  {
   return new DCPTypeType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "DCPType", index));
  }
  public DCPTypeType GetDCPType()
  {
   return GetDCPTypeAt(0);
  }
  public DCPTypeType DCPType
  {
   get
   {
    return GetDCPTypeAt(0);
   }
  }
  public void RemoveDCPTypeAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "DCPType", index);
  }
  public void RemoveDCPType()
  {
   while (HasDCPType())
    RemoveDCPTypeAt(0);
  }
  public void AddDCPType(DCPTypeType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "DCPType", newValue);
  }
  public void InsertDCPTypeAt(DCPTypeType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "DCPType", index, newValue);
  }
  public void ReplaceDCPTypeAt(DCPTypeType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "DCPType", index, newValue);
  }
        public DCPTypeCollection MyDCPTypes = new DCPTypeCollection( );
        public class DCPTypeCollection: IEnumerable
        {
            OperationType parent;
            public OperationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public DCPTypeEnumerator GetEnumerator()
   {
    return new DCPTypeEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class DCPTypeEnumerator: IEnumerator
        {
   int nIndex;
   OperationType parent;
   public DCPTypeEnumerator(OperationType par)
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
    return(nIndex < parent.DCPTypeCount );
   }
   public DCPTypeType Current
   {
    get
    {
     return(parent.GetDCPTypeAt(nIndex));
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
            MyFormats.Parent = this;
            MyDCPTypes.Parent = this;
 }
}
}
