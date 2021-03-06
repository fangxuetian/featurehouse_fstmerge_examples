using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_1_1
{
 public class ContactPersonPrimaryType : Altova.Xml.Node
 {
  public ContactPersonPrimaryType() : base() { SetCollectionParents(); }
  public ContactPersonPrimaryType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public ContactPersonPrimaryType(XmlNode node) : base(node) { SetCollectionParents(); }
  public ContactPersonPrimaryType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ContactPerson"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ContactPerson", i);
    InternalAdjustPrefix(DOMNode, false);
   }
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "ContactOrganization"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "ContactOrganization", i);
    InternalAdjustPrefix(DOMNode, false);
   }
  }
  public int GetContactPersonMinCount()
  {
   return 1;
  }
  public int ContactPersonMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactPersonMaxCount()
  {
   return 1;
  }
  public int ContactPersonMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactPersonCount()
  {
   return DomChildCount(NodeType.Element, "", "ContactPerson");
  }
  public int ContactPersonCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ContactPerson");
   }
  }
  public bool HasContactPerson()
  {
   return HasDomChild(NodeType.Element, "", "ContactPerson");
  }
  public SchemaString GetContactPersonAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "ContactPerson", index)));
  }
  public SchemaString GetContactPerson()
  {
   return GetContactPersonAt(0);
  }
  public SchemaString ContactPerson
  {
   get
   {
    return GetContactPersonAt(0);
   }
  }
  public void RemoveContactPersonAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ContactPerson", index);
  }
  public void RemoveContactPerson()
  {
   while (HasContactPerson())
    RemoveContactPersonAt(0);
  }
  public void AddContactPerson(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "", "ContactPerson", newValue.ToString());
  }
  public void InsertContactPersonAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "ContactPerson", index, newValue.ToString());
  }
  public void ReplaceContactPersonAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "ContactPerson", index, newValue.ToString());
  }
        public ContactPersonCollection MyContactPersons = new ContactPersonCollection( );
        public class ContactPersonCollection: IEnumerable
        {
            ContactPersonPrimaryType parent;
            public ContactPersonPrimaryType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactPersonEnumerator GetEnumerator()
   {
    return new ContactPersonEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactPersonEnumerator: IEnumerator
        {
   int nIndex;
   ContactPersonPrimaryType parent;
   public ContactPersonEnumerator(ContactPersonPrimaryType par)
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
    return(nIndex < parent.ContactPersonCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetContactPersonAt(nIndex));
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
  public int GetContactOrganizationMinCount()
  {
   return 1;
  }
  public int ContactOrganizationMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactOrganizationMaxCount()
  {
   return 1;
  }
  public int ContactOrganizationMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetContactOrganizationCount()
  {
   return DomChildCount(NodeType.Element, "", "ContactOrganization");
  }
  public int ContactOrganizationCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "ContactOrganization");
   }
  }
  public bool HasContactOrganization()
  {
   return HasDomChild(NodeType.Element, "", "ContactOrganization");
  }
  public SchemaString GetContactOrganizationAt(int index)
  {
   return new SchemaString(GetDomNodeValue(GetDomChildAt(NodeType.Element, "", "ContactOrganization", index)));
  }
  public SchemaString GetContactOrganization()
  {
   return GetContactOrganizationAt(0);
  }
  public SchemaString ContactOrganization
  {
   get
   {
    return GetContactOrganizationAt(0);
   }
  }
  public void RemoveContactOrganizationAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "ContactOrganization", index);
  }
  public void RemoveContactOrganization()
  {
   while (HasContactOrganization())
    RemoveContactOrganizationAt(0);
  }
  public void AddContactOrganization(SchemaString newValue)
  {
   AppendDomChild(NodeType.Element, "", "ContactOrganization", newValue.ToString());
  }
  public void InsertContactOrganizationAt(SchemaString newValue, int index)
  {
   InsertDomChildAt(NodeType.Element, "", "ContactOrganization", index, newValue.ToString());
  }
  public void ReplaceContactOrganizationAt(SchemaString newValue, int index)
  {
   ReplaceDomChildAt(NodeType.Element, "", "ContactOrganization", index, newValue.ToString());
  }
        public ContactOrganizationCollection MyContactOrganizations = new ContactOrganizationCollection( );
        public class ContactOrganizationCollection: IEnumerable
        {
            ContactPersonPrimaryType parent;
            public ContactPersonPrimaryType Parent
   {
    set
    {
     parent = value;
    }
   }
   public ContactOrganizationEnumerator GetEnumerator()
   {
    return new ContactOrganizationEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class ContactOrganizationEnumerator: IEnumerator
        {
   int nIndex;
   ContactPersonPrimaryType parent;
   public ContactOrganizationEnumerator(ContactPersonPrimaryType par)
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
    return(nIndex < parent.ContactOrganizationCount );
   }
   public SchemaString Current
   {
    get
    {
     return(parent.GetContactOrganizationAt(nIndex));
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
            MyContactPersons.Parent = this;
            MyContactOrganizations.Parent = this;
 }
}
}
