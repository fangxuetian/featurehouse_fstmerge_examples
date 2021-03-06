using System; 
using System.Collections; 
using System.Globalization; 
using System.IO; 
using System.Runtime.Serialization.Formatters.Binary; 
using System.Security.Permissions; 
using NewsComponents.Utils; namespace  NewsComponents.Net {
	
 [Serializable] 
 internal sealed class  DownloadRegistryManager {
		
  private static readonly  DownloadRegistryManager instance = new DownloadRegistryManager();
 
  private static readonly  log4net.ILog Logger = RssBandit.Common.Logging.Log.GetLogger(typeof(DownloadRegistryManager));
 
  private  const string root = "download.registry"; 
  private  DirectoryInfo rootDirInfo;
 
  private  Hashtable registry = new Hashtable( 10 );
 
  private  bool loaded = false;
 
  public static  DownloadRegistryManager Current {
   get { return instance; }
  }
 
  private  DownloadRegistryManager() {
  }
 
  public  void SetBaseFolder(string baseFolder) {
   string path = Path.Combine( baseFolder, root );
   if ( !Directory.Exists( path ) ) {
    rootDirInfo = Directory.CreateDirectory( path );
   }
   else {
    rootDirInfo = new DirectoryInfo( path );
   }
  }
 
  public  void Load() {
   foreach( FileInfo fi in RootDir.GetFiles() ) {
    this.LoadTask( fi.FullName );
   }
  }
 
  public  void UpdateTask(DownloadTask task) {
    SaveTask( task );
  }
 
  public  bool TaskAlreadyExists(DownloadTask task){
   return Tasks.ContainsKey(task.DownloadItem.Enclosure.Url);
  }
 
  public  void RegisterTask(DownloadTask task) {
   lock( Tasks.SyncRoot ) {
    if ( !Tasks.ContainsKey( task.DownloadItem.Enclosure.Url ) ) {
     Tasks.Add( task.DownloadItem.Enclosure.Url, task );
    }
   }
   SaveTask( task );
  }
 
  public  void UnRegisterTask(DownloadTask task) {
   lock( Tasks.SyncRoot ) {
    Tasks.Remove( task.DownloadItem.Enclosure.Url );
   }
   string fileName = Path.Combine( RootDir.FullName, task.TaskId.ToString() + ".task" );
   FileHelper.DestroyFile( fileName );
  }
 
  public  DownloadTask[] GetTasks() {
   DownloadTask[] result = new DownloadTask[ Tasks.Count ];
   Tasks.Values.CopyTo( result, 0 );
   return result;
  }
 
  public  DownloadTask[] GetByOwnerId(string ownerId) {
   ArrayList tasks = new ArrayList();
   lock( Tasks.SyncRoot ) {
    foreach( DownloadTask task in Tasks.Values ) {
     if ( task.DownloadItem.OwnerFeedId == ownerId ) {
      tasks.Add( task );
     }
    }
   }
   return (DownloadTask[])tasks.ToArray( typeof(DownloadTask) );
  }
 
  public  DownloadTask[] GetByOwnerItemId(string ownerItemId) {
   ArrayList tasks = new ArrayList();
   lock( Tasks.SyncRoot ) {
    foreach( DownloadTask task in Tasks.Values ) {
     if ( task.DownloadItem.OwnerItemId == ownerItemId ) {
      tasks.Add( task );
     }
    }
   }
   return (DownloadTask[])tasks.ToArray( typeof(DownloadTask) );
  }
 
  public  DownloadTask GetByItemID(Guid itemId) {
   lock( Tasks.SyncRoot ) {
    foreach( DownloadTask task in Tasks.Values ) {
     if ( task.DownloadItem.ItemId == itemId ) {
      return task;
     }
    }
   }
   return null;
  }
 
  private  DirectoryInfo RootDir {
   get {
    if (rootDirInfo != null)
     return rootDirInfo;
    SetBaseFolder(Path.GetTempPath());
    return rootDirInfo;
   }
  }
 
  [System.Security.Permissions.SecurityPermission( SecurityAction.Demand, SerializationFormatter=true )] 
  private  DownloadTask LoadTask( string taskFilePath ) {
   DownloadTask task = null;
   BinaryFormatter formatter = new BinaryFormatter();
   using( FileStream stream = new FileStream( taskFilePath, FileMode.Open, FileAccess.Read, FileShare.Read) ) {
    try{
     task = (DownloadTask)formatter.Deserialize( stream );
     lock( registry.SyncRoot ) {
      if( task.State != DownloadTaskState.Downloaded && !registry.ContainsKey( task.DownloadItem.Enclosure.Url ) ) {
       registry.Add( task.DownloadItem.Enclosure.Url , task );
      }else{
       stream.Close();
       string fileName = Path.Combine( RootDir.FullName, task.TaskId.ToString() + ".task" );
       FileHelper.DestroyFile( fileName );
      }
     }
    }catch(Exception e){
     Logger.Error("Error in DownloadRegistryManager.LoadTask():", e);
    }
   }
   return task;
  }
 
  [System.Security.Permissions.SecurityPermission(SecurityAction.Demand, SerializationFormatter=true)] 
  private  void SaveTask( DownloadTask task ) {
   string filename = Path.Combine( RootDir.FullName, String.Format( CultureInfo.InvariantCulture, "{0}.task", task.TaskId.ToString() ) );
   try {
    using(Stream stream = new FileStream( filename, FileMode.OpenOrCreate, FileAccess.Write, FileShare.Read ) ) {
     BinaryFormatter formatter = new BinaryFormatter();
     formatter.Serialize( stream, task );
    }
   }
   catch( Exception ex) {
    File.Delete( filename );
    Logger.Error( ex );
    throw;
   }
  }
 
  private  Hashtable Tasks {
   get {
    if ( !loaded ) {
     Load();
     loaded = true;
    }
    return registry;
   }
  }

	}

}
