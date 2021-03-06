using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_1_1
{
 public class UserDefinedSymbolizationType : Altova.Xml.Node
 {
  public UserDefinedSymbolizationType() : base() { SetCollectionParents(); }
  public UserDefinedSymbolizationType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public UserDefinedSymbolizationType(XmlNode node) : base(node) { SetCollectionParents(); }
  public UserDefinedSymbolizationType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "SupportSLD"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "SupportSLD", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "UserLayer"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "UserLayer", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "UserStyle"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "UserStyle", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "RemoteWFS"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "RemoteWFS", i);
    InternalAdjustPrefix(DOMNode, false);
   }
  }
  public int GetSupportSLDMinCount()
  {
   return 0;
  }
  public int SupportSLDMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetSupportSLDMaxCount()
  {
   return 1;
  }
  public int SupportSLDMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetSupportSLDCount()
  {
   return DomChildCount(NodeType.Attribute, "", "SupportSLD");
  }
  public int SupportSLDCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "SupportSLD");
   }
  }
  public bool HasSupportSLD()
  {
   return HasDomChild(NodeType.Attribute, "", "SupportSLD");
  }
  public SchemaString GetSupportSLDAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "SupportSLD", index)));
  }
  public SchemaString GetSupportSLD()
  {
   return GetSupportSLDAt(0);
  }
  public SchemaString SupportSLD
  {
   get
   {
    return GetSupportSLDAt(0);
   }
  }
  public void RemoveSupportSLDAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "SupportSLD", index);
  }
  public void RemoveSupportSLD()
  {
   while (HasSupportSLD())
    RemoveSupportSLDAt(0);
  }
  public void AddSupportSLD(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "SupportSLD", newValue.ToString());
  }
  public void InsertSupportSLDAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "SupportSLD", index, newValue.ToString());
  }
  public void ReplaceSupportSLDAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "SupportSLD", index, newValue.ToString());
  }
        public SupportSLDCollection MySupportSLDs = new SupportSLDCollection( );
        public class SupportSLDCollection: IEnumerable
        {
            UserDefinedSymbolizationType parent;
            public UserDefinedSymbolizationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public SupportSLDEnumerator GetEnumerator()
   {
    return new SupportSLDEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class SupportSLDEnumerator: IEnumerator
        {
   int nIndex;
   UserDefinedSymbolizationType parent;
   public SupportSLDEnumerator(UserDefinedSymbolizationType par)
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
    return(nIndex < parent.SupportSLDCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetSupportSLDAt(nIndex));
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
  public int GetUserLayerMinCount()
  {
   return 0;
  }
  public int UserLayerMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetUserLayerMaxCount()
  {
   return 1;
  }
  public int UserLayerMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetUserLayerCount()
  {
   return DomChildCount(NodeType.Attribute, "", "UserLayer");
  }
  public int UserLayerCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "UserLayer");
   }
  }
  public bool HasUserLayer()
  {
   return HasDomChild(NodeType.Attribute, "", "UserLayer");
  }
  public SchemaString GetUserLayerAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "UserLayer", index)));
  }
  public SchemaString GetUserLayer()
  {
   return GetUserLayerAt(0);
  }
  public SchemaString UserLayer
  {
   get
   {
    return GetUserLayerAt(0);
   }
  }
  public void RemoveUserLayerAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "UserLayer", index);
  }
  public void RemoveUserLayer()
  {
   while (HasUserLayer())
    RemoveUserLayerAt(0);
  }
  public void AddUserLayer(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "UserLayer", newValue.ToString());
  }
  public void InsertUserLayerAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "UserLayer", index, newValue.ToString());
  }
  public void ReplaceUserLayerAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "UserLayer", index, newValue.ToString());
  }
        public UserLayerCollection MyUserLayers = new UserLayerCollection( );
        public class UserLayerCollection: IEnumerable
        {
            UserDefinedSymbolizationType parent;
            public UserDefinedSymbolizationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public UserLayerEnumerator GetEnumerator()
   {
    return new UserLayerEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class UserLayerEnumerator: IEnumerator
        {
   int nIndex;
   UserDefinedSymbolizationType parent;
   public UserLayerEnumerator(UserDefinedSymbolizationType par)
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
    return(nIndex < parent.UserLayerCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetUserLayerAt(nIndex));
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
  public int GetUserStyleMinCount()
  {
   return 0;
  }
  public int UserStyleMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetUserStyleMaxCount()
  {
   return 1;
  }
  public int UserStyleMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetUserStyleCount()
  {
   return DomChildCount(NodeType.Attribute, "", "UserStyle");
  }
  public int UserStyleCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "UserStyle");
   }
  }
  public bool HasUserStyle()
  {
   return HasDomChild(NodeType.Attribute, "", "UserStyle");
  }
  public SchemaString GetUserStyleAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "UserStyle", index)));
  }
  public SchemaString GetUserStyle()
  {
   return GetUserStyleAt(0);
  }
  public SchemaString UserStyle
  {
   get
   {
    return GetUserStyleAt(0);
   }
  }
  public void RemoveUserStyleAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "UserStyle", index);
  }
  public void RemoveUserStyle()
  {
   while (HasUserStyle())
    RemoveUserStyleAt(0);
  }
  public void AddUserStyle(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "UserStyle", newValue.ToString());
  }
  public void InsertUserStyleAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "UserStyle", index, newValue.ToString());
  }
  public void ReplaceUserStyleAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "UserStyle", index, newValue.ToString());
  }
        public UserStyleCollection MyUserStyles = new UserStyleCollection( );
        public class UserStyleCollection: IEnumerable
        {
            UserDefinedSymbolizationType parent;
            public UserDefinedSymbolizationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public UserStyleEnumerator GetEnumerator()
   {
    return new UserStyleEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class UserStyleEnumerator: IEnumerator
        {
   int nIndex;
   UserDefinedSymbolizationType parent;
   public UserStyleEnumerator(UserDefinedSymbolizationType par)
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
    return(nIndex < parent.UserStyleCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetUserStyleAt(nIndex));
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
  public int GetRemoteWFSMinCount()
  {
   return 0;
  }
  public int RemoteWFSMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetRemoteWFSMaxCount()
  {
   return 1;
  }
  public int RemoteWFSMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetRemoteWFSCount()
  {
   return DomChildCount(NodeType.Attribute, "", "RemoteWFS");
  }
  public int RemoteWFSCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "RemoteWFS");
   }
  }
  public bool HasRemoteWFS()
  {
   return HasDomChild(NodeType.Attribute, "", "RemoteWFS");
  }
  public SchemaString GetRemoteWFSAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "RemoteWFS", index)));
  }
  public SchemaString GetRemoteWFS()
  {
   return GetRemoteWFSAt(0);
  }
  public SchemaString RemoteWFS
  {
   get
   {
    return GetRemoteWFSAt(0);
   }
  }
  public void RemoveRemoteWFSAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "RemoteWFS", index);
  }
  public void RemoveRemoteWFS()
  {
   while (HasRemoteWFS())
    RemoveRemoteWFSAt(0);
  }
  public void AddRemoteWFS(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "RemoteWFS", newValue.ToString());
  }
  public void InsertRemoteWFSAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "RemoteWFS", index, newValue.ToString());
  }
  public void ReplaceRemoteWFSAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "RemoteWFS", index, newValue.ToString());
  }
        public RemoteWFSCollection MyRemoteWFSs = new RemoteWFSCollection( );
        public class RemoteWFSCollection: IEnumerable
        {
            UserDefinedSymbolizationType parent;
            public UserDefinedSymbolizationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public RemoteWFSEnumerator GetEnumerator()
   {
    return new RemoteWFSEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class RemoteWFSEnumerator: IEnumerator
        {
   int nIndex;
   UserDefinedSymbolizationType parent;
   public RemoteWFSEnumerator(UserDefinedSymbolizationType par)
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
    return(nIndex < parent.RemoteWFSCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetRemoteWFSAt(nIndex));
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
            MySupportSLDs.Parent = this;
            MyUserLayers.Parent = this;
            MyUserStyles.Parent = this;
            MyRemoteWFSs.Parent = this;
 }
}
}
