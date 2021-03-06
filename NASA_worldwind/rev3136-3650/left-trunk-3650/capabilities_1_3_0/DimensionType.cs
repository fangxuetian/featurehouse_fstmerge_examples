using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_3_0.wms
{
 public class DimensionType : Altova.Xml.Node
 {
  public DimensionType() : base() { SetCollectionParents(); }
  public DimensionType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public DimensionType(XmlNode node) : base(node) { SetCollectionParents(); }
  public DimensionType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public SchemaString GetValue()
  {
   return new SchemaString(GetDomNodeValue(domNode));
  }
  public void SetValue(ISchemaType newValue)
  {
   SetDomNodeValue(domNode, newValue.ToString());
  }
  public SchemaString Value
  {
   get
   {
    return new SchemaString(GetDomNodeValue(domNode));
   }
   set
   {
    SetDomNodeValue(domNode, value.ToString());
   }
  }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "name"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "name", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "units"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "units", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "unitSymbol"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "unitSymbol", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "default"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "default", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "multipleValues"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "multipleValues", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "nearestValue"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "nearestValue", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Attribute, "", "current"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Attribute, "", "current", i);
    InternalAdjustPrefix(DOMNode, false);
   }
  }
  public int GetnameMinCount()
  {
   return 1;
  }
  public int nameMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetnameMaxCount()
  {
   return 1;
  }
  public int nameMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetnameCount()
  {
   return DomChildCount(NodeType.Attribute, "", "name");
  }
  public int nameCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "name");
   }
  }
  public bool Hasname()
  {
   return HasDomChild(NodeType.Attribute, "", "name");
  }
  public SchemaString GetnameAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "name", index)));
  }
  public SchemaString Getname()
  {
   return GetnameAt(0);
  }
  public SchemaString name
  {
   get
   {
    return GetnameAt(0);
   }
  }
  public void RemovenameAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "name", index);
  }
  public void Removename()
  {
   while (Hasname())
    RemovenameAt(0);
  }
  public void Addname(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "name", newValue.ToString());
  }
  public void InsertnameAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "name", index, newValue.ToString());
  }
  public void ReplacenameAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "name", index, newValue.ToString());
  }
        public nameCollection Mynames = new nameCollection( );
        public class nameCollection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public nameEnumerator GetEnumerator()
   {
    return new nameEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class nameEnumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public nameEnumerator(DimensionType par)
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
    return(nIndex < parent.nameCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetnameAt(nIndex));
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
  public int GetunitsMinCount()
  {
   return 1;
  }
  public int unitsMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetunitsMaxCount()
  {
   return 1;
  }
  public int unitsMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetunitsCount()
  {
   return DomChildCount(NodeType.Attribute, "", "units");
  }
  public int unitsCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "units");
   }
  }
  public bool Hasunits()
  {
   return HasDomChild(NodeType.Attribute, "", "units");
  }
  public SchemaString GetunitsAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "units", index)));
  }
  public SchemaString Getunits()
  {
   return GetunitsAt(0);
  }
  public SchemaString units
  {
   get
   {
    return GetunitsAt(0);
   }
  }
  public void RemoveunitsAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "units", index);
  }
  public void Removeunits()
  {
   while (Hasunits())
    RemoveunitsAt(0);
  }
  public void Addunits(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "units", newValue.ToString());
  }
  public void InsertunitsAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "units", index, newValue.ToString());
  }
  public void ReplaceunitsAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "units", index, newValue.ToString());
  }
        public unitsCollection Myunitss = new unitsCollection( );
        public class unitsCollection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public unitsEnumerator GetEnumerator()
   {
    return new unitsEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class unitsEnumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public unitsEnumerator(DimensionType par)
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
    return(nIndex < parent.unitsCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetunitsAt(nIndex));
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
  public int GetunitSymbolMinCount()
  {
   return 0;
  }
  public int unitSymbolMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetunitSymbolMaxCount()
  {
   return 1;
  }
  public int unitSymbolMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetunitSymbolCount()
  {
   return DomChildCount(NodeType.Attribute, "", "unitSymbol");
  }
  public int unitSymbolCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "unitSymbol");
   }
  }
  public bool HasunitSymbol()
  {
   return HasDomChild(NodeType.Attribute, "", "unitSymbol");
  }
  public SchemaString GetunitSymbolAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "unitSymbol", index)));
  }
  public SchemaString GetunitSymbol()
  {
   return GetunitSymbolAt(0);
  }
  public SchemaString unitSymbol
  {
   get
   {
    return GetunitSymbolAt(0);
   }
  }
  public void RemoveunitSymbolAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "unitSymbol", index);
  }
  public void RemoveunitSymbol()
  {
   while (HasunitSymbol())
    RemoveunitSymbolAt(0);
  }
  public void AddunitSymbol(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "unitSymbol", newValue.ToString());
  }
  public void InsertunitSymbolAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "unitSymbol", index, newValue.ToString());
  }
  public void ReplaceunitSymbolAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "unitSymbol", index, newValue.ToString());
  }
        public unitSymbolCollection MyunitSymbols = new unitSymbolCollection( );
        public class unitSymbolCollection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public unitSymbolEnumerator GetEnumerator()
   {
    return new unitSymbolEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class unitSymbolEnumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public unitSymbolEnumerator(DimensionType par)
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
    return(nIndex < parent.unitSymbolCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetunitSymbolAt(nIndex));
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
  public int Getdefault2MinCount()
  {
   return 0;
  }
  public int default2MinCount
  {
   get
   {
    return 0;
   }
  }
  public int Getdefault2MaxCount()
  {
   return 1;
  }
  public int default2MaxCount
  {
   get
   {
    return 1;
   }
  }
  public int Getdefault2Count()
  {
   return DomChildCount(NodeType.Attribute, "", "default");
  }
  public int default2Count
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "default");
   }
  }
  public bool Hasdefault2()
  {
   return HasDomChild(NodeType.Attribute, "", "default");
  }
  public SchemaString Getdefault2At(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "default", index)));
  }
  public SchemaString Getdefault2()
  {
   return Getdefault2At(0);
  }
  public SchemaString default2
  {
   get
   {
    return Getdefault2At(0);
   }
  }
  public void Removedefault2At(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "default", index);
  }
  public void Removedefault2()
  {
   while (Hasdefault2())
    Removedefault2At(0);
  }
  public void Adddefault2(SchemaString newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "default", newValue.ToString());
  }
  public void Insertdefault2At(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "default", index, newValue.ToString());
  }
  public void Replacedefault2At(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "default", index, newValue.ToString());
  }
        public default2Collection Mydefault2s = new default2Collection( );
        public class default2Collection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public default2Enumerator GetEnumerator()
   {
    return new default2Enumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class default2Enumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public default2Enumerator(DimensionType par)
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
    return(nIndex < parent.default2Count );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.Getdefault2At(nIndex));
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
  public int GetmultipleValuesMinCount()
  {
   return 0;
  }
  public int multipleValuesMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetmultipleValuesMaxCount()
  {
   return 1;
  }
  public int multipleValuesMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetmultipleValuesCount()
  {
   return DomChildCount(NodeType.Attribute, "", "multipleValues");
  }
  public int multipleValuesCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "multipleValues");
   }
  }
  public bool HasmultipleValues()
  {
   return HasDomChild(NodeType.Attribute, "", "multipleValues");
  }
  public SchemaBoolean GetmultipleValuesAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "multipleValues", index)));
  }
  public SchemaBoolean GetmultipleValues()
  {
   return GetmultipleValuesAt(0);
  }
  public SchemaBoolean multipleValues
  {
   get
   {
    return GetmultipleValuesAt(0);
   }
  }
  public void RemovemultipleValuesAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "multipleValues", index);
  }
  public void RemovemultipleValues()
  {
   while (HasmultipleValues())
    RemovemultipleValuesAt(0);
  }
  public void AddmultipleValues(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "multipleValues", newValue.ToString());
  }
  public void InsertmultipleValuesAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "multipleValues", index, newValue.ToString());
  }
  public void ReplacemultipleValuesAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "multipleValues", index, newValue.ToString());
  }
        public multipleValuesCollection MymultipleValuess = new multipleValuesCollection( );
        public class multipleValuesCollection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public multipleValuesEnumerator GetEnumerator()
   {
    return new multipleValuesEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class multipleValuesEnumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public multipleValuesEnumerator(DimensionType par)
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
    return(nIndex < parent.multipleValuesCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetmultipleValuesAt(nIndex));
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
  public int GetnearestValueMinCount()
  {
   return 0;
  }
  public int nearestValueMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetnearestValueMaxCount()
  {
   return 1;
  }
  public int nearestValueMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetnearestValueCount()
  {
   return DomChildCount(NodeType.Attribute, "", "nearestValue");
  }
  public int nearestValueCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "nearestValue");
   }
  }
  public bool HasnearestValue()
  {
   return HasDomChild(NodeType.Attribute, "", "nearestValue");
  }
  public SchemaBoolean GetnearestValueAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "nearestValue", index)));
  }
  public SchemaBoolean GetnearestValue()
  {
   return GetnearestValueAt(0);
  }
  public SchemaBoolean nearestValue
  {
   get
   {
    return GetnearestValueAt(0);
   }
  }
  public void RemovenearestValueAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "nearestValue", index);
  }
  public void RemovenearestValue()
  {
   while (HasnearestValue())
    RemovenearestValueAt(0);
  }
  public void AddnearestValue(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "nearestValue", newValue.ToString());
  }
  public void InsertnearestValueAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "nearestValue", index, newValue.ToString());
  }
  public void ReplacenearestValueAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "nearestValue", index, newValue.ToString());
  }
        public nearestValueCollection MynearestValues = new nearestValueCollection( );
        public class nearestValueCollection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public nearestValueEnumerator GetEnumerator()
   {
    return new nearestValueEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class nearestValueEnumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public nearestValueEnumerator(DimensionType par)
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
    return(nIndex < parent.nearestValueCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetnearestValueAt(nIndex));
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
  public int GetcurrentMinCount()
  {
   return 0;
  }
  public int currentMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetcurrentMaxCount()
  {
   return 1;
  }
  public int currentMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetcurrentCount()
  {
   return DomChildCount(NodeType.Attribute, "", "current");
  }
  public int currentCount
  {
   get
   {
    return DomChildCount(NodeType.Attribute, "", "current");
   }
  }
  public bool Hascurrent()
  {
   return HasDomChild(NodeType.Attribute, "", "current");
  }
  public SchemaBoolean GetcurrentAt(int index)
  {
   return new SchemaBoolean(GetDomNodeValue(GetDomChildAt(NodeType.Attribute, "", "current", index)));
  }
  public SchemaBoolean Getcurrent()
  {
   return GetcurrentAt(0);
  }
  public SchemaBoolean current
  {
   get
   {
    return GetcurrentAt(0);
   }
  }
  public void RemovecurrentAt(int index)
  {
   RemoveDomChildAt(NodeType.Attribute, "", "current", index);
  }
  public void Removecurrent()
  {
   while (Hascurrent())
    RemovecurrentAt(0);
  }
  public void Addcurrent(SchemaBoolean newValue)
  {
   AppendDomChild(NodeType.Attribute, "", "current", newValue.ToString());
  }
  public void InsertcurrentAt(SchemaBoolean newValue, int index)
  {
   InsertDomChildAt(NodeType.Attribute, "", "current", index, newValue.ToString());
  }
  public void ReplacecurrentAt(SchemaBoolean newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Attribute, "", "current", index, newValue.ToString());
  }
        public currentCollection Mycurrents = new currentCollection( );
        public class currentCollection: IEnumerable
        {
            DimensionType parent;
            public DimensionType Parent
   {
    set
    {
     parent = value;
    }
   }
   public currentEnumerator GetEnumerator()
   {
    return new currentEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class currentEnumerator: IEnumerator
        {
   int nIndex;
   DimensionType parent;
   public currentEnumerator(DimensionType par)
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
    return(nIndex < parent.currentCount );
   }
   public SchemaBoolean Current
   {
    get
    {
     return(parent.GetcurrentAt(nIndex));
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
            Mynames.Parent = this;
            Myunitss.Parent = this;
            MyunitSymbols.Parent = this;
            Mydefault2s.Parent = this;
            MymultipleValuess.Parent = this;
            MynearestValues.Parent = this;
            Mycurrents.Parent = this;
 }
}
}
