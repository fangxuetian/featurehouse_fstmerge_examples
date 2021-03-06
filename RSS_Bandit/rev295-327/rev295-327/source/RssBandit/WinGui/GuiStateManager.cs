using System; 
using RssBandit.WinGui.Utility; namespace  RssBandit.WinGui {
	
 public enum  NewsHandlerState  {
  Idle,
  RefreshOne,
  RefreshOneDone,
  RefreshCategory,
  RefreshAllAuto,
  RefreshAllForced,
  RefreshAllDone
 } 
 public class  GuiStateManager {
		
  public delegate  void  NewsHandlerBeforeStateMoveHandler (NewsHandlerState oldState, NewsHandlerState newState, ref bool cancel);
		
  public  event NewsHandlerBeforeStateMoveHandler NewsHandlerBeforeStateMove; 
  public delegate  void  NewsHandlerStateMovedHandler (NewsHandlerState oldState, NewsHandlerState newState);
		
  public  event NewsHandlerStateMovedHandler NewsHandlerStateMoved; 
  public  void MoveNewsHandlerStateTo(NewsHandlerState newState) {
   NewsHandlerState oldState = this.handlerState;
   bool shouldCancel = false;
   if (newState == oldState)
    return;
   if (NewsHandlerBeforeStateMove != null) {
    try {
     NewsHandlerBeforeStateMove(oldState, newState, ref shouldCancel);
    } catch {}
   }
   if (shouldCancel)
    return;
   this.handlerState = newState;
   if (NewsHandlerStateMoved != null) {
    try {
     NewsHandlerStateMoved(oldState, newState);
    } catch {}
   }
  }
 
  private  NewsHandlerState handlerState = NewsHandlerState.Idle;
 
  public  NewsHandlerState NewsHandlerState {
   get { return handlerState; }
  }
 
  public delegate  void  InternetConnectionStateMovedHandler (INetState oldState, INetState newState);
		
  public  event InternetConnectionStateMovedHandler InternetConnectionStateMoved; 
  public  void MoveInternetConnectionStateTo(INetState newState) {
   INetState oldState = internetConnectionState;
   internetConnectionState = newState;
   if (InternetConnectionStateMoved != null) {
    try {
     InternetConnectionStateMoved(oldState, newState);
    } catch {}
   }
  }
 
  public  bool InternetAccessAllowed {
   get {
    if ((internetConnectionState & INetState.Connected) > 0 && (internetConnectionState & INetState.Online) > 0)
     return true;
    return false;
   }
  }
 
  public  bool InternetConnectionOffline {
   get {
    if ((internetConnectionState & INetState.Offline) > 0)
     return true;
    return false;
   }
  }
 
  public  INetState InternetConnectionState {
   get { return internetConnectionState; }
  }
 
  private  INetState internetConnectionState = INetState.Invalid;
 
  public  GuiStateManager() {}

	}

}
