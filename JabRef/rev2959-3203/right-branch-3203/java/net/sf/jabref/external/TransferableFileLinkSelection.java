package net.sf.jabref.external;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.GUIGlobals;
import net.sf.jabref.Util;
import net.sf.jabref.BasePanel;
import net.sf.jabref.gui.FileListTableModel;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.ArrayList;


public class TransferableFileLinkSelection implements Transferable {

    List<File> fileList = new ArrayList<File>();

    public TransferableFileLinkSelection(BasePanel panel, BibtexEntry[] selection) {
        String s = selection[0].getField(GUIGlobals.FILE_FIELD);
        FileListTableModel tm = new FileListTableModel();
        if (s != null)
            tm.setContent(s);
        if (tm.getRowCount() > 0) {
            
            String dir = panel.metaData().getFileDirectory(GUIGlobals.FILE_FIELD);
            
            String fileDir = panel.metaData().getFileDirectory(GUIGlobals.FILE_FIELD);
            
            String[] dirs;
            if (panel.metaData().getFile() != null) {
                String databaseDir = panel.metaData().getFile().getParent();
                dirs = new String[] { dir, fileDir, databaseDir };
            }
            else
                dirs = new String[] { dir, fileDir };
            File expLink = Util.expandFilename(tm.getEntry(0).getLink(), dirs);
            fileList.add(expLink);

        }

    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] {DataFlavor.javaFileListFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        System.out.println("Query: "+dataFlavor.getHumanPresentableName()+" , "+
            dataFlavor.getDefaultRepresentationClass()+" , "+dataFlavor.getMimeType());
        return dataFlavor.equals(DataFlavor.javaFileListFlavor)
                || dataFlavor.equals(DataFlavor.stringFlavor);
    }

    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        
            return fileList;
        
        
    }
    
}
