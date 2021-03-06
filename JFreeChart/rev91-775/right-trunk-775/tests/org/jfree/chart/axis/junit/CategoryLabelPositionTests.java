

package org.jfree.chart.axis.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.text.TextAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.util.RectangleAnchor;


public class CategoryLabelPositionTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(CategoryLabelPositionTests.class);
    }

    
    public CategoryLabelPositionTests(String name) {
        super(name);
    }
    
    
    public void testEquals() {
        CategoryLabelPosition p1 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT, 
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        CategoryLabelPosition p2 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT, 
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, 
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT, 
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertTrue(p1.equals(p2));

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertTrue(p1.equals(p2));
    
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertTrue(p1.equals(p2));
    
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, 
                CategoryLabelWidthType.RANGE, 0.44f);
        assertTrue(p1.equals(p2));
    
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, 
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, 
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertTrue(p1.equals(p2));
    
        p1 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER,  Math.PI / 6.0, 
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP, 
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0, 
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertTrue(p1.equals(p2));   
    }
    
    
    public void testHashCode() {
        CategoryLabelPosition a1 = new CategoryLabelPosition();
        CategoryLabelPosition a2 = new CategoryLabelPosition();
        assertTrue(a1.equals(a2));
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }

    
    public void testSerialization() {

        CategoryLabelPosition p1 = new CategoryLabelPosition();
        CategoryLabelPosition p2 = null;

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(p1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            p2 = (CategoryLabelPosition) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(p1, p2);      
    }

}
