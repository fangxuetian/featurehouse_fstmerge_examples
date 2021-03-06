

package org.jfree.data.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.util.SortOrder;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedObjects;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.pie.DefaultPieDataset;


public class KeyedObjectsTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(KeyedObjectsTests.class);
    }

    
    public KeyedObjectsTests(String name) {
        super(name);
    }

    
    protected void setUp() {
        
    }

    
    public void testCloning() {
        KeyedObjects ko1 = new KeyedObjects();
        ko1.addObject("V1", new Integer(1));
        ko1.addObject("V2", null);
        ko1.addObject("V3", new Integer(3));
        KeyedObjects ko2 = null;
        try {
            ko2 = (KeyedObjects) ko1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(ko1 != ko2);
        assertTrue(ko1.getClass() == ko2.getClass());
        assertTrue(ko1.equals(ko2));
    }

    
    public void testCloning2() {
        
        Object obj1 = new ArrayList();
        KeyedObjects ko1 = new KeyedObjects();
        ko1.addObject("K1", obj1);
        KeyedObjects ko2 = null;
        try {
            ko2 = (KeyedObjects) ko1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(ko1 != ko2);
        assertTrue(ko1.getClass() == ko2.getClass());
        assertTrue(ko1.equals(ko2));

        
        assertTrue(ko2.getObject("K1") == obj1);

        
        obj1 = new DefaultPieDataset();
        ko1 = new KeyedObjects();
        ko1.addObject("K1", obj1);
        ko2 = null;
        try {
            ko2 = (KeyedObjects) ko1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(ko1 != ko2);
        assertTrue(ko1.getClass() == ko2.getClass());
        assertTrue(ko1.equals(ko2));

        
        assertTrue(ko2.getObject("K1") != obj1);
    }

    
    public void testInsertAndRetrieve() {

        KeyedObjects data = new KeyedObjects();
        data.addObject("A", new Double(1.0));
        data.addObject("B", new Double(2.0));
        data.addObject("C", new Double(3.0));
        data.addObject("D", null);

        
        assertEquals(data.getKey(0), "A");
        assertEquals(data.getKey(1), "B");
        assertEquals(data.getKey(2), "C");
        assertEquals(data.getKey(3), "D");

        
        assertEquals(data.getObject("A"), new Double(1.0));
        assertEquals(data.getObject("B"), new Double(2.0));
        assertEquals(data.getObject("C"), new Double(3.0));
        assertEquals(data.getObject("D"), null);

        boolean pass = false;
        try {
            data.getObject("Not a key");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        
        assertEquals(data.getObject(0), new Double(1.0));
        assertEquals(data.getObject(1), new Double(2.0));
        assertEquals(data.getObject(2), new Double(3.0));
        assertEquals(data.getObject(3), null);

    }

    
    public void testSerialization() {

        KeyedObjects ko1 = new KeyedObjects();
        ko1.addObject("Key 1", "Object 1");
        ko1.addObject("Key 2", null);
        ko1.addObject("Key 3", "Object 2");

        KeyedObjects ko2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(ko1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            ko2 = (KeyedObjects) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(ko1, ko2);

    }

    
    public void testGetObject() {
        
        KeyedObjects ko1 = new KeyedObjects();
        ko1.addObject("Key 1", "Object 1");
        ko1.addObject("Key 2", null);
        ko1.addObject("Key 3", "Object 2");
        assertEquals("Object 1", ko1.getObject(0));
        assertNull(ko1.getObject(1));
        assertEquals("Object 2", ko1.getObject(2));

        
        boolean pass = false;
        try {
            ko1.getObject(-1);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            ko1.getObject(3);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testGetKey() {
        
        KeyedObjects ko1 = new KeyedObjects();
        ko1.addObject("Key 1", "Object 1");
        ko1.addObject("Key 2", null);
        ko1.addObject("Key 3", "Object 2");
        assertEquals("Key 1", ko1.getKey(0));
        assertEquals("Key 2", ko1.getKey(1));
        assertEquals("Key 3", ko1.getKey(2));

        
        boolean pass = false;
        try {
            ko1.getKey(-1);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            ko1.getKey(3);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testGetIndex() {
        KeyedObjects ko1 = new KeyedObjects();
        ko1.addObject("Key 1", "Object 1");
        ko1.addObject("Key 2", null);
        ko1.addObject("Key 3", "Object 2");
        assertEquals(0, ko1.getIndex("Key 1"));
        assertEquals(1, ko1.getIndex("Key 2"));
        assertEquals(2, ko1.getIndex("Key 3"));

        
        boolean pass = false;
        try {
            ko1.getIndex(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testSetObject() {
        KeyedObjects ko1 = new KeyedObjects();
        ko1.setObject("Key 1", "Object 1");
        ko1.setObject("Key 2", null);
        ko1.setObject("Key 3", "Object 2");

        assertEquals("Object 1", ko1.getObject("Key 1"));
        assertEquals(null, ko1.getObject("Key 2"));
        assertEquals("Object 2", ko1.getObject("Key 3"));

        
        ko1.setObject("Key 2", "AAA");
        ko1.setObject("Key 3", "BBB");
        assertEquals("AAA", ko1.getObject("Key 2"));
        assertEquals("BBB", ko1.getObject("Key 3"));

        
        boolean pass = false;
        try {
            ko1.setObject(null, "XX");
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testRemoveValue() {
        KeyedObjects ko1 = new KeyedObjects();
        ko1.setObject("Key 1", "Object 1");
        ko1.setObject("Key 2", null);
        ko1.setObject("Key 3", "Object 2");

        ko1.removeValue(1);
        assertEquals(2, ko1.getItemCount());
        assertEquals(1, ko1.getIndex("Key 3"));

        ko1.removeValue("Key 1");
        assertEquals(1, ko1.getItemCount());
        assertEquals(0, ko1.getIndex("Key 3"));

        
        boolean pass = false;
        try {
            ko1.removeValue("UNKNOWN");
        }
        catch (UnknownKeyException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            ko1.removeValue(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testRemoveValueInt() {
        KeyedObjects ko1 = new KeyedObjects();
        ko1.setObject("Key 1", "Object 1");
        ko1.setObject("Key 2", null);
        ko1.setObject("Key 3", "Object 2");

        ko1.removeValue(1);
        assertEquals(2, ko1.getItemCount());
        assertEquals(1, ko1.getIndex("Key 3"));


        
        boolean pass = false;
        try {
            ko1.removeValue(-1);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            ko1.removeValue(2);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testSortByKeyAscending() {
        KeyedObjects data = new KeyedObjects();
        data.addObject("C", new Double(1.0));
        data.addObject("B", null);
        data.addObject("D", new Double(3.0));
        data.addObject("A", new Double(2.0));

        data.sortByKeys(SortOrder.ASCENDING);

        
        assertEquals("A", data.getKey(0));
        assertEquals("B", data.getKey(1));
        assertEquals("C", data.getKey(2));
        assertEquals("D", data.getKey(3));

        
        assertEquals(data.getObject("A"), new Double(2.0));
        assertEquals(data.getObject("B"), null);
        assertEquals(data.getObject("C"), new Double(1.0));
        assertEquals(data.getObject("D"), new Double(3.0));

        
        assertEquals(data.getObject(0), new Double(2.0));
        assertEquals(data.getObject(1), null);
        assertEquals(data.getObject(2), new Double(1.0));
        assertEquals(data.getObject(3), new Double(3.0));
    }

    
    public void testSortByKeyDescending() {
        KeyedObjects data = new KeyedObjects();
        data.addObject("C", new Double(1.0));
        data.addObject("B", null);
        data.addObject("D", new Double(3.0));
        data.addObject("A", new Double(2.0));

        data.sortByKeys(SortOrder.DESCENDING);

        
        assertEquals(data.getKey(0), "D");
        assertEquals(data.getKey(1), "C");
        assertEquals(data.getKey(2), "B");
        assertEquals(data.getKey(3), "A");

        
        assertEquals(data.getObject("A"), new Double(2.0));
        assertEquals(data.getObject("B"), null);
        assertEquals(data.getObject("C"), new Double(1.0));
        assertEquals(data.getObject("D"), new Double(3.0));

        
        assertEquals(data.getObject(0), new Double(3.0));
        assertEquals(data.getObject(1), new Double(1.0));
        assertEquals(data.getObject(2), null);
        assertEquals(data.getObject(3), new Double(2.0));
    }

    
    public void testSortByValueAscending() {
        KeyedObjects data = new KeyedObjects();
        data.addObject("C", new Double(1.0));
        data.addObject("B", null);
        data.addObject("D", new Double(3.0));
        data.addObject("A", new Double(2.0));

        data.sortByObjects(SortOrder.ASCENDING);

        
        assertEquals(data.getKey(0), "C");
        assertEquals(data.getKey(1), "A");
        assertEquals(data.getKey(2), "D");
        assertEquals(data.getKey(3), "B");

        
        assertEquals(data.getObject("A"), new Double(2.0));
        assertEquals(data.getObject("B"), null);
        assertEquals(data.getObject("C"), new Double(1.0));
        assertEquals(data.getObject("D"), new Double(3.0));

        
        assertEquals(data.getObject(0), new Double(1.0));
        assertEquals(data.getObject(1), new Double(2.0));
        assertEquals(data.getObject(2), new Double(3.0));
        assertEquals(data.getObject(3), null);
    }

    
    public void testSortByValueDescending() {
        KeyedObjects data = new KeyedObjects();
        data.addObject("C", new Double(1.0));
        data.addObject("B", null);
        data.addObject("D", new Double(3.0));
        data.addObject("A", new Double(2.0));

        data.sortByObjects(SortOrder.DESCENDING);

        
        assertEquals(data.getKey(0), "D");
        assertEquals(data.getKey(1), "A");
        assertEquals(data.getKey(2), "C");
        assertEquals(data.getKey(3), "B");

        
        assertEquals(data.getObject("A"), new Double(2.0));
        assertEquals(data.getObject("B"), null);
        assertEquals(data.getObject("C"), new Double(1.0));
        assertEquals(data.getObject("D"), new Double(3.0));

        
        assertEquals(data.getObject(0), new Double(3.0));
        assertEquals(data.getObject(1), new Double(2.0));
        assertEquals(data.getObject(2), new Double(1.0));
        assertEquals(data.getObject(3), null);
    }

}
