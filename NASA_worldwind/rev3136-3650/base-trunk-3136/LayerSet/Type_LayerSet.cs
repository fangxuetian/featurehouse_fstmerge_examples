using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace LayerSet
{
 public class Type_LayerSet : Altova.Xml.Node
 {
  public Type_LayerSet() : base() { SetCollectionParents(); }
  public Type_LayerSet(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public Type_LayerSet(XmlNode node) : base(node) { SetCollectionParents(); }
  public Type_LayerSet(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "Name"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "Name", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "ShowOnlyOneLayer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "ShowOnlyOneLayer", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "ShowAtStartup"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "ShowAtStartup", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ImageLayer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ImageLayer", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_ImageLayer(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "QuadTileSet"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "QuadTileSet", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_QuadTileSet2(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ShapeFileLayer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ShapeFileLayer", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_ShapeFileLayer2(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "MeshLayer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "MeshLayer", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_MeshLayer2(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "PathList"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "PathList", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_PathList2(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "Icon"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "Icon", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_Icon(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "TiledPlacenameSet"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "TiledPlacenameSet", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_TiledPlacenameSet2(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ChildLayerSet"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ChildLayerSet", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_LayerSet(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ExtendedInformation"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ExtendedInformation", i);
    InternalAdjustPrefix(DOMNode, true);
    new Type_ExtendedInformation(DOMNode).AdjustPrefix();
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
  public NameType2 GetNameAt(int index)
  {
   return new NameType2(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "Name", index)));
  }
  public XmlNode GetStartingNameCursor()
  {
   return GetDomFirstChild( NodeType.Attribute, "", "Name" );
  }
  public XmlNode GetAdvancedNameCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Attribute, "", "Name", curNode );
  }
  public NameType2 GetNameValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new NameType2( curNode.Value );
  }
  public NameType2 GetName()
  {
   return GetNameAt(0);
  }
  public NameType2 Name
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
  public void AddName(NameType2 newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "Name", newValue.ToString());
  }
  public void InsertNameAt(NameType2 newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "Name", index, newValue.ToString());
  }
  public void ReplaceNameAt(NameType2 newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "Name", index, newValue.ToString());
  }
        public NameCollection MyNames = new NameCollection( );
        public class NameCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
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
   Type_LayerSet parent;
   public NameEnumerator(Type_LayerSet par)
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
   public NameType2 Current
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
  public int GetShowOnlyOneLayerMinCount()
  {
   return 1;
  }
  public int ShowOnlyOneLayerMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetShowOnlyOneLayerMaxCount()
  {
   return 1;
  }
  public int ShowOnlyOneLayerMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetShowOnlyOneLayerCount()
  {
   return DomChildCount(NodeType.Attribute, "", "ShowOnlyOneLayer");
  }
  public int ShowOnlyOneLayerCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "ShowOnlyOneLayer");
   }
  }
  public bool HasShowOnlyOneLayer()
  {
   return HasDomChild(NodeType.Attribute, "", "ShowOnlyOneLayer");
  }
  public SchemaBoolean GetShowOnlyOneLayerAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "ShowOnlyOneLayer", index)));
  }
  public XmlNode GetStartingShowOnlyOneLayerCursor()
  {
   return GetDomFirstChild( NodeType.Attribute, "", "ShowOnlyOneLayer" );
  }
  public XmlNode GetAdvancedShowOnlyOneLayerCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Attribute, "", "ShowOnlyOneLayer", curNode );
  }
  public SchemaBoolean GetShowOnlyOneLayerValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaBoolean( curNode.Value );
  }
  public SchemaBoolean GetShowOnlyOneLayer()
  {
   return GetShowOnlyOneLayerAt(0);
  }
  public SchemaBoolean ShowOnlyOneLayer
  {
   get
   {
    return GetShowOnlyOneLayerAt(0);
   }
  }
  public void RemoveShowOnlyOneLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "ShowOnlyOneLayer", index);
  }
  public void RemoveShowOnlyOneLayer()
  {
   while (HasShowOnlyOneLayer())
    RemoveShowOnlyOneLayerAt(0);
  }
  public void AddShowOnlyOneLayer(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "ShowOnlyOneLayer", newValue.ToString());
  }
  public void InsertShowOnlyOneLayerAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "ShowOnlyOneLayer", index, newValue.ToString());
  }
  public void ReplaceShowOnlyOneLayerAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "ShowOnlyOneLayer", index, newValue.ToString());
  }
        public ShowOnlyOneLayerCollection MyShowOnlyOneLayers = new ShowOnlyOneLayerCollection( );
        public class ShowOnlyOneLayerCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public ShowOnlyOneLayerEnumerator GetEnumerator()
   {
    return new ShowOnlyOneLayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ShowOnlyOneLayerEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public ShowOnlyOneLayerEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.ShowOnlyOneLayerCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetShowOnlyOneLayerAt(nIndex));
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
  public int GetShowAtStartupMinCount()
  {
   return 1;
  }
  public int ShowAtStartupMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetShowAtStartupMaxCount()
  {
   return 1;
  }
  public int ShowAtStartupMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetShowAtStartupCount()
  {
   return DomChildCount(NodeType.Attribute, "", "ShowAtStartup");
  }
  public int ShowAtStartupCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "ShowAtStartup");
   }
  }
  public bool HasShowAtStartup()
  {
   return HasDomChild(NodeType.Attribute, "", "ShowAtStartup");
  }
  public SchemaBoolean GetShowAtStartupAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "ShowAtStartup", index)));
  }
  public XmlNode GetStartingShowAtStartupCursor()
  {
   return GetDomFirstChild( NodeType.Attribute, "", "ShowAtStartup" );
  }
  public XmlNode GetAdvancedShowAtStartupCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Attribute, "", "ShowAtStartup", curNode );
  }
  public SchemaBoolean GetShowAtStartupValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new SchemaBoolean( curNode.Value );
  }
  public SchemaBoolean GetShowAtStartup()
  {
   return GetShowAtStartupAt(0);
  }
  public SchemaBoolean ShowAtStartup
  {
   get
   {
    return GetShowAtStartupAt(0);
   }
  }
  public void RemoveShowAtStartupAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "ShowAtStartup", index);
  }
  public void RemoveShowAtStartup()
  {
   while (HasShowAtStartup())
    RemoveShowAtStartupAt(0);
  }
  public void AddShowAtStartup(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "ShowAtStartup", newValue.ToString());
  }
  public void InsertShowAtStartupAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "ShowAtStartup", index, newValue.ToString());
  }
  public void ReplaceShowAtStartupAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "ShowAtStartup", index, newValue.ToString());
  }
        public ShowAtStartupCollection MyShowAtStartups = new ShowAtStartupCollection( );
        public class ShowAtStartupCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public ShowAtStartupEnumerator GetEnumerator()
   {
    return new ShowAtStartupEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ShowAtStartupEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public ShowAtStartupEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.ShowAtStartupCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetShowAtStartupAt(nIndex));
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
  public int GetImageLayerMinCount()
  {
   return 0;
  }
  public int ImageLayerMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetImageLayerMaxCount()
  {
   return Int32.MaxValue;
  }
  public int ImageLayerMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetImageLayerCount()
  {
   return DomChildCount(NodeType.Element, "", "ImageLayer");
  }
  public int ImageLayerCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ImageLayer");
   }
  }
  public bool HasImageLayer()
  {
   return HasDomChild(NodeType.Element, "", "ImageLayer");
  }
  public Type_ImageLayer GetImageLayerAt(int index)
  {
   return new Type_ImageLayer(GetDomChildAt(NodeType.Element, "", "ImageLayer", index));
  }
  public XmlNode GetStartingImageLayerCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "ImageLayer" );
  }
  public XmlNode GetAdvancedImageLayerCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "ImageLayer", curNode );
  }
  public Type_ImageLayer GetImageLayerValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_ImageLayer( curNode );
  }
  public Type_ImageLayer GetImageLayer()
  {
   return GetImageLayerAt(0);
  }
  public Type_ImageLayer ImageLayer
  {
   get
   {
    return GetImageLayerAt(0);
   }
  }
  public void RemoveImageLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ImageLayer", index);
  }
  public void RemoveImageLayer()
  {
   while (HasImageLayer())
    RemoveImageLayerAt(0);
  }
  public void AddImageLayer(Type_ImageLayer newValue)
  {
   AppendDomElement("", "ImageLayer", newValue);
  }
  public void InsertImageLayerAt(Type_ImageLayer newValue, int index)
  {
   InsertDomElementAt("", "ImageLayer", index, newValue);
  }
  public void ReplaceImageLayerAt(Type_ImageLayer newValue, int index)
  {
   ReplaceDomElementAt("", "ImageLayer", index, newValue);
  }
        public ImageLayerCollection MyImageLayers = new ImageLayerCollection( );
        public class ImageLayerCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public ImageLayerEnumerator GetEnumerator()
   {
    return new ImageLayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ImageLayerEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public ImageLayerEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.ImageLayerCount );
   }
   public Type_ImageLayer Current
   {
    get
    {
     return(parent.GetImageLayerAt(nIndex));
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
  public int GetQuadTileSetMinCount()
  {
   return 0;
  }
  public int QuadTileSetMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetQuadTileSetMaxCount()
  {
   return Int32.MaxValue;
  }
  public int QuadTileSetMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetQuadTileSetCount()
  {
   return DomChildCount(NodeType.Element, "", "QuadTileSet");
  }
  public int QuadTileSetCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "QuadTileSet");
   }
  }
  public bool HasQuadTileSet()
  {
   return HasDomChild(NodeType.Element, "", "QuadTileSet");
  }
  public Type_QuadTileSet2 GetQuadTileSetAt(int index)
  {
   return new Type_QuadTileSet2(GetDomChildAt(NodeType.Element, "", "QuadTileSet", index));
  }
  public XmlNode GetStartingQuadTileSetCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "QuadTileSet" );
  }
  public XmlNode GetAdvancedQuadTileSetCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "QuadTileSet", curNode );
  }
  public Type_QuadTileSet2 GetQuadTileSetValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_QuadTileSet2( curNode );
  }
  public Type_QuadTileSet2 GetQuadTileSet()
  {
   return GetQuadTileSetAt(0);
  }
  public Type_QuadTileSet2 QuadTileSet
  {
   get
   {
    return GetQuadTileSetAt(0);
   }
  }
  public void RemoveQuadTileSetAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "QuadTileSet", index);
  }
  public void RemoveQuadTileSet()
  {
   while (HasQuadTileSet())
    RemoveQuadTileSetAt(0);
  }
  public void AddQuadTileSet(Type_QuadTileSet2 newValue)
  {
   AppendDomElement("", "QuadTileSet", newValue);
  }
  public void InsertQuadTileSetAt(Type_QuadTileSet2 newValue, int index)
  {
   InsertDomElementAt("", "QuadTileSet", index, newValue);
  }
  public void ReplaceQuadTileSetAt(Type_QuadTileSet2 newValue, int index)
  {
   ReplaceDomElementAt("", "QuadTileSet", index, newValue);
  }
        public QuadTileSetCollection MyQuadTileSets = new QuadTileSetCollection( );
        public class QuadTileSetCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public QuadTileSetEnumerator GetEnumerator()
   {
    return new QuadTileSetEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class QuadTileSetEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public QuadTileSetEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.QuadTileSetCount );
   }
   public Type_QuadTileSet2 Current
   {
    get
    {
     return(parent.GetQuadTileSetAt(nIndex));
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
  public int GetShapeFileLayerMinCount()
  {
   return 0;
  }
  public int ShapeFileLayerMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetShapeFileLayerMaxCount()
  {
   return Int32.MaxValue;
  }
  public int ShapeFileLayerMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetShapeFileLayerCount()
  {
   return DomChildCount(NodeType.Element, "", "ShapeFileLayer");
  }
  public int ShapeFileLayerCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ShapeFileLayer");
   }
  }
  public bool HasShapeFileLayer()
  {
   return HasDomChild(NodeType.Element, "", "ShapeFileLayer");
  }
  public Type_ShapeFileLayer2 GetShapeFileLayerAt(int index)
  {
   return new Type_ShapeFileLayer2(GetDomChildAt(NodeType.Element, "", "ShapeFileLayer", index));
  }
  public XmlNode GetStartingShapeFileLayerCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "ShapeFileLayer" );
  }
  public XmlNode GetAdvancedShapeFileLayerCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "ShapeFileLayer", curNode );
  }
  public Type_ShapeFileLayer2 GetShapeFileLayerValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_ShapeFileLayer2( curNode );
  }
  public Type_ShapeFileLayer2 GetShapeFileLayer()
  {
   return GetShapeFileLayerAt(0);
  }
  public Type_ShapeFileLayer2 ShapeFileLayer
  {
   get
   {
    return GetShapeFileLayerAt(0);
   }
  }
  public void RemoveShapeFileLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ShapeFileLayer", index);
  }
  public void RemoveShapeFileLayer()
  {
   while (HasShapeFileLayer())
    RemoveShapeFileLayerAt(0);
  }
  public void AddShapeFileLayer(Type_ShapeFileLayer2 newValue)
  {
   AppendDomElement("", "ShapeFileLayer", newValue);
  }
  public void InsertShapeFileLayerAt(Type_ShapeFileLayer2 newValue, int index)
  {
   InsertDomElementAt("", "ShapeFileLayer", index, newValue);
  }
  public void ReplaceShapeFileLayerAt(Type_ShapeFileLayer2 newValue, int index)
  {
   ReplaceDomElementAt("", "ShapeFileLayer", index, newValue);
  }
        public ShapeFileLayerCollection MyShapeFileLayers = new ShapeFileLayerCollection( );
        public class ShapeFileLayerCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public ShapeFileLayerEnumerator GetEnumerator()
   {
    return new ShapeFileLayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ShapeFileLayerEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public ShapeFileLayerEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.ShapeFileLayerCount );
   }
   public Type_ShapeFileLayer2 Current
   {
    get
    {
     return(parent.GetShapeFileLayerAt(nIndex));
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
  public int GetMeshLayerMinCount()
  {
   return 0;
  }
  public int MeshLayerMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetMeshLayerMaxCount()
  {
   return Int32.MaxValue;
  }
  public int MeshLayerMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetMeshLayerCount()
  {
   return DomChildCount(NodeType.Element, "", "MeshLayer");
  }
  public int MeshLayerCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "MeshLayer");
   }
  }
  public bool HasMeshLayer()
  {
   return HasDomChild(NodeType.Element, "", "MeshLayer");
  }
  public Type_MeshLayer2 GetMeshLayerAt(int index)
  {
   return new Type_MeshLayer2(GetDomChildAt(NodeType.Element, "", "MeshLayer", index));
  }
  public XmlNode GetStartingMeshLayerCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "MeshLayer" );
  }
  public XmlNode GetAdvancedMeshLayerCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "MeshLayer", curNode );
  }
  public Type_MeshLayer2 GetMeshLayerValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_MeshLayer2( curNode );
  }
  public Type_MeshLayer2 GetMeshLayer()
  {
   return GetMeshLayerAt(0);
  }
  public Type_MeshLayer2 MeshLayer
  {
   get
   {
    return GetMeshLayerAt(0);
   }
  }
  public void RemoveMeshLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "MeshLayer", index);
  }
  public void RemoveMeshLayer()
  {
   while (HasMeshLayer())
    RemoveMeshLayerAt(0);
  }
  public void AddMeshLayer(Type_MeshLayer2 newValue)
  {
   AppendDomElement("", "MeshLayer", newValue);
  }
  public void InsertMeshLayerAt(Type_MeshLayer2 newValue, int index)
  {
   InsertDomElementAt("", "MeshLayer", index, newValue);
  }
  public void ReplaceMeshLayerAt(Type_MeshLayer2 newValue, int index)
  {
   ReplaceDomElementAt("", "MeshLayer", index, newValue);
  }
        public MeshLayerCollection MyMeshLayers = new MeshLayerCollection( );
        public class MeshLayerCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public MeshLayerEnumerator GetEnumerator()
   {
    return new MeshLayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class MeshLayerEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public MeshLayerEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.MeshLayerCount );
   }
   public Type_MeshLayer2 Current
   {
    get
    {
     return(parent.GetMeshLayerAt(nIndex));
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
  public int GetPathListMinCount()
  {
   return 0;
  }
  public int PathListMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetPathListMaxCount()
  {
   return Int32.MaxValue;
  }
  public int PathListMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetPathListCount()
  {
   return DomChildCount(NodeType.Element, "", "PathList");
  }
  public int PathListCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "PathList");
   }
  }
  public bool HasPathList()
  {
   return HasDomChild(NodeType.Element, "", "PathList");
  }
  public Type_PathList2 GetPathListAt(int index)
  {
   return new Type_PathList2(GetDomChildAt(NodeType.Element, "", "PathList", index));
  }
  public XmlNode GetStartingPathListCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "PathList" );
  }
  public XmlNode GetAdvancedPathListCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "PathList", curNode );
  }
  public Type_PathList2 GetPathListValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_PathList2( curNode );
  }
  public Type_PathList2 GetPathList()
  {
   return GetPathListAt(0);
  }
  public Type_PathList2 PathList
  {
   get
   {
    return GetPathListAt(0);
   }
  }
  public void RemovePathListAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "PathList", index);
  }
  public void RemovePathList()
  {
   while (HasPathList())
    RemovePathListAt(0);
  }
  public void AddPathList(Type_PathList2 newValue)
  {
   AppendDomElement("", "PathList", newValue);
  }
  public void InsertPathListAt(Type_PathList2 newValue, int index)
  {
   InsertDomElementAt("", "PathList", index, newValue);
  }
  public void ReplacePathListAt(Type_PathList2 newValue, int index)
  {
   ReplaceDomElementAt("", "PathList", index, newValue);
  }
        public PathListCollection MyPathLists = new PathListCollection( );
        public class PathListCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public PathListEnumerator GetEnumerator()
   {
    return new PathListEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class PathListEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public PathListEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.PathListCount );
   }
   public Type_PathList2 Current
   {
    get
    {
     return(parent.GetPathListAt(nIndex));
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
  public int GetIconMinCount()
  {
   return 0;
  }
  public int IconMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetIconMaxCount()
  {
   return Int32.MaxValue;
  }
  public int IconMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetIconCount()
  {
   return DomChildCount(NodeType.Element, "", "Icon");
  }
  public int IconCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "Icon");
   }
  }
  public bool HasIcon()
  {
   return HasDomChild(NodeType.Element, "", "Icon");
  }
  public Type_Icon GetIconAt(int index)
  {
   return new Type_Icon(GetDomChildAt(NodeType.Element, "", "Icon", index));
  }
  public XmlNode GetStartingIconCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "Icon" );
  }
  public XmlNode GetAdvancedIconCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "Icon", curNode );
  }
  public Type_Icon GetIconValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_Icon( curNode );
  }
  public Type_Icon GetIcon()
  {
   return GetIconAt(0);
  }
  public Type_Icon Icon
  {
   get
   {
    return GetIconAt(0);
   }
  }
  public void RemoveIconAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "Icon", index);
  }
  public void RemoveIcon()
  {
   while (HasIcon())
    RemoveIconAt(0);
  }
  public void AddIcon(Type_Icon newValue)
  {
   AppendDomElement("", "Icon", newValue);
  }
  public void InsertIconAt(Type_Icon newValue, int index)
  {
   InsertDomElementAt("", "Icon", index, newValue);
  }
  public void ReplaceIconAt(Type_Icon newValue, int index)
  {
   ReplaceDomElementAt("", "Icon", index, newValue);
  }
        public IconCollection MyIcons = new IconCollection( );
        public class IconCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public IconEnumerator GetEnumerator()
   {
    return new IconEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class IconEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public IconEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.IconCount );
   }
   public Type_Icon Current
   {
    get
    {
     return(parent.GetIconAt(nIndex));
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
  public int GetTiledPlacenameSetMinCount()
  {
   return 0;
  }
  public int TiledPlacenameSetMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetTiledPlacenameSetMaxCount()
  {
   return Int32.MaxValue;
  }
  public int TiledPlacenameSetMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetTiledPlacenameSetCount()
  {
   return DomChildCount(NodeType.Element, "", "TiledPlacenameSet");
  }
  public int TiledPlacenameSetCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "TiledPlacenameSet");
   }
  }
  public bool HasTiledPlacenameSet()
  {
   return HasDomChild(NodeType.Element, "", "TiledPlacenameSet");
  }
  public Type_TiledPlacenameSet2 GetTiledPlacenameSetAt(int index)
  {
   return new Type_TiledPlacenameSet2(GetDomChildAt(NodeType.Element, "", "TiledPlacenameSet", index));
  }
  public XmlNode GetStartingTiledPlacenameSetCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "TiledPlacenameSet" );
  }
  public XmlNode GetAdvancedTiledPlacenameSetCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "TiledPlacenameSet", curNode );
  }
  public Type_TiledPlacenameSet2 GetTiledPlacenameSetValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_TiledPlacenameSet2( curNode );
  }
  public Type_TiledPlacenameSet2 GetTiledPlacenameSet()
  {
   return GetTiledPlacenameSetAt(0);
  }
  public Type_TiledPlacenameSet2 TiledPlacenameSet
  {
   get
   {
    return GetTiledPlacenameSetAt(0);
   }
  }
  public void RemoveTiledPlacenameSetAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "TiledPlacenameSet", index);
  }
  public void RemoveTiledPlacenameSet()
  {
   while (HasTiledPlacenameSet())
    RemoveTiledPlacenameSetAt(0);
  }
  public void AddTiledPlacenameSet(Type_TiledPlacenameSet2 newValue)
  {
   AppendDomElement("", "TiledPlacenameSet", newValue);
  }
  public void InsertTiledPlacenameSetAt(Type_TiledPlacenameSet2 newValue, int index)
  {
   InsertDomElementAt("", "TiledPlacenameSet", index, newValue);
  }
  public void ReplaceTiledPlacenameSetAt(Type_TiledPlacenameSet2 newValue, int index)
  {
   ReplaceDomElementAt("", "TiledPlacenameSet", index, newValue);
  }
        public TiledPlacenameSetCollection MyTiledPlacenameSets = new TiledPlacenameSetCollection( );
        public class TiledPlacenameSetCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public TiledPlacenameSetEnumerator GetEnumerator()
   {
    return new TiledPlacenameSetEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class TiledPlacenameSetEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public TiledPlacenameSetEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.TiledPlacenameSetCount );
   }
   public Type_TiledPlacenameSet2 Current
   {
    get
    {
     return(parent.GetTiledPlacenameSetAt(nIndex));
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
  public int GetChildLayerSetMinCount()
  {
   return 0;
  }
  public int ChildLayerSetMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetChildLayerSetMaxCount()
  {
   return Int32.MaxValue;
  }
  public int ChildLayerSetMaxCount
  {
   get
   {
    return Int32.MaxValue;
   }
  }
  public int GetChildLayerSetCount()
  {
   return DomChildCount(NodeType.Element, "", "ChildLayerSet");
  }
  public int ChildLayerSetCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ChildLayerSet");
   }
  }
  public bool HasChildLayerSet()
  {
   return HasDomChild(NodeType.Element, "", "ChildLayerSet");
  }
  public Type_LayerSet GetChildLayerSetAt(int index)
  {
   return new Type_LayerSet(GetDomChildAt(NodeType.Element, "", "ChildLayerSet", index));
  }
  public XmlNode GetStartingChildLayerSetCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "ChildLayerSet" );
  }
  public XmlNode GetAdvancedChildLayerSetCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "ChildLayerSet", curNode );
  }
  public Type_LayerSet GetChildLayerSetValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_LayerSet( curNode );
  }
  public Type_LayerSet GetChildLayerSet()
  {
   return GetChildLayerSetAt(0);
  }
  public Type_LayerSet ChildLayerSet
  {
   get
   {
    return GetChildLayerSetAt(0);
   }
  }
  public void RemoveChildLayerSetAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ChildLayerSet", index);
  }
  public void RemoveChildLayerSet()
  {
   while (HasChildLayerSet())
    RemoveChildLayerSetAt(0);
  }
  public void AddChildLayerSet(Type_LayerSet newValue)
  {
   AppendDomElement("", "ChildLayerSet", newValue);
  }
  public void InsertChildLayerSetAt(Type_LayerSet newValue, int index)
  {
   InsertDomElementAt("", "ChildLayerSet", index, newValue);
  }
  public void ReplaceChildLayerSetAt(Type_LayerSet newValue, int index)
  {
   ReplaceDomElementAt("", "ChildLayerSet", index, newValue);
  }
        public ChildLayerSetCollection MyChildLayerSets = new ChildLayerSetCollection( );
        public class ChildLayerSetCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public ChildLayerSetEnumerator GetEnumerator()
   {
    return new ChildLayerSetEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ChildLayerSetEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public ChildLayerSetEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.ChildLayerSetCount );
   }
   public Type_LayerSet Current
   {
    get
    {
     return(parent.GetChildLayerSetAt(nIndex));
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
  public int GetExtendedInformationMinCount()
  {
   return 0;
  }
  public int ExtendedInformationMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetExtendedInformationMaxCount()
  {
   return 1;
  }
  public int ExtendedInformationMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetExtendedInformationCount()
  {
   return DomChildCount(NodeType.Element, "", "ExtendedInformation");
  }
  public int ExtendedInformationCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ExtendedInformation");
   }
  }
  public bool HasExtendedInformation()
  {
   return HasDomChild(NodeType.Element, "", "ExtendedInformation");
  }
  public Type_ExtendedInformation GetExtendedInformationAt(int index)
  {
   return new Type_ExtendedInformation(GetDomChildAt(NodeType.Element, "", "ExtendedInformation", index));
  }
  public XmlNode GetStartingExtendedInformationCursor()
  {
   return GetDomFirstChild( NodeType.Element, "", "ExtendedInformation" );
  }
  public XmlNode GetAdvancedExtendedInformationCursor( XmlNode curNode )
  {
   return GetDomNextChild( NodeType.Element, "", "ExtendedInformation", curNode );
  }
  public Type_ExtendedInformation GetExtendedInformationValueAtCursor( XmlNode curNode )
  {
   if( curNode == null )
      throw new Altova.Xml.XmlException("Out of range");
   else
    return new Type_ExtendedInformation( curNode );
  }
  public Type_ExtendedInformation GetExtendedInformation()
  {
   return GetExtendedInformationAt(0);
  }
  public Type_ExtendedInformation ExtendedInformation
  {
   get
   {
    return GetExtendedInformationAt(0);
   }
  }
  public void RemoveExtendedInformationAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ExtendedInformation", index);
  }
  public void RemoveExtendedInformation()
  {
   while (HasExtendedInformation())
    RemoveExtendedInformationAt(0);
  }
  public void AddExtendedInformation(Type_ExtendedInformation newValue)
  {
   AppendDomElement("", "ExtendedInformation", newValue);
  }
  public void InsertExtendedInformationAt(Type_ExtendedInformation newValue, int index)
  {
   InsertDomElementAt("", "ExtendedInformation", index, newValue);
  }
  public void ReplaceExtendedInformationAt(Type_ExtendedInformation newValue, int index)
  {
   ReplaceDomElementAt("", "ExtendedInformation", index, newValue);
  }
        public ExtendedInformationCollection MyExtendedInformations = new ExtendedInformationCollection( );
        public class ExtendedInformationCollection: IEnumerable
        {
            Type_LayerSet parent;
            public Type_LayerSet Parent
   {
    set
    {
     parent = value;
    }
   }
   public ExtendedInformationEnumerator GetEnumerator()
   {
    return new ExtendedInformationEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ExtendedInformationEnumerator: IEnumerator
        {
   int nIndex;
   Type_LayerSet parent;
   public ExtendedInformationEnumerator(Type_LayerSet par)
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
    return(nIndex < parent.ExtendedInformationCount );
   }
   public Type_ExtendedInformation Current
   {
    get
    {
     return(parent.GetExtendedInformationAt(nIndex));
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
            MyShowOnlyOneLayers.Parent = this;
            MyShowAtStartups.Parent = this;
            MyImageLayers.Parent = this;
            MyQuadTileSets.Parent = this;
            MyShapeFileLayers.Parent = this;
            MyMeshLayers.Parent = this;
            MyPathLists.Parent = this;
            MyIcons.Parent = this;
            MyTiledPlacenameSets.Parent = this;
            MyChildLayerSets.Parent = this;
            MyExtendedInformations.Parent = this;
 }
}
}
