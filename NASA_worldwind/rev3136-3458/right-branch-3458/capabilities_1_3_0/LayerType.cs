using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_3_0.wms
{
 public class LayerType : Altova.Xml.Node
 {
  public LayerType() : base() { SetCollectionParents(); }
  public LayerType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public LayerType(XmlNode node) : base(node) { SetCollectionParents(); }
  public LayerType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "queryable"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "queryable", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "cascaded"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "cascaded", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "opaque"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "opaque", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "noSubsets"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "noSubsets", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "fixedWidth"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "fixedWidth", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "fixedHeight"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "fixedHeight", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Name"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Name", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Title"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Title", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Abstract"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Abstract", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "KeywordList"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "KeywordList", i);
    InternalAdjustPrefix(DOMNode, true);
    new KeywordListType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "CRS"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "CRS", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox", i);
    InternalAdjustPrefix(DOMNode, true);
    new EX_GeographicBoundingBoxType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox", i);
    InternalAdjustPrefix(DOMNode, true);
    new BoundingBoxType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Dimension"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Dimension", i);
    InternalAdjustPrefix(DOMNode, true);
    new DimensionType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Attribution"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Attribution", i);
    InternalAdjustPrefix(DOMNode, true);
    new AttributionType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL", i);
    InternalAdjustPrefix(DOMNode, true);
    new AuthorityURLType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Identifier"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Identifier", i);
    InternalAdjustPrefix(DOMNode, true);
    new IdentifierType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL", i);
    InternalAdjustPrefix(DOMNode, true);
    new MetadataURLType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "DataURL"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "DataURL", i);
    InternalAdjustPrefix(DOMNode, true);
    new DataURLType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL", i);
    InternalAdjustPrefix(DOMNode, true);
    new FeatureListURLType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Style"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Style", i);
    InternalAdjustPrefix(DOMNode, true);
    new StyleType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Layer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Layer", i);
    InternalAdjustPrefix(DOMNode, true);
    new LayerType(DOMNode).AdjustPrefix();
   }
  }
  public int GetqueryableMinCount()
  {
   return 0;
  }
  public int queryableMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetqueryableMaxCount()
  {
   return 1;
  }
  public int queryableMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetqueryableCount()
  {
   return DomChildCount(NodeType.Attribute, "", "queryable");
  }
  public int queryableCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "queryable");
   }
  }
  public bool Hasqueryable()
  {
   return HasDomChild(NodeType.Attribute, "", "queryable");
  }
  public SchemaBoolean GetqueryableAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "queryable", index)));
  }
  public SchemaBoolean Getqueryable()
  {
   return GetqueryableAt(0);
  }
  public SchemaBoolean queryable
  {
   get
   {
    return GetqueryableAt(0);
   }
  }
  public void RemovequeryableAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "queryable", index);
  }
  public void Removequeryable()
  {
   while (Hasqueryable())
    RemovequeryableAt(0);
  }
  public void Addqueryable(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "queryable", newValue.ToString());
  }
  public void InsertqueryableAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "queryable", index, newValue.ToString());
  }
  public void ReplacequeryableAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "queryable", index, newValue.ToString());
  }
        public queryableCollection Myqueryables = new queryableCollection( );
        public class queryableCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public queryableEnumerator GetEnumerator()
   {
    return new queryableEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class queryableEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public queryableEnumerator(LayerType par)
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
    return(nIndex < parent.queryableCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetqueryableAt(nIndex));
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
  public int GetcascadedMinCount()
  {
   return 0;
  }
  public int cascadedMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetcascadedMaxCount()
  {
   return 1;
  }
  public int cascadedMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetcascadedCount()
  {
   return DomChildCount(NodeType.Attribute, "", "cascaded");
  }
  public int cascadedCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "cascaded");
   }
  }
  public bool Hascascaded()
  {
   return HasDomChild(NodeType.Attribute, "", "cascaded");
  }
  public SchemaLong GetcascadedAt(int index)
  {
   return new SchemaLong(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "cascaded", index)));
  }
  public SchemaLong Getcascaded()
  {
   return GetcascadedAt(0);
  }
  public SchemaLong cascaded
  {
   get
   {
    return GetcascadedAt(0);
   }
  }
  public void RemovecascadedAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "cascaded", index);
  }
  public void Removecascaded()
  {
   while (Hascascaded())
    RemovecascadedAt(0);
  }
  public void Addcascaded(SchemaLong newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "cascaded", newValue.ToString());
  }
  public void InsertcascadedAt(SchemaLong newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "cascaded", index, newValue.ToString());
  }
  public void ReplacecascadedAt(SchemaLong newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "cascaded", index, newValue.ToString());
  }
        public cascadedCollection Mycascadeds = new cascadedCollection( );
        public class cascadedCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public cascadedEnumerator GetEnumerator()
   {
    return new cascadedEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class cascadedEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public cascadedEnumerator(LayerType par)
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
    return(nIndex < parent.cascadedCount );
   }
   public SchemaLong Current
   {
    get
    {
     return(parent.GetcascadedAt(nIndex));
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
  public int GetopaqueMinCount()
  {
   return 0;
  }
  public int opaqueMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetopaqueMaxCount()
  {
   return 1;
  }
  public int opaqueMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetopaqueCount()
  {
   return DomChildCount(NodeType.Attribute, "", "opaque");
  }
  public int opaqueCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "opaque");
   }
  }
  public bool Hasopaque()
  {
   return HasDomChild(NodeType.Attribute, "", "opaque");
  }
  public SchemaBoolean GetopaqueAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "opaque", index)));
  }
  public SchemaBoolean Getopaque()
  {
   return GetopaqueAt(0);
  }
  public SchemaBoolean opaque
  {
   get
   {
    return GetopaqueAt(0);
   }
  }
  public void RemoveopaqueAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "opaque", index);
  }
  public void Removeopaque()
  {
   while (Hasopaque())
    RemoveopaqueAt(0);
  }
  public void Addopaque(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "opaque", newValue.ToString());
  }
  public void InsertopaqueAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "opaque", index, newValue.ToString());
  }
  public void ReplaceopaqueAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "opaque", index, newValue.ToString());
  }
        public opaqueCollection Myopaques = new opaqueCollection( );
        public class opaqueCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public opaqueEnumerator GetEnumerator()
   {
    return new opaqueEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class opaqueEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public opaqueEnumerator(LayerType par)
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
    return(nIndex < parent.opaqueCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetopaqueAt(nIndex));
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
  public int GetnoSubsetsMinCount()
  {
   return 0;
  }
  public int noSubsetsMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetnoSubsetsMaxCount()
  {
   return 1;
  }
  public int noSubsetsMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetnoSubsetsCount()
  {
   return DomChildCount(NodeType.Attribute, "", "noSubsets");
  }
  public int noSubsetsCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "noSubsets");
   }
  }
  public bool HasnoSubsets()
  {
   return HasDomChild(NodeType.Attribute, "", "noSubsets");
  }
  public SchemaBoolean GetnoSubsetsAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "noSubsets", index)));
  }
  public SchemaBoolean GetnoSubsets()
  {
   return GetnoSubsetsAt(0);
  }
  public SchemaBoolean noSubsets
  {
   get
   {
    return GetnoSubsetsAt(0);
   }
  }
  public void RemovenoSubsetsAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "noSubsets", index);
  }
  public void RemovenoSubsets()
  {
   while (HasnoSubsets())
    RemovenoSubsetsAt(0);
  }
  public void AddnoSubsets(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "noSubsets", newValue.ToString());
  }
  public void InsertnoSubsetsAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "noSubsets", index, newValue.ToString());
  }
  public void ReplacenoSubsetsAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "noSubsets", index, newValue.ToString());
  }
        public noSubsetsCollection MynoSubsetss = new noSubsetsCollection( );
        public class noSubsetsCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public noSubsetsEnumerator GetEnumerator()
   {
    return new noSubsetsEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class noSubsetsEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public noSubsetsEnumerator(LayerType par)
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
    return(nIndex < parent.noSubsetsCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetnoSubsetsAt(nIndex));
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
  public int GetfixedWidthMinCount()
  {
   return 0;
  }
  public int fixedWidthMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetfixedWidthMaxCount()
  {
   return 1;
  }
  public int fixedWidthMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetfixedWidthCount()
  {
   return DomChildCount(NodeType.Attribute, "", "fixedWidth");
  }
  public int fixedWidthCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "fixedWidth");
   }
  }
  public bool HasfixedWidth()
  {
   return HasDomChild(NodeType.Attribute, "", "fixedWidth");
  }
  public SchemaLong GetfixedWidthAt(int index)
  {
   return new SchemaLong(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "fixedWidth", index)));
  }
  public SchemaLong GetfixedWidth()
  {
   return GetfixedWidthAt(0);
  }
  public SchemaLong fixedWidth
  {
   get
   {
    return GetfixedWidthAt(0);
   }
  }
  public void RemovefixedWidthAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "fixedWidth", index);
  }
  public void RemovefixedWidth()
  {
   while (HasfixedWidth())
    RemovefixedWidthAt(0);
  }
  public void AddfixedWidth(SchemaLong newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "fixedWidth", newValue.ToString());
  }
  public void InsertfixedWidthAt(SchemaLong newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "fixedWidth", index, newValue.ToString());
  }
  public void ReplacefixedWidthAt(SchemaLong newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "fixedWidth", index, newValue.ToString());
  }
        public fixedWidthCollection MyfixedWidths = new fixedWidthCollection( );
        public class fixedWidthCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public fixedWidthEnumerator GetEnumerator()
   {
    return new fixedWidthEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class fixedWidthEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public fixedWidthEnumerator(LayerType par)
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
    return(nIndex < parent.fixedWidthCount );
   }
   public SchemaLong Current
   {
    get
    {
     return(parent.GetfixedWidthAt(nIndex));
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
  public int GetfixedHeightMinCount()
  {
   return 0;
  }
  public int fixedHeightMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetfixedHeightMaxCount()
  {
   return 1;
  }
  public int fixedHeightMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetfixedHeightCount()
  {
   return DomChildCount(NodeType.Attribute, "", "fixedHeight");
  }
  public int fixedHeightCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "fixedHeight");
   }
  }
  public bool HasfixedHeight()
  {
   return HasDomChild(NodeType.Attribute, "", "fixedHeight");
  }
  public SchemaLong GetfixedHeightAt(int index)
  {
   return new SchemaLong(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "fixedHeight", index)));
  }
  public SchemaLong GetfixedHeight()
  {
   return GetfixedHeightAt(0);
  }
  public SchemaLong fixedHeight
  {
   get
   {
    return GetfixedHeightAt(0);
   }
  }
  public void RemovefixedHeightAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "fixedHeight", index);
  }
  public void RemovefixedHeight()
  {
   while (HasfixedHeight())
    RemovefixedHeightAt(0);
  }
  public void AddfixedHeight(SchemaLong newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "fixedHeight", newValue.ToString());
  }
  public void InsertfixedHeightAt(SchemaLong newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "fixedHeight", index, newValue.ToString());
  }
  public void ReplacefixedHeightAt(SchemaLong newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "fixedHeight", index, newValue.ToString());
  }
        public fixedHeightCollection MyfixedHeights = new fixedHeightCollection( );
        public class fixedHeightCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public fixedHeightEnumerator GetEnumerator()
   {
    return new fixedHeightEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class fixedHeightEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public fixedHeightEnumerator(LayerType par)
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
    return(nIndex < parent.fixedHeightCount );
   }
   public SchemaLong Current
   {
    get
    {
     return(parent.GetfixedHeightAt(nIndex));
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
  public int GetNameMinCount()
  {
   return 0;
  }
  public int NameMinCount
  {
   get
   {
    return 0;
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
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Name");
  }
  public int NameCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Name");
   }
  }
  public bool HasName()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Name");
  }
  public SchemaString GetNameAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Name", index)));
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
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Name", index);
  }
  public void RemoveName()
  {
   while (HasName())
    RemoveNameAt(0);
  }
  public void AddName(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "Name", newValue.ToString());
  }
  public void InsertNameAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Name", index, newValue.ToString());
  }
  public void ReplaceNameAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Name", index, newValue.ToString());
  }
        public NameCollection MyNames = new NameCollection( );
        public class NameCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
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
   LayerType parent;
   public NameEnumerator(LayerType par)
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
  public int GetTitleMinCount()
  {
   return 1;
  }
  public int TitleMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetTitleMaxCount()
  {
   return 1;
  }
  public int TitleMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetTitleCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Title");
  }
  public int TitleCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Title");
   }
  }
  public bool HasTitle()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Title");
  }
  public SchemaString GetTitleAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Title", index)));
  }
  public SchemaString GetTitle()
  {
   return GetTitleAt(0);
  }
  public SchemaString Title
  {
   get
   {
    return GetTitleAt(0);
   }
  }
  public void RemoveTitleAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Title", index);
  }
  public void RemoveTitle()
  {
   while (HasTitle())
    RemoveTitleAt(0);
  }
  public void AddTitle(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "Title", newValue.ToString());
  }
  public void InsertTitleAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Title", index, newValue.ToString());
  }
  public void ReplaceTitleAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Title", index, newValue.ToString());
  }
        public TitleCollection MyTitles = new TitleCollection( );
        public class TitleCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public TitleEnumerator GetEnumerator()
   {
    return new TitleEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class TitleEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public TitleEnumerator(LayerType par)
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
    return(nIndex < parent.TitleCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetTitleAt(nIndex));
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
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Abstract");
  }
  public int Abstract2Count
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Abstract");
   }
  }
  public bool HasAbstract2()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Abstract");
  }
  public SchemaString GetAbstract2At(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Abstract", index)));
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
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Abstract", index);
  }
  public void RemoveAbstract2()
  {
   while (HasAbstract2())
    RemoveAbstract2At(0);
  }
  public void AddAbstract2(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "Abstract", newValue.ToString());
  }
  public void InsertAbstract2At(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Abstract", index, newValue.ToString());
  }
  public void ReplaceAbstract2At(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Abstract", index, newValue.ToString());
  }
        public Abstract2Collection MyAbstract2s = new Abstract2Collection( );
        public class Abstract2Collection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
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
   LayerType parent;
   public Abstract2Enumerator(LayerType par)
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
  public int GetKeywordListMinCount()
  {
   return 0;
  }
  public int KeywordListMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetKeywordListMaxCount()
  {
   return 1;
  }
  public int KeywordListMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetKeywordListCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "KeywordList");
  }
  public int KeywordListCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "KeywordList");
   }
  }
  public bool HasKeywordList()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "KeywordList");
  }
  public KeywordListType GetKeywordListAt(int index)
  {
   return new KeywordListType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "KeywordList", index));
  }
  public KeywordListType GetKeywordList()
  {
   return GetKeywordListAt(0);
  }
  public KeywordListType KeywordList
  {
   get
   {
    return GetKeywordListAt(0);
   }
  }
  public void RemoveKeywordListAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "KeywordList", index);
  }
  public void RemoveKeywordList()
  {
   while (HasKeywordList())
    RemoveKeywordListAt(0);
  }
  public void AddKeywordList(KeywordListType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "KeywordList", newValue);
  }
  public void InsertKeywordListAt(KeywordListType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "KeywordList", index, newValue);
  }
  public void ReplaceKeywordListAt(KeywordListType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "KeywordList", index, newValue);
  }
        public KeywordListCollection MyKeywordLists = new KeywordListCollection( );
        public class KeywordListCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public KeywordListEnumerator GetEnumerator()
   {
    return new KeywordListEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class KeywordListEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public KeywordListEnumerator(LayerType par)
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
    return(nIndex < parent.KeywordListCount );
   }
   public KeywordListType Current
   {
    get
    {
     return(parent.GetKeywordListAt(nIndex));
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
  public int GetCRSMinCount()
  {
   return 0;
  }
  public int CRSMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetCRSMaxCount()
  {
   return Int32.MaxValue;
  }
  public int CRSMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetCRSCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "CRS");
  }
  public int CRSCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "CRS");
   }
  }
  public bool HasCRS()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "CRS");
  }
  public SchemaString GetCRSAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "CRS", index)));
  }
  public SchemaString GetCRS()
  {
   return GetCRSAt(0);
  }
  public SchemaString CRS
  {
   get
   {
    return GetCRSAt(0);
   }
  }
  public void RemoveCRSAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "CRS", index);
  }
  public void RemoveCRS()
  {
   while (HasCRS())
    RemoveCRSAt(0);
  }
  public void AddCRS(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "CRS", newValue.ToString());
  }
  public void InsertCRSAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "CRS", index, newValue.ToString());
  }
  public void ReplaceCRSAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "CRS", index, newValue.ToString());
  }
        public CRSCollection MyCRSs = new CRSCollection( );
        public class CRSCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public CRSEnumerator GetEnumerator()
   {
    return new CRSEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class CRSEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public CRSEnumerator(LayerType par)
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
    return(nIndex < parent.CRSCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetCRSAt(nIndex));
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
  public int GetEX_GeographicBoundingBoxMinCount()
  {
   return 0;
  }
  public int EX_GeographicBoundingBoxMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetEX_GeographicBoundingBoxMaxCount()
  {
   return 1;
  }
  public int EX_GeographicBoundingBoxMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetEX_GeographicBoundingBoxCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox");
  }
  public int EX_GeographicBoundingBoxCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox");
   }
  }
  public bool HasEX_GeographicBoundingBox()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox");
  }
  public EX_GeographicBoundingBoxType GetEX_GeographicBoundingBoxAt(int index)
  {
   return new EX_GeographicBoundingBoxType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox", index));
  }
  public EX_GeographicBoundingBoxType GetEX_GeographicBoundingBox()
  {
   return GetEX_GeographicBoundingBoxAt(0);
  }
  public EX_GeographicBoundingBoxType EX_GeographicBoundingBox
  {
   get
   {
    return GetEX_GeographicBoundingBoxAt(0);
   }
  }
  public void RemoveEX_GeographicBoundingBoxAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "EX_GeographicBoundingBox", index);
  }
  public void RemoveEX_GeographicBoundingBox()
  {
   while (HasEX_GeographicBoundingBox())
    RemoveEX_GeographicBoundingBoxAt(0);
  }
  public void AddEX_GeographicBoundingBox(EX_GeographicBoundingBoxType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "EX_GeographicBoundingBox", newValue);
  }
  public void InsertEX_GeographicBoundingBoxAt(EX_GeographicBoundingBoxType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "EX_GeographicBoundingBox", index, newValue);
  }
  public void ReplaceEX_GeographicBoundingBoxAt(EX_GeographicBoundingBoxType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "EX_GeographicBoundingBox", index, newValue);
  }
        public EX_GeographicBoundingBoxCollection MyEX_GeographicBoundingBoxs = new EX_GeographicBoundingBoxCollection( );
        public class EX_GeographicBoundingBoxCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public EX_GeographicBoundingBoxEnumerator GetEnumerator()
   {
    return new EX_GeographicBoundingBoxEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class EX_GeographicBoundingBoxEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public EX_GeographicBoundingBoxEnumerator(LayerType par)
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
    return(nIndex < parent.EX_GeographicBoundingBoxCount );
   }
   public EX_GeographicBoundingBoxType Current
   {
    get
    {
     return(parent.GetEX_GeographicBoundingBoxAt(nIndex));
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
  public int GetBoundingBoxMinCount()
  {
   return 0;
  }
  public int BoundingBoxMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetBoundingBoxMaxCount()
  {
   return Int32.MaxValue;
  }
  public int BoundingBoxMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetBoundingBoxCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox");
  }
  public int BoundingBoxCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox");
   }
  }
  public bool HasBoundingBox()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox");
  }
  public BoundingBoxType GetBoundingBoxAt(int index)
  {
   return new BoundingBoxType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox", index));
  }
  public BoundingBoxType GetBoundingBox()
  {
   return GetBoundingBoxAt(0);
  }
  public BoundingBoxType BoundingBox
  {
   get
   {
    return GetBoundingBoxAt(0);
   }
  }
  public void RemoveBoundingBoxAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "BoundingBox", index);
  }
  public void RemoveBoundingBox()
  {
   while (HasBoundingBox())
    RemoveBoundingBoxAt(0);
  }
  public void AddBoundingBox(BoundingBoxType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "BoundingBox", newValue);
  }
  public void InsertBoundingBoxAt(BoundingBoxType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "BoundingBox", index, newValue);
  }
  public void ReplaceBoundingBoxAt(BoundingBoxType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "BoundingBox", index, newValue);
  }
        public BoundingBoxCollection MyBoundingBoxs = new BoundingBoxCollection( );
        public class BoundingBoxCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public BoundingBoxEnumerator GetEnumerator()
   {
    return new BoundingBoxEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class BoundingBoxEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public BoundingBoxEnumerator(LayerType par)
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
    return(nIndex < parent.BoundingBoxCount );
   }
   public BoundingBoxType Current
   {
    get
    {
     return(parent.GetBoundingBoxAt(nIndex));
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
  public int GetDimensionMinCount()
  {
   return 0;
  }
  public int DimensionMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetDimensionMaxCount()
  {
   return Int32.MaxValue;
  }
  public int DimensionMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetDimensionCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Dimension");
  }
  public int DimensionCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Dimension");
   }
  }
  public bool HasDimension()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Dimension");
  }
  public DimensionType GetDimensionAt(int index)
  {
   return new DimensionType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Dimension", index));
  }
  public DimensionType GetDimension()
  {
   return GetDimensionAt(0);
  }
  public DimensionType Dimension
  {
   get
   {
    return GetDimensionAt(0);
   }
  }
  public void RemoveDimensionAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Dimension", index);
  }
  public void RemoveDimension()
  {
   while (HasDimension())
    RemoveDimensionAt(0);
  }
  public void AddDimension(DimensionType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "Dimension", newValue);
  }
  public void InsertDimensionAt(DimensionType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "Dimension", index, newValue);
  }
  public void ReplaceDimensionAt(DimensionType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "Dimension", index, newValue);
  }
        public DimensionCollection MyDimensions = new DimensionCollection( );
        public class DimensionCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public DimensionEnumerator GetEnumerator()
   {
    return new DimensionEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class DimensionEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public DimensionEnumerator(LayerType par)
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
    return(nIndex < parent.DimensionCount );
   }
   public DimensionType Current
   {
    get
    {
     return(parent.GetDimensionAt(nIndex));
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
  public int GetAttributionMinCount()
  {
   return 0;
  }
  public int AttributionMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetAttributionMaxCount()
  {
   return 1;
  }
  public int AttributionMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetAttributionCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Attribution");
  }
  public int AttributionCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Attribution");
   }
  }
  public bool HasAttribution()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Attribution");
  }
  public AttributionType GetAttributionAt(int index)
  {
   return new AttributionType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Attribution", index));
  }
  public AttributionType GetAttribution()
  {
   return GetAttributionAt(0);
  }
  public AttributionType Attribution
  {
   get
   {
    return GetAttributionAt(0);
   }
  }
  public void RemoveAttributionAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Attribution", index);
  }
  public void RemoveAttribution()
  {
   while (HasAttribution())
    RemoveAttributionAt(0);
  }
  public void AddAttribution(AttributionType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "Attribution", newValue);
  }
  public void InsertAttributionAt(AttributionType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "Attribution", index, newValue);
  }
  public void ReplaceAttributionAt(AttributionType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "Attribution", index, newValue);
  }
        public AttributionCollection MyAttributions = new AttributionCollection( );
        public class AttributionCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public AttributionEnumerator GetEnumerator()
   {
    return new AttributionEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class AttributionEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public AttributionEnumerator(LayerType par)
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
    return(nIndex < parent.AttributionCount );
   }
   public AttributionType Current
   {
    get
    {
     return(parent.GetAttributionAt(nIndex));
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
  public int GetAuthorityURLMinCount()
  {
   return 0;
  }
  public int AuthorityURLMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetAuthorityURLMaxCount()
  {
   return Int32.MaxValue;
  }
  public int AuthorityURLMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetAuthorityURLCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL");
  }
  public int AuthorityURLCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL");
   }
  }
  public bool HasAuthorityURL()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL");
  }
  public AuthorityURLType GetAuthorityURLAt(int index)
  {
   return new AuthorityURLType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL", index));
  }
  public AuthorityURLType GetAuthorityURL()
  {
   return GetAuthorityURLAt(0);
  }
  public AuthorityURLType AuthorityURL
  {
   get
   {
    return GetAuthorityURLAt(0);
   }
  }
  public void RemoveAuthorityURLAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AuthorityURL", index);
  }
  public void RemoveAuthorityURL()
  {
   while (HasAuthorityURL())
    RemoveAuthorityURLAt(0);
  }
  public void AddAuthorityURL(AuthorityURLType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "AuthorityURL", newValue);
  }
  public void InsertAuthorityURLAt(AuthorityURLType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "AuthorityURL", index, newValue);
  }
  public void ReplaceAuthorityURLAt(AuthorityURLType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "AuthorityURL", index, newValue);
  }
        public AuthorityURLCollection MyAuthorityURLs = new AuthorityURLCollection( );
        public class AuthorityURLCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public AuthorityURLEnumerator GetEnumerator()
   {
    return new AuthorityURLEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class AuthorityURLEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public AuthorityURLEnumerator(LayerType par)
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
    return(nIndex < parent.AuthorityURLCount );
   }
   public AuthorityURLType Current
   {
    get
    {
     return(parent.GetAuthorityURLAt(nIndex));
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
  public int GetIdentifierMinCount()
  {
   return 0;
  }
  public int IdentifierMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetIdentifierMaxCount()
  {
   return Int32.MaxValue;
  }
  public int IdentifierMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetIdentifierCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Identifier");
  }
  public int IdentifierCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Identifier");
   }
  }
  public bool HasIdentifier()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Identifier");
  }
  public IdentifierType GetIdentifierAt(int index)
  {
   return new IdentifierType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Identifier", index));
  }
  public IdentifierType GetIdentifier()
  {
   return GetIdentifierAt(0);
  }
  public IdentifierType Identifier
  {
   get
   {
    return GetIdentifierAt(0);
   }
  }
  public void RemoveIdentifierAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Identifier", index);
  }
  public void RemoveIdentifier()
  {
   while (HasIdentifier())
    RemoveIdentifierAt(0);
  }
  public void AddIdentifier(IdentifierType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "Identifier", newValue);
  }
  public void InsertIdentifierAt(IdentifierType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "Identifier", index, newValue);
  }
  public void ReplaceIdentifierAt(IdentifierType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "Identifier", index, newValue);
  }
        public IdentifierCollection MyIdentifiers = new IdentifierCollection( );
        public class IdentifierCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public IdentifierEnumerator GetEnumerator()
   {
    return new IdentifierEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class IdentifierEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public IdentifierEnumerator(LayerType par)
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
    return(nIndex < parent.IdentifierCount );
   }
   public IdentifierType Current
   {
    get
    {
     return(parent.GetIdentifierAt(nIndex));
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
  public int GetMetadataURLMinCount()
  {
   return 0;
  }
  public int MetadataURLMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetMetadataURLMaxCount()
  {
   return Int32.MaxValue;
  }
  public int MetadataURLMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetMetadataURLCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL");
  }
  public int MetadataURLCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL");
   }
  }
  public bool HasMetadataURL()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL");
  }
  public MetadataURLType GetMetadataURLAt(int index)
  {
   return new MetadataURLType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL", index));
  }
  public MetadataURLType GetMetadataURL()
  {
   return GetMetadataURLAt(0);
  }
  public MetadataURLType MetadataURL
  {
   get
   {
    return GetMetadataURLAt(0);
   }
  }
  public void RemoveMetadataURLAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MetadataURL", index);
  }
  public void RemoveMetadataURL()
  {
   while (HasMetadataURL())
    RemoveMetadataURLAt(0);
  }
  public void AddMetadataURL(MetadataURLType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "MetadataURL", newValue);
  }
  public void InsertMetadataURLAt(MetadataURLType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "MetadataURL", index, newValue);
  }
  public void ReplaceMetadataURLAt(MetadataURLType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "MetadataURL", index, newValue);
  }
        public MetadataURLCollection MyMetadataURLs = new MetadataURLCollection( );
        public class MetadataURLCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public MetadataURLEnumerator GetEnumerator()
   {
    return new MetadataURLEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class MetadataURLEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public MetadataURLEnumerator(LayerType par)
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
    return(nIndex < parent.MetadataURLCount );
   }
   public MetadataURLType Current
   {
    get
    {
     return(parent.GetMetadataURLAt(nIndex));
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
  public int GetDataURLMinCount()
  {
   return 0;
  }
  public int DataURLMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetDataURLMaxCount()
  {
   return Int32.MaxValue;
  }
  public int DataURLMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetDataURLCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "DataURL");
  }
  public int DataURLCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "DataURL");
   }
  }
  public bool HasDataURL()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "DataURL");
  }
  public DataURLType GetDataURLAt(int index)
  {
   return new DataURLType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "DataURL", index));
  }
  public DataURLType GetDataURL()
  {
   return GetDataURLAt(0);
  }
  public DataURLType DataURL
  {
   get
   {
    return GetDataURLAt(0);
   }
  }
  public void RemoveDataURLAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "DataURL", index);
  }
  public void RemoveDataURL()
  {
   while (HasDataURL())
    RemoveDataURLAt(0);
  }
  public void AddDataURL(DataURLType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "DataURL", newValue);
  }
  public void InsertDataURLAt(DataURLType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "DataURL", index, newValue);
  }
  public void ReplaceDataURLAt(DataURLType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "DataURL", index, newValue);
  }
        public DataURLCollection MyDataURLs = new DataURLCollection( );
        public class DataURLCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public DataURLEnumerator GetEnumerator()
   {
    return new DataURLEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class DataURLEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public DataURLEnumerator(LayerType par)
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
    return(nIndex < parent.DataURLCount );
   }
   public DataURLType Current
   {
    get
    {
     return(parent.GetDataURLAt(nIndex));
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
  public int GetFeatureListURLMinCount()
  {
   return 0;
  }
  public int FeatureListURLMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetFeatureListURLMaxCount()
  {
   return Int32.MaxValue;
  }
  public int FeatureListURLMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetFeatureListURLCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL");
  }
  public int FeatureListURLCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL");
   }
  }
  public bool HasFeatureListURL()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL");
  }
  public FeatureListURLType GetFeatureListURLAt(int index)
  {
   return new FeatureListURLType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL", index));
  }
  public FeatureListURLType GetFeatureListURL()
  {
   return GetFeatureListURLAt(0);
  }
  public FeatureListURLType FeatureListURL
  {
   get
   {
    return GetFeatureListURLAt(0);
   }
  }
  public void RemoveFeatureListURLAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "FeatureListURL", index);
  }
  public void RemoveFeatureListURL()
  {
   while (HasFeatureListURL())
    RemoveFeatureListURLAt(0);
  }
  public void AddFeatureListURL(FeatureListURLType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "FeatureListURL", newValue);
  }
  public void InsertFeatureListURLAt(FeatureListURLType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "FeatureListURL", index, newValue);
  }
  public void ReplaceFeatureListURLAt(FeatureListURLType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "FeatureListURL", index, newValue);
  }
        public FeatureListURLCollection MyFeatureListURLs = new FeatureListURLCollection( );
        public class FeatureListURLCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public FeatureListURLEnumerator GetEnumerator()
   {
    return new FeatureListURLEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class FeatureListURLEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public FeatureListURLEnumerator(LayerType par)
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
    return(nIndex < parent.FeatureListURLCount );
   }
   public FeatureListURLType Current
   {
    get
    {
     return(parent.GetFeatureListURLAt(nIndex));
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
  public int GetStyleMinCount()
  {
   return 0;
  }
  public int StyleMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetStyleMaxCount()
  {
   return Int32.MaxValue;
  }
  public int StyleMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetStyleCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Style");
  }
  public int StyleCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Style");
   }
  }
  public bool HasStyle()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Style");
  }
  public StyleType GetStyleAt(int index)
  {
   return new StyleType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Style", index));
  }
  public StyleType GetStyle()
  {
   return GetStyleAt(0);
  }
  public StyleType Style
  {
   get
   {
    return GetStyleAt(0);
   }
  }
  public void RemoveStyleAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Style", index);
  }
  public void RemoveStyle()
  {
   while (HasStyle())
    RemoveStyleAt(0);
  }
  public void AddStyle(StyleType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "Style", newValue);
  }
  public void InsertStyleAt(StyleType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "Style", index, newValue);
  }
  public void ReplaceStyleAt(StyleType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "Style", index, newValue);
  }
        public StyleCollection MyStyles = new StyleCollection( );
        public class StyleCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public StyleEnumerator GetEnumerator()
   {
    return new StyleEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class StyleEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public StyleEnumerator(LayerType par)
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
    return(nIndex < parent.StyleCount );
   }
   public StyleType Current
   {
    get
    {
     return(parent.GetStyleAt(nIndex));
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
  public int GetMinScaleDenominatorMinCount()
  {
   return 0;
  }
  public int MinScaleDenominatorMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetMinScaleDenominatorMaxCount()
  {
   return 1;
  }
  public int MinScaleDenominatorMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetMinScaleDenominatorCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator");
  }
  public int MinScaleDenominatorCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator");
   }
  }
  public bool HasMinScaleDenominator()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator");
  }
  public SchemaDecimal GetMinScaleDenominatorAt(int index)
  {
   return new SchemaDecimal(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator", index)));
  }
  public SchemaDecimal GetMinScaleDenominator()
  {
   return GetMinScaleDenominatorAt(0);
  }
  public SchemaDecimal MinScaleDenominator
  {
   get
   {
    return GetMinScaleDenominatorAt(0);
   }
  }
  public void RemoveMinScaleDenominatorAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator", index);
  }
  public void RemoveMinScaleDenominator()
  {
   while (HasMinScaleDenominator())
    RemoveMinScaleDenominatorAt(0);
  }
  public void AddMinScaleDenominator(SchemaDecimal newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator", newValue.ToString());
  }
  public void InsertMinScaleDenominatorAt(SchemaDecimal newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator", index, newValue.ToString());
  }
  public void ReplaceMinScaleDenominatorAt(SchemaDecimal newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MinScaleDenominator", index, newValue.ToString());
  }
        public MinScaleDenominatorCollection MyMinScaleDenominators = new MinScaleDenominatorCollection( );
        public class MinScaleDenominatorCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public MinScaleDenominatorEnumerator GetEnumerator()
   {
    return new MinScaleDenominatorEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class MinScaleDenominatorEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public MinScaleDenominatorEnumerator(LayerType par)
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
    return(nIndex < parent.MinScaleDenominatorCount );
   }
   public SchemaDecimal Current
   {
    get
    {
     return(parent.GetMinScaleDenominatorAt(nIndex));
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
  public int GetMaxScaleDenominatorMinCount()
  {
   return 0;
  }
  public int MaxScaleDenominatorMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetMaxScaleDenominatorMaxCount()
  {
   return 1;
  }
  public int MaxScaleDenominatorMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetMaxScaleDenominatorCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator");
  }
  public int MaxScaleDenominatorCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator");
   }
  }
  public bool HasMaxScaleDenominator()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator");
  }
  public SchemaDecimal GetMaxScaleDenominatorAt(int index)
  {
   return new SchemaDecimal(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator", index)));
  }
  public SchemaDecimal GetMaxScaleDenominator()
  {
   return GetMaxScaleDenominatorAt(0);
  }
  public SchemaDecimal MaxScaleDenominator
  {
   get
   {
    return GetMaxScaleDenominatorAt(0);
   }
  }
  public void RemoveMaxScaleDenominatorAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator", index);
  }
  public void RemoveMaxScaleDenominator()
  {
   while (HasMaxScaleDenominator())
    RemoveMaxScaleDenominatorAt(0);
  }
  public void AddMaxScaleDenominator(SchemaDecimal newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator", newValue.ToString());
  }
  public void InsertMaxScaleDenominatorAt(SchemaDecimal newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator", index, newValue.ToString());
  }
  public void ReplaceMaxScaleDenominatorAt(SchemaDecimal newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "MaxScaleDenominator", index, newValue.ToString());
  }
        public MaxScaleDenominatorCollection MyMaxScaleDenominators = new MaxScaleDenominatorCollection( );
        public class MaxScaleDenominatorCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public MaxScaleDenominatorEnumerator GetEnumerator()
   {
    return new MaxScaleDenominatorEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class MaxScaleDenominatorEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public MaxScaleDenominatorEnumerator(LayerType par)
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
    return(nIndex < parent.MaxScaleDenominatorCount );
   }
   public SchemaDecimal Current
   {
    get
    {
     return(parent.GetMaxScaleDenominatorAt(nIndex));
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
  public int GetLayerMinCount()
  {
   return 0;
  }
  public int LayerMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetLayerMaxCount()
  {
   return Int32.MaxValue;
  }
  public int LayerMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetLayerCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Layer");
  }
  public int LayerCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Layer");
   }
  }
  public bool HasLayer()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Layer");
  }
  public LayerType GetLayerAt(int index)
  {
   return new LayerType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Layer", index));
  }
  public LayerType GetLayer()
  {
   return GetLayerAt(0);
  }
  public LayerType Layer
  {
   get
   {
    return GetLayerAt(0);
   }
  }
  public void RemoveLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Layer", index);
  }
  public void RemoveLayer()
  {
   while (HasLayer())
    RemoveLayerAt(0);
  }
  public void AddLayer(LayerType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "Layer", newValue);
  }
  public void InsertLayerAt(LayerType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "Layer", index, newValue);
  }
  public void ReplaceLayerAt(LayerType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "Layer", index, newValue);
  }
        public LayerCollection MyLayers = new LayerCollection( );
        public class LayerCollection: IEnumerable
        {
            LayerType parent;
            public LayerType Parent
   {
    set
    {
     parent = value;
    }
   }
   public LayerEnumerator GetEnumerator()
   {
    return new LayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class LayerEnumerator: IEnumerator
        {
   int nIndex;
   LayerType parent;
   public LayerEnumerator(LayerType par)
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
    return(nIndex < parent.LayerCount );
   }
   public LayerType Current
   {
    get
    {
     return(parent.GetLayerAt(nIndex));
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
            Myqueryables.Parent = this;
            Mycascadeds.Parent = this;
            Myopaques.Parent = this;
            MynoSubsetss.Parent = this;
            MyfixedWidths.Parent = this;
            MyfixedHeights.Parent = this;
            MyNames.Parent = this;
            MyTitles.Parent = this;
            MyAbstract2s.Parent = this;
            MyKeywordLists.Parent = this;
            MyCRSs.Parent = this;
            MyEX_GeographicBoundingBoxs.Parent = this;
            MyBoundingBoxs.Parent = this;
            MyDimensions.Parent = this;
            MyAttributions.Parent = this;
            MyAuthorityURLs.Parent = this;
            MyIdentifiers.Parent = this;
            MyMetadataURLs.Parent = this;
            MyDataURLs.Parent = this;
            MyFeatureListURLs.Parent = this;
            MyStyles.Parent = this;
            MyMinScaleDenominators.Parent = this;
            MyMaxScaleDenominators.Parent = this;
            MyLayers.Parent = this;
 }
}
}
