using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_1_1
{
 public class RequestType : Altova.Xml.Node
 {
  public RequestType() : base() { SetCollectionParents(); }
  public RequestType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public RequestType(XmlNode node) : base(node) { SetCollectionParents(); }
  public RequestType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "GetCapabilities"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "GetCapabilities", i);
    InternalAdjustPrefix(DOMNode, false);
    new GetCapabilitiesType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "GetMap"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "GetMap", i);
    InternalAdjustPrefix(DOMNode, false);
    new GetMapType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "GetFeatureInfo"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "GetFeatureInfo", i);
    InternalAdjustPrefix(DOMNode, false);
    new GetFeatureInfoType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "DescribeLayer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "DescribeLayer", i);
    InternalAdjustPrefix(DOMNode, false);
    new DescribeLayerType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "GetLegendGraphic"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "GetLegendGraphic", i);
    InternalAdjustPrefix(DOMNode, false);
    new GetLegendGraphicType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "GetStyles"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "GetStyles", i);
    InternalAdjustPrefix(DOMNode, false);
    new GetStylesType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "PutStyles"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "PutStyles", i);
    InternalAdjustPrefix(DOMNode, false);
    new PutStylesType(DOMNode).AdjustPrefix();
   }
  }
  public int GetGetCapabilitiesMinCount()
  {
   return 1;
  }
  public int GetCapabilitiesMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetCapabilitiesMaxCount()
  {
   return 1;
  }
  public int GetCapabilitiesMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetCapabilitiesCount()
  {
   return DomChildCount(NodeType.Element, "", "GetCapabilities");
  }
  public int GetCapabilitiesCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "GetCapabilities");
   }
  }
  public bool HasGetCapabilities()
  {
   return HasDomChild(NodeType.Element, "", "GetCapabilities");
  }
  public GetCapabilitiesType GetGetCapabilitiesAt(int index)
  {
   return new GetCapabilitiesType(GetDomChildAt(NodeType.Element, "", "GetCapabilities", index));
  }
  public GetCapabilitiesType GetGetCapabilities()
  {
   return GetGetCapabilitiesAt(0);
  }
  public GetCapabilitiesType GetCapabilities
  {
   get
   {
    return GetGetCapabilitiesAt(0);
   }
  }
  public void RemoveGetCapabilitiesAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "GetCapabilities", index);
  }
  public void RemoveGetCapabilities()
  {
   while (HasGetCapabilities())
    RemoveGetCapabilitiesAt(0);
  }
  public void AddGetCapabilities(GetCapabilitiesType newValue)
  {
   AppendDomElement("", "GetCapabilities", newValue);
  }
  public void InsertGetCapabilitiesAt(GetCapabilitiesType newValue, int index)
  {
   InsertDomElementAt("", "GetCapabilities", index, newValue);
  }
  public void ReplaceGetCapabilitiesAt(GetCapabilitiesType newValue, int index)
  {
   ReplaceDomElementAt("", "GetCapabilities", index, newValue);
  }
        public GetCapabilitiesCollection MyGetCapabilitiess = new GetCapabilitiesCollection( );
        public class GetCapabilitiesCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public GetCapabilitiesEnumerator GetEnumerator()
   {
    return new GetCapabilitiesEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class GetCapabilitiesEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public GetCapabilitiesEnumerator(RequestType par)
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
    return(nIndex < parent.GetCapabilitiesCount );
   }
   public GetCapabilitiesType Current
   {
    get
    {
     return(parent.GetGetCapabilitiesAt(nIndex));
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
  public int GetGetMapMinCount()
  {
   return 1;
  }
  public int GetMapMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetMapMaxCount()
  {
   return 1;
  }
  public int GetMapMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetMapCount()
  {
   return DomChildCount(NodeType.Element, "", "GetMap");
  }
  public int GetMapCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "GetMap");
   }
  }
  public bool HasGetMap()
  {
   return HasDomChild(NodeType.Element, "", "GetMap");
  }
  public GetMapType GetGetMapAt(int index)
  {
   return new GetMapType(GetDomChildAt(NodeType.Element, "", "GetMap", index));
  }
  public GetMapType GetGetMap()
  {
   return GetGetMapAt(0);
  }
  public GetMapType GetMap
  {
   get
   {
    return GetGetMapAt(0);
   }
  }
  public void RemoveGetMapAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "GetMap", index);
  }
  public void RemoveGetMap()
  {
   while (HasGetMap())
    RemoveGetMapAt(0);
  }
  public void AddGetMap(GetMapType newValue)
  {
   AppendDomElement("", "GetMap", newValue);
  }
  public void InsertGetMapAt(GetMapType newValue, int index)
  {
   InsertDomElementAt("", "GetMap", index, newValue);
  }
  public void ReplaceGetMapAt(GetMapType newValue, int index)
  {
   ReplaceDomElementAt("", "GetMap", index, newValue);
  }
        public GetMapCollection MyGetMaps = new GetMapCollection( );
        public class GetMapCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public GetMapEnumerator GetEnumerator()
   {
    return new GetMapEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class GetMapEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public GetMapEnumerator(RequestType par)
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
    return(nIndex < parent.GetMapCount );
   }
   public GetMapType Current
   {
    get
    {
     return(parent.GetGetMapAt(nIndex));
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
  public int GetGetFeatureInfoMinCount()
  {
   return 0;
  }
  public int GetFeatureInfoMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetGetFeatureInfoMaxCount()
  {
   return 1;
  }
  public int GetFeatureInfoMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetFeatureInfoCount()
  {
   return DomChildCount(NodeType.Element, "", "GetFeatureInfo");
  }
  public int GetFeatureInfoCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "GetFeatureInfo");
   }
  }
  public bool HasGetFeatureInfo()
  {
   return HasDomChild(NodeType.Element, "", "GetFeatureInfo");
  }
  public GetFeatureInfoType GetGetFeatureInfoAt(int index)
  {
   return new GetFeatureInfoType(GetDomChildAt(NodeType.Element, "", "GetFeatureInfo", index));
  }
  public GetFeatureInfoType GetGetFeatureInfo()
  {
   return GetGetFeatureInfoAt(0);
  }
  public GetFeatureInfoType GetFeatureInfo
  {
   get
   {
    return GetGetFeatureInfoAt(0);
   }
  }
  public void RemoveGetFeatureInfoAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "GetFeatureInfo", index);
  }
  public void RemoveGetFeatureInfo()
  {
   while (HasGetFeatureInfo())
    RemoveGetFeatureInfoAt(0);
  }
  public void AddGetFeatureInfo(GetFeatureInfoType newValue)
  {
   AppendDomElement("", "GetFeatureInfo", newValue);
  }
  public void InsertGetFeatureInfoAt(GetFeatureInfoType newValue, int index)
  {
   InsertDomElementAt("", "GetFeatureInfo", index, newValue);
  }
  public void ReplaceGetFeatureInfoAt(GetFeatureInfoType newValue, int index)
  {
   ReplaceDomElementAt("", "GetFeatureInfo", index, newValue);
  }
        public GetFeatureInfoCollection MyGetFeatureInfos = new GetFeatureInfoCollection( );
        public class GetFeatureInfoCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public GetFeatureInfoEnumerator GetEnumerator()
   {
    return new GetFeatureInfoEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class GetFeatureInfoEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public GetFeatureInfoEnumerator(RequestType par)
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
    return(nIndex < parent.GetFeatureInfoCount );
   }
   public GetFeatureInfoType Current
   {
    get
    {
     return(parent.GetGetFeatureInfoAt(nIndex));
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
  public int GetDescribeLayerMinCount()
  {
   return 0;
  }
  public int DescribeLayerMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetDescribeLayerMaxCount()
  {
   return 1;
  }
  public int DescribeLayerMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetDescribeLayerCount()
  {
   return DomChildCount(NodeType.Element, "", "DescribeLayer");
  }
  public int DescribeLayerCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "DescribeLayer");
   }
  }
  public bool HasDescribeLayer()
  {
   return HasDomChild(NodeType.Element, "", "DescribeLayer");
  }
  public DescribeLayerType GetDescribeLayerAt(int index)
  {
   return new DescribeLayerType(GetDomChildAt(NodeType.Element, "", "DescribeLayer", index));
  }
  public DescribeLayerType GetDescribeLayer()
  {
   return GetDescribeLayerAt(0);
  }
  public DescribeLayerType DescribeLayer
  {
   get
   {
    return GetDescribeLayerAt(0);
   }
  }
  public void RemoveDescribeLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "DescribeLayer", index);
  }
  public void RemoveDescribeLayer()
  {
   while (HasDescribeLayer())
    RemoveDescribeLayerAt(0);
  }
  public void AddDescribeLayer(DescribeLayerType newValue)
  {
   AppendDomElement("", "DescribeLayer", newValue);
  }
  public void InsertDescribeLayerAt(DescribeLayerType newValue, int index)
  {
   InsertDomElementAt("", "DescribeLayer", index, newValue);
  }
  public void ReplaceDescribeLayerAt(DescribeLayerType newValue, int index)
  {
   ReplaceDomElementAt("", "DescribeLayer", index, newValue);
  }
        public DescribeLayerCollection MyDescribeLayers = new DescribeLayerCollection( );
        public class DescribeLayerCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public DescribeLayerEnumerator GetEnumerator()
   {
    return new DescribeLayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class DescribeLayerEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public DescribeLayerEnumerator(RequestType par)
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
    return(nIndex < parent.DescribeLayerCount );
   }
   public DescribeLayerType Current
   {
    get
    {
     return(parent.GetDescribeLayerAt(nIndex));
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
  public int GetGetLegendGraphicMinCount()
  {
   return 0;
  }
  public int GetLegendGraphicMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetGetLegendGraphicMaxCount()
  {
   return 1;
  }
  public int GetLegendGraphicMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetLegendGraphicCount()
  {
   return DomChildCount(NodeType.Element, "", "GetLegendGraphic");
  }
  public int GetLegendGraphicCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "GetLegendGraphic");
   }
  }
  public bool HasGetLegendGraphic()
  {
   return HasDomChild(NodeType.Element, "", "GetLegendGraphic");
  }
  public GetLegendGraphicType GetGetLegendGraphicAt(int index)
  {
   return new GetLegendGraphicType(GetDomChildAt(NodeType.Element, "", "GetLegendGraphic", index));
  }
  public GetLegendGraphicType GetGetLegendGraphic()
  {
   return GetGetLegendGraphicAt(0);
  }
  public GetLegendGraphicType GetLegendGraphic
  {
   get
   {
    return GetGetLegendGraphicAt(0);
   }
  }
  public void RemoveGetLegendGraphicAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "GetLegendGraphic", index);
  }
  public void RemoveGetLegendGraphic()
  {
   while (HasGetLegendGraphic())
    RemoveGetLegendGraphicAt(0);
  }
  public void AddGetLegendGraphic(GetLegendGraphicType newValue)
  {
   AppendDomElement("", "GetLegendGraphic", newValue);
  }
  public void InsertGetLegendGraphicAt(GetLegendGraphicType newValue, int index)
  {
   InsertDomElementAt("", "GetLegendGraphic", index, newValue);
  }
  public void ReplaceGetLegendGraphicAt(GetLegendGraphicType newValue, int index)
  {
   ReplaceDomElementAt("", "GetLegendGraphic", index, newValue);
  }
        public GetLegendGraphicCollection MyGetLegendGraphics = new GetLegendGraphicCollection( );
        public class GetLegendGraphicCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public GetLegendGraphicEnumerator GetEnumerator()
   {
    return new GetLegendGraphicEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class GetLegendGraphicEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public GetLegendGraphicEnumerator(RequestType par)
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
    return(nIndex < parent.GetLegendGraphicCount );
   }
   public GetLegendGraphicType Current
   {
    get
    {
     return(parent.GetGetLegendGraphicAt(nIndex));
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
  public int GetGetStylesMinCount()
  {
   return 0;
  }
  public int GetStylesMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetGetStylesMaxCount()
  {
   return 1;
  }
  public int GetStylesMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetGetStylesCount()
  {
   return DomChildCount(NodeType.Element, "", "GetStyles");
  }
  public int GetStylesCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "GetStyles");
   }
  }
  public bool HasGetStyles()
  {
   return HasDomChild(NodeType.Element, "", "GetStyles");
  }
  public GetStylesType GetGetStylesAt(int index)
  {
   return new GetStylesType(GetDomChildAt(NodeType.Element, "", "GetStyles", index));
  }
  public GetStylesType GetGetStyles()
  {
   return GetGetStylesAt(0);
  }
  public GetStylesType GetStyles
  {
   get
   {
    return GetGetStylesAt(0);
   }
  }
  public void RemoveGetStylesAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "GetStyles", index);
  }
  public void RemoveGetStyles()
  {
   while (HasGetStyles())
    RemoveGetStylesAt(0);
  }
  public void AddGetStyles(GetStylesType newValue)
  {
   AppendDomElement("", "GetStyles", newValue);
  }
  public void InsertGetStylesAt(GetStylesType newValue, int index)
  {
   InsertDomElementAt("", "GetStyles", index, newValue);
  }
  public void ReplaceGetStylesAt(GetStylesType newValue, int index)
  {
   ReplaceDomElementAt("", "GetStyles", index, newValue);
  }
        public GetStylesCollection MyGetStyless = new GetStylesCollection( );
        public class GetStylesCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public GetStylesEnumerator GetEnumerator()
   {
    return new GetStylesEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class GetStylesEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public GetStylesEnumerator(RequestType par)
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
    return(nIndex < parent.GetStylesCount );
   }
   public GetStylesType Current
   {
    get
    {
     return(parent.GetGetStylesAt(nIndex));
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
  public int GetPutStylesMinCount()
  {
   return 0;
  }
  public int PutStylesMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetPutStylesMaxCount()
  {
   return 1;
  }
  public int PutStylesMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetPutStylesCount()
  {
   return DomChildCount(NodeType.Element, "", "PutStyles");
  }
  public int PutStylesCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "PutStyles");
   }
  }
  public bool HasPutStyles()
  {
   return HasDomChild(NodeType.Element, "", "PutStyles");
  }
  public PutStylesType GetPutStylesAt(int index)
  {
   return new PutStylesType(GetDomChildAt(NodeType.Element, "", "PutStyles", index));
  }
  public PutStylesType GetPutStyles()
  {
   return GetPutStylesAt(0);
  }
  public PutStylesType PutStyles
  {
   get
   {
    return GetPutStylesAt(0);
   }
  }
  public void RemovePutStylesAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "PutStyles", index);
  }
  public void RemovePutStyles()
  {
   while (HasPutStyles())
    RemovePutStylesAt(0);
  }
  public void AddPutStyles(PutStylesType newValue)
  {
   AppendDomElement("", "PutStyles", newValue);
  }
  public void InsertPutStylesAt(PutStylesType newValue, int index)
  {
   InsertDomElementAt("", "PutStyles", index, newValue);
  }
  public void ReplacePutStylesAt(PutStylesType newValue, int index)
  {
   ReplaceDomElementAt("", "PutStyles", index, newValue);
  }
        public PutStylesCollection MyPutStyless = new PutStylesCollection( );
        public class PutStylesCollection: IEnumerable
        {
            RequestType parent;
            public RequestType Parent
   {
    set
    {
     parent = value;
    }
   }
   public PutStylesEnumerator GetEnumerator()
   {
    return new PutStylesEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class PutStylesEnumerator: IEnumerator
        {
   int nIndex;
   RequestType parent;
   public PutStylesEnumerator(RequestType par)
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
    return(nIndex < parent.PutStylesCount );
   }
   public PutStylesType Current
   {
    get
    {
     return(parent.GetPutStylesAt(nIndex));
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
            MyGetCapabilitiess.Parent = this;
            MyGetMaps.Parent = this;
            MyGetFeatureInfos.Parent = this;
            MyDescribeLayers.Parent = this;
            MyGetLegendGraphics.Parent = this;
            MyGetStyless.Parent = this;
            MyPutStyless.Parent = this;
 }
}
}
