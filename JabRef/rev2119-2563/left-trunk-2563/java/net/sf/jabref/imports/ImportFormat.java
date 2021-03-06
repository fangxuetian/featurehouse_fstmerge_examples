package net.sf.jabref.imports;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jabref.BibtexEntry;


public abstract class ImportFormat implements Comparable<ImportFormat> {

    private boolean isCustomImporter;
    
    
    public ImportFormat() {
      this.isCustomImporter = false;
    }

    
    public abstract boolean isRecognizedFormat(InputStream in) throws IOException;

    
    public abstract List<BibtexEntry> importEntries(InputStream in) throws IOException;


    
    public abstract String getFormatName();
    
    
    public String getExtensions() {
      return null;
    }
    
    
    public String getCLIId() {
      String id = getFormatName();
      StringBuffer result = new StringBuffer(id.length());
      for (int i = 0; i < id.length(); i++) {
        char c = id.charAt(i);
        if (Character.isLetterOrDigit(c)) {
          result.append(Character.toLowerCase(c));
        }
      }
      return result.toString();
    }
    
    
    public String getDescription() {
      return "No description available for " + getFormatName() + ".";
    }
    
    
    public final void setIsCustomImporter(boolean isCustomImporter) {
      this.isCustomImporter = isCustomImporter;
    }
    
    
    public final boolean getIsCustomImporter() {
      return this.isCustomImporter; 
    }
        
    
    public int hashCode() {
      return getFormatName().hashCode();
    }
    
    
    public boolean equals(Object o) {
      return o != null 
          && o instanceof ImportFormat
          && ((ImportFormat)o).getIsCustomImporter() == getIsCustomImporter() 
          && ((ImportFormat)o).getFormatName().equals(getFormatName());
    }
    
    
    public String toString() {
      return getFormatName();
    }
    
    
    public int compareTo(ImportFormat importer) {
      int result = 0;
      if (getIsCustomImporter() == importer.getIsCustomImporter()) {
        result = getFormatName().compareTo(importer.getFormatName());
      } else {
        result = getIsCustomImporter() ? 1 : -1;
      }
      return result;
    }
}
