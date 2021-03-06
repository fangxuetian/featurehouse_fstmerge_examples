using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace WorldXmlDescriptor
{
 public class WorldType : Altova.Xml.Node
 {
  public WorldType() : base() { SetCollectionParents(); }
  public WorldType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public WorldType(XmlNode node) : base(node) { SetCollectionParents(); }
  public WorldType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "Name"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "Name", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "EquatorialRadius"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "EquatorialRadius", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "LayerDirectory"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "LayerDirectory", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "TerrainAccessor"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "TerrainAccessor", i);
    InternalAdjustPrefix(DOMNode, true);
    new TerrainAccessor(DOMNode).AdjustPrefix();
   }
  }
  public int GetNameMinCount()
  {
   return 1;
  }
  public int NameMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetNameMaxCount()
  {
   return 1;
  }
  public int NameMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetNameCount()
  {
   return DomChildCount(NodeType.Attribute, "", "Name");
  }
  public int NameCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "Name");
   }
  }
  public bool HasName()
  {
   return HasDomChild(NodeType.Attribute, "", "Name");
  }
  public SchemaString GetNameAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "Name", index)));
  }
  public SchemaString GetName()
  {
   return GetNameAt(0);
  }
  public SchemaString Name
  {
   get
   {
    return GetNameAt(0);
   }
  }
  public void RemoveNameAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "Name", index);
  }
  public void RemoveName()
  {
   while (HasName())
    RemoveNameAt(0);
  }
  public void AddName(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "Name", newValue.ToString());
  }
  public void InsertNameAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "Name", index, newValue.ToString());
  }
  public void ReplaceNameAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "Name", index, newValue.ToString());
  }
        public NameCollection MyNames = new NameCollection( );
        public class NameCollection: IEnumerable
        {
            WorldType parent;
            public WorldType Parent
   {
    set
    {
     parent = value;
    }
   }
   public NameEnumerator GetEnumerator()
   {
    return new NameEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class NameEnumerator: IEnumerator
        {
   int nIndex;
   WorldType parent;
   public NameEnumerator(WorldType par)
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
    return(nIndex < parent.NameCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetNameAt(nIndex));
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
  public int GetEquatorialRadiusMinCount()
  {
   return 1;
  }
  public int EquatorialRadiusMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetEquatorialRadiusMaxCount()
  {
   return 1;
  }
  public int EquatorialRadiusMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetEquatorialRadiusCount()
  {
   return DomChildCount(NodeType.Attribute, "", "EquatorialRadius");
  }
  public int EquatorialRadiusCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "EquatorialRadius");
   }
  }
  public bool HasEquatorialRadius()
  {
   return HasDomChild(NodeType.Attribute, "", "EquatorialRadius");
  }
  public SchemaDecimal GetEquatorialRadiusAt(int index)
  {
   return new SchemaDecimal(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "EquatorialRadius", index)));
  }
  public SchemaDecimal GetEquatorialRadius()
  {
   return GetEquatorialRadiusAt(0);
  }
  public SchemaDecimal EquatorialRadius
  {
   get
   {
    return GetEquatorialRadiusAt(0);
   }
  }
  public void RemoveEquatorialRadiusAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "EquatorialRadius", index);
  }
  public void RemoveEquatorialRadius()
  {
   while (HasEquatorialRadius())
    RemoveEquatorialRadiusAt(0);
  }
  public void AddEquatorialRadius(SchemaDecimal newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "EquatorialRadius", newValue.ToString());
  }
  public void InsertEquatorialRadiusAt(SchemaDecimal newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "EquatorialRadius", index, newValue.ToString());
  }
  public void ReplaceEquatorialRadiusAt(SchemaDecimal newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "EquatorialRadius", index, newValue.ToString());
  }
        public EquatorialRadiusCollection MyEquatorialRadiuss = new EquatorialRadiusCollection( );
        public class EquatorialRadiusCollection: IEnumerable
        {
            WorldType parent;
            public WorldType Parent
   {
    set
    {
     parent = value;
    }
   }
   public EquatorialRadiusEnumerator GetEnumerator()
   {
    return new EquatorialRadiusEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class EquatorialRadiusEnumerator: IEnumerator
        {
   int nIndex;
   WorldType parent;
   public EquatorialRadiusEnumerator(WorldType par)
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
    return(nIndex < parent.EquatorialRadiusCount );
   }
   public SchemaDecimal Current
   {
    get
    {
     return(parent.GetEquatorialRadiusAt(nIndex));
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
  public int GetLayerDirectoryMinCount()
  {
   return 1;
  }
  public int LayerDirectoryMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetLayerDirectoryMaxCount()
  {
   return 1;
  }
  public int LayerDirectoryMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetLayerDirectoryCount()
  {
   return DomChildCount(NodeType.Attribute, "", "LayerDirectory");
  }
  public int LayerDirectoryCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "LayerDirectory");
   }
  }
  public bool HasLayerDirectory()
  {
   return HasDomChild(NodeType.Attribute, "", "LayerDirectory");
  }
  public SchemaString GetLayerDirectoryAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "LayerDirectory", index)));
  }
  public SchemaString GetLayerDirectory()
  {
   return GetLayerDirectoryAt(0);
  }
  public SchemaString LayerDirectory
  {
   get
   {
    return GetLayerDirectoryAt(0);
   }
  }
  public void RemoveLayerDirectoryAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "LayerDirectory", index);
  }
  public void RemoveLayerDirectory()
  {
   while (HasLayerDirectory())
    RemoveLayerDirectoryAt(0);
  }
  public void AddLayerDirectory(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "LayerDirectory", newValue.ToString());
  }
  public void InsertLayerDirectoryAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "LayerDirectory", index, newValue.ToString());
  }
  public void ReplaceLayerDirectoryAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "LayerDirectory", index, newValue.ToString());
  }
        public LayerDirectoryCollection MyLayerDirectorys = new LayerDirectoryCollection( );
        public class LayerDirectoryCollection: IEnumerable
        {
            WorldType parent;
            public WorldType Parent
   {
    set
    {
     parent = value;
    }
   }
   public LayerDirectoryEnumerator GetEnumerator()
   {
    return new LayerDirectoryEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class LayerDirectoryEnumerator: IEnumerator
        {
   int nIndex;
   WorldType parent;
   public LayerDirectoryEnumerator(WorldType par)
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
    return(nIndex < parent.LayerDirectoryCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetLayerDirectoryAt(nIndex));
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
  public int GetTerrainAccessorMinCount()
  {
   return 0;
  }
  public int TerrainAccessorMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetTerrainAccessorMaxCount()
  {
   return Int32.MaxValue;
  }
  public int TerrainAccessorMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetTerrainAccessorCount()
  {
   return DomChildCount(NodeType.Element, "", "TerrainAccessor");
  }
  public int TerrainAccessorCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "TerrainAccessor");
   }
  }
  public bool HasTerrainAccessor()
  {
   return HasDomChild(NodeType.Element, "", "TerrainAccessor");
  }
  public TerrainAccessor GetTerrainAccessorAt(int index)
  {
   return new TerrainAccessor(GetDomChildAt(NodeType.Element, "", "TerrainAccessor", index));
  }
  public TerrainAccessor GetTerrainAccessor()
  {
   return GetTerrainAccessorAt(0);
  }
  public TerrainAccessor TerrainAccessor
  {
   get
   {
    return GetTerrainAccessorAt(0);
   }
  }
  public void RemoveTerrainAccessorAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "TerrainAccessor", index);
  }
  public void RemoveTerrainAccessor()
  {
   while (HasTerrainAccessor())
    RemoveTerrainAccessorAt(0);
  }
  public void AddTerrainAccessor(TerrainAccessor newValue)
  {
   AppendDomElement("", "TerrainAccessor", newValue);
  }
  public void InsertTerrainAccessorAt(TerrainAccessor newValue, int index)
  {
   InsertDomElementAt("", "TerrainAccessor", index, newValue);
  }
  public void ReplaceTerrainAccessorAt(TerrainAccessor newValue, int index)
  {
   ReplaceDomElementAt("", "TerrainAccessor", index, newValue);
  }
        public TerrainAccessorCollection MyTerrainAccessors = new TerrainAccessorCollection( );
        public class TerrainAccessorCollection: IEnumerable
        {
            WorldType parent;
            public WorldType Parent
   {
    set
    {
     parent = value;
    }
   }
   public TerrainAccessorEnumerator GetEnumerator()
   {
    return new TerrainAccessorEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class TerrainAccessorEnumerator: IEnumerator
        {
   int nIndex;
   WorldType parent;
   public TerrainAccessorEnumerator(WorldType par)
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
    return(nIndex < parent.TerrainAccessorCount );
   }
   public TerrainAccessor Current
   {
    get
    {
     return(parent.GetTerrainAccessorAt(nIndex));
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
            MyNames.Parent = this;
            MyEquatorialRadiuss.Parent = this;
            MyLayerDirectorys.Parent = this;
            MyTerrainAccessors.Parent = this;
 }
}
}
