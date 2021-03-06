using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Net;
using System.Runtime.Serialization;
using System.Security.Permissions;
using NewsComponents.Net;
using NewsComponents.Resources;
using NewsComponents.Utils;
namespace NewsComponents
{
    public interface IDownloadInfoProvider
    {
        IWebProxy Proxy { get; }
        string GetTargetFolder(DownloadItem item);
        ICredentials GetCredentials(DownloadItem item);
        string InitialDownloadLocation { get; }
    }
    public class BackgroundDownloadManager : IDownloadInfoProvider
    {
        private static readonly AsyncWebRequest asyncWebRequest = null;
        private readonly string applicationId;
        private readonly FeedSource downloadInfoProvider = null;
        private const string AsyncTaskKey = "IsAsyncTask";
        public string InitialDownloadLocation
        {
            get
            {
                return downloadInfoProvider.CacheLocation;
            }
        }
        public IWebProxy Proxy
        {
            get
            {
                return downloadInfoProvider.Proxy;
            }
        }
        public event DownloadStartedEventHandler DownloadStarted;
        public event DownloadProgressEventHandler DownloadProgress;
        public event DownloadErrorEventHandler DownloadError;
        public event DownloadCompletedEventHandler DownloadCompleted;
        public BackgroundDownloadManager(INewsComponentsConfiguration configuration, FeedSource downloadInfoProvider)
        {
            applicationId = configuration.ApplicationID;
            this.downloadInfoProvider = downloadInfoProvider;
            DownloadRegistryManager.Current.SetBaseFolder(configuration.UserLocalApplicationDataPath);
        }
        public BackgroundDownloadManager(string applicationName, FeedSource downloadInfoProvider)
        {
            applicationId = applicationName;
            this.downloadInfoProvider = downloadInfoProvider;
            DownloadRegistryManager.Current.SetBaseFolder(GetUserPath(applicationId));
        }
        static BackgroundDownloadManager()
        {
            asyncWebRequest = new AsyncWebRequest();
        }
        public static AsyncWebRequest AsyncWebRequest
        {
            get
            {
                return asyncWebRequest;
            }
        }
        private static string GetUserPath(string appname)
        {
            string s = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), appname);
            if (!Directory.Exists(s)) Directory.CreateDirectory(s);
            return s;
        }
        public static IDictionary<string, string> GetRelevantHttpHeaders(DownloadFile file, ICredentials credentials, IWebProxy proxy)
        {
            Dictionary<string, string> headers = new Dictionary<string, string>();
            if (file == null || StringHelper.EmptyTrimOrNull(file.Source))
                return headers;
            if (file.Source.StartsWith("http"))
            {
                HttpWebRequest request = (HttpWebRequest) WebRequest.Create(file.Source);
                if (credentials != null)
                    request.Credentials = credentials;
                request.AllowAutoRedirect = true;
                request.Method = "HEAD";
                request.Proxy = proxy;
                WebResponse response = request.GetResponse();
                if (response != null)
                {
                    headers.Add("Content-Length", response.ContentLength.ToString(CultureInfo.InvariantCulture));
                    headers.Add("Content-Disposition", response.Headers["Content-Disposition"]);
                    headers.Add("Content-Type", response.ContentType);
                    response.Close();
                }
            }
            return headers;
        }
        public static DownloadTask[] GetTasks()
        {
            return DownloadRegistryManager.Current.GetTasks();
        }
        private IDownloader GetDownloader(DownloadTask task)
        {
            IDownloader downloader = null;
            if (task.Downloader != null)
            {
                return task.Downloader;
            }
            bool contentLengthSpecified = UpdateTaskFromHttpHeaders(task);
            if (IsOSAtLeastWindowsXP && !FileHelper.IsUncPath(task.DownloadItem.File.Source))
            {
                if (contentLengthSpecified)
                {
                    downloader = new BITSDownloader();
                }
                else if (task.DownloadItem.Enclosure.Length <= (15*1024*1024))
                {
                    downloader = new HttpDownloader();
                }
                if (downloader != null)
                {
                    downloader.DownloadStarted += OnDownloadStarted;
                    downloader.DownloadProgress += OnDownloadProgress;
                    downloader.DownloadCompleted += OnDownloadCompleted;
                    downloader.DownloadError += OnDownloadError;
                }
            }
            task.Downloader = downloader;
            return downloader;
        }
        private static long GetFileSize(DownloadTask task)
        {
            if (task.DownloadItem.File.FileSize > 0)
            {
                return task.DownloadItem.File.FileSize;
            }
            else if (task.DownloadItem.Enclosure.Length > 0)
            {
                return task.DownloadItem.Enclosure.Length;
            }
            else
            {
                return -1;
            }
        }
        private static bool UpdateTaskFromHttpHeaders(DownloadTask task)
        {
            long contentLength = task.DownloadItem.Enclosure.Length;
            try
            {
                IDictionary<string, string> headers = GetRelevantHttpHeaders(task.DownloadItem.File, task.DownloadItem.Credentials,
                                                           task.DownloadItem.Proxy);
                contentLength = long.Parse(headers["Content-Length"], CultureInfo.InvariantCulture);
                if (contentLength > 0)
                {
                    task.DownloadItem.File.FileSize = contentLength;
                }
                if (!string.IsNullOrEmpty(headers["Content-Type"]))
                {
                    task.DownloadItem.File.SuggestedType = new MimeType(headers["Content-Type"]);
                }
                if (!string.IsNullOrEmpty(headers["Content-Disposition"]))
                {
                    string[] components = headers["Content-Disposition"].Split(new char[] {';'});
                    foreach (string s in components)
                    {
                        string str = s.Trim();
                        if (str.StartsWith("filename=") && str.Length > 9)
                        {
                            task.DownloadItem.File.LocalName = str.Substring(9);
                        }
                    }
                }
            }
            catch
            {
                ;
            }
            return (contentLength > 0);
        }
        private static bool IsOSAtLeastWindowsXP
        {
            get
            {
                return
                    (Environment.OSVersion.Platform == PlatformID.Win32NT &&
                     (Environment.OSVersion.Version.Major > 5 ||
                      (Environment.OSVersion.Version.Major == 5 && Environment.OSVersion.Version.Minor >= 1)));
            }
        }
        public string GetTargetFolder(DownloadItem item)
        {
            return downloadInfoProvider.GetEnclosureFolder(item.OwnerFeedId, item.File.LocalName);
        }
        public ICredentials GetCredentials(DownloadItem item)
        {
            return downloadInfoProvider.GetFeedCredentials(item.OwnerFeedId);
        }
        private void SubmitTask(DownloadTask task, TimeSpan maxWaitTime)
        {
            IDownloader downloader = GetDownloader(task);
            if ((downloader == null) || (task.DownloadItem.File == null))
            {
                OnDownloadCompleted(null, new TaskEventArgs(task));
                return;
            }
            try
            {
                downloader.Download(task, maxWaitTime);
            }
            finally
            {
                Release(downloader);
            }
        }
        private void SubmitTaskAsync(DownloadTask task)
        {
            IDownloader downloader = GetDownloader(task);
            if (downloadInfoProvider.EnclosureCacheSize != Int32.MaxValue)
            {
                long limitInBytes = downloadInfoProvider.EnclosureCacheSize*1024*1024;
                long filesize = GetFileSize(task);
                DirectoryInfo targetDir = new DirectoryInfo(downloadInfoProvider.EnclosureFolder);
                long spaceUsed = FileHelper.GetSize(targetDir);
                if (!downloadInfoProvider.EnclosureFolder.Equals(downloadInfoProvider.PodcastFolder))
                {
                    DirectoryInfo podcastDir = new DirectoryInfo(downloadInfoProvider.EnclosureFolder);
                    spaceUsed += FileHelper.GetSize(podcastDir);
                }
                if ((filesize + spaceUsed) > limitInBytes)
                {
                    DownloadRegistryManager.Current.UnRegisterTask(task);
                    string fileName = task.DownloadItem.File.LocalName;
                    int limit = downloadInfoProvider.EnclosureCacheSize;
                    throw new DownloaderException(ComponentsText.ExceptionEnclosureCacheLimitReached(fileName, limit));
                }
            }
            if ((downloader == null) || (task.DownloadItem.File == null))
            {
                OnDownloadCompleted(null, new TaskEventArgs(task));
            }
            else
            {
                downloader.BeginDownload(task);
            }
        }
        public bool EndTask(DownloadTask task)
        {
            IDownloader downloader = GetDownloader(task);
            bool success = true;
            if (downloader != null)
            {
                try
                {
                    success = downloader.CancelDownload(task);
                }
                finally
                {
                    Release(downloader);
                }
            }
            return success;
        }
        public void ResumePendingDownloads()
        {
            foreach (DownloadTask task in DownloadRegistryManager.Current.GetTasks())
            {
                switch (task.State)
                {
                    case DownloadTaskState.DownloadError:
                    case DownloadTaskState.Downloading:
                        task.Init(task.DownloadItem, this);
                        SubmitTaskAsync(task);
                        break;
                    case DownloadTaskState.Downloaded:
                        DownloadRegistryManager.Current.UnRegisterTask(task);
                        break;
                }
            }
        }
        public void CancelPendingDownloads(string ownerId)
        {
            DownloadTask[] pendingTasks = DownloadRegistryManager.Current.GetByOwnerId(ownerId);
            foreach (DownloadTask task in pendingTasks)
            {
                GetDownloader(task).CancelDownload(task);
                DownloadRegistryManager.Current.UnRegisterTask(task);
            }
        }
        public void CancelPendingDownloads()
        {
            DownloadTask[] pendingTasks = DownloadRegistryManager.Current.GetTasks();
            foreach (DownloadTask task in pendingTasks)
            {
                GetDownloader(task).CancelDownload(task);
                DownloadRegistryManager.Current.UnRegisterTask(task);
            }
        }
        public void Download(DownloadItem[] items, TimeSpan maxWaitTime)
        {
            foreach (DownloadItem item in items)
            {
                Download(item, maxWaitTime);
            }
        }
        public void Download(DownloadItem item, TimeSpan maxWaitTime)
        {
            DownloadTask task = new DownloadTask(item, this);
            if (DownloadRegistryManager.Current.TaskAlreadyExists(task))
            {
                DownloadRegistryManager.Current.RegisterTask(task);
                task[AsyncTaskKey] = false;
                SubmitTask(task, maxWaitTime);
            }
        }
        public void BeginDownload(DownloadItem[] items)
        {
            foreach (DownloadItem item in items)
            {
                BeginDownload(item);
            }
        }
        public void BeginDownload(DownloadItem item)
        {
            DownloadTask task = new DownloadTask(item, this);
            if (!DownloadRegistryManager.Current.TaskAlreadyExists(task))
            {
                DownloadRegistryManager.Current.RegisterTask(task);
                task[AsyncTaskKey] = true;
                SubmitTaskAsync(task);
            }
        }
        public void CancelDownload(DownloadItem item)
        {
            DownloadTask task = DownloadRegistryManager.Current.GetByItemID(item.ItemId);
            if (task != null)
            {
                switch (task.State)
                {
                    case DownloadTaskState.None:
                        {
                            DownloadRegistryManager.Current.UnRegisterTask(task);
                            break;
                        }
                    case DownloadTaskState.DownloadError:
                    case DownloadTaskState.Downloading:
                        {
                            EndTask(task);
                            DownloadRegistryManager.Current.UnRegisterTask(task);
                            break;
                        }
                    case DownloadTaskState.Downloaded:
                        {
                            DownloadRegistryManager.Current.UnRegisterTask(task);
                            break;
                        }
                }
                lock (task.SyncRoot)
                {
                    task.State = DownloadTaskState.Cancelled;
                }
            }
        }
        private void OnDownloadCompleted(object sender, TaskEventArgs e)
        {
            string fileLocation = Path.Combine(e.Task.DownloadFilesBase, e.Task.DownloadItem.File.LocalName);
            string finalLocation = Path.Combine(e.Task.DownloadItem.TargetFolder, e.Task.DownloadItem.File.LocalName);
            DownloadRegistryManager.Current.UnRegisterTask(e.Task);
            try
            {
                FileStreams FS = new FileStreams(fileLocation);
                int i = FS.IndexOf("Zone.Identifier");
                if (i != -1)
                {
                    FS.Remove("Zone.Identifier");
                }
                FS.Add("Zone.Identifier");
                FileStream fs = FS["Zone.Identifier"].Open(FileMode.OpenOrCreate, FileAccess.Write);
                StreamWriter writer = new StreamWriter(fs);
                writer.WriteLine("[ZoneTransfer]");
                writer.WriteLine("ZoneId=3");
                writer.Close();
                fs.Close();
                if (!Directory.Exists(e.Task.DownloadItem.TargetFolder))
                {
                    Directory.CreateDirectory(e.Task.DownloadItem.TargetFolder);
                }
                FileHelper.MoveFile(fileLocation, finalLocation, MoveFileFlag.CopyAllowed | MoveFileFlag.ReplaceExisting);
                if (DownloadCompleted != null)
                {
                    DownloadCompleted(this, new DownloadItemEventArgs(e.Task.DownloadItem));
                }
                if (sender is IDownloader)
                {
                    IDownloader downloader = (IDownloader) sender;
                    Release(downloader);
                }
            }
            catch (Exception error)
            {
                OnDownloadError(this, new DownloadTaskErrorEventArgs(e.Task, error));
                return;
            }
        }
        private void OnDownloadError(object sender, DownloadTaskErrorEventArgs e)
        {
            DownloadRegistryManager.Current.UnRegisterTask(e.Task);
            if (DownloadError != null)
            {
                DownloadError(this, new DownloadItemErrorEventArgs(e.Task.DownloadItem, e.Exception));
            }
            IDownloader downloader = (IDownloader) sender;
            Release(downloader);
        }
        private void OnDownloadProgress(object sender, DownloadTaskProgressEventArgs e)
        {
            if (DownloadProgress != null)
            {
                DownloadProgressEventArgs eventArgs = new DownloadProgressEventArgs(
                    e.BytesTotal,
                    e.BytesTransferred,
                    e.FilesTotal,
                    e.FilesTransferred,
                    e.Task.DownloadItem);
                DownloadProgress(this, eventArgs);
                if (eventArgs.Cancel)
                {
                    CancelDownload(e.Task.DownloadItem);
                }
            }
        }
        private void OnDownloadStarted(object sender, TaskEventArgs e)
        {
            e.Task.State = DownloadTaskState.Downloading;
            DownloadRegistryManager.Current.UpdateTask(e.Task);
            if (DownloadStarted != null)
            {
                DownloadStartedEventArgs eventArgs = new DownloadStartedEventArgs(e.Task.DownloadItem);
                DownloadStarted(this, eventArgs);
                if (eventArgs.Cancel)
                {
                    CancelDownload(eventArgs.DownloadItem);
                }
            }
        }
        private void Release(IDownloader downloader)
        {
            downloader.DownloadStarted -= OnDownloadStarted;
            downloader.DownloadProgress -= OnDownloadProgress;
            downloader.DownloadCompleted -= OnDownloadCompleted;
            downloader.DownloadError -= OnDownloadError;
            IDisposable disposable = downloader as IDisposable;
            if (disposable != null)
            {
                disposable.Dispose();
            }
        }
    }
    public interface IDownloader
    {
        void Download(DownloadTask task, TimeSpan maxWaitTime);
        void BeginDownload(DownloadTask task);
        bool CancelDownload(DownloadTask task);
        event DownloadTaskProgressEventHandler DownloadProgress;
        event DownloadTaskStartedEventHandler DownloadStarted;
        event DownloadTaskCompletedEventHandler DownloadCompleted;
        event DownloadTaskErrorEventHandler DownloadError;
    }
    [Serializable]
    public class DownloaderException : Exception
    {
        public DownloaderException()
        {
        }
        public DownloaderException(string message) : base(message)
        {
        }
        public DownloaderException(string message, Exception innerException) : base(message, innerException)
        {
        }
        [SecurityPermission(SecurityAction.Demand, SerializationFormatter=true)]
        protected DownloaderException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}
