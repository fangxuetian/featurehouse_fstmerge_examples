using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_3_0.wms
{
 public class ContactInformationType : Altova.Xml.Node
 {
  public ContactInformationType() : base() { SetCollectionParents(); }
  public ContactInformationType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public ContactInformationType(XmlNode node) : base(node) { SetCollectionParents(); }
  public ContactInformationType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary", i);
    InternalAdjustPrefix(DOMNode, true);
    new ContactPersonPrimaryType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress", i);
    InternalAdjustPrefix(DOMNode, true);
    new ContactAddressType(DOMNode).AdjustPrefix();
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone", i);
    InternalAdjustPrefix(DOMNode, true);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress", i);
    InternalAdjustPrefix(DOMNode, true);
   }
  }
  public int GetContactPersonPrimaryMinCount()
  {
   return 0;
  }
  public int ContactPersonPrimaryMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetContactPersonPrimaryMaxCount()
  {
   return 1;
  }
  public int ContactPersonPrimaryMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactPersonPrimaryCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary");
  }
  public int ContactPersonPrimaryCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary");
   }
  }
  public bool HasContactPersonPrimary()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary");
  }
  public ContactPersonPrimaryType GetContactPersonPrimaryAt(int index)
  {
   return new ContactPersonPrimaryType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary", index));
  }
  public ContactPersonPrimaryType GetContactPersonPrimary()
  {
   return GetContactPersonPrimaryAt(0);
  }
  public ContactPersonPrimaryType ContactPersonPrimary
  {
   get
   {
    return GetContactPersonPrimaryAt(0);
   }
  }
  public void RemoveContactPersonPrimaryAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPersonPrimary", index);
  }
  public void RemoveContactPersonPrimary()
  {
   while (HasContactPersonPrimary())
    RemoveContactPersonPrimaryAt(0);
  }
  public void AddContactPersonPrimary(ContactPersonPrimaryType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "ContactPersonPrimary", newValue);
  }
  public void InsertContactPersonPrimaryAt(ContactPersonPrimaryType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "ContactPersonPrimary", index, newValue);
  }
  public void ReplaceContactPersonPrimaryAt(ContactPersonPrimaryType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "ContactPersonPrimary", index, newValue);
  }
        public ContactPersonPrimaryCollection MyContactPersonPrimarys = new ContactPersonPrimaryCollection( );
        public class ContactPersonPrimaryCollection: IEnumerable
        {
            ContactInformationType parent;
            public ContactInformationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactPersonPrimaryEnumerator GetEnumerator()
   {
    return new ContactPersonPrimaryEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactPersonPrimaryEnumerator: IEnumerator
        {
   int nIndex;
   ContactInformationType parent;
   public ContactPersonPrimaryEnumerator(ContactInformationType par)
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
    return(nIndex < parent.ContactPersonPrimaryCount );
   }
   public ContactPersonPrimaryType Current
   {
    get
    {
     return(parent.GetContactPersonPrimaryAt(nIndex));
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
  public int GetContactPositionMinCount()
  {
   return 0;
  }
  public int ContactPositionMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetContactPositionMaxCount()
  {
   return 1;
  }
  public int ContactPositionMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactPositionCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition");
  }
  public int ContactPositionCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition");
   }
  }
  public bool HasContactPosition()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition");
  }
  public SchemaString GetContactPositionAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition", index)));
  }
  public SchemaString GetContactPosition()
  {
   return GetContactPositionAt(0);
  }
  public SchemaString ContactPosition
  {
   get
   {
    return GetContactPositionAt(0);
   }
  }
  public void RemoveContactPositionAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition", index);
  }
  public void RemoveContactPosition()
  {
   while (HasContactPosition())
    RemoveContactPositionAt(0);
  }
  public void AddContactPosition(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition", newValue.ToString());
  }
  public void InsertContactPositionAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition", index, newValue.ToString());
  }
  public void ReplaceContactPositionAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactPosition", index, newValue.ToString());
  }
        public ContactPositionCollection MyContactPositions = new ContactPositionCollection( );
        public class ContactPositionCollection: IEnumerable
        {
            ContactInformationType parent;
            public ContactInformationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactPositionEnumerator GetEnumerator()
   {
    return new ContactPositionEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactPositionEnumerator: IEnumerator
        {
   int nIndex;
   ContactInformationType parent;
   public ContactPositionEnumerator(ContactInformationType par)
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
    return(nIndex < parent.ContactPositionCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetContactPositionAt(nIndex));
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
  public int GetContactAddressMinCount()
  {
   return 0;
  }
  public int ContactAddressMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetContactAddressMaxCount()
  {
   return 1;
  }
  public int ContactAddressMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactAddressCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress");
  }
  public int ContactAddressCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress");
   }
  }
  public bool HasContactAddress()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress");
  }
  public ContactAddressType GetContactAddressAt(int index)
  {
   return new ContactAddressType(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress", index));
  }
  public ContactAddressType GetContactAddress()
  {
   return GetContactAddressAt(0);
  }
  public ContactAddressType ContactAddress
  {
   get
   {
    return GetContactAddressAt(0);
   }
  }
  public void RemoveContactAddressAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactAddress", index);
  }
  public void RemoveContactAddress()
  {
   while (HasContactAddress())
    RemoveContactAddressAt(0);
  }
  public void AddContactAddress(ContactAddressType newValue)
  {
   AppendDomElement("http://www.opengis.net/wms", "ContactAddress", newValue);
  }
  public void InsertContactAddressAt(ContactAddressType newValue, int index)
  {
   InsertDomElementAt("http://www.opengis.net/wms", "ContactAddress", index, newValue);
  }
  public void ReplaceContactAddressAt(ContactAddressType newValue, int index)
  {
   ReplaceDomElementAt("http://www.opengis.net/wms", "ContactAddress", index, newValue);
  }
        public ContactAddressCollection MyContactAddresss = new ContactAddressCollection( );
        public class ContactAddressCollection: IEnumerable
        {
            ContactInformationType parent;
            public ContactInformationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactAddressEnumerator GetEnumerator()
   {
    return new ContactAddressEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactAddressEnumerator: IEnumerator
        {
   int nIndex;
   ContactInformationType parent;
   public ContactAddressEnumerator(ContactInformationType par)
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
    return(nIndex < parent.ContactAddressCount );
   }
   public ContactAddressType Current
   {
    get
    {
     return(parent.GetContactAddressAt(nIndex));
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
  public int GetContactVoiceTelephoneMinCount()
  {
   return 0;
  }
  public int ContactVoiceTelephoneMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetContactVoiceTelephoneMaxCount()
  {
   return 1;
  }
  public int ContactVoiceTelephoneMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactVoiceTelephoneCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone");
  }
  public int ContactVoiceTelephoneCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone");
   }
  }
  public bool HasContactVoiceTelephone()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone");
  }
  public SchemaString GetContactVoiceTelephoneAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone", index)));
  }
  public SchemaString GetContactVoiceTelephone()
  {
   return GetContactVoiceTelephoneAt(0);
  }
  public SchemaString ContactVoiceTelephone
  {
   get
   {
    return GetContactVoiceTelephoneAt(0);
   }
  }
  public void RemoveContactVoiceTelephoneAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone", index);
  }
  public void RemoveContactVoiceTelephone()
  {
   while (HasContactVoiceTelephone())
    RemoveContactVoiceTelephoneAt(0);
  }
  public void AddContactVoiceTelephone(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone", newValue.ToString());
  }
  public void InsertContactVoiceTelephoneAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone", index, newValue.ToString());
  }
  public void ReplaceContactVoiceTelephoneAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactVoiceTelephone", index, newValue.ToString());
  }
        public ContactVoiceTelephoneCollection MyContactVoiceTelephones = new ContactVoiceTelephoneCollection( );
        public class ContactVoiceTelephoneCollection: IEnumerable
        {
            ContactInformationType parent;
            public ContactInformationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactVoiceTelephoneEnumerator GetEnumerator()
   {
    return new ContactVoiceTelephoneEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactVoiceTelephoneEnumerator: IEnumerator
        {
   int nIndex;
   ContactInformationType parent;
   public ContactVoiceTelephoneEnumerator(ContactInformationType par)
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
    return(nIndex < parent.ContactVoiceTelephoneCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetContactVoiceTelephoneAt(nIndex));
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
  public int GetContactFacsimileTelephoneMinCount()
  {
   return 0;
  }
  public int ContactFacsimileTelephoneMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetContactFacsimileTelephoneMaxCount()
  {
   return 1;
  }
  public int ContactFacsimileTelephoneMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactFacsimileTelephoneCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone");
  }
  public int ContactFacsimileTelephoneCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone");
   }
  }
  public bool HasContactFacsimileTelephone()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone");
  }
  public SchemaString GetContactFacsimileTelephoneAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone", index)));
  }
  public SchemaString GetContactFacsimileTelephone()
  {
   return GetContactFacsimileTelephoneAt(0);
  }
  public SchemaString ContactFacsimileTelephone
  {
   get
   {
    return GetContactFacsimileTelephoneAt(0);
   }
  }
  public void RemoveContactFacsimileTelephoneAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone", index);
  }
  public void RemoveContactFacsimileTelephone()
  {
   while (HasContactFacsimileTelephone())
    RemoveContactFacsimileTelephoneAt(0);
  }
  public void AddContactFacsimileTelephone(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone", newValue.ToString());
  }
  public void InsertContactFacsimileTelephoneAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone", index, newValue.ToString());
  }
  public void ReplaceContactFacsimileTelephoneAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactFacsimileTelephone", index, newValue.ToString());
  }
        public ContactFacsimileTelephoneCollection MyContactFacsimileTelephones = new ContactFacsimileTelephoneCollection( );
        public class ContactFacsimileTelephoneCollection: IEnumerable
        {
            ContactInformationType parent;
            public ContactInformationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactFacsimileTelephoneEnumerator GetEnumerator()
   {
    return new ContactFacsimileTelephoneEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactFacsimileTelephoneEnumerator: IEnumerator
        {
   int nIndex;
   ContactInformationType parent;
   public ContactFacsimileTelephoneEnumerator(ContactInformationType par)
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
    return(nIndex < parent.ContactFacsimileTelephoneCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetContactFacsimileTelephoneAt(nIndex));
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
  public int GetContactElectronicMailAddressMinCount()
  {
   return 0;
  }
  public int ContactElectronicMailAddressMinCount
  {
   get
   {
    return 0;
   }
  }
  public int GetContactElectronicMailAddressMaxCount()
  {
   return 1;
  }
  public int ContactElectronicMailAddressMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactElectronicMailAddressCount()
  {
   return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress");
  }
  public int ContactElectronicMailAddressCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress");
   }
  }
  public bool HasContactElectronicMailAddress()
  {
   return HasDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress");
  }
  public SchemaString GetContactElectronicMailAddressAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress", index)));
  }
  public SchemaString GetContactElectronicMailAddress()
  {
   return GetContactElectronicMailAddressAt(0);
  }
  public SchemaString ContactElectronicMailAddress
  {
   get
   {
    return GetContactElectronicMailAddressAt(0);
   }
  }
  public void RemoveContactElectronicMailAddressAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress", index);
  }
  public void RemoveContactElectronicMailAddress()
  {
   while (HasContactElectronicMailAddress())
    RemoveContactElectronicMailAddressAt(0);
  }
  public void AddContactElectronicMailAddress(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress", newValue.ToString());
  }
  public void InsertContactElectronicMailAddressAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress", index, newValue.ToString());
  }
  public void ReplaceContactElectronicMailAddressAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "http://www.opengis.net/wms", "ContactElectronicMailAddress", index, newValue.ToString());
  }
        public ContactElectronicMailAddressCollection MyContactElectronicMailAddresss = new ContactElectronicMailAddressCollection( );
        public class ContactElectronicMailAddressCollection: IEnumerable
        {
            ContactInformationType parent;
            public ContactInformationType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactElectronicMailAddressEnumerator GetEnumerator()
   {
    return new ContactElectronicMailAddressEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactElectronicMailAddressEnumerator: IEnumerator
        {
   int nIndex;
   ContactInformationType parent;
   public ContactElectronicMailAddressEnumerator(ContactInformationType par)
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
    return(nIndex < parent.ContactElectronicMailAddressCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetContactElectronicMailAddressAt(nIndex));
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
            MyContactPersonPrimarys.Parent = this;
            MyContactPositions.Parent = this;
            MyContactAddresss.Parent = this;
            MyContactVoiceTelephones.Parent = this;
            MyContactFacsimileTelephones.Parent = this;
            MyContactElectronicMailAddresss.Parent = this;
 }
}
}
