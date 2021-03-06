
package net.sourceforge.squirrel_sql.plugins.db2.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.BaseDataTypeComponent;
import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.IDataTypeComponent;
import net.sourceforge.squirrel_sql.fw.sql.ISQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;


public class DB2XmlTypeDataTypeComponent extends BaseDataTypeComponent
		implements IDataTypeComponent {

	
	private static final ILogger s_log = LoggerController
			.createLogger(DB2XmlTypeDataTypeComponent.class);

	
	private static final StringManager s_stringMgr = StringManagerFactory
			.getStringManager(DB2XmlTypeDataTypeComponent.class);

	
	static interface i18n {
		String CELL_ERROR_MSG = s_stringMgr
				.getString("DB2XmlTypeDataTypeComponent.cellErrorMsg");
	}

	

	
	public boolean canDoFileIO() {
		return true;
	}

	
	public String getClassName() {
		return String.class.getName();
	}

	
	public Object getDefaultValue(String dbDefaultValue) {
		
		if (s_log.isInfoEnabled()) {
			s_log.info("getDefaultValue: not yet implemented");
		}
		return null;
	}

	
	public String getWhereClauseValue(Object value, ISQLDatabaseMetaData md) {
		StringBuilder where = new StringBuilder();
		if (value == null || value.toString() == null) {
			where.append(_colDef.getFullTableColumnName());
			where.append(" IS NULL");
		} else {
			where.append("XMLSERIALIZE (CONTENT ");
			where.append(_colDef.getFullTableColumnName());
			where.append(" AS CLOB(1M)) like '");
			where.append(value);
			where.append("'");
		}
		return where.toString();
	}

	
	public boolean isEditableInCell(Object originalValue) {
		return !i18n.CELL_ERROR_MSG.equals(originalValue);
	}

	
	public boolean isEditableInPopup(Object originalValue) {
		return !i18n.CELL_ERROR_MSG.equals(originalValue);
	}

	
	public boolean needToReRead(Object originalValue) {
		return false;
	}

	
	public Object readResultSet(ResultSet rs, int idx, boolean limitDataRead)
			throws SQLException {
		String result = null;
		try {
			result = rs.getString(idx);
			if (rs.wasNull() || result == null) {
				return NULL_VALUE_PATTERN;
			}
		} catch (Exception e) {
			s_log.error("Unexpected exception while attempting to read "
					+ "SYS.XMLType column", e);
		}
		if (result == null) {
			result = i18n.CELL_ERROR_MSG;
		}
		return result;
	}

	
	public void setPreparedStatementValue(PreparedStatement pstmt,
			Object value, int position) throws SQLException {
		try {
			if (value == null) {
				pstmt.setNull(position, _colDef.getSqlType(), _colDef
						.getSqlTypeName());
			} else {
				pstmt.setString(position, value.toString());

			}
		} catch (Exception e) {
			s_log.error("setPreparedStatementValue: Unexpected exception - "
					+ e.getMessage(), e);
		}
	}

	
	public boolean useBinaryEditingPanel() {
		return false;
	}

	
	public boolean areEqual(Object obj1, Object obj2) {
		return ((String) obj1).equals(obj2);
	}

}