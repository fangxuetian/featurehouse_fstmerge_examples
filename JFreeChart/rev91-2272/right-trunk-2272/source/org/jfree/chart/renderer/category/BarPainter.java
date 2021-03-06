

package org.jfree.chart.renderer.category;

import java.awt.Graphics2D;
import java.awt.geom.RectangularShape;

import org.jfree.chart.util.RectangleEdge;


public interface BarPainter {

    
    public void paintBar(Graphics2D g2, BarRenderer renderer,
            int row, int column, boolean selected,
            RectangularShape bar, RectangleEdge base);

    
    public void paintBarShadow(Graphics2D g2, BarRenderer renderer,
            int row, int column, boolean selected, 
            RectangularShape bar, RectangleEdge base, boolean pegShadow);

}
