
package test.net.sourceforge.pmd.cpd;

import static org.junit.Assert.assertEquals;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.cpd.JavascriptTokenizer;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;

import org.junit.Test;

public class JavascriptTokenizerTest {

    @Test
    public void test1() throws Throwable {
        Tokenizer tokenizer = new JavascriptTokenizer();
        SourceCode sourceCode = new SourceCode( new SourceCode.StringCodeLoader( getCode1() ) );
        Tokens tokens = new Tokens();
        tokenizer.tokenize( sourceCode, tokens );
        assertEquals( 22, tokens.size() );
    }

    @Test
    public void test2() throws Throwable {
        Tokenizer t = new JavascriptTokenizer();
        SourceCode sourceCode = new SourceCode( new SourceCode.StringCodeLoader( getCode2() ) );
        Tokens tokens = new Tokens();
        t.tokenize( sourceCode, tokens );
        assertEquals( 22, tokens.size() );
    }

    
    private String getCode1() {
        StringBuilder sb = new StringBuilder();
        sb.append( "function switchToRealPassword() {" ).append(PMD.EOL);
        sb.append( "   var real = $('realPass')" ).append(PMD.EOL);
        sb.append( "   var prompt = $('promptPass')" ).append(PMD.EOL);
        sb.append( "   real.style.display = 'inline'" ).append(PMD.EOL);
        sb.append( "   prompt.style.display = 'none'" ).append(PMD.EOL);
        sb.append( "   real.focus()" ).append(PMD.EOL);
        sb.append( "}" ).append(PMD.EOL);
        return sb.toString();
    }

    
    private String getCode2() {
        StringBuilder sb = new StringBuilder();
        sb.append( "function switchToRealPassword() {" ).append(PMD.EOL);
        sb.append( "   var real = $('realPass');" ).append(PMD.EOL);
        sb.append( "   var prompt = $('promptPass');" ).append(PMD.EOL);
        sb.append( "   real.style.display = 'inline';" ).append(PMD.EOL);
        sb.append( "   prompt.style.display = 'none';" ).append(PMD.EOL);
        sb.append( "   real.focus();" ).append(PMD.EOL);
        sb.append( "}" ).append(PMD.EOL);
        return sb.toString();
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter( JavascriptTokenizerTest.class );
    }
}