using System;
using System.Collections;
using System.Collections.Generic;
using System.Net;
using System.Windows.Forms;
using NewsComponents;
namespace RssBandit.AppServices
{
 public interface ICoreApplication
 {
  event EventHandler PreferencesChanged;
  event EventHandler FeedlistLoaded;
  event FeedDeletedHandler FeedDeleted;
  int CurrentGlobalRefreshRate { get; }
  IWebProxy Proxy { get; }
  IDictionary Identities { get; }
        IDictionary<string, INntpServerDefinition> NntpServerDefinitions { get; }
  IList GetNntpNewsGroups(string nntpServerName, bool forceReloadFromServer);
  IList GetItemFormatterStylesheets();
  IList WebSearchEngines { get ; }
  DateTime LastAutoUpdateCheck { get; }
  void ShowOptions(OptionDialogSection selectedSection, IWin32Window owner, EventHandler optionsChangedHandler);
  void ShowUserIdentityManagementDialog(IWin32Window owner, EventHandler definitionChangedHandler);
  void ShowNntpServerManagementDialog(IWin32Window owner, EventHandler definitionChangedHandler);
  void ShowPodcastOptionsDialog(IWin32Window owner, EventHandler optionsChangedHandler);
  string EnclosureFolder { get; }
  string PodcastFolder { get; }
  string PodcastFileExtensions { get; }
  bool DownloadCreateFolderPerFeed {get;}
  bool EnableEnclosureAlerts {get;}
  bool DownloadEnclosures {get;}
  int EnclosureCacheSize { get;}
  int NumEnclosuresToDownloadOnNewFeed { get; }
  void NavigateToUrl(string url, string tabCaption, bool forceNewTabOrWindow, bool setFocus);
  void NavigateToUrlAsUserPreferred(string url, string tabCaption, bool forceNewTabOrWindow, bool setFocus);
  void NavigateToUrlInExternalBrowser(string url);
  string DefaultCategory { get; }
  string[] GetCategories();
  void AddCategory(string category);
  bool SubscribeToFeed(string url);
  bool SubscribeToFeed(string url, string category);
  bool SubscribeToFeed(string url, string category, string title);
        bool ContainsFeed(string url);
        bool TryGetFeedDetails(string url, out string category, out string title, out string link);
  void RegisterReceivingNewsChannelProcessor(IChannelProcessor channelProcessor);
  void UnregisterReceivingNewsChannelProcessor(IChannelProcessor channelProcessor);
  void RegisterDisplayingNewsChannelProcessor (IChannelProcessor channelProcessor);
  void UnregisterDisplayingNewsChannelProcessor (IChannelProcessor channelProcessor);
 }
}
