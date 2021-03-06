
package net.sourceforge.pmd.lang.java;

import java.io.Reader;

import net.sourceforge.pmd.lang.java.ast.JavaParser;
import net.sourceforge.pmd.lang.java.ast.ParseException;


public class Java13Parser extends AbstractJavaParser {

    @Override
    protected JavaParser createJavaParser(Reader source) throws ParseException {
	JavaParser javaParser = super.createJavaParser(source);
	javaParser.setJDK13();
	return javaParser;
    }
}
