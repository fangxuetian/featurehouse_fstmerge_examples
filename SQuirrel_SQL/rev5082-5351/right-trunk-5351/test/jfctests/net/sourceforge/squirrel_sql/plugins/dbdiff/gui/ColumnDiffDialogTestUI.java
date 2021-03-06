
package net.sourceforge.squirrel_sql.plugins.dbdiff.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import net.sourceforge.squirrel_sql.client.ApplicationArguments;
import net.sourceforge.squirrel_sql.fw.FwTestUtil;
import net.sourceforge.squirrel_sql.fw.sql.ISQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.fw.sql.TableColumnInfo;
import net.sourceforge.squirrel_sql.plugins.dbdiff.ColumnDifference;

import static java.sql.Types.*;

public class ColumnDiffDialogTestUI {

    public static void main(String[] args) throws Exception {
        ApplicationArguments.initialize(new String[] {});
        
        ISQLDatabaseMetaData md = FwTestUtil.getEasyMockSQLMetaData("oracle", "jdbc:oracle");
        ColumnDifference diff = new ColumnDifference();
        TableColumnInfo column1 = FwTestUtil.getBigintColumnInfo(md, true);
        TableColumnInfo column2 = FwTestUtil.getVarcharColumnInfo(md, true, 100);
        diff.setColumns(column1, column2);
        diff.execute();

        ColumnDifference diff2 = new ColumnDifference();
        TableColumnInfo column3 = FwTestUtil.getVarcharColumnInfo(md, true, 200);
        TableColumnInfo column4 = FwTestUtil.getVarcharColumnInfo(md, true, 100);
        diff2.setColumns(column3, column4);
        diff2.execute();
        
        ColumnDifference diff3 = new ColumnDifference();
        TableColumnInfo column5 = 
            FwTestUtil.getTableColumnInfo(md, "LongColumnNameThatIsUnreal", VARCHAR, 100, 0, false);
        diff3.setColumn1(column5);
        diff3.setCol2Exists(false);
        
        final ArrayList<ColumnDifference> diffs = new ArrayList<ColumnDifference>();
        diffs.add(diff);
        diffs.add(diff2);
        diffs.add(diff3);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame();
                ColumnDiffDialog cdd = 
                    new ColumnDiffDialog(f, true);
                cdd.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        System.exit(0);
                    }
                });
                cdd.setSession1Label("Oracle1");
                cdd.setSession2Label("Oracle2");
                cdd.setColumnDifferences(diffs);
                cdd.setVisible(true);
            }
        });        
    }
    
}
