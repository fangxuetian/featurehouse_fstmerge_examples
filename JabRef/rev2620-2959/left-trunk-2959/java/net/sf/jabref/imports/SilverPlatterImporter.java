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

import java.util.regex.Pattern;
import net.sf.jabref.BibtexFields;


public class SilverPlatterImporter extends ImportFormat {

    
    public String getFormatName() {
        return "SilverPlatter";
    }

    
    public String getCLIId() {
      return "silverplatter";
    }

    
    public boolean isRecognizedFormat(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));

        
        
        
        
        Pattern pat1 = Pattern.compile("Record.*INSPEC.*");
        String str;
        while ((str = in.readLine()) != null){

            if (pat1.matcher(str).find())
                return false; 

            if ((str.length()>=5) && (str.substring(0, 5).equals("TI:  ")))
                return true;
        }
        return false;
    }

    
    public List<BibtexEntry> importEntries(InputStream stream) throws IOException {
        ArrayList<BibtexEntry> bibitems = new ArrayList<BibtexEntry>();
        BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));
        boolean isChapter = false;
        String str;
        StringBuffer sb = new StringBuffer();
        while ((str = in.readLine()) != null){
            if (str.length() < 2) sb.append("__::__").append(str);
            else
            sb.append("__NEWFIELD__").append(str);
        }
        in.close();
        String[] entries = sb.toString().split("__::__");
        String Type = "";
        HashMap<String, String> h = new HashMap<String, String>();
        entryLoop: for (int i = 0; i < entries.length; i++){
            if (entries[i].trim().length() < 6) continue entryLoop;
            
            h.clear();
            String[] fields = entries[i].split("__NEWFIELD__");
            fieldLoop: for (int j = 0; j < fields.length; j++){
                if (fields[j].length() < 6) continue fieldLoop;
                
                String s = fields[j];
                String f3 = s.substring(0, 2);
                String frest = s.substring(5);
                if (f3.equals("TI")) h.put("title", frest);
                
                else if (f3.equals("AU")){
                    if (frest.trim().endsWith("(ed)")){
                        String ed = frest.trim();
                        ed = ed.substring(0, ed.length() - 4);
                        h.put("editor", AuthorList.fixAuthor_lastNameFirst(ed.replaceAll(",-", ", ")
                                .replaceAll(";", " and ")));
                    }else h.put("author", AuthorList.fixAuthor_lastNameFirst(frest.replaceAll(
                                           ",-", ", ").replaceAll(";", " and ")));
                }else if (f3.equals("AB")) h.put("abstract", frest);
                else if (f3.equals("DE")){
                    String kw = frest.replaceAll("-;", ",").toLowerCase();
                    h.put("keywords", kw.substring(0, kw.length() - 1));
                }else if (f3.equals("SO")){
                    int m = frest.indexOf(".");
                    if (m >= 0){
                        String jr = frest.substring(0, m);
                        h.put("journal", jr.replaceAll("-", " "));
                        frest = frest.substring(m);
                        m = frest.indexOf(";");
                        if (m >= 5){
                            String yr = frest.substring(m - 5, m).trim();
                            h.put("year", yr);
                            frest = frest.substring(m);
                            m = frest.indexOf(":");
                            if (m >= 0){
                                String pg = frest.substring(m + 1).trim();
                                h.put("pages", pg);
                                h.put("volume", frest.substring(1, m));
                            }
                        }
                    }
                }else if (f3.equals("PB")){
                    int m = frest.indexOf(":");
                    if (m >= 0){
                        String jr = frest.substring(0, m);
                        h.put("publisher", jr.replaceAll("-", " ").trim());
                        frest = frest.substring(m);
                        m = frest.indexOf(", ");
                        if (m + 2 < frest.length()){
                            String yr = frest.substring(m + 2).trim();
                            try {
                                Integer.parseInt(yr);
                                h.put("year", yr);
                            } catch (NumberFormatException ex) {
                                
                                
                            }

                        }

                    }
                } else if (f3.equals("AF")) {
                    h.put("school", frest.trim());

                }else if (f3.equals("DT")){
                    frest = frest.trim();
                    if (frest.equals("Monograph")) Type = "book";
                    else if (frest.startsWith("Dissertation")) Type = "phdthesis";
                    else if (frest.toLowerCase().indexOf("journal") >= 0) Type = "article";
                    else if (frest.equals("Contribution") || frest.equals("Chapter")){
                        Type = "incollection";
                        
                        
                        isChapter = true;
                    }

                    else Type = frest.replaceAll(" ", "");
                }
            }

            if (isChapter) {
                Object titleO = h.get("title");
                if (titleO != null) {
                    String title = ((String)titleO).trim();
                    int inPos = title.indexOf("\" in ");
                    int pgPos = title.lastIndexOf(" ");
                    if (inPos > 1) h.put("title", title.substring(1, inPos));
                    if (pgPos > inPos) h.put("pages", title.substring(pgPos)
                                             .replaceAll("-", "--"));

                }

            }

            BibtexEntry b = new BibtexEntry(BibtexFields.DEFAULT_BIBTEXENTRY_ID, Globals
                                            .getEntryType(Type)); 
            
            b.setField(h);

            bibitems.add(b);

        }


        return bibitems;
    }
}


