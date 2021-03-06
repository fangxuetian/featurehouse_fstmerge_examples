

package org.jfree.data.category.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.PublicCloneable;


public class DefaultCategoryDatasetTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(DefaultCategoryDatasetTests.class);
    }

    
    public DefaultCategoryDatasetTests(String name) {
        super(name);
    }

    
    public void testGetValue() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        assertEquals(new Double(1.0), d.getValue("R1", "C1"));
        boolean pass = false;
        try {
            d.getValue("XX", "C1");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            d.getValue("R1", "XX");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testGetValue2() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        boolean pass = false;
        try {
             d.getValue(0, 0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testIncrementValue() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.incrementValue(2.0, "R1", "C1");
        assertEquals(new Double(3.0), d.getValue("R1", "C1"));

        
        d.addValue(null, "R2", "C1");
        d.incrementValue(2.0, "R2", "C1");
        assertEquals(new Double(2.0), d.getValue("R2", "C1"));

        
        boolean pass = false;
        try {
            d.incrementValue(1.0, "XX", "C1");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            d.incrementValue(1.0, "R1", "XX");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testGetRowCount() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        assertTrue(d.getRowCount() == 0);

        d.addValue(1.0, "R1", "C1");
        assertTrue(d.getRowCount() == 1);

        d.addValue(1.0, "R2", "C1");
        assertTrue(d.getRowCount() == 2);

        d.addValue(2.0, "R2", "C1");
        assertTrue(d.getRowCount() == 2);

        
        d.setValue(null, "R2", "C1");
        assertTrue(d.getRowCount() == 2);
    }

    
    public void testGetColumnCount() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        assertTrue(d.getColumnCount() == 0);

        d.addValue(1.0, "R1", "C1");
        assertTrue(d.getColumnCount() == 1);

        d.addValue(1.0, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);

        d.addValue(2.0, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);

        
        d.setValue(null, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);
    }

    
    public void testEquals() {
        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        d1.setValue(23.4, "R1", "C1");
        DefaultCategoryDataset d2 = new DefaultCategoryDataset();
        d2.setValue(23.4, "R1", "C1");
        assertTrue(d1.equals(d2));
        assertTrue(d2.equals(d1));

        d1.setValue(36.5, "R1", "C2");
        assertFalse(d1.equals(d2));
        d2.setValue(36.5, "R1", "C2");
        assertTrue(d1.equals(d2));

        d1.setValue(null, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.setValue(null, "R1", "C1");
        assertTrue(d1.equals(d2));
    }

    
    public void testSerialization() {

        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        d1.setValue(23.4, "R1", "C1");
        DefaultCategoryDataset d2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            d2 = (DefaultCategoryDataset) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(d1, d2);

    }

    
    public void testAddValue() {
        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        d1.addValue(null, "R1", "C1");
        assertNull(d1.getValue("R1", "C1"));
        d1.addValue(new Double(1.0), "R2", "C1");
        assertEquals(new Double(1.0), d1.getValue("R2", "C1"));

        boolean pass = false;
        try {
            d1.addValue(new Double(1.1), null, "C2");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testRemoveValue() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.removeValue("R1", "C1");
        d.addValue(new Double(1.0), "R1", "C1");
        d.removeValue("R1", "C1");
        assertEquals(0, d.getRowCount());
        assertEquals(0, d.getColumnCount());

        d.addValue(new Double(1.0), "R1", "C1");
        d.addValue(new Double(2.0), "R2", "C1");
        d.removeValue("R1", "C1");
        assertEquals(new Double(2.0), d.getValue(0, 0));

        boolean pass = false;
        try {
            d.removeValue(null, "C1");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            d.removeValue("R1", null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testCloning() {
        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        DefaultCategoryDataset d2 = null;
        try {
            d2 = (DefaultCategoryDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));

        
        d1.addValue(1.0, "R1", "C1");
        d1.addValue(2.0, "R1", "C2");
        try {
            d2 = (DefaultCategoryDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));

        
        d1.addValue(3.0, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.addValue(3.0, "R1", "C1");
        assertTrue(d1.equals(d2));
    }

    
    public void testPublicCloneable() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        assertTrue(d instanceof PublicCloneable);
    }

    private static final double EPSILON = 0.0000000001;

    
    public void testBug1835955() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R2", "C2");
        d.removeColumn("C2");
        d.addValue(3.0, "R2", "C2");
        assertEquals(3.0, d.getValue("R2", "C2").doubleValue(), EPSILON);
    }

    
    public void testRemoveColumn() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R2", "C2");
        assertEquals(2, d.getColumnCount());
        d.removeColumn("C2");
        assertEquals(1, d.getColumnCount());

        boolean pass = false;
        try {
            d.removeColumn("XXX");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            d.removeColumn(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testRemoveRow() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R2", "C2");
        assertEquals(2, d.getRowCount());
        d.removeRow("R2");
        assertEquals(1, d.getRowCount());

        boolean pass = false;
        try {
            d.removeRow("XXX");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            d.removeRow(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

}
