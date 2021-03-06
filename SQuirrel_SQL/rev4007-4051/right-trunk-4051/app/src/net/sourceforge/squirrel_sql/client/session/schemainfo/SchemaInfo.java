package net.sourceforge.squirrel_sql.client.session.schemainfo;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import net.sourceforge.squirrel_sql.client.IApplication;
import net.sourceforge.squirrel_sql.client.gui.db.SQLAliasSchemaProperties;
import net.sourceforge.squirrel_sql.client.gui.db.SchemaLoadInfo;
import net.sourceforge.squirrel_sql.client.gui.db.SchemaNameLoadInfo;
import net.sourceforge.squirrel_sql.client.session.ExtendedColumnInfo;
import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.client.session.event.SessionAdapter;
import net.sourceforge.squirrel_sql.client.session.event.SessionEvent;
import net.sourceforge.squirrel_sql.fw.sql.DataTypeInfo;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.IProcedureInfo;
import net.sourceforge.squirrel_sql.fw.sql.ISQLConnection;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.sql.ProcedureInfo;
import net.sourceforge.squirrel_sql.fw.sql.ProgressCallBack;
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.fw.sql.TableColumnInfo;
import net.sourceforge.squirrel_sql.fw.sql.TableInfo;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

public class SchemaInfo
{
   public static final int TABLE_EXT_NOT_A_TABLE = 0;
   public static final int TABLE_EXT_COLS_LOADED_IN_THIS_CALL = 1;
   public static final int TABLE_EXT_COLS_LOADED_BEFORE = 2;



   private static final StringManager s_stringMgr =
      StringManagerFactory.getStringManager(SchemaInfo.class);

   private boolean _loading = false;
   private boolean _loaded = false;

   private SQLDatabaseMetaData _dmd;
   ISession _session = null;


   private static final ILogger s_log = LoggerController.createLogger(SchemaInfo.class);
   private SessionAdapter _sessionListener;

   
   private static final int LOAD_METHODS_COUNT = 7;

   private static final int MAX_PROGRESS = 100;

   static interface i18n {
       
       String LOADING_CATALOGS_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingCatalogs");
       
       
       String LOADING_KEYWORDS_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingKeywords");
       
       
       String LOADING_DATATYPES_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingDataTypes");
       
       
       String LOADING_FUNCTIONS_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingFunctions");

       
       String LOADING_TABLES_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingTables");
       
       
       String LOADING_PROCS_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingStoredProcedures");
       
       
       String LOADING_SCHEMAS_MSG = 
           s_stringMgr.getString("SchemaInfo.loadingSchemas");

   }
   
   
   final HashMap<CaseInsensitiveString,CaseInsensitiveString> _tablesLoadingColsInBackground = 
       new HashMap<CaseInsensitiveString,CaseInsensitiveString>();


   private SchemaInfoCache _schemaInfoCache;


   private Vector<SchemaInfoUpdateListener> _listeners = 
       new Vector<SchemaInfoUpdateListener>();
   private boolean _inInitialLoad;
   private long _initalLoadBeginTime;
   private boolean _sessionStartupTimeHintShown;

   private boolean _schemasAndCatalogsLoaded;
   private boolean _tablesLoaded;
   private boolean _storedProceduresLoaded;

   public SchemaInfo(IApplication app)
   {
      _sessionListener = new SessionAdapter()
      {
         public void connectionClosedForReconnect(SessionEvent evt)
         {
            if (null != _session && _session.getIdentifier().equals(evt.getSession().getIdentifier()))
            {
               _dmd = null;
            }
         }

         public void reconnected(SessionEvent evt)
         {
            if (null != _session && _session.getIdentifier().equals(evt.getSession().getIdentifier()))
            {
               _dmd = _session.getSQLConnection().getSQLMetaData();
               if (null != _dmd)
               {
                  s_log.info(s_stringMgr.getString("SchemaInfo.SuccessfullyRestoredDatabaseMetaData"));
               }
            }
         }

         public void sessionClosing(SessionEvent evt)
         {
            SchemaInfoCacheSerializer.store(_session, _schemaInfoCache);
         }
      };

      if (app != null)
      {
         app.getSessionManager().addSessionListener(_sessionListener);
      }
   }


   public void initialLoad(ISession session)
   {
      _session = session;

      breathing();
      _schemaInfoCache = SchemaInfoCacheSerializer.load(_session);

      try
      {
         _inInitialLoad = true;
         _initalLoadBeginTime = System.currentTimeMillis();
         privateLoadAll();
      }
      finally
      {
         _inInitialLoad = false;
         _schemaInfoCache.initialLoadDone();
      }
   }


   public void reloadAll()
   {
      reloadAll(true);
   }

   
   void reloadAll(boolean fireSchemaInfoUpdate)
   {
      _schemaInfoCache.clearAll();
      privateLoadAll();

      if(fireSchemaInfoUpdate)
      {
         fireSchemaInfoUpdate();
      }
   }

   private void privateLoadAll()
   {
      synchronized (this)
      {
         if(_loading)
         {
            return;
         }

         _loading = true;

         _schemasAndCatalogsLoaded = false;
         _tablesLoaded = false;
         _storedProceduresLoaded = false;
      }

      breathing();


       
      long mstart = System.currentTimeMillis();
      long mfinish = 0;
      
      try
      {
         ISQLConnection conn = _session.getSQLConnection();
         _dmd = conn.getSQLMetaData();

         _dmd.clearCache();


         int progress = 0;


         progress = loadCatalogs(progress);

         progress = loadSchemas(progress);

         notifySchemasAndCatalogsLoad();

         long start = 0, finish = 0;
         try
         {
            if (s_log.isDebugEnabled()) {
                s_log.debug(i18n.LOADING_KEYWORDS_MSG);
                start = System.currentTimeMillis();
            }            

            int beginProgress = getLoadMethodProgress(progress++);
            setProgress(i18n.LOADING_KEYWORDS_MSG, beginProgress);
            loadKeywords(i18n.LOADING_KEYWORDS_MSG, beginProgress);

            if (s_log.isDebugEnabled()) {
                finish = System.currentTimeMillis();
                s_log.debug("Keywords loaded in " + (finish - start) + " ms");
            }
         }
         catch (Exception ex)
         {
            s_log.error("Error loading keywords", ex);
         }

         try
         {
            if (s_log.isDebugEnabled()) {
                s_log.debug(i18n.LOADING_DATATYPES_MSG);
                start = System.currentTimeMillis();
            }
            int beginProgress = getLoadMethodProgress(progress++);
            setProgress(i18n.LOADING_DATATYPES_MSG, beginProgress);
            loadDataTypes(i18n.LOADING_DATATYPES_MSG, beginProgress);

            if (s_log.isDebugEnabled()) {
                finish = System.currentTimeMillis();
                s_log.debug("Data types loaded in " + (finish - start) + " ms");
            }
         }
         catch (Exception ex)
         {
            s_log.error("Error loading data types", ex);
         }

         try
         {
            if (s_log.isDebugEnabled()) {
                s_log.debug(i18n.LOADING_FUNCTIONS_MSG);
                start = System.currentTimeMillis();
            }
            
            int beginProgress = getLoadMethodProgress(progress++);
            setProgress(i18n.LOADING_FUNCTIONS_MSG, beginProgress);
            loadGlobalFunctions(i18n.LOADING_FUNCTIONS_MSG, beginProgress);

            if (s_log.isDebugEnabled()) {
                finish = System.currentTimeMillis();
                s_log.debug("Functions loaded in " + (finish - start) + " ms");
            }
         }
         catch (Exception ex)
         {
            s_log.error("Error loading functions", ex);
         }

         progress = loadTables(null, null, null, null, progress);
         notifyTablesLoaded();


         progress = loadStoredProcedures(null, null, null, progress);
         notifyStoredProceduresLoaded();

      }
      finally
      {
         if (_session != null && _session.getSessionSheet() != null)
         {
            _session.getSessionSheet().setStatusBarProgressFinished();
         }

         _loading = false;
         _loaded = true;
      }
      if (s_log.isDebugEnabled()) {
          mfinish = System.currentTimeMillis();
          s_log.debug("SchemaInfo.load took " + (mfinish - mstart) + " ms");
      }
   }

   private void notifyStoredProceduresLoaded()
   {
      synchronized(this)
      {
         _storedProceduresLoaded = true;
         this.notifyAll();
      }
   }

   private void notifyTablesLoaded()
   {
      synchronized(this)
      {
         _tablesLoaded = true;
         this.notifyAll();
      }
   }

   private void notifySchemasAndCatalogsLoad()
   {
      synchronized(this)
      {
         _schemasAndCatalogsLoaded = true;
         this.notifyAll();
      }
   }


   private int loadStoredProcedures(String catalog, String schema, String procNamePattern, int progress)
   {
      
      long start = 0, finish = 0;
      try
      {
         if (s_log.isDebugEnabled()) {
             s_log.debug(i18n.LOADING_PROCS_MSG);
             start = System.currentTimeMillis();
         }

         int beginProgress = getLoadMethodProgress(progress++);
         setProgress(i18n.LOADING_PROCS_MSG, beginProgress);
         privateLoadStoredProcedures(catalog, 
                                     schema, 
                                     procNamePattern, 
                                     i18n.LOADING_PROCS_MSG, 
                                     beginProgress);

         if (s_log.isDebugEnabled()) {
             finish = System.currentTimeMillis();
             s_log.debug("stored procedures loaded in " + (finish - start) + " ms");
         }
      }
      catch (Exception ex)
      {
         s_log.error("Error loading stored procedures", ex);
      }
      return progress;
   }

   private int loadTables(String catalog, 
                          String schema, 
                          String tableNamePattern, 
                          String[] types, 
                          int progress)
   {
      long start = 0, finish = 0;
      try
      {
         
         if (s_log.isDebugEnabled()) {
             s_log.debug(i18n.LOADING_TABLES_MSG);
             start = System.currentTimeMillis();
         }
         int beginProgress = getLoadMethodProgress(progress++);
         setProgress(i18n.LOADING_TABLES_MSG, beginProgress);
         privateLoadTables(catalog, 
                           schema, 
                           tableNamePattern, 
                           types, 
                           i18n.LOADING_TABLES_MSG, 
                           beginProgress);
         if (s_log.isDebugEnabled()) {
             finish = System.currentTimeMillis();
             s_log.debug("Tables loaded in " + (finish - start) + " ms");
         }
      }
      catch (Exception ex)
      {
         s_log.error("Error loading tables", ex);
      }
      return progress;
   }

   private int loadSchemas(int progress)
   {
      long start = 0, finish = 0;
      try
      {
         if (s_log.isDebugEnabled()) {
             s_log.debug(i18n.LOADING_SCHEMAS_MSG);
             start = System.currentTimeMillis();
         }

         int beginProgress = getLoadMethodProgress(progress++);
         setProgress(i18n.LOADING_SCHEMAS_MSG, beginProgress);
         privateLoadSchemas();

         if (s_log.isDebugEnabled()) {
             finish = System.currentTimeMillis();
             s_log.debug("Schemas loaded in " + (finish - start) + " ms");
         }
      }
      catch (Exception ex)
      {
         s_log.error("Error loading schemas", ex);
      }
      return progress;
   }

   private int loadCatalogs(int progress)
   {
      long start = 0, finish = 0;
      try
      {
         if (s_log.isDebugEnabled()) {
             s_log.debug(i18n.LOADING_CATALOGS_MSG);
             start = System.currentTimeMillis();
         }

         int beginProgress = getLoadMethodProgress(progress++);
         setProgress(i18n.LOADING_CATALOGS_MSG, beginProgress);
         privateLoadCatalogs();

         if (s_log.isDebugEnabled()) {
             finish = System.currentTimeMillis();
             s_log.debug("Catalogs loaded in " + (finish - start) + " ms");
         }
      }
      catch (Exception ex)
      {
         s_log.error("Error loading catalogs", ex);
      }
      return progress;
   }

   private int getLoadMethodProgress(int progress)
   {
      return (int)(((double)progress) / ((double)LOAD_METHODS_COUNT) * (MAX_PROGRESS));
   }

   private void setProgress(final String note, final int value)
   {
      breathing();

      if (_session == null || _session.getSessionSheet() == null)
      {
         return;
      }

     _session.getSessionSheet().setStatusBarProgress(note, 0, MAX_PROGRESS, value);

      if(_inInitialLoad
         && false == _sessionStartupTimeHintShown
         && false == _session.getAlias().getSchemaProperties().isCacheSchemaIndependentMetaData()
         && SQLAliasSchemaProperties.GLOBAL_STATE_LOAD_ALL_CACHE_NONE == _session.getAlias().getSchemaProperties().getGlobalState()
         && _session.getApplication().getSquirrelPreferences().getShowSessionStartupTimeHint()
         && System.currentTimeMillis() - _initalLoadBeginTime > 3000)
      {
         _sessionStartupTimeHintShown = true;
         SwingUtilities.invokeLater(new Runnable()
         {
            public void run()
            {
               new SessionStartupTimeHintController(_session);
            }
         });
      }
   }

   
   private void breathing()
   {
      synchronized(this)
      {
         try
         {
            wait(50);
         }
         catch (InterruptedException e)
         {
            s_log.info("Interrupted", e);
         }
      }
   }

   private void privateLoadStoredProcedures(String catalog, String schema, String procNamePattern, final String msg, final int beginProgress)
   {
      try
      {

         ProgressCallBack pcb = new ProgressCallBack()
         {
            public void currentlyLoading(String simpleName)
            {
               setProgress(msg + " (" + simpleName + ")", beginProgress);
            }
         };


         SchemaLoadInfo[] schemaLoadInfos = _schemaInfoCache.getMatchingSchemaLoadInfos(schema);

         for (int i = 0; i < schemaLoadInfos.length; i++)
         {
            if(schemaLoadInfos[i].loadProcedures)
            {
               IProcedureInfo[] procedures = _dmd.getProcedures(catalog, schemaLoadInfos[i].schemaName, procNamePattern, pcb);

               for (int j = 0; j < procedures.length; j++)
               {
                  _schemaInfoCache.writeToProcedureCache(procedures[j]);
               }
            }
         }
      }
      catch (Throwable th)
      {
         s_log.error("Failed to load stored procedures", th);
      }

   }

   private void privateLoadCatalogs()
   {
      try
      {
         if(false == _schemaInfoCache.loadSchemaIndependentMetaData())
         {
            return;
         }

         _schemaInfoCache.writeCatalogs(_dmd.getCatalogs());
      }
      catch (Throwable th)
      {
         s_log.error("failed to load catalog names", th);
      }
   }

   private void privateLoadSchemas()
   {
      try
      {

         _session.getApplication().getSessionManager().clearAllowedSchemaCache(_session);

         SchemaNameLoadInfo schemaNameLoadInfo = _schemaInfoCache.getSchemaNameLoadInfo();

         if(SchemaNameLoadInfo.STATE_DONT_REFERESH_SCHEMA_NAMES == schemaNameLoadInfo.state)
         {
            return;
         }

         String[] schemasToWrite;
         if(SchemaNameLoadInfo.STATE_REFERESH_SCHEMA_NAMES_FROM_DB == schemaNameLoadInfo.state)
         {
            schemasToWrite = _session.getApplication().getSessionManager().getAllowedSchemas(_session);
         }
         else if(SchemaNameLoadInfo.STATE_USES_PROVIDED_SCHEMA_NAMES == schemaNameLoadInfo.state)
         {
            schemasToWrite = schemaNameLoadInfo.schemaNames;
         }
         else
         {
            throw new IllegalArgumentException("Unknown SchemaNameLoadInfo.state = " + schemaNameLoadInfo.state);
         }

         _schemaInfoCache.writeSchemas(schemasToWrite);
      }
      catch (Throwable th)
      {
         s_log.error("failed to load schema names", th);
      }
   }


   public boolean isKeyword(String data)
   {
      return isKeyword(new CaseInsensitiveString(data));
   }

   
   public boolean isKeyword(CaseInsensitiveString data)
   {
      if (!_loading && data != null)
      {
         return _schemaInfoCache.getKeywordsForReadOnly().containsKey(data);
      }
      return false;
   }


   public boolean isDataType(String data)
   {
      return isDataType(new CaseInsensitiveString(data));
   }


   
   public boolean isDataType(CaseInsensitiveString data)
   {
      if (!_loading && data != null)
      {
         return _schemaInfoCache.getDataTypesForReadOnly().containsKey(data);
      }
      return false;
   }


   public boolean isFunction(String data)
   {
      return isFunction(new CaseInsensitiveString(data));
   }

   
   public boolean isFunction(CaseInsensitiveString data)
   {
      if (!_loading && data != null)
      {
         return _schemaInfoCache.getFunctionsForReadOnly().containsKey(data);
      }
      return false;
   }

   public boolean isTable(String data)
   {
      return isTable(new CaseInsensitiveString(data));
   }


   public boolean isTable(CaseInsensitiveString data)
   {
      int tableExtRes = isTableExt(data);
      return TABLE_EXT_COLS_LOADED_IN_THIS_CALL == tableExtRes || TABLE_EXT_COLS_LOADED_BEFORE == tableExtRes;

   }

   
   public int isTableExt(CaseInsensitiveString data)
   {
      if (!_loading && data != null)
      {
         if(_schemaInfoCache.getTableNamesForReadOnly().containsKey(data))
         {
            if (loadColumns(data))
            {
               return TABLE_EXT_COLS_LOADED_IN_THIS_CALL;
            }
            else
            {
               return TABLE_EXT_COLS_LOADED_BEFORE;
            }
         }
      }
      return TABLE_EXT_NOT_A_TABLE;
   }


   public boolean isColumn(String data)
   {
      return isColumn(new CaseInsensitiveString(data));
   }


   
   public boolean isColumn(CaseInsensitiveString data)
   {
      if (!_loading && data != null)
      {
         return _schemaInfoCache.getExtColumnInfosByColumnNameForReadOnly().containsKey(data);
      }
      return false;
   }

   
   public String getCaseSensitiveTableName(String data)
   {
      if (!_loading && data != null)
      {
         return _schemaInfoCache.getTableNamesForReadOnly().get(new CaseInsensitiveString(data));
      }
      return null;
   }

   public String getCaseSensitiveProcedureName(String data)
   {
      if (!_loading && data != null)
      {
         return _schemaInfoCache.getProcedureNamesForReadOnly().get(new CaseInsensitiveString(data));
      }
      return null;
   }




   private void loadKeywords(String msg, int beginProgress)
   {
      try
      {
         if(false == _schemaInfoCache.loadSchemaIndependentMetaData())
         {
            return;
         }

         setProgress(msg + " (default keywords)", beginProgress);

         Hashtable<CaseInsensitiveString, String> keywordsBuf = 
             new Hashtable<CaseInsensitiveString, String>();

         for (int i = 0; i < DefaultKeywords.KEY_WORDS.length; i++)
         {
            String kw = DefaultKeywords.KEY_WORDS[i];
            keywordsBuf.put(new CaseInsensitiveString(kw), kw);
         }


         
         if (_dmd != null)
         {

            setProgress(msg + " (DB specific keywords)", beginProgress);

            String[] sqlKeywords = _dmd.getSQLKeywords();

            for (int i = 0; i < sqlKeywords.length; i++)
            {
               _schemaInfoCache.getKeywordsForReadOnly().put(new CaseInsensitiveString(sqlKeywords[i]), sqlKeywords[i]);
            }

            String catalogTerm = _dmd.getCatalogTerm();
            if (catalogTerm != null) {
            	keywordsBuf.put(new CaseInsensitiveString(catalogTerm), catalogTerm);
            }

            String schemaTerm = _dmd.getSchemaTerm();
            if (schemaTerm != null) {
            	keywordsBuf.put(new CaseInsensitiveString(schemaTerm), schemaTerm);
            }

            String procedureTerm = _dmd.getProcedureTerm();
            if (procedureTerm != null) {
            	keywordsBuf.put(new CaseInsensitiveString(procedureTerm), procedureTerm);
            }
         }

         _schemaInfoCache.writeKeywords(keywordsBuf);
      }
      catch (Throwable ex)
      {
         s_log.error("Error occured creating keyword collection", ex);
      }
   }

   private void loadDataTypes(String msg, int beginProgress)
   {
      try
      {
         if(false == _schemaInfoCache.loadSchemaIndependentMetaData())
         {
            return;
         }

         Hashtable<CaseInsensitiveString, String> dataTypesBuf = 
             new Hashtable<CaseInsensitiveString, String>();

         DataTypeInfo[] infos = _dmd.getDataTypes();
         for (int i = 0; i < infos.length; i++)
         {
            String typeName = infos[i].getSimpleName();
            dataTypesBuf.put(new CaseInsensitiveString(typeName), typeName);

            if(0 == i % 100 )
            {
               setProgress(msg + " (" + typeName + ")", beginProgress);
            }

         }

         _schemaInfoCache.writeDataTypes(dataTypesBuf);
      }
      catch (Throwable ex)
      {
         s_log.error("Error occured creating data types collection", ex);
      }
   }

   private void loadGlobalFunctions(String msg, int beginProgress)
   {
      if(false == _schemaInfoCache.loadSchemaIndependentMetaData())
      {
         return;
      }

      ArrayList<String> buf = new ArrayList<String>();

      try
      {
         setProgress(msg + " (numeric functions)", beginProgress);
         buf.addAll(Arrays.asList(_dmd.getNumericFunctions()));
      }
      catch (Throwable ex)
      {
         s_log.error("Error", ex);
      }

      try
      {
         setProgress(msg + " (string functions)", beginProgress);
         buf.addAll(Arrays.asList((_dmd.getStringFunctions())));
      }
      catch (Throwable ex)
      {
         s_log.error("Error", ex);
      }

      try
      {
         setProgress(msg + " (time/date functions)", beginProgress);
         buf.addAll(Arrays.asList(_dmd.getTimeDateFunctions()));
      }
      catch (Throwable ex)
      {
         s_log.error("Error", ex);
      }

      Hashtable<CaseInsensitiveString, String> functionsBuf = 
          new Hashtable<CaseInsensitiveString, String>();
      for (int i = 0; i < buf.size(); i++)
      {
         String func = buf.get(i);
         if (func.length() > 0)
         {
            functionsBuf.put(new CaseInsensitiveString(func) ,func);
         }

      }

      _schemaInfoCache.writeFunctions(functionsBuf);
   }



   public String[] getKeywords()
   {
      return _schemaInfoCache.getKeywordsForReadOnly().values().toArray(new String[_schemaInfoCache.getKeywordsForReadOnly().size()]);
   }

   public String[] getDataTypes()
   {
      return _schemaInfoCache.getDataTypesForReadOnly().values().toArray(new String[_schemaInfoCache.getDataTypesForReadOnly().size()]);
   }

   public String[] getFunctions()
   {
      return _schemaInfoCache.getFunctionsForReadOnly().values().toArray(new String[_schemaInfoCache.getFunctionsForReadOnly().size()]);
   }

   public String[] getTables()
   {
      return _schemaInfoCache.getTableNamesForReadOnly().values().toArray(new String[_schemaInfoCache.getTableNamesForReadOnly().size()]);
   }

   public String[] getCatalogs()
   {
      return _schemaInfoCache.getCatalogsForReadOnly().toArray(new String[_schemaInfoCache.getCatalogsForReadOnly().size()]);
   }

   public String[] getSchemas()
   {
      return _schemaInfoCache.getSchemasForReadOnly().toArray(new String[_schemaInfoCache.getSchemasForReadOnly().size()]);
   }

   public ITableInfo[] getITableInfos()
   {
      return getITableInfos(null, null);
   }

   public ITableInfo[] getITableInfos(String catalog, String schema)
   {
      return getITableInfos(catalog, schema, null);
   }


   public ITableInfo[] getITableInfos(String catalog, String schema, String simpleName)
   {
      return getITableInfos(catalog, schema, simpleName, null);
   }

   public ITableInfo[] getITableInfos(String catalog, String schema, String tableNamePattern, String[] types)
   {
      ArrayList<ITableInfo> ret = new ArrayList<ITableInfo>();
      if (null != types)
      {
         
         ITableInfo[] tableInfosForUncachedTypes = getTableInfosForUncachedTypes(catalog, schema, tableNamePattern, types);
         ret.addAll(Arrays.asList(tableInfosForUncachedTypes));
      }

      List<ITableInfo> tis = 
          _schemaInfoCache.getITableInfosForReadOnly();
      for(ITableInfo iTableInfo : tis) 
      {
         if(null != catalog &&
            false == catalog.equalsIgnoreCase(iTableInfo.getCatalogName()) &&
            false == fulfillsPlatformDependendMatches(iTableInfo, catalog)
            )
         {
            continue;
         }

         if(null != schema && false == schema.equalsIgnoreCase(iTableInfo.getSchemaName()) )
         {
            continue;
         }

         if(false == SchemaInfoCache.containsType(types, iTableInfo.getType()))
         {
            continue;
         }

         if(null != tableNamePattern && false == tableNamePattern.endsWith("%") && false == iTableInfo.getSimpleName().equals(tableNamePattern))
         {
            continue;
         }

         if(null != tableNamePattern && tableNamePattern.endsWith("%"))
         {
            String tableNameBegin = tableNamePattern.substring(0, tableNamePattern.length() - 1);
            if(false == iTableInfo.getSimpleName().startsWith(tableNameBegin))
            {
               continue;
            }
         }

         ret.add(iTableInfo);
      }

      return ret.toArray(new ITableInfo[ret.size()]);
   }

   private boolean fulfillsPlatformDependendMatches(ITableInfo iTableInfo, String catalog)
   {
      if(SQLDatabaseMetaData.DriverMatch.isComHttxDriver(_session.getSQLConnection()))
      {
         return ( iTableInfo.getCatalogName()==null && "\".\"".equals(catalog));
      }
      else
      {
         return false;
      }


   }

   private ITableInfo[] getTableInfosForUncachedTypes(String catalog, String schema, String tableNamePattern, String[] types)
   {
      try
      {
         ArrayList<String> missingTypes = new ArrayList<String>();
         for (int i = 0; i < types.length; i++)
         {
            if(false == _schemaInfoCache.isCachedTableType(types[i]))
            {
               missingTypes.add(types[i]);
            }
         }

         if(0 < missingTypes.size())
         {
            try
            {
               String[] buf = missingTypes.toArray(new String[missingTypes.size()]);
               ProgressCallBack pcb = new ProgressCallBack()
               {
                  public void currentlyLoading(String simpleName)
                  {
                     StringBuilder tmp = new StringBuilder(i18n.LOADING_TABLES_MSG);
                     tmp.append(" (");
                     tmp.append(simpleName);
                     tmp.append(")");
                     setProgress(tmp.toString(), 1);
                  }
               };
               return _dmd.getTables(catalog, schema, tableNamePattern, buf, pcb);
            }
            finally
            {
               _session.getSessionSheet().setStatusBarProgressFinished();
            }
         }

      }
      catch (SQLException e)
      {
         s_log.error("Error loading uncached tables", e);
      }

      return new ITableInfo[0];
   }



   public IProcedureInfo[] getStoredProceduresInfos(String catalog, String schema)
   {
      return getStoredProceduresInfos(catalog, schema, null);
   }


   public IProcedureInfo[] getStoredProceduresInfos(String catalog, String schema, String procNamePattern)
   {
      ArrayList<IProcedureInfo> ret = new ArrayList<IProcedureInfo>();

      for (Iterator<IProcedureInfo> i = 
          _schemaInfoCache.getIProcedureInfosForReadOnly().keySet().iterator(); i.hasNext();)
      {

         IProcedureInfo iProcInfo = i.next();
         boolean toAdd = true;
         if (null != catalog && false == catalog.equalsIgnoreCase(iProcInfo.getCatalogName()))
         {
            toAdd = false;
         }

         if (null != schema && false == schema.equalsIgnoreCase(iProcInfo.getSchemaName()))
         {
            toAdd = false;
         }

         if (null != procNamePattern && false == procNamePattern.endsWith("%") && false == iProcInfo.getSimpleName().equals(procNamePattern))
         {
            toAdd = false;
         }

         if (null != procNamePattern && procNamePattern.endsWith("%"))
         {
            String tableNameBegin = procNamePattern.substring(0, procNamePattern.length() - 1);
            if (false == iProcInfo.getSimpleName().startsWith(tableNameBegin))
            {
               toAdd = false;
            }
         }


         if (toAdd)
         {
            ret.add(iProcInfo);
         }
      }

      return ret.toArray(new IProcedureInfo[ret.size()]);
   }


   public boolean isLoaded()
   {
      return _loaded;
   }

   private void privateLoadTables(String catalog,
                                  String schema,
                                  String tableNamePattern,
                                  String[] types,
                                  final String msg,
                                  final int beginProgress)
   {
      try
      {
         ProgressCallBack pcb = new ProgressCallBack()
         {
            public void currentlyLoading(String simpleName)
            {
               setProgress(msg + " (" + simpleName + ")", beginProgress);
            }
         };
         SchemaLoadInfo[] schemaLoadInfos = 
            _schemaInfoCache.getMatchingSchemaLoadInfos(schema, types);
         
         for (int i = 0; i < schemaLoadInfos.length; i++)
         {
            ITableInfo[] infos = _dmd.getTables(catalog,
                  schemaLoadInfos[i].schemaName, tableNamePattern,
                  schemaLoadInfos[i].tableTypes, pcb);
            _schemaInfoCache.writeToTableCache(infos);
         }
      }
      catch (Throwable th)
      {
         s_log.error("failed to load table names", th);
      }
   }

   
   private boolean loadColumns(final CaseInsensitiveString tableName)
   {
      try
      {
         if(_schemaInfoCache.getExtendedColumnInfosByTableNameForReadOnly().containsKey(tableName))
         {
            return false;
         }


         if (_session.getProperties().getLoadColumnsInBackground())
         {
            if(_tablesLoadingColsInBackground.containsKey(tableName))
            {
               return false;
            }

            
            
            
            final CaseInsensitiveString imutableString = new CaseInsensitiveString(tableName.toString());
            _tablesLoadingColsInBackground.put(imutableString, imutableString);
            _session.getApplication().getThreadPool().addTask(new Runnable()
            {
               public void run()
               {
                  try
                  {
                     accessDbToLoadColumns(imutableString);
                     _tablesLoadingColsInBackground.remove(imutableString);
                  }
                  catch (SQLException e)
                  {
                     throw new RuntimeException(e);
                  }
               }
            });
         }
         else
         {
            accessDbToLoadColumns(tableName);
         }
      }
      catch (Throwable th)
      {
         s_log.error("failed to load table names", th);
      }

      return true;
   }

   private void accessDbToLoadColumns(CaseInsensitiveString tableName)
      throws SQLException
   {
      if (null == _dmd)
      {
         s_log.warn(s_stringMgr.getString("SchemaInfo.UnableToLoadColumns", tableName));
         return;
      }
      String name = getCaseSensitiveTableName(tableName.toString());
      TableInfo ti =
         new TableInfo(null, null, name, "TABLE", null, _dmd);
      TableColumnInfo[] infos = _dmd.getColumnInfo(ti);
      _schemaInfoCache.writeColumsToCache(infos, tableName);
   }


   public ExtendedColumnInfo[] getExtendedColumnInfos(String tableName)
   {
      return getExtendedColumnInfos(null, null, tableName);
   }

   public ExtendedColumnInfo[] getExtendedColumnInfos(String catalog, String schema, String tableName)
   {
      CaseInsensitiveString cissTableName = new CaseInsensitiveString(tableName);
      loadColumns(cissTableName);
      List<ExtendedColumnInfo> extColInfo = _schemaInfoCache.getExtendedColumnInfosByTableNameForReadOnly().get(cissTableName);

      if (null == extColInfo)
      {
         return new ExtendedColumnInfo[0];
      }

      if (null == catalog && null == schema)
      {
         return extColInfo.toArray(new ExtendedColumnInfo[extColInfo.size()]);
      }
      else
      {
         ArrayList<ExtendedColumnInfo> ret = new ArrayList<ExtendedColumnInfo>();

         for (int i = 0; i < extColInfo.size(); i++)
         {
            ExtendedColumnInfo extendedColumnInfo = extColInfo.get(i);
            boolean toAdd = true;
            if (null != catalog && false == catalog.equalsIgnoreCase(extendedColumnInfo.getCatalog()))
            {
               toAdd = false;
            }

            if (null != schema && false == schema.equalsIgnoreCase(extendedColumnInfo.getSchema()))
            {
               toAdd = false;
            }

            if (toAdd)
            {
               ret.add(extendedColumnInfo);
            }
         }

         return ret.toArray(new ExtendedColumnInfo[ret.size()]);
      }
   }

   public void dispose()
   {
      
      
      
      _session.getApplication().getSessionManager().removeSessionListener(_sessionListener);
   }

   public boolean isProcedure(CaseInsensitiveString data)
   {
      return _schemaInfoCache.getProcedureNamesForReadOnly().containsKey(data);
   }

   public void reload(IDatabaseObjectInfo doi)
   {
      reload(doi, true);
   }

   
   void reload(IDatabaseObjectInfo doi, boolean fireSchemaInfoUpdate)
   {
      boolean doReloadAll = false;

      try
      {
         synchronized (this)
         {
            if(_loading)
            {
               return;
            }
            _loading = true;
            _schemasAndCatalogsLoaded = false;
            _tablesLoaded = false;
            _storedProceduresLoaded = false;

         }


         if (doi instanceof ITableInfo)
         {
            ITableInfo ti = (ITableInfo) doi;
            DatabaseObjectType dot = ti.getDatabaseObjectType();

            String[] types = null;
            if (DatabaseObjectType.TABLE == dot)
            {
               types = new String[]{"TABLE"};
            }
            else if (DatabaseObjectType.VIEW == dot)
            {
               types = new String[]{"VIEW"};
            }

            _schemaInfoCache.clearTables(ti.getCatalogName(), ti.getSchemaName(), ti.getSimpleName(), types);
            loadTables(ti.getCatalogName(), ti.getSchemaName(), ti.getSimpleName(), types, 1);
         }
         else if(doi instanceof IProcedureInfo)
         {
            IProcedureInfo pi = (IProcedureInfo) doi;
            _schemaInfoCache.clearStoredProcedures(pi.getCatalogName(), pi.getSchemaName(), pi.getSimpleName());
            loadStoredProcedures(pi.getCatalogName(), pi.getSchemaName(), pi.getSimpleName(), 1);
         }
         else if(DatabaseObjectType.TABLE_TYPE_DBO == doi.getDatabaseObjectType())
         {
            
            _schemaInfoCache.clearTables(doi.getCatalogName(), doi.getSchemaName(), null, null);
            loadTables(doi.getCatalogName(), doi.getSchemaName(), null, null, 0);
         }
         else if(DatabaseObjectType.TABLE == doi.getDatabaseObjectType())
         {
            
            _schemaInfoCache.clearTables(doi.getCatalogName(), doi.getSchemaName(), null, new String[]{"TABLE"});
            loadTables(doi.getCatalogName(), doi.getSchemaName(), null, new String[]{"TABLE"}, 1);
         }
         else if(DatabaseObjectType.VIEW == doi.getDatabaseObjectType())
         {
            
            _schemaInfoCache.clearTables(doi.getCatalogName(), doi.getSchemaName(), null, new String[]{"VIEW"});
            loadTables(doi.getCatalogName(), doi.getSchemaName(), null, new String[]{"VIEW"}, 1);
         }
         else if(DatabaseObjectType.PROCEDURE == doi.getDatabaseObjectType() || DatabaseObjectType.PROC_TYPE_DBO == doi.getDatabaseObjectType())
         {
            _schemaInfoCache.clearStoredProcedures(doi.getCatalogName(), doi.getSchemaName(), null);
            loadStoredProcedures(doi.getCatalogName(), doi.getSchemaName(), null, 1);
         }
         else if(DatabaseObjectType.SCHEMA == doi.getDatabaseObjectType())
         {
            
            
            _schemaInfoCache.clearTables(null, doi.getSchemaName(), null, null);
            int progress = loadTables(null, doi.getSchemaName(), null, null, 1);

            
            _schemaInfoCache.clearStoredProcedures(null, doi.getSchemaName(), null);
            loadStoredProcedures(null, doi.getSchemaName(), null, progress);
         }
         else if(DatabaseObjectType.CATALOG == doi.getDatabaseObjectType())
         {
            
            
            _schemaInfoCache.clearTables(doi.getCatalogName(), null, null, null);
            int progress = loadTables(doi.getCatalogName(), null, null, null, 1);

            
            _schemaInfoCache.clearStoredProcedures(doi.getCatalogName(), null, null);
            loadStoredProcedures(doi.getCatalogName(), null, null, progress);
         }
         else if(DatabaseObjectType.SESSION == doi.getDatabaseObjectType())
         {
            doReloadAll = true;
         }

         
         
         
      }
      finally
      {
         _session.getSessionSheet().setStatusBarProgressFinished();
         _loading = false;
         _schemasAndCatalogsLoaded = true;
         _tablesLoaded = true;
         _storedProceduresLoaded = true;
         notifySchemasAndCatalogsLoad();
         notifyTablesLoaded();
         notifyStoredProceduresLoaded();

         if(doReloadAll)
         {
            reloadAll(fireSchemaInfoUpdate);
         }
         else
         {
            if(fireSchemaInfoUpdate)
            {
               fireSchemaInfoUpdate();
            }
         }
      }
   }

   public void fireSchemaInfoUpdate()
   {
      System.out.println("SchemaInfo.fireSchemaInfoUpdate");
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            SchemaInfoUpdateListener[] listeners = 
                _listeners.toArray(new SchemaInfoUpdateListener[0]);

            for (int i = 0; i < listeners.length; i++)
            {
               listeners[i].schemaInfoUpdated();
            }
         }
      });


   }

   public void addSchemaInfoUpdateListener(SchemaInfoUpdateListener l)
   {
      _listeners.remove(l);
      _listeners.add(l);
   }

   public void removeSchemaInfoUpdateListener(SchemaInfoUpdateListener l)
   {
      _listeners.remove(l);
   }


   public void refershCacheForSimpleTableName(String simpleTableName)
   {
      refershCacheForSimpleTableName(simpleTableName, true);
   }

   
   void refershCacheForSimpleTableName(String simpleTableName, boolean fireSchemaInfoUpdate)
   {
      HashMap<String, String> caseSensitiveTableNames = new HashMap<String, String>();

      CaseInsensitiveString caseInsensitiveTableName = new CaseInsensitiveString(simpleTableName);
      String caseSensitiveTableName = _schemaInfoCache.getTableNamesForReadOnly().get(caseInsensitiveTableName);

      caseSensitiveTableNames.put(caseSensitiveTableName, caseSensitiveTableName);

      
      
      for(Iterator<String> i=caseSensitiveTableNames.keySet().iterator(); i.hasNext();)
      {
         String buf = i.next();
         TableInfo ti = new TableInfo(null, null, buf, null, null, _dmd);
         reload(ti, fireSchemaInfoUpdate);
      }
      
      






   }

   public void refreshCacheForSimpleProcedureName(String simpleProcName)
   {
      refreshCacheForSimpleProcedureName(simpleProcName, true);
   }

   
   void refreshCacheForSimpleProcedureName(String simpleProcName, boolean fireSchemaInfoUpdate)
   {
      HashMap<String, String> caseSensitiveProcNames = new HashMap<String, String>();

      CaseInsensitiveString caseInsensitiveProcName = new CaseInsensitiveString(simpleProcName);
      String caseSensitiveProcName = _schemaInfoCache.getProcedureNamesForReadOnly().remove(caseInsensitiveProcName);

      caseSensitiveProcNames.put(caseSensitiveProcName, caseSensitiveProcName);

      
      
      for(Iterator<String> i=caseSensitiveProcNames.keySet().iterator(); i.hasNext();)
      {
         String buf = i.next();
         ProcedureInfo pi = new ProcedureInfo(null, null, buf, null, DatabaseMetaData.procedureResultUnknown, _dmd);
         reload(pi, fireSchemaInfoUpdate);
      }
      
      






   }

   public void waitTillSchemasAndCatalogsLoaded()
   {
      try
      {
         synchronized (this)
         {
            while (false == _schemasAndCatalogsLoaded)
            {
               this.wait();
            }
         }
      }
      catch (InterruptedException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void waitTillTablesLoaded()
   {
      try
      {
         synchronized (this)
         {
            while (false == _tablesLoaded)
            {
               this.wait();
            }
         }
      }
      catch (InterruptedException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void waitTillStoredProceduresLoaded()
   {
      try
      {
         synchronized (this)
         {
            while (false == _storedProceduresLoaded)
            {
               this.wait();
            }
         }
      }
      catch (InterruptedException e)
      {
         throw new RuntimeException(e);
      }
   }


}
