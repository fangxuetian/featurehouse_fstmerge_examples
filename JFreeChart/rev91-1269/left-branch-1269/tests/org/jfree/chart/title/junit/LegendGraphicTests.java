

package org.jfree.chart.title.junit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.title.LegendGraphic;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.StandardGradientPaintTransformer;


public class LegendGraphicTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(LegendGraphicTests.class);
    }

    
    public LegendGraphicTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        LegendGraphic g1 = new LegendGraphic(new Rectangle2D.Double(1.0, 2.0,
                3.0, 4.0), Color.black);
        LegendGraphic g2 = new LegendGraphic(new Rectangle2D.Double(1.0, 2.0,
                3.0, 4.0), Color.black);
        assertEquals(g1, g2);
        assertEquals(g2, g1);

        
        g1.setShapeVisible(!g1.isShapeVisible());
        assertFalse(g1.equals(g2));
        g2.setShapeVisible(!g2.isShapeVisible());
        assertTrue(g1.equals(g2));

        
        g1.setShape(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertFalse(g1.equals(g2));
        g2.setShape(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertTrue(g1.equals(g2));

        
        g1.setShapeFilled(!g1.isShapeFilled());
        assertFalse(g1.equals(g2));
        g2.setShapeFilled(!g2.isShapeFilled());
        assertTrue(g1.equals(g2));

        
        g1.setFillPaint(Color.green);
        assertFalse(g1.equals(g2));
        g2.setFillPaint(Color.green);
        assertTrue(g1.equals(g2));

        
        g1.setShapeOutlineVisible(!g1.isShapeOutlineVisible());
        assertFalse(g1.equals(g2));
        g2.setShapeOutlineVisible(!g2.isShapeOutlineVisible());
        assertTrue(g1.equals(g2));

        
        g1.setOutlinePaint(Color.green);
        assertFalse(g1.equals(g2));
        g2.setOutlinePaint(Color.green);
        assertTrue(g1.equals(g2));

        
        g1.setOutlineStroke(new BasicStroke(1.23f));
        assertFalse(g1.equals(g2));
        g2.setOutlineStroke(new BasicStroke(1.23f));
        assertTrue(g1.equals(g2));

        
        g1.setShapeAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertFalse(g1.equals(g2));
        g2.setShapeAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertTrue(g1.equals(g2));

        
        g1.setShapeLocation(RectangleAnchor.BOTTOM_RIGHT);
        assertFalse(g1.equals(g2));
        g2.setShapeLocation(RectangleAnchor.BOTTOM_RIGHT);
        assertTrue(g1.equals(g2));

        
        g1.setLineVisible(!g1.isLineVisible());
        assertFalse(g1.equals(g2));
        g2.setLineVisible(!g2.isLineVisible());
        assertTrue(g1.equals(g2));

        
        g1.setLine(new Line2D.Double(1.0, 2.0, 3.0, 4.0));
        assertFalse(g1.equals(g2));
        g2.setLine(new Line2D.Double(1.0, 2.0, 3.0, 4.0));
        assertTrue(g1.equals(g2));

        
        g1.setLinePaint(Color.green);
        assertFalse(g1.equals(g2));
        g2.setLinePaint(Color.green);
        assertTrue(g1.equals(g2));

        
        g1.setLineStroke(new BasicStroke(1.23f));
        assertFalse(g1.equals(g2));
        g2.setLineStroke(new BasicStroke(1.23f));
        assertTrue(g1.equals(g2));

        
        g1.setFillPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        assertFalse(g1.equals(g2));
        g2.setFillPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_HORIZONTAL));
        assertTrue(g1.equals(g2));

    }

    
    public void testHashcode() {
        LegendGraphic g1 = new LegendGraphic(new Rectangle2D.Double(1.0, 2.0,
                3.0, 4.0), Color.black);
        LegendGraphic g2 = new LegendGraphic(new Rectangle2D.Double(1.0, 2.0,
                3.0, 4.0), Color.black);
        assertTrue(g1.equals(g2));
        int h1 = g1.hashCode();
        int h2 = g2.hashCode();
        assertEquals(h1, h2);
    }

    
    public void testCloning() {
        Rectangle r = new Rectangle(1, 2, 3, 4);
        LegendGraphic g1 = new LegendGraphic(r, Color.black);
        LegendGraphic g2 = null;
        try {
            g2 = (LegendGraphic) g1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(g1 != g2);
        assertTrue(g1.getClass() == g2.getClass());
        assertTrue(g1.equals(g2));

        
        r.setBounds(4, 3, 2, 1);
        assertFalse(g1.equals(g2));
    }

    
    public void testCloning2() {
        Rectangle r = new Rectangle(1, 2, 3, 4);
        LegendGraphic g1 = new LegendGraphic(r, Color.black);
        Line2D l = new Line2D.Double(1.0, 2.0, 3.0, 4.0);
        g1.setLine(l);
        LegendGraphic g2 = null;
        try {
            g2 = (LegendGraphic) g1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(g1 != g2);
        assertTrue(g1.getClass() == g2.getClass());
        assertTrue(g1.equals(g2));

        
        l.setLine(4.0, 3.0, 2.0, 1.0);
        assertFalse(g1.equals(g2));

    }

    
    public void testSerialization() {

        Stroke s = new BasicStroke(1.23f);
        LegendGraphic g1 = new LegendGraphic(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), Color.black);
        g1.setOutlineStroke(s);
        LegendGraphic g2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(g1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray())
            );
            g2 = (LegendGraphic) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(g1.equals(g2));

    }

}
