

package edu.rice.cs.util;

import edu.rice.cs.drjava.DrJavaTestCase;
import edu.rice.cs.drjava.config.*;

import edu.rice.cs.plt.tuple.Pair;
import edu.rice.cs.plt.lambda.Lambda;
import java.io.PrintWriter;
import java.util.List;


public class StringOpsTest extends DrJavaTestCase {
  
  public void testReplace() {
    String test = "aabbccdd";
    assertEquals("testReplace:", "aab12cdd", StringOps.replace(test, "bc","12"));
    test = "cabcabc";
    assertEquals("testReplace:", "cabc", StringOps.replace(test, "cabc", "c"));
  }
  
  public void testEscapeUnEscapeFileName() {
    assertEquals("", StringOps.escapeFileName(""));
    assertEquals("abc123", StringOps.escapeFileName("abc123"));
    assertEquals("abc123", StringOps.unescapeFileName("abc123"));
    
    assertEquals("\u\u", StringOps.escapeFileName("\u"));
    assertEquals("\u", StringOps.unescapeFileName("\u\u"));
    
    assertEquals("\u ", StringOps.escapeFileName(" "));
    assertEquals(" ", StringOps.unescapeFileName("\u "));
    
    assertEquals("\u" + String.valueOf(java.io.File.pathSeparatorChar), StringOps.escapeFileName(String.valueOf(java.io.File.pathSeparatorChar)));
    assertEquals(String.valueOf(java.io.File.pathSeparatorChar), StringOps.unescapeFileName("\u" + String.valueOf(java.io.File.pathSeparatorChar)));
    
    assertEquals("\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR), StringOps.escapeFileName(String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR)));
    assertEquals(String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR), StringOps.unescapeFileName("\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR)));
    
    assertEquals("\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR), StringOps.escapeFileName(String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR)));
    assertEquals(String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR), StringOps.unescapeFileName("\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR)));
    
    assertEquals("\u:", StringOps.escapeFileName(":"));
    assertEquals(":", StringOps.unescapeFileName("\u:"));
    
    assertEquals("\u\u\u\u", StringOps.escapeFileName("\u\u"));
    assertEquals("\u\u", StringOps.unescapeFileName("\u\u\u\u"));
    
    assertEquals("\u \u ", StringOps.escapeFileName("  "));
    assertEquals("  ", StringOps.unescapeFileName("\u \u "));
    
    assertEquals("\u" + String.valueOf(java.io.File.pathSeparatorChar) + "\u" + String.valueOf(java.io.File.pathSeparatorChar), StringOps.escapeFileName(String.valueOf(java.io.File.pathSeparatorChar) + String.valueOf(java.io.File.pathSeparatorChar)));
    assertEquals(String.valueOf(java.io.File.pathSeparatorChar) + String.valueOf(java.io.File.pathSeparatorChar), StringOps.unescapeFileName("\u" + String.valueOf(java.io.File.pathSeparatorChar) + "\u" + String.valueOf(java.io.File.pathSeparatorChar)));
    
    assertEquals("\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR), StringOps.escapeFileName(String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR)));
    assertEquals(String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR), StringOps.unescapeFileName("\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR)));
                 
    assertEquals("\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + "\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR), StringOps.escapeFileName(String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR)));
    assertEquals(String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR), StringOps.unescapeFileName("\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + "\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR)));
    
    assertEquals("\u:\u:", StringOps.escapeFileName("::"));
    assertEquals("::", StringOps.unescapeFileName("\u:\u:"));
    
    assertEquals("abc\u\u23", StringOps.escapeFileName("abc\u23"));
    assertEquals("abc\u23", StringOps.unescapeFileName("abc\u\u23"));
    
    
    assertEquals("abc\u 123", StringOps.escapeFileName("abc 123"));
    assertEquals("abc 123", StringOps.unescapeFileName("abc\u 123"));
    
    assertEquals("abc\u" + String.valueOf(java.io.File.pathSeparatorChar) + "123", StringOps.escapeFileName("abc" + String.valueOf(java.io.File.pathSeparatorChar) + "123"));
    assertEquals("abc" + String.valueOf(java.io.File.pathSeparatorChar) + "123", StringOps.unescapeFileName("abc\u" + String.valueOf(java.io.File.pathSeparatorChar) + "123"));
    
    assertEquals("abc\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "123", StringOps.escapeFileName("abc" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "123"));
    assertEquals("abc" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "123", StringOps.unescapeFileName("abc\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "123"));
    
    assertEquals("abc\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + "123", StringOps.escapeFileName("abc" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + "123"));
    assertEquals("abc" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + "123", StringOps.unescapeFileName("abc\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR) + "123"));
    
    assertEquals("abc\u:123", StringOps.escapeFileName("abc:123"));
    assertEquals("abc:123", StringOps.unescapeFileName("abc\u:123"));
    
    assertEquals("\u\u", StringOps.escapeFileName("\u"));
    assertEquals("\u", StringOps.unescapeFileName("\u\u"));
    
    assertEquals("abc123\u \u\u\u:\u" + String.valueOf(java.io.File.pathSeparatorChar) + "\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR), StringOps.escapeFileName("abc123 \u:" + String.valueOf(java.io.File.pathSeparatorChar) + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR)));
    assertEquals("abc123 \u:" + String.valueOf(java.io.File.pathSeparatorChar) + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR), StringOps.unescapeFileName("abc123\u \u\u\u:\u" + String.valueOf(java.io.File.pathSeparatorChar) + "\u" + String.valueOf(ProcessChain.PROCESS_SEPARATOR_CHAR) + "\u" + String.valueOf(ProcessChain.PIPE_SEPARATOR_CHAR)));
  }
  
  public void testEncodeHTML() {
    assertEquals("", StringOps.encodeHTML(""));
    assertEquals("abc", StringOps.encodeHTML("abc"));
    assertEquals("&amp;", StringOps.encodeHTML("&"));
    assertEquals("&lt;", StringOps.encodeHTML("<"));
    assertEquals("&gt;", StringOps.encodeHTML(">"));
    assertEquals("<br>", StringOps.encodeHTML(System.getProperty("line.separator")));
    assertEquals("<br>", StringOps.encodeHTML("\n"));
    assertEquals("&amp;&lt;&gt;<br><br>", StringOps.encodeHTML("&<>" + System.getProperty("line.separator") + "\n"));
    assertEquals("&amp;&amp;", StringOps.encodeHTML("&&"));
    assertEquals("&lt;&lt;", StringOps.encodeHTML("<<"));
    assertEquals("&gt;&gt;", StringOps.encodeHTML(">>"));
    assertEquals("<br><br>", StringOps.encodeHTML(System.getProperty("line.separator") + System.getProperty("line.separator")));
    assertEquals("<br><br>", StringOps.encodeHTML("\n\n"));
  }
  
  public void testCompress() {
    assertEquals("", StringOps.compress(""));
    assertEquals("abc", StringOps.compress("abc"));
    assertEquals(" ", StringOps.compress(" "));
    assertEquals(" ", StringOps.compress("  "));
    assertEquals(" ", StringOps.compress(" \n\t\n"));
    assertEquals("\n", StringOps.compress("\n"));
    assertEquals("\t", StringOps.compress("\t"));
    assertEquals("\n", StringOps.compress("\n\t\t"));
    assertEquals("\t", StringOps.compress("\t\n\n"));
    
    assertEquals(" abc", StringOps.compress("  abc"));
    assertEquals(" abc", StringOps.compress(" \n\t\nabc"));
    assertEquals("\nabc", StringOps.compress("\nabc"));
    assertEquals("\tabc", StringOps.compress("\tabc"));
    assertEquals("\nabc", StringOps.compress("\n\t\tabc"));
    assertEquals("\tabc", StringOps.compress("\t\n\nabc"));
    
    assertEquals(" abc ", StringOps.compress("  abc   "));
    assertEquals(" abc ", StringOps.compress(" \n\t\nabc   "));
    assertEquals("\nabc\t", StringOps.compress("\nabc\t\n"));
    assertEquals("\tabc\n", StringOps.compress("\tabc\n\t"));
    assertEquals("\nabc\n", StringOps.compress("\n\t\tabc\n\t "));
    assertEquals("\tabc ", StringOps.compress("\t\n\nabc \t\n"));
  }
  
  public void testFlatten() {
    assertEquals("", StringOps.flatten(""));
    assertEquals("abc", StringOps.flatten("abc"));
    assertEquals("" + StringOps.SEPARATOR, StringOps.flatten("\n"));
    assertEquals("" + StringOps.SEPARATOR+StringOps.SEPARATOR+StringOps.SEPARATOR, StringOps.flatten("\n\n\n"));
    assertEquals("abc" + StringOps.SEPARATOR + "def" + StringOps.SEPARATOR + "ghi" + StringOps.SEPARATOR, StringOps.flatten("abc\ndef\nghi\n"));
    assertEquals("abc" + StringOps.SEPARATOR+StringOps.SEPARATOR+
                 "def" + StringOps.SEPARATOR+StringOps.SEPARATOR+
                 "ghi" + StringOps.SEPARATOR+StringOps.SEPARATOR, StringOps.flatten("abc\n\ndef\n\nghi\n\n"));
  }
  
  
  public void testGetOffsetAndLength() {
    String test = "123456789\n123456789\n123456789\n";
    
    
    
    Pair<Integer, Integer> oAndL = StringOps.getOffsetAndLength(test, 1, 1, 1, 9);
    assertEquals("testGetOffsetAndLength- offSet:", Integer.valueOf(0), oAndL.first());
    assertEquals("testGetOffsetAndLength- length:", Integer.valueOf(9), oAndL.second());
    
    oAndL = StringOps.getOffsetAndLength(test, 1, 1, 2, 3);
    assertEquals("testGetOffsetAndLength- offSet:", Integer.valueOf(0), oAndL.first());
    assertEquals("testGetOffsetAndLength- length:", Integer.valueOf(13), oAndL.second());
    
    oAndL = StringOps.getOffsetAndLength(test, 1, 5, 2, 3);
    assertEquals("testGetOffsetAndLength- offSet:", Integer.valueOf(4), oAndL.first());
    assertEquals("testGetOffsetAndLength- length:", Integer.valueOf(9), oAndL.second());
    
    oAndL = StringOps.getOffsetAndLength(test, 1, 1, 1, 1);
    assertEquals("testGetOffsetAndLength- offSet:", Integer.valueOf(0), oAndL.first());
    assertEquals("testGetOffsetAndLength- length:", Integer.valueOf(1), oAndL.second());
    
    oAndL = StringOps.getOffsetAndLength(test, 3, 5, 3, 5);
    assertEquals("testGetOffsetAndLength- offSet:", Integer.valueOf(24), oAndL.first());
    assertEquals("testGetOffsetAndLength- length:", Integer.valueOf(1), oAndL.second());
    
    oAndL = StringOps.getOffsetAndLength(test, 2, 3, 3, 6);
    assertEquals("testGetOffsetAndLength- offSet:", Integer.valueOf(12), oAndL.first());
    assertEquals("testGetOffsetAndLength- length:", Integer.valueOf(14), oAndL.second());
    
    try {
      StringOps.getOffsetAndLength(test, 3, 2, 2, 3);
      fail("Should not have been able to compute offset where startRow > endRow");
    }
    catch (IllegalArgumentException ex) {
      
    }
    
    try {
      StringOps.getOffsetAndLength(test, 2, 4, 2, 3);
      fail("Should not have been able to compute offset where start > end");
    }
    catch (IllegalArgumentException ex) {
      
    }
    
    try {
      StringOps.getOffsetAndLength(test, 4, 4, 5, 5);
      fail("Should not have been able to compute offset where the\n" +
           "given coordinates are not contained within the string");
    }
    catch (IllegalArgumentException ex) {
      
    }
    
    try {
      StringOps.getOffsetAndLength(test, 3, 4, 3, 12);
      fail("Should not have been able to compute offset where the\n" +
           "given coordinates are not contained within the string");
    }
    catch (IllegalArgumentException ex) {
      
    }
    
    try {
      StringOps.getOffsetAndLength(test, 2, 15, 3, 1);
      fail("Should not have been able to compute offset where the\n" +
           "given coordinates are not contained within the string");
    }
    catch (IllegalArgumentException ex) {
      
    }
  }
  
  
  public void testGetStackTrace() {
    final String trace = "hello";
    Throwable t = new Throwable() {
      public void printStackTrace(PrintWriter w) {
        w.print(trace);
      }
    };
    assertEquals("Should have returned the correct stack trace!", trace, StringOps.getStackTrace(t));
  }
  
  
  public void testConvertToLiteral() {
    String toConvert = " a  b  c  d";
    String expResult = "\" a  b  c  d\"";
    String actResult = StringOps.convertToLiteral(toConvert);
    assertEquals("converting " + toConvert + " should yield " +  expResult, expResult, actResult);
    
    toConvert = "\\ hello world \\";
    expResult = "\"\\\\ hello world \\\\\"";
    actResult = StringOps.convertToLiteral(toConvert);
    assertEquals("converting " + toConvert + " should yield " +  expResult, expResult, actResult);
    
    toConvert = "\\\n\\n";
    expResult = "\"\\\\\\n\\\\n\"";
    actResult = StringOps.convertToLiteral(toConvert);
    assertEquals("converting " + toConvert + " should yield " +  expResult, expResult, actResult);
    
    toConvert = "\\\"\t\\t";
    expResult = "\"\\\\\\\"\\t\\\\t\"";
    actResult = StringOps.convertToLiteral(toConvert);
    assertEquals("converting " + toConvert + " should yield " +  expResult, expResult, actResult);
    
    toConvert = "\"\\\"\t\\n\n\\\n\"";
    expResult = "\"\\\"\\\\\\\"\\t\\\\n\\n\\\\\\n\\\"\"";
    actResult = StringOps.convertToLiteral(toConvert);
    assertEquals("converting " + toConvert + " should yield " +  expResult, expResult, actResult);
    
    toConvert = "    ";
    expResult = "\"    \"";
    actResult = StringOps.convertToLiteral(toConvert);
    assertEquals("converting " + toConvert + " should yield " +  expResult, expResult, actResult);
  }
  
  private static class TestGetSimpleNameInner {
    public static class Nested {
      public static Class<?> anonClass() {
        java.awt.event.ActionListener l = new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent e) { }
        };
        return l.getClass();
      }
    }
    public class Inner {
      public Class<?> anonClass() {
        java.awt.event.ActionListener l = new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent e) { }
        };
        return l.getClass();
      }
    }
    public Inner getInner() {
      return new Inner();
    }
    public static Class<?> anonClass() {
      java.awt.event.ActionListener l = new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) { }
      };
      return l.getClass();
    }
    public static Lambda<Object, Class<?>> getLambda() {
      return new Lambda<Object, Class<?>>() {
        public Class<?> value(Object param) {
          java.awt.event.ActionListener l = new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) { }
          };
          return l.getClass();
        }
      };
    }
  }
  
  
  public void testGetSimpleName() {
    String exp = "Integer";
    String act = StringOps.getSimpleName(java.lang.Integer.class);
    assertEquals("Wrong simple name for java.lang.Integer, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "TestGetSimpleNameInner";
    act = StringOps.getSimpleName(TestGetSimpleNameInner.class);
    assertEquals("Wrong simple name for TestGetSimpleNameInner, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "Nested";
    act = StringOps.getSimpleName(TestGetSimpleNameInner.Nested.class);
    assertEquals("Wrong simple name for TestGetSimpleNameInner.Nested, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "Inner";
    act = StringOps.getSimpleName(TestGetSimpleNameInner.Inner.class);
    assertEquals("Wrong simple name for TestGetSimpleNameInner.Inner, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    java.awt.event.ActionListener l = new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) { }
    };
    
    exp = "";
    act = StringOps.getSimpleName(l.getClass());
    assertEquals("Wrong simple name for anonymous inner class, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "";
    act = StringOps.getSimpleName(TestGetSimpleNameInner.anonClass());
    assertEquals("Wrong simple name for anonymous inner class, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "";
    act = StringOps.getSimpleName(TestGetSimpleNameInner.Nested.anonClass());
    assertEquals("Wrong simple name for anonymous inner class, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "";
    act = StringOps.getSimpleName((new TestGetSimpleNameInner()).getInner().anonClass());
    assertEquals("Wrong simple name for anonymous inner class, exp=" + exp + ", act=" + act,
                 exp,
                 act);
    
    exp = "";
    act = StringOps.getSimpleName(TestGetSimpleNameInner.getLambda().value(null));
    assertEquals("Wrong simple name for anonymous inner class, exp=" + exp + ", act=" + act,
                 exp,
                 act);
  }
  
  



























































  
  public void testToStringLong() {
    long[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new long[] { }));
    assertEquals("[1]", StringOps.toString(new long[] {1}));
    assertEquals("[1, 2]", StringOps.toString(new long[] {1, 2}));
  }
  
  public void testToStringInt() {
    int[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new int[] { }));
    assertEquals("[1]", StringOps.toString(new int[] {1}));
    assertEquals("[1, 2]", StringOps.toString(new int[] {1, 2}));
  }
  
  public void testToStringShort() {
    short[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new short[] { }));
    assertEquals("[1]", StringOps.toString(new short[] {1}));
    assertEquals("[1, 2]", StringOps.toString(new short[] {1, 2}));
  }
  
  public void testToStringChar() {
    char[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new char[] { }));
    assertEquals("[a]", StringOps.toString(new char[] {'a'}));
    assertEquals("[a, b]", StringOps.toString(new char[] {'a', 'b'}));
  }
  
  public void testToStringByte() {
    byte[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new byte[] { }));
    assertEquals("[1]", StringOps.toString(new byte[] {1}));
    assertEquals("[1, 2]", StringOps.toString(new byte[] {1, 2}));
  }
  
  public void testToStringBoolean() {
    boolean[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new boolean[] { }));
    assertEquals("[true]", StringOps.toString(new boolean[] {true}));
    assertEquals("[true, false]", StringOps.toString(new boolean[] {true, false}));
  }
  
  public void testToStringFloat() {
    float[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new float[] { }));
    assertEquals("[1.23]", StringOps.toString(new float[] {1.23f}));
    assertEquals("[1.23, 4.56]", StringOps.toString(new float[] {1.23f, 4.56f}));
  }
  
  public void testToStringDouble() {
    double[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new double[] { }));
    assertEquals("[1.23]", StringOps.toString(new double[] {1.23}));
    assertEquals("[1.23, 4.56]", StringOps.toString(new double[] {1.23, 4.56}));
  }
  
  public void testToStringObject() {
    Object[] a = null;
    assertEquals("null", StringOps.toString(a));
    assertEquals("[]", StringOps.toString(new Object[] { }));
    assertEquals("[123]", StringOps.toString(new Object[] {"123"}));
    assertEquals("[123, 123]", StringOps.toString(new Object[] {"123", Integer.valueOf(123)}));
  }
  
  public void testMemSizeToString() {
    assertEquals("<1024", "500 bytes", StringOps.memSizeToString(500));
    assertEquals("1KB", "1 kilobyte", StringOps.memSizeToString(1024));
    assertEquals("2KB", "2 kilobytes", StringOps.memSizeToString(1024*2));
    assertEquals("1.5KB", "1.50 kilobytes", StringOps.memSizeToString((long)(1024*1.5)));
    assertEquals("1MB", "1 megabyte", StringOps.memSizeToString((1024*1024)));
    assertEquals("2MB", "2 megabytes", StringOps.memSizeToString((1024*1024*2)));
    assertEquals("1.1MB", "1.10 megabytes", StringOps.memSizeToString((long)(1024*1024*1.1)));
    assertEquals("1GB", "1 gigabyte", StringOps.memSizeToString((1024*1024*1024)));
    assertEquals("1.25GB", "1.25 gigabytes", StringOps.memSizeToString((long)(1024*1024*1024*1.25)));
    assertEquals("1TB", "1024 gigabytes", StringOps.memSizeToString((long)(1024)*1024*1024*1024));
    assertEquals("1024TB", "1048576 gigabytes", StringOps.memSizeToString((long)(1024)*1024*1024*1024*1024));
  }
  
  public void testCommandLineToLists() {
    List<List<List<String>>> seqs = StringOps.commandLineToLists("a b c");
    assertEquals(1, seqs.size());    
    List<List<String>> pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    List<String> l = pipe.get(0);
    
    assertEquals(3, l.size());
    assertEquals("a", l.get(0));
    assertEquals("b", l.get(1));
    assertEquals("c", l.get(2));
    
    seqs = StringOps.commandLineToLists("a'b c'");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a'b c'", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b c\"");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b c\"", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b 'c'\"");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b 'c'\"", l.get(0));
    
    seqs = StringOps.commandLineToLists("a \"b c\"");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(2, l.size());
    assertEquals("a", l.get(0));
    assertEquals("\"b c\"", l.get(1));
    
    seqs = StringOps.commandLineToLists("\u");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    
    seqs = StringOps.commandLineToLists("\u\u");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    
    seqs = StringOps.commandLineToLists("\u ");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\u b");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a b", l.get(0));
  }
  
  public void testCommandLineToLists_Pipe() {
    List<List<List<String>>> seqs = StringOps.commandLineToLists("a b c | d e f g");
    assertEquals(1, seqs.size());
    List<List<String>> pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    List<String> l = pipe.get(0);
    
    assertEquals(3, l.size());
    assertEquals("a", l.get(0));
    assertEquals("b", l.get(1));
    assertEquals("c", l.get(2));
    l = pipe.get(1);
    
    assertEquals(4, l.size());
    assertEquals("d", l.get(0));
    assertEquals("e", l.get(1));
    assertEquals("f", l.get(2));
    assertEquals("g", l.get(3));
    
    seqs = StringOps.commandLineToLists("a'b c' | d'e f g'");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a'b c'", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d'e f g'", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b c\" | d\"e f\"g");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b c\"", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d\"e f\"g", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b 'c'\" | d\"e 'f'\"g");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b 'c'\"", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d\"e 'f'\"g", l.get(0));
    
    seqs = StringOps.commandLineToLists("a \"b c\" | d \"e f\" g");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(2, l.size());
    assertEquals("a", l.get(0));
    assertEquals("\"b c\"", l.get(1));
    l = pipe.get(1);
    
    assertEquals(3, l.size());
    assertEquals("d", l.get(0));
    assertEquals("\"e f\"", l.get(1));
    assertEquals("g", l.get(2));
    
    seqs = StringOps.commandLineToLists("\u | \u");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0)); 
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0)); 
    
    seqs = StringOps.commandLineToLists("\u\u | \u\u");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    
    seqs = StringOps.commandLineToLists("\u  | \u  | \u ");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(3, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    l = pipe.get(2);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\u b | c\u d\u e");
    assertEquals(1, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a b", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("c d e", l.get(0));
  }
  
  public void testCommandLineToLists_Seq() {
    List<List<List<String>>> seqs = StringOps.commandLineToLists("a b c # d e f g");
    assertEquals(2, seqs.size());
    List<List<String>> pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    List<String> l = pipe.get(0);
    
    assertEquals(3, l.size());
    assertEquals("a", l.get(0));
    assertEquals("b", l.get(1));
    assertEquals("c", l.get(2));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(4, l.size());
    assertEquals("d", l.get(0));
    assertEquals("e", l.get(1));
    assertEquals("f", l.get(2));
    assertEquals("g", l.get(3));
    
    seqs = StringOps.commandLineToLists("a'b c' # d'e f g'");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a'b c'", l.get(0));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("d'e f g'", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b c\" # d\"e f\"g");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b c\"", l.get(0));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("d\"e f\"g", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b 'c'\" # d\"e 'f'\"g");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b 'c'\"", l.get(0));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("d\"e 'f'\"g", l.get(0));
    
    seqs = StringOps.commandLineToLists("a \"b c\" # d \"e f\" g");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(2, l.size());
    assertEquals("a", l.get(0));
    assertEquals("\"b c\"", l.get(1));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(3, l.size());
    assertEquals("d", l.get(0));
    assertEquals("\"e f\"", l.get(1));
    assertEquals("g", l.get(2));
    
    seqs = StringOps.commandLineToLists("\u # \u");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0)); 
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0)); 
    
    seqs = StringOps.commandLineToLists("\u\u # \u\u");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    
    seqs = StringOps.commandLineToLists("\u  # \u  # \u ");
    assertEquals(3, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    pipe = seqs.get(2);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\u b # c\u d\u e");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(1, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a b", l.get(0));
    pipe = seqs.get(1);
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("c d e", l.get(0));
  }
  
  public void testCommandLineToLists_PipeAndSeq() {
    List<List<List<String>>> seqs = StringOps.commandLineToLists("a b c | d e f g # a2 b2 c2 | d2 e2 f2 g2");
    assertEquals(2, seqs.size());
    List<List<String>> pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    List<String> l = pipe.get(0);
    
    assertEquals(3, l.size());
    assertEquals("a", l.get(0));
    assertEquals("b", l.get(1));
    assertEquals("c", l.get(2));
    l = pipe.get(1);
    
    assertEquals(4, l.size());
    assertEquals("d", l.get(0));
    assertEquals("e", l.get(1));
    assertEquals("f", l.get(2));
    assertEquals("g", l.get(3));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);
    
    assertEquals(3, l.size());
    assertEquals("a2", l.get(0));
    assertEquals("b2", l.get(1));
    assertEquals("c2", l.get(2));
    l = pipe.get(1);
    
    assertEquals(4, l.size());
    assertEquals("d2", l.get(0));
    assertEquals("e2", l.get(1));
    assertEquals("f2", l.get(2));
    assertEquals("g2", l.get(3));
    
    seqs = StringOps.commandLineToLists("a'b c' | d'e f g' # a2'b2 c2' | d2'e2 f2 g2'");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a'b c'", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d'e f g'", l.get(0));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a2'b2 c2'", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d2'e2 f2 g2'", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b c\" | d\"e f\"g # a2\"b2 c2\" | d2\"e2 f2\"g2");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b c\"", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d\"e f\"g", l.get(0));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a2\"b2 c2\"", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d2\"e2 f2\"g2", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\"b 'c'\" | d\"e 'f'\"g # a2\"b2 'c2'\" | d2\"e2 'f2'\"g2");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a\"b 'c'\"", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d\"e 'f'\"g", l.get(0));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a2\"b2 'c2'\"", l.get(0));
    l = pipe.get(1);    
    
    assertEquals(1, l.size());
    assertEquals("d2\"e2 'f2'\"g2", l.get(0));
    
    seqs = StringOps.commandLineToLists("a \"b c\" | d \"e f\" g # a2 \"b2 c2\" | d2 \"e2 f2\" g2");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(2, l.size());
    assertEquals("a", l.get(0));
    assertEquals("\"b c\"", l.get(1));
    l = pipe.get(1);
    
    assertEquals(3, l.size());
    assertEquals("d", l.get(0));
    assertEquals("\"e f\"", l.get(1));
    assertEquals("g", l.get(2));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(2, l.size());
    assertEquals("a2", l.get(0));
    assertEquals("\"b2 c2\"", l.get(1));
    l = pipe.get(1);
    
    assertEquals(3, l.size());
    assertEquals("d2", l.get(0));
    assertEquals("\"e2 f2\"", l.get(1));
    assertEquals("g2", l.get(2));
    
    seqs = StringOps.commandLineToLists("\u | \u # \u | \u");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0)); 
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0)); 
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0)); 
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0)); 
    
    seqs = StringOps.commandLineToLists("\u\u | \u\u # \u\u | \u\u");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("\u", l.get(0));
    
    seqs = StringOps.commandLineToLists("\u  | \u  | \u  # \u  | \u  | \u ");
    assertEquals(2, seqs.size());
    pipe = seqs.get(0);
    assertEquals(3, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    l = pipe.get(2);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    pipe = seqs.get(1);
    assertEquals(3, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    l = pipe.get(2);
    
    assertEquals(1, l.size());
    assertEquals(" ", l.get(0));
    
    seqs = StringOps.commandLineToLists("a\u b | c\u d\u e # a2\u b2 | c2\u d2\u e2");
    assertEquals(2, seqs.size());    
    pipe = seqs.get(0);
    assertEquals(2, pipe.size());
    l = pipe.get(0);    
    
    assertEquals(1, l.size());
    assertEquals("a b", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("c d e", l.get(0));
    pipe = seqs.get(1);
    assertEquals(2, pipe.size());
    l = pipe.get(0);
    
    assertEquals(1, l.size());
    assertEquals("a2 b2", l.get(0));
    l = pipe.get(1);
    
    assertEquals(1, l.size());
    assertEquals("c2 d2 e2", l.get(0));
  }
  
  public void testReplaceVariables() {
    PropertyMaps props = new PropertyMaps();
    props.setProperty("1", new ConstantProperty("var", "foo", ""));
    props.setProperty("1", new ConstantProperty("xxx", "bar", ""));
    
    assertEquals("abcxyz", StringOps.replaceVariables("abcxyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcfooxyz", StringOps.replaceVariables("abc${var}xyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcbarxyz", StringOps.replaceVariables("abc${xxx}xyz",props,PropertyMaps.GET_LAZY));
  }
  
  public void testReplaceVariables2() {
    PropertyMaps props = new PropertyMaps();
    props.setProperty("1", new ConstantProperty("var", "foo", ""));
    props.setProperty("1", new ConstantProperty("xxx", "bar", ""));
    props.setProperty("2", new ConstantProperty("yyy", "bam", ""));
    props.setProperty("2", new ConstantProperty("xxx", "new", ""));
    
    assertEquals("abcxyz", StringOps.replaceVariables("abcxyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcfooxyz", StringOps.replaceVariables("abc${var}xyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcbarxyz", StringOps.replaceVariables("abc${xxx}xyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcbamxyz", StringOps.replaceVariables("abc${yyy}xyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcbarbamxyz", StringOps.replaceVariables("abc${xxx}${yyy}xyz",props,PropertyMaps.GET_LAZY));
  }
  
  public void testReplaceVariables3() {
    PropertyMaps props = new PropertyMaps();
    props.setProperty("1", new ConstantProperty("var", "foo", ""));
    props.setProperty("1", new ConstantProperty("xxx", "bar", ""));
    
    assertEquals("abcxyz", StringOps.replaceVariables("abcxyz",props,PropertyMaps.GET_LAZY));
    assertEquals("fooxyz", StringOps.replaceVariables("${var}xyz",props,PropertyMaps.GET_LAZY));
    String source = "abc $${xxx}xyz";
    String actual = StringOps.replaceVariables(source,props,PropertyMaps.GET_LAZY);
    String expected = "abc ${xxx}xyz";



    assertEquals(expected, actual);
    assertEquals("${xxx}xyz", StringOps.replaceVariables("$${xxx}xyz",props,PropertyMaps.GET_LAZY));
  }

  public void testReplaceVariables4() {
    PropertyMaps props = new PropertyMaps();
    props.setProperty("1", new ConstantProperty("var", "foo", "") {
      public void resetAttributes() {
        _attributes.clear();
        _attributes.put("attr", null);
      }
      public String getCurrent(PropertyMaps pm) {
        if (_attributes.get("attr") == null) fail("Attribute attr for property var should be set.");
        return super.getCurrent(pm);
      }
    });
    props.setProperty("1", new ConstantProperty("xxx", "bar", "") {
      public void resetAttributes() {
        _attributes.clear();
        _attributes.put("attr1", null);
        _attributes.put("attr2", null);
      }
      public String getCurrent(PropertyMaps pm) {
        if (_attributes.get("attr1") == null) fail("Attribute attr1 for property xxx should be set.");
        if (_attributes.get("attr2") == null) fail("Attribute attr2 for property xxx should be set.");
        return super.getCurrent(pm);
      }
    });
    
    assertEquals("abcxyz", StringOps.replaceVariables("abcxyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcfooxyz", StringOps.replaceVariables("abc${var;attr=\"xxx\"}xyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcbarxyz", StringOps.replaceVariables("abc${xxx;attr1=\"xxx\";attr2=\"yyy\"}xyz",props,PropertyMaps.GET_LAZY));
    assertEquals("abcbarxyz", StringOps.replaceVariables("abc${xxx;attr1=\"abc${var;attr=\"xxx\"}xyz\";attr2=\"yyy\"}xyz",props,PropertyMaps.GET_LAZY));
    try {
      assertEquals("abcbarxyz", StringOps.replaceVariables("abc${xxx;attr2=\"yyy\"}xyz",props,PropertyMaps.GET_LAZY));
      fail("Forgot to set attr1, should fail.");
    }
    catch(junit.framework.AssertionFailedError afe) {  }
    try {
      StringOps.replaceVariables("abc${xxx;attr1=\"abc${var}xyz\";attr2=\"yyy\"}xyz",props,PropertyMaps.GET_LAZY);
      fail("Forgot to set attr1, should fail.");
    }
    catch(junit.framework.AssertionFailedError afe) {  }
    
    assertEquals("${notfound}", StringOps.replaceVariables("${notfound}",props,PropertyMaps.GET_LAZY));

    props.setProperty("1", new ConstantProperty("var", "foo", ""));
    props.setProperty("1", new ConstantProperty("xxx", "bar", ""));
    assertTrue(StringOps.replaceVariables("abc${xxx;;}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;=}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;\"\"}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));


    assertTrue(StringOps.replaceVariables("abc${xxx;attr}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr\"\"}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr${}}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr;}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr foo}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=${}}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=;}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=abc}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr==}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=\"abc\";}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=\"abc\"\"abc\"}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=\"abc\"${}}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=\"abc\"=}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
    assertTrue(StringOps.replaceVariables("abc${xxx;attr=\"abc\"foo}xyz",props,PropertyMaps.GET_LAZY).contains("<-- Error: "));
  }
  
  public void testIsMemberClass() {
    assertFalse("StringOpsTest is not a member class", StringOps.isMemberClass(StringOpsTest.class));
    assertTrue("XMLConfig.XMLConfigException is a member class", StringOps.isMemberClass(XMLConfig.XMLConfigException.class));
  }
}
