

package org.jfree.chart.renderer.category.junit;

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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;


public class GroupedStackedBarRendererTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(GroupedStackedBarRendererTests.class);
    }

    
    public GroupedStackedBarRendererTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        GroupedStackedBarRenderer r1 = new GroupedStackedBarRenderer();
        GroupedStackedBarRenderer r2 = new GroupedStackedBarRenderer();
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r1));

        
        KeyToGroupMap m1 = new KeyToGroupMap("G1");
        m1.mapKeyToGroup("S1", "G2");
        r1.setSeriesToGroupMap(m1);
        assertFalse(r1.equals(r2));
        KeyToGroupMap m2 = new KeyToGroupMap("G1");
        m2.mapKeyToGroup("S1", "G2");
        r2.setSeriesToGroupMap(m2);
        assertTrue(r1.equals(r2));
    }

    
    public void testCloning() {
        GroupedStackedBarRenderer r1 = new GroupedStackedBarRenderer();
        GroupedStackedBarRenderer r2 = null;
        try {
            r2 = (GroupedStackedBarRenderer) r1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(r1 != r2);
        assertTrue(r1.getClass() == r2.getClass());
        assertTrue(r1.equals(r2));
    }

    
    public void testPublicCloneable() {
        GroupedStackedBarRenderer r1 = new GroupedStackedBarRenderer();
        assertTrue(r1 instanceof PublicCloneable);
    }

    
    public void testSerialization() {

        GroupedStackedBarRenderer r1 = new GroupedStackedBarRenderer();
        GroupedStackedBarRenderer r2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(r1);
            out.close();

            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                    buffer.toByteArray()));
            r2 = (GroupedStackedBarRenderer) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(r1, r2);

    }

    
    public void testDrawWithNullInfo() {
        boolean success = false;
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            dataset.addValue(1.0, "S1", "C1");
            dataset.addValue(2.0, "S1", "C2");
            dataset.addValue(3.0, "S2", "C1");
            dataset.addValue(4.0, "S2", "C2");
            GroupedStackedBarRenderer renderer
                    = new GroupedStackedBarRenderer();
            CategoryPlot plot = new CategoryPlot(dataset,
                    new CategoryAxis("Category"), new NumberAxis("Value"),
                    renderer);
            JFreeChart chart = new JFreeChart(plot);
             chart.createBufferedImage(300, 200,
                    null);
            success = true;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            success = false;
        }
        assertTrue(success);
    }

}
