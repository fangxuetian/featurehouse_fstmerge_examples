using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Net;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Xml;
using System.Xml.Schema;
using System.Xml.Serialization;
using System.Xml.Xsl;
using log4net;
using NewsComponents.Collections;
using NewsComponents.Feed;
using NewsComponents.Net;
using NewsComponents.News;
using NewsComponents.RelationCosmos;
using NewsComponents.Resources;
using NewsComponents.Search;
using NewsComponents.Storage;
using NewsComponents.Threading;
using NewsComponents.Utils;
using RssBandit.Common;
using RssBandit.Common.Logging;
using RC = NewsComponents.RelationCosmos;
namespace NewsComponents
{
    public enum FeedListFormat
    {
        OCS,
        OPML,
        NewsHandler,
        NewsHandlerLite,
    }
    public class NewsHandler
    {
        static NewsHandler()
        {
            StringBuilder sb = new StringBuilder(200);
            sb.Append("{0}");
            sb.Append(" (.NET CLR ");
            sb.Append(Environment.Version);
            sb.Append("; ");
            sb.Append(Environment.OSVersion.ToString().Replace("Microsoft Windows ", "Win"));
            sb.Append("; http://www.rssbandit.org");
            sb.Append(")");
            userAgentTemplate = sb.ToString();
        }
        public NewsHandler() :
            this(NewsComponentsConfiguration.Default)
        {
        }
        public NewsHandler(INewsComponentsConfiguration configuration)
        {
            this.configuration = configuration;
            if (this.configuration == null)
                this.configuration = new NewsComponentsConfiguration();
            ValidateAndThrow(this.configuration);
            this.LoadFeedlistSchema();
            this.rssParser = new RssParser(this);
            this.searchHandler = new LuceneSearch(this.configuration, this);
            this.PodcastFolder = this.configuration.DownloadedFilesDataPath;
            this.EnclosureFolder = this.configuration.DownloadedFilesDataPath;
            if (this.EnclosureFolder != null)
            {
                this.enclosureDownloader = new BackgroundDownloadManager(this.configuration, this);
                this.enclosureDownloader.DownloadCompleted += this.OnEnclosureDownloadComplete;
            }
            this.AsyncWebRequest = new AsyncWebRequest();
            this.AsyncWebRequest.OnAllRequestsComplete += this.OnAllRequestsComplete;
        }
        private readonly INewsComponentsConfiguration configuration = null;
        public INewsComponentsConfiguration Configuration
        {
            get
            {
                return this.configuration;
            }
        }
        private static void ValidateAndThrow(INewsComponentsConfiguration configuration)
        {
            if (configuration == null)
                throw new ArgumentNullException("configuration");
            if (string.IsNullOrEmpty(configuration.ApplicationID))
                throw new InvalidOperationException(
                    "INewsComponentsConfiguration.ApplicationID cannot be null or empty.");
            if (configuration.CacheManager == null)
                throw new InvalidOperationException("INewsComponentsConfiguration.CacheManager cannot be null.");
            if (configuration.PersistedSettings == null)
                throw new InvalidOperationException("INewsComponentsConfiguration.PersistedSettings cannot be null.");
            if (string.IsNullOrEmpty(configuration.UserApplicationDataPath))
                throw new InvalidOperationException(
                    "INewsComponentsConfiguration.UserApplicationDataPath cannot be null or empty.");
            if (string.IsNullOrEmpty(configuration.UserLocalApplicationDataPath))
                throw new InvalidOperationException(
                    "INewsComponentsConfiguration.UserLocalApplicationDataPath cannot be null or empty.");
        }
        internal CacheManager CacheHandler
        {
            get
            {
                return configuration.CacheManager;
            }
        }
        private readonly AsyncWebRequest AsyncWebRequest = null;
        private static DateTime ApplicationStartTime = DateTime.Now;
        public bool DownloadIntervalReached
        {
            get
            {
                return (DateTime.Now - ApplicationStartTime).TotalMilliseconds >= this.RefreshRate;
            }
        }
        private readonly BackgroundDownloadManager enclosureDownloader;
        internal string CacheLocation
        {
            get
            {
                return this.CacheHandler.CacheLocation;
            }
        }
        private readonly RssParser rssParser;
        internal RssParser RssParser
        {
            get
            {
                return this.rssParser;
            }
        }
        private LuceneSearch searchHandler;
        public LuceneSearch SearchHandler
        {
            get
            {
                return this.searchHandler;
            }
            set
            {
                this.searchHandler = value;
            }
        }
        public static readonly List<NewsItem> EmptyItemList = new List<NewsItem>(0);
        private static readonly ILog _log = Log.GetLogger(typeof (NewsHandler));
        private static readonly IRelationCosmos relationCosmos = RelationCosmosFactory.Create();
        private static readonly NewsChannelServices receivingNewsChannel = new NewsChannelServices();
        private IWebProxy proxy = WebRequest.DefaultWebProxy;
        public IWebProxy Proxy
        {
            set
            {
                proxy = value;
                RssParser.GlobalProxy = value;
            }
            get
            {
                return proxy;
            }
        }
        private static bool setCookies = true;
        public static bool SetCookies
        {
            set
            {
                setCookies = value;
            }
            get
            {
                return setCookies;
            }
        }
        internal static bool buildRelationCosmos = true;
        public static bool BuildRelationCosmos
        {
            set
            {
                buildRelationCosmos = value;
                if (buildRelationCosmos == false)
                    relationCosmos.Clear();
            }
            get
            {
                return buildRelationCosmos;
            }
        }
        private bool offline = false;
        public bool Offline
        {
            set
            {
                offline = value;
                RssParser.Offline = value;
            }
            get
            {
                return offline;
            }
        }
        private static bool categoryMismatch = false;
        private static bool traceMode = false;
        public static bool TraceMode
        {
            set
            {
                traceMode = value;
            }
            get
            {
                return traceMode;
            }
        }
        private static void Trace(string formatString, params object[] paramArray)
        {
            if (traceMode)
                _log.Info(String.Format(formatString, paramArray));
        }
        private static bool unconditionalCommentRss = false;
        public static bool UnconditionalCommentRss
        {
            set
            {
                unconditionalCommentRss = value;
            }
            get
            {
                return unconditionalCommentRss;
            }
        }
        private static bool topStoriesModified = false;
        public static bool TopStoriesModified
        {
            get
            {
                return topStoriesModified;
            }
        }
        private class storyNdate
        {
            public storyNdate(string title, DateTime date)
            {
                storyTitle = title;
                firstSeen = date;
            }
            public readonly string storyTitle;
            public readonly DateTime firstSeen;
        }
        private static readonly Dictionary<string, storyNdate> TopStoryTitles = new Dictionary<string, storyNdate>();
        public static ICredentials CreateCredentialsFrom(NewsFeed f)
        {
            if (f != null && !string.IsNullOrEmpty(f.authUser))
            {
                string u = null, p = null;
                GetFeedCredentials(f, ref u, ref p);
                return CreateCredentialsFrom(f.link, u, p);
            }
            return null;
        }
        public static ICredentials CreateCredentialsFrom(string url, string domainUser, string password)
        {
            ICredentials c = null;
            if (!string.IsNullOrEmpty(domainUser))
            {
                NetworkCredential credentials = CreateCredentialsFrom(domainUser, password);
                try
                {
                    Uri feedUri = new Uri(url);
                    CredentialCache cc = new CredentialCache();
                    cc.Add(feedUri, "Basic", credentials);
                    cc.Add(feedUri, "Digest", credentials);
                    cc.Add(feedUri, "NTLM", credentials);
                    c = cc;
                }
                catch (UriFormatException)
                {
                    c = credentials;
                }
            }
            return c;
        }
        public static NetworkCredential CreateCredentialsFrom(string domainUser, string password)
        {
            NetworkCredential c = null;
            if (domainUser != null)
            {
                NetworkCredential credentials;
                string[] aDomainUser = domainUser.Split(new char[] {'\\'});
                if (aDomainUser.GetLength(0) > 1)
                    credentials = new NetworkCredential(aDomainUser[1], password, aDomainUser[0]);
                else
                    credentials = new NetworkCredential(aDomainUser[0], password);
                c = credentials;
            }
            return c;
        }
        public static void SetFeedCredentials(NewsFeed f, string user, string pwd)
        {
            if (f == null) return;
            f.authPassword = CryptHelper.EncryptB(pwd);
            f.authUser = user;
        }
        public static void GetFeedCredentials(NewsFeed f, ref string user, ref string pwd)
        {
            if (f == null) return;
            pwd = CryptHelper.Decrypt(f.authPassword);
            user = f.authUser;
        }
        public ICredentials GetFeedCredentials(string feedUrl)
        {
            if (feedUrl != null && FeedsTable.ContainsKey(feedUrl))
                return GetFeedCredentials(FeedsTable[feedUrl]);
            return null;
        }
        public static ICredentials GetFeedCredentials(NewsFeed f)
        {
            ICredentials c = null;
            if (f != null && f.authUser != null)
            {
                return CreateCredentialsFrom(f);
            }
            return c;
        }
        public static void SetNntpServerCredentials(INntpServerDefinition sd, string user, string pwd)
        {
            NntpServerDefinition server = (NntpServerDefinition) sd;
            if (server == null) return;
            server.AuthPassword = CryptHelper.EncryptB(pwd);
            server.AuthUser = user;
        }
        public static void GetNntpServerCredentials(INntpServerDefinition sd, ref string user, ref string pwd)
        {
            NntpServerDefinition server = (NntpServerDefinition) sd;
            if (server == null) return;
            pwd = (server.AuthPassword != null ? CryptHelper.Decrypt(server.AuthPassword) : null);
            user = server.AuthUser;
        }
        public ICredentials GetNntpServerCredentials(string serverAccountName)
        {
            if (serverAccountName != null && nntpServers.ContainsKey(serverAccountName))
                return GetFeedCredentials(nntpServers[serverAccountName]);
            return null;
        }
        internal ICredentials GetNntpServerCredentials(NewsFeed f)
        {
            ICredentials c = null;
            if (f == null || ! RssHelper.IsNntpUrl(f.link))
                return c;
            try
            {
                Uri feedUri = new Uri(f.link);
                foreach (NntpServerDefinition nsd in this.nntpServers.Values)
                {
                    if (nsd.Server.Equals(feedUri.Authority))
                    {
                        c = this.GetNntpServerCredentials(nsd.Name);
                        break;
                    }
                }
            }
            catch (UriFormatException)
            {
                ;
            }
            return c;
        }
        public static ICredentials GetFeedCredentials(INntpServerDefinition sd)
        {
            ICredentials c = null;
            if (sd.AuthUser != null)
            {
                string u = null, p = null;
                GetNntpServerCredentials(sd, ref u, ref p);
                c = CreateCredentialsFrom(u, p);
            }
            return c;
        }
        public static string GetUserPath(string appname)
        {
            string s = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData), appname);
            if (!Directory.Exists(s)) Directory.CreateDirectory(s);
            return s;
        }
        private TimeSpan maxitemage = new TimeSpan(90, 0, 0, 0);
  public TimeSpan MaxItemAge
  {
   get
   {
    return this.maxitemage;
   }
   set
   {
    this.maxitemage = value;
   }
  }
  [MethodImpl(MethodImplOptions.Synchronized)]
  public void ClearAllMaxItemAgeSettings()
  {
   string[] keys;
   lock (_feedsTable)
   {
    keys = new string[_feedsTable.Count];
    if (_feedsTable.Count > 0)
     _feedsTable.Keys.CopyTo(keys, 0);
   }
   for (int i = 0, len = keys.Length; i < len; i++)
   {
    NewsFeed f;
    if (_feedsTable.TryGetValue(keys[i], out f))
    {
     f.maxitemage = null;
    }
   }
   foreach (category c in this.categories.Values)
   {
    c.maxitemage = null;
   }
  }
        private string stylesheet;
        public string Stylesheet
        {
            get
            {
                return this.stylesheet;
            }
            set
            {
                this.stylesheet = value;
            }
        }
        private string enclosurefolder;
        public string EnclosureFolder
        {
            get
            {
                return this.enclosurefolder;
            }
            set
            {
                this.enclosurefolder = value;
            }
        }
        private readonly ArrayList podcastfileextensions = new ArrayList();
        public string PodcastFileExtensionsAsString
        {
            get
            {
                StringBuilder toReturn = new StringBuilder();
                foreach (string s in this.podcastfileextensions)
                {
                    if (!StringHelper.EmptyTrimOrNull(s))
                    {
                        toReturn.Append(s);
                        toReturn.Append(";");
                    }
                }
                return toReturn.ToString();
            }
            set
            {
                string[] fileexts = value.Split(new char[] {';', ' '});
                this.podcastfileextensions.Clear();
                foreach (string s in fileexts)
                {
                    this.podcastfileextensions.Add(s);
                }
            }
        }
        private string podcastfolder;
        public string PodcastFolder
        {
            get
            {
                return this.podcastfolder;
            }
            set
            {
                this.podcastfolder = value;
            }
        }
        private bool markitemsreadonexit;
        public bool MarkItemsReadOnExit
        {
            get
            {
                return this.markitemsreadonexit;
            }
            set
            {
                this.markitemsreadonexit = value;
            }
        }
        private bool downloadenclosures;
        public bool DownloadEnclosures
        {
            get
            {
                return this.downloadenclosures;
            }
            set
            {
                this.downloadenclosures = value;
            }
        }
        private int enclosurecachesize = Int32.MaxValue;
        public int EnclosureCacheSize
        {
            get
            {
                return this.enclosurecachesize;
            }
            set
            {
                this.enclosurecachesize = value;
            }
        }
        private int numtodownloadonnewfeed = Int32.MaxValue;
        public int NumEnclosuresToDownloadOnNewFeed
        {
            get
            {
                return this.numtodownloadonnewfeed;
            }
            set
            {
                this.numtodownloadonnewfeed = value;
            }
        }
        private bool createsubfoldersforenclosures;
        public bool CreateSubfoldersForEnclosures
        {
            get
            {
                return this.createsubfoldersforenclosures;
            }
            set
            {
                this.createsubfoldersforenclosures = value;
            }
        }
        private bool enclosurealert;
        public bool EnclosureAlert
        {
            get
            {
                return this.enclosurealert;
            }
            set
            {
                this.enclosurealert = value;
            }
        }
        private string listviewlayout;
        public string FeedColumnLayout
        {
            get
            {
                return this.listviewlayout;
            }
            set
            {
                this.listviewlayout = value;
            }
        }
        public const string DefaultUserAgent = "NewsHandler 1.1";
        private static readonly string userAgentTemplate;
        private static string globalLongUserAgent;
        public static string UserAgentString(string userAgent)
        {
            if (string.IsNullOrEmpty(userAgent))
                return GlobalUserAgentString;
            return String.Format(userAgentTemplate, userAgent);
        }
        public static string GlobalUserAgentString
        {
            get
            {
                if (null == globalLongUserAgent)
                    globalLongUserAgent = UserAgentString(DefaultUserAgent);
                return globalLongUserAgent;
            }
        }
        private string useragent = DefaultUserAgent;
        public string UserAgent
        {
            get
            {
                return useragent;
            }
            set
            {
                useragent = value;
                globalLongUserAgent = UserAgentString(useragent);
            }
        }
        public string FullUserAgent
        {
            get
            {
                return UserAgentString(this.UserAgent);
            }
        }
        private int numTitlesToDownload = 0;
        private ManualResetEvent eventX;
        private IDictionary<string, NewsFeed> _feedsTable = new SortedDictionary<string, NewsFeed>(UriHelper.Comparer);
        private CategoriesCollection categories = new CategoriesCollection();
        private FeedColumnLayoutCollection layouts = new FeedColumnLayoutCollection();
        private readonly Dictionary<string, FeedDetailsInternal> itemsTable =
            new Dictionary<string, FeedDetailsInternal>();
        private IDictionary<string, INntpServerDefinition> nntpServers = new Dictionary<string, INntpServerDefinition>();
        private IDictionary<string, UserIdentity> identities = new Dictionary<string, UserIdentity>();
        public delegate void DownloadFeedStartedCallback(object sender, DownloadFeedCancelEventArgs e);
        public event DownloadFeedStartedCallback BeforeDownloadFeedStarted = null;
        [ComVisible(false)]
        public class DownloadFeedCancelEventArgs : CancelEventArgs
        {
            public DownloadFeedCancelEventArgs(Uri feed, bool cancel) : base(cancel)
            {
                this.feedUri = feed;
            }
            private readonly Uri feedUri;
            public Uri FeedUri
            {
                get
                {
                    return feedUri;
                }
            }
        }
        public delegate void UpdatedFeedCallback(object sender, UpdatedFeedEventArgs e);
        public event UpdatedFeedCallback OnUpdatedFeed = null;
        public delegate void DownloadedEnclosureCallback(object sender, DownloadItemEventArgs e);
        public event DownloadedEnclosureCallback OnDownloadedEnclosure = null;
        public delegate void UpdatedFaviconCallback(object sender, UpdatedFaviconEventArgs e);
        public event UpdatedFaviconCallback OnUpdatedFavicon = null;
        public class UpdatedFaviconEventArgs : EventArgs
        {
            public UpdatedFaviconEventArgs(string favicon, StringCollection feedUrls)
            {
                this.favicon = favicon;
                this.feedUrls = feedUrls;
            }
            private readonly string favicon;
            public string Favicon
            {
                get
                {
                    return this.favicon;
                }
            }
            private readonly StringCollection feedUrls;
            public StringCollection FeedUrls
            {
                get
                {
                    return this.feedUrls;
                }
            }
        }
        public class UpdatedFeedEventArgs : EventArgs
        {
            public UpdatedFeedEventArgs(Uri requestUri, Uri newUri, RequestResult result, int priority,
                                        bool firstSuccessfulDownload)
            {
                this.requestUri = requestUri;
                this.newUri = newUri;
                this.result = result;
                this.priority = priority;
                this.firstSuccessfulDownload = firstSuccessfulDownload;
            }
            private readonly Uri requestUri;
            private readonly Uri newUri;
            public Uri UpdatedFeedUri
            {
                get
                {
                    return requestUri;
                }
            }
            public Uri NewFeedUri
            {
                get
                {
                    return newUri;
                }
            }
            private readonly RequestResult result;
            public RequestResult UpdateState
            {
                get
                {
                    return result;
                }
            }
            private readonly int priority;
            public int Priority
            {
                get
                {
                    return priority;
                }
            }
            private readonly bool firstSuccessfulDownload;
            public bool FirstSuccessfulDownload
            {
                get
                {
                    return firstSuccessfulDownload;
                }
            }
        }
        public delegate void UpdateFeedExceptionCallback(object sender, UpdateFeedExceptionEventArgs e);
        public event UpdateFeedExceptionCallback OnUpdateFeedException = null;
        public class UpdateFeedExceptionEventArgs : EventArgs
        {
            public UpdateFeedExceptionEventArgs(string requestUri, Exception e, int priority)
            {
                this.requestUri = requestUri;
                this.exception = e;
                this.priority = priority;
            }
            private readonly string requestUri;
            public string FeedUri
            {
                get
                {
                    return requestUri;
                }
            }
            private readonly Exception exception;
            public Exception ExceptionThrown
            {
                get
                {
                    return exception;
                }
            }
            private readonly int priority;
            public int Priority
            {
                get
                {
                    return priority;
                }
            }
        }
        public class UpdateFeedsEventArgs : EventArgs
        {
            public UpdateFeedsEventArgs(bool forced)
            {
                this.forced = forced;
            }
            private readonly bool forced;
            public bool ForcedRefresh
            {
                get
                {
                    return forced;
                }
            }
        }
        public class UpdateFeedEventArgs : UpdateFeedsEventArgs
        {
            public UpdateFeedEventArgs(Uri feed, bool forced, int priority) : base(forced)
            {
                this.feedUri = feed;
                this.priority = priority;
            }
            private readonly Uri feedUri;
            public Uri FeedUri
            {
                get
                {
                    return feedUri;
                }
            }
            private readonly int priority;
            public int Priority
            {
                get
                {
                    return priority;
                }
            }
        }
        public delegate void UpdateFeedsStartedHandler(object sender, UpdateFeedsEventArgs e);
        public event UpdateFeedsStartedHandler UpdateFeedsStarted = null;
        public delegate void UpdateFeedStartedHandler(object sender, UpdateFeedEventArgs e);
        public event UpdateFeedStartedHandler UpdateFeedStarted = null;
        public event EventHandler OnAllAsyncRequestsCompleted = null;
        public delegate void NewsItemSearchResultEventHandler(object sender, NewsItemSearchResultEventArgs e);
        public delegate void FeedSearchResultEventHandler(object sender, FeedSearchResultEventArgs e);
        public delegate void SearchFinishedEventHandler(object sender, SearchFinishedEventArgs e);
        public event NewsItemSearchResultEventHandler NewsItemSearchResult;
        public event SearchFinishedEventHandler SearchFinished;
        [ComVisible(false)]
        public class FeedSearchResultEventArgs : CancelEventArgs
        {
            public FeedSearchResultEventArgs(
                NewsFeed f, object tag, bool cancel) : base(cancel)
            {
                this.Feed = f;
                this.Tag = tag;
            }
            public NewsFeed Feed;
            public object Tag;
        }
        [ComVisible(false)]
        public class NewsItemSearchResultEventArgs : CancelEventArgs
        {
            public NewsItemSearchResultEventArgs(
                List<NewsItem> items, object tag, bool cancel) : base(cancel)
            {
                this.NewsItems = items;
                this.Tag = tag;
            }
            public List<NewsItem> NewsItems;
            public object Tag;
        }
        public class SearchFinishedEventArgs : EventArgs
        {
            public SearchFinishedEventArgs(
                object tag, FeedInfoList matchingFeeds, int matchingFeedsCount, int matchingItemsCount) :
                    this(tag, matchingFeeds, new List<NewsItem>(), matchingFeedsCount, matchingItemsCount)
            {
                List<NewsItem> temp = new List<NewsItem>();
                foreach (FeedInfo fi in matchingFeeds)
                {
                    foreach (NewsItem ni in fi.ItemsList)
                    {
                        if (ni is SearchHitNewsItem)
                            temp.Add(ni);
                        else
                            temp.Add(new SearchHitNewsItem(ni));
                    }
                    fi.ItemsList.Clear();
                    fi.ItemsList.AddRange(temp);
                    this.MatchingItems.AddRange(temp);
                    temp.Clear();
                }
            }
            public SearchFinishedEventArgs(
                object tag, FeedInfoList matchingFeeds, IEnumerable<NewsItem> matchingNewsItems, int matchingFeedsCount,
                int matchingItemsCount)
            {
                this.MatchingFeedsCount = matchingFeedsCount;
                this.MatchingItemsCount = matchingItemsCount;
                this.MatchingFeeds = matchingFeeds;
                this.MatchingItems = new List<NewsItem>(matchingNewsItems);
                this.Tag = tag;
            }
            public readonly int MatchingFeedsCount;
            public readonly int MatchingItemsCount;
            public readonly object Tag;
            public readonly FeedInfoList MatchingFeeds;
            public readonly List<NewsItem> MatchingItems;
        }
        private const int maxItemsPerSearchResult = 10;
        private List<NewsItem> SearchNewsItemsHelper(IEnumerable<NewsItem> prevMatchItems,
                                                     SearchCriteriaCollection criteria, FeedDetailsInternal fi,
                                                     FeedDetailsInternal fiMatchedItems, ref int itemmatches,
                                                     ref int feedmatches, object tag)
        {
            List<NewsItem> matchItems = new List<NewsItem>(maxItemsPerSearchResult);
            matchItems.AddRange(prevMatchItems);
            bool cancel = false;
            bool feedmatch = false;
            foreach (NewsItem item in fi.ItemsList)
            {
                if (criteria.Match(item))
                {
                    feedmatch = true;
                    matchItems.Add(item);
                    fiMatchedItems.ItemsList.Add(item);
                    itemmatches++;
                    if ((itemmatches%50) == 0)
                    {
                        cancel = RaiseNewsItemSearchResultEvent(matchItems, tag);
                        matchItems.Clear();
                    }
                    if (cancel) throw new InvalidOperationException("SEARCH CANCELLED");
                }
            }
            if (feedmatch) feedmatches++;
            return matchItems;
        }
        public void SearchNewsItems(SearchCriteriaCollection criteria, NewsFeed[] scope, object tag, string cultureName,
                                    bool returnFullItemText)
        {
            int feedmatches = 0;
            int itemmatches = 0;
            IList<NewsItem> unreturnedMatchItems = new List<NewsItem>();
            FeedInfoList fiList = new FeedInfoList(String.Empty);
            Exception ex;
            bool valid = this.SearchHandler.ValidateSearchCriteria(criteria, cultureName, out ex);
            if (ex != null)
            {
                fiList.Add((FeedInfo) CreateHelpNewsItemFromException(ex).FeedDetails);
                feedmatches = fiList.Count;
                unreturnedMatchItems = fiList.GetAllNewsItems();
                itemmatches = unreturnedMatchItems.Count;
            }
            if (valid)
            {
                try
                {
                    LuceneSearch.Result r = this.SearchHandler.ExecuteSearch(criteria, scope, cultureName);
                    SearchCriteriaProperty criteriaProperty = null;
                    foreach (ISearchCriteria sc in criteria)
                    {
                        criteriaProperty = sc as SearchCriteriaProperty;
                        if (criteriaProperty != null &&
                            PropertyExpressionKind.Unread == criteriaProperty.WhatKind)
                            break;
                    }
                    ItemReadState readState = ItemReadState.Ignore;
                    if (criteriaProperty != null)
                    {
                        if (criteriaProperty.BeenRead)
                            readState = ItemReadState.BeenRead;
                        else
                            readState = ItemReadState.Unread;
                    }
                    if (r != null && r.ItemMatchCount > 0)
                    {
                        SearchHitNewsItem[] nids = new SearchHitNewsItem[r.ItemsMatched.Count];
                        r.ItemsMatched.CopyTo(nids, 0);
                        fiList.AddRange(FindNewsItems(nids, readState, returnFullItemText));
                        feedmatches = fiList.Count;
                        unreturnedMatchItems = fiList.GetAllNewsItems();
                        itemmatches = unreturnedMatchItems.Count;
                    }
                }
                catch (Exception searchEx)
                {
                    fiList.Add((FeedInfo) CreateHelpNewsItemFromException(searchEx).FeedDetails);
                    feedmatches = fiList.Count;
                    unreturnedMatchItems = fiList.GetAllNewsItems();
                    itemmatches = unreturnedMatchItems.Count;
                }
            }
            RaiseSearchFinishedEvent(tag, fiList, unreturnedMatchItems, feedmatches, itemmatches);
        }
        private static ExceptionalNewsItem CreateHelpNewsItemFromException(Exception e)
        {
            if (e == null)
                throw new ArgumentNullException("e");
            NewsFeed f = new NewsFeed();
            f.link = "http://www.rssbandit.org/docs/";
            f.title = ComponentsText.ExceptionHelpFeedTitle;
            ExceptionalNewsItem newsItem =
                new ExceptionalNewsItem(f, ComponentsText.ExceptionHelpFeedItemTitle(e.GetType().Name),
                                        (e.HelpLink ?? "http://www.rssbandit.org/docs/"),
                                        e.Message, e.Source, DateTime.Now.ToUniversalTime(), Guid.NewGuid().ToString());
            newsItem.Subject = e.GetType().Name;
            newsItem.CommentStyle = SupportedCommentStyle.None;
            newsItem.Enclosures = GetList<Enclosure>.Empty;
            newsItem.WatchComments = false;
            newsItem.Language = CultureInfo.CurrentUICulture.Name;
            newsItem.HasNewComments = false;
            FeedInfo fi = new FeedInfo(f.id, f.cacheurl, new List<NewsItem>(new NewsItem[] {newsItem}),
                                       f.title, f.link, ComponentsText.ExceptionHelpFeedDesc,
                                       new Dictionary<XmlQualifiedName, string>(1), newsItem.Language);
            newsItem.FeedDetails = fi;
            return newsItem;
        }
        public void SearchNewsItems(SearchCriteriaCollection criteria, NewsFeed[] scope, object tag)
        {
            int feedmatches = 0;
            int itemmatches = 0;
            int feedcounter = 0;
            List<NewsItem> unreturnedMatchItems = new List<NewsItem>();
            FeedInfoList fiList = new FeedInfoList(String.Empty);
            try
            {
                FeedInfo[] feedInfos;
                if (scope.Length == 0)
                {
                    lock (itemsTable)
                    {
                        feedInfos = new FeedInfo[itemsTable.Count];
                        itemsTable.Values.CopyTo(feedInfos, 0);
                    }
                    foreach (FeedInfo fi in feedInfos)
                    {
                        FeedInfo fiClone = fi.Clone(false);
                        unreturnedMatchItems =
                            SearchNewsItemsHelper(unreturnedMatchItems, criteria, fi, fiClone, ref itemmatches,
                                                  ref feedmatches, tag);
                        feedcounter++;
                        if ((feedcounter%5) == 0)
                        {
                            bool cancel = RaiseNewsItemSearchResultEvent(unreturnedMatchItems, tag);
                            unreturnedMatchItems.Clear();
                            if (cancel)
                                break;
                        }
                        if (fiClone.ItemsList.Count != 0)
                        {
                            fiList.Add(fiClone);
                        }
                    }
                }
                else
                {
                    lock (itemsTable)
                    {
                        feedInfos = new FeedInfo[scope.Length];
                        for (int i = 0; i < scope.Length; i++)
                        {
                            feedInfos[i] = (FeedInfo) itemsTable[scope[i].link];
                        }
                    }
                    foreach (FeedInfo fi in feedInfos)
                    {
                        if (fi != null)
                        {
                            FeedInfo fiClone = fi.Clone(false);
                            unreturnedMatchItems =
                                SearchNewsItemsHelper(unreturnedMatchItems, criteria, fi, fiClone, ref itemmatches,
                                                      ref feedmatches, tag);
                            feedcounter++;
                            if ((feedcounter%5) == 0)
                            {
                                bool cancel = RaiseNewsItemSearchResultEvent(unreturnedMatchItems, tag);
                                unreturnedMatchItems.Clear();
                                if (cancel)
                                    break;
                            }
                            if (fiClone.ItemsList.Count != 0)
                            {
                                fiList.Add(fiClone);
                            }
                        }
                    }
                }
                if (unreturnedMatchItems.Count > 0)
                {
                    RaiseNewsItemSearchResultEvent(unreturnedMatchItems, tag);
                }
            }
            catch (InvalidOperationException ioe)
            {
                Trace("SearchNewsItems() casued InvalidOperationException: {0}", ioe);
            }
            RaiseSearchFinishedEvent(tag, fiList, feedmatches, itemmatches);
        }
        public void SearchRemoteFeed(string searchFeedUrl, object tag)
        {
            int feedmatches;
            int itemmatches;
            List<NewsItem> unreturnedMatchItems = this.GetItemsForFeed(searchFeedUrl);
            RaiseNewsItemSearchResultEvent(unreturnedMatchItems, tag);
            feedmatches = 1;
            itemmatches = unreturnedMatchItems.Count;
            FeedInfo fi =
                new FeedInfo(String.Empty, String.Empty, unreturnedMatchItems, String.Empty, String.Empty, String.Empty,
                             new Dictionary<XmlQualifiedName, string>(), String.Empty);
            FeedInfoList fil = new FeedInfoList(String.Empty);
            fil.Add(fi);
            RaiseSearchFinishedEvent(tag, fil, feedmatches, itemmatches);
        }
        public void SearchFeeds(SearchCriteriaCollection criteria, NewsFeed[] scope, object tag)
        {
            throw new NotSupportedException();
        }
        private bool RaiseNewsItemSearchResultEvent(IEnumerable<NewsItem> matchItems, object tag)
        {
            try
            {
                if (NewsItemSearchResult != null)
                {
                    NewsItemSearchResultEventArgs ea =
                        new NewsItemSearchResultEventArgs(new List<NewsItem>(matchItems), tag, false);
                    NewsItemSearchResult(this, ea);
                    return ea.Cancel;
                }
            }
            catch
            {
            }
            return false;
        }
        private void RaiseSearchFinishedEvent(object tag, FeedInfoList matchingFeeds, int matchingFeedsCount,
                                              int matchingItemsCount)
        {
            try
            {
                if (SearchFinished != null)
                {
                    SearchFinished(this,
                                   new SearchFinishedEventArgs(tag, matchingFeeds, matchingFeedsCount,
                                                               matchingItemsCount));
                }
            }
            catch (Exception e)
            {
                Trace("SearchFinished() event code raises exception: {0}", e);
            }
        }
        private void RaiseSearchFinishedEvent(object tag, FeedInfoList matchingFeeds, IEnumerable<NewsItem> matchingItems,
                                              int matchingFeedsCount, int matchingItemsCount)
        {
            try
            {
                if (SearchFinished != null)
                {
                    SearchFinished(this,
                                   new SearchFinishedEventArgs(tag, matchingFeeds, matchingItems, matchingFeedsCount,
                                                               matchingItemsCount));
                }
            }
            catch (Exception e)
            {
                Trace("SearchFinished() event code raises exception: {0}", e);
            }
        }
        public NewsItem FindNewsItem(SearchHitNewsItem nid)
        {
            if (nid != null)
            {
                FeedInfo fi = this.itemsTable[nid.FeedLink] as FeedInfo;
                if (fi != null)
                {
                    List<NewsItem> items = new List<NewsItem>(fi.ItemsList);
                    foreach (NewsItem ni in items)
                    {
                        if (ni.Id.Equals(nid.Id))
                        {
                            return ni;
                        }
                    }
                }
            }
            return null;
        }
        public FeedInfoList FindNewsItems(SearchHitNewsItem[] nids)
        {
            return this.FindNewsItems(nids, ItemReadState.Ignore, false);
        }
        public FeedInfoList FindNewsItems(SearchHitNewsItem[] nids, ItemReadState readState, bool returnFullItemText)
        {
            FeedInfoList fiList = new FeedInfoList(String.Empty);
            Dictionary<string, FeedInfo> matchedFeeds = new Dictionary<string, FeedInfo>();
            Dictionary<string, List<NewsItem> > itemlists = new Dictionary<string, List<NewsItem> >();
            foreach (SearchHitNewsItem nid in nids)
            {
                FeedDetailsInternal fdi;
                FeedInfo fi, originalfi = null;
                if (this.itemsTable.TryGetValue(nid.FeedLink, out fdi))
                    originalfi = fdi as FeedInfo;
                if (originalfi != null)
                {
                    List<NewsItem> items;
                    if (matchedFeeds.ContainsKey(nid.FeedLink))
                    {
                        fi = matchedFeeds[nid.FeedLink];
                        items = itemlists[nid.FeedLink];
                    }
                    else
                    {
                        fi = originalfi.Clone(false);
                        items = new List<NewsItem>(originalfi.ItemsList);
                        matchedFeeds.Add(nid.FeedLink, fi);
                        itemlists.Add(nid.FeedLink, items);
                    }
                    bool beenRead = (readState == ItemReadState.BeenRead);
                    foreach (NewsItem ni in items)
                    {
                        if (ni.Id.Equals(nid.Id))
                        {
                            if (readState == ItemReadState.Ignore ||
                                ni.BeenRead == beenRead)
                            {
                                nid.BeenRead = ni.BeenRead;
                                if (returnFullItemText && !nid.HasContent)
                                    this.GetCachedContentForItem(nid);
                                fi.ItemsList.Add(nid);
                                nid.FeedDetails = fi;
                            }
                            break;
                        }
                    }
                }
            }
            foreach (FeedInfo f in matchedFeeds.Values)
            {
                if (f.ItemsList.Count > 0)
                {
                    fiList.Add(f);
                }
            }
            return fiList;
        }
        internal string applicationName = "NewsComponents";
        public FeedColumnLayoutCollection ColumnLayouts
        {
            get
            {
                if (layouts == null)
                {
                    layouts = new FeedColumnLayoutCollection();
                }
                return layouts;
            }
        }
        public static string CategorySeparator = @"\";
        public CategoriesCollection Categories
        {
            get
            {
                if (categories == null)
                {
                    categories = new CategoriesCollection();
                }
                return categories;
            }
        }
        public IDictionary<string, NewsFeed> FeedsTable
        {
            get
            {
                if (!validationErrorOccured)
                {
                    return _feedsTable;
                }
                else
                {
                    return null;
                }
            }
        }
        public IDictionary<string, INntpServerDefinition> NntpServers
        {
            [DebuggerStepThrough]
            get
            {
                if (this.nntpServers == null)
                {
                    this.nntpServers = new Dictionary<string, INntpServerDefinition>();
                }
                return this.nntpServers;
            }
        }
        public IDictionary<string, UserIdentity> UserIdentity
        {
            [DebuggerStepThrough]
            get
            {
                if (this.identities == null)
                {
                    this.identities = new Dictionary<string, UserIdentity>();
                }
                return this.identities;
            }
        }
        private int refreshrate = 60*60*1000;
        public int RefreshRate
        {
            set
            {
                if (value >= 0)
                {
                    this.refreshrate = value;
                }
                string[] keys;
                lock (FeedsTable)
                {
                    keys = new string[FeedsTable.Count];
                    if (FeedsTable.Count > 0)
                        FeedsTable.Keys.CopyTo(keys, 0);
                }
                for (int i = 0, len = keys.Length; i < len; i++)
                {
                    NewsFeed f = null;
                    if (FeedsTable.TryGetValue(keys[i], out f)) {
                        f.refreshrate = this.refreshrate;
                        f.refreshrateSpecified = true;
                    }
                }
            }
            get
            {
                return refreshrate;
            }
        }
        private static bool validationErrorOccured = false;
        private XmlSchema feedsSchema = null;
        public bool FeedsListOK
        {
            get
            {
                return !validationErrorOccured;
            }
        }
        private string[] GetFeedsTableKeys()
        {
            string[] keys;
            lock (FeedsTable)
            {
                keys = new string[FeedsTable.Count];
                if (FeedsTable.Count > 0)
                    FeedsTable.Keys.CopyTo(keys, 0);
            }
            return keys;
        }
        public IList<RelationHRefEntry> GetTopStories(TimeSpan since, int numStories)
        {
            string[] keys = GetFeedsTableKeys();
            Dictionary<RelationHRefEntry, List<RankedNewsItem> > allLinks =
                new Dictionary<RelationHRefEntry, List<RankedNewsItem> >();
            for (int i = 0; i < keys.Length; i++)
            {
                if (!itemsTable.ContainsKey(keys[i]))
                {
                    continue;
                }
                FeedInfo fi = (FeedInfo) itemsTable[keys[i]];
                List<NewsItem> items =
                    fi.ItemsList.FindAll(delegate(NewsItem item)
                                             {
                                                 return (DateTime.Now - item.Date) < since;
                                             });
                foreach (NewsItem item in items)
                {
                    float score = 1.0f - (DateTime.Now.Ticks - item.Date.Ticks)*1.0f/since.Ticks;
                    RankedNewsItem rni = new RankedNewsItem(item, score);
                    foreach (string url in item.OutGoingLinks)
                    {
                        RelationHRefEntry href = new RelationHRefEntry(url, null, 0.0f);
                        if (!allLinks.ContainsKey(href))
                        {
                            allLinks[href] = new List<RankedNewsItem>();
                        }
                        allLinks[href].Add(rni);
                    }
                }
            }
            List<RelationHRefEntry> weightedLinks = new List<RelationHRefEntry>();
            foreach (KeyValuePair<RelationHRefEntry, List<RankedNewsItem> > linkNvotes in allLinks)
            {
                Dictionary<string, float> votesPerFeed = new Dictionary<string, float>();
                foreach (RankedNewsItem voteItem in linkNvotes.Value)
                {
                    string feedLink = voteItem.Item.FeedLink;
                    if (votesPerFeed.ContainsKey(feedLink))
                    {
                        votesPerFeed[feedLink] = Math.Min(votesPerFeed[feedLink], voteItem.Score);
                    }
                    else
                    {
                        votesPerFeed.Add(feedLink, voteItem.Score);
                        linkNvotes.Key.References.Add(voteItem.Item);
                    }
                }
                float totalScore = 0.0f;
                foreach (float value in votesPerFeed.Values)
                {
                    totalScore += value;
                }
                linkNvotes.Key.Score = totalScore;
                weightedLinks.Add(linkNvotes.Key);
            }
            weightedLinks.Sort(delegate(RelationHRefEntry x, RelationHRefEntry y)
                                   {
                                       return y.Score.CompareTo(x.Score);
                                   });
            weightedLinks = weightedLinks.GetRange(0, Math.Min(numStories, weightedLinks.Count));
            numTitlesToDownload = numStories;
            this.eventX = new ManualResetEvent(false);
            foreach (RelationHRefEntry weightedLink in weightedLinks)
            {
                if (TopStoryTitles.ContainsKey(weightedLink.HRef))
                {
                    weightedLink.Text = TopStoryTitles[weightedLink.HRef].storyTitle;
                    Interlocked.Decrement(ref numTitlesToDownload);
                }
                else
                {
                    PriorityThreadPool.QueueUserWorkItem(GetHtmlTitleHelper, weightedLink,
                                                         (int) ThreadPriority.Normal);
                }
            }
            if (numTitlesToDownload > 0)
            {
                eventX.WaitOne(Timeout.Infinite, true);
            }
            return weightedLinks;
        }
        private void GetHtmlTitleHelper(object obj)
        {
            try
            {
                RelationHRefEntry weightedLink = (RelationHRefEntry)obj;
                string title =
                    HtmlHelper.FindTitle(weightedLink.HRef, weightedLink.HRef, this.proxy,
                                         CredentialCache.DefaultCredentials);
                weightedLink.Text = title;
                if (!title.Equals(weightedLink.HRef))
                {
                    TopStoryTitles.Add(weightedLink.HRef, new storyNdate(title, DateTime.Now));
                    topStoriesModified = true;
                }
            }
            finally
            {
                Interlocked.Decrement(ref numTitlesToDownload);
                if (numTitlesToDownload <= 0)
                {
                    eventX.Set();
                }
            }
        }
        public IEnumerable<NewsFeed> GetNonInternetFeeds()
        {
            List<NewsFeed> toReturn = new List<NewsFeed>();
            if (this.FeedsTable.Count == 0)
                return toReturn;
            string[] keys = new string[this.FeedsTable.Keys.Count];
            this.FeedsTable.Keys.CopyTo(keys, 0);
            foreach (string url in keys)
            {
                try
                {
                    Uri uri = new Uri(url);
                    if (uri.IsFile || uri.IsUnc || !uri.Authority.Contains(".")) {
                        NewsFeed f = null;
                        if (FeedsTable.TryGetValue(url, out f)) {
                            toReturn.Add(f);
                        }
                    }
                }
                catch (Exception e)
                {
                    _log.Error("Exception in GetNonInternetFeeds()", e);
                }
            }
            return toReturn;
        }
        private void LoadFeedlistSchema()
        {
            using (Stream xsdStream = Resource.Manager.GetStream("Resources.feedListSchema.xsd"))
            {
                feedsSchema = XmlSchema.Read(xsdStream, null);
            }
        }
        private static void LoadCachedTopStoryTitles()
        {
            try
            {
                string topStories = Path.Combine(GetUserPath("RssBandit"), "top-stories.xml");
                if (File.Exists(topStories))
                {
                    XmlDocument doc = new XmlDocument();
                    doc.Load(topStories);
                    foreach (XmlElement story in doc.SelectNodes(""))
                    {
                        TopStoryTitles.Add(story.Attributes["url"].Value,
                                           new storyNdate(story.Attributes["title"].Value,
                                                          XmlConvert.ToDateTime(story.Attributes["firstSeen"].Value))
                            );
                    }
                }
            }
            catch (Exception e)
            {
                _log.Error("Error in LoadCachedTopStoryTitles()", e);
            }
        }
        public static void SaveCachedTopStoryTitles()
        {
            DateTime TwoWeeksAgo = DateTime.Now.Subtract(new TimeSpan(14, 0, 0, 0));
            topStoriesModified = false;
            try
            {
                XmlWriter writer = XmlWriter.Create(Path.Combine(GetUserPath("RssBandit"), "top-stories.xml"));
                writer.WriteStartDocument();
                writer.WriteStartElement("stories");
                foreach (KeyValuePair<string, storyNdate> story in TopStoryTitles)
                {
                    if (story.Value.firstSeen > TwoWeeksAgo)
                    {
                        writer.WriteStartElement("story");
                        writer.WriteAttributeString("url", story.Key);
                        writer.WriteAttributeString("title", story.Value.storyTitle);
                        writer.WriteAttributeString("firstSeen", XmlConvert.ToString(story.Value.firstSeen));
                        writer.WriteEndElement();
                    }
                }
                writer.WriteEndDocument();
                writer.Flush();
                writer.Close();
            }
            catch (Exception e)
            {
                _log.Error("Error in SaveCachedTopStoryTitles()", e);
            }
        }
        public void LoadFeedlist(string feedListUrl, ValidationEventHandler veh)
        {
            LoadFeedlist(AsyncWebRequest.GetSyncResponseStream(feedListUrl, null, this.UserAgent, this.Proxy), veh);
            this.SearchHandler.CheckIndex();
        }
        public void LoadFeedlist(Stream xmlStream, ValidationEventHandler veh)
        {
            XmlParserContext context =
                new XmlParserContext(null, new RssBanditXmlNamespaceResolver(), null, XmlSpace.None);
            XmlReader reader = new RssBanditXmlReader(xmlStream, XmlNodeType.Document, context);
            validationErrorOccured = false;
            XmlSerializer serializer = XmlHelper.SerializerCache.GetSerializer(typeof (feeds));
            feeds myFeeds = (feeds) serializer.Deserialize(reader);
            reader.Close();
            if (!validationErrorOccured)
            {
                if (myFeeds.feed != null)
                {
                    foreach (NewsFeed f in myFeeds.feed)
                    {
                        if (_feedsTable.ContainsKey(f.link) == false)
                        {
                            bool isBadUri = false;
                            try
                            {
                                Uri uri = new Uri(f.link);
                                if (NntpWebRequest.NewsUriScheme.Equals(uri.Scheme))
                                {
                                    f.link = NntpWebRequest.NntpUriScheme + uri.CanonicalizedUri().Substring(uri.Scheme.Length);
                                }
                                else
                                {
                                    f.link = uri.CanonicalizedUri();
                                }
                            }
                            catch (Exception)
                            {
                                isBadUri = true;
                            }
                            if (isBadUri)
                            {
                                continue;
                            }
                            else
                            {
                                if (_feedsTable.ContainsKey(f.link) == false)
                                    _feedsTable.Add(f.link, f);
                            }
                        }
                    }
                }
                if (myFeeds.categories != null)
                {
                    foreach (category cat in myFeeds.categories)
                    {
                        string cat_trimmed = cat.Value.Trim();
                        if (!this.categories.ContainsKey(cat_trimmed))
                        {
                            cat.Value = cat_trimmed;
                            this.categories.Add(cat_trimmed, cat);
                        }
                    }
                }
                if (categoryMismatch && (myFeeds.feed != null))
                {
                    foreach (NewsFeed f in myFeeds.feed)
                    {
                        if (f.category != null)
                        {
                            string cat_trimmed = f.category = f.category.Trim();
                            if (!this.categories.ContainsKey(cat_trimmed))
                            {
                                this.categories.Add(cat_trimmed);
                            }
                        }
                    }
                    categoryMismatch = false;
                }
                if (myFeeds.listviewLayouts != null)
                {
                    foreach (listviewLayout layout in myFeeds.listviewLayouts)
                    {
                        string layout_trimmed = layout.ID.Trim();
                        if (!this.layouts.ContainsKey(layout_trimmed))
                        {
                            this.layouts.Add(layout_trimmed, layout.FeedColumnLayout);
                        }
                    }
                }
                if (myFeeds.nntpservers != null)
                {
                    foreach (NntpServerDefinition sd in myFeeds.nntpservers)
                    {
                        if (nntpServers.ContainsKey(sd.Name) == false)
                        {
                            nntpServers.Add(sd.Name, sd);
                        }
                    }
                }
                if (myFeeds.identities != null)
                {
                    foreach (UserIdentity ui in myFeeds.identities)
                    {
                        if (identities.ContainsKey(ui.Name) == false)
                        {
                            identities.Add(ui.Name, ui);
                        }
                    }
                }
                if (myFeeds.refreshrateSpecified)
                {
                    this.refreshrate = myFeeds.refreshrate;
                }
                if (!string.IsNullOrEmpty(myFeeds.stylesheet))
                {
                    this.stylesheet = myFeeds.stylesheet;
                }
                if (myFeeds.downloadenclosuresSpecified)
                {
                    this.downloadenclosures = myFeeds.downloadenclosures;
                }
                if (myFeeds.enclosurecachesizeSpecified)
                {
                    this.enclosurecachesize = myFeeds.enclosurecachesize;
                }
                if (myFeeds.numtodownloadonnewfeedSpecified)
                {
                    this.numtodownloadonnewfeed = myFeeds.numtodownloadonnewfeed;
                }
                if (myFeeds.enclosurealertSpecified)
                {
                    this.enclosurealert = myFeeds.enclosurealert;
                }
                if (myFeeds.createsubfoldersforenclosuresSpecified)
                {
                    this.createsubfoldersforenclosures = myFeeds.createsubfoldersforenclosures;
                }
                if (myFeeds.markitemsreadonexitSpecified)
                {
                    this.markitemsreadonexit = myFeeds.markitemsreadonexit;
                }
                if (!string.IsNullOrEmpty(myFeeds.enclosurefolder))
                {
                    this.EnclosureFolder = myFeeds.enclosurefolder;
                }
                if (!string.IsNullOrEmpty(myFeeds.podcastfolder))
                {
                    this.PodcastFolder = myFeeds.podcastfolder;
                }
                if (!string.IsNullOrEmpty(myFeeds.podcastfileexts))
                {
                    this.PodcastFileExtensionsAsString = myFeeds.podcastfileexts;
                }
                if (!string.IsNullOrEmpty(myFeeds.listviewlayout))
                {
                    this.listviewlayout = myFeeds.listviewlayout;
                }
                try
                {
                    if (!string.IsNullOrEmpty(myFeeds.maxitemage))
                    {
                        this.maxitemage = XmlConvert.ToTimeSpan(myFeeds.maxitemage);
                    }
                }
                catch (FormatException fe)
                {
                    Trace("Error occured while parsing maximum item age from feed list: {0}", fe.ToString());
                }
            }
        }
        public void DisableFeed(string feedUrl)
        {
            if (!FeedsTable.ContainsKey(feedUrl))
            {
                return;
            }
            NewsFeed f = FeedsTable[feedUrl];
            f.refreshrate = 0;
            f.refreshrateSpecified = true;
        }
        public void DeleteItem(NewsItem item)
        {
            if (item.Feed != null && !string.IsNullOrEmpty(item.Feed.link))
            {
                FeedInfo fi = itemsTable[item.Feed.link] as FeedInfo;
                if (fi != null)
                {
                    lock (fi.itemsList)
                    {
                        item.Feed.deletedstories.Add(item.Id);
                        fi.itemsList.Remove(item);
                    }
                }
            }
        }
        public void DeleteAllItemsInFeed(NewsFeed feed)
        {
            if (feed != null && !string.IsNullOrEmpty(feed.link) && FeedsTable.ContainsKey(feed.link))
            {
                FeedInfo fi = itemsTable[feed.link] as FeedInfo;
                if (fi == null)
                {
                    fi = (FeedInfo) this.GetFeed(feed);
                }
                if (fi != null)
                {
                    lock (fi.itemsList)
                    {
                        foreach (NewsItem item in fi.itemsList)
                        {
                            feed.deletedstories.Add(item.Id);
                        }
                        fi.itemsList.Clear();
                    }
                }
                this.SearchHandler.IndexRemove(feed.id);
            }
        }
        public void DeleteAllItemsInFeed(string feedUrl)
        {
            if (FeedsTable.ContainsKey(feedUrl))
            {
                this.DeleteAllItemsInFeed(FeedsTable[feedUrl]);
            }
        }
        public void RestoreDeletedItem(NewsItem item)
        {
            if (item.Feed != null && !string.IsNullOrEmpty(item.Feed.link) && FeedsTable.ContainsKey(item.Feed.link))
            {
                FeedInfo fi = itemsTable[item.Feed.link] as FeedInfo;
                if (fi == null)
                {
                    fi = (FeedInfo) this.GetFeed(item.Feed);
                }
                if (fi != null)
                {
                    lock (fi.itemsList)
                    {
                        item.Feed.deletedstories.Remove(item.Id);
                        fi.itemsList.Add(item);
                    }
                }
                this.SearchHandler.IndexAdd(item);
            }
        }
        public void RestoreDeletedItem(IList<NewsItem> deletedItems)
        {
            foreach (NewsItem item in deletedItems)
            {
                this.RestoreDeletedItem(item);
            }
            this.SearchHandler.IndexAdd(deletedItems);
        }
        public void DeleteFeed(string feedUrl)
        {
            if (!FeedsTable.ContainsKey(feedUrl))
            {
                return;
            }
            NewsFeed f = FeedsTable[feedUrl];
            FeedsTable.Remove(feedUrl);
            if (itemsTable.ContainsKey(feedUrl))
            {
                itemsTable.Remove(feedUrl);
            }
            this.SearchHandler.IndexRemove(f.id);
            if (this.enclosureDownloader != null)
                this.enclosureDownloader.CancelPendingDownloads(feedUrl);
            try
            {
                this.CacheHandler.RemoveFeed(f);
            }
            catch (Exception e)
            {
                throw new ApplicationException(e.Message, e);
            }
        }
        public void SaveFeedList(Stream feedStream)
        {
            this.SaveFeedList(feedStream, FeedListFormat.NewsHandler);
        }
        private static XmlElement CreateCategoryHive(XmlElement startNode, string category)
        {
            if (category == null || category.Length == 0 || startNode == null) return startNode;
            string[] catHives = category.Split(CategorySeparator.ToCharArray());
            XmlElement n;
            bool wasNew = false;
            foreach (string catHive in catHives)
            {
                if (!wasNew)
                {
                    string xpath = "child::outline[@title=" + buildXPathString(catHive) + " and (count(@*)= 1)]";
                    n = (XmlElement) startNode.SelectSingleNode(xpath);
                }
                else
                {
                    n = null;
                }
                if (n == null)
                {
                    n = startNode.OwnerDocument.CreateElement("outline");
                    n.SetAttribute("title", catHive);
                    startNode.AppendChild(n);
                    wasNew = true;
                }
                startNode = n;
            }
            return startNode;
        }
        private static listviewLayout FindLayout(IEquatable<string> id, IEnumerable<listviewLayout> layouts)
        {
            foreach (listviewLayout layout in layouts)
            {
                if (id.Equals(layout.ID))
                    return layout;
            }
            return null;
        }
        public static string buildXPathString(string input)
        {
            string[] components = input.Split(new char[] {'\''});
            string result = "";
            result += "concat(''";
            for (int i = 0; i < components.Length; i++)
            {
                result += ", '" + components[i] + "'";
                if (i < components.Length - 1)
                {
                    result += ", \"'\"";
                }
            }
            result += ")";
            Console.WriteLine(result);
            return result;
        }
        public void SaveFeedList(Stream feedStream, FeedListFormat format)
        {
            this.SaveFeedList(feedStream, format, this._feedsTable, true);
        }
        public void SaveFeedList(Stream feedStream, FeedListFormat format, IDictionary<string, NewsFeed> feeds,
                                 bool includeEmptyCategories)
        {
            if (feedStream == null)
                throw new ArgumentNullException("feedStream");
            if (format.Equals(FeedListFormat.OPML))
            {
                XmlDocument opmlDoc = new XmlDocument();
                opmlDoc.LoadXml("<opml version='1.0'><head /><body /></opml>");
                Dictionary<string, XmlElement> categoryTable = new Dictionary<string, XmlElement>(categories.Count);
                foreach (NewsFeed f in feeds.Values)
                {
                    XmlElement outline = opmlDoc.CreateElement("outline");
                    outline.SetAttribute("title", f.title);
                    outline.SetAttribute("xmlUrl", f.link);
                    outline.SetAttribute("type", "rss");
                    outline.SetAttribute("text", f.title);
                   FeedDetailsInternal fi;
                    bool success = itemsTable.TryGetValue(f.link, out fi);
     if(success){
      outline.SetAttribute("htmlUrl", fi.Link);
      outline.SetAttribute("description", fi.Description);
     }
                    string category = (f.category ?? String.Empty);
                    XmlElement catnode;
                    if (categoryTable.ContainsKey(category))
                        catnode = categoryTable[category];
                    else
                    {
                        catnode = CreateCategoryHive((XmlElement) opmlDoc.DocumentElement.ChildNodes[1], category);
                        categoryTable.Add(category, catnode);
                    }
                    catnode.AppendChild(outline);
                }
                if (includeEmptyCategories)
                {
                    foreach (string category in this.categories.Keys)
                    {
                        CreateCategoryHive((XmlElement) opmlDoc.DocumentElement.ChildNodes[1], category);
                    }
                }
                XmlTextWriter opmlWriter = new XmlTextWriter(feedStream, Encoding.UTF8);
                opmlWriter.Formatting = Formatting.Indented;
                opmlDoc.Save(opmlWriter);
            }
            else if (format.Equals(FeedListFormat.NewsHandler) || format.Equals(FeedListFormat.NewsHandlerLite))
            {
                XmlSerializer serializer = XmlHelper.SerializerCache.GetSerializer(typeof (feeds));
                feeds feedlist = new feeds();
                if (feeds != null)
                {
                    feedlist.refreshrate = this.refreshrate;
                    feedlist.refreshrateSpecified = true;
                    feedlist.downloadenclosures = this.downloadenclosures;
                    feedlist.downloadenclosuresSpecified = true;
                    feedlist.enclosurealert = this.enclosurealert;
                    feedlist.enclosurealertSpecified = true;
                    feedlist.createsubfoldersforenclosures = this.createsubfoldersforenclosures;
                    feedlist.createsubfoldersforenclosuresSpecified = true;
                    feedlist.numtodownloadonnewfeed = this.numtodownloadonnewfeed;
                    feedlist.numtodownloadonnewfeedSpecified = true;
                    feedlist.enclosurecachesize = this.enclosurecachesize;
                    feedlist.enclosurecachesizeSpecified = true;
                    feedlist.maxitemage = XmlConvert.ToString(this.maxitemage);
                    feedlist.listviewlayout = this.listviewlayout;
                    feedlist.stylesheet = this.stylesheet;
                    feedlist.enclosurefolder = this.EnclosureFolder;
                    feedlist.podcastfolder = this.PodcastFolder;
                    feedlist.podcastfileexts = this.PodcastFileExtensionsAsString;
                    feedlist.markitemsreadonexit = this.markitemsreadonexit;
                    feedlist.markitemsreadonexitSpecified = true;
                    foreach (NewsFeed f in feeds.Values)
                    {
                        feedlist.feed.Add(f);
                        if (itemsTable.ContainsKey(f.link))
                        {
                            IList<NewsItem> items = itemsTable[f.link].ItemsList;
                            if (!format.Equals(FeedListFormat.NewsHandlerLite))
                            {
                                foreach (NewsItem ri in items)
                                {
                                    if (ri.BeenRead && !f.storiesrecentlyviewed.Contains(ri.Id))
                                    {
                                        f.storiesrecentlyviewed.Add(ri.Id);
                                    }
                                }
                            }
                        }
                    }
                }
                List<category> c = new List<category>(this.categories.Count);
                for (int i = 0; i < this.categories.Count; i++)
                {
                    CategoryEntry s = this.categories[i];
                    if (s.Value.Value == null)
                    {
                        this.categories.RemoveAt(i);
                        i--;
                    }
                    else
                    {
                        c.Add(s.Value);
                    }
                }
                if (c.Count == 0)
                {
                    feedlist.categories = null;
                }
                else
                {
                    feedlist.categories = c;
                }
                List<listviewLayout> lvl = new List<listviewLayout>(this.layouts.Count);
                for (int i = 0; i < this.layouts.Count; i++)
                {
                    FeedColumnLayoutEntry s = this.layouts[i];
                    if (s.Value == null)
                    {
                        this.layouts.RemoveAt(i);
                        i--;
                    }
                    else
                    {
                        lvl.Add(new listviewLayout(s.Key, s.Value));
                    }
                }
                if (lvl.Count == 0)
                {
                    feedlist.listviewLayouts = null;
                }
                else
                {
                    feedlist.listviewLayouts = lvl;
                }
                List<NntpServerDefinition> nntps = new List<NntpServerDefinition>(nntpServers.Values.Count);
                foreach (INntpServerDefinition val in nntpServers.Values)
                    nntps.Add((NntpServerDefinition)val);
                if (nntps.Count == 0)
                {
                    feedlist.nntpservers = null;
                }
                else
                {
                    feedlist.nntpservers = nntps;
                }
                List<UserIdentity> ids = new List<UserIdentity>(this.identities.Values);
                if (ids.Count == 0)
                {
                    feedlist.identities = null;
                }
                else
                {
                    feedlist.identities = ids;
                }
                TextWriter writer = new StreamWriter(feedStream);
                serializer.Serialize(writer, feedlist);
            }
        }
        public void MarkForDownload(NewsFeed f)
        {
            f.etag = null;
            f.lastretrievedSpecified = false;
            f.lastretrieved = DateTime.MinValue;
            f.lastmodified = DateTime.MinValue;
        }
        public void MarkForDownload()
        {
            if (this.FeedsListOK)
            {
                foreach (NewsFeed f in this.FeedsTable.Values)
                {
                    this.MarkForDownload(f);
                }
            }
        }
        public void ClearItemsCache()
        {
            this.itemsTable.Clear();
            this.CacheHandler.ClearCache();
        }
        public void MarkAllCachedItemsAsRead()
        {
            foreach (NewsFeed f in this.FeedsTable.Values)
            {
                this.MarkAllCachedItemsAsRead(f);
            }
        }
        public void MarkAllCachedCategoryItemsAsRead(string category)
        {
            if (FeedsListOK)
            {
                if (this.categories.ContainsKey(category))
                {
                    foreach (NewsFeed f in this.FeedsTable.Values)
                    {
                        if ((f.category != null) && f.category.Equals(category))
                        {
                            this.MarkAllCachedItemsAsRead(f);
                        }
                    }
                }
                else if (category == null )
                {
                    foreach (NewsFeed f in this.FeedsTable.Values)
                    {
                        if (f.category == null)
                        {
                            this.MarkAllCachedItemsAsRead(f);
                        }
                    }
                }
            }
        }
        public void MarkAllCachedItemsAsRead(string feedUrl)
        {
            if (!string.IsNullOrEmpty(feedUrl))
            {
                NewsFeed feed = null;
                if (this.FeedsTable.TryGetValue(feedUrl, out feed))
                {
                    this.MarkAllCachedItemsAsRead(feed);
                }
            }
        }
        public void MarkAllCachedItemsAsRead(NewsFeed feed)
        {
            if (feed != null && !string.IsNullOrEmpty(feed.link) && itemsTable.ContainsKey(feed.link))
            {
                FeedInfo fi = itemsTable[feed.link] as FeedInfo;
                if (fi != null)
                {
                    foreach (NewsItem ri in fi.itemsList)
                    {
                        ri.BeenRead = true;
                    }
                }
                feed.containsNewMessages = false;
            }
        }
        public void AddFeed(NewsFeed f, FeedInfo fi)
        {
            if (f != null)
            {
                lock (this.FeedsTable)
                {
                    if (FeedsTable.ContainsKey(f.link))
                    {
                        FeedsTable.Remove(f.link);
                    }
                    FeedsTable.Add(f.link, f);
                }
            }
            if (fi != null && f != null)
            {
                lock (this.itemsTable)
                {
                    if (itemsTable.ContainsKey(f.link))
                    {
                        itemsTable.Remove(f.link);
                    }
                    itemsTable.Add(f.link, fi);
                }
            }
        }
        private const NewsFeedProperty cacheRelevantPropertyChanges =
            NewsFeedProperty.FeedItemFlag |
            NewsFeedProperty.FeedItemReadState |
            NewsFeedProperty.FeedItemCommentCount |
            NewsFeedProperty.FeedItemNewCommentsRead |
            NewsFeedProperty.FeedItemWatchComments |
            NewsFeedProperty.FeedCredentials;
        public bool IsCacheRelevantChange(NewsFeedProperty changedProperty)
        {
            return (cacheRelevantPropertyChanges & changedProperty) != NewsFeedProperty.None;
        }
        private const NewsFeedProperty subscriptionRelevantPropertyChanges =
            NewsFeedProperty.FeedLink |
            NewsFeedProperty.FeedTitle |
            NewsFeedProperty.FeedCategory |
            NewsFeedProperty.FeedItemsDeleteUndelete |
            NewsFeedProperty.FeedItemReadState |
            NewsFeedProperty.FeedMaxItemAge |
            NewsFeedProperty.FeedRefreshRate |
            NewsFeedProperty.FeedCacheUrl |
            NewsFeedProperty.FeedAdded |
            NewsFeedProperty.FeedRemoved |
            NewsFeedProperty.FeedCategoryAdded |
            NewsFeedProperty.FeedCategoryRemoved |
            NewsFeedProperty.FeedAlertOnNewItemsReceived |
            NewsFeedProperty.FeedMarkItemsReadOnExit |
            NewsFeedProperty.General;
        public bool IsSubscriptionRelevantChange(NewsFeedProperty changedProperty)
        {
            return (subscriptionRelevantPropertyChanges & changedProperty) != NewsFeedProperty.None;
        }
        public void ApplyFeedModifications(string feedUrl)
        {
            if (feedUrl == null || feedUrl.Length == 0)
                throw new ArgumentNullException("feedUrl");
            FeedDetailsInternal fi = null;
            NewsFeed f = null;
            if (itemsTable.ContainsKey(feedUrl))
            {
                fi = itemsTable[feedUrl];
            }
            if (this.FeedsTable.ContainsKey(feedUrl))
            {
                f = this.FeedsTable[feedUrl];
            }
            if (fi != null && f != null)
            {
                try
                {
                    f.cacheurl = this.SaveFeed(f);
                }
                catch (Exception ex)
                {
                    Trace("ApplyFeedModifications() cause exception while saving feed '{0}'to cache: {1}", feedUrl,
                          ex.Message);
                }
            }
        }
        private static bool IsPropertyValueSet(object value, string propertyName, object owner)
        {
            if (value == null)
            {
                return false;
            }
            else if (value is string)
            {
                bool isSet = !string.IsNullOrEmpty((string) value);
                if (propertyName.Equals("maxitemage") && isSet)
                {
                    isSet = !value.Equals(XmlConvert.ToString(TimeSpan.MaxValue));
                }
                return isSet;
            }
            else
            {
                return (bool) owner.GetType().GetField(propertyName + "Specified").GetValue(owner);
            }
        }
        private object GetFeedProperty(string feedUrl, string propertyName)
        {
            return this.GetFeedProperty(feedUrl, propertyName, false);
        }
        private object GetFeedProperty(string feedUrl, string propertyName, bool inheritCategory)
        {
            object value =
                this.GetType().GetField(propertyName, BindingFlags.NonPublic | BindingFlags.Instance).GetValue(this);
            if (_feedsTable.ContainsKey(feedUrl))
            {
                NewsFeed f = this.FeedsTable[feedUrl];
                object f_value = f.GetType().GetField(propertyName).GetValue(f);
                if (IsPropertyValueSet(f_value, propertyName, f))
                {
                    if (propertyName.Equals("maxitemage"))
                    {
                        f_value = XmlConvert.ToTimeSpan((string) f_value);
                    }
                    value = f_value;
                }
                else if (inheritCategory && !string.IsNullOrEmpty(f.category))
                {
                    category c = this.Categories.GetByKey(f.category);
                    while (c != null)
                    {
                        object c_value = c.GetType().GetField(propertyName).GetValue(c);
                        if (IsPropertyValueSet(c_value, propertyName, c))
                        {
                            if (propertyName.Equals("maxitemage"))
                            {
                                c_value = XmlConvert.ToTimeSpan((string) c_value);
                            }
                            value = c_value;
                            break;
                        }
                        else
                        {
                            c = c.parent;
                        }
                    }
                }
            }
            return value;
        }
        private void SetFeedProperty(string feedUrl, string propertyName, object value)
        {
            if (_feedsTable.ContainsKey(feedUrl))
            {
                NewsFeed f = this.FeedsTable[feedUrl];
                if (value is TimeSpan)
                {
                    value = XmlConvert.ToString((TimeSpan) value);
                }
                f.GetType().GetField(propertyName).SetValue(f, value);
                if ((value != null) && !(value is string))
                {
                    f.GetType().GetField(propertyName + "Specified").SetValue(f, true);
                }
            }
        }
        public void SetMaxItemAge(string feedUrl, TimeSpan age)
        {
            this.SetFeedProperty(feedUrl, "maxitemage", age);
        }
        public TimeSpan GetMaxItemAge(string feedUrl)
        {
            return (TimeSpan) this.GetFeedProperty(feedUrl, "maxitemage", true);
        }
        public void SetRefreshRate(string feedUrl, int refreshRate)
        {
            this.SetFeedProperty(feedUrl, "refreshrate", refreshRate);
        }
        public int GetRefreshRate(string feedUrl)
        {
            return (int) this.GetFeedProperty(feedUrl, "refreshrate", true);
        }
        public void SetStyleSheet(string feedUrl, string style)
        {
            this.SetFeedProperty(feedUrl, "stylesheet", style);
        }
        public string GetStyleSheet(string feedUrl)
        {
            return (string) this.GetFeedProperty(feedUrl, "stylesheet");
        }
        public void SetEnclosureFolder(string feedUrl, string folder)
        {
            this.SetFeedProperty(feedUrl, "enclosurefolder", folder);
        }
        public string GetEnclosureFolder(string feedUrl, string filename)
        {
            string folderName = (IsPodcast(filename) ? this.PodcastFolder : this.EnclosureFolder);
            if (this.CreateSubfoldersForEnclosures && this.FeedsTable.ContainsKey(feedUrl))
            {
                NewsFeed f = FeedsTable[feedUrl];
                folderName = Path.Combine(folderName, FileHelper.CreateValidFileName(f.title));
            }
            return folderName;
        }
        public void SetFeedColumnLayout(string feedUrl, string layout)
        {
            this.SetFeedProperty(feedUrl, "listviewlayout", layout);
        }
        public string GetFeedColumnLayout(string feedUrl)
        {
            return (string) this.GetFeedProperty(feedUrl, "listviewlayout");
        }
        public void SetMarkItemsReadOnExit(string feedUrl, bool markitemsread)
        {
            this.SetFeedProperty(feedUrl, "markitemsreadonexit", markitemsread);
        }
        public bool GetMarkItemsReadOnExit(string feedUrl)
        {
            return (bool) this.GetFeedProperty(feedUrl, "markitemsreadonexit");
        }
        public void SetDownloadEnclosures(string feedUrl, bool download)
        {
            this.SetFeedProperty(feedUrl, "downloadenclosures", download);
        }
        public bool GetDownloadEnclosures(string feedUrl)
        {
            return (bool) this.GetFeedProperty(feedUrl, "downloadenclosures");
        }
        public void SetEnclosureAlert(string feedUrl, bool alert)
        {
            this.SetFeedProperty(feedUrl, "enclosurealert", alert);
        }
        public bool GetEnclosureAlert(string feedUrl)
        {
            return (bool) this.GetFeedProperty(feedUrl, "enclosurealert");
        }
        private object GetCategoryProperty(string category, string propertyName)
        {
            object value =
                this.GetType().GetField(propertyName, BindingFlags.NonPublic | BindingFlags.Instance).GetValue(this);
            if (!string.IsNullOrEmpty(category))
            {
                category c = this.Categories.GetByKey(category);
                while (c != null)
                {
                    object c_value = c.GetType().GetField(propertyName).GetValue(c);
                    if (IsPropertyValueSet(c_value, propertyName, c))
                    {
                        if (propertyName.Equals("maxitemage"))
                        {
                            c_value = XmlConvert.ToTimeSpan((string) c_value);
                        }
                        value = c_value;
                        break;
                    }
                    else
                    {
                        c = c.parent;
                    }
                }
            }
            return value;
        }
        private void SetCategoryProperty(string category, string propertyName, object value)
        {
            if (!string.IsNullOrEmpty(category))
            {
                foreach (category c in this.Categories.Values)
                {
                    if (c.Value.Equals(category) || c.Value.StartsWith(category + CategorySeparator))
                    {
                        if (value is TimeSpan)
                        {
                            value = XmlConvert.ToString((TimeSpan) value);
                        }
                        c.GetType().GetField(propertyName).SetValue(c, value);
                        if ((value != null) && !(value is string))
                        {
                            c.GetType().GetField(propertyName + "Specified").SetValue(c, true);
                        }
                        break;
                    }
                }
            }
        }
        public void SetCategoryMaxItemAge(string category, TimeSpan age)
        {
            this.SetCategoryProperty(category, "maxitemage", age);
        }
        public TimeSpan GetCategoryMaxItemAge(string category)
        {
            return (TimeSpan) this.GetCategoryProperty(category, "maxitemage");
        }
        public void SetCategoryRefreshRate(string category, int refreshRate)
        {
            this.SetCategoryProperty(category, "refreshrate", refreshRate);
        }
        public int GetCategoryRefreshRate(string category)
        {
            return (int) this.GetCategoryProperty(category, "refreshrate");
        }
        public void SetCategoryStyleSheet(string category, string style)
        {
            this.SetCategoryProperty(category, "stylesheet", style);
        }
        public string GetCategoryStyleSheet(string category)
        {
            return (string) this.GetCategoryProperty(category, "stylesheet");
        }
        public void SetCategoryEnclosureFolder(string category, string folder)
        {
            this.SetCategoryProperty(category, "enclosurefolder", folder);
        }
        public string GetCategoryEnclosureFolder(string category)
        {
            return (string) this.GetCategoryProperty(category, "enclosurefolder");
        }
        public void SetCategoryFeedColumnLayout(string category, string layout)
        {
            this.SetCategoryProperty(category, "listviewlayout", layout);
        }
        public string GetCategoryFeedColumnLayout(string category)
        {
            return (string) this.GetCategoryProperty(category, "listviewlayout");
        }
        public void SetCategoryMarkItemsReadOnExit(string category, bool markitemsread)
        {
            this.SetCategoryProperty(category, "markitemsreadonexit", markitemsread);
        }
        public bool GetCategoryMarkItemsReadOnExit(string category)
        {
            return (bool) this.GetCategoryProperty(category, "markitemsreadonexit");
        }
        public void SetCategoryDownloadEnclosures(string category, bool download)
        {
            this.SetCategoryProperty(category, "downloadenclosures", download);
        }
        public bool GetCategoryDownloadEnclosures(string category)
        {
            return (bool) this.GetCategoryProperty(category, "downloadenclosures");
        }
        public void SetCategoryEnclosureAlert(string category, bool alert)
        {
            this.SetCategoryProperty(category, "enclosurealert", alert);
        }
        public bool GetCategoryEnclosureAlert(string category)
        {
            return (bool) this.GetCategoryProperty(category, "enclosurealert");
        }
        public IFeedDetails GetFeedInfo(string feedUrl)
        {
            return this.GetFeedInfo(feedUrl, null);
        }
        public IFeedDetails GetFeedInfo(string feedUrl, ICredentials credentials)
        {
            if (string.IsNullOrEmpty(feedUrl))
                return null;
            FeedDetailsInternal fd = null;
            if (!itemsTable.ContainsKey(feedUrl))
            {
                NewsFeed theFeed = FeedsTable[feedUrl];
                if (theFeed == null)
                {
                    using (
                        Stream mem =
                            AsyncWebRequest.GetSyncResponseStream(feedUrl, credentials, this.UserAgent, this.Proxy))
                    {
                        NewsFeed f = new NewsFeed();
                        f.link = feedUrl;
                        if (RssParser.CanProcessUrl(feedUrl))
                        {
                            fd = RssParser.GetItemsForFeed(f, mem, false);
                        }
                    }
                    return fd;
                }
                fd = this.GetFeed(theFeed);
                lock (itemsTable)
                {
                    if (!itemsTable.ContainsKey(feedUrl) && (fd != null))
                    {
                        itemsTable.Add(feedUrl, fd);
                    }
                }
            }
            else
            {
                fd = itemsTable[feedUrl];
            }
            return fd;
        }
        public List<NewsItem> GetItemsForFeed(NewsFeed f)
        {
            List<NewsItem> returnList = EmptyItemList;
            if (this.offline)
                return returnList;
            ICredentials c = null;
            if (RssHelper.IsNntpUrl(f.link))
            {
                try
                {
                    Uri feedUri = new Uri(f.link);
                    foreach (NntpServerDefinition nsd in this.nntpServers.Values)
                    {
                        if (nsd.Server.Equals(feedUri.Authority))
                        {
                            c = this.GetNntpServerCredentials(nsd.Name);
                            break;
                        }
                    }
                }
                catch (UriFormatException)
                {
                    ;
                }
            }
            else
            {
                c = CreateCredentialsFrom(f);
            }
            using (Stream mem = AsyncWebRequest.GetSyncResponseStream(f.link, c, this.UserAgent, this.Proxy))
            {
                if (RssParser.CanProcessUrl(f.link))
                {
                    returnList = RssParser.GetItemsForFeed(f, mem, false).itemsList;
                }
            }
            return returnList;
        }
        public List<NewsItem> GetItemsForFeed(string feedUrl)
        {
            NewsFeed f = new NewsFeed();
            f.link = feedUrl;
            return this.GetItemsForFeed(f);
        }
        public static IFeedDetails GetItemsForFeed(NewsFeed f, XmlReader feedReader, bool cachedStream)
        {
            if (f == null || f.link == null)
                return null;
            if (RssParser.CanProcessUrl(f.link))
            {
                return RssParser.GetItemsForFeed(f, feedReader, cachedStream);
            }
            throw new ApplicationException(ComponentsText.ExceptionNoProcessingHandlerMessage(f.link));
        }
        public static IFeedDetails GetItemsForFeed(NewsFeed f, Stream feedStream, bool cachedStream)
        {
            if (f == null || f.link == null)
                return null;
            if (RssParser.CanProcessUrl(f.link))
            {
                return RssParser.GetItemsForFeed(f, feedStream, cachedStream);
            }
            throw new ApplicationException(ComponentsText.ExceptionNoProcessingHandlerMessage(f.link));
        }
        public IList<NewsItem> GetItemsForFeed(string feedUrl, bool force_download)
        {
            string url2Access = feedUrl;
            if (((!force_download) || this.offline) && itemsTable.ContainsKey(feedUrl))
            {
                return itemsTable[feedUrl].ItemsList;
            }
            NewsFeed theFeed = null;
            if (FeedsTable.ContainsKey(feedUrl))
                theFeed = FeedsTable[feedUrl];
            if (theFeed == null)
                return EmptyItemList;
            try
            {
                if (((!force_download) || this.offline) && (!itemsTable.ContainsKey(feedUrl)) &&
                    ((theFeed.cacheurl != null) && (theFeed.cacheurl.Length > 0) &&
                     (this.CacheHandler.FeedExists(theFeed))))
                {
                    bool getFromCache;
                    lock (itemsTable)
                    {
                        getFromCache = !itemsTable.ContainsKey(feedUrl);
                    }
                    if (getFromCache)
                    {
                        FeedDetailsInternal fi = this.GetFeed(theFeed);
                        if (fi != null)
                        {
                            lock (itemsTable)
                            {
                                if (!itemsTable.ContainsKey(feedUrl))
                                    itemsTable.Add(feedUrl, fi);
                            }
                        }
                    }
                    return itemsTable[feedUrl].ItemsList;
                }
            }
            catch (Exception ex)
            {
                Trace("Error retrieving feed '{0}' from cache: {1}", feedUrl, ex.ToString());
            }
            if (this.offline)
            {
                return EmptyItemList;
            }
            try
            {
                new Uri(url2Access);
            }
            catch (UriFormatException ufex)
            {
                Trace("Uri format exception on '{0}': {1}", url2Access, ufex.Message);
                throw;
            }
            this.AsyncGetItemsForFeed(feedUrl, true, true);
            return EmptyItemList;
        }
        public int AsyncRequestsPending()
        {
            return this.AsyncWebRequest.PendingRequests;
        }
        public NewsItem CopyNewsItemTo(NewsItem item, NewsFeed f)
        {
            if (!item.HasContent)
                this.GetCachedContentForItem(item);
            return item.CopyTo(f);
        }
        public void GetCachedContentForItem(NewsItem item)
        {
            this.CacheHandler.LoadItemContent(item);
        }
        public IList<NewsItem> GetCachedItemsForFeed(string feedUrl)
        {
            lock (itemsTable)
            {
                if (itemsTable.ContainsKey(feedUrl))
                {
                    return itemsTable[feedUrl].ItemsList;
                }
            }
            NewsFeed theFeed = null;
            try
            {
                if (FeedsTable.TryGetValue(feedUrl, out theFeed))
                {
                    if ((theFeed.cacheurl != null) && (theFeed.cacheurl.Trim().Length > 0) &&
                        (this.CacheHandler.FeedExists(theFeed)))
                    {
                        bool getFromCache;
                        lock (itemsTable)
                        {
                            getFromCache = !itemsTable.ContainsKey(feedUrl);
                        }
                        if (getFromCache)
                        {
                            FeedDetailsInternal fi = this.GetFeed(theFeed);
                            if (fi != null)
                            {
                                lock (itemsTable)
                                {
                                    if (!itemsTable.ContainsKey(feedUrl))
                                        itemsTable.Add(feedUrl, fi);
                                }
                            }
                        }
                        return itemsTable[feedUrl].ItemsList;
                    }
                }
            }
            catch (FileNotFoundException)
            {
            }
            catch (XmlException xe)
            {
                Trace("Xml Error retrieving feed '{0}' from cache: {1}", feedUrl, xe.ToString());
                this.CacheHandler.RemoveFeed(theFeed);
            }
            catch (Exception ex)
            {
                Trace("Error retrieving feed '{0}' from cache: {1}", feedUrl, ex.ToString());
                if (theFeed != null && !theFeed.causedException)
                {
                    theFeed.causedException = true;
                    RaiseOnUpdateFeedException(feedUrl,
                                               new Exception(
                                                   "Error retrieving feed {" + feedUrl + "} from cache: " + ex.Message,
                                                   ex), 11);
                }
            }
            return EmptyItemList;
        }
        public bool AsyncGetItemsForFeed(string feedUrl, bool force_download)
        {
            return this.AsyncGetItemsForFeed(feedUrl, force_download, false);
        }
        public bool AsyncGetItemsForFeed(string feedUrl, bool force_download, bool manual)
        {
            if (feedUrl == null || feedUrl.Trim().Length == 0)
                throw new ArgumentNullException("feedUrl");
            string etag = null;
            bool requestQueued = false;
            int priority = 10;
            if (force_download)
                priority += 100;
            if (manual)
                priority += 1000;
            try
            {
                Uri reqUri = new Uri(feedUrl);
                try
                {
                    if ((!force_download) || this.offline)
                    {
                        GetCachedItemsForFeed(feedUrl);
                        RaiseOnUpdatedFeed(reqUri, null, RequestResult.NotModified, priority, false);
                        return false;
                    }
                }
                catch (XmlException xe)
                {
                    Trace("Unexpected error retrieving cached feed '{0}': {1}", feedUrl, xe.ToString());
                }
                NewsFeed theFeed = null;
                if (FeedsTable.ContainsKey(feedUrl))
                    theFeed = FeedsTable[feedUrl];
                if (theFeed == null)
                    return false;
                RaiseOnUpdateFeedStarted(reqUri, force_download, priority);
                DateTime lastModified = DateTime.MinValue;
                if (itemsTable.ContainsKey(feedUrl))
                {
                    etag = theFeed.etag;
                    lastModified = (theFeed.lastretrievedSpecified ? theFeed.lastretrieved : theFeed.lastmodified);
                }
                ICredentials c;
                if (RssHelper.IsNntpUrl(theFeed.link))
                {
                    c = GetNntpServerCredentials(theFeed);
                }
                else
                {
                    c = CreateCredentialsFrom(theFeed);
                }
                RequestParameter reqParam =
                    RequestParameter.Create(reqUri, this.UserAgent, this.Proxy, c, lastModified, etag);
                reqParam.SetCookies = SetCookies;
                AsyncWebRequest.QueueRequest(reqParam,
                                             null ,
                                             OnRequestStart,
                                             OnRequestComplete,
                                             OnRequestException, priority);
                requestQueued = true;
            }
            catch (Exception e)
            {
                Trace("Unexpected error on QueueRequest(), processing feed '{0}': {1}", feedUrl, e.ToString());
                RaiseOnUpdateFeedException(feedUrl, e, priority);
            }
            return requestQueued;
        }
        public Hashtable GetFailureContext(Uri feedUri)
        {
            NewsFeed f = null;
            if (feedUri == null || !FeedsTable.TryGetValue(feedUri.CanonicalizedUri(), out f))
                return new Hashtable();
            return this.GetFailureContext(f);
        }
        public Hashtable GetFailureContext(string feedUri)
        {
            if (feedUri == null)
                return new Hashtable();
            if (FeedsTable.ContainsKey(feedUri))
                return this.GetFailureContext(FeedsTable[feedUri]);
            else
                return new Hashtable();
        }
        public Hashtable GetFailureContext(NewsFeed f)
        {
            if (f == null)
            {
                return new Hashtable();
            }
            FeedInfo fi = null;
            lock (itemsTable)
            {
                if (itemsTable.ContainsKey(f.link))
                {
                    fi = itemsTable[f.link] as FeedInfo;
                }
            }
            return GetFailureContext(f, fi);
        }
        public static Hashtable GetFailureContext(NewsFeed f, IFeedDetails fi)
        {
            Hashtable context = new Hashtable();
            if (f == null)
            {
                return context;
            }
            context.Add("FULL_TITLE", (f.category ?? String.Empty) + CategorySeparator + f.title);
            context.Add("FAILURE_OBJECT", f);
            if (fi == null)
                return context;
            context.Add("PUBLISHER_HOMEPAGE", fi.Link);
            XmlElement xe = RssHelper.GetOptionalElement(fi.OptionalElements, "managingEditor", String.Empty);
            if (xe != null)
                context.Add("PUBLISHER", xe.InnerText);
            xe = RssHelper.GetOptionalElement(fi.OptionalElements, "webMaster", String.Empty);
            if (xe != null)
            {
                context.Add("TECH_CONTACT", xe.InnerText);
            }
            else
            {
                xe = RssHelper.GetOptionalElement(fi.OptionalElements, "errorReportsTo", "http://webns.net/mvcb/");
                if (xe != null && xe.Attributes["resource", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"] != null)
                    context.Add("TECH_CONTACT",
                                xe.Attributes["resource", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"].InnerText);
            }
            xe = RssHelper.GetOptionalElement(fi.OptionalElements, "generator", String.Empty);
            if (xe != null)
                context.Add("GENERATOR", xe.InnerText);
            return context;
        }
        private void OnRequestStart(Uri requestUri, ref bool cancel)
        {
            Trace("AsyncRequest.OnRequestStart('{0}') downloading", requestUri.ToString());
            this.RaiseOnDownloadFeedStarted(requestUri, ref cancel);
            if (!cancel)
                cancel = this.Offline;
        }
        private void OnRequestException(Uri requestUri, Exception e, int priority)
        {
            Trace("AsyncRequst.OnRequestException() fetching '{0}': {1}", requestUri.ToString(), e.ToString());
            string key = requestUri.CanonicalizedUri();
            if (this.FeedsTable.ContainsKey(key))
            {
                Trace("AsyncRequest.OnRequestException() '{0}' found in feedsTable.", requestUri.ToString());
                NewsFeed f = FeedsTable[key];
                f.causedException = true;
            }
            else
            {
                Trace("AsyncRequst.OnRequestException() '{0}' NOT found in feedsTable.", requestUri.ToString());
            }
            RaiseOnUpdateFeedException(requestUri.CanonicalizedUri(), e, priority);
        }
        private void OnRequestComplete(Uri requestUri, Stream response, Uri newUri, string eTag, DateTime lastModified,
                                       RequestResult result, int priority)
        {
            Trace("AsyncRequest.OnRequestComplete: '{0}': {1}", requestUri.ToString(), result);
            if (newUri != null)
                Trace("AsyncRequest.OnRequestComplete: perma redirect of '{0}' to '{1}'.", requestUri.ToString(),
                      newUri.ToString());
            IList<NewsItem> itemsForFeed;
            bool firstSuccessfulDownload = false;
            try
            {
                NewsFeed theFeed = null;
                if (!FeedsTable.TryGetValue(requestUri.CanonicalizedUri(), out theFeed))
                {
                    Trace("ATTENTION! FeedsTable[requestUri] as NewsFeed returns null for: '{0}'",
                          requestUri.ToString());
                    return;
                }
                string feedUrl = theFeed.link;
                if (true)
                {
                    if (String.Compare(feedUrl, requestUri.CanonicalizedUri(), true) != 0)
                        Trace("feed.link != requestUri: \r\n'{0}'\r\n'{1}'", feedUrl, requestUri.CanonicalizedUri());
                }
                if (newUri != null)
                {
                    FeedsTable.Remove(feedUrl);
                    theFeed.link = newUri.CanonicalizedUri();
                    FeedsTable.Add(theFeed.link, theFeed);
                    lock (itemsTable)
                    {
                        if (itemsTable.ContainsKey(feedUrl))
                        {
                            FeedDetailsInternal FI = itemsTable[feedUrl];
                            itemsTable.Remove(feedUrl);
                            itemsTable.Remove(theFeed.link);
                            itemsTable.Add(theFeed.link, FI);
                        }
                    }
                    feedUrl = theFeed.link;
                }
                if (result == RequestResult.OK)
                {
                    FeedDetailsInternal fi;
                    if ((requestUri.Scheme == NntpWebRequest.NntpUriScheme) ||
                        (requestUri.Scheme == NntpWebRequest.NewsUriScheme))
                    {
                        fi = NntpParser.GetItemsForNewsGroup(theFeed, response, false);
                    }
                    else
                    {
                        fi = RssParser.GetItemsForFeed(theFeed, response, false);
                    }
                    FeedDetailsInternal fiFromCache = null;
                    try
                    {
                        if (!itemsTable.ContainsKey(feedUrl))
                        {
                            fiFromCache = this.GetFeed(theFeed);
                        }
                    }
                    catch (Exception ex)
                    {
                        Trace("this.GetFeed(theFeed) caused exception: {0}", ex.ToString());
                    }
                    List<NewsItem> newReceivedItems = null;
                    lock (itemsTable)
                    {
                        if (!itemsTable.ContainsKey(feedUrl) && (fiFromCache != null))
                        {
                            itemsTable.Add(feedUrl, fiFromCache);
                        }
                        if (itemsTable.ContainsKey(feedUrl))
                        {
                            FeedDetailsInternal fi2 = itemsTable[feedUrl];
                            if (RssParser.CanProcessUrl(feedUrl))
                            {
                                fi.ItemsList = MergeAndPurgeItems(fi2.ItemsList, fi.ItemsList, theFeed.deletedstories,
                                                                  out newReceivedItems, theFeed.replaceitemsonrefresh);
                            }
                            if ((String.Compare(fi2.Link, fi.Link, true) != 0) &&
                                (newReceivedItems.Count == fi.ItemsList.Count))
                            {
                                foreach (FeedDetailsInternal fdi in itemsTable.Values)
                                {
                                    if (String.Compare(fdi.Link, fi.Link, true) == 0)
                                    {
                                        RaiseOnUpdatedFeed(requestUri, null, RequestResult.NotModified, priority, false);
                                        _log.Error(
                                            String.Format(
                                                "Feed mixup encountered when downloading {2} because fi2.link != fi.link: {0}!= {1}",
                                                fi2.Link, fi.Link, requestUri.AbsoluteUri));
                                        return;
                                    }
                                }
                            }
                            itemsTable.Remove(feedUrl);
                        }
                        else
                        {
                            firstSuccessfulDownload = true;
                            newReceivedItems = fi.ItemsList;
                            RelationCosmosAddRange(newReceivedItems);
                        }
                        itemsTable.Add(feedUrl, fi);
                    }
                    theFeed.etag = eTag;
                    if (lastModified > theFeed.lastmodified)
                    {
                        theFeed.lastmodified = lastModified;
                    }
                    theFeed.lastretrieved = new DateTime(DateTime.Now.Ticks);
                    theFeed.lastretrievedSpecified = true;
                    theFeed.cacheurl = this.SaveFeed(theFeed);
                    this.SearchHandler.IndexAdd(newReceivedItems);
                    theFeed.causedException = false;
                    itemsForFeed = fi.ItemsList;
                    if (this.GetDownloadEnclosures(theFeed.link))
                    {
                        int numDownloaded = 0;
                        int maxDownloads = (firstSuccessfulDownload
                                                ? this.NumEnclosuresToDownloadOnNewFeed
                                                : Int32.MaxValue);
                        if (newReceivedItems != null)
                            foreach (NewsItem ni in newReceivedItems)
                            {
                                if (numDownloaded >= maxDownloads)
                                {
                                    MarkEnclosuresDownloaded(ni);
                                    continue;
                                }
                                try
                                {
                                    numDownloaded += this.DownloadEnclosure(ni, maxDownloads - numDownloaded);
                                }
                                catch (DownloaderException de)
                                {
                                    _log.Error("Error occured when downloading enclosures in OnRequestComplete():", de);
                                }
                            }
                    }
                    theFeed.containsNewMessages = false;
                    theFeed.storiesrecentlyviewed.Clear();
                    foreach (NewsItem ri in itemsForFeed)
                    {
                        if (ri.BeenRead)
                        {
                            theFeed.storiesrecentlyviewed.Add(ri.Id);
                        }
                        if (ri.HasNewComments)
                        {
                            theFeed.containsNewComments = true;
                        }
                    }
                    if (itemsForFeed.Count > theFeed.storiesrecentlyviewed.Count)
                    {
                        theFeed.containsNewMessages = true;
                    }
                }
                else if (result == RequestResult.NotModified)
                {
                    theFeed.lastretrieved = new DateTime(DateTime.Now.Ticks);
                    theFeed.lastretrievedSpecified = true;
                    theFeed.causedException = false;
                }
                else
                {
                    throw new NotImplementedException("Unhandled RequestResult: " + result);
                }
                RaiseOnUpdatedFeed(requestUri, newUri, result, priority, firstSuccessfulDownload);
            }
            catch (Exception e)
            {
                string key = requestUri.CanonicalizedUri();
                if (this.FeedsTable.ContainsKey(key))
                {
                    Trace("AsyncRequest.OnRequestComplete('{0}') Exception: ", requestUri.ToString(), e.StackTrace);
                    NewsFeed f = FeedsTable[key];
                    f.causedException = true;
                }
                else
                {
                    Trace("AsyncRequest.OnRequestComplete('{0}') Exception on feed not contained in FeedsTable: ",
                          requestUri.ToString(), e.StackTrace);
                }
                RaiseOnUpdateFeedException(requestUri.CanonicalizedUri(), e, priority);
            }
            finally
            {
                if (response != null)
                    response.Close();
            }
        }
        private void OnAllRequestsComplete()
        {
            RaiseOnAllAsyncRequestsCompleted();
        }
        private void OnEnclosureDownloadComplete(object sender, DownloadItemEventArgs e)
        {
            if (this.OnDownloadedEnclosure != null)
            {
                try
                {
                    this.OnDownloadedEnclosure(sender, e);
                }
                catch
                {
                }
            }
        }
        private static readonly byte[] ico_magic = new byte[] {0, 0, 1, 0};
        private static readonly int ico_magic_len = ico_magic.Length;
        private static readonly byte[] png_magic = new byte[] {0x89, 0x50, 0x4e, 0x47};
        private static readonly int png_magic_len = png_magic.Length;
        private static readonly byte[] gif_magic = new byte[] {0x47, 0x49, 0x46};
        private static readonly int gif_magic_len = gif_magic.Length;
        private static readonly byte[] jpg_magic = new byte[] {0xff, 0xd8};
        private static readonly int jpg_magic_len = jpg_magic.Length;
        private static readonly byte[] bmp_magic = new byte[] {0x42, 0x4d};
        private static readonly int bmp_magic_len = bmp_magic.Length;
        private static string GetExtensionForDetectedImage(byte[] bytes)
        {
            if (bytes == null)
                throw new ArgumentNullException("bytes");
            int i, len = bytes.Length;
            for (i = 0; i < jpg_magic_len && i < len; i++)
            {
                if (bytes[i] != jpg_magic[i]) break;
            }
            if (i == jpg_magic_len) return ".jpg";
            for (i = 0; i < ico_magic_len && i < len; i++)
            {
                if (bytes[i] != ico_magic[i]) break;
            }
            if (i == ico_magic_len) return ".ico";
            for (i = 0; i < png_magic_len && i < len; i++)
            {
                if (bytes[i] != png_magic[i]) break;
            }
            if (i == png_magic_len) return ".png";
            for (i = 0; i < gif_magic_len && i < len; i++)
            {
                if (bytes[i] != gif_magic[i]) break;
            }
            if (i == gif_magic_len) return ".gif";
            for (i = 0; i < bmp_magic_len && i < len; i++)
            {
                if (bytes[i] != bmp_magic[i]) break;
            }
            if (i == bmp_magic_len) return ".bmp";
            return null;
        }
        private void OnFaviconRequestComplete(Uri requestUri, Stream response, Uri newUri, string eTag,
                                              DateTime lastModified, RequestResult result, int priority)
        {
            Trace("AsyncRequest.OnFaviconRequestComplete: '{0}': {1}", requestUri.ToString(), result);
            if (newUri != null)
                Trace("AsyncRequest.OnFaviconRequestComplete: perma redirect of '{0}' to '{1}'.", requestUri.ToString(),
                      newUri.ToString());
            try
            {
                StringCollection feedUrls = new StringCollection();
                string favicon = null;
                if (result == RequestResult.OK)
                {
                    BinaryReader br = new BinaryReader(response);
                    byte[] bytes = new byte[response.Length];
                    if (bytes.Length > 0)
                    {
                        bytes = br.ReadBytes((int) response.Length);
                        string ext = GetExtensionForDetectedImage(bytes);
                        if (ext != null)
                        {
                            favicon = GenerateFaviconUrl(requestUri, ext);
                            string filelocation = Path.Combine(this.CacheHandler.CacheLocation, favicon);
                            using (FileStream fs = FileHelper.OpenForWrite(filelocation))
                            {
                                BinaryWriter bw = new BinaryWriter(fs);
                                bw.Write(bytes);
                                bw.Flush();
                            }
                        }
                    }
                    else
                    {
                    }
                    string[] keys;
                    lock (FeedsTable)
                    {
                        keys = new string[FeedsTable.Count];
                        if (FeedsTable.Count > 0)
                            FeedsTable.Keys.CopyTo(keys, 0);
                    }
                    foreach (string feedUrl in keys)
                    {
                        if (itemsTable.ContainsKey(feedUrl))
                        {
                            string websiteUrl = ((FeedInfo) itemsTable[feedUrl]).Link;
                            Uri uri = null;
                            try
                            {
                                uri = new Uri(websiteUrl);
                            }
                            catch (Exception)
                            {
                                ;
                            }
                            if ((uri != null) && uri.Authority.Equals(requestUri.Authority))
                            {
                                feedUrls.Add(feedUrl);
                                NewsFeed f = FeedsTable[feedUrl];
                                f.favicon = favicon;
                            }
                        }
                    }
                }
                if (favicon != null)
                {
                    RaiseOnUpdatedFavicon(favicon, feedUrls);
                }
            }
            catch (Exception e)
            {
                Trace("AsyncRequest.OnFaviconRequestComplete('{0}') Exception on fetching favicon at: ",
                      requestUri.ToString(), e.StackTrace);
            }
            finally
            {
                if (response != null)
                    response.Close();
            }
        }
        private void RaiseOnDownloadFeedStarted(Uri requestUri, ref bool cancel)
        {
            if (BeforeDownloadFeedStarted != null)
            {
                try
                {
                    DownloadFeedCancelEventArgs ea = new DownloadFeedCancelEventArgs(requestUri, cancel);
                    BeforeDownloadFeedStarted(this, ea);
                    cancel = ea.Cancel;
                }
                catch
                {
                }
            }
        }
        private void RaiseOnUpdatedFavicon(string favicon, StringCollection feedUrls)
        {
            if (OnUpdatedFavicon != null)
            {
                try
                {
                    OnUpdatedFavicon(this, new UpdatedFaviconEventArgs(favicon, feedUrls));
                }
                catch
                {
                }
            }
        }
        private void RaiseOnUpdatedFeed(Uri requestUri, Uri newUri, RequestResult result, int priority,
                                        bool firstSuccessfulDownload)
        {
            if (OnUpdatedFeed != null)
            {
                try
                {
                    OnUpdatedFeed(this,
                                  new UpdatedFeedEventArgs(requestUri, newUri, result, priority, firstSuccessfulDownload));
                }
                catch
                {
                }
            }
        }
        private void RaiseOnUpdateFeedException(string requestUri, Exception e, int priority)
        {
            if (OnUpdateFeedException != null)
            {
                try
                {
                    if (requestUri != null && RssParser.CanProcessUrl(requestUri))
                        e = new FeedRequestException(e.Message, e, this.GetFailureContext(requestUri));
                    OnUpdateFeedException(this, new UpdateFeedExceptionEventArgs(requestUri, e, priority));
                }
                catch
                {
                }
            }
        }
        private void RaiseOnAllAsyncRequestsCompleted()
        {
            if (OnAllAsyncRequestsCompleted != null)
            {
                try
                {
                    OnAllAsyncRequestsCompleted(this, new EventArgs());
                }
                catch
                {
                }
            }
        }
        private void RaiseOnUpdateFeedsStarted(bool forced)
        {
            if (UpdateFeedsStarted != null)
            {
                try
                {
                    UpdateFeedsStarted(this, new UpdateFeedsEventArgs(forced));
                }
                catch
                {
                }
            }
        }
        private void RaiseOnUpdateFeedStarted(Uri feedUri, bool forced, int priority)
        {
            if (UpdateFeedStarted != null)
            {
                try
                {
                    UpdateFeedStarted(this, new UpdateFeedEventArgs(feedUri, forced, priority));
                }
                catch
                {
                }
            }
        }
        private static string GenerateFaviconUrl(Uri uri, string extension)
        {
            return uri.Authority.Replace(".", "-") + extension;
        }
        public bool IsPodcast(string filename)
        {
            if (string.IsNullOrEmpty(filename))
            {
                return false;
            }
            string fileext = Path.GetExtension(filename);
            if (fileext.Length > 1)
            {
                fileext = fileext.Substring(1);
                foreach (string podcastExt in this.podcastfileextensions)
                {
                    if (fileext.ToLower().Equals(podcastExt.ToLower()))
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        private static void MarkEnclosuresDownloaded(NewsItem item)
        {
            if (item == null)
            {
                return;
            }
            foreach (Enclosure enc in item.Enclosures)
            {
                enc.Downloaded = true;
            }
        }
        private int DownloadEnclosure(NewsItem item, int maxNumToDownload)
        {
            int numDownloaded = 0;
            if ((maxNumToDownload > 0) && (item != null) && (item.Enclosures.Count > 0))
            {
                foreach (Enclosure enc in item.Enclosures)
                {
                    DownloadItem di = new DownloadItem(item.Feed.link, item.Id, enc, this.enclosureDownloader);
                    if (!enc.Downloaded)
                    {
                        this.enclosureDownloader.BeginDownload(di);
                        enc.Downloaded = true;
                        numDownloaded++;
                    }
                    if (numDownloaded >= maxNumToDownload) break;
                }
            }
            if (item != null && numDownloaded < item.Enclosures.Count)
            {
                MarkEnclosuresDownloaded(item);
            }
            return numDownloaded;
        }
        public void DownloadEnclosure(NewsItem item)
        {
            this.DownloadEnclosure(item, Int32.MaxValue);
        }
        public void DownloadEnclosure(NewsItem item, string fileName)
        {
            if ((item != null) && (item.Enclosures.Count > 0))
            {
                foreach (Enclosure enc in item.Enclosures)
                {
                    if (enc.Url.EndsWith(fileName))
                    {
                        DownloadItem di = new DownloadItem(item.Feed.link, item.Id, enc, this.enclosureDownloader);
                        this.enclosureDownloader.BeginDownload(di);
                        enc.Downloaded = true;
                        break;
                    }
                }
            }
        }
        public void ResumePendingDownloads()
        {
            this.enclosureDownloader.ResumePendingDownloads();
        }
        public void RefreshFavicons()
        {
            if ((this.FeedsListOK == false) || this.offline)
            {
                return;
            }
            StringCollection websites = new StringCollection();
            try
            {
                string[] keys = GetFeedsTableKeys();
                for (int i = 0, len = keys.Length; i < len; i++)
                {
                    if (!itemsTable.ContainsKey(keys[i]))
                    {
                        continue;
                    }
                    FeedInfo fi = (FeedInfo) itemsTable[keys[i]];
                    Uri webSiteUrl = null;
                    try
                    {
                        webSiteUrl = new Uri(fi.link);
                    }
                    catch (Exception)
                    {
                        ;
                    }
                    if (webSiteUrl == null || !webSiteUrl.Scheme.ToLower().Equals("http"))
                    {
                        continue;
                    }
                    if (!websites.Contains(webSiteUrl.Authority))
                    {
                        UriBuilder reqUri = new UriBuilder("http", webSiteUrl.Authority);
                        reqUri.Path = "favicon.ico";
                        try
                        {
                            webSiteUrl = reqUri.Uri;
                        }
                        catch (UriFormatException)
                        {
                            _log.ErrorFormat("Error creating URL '{0}/{1}' in RefreshFavicons", webSiteUrl,
                                             "favicon.ico");
                            continue;
                        }
                        RequestParameter reqParam = RequestParameter.Create(webSiteUrl, this.UserAgent, this.Proxy,
                                                                             null,
                                                                             DateTime.MinValue,
                                                                             null);
                        reqParam.SetCookies = SetCookies;
                        AsyncWebRequest.QueueRequest(reqParam,
                                                     null ,
                                                     null ,
                                                     OnFaviconRequestComplete,
                                                     null ,
                                                     100 );
                        websites.Add(webSiteUrl.Authority);
                    }
                }
            }
            catch (InvalidOperationException ioe)
            {
                Trace("RefreshFavicons() InvalidOperationException: {0}", ioe.ToString());
            }
        }
        public void RefreshFeeds(bool force_download)
        {
            if (this.FeedsListOK == false)
            {
                return;
            }
            bool anyRequestQueued = false;
            try
            {
                RaiseOnUpdateFeedsStarted(force_download);
                string[] keys = GetFeedsTableKeys();
                for (int i = 0, len = keys.Length; i < len; i++)
                {
                    if (!FeedsTable.ContainsKey(keys[i]))
                        continue;
                    NewsFeed current = FeedsTable[keys[i]];
                    try
                    {
                        if (!force_download && current.causedExceptionCount >= 10)
                        {
                            continue;
                        }
                        if (current.refreshrateSpecified && (current.refreshrate == 0))
                        {
                            continue;
                        }
                        if (itemsTable.ContainsKey(current.link))
                        {
                            if ((!force_download) && current.lastretrievedSpecified)
                            {
                                double timeSinceLastDownload =
                                    DateTime.Now.Subtract(current.lastretrieved).TotalMilliseconds;
                                int refreshRate = current.refreshrateSpecified ? current.refreshrate : this.RefreshRate;
                                if (!DownloadIntervalReached || (timeSinceLastDownload < refreshRate))
                                {
                                    continue;
                                }
                            }
                            if (this.AsyncGetItemsForFeed(current.link, true, false))
                                anyRequestQueued = true;
                        }
                        else
                        {
                            if (current.lastretrievedSpecified && string.IsNullOrEmpty(current.cacheurl))
                            {
                                double timeSinceLastDownload =
                                    DateTime.Now.Subtract(current.lastretrieved).TotalMilliseconds;
                                int refreshRate = current.refreshrateSpecified ? current.refreshrate : this.RefreshRate;
                                if (!DownloadIntervalReached || (timeSinceLastDownload < refreshRate))
                                {
                                    continue;
                                }
                            }
                            if (!force_download)
                            {
                                if (!string.IsNullOrEmpty(current.cacheurl) &&
                                    !this.CacheHandler.FeedExists(current))
                                    force_download = true;
                            }
                            if (this.AsyncGetItemsForFeed(current.link, force_download, false))
                                anyRequestQueued = true;
                        }
                        Thread.Sleep(15);
                    }
                    catch (Exception e)
                    {
                        Trace("RefreshFeeds(bool) unexpected error processing feed '{0}': {1}", keys[i], e.ToString());
                    }
                }
            }
            catch (InvalidOperationException ioe)
            {
                Trace("RefreshFeeds(bool) InvalidOperationException: {0}", ioe.ToString());
            }
            finally
            {
                if (offline || !anyRequestQueued)
                    RaiseOnAllAsyncRequestsCompleted();
            }
        }
        public void RefreshFeeds(string category, bool force_download)
        {
            if (this.FeedsListOK == false)
            {
                return;
            }
            bool anyRequestQueued = false;
            try
            {
                RaiseOnUpdateFeedsStarted(force_download);
                string[] keys = GetFeedsTableKeys();
                for (int i = 0, len = keys.Length; i < len; i++)
                {
                    if (!FeedsTable.ContainsKey(keys[i]))
                        continue;
                    NewsFeed current = FeedsTable[keys[i]];
                    try
                    {
                        if (!force_download && current.causedExceptionCount >= 3)
                        {
                            continue;
                        }
                        if (current.refreshrateSpecified && (current.refreshrate == 0))
                        {
                            continue;
                        }
                        if (itemsTable.ContainsKey(current.link))
                        {
                            if ((!force_download) && current.lastretrievedSpecified)
                            {
                                double timeSinceLastDownload =
                                    DateTime.Now.Subtract(current.lastretrieved).TotalMilliseconds;
                                int refreshRate = current.refreshrateSpecified ? current.refreshrate : this.RefreshRate;
                                if (!DownloadIntervalReached || (timeSinceLastDownload < refreshRate))
                                {
                                    continue;
                                }
                            }
                            if (current.category != null && IsChildOrSameCategory(category, current.category))
                            {
                                if (this.AsyncGetItemsForFeed(current.link, true, false))
                                    anyRequestQueued = true;
                            }
                        }
                        else
                        {
                            if (current.category != null && IsChildOrSameCategory(category, current.category))
                            {
                                if (this.AsyncGetItemsForFeed(current.link, force_download, false))
                                    anyRequestQueued = true;
                            }
                        }
                        Thread.Sleep(15);
                    }
                    catch (Exception e)
                    {
                        Trace("RefreshFeeds(string,bool) unexpected error processing feed '{0}': {1}", current.link,
                              e.ToString());
                    }
                }
            }
            catch (InvalidOperationException ioe)
            {
                Trace("RefreshFeeds(string,bool) InvalidOperationException: {0}", ioe.ToString());
            }
            finally
            {
                if (offline || !anyRequestQueued)
                    RaiseOnAllAsyncRequestsCompleted();
            }
        }
        private static bool IsChildOrSameCategory(string category, string testCategory)
        {
            if (testCategory.Equals(category) || testCategory.StartsWith(category + CategorySeparator))
                return true;
            else
                return false;
        }
        public XmlDocument ConvertFeedList(XmlDocument doc)
        {
            ImportFilter importFilter = new ImportFilter(doc);
            XslTransform transform = importFilter.GetImportXsl();
            if (transform != null)
            {
                XmlDocument temp = new XmlDocument();
                temp.Load(transform.Transform(doc, null));
                doc = temp;
            }
            else
            {
                if (importFilter.Format == ImportFeedFormat.Bandit)
                {
                    XmlParserContext context =
                        new XmlParserContext(null, new RssBanditXmlNamespaceResolver(), null, XmlSpace.None);
                    XmlReader vr = new RssBanditXmlReader(doc.OuterXml, XmlNodeType.Document, context);
                    doc.Load(vr);
                    vr.Close();
                }
                else
                {
                    throw new ApplicationException("Unknown Feed Format.", null);
                }
            }
            return doc;
        }
        public void ReplaceFeedlist(Stream feedlist)
        {
            this.ImportFeedlist(feedlist, String.Empty, true);
        }
        public void ImportFeedlist(Stream feedlist, string category, bool replace)
        {
            XmlDocument doc = new XmlDocument();
            doc.Load(feedlist);
            doc = ConvertFeedList(doc);
            XmlNodeReader reader = new XmlNodeReader(doc);
            XmlSerializer serializer = XmlHelper.SerializerCache.GetSerializer(typeof (feeds));
            feeds myFeeds = (feeds) serializer.Deserialize(reader);
            reader.Close();
            bool keepLocalSettings = true;
            this.ImportFeedlist(myFeeds, category, replace, keepLocalSettings);
        }
        public void ImportFeedlist(feeds myFeeds, string category, bool replace, bool keepLocalSettings)
        {
            CategoriesCollection cats = new CategoriesCollection();
            FeedColumnLayoutCollection colLayouts = new FeedColumnLayoutCollection();
            IDictionary<string, NewsFeed> syncedfeeds = new SortedDictionary<string, NewsFeed>();
            DateTime[] dta = RssHelper.InitialLastRetrievedSettings(myFeeds.feed.Count, this.RefreshRate);
            int dtaCount = dta.Length, count = 0;
            while (myFeeds.feed.Count != 0)
            {
                NewsFeed f1 = myFeeds.feed[0];
                bool isBadUri = false;
                try
                {
                    new Uri(f1.link);
                }
                catch (Exception)
                {
                    isBadUri = true;
                }
                if (isBadUri)
                {
                    myFeeds.feed.RemoveAt(0);
                    continue;
                }
                if (replace && _feedsTable.ContainsKey(f1.link))
                {
                    NewsFeed f2 = _feedsTable[f1.link];
                    if (!keepLocalSettings)
                    {
                        f2.category = f1.category;
                        if ((f2.category != null) && !cats.ContainsKey(f2.category))
                        {
                            cats.Add(f2.category);
                        }
                        if ((f1.listviewlayout != null) && !colLayouts.ContainsKey(f1.listviewlayout))
                        {
                            listviewLayout layout = FindLayout(f1.listviewlayout, myFeeds.listviewLayouts);
                            if (layout != null)
                                colLayouts.Add(f1.listviewlayout, layout.FeedColumnLayout);
                            else
                                f1.listviewlayout = null;
                        }
                        f2.listviewlayout = (f1.listviewlayout ?? f2.listviewlayout);
                        f2.title = f1.title;
                        f2.markitemsreadonexitSpecified = f1.markitemsreadonexitSpecified;
                        if (f1.markitemsreadonexitSpecified)
                        {
                            f2.markitemsreadonexit = f1.markitemsreadonexit;
                        }
                        f2.stylesheet = (f1.stylesheet ?? f2.stylesheet);
                        f2.maxitemage = (f1.maxitemage ?? f2.maxitemage);
                        f2.alertEnabledSpecified = f1.alertEnabledSpecified;
                        f2.alertEnabled = (f1.alertEnabledSpecified ? f1.alertEnabled : f2.alertEnabled);
                        f2.refreshrateSpecified = f1.refreshrateSpecified;
                        f2.refreshrate = (f1.refreshrateSpecified ? f1.refreshrate : f2.refreshrate);
                        f2.authPassword = f1.authPassword;
                        f2.authUser = f1.authUser;
                    }
                    foreach (string story in f1.deletedstories)
                    {
                        if (!f2.deletedstories.Contains(story))
                        {
                            f2.deletedstories.Add(story);
                        }
                    }
                    foreach (string story in f1.storiesrecentlyviewed)
                    {
                        if (!f2.storiesrecentlyviewed.Contains(story))
                        {
                            f2.storiesrecentlyviewed.Add(story);
                        }
                    }
                    if (itemsTable.ContainsKey(f2.link))
                    {
                        List<NewsItem> items = ((FeedInfo) itemsTable[f2.link]).itemsList;
                        foreach (NewsItem item in items)
                        {
                            if (f2.storiesrecentlyviewed.Contains(item.Id))
                            {
                                item.BeenRead = true;
                            }
                        }
                    }
                    syncedfeeds.Add(f2.link, f2);
                }
                else
                {
                    if (replace)
                    {
                        if ((f1.category != null) && !cats.ContainsKey(f1.category))
                        {
                            cats.Add(f1.category);
                        }
                        if ((f1.listviewlayout != null) && !colLayouts.ContainsKey(f1.listviewlayout))
                        {
                            listviewLayout layout = FindLayout(f1.listviewlayout, myFeeds.listviewLayouts);
                            if (layout != null)
                                colLayouts.Add(f1.listviewlayout, layout.FeedColumnLayout);
                            else
                                f1.listviewlayout = null;
                        }
                        if (!syncedfeeds.ContainsKey(f1.link))
                        {
                            syncedfeeds.Add(f1.link, f1);
                        }
                    }
                    else
                    {
                        if (category.Length > 0)
                        {
                            f1.category = (f1.category == null ? category : category + CategorySeparator + f1.category);
                        }
                        if (!_feedsTable.ContainsKey(f1.link))
                        {
                            f1.lastretrievedSpecified = true;
                            f1.lastretrieved = dta[count%dtaCount];
                            _feedsTable.Add(f1.link, f1);
                        }
                    }
                }
                myFeeds.feed.RemoveAt(0);
                count++;
            }
            IDictionary<string, INntpServerDefinition> serverList = new Dictionary<string, INntpServerDefinition>();
            IDictionary<string, UserIdentity> identityList = new Dictionary<string, UserIdentity>();
            foreach (UserIdentity identity in myFeeds.identities)
            {
                if (replace)
                {
                    identityList.Add(identity.Name, identity);
                }
                else if (!this.identities.ContainsKey(identity.Name))
                {
                    this.identities.Add(identity.Name, identity);
                }
            }
            foreach (NntpServerDefinition server in myFeeds.nntpservers)
            {
                if (replace)
                {
                    serverList.Add(server.Name, server);
                }
                else if (!this.identities.ContainsKey(server.Name))
                {
                    this.nntpServers.Add(server.Name, server);
                }
            }
            foreach (listviewLayout layout in myFeeds.listviewLayouts)
            {
                if (replace)
                {
                    if (layout.FeedColumnLayout.LayoutType == LayoutType.GlobalFeedLayout ||
                        layout.FeedColumnLayout.LayoutType == LayoutType.GlobalCategoryLayout ||
                        layout.FeedColumnLayout.LayoutType == LayoutType.SearchFolderLayout ||
                        layout.FeedColumnLayout.LayoutType == LayoutType.SpecialFeedsLayout)
                        colLayouts.Add(layout.ID, layout.FeedColumnLayout);
                }
                else if (!this.layouts.ContainsKey(layout.ID))
                {
                    if (layout.FeedColumnLayout.LayoutType != LayoutType.GlobalFeedLayout ||
                        layout.FeedColumnLayout.LayoutType != LayoutType.GlobalCategoryLayout ||
                        layout.FeedColumnLayout.LayoutType != LayoutType.SearchFolderLayout ||
                        layout.FeedColumnLayout.LayoutType != LayoutType.SpecialFeedsLayout)
                        this.layouts.Add(layout.ID, layout.FeedColumnLayout);
                }
            }
            if (replace)
            {
                this._feedsTable = syncedfeeds;
                this.categories = cats;
                this.identities = identityList;
                this.nntpServers = serverList;
                this.layouts = colLayouts;
            }
            else
            {
                if (myFeeds.categories.Count == 0)
                {
                    if (category.Length > 0 && this.categories.ContainsKey(category) == false)
                    {
                        this.categories.Add(category);
                    }
                }
                else
                {
                    foreach (category cat in myFeeds.categories)
                    {
                        string cat2 = (category.Length == 0 ? cat.Value : category + CategorySeparator + cat.Value);
                        if (this.categories.ContainsKey(cat2) == false)
                        {
                            this.categories.Add(cat2);
                        }
                    }
                }
            }
            if (validationErrorOccured)
            {
                validationErrorOccured = false;
            }
        }
        public void ImportFeedlist(Stream feedlist)
        {
            this.ImportFeedlist(feedlist, String.Empty, false);
        }
        public void ImportFeedlist(Stream feedlist, string category)
        {
            try
            {
                this.ImportFeedlist(feedlist, category, false);
            }
            catch (Exception e)
            {
                throw new ApplicationException(e.Message, e);
            }
        }
        public static void ValidationCallbackOne(object sender,
                                                 ValidationEventArgs args)
        {
            if (args.Severity == XmlSeverityType.Error)
            {
                Trace("ValidationCallbackOne() message: {0}", args.Message);
                XmlSchemaException xse = args.Exception;
                if (xse != null)
                {
                    Type xseType = xse.GetType();
                    FieldInfo resFieldInfo = xseType.GetField("res", BindingFlags.NonPublic | BindingFlags.Instance);
                    string errorType = (string) resFieldInfo.GetValue(xse);
                    if (!errorType.Equals("Sch_UnresolvedKeyref") && !errorType.Equals("Sch_DuplicateKey"))
                    {
                        validationErrorOccured = true;
                    }
                    else
                    {
                        categoryMismatch = true;
                    }
                }
            }
        }
        private string SaveFeed(NewsFeed feed)
        {
            TimeSpan maxItemAge = this.GetMaxItemAge(feed.link);
            FeedDetailsInternal fi = this.itemsTable[feed.link];
            IList<NewsItem> items = fi.ItemsList;
            if (maxItemAge != TimeSpan.MinValue)
            {
                lock (items)
                {
                    for (int i = 0, count = items.Count; i < count; i++)
                    {
                        NewsItem item = items[i];
                        if (feed.deletedstories.Contains(item.Id) || ((DateTime.Now - item.Date) >= maxItemAge))
                        {
                            items.Remove(item);
                            RelationCosmosRemove(item);
                            SearchHandler.IndexRemove(item);
                            count--;
                            i--;
                        }
                    }
                }
            }
            return this.CacheHandler.SaveFeed(fi);
        }
        private FeedDetailsInternal GetFeed(NewsFeed feed)
        {
            FeedDetailsInternal fi = this.CacheHandler.GetFeed(feed);
            if (fi != null)
            {
                TimeSpan maxItemAge = this.GetMaxItemAge(feed.link);
                int readItems = 0;
                IList<NewsItem> items = fi.ItemsList;
                lock (items)
                {
                    bool keepAll = (maxItemAge == TimeSpan.MinValue) && (feed.deletedstories.Count == 0);
                    maxItemAge = (maxItemAge == TimeSpan.MinValue ? TimeSpan.MaxValue : maxItemAge);
                    for (int i = 0, count = items.Count; i < count; i++)
                    {
                        NewsItem item = items[i];
                        if ((!keepAll) && ((DateTime.Now - item.Date) >= maxItemAge) ||
                            feed.deletedstories.Contains(item.Id))
                        {
                            items.RemoveAt(i);
                            RelationCosmosRemove(item);
                            i--;
                            count--;
                        }
                        else if (item.BeenRead)
                        {
                            readItems++;
                        }
                    }
                }
                if (readItems == items.Count)
                {
                    feed.containsNewMessages = false;
                }
                else
                {
                    feed.containsNewMessages = true;
                }
            }
            return fi;
        }
        public static List<NewsItem> MergeAndPurgeItems(List<NewsItem> oldItems, List<NewsItem> newItems,
                                                        List<string> deletedItems, out List<NewsItem> receivedNewItems,
                                                        bool onlyKeepNewItems)
        {
            receivedNewItems = new List<NewsItem>();
            lock (oldItems)
            {
                foreach (NewsItem newitem in newItems)
                {
                    int index = oldItems.IndexOf(newitem);
                    if (index == -1)
                    {
                        if (!deletedItems.Contains(newitem.Id))
                        {
                            receivedNewItems.Add(newitem);
                            oldItems.Add(newitem);
                            ReceivingNewsChannelServices.ProcessItem(newitem);
                        }
                    }
                    else
                    {
                        NewsItem olditem = oldItems[index];
                        newitem.BeenRead = olditem.BeenRead;
                        newitem.Date = olditem.Date;
                        newitem.FlagStatus = olditem.FlagStatus;
                        if (olditem.WatchComments)
                        {
                            newitem.WatchComments = true;
                            if ((olditem.HasNewComments) || (olditem.CommentCount < newitem.CommentCount))
                            {
                                newitem.HasNewComments = true;
                            }
                        }
                        if (newitem.CommentCount == NewsItem.NoComments)
                        {
                            newitem.CommentCount = olditem.CommentCount;
                        }
                        if (olditem.Enclosures.Count > 0)
                        {
                            foreach (Enclosure enc in olditem.Enclosures)
                            {
                                int j = newitem.Enclosures.IndexOf(enc);
                                if (j != -1)
                                {
                                    Enclosure oldEnc = newitem.Enclosures[j];
                                    enc.Downloaded = oldEnc.Downloaded;
                                }
                                else
                                {
                                    if (ReferenceEquals(newitem.Enclosures, GetList<Enclosure>.Empty))
                                    {
                                        newitem.Enclosures = new List<Enclosure>();
                                    }
                                    newitem.Enclosures.Add(enc);
                                }
                            }
                        }
                        oldItems.RemoveAt(index);
                        oldItems.Add(newitem);
                        RelationCosmosRemove(olditem);
                    }
                }
                RelationCosmosAddRange(receivedNewItems);
            }
            if (onlyKeepNewItems)
            {
                return newItems;
            }
            else
            {
                return oldItems;
            }
        }
        public void PostComment(string url, NewsItem item2post, NewsItem inReply2item)
        {
            if (inReply2item.CommentStyle == SupportedCommentStyle.CommentAPI)
            {
                this.RssParser.PostCommentViaCommentAPI(url, item2post, inReply2item,
                                                        GetFeedCredentials(inReply2item.Feed));
            }
            else if (inReply2item.CommentStyle == SupportedCommentStyle.NNTP)
            {
                NntpParser.PostCommentViaNntp(item2post, inReply2item, GetNntpServerCredentials(inReply2item.Feed));
            }
        }
        public void PostComment(NewsItem item2post, NewsFeed postTarget)
        {
            if (item2post.CommentStyle == SupportedCommentStyle.NNTP)
            {
                NntpParser.PostCommentViaNntp(item2post, postTarget, GetNntpServerCredentials(postTarget));
            }
        }
        public ICollection<NewsItem> GetItemsWithIncomingLinks(NewsItem item, IList<NewsItem> excludeItemsList)
        {
            if (buildRelationCosmos)
                return relationCosmos.GetIncoming(item, excludeItemsList);
            else
                return new List<NewsItem>();
        }
        public IList<NewsItem> GetItemsWithIncomingLinks(string url, DateTime since)
        {
            url = RelationCosmos.RelationCosmos.UrlTable.Add(url);
            if (buildRelationCosmos)
                return relationCosmos.GetIncoming<NewsItem>(url, since);
            else
                return new List<NewsItem>();
        }
        public ICollection<NewsItem> GetItemsFromOutGoingLinks(NewsItem item, IList<NewsItem> excludeItemsList)
        {
            if (buildRelationCosmos)
                return relationCosmos.GetOutgoing(item, excludeItemsList);
            else
                return new List<NewsItem>();
        }
        public bool HasItemAnyRelations(NewsItem item, IList<NewsItem> excludeItemsList)
        {
            if (buildRelationCosmos)
                return relationCosmos.HasIncomingOrOutgoing(item, excludeItemsList);
            else
                return false;
        }
        internal static void RelationCosmosAdd<T>(T relation)
            where T : RelationBase<T>
        {
            if (buildRelationCosmos)
                relationCosmos.Add(relation);
            else
                return;
        }
        internal static void RelationCosmosAddRange<T>(IEnumerable<T> relations)
            where T : RelationBase<T>
        {
            if (buildRelationCosmos)
                relationCosmos.AddRange(relations);
            else
                return;
        }
        internal static void RelationCosmosRemove<T>(T relation)
            where T : RelationBase<T>
        {
            if (buildRelationCosmos)
                relationCosmos.Remove(relation);
            else
                return;
        }
        internal static void RelationCosmosRemoveRange<T>(IList<T> relations)
            where T : RelationBase<T>
        {
            if (buildRelationCosmos)
                relationCosmos.RemoveRange(relations);
            else
                return;
        }
        public void RegisterReceivingNewsChannel(INewsChannel channel)
        {
            receivingNewsChannel.RegisterNewsChannel(channel);
        }
        public void UnregisterReceivingNewsChannel(INewsChannel channel)
        {
            receivingNewsChannel.UnregisterNewsChannel(channel);
        }
        internal static NewsChannelServices ReceivingNewsChannelServices
        {
            get
            {
                return receivingNewsChannel;
            }
        }
        public XmlSchema FeedsSchema
        {
            get
            {
                return feedsSchema;
            }
        }
    }
    [Flags]
    public enum NewsFeedProperty
    {
        None = 0,
        FeedLink = 0x1,
        FeedUrl = 0x2,
        FeedTitle = 0x4,
        FeedCategory = 0x8,
        FeedDescription = 0x10,
        FeedType = 0x20,
        FeedItemsDeleteUndelete = 0x40,
        FeedItemFlag = 0x80,
        FeedItemReadState = 0x100,
        FeedItemCommentCount = 0x200,
        FeedMaxItemAge = 0x400,
        FeedItemWatchComments = 0x800,
        FeedRefreshRate = 0x1000,
        FeedCacheUrl = 0x2000,
        FeedAdded = 0x4000,
        FeedRemoved = 0x8000,
        FeedCategoryRemoved = 0x10000,
        FeedCategoryAdded = 0x20000,
        FeedCredentials = 0x40000,
        FeedAlertOnNewItemsReceived = 0x80000,
        FeedMarkItemsReadOnExit = 0x100000,
        FeedStylesheet = 0x200000,
        FeedItemNewCommentsRead = 0x400000,
        General = 0x8000000,
    }
    internal interface FeedDetailsInternal : IFeedDetails
    {
        new Dictionary<XmlQualifiedName, string> OptionalElements { get; }
        List<NewsItem> ItemsList { get; set; }
        string FeedLocation { get; set; }
        string Id { get; set; }
        void WriteTo(XmlWriter writer);
        void WriteTo(XmlWriter writer, bool noDescriptions);
        void WriteItemContents(BinaryReader reader, BinaryWriter writer);
    }
    public interface ISizeInfo
    {
        int GetSize();
        string GetSizeDetails();
    }
    internal class RssBanditXmlNamespaceResolver : XmlNamespaceManager
    {
        public RssBanditXmlNamespaceResolver() : base(new NameTable())
        {
        }
        public override void AddNamespace(string prefix, string uri)
        {
            if (uri == NamespaceCore.Feeds_v2003)
            {
                uri = NamespaceCore.Feeds_vCurrent;
            }
            base.AddNamespace(prefix, uri);
        }
    }
    internal class RssBanditXmlReader : XmlTextReader
    {
        public RssBanditXmlReader(Stream s, XmlNodeType nodeType, XmlParserContext context) : base(s, nodeType, context)
        {
        }
        public RssBanditXmlReader(string s, XmlNodeType nodeType, XmlParserContext context) : base(s, nodeType, context)
        {
        }
        public override string Value
        {
            get
            {
                if ((this.NodeType == XmlNodeType.Attribute) &&
                    (base.Value == NamespaceCore.Feeds_v2003))
                {
                    return NamespaceCore.Feeds_vCurrent;
                }
                else
                {
                    return base.Value;
                }
            }
        }
    }
}
