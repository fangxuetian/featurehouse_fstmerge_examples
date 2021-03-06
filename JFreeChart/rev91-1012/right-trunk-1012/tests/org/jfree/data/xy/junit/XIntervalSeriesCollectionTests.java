

package org.jfree.data.xy.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.xy.XIntervalSeries;
import org.jfree.data.xy.XIntervalSeriesCollection;


public class XIntervalSeriesCollectionTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(XIntervalSeriesCollectionTests.class);
    }

    
    public XIntervalSeriesCollectionTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        XIntervalSeriesCollection c1 = new XIntervalSeriesCollection();
        XIntervalSeriesCollection c2 = new XIntervalSeriesCollection();
        assertEquals(c1, c2);
        
        
        XIntervalSeries s1 = new XIntervalSeries("Series");
        s1.add(1.0, 1.1, 1.2, 1.3);
        c1.addSeries(s1);
        assertFalse(c1.equals(c2));
        XIntervalSeries s2 = new XIntervalSeries("Series");
        s2.add(1.0, 1.1, 1.2, 1.3);
        c2.addSeries(s2);
        assertTrue(c1.equals(c2));
        
        
        c1.addSeries(new XIntervalSeries("Empty Series"));
        assertFalse(c1.equals(c2));
        c2.addSeries(new XIntervalSeries("Empty Series"));
        assertTrue(c1.equals(c2));
    }

    
    public void testCloning() {
        XIntervalSeriesCollection c1 = new XIntervalSeriesCollection();
        XIntervalSeries s1 = new XIntervalSeries("Series");
        s1.add(1.0, 1.1, 1.2, 1.3);
        c1.addSeries(s1);
        XIntervalSeriesCollection c2 = null;
        try {
            c2 = (XIntervalSeriesCollection) c1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(c1 != c2);
        assertTrue(c1.getClass() == c2.getClass());
        assertTrue(c1.equals(c2));
        
        
        s1.setDescription("XYZ");
        assertFalse(c1.equals(c2));
    }

    
    public void testSerialization() {
        XIntervalSeriesCollection c1 = new XIntervalSeriesCollection();
        XIntervalSeries s1 = new XIntervalSeries("Series");
        s1.add(1.0, 1.1, 1.2, 1.3);
        XIntervalSeriesCollection c2 = null;
        
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(c1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            c2 = (XIntervalSeriesCollection) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(c1, c2);
    }
    
    
    public void test1170825() {
        XIntervalSeries s1 = new XIntervalSeries("Series1");
        XIntervalSeriesCollection dataset = new XIntervalSeriesCollection();
        dataset.addSeries(s1);
        try {
             dataset.getSeries(1);
        }
        catch (IllegalArgumentException e) {
            
        }
        catch (IndexOutOfBoundsException e) {
            assertTrue(false);  
        }
    }
    
}
