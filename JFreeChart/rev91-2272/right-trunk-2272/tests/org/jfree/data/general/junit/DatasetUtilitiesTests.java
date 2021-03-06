

package org.jfree.data.general.junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.data.KeyToGroupMap;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.pie.DefaultPieDataset;
import org.jfree.data.pie.PieDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.statistics.MultiValueCategoryDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;


public class DatasetUtilitiesTests extends TestCase {

    private static final double EPSILON = 0.0000000001;

    
    public static Test suite() {
        return new TestSuite(DatasetUtilitiesTests.class);
    }

    
    public DatasetUtilitiesTests(String name) {
        super(name);
    }

    
    public void testJava() {
        assertTrue(Double.isNaN(Math.min(1.0, Double.NaN)));
        assertTrue(Double.isNaN(Math.max(1.0, Double.NaN)));
    }

    
    public void testCalculatePieDatasetTotal() {
        DefaultPieDataset d = new DefaultPieDataset();
        assertEquals(0.0, DatasetUtilities.calculatePieDatasetTotal(d),
                EPSILON);
        d.setValue("A", 1.0);
        assertEquals(1.0, DatasetUtilities.calculatePieDatasetTotal(d),
                EPSILON);
        d.setValue("B", 3.0);
        assertEquals(4.0, DatasetUtilities.calculatePieDatasetTotal(d),
                EPSILON);
    }

    
    public void testFindDomainBounds() {
        XYDataset dataset = createXYDataset1();
        Range r = DatasetUtilities.findDomainBounds(dataset);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(3.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindDomainBounds2() {
        DefaultIntervalXYDataset dataset = new DefaultIntervalXYDataset();
        double[] x1 = new double[] {1.0, 2.0, 3.0};
        double[] x1Start = new double[] {0.9, 1.9, 2.9};
        double[] x1End = new double[] {1.1, 2.1, 3.1};
        double[] y1 = new double[] {4.0, 5.0, 6.0};
        double[] y1Start = new double[] {1.09, 2.09, 3.09};
        double[] y1End = new double[] {1.11, 2.11, 3.11};
        double[][] data1 = new double[][] {x1, x1Start, x1End, y1, y1Start,
                y1End};
        dataset.addSeries("S1", data1);
        Range r = DatasetUtilities.findDomainBounds(dataset);
        assertEquals(0.9, r.getLowerBound(), EPSILON);
        assertEquals(3.1, r.getUpperBound(), EPSILON);
    }

    
    public void testFindDomainBounds3() {
        DefaultIntervalXYDataset dataset = new DefaultIntervalXYDataset();
        double[] x1 = new double[] {1.0, 2.0, 3.0};
        double[] x1Start = new double[] {0.9, 1.9, 2.9};
        double[] x1End = new double[] {1.1, 2.1, 3.1};
        double[] y1 = new double[] {4.0, 5.0, 6.0};
        double[] y1Start = new double[] {1.09, 2.09, 3.09};
        double[] y1End = new double[] {1.11, 2.11, 3.11};
        double[][] data1 = new double[][] {x1, x1Start, x1End, y1, y1Start,
                y1End};
        dataset.addSeries("S1", data1);
        Range r = DatasetUtilities.findDomainBounds(dataset, false);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(3.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindDomainBounds_NaN() {
        DefaultIntervalXYDataset dataset = new DefaultIntervalXYDataset();
        double[] x1 = new double[] {1.0, 2.0, Double.NaN};
        double[] x1Start = new double[] {0.9, 1.9, Double.NaN};
        double[] x1End = new double[] {1.1, 2.1, Double.NaN};
        double[] y1 = new double[] {4.0, 5.0, 6.0};
        double[] y1Start = new double[] {1.09, 2.09, 3.09};
        double[] y1End = new double[] {1.11, 2.11, 3.11};
        double[][] data1 = new double[][] {x1, x1Start, x1End, y1, y1Start,
                y1End};
        dataset.addSeries("S1", data1);
        Range r = DatasetUtilities.findDomainBounds(dataset);
        assertEquals(0.9, r.getLowerBound(), EPSILON);
        assertEquals(2.1, r.getUpperBound(), EPSILON);

        r = DatasetUtilities.findDomainBounds(dataset, false);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(2.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateDomainBounds() {
        XYDataset dataset = createXYDataset1();
        Range r = DatasetUtilities.iterateDomainBounds(dataset);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(3.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateDomainBounds_NaN() {
        DefaultXYDataset dataset = new DefaultXYDataset();
        double[] x = new double[] {1.0, 2.0, Double.NaN, 3.0};
        double[] y = new double[] {9.0, 8.0, 7.0, 6.0};
        dataset.addSeries("S1", new double[][] {x, y});
        Range r = DatasetUtilities.iterateDomainBounds(dataset);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(3.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateDomainBounds_NaN2() {
        DefaultIntervalXYDataset dataset = new DefaultIntervalXYDataset();
        double[] x1 = new double[] {Double.NaN, 2.0, 3.0};
        double[] x1Start = new double[] {0.9, Double.NaN, 2.9};
        double[] x1End = new double[] {1.1, Double.NaN, 3.1};
        double[] y1 = new double[] {4.0, 5.0, 6.0};
        double[] y1Start = new double[] {1.09, 2.09, 3.09};
        double[] y1End = new double[] {1.11, 2.11, 3.11};
        double[][] data1 = new double[][] {x1, x1Start, x1End, y1, y1Start,
                y1End};
        dataset.addSeries("S1", data1);
        Range r = DatasetUtilities.iterateDomainBounds(dataset, false);
        assertEquals(2.0, r.getLowerBound(), EPSILON);
        assertEquals(3.0, r.getUpperBound(), EPSILON);
        r = DatasetUtilities.iterateDomainBounds(dataset, true);
        assertEquals(0.9, r.getLowerBound(), EPSILON);
        assertEquals(3.1, r.getUpperBound(), EPSILON);
    }

    
    public void testFindRangeBounds_CategoryDataset() {
        CategoryDataset dataset = createCategoryDataset1();
        Range r = DatasetUtilities.findRangeBounds(dataset);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(6.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindRangeBounds() {
        XYDataset dataset = createXYDataset1();
        Range r = DatasetUtilities.findRangeBounds(dataset);
        assertEquals(100.0, r.getLowerBound(), EPSILON);
        assertEquals(105.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindRangeBounds2() {
        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
        Range r = DatasetUtilities.findRangeBounds(dataset);
        assertNull(r);
        YIntervalSeries s1 = new YIntervalSeries("S1");
        dataset.addSeries(s1);
        r = DatasetUtilities.findRangeBounds(dataset);
        assertNull(r);

        
        s1.add(1.0, 2.0, 1.5, 2.5);
        r = DatasetUtilities.findRangeBounds(dataset);
        assertEquals(1.5, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        r = DatasetUtilities.findRangeBounds(dataset, false);
        assertEquals(2.0, r.getLowerBound(), EPSILON);
        assertEquals(2.0, r.getUpperBound(), EPSILON);

        
        s1.add(2.0, 2.0, 1.4, 2.1);
        r = DatasetUtilities.findRangeBounds(dataset);
        assertEquals(1.4, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        
        YIntervalSeries s2 = new YIntervalSeries("S2");
        dataset.addSeries(s2);
        r = DatasetUtilities.findRangeBounds(dataset);
        assertEquals(1.4, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        
        s2.add(1.0, 2.0, 1.9, 2.6);
        r = DatasetUtilities.findRangeBounds(dataset);
        assertEquals(1.4, r.getLowerBound(), EPSILON);
        assertEquals(2.6, r.getUpperBound(), EPSILON);

        
        r = DatasetUtilities.findRangeBounds(dataset, false);
        assertEquals(2.0, r.getLowerBound(), EPSILON);
        assertEquals(2.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds_CategoryDataset() {
        CategoryDataset dataset = createCategoryDataset1();
        Range r = DatasetUtilities.iterateRangeBounds(dataset, false);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(6.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds2_CategoryDataset() {
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Range r = DatasetUtilities.iterateRangeBounds(dataset, false);
        assertNull(r);

        
        dataset.addValue(1.23, "R1", "C1");
        r = DatasetUtilities.iterateRangeBounds(dataset, false);
        assertEquals(1.23, r.getLowerBound(), EPSILON);
        assertEquals(1.23, r.getUpperBound(), EPSILON);

        
        dataset.addValue(null, "R2", "C1");
        r = DatasetUtilities.iterateRangeBounds(dataset, false);
        assertEquals(1.23, r.getLowerBound(), EPSILON);
        assertEquals(1.23, r.getUpperBound(), EPSILON);

        
        dataset.addValue(Double.NaN, "R2", "C1");
        r = DatasetUtilities.iterateRangeBounds(dataset, false);
        assertEquals(1.23, r.getLowerBound(), EPSILON);
        assertEquals(1.23, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds3_CategoryDataset() {
        Number[][] starts = new Double[2][3];
        Number[][] ends = new Double[2][3];
        starts[0][0] = new Double(1.0);
        starts[0][1] = new Double(2.0);
        starts[0][2] = new Double(3.0);
        starts[1][0] = new Double(11.0);
        starts[1][1] = new Double(12.0);
        starts[1][2] = new Double(13.0);
        ends[0][0] = new Double(4.0);
        ends[0][1] = new Double(5.0);
        ends[0][2] = new Double(6.0);
        ends[1][0] = new Double(16.0);
        ends[1][1] = new Double(15.0);
        ends[1][2] = new Double(14.0);

        DefaultIntervalCategoryDataset d = new DefaultIntervalCategoryDataset(
                starts, ends);
        Range r = DatasetUtilities.iterateRangeBounds(d, false);
        assertEquals(4.0, r.getLowerBound(), EPSILON);
        assertEquals(16.0, r.getUpperBound(), EPSILON);
        r = DatasetUtilities.iterateRangeBounds(d, true);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(16.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds() {
        XYDataset dataset = createXYDataset1();
        Range r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(100.0, r.getLowerBound(), EPSILON);
        assertEquals(105.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds2() {
        XYSeries s1 = new XYSeries("S1");
        s1.add(1.0, 1.1);
        s1.add(2.0, null);
        s1.add(3.0, 3.3);
        XYSeriesCollection dataset = new XYSeriesCollection(s1);
        Range r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.1, r.getLowerBound(), EPSILON);
        assertEquals(3.3, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds3() {
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        Range r = DatasetUtilities.iterateRangeBounds(dataset);
        assertNull(r);
        XYSeries s1 = new XYSeries("S1");
        dataset.addSeries(s1);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertNull(r);

        
        s1.add(1.0, 1.23);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.23, r.getLowerBound(), EPSILON);
        assertEquals(1.23, r.getUpperBound(), EPSILON);

        
        s1.add(2.0, null);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.23, r.getLowerBound(), EPSILON);
        assertEquals(1.23, r.getUpperBound(), EPSILON);

        
        s1.add(3.0, Double.NaN);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.23, r.getLowerBound(), EPSILON);
        assertEquals(1.23, r.getUpperBound(), EPSILON);
    }

    
    public void testIterateRangeBounds4() {
        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
        Range r = DatasetUtilities.iterateRangeBounds(dataset);
        assertNull(r);
        YIntervalSeries s1 = new YIntervalSeries("S1");
        dataset.addSeries(s1);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertNull(r);

        
        s1.add(1.0, 2.0, 1.5, 2.5);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.5, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        
        s1.add(2.0, 2.0, 1.4, 2.1);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.4, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        
        YIntervalSeries s2 = new YIntervalSeries("S2");
        dataset.addSeries(s2);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.4, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        
        s2.add(1.0, 2.0, 1.9, 2.6);
        r = DatasetUtilities.iterateRangeBounds(dataset);
        assertEquals(1.4, r.getLowerBound(), EPSILON);
        assertEquals(2.6, r.getUpperBound(), EPSILON);
    }

    
    public void testFindMinimumDomainValue() {
        XYDataset dataset = createXYDataset1();
        Number minimum = DatasetUtilities.findMinimumDomainValue(dataset);
        assertEquals(new Double(1.0), minimum);
    }

    
    public void testFindMaximumDomainValue() {
        XYDataset dataset = createXYDataset1();
        Number maximum = DatasetUtilities.findMaximumDomainValue(dataset);
        assertEquals(new Double(3.0), maximum);
    }

    
    public void testFindMinimumRangeValue() {
        CategoryDataset d1 = createCategoryDataset1();
        Number min1 = DatasetUtilities.findMinimumRangeValue(d1);
        assertEquals(new Double(1.0), min1);

        XYDataset d2 = createXYDataset1();
        Number min2 = DatasetUtilities.findMinimumRangeValue(d2);
        assertEquals(new Double(100.0), min2);
    }

    
    public void testFindMaximumRangeValue() {
        CategoryDataset d1 = createCategoryDataset1();
        Number max1 = DatasetUtilities.findMaximumRangeValue(d1);
        assertEquals(new Double(6.0), max1);

        XYDataset dataset = createXYDataset1();
        Number maximum = DatasetUtilities.findMaximumRangeValue(dataset);
        assertEquals(new Double(105.0), maximum);
    }

    
    public void testMinMaxRange() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100.0, "Series 1", "Type 1");
        dataset.addValue(101.1, "Series 1", "Type 2");
        Number min = DatasetUtilities.findMinimumRangeValue(dataset);
        assertTrue(min.doubleValue() < 100.1);
        Number max = DatasetUtilities.findMaximumRangeValue(dataset);
        assertTrue(max.doubleValue() > 101.0);
    }

    
    public void test803660() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100.0, "Series 1", "Type 1");
        dataset.addValue(101.1, "Series 1", "Type 2");
        Number n = DatasetUtilities.findMaximumRangeValue(dataset);
        assertTrue(n.doubleValue() > 101.0);
    }

    
    public void testCumulativeRange1() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Series 1", "Start");
        dataset.addValue(15.0, "Series 1", "Delta 1");
        dataset.addValue(-7.0, "Series 1", "Delta 2");
        Range range = DatasetUtilities.findCumulativeRangeBounds(dataset);
        assertEquals(0.0, range.getLowerBound(), 0.00000001);
        assertEquals(25.0, range.getUpperBound(), 0.00000001);
    }

    
    public void testCumulativeRange2() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(-21.4, "Series 1", "Start Value");
        dataset.addValue(11.57, "Series 1", "Delta 1");
        dataset.addValue(3.51, "Series 1", "Delta 2");
        dataset.addValue(-12.36, "Series 1", "Delta 3");
        dataset.addValue(3.39, "Series 1", "Delta 4");
        dataset.addValue(38.68, "Series 1", "Delta 5");
        dataset.addValue(-43.31, "Series 1", "Delta 6");
        dataset.addValue(-29.59, "Series 1", "Delta 7");
        dataset.addValue(35.30, "Series 1", "Delta 8");
        dataset.addValue(5.0, "Series 1", "Delta 9");
        Range range = DatasetUtilities.findCumulativeRangeBounds(dataset);
        assertEquals(-49.51, range.getLowerBound(), 0.00000001);
        assertEquals(23.39, range.getUpperBound(), 0.00000001);
    }

    
    public void testCumulativeRange3() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(15.76, "Product 1", "Labour");
        dataset.addValue(8.66, "Product 1", "Administration");
        dataset.addValue(4.71, "Product 1", "Marketing");
        dataset.addValue(3.51, "Product 1", "Distribution");
        dataset.addValue(32.64, "Product 1", "Total Expense");
        Range range = DatasetUtilities.findCumulativeRangeBounds(dataset);
        assertEquals(0.0, range.getLowerBound(), EPSILON);
        assertEquals(65.28, range.getUpperBound(), EPSILON);
    }

    
    public void testCumulativeRange_NaN() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Series 1", "Start");
        dataset.addValue(15.0, "Series 1", "Delta 1");
        dataset.addValue(Double.NaN, "Series 1", "Delta 2");
        Range range = DatasetUtilities.findCumulativeRangeBounds(dataset);
        assertEquals(0.0, range.getLowerBound(), EPSILON);
        assertEquals(25.0, range.getUpperBound(), EPSILON);
    }

    
    public void testCreateCategoryDataset1() {
        String[] rowKeys = {"R1", "R2", "R3"};
        String[] columnKeys = {"C1", "C2"};
        double[][] data = new double[3][];
        data[0] = new double[] {1.1, 1.2};
        data[1] = new double[] {2.1, 2.2};
        data[2] = new double[] {3.1, 3.2};
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
                rowKeys, columnKeys, data);
        assertTrue(dataset.getRowCount() == 3);
        assertTrue(dataset.getColumnCount() == 2);
    }

    
    public void testCreateCategoryDataset2() {
        boolean pass = false;
        String[] rowKeys = {"R1", "R2", "R3"};
        String[] columnKeys = {"C1", "C2"};
        double[][] data = new double[2][];
        data[0] = new double[] {1.1, 1.2, 1.3};
        data[1] = new double[] {2.1, 2.2, 2.3};
        CategoryDataset dataset = null;
        try {
            dataset = DatasetUtilities.createCategoryDataset(rowKeys,
                    columnKeys, data);
        }
        catch (IllegalArgumentException e) {
            pass = true;  
        }
        assertTrue(pass);
        assertTrue(dataset == null);
    }

    
    public void testMaximumStackedRangeValue() {
        double v1 = 24.3;
        double v2 = 14.2;
        double v3 = 33.2;
        double v4 = 32.4;
        double v5 = 26.3;
        double v6 = 22.6;
        Number answer = new Double(Math.max(v1 + v2 + v3, v4 + v5 + v6));
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(v1, "Row 0", "Column 0");
        d.addValue(v2, "Row 1", "Column 0");
        d.addValue(v3, "Row 2", "Column 0");
        d.addValue(v4, "Row 0", "Column 1");
        d.addValue(v5, "Row 1", "Column 1");
        d.addValue(v6, "Row 2", "Column 1");
        Number max = DatasetUtilities.findMaximumStackedRangeValue(d);
        assertTrue(max.equals(answer));
    }

    
    public void testFindStackedRangeBounds_CategoryDataset1() {
        CategoryDataset d1 = createCategoryDataset1();
        Range r = DatasetUtilities.findStackedRangeBounds(d1);
        assertEquals(0.0, r.getLowerBound(), EPSILON);
        assertEquals(15.0, r.getUpperBound(), EPSILON);

        d1 = createCategoryDataset2();
        r = DatasetUtilities.findStackedRangeBounds(d1);
        assertEquals(-2.0, r.getLowerBound(), EPSILON);
        assertEquals(2.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindStackedRangeBounds_CategoryDataset2() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Range r = DatasetUtilities.findStackedRangeBounds(dataset);
        assertTrue(r == null);

        dataset.addValue(5.0, "R1", "C1");
        r = DatasetUtilities.findStackedRangeBounds(dataset, 3.0);
        assertEquals(3.0, r.getLowerBound(), EPSILON);
        assertEquals(8.0, r.getUpperBound(), EPSILON);

        dataset.addValue(-1.0, "R2", "C1");
        r = DatasetUtilities.findStackedRangeBounds(dataset, 3.0);
        assertEquals(2.0, r.getLowerBound(), EPSILON);
        assertEquals(8.0, r.getUpperBound(), EPSILON);

        dataset.addValue(null, "R3", "C1");
        r = DatasetUtilities.findStackedRangeBounds(dataset, 3.0);
        assertEquals(2.0, r.getLowerBound(), EPSILON);
        assertEquals(8.0, r.getUpperBound(), EPSILON);

        dataset.addValue(Double.NaN, "R4", "C1");
        r = DatasetUtilities.findStackedRangeBounds(dataset, 3.0);
        assertEquals(2.0, r.getLowerBound(), EPSILON);
        assertEquals(8.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindStackedRangeBounds_CategoryDataset3() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        KeyToGroupMap map = new KeyToGroupMap("Group A");
        Range r = DatasetUtilities.findStackedRangeBounds(dataset, map);
        assertTrue(r == null);

        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R2", "C1");
        dataset.addValue(3.0, "R3", "C1");
        dataset.addValue(4.0, "R4", "C1");

        map.mapKeyToGroup("R1", "Group A");
        map.mapKeyToGroup("R2", "Group A");
        map.mapKeyToGroup("R3", "Group B");
        map.mapKeyToGroup("R4", "Group B");

        r = DatasetUtilities.findStackedRangeBounds(dataset, map);
        assertEquals(0.0, r.getLowerBound(), EPSILON);
        assertEquals(7.0, r.getUpperBound(), EPSILON);

        dataset.addValue(null, "R5", "C1");
        r = DatasetUtilities.findStackedRangeBounds(dataset, map);
        assertEquals(0.0, r.getLowerBound(), EPSILON);
        assertEquals(7.0, r.getUpperBound(), EPSILON);

        dataset.addValue(Double.NaN, "R6", "C1");
        r = DatasetUtilities.findStackedRangeBounds(dataset, map);
        assertEquals(0.0, r.getLowerBound(), EPSILON);
        assertEquals(7.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindStackedRangeBoundsForTableXYDataset1() {
        TableXYDataset d2 = createTableXYDataset1();
        Range r = DatasetUtilities.findStackedRangeBounds(d2);
        assertEquals(-2.0, r.getLowerBound(), EPSILON);
        assertEquals(2.0, r.getUpperBound(), EPSILON);
    }

    
    public void testFindStackedRangeBoundsForTableXYDataset2() {
        DefaultTableXYDataset d = new DefaultTableXYDataset();
        Range r = DatasetUtilities.findStackedRangeBounds(d);
        assertEquals(r, new Range(0.0, 0.0));
    }

    
    public void testStackedRangeWithMap() {
        CategoryDataset d = createCategoryDataset1();
        KeyToGroupMap map = new KeyToGroupMap("G0");
        map.mapKeyToGroup("R2", "G1");
        Range r = DatasetUtilities.findStackedRangeBounds(d, map);
        assertEquals(0.0, r.getLowerBound(), EPSILON);
        assertEquals(9.0, r.getUpperBound(), EPSILON);
    }

    
    public void testIsEmptyOrNullXYDataset() {
        XYSeriesCollection dataset = null;
        assertTrue(DatasetUtilities.isEmptyOrNull(dataset));
        dataset = new XYSeriesCollection();
        assertTrue(DatasetUtilities.isEmptyOrNull(dataset));
        XYSeries s1 = new XYSeries("S1");
        dataset.addSeries(s1);
        assertTrue(DatasetUtilities.isEmptyOrNull(dataset));
        s1.add(1.0, 2.0);
        assertFalse(DatasetUtilities.isEmptyOrNull(dataset));
        s1.clear();
        assertTrue(DatasetUtilities.isEmptyOrNull(dataset));
    }

    
    public void testLimitPieDataset() {

        
        DefaultPieDataset d1 = new DefaultPieDataset();
        PieDataset d2 = DatasetUtilities.createConsolidatedPieDataset(d1,
                "Other", 0.05);
        assertEquals(0, d2.getItemCount());

        
        d1.setValue("Item 1", 1.0);
        d1.setValue("Item 2", 49.50);
        d1.setValue("Item 3", 49.50);
        d2 = DatasetUtilities.createConsolidatedPieDataset(d1, "Other", 0.05);
        assertEquals(3, d2.getItemCount());
        assertEquals("Item 1", d2.getKey(0));
        assertEquals("Item 2", d2.getKey(1));
        assertEquals("Item 3", d2.getKey(2));

        
        d1.setValue("Item 4", 1.0);
        d2 = DatasetUtilities.createConsolidatedPieDataset(d1, "Other", 0.05,
                2);

        
        assertEquals(3, d2.getItemCount());
        assertEquals("Item 2", d2.getKey(0));
        assertEquals("Item 3", d2.getKey(1));
        assertEquals("Other", d2.getKey(2));
        assertEquals(new Double(2.0), d2.getValue("Other"));

    }

    
    public void testSampleFunction2D() {
        Function2D f = new LineFunction2D(0, 1);
        XYDataset dataset = DatasetUtilities.sampleFunction2D(f, 0.0, 1.0, 2,
                "S1");
        assertEquals(1, dataset.getSeriesCount());
        assertEquals("S1", dataset.getSeriesKey(0));
        assertEquals(2, dataset.getItemCount(0));
        assertEquals(0.0, dataset.getXValue(0, 0), EPSILON);
        assertEquals(0.0, dataset.getYValue(0, 0), EPSILON);
        assertEquals(1.0, dataset.getXValue(0, 1), EPSILON);
        assertEquals(1.0, dataset.getYValue(0, 1), EPSILON);
    }

    
    public void testFindMinimumStackedRangeValue() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        
        Number min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertNull(min);

        dataset.addValue(1.0, "R1", "C1");
        min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertEquals(0.0, min.doubleValue(), EPSILON);

        dataset.addValue(2.0, "R2", "C1");
        min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertEquals(0.0, min.doubleValue(), EPSILON);

        dataset.addValue(-3.0, "R3", "C1");
        min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertEquals(-3.0, min.doubleValue(), EPSILON);

        dataset.addValue(Double.NaN, "R4", "C1");
        min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertEquals(-3.0, min.doubleValue(), EPSILON);
    }

    
    public void testFindMinimumStackedRangeValue2() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(-1.0, "R1", "C1");
        Number min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertEquals(-1.0, min.doubleValue(), EPSILON);

        dataset.addValue(-2.0, "R2", "C1");
        min = DatasetUtilities.findMinimumStackedRangeValue(dataset);
        assertEquals(-3.0, min.doubleValue(), EPSILON);
    }

    
    public void testFindMaximumStackedRangeValue() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        
        Number max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertNull(max);

        dataset.addValue(1.0, "R1", "C1");
        max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertEquals(1.0, max.doubleValue(), EPSILON);

        dataset.addValue(2.0, "R2", "C1");
        max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertEquals(3.0, max.doubleValue(), EPSILON);

        dataset.addValue(-3.0, "R3", "C1");
        max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertEquals(3.0, max.doubleValue(), EPSILON);

        dataset.addValue(Double.NaN, "R4", "C1");
        max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertEquals(3.0, max.doubleValue(), EPSILON);
    }

    
    public void testFindMaximumStackedRangeValue2() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(-1.0, "R1", "C1");
        Number max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertEquals(0.0, max.doubleValue(), EPSILON);

        dataset.addValue(-2.0, "R2", "C1");
        max = DatasetUtilities.findMaximumStackedRangeValue(dataset);
        assertEquals(0.0, max.doubleValue(), EPSILON);
    }

    
    private CategoryDataset createCategoryDataset1() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        result.addValue(1.0, "R0", "C0");
        result.addValue(1.0, "R1", "C0");
        result.addValue(1.0, "R2", "C0");
        result.addValue(4.0, "R0", "C1");
        result.addValue(5.0, "R1", "C1");
        result.addValue(6.0, "R2", "C1");
        return result;
    }

    
    private CategoryDataset createCategoryDataset2() {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        result.addValue(1.0, "R0", "C0");
        result.addValue(-2.0, "R1", "C0");
        result.addValue(2.0, "R0", "C1");
        result.addValue(-1.0, "R1", "C1");
        return result;
    }


    
    private XYDataset createXYDataset1() {
        XYSeries series1 = new XYSeries("S1");
        series1.add(1.0, 100.0);
        series1.add(2.0, 101.0);
        series1.add(3.0, 102.0);
        XYSeries series2 = new XYSeries("S2");
        series2.add(1.0, 103.0);
        series2.add(2.0, null);
        series2.add(3.0, 105.0);
        XYSeriesCollection result = new XYSeriesCollection();
        result.addSeries(series1);
        result.addSeries(series2);
        result.setIntervalWidth(0.0);
        return result;
    }

    
    private TableXYDataset createTableXYDataset1() {
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();

        XYSeries s1 = new XYSeries("Series 1", true, false);
        s1.add(1.0, 1.0);
        s1.add(2.0, 2.0);
        dataset.addSeries(s1);

        XYSeries s2 = new XYSeries("Series 2", true, false);
        s2.add(1.0, -2.0);
        s2.add(2.0, -1.0);
        dataset.addSeries(s2);

        return dataset;
    }

    
    public void testIterateToFindRangeBounds1_XYDataset() {
        
        boolean pass = false;
        try {
            DatasetUtilities.iterateToFindRangeBounds(null, new ArrayList(),
                    new Range(0.0, 1.0), true);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            DatasetUtilities.iterateToFindRangeBounds(new XYSeriesCollection(),
                    null, new Range(0.0, 1.0), true);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);

        
        pass = false;
        try {
            DatasetUtilities.iterateToFindRangeBounds(new XYSeriesCollection(),
                    new ArrayList(), null, true);
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }

    
    public void testIterateToFindRangeBounds2_XYDataset() {
        List visibleSeriesKeys = new ArrayList();
        Range xRange = new Range(0.0, 10.0);

        
        XYSeriesCollection dataset = new XYSeriesCollection();
        Range r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertNull(r);

        
        XYSeries s1 = new XYSeries("A");
        dataset.addSeries(s1);
        visibleSeriesKeys.add("A");
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertNull(r);

        
        s1.add(1.0, null);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertNull(r);

        
        s1.add(2.0, Double.NaN);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertNull(r);

        
        s1.add(3.0, 5.0);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertEquals(new Range(5.0, 5.0), r);

        
        s1.add(4.0, 6.0);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertEquals(new Range(5.0, 6.0), r);

        
        XYSeries s2 = new XYSeries("B");
        dataset.addSeries(s2);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertEquals(new Range(5.0, 6.0), r);
        visibleSeriesKeys.add("B");
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertEquals(new Range(5.0, 6.0), r);

        
        s2.add(5.0, 15.0);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertEquals(new Range(5.0, 15.0), r);

        
        s2.add(15.0, 150.0);
        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false);
        assertEquals(new Range(5.0, 15.0), r);

        r = DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, new Range(0.0, 20.0), false);
        assertEquals(new Range(5.0, 150.0), r);
    }

    
    public void testIterateToFindRangeBounds_BoxAndWhiskerXYDataset() {
        DefaultBoxAndWhiskerXYDataset dataset
                = new DefaultBoxAndWhiskerXYDataset("Series 1");
        List visibleSeriesKeys = new ArrayList();
        visibleSeriesKeys.add("Series 1");
        Range xRange = new Range(Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY);
        assertNull(DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false));

        dataset.add(new Date(50L), new BoxAndWhiskerItem(5.0, 4.9, 2.0, 8.0,
                1.0, 9.0, 0.0, 10.0, new ArrayList()));
        assertEquals(new Range(5.0, 5.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, false));
        assertEquals(new Range(1.0, 9.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, xRange, true));
    }

    
    public void testIterateToFindRangeBounds_StatisticalCategoryDataset() {
        DefaultStatisticalCategoryDataset dataset
                = new DefaultStatisticalCategoryDataset();
        List visibleSeriesKeys = new ArrayList();
        assertNull(DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, false));
        dataset.add(1.0, 0.5, "R1", "C1");
        visibleSeriesKeys.add("R1");
        assertEquals(new Range(1.0, 1.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, false));
        assertEquals(new Range(0.5, 1.5),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, true));
    }

    
    public void testIterateToFindRangeBounds_MultiValueCategoryDataset() {
        DefaultMultiValueCategoryDataset dataset
                = new DefaultMultiValueCategoryDataset();
        List visibleSeriesKeys = new ArrayList();
        assertNull(DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, true));
        List values = Arrays.asList(new Double[] {new Double(1.0)});
        dataset.add(values, "R1", "C1");
        visibleSeriesKeys.add("R1");
        assertEquals(new Range(1.0, 1.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, true));

        values = Arrays.asList(new Double[] {new Double(2.0), new Double(3.0)});
        dataset.add(values, "R1", "C2");
        assertEquals(new Range(1.0, 3.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, true));

        values = Arrays.asList(new Double[] {new Double(-1.0),
                new Double(-2.0)});
        dataset.add(values, "R2", "C1");
        assertEquals(new Range(1.0, 3.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, true));
        visibleSeriesKeys.add("R2");
        assertEquals(new Range(-2.0, 3.0),
                DatasetUtilities.iterateToFindRangeBounds(dataset,
                visibleSeriesKeys, true));
    }

    
    public void testIterateRangeBounds_IntervalCategoryDataset() {
        TestIntervalCategoryDataset d = new TestIntervalCategoryDataset();
        d.addItem(1.0, 2.0, 3.0, "R1", "C1");
        assertEquals(new Range(1.0, 3.0),
                DatasetUtilities.iterateRangeBounds(d));

        d = new TestIntervalCategoryDataset();
        d.addItem(2.5, 2.0, 3.0, "R1", "C1");
        assertEquals(new Range(2.0, 3.0),
                DatasetUtilities.iterateRangeBounds(d));

        d = new TestIntervalCategoryDataset();
        d.addItem(4.0, 2.0, 3.0, "R1", "C1");
        assertEquals(new Range(2.0, 4.0),
                DatasetUtilities.iterateRangeBounds(d));

        d = new TestIntervalCategoryDataset();
        d.addItem(null, 2.0, 3.0, "R1", "C1");
        assertEquals(new Range(2.0, 3.0),
                DatasetUtilities.iterateRangeBounds(d));

        
        d = new TestIntervalCategoryDataset();
        d.addItem(null, null, null, "R1", "C1");
        assertNull(DatasetUtilities.iterateRangeBounds(d));

        d = new TestIntervalCategoryDataset();
        d.addItem(1.0, null, null, "R1", "C1");
        assertEquals(new Range(1.0, 1.0),
                DatasetUtilities.iterateRangeBounds(d));

        d = new TestIntervalCategoryDataset();
        d.addItem(null, 1.0, null, "R1", "C1");
        assertEquals(new Range(1.0, 1.0),
                DatasetUtilities.iterateRangeBounds(d));

        d = new TestIntervalCategoryDataset();
        d.addItem(null, null, 1.0, "R1", "C1");
        assertEquals(new Range(1.0, 1.0),
                DatasetUtilities.iterateRangeBounds(d));
    }

    
    public void testBug2849731() {
        TestIntervalCategoryDataset d = new TestIntervalCategoryDataset();
        d.addItem(2.5, 2.0, 3.0, "R1", "C1");
        d.addItem(4.0, null, null, "R2", "C1");
        assertEquals(new Range(2.0, 4.0),
                DatasetUtilities.iterateRangeBounds(d));
    }

    
    public void testBug2849731_2() {
        XYIntervalSeriesCollection d = new XYIntervalSeriesCollection();
        XYIntervalSeries s = new XYIntervalSeries("S1");
        s.add(1.0, Double.NaN, Double.NaN, Double.NaN, 1.5, Double.NaN);
        d.addSeries(s);
        Range r = DatasetUtilities.iterateDomainBounds(d);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(1.0, r.getUpperBound(), EPSILON);

        s.add(1.0, 1.5, Double.NaN, Double.NaN, 1.5, Double.NaN);
        r = DatasetUtilities.iterateDomainBounds(d);
        assertEquals(1.0, r.getLowerBound(), EPSILON);
        assertEquals(1.5, r.getUpperBound(), EPSILON);

        s.add(1.0, Double.NaN, 0.5, Double.NaN, 1.5, Double.NaN);
        r = DatasetUtilities.iterateDomainBounds(d);
        assertEquals(0.5, r.getLowerBound(), EPSILON);
        assertEquals(1.5, r.getUpperBound(), EPSILON);
    }

    
    public void testBug2849731_3() {
        XYIntervalSeriesCollection d = new XYIntervalSeriesCollection();
        XYIntervalSeries s = new XYIntervalSeries("S1");
        s.add(1.0, Double.NaN, Double.NaN, 1.5, Double.NaN, Double.NaN);
        d.addSeries(s);
        Range r = DatasetUtilities.iterateRangeBounds(d);
        assertEquals(1.5, r.getLowerBound(), EPSILON);
        assertEquals(1.5, r.getUpperBound(), EPSILON);

        s.add(1.0, 1.5, Double.NaN, Double.NaN, Double.NaN, 2.5);
        r = DatasetUtilities.iterateRangeBounds(d);
        assertEquals(1.5, r.getLowerBound(), EPSILON);
        assertEquals(2.5, r.getUpperBound(), EPSILON);

        s.add(1.0, Double.NaN, 0.5, Double.NaN, 3.5, Double.NaN);
        r = DatasetUtilities.iterateRangeBounds(d);
        assertEquals(1.5, r.getLowerBound(), EPSILON);
        assertEquals(3.5, r.getUpperBound(), EPSILON);
    }

}
