
package net.sourceforge.pmd.cpd;

import java.io.StringReader;

import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.TokenManager;
import net.sourceforge.pmd.lang.jsp.ast.Token;

public class JSPTokenizer implements Tokenizer {

    public void tokenize(SourceCode sourceCode, Tokens tokenEntries) {
	StringBuffer buffer = sourceCode.getCodeBuffer();
	TokenManager tokenMgr = Language.JSP.getDefaultVersion().getLanguageVersionHandler().getParser()
		.getTokenManager(sourceCode.getFileName(), new StringReader(buffer.toString()));
	Token currentToken = (Token) tokenMgr.getNextToken();
	while (currentToken.image.length() > 0) {
	    tokenEntries.add(new TokenEntry(String.valueOf(currentToken.kind), sourceCode.getFileName(),
		    currentToken.beginLine));
	    currentToken = (Token) tokenMgr.getNextToken();
	}
	tokenEntries.add(TokenEntry.getEOF());
    }
}
