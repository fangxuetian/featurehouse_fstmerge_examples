using System; 
using System.ComponentModel; namespace  System.Windows.Forms.ThListView {
	
 [TypeConverter(typeof(ThreadedListViewGroupConverter))] 
 public class  ThreadedListViewGroup {
		
  private  string _text;
 
  private  int _index;
 
  public  ThreadedListViewGroup() {
  }
 
  public  ThreadedListViewGroup(string text, int index) {
   _text = text;
   _index = index;
  }
 
  public  ThreadedListViewGroup(string text) {
   _text = text;
  }
 
  public  string GroupText {
   get {
    return _text;
   }
   set {
    _text = value;
   }
  }
 
  public  int GroupIndex {
   get {
    return _index;
   }
   set {
    _index = value;
   }
  }
 
  public override  string ToString() {
   return _text;
  }

	}

}
