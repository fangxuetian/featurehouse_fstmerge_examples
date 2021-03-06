using System; 
using System.IO; 
using Lucene.Net.Store; 
using Directory=Lucene.Net.Store.Directory; 
using Logger = RssBandit.Common.Logging; namespace  NewsComponents.Search {
	
 internal class  LuceneSettings {
		
  private  const string IndexFolderName = "index"; 
  private  IPersistedSettings settings;
 
  private  SearchIndexBehavior behavior;
 
  private  string indexPath;
 
  public  LuceneSettings(INewsComponentsConfiguration configuration) {
   settings = configuration.PersistedSettings;
   behavior = configuration.SearchIndexBehavior;
   indexPath = BuildAndCreateIndexDirectoryPath(configuration);
  }
 
  public  bool IsFileBasedSearch {
   get {
    return (this.behavior != SearchIndexBehavior.NoIndexing) && (
            this.behavior == SearchIndexBehavior.AppDataDirectoryBased ||
            this.behavior == SearchIndexBehavior.LocalAppDataDirectoryBased ||
            this.behavior == SearchIndexBehavior.TempDirectoryBased);
   }
  }
 
  public  bool IsRAMBasedSearch {
   get {
    return false;
   }
  }
 
  internal  string IndexPath {
   get { return this.indexPath; }
  }
 
        internal  Directory GetIndexDirectory() {
            return this.GetIndexDirectory(false);
        }
 
        internal  Directory GetIndexDirectory(bool create) {
            if (IsRAMBasedSearch) {
                return new RAMDirectory();
            } else {
                return FSDirectory.GetDirectory(this.indexPath, create);
            }
        }
 
  public  SearchIndexBehavior SearchIndexBehavior {
   get { return this.behavior; }
  }
 
  public  DateTime LastIndexOptimization {
   get {
    return (DateTime)settings.GetProperty("Lucene.LastIndexOptimization", typeof(DateTime), DateTime.MinValue);
   }
   set {
    settings.SetProperty("Lucene.LastIndexOptimization", value);
   }
  }
 
  public override  string ToString()
  {
   if (IsRAMBasedSearch)
    return "Index@RAM";
   else
    return "Index@"+ this.indexPath;
  }
 
  private static  string BuildAndCreateIndexDirectoryPath(INewsComponentsConfiguration configuration)
  {
   string path = null;
   if (configuration.SearchIndexBehavior != SearchIndexBehavior.NoIndexing)
   {
    switch (configuration.SearchIndexBehavior) {
     case SearchIndexBehavior.LocalAppDataDirectoryBased:
      {
       path = Path.Combine(configuration.UserLocalApplicationDataPath,
                           IndexFolderName);
       break;
      }
     case SearchIndexBehavior.AppDataDirectoryBased:
      {
       path = Path.Combine(configuration.UserApplicationDataPath,
                           IndexFolderName);
       break;
      }
     case SearchIndexBehavior.TempDirectoryBased:
      {
       path = Path.Combine(Path.GetTempPath(),
                           String.Format("{0}.{1}", configuration.ApplicationID, IndexFolderName));
       break;
      }
    }
    if (path != null) {
     if (!System.IO.Directory.Exists(path))
      System.IO.Directory.CreateDirectory(path);
    }
   }
   return path;
  }

	}

}
