package net.sf.jabref.imports;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jabref.BibtexEntry;
import net.sf.jabref.Globals;
import net.sf.jabref.Util;
import net.sf.jabref.AuthorList;


public class OvidImporter extends ImportFormat {

    public static Pattern ovid_src_pat = Pattern
    .compile("Source ([ \\w&\\-,:]+)\\.[ ]+([0-9]+)\\(([\\w\\-]+)\\):([0-9]+\\-?[0-9]+?)\\,.*([0-9][0-9][0-9][0-9])");

    public static Pattern ovid_src_pat_no_issue = Pattern
    .compile("Source ([ \\w&\\-,:]+)\\.[ ]+([0-9]+):([0-9]+\\-?[0-9]+?)\\,.*([0-9][0-9][0-9][0-9])");

    public static Pattern ovid_src_pat_2 = Pattern.compile(
            "([ \\w&\\-,]+)\\. Vol ([0-9]+)\\(([\\w\\-]+)\\) ([A-Za-z]+) ([0-9][0-9][0-9][0-9]), ([0-9]+\\-?[0-9]+)");

    public static Pattern incollection_pat = Pattern.compile(
            "(.+)\\(([0-9][0-9][0-9][0-9])\\)\\. ([ \\w&\\-,:]+)\\.[ ]+\\(pp. ([0-9]+\\-?[0-9]+?)\\).[A-Za-z0-9, ]+pp\\. "
            +"([\\w, ]+): ([\\w, ]+)");
    public static Pattern book_pat = Pattern.compile(
                "\\(([0-9][0-9][0-9][0-9])\\)\\. [A-Za-z, ]+([0-9]+) pp\\. ([\\w, ]+): ([\\w, ]+)");

    
    


    
    public String getFormatName() {
    return "Ovid";
    }

    
    public String getCLIId() {
      return "ovid";
    }
    
    
    public boolean isRecognizedFormat(InputStream in) throws IOException {
    return true;
    }

    
    public List importEntries(InputStream stream) throws IOException {
    ArrayList bibitems = new ArrayList();
    StringBuffer sb = new StringBuffer();
    BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));
    String line;
    while ((line = in.readLine()) != null){
        if (line.length() > 0 && line.charAt(0) != ' '){
        sb.append("__NEWFIELD__");
        }
        sb.append(line);
        sb.append('\n');
    }

    String items[] = sb.toString().split("<[0-9]+>");

    for (int i = 1; i < items.length; i++){
        HashMap h = new HashMap();
        String[] fields = items[i].split("__NEWFIELD__");
        for (int j = 0; j < fields.length; j++){
            int linebreak = fields[j].indexOf('\n');
            String fieldName = fields[j].substring(0, linebreak).trim();
            String content = fields[j].substring(linebreak).trim();

            
            boolean isAuthor = fieldName.indexOf("Author") == 0
                && fieldName.indexOf("Author Keywords") == -1
                && fieldName.indexOf("Author e-mail") == -1;

            
            
            if (!isAuthor && content.endsWith("."))
                    content = content.substring(0, content.length()-1);
            
            if (isAuthor) {

                h.put("author", content);


        }else if (fieldName.indexOf("Title") == 0) {
                content = content.replaceAll("\\[.+\\]", "").trim();
                if (content.endsWith("."))
                    content = content.substring(0, content.length()-1);
                h.put("title", content);
        }

        else if (fieldName.indexOf("Chapter Title") == 0) h.put("chaptertitle", content);

        
        
        
        
        else if (fieldName.indexOf("Source") == 0){
                Matcher matcher;
            if ((matcher = ovid_src_pat.matcher(content)).find()) {
            h.put("journal", matcher.group(1));
            h.put("volume", matcher.group(2));
            h.put("issue", matcher.group(3));
            h.put("pages", matcher.group(4));
            h.put("year", matcher.group(5));
            } else if ((matcher = ovid_src_pat_no_issue.matcher(content)).find()) {
                h.put("journal", matcher.group(1));
                h.put("volume", matcher.group(2));
                h.put("pages", matcher.group(3));
                h.put("year", matcher.group(4));
            } else if ((matcher = ovid_src_pat_2.matcher(content)).find()) {

                h.put("journal", matcher.group(1));
                h.put("volume", matcher.group(2));
                h.put("issue", matcher.group(3));
                h.put("month", matcher.group(4));
                h.put("year", matcher.group(5));
                h.put("pages", matcher.group(6));

            } else if ((matcher = incollection_pat.matcher(content)).find()) {
                h.put("editor", matcher.group(1).replaceAll(" \\(Ed\\)", ""));
                h.put("year", matcher.group(2));
                h.put("booktitle", matcher.group(3));
                h.put("pages", matcher.group(4));
                h.put("address", matcher.group(5));
                h.put("publisher", matcher.group(6));
            } else if ((matcher = book_pat.matcher(content)).find()) {
                h.put("year", matcher.group(1));
                h.put("pages", matcher.group(2));
                h.put("address", matcher.group(3));
                h.put("publisher", matcher.group(4));

            }
            
            if (h.get("pages") != null) {
                h.put("pages", ((String)h.get("pages")).replaceAll("-", "--"));
            }

        } else if (fieldName.equals("Abstract")) {
                h.put("abstract", content);

        } else if (fieldName.equals("Publication Type")) {
             if (content.indexOf("Book") >= 0)
                h.put("entrytype", "book");
             else if (content.indexOf("Journal") >= 0)
                h.put("entrytype", "article");
             else if (content.indexOf("Conference Paper") >= 0)
                h.put("entrytype", "inproceedings");
        }
        }

        
        
        String auth = (String)h.get("author");
        if ((auth != null) && (auth.indexOf(" [Ed]") >= 0)) {
            h.remove("author");
            h.put("editor", auth.replaceAll(" \\[Ed\\]", ""));
        }

        
        auth = (String)h.get("author");
        if (auth != null)
            h.put("author", fixNames(auth));
        auth = (String)h.get("editor");
        if (auth != null)
            h.put("editor", fixNames(auth));



        
        String entryType = h.containsKey("entrytype") ? (String)h.get("entrytype") : "other";
        h.remove("entrytype");
        if (entryType.equals("book")) {
            if (h.containsKey("chaptertitle")) {
                
                entryType = "incollection";
                
                h.put("title", h.remove("chaptertitle"));
            }
        }
        BibtexEntry b = new BibtexEntry(Util.createNeutralId(), Globals.getEntryType(entryType));
        b.setField(h);

        bibitems.add(b);

    }

    return bibitems;
    }

    
    private String fixNames(String content) {
        String names;
        if (content.indexOf(";") > 0){ 
            names = content.replaceAll("[^\\.A-Za-z,;\\- ]", "").replaceAll(";", " and");
        } else if (content.indexOf("  ") > 0) {
            String[] sNames = content.split("  ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sNames.length; i++) {
                if (i > 0) sb.append(" and ");
                sb.append(sNames[i].replaceFirst(" ", ", "));
            }
            names = sb.toString();
        } else
            names = content;
        return AuthorList.fixAuthor_lastNameFirst(names);
    }

}


