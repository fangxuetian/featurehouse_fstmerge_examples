using System;
using System.Collections;
using System.Xml;
using Altova.Types;
namespace capabilities_1_1_1
{
 public class PostType : Altova.Xml.Node
 {
  public PostType() : base() { SetCollectionParents(); }
  public PostType(XmlDocument doc) : base(doc) { SetCollectionParents(); }
  public PostType(XmlNode node) : base(node) { SetCollectionParents(); }
  public PostType(Altova.Xml.Node node) : base(node) { SetCollectionParents(); }
  public override void AdjustPrefix()
  {
   for (int i = 0; i < DomChildCount(NodeType.Element, "", "OnlineResource"); i++)
   {
    XmlNode DOMNode = GetDomChildAt(NodeType.Element, "", "OnlineResource", i);
    InternalAdjustPrefix(DOMNode, false);
    new OnlineResourceType(DOMNode).AdjustPrefix();
   }
  }
  public int GetOnlineResourceMinCount()
  {
   return 1;
  }
  public int OnlineResourceMinCount
  {
   get
   {
    return 1;
   }
  }
  public int GetOnlineResourceMaxCount()
  {
   return 1;
  }
  public int OnlineResourceMaxCount
  {
   get
   {
    return 1;
   }
  }
  public int GetOnlineResourceCount()
  {
   return DomChildCount(NodeType.Element, "", "OnlineResource");
  }
  public int OnlineResourceCount
  {
   get
   {
    return DomChildCount(NodeType.Element, "", "OnlineResource");
   }
  }
  public bool HasOnlineResource()
  {
   return HasDomChild(NodeType.Element, "", "OnlineResource");
  }
  public OnlineResourceType GetOnlineResourceAt(int index)
  {
   return new OnlineResourceType(GetDomChildAt(NodeType.Element, "", "OnlineResource", index));
  }
  public OnlineResourceType GetOnlineResource()
  {
   return GetOnlineResourceAt(0);
  }
  public OnlineResourceType OnlineResource
  {
   get
   {
    return GetOnlineResourceAt(0);
   }
  }
  public void RemoveOnlineResourceAt(int index)
  {
   RemoveDomChildAt(NodeType.Element, "", "OnlineResource", index);
  }
  public void RemoveOnlineResource()
  {
   while (HasOnlineResource())
    RemoveOnlineResourceAt(0);
  }
  public void AddOnlineResource(OnlineResourceType newValue)
  {
   AppendDomElement("", "OnlineResource", newValue);
  }
  public void InsertOnlineResourceAt(OnlineResourceType newValue, int index)
  {
   InsertDomElementAt("", "OnlineResource", index, newValue);
  }
  public void ReplaceOnlineResourceAt(OnlineResourceType newValue, int index)
  {
   ReplaceDomElementAt("", "OnlineResource", index, newValue);
  }
        public OnlineResourceCollection MyOnlineResources = new OnlineResourceCollection( );
        public class OnlineResourceCollection: IEnumerable
        {
            PostType parent;
            public PostType Parent
   {
    set
    {
     parent = value;
    }
   }
   public OnlineResourceEnumerator GetEnumerator()
   {
    return new OnlineResourceEnumerator(parent);
   }
   IEnumerator IEnumerable.GetEnumerator()
   {
    return GetEnumerator();
   }
        }
        public class OnlineResourceEnumerator: IEnumerator
        {
   int nIndex;
   PostType parent;
   public OnlineResourceEnumerator(PostType par)
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
    return(nIndex < parent.OnlineResourceCount );
   }
   public OnlineResourceType Current
   {
    get
    {
     return(parent.GetOnlineResourceAt(nIndex));
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
            MyOnlineResources.Parent = this;
 }
}
}
