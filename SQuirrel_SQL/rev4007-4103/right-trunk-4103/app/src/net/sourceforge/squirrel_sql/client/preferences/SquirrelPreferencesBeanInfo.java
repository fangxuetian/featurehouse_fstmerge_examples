package net.sourceforge.squirrel_sql.client.preferences;



import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;


public class SquirrelPreferencesBeanInfo extends SimpleBeanInfo
   implements SquirrelPreferences.IPropertyNames
{
   private static PropertyDescriptor[] s_dscrs;

   @SuppressWarnings("unchecked")
   private static Class CLS = SquirrelPreferences.class;

   public SquirrelPreferencesBeanInfo() throws IntrospectionException
   {
      super();
      if (s_dscrs == null)
      {
         s_dscrs = new PropertyDescriptor[]
         {
            new PropertyDescriptor(SESSION_PROPERTIES, CLS,
               "getSessionProperties", "setSessionProperties"),
            new PropertyDescriptor(MAIN_FRAME_STATE, CLS,
               "getMainFrameWindowState", "setMainFrameWindowState"),
            new PropertyDescriptor(SHOW_CONTENTS_WHEN_DRAGGING, CLS,
               "getShowContentsWhenDragging", "setShowContentsWhenDragging"),
            new PropertyDescriptor(LOGIN_TIMEOUT, CLS,
               "getLoginTimeout", "setLoginTimeout"),
            new PropertyDescriptor(LARGE_SCRIPT_STMT_COUNT, CLS,
               "getLargeScriptStmtCount", "setLargeScriptStmtCount"),
            new PropertyDescriptor(JDBC_DEBUG_TYPE, CLS,
               "getJdbcDebugType", "setJdbcDebugType"),
            new PropertyDescriptor(SHOW_MAIN_STATUS_BAR, CLS,
               "getShowMainStatusBar", "setShowMainStatusBar"),
            new PropertyDescriptor(SHOW_MAIN_TOOL_BAR, CLS,
               "getShowMainToolBar", "setShowMainToolBar"),
            new PropertyDescriptor(SHOW_ALIASES_TOOL_BAR, CLS,
               "getShowAliasesToolBar", "setShowAliasesToolBar"),
            new PropertyDescriptor(SHOW_DRIVERS_TOOL_BAR, CLS,
               "getShowDriversToolBar", "setShowDriversToolBar"),
            new PropertyDescriptor(SHOW_TOOLTIPS, CLS,
               "getShowToolTips", "setShowToolTips"),
            new PropertyDescriptor(SCROLLABLE_TABBED_PANES, CLS,
               "getUseScrollableTabbedPanes", "setUseScrollableTabbedPanes"),
            new IndexedPropertyDescriptor(ACTION_KEYS, CLS,
               "getActionKeys", "setActionKeys",
               "getActionKeys", "setActionKeys"),
            new PropertyDescriptor(PROXY, CLS,
               "getProxySettings", "setProxySettings"),
            new PropertyDescriptor(UPDATE, CLS,
               "getUpdateSettings", "setUpdateSettings"),
            new PropertyDescriptor(ALIASES_SELECTED_INDEX, CLS,
               "getAliasesSelectedIndex", "setAliasesSelectedIndex"),
            new PropertyDescriptor(DRIVERS_SELECTED_INDEX, CLS,
               "getDriversSelectedIndex", "setDriversSelectedIndex"),
            new PropertyDescriptor(SHOW_LOADED_DRIVERS_ONLY, CLS,
               "getShowLoadedDriversOnly", "setShowLoadedDriversOnly"),
            new PropertyDescriptor(MAXIMIMIZE_SESSION_SHEET_ON_OPEN, CLS,
               "getMaximizeSessionSheetOnOpen", "setMaximizeSessionSheetOnOpen"),
            new PropertyDescriptor(SHOW_COLOR_ICONS_IN_TOOLBAR, CLS,
               "getShowColoriconsInToolbar", "setShowColoriconsInToolbar"),
            new PropertyDescriptor(FIRST_RUN, CLS,
               "isFirstRun", "setFirstRun"),
            new PropertyDescriptor(CONFIRM_SESSION_CLOSE, CLS,
               "getConfirmSessionClose", "setConfirmSessionClose"),
            new IndexedPropertyDescriptor(PLUGIN_STATUSES, CLS,
               "getPluginStatuses", "setPluginStatuses",
               "getPluginStatus", "setPluginStatus"),
            new PropertyDescriptor(NEW_SESSION_VIEW, CLS,
               "getNewSessionView", "setNewSessionView"),
            new PropertyDescriptor(FILE_OPEN_IN_PREVIOUS_DIR, CLS,
               "isFileOpenInPreviousDir", "setFileOpenInPreviousDir"),
            new PropertyDescriptor(FILE_OPEN_IN_SPECIFIED_DIR, CLS,
               "isFileOpenInSpecifiedDir", "setFileOpenInSpecifiedDir"),
            new PropertyDescriptor(FILE_SPECIFIED_DIR, CLS,
               "getFileSpecifiedDir", "setFileSpecifiedDir"),
            new PropertyDescriptor(FILE_PREVIOUS_DIR, CLS,
               "getFilePreviousDir", "setFilePreviousDir"),
            new PropertyDescriptor(SHOW_PLUGIN_FILES_IN_SPLASH_SCREEN, CLS,
               "getShowPluginFilesInSplashScreen", 
               "setShowPluginFilesInSplashScreen"),
            new PropertyDescriptor(WARN_JRE_JDBC_MISMATCH, CLS,
               "getWarnJreJdbcMismatch", "setWarnJreJdbcMismatch"),
            new PropertyDescriptor(WARN_FOR_UNSAVED_FILE_EDITS, CLS,
               "getWarnForUnsavedFileEdits", "setWarnForUnsavedFileEdits"),
            new PropertyDescriptor(WARN_FOR_UNSAVED_BUFFER_EDITS, CLS,
               "getWarnForUnsavedBufferEdits", "setWarnForUnsavedBufferEdits"),
            new PropertyDescriptor(SHOW_SESSION_STARTUP_TIME_HINT, CLS,
               "getShowSessionStartupTimeHint", "setShowSessionStartupTimeHint"),
            new PropertyDescriptor(SHOW_DEBUG_LOG_MESSAGES, CLS,
               "getShowDebugLogMessages", "setShowDebugLogMessages"),               
            new PropertyDescriptor(SHOW_INFO_LOG_MESSAGES, CLS,
               "getShowInfoLogMessages", "setShowInfoLogMessages"),
            new PropertyDescriptor(SHOW_ERROR_LOG_MESSAGES, CLS,
               "getShowErrorLogMessages", "setShowErrorLogMessages"),
            new PropertyDescriptor(SAVE_PREFERENCES_IMMEDIATELY, CLS,
               "getSavePreferencesImmediately", "setSavePreferencesImmediately"),            
         };
      }
   }

   public PropertyDescriptor[] getPropertyDescriptors()
   {
      return s_dscrs;
   }
}
