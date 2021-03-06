

package org.jfree.data.statistics.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.Range;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;


public class DefaultBoxAndWhiskerXYDatasetTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(DefaultBoxAndWhiskerXYDatasetTests.class);
    }

    
    public DefaultBoxAndWhiskerXYDatasetTests(String name) {
        super(name);
    }

    
    public void testEquals() {
        DefaultBoxAndWhiskerXYDataset d1 = new DefaultBoxAndWhiskerXYDataset(
                "Series");
        DefaultBoxAndWhiskerXYDataset d2 = new DefaultBoxAndWhiskerXYDataset(
                "Series");
        assertTrue(d1.equals(d2));

        d1.add(new Date(1L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        assertFalse(d1.equals(d2));
        d2.add(new Date(1L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        assertTrue(d1.equals(d2));
    }

    
    public void testSerialization() {

        DefaultBoxAndWhiskerXYDataset d1 = new DefaultBoxAndWhiskerXYDataset(
                "Series");
        d1.add(new Date(1L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        DefaultBoxAndWhiskerXYDataset d2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                    buffer.toByteArray()));
            d2 = (DefaultBoxAndWhiskerXYDataset) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(d1, d2);

        
        d1.add(new Date(2L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        assertFalse(d1.equals(d2));
    }

    
    public void testCloning() {
        DefaultBoxAndWhiskerXYDataset d1 = new DefaultBoxAndWhiskerXYDataset(
                "Series");
        d1.add(new Date(1L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        DefaultBoxAndWhiskerXYDataset d2 = null;
        try {
            d2 = (DefaultBoxAndWhiskerXYDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));

        
        d1.add(new Date(2L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        assertFalse(d1.equals(d2));
    }

    private static final double EPSILON = 0.0000000001;

    
    public void testAdd() {
        DefaultBoxAndWhiskerXYDataset dataset
                = new DefaultBoxAndWhiskerXYDataset("S1");
        BoxAndWhiskerItem item1 = new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0,
                5.0, 6.0, 7.0, 8.0, new ArrayList());
        dataset.add(new Date(33L), item1);

        assertEquals(1.0, dataset.getY(0, 0).doubleValue(), EPSILON);
        assertEquals(1.0, dataset.getMeanValue(0, 0).doubleValue(), EPSILON);
        assertEquals(2.0, dataset.getMedianValue(0, 0).doubleValue(), EPSILON);
        assertEquals(3.0, dataset.getQ1Value(0, 0).doubleValue(), EPSILON);
        assertEquals(4.0, dataset.getQ3Value(0, 0).doubleValue(), EPSILON);
        assertEquals(5.0, dataset.getMinRegularValue(0, 0).doubleValue(),
                EPSILON);
        assertEquals(6.0, dataset.getMaxRegularValue(0, 0).doubleValue(),
                EPSILON);
        assertEquals(7.0, dataset.getMinOutlier(0, 0).doubleValue(), EPSILON);
        assertEquals(8.0, dataset.getMaxOutlier(0, 0).doubleValue(), EPSILON);
        assertEquals(new Range(5.0, 6.0), dataset.getRangeBounds(false));
    }

    
    public void testConstructor() {
        DefaultBoxAndWhiskerXYDataset dataset
                = new DefaultBoxAndWhiskerXYDataset("S1");
        assertEquals(1, dataset.getSeriesCount());
        assertEquals(0, dataset.getItemCount(0));
        assertTrue(Double.isNaN(dataset.getRangeLowerBound(false)));
        assertTrue(Double.isNaN(dataset.getRangeUpperBound(false)));
    }

    
    public void testGetRangeBounds() {
        DefaultBoxAndWhiskerXYDataset d1
                = new DefaultBoxAndWhiskerXYDataset("S");
        d1.add(new Date(1L), new BoxAndWhiskerItem(1.0, 2.0, 3.0, 4.0, 5.0,
                6.0, 7.0, 8.0, new ArrayList()));
        assertEquals(new Range(5.0, 6.0), d1.getRangeBounds(false));
        assertEquals(new Range(5.0, 6.0), d1.getRangeBounds(true));

        d1.add(new Date(1L), new BoxAndWhiskerItem(1.5, 2.5, 3.5, 4.5, 5.5,
                6.5, 7.5, 8.5, new ArrayList()));
        assertEquals(new Range(5.0, 6.5), d1.getRangeBounds(false));
        assertEquals(new Range(5.0, 6.5), d1.getRangeBounds(true));

        d1.add(new Date(2L), new BoxAndWhiskerItem(2.5, 3.5, 4.5, 5.5, 6.5,
                7.5, 8.5, 9.5, new ArrayList()));
        assertEquals(new Range(5.0, 7.5), d1.getRangeBounds(false));
        assertEquals(new Range(5.0, 7.5), d1.getRangeBounds(true));
    }


}

