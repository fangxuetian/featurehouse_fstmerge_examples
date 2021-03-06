

package org.jfree.chart.junit;

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

import org.jfree.chart.PaintMap;


public class PaintMapTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(PaintMapTests.class);
    }

    
    public PaintMapTests(String name) {
        super(name);
    }

    
    public void testGetPaint() {
        PaintMap m1 = new PaintMap();
        assertEquals(null, m1.getPaint("A"));
        m1.put("A", Color.red);
        assertEquals(Color.red, m1.getPaint("A"));
        m1.put("A", null);
        assertEquals(null, m1.getPaint("A"));

        
        boolean pass = false;
        try {
            m1.getPaint(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testPut() {
        PaintMap m1 = new PaintMap();
        m1.put("A", Color.red);
        assertEquals(Color.red, m1.getPaint("A"));

        
        boolean pass = false;
        try {
            m1.put(null, Color.blue);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testEquals() {
        PaintMap m1 = new PaintMap();
        PaintMap m2 = new PaintMap();
        assertTrue(m1.equals(m1));
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(null));
        assertFalse(m1.equals("ABC"));

        m1.put("K1", Color.red);
        assertFalse(m1.equals(m2));
        m2.put("K1", Color.red);
        assertTrue(m1.equals(m2));

        m1.put("K2", new GradientPaint(1.0f, 2.0f, Color.green, 3.0f, 4.0f,
                Color.yellow));
        assertFalse(m1.equals(m2));
        m2.put("K2", new GradientPaint(1.0f, 2.0f, Color.green, 3.0f, 4.0f,
                Color.yellow));
        assertTrue(m1.equals(m2));

        m1.put("K2", null);
        assertFalse(m1.equals(m2));
        m2.put("K2", null);
        assertTrue(m1.equals(m2));
    }

    
    public void testCloning() {
        PaintMap m1 = new PaintMap();
        PaintMap m2 = null;
        try {
            m2 = (PaintMap) m1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(m1.equals(m2));

        m1.put("K1", Color.red);
        m1.put("K2", new GradientPaint(1.0f, 2.0f, Color.green, 3.0f, 4.0f,
                Color.yellow));
        try {
            m2 = (PaintMap) m1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(m1.equals(m2));
    }

    
    public void testSerialization1() {
        PaintMap m1 = new PaintMap();
        PaintMap m2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(m1);
            out.close();

            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                    buffer.toByteArray()));
            m2 = (PaintMap) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(m1, m2);
    }

    
    public void testSerialization2() {
        PaintMap m1 = new PaintMap();
        m1.put("K1", Color.red);
        m1.put("K2", new GradientPaint(1.0f, 2.0f, Color.green, 3.0f, 4.0f,
                Color.yellow));
        PaintMap m2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(m1);
            out.close();

            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                    buffer.toByteArray()));
            m2 = (PaintMap) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(m1, m2);
    }

    
    public void testKeysOfDifferentClasses() {
        PaintMap m = new PaintMap();
        m.put("ABC", Color.red);
        m.put(new Integer(99), Color.blue);
        assertEquals(Color.blue, m.getPaint(new Integer(99)));
    }

}

