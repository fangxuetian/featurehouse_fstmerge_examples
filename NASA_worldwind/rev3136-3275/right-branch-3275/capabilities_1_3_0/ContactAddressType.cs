using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_3_0.wms
{
 public class ContactAddressType : Altova.Xml.Node
 {
  public ContactAddressType() : base() { SetCollectionParents(); }
  public ContactAddressType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public ContactAddressType(XmlNode node) : base(node) { SetCollectionParents(); }
  public ContactAddressType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "AddressType"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AddressType", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Address"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Address", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "City"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "City", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "PostCode"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "PostCode", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Country"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Country", i);
    InternalAdjustPrefix(DOMNode, true);
   }
  }
  public int GetAddressTypeMinCount()
  {
   return 1;
  }
  public int AddressTypeMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetAddressTypeMaxCount()
  {
   return 1;
  }
  public int AddressTypeMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetAddressTypeCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "AddressType");
  }
  public int AddressTypeCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "AddressType");
   }
  }
  public bool HasAddressType()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "AddressType");
  }
  public SchemaString GetAddressTypeAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AddressType", index)));
  }
  public SchemaString GetAddressType()
  {
   return GetAddressTypeAt(0);
  }
  public SchemaString AddressType
  {
   get
   {
    return GetAddressTypeAt(0);
   }
  }
  public void RemoveAddressTypeAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AddressType", index);
  }
  public void RemoveAddressType()
  {
   while (HasAddressType())
    RemoveAddressTypeAt(0);
  }
  public void AddAddressType(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "AddressType", newValue.ToString());
  }
  public void InsertAddressTypeAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AddressType", index, newValue.ToString());
  }
  public void ReplaceAddressTypeAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "AddressType", index, newValue.ToString());
  }
        public AddressTypeCollection MyAddressTypes = new AddressTypeCollection( );
        public class AddressTypeCollection: IEnumerable
        {
            ContactAddressType parent;
            public ContactAddressType Parent
   {
    set
    {
     parent = value;
    }
   }
   public AddressTypeEnumerator GetEnumerator()
   {
    return new AddressTypeEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class AddressTypeEnumerator: IEnumerator
        {
   int nIndex;
   ContactAddressType parent;
   public AddressTypeEnumerator(ContactAddressType par)
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
    return(nIndex < parent.AddressTypeCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetAddressTypeAt(nIndex));
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
  public int GetAddressMinCount()
  {
   return 1;
  }
  public int AddressMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetAddressMaxCount()
  {
   return 1;
  }
  public int AddressMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetAddressCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Address");
  }
  public int AddressCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Address");
   }
  }
  public bool HasAddress()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Address");
  }
  public SchemaString GetAddressAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Address", index)));
  }
  public SchemaString GetAddress()
  {
   return GetAddressAt(0);
  }
  public SchemaString Address
  {
   get
   {
    return GetAddressAt(0);
   }
  }
  public void RemoveAddressAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Address", index);
  }
  public void RemoveAddress()
  {
   while (HasAddress())
    RemoveAddressAt(0);
  }
  public void AddAddress(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "Address", newValue.ToString());
  }
  public void InsertAddressAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Address", index, newValue.ToString());
  }
  public void ReplaceAddressAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Address", index, newValue.ToString());
  }
        public AddressCollection MyAddresss = new AddressCollection( );
        public class AddressCollection: IEnumerable
        {
            ContactAddressType parent;
            public ContactAddressType Parent
   {
    set
    {
     parent = value;
    }
   }
   public AddressEnumerator GetEnumerator()
   {
    return new AddressEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class AddressEnumerator: IEnumerator
        {
   int nIndex;
   ContactAddressType parent;
   public AddressEnumerator(ContactAddressType par)
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
    return(nIndex < parent.AddressCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetAddressAt(nIndex));
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
  public int GetCityMinCount()
  {
   return 1;
  }
  public int CityMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetCityMaxCount()
  {
   return 1;
  }
  public int CityMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetCityCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "City");
  }
  public int CityCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "City");
   }
  }
  public bool HasCity()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "City");
  }
  public SchemaString GetCityAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "City", index)));
  }
  public SchemaString GetCity()
  {
   return GetCityAt(0);
  }
  public SchemaString City
  {
   get
   {
    return GetCityAt(0);
   }
  }
  public void RemoveCityAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "City", index);
  }
  public void RemoveCity()
  {
   while (HasCity())
    RemoveCityAt(0);
  }
  public void AddCity(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "City", newValue.ToString());
  }
  public void InsertCityAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "City", index, newValue.ToString());
  }
  public void ReplaceCityAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "City", index, newValue.ToString());
  }
        public CityCollection MyCitys = new CityCollection( );
        public class CityCollection: IEnumerable
        {
            ContactAddressType parent;
            public ContactAddressType Parent
   {
    set
    {
     parent = value;
    }
   }
   public CityEnumerator GetEnumerator()
   {
    return new CityEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class CityEnumerator: IEnumerator
        {
   int nIndex;
   ContactAddressType parent;
   public CityEnumerator(ContactAddressType par)
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
    return(nIndex < parent.CityCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetCityAt(nIndex));
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
  public int GetStateOrProvinceMinCount()
  {
   return 1;
  }
  public int StateOrProvinceMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetStateOrProvinceMaxCount()
  {
   return 1;
  }
  public int StateOrProvinceMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetStateOrProvinceCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince");
  }
  public int StateOrProvinceCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince");
   }
  }
  public bool HasStateOrProvince()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince");
  }
  public SchemaString GetStateOrProvinceAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince", index)));
  }
  public SchemaString GetStateOrProvince()
  {
   return GetStateOrProvinceAt(0);
  }
  public SchemaString StateOrProvince
  {
   get
   {
    return GetStateOrProvinceAt(0);
   }
  }
  public void RemoveStateOrProvinceAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince", index);
  }
  public void RemoveStateOrProvince()
  {
   while (HasStateOrProvince())
    RemoveStateOrProvinceAt(0);
  }
  public void AddStateOrProvince(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince", newValue.ToString());
  }
  public void InsertStateOrProvinceAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince", index, newValue.ToString());
  }
  public void ReplaceStateOrProvinceAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "StateOrProvince", index, newValue.ToString());
  }
        public StateOrProvinceCollection MyStateOrProvinces = new StateOrProvinceCollection( );
        public class StateOrProvinceCollection: IEnumerable
        {
            ContactAddressType parent;
            public ContactAddressType Parent
   {
    set
    {
     parent = value;
    }
   }
   public StateOrProvinceEnumerator GetEnumerator()
   {
    return new StateOrProvinceEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class StateOrProvinceEnumerator: IEnumerator
        {
   int nIndex;
   ContactAddressType parent;
   public StateOrProvinceEnumerator(ContactAddressType par)
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
    return(nIndex < parent.StateOrProvinceCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetStateOrProvinceAt(nIndex));
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
  public int GetPostCodeMinCount()
  {
   return 1;
  }
  public int PostCodeMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetPostCodeMaxCount()
  {
   return 1;
  }
  public int PostCodeMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetPostCodeCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "PostCode");
  }
  public int PostCodeCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "PostCode");
   }
  }
  public bool HasPostCode()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "PostCode");
  }
  public SchemaString GetPostCodeAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "PostCode", index)));
  }
  public SchemaString GetPostCode()
  {
   return GetPostCodeAt(0);
  }
  public SchemaString PostCode
  {
   get
   {
    return GetPostCodeAt(0);
   }
  }
  public void RemovePostCodeAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "PostCode", index);
  }
  public void RemovePostCode()
  {
   while (HasPostCode())
    RemovePostCodeAt(0);
  }
  public void AddPostCode(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "PostCode", newValue.ToString());
  }
  public void InsertPostCodeAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "PostCode", index, newValue.ToString());
  }
  public void ReplacePostCodeAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "PostCode", index, newValue.ToString());
  }
        public PostCodeCollection MyPostCodes = new PostCodeCollection( );
        public class PostCodeCollection: IEnumerable
        {
            ContactAddressType parent;
            public ContactAddressType Parent
   {
    set
    {
     parent = value;
    }
   }
   public PostCodeEnumerator GetEnumerator()
   {
    return new PostCodeEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class PostCodeEnumerator: IEnumerator
        {
   int nIndex;
   ContactAddressType parent;
   public PostCodeEnumerator(ContactAddressType par)
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
    return(nIndex < parent.PostCodeCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetPostCodeAt(nIndex));
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
  public int GetCountryMinCount()
  {
   return 1;
  }
  public int CountryMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetCountryMaxCount()
  {
   return 1;
  }
  public int CountryMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetCountryCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Country");
  }
  public int CountryCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "Country");
   }
  }
  public bool HasCountry()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "Country");
  }
  public SchemaString GetCountryAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Country", index)));
  }
  public SchemaString GetCountry()
  {
   return GetCountryAt(0);
  }
  public SchemaString Country
  {
   get
   {
    return GetCountryAt(0);
   }
  }
  public void RemoveCountryAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Country", index);
  }
  public void RemoveCountry()
  {
   while (HasCountry())
    RemoveCountryAt(0);
  }
  public void AddCountry(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "Country", newValue.ToString());
  }
  public void InsertCountryAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Country", index, newValue.ToString());
  }
  public void ReplaceCountryAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "Country", index, newValue.ToString());
  }
        public CountryCollection MyCountrys = new CountryCollection( );
        public class CountryCollection: IEnumerable
        {
            ContactAddressType parent;
            public ContactAddressType Parent
   {
    set
    {
     parent = value;
    }
   }
   public CountryEnumerator GetEnumerator()
   {
    return new CountryEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class CountryEnumerator: IEnumerator
        {
   int nIndex;
   ContactAddressType parent;
   public CountryEnumerator(ContactAddressType par)
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
    return(nIndex < parent.CountryCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetCountryAt(nIndex));
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
            MyAddressTypes.Parent = this;
            MyAddresss.Parent = this;
            MyCitys.Parent = this;
            MyStateOrProvinces.Parent = this;
            MyPostCodes.Parent = this;
            MyCountrys.Parent = this;
 }
}
}
