using System; 
using System.Collections; 
using System.Diagnostics; 
using System.Xml; namespace  Microsoft.Office.OneNote {
	
 [Serializable] 
 public abstract class  ImportNode  : ICloneable {
		
  protected  ImportNode()
  {
   CommitPending = true;
  }
 
  protected internal abstract  void SerializeToXml(XmlNode parentNode);
 
  public abstract  object Clone();
 
  protected internal  void AddChild(ImportNode child)
  {
   AddChild(child, null);
  }
 
  protected internal  ImportNode AddChild(ImportNode child, String childName)
  {
   Debug.Assert(!children.Contains(child));
   if (child.Parent != null && child.Parent != this)
   {
    child = (ImportNode) child.Clone();
   }
   children.Add(child);
   child.Parent = this;
   child.Name = childName;
   child.CommitPending = true;
   return child;
  }
 
  protected internal  int GetChildCount()
  {
   return children.Count;
  }
 
  protected internal  ImportNode GetChild(int index)
  {
   return (ImportNode) children[index];
  }
 
  protected internal  ImportNode GetChild(string childName)
  {
   foreach (ImportNode node in children)
   {
    if (childName.Equals(node.Name))
    {
     return node;
    }
   }
   return null;
  }
 
  protected internal  void RemoveChild(ImportNode child)
  {
   Debug.Assert(child.Parent == this);
   Debug.Assert(children.Contains(child));
   children.Remove(child);
   child.Parent = null;
   child.Name = null;
   CommitPending = true;
  }
 
  protected internal  string Name
  {
   get
   {
    return name;
   }
   set
   {
    name = value;
   }
  }
 
  protected internal  ImportNode Parent
  {
   get
   {
    return parent;
   }
   set
   {
    parent = value;
   }
  }
 
  protected internal  Page ContainingPage
  {
   get
   {
    ImportNode node = this;
    do
    {
     Page containingPage = node as Page;
     if (containingPage != null)
     {
      return containingPage;
     }
     node = node.Parent;
    } while (node != null);
    return null;
   }
  }
 
  protected internal  bool CommitPending
  {
   get
   {
    return commitPending;
   }
   set
   {
    commitPending = value;
    if (commitPending)
    {
     if (parent != null)
      parent.CommitPending = true;
    }
    else
    {
     for (int i = 0; i < children.Count; i++)
     {
      ImportNode child = (ImportNode) children[i];
      child.CommitPending = false;
     }
    }
   }
  }
 
  private  string name;
 
  private  ImportNode parent;
 
  private  ArrayList children = new ArrayList();
 
  private  bool commitPending;

	}

}
