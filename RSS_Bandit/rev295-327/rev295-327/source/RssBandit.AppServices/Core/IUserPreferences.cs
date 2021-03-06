using System; 
using System.Drawing; namespace  RssBandit.AppServices {
	
 public interface  IUserPreferences :  IPropertyChange {
		
  string UserIdentityForComments { get; } 
  bool FeedRefreshOnStartup { get; } 
  bool UseProxy { get; } 
  bool UseIEProxySettings { get; } 
  bool BypassProxyOnLocal { get; } 
  string[] ProxyBypassList { get; } 
  bool ProxyCustomCredentials { get; } 
  string ProxyAddress { get; } 
  int ProxyPort { get; } 
  string ProxyUser { get; } 
  string ProxyPassword { get; } 
  string NewsItemStylesheetFile { get; } 
  bool ReuseFirstBrowserTab { get; } 
  bool MarkItemsReadOnExit { get; } 
  bool NewsItemOpenLinkInDetailWindow { get; } 
  HideToTray HideToTrayAction { get; } 
  AutoUpdateMode AutoUpdateFrequency { get; } 
  [Obsolete("Please use the propery DateTime ICoreApplication:LastAutoUpdateCheck instead!", true)]
  DateTime LastAutoUpdateCheck { get; } 
  TimeSpan MaxItemAge { get; } 
  Font NormalFont { get; } 
  Color NormalFontColor { get; } 
  Font UnreadFont { get; } 
  Color UnreadFontColor { get; } 
  Font FlagFont { get; } 
  Color FlagFontColor { get; } 
  Font RefererFont { get; } 
  Color RefererFontColor { get; } 
  Font ErrorFont { get; } 
  Color ErrorFontColor { get; } 
  Font NewCommentsFont { get; } 
  Color NewCommentsFontColor { get; } 
  bool UseRemoteStorage { get; } 
  string RemoteStorageUserName { get; } 
  string RemoteStoragePassword { get; } 
  RemoteStorageProtocolType RemoteStorageProtocol { get; } 
  string RemoteStorageLocation { get; } 
  BrowserBehaviorOnNewWindow BrowserOnNewWindow { get; } 
  string BrowserCustomExecOnNewWindow { get; } 
  bool BrowserJavascriptAllowed { get; } 
  bool BrowserJavaAllowed { get; } 
  bool BrowserActiveXAllowed { get; } 
  bool BrowserBGSoundAllowed { get; } 
  bool BrowserVideoAllowed { get; } 
  bool BrowserImagesAllowed { get; } 
  DisplayFeedAlertWindow ShowAlertWindow { get; } 
  bool ShowNewItemsReceivedBalloon { get; } 
  bool BuildRelationCosmos { get; }
	}

}
