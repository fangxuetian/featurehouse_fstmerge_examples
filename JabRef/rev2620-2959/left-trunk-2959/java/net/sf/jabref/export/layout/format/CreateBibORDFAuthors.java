




















package net.sf.jabref.export.layout.format;

import net.sf.jabref.export.layout.LayoutFormatter;



public class CreateBibORDFAuthors implements LayoutFormatter
{
    

    public String format(String fieldText) {
    	
    	








        StringBuffer sb = new StringBuffer(100);

        if (fieldText.indexOf(" and ") == -1)
        {
          singleAuthor(sb, fieldText, 1);
        }
        else
        {
            String[] names = fieldText.split(" and ");
            for (int i=0; i<names.length; i++)
            {
              singleAuthor(sb, names[i], (i+1));
              if (i < names.length -1)
                sb.append("\n");
            }
        }



        fieldText = sb.toString();

        return fieldText;
    }

    
    protected void singleAuthor(StringBuffer sb, String author, int position) {
        sb.append("<bibo:contribution>\n");
        sb.append("  <bibo:Contribution>\n");
        sb.append("    <bibo:role rdf:resource=\"http://purl.org/ontology/bibo/roles/author\" />\n");
        sb.append("    <bibo:contributor><foaf:Person foaf:name=\"" + author + "\"/></bibo:contributor>\n");
        sb.append("    <bibo:position>" + position + "</bibo:position>\n");
        sb.append("  </bibo:Contribution>\n");
        sb.append("</bibo:contribution>\n");
    }
}



