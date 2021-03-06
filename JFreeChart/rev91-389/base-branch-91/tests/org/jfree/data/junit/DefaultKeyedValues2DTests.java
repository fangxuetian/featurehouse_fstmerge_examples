

package org.jfree.data.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.UnknownKeyException;


public class DefaultKeyedValues2DTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(DefaultKeyedValues2DTests.class);
    }
    
    
    public DefaultKeyedValues2DTests(String name) {
        super(name);
    }

    
    public void testGetValue() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        d.addValue(new Double(1.0), "R1", "C1");
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
    
    
    public void testCloning() {
        DefaultKeyedValues2D v1 = new DefaultKeyedValues2D();
        v1.setValue(new Integer(1), "V1", "C1");
        v1.setValue(null, "V2", "C1");
        v1.setValue(new Integer(3), "V3", "C2");
        DefaultKeyedValues2D v2 = null;
        try {
            v2 = (DefaultKeyedValues2D) v1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(v1 != v2);
        assertTrue(v1.getClass() == v2.getClass());
        assertTrue(v1.equals(v2));
        
        
        v2.setValue(new Integer(2), "V2", "C1");
        assertFalse(v1.equals(v2));
    }
    
    
    public void testSerialization() {

        DefaultKeyedValues2D kv2D1 = new DefaultKeyedValues2D();
        kv2D1.addValue(new Double(234.2), "Row1", "Col1");
        kv2D1.addValue(null, "Row1", "Col2");
        kv2D1.addValue(new Double(345.9), "Row2", "Col1");
        kv2D1.addValue(new Double(452.7), "Row2", "Col2");

        DefaultKeyedValues2D kv2D2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(kv2D1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            kv2D2 = (DefaultKeyedValues2D) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(kv2D1, kv2D2);

    }
    
    
    public void testEquals() {
        DefaultKeyedValues2D d1 = new DefaultKeyedValues2D();
        DefaultKeyedValues2D d2 = new DefaultKeyedValues2D();
        assertTrue(d1.equals(d2));
        assertTrue(d2.equals(d1));
        
        d1.addValue(new Double(1.0), new Double(2.0), "S1");
        assertFalse(d1.equals(d2));
        d2.addValue(new Double(1.0), new Double(2.0), "S1");
        assertTrue(d1.equals(d2));
    }
    
    
    public void testSparsePopulation() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        d.addValue(new Integer(11), "R1", "C1");
        d.addValue(new Integer(22), "R2", "C2");
        
        assertEquals(new Integer(11), d.getValue("R1", "C1"));
        assertNull(d.getValue("R1", "C2"));
        assertEquals(new Integer(22), d.getValue("R2", "C2"));
        assertNull(d.getValue("R2", "C1"));
    }
    
    
    public void testRowCount() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        assertEquals(0, d.getRowCount());
        d.addValue(new Double(1.0), "R1", "C1");
        assertEquals(1, d.getRowCount());
        d.addValue(new Double(2.0), "R2", "C1");
        assertEquals(2, d.getRowCount());
    }

    
    public void testColumnCount() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        assertEquals(0, d.getColumnCount());
        d.addValue(new Double(1.0), "R1", "C1");
        assertEquals(1, d.getColumnCount());
        d.addValue(new Double(2.0), "R1", "C2");
        assertEquals(2, d.getColumnCount());
    }
    
    private static final double EPSILON = 0.0000000001;
    
    
    public void testGetValue2() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        boolean pass = false;
        try {
            d.getValue(0, 0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
        d.addValue(new Double(1.0), "R1", "C1");
        assertEquals(1.0, d.getValue(0, 0).doubleValue(), EPSILON);
        d.addValue(new Double(2.0), "R2", "C2");
        assertEquals(2.0, d.getValue(1, 1).doubleValue(), EPSILON);
        assertNull(d.getValue(1, 0));
        assertNull(d.getValue(0, 1));
        
        pass = false;
        try {
            d.getValue(2, 0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }
    
    
    public void testGetRowKey() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        boolean pass = false;
        try {
            d.getRowKey(0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
        d.addValue(new Double(1.0), "R1", "C1");
        d.addValue(new Double(1.0), "R2", "C1");
        assertEquals("R1", d.getRowKey(0));
        assertEquals("R2", d.getRowKey(1));
        
        
        d = new DefaultKeyedValues2D(true);
        d.addValue(new Double(1.0), "R1", "C1");
        assertEquals("R1", d.getRowKey(0));
        d.addValue(new Double(0.0), "R0", "C1");
        assertEquals("R0", d.getRowKey(0));
        assertEquals("R1", d.getRowKey(1));
    }
    
    
    public void testGetColumnKey() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        boolean pass = false;
        try {
            d.getColumnKey(0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
        d.addValue(new Double(1.0), "R1", "C1");
        d.addValue(new Double(1.0), "R1", "C2");
        assertEquals("C1", d.getColumnKey(0));
        assertEquals("C2", d.getColumnKey(1));
    }
    
    
    public void testRemoveValue() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        d.removeValue("R1", "C1");
        d.addValue(new Double(1.0), "R1", "C1");
        d.removeValue("R1", "C1");
        assertEquals(0, d.getRowCount());
        assertEquals(0, d.getColumnCount());
        
        d.addValue(new Double(1.0), "R1", "C1");
        d.addValue(new Double(2.0), "R2", "C1");
        d.removeValue("R1", "C1");
        assertEquals(new Double(2.0), d.getValue(0, 0));
    }
    
    
    public void testRemoveValueBug1690654() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        d.addValue(new Double(1.0), "R1", "C1");
        d.addValue(new Double(2.0), "R2", "C2");
        assertEquals(2, d.getColumnCount());
        assertEquals(2, d.getRowCount());
        d.removeValue("R2", "C2");
        assertEquals(1, d.getColumnCount());
        assertEquals(1, d.getRowCount());
        assertEquals(new Double(1.0), d.getValue(0, 0));
    }
    
    
    public void testRemoveRow() {
        DefaultKeyedValues2D d = new DefaultKeyedValues2D();
        boolean pass = false;
        try {
            d.removeRow(0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);        
    }

}
