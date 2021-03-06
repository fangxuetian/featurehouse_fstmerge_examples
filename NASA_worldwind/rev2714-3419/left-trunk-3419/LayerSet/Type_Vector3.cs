using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace LayerSet
{
 public class Type_Vector3 : Altova.Xml.Node
 {
  public Type_Vector3() : base() { SetCollectionParents(); }
  public Type_Vector3(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public Type_Vector3(XmlNode node) : base(node) { SetCollectionParents(); }
  public Type_Vector3(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "x"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "x", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "y"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "y", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "z"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "z", i);
    InternalAdjustPrefix(DOMNode, true);
   }
  }
  public int GetxMinCount()
  {
   return 1;
  }
  public int xMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetxMaxCount()
  {
   return 1;
  }
  public int xMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetxCount()
  {
   return DomChildCount(NodeType.Element, "", "x");
  }
  public int xCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "x");
   }
  }
  public bool Hasx()
  {
   return HasDomChild(NodeType.Element, "", "x");
  }
  public SchemaDecimal GetxAt(int index)
  {
   return new SchemaDecimal(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "x", index)));
  }
  public XmlNode GetStartingxCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "x" );
  }
  public XmlNode GetAdvancedxCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "x", curNode );
  }
  public SchemaDecimal GetxValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaDecimal( curNode.InnerText );
  }
  public SchemaDecimal Getx()
  {
   return GetxAt(0);
  }
  public SchemaDecimal x
  {
   get
   {
    return GetxAt(0);
   }
  }
  public void RemovexAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "x", index);
  }
  public void Removex()
  {
   while (Hasx())
    RemovexAt(0);
  }
  public void Addx(SchemaDecimal newValue)
  {
   AppendDomChild(NodeType.Element, "", "x", newValue.ToString());
  }
  public void InsertxAt(SchemaDecimal newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "x", index, newValue.ToString());
  }
  public void ReplacexAt(SchemaDecimal newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "x", index, newValue.ToString());
  }
        public xCollection Myxs = new xCollection( );
        public class xCollection: IEnumerable
        {
            Type_Vector3 parent;
            public Type_Vector3 Parent
   {
    set
    {
     parent = value;
    }
   }
   public xEnumerator GetEnumerator()
   {
    return new xEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class xEnumerator: IEnumerator
        {
   int nIndex;
   Type_Vector3 parent;
   public xEnumerator(Type_Vector3 par)
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
    return(nIndex < parent.xCount );
   }
   public SchemaDecimal Current
   {
    get
    {
     return(parent.GetxAt(nIndex));
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
  public int GetyMinCount()
  {
   return 1;
  }
  public int yMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetyMaxCount()
  {
   return 1;
  }
  public int yMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetyCount()
  {
   return DomChildCount(NodeType.Element, "", "y");
  }
  public int yCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "y");
   }
  }
  public bool Hasy()
  {
   return HasDomChild(NodeType.Element, "", "y");
  }
  public SchemaDecimal GetyAt(int index)
  {
   return new SchemaDecimal(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "y", index)));
  }
  public XmlNode GetStartingyCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "y" );
  }
  public XmlNode GetAdvancedyCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "y", curNode );
  }
  public SchemaDecimal GetyValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaDecimal( curNode.InnerText );
  }
  public SchemaDecimal Gety()
  {
   return GetyAt(0);
  }
  public SchemaDecimal y
  {
   get
   {
    return GetyAt(0);
   }
  }
  public void RemoveyAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "y", index);
  }
  public void Removey()
  {
   while (Hasy())
    RemoveyAt(0);
  }
  public void Addy(SchemaDecimal newValue)
  {
   AppendDomChild(NodeType.Element, "", "y", newValue.ToString());
  }
  public void InsertyAt(SchemaDecimal newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "y", index, newValue.ToString());
  }
  public void ReplaceyAt(SchemaDecimal newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "y", index, newValue.ToString());
  }
        public yCollection Myys = new yCollection( );
        public class yCollection: IEnumerable
        {
            Type_Vector3 parent;
            public Type_Vector3 Parent
   {
    set
    {
     parent = value;
    }
   }
   public yEnumerator GetEnumerator()
   {
    return new yEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class yEnumerator: IEnumerator
        {
   int nIndex;
   Type_Vector3 parent;
   public yEnumerator(Type_Vector3 par)
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
    return(nIndex < parent.yCount );
   }
   public SchemaDecimal Current
   {
    get
    {
     return(parent.GetyAt(nIndex));
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
  public int GetzMinCount()
  {
   return 1;
  }
  public int zMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetzMaxCount()
  {
   return 1;
  }
  public int zMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetzCount()
  {
   return DomChildCount(NodeType.Element, "", "z");
  }
  public int zCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "z");
   }
  }
  public bool Hasz()
  {
   return HasDomChild(NodeType.Element, "", "z");
  }
  public SchemaDecimal GetzAt(int index)
  {
   return new SchemaDecimal(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "z", index)));
  }
  public XmlNode GetStartingzCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "z" );
  }
  public XmlNode GetAdvancedzCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "z", curNode );
  }
  public SchemaDecimal GetzValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaDecimal( curNode.InnerText );
  }
  public SchemaDecimal Getz()
  {
   return GetzAt(0);
  }
  public SchemaDecimal z
  {
   get
   {
    return GetzAt(0);
   }
  }
  public void RemovezAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "z", index);
  }
  public void Removez()
  {
   while (Hasz())
    RemovezAt(0);
  }
  public void Addz(SchemaDecimal newValue)
  {
   AppendDomChild(NodeType.Element, "", "z", newValue.ToString());
  }
  public void InsertzAt(SchemaDecimal newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "z", index, newValue.ToString());
  }
  public void ReplacezAt(SchemaDecimal newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "z", index, newValue.ToString());
  }
        public zCollection Myzs = new zCollection( );
        public class zCollection: IEnumerable
        {
            Type_Vector3 parent;
            public Type_Vector3 Parent
   {
    set
    {
     parent = value;
    }
   }
   public zEnumerator GetEnumerator()
   {
    return new zEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class zEnumerator: IEnumerator
        {
   int nIndex;
   Type_Vector3 parent;
   public zEnumerator(Type_Vector3 par)
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
    return(nIndex < parent.zCount );
   }
   public SchemaDecimal Current
   {
    get
    {
     return(parent.GetzAt(nIndex));
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
            Myxs.Parent = this;
            Myys.Parent = this;
            Myzs.Parent = this;
 }
}
}
