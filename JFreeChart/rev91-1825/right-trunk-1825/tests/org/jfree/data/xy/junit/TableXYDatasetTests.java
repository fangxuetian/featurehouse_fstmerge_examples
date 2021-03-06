

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

import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;


public class TableXYDatasetTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(TableXYDatasetTests.class);
    }

    
    public TableXYDatasetTests(String name) {
        super(name);
    }

    
    public void testEquals() {

        DefaultTableXYDataset d1 = new DefaultTableXYDataset();
        DefaultTableXYDataset d2 = new DefaultTableXYDataset();
        assertTrue(d1.equals(d2));
        assertTrue(d2.equals(d1));

        d1.addSeries(createSeries1());
        assertFalse(d1.equals(d2));

        d2.addSeries(createSeries1());
        assertTrue(d1.equals(d2));

    }

    
    public void testCloning() {
        DefaultTableXYDataset d1 = new DefaultTableXYDataset();
        d1.addSeries(createSeries1());
        DefaultTableXYDataset d2 = null;
        try {
            d2 = (DefaultTableXYDataset) d1.clone();
        }
        catch (CloneNotSupportedException e) {
            System.err.println("Failed to clone.");
        }
        assertTrue(d1 != d2);
        assertTrue(d1.getClass() == d2.getClass());
        assertTrue(d1.equals(d2));
    }

    
    public void testPublicCloneable() {
        DefaultTableXYDataset d1 = new DefaultTableXYDataset();
        assertTrue(d1 instanceof PublicCloneable);
    }

    
    public void testSerialization() {

        DefaultTableXYDataset d1 = new DefaultTableXYDataset();
        d1.addSeries(createSeries2());
        DefaultTableXYDataset d2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray())
            );
            d2 = (DefaultTableXYDataset) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(d1, d2);

    }

    
    public void testTableXYDataset() {

        XYSeries series1 = createSeries1();
        XYSeries series2 = createSeries2();

        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        
        assertEquals(6, dataset.getItemCount());
        assertEquals(6, dataset.getX(0, 5).intValue());
        assertEquals(null, dataset.getY(0, 5));
        assertEquals(6, dataset.getX(1, 5).intValue());
        assertEquals(2, dataset.getY(1, 5).intValue());

        
        
        series2.add(7, 2);
        assertEquals(7, dataset.getItemCount());
        assertEquals(null, dataset.getY(0, 6));
        assertEquals(2, dataset.getY(1, 6).intValue());

        
        dataset.removeSeries(series1);
        
        assertEquals(7, dataset.getItemCount());

        
        dataset.removeSeries(series2);
        series1 = createSeries1();
        dataset.addSeries(series1);

        
        assertEquals(4, dataset.getItemCount());

    }

    
    public void test788597() {
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(createSeries1());
        assertEquals(4, dataset.getItemCount());
        dataset.removeAllSeries();
        assertEquals(0, dataset.getItemCount());
    }

    
    public void testRemoveAllValuesForX() {
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(createSeries1());
        dataset.addSeries(createSeries2());
        dataset.removeAllValuesForX(new Double(2.0));
        assertEquals(5, dataset.getItemCount());
        assertEquals(new Double(1.0), dataset.getX(0, 0));
        assertEquals(new Double(3.0), dataset.getX(0, 1));
        assertEquals(new Double(4.0), dataset.getX(0, 2));
        assertEquals(new Double(5.0), dataset.getX(0, 3));
        assertEquals(new Double(6.0), dataset.getX(0, 4));
    }

    
    public void testPrune() {
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        dataset.addSeries(createSeries1());
        dataset.addSeries(createSeries2());
        dataset.removeSeries(1);
        dataset.prune();
        assertEquals(4, dataset.getItemCount());
    }

    
    public void testAutoPrune() {

        
        DefaultTableXYDataset dataset = new DefaultTableXYDataset(true);
        dataset.addSeries(createSeriesA());
        assertEquals(2, dataset.getItemCount());  
        dataset.addSeries(createSeriesB());
        assertEquals(2, dataset.getItemCount());  
        dataset.removeSeries(1);
        assertEquals(1, dataset.getItemCount());  

        
        DefaultTableXYDataset dataset2 = new DefaultTableXYDataset(true);
        dataset2.addSeries(createSeriesA());
        assertEquals(2, dataset2.getItemCount());  
        dataset2.addSeries(createSeriesB());
        assertEquals(2, dataset2.getItemCount());  
        dataset2.removeSeries(1);
        assertEquals(1, dataset2.getItemCount());  

    }

    
    private XYSeries createSeriesA() {
        XYSeries s = new XYSeries("A", true, false);
        s.add(1.0, 1.1);
        s.add(2.0, null);
        return s;
    }

    
    private XYSeries createSeriesB() {
        XYSeries s = new XYSeries("B", true, false);
        s.add(1.0, null);
        s.add(2.0, 2.2);
        return s;
    }

    
    private XYSeries createSeries1() {
        XYSeries series1 = new XYSeries("Series 1", true, false);
        series1.add(1.0, 1.0);
        series1.add(2.0, 1.0);
        series1.add(4.0, 1.0);
        series1.add(5.0, 1.0);
        return series1;
    }

    
    private XYSeries createSeries2() {
        XYSeries series2 = new XYSeries("Series 2", true, false);
        series2.add(2.0, 2.0);
        series2.add(3.0, 2.0);
        series2.add(4.0, 2.0);
        series2.add(5.0, 2.0);
        series2.add(6.0, 2.0);
        return series2;
    }

}
