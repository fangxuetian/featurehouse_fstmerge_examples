
package adhoc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.client.ApplicationArguments;

public class OracleSynonymTest {

    String jdbcDriver = "oracle.jdbc.OracleDriver";
    
    String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:csuite";
    
    String user = "c2";
    
    String pass = "password";
    
    Connection con = null;
    
    String catalog = null;
    String schema = "C2";
    String synonym = "FOO";
    
    public OracleSynonymTest() throws Exception {
        init();
    }
    
    public void init() throws Exception {
        ApplicationArguments.initialize(new String[0]);
        Class.forName(jdbcDriver);
        con = DriverManager.getConnection(jdbcUrl,user, pass);
    }
    

    
    public void doTest() throws SQLException {
        ResultSet rs = con.getMetaData().getColumns(catalog, schema, synonym, "%");
        while (rs.next()) {
            System.out.println("Column="+rs.getString("COLUMN_NAME"));
        }
    }
    
    
    public void shutdown() throws SQLException {
        con.close();
    }
    
    
    public static void main(String[] args) throws Exception {
        
        OracleSynonymTest test = new OracleSynonymTest();
        
        test.doTest();
        
        test.shutdown();
    }

}
