

package org.jfree.chart.labels.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.labels.StandardXYToolTipGenerator;


public class StandardXYToolTipGeneratorTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(StandardXYToolTipGeneratorTests.class);
    }

    
    public StandardXYToolTipGeneratorTests(String name) {
        super(name);
    }

    
    public void testEquals() {

        
        String f1 = "{1}";
        String f2 = "{2}";
        NumberFormat xnf1 = new DecimalFormat("0.00");
        NumberFormat xnf2 = new DecimalFormat("0.000");
        NumberFormat ynf1 = new DecimalFormat("0.00");
        NumberFormat ynf2 = new DecimalFormat("0.000");

        StandardXYToolTipGenerator g1 = null;
        StandardXYToolTipGenerator g2 = null;

        g1 = new StandardXYToolTipGenerator(f1, xnf1, ynf1);
        g2 = new StandardXYToolTipGenerator(f1, xnf1, ynf1);
        assertTrue(g1.equals(g2));
        assertTrue(g2.equals(g1));

        g1 = new StandardXYToolTipGenerator(f2, xnf1, ynf1);
        assertFalse(g1.equals(g2));
        g2 = new StandardXYToolTipGenerator(f2, xnf1, ynf1);
        assertTrue(g1.equals(g2));

        g1 = new StandardXYToolTipGenerator(f2, xnf2, ynf1);
        assertFalse(g1.equals(g2));
        g2 = new StandardXYToolTipGenerator(f2, xnf2, ynf1);
        assertTrue(g1.equals(g2));

        g1 = new StandardXYToolTipGenerator(f2, xnf2, ynf2);
        assertFalse(g1.equals(g2));
        g2 = new StandardXYToolTipGenerator(f2, xnf2, ynf2);
        assertTrue(g1.equals(g2));

        DateFormat xdf1 = new SimpleDateFormat("d-MMM");
        DateFormat xdf2 = new SimpleDateFormat("d-MMM-yyyy");
        DateFormat ydf1 = new SimpleDateFormat("d-MMM");
        DateFormat ydf2 = new SimpleDateFormat("d-MMM-yyyy");

        g1 = new StandardXYToolTipGenerator(f1, xdf1, ydf1);
        g2 = new StandardXYToolTipGenerator(f1, xdf1, ydf1);
        assertTrue(g1.equals(g2));
        assertTrue(g2.equals(g1));

        g1 = new StandardXYToolTipGenerator(f1, xdf2, ydf1);
        assertFalse(g1.equals(g2));
        g2 = new StandardXYToolTipGenerator(f1, xdf2, ydf1);
        assertTrue(g1.equals(g2));

        g1 = new StandardXYToolTipGenerator(f1, xdf2, ydf2);
        assertFalse(g1.equals(g2));
        g2 = new StandardXYToolTipGenerator(f1, xdf2, ydf2);
        assertTrue(g1.equals(g2));

    }

    
    public void testHashCode() {
        StandardXYToolTipGenerator g1
                = new StandardXYToolTipGenerator();
        StandardXYToolTipGenerator g2
                = new StandardXYToolTipGenerator();
        assertTrue(g1.equals(g2));
        assertTrue(g1.hashCode() == g2.hashCode());
    }

    
    public void testCloning() {
        StandardXYToolTipGenerator g1 = new StandardXYToolTipGenerator();
        StandardXYToolTipGenerator g2 = null;
        try {
            g2 = (StandardXYToolTipGenerator) g1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(g1 != g2);
        assertTrue(g1.getClass() == g2.getClass());
        assertTrue(g1.equals(g2));
    }

    
    public void testSerialization() {

        StandardXYToolTipGenerator g1 = new StandardXYToolTipGenerator();
        StandardXYToolTipGenerator g2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(g1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            g2 = (StandardXYToolTipGenerator) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(g1, g2);

    }

}
