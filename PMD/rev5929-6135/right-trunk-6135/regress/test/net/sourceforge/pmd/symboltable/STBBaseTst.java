package test.net.sourceforge.pmd.symboltable;

import java.io.StringReader;

import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.symboltable.SymbolFacade;

public abstract class STBBaseTst {

    protected ASTCompilationUnit acu;
    protected SymbolFacade stb;

    protected void parseCode(String code) {
        parseCode(code, LanguageVersion.JAVA_14);
    }

    protected void parseCode15(String code) {
        parseCode(code, LanguageVersion.JAVA_15);
    }

    protected void parseCode(String code, LanguageVersion languageVersion) {
   	  acu = (ASTCompilationUnit)Language.JAVA.getDefaultVersion().getLanguageVersionHandler().getParser().parse(null, new StringReader(code));
        stb = new SymbolFacade();
        stb.initializeWith(acu);
    }
}
