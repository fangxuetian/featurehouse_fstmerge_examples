package test.net.sourceforge.pmd;

import static org.junit.Assert.assertEquals;

import java.io.File;

import junit.framework.JUnit4TestAdapter;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionDiscoverer;

import org.junit.Test;

public class LanguageVersionDiscovererTest {

    
    @Test
    public void testJspFile() {
        LanguageVersionDiscoverer discoverer = new LanguageVersionDiscoverer();
        File jspFile = new File("/path/to/MyPage.jsp");
        LanguageVersion languageVersion = discoverer.getDefaultLanguageVersionForFile(jspFile);
        assertEquals("LanguageVersion must be JSP!", LanguageVersion.JSP, languageVersion);
    }

    
    @Test
    public void testJavaFileUsingDefaults() {
        LanguageVersionDiscoverer discoverer = new LanguageVersionDiscoverer();
        File javaFile = new File("/path/to/MyClass.java");

        LanguageVersion languageVersion = discoverer.getDefaultLanguageVersionForFile(javaFile);
        assertEquals("LanguageVersion must be Java 1.5!", LanguageVersion.JAVA_15, languageVersion);
    }

    
    @Test
    public void testJavaFileUsing15() {
        LanguageVersionDiscoverer discoverer = new LanguageVersionDiscoverer();
        discoverer.setDefaultLanguageVersion(LanguageVersion.JAVA_14);
        File javaFile = new File("/path/to/MyClass.java");

        LanguageVersion languageVersion = discoverer.getDefaultLanguageVersionForFile(javaFile);
        assertEquals("LanguageVersion must be Java 1.4!", LanguageVersion.JAVA_14, languageVersion);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LanguageVersionDiscovererTest.class);
    }
}
