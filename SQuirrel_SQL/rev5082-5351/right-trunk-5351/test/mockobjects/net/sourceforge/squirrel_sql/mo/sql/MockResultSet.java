package net.sourceforge.squirrel_sql.mo.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import net.sourceforge.squirrel_sql.fw.sql.TableColumnInfo;


public class MockResultSet implements ResultSet
{

	
	ResultSetMetaData rsmd = null;

	
	TableColumnInfo[] _infos = null;

	
	ArrayList<Object[]> rowData = new ArrayList<Object[]>();

	
	int cursorIndex = -1;

	public MockResultSet()
	{
	}

	public void setMetaData(ResultSetMetaData md)
	{
		rsmd = md;
	}

	
	public MockResultSet(TableColumnInfo[] infos)
	{
		_infos = infos;
		rsmd = new MockResultSetMetaData(infos);
	}

	public void setTableColumnInfos(TableColumnInfo[] infos)
	{
		_infos = infos;
		rsmd = new MockResultSetMetaData(infos);
	}

	
	public void addRow(Object[] values)
	{
		rowData.add(values);
	}

	public boolean absolute(int arg0) throws SQLException
	{

		return false;
	}

	public void afterLast() throws SQLException
	{

	}

	public void beforeFirst() throws SQLException
	{

	}

	public void cancelRowUpdates() throws SQLException
	{

	}

	public void clearWarnings() throws SQLException
	{

	}

	public void close() throws SQLException
	{

	}

	public void deleteRow() throws SQLException
	{

	}

	public int findColumn(String arg0) throws SQLException
	{

		return 0;
	}

	public boolean first() throws SQLException
	{

		return false;
	}

	public Array getArray(int arg0) throws SQLException
	{

		return null;
	}

	public Array getArray(String arg0) throws SQLException
	{

		return null;
	}

	public InputStream getAsciiStream(int arg0) throws SQLException
	{

		return null;
	}

	public InputStream getAsciiStream(String arg0) throws SQLException
	{

		return null;
	}

	public BigDecimal getBigDecimal(int arg0) throws SQLException
	{

		return null;
	}

	public BigDecimal getBigDecimal(String arg0) throws SQLException
	{

		return null;
	}

	public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException
	{

		return null;
	}

	public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException
	{

		return null;
	}

	public InputStream getBinaryStream(int arg0) throws SQLException
	{

		return null;
	}

	public InputStream getBinaryStream(String arg0) throws SQLException
	{

		return null;
	}

	public Blob getBlob(int arg0) throws SQLException
	{

		return null;
	}

	public Blob getBlob(String arg0) throws SQLException
	{

		return null;
	}

	public boolean getBoolean(int arg0) throws SQLException
	{

		return false;
	}

	public boolean getBoolean(String arg0) throws SQLException
	{

		return false;
	}

	public byte getByte(int arg0) throws SQLException
	{

		return 0;
	}

	public byte getByte(String arg0) throws SQLException
	{

		return 0;
	}

	public byte[] getBytes(int arg0) throws SQLException
	{

		return null;
	}

	public byte[] getBytes(String arg0) throws SQLException
	{

		return null;
	}

	public Reader getCharacterStream(int arg0) throws SQLException
	{

		return null;
	}

	public Reader getCharacterStream(String arg0) throws SQLException
	{

		return null;
	}

	public Clob getClob(int arg0) throws SQLException
	{

		return null;
	}

	public Clob getClob(String arg0) throws SQLException
	{

		return null;
	}

	public int getConcurrency() throws SQLException
	{

		return 0;
	}

	public String getCursorName() throws SQLException
	{

		return null;
	}

	public Date getDate(int colIdx) throws SQLException
	{
		Object[] currentRow = currentRow();
		if (colIdx - 1 < currentRow.length)
		{

			if (currentRow[colIdx - 1] instanceof Date)
			{
				return (Date) currentRow[colIdx - 1];
			}
			else
			{
				throw new SQLException("not a Date: " + currentRow[colIdx - 1].getClass().getName());
			}
		}
		else
		{
			throw new SQLException("Error: requested value for non-existant column with index=" + (colIdx - 1));
		}
	}

	public Date getDate(String arg0) throws SQLException
	{

		return null;
	}

	public Date getDate(int arg0, Calendar arg1) throws SQLException
	{

		return null;
	}

	public Date getDate(String arg0, Calendar arg1) throws SQLException
	{

		return null;
	}

	public double getDouble(int arg0) throws SQLException
	{

		return 0;
	}

	public double getDouble(String arg0) throws SQLException
	{

		return 0;
	}

	public int getFetchDirection() throws SQLException
	{

		return 0;
	}

	public int getFetchSize() throws SQLException
	{

		return 0;
	}

	public float getFloat(int arg0) throws SQLException
	{

		return 0;
	}

	public float getFloat(String arg0) throws SQLException
	{

		return 0;
	}

	public int getInt(int arg0) throws SQLException
	{
		return getIntValue(arg0);
	}

	public int getInt(String arg0) throws SQLException
	{

		return 0;
	}

	public long getLong(int arg0) throws SQLException
	{
		return getLongValue(arg0);
	}

	public long getLong(String arg0) throws SQLException
	{

		return 0;
	}

	public ResultSetMetaData getMetaData() throws SQLException
	{
		return rsmd;
	}

	public Object getObject(int column) throws SQLException
	{
		return currentRow()[column - 1];
	}

	public Object getObject(String arg0) throws SQLException
	{

		return null;
	}

	@SuppressWarnings("unchecked")
	public Object getObject(int arg0, Map arg1) throws SQLException
	{

		return null;
	}

	@SuppressWarnings("unchecked")
	public Object getObject(String arg0, Map arg1) throws SQLException
	{

		return null;
	}

	public Ref getRef(int arg0) throws SQLException
	{

		return null;
	}

	public Ref getRef(String arg0) throws SQLException
	{

		return null;
	}

	public int getRow() throws SQLException
	{

		return 0;
	}

	public short getShort(int arg0) throws SQLException
	{

		return 0;
	}

	public short getShort(String arg0) throws SQLException
	{

		return 0;
	}

	public Statement getStatement() throws SQLException
	{

		return null;
	}

	public String getString(int arg0) throws SQLException
	{
		return getStringValue(arg0);
	}

	public String getString(String arg0) throws SQLException
	{

		return null;
	}

	public Time getTime(int arg0) throws SQLException
	{

		return null;
	}

	public Time getTime(String arg0) throws SQLException
	{

		return null;
	}

	public Time getTime(int arg0, Calendar arg1) throws SQLException
	{

		return null;
	}

	public Time getTime(String arg0, Calendar arg1) throws SQLException
	{

		return null;
	}

	public Timestamp getTimestamp(int column) throws SQLException
	{
		Object date = getObject(column);
		if (date instanceof java.util.Date)
		{
			long time = ((java.util.Date) date).getTime();
			return new Timestamp(time);
		}
		else
		{
			return (Timestamp) date;
		}
	}

	public Timestamp getTimestamp(String arg0) throws SQLException
	{

		return null;
	}

	public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException
	{

		return null;
	}

	public Timestamp getTimestamp(String arg0, Calendar arg1) throws SQLException
	{

		return null;
	}

	public int getType() throws SQLException
	{

		return 0;
	}

	public URL getURL(int arg0) throws SQLException
	{

		return null;
	}

	public URL getURL(String arg0) throws SQLException
	{

		return null;
	}

	public InputStream getUnicodeStream(int arg0) throws SQLException
	{

		return null;
	}

	public InputStream getUnicodeStream(String arg0) throws SQLException
	{

		return null;
	}

	public SQLWarning getWarnings() throws SQLException
	{

		return null;
	}

	public void insertRow() throws SQLException
	{

	}

	public boolean isAfterLast() throws SQLException
	{

		return false;
	}

	public boolean isBeforeFirst() throws SQLException
	{

		return false;
	}

	public boolean isFirst() throws SQLException
	{

		return false;
	}

	public boolean isLast() throws SQLException
	{

		return false;
	}

	public boolean last() throws SQLException
	{

		return false;
	}

	public void moveToCurrentRow() throws SQLException
	{

	}

	public void moveToInsertRow() throws SQLException
	{

	}

	public boolean next() throws SQLException
	{

		if (cursorIndex + 1 < rowData.size())
		{
			cursorIndex++;
			return true;
		}
		return false;
	}

	public boolean previous() throws SQLException
	{

		return false;
	}

	public void refreshRow() throws SQLException
	{

	}

	public boolean relative(int arg0) throws SQLException
	{

		return false;
	}

	public boolean rowDeleted() throws SQLException
	{

		return false;
	}

	public boolean rowInserted() throws SQLException
	{

		return false;
	}

	public boolean rowUpdated() throws SQLException
	{

		return false;
	}

	public void setFetchDirection(int arg0) throws SQLException
	{

	}

	public void setFetchSize(int arg0) throws SQLException
	{

	}

	public void updateArray(int arg0, Array arg1) throws SQLException
	{

	}

	public void updateArray(String arg0, Array arg1) throws SQLException
	{

	}

	public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException
	{

	}

	public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException
	{

	}

	public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException
	{

	}

	public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException
	{

	}

	public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException
	{

	}

	public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException
	{

	}

	public void updateBlob(int arg0, Blob arg1) throws SQLException
	{

	}

	public void updateBlob(String arg0, Blob arg1) throws SQLException
	{

	}

	public void updateBoolean(int arg0, boolean arg1) throws SQLException
	{

	}

	public void updateBoolean(String arg0, boolean arg1) throws SQLException
	{

	}

	public void updateByte(int arg0, byte arg1) throws SQLException
	{

	}

	public void updateByte(String arg0, byte arg1) throws SQLException
	{

	}

	public void updateBytes(int arg0, byte[] arg1) throws SQLException
	{

	}

	public void updateBytes(String arg0, byte[] arg1) throws SQLException
	{

	}

	public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException
	{

	}

	public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException
	{

	}

	public void updateClob(int arg0, Clob arg1) throws SQLException
	{

	}

	public void updateClob(String arg0, Clob arg1) throws SQLException
	{

	}

	public void updateDate(int arg0, Date arg1) throws SQLException
	{

	}

	public void updateDate(String arg0, Date arg1) throws SQLException
	{

	}

	public void updateDouble(int arg0, double arg1) throws SQLException
	{

	}

	public void updateDouble(String arg0, double arg1) throws SQLException
	{

	}

	public void updateFloat(int arg0, float arg1) throws SQLException
	{

	}

	public void updateFloat(String arg0, float arg1) throws SQLException
	{

	}

	public void updateInt(int arg0, int arg1) throws SQLException
	{

	}

	public void updateInt(String arg0, int arg1) throws SQLException
	{

	}

	public void updateLong(int arg0, long arg1) throws SQLException
	{

	}

	public void updateLong(String arg0, long arg1) throws SQLException
	{

	}

	public void updateNull(int arg0) throws SQLException
	{

	}

	public void updateNull(String arg0) throws SQLException
	{

	}

	public void updateObject(int arg0, Object arg1) throws SQLException
	{

	}

	public void updateObject(String arg0, Object arg1) throws SQLException
	{

	}

	public void updateObject(int arg0, Object arg1, int arg2) throws SQLException
	{

	}

	public void updateObject(String arg0, Object arg1, int arg2) throws SQLException
	{

	}

	public void updateRef(int arg0, Ref arg1) throws SQLException
	{

	}

	public void updateRef(String arg0, Ref arg1) throws SQLException
	{

	}

	public void updateRow() throws SQLException
	{

	}

	public void updateShort(int arg0, short arg1) throws SQLException
	{

	}

	public void updateShort(String arg0, short arg1) throws SQLException
	{

	}

	public void updateString(int arg0, String arg1) throws SQLException
	{

	}

	public void updateString(String arg0, String arg1) throws SQLException
	{

	}

	public void updateTime(int arg0, Time arg1) throws SQLException
	{

	}

	public void updateTime(String arg0, Time arg1) throws SQLException
	{

	}

	public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException
	{

	}

	public void updateTimestamp(String arg0, Timestamp arg1) throws SQLException
	{

	}

	public boolean wasNull() throws SQLException
	{

		return false;
	}

	
	private String getStringValue(int colIdx) throws SQLException
	{
		Object[] currentRow = currentRow();
		if (colIdx - 1 < currentRow.length)
		{
			return String.valueOf(currentRow[colIdx - 1]);
		}
		else
		{
			throw new SQLException("Error: requested value for non-existant column with index=" + (colIdx - 1));
		}
	}

	private int getIntValue(int colIdx) throws SQLException
	{
		return Integer.parseInt(getStringValue(colIdx));
	}

	private long getLongValue(int colIdx) throws SQLException
	{
		return Long.parseLong(getStringValue(colIdx));
	}

	private Object[] currentRow()
	{
		return rowData.get(cursorIndex);
	}

	
	public int getHoldability() throws SQLException
	{

		return 0;
	}

	
	public Reader getNCharacterStream(int columnIndex) throws SQLException
	{

		return null;
	}

	
	public Reader getNCharacterStream(String columnLabel) throws SQLException
	{

		return null;
	}

	
	public boolean isClosed() throws SQLException
	{

		return false;
	}

	
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException
	{

	}

	
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException
	{

	}

	
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException
	{

	}

	
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException
	{

	}

	
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException
	{

	}

	
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException
	{

	}

	
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException
	{

	}

	
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException
	{

	}

	
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException
	{

	}

	
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException
	{

	}

	
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException
	{

	}

	
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException
	{

	}

	
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException
	{

	}

	
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException
	{

	}

	
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
	{

	}

	
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException
	{

	}

	
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException
	{

	}

	
	public void updateClob(int columnIndex, Reader reader) throws SQLException
	{

	}

	
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException
	{

	}

	
	public void updateClob(String columnLabel, Reader reader) throws SQLException
	{

	}


   public RowId getRowId(int columnIndex) throws SQLException
   {
      return null;  
   }

   public RowId getRowId(String columnLabel) throws SQLException
   {
      return null;  
   }

   public void updateRowId(int columnIndex, RowId x) throws SQLException
   {
      
   }

   public void updateRowId(String columnLabel, RowId x) throws SQLException
   {
      
   }

   public void updateNString(int columnIndex, String nString) throws SQLException
   {
      
   }

   public void updateNString(String columnLabel, String nString) throws SQLException
   {
      
   }

   public void updateNClob(int columnIndex, NClob nClob) throws SQLException
   {
      
   }

   public void updateNClob(String columnLabel, NClob nClob) throws SQLException
   {
      
   }

   public NClob getNClob(int columnIndex) throws SQLException
   {
      return null;  
   }

   public NClob getNClob(String columnLabel) throws SQLException
   {
      return null;  
   }

   public SQLXML getSQLXML(int columnIndex) throws SQLException
   {
      return null;  
   }

   public SQLXML getSQLXML(String columnLabel) throws SQLException
   {
      return null;  
   }

   public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException
   {
      
   }

   public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException
   {
      
   }

   public String getNString(int columnIndex) throws SQLException
   {
      return null;  
   }

   public String getNString(String columnLabel) throws SQLException
   {
      return null;  
   }

   public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException
   {
      
   }

   public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
   {
      
   }

   public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException
   {
      
   }

   public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException
   {
      
   }

   public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException
   {
      
   }

   public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException
   {
      
   }

   public void updateNClob(int columnIndex, Reader reader) throws SQLException
   {
      
   }

   public void updateNClob(String columnLabel, Reader reader) throws SQLException
   {
      
   }

   public <T> T unwrap(Class<T> iface) throws SQLException
   {
      return null;  
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException
   {
      return false;  
   }
}
