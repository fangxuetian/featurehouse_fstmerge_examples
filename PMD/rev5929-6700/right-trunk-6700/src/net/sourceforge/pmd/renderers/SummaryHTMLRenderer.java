
package net.sourceforge.pmd.renderers;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.pmd.PMD;


public class SummaryHTMLRenderer extends AbstractAccumulatingRenderer {

    public static final String NAME = "summaryhtml";

    public static final String LINK_PREFIX = HTMLRenderer.LINK_PREFIX;
    public static final String LINE_PREFIX = HTMLRenderer.LINE_PREFIX;

    public SummaryHTMLRenderer(Properties properties) {
	super(NAME, "Summary HTML format.", properties);

	
	super.defineProperty(HTMLRenderer.LINK_PREFIX, "Path to HTML source.");
	super.defineProperty(HTMLRenderer.LINE_PREFIX, "Prefix for line number anchor in the source file.");
    }

    
    @Override
    public void end() throws IOException {
	writer.write("<html><head><title>PMD</title></head><body>" + PMD.EOL);
	renderSummary();
	writer.write("<h2><center>Detail</h2></center>");
	writer.write("<table align=\"center\" cellspacing=\"0\" cellpadding=\"3\"><tr>" + PMD.EOL);
	new HTMLRenderer(properties).renderBody(writer, report);
	writer.write("</table></body></html>" + PMD.EOL);
    }

    
    public void renderSummary() throws IOException {
	StringBuffer buf = new StringBuffer(500);
	buf.append("<h2><center>Summary</h2></center>");
	buf.append("<table align=\"center\" cellspacing=\"0\" cellpadding=\"3\">");
	buf.append("<th>Rule name</th>");
	buf.append("<th>Number of violations</th>");
	writer.write(buf.toString());
	Map<String, Integer> summary = report.getSummary();
	for (Map.Entry<String, Integer> entry : summary.entrySet()) {
	    String ruleName = entry.getKey();
	    buf.setLength(0);
	    buf.append("<tr>");
	    buf.append("<td>" + ruleName + "</td>");
	    buf.append("<td align=center>" + entry.getValue().intValue() + "</td>");
	    buf.append("</tr>");
	    writer.write(buf.toString());
	}
	writer.write("</table>");
    }
}
