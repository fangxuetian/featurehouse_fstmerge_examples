

package org.jfree.chart.labels.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.labels.MultipleXYSeriesLabelGenerator;


public class MultipleXYSeriesLabelGeneratorTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(MultipleXYSeriesLabelGeneratorTests.class);
    }

    
    public MultipleXYSeriesLabelGeneratorTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        MultipleXYSeriesLabelGenerator g1 
                = new MultipleXYSeriesLabelGenerator();
        MultipleXYSeriesLabelGenerator g2 
                = new MultipleXYSeriesLabelGenerator();
        assertTrue(g1.equals(g2));
        assertTrue(g2.equals(g1)); 
        
        g1 = new MultipleXYSeriesLabelGenerator("Series {0}");
        assertFalse(g1.equals(g2));
        g2 = new MultipleXYSeriesLabelGenerator("Series {0}");
        assertTrue(g1.equals(g2));
        
        g1.addSeriesLabel(1, "Additional 1");
        assertFalse(g1.equals(g2));
        g2.addSeriesLabel(1, "Additional 1");
        assertTrue(g1.equals(g2));
    }

    
    public void testCloning() {
        MultipleXYSeriesLabelGenerator g1 
                = new MultipleXYSeriesLabelGenerator();
        MultipleXYSeriesLabelGenerator g2 = null;
        try {
            g2 = (MultipleXYSeriesLabelGenerator) g1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(g1 != g2);
        assertTrue(g1.getClass() == g2.getClass());
        assertTrue(g1.equals(g2));
        
        
        g1.addSeriesLabel(3, "Add3");
        assertFalse(g1.equals(g2));
        g2.addSeriesLabel(3, "Add3");
        assertTrue(g1.equals(g2));
    }

    
    public void testSerialization() {

        MultipleXYSeriesLabelGenerator g1 
                = new MultipleXYSeriesLabelGenerator();
        g1.addSeriesLabel(0, "Add0");
        g1.addSeriesLabel(0, "Add0b");
        g1.addSeriesLabel(1, "Add1");
        MultipleXYSeriesLabelGenerator g2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(g1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            g2 = (MultipleXYSeriesLabelGenerator) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(g1, g2);

    }

}
