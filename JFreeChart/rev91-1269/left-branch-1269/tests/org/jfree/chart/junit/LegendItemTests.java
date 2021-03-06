

package org.jfree.chart.junit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.font.TextAttribute;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.AttributedString;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.LegendItem;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;


public class LegendItemTests extends TestCase {

    
    public static Test suite() {
        return new TestSuite(LegendItemTests.class);
    }

    
    public LegendItemTests(String name) {
        super(name);
    }

    
    public void testEquals() {

        LegendItem item1 = new LegendItem("Label", "Description",
                "ToolTip", "URL", true,
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), true, Color.red,
                true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0),
                new BasicStroke(2.1f), Color.green);
        LegendItem item2 = new LegendItem("Label", "Description",
                "ToolTip", "URL", true,
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));
        assertTrue(item2.equals(item1));

        item1 = new LegendItem("Label2", "Description", "ToolTip", "URL",
                true, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), true,
                Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description", "ToolTip", "URL",
                true, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", true, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", true, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                true, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.red, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, true, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.blue, new BasicStroke(1.2f),
                true, new Line2D.Double(1.0, 2.0, 3.0, 4.0),
                new BasicStroke(2.1f), Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, false, Color.blue, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, false, Color.yellow, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, false, Color.yellow, new BasicStroke(1.2f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, false, Color.yellow, new BasicStroke(2.1f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, false, Color.yellow, new BasicStroke(2.1f), true,
                new Line2D.Double(1.0, 2.0, 3.0, 4.0), new BasicStroke(2.1f),
                Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(1.0, 2.0, 3.0, 4.0),
                new BasicStroke(2.1f), Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(1.0, 2.0, 3.0, 4.0),
                new BasicStroke(2.1f),  Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(4.0, 3.0, 2.0, 1.0),
                new BasicStroke(2.1f), Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(4.0, 3.0, 2.0, 1.0),
                new BasicStroke(2.1f), Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(4.0, 3.0, 2.0, 1.0),
                new BasicStroke(3.3f), Color.green);
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(4.0, 3.0, 2.0, 1.0),
                new BasicStroke(3.3f), Color.green);
        assertTrue(item1.equals(item2));

        item1 = new LegendItem("Label2", "Description2", "ToolTip", "URL",
                false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0), false,
                Color.black, false, Color.yellow, new BasicStroke(2.1f), false,
            new Line2D.Double(4.0, 3.0, 2.0, 1.0), new BasicStroke(3.3f),
            Color.white
        );
        assertFalse(item1.equals(item2));
        item2 = new LegendItem("Label2", "Description2", "ToolTip",
                "URL", false, new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0),
                false, Color.black, false, Color.yellow, new BasicStroke(2.1f),
                false, new Line2D.Double(4.0, 3.0, 2.0, 1.0),
                new BasicStroke(3.3f),
                Color.white);
        assertTrue(item1.equals(item2));

        
        item1.setFillPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_VERTICAL));
        assertFalse(item1.equals(item2));
        item2.setFillPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.CENTER_VERTICAL));
        assertTrue(item1.equals(item2));

        
        item1.setLabelFont(new Font("Dialog", Font.PLAIN, 13));
        assertFalse(item1.equals(item2));
        item2.setLabelFont(new Font("Dialog", Font.PLAIN, 13));
        assertTrue(item1.equals(item2));

        
        item1.setLabelPaint(Color.red);
        assertFalse(item1.equals(item2));
        item2.setLabelPaint(Color.red);
        assertTrue(item1.equals(item2));

        
        item1.setFillPaint(new GradientPaint(1.0f, 2.0f, Color.green, 3.0f,
        		4.0f, Color.blue));
        assertFalse(item1.equals(item2));
        item2.setFillPaint(new GradientPaint(1.0f, 2.0f, Color.green, 3.0f,
        		4.0f, Color.blue));
        assertTrue(item1.equals(item2));

        
        item1.setOutlinePaint(new GradientPaint(1.1f, 2.2f, Color.green, 3.3f,
        		4.4f, Color.blue));
        assertFalse(item1.equals(item2));
        item2.setOutlinePaint(new GradientPaint(1.1f, 2.2f, Color.green, 3.3f,
        		4.4f, Color.blue));
        assertTrue(item1.equals(item2));

        
        item1.setLinePaint(new GradientPaint(0.1f, 0.2f, Color.green, 0.3f,
        		0.4f, Color.blue));
        assertFalse(item1.equals(item2));
        item2.setLinePaint(new GradientPaint(0.1f, 0.2f, Color.green, 0.3f,
        		0.4f, Color.blue));
        assertTrue(item1.equals(item2));

    }

    
    public void testSerialization() {
        LegendItem item1 = new LegendItem("Item", "Description",
                "ToolTip", "URL",
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), new GradientPaint(
                		5.0f, 6.0f, Color.blue, 7.0f, 8.0f, Color.gray));
        item1.setLabelPaint(new GradientPaint(1.0f, 2.0f, Color.red, 3.0f,
        		4.0f, Color.yellow));
        item1.setOutlinePaint(new GradientPaint(4.0f, 3.0f, Color.green, 2.0f,
        		1.0f, Color.red));
        item1.setLinePaint(new GradientPaint(1.0f, 2.0f, Color.white, 3.0f,
        		4.0f, Color.red));
        LegendItem item2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(item1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            item2 = (LegendItem) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(item1.equals(item2));
    }

    
    public void testSerialization2() {
        AttributedString as = new AttributedString("Test String");
        as.addAttribute(TextAttribute.FONT, new Font("Dialog", Font.PLAIN, 12));
        LegendItem item1 = new LegendItem(as, "Description", "ToolTip", "URL",
                new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0), Color.red);
        LegendItem item2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(item1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
            item2 = (LegendItem) in.readObject();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(item1, item2);
    }

    
    public void testCloning() {
        LegendItem item1 = new LegendItem("Item");
        LegendItem item2 = null;
        try {
            item2 = (LegendItem) item1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assertTrue(item1 != item2);
        assertTrue(item1.getClass() == item2.getClass());
        assertTrue(item1.equals(item2));

        
        assertTrue(item1.getDataset() == item2.getDataset());
    }

}
