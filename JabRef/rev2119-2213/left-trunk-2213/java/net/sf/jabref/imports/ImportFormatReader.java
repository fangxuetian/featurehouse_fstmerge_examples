
package net.sf.jabref.imports;

import java.io.*;
import net.sf.jabref.plugin.core.JabRefPlugin;
import net.sf.jabref.plugin.core.generated._JabRefPlugin.ImportFormatExtension;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.jabref.*;
import net.sf.jabref.plugin.PluginCore;


public class ImportFormatReader {

    public static String BIBTEX_FORMAT = "BibTeX";

  
  private SortedSet<ImportFormat> formats = new TreeSet<ImportFormat>();

  public ImportFormatReader() {
    super();
  }

  public void resetImportFormats() {
    formats.clear();
    
    
    
    formats.add(new CsaImporter());   
    formats.add(new IsiImporter());
    formats.add(new EndnoteImporter());
    formats.add(new BibteXMLImporter());
    formats.add(new BiblioscapeImporter());
    formats.add(new SixpackImporter());
    formats.add(new InspecImporter());
    formats.add(new ScifinderImporter());
    formats.add(new OvidImporter());
    formats.add(new RisImporter());
    formats.add(new JstorImporter());
    formats.add(new SilverPlatterImporter());
    formats.add(new BiomailImporter());
    formats.add(new RepecNepImporter());  
    formats.add(new PdfXmpImporter());
    formats.add(new CopacImporter());
    formats.add(new MsBibImporter());

    
    JabRefPlugin jabrefPlugin = JabRefPlugin.getInstance(PluginCore.getManager());
	if (jabrefPlugin != null){
		for (ImportFormatExtension ext : jabrefPlugin.getImportFormatExtensions()){
			ImportFormat importFormat = ext.getImportFormat();
			if (importFormat != null){
				formats.add(importFormat);
			}
		}
	}
	
	
    for (CustomImportList.Importer importer : Globals.prefs.customImports){
       try {
        ImportFormat imFo = importer.getInstance();
        formats.add(imFo);
      } catch(Exception e) {
        System.err.println("Could not instantiate " + importer.getName() + " importer, will ignore it. Please check if the class is still available.");
        e.printStackTrace();
      }      
    }
  }
  
  
  public ImportFormat getByCliId(String cliId) {
    for (ImportFormat format : formats){
      if (format.getCLIId().equals(cliId)) {
        return format;
      }
    }
    return null;
  }
  
  public List<BibtexEntry> importFromStream(String format, InputStream in)
    throws IOException {
    ImportFormat importer = getByCliId(format);

    if (importer == null)
      throw new IllegalArgumentException("Unknown import format: " + format);

    List<BibtexEntry> res = importer.importEntries(in);

    
    if (res != null)
      purgeEmptyEntries(res);

    return res;
  }

  public List<BibtexEntry> importFromFile(String format, String filename)
    throws IOException {
    ImportFormat importer = getByCliId(format);

    if (importer == null)
      throw new IllegalArgumentException("Unknown import format: " + format);

    return importFromFile(importer, filename);
  }

    public List<BibtexEntry> importFromFile(ImportFormat importer, String filename) throws IOException {
        List<BibtexEntry> result = null;
        InputStream stream = null;

        try {
            File file = new File(filename);
            stream = new FileInputStream(file);

            if (!importer.isRecognizedFormat(stream))
                throw new IOException(Globals.lang("Wrong file format"));

            stream = new FileInputStream(file);

            result = importer.importEntries(stream);
        } finally {
            try {
                if (stream != null)
                    stream.close();
            } catch (IOException ex) {
                throw ex;
            }
        }

        return result;
    }

  public static BibtexDatabase createDatabase(List<BibtexEntry> bibentries) {
    purgeEmptyEntries(bibentries);

    BibtexDatabase database = new BibtexDatabase();

    for (Iterator<BibtexEntry> i = bibentries.iterator(); i.hasNext();) {
      BibtexEntry entry = i.next();

      try {
        entry.setId(Util.createNeutralId());
        database.insertEntry(entry);
      } catch (KeyCollisionException ex) {
        
        System.err.println("KeyCollisionException [ addBibEntries(...) ]");
      }
    }

    return database;
  }

  
  public SortedSet<ImportFormat> getCustomImportFormats() {
    SortedSet<ImportFormat> result = new TreeSet<ImportFormat>();
    for (ImportFormat format : formats){
      if (format.getIsCustomImporter()) {
        result.add(format);  
      }
    }
    return result;
  }
  
  
  public SortedSet<ImportFormat> getBuiltInInputFormats() {
		SortedSet<ImportFormat> result = new TreeSet<ImportFormat>();
		for (ImportFormat format : formats) {
			if (!format.getIsCustomImporter()) {
				result.add(format);
			}
		}
		return result;
	}
  
  
  public SortedSet<ImportFormat> getImportFormats() {
    return this.formats;
  }

  
  public String getImportFormatList() {
    StringBuffer sb = new StringBuffer();

    for (ImportFormat imFo : formats){
      int pad = Math.max(0, 14 - imFo.getFormatName().length());
      sb.append("  ");
      sb.append(imFo.getFormatName());

      for (int j = 0; j < pad; j++)
        sb.append(" ");

      sb.append(" : ");
      sb.append(imFo.getCLIId());
      sb.append("\n");
    }

    String res = sb.toString();

    return res; 
  }


    
    public static String expandAuthorInitials(String name) {
      String[] authors = name.split(" and ");
      StringBuffer sb = new StringBuffer();
      for (int i=0; i<authors.length; i++) {
          if (authors[i].indexOf(", ") >= 0) {
              String[] names = authors[i].split(", ");
              if (names.length > 0) {
                  sb.append(names[0]);
                  if (names.length > 1)
                    sb.append(", ");
              }
              for (int j=1; j<names.length; j++) {
                  sb.append(expandAll(names[j]));
              }

          } else {
              String[] names = authors[i].split(" ");
              if (names.length > 0) {
                  sb.append(expandAll(names[0]));
              }
              for (int j=1; j<names.length; j++) {
                  sb.append(" ");
                  sb.append(names[j]);
              }
          }
          if (i < authors.length-1)
              sb.append(" and ");
      }

      return sb.toString().trim();
  }
  



    public static String expandAll(String s) {
        
        
        if (s.length() == 0)
          return s;
        
        if ((s.length() == 1) && (Character.isLetter(s.charAt(0)) &&
                Character.isUpperCase(s.charAt(0))))
          return s+".";
        StringBuffer sb = new StringBuffer();
        char c = s.charAt(0), d = 0;
        for (int i=1; i<s.length(); i++) {
            d = s.charAt(i);
            if (Character.isLetter(c) && Character.isUpperCase(c) &&
                    Character.isLetter(d) && Character.isUpperCase(d)) {
                sb.append(c);
                sb.append(". ");
            }
            else {
                sb.append(c);
            }
            c = d;
        }
        if (Character.isLetter(c) && Character.isUpperCase(c) &&
              Character.isLetter(d) && Character.isUpperCase(d)) {
            sb.append(c);
            sb.append(". ");
        }
        else {
            sb.append(c);
        }
        return sb.toString().trim();
    }


  static File checkAndCreateFile(String filename) {
    File f = new File(filename);

    if (!f.exists() && !f.canRead() && !f.isFile()) {
      System.err.println("Error " + filename
        + " is not a valid file and|or is not readable.");
      Globals.logger("Error " + filename + " is not a valid file and|or is not readable.");

      return null;
    } else

      return f;
  }

  
  
  
  public static void setIfNecessary(BibtexEntry be, String field, String content) {
    if (!content.equals(""))
      be.setField(field, content);
  }



    public static Reader getReader(File f, String encoding)
      throws IOException {
      InputStreamReader reader;
      reader = new InputStreamReader(new FileInputStream(f), encoding);

      return reader;
    }

  public static Reader getReaderDefaultEncoding(InputStream in)
    throws IOException {
    InputStreamReader reader;
    reader = new InputStreamReader(in, Globals.prefs.get("defaultEncoding"));

    return reader;
  }

  public static BibtexDatabase import_File(String format, String filename)
    throws IOException {
    BibtexDatabase database = null;
    List<BibtexEntry> bibentries = null;
    File f = new File(filename);

    if (!f.exists())
      throw new IOException(Globals.lang("File not found") + ": " + filename);

    try {
      bibentries = Globals.importFormatReader.importFromFile(format, filename);
    } catch (IllegalArgumentException ex) {
      throw new IOException(Globals.lang("Could not resolve import format") + " '"
        + format + "'");
    }

    if (bibentries == null)
      throw new IOException(Globals.lang("Import failed"));

    
    purgeEmptyEntries(bibentries);

    
    database = new BibtexDatabase();

    Iterator<BibtexEntry> it = bibentries.iterator();

    while (it.hasNext()) {
      BibtexEntry entry = it.next();

      try {
        entry.setId(Util.createNeutralId());
        database.insertEntry(entry);
      } catch (KeyCollisionException ex) {
        
        System.err.println("KeyCollisionException [ addBibEntries(...) ]");
      }
    }

    return database;
  }

  
  public static void purgeEmptyEntries(List<BibtexEntry> entries) {
    for (Iterator<BibtexEntry> i = entries.iterator(); i.hasNext();) {
      BibtexEntry entry = i.next();

      
      Object[] o = entry.getAllFields();

      
      if (o.length == 0)
        i.remove();
    }
  }

  
  public Object[] importUnknownFormat(String filename) {
    Object entryList = null;
    String usedFormat = null;
    int bestResult = 0;

          

    
    for (ImportFormat imFo : getImportFormats()){
			try {
				
				List<BibtexEntry> entries = importFromFile(imFo, filename);

				if (entries != null)
					purgeEmptyEntries(entries);

				int entryCount = ((entries != null) ? entries.size() : 0);

				
				
				if (entryCount > bestResult) {
					bestResult = entryCount;
					usedFormat = imFo.getFormatName();
					entryList = entries;
					
				}
			} catch (Exception e) {
			}
		}
      System.out.println("Used format: "+usedFormat);
    
    if (entryList == null) {
	try {
	    ParserResult pr = OpenDatabaseAction.loadDatabase(new File(filename), Globals.prefs.get("defaultEncoding"));
	    if ((pr.getDatabase().getEntryCount() > 0)
		|| (pr.getDatabase().getStringCount() > 0)) {
		entryList = pr;
        pr.setFile(new File(filename));
		usedFormat = BIBTEX_FORMAT;
	    }
	} catch (Throwable ex) {
	    
	}
	
    }

    return new Object[] { usedFormat, entryList };
  }
}
