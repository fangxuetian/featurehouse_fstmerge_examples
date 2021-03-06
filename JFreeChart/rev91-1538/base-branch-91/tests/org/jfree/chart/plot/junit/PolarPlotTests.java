

package org.jfree.chart.plot.junit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Stroke;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;


public class PolarPlotTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(PolarPlotTests.class);
    }

    
    public PolarPlotTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        PolarPlot plot1 = new PolarPlot();
        PolarPlot plot2 = new PolarPlot();
        assertTrue(plot1.equals(plot2));
        assertTrue(plot2.equals(plot1));
        
        plot1.setAngleGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.red,
                3.0f, 4.0f, Color.blue));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.red,
                3.0f, 4.0f, Color.blue));
        assertTrue(plot1.equals(plot2));
        
        Stroke s = new BasicStroke(1.23f);
        plot1.setAngleGridlineStroke(s);
        assertFalse(plot1.equals(plot2));
        plot2.setAngleGridlineStroke(s);
        assertTrue(plot1.equals(plot2));
        
        plot1.setAngleGridlinesVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setAngleGridlinesVisible(false);
        assertTrue(plot1.equals(plot2));
        
        plot1.setAngleLabelFont(new Font("Serif", Font.PLAIN, 9));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleLabelFont(new Font("Serif", Font.PLAIN, 9));
        assertTrue(plot1.equals(plot2));
        
        plot1.setAngleLabelPaint(new GradientPaint(9.0f, 8.0f, Color.blue,
                7.0f, 6.0f, Color.red));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleLabelPaint(new GradientPaint(9.0f, 8.0f, Color.blue,
                7.0f, 6.0f, Color.red));
        assertTrue(plot1.equals(plot2));
        
        plot1.setAngleLabelsVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setAngleLabelsVisible(false);
        assertTrue(plot1.equals(plot2));
        
        plot1.setAxis(new NumberAxis("Test"));
        assertFalse(plot1.equals(plot2));
        plot2.setAxis(new NumberAxis("Test"));
        assertTrue(plot1.equals(plot2));
        
        plot1.setRadiusGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.white,
                3.0f, 4.0f, Color.black));
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.white,
                3.0f, 4.0f, Color.black));
        assertTrue(plot1.equals(plot2));
        
        plot1.setRadiusGridlineStroke(s);
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusGridlineStroke(s);
        assertTrue(plot1.equals(plot2));
        
        plot1.setRadiusGridlinesVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusGridlinesVisible(false);
        assertTrue(plot1.equals(plot2));
        
        plot1.addCornerTextItem("XYZ");
        assertFalse(plot1.equals(plot2));
        plot2.addCornerTextItem("XYZ");
        assertTrue(plot1.equals(plot2));   
    }

    
    public void testCloning() {
        PolarPlot p1 = new PolarPlot();
        PolarPlot p2 = null;
        try {
            p2 = (PolarPlot) p1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(p1 != p2);
        assertTrue(p1.getClass() == p2.getClass());
        assertTrue(p1.equals(p2));
        
        
        p1.addCornerTextItem("XYZ");
        assertFalse(p1.equals(p2));
        p2.addCornerTextItem("XYZ");
        assertTrue(p1.equals(p2));
        
        p1 = new PolarPlot(new DefaultXYDataset(), new NumberAxis("A1"), 
                new DefaultPolarItemRenderer());
        p2 = null;
        try {
            p2 = (PolarPlot) p1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.err.println("Failed to clone.");
        }
        assertTrue(p1 != p2);
        assertTrue(p1.getClass() == p2.getClass());
        assertTrue(p1.equals(p2));
        
        
        p1.getAxis().setLabel("ABC");
        assertFalse(p1.equals(p2));
        p2.getAxis().setLabel("ABC");
        assertTrue(p1.equals(p2));
        
    }

    
    public void testSerialization() {

        PolarPlot p1 = new PolarPlot();
        p1.setAngleGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f,
                4.0f, Color.blue));
        p1.setAngleLabelPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f,
                4.0f, Color.blue));
        p1.setRadiusGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f,
                4.0f, Color.blue));
        PolarPlot p2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(p1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            p2 = (PolarPlot) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(p1, p2);

    }

}
