

package org.jfree.data.general.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DefaultPieDataset;


public class DefaultPieDatasetTests extends TestCase
    implements DatasetChangeListener {

    private DatasetChangeEvent lastEvent;

    
    public void datasetChanged(DatasetChangeEvent event) {
        this.lastEvent = event;
    }

    
    public static Test suite() {
        return new TestSuite(DefaultPieDatasetTests.class);
    }

    
    public DefaultPieDatasetTests(String name) {
        super(name);
    }

    
    public void testClear() {
        DefaultPieDataset d = new DefaultPieDataset();
        d.addChangeListener(this);
        
        d.clear();
        assertNull(this.lastEvent);
        d.setValue("A", 1.0);
        assertEquals(1, d.getItemCount());
        this.lastEvent = null;
        d.clear();
        assertNotNull(this.lastEvent);
        assertEquals(0, d.getItemCount());
    }

    
    public void testGetKey() {
        DefaultPieDataset d = new DefaultPieDataset();
        d.setValue("A", 1.0);
        d.setValue("B", 2.0);
        assertEquals("A", d.getKey(0));
        assertEquals("B", d.getKey(1));

        boolean pass = false;
        try {
            d.getKey(-1);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            d.getKey(2);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testGetIndex() {
        DefaultPieDataset d = new DefaultPieDataset();
        d.setValue("A", 1.0);
        d.setValue("B", 2.0);
        assertEquals(0, d.getIndex("A"));
        assertEquals(1, d.getIndex("B"));
        assertEquals(-1, d.getIndex("XX"));

        boolean pass = false;
        try {
            d.getIndex(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testCloning() {
        DefaultPieDataset d1 = new DefaultPieDataset();
        d1.setValue("V1", new Integer(1));
        d1.setValue("V2", null);
        d1.setValue("V3", new Integer(3));
        DefaultPieDataset d2 = null;
        try {
            d2 = (DefaultPieDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            System.err.println("Failed to clone.");
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));
    }

    
    public void testSerialization() {

        DefaultPieDataset d1 = new DefaultPieDataset();
        d1.setValue("C1", new Double(234.2));
        d1.setValue("C2", null);
        d1.setValue("C3", new Double(345.9));
        d1.setValue("C4", new Double(452.7));

        DefaultPieDataset d2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray())
            );
            d2 = (DefaultPieDataset) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(d1, d2);

    }

}
