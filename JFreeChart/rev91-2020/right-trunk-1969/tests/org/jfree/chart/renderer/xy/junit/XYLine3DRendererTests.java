

package org.jfree.chart.renderer.xy.junit;

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

import org.jfree.chart.renderer.xy.XYLine3DRenderer;
import org.jfree.chart.util.PublicCloneable;


public class XYLine3DRendererTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(XYLine3DRendererTests.class);
    }

    
    public XYLine3DRendererTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        XYLine3DRenderer r1 = new XYLine3DRenderer();
        XYLine3DRenderer r2 = new XYLine3DRenderer();
        assertEquals(r1, r2);

        r1.setXOffset(11.1);
        assertFalse(r1.equals(r2));
        r2.setXOffset(11.1);
        assertTrue(r1.equals(r2));

        r1.setYOffset(11.1);
        assertFalse(r1.equals(r2));
        r2.setYOffset(11.1);
        assertTrue(r1.equals(r2));

        r1.setWallPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f,
                4.0f, Color.blue));
        assertFalse(r1.equals(r2));
        r2.setWallPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f,
                4.0f, Color.blue));
        assertTrue(r1.equals(r2));
    }

    
    public void testHashcode() {
        XYLine3DRenderer r1 = new XYLine3DRenderer();
        XYLine3DRenderer r2 = new XYLine3DRenderer();
        assertTrue(r1.equals(r2));
        int h1 = r1.hashCode();
        int h2 = r2.hashCode();
        assertEquals(h1, h2);
    }

    
    public void testCloning() {
        XYLine3DRenderer r1 = new XYLine3DRenderer();
        r1.setWallPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f, 4.0f,
                Color.blue));
        XYLine3DRenderer r2 = null;
        try {
            r2 = (XYLine3DRenderer) r1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(r1 != r2);
        assertTrue(r1.getClass() == r2.getClass());
        assertTrue(r1.equals(r2));
    }

    
    public void testPublicCloneable() {
        XYLine3DRenderer r1 = new XYLine3DRenderer();
        assertTrue(r1 instanceof PublicCloneable);
    }

    
    public void testSerialization() {

        XYLine3DRenderer r1 = new XYLine3DRenderer();
        r1.setWallPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f, 4.0f,
                Color.blue));
        XYLine3DRenderer r2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(r1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            r2 = (XYLine3DRenderer) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(r1, r2);

    }

}
