package net.sourceforge.squirrel_sql.client.session.properties;



import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;


public class SessionPropertiesBeanInfo extends SimpleBeanInfo
{

	private interface IPropNames extends SessionProperties.IPropertyNames
	{
		
	}

	
	@Override	
	public PropertyDescriptor[] getPropertyDescriptors()
	{
		try
		{
			PropertyDescriptor[] result =
				new PropertyDescriptor[] {
						new PropertyDescriptor(IPropNames.AUTO_COMMIT, SessionProperties.class, "getAutoCommit",
							"setAutoCommit"),
						new PropertyDescriptor(IPropNames.COMMIT_ON_CLOSING_CONNECTION, SessionProperties.class,
							"getCommitOnClosingConnection", "setCommitOnClosingConnection"),
						new PropertyDescriptor(IPropNames.CONTENTS_LIMIT_ROWS, SessionProperties.class,
							"getContentsLimitRows", "setContentsLimitRows"),
						new PropertyDescriptor(IPropNames.CONTENTS_NBR_ROWS_TO_SHOW, SessionProperties.class,
							"getContentsNbrRowsToShow", "setContentsNbrRowsToShow"),
						new PropertyDescriptor(IPropNames.FONT_INFO, SessionProperties.class, "getFontInfo",
							"setFontInfo"),
						new PropertyDescriptor(IPropNames.META_DATA_OUTPUT_CLASS_NAME, SessionProperties.class,
							"getMetaDataOutputClassName", "setMetaDataOutputClassName"),
						new PropertyDescriptor(IPropNames.SHOW_ROW_COUNT, SessionProperties.class,
							"getShowRowCount", "setShowRowCount"),
						new PropertyDescriptor(IPropNames.SHOW_TOOL_BAR, SessionProperties.class, "getShowToolBar",
							"setShowToolBar"),
						new PropertyDescriptor(IPropNames.SQL_LIMIT_ROWS, SessionProperties.class,
							"getSQLLimitRows", "setSQLLimitRows"),
						new PropertyDescriptor(IPropNames.SQL_NBR_ROWS_TO_SHOW, SessionProperties.class,
							"getSQLNbrRowsToShow", "setSQLNbrRowsToShow"),
						new PropertyDescriptor(IPropNames.SQL_STATEMENT_SEPARATOR_STRING, SessionProperties.class,
							"getSQLStatementSeparator", "setSQLStatementSeparator"),
						new PropertyDescriptor(IPropNames.SQL_RESULTS_OUTPUT_CLASS_NAME, SessionProperties.class,
							"getSQLResultsOutputClassName", "setSQLResultsOutputClassName"),
						new PropertyDescriptor(IPropNames.SQL_START_OF_LINE_COMMENT, SessionProperties.class,
							"getStartOfLineComment", "setStartOfLineComment"),
						new PropertyDescriptor(IPropNames.REMOVE_MULTI_LINE_COMMENT, SessionProperties.class,
							"getRemoveMultiLineComment", "setRemoveMultiLineComment"),
						new PropertyDescriptor(IPropNames.LIMIT_SQL_ENTRY_HISTORY_SIZE, SessionProperties.class,
							"getLimitSQLEntryHistorySize", "setLimitSQLEntryHistorySize"),
						new PropertyDescriptor(IPropNames.SQL_ENTRY_HISTORY_SIZE, SessionProperties.class,
							"getSQLEntryHistorySize", "setSQLEntryHistorySize"),
						new PropertyDescriptor(IPropNames.SQL_SHARE_HISTORY, SessionProperties.class,
							"getSQLShareHistory", "setSQLShareHistory"),
						new PropertyDescriptor(IPropNames.MAIN_TAB_PLACEMENT, SessionProperties.class,
							"getMainTabPlacement", "setMainTabPlacement"),
						new PropertyDescriptor(IPropNames.OBJECT_TAB_PLACEMENT, SessionProperties.class,
							"getObjectTabPlacement", "setObjectTabPlacement"),
						new PropertyDescriptor(IPropNames.SQL_EXECUTION_TAB_PLACEMENT, SessionProperties.class,
							"getSQLExecutionTabPlacement", "setSQLExecutionTabPlacement"),
						new PropertyDescriptor(IPropNames.SQL_RESULTS_TAB_PLACEMENT, SessionProperties.class,
							"getSQLResultsTabPlacement", "setSQLResultsTabPlacement"),
						new PropertyDescriptor(IPropNames.TABLE_CONTENTS_OUTPUT_CLASS_NAME,
							SessionProperties.class, "getTableContentsOutputClassName",
							"setTableContentsOutputClassName"),
						new PropertyDescriptor(IPropNames.ABORT_ON_ERROR, SessionProperties.class,
							"getAbortOnError", "setAbortOnError"),
						new PropertyDescriptor(IPropNames.SQL_RESULT_TAB_LIMIT, SessionProperties.class,
							"getSqlResultTabLimit", "setSqlResultTabLimit"),
						new PropertyDescriptor(IPropNames.LIMIT_SQL_RESULT_TABS, SessionProperties.class,
							"getLimitSQLResultTabs", "setLimitSQLResultTabs"),
						new PropertyDescriptor(IPropNames.LOAD_SCHEMAS_CATALOGS, SessionProperties.class,
							"getLoadSchemasCatalogs", "setLoadSchemasCatalogs"),
						new PropertyDescriptor(IPropNames.SHOW_RESULTS_META_DATA, SessionProperties.class,
							"getShowResultsMetaData", "setShowResultsMetaData"),

						new PropertyDescriptor(IPropNames.CATALOG_FILTER_INCLUDE, SessionProperties.class,
							"getCatalogFilterInclude", "setCatalogFilterInclude"),
						new PropertyDescriptor(IPropNames.SCHEMA_FILTER_INCLUDE, SessionProperties.class,
							"getSchemaFilterInclude", "setSchemaFilterInclude"),
						new PropertyDescriptor(IPropNames.OBJECT_FILTER_INCLUDE, SessionProperties.class,
							"getObjectFilterInclude", "setObjectFilterInclude"),
						new PropertyDescriptor(IPropNames.CATALOG_FILTER_EXCLUDE, SessionProperties.class,
							"getCatalogFilterExclude", "setCatalogFilterExclude"),
						new PropertyDescriptor(IPropNames.SCHEMA_FILTER_EXCLUDE, SessionProperties.class,
							"getSchemaFilterExclude", "setSchemaFilterExclude"),
						new PropertyDescriptor(IPropNames.OBJECT_FILTER_EXCLUDE, SessionProperties.class,
							"getObjectFilterExclude", "setObjectFilterExclude") };

			return result;
		}
		catch (IntrospectionException e)
		{
			throw new Error(e);
		}
	}
}
