using System; 
using System.Collections; 
using RssBandit.WinGui.Interfaces; 
using RssBandit.AppServices; 
using RssBandit.WinGui.Menus; 
using RssBandit.WinGui.Tools; namespace  RssBandit.WinGui.Utility {
	
 public class  CommandMediator : ICommandMediator {
		
  public  event EventHandler BeforeCommandStateChanged; 
  public  event EventHandler AfterCommandStateChanged; 
  private  Hashtable registeredCommands;
 
  public  CommandMediator()
  {
   this.registeredCommands = new Hashtable(15);
  }
 
  public  void RegisterCommand(string cmdId, ICommandComponent cmd) {
   ArrayList al;
   if (registeredCommands.ContainsKey(cmdId)) {
    al = (ArrayList)registeredCommands[cmdId];
   }
   else {
    al = new ArrayList(1);
    registeredCommands.Add (cmdId,al);
   }
   al.Add(cmd);
  }
 
  public  void ReRegisterCommand(ICommand cmd)
  {
   if (cmd != null && registeredCommands.ContainsKey(cmd.CommandID))
   {
    ArrayList al = (ArrayList)registeredCommands[cmd.CommandID];
    Type toRegisterType = cmd.GetType();
    for (int i=0; al != null && i < al.Count; i++)
    {
     object registeredCommand = al[i];
     if (registeredCommand.GetType().Equals(toRegisterType))
     {
      if (registeredCommand is AppPopupMenuCommand) {
       AppPopupMenuCommand appCmd = (AppPopupMenuCommand)cmd;
       appCmd.ReJoinMediatorFrom((AppPopupMenuCommand)registeredCommand);
       ((AppPopupMenuCommand)al[i]).Dispose();
       al[i] = appCmd;
      } else
      if (registeredCommand is AppButtonToolCommand) {
       AppButtonToolCommand appCmd = (AppButtonToolCommand)cmd;
       appCmd.ReJoinMediatorFrom((AppButtonToolCommand)registeredCommand);
       ((AppButtonToolCommand)al[i]).Dispose();
       al[i] = appCmd;
      } else
      if (registeredCommand is AppStateButtonToolCommand) {
       AppStateButtonToolCommand appCmd = (AppStateButtonToolCommand)cmd;
       appCmd.ReJoinMediatorFrom((AppStateButtonToolCommand)registeredCommand);
       ((AppStateButtonToolCommand)al[i]).Dispose();
       al[i] = appCmd;
      }
     }
    }
   }
  }
 
  public  void UnregisterCommand(string cmdId, ICommandComponent cmd) {
   if (registeredCommands.ContainsKey(cmdId) && cmd != null) {
    ArrayList al = (ArrayList)registeredCommands[cmdId];
    al.Remove(cmd);
   }
  }
 
  private  bool IsCommandComponentEnabled(string cmdId) {
   if (registeredCommands.ContainsKey(cmdId))
    return ((ICommandComponent)((ArrayList)registeredCommands[cmdId])[0]).Enabled;
   return false;
  }
 
  private  void SetCommandComponentEnabled(string cmdId, bool newValue) {
   if (registeredCommands.ContainsKey(cmdId)) {
    ArrayList al = (ArrayList)registeredCommands[cmdId];
    foreach (ICommandComponent cmd in al)
     cmd.Enabled = newValue;
   }
  }
 
  private  bool IsCommandComponentChecked(string cmdId) {
   if (registeredCommands.ContainsKey(cmdId))
    return ((ICommandComponent)((ArrayList)registeredCommands[cmdId])[0]).Checked;
   return false;
  }
 
  private  void SetCommandComponentChecked(string cmdId, bool newValue) {
   if (registeredCommands.ContainsKey(cmdId)) {
    ArrayList al = (ArrayList)registeredCommands[cmdId];
    foreach (ICommandComponent cmd in al)
     cmd.Checked = newValue;
   }
  }
 
  private  bool IsCommandComponentVisible(string cmdId) {
   if (registeredCommands.ContainsKey(cmdId))
    return ((ICommandComponent)((ArrayList)registeredCommands[cmdId])[0]).Visible;
   return false;
  }
 
  private  void SetCommandComponentVisible(string cmdId, bool newValue) {
   if (registeredCommands.ContainsKey(cmdId)) {
    ArrayList al = (ArrayList)registeredCommands[cmdId];
    foreach (ICommandComponent cmd in al)
     cmd.Visible = newValue;
   }
  }
 
  public  void SetEnabled(params string[] args) {
   bool b; string cmdId;
   RaiseBeforeCommandStateChanged();
   foreach (string cmdParam in args) {
    b = String.Compare(cmdParam.Substring(0,1),"+") == 0;
    cmdId = cmdParam.Substring(1);
    SetCommandComponentEnabled (cmdId, b);
   }
   RaiseAfterCommandStateChanged();
  }
 
  public  void SetEnabled(bool newState, params string[] args) {
   RaiseBeforeCommandStateChanged();
   foreach (string cmdParam in args) {
    SetCommandComponentEnabled (cmdParam, newState);
   }
   RaiseAfterCommandStateChanged();
  }
 
  public  void SetChecked(params string[] args) {
   bool b; string cmdId;
   RaiseBeforeCommandStateChanged();
   foreach (string cmdParam in args) {
    b = String.Compare(cmdParam.Substring(0,1),"+") == 0;
    cmdId = cmdParam.Substring(1);
    SetCommandComponentChecked (cmdId, b);
   }
   RaiseAfterCommandStateChanged();
  }
 
  public  void SetChecked(bool newState, params string[] args) {
   RaiseBeforeCommandStateChanged();
   foreach (string cmdParam in args) {
    SetCommandComponentChecked (cmdParam, newState);
   }
   RaiseAfterCommandStateChanged();
  }
 
  public  void SetVisible(params string[] args) {
   bool b; string cmdId;
   RaiseBeforeCommandStateChanged();
   foreach (string cmdParam in args) {
    b = String.Compare(cmdParam.Substring(0,1),"+") == 0;
    cmdId = cmdParam.Substring(1);
    SetCommandComponentVisible (cmdId, b);
   }
   RaiseAfterCommandStateChanged();
  }
 
  public  void SetVisible(bool newState, params string[] args) {
   RaiseBeforeCommandStateChanged();
   foreach (string cmdParam in args) {
    SetCommandComponentVisible (cmdParam, newState);
   }
   RaiseAfterCommandStateChanged();
  }
 
  public  void Execute(string identifier) {
   if (registeredCommands.ContainsKey(identifier)) {
    ArrayList al = (ArrayList)registeredCommands[identifier];
    foreach (ICommand cmd in al) {
     cmd.Execute();
     break;
    }
   }
  }
 
  public  bool IsChecked(ICommand command)
  {
   bool _checked = false;
   ICommandComponent cmd = command as ICommandComponent;
   if (cmd != null) {
    if (command is AppContextMenuCommand)
     _checked = !cmd.Checked;
    if (command is AppStateButtonToolCommand)
     _checked = cmd.Checked;
   }
   return _checked;
  }
 
  private  void RaiseBeforeCommandStateChanged() {
   if (BeforeCommandStateChanged != null)
    BeforeCommandStateChanged(this, EventArgs.Empty);
  }
 
  private  void RaiseAfterCommandStateChanged() {
   if (AfterCommandStateChanged != null)
    AfterCommandStateChanged(this, EventArgs.Empty);
  }
 
  public  bool IsVisible(string identifier) {
   return IsCommandComponentVisible(identifier);
  }
 
  public  bool IsChecked(string identifier) {
   return IsCommandComponentChecked(identifier);
  }
 
  public  bool IsEnabled(string identifier) {
   return IsCommandComponentEnabled(identifier);
  }
 
  void ICommandMediator.SetChecked(params string[] identifierArgs) {
   SetChecked(true, identifierArgs);
  }
 
  public  void SetUncheck(params string[] identifierArgs) {
   SetChecked(false, identifierArgs);
  }
 
  void ICommandMediator.SetEnabled(params string[] identifierArgs) {
   SetEnabled(true, identifierArgs);
  }
 
  public  void SetDisabled(params string[] identifierArgs) {
   SetEnabled(false, identifierArgs);
  }
 
  public  void SetInvisible(params string[] identifierArgs) {
   SetVisible(false, identifierArgs);
  }
 
  void ICommandMediator.SetVisible(params string[] identifierArgs) {
   SetVisible(true, identifierArgs);
  }

	}

}
