package test;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class IngresMoneyTest
{

	private static final String tableName = "moneylengthtest";

	private static final String dropTable = "DROP TABLE \"" + tableName + "\"";

	private static final String createTable = "CREATE TABLE " + tableName + " (amt money)";

	private static void execute(Connection con, String sql, boolean printError)
	{
		Statement stmt = null;
		try
		{
			stmt = con.createStatement();
			System.out.println("Executing sql: " + sql);
			stmt.execute(sql);
		} catch (SQLException e)
		{
			if (printError)
			{
				e.printStackTrace();
			}
		} finally
		{
			if (stmt != null)
				try
				{
					stmt.close();
				} catch (SQLException e)
				{
				}
		}
	}

	private static void test(Connection con) throws Exception
	{
		execute(con, dropTable, false);
		execute(con, createTable, true);

		ResultSet rs = null;
		DatabaseMetaData md = con.getMetaData();
		rs = md.getColumns(null, null, tableName, null);
		while (rs.next())
		{
			String tableName = rs.getString(3); 
			String columnName = rs.getString(4); 
			int columnSize = rs.getInt(7); 
			int decimalDigits = rs.getInt(9); 

			System.out.println("tableName=" + tableName);
			System.out.println("columnName=" + columnName);
			System.out.println("columnSize=" + columnSize);
			System.out.println("decimalDigits=" + decimalDigits);
		}
	}

	
	public static void main(String[] args) throws Exception
	{
		Class.forName("com.ingres.jdbc.IngresDriver");
		String jdbcUrl = "jdbc:ingres://192.168.1.132:ii7/dbcopydest";
		Connection con = DriverManager.getConnection(jdbcUrl, "dbcopy", "password");
		test(con);
	}

}
