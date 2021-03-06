

package edu.rice.cs.plt.reflect;

import java.util.Set;
import java.util.TreeSet;
import edu.rice.cs.plt.iter.IterUtil;
import junit.framework.TestCase;

import static edu.rice.cs.plt.reflect.JavaVersion.*;

public class JavaVersionTest extends TestCase {
  
  public void testParseClassVersion() {
    assertEquals(JAVA_6, parseClassVersion("50.0"));
    assertEquals(JAVA_5, parseClassVersion("49.0"));
    assertEquals(JAVA_1_4, parseClassVersion("48.0"));
  }
  
  public void testParseFullVersion() {
    FullVersion v1 = parseFullVersion("1.4.2_10");
    assertEquals(JAVA_1_4, v1.majorVersion());
    assertEquals("1.4.2_10", v1.versionString());
    
    FullVersion v2 = parseFullVersion("1.4.3");
    assertEquals(JAVA_1_4, v2.majorVersion());
    assertEquals("1.4.3", v2.versionString());
    
    FullVersion v3 = parseFullVersion("1.5.0.1");
    assertEquals(JAVA_5, v3.majorVersion());
    assertEquals("5.0_1", v3.versionString());
    
    FullVersion v4 = parseFullVersion("1.5.1-beta");
    assertEquals(JAVA_5, v4.majorVersion());
    assertEquals("5.1-beta", v4.versionString());
    
    FullVersion v5 = parseFullVersion("1.5.1");
    assertEquals(JAVA_5, v5.majorVersion());
    assertEquals("5.1", v5.versionString());
    
    FullVersion v6 = parseFullVersion("1.6.0_2");
    assertEquals(JAVA_6, v6.majorVersion());
    assertEquals("6.0_2", v6.versionString());
    
    FullVersion v7 = parseFullVersion("1.6.0_11");
    assertEquals("6.0_11", v7.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(v7);
    sorter.add(v5);
    sorter.add(v3);
    sorter.add(v1);
    sorter.add(v2);
    sorter.add(v4);
    sorter.add(v6);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(v1, v2, v3, v4, v5, v6, v7)));
  }
  
  public void testParseFullVersionUnrecognized() {
    FullVersion v1 = parseFullVersion("1.4.2_10","","");
    assertEquals(JAVA_1_4, v1.majorVersion());
    assertEquals("1.4.2_10", v1.versionString());
    
    FullVersion v2 = parseFullVersion("1.4.3","","");
    assertEquals(JAVA_1_4, v2.majorVersion());
    assertEquals("1.4.3", v2.versionString());
    
    FullVersion v3 = parseFullVersion("1.5.0.1","","");
    assertEquals(JAVA_5, v3.majorVersion());
    assertEquals("5.0_1", v3.versionString());
    
    FullVersion v4 = parseFullVersion("1.5.1-beta","","");
    assertEquals(JAVA_5, v4.majorVersion());
    assertEquals("5.1-beta", v4.versionString());
    
    FullVersion v5 = parseFullVersion("1.5.1","","");
    assertEquals(JAVA_5, v5.majorVersion());
    assertEquals("5.1", v5.versionString());
    
    FullVersion v6 = parseFullVersion("1.6.0_2","","");
    assertEquals(JAVA_6, v6.majorVersion());
    assertEquals("6.0_2", v6.versionString());
    
    FullVersion v7 = parseFullVersion("1.6.0_11","","");
    assertEquals("6.0_11", v7.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(v7);
    sorter.add(v5);
    sorter.add(v3);
    sorter.add(v1);
    sorter.add(v2);
    sorter.add(v4);
    sorter.add(v6);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(v1, v2, v3, v4, v5, v6, v7)));
  }
  
  public void testParseFullVersionApple() {
    FullVersion v1 = parseFullVersion("1.4.2_10","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals(JAVA_1_4, v1.majorVersion());
    assertEquals("1.4.2_10", v1.versionString());
    
    FullVersion v2 = parseFullVersion("1.4.3","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals(JAVA_1_4, v2.majorVersion());
    assertEquals("1.4.3", v2.versionString());
    
    FullVersion v3 = parseFullVersion("1.5.0.1","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals(JAVA_5, v3.majorVersion());
    assertEquals("5.0_1", v3.versionString());
    
    FullVersion v4 = parseFullVersion("1.5.1-beta","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals(JAVA_5, v4.majorVersion());
    assertEquals("5.1-beta", v4.versionString());
    
    FullVersion v5 = parseFullVersion("1.5.1","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals(JAVA_5, v5.majorVersion());
    assertEquals("5.1", v5.versionString());
    
    FullVersion v6 = parseFullVersion("1.6.0_2","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals(JAVA_6, v6.majorVersion());
    assertEquals("6.0_2", v6.versionString());
    
    FullVersion v7 = parseFullVersion("1.6.0_11","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals("6.0_11", v7.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(v7);
    sorter.add(v5);
    sorter.add(v3);
    sorter.add(v1);
    sorter.add(v2);
    sorter.add(v4);
    sorter.add(v6);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(v1, v2, v3, v4, v5, v6, v7)));
  }
  
  public void testParseFullVersionSun() {
    FullVersion v1 = parseFullVersion("1.4.2_10","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals(JAVA_1_4, v1.majorVersion());
    assertEquals("1.4.2_10", v1.versionString());
    
    FullVersion v2 = parseFullVersion("1.4.3","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals(JAVA_1_4, v2.majorVersion());
    assertEquals("1.4.3", v2.versionString());
    
    FullVersion v3 = parseFullVersion("1.5.0.1","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals(JAVA_5, v3.majorVersion());
    assertEquals("5.0_1", v3.versionString());
    
    FullVersion v4 = parseFullVersion("1.5.1-beta","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals(JAVA_5, v4.majorVersion());
    assertEquals("5.1-beta", v4.versionString());
    
    FullVersion v5 = parseFullVersion("1.5.1","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals(JAVA_5, v5.majorVersion());
    assertEquals("5.1", v5.versionString());
    
    FullVersion v6 = parseFullVersion("1.6.0_2","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals(JAVA_6, v6.majorVersion());
    assertEquals("6.0_2", v6.versionString());
    
    FullVersion v7 = parseFullVersion("1.6.0_11","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals("6.0_11", v7.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(v7);
    sorter.add(v5);
    sorter.add(v3);
    sorter.add(v1);
    sorter.add(v2);
    sorter.add(v4);
    sorter.add(v6);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(v1, v2, v3, v4, v5, v6, v7)));
  }
  
  public void testParseFullVersionOpenJDK() {
    FullVersion v1 = parseFullVersion("1.4.2_10","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals(JAVA_1_4, v1.majorVersion());
    assertEquals("1.4.2_10-OpenJDK", v1.versionString());
    
    FullVersion v2 = parseFullVersion("1.4.3","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals(JAVA_1_4, v2.majorVersion());
    assertEquals("1.4.3-OpenJDK", v2.versionString());
    
    FullVersion v3 = parseFullVersion("1.5.0.1","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals(JAVA_5, v3.majorVersion());
    assertEquals("5.0_1-OpenJDK", v3.versionString());
    
    FullVersion v4 = parseFullVersion("1.5.1-beta","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals(JAVA_5, v4.majorVersion());
    assertEquals("5.1-beta-OpenJDK", v4.versionString());
    
    FullVersion v5 = parseFullVersion("1.5.1","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals(JAVA_5, v5.majorVersion());
    assertEquals("5.1-OpenJDK", v5.versionString());
    
    FullVersion v6 = parseFullVersion("1.6.0_2","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals(JAVA_6, v6.majorVersion());
    assertEquals("6.0_2-OpenJDK", v6.versionString());
    
    FullVersion v7 = parseFullVersion("1.6.0_11","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals("6.0_11-OpenJDK", v7.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(v7);
    sorter.add(v5);
    sorter.add(v3);
    sorter.add(v1);
    sorter.add(v2);
    sorter.add(v4);
    sorter.add(v6);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(v1, v2, v3, v4, v5, v6, v7)));
  }
  
  public void testParseFullVersionMint() {
    FullVersion v1 = parseFullVersion("1.4.2_10","mint","mint");
    assertEquals(JAVA_1_4, v1.majorVersion());
    assertEquals("1.4.2_10-Mint", v1.versionString());
    
    FullVersion v2 = parseFullVersion("1.4.3","mint","mint");
    assertEquals(JAVA_1_4, v2.majorVersion());
    assertEquals("1.4.3-Mint", v2.versionString());
    
    FullVersion v3 = parseFullVersion("1.5.0.1","mint","mint");
    assertEquals(JAVA_5, v3.majorVersion());
    assertEquals("5.0_1-Mint", v3.versionString());
    
    FullVersion v4 = parseFullVersion("1.5.1-beta","mint","mint");
    assertEquals(JAVA_5, v4.majorVersion());
    assertEquals("5.1-beta-Mint", v4.versionString());
    
    FullVersion v5 = parseFullVersion("1.5.1","mint","mint");
    assertEquals(JAVA_5, v5.majorVersion());
    assertEquals("5.1-Mint", v5.versionString());
    
    FullVersion v6 = parseFullVersion("1.6.0_2","mint","mint");
    assertEquals(JAVA_6, v6.majorVersion());
    assertEquals("6.0_2-Mint", v6.versionString());
    
    FullVersion v7 = parseFullVersion("1.6.0_11","mint","mint");
    assertEquals("6.0_11-Mint", v7.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(v7);
    sorter.add(v5);
    sorter.add(v3);
    sorter.add(v1);
    sorter.add(v2);
    sorter.add(v4);
    sorter.add(v6);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(v1, v2, v3, v4, v5, v6, v7)));
  }
  
  public void testParseFullVersionSort() {
    FullVersion vUnrecognized = parseFullVersion("1.6.0_11","","");
    assertEquals("6.0_11", vUnrecognized.versionString());
    FullVersion vMint = parseFullVersion("1.6.0_11","mint","mint");
    assertEquals("6.0_11-Mint", vMint.versionString());
    FullVersion vOpenJDK = parseFullVersion("1.6.0_11","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals("6.0_11-OpenJDK", vOpenJDK.versionString());
    FullVersion vApple = parseFullVersion("1.6.0_11","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals("6.0_11", vApple.versionString());
    FullVersion vSun = parseFullVersion("1.6.0_11","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals("6.0_11", vSun.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(vSun);
    sorter.add(vApple);
    sorter.add(vOpenJDK);
    sorter.add(vUnrecognized);
    sorter.add(vMint);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(vMint, vUnrecognized, vOpenJDK, vApple, vSun)));
  }
  
  public void testParseFullVersionDifferentSort() {
    FullVersion vUnrecognized = parseFullVersion("1.6.0_11","","");
    assertEquals("6.0_11", vUnrecognized.versionString());
    FullVersion vMint = parseFullVersion("1.7.0_11","mint","mint");
    assertEquals("7.0_11-Mint", vMint.versionString());
    FullVersion vOpenJDK = parseFullVersion("1.6.0_11","OpenJDK Runtime Environment","Sun Microsystems Inc.");
    assertEquals("6.0_11-OpenJDK", vOpenJDK.versionString());
    FullVersion vApple = parseFullVersion("1.6.0_11","Java(TM) 2 Runtime Environment, Standard Edition","\"Apple Computer, Inc.\"");
    assertEquals("6.0_11", vApple.versionString());
    FullVersion vSun = parseFullVersion("1.6.0_11","Java(TM) 2 Runtime Environment, Standard Edition","Sun Microsystems Inc.");
    assertEquals("6.0_11", vSun.versionString());
    
    Set<FullVersion> sorter = new TreeSet<FullVersion>();
    sorter.add(vSun);
    sorter.add(vApple);
    sorter.add(vOpenJDK);
    sorter.add(vUnrecognized);
    sorter.add(vMint);
    
    assertTrue(IterUtil.isEqual(sorter, IterUtil.make(vMint, vUnrecognized, vOpenJDK, vApple, vSun)));
  }
  
  public void testVersionToFullVersion() {
    assertEquals("Java 1.1.0", JAVA_1_1.fullVersion().toString());
    assertEquals("Java 1.2.0", JAVA_1_2.fullVersion().toString());
    assertEquals("Java 1.3.0", JAVA_1_3.fullVersion().toString());
    assertEquals("Java 1.4.0", JAVA_1_4.fullVersion().toString());
    assertEquals("Java 5.0", JAVA_5.fullVersion().toString());
    assertEquals("Java 6.0", JAVA_6.fullVersion().toString());
    assertEquals("Java 7.0", JAVA_7.fullVersion().toString());
    assertEquals("Java >7.0", FUTURE.fullVersion().toString());
  }
}
