using System.Drawing; 
using System.Collections; 
using System.Collections.Generic; 
using RssBandit.WinGui.Utility; 
using NewsComponents; namespace  RssBandit.WinGui.Interfaces {
	
 public interface  ICommand {
		
  void Initialize(); 
  void Execute(); 
  string CommandID { get; } 
  CommandMediator Mediator { get ; }
	}
	
 public interface  ICommandComponent {
		
  bool Checked { get ; set; } 
  bool Enabled { get ; set; } 
  bool Visible { get ; set; }
	}
	
 public delegate  void  ExecuteCommandHandler (ICommand sender);
	
 public interface  ITabState {
		
  string Title { get; set; } 
  string Url { get; set; } 
  bool CanClose { get; set; } 
  bool CanGoBack { get; set; } 
  bool CanGoForward { get; set; } 
  ITextImageItem[] GoBackHistoryItems(int maxItems); 
  ITextImageItem[] GoForwardHistoryItems(int maxItems);
	}
	
 public interface  ITextImageItem {
		
  Image Image { get; } 
  string Text { get; }
	}
	
 public interface  INewsItemFilter {
		
  bool Match(NewsItem item); 
  void ApplyAction(NewsItem item, System.Windows.Forms.ThListView.ThreadedListViewItem lvItem);
	}
	
 public interface  ISmartFolder {
		
  bool ContainsNewMessages { get ; } 
  bool HasNewComments { get ; } 
  int NewMessagesCount { get ; } 
  int NewCommentsCount { get ; } 
  void MarkItemRead (NewsItem item); 
  void MarkItemUnread (NewsItem item); 
  List<NewsItem> Items { get ; } 
  void Add (NewsItem item); 
  void Remove (NewsItem item); 
  void UpdateReadStatus (); 
  void UpdateCommentStatus (); 
  bool Modified { get ; set ; }
	}
	
 public enum  FeedNodeType 
 {
  Root,
  Category,
  Feed,
  SmartFolder,
  Finder,
  FinderCategory,
 }
}
