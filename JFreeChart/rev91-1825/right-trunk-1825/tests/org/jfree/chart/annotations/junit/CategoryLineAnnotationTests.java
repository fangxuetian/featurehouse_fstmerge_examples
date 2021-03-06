

package org.jfree.chart.annotations.junit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.annotations.CategoryLineAnnotation;
import org.jfree.chart.util.PublicCloneable;


public class CategoryLineAnnotationTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(CategoryLineAnnotationTests.class);
    }

    
    public CategoryLineAnnotationTests(String name) {
        super(name);
    }

    
    public void testEquals() {

        BasicStroke s1 = new BasicStroke(1.0f);
        BasicStroke s2 = new BasicStroke(2.0f);
        CategoryLineAnnotation a1 = new CategoryLineAnnotation(
            "Category 1", 1.0, "Category 2", 2.0, Color.red, s1);
        CategoryLineAnnotation a2 = new CategoryLineAnnotation(
            "Category 1", 1.0, "Category 2", 2.0, Color.red, s1);
        assertTrue(a1.equals(a2));
        assertTrue(a2.equals(a1));

        
        a1.setCategory1("Category A");
        assertFalse(a1.equals(a2));
        a2.setCategory1("Category A");
        assertTrue(a1.equals(a2));

        
        a1.setValue1(0.15);
        assertFalse(a1.equals(a2));
        a2.setValue1(0.15);
        assertTrue(a1.equals(a2));

        
        a1.setCategory2("Category B");
        assertFalse(a1.equals(a2));
        a2.setCategory2("Category B");
        assertTrue(a1.equals(a2));

        
        a1.setValue2(0.25);
        assertFalse(a1.equals(a2));
        a2.setValue2(0.25);
        assertTrue(a1.equals(a2));

        
        a1.setPaint(Color.yellow);
        assertFalse(a1.equals(a2));
        a2.setPaint(Color.yellow);
        assertTrue(a1.equals(a2));

        
        a1.setStroke(s2);
        assertFalse(a1.equals(a2));
        a2.setStroke(s2);
        assertTrue(a1.equals(a2));
    }

    
    public void testHashcode() {
        CategoryLineAnnotation a1 = new CategoryLineAnnotation(
            "Category 1", 1.0, "Category 2", 2.0, Color.red,
            new BasicStroke(1.0f));
        CategoryLineAnnotation a2 = new CategoryLineAnnotation(
            "Category 1", 1.0, "Category 2", 2.0, Color.red,
            new BasicStroke(1.0f));
        assertTrue(a1.equals(a2));
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }

    
    public void testCloning() {
        CategoryLineAnnotation a1 = new CategoryLineAnnotation(
            "Category 1", 1.0, "Category 2", 2.0, Color.red,
            new BasicStroke(1.0f));
        CategoryLineAnnotation a2 = null;
        try {
            a2 = (CategoryLineAnnotation) a1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(a1 != a2);
        assertTrue(a1.getClass() == a2.getClass());
        assertTrue(a1.equals(a2));
    }

    
    public void testPublicCloneable() {
        CategoryLineAnnotation a1 = new CategoryLineAnnotation(
                "Category 1", 1.0, "Category 2", 2.0, Color.red,
                new BasicStroke(1.0f));
        assertTrue(a1 instanceof PublicCloneable);
    }

    
    public void testSerialization() {

        CategoryLineAnnotation a1 = new CategoryLineAnnotation(
            "Category 1", 1.0, "Category 2", 2.0, Color.red,
            new BasicStroke(1.0f));
        CategoryLineAnnotation a2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(a1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray())
            );
            a2 = (CategoryLineAnnotation) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(a1, a2);

    }

}
