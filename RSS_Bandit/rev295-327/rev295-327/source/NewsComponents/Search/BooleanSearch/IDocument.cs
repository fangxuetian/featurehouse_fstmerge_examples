using System; namespace  NewsComponents.Search.BooleanSearch {
	
 public interface  IDocument {
		
  bool Find(string str); 
  string Name();
	}

}
