

package org.jfree.chart.plot.junit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.event.MarkerChangeEvent;
import org.jfree.chart.event.MarkerChangeListener;
import org.jfree.chart.plot.CategoryMarker;


public class CategoryMarkerTests extends TestCase 
        implements MarkerChangeListener {

    MarkerChangeEvent lastEvent;
    
    public void markerChanged(MarkerChangeEvent event) {
        this.lastEvent = event;
    }

    
    public static Test suite() {
        return new TestSuite(CategoryMarkerTests.class);
    }

    
    public CategoryMarkerTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        CategoryMarker m1 = new CategoryMarker("A");
        CategoryMarker m2 = new CategoryMarker("A");
        assertTrue(m1.equals(m2));
        assertTrue(m2.equals(m1));
        
        
        m1 = new CategoryMarker("B");
        assertFalse(m1.equals(m2));
        m2 = new CategoryMarker("B");
        assertTrue(m1.equals(m2));
        
        
        m1 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(1.1f));
        assertFalse(m1.equals(m2));
        m2 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(1.1f));
        assertTrue(m1.equals(m2));

        
        m1 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f));
        assertFalse(m1.equals(m2));
        m2 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f));
        assertTrue(m1.equals(m2));

        
        m1 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f), Color.red,
                new BasicStroke(1.0f), 1.0f);
        assertFalse(m1.equals(m2));
        m2 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f), Color.red,
                new BasicStroke(1.0f), 1.0f);
        assertTrue(m1.equals(m2));

        
        m1 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f), Color.red,
                new BasicStroke(3.3f), 1.0f);
        assertFalse(m1.equals(m2));
        m2 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f), Color.red,
                new BasicStroke(3.3f), 1.0f);
        assertTrue(m1.equals(m2));

        
        m1 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f), Color.red,
                new BasicStroke(1.0f), 0.5f);
        assertFalse(m1.equals(m2));
        m2 = new CategoryMarker("A", new GradientPaint(1.0f, 2.0f, Color.white, 
                3.0f, 4.0f, Color.yellow), new BasicStroke(2.2f), Color.red,
                new BasicStroke(1.0f), 0.5f);
        assertTrue(m1.equals(m2));

    }
        
    
    public void testCloning() {
        CategoryMarker m1 = new CategoryMarker("A", new GradientPaint(1.0f, 
                2.0f, Color.white, 3.0f, 4.0f, Color.yellow), 
                new BasicStroke(1.1f));
        CategoryMarker m2 = null;
        try {
            m2 = (CategoryMarker) m1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(m1 != m2);
        assertTrue(m1.getClass() == m2.getClass());
        assertTrue(m1.equals(m2));
    }

   
    public void testSerialization() {

        CategoryMarker m1 = new CategoryMarker("A", new GradientPaint(1.0f, 
                2.0f, Color.white, 3.0f, 4.0f, Color.yellow), 
                new BasicStroke(1.1f));
        CategoryMarker m2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(m1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            m2 = (CategoryMarker) in.readObject();
            in.close();
        }
        catch (Exception e) {
            fail(e.toString());
        }
        boolean b = m1.equals(m2);
        assertTrue(b);

    }
    
    
    public void testGetSetKey() {
        CategoryMarker m = new CategoryMarker("X");
        m.addChangeListener(this);
        this.lastEvent = null;
        assertEquals("X", m.getKey());
        m.setKey("Y");
        assertEquals("Y", m.getKey());
        assertEquals(m, this.lastEvent.getMarker());
        
        
        try {
            m.setKey(null);
            fail("Expected an IllegalArgumentException for null.");
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    
    public void testGetSetDrawAsLine() {
        CategoryMarker m = new CategoryMarker("X");
        m.addChangeListener(this);
        this.lastEvent = null;
        assertEquals(false, m.getDrawAsLine());
        m.setDrawAsLine(true);
        assertEquals(true, m.getDrawAsLine());
        assertEquals(m, this.lastEvent.getMarker());
    }
}
