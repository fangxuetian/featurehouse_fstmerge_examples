

package org.jfree.data.time.junit;

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
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.Month;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.Year;
import org.jfree.date.MonthConstants;


public class TimeSeriesTests extends TestCase implements SeriesChangeListener {

    
    private TimeSeries seriesA;

    
    private TimeSeries seriesB;

    
    private TimeSeries seriesC;

    
    private boolean gotSeriesChangeEvent = false;

    
    public static Test suite() {
        return new TestSuite(TimeSeriesTests.class);
    }

    
    public TimeSeriesTests(String name) {
        super(name);
    }

    
    protected void setUp() {

        this.seriesA = new TimeSeries("Series A", Year.class);
        try {
            this.seriesA.add(new Year(2000), new Integer(102000));
            this.seriesA.add(new Year(2001), new Integer(102001));
            this.seriesA.add(new Year(2002), new Integer(102002));
            this.seriesA.add(new Year(2003), new Integer(102003));
            this.seriesA.add(new Year(2004), new Integer(102004));
            this.seriesA.add(new Year(2005), new Integer(102005));
        }
        catch (SeriesException e) {
            System.err.println("Problem creating series.");
        }

        this.seriesB = new TimeSeries("Series B", Year.class);
        try {
            this.seriesB.add(new Year(2006), new Integer(202006));
            this.seriesB.add(new Year(2007), new Integer(202007));
            this.seriesB.add(new Year(2008), new Integer(202008));
        }
        catch (SeriesException e) {
            System.err.println("Problem creating series.");
        }

        this.seriesC = new TimeSeries("Series C", Year.class);
        try {
            this.seriesC.add(new Year(1999), new Integer(301999));
            this.seriesC.add(new Year(2000), new Integer(302000));
            this.seriesC.add(new Year(2002), new Integer(302002));
        }
        catch (SeriesException e) {
            System.err.println("Problem creating series.");
        }

    }

    
    public void seriesChanged(SeriesChangeEvent event) {
        this.gotSeriesChangeEvent = true;
    }

    
    public void testClone() {

        TimeSeries series = new TimeSeries("Test Series");

        RegularTimePeriod jan1st2002 = new Day(1, MonthConstants.JANUARY, 2002);
        try {
            series.add(jan1st2002, new Integer(42));
        }
        catch (SeriesException e) {
            System.err.println("Problem adding to series.");
        }

        TimeSeries clone = null;
        try {
            clone = (TimeSeries) series.clone();
            clone.setKey("Clone Series");
            try {
                clone.update(jan1st2002, new Integer(10));
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }

        int seriesValue = series.getValue(jan1st2002).intValue();
        int cloneValue = Integer.MAX_VALUE;
        if (clone != null) {
            cloneValue = clone.getValue(jan1st2002).intValue();
        }

        assertEquals(42, seriesValue);
        assertEquals(10, cloneValue);
        assertEquals("Test Series", series.getKey());
        if (clone != null) {
            assertEquals("Clone Series", clone.getKey());
        }
        else {
            assertTrue(false);
        }

    }

    
    public void testClone2() {
        TimeSeries s1 = new TimeSeries("S1", Year.class);
        s1.add(new Year(2007), 100.0);
        s1.add(new Year(2008), null);
        s1.add(new Year(2009), 200.0);
        TimeSeries s2 = null;
        try {
            s2 = (TimeSeries) s1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(s1.equals(s2));

        
        s2.addOrUpdate(new Year(2009), 300.0);
        assertFalse(s1.equals(s2));
        s1.addOrUpdate(new Year(2009), 300.0);
        assertTrue(s1.equals(s2));
    }

    
    public void testAddValue() {

        try {
            this.seriesA.add(new Year(1999), new Integer(1));
        }
        catch (SeriesException e) {
            System.err.println("Problem adding to series.");
        }

        int value = this.seriesA.getValue(0).intValue();
        assertEquals(1, value);

    }

    
    public void testGetValue() {

        Number value1 = this.seriesA.getValue(new Year(1999));
        assertNull(value1);
        int value2 = this.seriesA.getValue(new Year(2000)).intValue();
        assertEquals(102000, value2);

    }

    
    public void testDelete() {
        this.seriesA.delete(0, 0);
        assertEquals(5, this.seriesA.getItemCount());
        Number value = this.seriesA.getValue(new Year(2000));
        assertNull(value);
    }

    
    public void testDelete2() {
        TimeSeries s1 = new TimeSeries("Series", Year.class);
        s1.add(new Year(2000), 13.75);
        s1.add(new Year(2001), 11.90);
        s1.add(new Year(2002), null);
        s1.addChangeListener(this);
        this.gotSeriesChangeEvent = false;
        s1.delete(new Year(2001));
        assertTrue(this.gotSeriesChangeEvent);
        assertEquals(2, s1.getItemCount());
        assertEquals(null, s1.getValue(new Year(2001)));

        
        this.gotSeriesChangeEvent = false;
        s1.delete(new Year(2006));
        assertFalse(this.gotSeriesChangeEvent);

        
        try {
            s1.delete(null);
            fail("Expected IllegalArgumentException.");
        }
        catch (IllegalArgumentException e) {
            
        }
    }

    
    public void testDelete3() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.add(new Year(2011), 1.1);
        s1.add(new Year(2012), 2.2);
        s1.add(new Year(2013), 3.3);
        s1.add(new Year(2014), 4.4);
        s1.add(new Year(2015), 5.5);
        s1.add(new Year(2016), 6.6);
        s1.delete(2, 5);
        assertEquals(2, s1.getItemCount());
        assertEquals(new Year(2011), s1.getTimePeriod(0));
        assertEquals(new Year(2012), s1.getTimePeriod(1));
        assertEquals(1.1, s1.getMinY(), EPSILON);
        assertEquals(2.2, s1.getMaxY(), EPSILON);
    }

    
    public void testDelete_RegularTimePeriod() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.add(new Year(2010), 1.1);
        s1.add(new Year(2011), 2.2);
        s1.add(new Year(2012), 3.3);
        s1.add(new Year(2013), 4.4);
        s1.delete(new Year(2010));
        s1.delete(new Year(2013));
        assertEquals(2.2, s1.getMinY(), EPSILON);
        assertEquals(3.3, s1.getMaxY(), EPSILON);
    }

    
    public void testSerialization() {
        TimeSeries s1 = new TimeSeries("A test");
        s1.add(new Year(2000), 13.75);
        s1.add(new Year(2001), 11.90);
        s1.add(new Year(2002), null);
        s1.add(new Year(2005), 19.32);
        s1.add(new Year(2007), 16.89);
        TimeSeries s2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(s1);
            out.close();
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                    buffer.toByteArray()));
            s2 = (TimeSeries) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(s1.equals(s2));
    }

    
    public void testEquals() {
        TimeSeries s1 = new TimeSeries("Time Series 1");
        TimeSeries s2 = new TimeSeries("Time Series 2");
        boolean b1 = s1.equals(s2);
        assertFalse("b1", b1);

        s2.setKey("Time Series 1");
        boolean b2 = s1.equals(s2);
        assertTrue("b2", b2);

        RegularTimePeriod p1 = new Day();
        RegularTimePeriod p2 = p1.next();
        s1.add(p1, 100.0);
        s1.add(p2, 200.0);
        boolean b3 = s1.equals(s2);
        assertFalse("b3", b3);

        s2.add(p1, 100.0);
        s2.add(p2, 200.0);
        boolean b4 = s1.equals(s2);
        assertTrue("b4", b4);

        s1.setMaximumItemCount(100);
        boolean b5 = s1.equals(s2);
        assertFalse("b5", b5);

        s2.setMaximumItemCount(100);
        boolean b6 = s1.equals(s2);
        assertTrue("b6", b6);

        s1.setMaximumItemAge(100);
        boolean b7 = s1.equals(s2);
        assertFalse("b7", b7);

        s2.setMaximumItemAge(100);
        boolean b8 = s1.equals(s2);
        assertTrue("b8", b8);
    }

    
    public void testEquals2() {
        TimeSeries s1 = new TimeSeries("Series", null, null, Day.class);
        TimeSeries s2 = new TimeSeries("Series", null, null, Day.class);
        assertTrue(s1.equals(s2));
    }

    
    public void testEquals3() {
        TimeSeries s1 = new TimeSeries("Series", Day.class);
        TimeSeries s2 = new TimeSeries("Series", Month.class);
        assertFalse(s1.equals(s2));
    }

    
    public void testCreateCopy1() {

        TimeSeries series = new TimeSeries("Series", Month.class);
        series.add(new Month(MonthConstants.JANUARY, 2003), 45.0);
        series.add(new Month(MonthConstants.FEBRUARY, 2003), 55.0);
        series.add(new Month(MonthConstants.JUNE, 2003), 35.0);
        series.add(new Month(MonthConstants.NOVEMBER, 2003), 85.0);
        series.add(new Month(MonthConstants.DECEMBER, 2003), 75.0);

        try {
            
            TimeSeries result1 = series.createCopy(
                    new Month(MonthConstants.NOVEMBER, 2002),
                    new Month(MonthConstants.DECEMBER, 2002));
            assertEquals(0, result1.getItemCount());

            
            TimeSeries result2 = series.createCopy(
                    new Month(MonthConstants.NOVEMBER, 2002),
                    new Month(MonthConstants.JANUARY, 2003));
            assertEquals(1, result2.getItemCount());

            
            
            TimeSeries result3 = series.createCopy(
                    new Month(MonthConstants.NOVEMBER, 2002),
                    new Month(MonthConstants.APRIL, 2003));
            assertEquals(2, result3.getItemCount());

            TimeSeries result4 = series.createCopy(
                    new Month(MonthConstants.NOVEMBER, 2002),
                    new Month(MonthConstants.DECEMBER, 2003));
            assertEquals(5, result4.getItemCount());

            TimeSeries result5 = series.createCopy(
                    new Month(MonthConstants.NOVEMBER, 2002),
                    new Month(MonthConstants.MARCH, 2004));
            assertEquals(5, result5.getItemCount());

            TimeSeries result6 = series.createCopy(
                    new Month(MonthConstants.JANUARY, 2003),
                    new Month(MonthConstants.JANUARY, 2003));
            assertEquals(1, result6.getItemCount());

            TimeSeries result7 = series.createCopy(
                    new Month(MonthConstants.JANUARY, 2003),
                    new Month(MonthConstants.APRIL, 2003));
            assertEquals(2, result7.getItemCount());

            TimeSeries result8 = series.createCopy(
                    new Month(MonthConstants.JANUARY, 2003),
                    new Month(MonthConstants.DECEMBER, 2003));
            assertEquals(5, result8.getItemCount());

            TimeSeries result9 = series.createCopy(
                    new Month(MonthConstants.JANUARY, 2003),
                    new Month(MonthConstants.MARCH, 2004));
            assertEquals(5, result9.getItemCount());

            TimeSeries result10 = series.createCopy(
                    new Month(MonthConstants.MAY, 2003),
                    new Month(MonthConstants.DECEMBER, 2003));
            assertEquals(3, result10.getItemCount());

            TimeSeries result11 = series.createCopy(
                    new Month(MonthConstants.MAY, 2003),
                    new Month(MonthConstants.MARCH, 2004));
            assertEquals(3, result11.getItemCount());

            TimeSeries result12 = series.createCopy(
                    new Month(MonthConstants.DECEMBER, 2003),
                    new Month(MonthConstants.DECEMBER, 2003));
            assertEquals(1, result12.getItemCount());

            TimeSeries result13 = series.createCopy(
                    new Month(MonthConstants.DECEMBER, 2003),
                    new Month(MonthConstants.MARCH, 2004));
            assertEquals(1, result13.getItemCount());

            TimeSeries result14 = series.createCopy(
                    new Month(MonthConstants.JANUARY, 2004),
                    new Month(MonthConstants.MARCH, 2004));
            assertEquals(0, result14.getItemCount());
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }

    }

    
    public void testCreateCopy2() {

        TimeSeries series = new TimeSeries("Series", Month.class);
        series.add(new Month(MonthConstants.JANUARY, 2003), 45.0);
        series.add(new Month(MonthConstants.FEBRUARY, 2003), 55.0);
        series.add(new Month(MonthConstants.JUNE, 2003), 35.0);
        series.add(new Month(MonthConstants.NOVEMBER, 2003), 85.0);
        series.add(new Month(MonthConstants.DECEMBER, 2003), 75.0);

        try {
            
            TimeSeries result1 = series.createCopy(0, 0);
            assertEquals(new Month(1, 2003), result1.getTimePeriod(0));

            
            result1 = series.createCopy(0, 1);
            assertEquals(new Month(2, 2003), result1.getTimePeriod(1));

            
            result1 = series.createCopy(1, 3);
            assertEquals(new Month(2, 2003), result1.getTimePeriod(0));
            assertEquals(new Month(11, 2003), result1.getTimePeriod(2));

            
            result1 = series.createCopy(3, 4);
            assertEquals(new Month(11, 2003), result1.getTimePeriod(0));
            assertEquals(new Month(12, 2003), result1.getTimePeriod(1));

            
            result1 = series.createCopy(4, 4);
            assertEquals(new Month(12, 2003), result1.getTimePeriod(0));
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }

        
        boolean pass = false;
        try {
             series.createCopy(-1, 1);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        catch (CloneNotSupportedException e) {
            pass = false;
        }
        assertTrue(pass);

        
        pass = false;
        try {
             series.createCopy(1, 0);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        catch (CloneNotSupportedException e) {
            pass = false;
        }
        assertTrue(pass);

        TimeSeries series2 = new TimeSeries("Series 2");
        try {
            TimeSeries series3 = series2.createCopy(99, 999);
            assertEquals(0, series3.getItemCount());
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }
    }

    
    public void testCreateCopy3() throws CloneNotSupportedException {
        TimeSeries s1 = new TimeSeries("S1");
        s1.add(new Year(2009), 100.0);
        s1.add(new Year(2010), 101.0);
        s1.add(new Year(2011), 102.0);
        assertEquals(100.0, s1.getMinY(), EPSILON);
        assertEquals(102.0, s1.getMaxY(), EPSILON);
        
        TimeSeries s2 = s1.createCopy(0, 1);
        assertEquals(100.0, s2.getMinY(), EPSILON);
        assertEquals(101.0, s2.getMaxY(), EPSILON);

        TimeSeries s3 = s1.createCopy(1, 2);
        assertEquals(101.0, s3.getMinY(), EPSILON);
        assertEquals(102.0, s3.getMaxY(), EPSILON);
    }

    
    public void testSetMaximumItemCount() {
        TimeSeries s1 = new TimeSeries("S1", Year.class);
        s1.add(new Year(2000), 13.75);
        s1.add(new Year(2001), 11.90);
        s1.add(new Year(2002), null);
        s1.add(new Year(2005), 19.32);
        s1.add(new Year(2007), 16.89);
        assertTrue(s1.getItemCount() == 5);

        s1.setMaximumItemCount(3);
        assertTrue(s1.getItemCount() == 3);
        TimeSeriesDataItem item = s1.getDataItem(0);
        assertTrue(item.getPeriod().equals(new Year(2002)));
        assertEquals(16.89, s1.getMinY(), EPSILON);
        assertEquals(19.32, s1.getMaxY(), EPSILON);
    }

    
    public void testAddOrUpdate() {
        TimeSeries s1 = new TimeSeries("S1", Year.class);
        s1.setMaximumItemCount(2);
        s1.addOrUpdate(new Year(2000), 100.0);
        assertEquals(1, s1.getItemCount());
        s1.addOrUpdate(new Year(2001), 101.0);
        assertEquals(2, s1.getItemCount());
        s1.addOrUpdate(new Year(2001), 102.0);
        assertEquals(2, s1.getItemCount());
        s1.addOrUpdate(new Year(2002), 103.0);
        assertEquals(2, s1.getItemCount());
    }

    
    public void testAddOrUpdate2() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.setMaximumItemCount(2);
        s1.addOrUpdate(new Year(2010), 1.1);
        s1.addOrUpdate(new Year(2011), 2.2);
        s1.addOrUpdate(new Year(2012), 3.3);
        assertEquals(2, s1.getItemCount());
        assertEquals(2.2, s1.getMinY(), EPSILON);
        assertEquals(3.3, s1.getMaxY(), EPSILON);
    }

    
    public void testAddOrUpdate3() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.addOrUpdate(new Year(2010), 1.1);
        assertEquals(Year.class, s1.getTimePeriodClass());

        boolean pass = false;
        try {
            s1.addOrUpdate(new Month(1, 2009), 0.0);
        }
        catch (SeriesException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testAddOrUpdate4() {
        TimeSeries ts = new TimeSeries("S");
        TimeSeriesDataItem overwritten = ts.addOrUpdate(new Year(2009), 20.09);
        assertNull(overwritten);
        overwritten = ts.addOrUpdate(new Year(2009), 1.0);
        assertEquals(new Double(20.09), overwritten.getValue());
        assertEquals(new Double(1.0), ts.getValue(new Year(2009)));

        
        overwritten.setValue(null);
        assertEquals(new Double(1.0), ts.getValue(new Year(2009)));

        TimeSeriesDataItem item = new TimeSeriesDataItem(new Year(2010), 20.10);
        overwritten = ts.addOrUpdate(item);
        assertNull(overwritten);
        assertEquals(new Double(20.10), ts.getValue(new Year(2010)));
        
        item.setValue(null);
        assertEquals(new Double(20.10), ts.getValue(new Year(2010)));
    }

    
    public void testBug1075255() {
        TimeSeries ts = new TimeSeries("dummy", FixedMillisecond.class);
        ts.add(new FixedMillisecond(0L), 0.0);
        TimeSeries ts2 = new TimeSeries("dummy2", FixedMillisecond.class);
        ts2.add(new FixedMillisecond(0L), 1.0);
        try {
            ts.addAndOrUpdate(ts2);
        }
        catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
        assertEquals(1, ts.getItemCount());
    }

    
    public void testBug1832432() {
        TimeSeries s1 = new TimeSeries("Series");
        TimeSeries s2 = null;
        try {
            s2 = (TimeSeries) s1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(s1 != s2);
        assertTrue(s1.getClass() == s2.getClass());
        assertTrue(s1.equals(s2));

        
        s1.add(new Day(1, 1, 2007), 100.0);
        assertFalse(s1.equals(s2));
    }

    
    public void testGetIndex() {
        TimeSeries series = new TimeSeries("Series", Month.class);
        assertEquals(-1, series.getIndex(new Month(1, 2003)));

        series.add(new Month(1, 2003), 45.0);
        assertEquals(0, series.getIndex(new Month(1, 2003)));
        assertEquals(-1, series.getIndex(new Month(12, 2002)));
        assertEquals(-2, series.getIndex(new Month(2, 2003)));

        series.add(new Month(3, 2003), 55.0);
        assertEquals(-1, series.getIndex(new Month(12, 2002)));
        assertEquals(0, series.getIndex(new Month(1, 2003)));
        assertEquals(-2, series.getIndex(new Month(2, 2003)));
        assertEquals(1, series.getIndex(new Month(3, 2003)));
        assertEquals(-3, series.getIndex(new Month(4, 2003)));
    }

    
    public void testGetDataItem1() {
        TimeSeries series = new TimeSeries("S", Year.class);

        
        boolean pass = false;
        try {
             series.getDataItem(0);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);

        series.add(new Year(2006), 100.0);
        TimeSeriesDataItem item = series.getDataItem(0);
        assertEquals(new Year(2006), item.getPeriod());
        pass = false;
        try {
            series.getDataItem(-1);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);

        pass = false;
        try {
            series.getDataItem(1);
        }
        catch (IndexOutOfBoundsException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testGetDataItem2() {
        TimeSeries series = new TimeSeries("S", Year.class);
        assertNull(series.getDataItem(new Year(2006)));

        
        boolean pass = false;
        try {
             series.getDataItem(null);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testRemoveAgedItems() {
        TimeSeries series = new TimeSeries("Test Series", Year.class);
        series.addChangeListener(this);
        assertEquals(Long.MAX_VALUE, series.getMaximumItemAge());
        assertEquals(Integer.MAX_VALUE, series.getMaximumItemCount());
        this.gotSeriesChangeEvent = false;

        
        series.removeAgedItems(true);
        assertEquals(0, series.getItemCount());
        assertFalse(this.gotSeriesChangeEvent);

        
        series.add(new Year(1999), 1.0);
        series.setMaximumItemAge(0);
        this.gotSeriesChangeEvent = false;
        series.removeAgedItems(true);
        assertEquals(1, series.getItemCount());
        assertFalse(this.gotSeriesChangeEvent);

        
        series.setMaximumItemAge(10);
        series.add(new Year(2001), 2.0);
        this.gotSeriesChangeEvent = false;
        series.setMaximumItemAge(2);
        assertEquals(2, series.getItemCount());
        assertEquals(0, series.getIndex(new Year(1999)));
        assertFalse(this.gotSeriesChangeEvent);
        series.setMaximumItemAge(1);
        assertEquals(1, series.getItemCount());
        assertEquals(0, series.getIndex(new Year(2001)));
        assertTrue(this.gotSeriesChangeEvent);
    }

    
    public void testRemoveAgedItems2() {
        long y2006 = 1157087372534L;  
        TimeSeries series = new TimeSeries("Test Series", Year.class);
        series.addChangeListener(this);
        assertEquals(Long.MAX_VALUE, series.getMaximumItemAge());
        assertEquals(Integer.MAX_VALUE, series.getMaximumItemCount());
        this.gotSeriesChangeEvent = false;

        
        series.removeAgedItems(y2006, true);
        assertEquals(0, series.getItemCount());
        assertFalse(this.gotSeriesChangeEvent);

        
        series.add(new Year(2004), 1.0);
        series.setMaximumItemAge(1);
        this.gotSeriesChangeEvent = false;
        series.removeAgedItems(new Year(2005).getMiddleMillisecond(), true);
        assertEquals(1, series.getItemCount());
        assertFalse(this.gotSeriesChangeEvent);
        series.removeAgedItems(y2006, true);
        assertEquals(0, series.getItemCount());
        assertTrue(this.gotSeriesChangeEvent);

        
        series.setMaximumItemAge(2);
        series.add(new Year(2003), 1.0);
        series.add(new Year(2005), 2.0);
        assertEquals(2, series.getItemCount());
        this.gotSeriesChangeEvent = false;
        assertEquals(2, series.getItemCount());

        series.removeAgedItems(new Year(2005).getMiddleMillisecond(), true);
        assertEquals(2, series.getItemCount());
        assertFalse(this.gotSeriesChangeEvent);
        series.removeAgedItems(y2006, true);
        assertEquals(1, series.getItemCount());
        assertTrue(this.gotSeriesChangeEvent);
    }

    
    public void testRemoveAgedItems3() {
        TimeSeries s = new TimeSeries("Test");
        boolean pass = true;
        try {
            s.removeAgedItems(0L, true);
        }
        catch (Exception e) {
            pass = false;
        }
        assertTrue(pass);
    }

    
    public void testRemoveAgedItems4() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.setMaximumItemAge(2);
        s1.add(new Year(2010), 1.1);
        s1.add(new Year(2011), 2.2);
        s1.add(new Year(2012), 3.3);
        s1.add(new Year(2013), 2.5);
        assertEquals(3, s1.getItemCount());
        assertEquals(2.2, s1.getMinY(), EPSILON);
        assertEquals(3.3, s1.getMaxY(), EPSILON);
    }

    
    public void testRemoveAgedItems5() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.setMaximumItemAge(4);
        s1.add(new Year(2010), 1.1);
        s1.add(new Year(2011), 2.2);
        s1.add(new Year(2012), 3.3);
        s1.add(new Year(2013), 2.5);
        s1.removeAgedItems(new Year(2015).getMiddleMillisecond(), true);
        assertEquals(3, s1.getItemCount());
        assertEquals(2.2, s1.getMinY(), EPSILON);
        assertEquals(3.3, s1.getMaxY(), EPSILON);
    }

    
    public void testHashCode() {
        TimeSeries s1 = new TimeSeries("Test");
        TimeSeries s2 = new TimeSeries("Test");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add(new Day(1, 1, 2007), 500.0);
        s2.add(new Day(1, 1, 2007), 500.0);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add(new Day(2, 1, 2007), null);
        s2.add(new Day(2, 1, 2007), null);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add(new Day(5, 1, 2007), 111.0);
        s2.add(new Day(5, 1, 2007), 111.0);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add(new Day(9, 1, 2007), 1.0);
        s2.add(new Day(9, 1, 2007), 1.0);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    
    public void testBug1864222() {
        TimeSeries s = new TimeSeries("S");
        s.add(new Day(19, 8, 2005), 1);
        s.add(new Day(31, 1, 2006), 1);
        boolean pass = true;
        try {
            s.createCopy(new Day(1, 12, 2005), new Day(18, 1, 2006));
        }
        catch (CloneNotSupportedException e) {
            pass = false;
        }
        assertTrue(pass);
    }

    private static final double EPSILON = 0.0000000001;

    
    public void testGetMinY() {
        TimeSeries s1 = new TimeSeries("S1");
        assertTrue(Double.isNaN(s1.getMinY()));

        s1.add(new Year(2008), 1.1);
        assertEquals(1.1, s1.getMinY(), EPSILON);

        s1.add(new Year(2009), 2.2);
        assertEquals(1.1, s1.getMinY(), EPSILON);

        s1.add(new Year(2000), 99.9);
        assertEquals(1.1, s1.getMinY(), EPSILON);

        s1.add(new Year(2002), -1.1);
        assertEquals(-1.1, s1.getMinY(), EPSILON);

        s1.add(new Year(2003), null);
        assertEquals(-1.1, s1.getMinY(), EPSILON);

        s1.addOrUpdate(new Year(2002), null);
        assertEquals(1.1, s1.getMinY(), EPSILON);
   }

    
    public void testGetMaxY() {
        TimeSeries s1 = new TimeSeries("S1");
        assertTrue(Double.isNaN(s1.getMaxY()));

        s1.add(new Year(2008), 1.1);
        assertEquals(1.1, s1.getMaxY(), EPSILON);

        s1.add(new Year(2009), 2.2);
        assertEquals(2.2, s1.getMaxY(), EPSILON);

        s1.add(new Year(2000), 99.9);
        assertEquals(99.9, s1.getMaxY(), EPSILON);

        s1.add(new Year(2002), -1.1);
        assertEquals(99.9, s1.getMaxY(), EPSILON);

        s1.add(new Year(2003), null);
        assertEquals(99.9, s1.getMaxY(), EPSILON);

        s1.addOrUpdate(new Year(2000), null);
        assertEquals(2.2, s1.getMaxY(), EPSILON);
    }

    
    public void testClear() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.add(new Year(2009), 1.1);
        s1.add(new Year(2010), 2.2);

        assertEquals(2, s1.getItemCount());

        s1.clear();
        assertEquals(0, s1.getItemCount());
        assertTrue(Double.isNaN(s1.getMinY()));
        assertTrue(Double.isNaN(s1.getMaxY()));
    }

    
    public void testAdd() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.setMaximumItemCount(2);
        s1.add(new Year(2010), 1.1);
        s1.add(new Year(2011), 2.2);
        s1.add(new Year(2012), 3.3);
        assertEquals(2, s1.getItemCount());
        assertEquals(2.2, s1.getMinY(), EPSILON);
        assertEquals(3.3, s1.getMaxY(), EPSILON);
    }

    
    public void testUpdate_RegularTimePeriod() {
        TimeSeries s1 = new TimeSeries("S1");
        s1.add(new Year(2010), 1.1);
        s1.add(new Year(2011), 2.2);
        s1.add(new Year(2012), 3.3);
        s1.update(new Year(2012), 4.4);
        assertEquals(4.4, s1.getMaxY(), EPSILON);
        s1.update(new Year(2010), 0.5);
        assertEquals(0.5, s1.getMinY(), EPSILON);
        s1.update(new Year(2012), null);
        assertEquals(2.2, s1.getMaxY(), EPSILON);
        s1.update(new Year(2010), null);
        assertEquals(2.2, s1.getMinY(), EPSILON);
    }

    
    public void testAdd_TimeSeriesDataItem() {
        TimeSeriesDataItem item = new TimeSeriesDataItem(new Year(2009), 1.0);
        TimeSeries series = new TimeSeries("S1");
        series.add(item);
        assertTrue(item.equals(series.getDataItem(0)));
        item.setValue(new Double(99.9));
        assertFalse(item.equals(series.getDataItem(0)));
    }

}
