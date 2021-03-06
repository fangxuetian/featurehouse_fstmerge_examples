

package org.jfree.chart.renderer.xy.junit;

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

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class XYDotRendererTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(XYDotRendererTests.class);
    }

    
    public XYDotRendererTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        XYDotRenderer r1 = new XYDotRenderer();
        XYDotRenderer r2 = new XYDotRenderer();
        assertEquals(r1, r2);

        r1.setDotWidth(11);
        assertFalse(r1.equals(r2));
        r2.setDotWidth(11);
        assertTrue(r1.equals(r2));

        r1.setDotHeight(12);
        assertFalse(r1.equals(r2));
        r2.setDotHeight(12);
        assertTrue(r1.equals(r2));

        r1.setLegendShape(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertFalse(r1.equals(r2));
        r2.setLegendShape(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertTrue(r1.equals(r2));
    }

    
    public void testHashcode() {
        XYDotRenderer r1 = new XYDotRenderer();
        XYDotRenderer r2 = new XYDotRenderer();
        assertTrue(r1.equals(r2));
        int h1 = r1.hashCode();
        int h2 = r2.hashCode();
        assertEquals(h1, h2);

        r1.setDotHeight(12);
        r2.setDotHeight(12);
        assertTrue(r1.equals(r2));
        h1 = r1.hashCode();
        h2 = r2.hashCode();
        assertEquals(h1, h2);
    }

    
    public void testCloning() {
        XYDotRenderer r1 = new XYDotRenderer();
        XYDotRenderer r2 = null;
        try {
            r2 = (XYDotRenderer) r1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(r1 != r2);
        assertTrue(r1.getClass() == r2.getClass());
        assertTrue(r1.equals(r2));
    }

    
    public void testPublicCloneable() {
        XYDotRenderer r1 = new XYDotRenderer();
        assertTrue(r1 instanceof PublicCloneable);
    }

    
    public void testSerialization() {

        XYDotRenderer r1 = new XYDotRenderer();
        XYDotRenderer r2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(r1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            r2 = (XYDotRenderer) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(r1, r2);

    }

    
    public void testGetLegendItemSeriesIndex() {
        XYSeriesCollection d1 = new XYSeriesCollection();
        XYSeries s1 = new XYSeries("S1");
        s1.add(1.0, 1.1);
        XYSeries s2 = new XYSeries("S2");
        s2.add(1.0, 1.1);
        d1.addSeries(s1);
        d1.addSeries(s2);

        XYSeriesCollection d2 = new XYSeriesCollection();
        XYSeries s3 = new XYSeries("S3");
        s3.add(1.0, 1.1);
        XYSeries s4 = new XYSeries("S4");
        s4.add(1.0, 1.1);
        XYSeries s5 = new XYSeries("S5");
        s5.add(1.0, 1.1);
        d2.addSeries(s3);
        d2.addSeries(s4);
        d2.addSeries(s5);

        XYDotRenderer r = new XYDotRenderer();
        XYPlot plot = new XYPlot(d1, new NumberAxis("x"),
                new NumberAxis("y"), r);
        plot.setDataset(1, d2);
         new JFreeChart(plot);
        LegendItem li = r.getLegendItem(1, 2);
        assertEquals("S5", li.getLabel());
        assertEquals(1, li.getDatasetIndex());
        assertEquals(2, li.getSeriesIndex());
    }

}
