
package net.sourceforge.pmd.lang.ecmascript;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.pmd.lang.AbstractParser;
import net.sourceforge.pmd.lang.TokenManager;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.ast.ParseException;


public class Ecmascript3Parser extends AbstractParser {

    public TokenManager createTokenManager(Reader source) {
	return null;
    }

    public boolean canParse() {
	return true;
    }

    public Node parse(String fileName, Reader source) throws ParseException {
	return new net.sourceforge.pmd.lang.ecmascript.ast.EcmascriptParser().parse(source);
    }

    public Map<Integer, String> getSuppressMap() {
	return new HashMap<Integer, String>(); 
    }
}
