package net.sourceforge.pmd.renderers;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.Report;


public abstract class OnTheFlyRenderer extends AbstractRenderer {

    protected List<Report.ProcessingError> errors = new LinkedList<Report.ProcessingError>();

    protected List<Report.SuppressedViolation> suppressed = new LinkedList<Report.SuppressedViolation>();

    
    public void render(Writer writer, Report report) throws IOException {
        setWriter(writer);
        start();
        renderFileReport(report);
        end();
    }

    
    public void renderFileReport(Report report) throws IOException {
        Iterator<RuleViolation> violations = report.iterator();
        if (violations.hasNext()) {
            renderFileViolations(violations);
            getWriter().flush();
        }

        
        for (Iterator<Report.ProcessingError> i = report.errors(); i.hasNext();) {
            errors.add(i.next());
        }

        if (showSuppressedViolations) {
            suppressed.addAll(report.getSuppressedRuleViolations());
        }
    }

    
    public abstract void start() throws IOException;

    
    public abstract void renderFileViolations(Iterator<RuleViolation> violations) throws IOException;

    
    public abstract void end() throws IOException;

}
