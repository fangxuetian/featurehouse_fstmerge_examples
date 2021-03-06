using System.Reflection; 
using System.IO; 
using System.Drawing; 
using System.Windows.Forms; namespace  RssBandit {
	
 internal sealed class  Resource {
		
  public sealed class  ItemFlagBackground {
			
   public static  Color Red { get { return Color.FromArgb(207, 93, 96); } }
 
   public static  Color Purple { get { return Color.FromArgb(165, 88, 124); } }
 
   public static  Color Blue { get { return Color.FromArgb(92, 131, 180); } }
 
   public static  Color Yellow { get { return Color.FromArgb(255, 193, 96); } }
 
   public static  Color Green { get { return Color.FromArgb(139, 180, 103); } }
 
   public static  Color Orange { get { return Color.FromArgb(255, 140, 90); } }

		}
		
  public sealed class  NewsItemImage {
			
   public  const int DefaultRead = 0; 
   public  const int DefaultUnread = 1; 
   public  const int OutgoingRead = 2; 
   public  const int OutgoingUnread = 3; 
   public  const int IncomingRead = 4; 
   public  const int IncomingUnread = 5; 
   public  const int CommentRead = 6; 
   public  const int CommentUnread = 7; 
   public  const int ReplySentRead = 8; 
   public  const int ReplySentUnread = 9;
		}
		
  public sealed class  FlagImage {
			
   public  const int Red = 14; 
   public  const int Purple = 15; 
   public  const int Blue = 16; 
   public  const int Yellow = 17; 
   public  const int Green = 18; 
   public  const int Orange = 19; 
   public  const int Clear = 20; 
   public  const int Complete = 21;
		}
		
  public sealed class  NewsItemRelatedImage {
			
   public  const int Attachment = 22; 
   public  const int Failure = 23;
		}
		
  public sealed class  ToolItemImage {
			
   public  const int RefreshAll = 0; 
   public  const int NewSubscription = 1; 
   public  const int Delete = 2; 
   public  const int NextUnreadItem = 3; 
   public  const int NextUnreadSubscription = 4; 
   public  const int PostReply = 5; 
   public  const int OptionsDialog = 6; 
   public  const int MarkAsRead = 7; 
   public  const int ToggleSubscriptions = 13; 
   public  const int ToggleRssSearch = 14; 
   public  const int Search = 15; 
   public  const int NewPost = 16; 
   public  const int ItemDetailViewAtRight = 17; 
   public  const int ItemDetailViewAtBottom = 18; 
   public  const int ItemDetailViewAtLeft = 19; 
   public  const int ItemDetailViewAtTop = 20; 
   public  const int ItemDetailViewWithoutList = 21; 
   public  const int NewNntpSubscription = 22; 
   public  const int NewDiscoveredSubscription = 23; 
   public  const int BrowserItemImageOffset = 30;
		}
		
  public sealed class  BrowserItemImage {
			
   public  const int OpenNewTab = 0; 
   public  const int OpenFolder = 1; 
   public  const int Save = 2; 
   public  const int Cut = 3; 
   public  const int Copy = 4; 
   public  const int Paste = 5; 
   public  const int Delete = 6; 
   public  const int Checked = 7; 
   public  const int Undo = 8; 
   public  const int Redo = 9; 
   public  const int Zoom = 10; 
   public  const int Print = 11; 
   public  const int Search = 12; 
   public  const int SearchContinue = 13; 
   public  const int PointedHelp = 14; 
   public  const int ZoomIn = 15; 
   public  const int ZoomOut = 16; 
   public  const int GoBack = 17; 
   public  const int GoForward = 18; 
   public  const int CancelNavigation = 21; 
   public  const int Refresh = 22; 
   public  const int Home = 23; 
   public  const int Mail = 34; 
   public  const int SearchWeb = 37; 
   public  const int DoNavigate = 38; 
   public  const int OpenInExternalBrowser = 39;
		}
		
  public sealed class  SubscriptionTreeImage {
			
   public  const int AllSubscriptions = 0; 
   public  const int AllSubscriptionsExpanded = 1; 
   public  const int AllFinderFolders = 21; 
   public  const int AllFinderFoldersExpanded = 22; 
   public  const int AllSmartFolders = 19; 
   public  const int AllSmartFoldersExpanded = 20; 
   public  const int SubscriptionsCategory = 2; 
   public  const int SubscriptionsCategoryExpanded = 3; 
   public  const int FinderCategory = 2; 
   public  const int FinderCategoryExpanded = 3; 
   public  const int Feed = 4; 
   public  const int FeedSelected = 4; 
   public  const int FeedDisabled = 5; 
   public  const int FeedDisabledSelected = 5; 
   public  const int FeedFailure = 6; 
   public  const int FeedFailureSelected = 6; 
   public  const int FeedSecured = 7; 
   public  const int FeedSecuredSelected = 7; 
   public  const int FeedUpdating = 8; 
   public  const int FeedUpdatingSelected = 8; 
   public  const int Nntp = 23; 
   public  const int NntpSelected = 23; 
   public  const int NntpDisabled = 24; 
   public  const int NntpDisabledSelected = 24; 
   public  const int NntpFailure = 25; 
   public  const int NntpFailureSelected = 25; 
   public  const int NntpSecured = 26; 
   public  const int NntpSecuredSelected = 26; 
   public  const int NntpUpdating = 27; 
   public  const int NntpUpdatingSelected = 27; 
   public  const int Exceptions = 9; 
   public  const int ExceptionsSelected = 9; 
   public  const int SentItems = 11; 
   public  const int SentItemsSelected = 11; 
   public  const int WatchedItems = 28; 
   public  const int WatchedItemsSelected = 29; 
   public  const int RedFlag = 12; 
   public  const int RedFlagSelected = 12; 
   public  const int BlueFlag = 13; 
   public  const int BlueFlagSelected = 13; 
   public  const int GreenFlag = 14; 
   public  const int GreenFlagSelected = 14; 
   public  const int YellowFlag = 15; 
   public  const int YellowFlagSelected = 15; 
   public  const int ReplyFlag = 16; 
   public  const int ReplyFlagSelected = 16; 
   public  const int WasteBasketEmpty = 17; 
   public  const int WasteBasketEmptySelected = 17; 
   public  const int WasteBasketFull = 18; 
   public  const int WasteBasketFullSelected = 18; 
   public  const int SearchFolder = 10; 
   public  const int SearchFolderSelected = 10;
		}
		
  internal sealed class  Toolbar {
			
   public static  string WebTools = "tbWebBrowser";
 
   public static  string MenuBar = "tbMainMenu";
 
   public static  string MainTools = "tbMainAppBar";
 
   public static  string SearchTools = "tbSearchBar";

		}
		
  internal sealed class  NavigatorGroup {
			
   public static  string Subscriptions = "groupFeedsTree";
 
   public static  string RssSearch = "groupFeedsSearch";

		}
		
  public class  ApplicationSound {
			
   private  const string appSndPrefix = "RSSBANDIT_"; 
   public  const string FeedDiscovered = appSndPrefix + "FeedDiscovered"; 
   public  const string NewItemsReceived = appSndPrefix + "ItemsReceived"; 
   public  const string NewAttachmentDownloaded = appSndPrefix + "AttachmentDownloaded";
		}
		
  private  Resource() { }
 
  public static  Stream GetStream( string name ){
   return Assembly.GetExecutingAssembly().GetManifestResourceStream(typeof(Resource).Namespace + "." + name);
  }
 
  public static  Cursor LoadCursor(string cursorName) {
   return new Cursor(GetStream(cursorName));
  }
 
  public static  Icon LoadIcon(string iconName) {
   return new Icon(GetStream(iconName));
  }
 
  public static  Icon LoadIcon(string iconName, Size iconSize) {
   return new Icon(LoadIcon(iconName), iconSize);
  }
 
  public static  Bitmap LoadBitmap(string imageName) {
   return LoadBitmap(imageName, false, Point.Empty);
  }
 
  public static  Bitmap LoadBitmap(string imageName, Point transparentPixel) {
   return LoadBitmap(imageName, true, transparentPixel);
  }
 
  private static  Bitmap LoadBitmap(string imageName, bool makeTransparent, Point transparentPixel) {
   Bitmap bmp = new Bitmap(GetStream(imageName));
   if (makeTransparent) {
    Color c = bmp.GetPixel(transparentPixel.X, transparentPixel.Y);
    bmp.MakeTransparent(c);
   }
   return bmp;
  }
 
  public static  ImageList LoadBitmapStrip(string imageName, Size imageSize) {
   return LoadBitmapStrip(imageName, imageSize, false, Point.Empty);
  }
 
  public static  ImageList LoadBitmapStrip(string imageName, Size imageSize, Point transparentPixel) {
   return LoadBitmapStrip(imageName, imageSize, true, transparentPixel);
  }
 
  private static  ImageList LoadBitmapStrip(string imageName, Size imageSize, bool makeTransparent, Point transparentPixel) {
   Bitmap bmp = LoadBitmap(imageName, makeTransparent, transparentPixel);
            ImageList img = new ImageList();
            img.ColorDepth = ColorDepth.Depth32Bit;
   img.ImageSize = imageSize;
   img.Images.AddStrip(bmp);
   return img;
  }

	}

}
