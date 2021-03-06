package net.sourceforge.squirrel_sql.client.preferences;

import net.sourceforge.squirrel_sql.client.action.ActionKeys;
import net.sourceforge.squirrel_sql.client.gui.mainframe.MainFrameWindowState;
import net.sourceforge.squirrel_sql.client.plugin.PluginStatus;
import net.sourceforge.squirrel_sql.client.session.properties.SessionProperties;
import net.sourceforge.squirrel_sql.client.util.ApplicationFiles;
import net.sourceforge.squirrel_sql.fw.util.PropertyChangeReporter;
import net.sourceforge.squirrel_sql.fw.util.ProxySettings;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.UpdateSettings;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanReader;
import net.sourceforge.squirrel_sql.fw.xml.XMLBeanWriter;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("serial")
public class SquirrelPreferences implements Serializable
{

   public interface IPropertyNames
   {
      String ACTION_KEYS = "actionKeys";
      String ALIASES_SELECTED_INDEX = "aliasesSelectdIndex";
      String CONFIRM_SESSION_CLOSE = "confirmSessionClose";
      String DRIVERS_SELECTED_INDEX = "driversSelectdIndex";
      String FIRST_RUN = "firstRun";
      String JDBC_DEBUG_TYPE = "jdbcDebugtype";
      String LOGIN_TIMEOUT = "loginTimeout";
      String LARGE_SCRIPT_STMT_COUNT = "largeScriptStmtCount";
      String MAIN_FRAME_STATE = "mainFrameWindowState";
      String MAXIMIMIZE_SESSION_SHEET_ON_OPEN = "maximizeSessionSheetOnOpen";
      String NEW_SESSION_VIEW = "newSessionView";
      String PLUGIN_OBJECTS = "pluginObjects";
      String PLUGIN_STATUSES = "pluginStatuses";
      String PROXY = "proxyPerferences";
      String UPDATE = "updatePreferences";
      String SCROLLABLE_TABBED_PANES = "getUseScrollableTabbedPanes";
      String SESSION_PROPERTIES = "sessionProperties";
      String SHOW_ALIASES_TOOL_BAR = "showAliasesToolBar";
      String SHOW_CONTENTS_WHEN_DRAGGING = "showContentsWhenDragging";
      String SHOW_DRIVERS_TOOL_BAR = "showDriversToolBar";
      String SHOW_LOADED_DRIVERS_ONLY = "showLoadedDriversOnly";
      String SHOW_MAIN_STATUS_BAR = "showMainStatusBar";
      String SHOW_MAIN_TOOL_BAR = "showMainToolBar";
      String SHOW_TOOLTIPS = "showToolTips";
      String SHOW_COLOR_ICONS_IN_TOOLBAR = "showColorIconsInToolbars";
      String SHOW_PLUGIN_FILES_IN_SPLASH_SCREEN = "showPluginFilesInSplashScreen";
      String FILE_OPEN_IN_PREVIOUS_DIR = "fileOpenInPreviousDir";
      String FILE_OPEN_IN_SPECIFIED_DIR = "fileOpenInSpecifiedDir";
      String FILE_SPECIFIED_DIR = "fileSpecifiedDir";
      String FILE_PREVIOUS_DIR = "filePreviousdDir";
      String WARN_JRE_JDBC_MISMATCH = "warnJreJdbcMismatch";
      String WARN_FOR_UNSAVED_FILE_EDITS = "warnForUnsavedFileEdits";
      String WARN_FOR_UNSAVED_BUFFER_EDITS = "warnForUnsavedBufferEdits";
      String SHOW_SESSION_STARTUP_TIME_HINT = "showSessionStartupTimeHint";
      String SHOW_DEBUG_LOG_MESSAGES = "showDebugLogMessages";
      String SHOW_INFO_LOG_MESSAGES = "showInfoLogMessages";
      String SHOW_ERROR_LOG_MESSAGES = "showErrorLogMessages";
      String SAVE_PREFERENCES_IMMEDIATELY = "savePreferencesImmediately";      
   }

   public interface IJdbcDebugTypes
	{
		int NONE = 0;
		int TO_STREAM = 1;
		int TO_WRITER = 2;
	}

	
	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(SquirrelPreferences.class);

	
	private final static ILogger s_log =
		LoggerController.createLogger(SquirrelPreferences.class);

	
	private MainFrameWindowState _mainFrameState = new MainFrameWindowState();

	
	private SessionProperties _sessionProps = new SessionProperties();

	
	private boolean _showContentsWhenDragging = false;


	private boolean _fileOpenInPreviousDir = true;

	private boolean _fileOpenInSpecifiedDir = false;

	private String _fileSpecifiedDir = "";

	private String _filePreviousDir = System.getProperty("user.home");

	
	private int _jdbcDebugType = IJdbcDebugTypes.NONE;

	
	private int _loginTimeout = 30;

    
    private int _largeScriptStmtCount = 200;
    
	
	
	private String _newSessionView;

	
	private boolean _showToolTips = true;

	
	private boolean _useScrollableTabbedPanes = false;

	
	private boolean _showMainStatusBar = true;

	
	private boolean _showMainToolBar = true;

	
	private boolean _showDriversToolBar = true;

	
	private boolean _maxSessionSheetOnOpen = false;

	
	private boolean _showAliasesToolBar = true;

	
	private boolean _showColorIconsInToolbars = true;

    
    private boolean _showPluginFilesInSplashScreen = false;
    
	
	private ActionKeys[] _actionsKeys = new ActionKeys[0];

	
	private ProxySettings _proxySettings = new ProxySettings();

	
	private UpdateSettings _updateSettings = new UpdateSettings();
	
	
	private int _driversSelectedIndex = -1;

	
	private int _aliasesSelectedIndex = -1;

	
	private boolean _showLoadedDriversOnly;

 	
 	private boolean _firstRun = true;

	
 	private boolean _confirmSessionClose = true;

    
    private boolean _warnJreJdbcMismatch = true;
    
	
	private final ArrayList<PluginStatus> _pluginStatusInfoColl = 
        new ArrayList<PluginStatus>();

    
    private boolean _warnForUnsavedFileEdits = true;

    
    private boolean _warnForUnsavedBufferEdits = true;

    
    private boolean _showSessionStartupTimeHint = true;

    
    private boolean _showDebugLogMessages = true;

    
    private boolean _showInfoLogMessages = true;

    
    private boolean _showErrorLogMessages = true;

    
    private boolean _savePreferencesImmediately = true;

    

    
    

    
    

    
     

    
	
	private transient PropertyChangeReporter _propChgReporter;


	
	
	public SquirrelPreferences()
	{
		super();
		loadDefaults();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChangeReporter().addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		getPropertyChangeReporter().removePropertyChangeListener(listener);
	}

	public String getNewSessionView()
	{
		return _newSessionView;
	}
	
	public synchronized void setNewSessionView(String data)
	{
		if (((data == null) && (_newSessionView != null)) || (data != null)
				&& !data.equals(_newSessionView))
		{
			final String oldValue = _newSessionView;
			_newSessionView = data;
			getPropertyChangeReporter().firePropertyChange(
					IPropertyNames.NEW_SESSION_VIEW, oldValue, _newSessionView);
		}
	}

   public SessionProperties getSessionProperties()
	{
		return _sessionProps;
	}

	public synchronized void setSessionProperties(SessionProperties data)
	{
		if (_sessionProps != data)
		{
			final SessionProperties oldValue = _sessionProps;
			_sessionProps = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SESSION_PROPERTIES,
												oldValue, _sessionProps);
		}
	}

	public MainFrameWindowState getMainFrameWindowState()
	{
		return _mainFrameState;
	}

	
	public synchronized void setMainFrameWindowState(MainFrameWindowState data)
	{
		final MainFrameWindowState oldValue = _mainFrameState;
		_mainFrameState = data;
		getPropertyChangeReporter().firePropertyChange(IPropertyNames.MAIN_FRAME_STATE,
											oldValue, _mainFrameState);
	}

	public boolean getShowContentsWhenDragging()
	{
		return _showContentsWhenDragging;
	}

	public synchronized void setShowContentsWhenDragging(boolean data)
	{
		if (data != _showContentsWhenDragging)
		{
			final boolean oldValue = _showContentsWhenDragging;
			_showContentsWhenDragging = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SHOW_CONTENTS_WHEN_DRAGGING,
												oldValue, _showContentsWhenDragging);
		}
	}

	public boolean getShowMainStatusBar()
	{
		return _showMainStatusBar;
	}

	public synchronized void setShowMainStatusBar(boolean data)
	{
		if (data != _showMainStatusBar)
		{
			final boolean oldValue = _showMainStatusBar;
			_showMainStatusBar = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SHOW_MAIN_STATUS_BAR,
											oldValue, _showMainStatusBar);
		}
	}

	public boolean getShowMainToolBar()
	{
		return _showMainToolBar;
	}

	public synchronized void setShowMainToolBar(boolean data)
	{
		if (data != _showMainToolBar)
		{
			final boolean oldValue = _showMainToolBar;
			_showMainToolBar = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SHOW_MAIN_TOOL_BAR,
												oldValue, _showMainToolBar);
		}
	}

	public boolean getShowAliasesToolBar()
	{
		return _showAliasesToolBar;
	}

	public synchronized void setShowAliasesToolBar(boolean data)
	{
		if (data != _showAliasesToolBar)
		{
			final boolean oldValue = _showAliasesToolBar;
			_showAliasesToolBar = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SHOW_ALIASES_TOOL_BAR,
												oldValue, _showAliasesToolBar);
		}
	}

	public boolean getShowDriversToolBar()
	{
		return _showDriversToolBar;
	}

	public synchronized void setShowDriversToolBar(boolean data)
	{
		if (data != _showDriversToolBar)
		{
			final boolean oldValue = _showDriversToolBar;
			_showDriversToolBar = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SHOW_DRIVERS_TOOL_BAR,
												oldValue, _showDriversToolBar);
		}
	}

	public boolean getShowColoriconsInToolbar()
	{
		return _showColorIconsInToolbars;
	}

	public synchronized void setShowColoriconsInToolbar(boolean data)
	{
		if (data != _showColorIconsInToolbars)
		{
			final boolean oldValue = _showColorIconsInToolbars;
			_showColorIconsInToolbars = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.SHOW_COLOR_ICONS_IN_TOOLBAR,
												oldValue, _showColorIconsInToolbars);
		}
	}

    public boolean getShowPluginFilesInSplashScreen()
    {
        return _showPluginFilesInSplashScreen;
    }

    public synchronized void setShowPluginFilesInSplashScreen(boolean data)
    {
        if (data != _showPluginFilesInSplashScreen)
        {
            final boolean oldValue = _showPluginFilesInSplashScreen;
            _showPluginFilesInSplashScreen = data;
            getPropertyChangeReporter().firePropertyChange(
                            IPropertyNames.SHOW_PLUGIN_FILES_IN_SPLASH_SCREEN,
                            oldValue, 
                            _showPluginFilesInSplashScreen);
        }
    }    
    
	public int getLoginTimeout()
	{
		return _loginTimeout;
	}

	public synchronized void setLoginTimeout(int data)
	{
		if (data != _loginTimeout)
		{
			final int oldValue = _loginTimeout;
			_loginTimeout = data;
			getPropertyChangeReporter().firePropertyChange(IPropertyNames.LOGIN_TIMEOUT,
												oldValue, _loginTimeout);
		}
	}

    public int getLargeScriptStmtCount() {
        return _largeScriptStmtCount;
    }
    
    public synchronized void setLargeScriptStmtCount(int count) {
        if (count != _largeScriptStmtCount) {
            final int oldValue = _largeScriptStmtCount;
            _largeScriptStmtCount = count;
            getPropertyChangeReporter().firePropertyChange(
                                        IPropertyNames.LARGE_SCRIPT_STMT_COUNT,
                                        oldValue, 
                                        _largeScriptStmtCount);
        }
    }
    
	public int getJdbcDebugType()
	{
		return _jdbcDebugType;
	}

	public synchronized void setJdbcDebugType(int data)
	{
		if (data < IJdbcDebugTypes.NONE || data > IJdbcDebugTypes.TO_WRITER)
		{
			throw new IllegalArgumentException("Invalid setDebugJdbcToStream of :" + data);
		}

		if (data != _jdbcDebugType)
		{
			final int oldValue = _jdbcDebugType;
			_jdbcDebugType = data;
			getPropertyChangeReporter().firePropertyChange(
					IPropertyNames.JDBC_DEBUG_TYPE, oldValue, _jdbcDebugType);
		}
	}

	public boolean getShowToolTips()
	{
		return _showToolTips;
	}

	public synchronized void setShowToolTips(boolean data)
	{
		if (data != _showToolTips)
		{
			final boolean oldValue = _showToolTips;
			_showToolTips = data;
			getPropertyChangeReporter().firePropertyChange(
												IPropertyNames.SHOW_TOOLTIPS,
												oldValue, _showToolTips);
		}
	}

	public boolean getUseScrollableTabbedPanes()
	{
		return _useScrollableTabbedPanes;
	}

	public synchronized void setUseScrollableTabbedPanes(boolean data)
	{
		if (data != _useScrollableTabbedPanes)
		{
			final boolean oldValue = _useScrollableTabbedPanes;
			_useScrollableTabbedPanes = data;
			getPropertyChangeReporter().firePropertyChange(
										IPropertyNames.SCROLLABLE_TABBED_PANES,
										oldValue, _useScrollableTabbedPanes);
		}
	}

	public boolean getMaximizeSessionSheetOnOpen()
	{
		return _maxSessionSheetOnOpen;
	}

	public synchronized void setMaximizeSessionSheetOnOpen(boolean data)
	{
		if (data != _maxSessionSheetOnOpen)
		{
			final boolean oldValue = _maxSessionSheetOnOpen;
			_maxSessionSheetOnOpen= data;
			getPropertyChangeReporter().firePropertyChange(
							IPropertyNames.MAXIMIMIZE_SESSION_SHEET_ON_OPEN,
							oldValue, _maxSessionSheetOnOpen);
		}
	}

	public ActionKeys[] getActionKeys()
	{
		return _actionsKeys;
	}

	public ActionKeys getActionKeys(int idx)
	{
		return _actionsKeys[idx];
	}

	
	public synchronized void setActionKeys(ActionKeys[] data)
	{
		final ActionKeys[] oldValue = _actionsKeys;
		_actionsKeys = data != null ? data : new ActionKeys[0];
		getPropertyChangeReporter().firePropertyChange(IPropertyNames.ACTION_KEYS,
											oldValue, _actionsKeys);
	}

	
	public void setActionKeys(int idx, ActionKeys value)
	{
		final ActionKeys[] oldValue = _actionsKeys;
		_actionsKeys[idx] = value;
		getPropertyChangeReporter().firePropertyChange(IPropertyNames.ACTION_KEYS,
											oldValue, _actionsKeys);
	}

	public synchronized PluginStatus[] getPluginStatuses()
	{
		final PluginStatus[] ar = new PluginStatus[_pluginStatusInfoColl.size()];
		return _pluginStatusInfoColl.toArray(ar);
	}

	public PluginStatus getPluginStatus(int idx)
	{
		return _pluginStatusInfoColl.get(idx);
	}

	
	public synchronized void setPluginStatuses(PluginStatus[] data)
	{
		if (data == null)
		{
			data = new PluginStatus[0];
		}

		PluginStatus[] oldValue = new PluginStatus[_pluginStatusInfoColl.size()];
		oldValue = _pluginStatusInfoColl.toArray(oldValue);
		_pluginStatusInfoColl.clear();
		_pluginStatusInfoColl.addAll(Arrays.asList(data));
		getPropertyChangeReporter().firePropertyChange(IPropertyNames.PLUGIN_STATUSES,
											oldValue, data);
	}

	
	public synchronized void setPluginStatus(int idx, PluginStatus value)
	{
		_pluginStatusInfoColl.ensureCapacity(idx + 1);
		final PluginStatus oldValue = _pluginStatusInfoColl.get(idx);;
		_pluginStatusInfoColl.set(idx, value);
		getPropertyChangeReporter().firePropertyChange(IPropertyNames.PLUGIN_STATUSES,
											oldValue, value);
	}

	
	public ProxySettings getProxySettings()
	{
		return (ProxySettings)_proxySettings.clone();
	}

	public UpdateSettings getUpdateSettings() {
	   return (UpdateSettings)_updateSettings.clone(); 
	}

   
   public synchronized void setUpdateSettings(UpdateSettings data)
   {
      if (data == null)
      {
         data = new UpdateSettings();
      }
      final UpdateSettings oldValue = _updateSettings;
      _updateSettings= data;
      getPropertyChangeReporter().firePropertyChange(IPropertyNames.UPDATE,
                                 oldValue, _updateSettings);
   }
	
	
	
	public synchronized void setProxySettings(ProxySettings data)
	{
		if (data == null)
		{
			data = new ProxySettings();
		}
		final ProxySettings oldValue = _proxySettings;
		_proxySettings= data;
		getPropertyChangeReporter().firePropertyChange(IPropertyNames.PROXY,
											oldValue, _proxySettings);
	}

	
	public int getAliasesSelectedIndex()
	{
		return _aliasesSelectedIndex;
	}

	
	public synchronized void setAliasesSelectedIndex(int idx)
	{
		if (idx != _aliasesSelectedIndex)
		{
			final int oldValue = _aliasesSelectedIndex;
			_aliasesSelectedIndex = idx;
			getPropertyChangeReporter().firePropertyChange(
										IPropertyNames.ALIASES_SELECTED_INDEX,
										oldValue, _aliasesSelectedIndex);
		}
	}

	
	public int getDriversSelectedIndex()
	{
		return _driversSelectedIndex;
	}

	
	public synchronized void setDriversSelectedIndex(int idx)
	{
		if (idx != _driversSelectedIndex)
		{
			final int oldValue = _driversSelectedIndex;
			_driversSelectedIndex = idx;
			getPropertyChangeReporter().firePropertyChange(
										IPropertyNames.DRIVERS_SELECTED_INDEX,
										oldValue, _driversSelectedIndex);
		}
	}

	
	public boolean getShowLoadedDriversOnly()
	{
		return _showLoadedDriversOnly;
	}

	
	public synchronized void setShowLoadedDriversOnly(boolean data)
	{
		if (data != _showLoadedDriversOnly)
		{
			final boolean oldValue = _showLoadedDriversOnly;
			_showLoadedDriversOnly = data;
			getPropertyChangeReporter().firePropertyChange(
										IPropertyNames.SHOW_LOADED_DRIVERS_ONLY,
										oldValue, _showLoadedDriversOnly);
		}
	}

 	
 	public boolean isFirstRun()
 	{
 		return _firstRun;
 	}

 	public synchronized void setFirstRun(boolean data)
 	{
 		if (data != _firstRun)
 		{
 			final boolean oldValue = _firstRun;
 			_firstRun = data;
 			getPropertyChangeReporter().firePropertyChange(IPropertyNames.FIRST_RUN,
 											oldValue, _firstRun);
 		}
 	}
 	
 	public boolean getConfirmSessionClose()
 	{
 		return _confirmSessionClose;
 	}

 	public synchronized void setConfirmSessionClose(boolean data)
 	{
 		if (data != _confirmSessionClose)
 		{
 			final boolean oldValue = _confirmSessionClose;
 			_confirmSessionClose = data;
 			getPropertyChangeReporter().firePropertyChange(
 										IPropertyNames.CONFIRM_SESSION_CLOSE,
 										oldValue, _confirmSessionClose);
 		}
 	}


   public boolean isFileOpenInPreviousDir()
   {
      return _fileOpenInPreviousDir;
   }

   public synchronized void setFileOpenInPreviousDir(boolean data)
   {
      if (data != _fileOpenInPreviousDir)
      {
         final boolean oldValue = _fileOpenInPreviousDir;
         _fileOpenInPreviousDir = data;
         getPropertyChangeReporter().firePropertyChange(
                              IPropertyNames.FILE_OPEN_IN_PREVIOUS_DIR,
                              oldValue, _fileOpenInPreviousDir);
      }
   }


   public boolean isFileOpenInSpecifiedDir()
   {
      return _fileOpenInSpecifiedDir;
   }

   public synchronized void setFileOpenInSpecifiedDir(boolean data)
   {
      if (data != _fileOpenInSpecifiedDir)
      {
         final boolean oldValue = _fileOpenInSpecifiedDir;
         _fileOpenInSpecifiedDir = data;
         getPropertyChangeReporter().firePropertyChange(
                              IPropertyNames.FILE_OPEN_IN_SPECIFIED_DIR,
                              oldValue, _fileOpenInSpecifiedDir);
      }
   }

   public String getFileSpecifiedDir()
   {
      return _fileSpecifiedDir;
   }

   public synchronized void setFileSpecifiedDir(String data)
   {
      if (false == ("" + data).equals(_fileSpecifiedDir))
      {
         final String oldValue = _fileSpecifiedDir;
         _fileSpecifiedDir = data;
         getPropertyChangeReporter().firePropertyChange(
                              IPropertyNames.FILE_SPECIFIED_DIR,
                              oldValue, _fileSpecifiedDir);
      }
   }

   public String getFilePreviousDir()
   {
      return _filePreviousDir;
   }

   public synchronized void setFilePreviousDir(String data)
   {
      if (false == ("" + data).equals(_filePreviousDir))
      {
         final String oldValue = _filePreviousDir;
         _filePreviousDir = data;
         getPropertyChangeReporter().firePropertyChange(
                              IPropertyNames.FILE_PREVIOUS_DIR,
                              oldValue, _filePreviousDir);
      }
   }



	
	public boolean isJdbcDebugToStream()
	{
		return _jdbcDebugType == IJdbcDebugTypes.TO_STREAM;
	}

	
	public boolean isJdbcDebugToWriter()
	{
		return _jdbcDebugType == IJdbcDebugTypes.TO_WRITER;
	}

	
	public boolean isJdbcDebugDontDebug()
	{
		return !(isJdbcDebugToStream() || isJdbcDebugToWriter());
	}

	
	public void doJdbcDebugToStream()
	{
		setJdbcDebugType(IJdbcDebugTypes.TO_STREAM);
	}

	
	public void doJdbcDebugToWriter()
	{
		setJdbcDebugType(IJdbcDebugTypes.TO_WRITER);
	}

	
	public void dontDoJdbcDebug()
	{
		setJdbcDebugType(IJdbcDebugTypes.NONE);
	}

    @SuppressWarnings("unchecked")
	public static SquirrelPreferences load()
	{
		File prefsFile = new ApplicationFiles().getUserPreferencesFile();
		try
		{
			XMLBeanReader doc = new XMLBeanReader();
			doc.load(prefsFile);
			Iterator it = doc.iterator();
			if (it.hasNext())
			{
				return (SquirrelPreferences)it.next();

			}
		}
		catch (FileNotFoundException ignore)
		{
			
		}
		catch (Exception ex)
		{
			s_log.error(s_stringMgr.getString("SquirrelPreferences.error.reading", prefsFile.getPath()), ex);
		}
		return new SquirrelPreferences();
	}

	
	public synchronized void save()
	{
		File prefsFile = new ApplicationFiles().getUserPreferencesFile();
		try
		{
			XMLBeanWriter wtr = new XMLBeanWriter(this);
			wtr.save(prefsFile);
		}
		catch (Exception ex)
		{
			s_log.error(s_stringMgr.getString("SquirrelPreferences.error.writing",
												prefsFile.getPath()), ex);
		}
	}

	private void loadDefaults()
	{
		if (_loginTimeout == -1)
		{
			_loginTimeout = DriverManager.getLoginTimeout();
		}
	}

	private synchronized PropertyChangeReporter getPropertyChangeReporter()
	{
		if (_propChgReporter == null)
		{
			_propChgReporter = new PropertyChangeReporter(this);
		}
		return _propChgReporter;
	}

    
    public synchronized void setWarnJreJdbcMismatch(boolean data) {
        if (data != _warnJreJdbcMismatch)
        {
            final boolean oldValue = _warnJreJdbcMismatch;
            _warnJreJdbcMismatch = data;
            getPropertyChangeReporter().firePropertyChange(
                                        IPropertyNames.WARN_JRE_JDBC_MISMATCH,
                                        oldValue, _warnJreJdbcMismatch);
        }
    }

    
    public boolean getWarnJreJdbcMismatch() {
        return _warnJreJdbcMismatch;
    }

    
    public synchronized void setWarnForUnsavedFileEdits(boolean data) {
        if (data != _warnForUnsavedFileEdits)
        {
            final boolean oldValue = _warnForUnsavedFileEdits;
            _warnForUnsavedFileEdits = data;
            getPropertyChangeReporter().firePropertyChange(
                                        IPropertyNames.WARN_FOR_UNSAVED_FILE_EDITS,
                                        oldValue, _warnForUnsavedFileEdits);
        }
    }

    
    public boolean getWarnForUnsavedFileEdits() {
        return _warnForUnsavedFileEdits;
    }
    
    
    public synchronized void setWarnForUnsavedBufferEdits(boolean data) {
        if (data != _warnForUnsavedBufferEdits)
        {
            final boolean oldValue = _warnForUnsavedBufferEdits;
            _warnForUnsavedBufferEdits = data;
            getPropertyChangeReporter().firePropertyChange(
                                        IPropertyNames.WARN_FOR_UNSAVED_BUFFER_EDITS,
                                        oldValue, _warnForUnsavedBufferEdits);
        }
    }

    
    public boolean getWarnForUnsavedBufferEdits() {
        return _warnForUnsavedBufferEdits;
    }



   
   public synchronized void setShowSessionStartupTimeHint(boolean data)
   {
      if (data != _showSessionStartupTimeHint)
      {
         final boolean oldValue = _showSessionStartupTimeHint;
         _showSessionStartupTimeHint = data;
         getPropertyChangeReporter().firePropertyChange(
            IPropertyNames.SHOW_SESSION_STARTUP_TIME_HINT,
            oldValue, _showSessionStartupTimeHint);
      }
   }

   
   public boolean getShowSessionStartupTimeHint()
   {
      return _showSessionStartupTimeHint;
   }

   
   public synchronized void setShowDebugLogMessages(boolean data)
   {
      if (data != _showDebugLogMessages)
      {
         final boolean oldValue = _showDebugLogMessages;
         _showDebugLogMessages = data;
         getPropertyChangeReporter().firePropertyChange(
            IPropertyNames.SHOW_DEBUG_LOG_MESSAGES,
            oldValue, _showDebugLogMessages);
      }
   }

   
   public boolean getShowDebugLogMessage()
   {
      return _showDebugLogMessages;
   }

 


   public void setShowInfoLogMessages(boolean data) {
       if (data != _showInfoLogMessages)
       {
          final boolean oldValue = _showInfoLogMessages;
          _showInfoLogMessages = data;
          getPropertyChangeReporter().firePropertyChange(
             IPropertyNames.SHOW_INFO_LOG_MESSAGES,
             oldValue, _showInfoLogMessages);
       }
   }

   
   public boolean getShowInfoLogMessages() {
       return _showInfoLogMessages;
   }

   
   public void setShowErrorLogMessages(boolean data) {
       if (data != _showErrorLogMessages)
       {
          final boolean oldValue = _showErrorLogMessages;
          _showErrorLogMessages = data;
          getPropertyChangeReporter().firePropertyChange(
             IPropertyNames.SHOW_ERROR_LOG_MESSAGES,
             oldValue, _showErrorLogMessages);
       }
   }

   
   public boolean getShowErrorLogMessages() {
       return _showErrorLogMessages;
   }
   
   
   public void setSavePreferencesImmediately(boolean data) {
       if (data != _showErrorLogMessages)
       {
          final boolean oldValue = _savePreferencesImmediately;
          _savePreferencesImmediately = data;
          getPropertyChangeReporter().firePropertyChange(
             IPropertyNames.SAVE_PREFERENCES_IMMEDIATELY,
             oldValue, _savePreferencesImmediately);
       }
   }

   
   public boolean getSavePreferencesImmediately() {
       return _savePreferencesImmediately;
   }   
   
}
