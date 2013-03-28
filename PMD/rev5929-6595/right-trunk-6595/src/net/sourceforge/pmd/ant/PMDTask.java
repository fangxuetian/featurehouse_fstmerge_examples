
package net.sourceforge.pmd.ant;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;
import java.util.logging.Level;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.SimpleRuleSetNameMapper;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.renderers.AbstractRenderer;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.ClasspathClassLoader;
import net.sourceforge.pmd.util.datasource.DataSource;
import net.sourceforge.pmd.util.datasource.FileDataSource;
import net.sourceforge.pmd.util.log.AntLogHandler;
import net.sourceforge.pmd.util.log.ScopedLogHandlersManager;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class PMDTask extends Task {

    private Path classpath;
    private Path auxClasspath;
    private List<Formatter> formatters = new ArrayList<Formatter>();
    private List<FileSet> filesets = new ArrayList<FileSet>();
    private RulePriority minPriority = RulePriority.LOW;
    private boolean shortFilenames;
    private String ruleSetFiles;
    private String encoding = System.getProperty("file.encoding");
    private boolean failOnError;
    private boolean failOnRuleViolation;
	private int maxRuleViolations = 0;
    private String targetJDK = "1.5";
    private String failuresPropertyName;
    private String suppressMarker = PMD.SUPPRESS_MARKER;
    private int cpus = Runtime.getRuntime().availableProcessors();
    private final Collection<RuleSetWrapper> nestedRules = new ArrayList<RuleSetWrapper>();

    public void setShortFilenames(boolean value) {
        this.shortFilenames = value;
    }

    public void setTargetJDK(String value) {
        this.targetJDK = value;
    }

    public void setSuppressMarker(String value) {
        this.suppressMarker = value;
    }

    public void setFailOnError(boolean fail) {
        this.failOnError = fail;
    }

    public void setFailOnRuleViolation(boolean fail) {
        this.failOnRuleViolation = fail;
    }

	public void setMaxRuleViolations(int max) {
	    if (max >= 0) {
		    this.maxRuleViolations = max;
		    this.failOnRuleViolation = true;
		}
	}


    public void setRuleSetFiles(String ruleSetFiles) {
        this.ruleSetFiles = ruleSetFiles;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setCpus(int cpus) {
        this.cpus = cpus;
    }

    public void setFailuresPropertyName(String failuresPropertyName) {
        this.failuresPropertyName = failuresPropertyName;
    }

    public void setMinimumPriority(int minPriority) {
        this.minPriority = RulePriority.valueOf(minPriority);
    }

    public void addFileset(FileSet set) {
        filesets.add(set);
    }

    public void addFormatter(Formatter f) {
        formatters.add(f);
    }

    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    public Path getClasspath() {
        return classpath;
    }

    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        return classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createLongClasspath().setRefid(r);
    }

    public void setAuxClasspath(Path auxClasspath) {
        this.auxClasspath = auxClasspath;
    }

    public Path getAuxClasspath() {
        return auxClasspath;
    }

    public Path createAuxClasspath() {
        if (auxClasspath == null) {
            auxClasspath = new Path(getProject());
        }
        return auxClasspath.createPath();
    }

    public void setAuxClasspathRef(Reference r) {
        createLongAuxClasspath().setRefid(r);
    }

    private class AntTaskNameMapper extends SimpleRuleSetNameMapper {
        public AntTaskNameMapper(String s) {
            super(s);
        }

        @Override
        protected void check(String name) {
            if (name.indexOf("rulesets") == -1 && nameMap.containsKey(name)) {
                append(nameMap.get(name));
            } else {
                
                append(getProject().replaceProperties(name));
            }
        }

    }

    private void doTask(){
        ruleSetFiles = new AntTaskNameMapper(ruleSetFiles).getRuleSets();

        ClassLoader classLoader;
        if (classpath == null) {
            log("Using the normal ClassLoader", Project.MSG_VERBOSE);
            classLoader = getClass().getClassLoader();
        } else {
            log("Using the AntClassLoader", Project.MSG_VERBOSE);
            classLoader = new AntClassLoader(getProject(), classpath);
        }
        
        String extraPath = getProject().getBaseDir().toString();
        if (auxClasspath != null) {
            log("Using auxclasspath: " + auxClasspath, Project.MSG_VERBOSE);
            extraPath = auxClasspath.toString() + File.pathSeparator + extraPath;
        }
        try {
            classLoader = new ClasspathClassLoader(extraPath, classLoader);
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage());
        }

        RuleSetFactory ruleSetFactory = new RuleSetFactory();
        ruleSetFactory.setClassLoader(classLoader);
        for (Formatter formatter: formatters) {
            log("Sending a report to " + formatter, Project.MSG_VERBOSE);
            formatter.start(getProject().getBaseDir().toString());
        }

        try {
            
            RuleSets rules;
            ruleSetFactory.setMinimumPriority(minPriority);
            ruleSetFactory.setWarnDeprecated(true);
            rules = ruleSetFactory.createRuleSets(ruleSetFiles);
            ruleSetFactory.setWarnDeprecated(false);
            logRulesUsed(rules);
        } catch (RuleSetNotFoundException e) {
            throw new BuildException(e.getMessage());
        }

        List<LanguageVersion> languageVersions = new ArrayList<LanguageVersion>();
        Language language = Language.JAVA;
        LanguageVersion languageVersion = language.getVersion(targetJDK);
        if (languageVersion == null) {
        	languageVersion = language.getDefaultVersion();
        }
        languageVersions.add(languageVersion);
        log("Targeting " + languageVersion.getShortName(), Project.MSG_VERBOSE);

        if (suppressMarker != null) {
            log("Setting suppress marker to be " + suppressMarker, Project.MSG_VERBOSE);
        }

        RuleContext ctx = new RuleContext();
        Report errorReport = new Report();
        final AtomicInteger reportSize = new AtomicInteger();
        for (FileSet fs: filesets) {
            List<DataSource> files = new LinkedList<DataSource>();
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            String[] srcFiles = ds.getIncludedFiles();
            for (String srcFile : srcFiles) {
                File file = new File(ds.getBasedir() + System.getProperty("file.separator") + srcFile);
                files.add(new FileDataSource(file));
            }

            final String inputPath = ds.getBasedir().getPath();

            Renderer logRenderer = new AbstractRenderer("log", "Logging renderer", null) {
                public void start() {}

                public void startFileAnalysis(DataSource dataSource) {
                    log("Processing file " + dataSource.getNiceFileName(false, inputPath), Project.MSG_VERBOSE);
                }

                public void renderFileReport(Report r) {
                    int size = r.size();
                    if (size > 0) {
                        reportSize.addAndGet(size);
                    }
                }

                public void end() {}
            };
            List<Renderer> renderers = new LinkedList<Renderer>();
            renderers.add(logRenderer);
            for (Formatter formatter: formatters) {
                renderers.add(formatter.getRenderer());
            }
            try {
                PMD.processFiles(cpus, ruleSetFactory, languageVersions, files, ctx,
                    renderers, ruleSetFiles,
                    shortFilenames, inputPath,
                    encoding, suppressMarker, classLoader);
            } catch (RuntimeException pmde) {
                pmde.printStackTrace();
                log(pmde.toString(), Project.MSG_VERBOSE);
                if (pmde.getCause() != null) {
                    StringWriter strWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(strWriter);
                    pmde.getCause().printStackTrace(printWriter);
                    log(strWriter.toString(), Project.MSG_VERBOSE);
                }
                if (pmde.getCause() != null && pmde.getCause().getMessage() != null) {
                    log(pmde.getCause().getMessage(), Project.MSG_VERBOSE);
                }
                if (failOnError) {
                    throw new BuildException(pmde);
                }
                errorReport.addError(new Report.ProcessingError(pmde.getMessage(), ctx.getSourceCodeFilename()));
            }
        }

        int problemCount = reportSize.get();
        log(problemCount + " problems found", Project.MSG_VERBOSE);

        for (Formatter formatter: formatters) {
            formatter.end(errorReport);
        }

        if (failuresPropertyName != null && problemCount > 0) {
            getProject().setProperty(failuresPropertyName, String.valueOf(problemCount));
            log("Setting property " + failuresPropertyName + " to " + problemCount, Project.MSG_VERBOSE);
        }

        if (failOnRuleViolation && problemCount > maxRuleViolations) {
            throw new BuildException("Stopping build since PMD found " + problemCount + " rule violations in the code");
        }
    }

    @Override
    public void execute() throws BuildException {
        validate();
        final Handler antLogHandler = new AntLogHandler(this);
        final ScopedLogHandlersManager logManager = new ScopedLogHandlersManager(Level.FINEST,antLogHandler);
        try{
            doTask();
        }finally{
            logManager.close();
        }
    }

    private void logRulesUsed(RuleSets rules) {
        log("Using these rulesets: " + ruleSetFiles, Project.MSG_VERBOSE);

        RuleSet[] ruleSets = rules.getAllRuleSets();
        for (RuleSet ruleSet : ruleSets) {
            for (Rule rule: ruleSet.getRules()) {
                log("Using rule " + rule.getName(), Project.MSG_VERBOSE);
            }
        }
    }

    private void validate() throws BuildException {
        
        for (Formatter f: formatters) {
            if (f.isNoOutputSupplied()) {
                throw new BuildException("toFile or toConsole needs to be specified in Formatter");
            }
        }

        if (ruleSetFiles == null) {
            if (nestedRules.isEmpty()) {
                throw new BuildException("No rulesets specified");
            }
            ruleSetFiles = getNestedRuleSetFiles();
        }

        LanguageVersion languageVersion = Language.JAVA.getVersion(targetJDK);
        if (languageVersion == null && !targetJDK.equals("jsp")) {
            StringBuilder sb = new StringBuilder();
            sb.append("The targetjdk attribute, if used, must be one of ");
            for (LanguageVersion v: Language.JAVA.getVersions()) {
                sb.append('\'').append(v.getVersion()).append("', ");
            }
            sb.append("'jsp'.");
            throw new BuildException(sb.toString());
        }
    }

    private String getNestedRuleSetFiles() {
        final StringBuffer sb = new StringBuffer();
        for (Iterator<RuleSetWrapper> it = nestedRules.iterator(); it.hasNext();) {
            RuleSetWrapper rs = it.next();
            sb.append(rs.getFile());
            if (it.hasNext()) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    private Path createLongClasspath() {
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        return classpath.createPath();
    }

    private Path createLongAuxClasspath() {
        if (auxClasspath == null) {
            auxClasspath = new Path(getProject());
        }
        return auxClasspath.createPath();
    }

    public void addRuleset(RuleSetWrapper r) {
        nestedRules.add(r);
    }

}
