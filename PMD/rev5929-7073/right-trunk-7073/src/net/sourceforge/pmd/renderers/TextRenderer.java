
package net.sourceforge.pmd.renderers;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Properties;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;


public class TextRenderer extends AbstractIncrementingRenderer {

    public static final String NAME = "text";

    public TextRenderer(Properties properties) {
	super(NAME, "Text format.", properties);
    }

    
    @Override
    public void start() throws IOException {
    }

    
    @Override
    public void renderFileViolations(Iterator<RuleViolation> violations) throws IOException {
	Writer writer = getWriter();
	StringBuffer buf = new StringBuffer();

	while (violations.hasNext()) {
	    buf.setLength(0);
	    RuleViolation rv = violations.next();
	    buf.append(rv.getFilename());
	    buf.append(':').append(Integer.toString(rv.getBeginLine()));
	    buf.append('\t').append(rv.getDescription()).append(PMD.EOL);
	    writer.write(buf.toString());
	}
    }

    
    @Override
    public void end() throws IOException {
	Writer writer = getWriter();
	StringBuffer buf = new StringBuffer(500);
	if (!errors.isEmpty()) {

	    for (Report.ProcessingError error : errors) {
		buf.setLength(0);
		buf.append(error.getFile());
		buf.append("\t-\t").append(error.getMsg()).append(PMD.EOL);
		writer.write(buf.toString());
	    }
	}

	for (Report.SuppressedViolation excluded : suppressed) {
	    buf.setLength(0);
	    buf.append(excluded.getRuleViolation().getRule().getName());
	    buf.append(" rule violation suppressed by ");
	    buf.append(excluded.suppressedByNOPMD() ? "//NOPMD" : "Annotation");
	    buf.append(" in ").append(excluded.getRuleViolation().getFilename()).append(PMD.EOL);
	    writer.write(buf.toString());
	}
    }

}
