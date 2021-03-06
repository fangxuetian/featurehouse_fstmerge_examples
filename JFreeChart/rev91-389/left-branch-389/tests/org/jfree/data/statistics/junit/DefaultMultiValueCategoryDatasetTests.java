

package org.jfree.data.statistics.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.UnknownKeyException;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;


public class DefaultMultiValueCategoryDatasetTests extends TestCase {
    
    
    public static Test suite() {
        return new TestSuite(DefaultMultiValueCategoryDatasetTests.class);
    }

    
    public DefaultMultiValueCategoryDatasetTests(String name) {
        super(name);
    }
    
    
    public void testGetValue() {
        DefaultMultiValueCategoryDataset d 
                = new DefaultMultiValueCategoryDataset();
        List values = new ArrayList();
        values.add(new Integer(1));
        values.add(new Integer(2));
        d.add(values, "R1", "C1");
        assertEquals(new Double(1.5), d.getValue("R1", "C1"));
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
        DefaultMultiValueCategoryDataset d 
                = new DefaultMultiValueCategoryDataset();
        boolean pass = false;
        try {
             d.getValue(0, 0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }
        
    
    public void testGetRowCount() {
        DefaultMultiValueCategoryDataset d 
                = new DefaultMultiValueCategoryDataset();
        assertTrue(d.getRowCount() == 0);
        List values = new ArrayList();
        d.add(values, "R1", "C1");
        assertTrue(d.getRowCount() == 1);
        
        d.add(values, "R2", "C1");
        assertTrue(d.getRowCount() == 2);
        
        d.add(values, "R2", "C1");
        assertTrue(d.getRowCount() == 2);
    }

    
    public void testGetColumnCount() {
        DefaultMultiValueCategoryDataset d 
                = new DefaultMultiValueCategoryDataset();
        assertTrue(d.getColumnCount() == 0);
        
        List values = new ArrayList();
        d.add(values, "R1", "C1");
        assertTrue(d.getColumnCount() == 1);
        
        d.add(values, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);
        
        d.add(values, "R1", "C2");
        assertTrue(d.getColumnCount() == 2);

    }

    
    public void testEquals() {
        DefaultMultiValueCategoryDataset d1 
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2 
                = new DefaultMultiValueCategoryDataset();
        assertTrue(d1.equals(d2));
        assertTrue(d2.equals(d1));
        
        List values = new ArrayList();
        d1.add(values, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C1");
        assertTrue(d1.equals(d2));
        
        values.add(new Integer(99));
        d1.add(values, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C1");
        assertTrue(d1.equals(d2));
        
        values.add(new Integer(99));
        d1.add(values, "R1", "C2");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C2");
        assertTrue(d1.equals(d2));        
    }

    
    public void testSerialization() {

        DefaultMultiValueCategoryDataset d1 
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2 
                = new DefaultMultiValueCategoryDataset();

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            d2 = (DefaultMultiValueCategoryDataset) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(d1, d2);

    }
    
    
    public void testAddValue() {
        DefaultMultiValueCategoryDataset d1 
                = new DefaultMultiValueCategoryDataset();
        
        boolean pass = false;
        try {
            d1.add(null, "R1", "C1");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
            
        List values = new ArrayList();
        d1.add(values, "R2", "C1");
        assertEquals(values, d1.getValues("R2", "C1"));
        
        pass = false;
        try {
            d1.add(values, null, "C2");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }
      
    
    public void testCloning() {
        DefaultMultiValueCategoryDataset d1 
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2 = null;
        try {
            d2 = (DefaultMultiValueCategoryDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));
        
        
        List values = new ArrayList();
        values.add(new Integer(99));
        d1.add(values, "R1", "C1");
        try {
            d2 = (DefaultMultiValueCategoryDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));
        
        
        List values2 = new ArrayList();
        values2.add(new Integer(111));
        d1.add(values2, "R2", "C2");
        assertFalse(d1.equals(d2));
        d2.add(values2, "R2", "C2");
        assertTrue(d1.equals(d2));
    }

}
