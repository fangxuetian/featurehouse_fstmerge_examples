package net.sf.jabref.imports;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.AuthorList;
import net.sf.jabref.BibtexFields;


public class ScifinderImporter extends ImportFormat {

    
    public String getFormatName() {
    return "Scifinder";
    }

    
    public String getCLIId() {
      return "scifinder";
    }

    
    public boolean isRecognizedFormat(InputStream stream) throws IOException {

        BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));
        String str;
        int i=0;
        while (((str = in.readLine()) != null) && (i < 50)) {

			if (str.trim().equals("START_RECORD"))
				return true;

            i++;
        }

		return false;
    }

    
    public List<BibtexEntry> importEntries(InputStream stream) throws IOException {
    ArrayList<BibtexEntry> bibitems = new ArrayList<BibtexEntry>();
    StringBuffer sb = new StringBuffer();
    BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));
    String str;
    while ((str = in.readLine()) != null){
        sb.append(str);
    }

    String[] entries = sb.toString().split("START_RECORD");
    HashMap<String, String> hm = new HashMap<String, String>();
    for (int i = 1; i < entries.length; i++){
        String[] fields = entries[i].split("FIELD ");
        String Type = "";
        hm.clear(); 
        for (int j = 0; j < fields.length; j++)
        if (fields[j].indexOf(":") >= 0){
            String tmp[] = new String[2];
            tmp[0] = fields[j].substring(0, fields[j].indexOf(":"));
            tmp[1] = fields[j].substring(fields[j].indexOf(":") + 1).trim();
            if (tmp.length > 1){
            if (tmp[0].equals("Author")) hm.put("author", AuthorList.fixAuthor_lastNameFirst(tmp[1].replaceAll(";", " and ")));
            else if (tmp[0].equals("Title")) hm.put("title", tmp[1]);

            else if (tmp[0].equals("Journal Title")) hm.put("journal", tmp[1]);

            else if (tmp[0].equals("Volume")) hm.put("volume", tmp[1]);
            else if (tmp[0].equals("Page")) hm.put("pages", tmp[1]);
            else if (tmp[0].equals("Publication Year")) hm.put("year", tmp[1]);
            else if (tmp[0].equals("Abstract")) hm.put("abstract", tmp[1]);
            else if (tmp[0].equals("Supplementary Terms")) hm.put("keywords",
                                          tmp[1]);
            else if (tmp[0].equals("Document Type")) {
                                if (tmp[1].startsWith("Journal") || tmp[1].startsWith("Review"))
                                    Type = "article";
                                else if (tmp[1].equals("Dissertation"))
                                    Type = "phdthesis";
                                else
                                    Type = tmp[1];
                        }
            }
        }

        BibtexEntry b = new BibtexEntry(BibtexFields.DEFAULT_BIBTEXENTRY_ID, Globals
                        .getEntryType(Type)); 
        
        b.setField(hm);
        bibitems.add(b);

    }
    return bibitems;
    }
}


