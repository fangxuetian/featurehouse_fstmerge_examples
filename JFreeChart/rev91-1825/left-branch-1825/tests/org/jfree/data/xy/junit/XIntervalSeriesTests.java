

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

import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.general.SeriesChangeListener;
import org.jfree.data.xy.XIntervalSeries;


public class XIntervalSeriesTests extends TestCase
        implements SeriesChangeListener {

    SeriesChangeEvent lastEvent;

    
    public void seriesChanged(SeriesChangeEvent event) {
        this.lastEvent = event;
    }

    
    public static Test suite() {
        return new TestSuite(XIntervalSeriesTests.class);
    }

    
    public XIntervalSeriesTests(String name) {
        super(name);
    }

    
    public void testEquals() {

        XIntervalSeries s1 = new XIntervalSeries("s1");
        XIntervalSeries s2 = new XIntervalSeries("s1");
        assertTrue(s1.equals(s2));

        
        s1 = new XIntervalSeries("s2");
        assertFalse(s1.equals(s2));
        s2 = new XIntervalSeries("s2");
        assertTrue(s1.equals(s2));

        
        s1 = new XIntervalSeries("s2", false, true);
        assertFalse(s1.equals(s2));
        s2 = new XIntervalSeries("s2", false, true);
        assertTrue(s1.equals(s2));

        
        s1 = new XIntervalSeries("s2", false, false);
        assertFalse(s1.equals(s2));
        s2 = new XIntervalSeries("s2", false, false);
        assertTrue(s1.equals(s2));

        
        s1.add(1.0, 0.5, 1.5, 2.0);
        assertFalse(s1.equals(s2));
        s2.add(1.0, 0.5, 1.5, 2.0);
        assertTrue(s2.equals(s1));

        
        s1.add(2.0, 0.5, 1.5, 2.0);
        assertFalse(s1.equals(s2));
        s2.add(2.0, 0.5, 1.5, 2.0);
        assertTrue(s2.equals(s1));

        
        s1.remove(new Double(1.0));
        assertFalse(s1.equals(s2));
        s2.remove(new Double(1.0));
        assertTrue(s2.equals(s1));

    }

    
    public void testCloning() {
        XIntervalSeries s1 = new XIntervalSeries("s1");
        s1.add(1.0, 0.5, 1.5, 2.0);
        XIntervalSeries s2 = null;
        try {
            s2 = (XIntervalSeries) s1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(s1 != s2);
        assertTrue(s1.getClass() == s2.getClass());
        assertTrue(s1.equals(s2));
    }

    
    public void testSerialization() {

        XIntervalSeries s1 = new XIntervalSeries("s1");
        s1.add(1.0, 0.5, 1.5, 2.0);
        XIntervalSeries s2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(s1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            s2 = (XIntervalSeries) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(s1, s2);

    }

    
    public void testIndexOf() {
        XIntervalSeries s1 = new XIntervalSeries("Series 1");
        s1.add(1.0, 1.0, 1.0, 2.0);
        s1.add(2.0, 2.0, 2.0, 3.0);
        s1.add(3.0, 3.0, 3.0, 4.0);
        assertEquals(0, s1.indexOf(new Double(1.0)));
    }

    
    public void testIndexOf2() {
        XIntervalSeries s1 = new XIntervalSeries("Series 1", false, true);
        s1.add(1.0, 1.0, 1.0, 2.0);
        s1.add(3.0, 3.0, 3.0, 3.0);
        s1.add(2.0, 2.0, 2.0, 2.0);
        assertEquals(0, s1.indexOf(new Double(1.0)));
        assertEquals(1, s1.indexOf(new Double(3.0)));
        assertEquals(2, s1.indexOf(new Double(2.0)));
    }

    
    public void testRemove() {
        XIntervalSeries s1 = new XIntervalSeries("Series 1");
        s1.add(1.0, 1.0, 1.0, 2.0);
        s1.add(2.0, 2.0, 2.0, 2.0);
        s1.add(3.0, 3.0, 3.0, 3.0);
        assertEquals(3, s1.getItemCount());

        s1.remove(new Double(2.0));
        assertEquals(new Double(3.0), s1.getX(1));

        s1.remove(new Double(1.0));
        assertEquals(new Double(3.0), s1.getX(0));
    }

    private static final double EPSILON = 0.0000000001;

    
    public void testAdditionOfDuplicateXValues() {
        XIntervalSeries s1 = new XIntervalSeries("Series 1");
        s1.add(1.0, 1.0, 1.0, 1.0);
        s1.add(2.0, 2.0, 2.0, 2.0);
        s1.add(2.0, 3.0, 3.0, 3.0);
        s1.add(2.0, 4.0, 4.0, 4.0);
        s1.add(3.0, 5.0, 5.0, 5.0);
        assertEquals(1.0, s1.getYValue(0), EPSILON);
        assertEquals(2.0, s1.getYValue(1), EPSILON);
        assertEquals(3.0, s1.getYValue(2), EPSILON);
        assertEquals(4.0, s1.getYValue(3), EPSILON);
        assertEquals(5.0, s1.getYValue(4), EPSILON);
    }

    
    public void testAdd() {
        XIntervalSeries series = new XIntervalSeries("Series", false, true);
        series.add(5.0, 5.50, 5.50, 5.50);
        series.add(5.1, 5.51, 5.51, 5.51);
        series.add(6.0, 6.6, 6.6, 6.6);
        series.add(3.0, 3.3, 3.3, 3.3);
        series.add(4.0, 4.4, 4.4, 4.4);
        series.add(2.0, 2.2, 2.2, 2.2);
        series.add(1.0, 1.1, 1.1, 1.1);
        assertEquals(5.5, series.getYValue(0), EPSILON);
        assertEquals(5.51, series.getYValue(1), EPSILON);
        assertEquals(6.6, series.getYValue(2), EPSILON);
        assertEquals(3.3, series.getYValue(3), EPSILON);
        assertEquals(4.4, series.getYValue(4), EPSILON);
        assertEquals(2.2, series.getYValue(5), EPSILON);
        assertEquals(1.1, series.getYValue(6), EPSILON);
    }

    
    public void testSetMaximumItemCount() {
        XIntervalSeries s1 = new XIntervalSeries("S1");
        assertEquals(Integer.MAX_VALUE, s1.getMaximumItemCount());
        s1.setMaximumItemCount(2);
        assertEquals(2, s1.getMaximumItemCount());
        s1.add(1.0, 1.1, 1.1, 1.1);
        s1.add(2.0, 2.2, 2.2, 2.2);
        s1.add(3.0, 3.3, 3.3, 3.3);
        assertEquals(2.0, s1.getX(0).doubleValue(), EPSILON);
        assertEquals(3.0, s1.getX(1).doubleValue(), EPSILON);
    }

    
    public void testSetMaximumItemCount2() {
        XIntervalSeries s1 = new XIntervalSeries("S1");
        s1.add(1.0, 1.1, 1.1, 1.1);
        s1.add(2.0, 2.2, 2.2, 2.2);
        s1.add(3.0, 3.3, 3.3, 3.3);
        s1.setMaximumItemCount(2);
        assertEquals(2.0, s1.getX(0).doubleValue(), EPSILON);
        assertEquals(3.0, s1.getX(1).doubleValue(), EPSILON);
    }

    
    public void testClear() {
        XIntervalSeries s1 = new XIntervalSeries("S1");
        s1.addChangeListener(this);
        s1.clear();
        assertNull(this.lastEvent);
        assertTrue(s1.isEmpty());
        s1.add(1.0, 2.0, 3.0, 4.0);
        assertFalse(s1.isEmpty());
        s1.clear();
        assertNotNull(this.lastEvent);
        assertTrue(s1.isEmpty());
    }

    
    public void testGetXLowValue() {
        XIntervalSeries s1 = new XIntervalSeries("S1");
        s1.add(1.0, 2.0, 3.0, 4.0);
        assertEquals(2.0, s1.getXLowValue(0), EPSILON);
        s1.add(2.0, 1.0, 4.0, 2.5);
        assertEquals(1.0, s1.getXLowValue(1), EPSILON);
    }

    
    public void testGetXHighValue() {
        XIntervalSeries s1 = new XIntervalSeries("S1");
        s1.add(1.0, 2.0, 3.0, 4.0);
        assertEquals(3.0, s1.getXHighValue(0), EPSILON);
        s1.add(2.0, 1.0, 4.0, 2.5);
        assertEquals(4.0, s1.getXHighValue(1), EPSILON);
    }

}
