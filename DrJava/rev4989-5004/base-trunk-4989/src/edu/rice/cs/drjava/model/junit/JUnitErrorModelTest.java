

package edu.rice.cs.drjava.model.junit;

import edu.rice.cs.drjava.model.GlobalModelTestCase;
import edu.rice.cs.drjava.model.OpenDefinitionsDocument;
import edu.rice.cs.util.swing.Utilities;

import java.io.File;

import static edu.rice.cs.plt.debug.DebugUtil.debug;


public final class JUnitErrorModelTest extends GlobalModelTestCase {
  
  private JUnitErrorModel _m;
  
  private static final String MONKEYTEST_FAIL_TEXT =
    "import junit.framework.*; \n" +
    "import java.io.*; \n" +
    "public class MonkeyTestFail extends TestCase { \n" +
    "  public MonkeyTestFail(String name) { super(name); } \n" +
    "  public void testShouldFail() { \n" +
    "    assertEquals(\"monkey\", \"baboon\"); \n" +
    "  } \n" +
    "  public void testShouldErr() throws Exception { \n" +
    "    throw new IOException(\"Error\"); \n" +
    "  } \n" +
    "}";
  
  private static final String TEST_ONE =
    "import junit.framework.TestCase;\n" +
    "public class TestOne extends TestCase {\n" +
    "  public void testMyMethod() {\n" +
    "    assertTrue(false);\n" +
    "  }\n" +
    "  public TestOne() {\n" +
    "    super();\n" +
    "  }\n" +
    "  public java.lang.String toString() {\n" +
    "    return \"TestOne(\" + \")\";\n" +
    "  }\n" +
    "  public boolean equals(java.lang.Object o) {\n" +
    "    if ((o == null) || getClass() != o.getClass()) return false;\n" +
    "    return true;\n" +
    "  }\n" +
    "  public int hashCode() {\n" +
    "    return getClass().hashCode();\n" +
    "  }\n" +
    "  public void testThrowing() throws Exception{\n" +
    "    throw new Exception(\"here\");\n" +
    "  }\n" +
    "  public void testFail(){\n" +
    "    fail(\"i just failed the test\");\n" +
    "  }\n" +
    "}";
  
  private static final String TEST_TWO =
    "import junit.framework.TestCase;\n" +
    "public class TestTwo extends TestOne {\n" +
    "  public void testTwo() {\n" +
    "    assertTrue(true);\n" +
    "  }\n" +
    "  public TestTwo() {\n" +
    "    super();\n" +
    "  }\n" +
    "  public java.lang.String toString() {\n" +
    "    return \"TestTwo(\" + \")\";\n" +
    "  }\n" +
    "  public boolean equals(java.lang.Object o) {\n" +
    "    if ((o == null) || getClass() != o.getClass()) return false;\n" +
    "    return true;\n" +
    "  }\n" +
    "  public int hashCode() {\n" +
    "    return getClass().hashCode();\n" +
    "  }\n" +
    "}";
  








  
  private static final String ABC_CLASS_ONE =
    "class ABC extends java.util.Vector {}\n";
  
  private static final String ABC_CLASS_TWO =
    "class ABC extends java.util.ArrayList {}\n";
  
  private static final String ABC_TEST =
    "public class ABCTest extends junit.framework.TestCase {\n" +
    "  public void testABC() {\n" +
    "    new ABC().get(0);\n" +
    "  }\n" +
    "}";
  
  private static final String LANGUAGE_LEVEL_TEST =
    "class MyTest extends junit.framework.TestCase {\n"+
    "  void testMyMethod() {\n"+
    "    assertEquals(\"OneString\", \"TwoStrings\");\n"+
    "  }\n"+
    "}\n";
  








    
  
  public void testErrorsArrayInOrder() throws Exception {
    debug.logStart();
    _m = new JUnitErrorModel(new JUnitError[0], _model, false);
    final OpenDefinitionsDocument doc = setupDocument(MONKEYTEST_FAIL_TEXT);
    final File file = new File(_tempDir, "MonkeyTestFail.java");
    saveFile(doc, new FileSelector(file));
    
    JUnitTestListener listener = new JUnitTestListener();
    _model.addListener(listener);
    
    testStartCompile(doc);
    
    listener.waitCompileDone();
    
    if (_model.getCompilerModel().getNumErrors() > 0) fail("compile failed: " + getCompilerErrorString());
    listener.checkCompileOccurred();
    
    listener.runJUnit(doc);
    
    listener.assertJUnitStartCount(1);
    
    _model.getJUnitModel().getJUnitDocument().remove(0, _model.getJUnitModel().getJUnitDocument().getLength() - 1);
    
    
    
    _m = _model.getJUnitModel().getJUnitErrorModel();
    
    
    
    
    
    assertEquals("the test results should have one error and one failure "+_m.getNumErrors(), 2, _m.getNumErrors());
    
    assertEquals("test case has one error reported" + _m.getError(0).message(), _m.getError(0).isWarning(), false);
    
    assertEquals("test case has one failure reported" + _m.getError(1).message(), _m.getError(1).isWarning(), true);
    
    debug.logEnd();
  }
  
  
  public void testVerifyErrorHandledCorrectly() throws Exception {
    OpenDefinitionsDocument doc = setupDocument(ABC_CLASS_ONE);
    final File file = new File(_tempDir, "ABC1.java");
    saveFile(doc, new FileSelector(file));
    
    Utilities.clearEventQueue();
    
    OpenDefinitionsDocument doc2 = setupDocument(ABC_TEST);
    final File file2 = new File(_tempDir, "ABCTest.java");
    saveFile(doc2, new FileSelector(file2));
    
    


    _model.getCompilerModel().compileAll();
    Utilities.clearEventQueue();
    
    final OpenDefinitionsDocument doc3 = setupDocument(ABC_CLASS_TWO);
    final File file3 = new File(_tempDir, "ABC2.java");
    saveFile(doc3, new FileSelector(file3));
    
    JUnitTestListener listener = new JUnitNonTestListener();
    

    
    _model.addListener(listener);
    
    listener.compile(doc3);

    if (_model.getCompilerModel().getNumErrors() > 0) {
      fail("compile failed: " + getCompilerErrorString());
    }
    listener.resetCounts();
    


    
    listener.assertClassFileErrorCount(0);
    listener.runJUnit(doc2);
    listener.waitJUnitDone();
    
    double version = Double.valueOf(System.getProperty("java.specification.version"));
    if (version < 1.5) listener.assertClassFileErrorCount(1);
    else 
      assertEquals("Should report one error", 1, _model.getJUnitModel().getJUnitErrorModel().getNumErrors());
    
    _model.removeListener(listener);
  }
  







































  
  
  public void testErrorInSuperClass() throws Exception {
    debug.logStart();
    OpenDefinitionsDocument doc1 = setupDocument(TEST_ONE);
    OpenDefinitionsDocument doc2 = setupDocument(TEST_TWO);
    final File file1 = new File(_tempDir, "TestOne.java");
    final File file2 = new File(_tempDir, "TestTwo.java");
    saveFile(doc1, new FileSelector(file1));
    saveFile(doc2, new FileSelector(file2));
    
    JUnitTestListener listener = new JUnitTestListener();
    _model.addListener(listener);
    _model.getCompilerModel().compileAll();


    
    listener.waitCompileDone();
    _log.log("Testing the first document");
    listener.runJUnit(doc1); 
    
    Utilities.clearEventQueue();
    
    _log.log("First document test should be complete");
    listener.assertJUnitStartCount(1);  
    
    _m = _model.getJUnitModel().getJUnitErrorModel();
    
    assertEquals("test case has one error reported", 3, _m.getNumErrors());
    assertTrue("first error should be an error not a warning", !_m.getError(0).isWarning());
    
    assertTrue("it's a junit error", _m.getError(0) instanceof JUnitError);
    
    assertEquals("The first error is on line 5", 3, _m.getError(0).lineNumber());
    assertEquals("The first error is on line 5", 19, _m.getError(1).lineNumber());
    assertEquals("The first error is on line 5", 22, _m.getError(2).lineNumber());
    
    _log.log("Testing the second document");
    listener.resetJUnitCounts();
    
    listener.runJUnit(doc2);
    _log.log("Second document testing should be complete");
    
    Utilities.clearEventQueue();
    
    listener.assertJUnitStartCount(1);
    
    assertEquals("test case has one error reported", 3, _m.getNumErrors());
    assertTrue("first error should be an error not a warning", !_m.getError(0).isWarning());
    assertEquals("The first error is on line 5", 3, _m.getError(0).lineNumber());
    assertEquals("The first error is on line 5", 19, _m.getError(1).lineNumber());
    assertEquals("The first error is on line 5", 22, _m.getError(2).lineNumber());
    
    _model.removeListener(listener);
    debug.logEnd();
  }
}

