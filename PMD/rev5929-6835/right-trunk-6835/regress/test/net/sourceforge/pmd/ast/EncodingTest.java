package test.net.sourceforge.pmd.ast;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;

import org.junit.Ignore;
import org.junit.Test;

public class EncodingTest {

    @Ignore("FIXME")
    @Test
    public void testDecodingOfUTF8() throws Throwable {
        
        
        String encoding = "UTF-8";

        String code = new String(TEST_UTF8.getBytes(), encoding);
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(code.getBytes()));
        ASTCompilationUnit acu = (ASTCompilationUnit)LanguageVersion.JAVA_14.getLanguageVersionHandler().getParser().parse(null, isr);
        String methodName = acu.findDescendantsOfType(ASTMethodDeclarator.class).get(0).getImage();
        assertEquals(new String("é".getBytes(), encoding), methodName);
    }

    private static final String TEST_UTF8 =
            "class Foo {" + PMD.EOL +
            " void é() {}" + PMD.EOL +
            " void fiddle() {}" + PMD.EOL +
            "}";

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(EncodingTest.class);
    }
}
