using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace LayerSet
{
 public class Type_ExtendedInformation : Altova.Xml.Node
 {
  public Type_ExtendedInformation() : base() { SetCollectionParents(); }
  public Type_ExtendedInformation(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public Type_ExtendedInformation(XmlNode node) : base(node) { SetCollectionParents(); }
  public Type_ExtendedInformation(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "Abstract"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "Abstract", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ToolBarImage"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ToolBarImage", i);
    InternalAdjustPrefix(DOMNode, true);
   }
  }
  public int GetAbstract2MinCount()
  {
   return 0;
  }
  public int Abstract2MinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetAbstract2MaxCount()
  {
   return 1;
  }
  public int Abstract2MaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetAbstract2Count()
  {
   return DomChildCount(NodeType.Element, "", "Abstract");
  }
  public int Abstract2Count
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "Abstract");
   }
  }
  public bool HasAbstract2()
  {
   return HasDomChild(NodeType.Element, "", "Abstract");
  }
  public SchemaString GetAbstract2At(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "Abstract", index)));
  }
  public XmlNode GetStartingAbstract2Cursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "Abstract" );
  }
  public XmlNode GetAdvancedAbstract2Cursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "Abstract", curNode );
  }
  public SchemaString GetAbstract2ValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaString( curNode.InnerText );
  }
  public SchemaString GetAbstract2()
  {
   return GetAbstract2At(0);
  }
  public SchemaString Abstract2
  {
   get
   {
    return GetAbstract2At(0);
   }
  }
  public void RemoveAbstract2At(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "Abstract", index);
  }
  public void RemoveAbstract2()
  {
   while (HasAbstract2())
    RemoveAbstract2At(0);
  }
  public void AddAbstract2(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "", "Abstract", newValue.ToString());
  }
  public void InsertAbstract2At(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "Abstract", index, newValue.ToString());
  }
  public void ReplaceAbstract2At(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "Abstract", index, newValue.ToString());
  }
        public Abstract2Collection MyAbstract2s = new Abstract2Collection( );
        public class Abstract2Collection: IEnumerable
        {
            Type_ExtendedInformation parent;
            public Type_ExtendedInformation Parent
   {
    set
    {
     parent = value;
    }
   }
   public Abstract2Enumerator GetEnumerator()
   {
    return new Abstract2Enumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class Abstract2Enumerator: IEnumerator
        {
   int nIndex;
   Type_ExtendedInformation parent;
   public Abstract2Enumerator(Type_ExtendedInformation par)
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
    return(nIndex < parent.Abstract2Count );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetAbstract2At(nIndex));
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
  public int GetToolBarImageMinCount()
  {
   return 0;
  }
  public int ToolBarImageMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetToolBarImageMaxCount()
  {
   return 1;
  }
  public int ToolBarImageMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetToolBarImageCount()
  {
   return DomChildCount(NodeType.Element, "", "ToolBarImage");
  }
  public int ToolBarImageCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ToolBarImage");
   }
  }
  public bool HasToolBarImage()
  {
   return HasDomChild(NodeType.Element, "", "ToolBarImage");
  }
  public SchemaString GetToolBarImageAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "ToolBarImage", index)));
  }
  public XmlNode GetStartingToolBarImageCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "ToolBarImage" );
  }
  public XmlNode GetAdvancedToolBarImageCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "ToolBarImage", curNode );
  }
  public SchemaString GetToolBarImageValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaString( curNode.InnerText );
  }
  public SchemaString GetToolBarImage()
  {
   return GetToolBarImageAt(0);
  }
  public SchemaString ToolBarImage
  {
   get
   {
    return GetToolBarImageAt(0);
   }
  }
  public void RemoveToolBarImageAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ToolBarImage", index);
  }
  public void RemoveToolBarImage()
  {
   while (HasToolBarImage())
    RemoveToolBarImageAt(0);
  }
  public void AddToolBarImage(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "", "ToolBarImage", newValue.ToString());
  }
  public void InsertToolBarImageAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "ToolBarImage", index, newValue.ToString());
  }
  public void ReplaceToolBarImageAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "ToolBarImage", index, newValue.ToString());
  }
        public ToolBarImageCollection MyToolBarImages = new ToolBarImageCollection( );
        public class ToolBarImageCollection: IEnumerable
        {
            Type_ExtendedInformation parent;
            public Type_ExtendedInformation Parent
   {
    set
    {
     parent = value;
    }
   }
   public ToolBarImageEnumerator GetEnumerator()
   {
    return new ToolBarImageEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ToolBarImageEnumerator: IEnumerator
        {
   int nIndex;
   Type_ExtendedInformation parent;
   public ToolBarImageEnumerator(Type_ExtendedInformation par)
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
    return(nIndex < parent.ToolBarImageCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetToolBarImageAt(nIndex));
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
            MyAbstract2s.Parent = this;
            MyToolBarImages.Parent = this;
 }
}
}
