using System;
using System.Collections.Generic;
using System.IO;
using System.Timers;
namespace ThoughtWorks.CruiseControl.Core.Util
{
 public class FileChangedWatcher : IFileWatcher
 {
  private List<FileSystemWatcher> watchers= new List< FileSystemWatcher >( );
  private Timer timer;
     private FileSystemEventArgs firstArgs = null;
  public FileChangedWatcher(params string[] filenames)
  {
            for( int idx = 0; idx < filenames.Length; ++idx )
            {
                AddWatcher( filenames[ idx ] );
            }
            timer = new Timer(500);
   timer.AutoReset = false;
   timer.Elapsed += HandleTimerElapsed;
  }
     public event FileSystemEventHandler OnFileChanged;
     public void AddWatcher (string filename)
     {
         FileSystemWatcher watcher = new FileSystemWatcher();
            watchers.Add( watcher );
         watcher.Filter = Path.GetFileName( filename );
         watcher.Path = new FileInfo(filename).DirectoryName;
         watcher.Changed += HandleFileChanged;
         watcher.Renamed += HandleFileChanged;
         watcher.EnableRaisingEvents = true;
     }
     private void HandleFileChanged(object sender, FileSystemEventArgs args)
  {
         firstArgs = firstArgs ?? args;
   timer.Start();
  }
  private void HandleFileChanged(object sender, RenamedEventArgs args)
  {
            firstArgs = firstArgs ?? args;
   timer.Start();
  }
  private void HandleTimerElapsed(object sender, ElapsedEventArgs args)
  {
   timer.Stop();
            Log.Info( "Config file modification detected for  " + firstArgs.FullPath );
   OnFileChanged(sender, firstArgs);
      firstArgs = null;
  }
  void IDisposable.Dispose()
  {
      foreach ( FileSystemWatcher watcher in watchers )
      {
                watcher.EnableRaisingEvents = false;
       watcher.Dispose();
      }
  }
 }
}
