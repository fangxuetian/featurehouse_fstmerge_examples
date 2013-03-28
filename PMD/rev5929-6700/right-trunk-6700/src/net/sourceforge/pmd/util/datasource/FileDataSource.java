
package net.sourceforge.pmd.util.datasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;



public class FileDataSource implements DataSource {
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
    private File file;

    
    public FileDataSource(File file) {
        this.file = file;
    }

    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    public String getNiceFileName(boolean shortNames, String inputFileName) {
        return glomName(shortNames, inputFileName, file);
    }

    private String glomName(boolean shortNames, String inputFileName, File file) {
        if (shortNames && inputFileName.indexOf(',') == -1) {
            if (new File(inputFileName).isDirectory()) {
                return trimAnyPathSep(file.getAbsolutePath().substring(inputFileName.length()));
            } else {
                if (inputFileName.indexOf(FILE_SEPARATOR.charAt(0)) == -1) {
                    return inputFileName;
                }
                return trimAnyPathSep(inputFileName.substring(inputFileName.lastIndexOf(System.getProperty("file.separator"))));
            }
        } 

        try {
            return file.getCanonicalFile().getAbsolutePath();
        } catch (Exception e) {
            return file.getAbsolutePath();
        }
    }

    private String trimAnyPathSep(String name) {

    	return name.startsWith(FILE_SEPARATOR) ?
            name.substring(1) :
            name;
    }
}
