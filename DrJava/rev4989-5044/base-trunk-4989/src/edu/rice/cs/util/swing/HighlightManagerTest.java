

package edu.rice.cs.util.swing;

import edu.rice.cs.drjava.DrJavaTestCase;
import edu.rice.cs.drjava.ui.ReverseHighlighter;

import javax.swing.*;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;


public class HighlightManagerTest extends DrJavaTestCase {
  
  
  
  JTextComponent jtc;
  Highlighter.HighlightPainter p, p1, p2;
  HighlightManager hm;
  
  public void setUp() throws Exception {
    super.setUp();

    jtc = new JTextField();
    jtc.setHighlighter(new ReverseHighlighter());
    
    p = new ReverseHighlighter.DrJavaHighlightPainter(Color.BLACK);
    p1 = new ReverseHighlighter.DefaultFrameHighlightPainter(Color.RED, 2);
    p2 = new ReverseHighlighter.DrJavaHighlightPainter(Color.BLACK);
    
    hm = new HighlightManager(jtc);
    
    hm.addHighlight(0,0,p);
    hm.addHighlight(0,1,p);
  }
    
  public void testAddRemove() {
    hm.addHighlight(0,0,p);
    hm.addHighlight(0,1,p);
    assertEquals("HighlightManager add Test", 2, hm.size());
    hm.removeHighlight(0,0,p);
    assertEquals("HighlightManager remove Test 1", 1, hm.size());
    hm.removeHighlight(0,1,p);
    assertEquals("HighlightManager remove Test 1", 0, hm.size());
  }
  
  public void testHighlightInfoEquals() {
    HighlightManager.HighlightInfo hi1, hi2, hi3, hi4;
    hi1 = hm.new HighlightInfo(0,0,p);
    hi2 = hm.new HighlightInfo(0,0,p);
    hi3 = hm.new HighlightInfo(0,1,p);
    hi4 = hm.new HighlightInfo(0,0,p1);

    assertEquals("HighlightInfo equals test 1", hi1, hi2);
    assertFalse("HighlightInfo equals test 2", hi1.equals(hi3));
    assertFalse("HighlightInfo equals test 3", hi1.equals(hi4));
  }
}
