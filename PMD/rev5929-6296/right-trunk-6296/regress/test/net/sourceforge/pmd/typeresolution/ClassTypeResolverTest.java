package test.net.sourceforge.pmd.typeresolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.java.ast.ASTAllocationExpression;
import net.sourceforge.pmd.lang.java.ast.ASTBooleanLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTNullLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTReferenceType;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.ast.TypeNode;
import net.sourceforge.pmd.lang.java.typeresolution.ClassTypeResolver;

import org.jaxen.JaxenException;
import org.junit.Before;
import org.junit.Test;

import test.net.sourceforge.pmd.testframework.TestDescriptor;
import test.net.sourceforge.pmd.typeresolution.testdata.AnonymousInnerClass;
import test.net.sourceforge.pmd.typeresolution.testdata.ArrayListFound;
import test.net.sourceforge.pmd.typeresolution.testdata.ExtraTopLevelClass;
import test.net.sourceforge.pmd.typeresolution.testdata.InnerClass;
import test.net.sourceforge.pmd.typeresolution.testdata.Literals;
import test.net.sourceforge.pmd.typeresolution.testdata.Operators;
import test.net.sourceforge.pmd.typeresolution.testdata.Promotion;

public class ClassTypeResolverTest {

	private boolean isJdk14;

	@Before
	public void setUp() {
		try {
			Class.forName("java.lang.Appendable");
		} catch (Throwable t) {
			isJdk14 = true;
		}
	}

	@Test
	public void testClassNameExists() {
		ClassTypeResolver classTypeResolver = new ClassTypeResolver();
		assertEquals(true, classTypeResolver.classNameExists("java.lang.System"));
		assertEquals(false, classTypeResolver.classNameExists("im.sure.that.this.does.not.Exist"));
		assertEquals(true, classTypeResolver.classNameExists("java.awt.List"));
	}

	@Test
	public void acceptanceTest() {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(ArrayListFound.class);
		assertEquals(ArrayListFound.class, acu.getFirstDescendantOfType(ASTTypeDeclaration.class).getType());
		assertEquals(ArrayListFound.class, acu.getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class).getType());
		ASTImportDeclaration id = acu.getFirstDescendantOfType(ASTImportDeclaration.class);
		assertEquals("java.util", id.getPackage().getName());
		assertEquals(java.util.ArrayList.class, id.getType());
		assertEquals(ArrayList.class, acu.getFirstDescendantOfType(ASTClassOrInterfaceType.class).getType());
		assertEquals(ArrayList.class, acu.getFirstDescendantOfType(ASTReferenceType.class).getType());
		assertEquals(ArrayList.class, acu.getFirstDescendantOfType(ASTType.class).getType());
		assertEquals(ArrayList.class, acu.getFirstDescendantOfType(ASTVariableDeclaratorId.class).getType());
		assertEquals(ArrayList.class, acu.getFirstDescendantOfType(ASTVariableDeclarator.class).getType());
		assertEquals(ArrayList.class, acu.getFirstDescendantOfType(ASTFieldDeclaration.class).getType());
	}

	@Test
	public void testExtraTopLevelClass() throws ClassNotFoundException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(ExtraTopLevelClass.class);
		Class<?> theExtraTopLevelClass = Class.forName("test.net.sourceforge.pmd.typeresolution.testdata.TheExtraTopLevelClass");
		
		ASTTypeDeclaration typeDeclaration = (ASTTypeDeclaration)acu.jjtGetChild(1);
		assertEquals(ExtraTopLevelClass.class, typeDeclaration.getType());
		assertEquals(ExtraTopLevelClass.class,
				typeDeclaration.getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class).getType());
		
		typeDeclaration = (ASTTypeDeclaration)acu.jjtGetChild(2);
		assertEquals(theExtraTopLevelClass, typeDeclaration.getType());
		assertEquals(theExtraTopLevelClass,
				typeDeclaration.getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class).getType());
	}

	@Test
	public void testInnerClass() throws ClassNotFoundException {
		if (TestDescriptor.inRegressionTestMode()) {
			
			return;
		}

		ASTCompilationUnit acu = parseAndTypeResolveForClass(InnerClass.class);
		Class<?> theInnerClass = Class.forName("test.net.sourceforge.pmd.typeresolution.testdata.InnerClass$TheInnerClass");
		
		ASTTypeDeclaration typeDeclaration = acu.getFirstDescendantOfType(ASTTypeDeclaration.class);
		assertEquals(InnerClass.class, typeDeclaration.getType());
		ASTClassOrInterfaceDeclaration outerClassDeclaration = typeDeclaration.getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class);
		assertEquals(InnerClass.class, outerClassDeclaration.getType());
		
		assertEquals(theInnerClass,
				outerClassDeclaration.getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class).getType());
	}

	@Test
	public void testAnonymousInnerClass() throws ClassNotFoundException {
		if (TestDescriptor.inRegressionTestMode()) {
			
			return;
		}

		ASTCompilationUnit acu = parseAndTypeResolveForClass(AnonymousInnerClass.class);
		Class<?> theAnonymousInnerClass = Class.forName("test.net.sourceforge.pmd.typeresolution.testdata.AnonymousInnerClass$1");
		
		ASTTypeDeclaration typeDeclaration = acu.getFirstDescendantOfType(ASTTypeDeclaration.class);
		assertEquals(AnonymousInnerClass.class, typeDeclaration.getType());
		ASTClassOrInterfaceDeclaration outerClassDeclaration = typeDeclaration.getFirstDescendantOfType(ASTClassOrInterfaceDeclaration.class);
		assertEquals(AnonymousInnerClass.class, outerClassDeclaration.getType());
		
		assertEquals(theAnonymousInnerClass,
				outerClassDeclaration.getFirstDescendantOfType(ASTAllocationExpression.class).getType());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLiterals() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Literals.class);
		List<ASTLiteral> literals = acu.findChildNodesWithXPath("//Literal");
		int index = 0;

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(String.class, literals.get(index++).getType());

		
		assertEquals(Boolean.TYPE, literals.get(index).getFirstDescendantOfType(ASTBooleanLiteral.class).getType());
		assertEquals(Boolean.TYPE, literals.get(index++).getType());

		
		assertEquals(Boolean.TYPE, literals.get(index).getFirstDescendantOfType(ASTBooleanLiteral.class).getType());
		assertEquals(Boolean.TYPE, literals.get(index++).getType());

		
		assertNull(literals.get(index).getFirstDescendantOfType(ASTNullLiteral.class).getType());
		assertNull(literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Character.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Character.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Long.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Long.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Long.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Character.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Float.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Float.TYPE, literals.get(index++).getType());

		
		if (!isJdk14) {
			
			assertEquals(0, literals.get(index).jjtGetNumChildren());
			assertEquals(Float.TYPE, literals.get(index++).getType());
		} else {
			index++;
		}

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Character.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Double.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Double.TYPE, literals.get(index++).getType());

		
		if (!isJdk14) {
			
			assertEquals(0, literals.get(index).jjtGetNumChildren());
			assertEquals(Double.TYPE, literals.get(index++).getType());
		} else {
			index++;
		}

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Integer.TYPE, literals.get(index++).getType());

		
		assertEquals(0, literals.get(index).jjtGetNumChildren());
		assertEquals(Character.TYPE, literals.get(index++).getType());

		
		assertEquals("All literals not tested", index, literals.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testUnaryNumericPromotion() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Promotion.class);
		List<ASTExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'unaryNumericPromotion']]//Expression[UnaryExpression]");
		int index = 0;

		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBinaryNumericPromotion() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Promotion.class);
		List<ASTExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'binaryNumericPromotion']]//Expression[AdditiveExpression]");
		int index = 0;

		
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Float.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBinaryStringPromotion() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Promotion.class);
		List<ASTExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'binaryStringPromotion']]//Expression");
		int index = 0;

		assertEquals(String.class, expressions.get(index++).getType());
		assertEquals(String.class, expressions.get(index++).getType());
		assertEquals(String.class, expressions.get(index++).getType());
		assertEquals(String.class, expressions.get(index++).getType());
		assertEquals(String.class, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testUnaryLogicalOperators() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Operators.class);
		List<ASTExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'unaryLogicalOperators']]//Expression");
		int index = 0;

		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBinaryLogicalOperators() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Operators.class);
		List<ASTExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'binaryLogicalOperators']]//Expression");
		int index = 0;

		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());
		assertEquals(Boolean.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testUnaryNumericOperators() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Operators.class);
		List<TypeNode> expressions = new ArrayList<TypeNode>();
		expressions.addAll(acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'unaryNumericOperators']]//Expression"));
		expressions.addAll(acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'unaryNumericOperators']]//PostfixExpression"));
		expressions.addAll(acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'unaryNumericOperators']]//PreIncrementExpression"));
		expressions.addAll(acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'unaryNumericOperators']]//PreDecrementExpression"));
		int index = 0;

		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());
		assertEquals(Double.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBinaryNumericOperators() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Operators.class);
		List<ASTExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'binaryNumericOperators']]//Expression");
		int index = 0;

		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());
		assertEquals(Integer.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testAssignmentOperators() throws JaxenException {
		ASTCompilationUnit acu = parseAndTypeResolveForClass(Operators.class);
		List<ASTStatementExpression> expressions = acu.findChildNodesWithXPath("//Block[preceding-sibling::MethodDeclarator[@Image = 'assignmentOperators']]//StatementExpression");
		int index = 0;

		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());
		assertEquals(Long.TYPE, expressions.get(index++).getType());

		
		assertEquals("All expressions not tested", index, expressions.size());
	}

	public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(ClassTypeResolverTest.class);
	}

	
	
	
	private ASTCompilationUnit parseAndTypeResolveForClass(Class<?> clazz) {
		String sourceFile = clazz.getName().replace('.', '/') + ".java";
		InputStream is = ClassTypeResolverTest.class.getClassLoader().getResourceAsStream(sourceFile);
		if (is == null) {
			throw new IllegalArgumentException("Unable to find source file " + sourceFile + " for " + clazz);
		}
		LanguageVersionHandler languageVersionHandler = LanguageVersion.JAVA_15.getLanguageVersionHandler();
		ASTCompilationUnit acu = (ASTCompilationUnit)languageVersionHandler.getParser().parse(null, new InputStreamReader(is));
		languageVersionHandler.getSymbolFacade().start(acu);
		languageVersionHandler.getTypeResolutionFacade(ClassTypeResolverTest.class.getClassLoader()).start(acu);
		return acu;
	}
}
