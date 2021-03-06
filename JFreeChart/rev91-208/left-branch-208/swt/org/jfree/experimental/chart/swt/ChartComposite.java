

package org.jfree.experimental.chart.swt;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.event.EventListenerList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.plot.Zoomable;
import org.jfree.experimental.chart.swt.editor.SWTChartEditor;
import org.jfree.experimental.swt.SWTGraphics2D;
import org.jfree.experimental.swt.SWTUtils;


public class ChartComposite extends Composite implements ChartChangeListener,
                                                         ChartProgressListener,
                                                         SelectionListener,
                                                         Printable
{
    
    public static final boolean DEFAULT_BUFFER_USED = false;

    
    public static final int DEFAULT_WIDTH = 680;

    
    public static final int DEFAULT_HEIGHT = 420;

    
    public static final int DEFAULT_MINIMUM_DRAW_WIDTH = 300;

    
    public static final int DEFAULT_MINIMUM_DRAW_HEIGHT = 200;

    
    public static final int DEFAULT_MAXIMUM_DRAW_WIDTH = 800;

    
    public static final int DEFAULT_MAXIMUM_DRAW_HEIGHT = 600;

    
    public static final int DEFAULT_ZOOM_TRIGGER_DISTANCE = 10;

    
    public static final String PROPERTIES_COMMAND = "PROPERTIES";

    
    public static final String SAVE_COMMAND = "SAVE";

    
    public static final String PRINT_COMMAND = "PRINT";

    
    public static final String ZOOM_IN_BOTH_COMMAND = "ZOOM_IN_BOTH";

    
    public static final String ZOOM_IN_DOMAIN_COMMAND = "ZOOM_IN_DOMAIN";

    
    public static final String ZOOM_IN_RANGE_COMMAND = "ZOOM_IN_RANGE";

    
    public static final String ZOOM_OUT_BOTH_COMMAND = "ZOOM_OUT_BOTH";

    
    public static final String ZOOM_OUT_DOMAIN_COMMAND = "ZOOM_DOMAIN_BOTH";

    
    public static final String ZOOM_OUT_RANGE_COMMAND = "ZOOM_RANGE_BOTH";

    
    public static final String ZOOM_RESET_BOTH_COMMAND = "ZOOM_RESET_BOTH";

    
    public static final String ZOOM_RESET_DOMAIN_COMMAND = "ZOOM_RESET_DOMAIN";

    
    public static final String ZOOM_RESET_RANGE_COMMAND = "ZOOM_RESET_RANGE";

    
    public JFreeChart chart;

    
    private Canvas canvas;
    
    
    private EventListenerList chartMouseListeners;

    
    private boolean useBuffer;

    
    private boolean refreshBuffer;

    
    private boolean displayToolTips;

    
    private org.eclipse.swt.graphics.Image chartBuffer;

    
    private int chartBufferHeight;

    
    private int chartBufferWidth;

    
    private int minimumDrawWidth;

    
    private int minimumDrawHeight;

    
    private int maximumDrawWidth;

    
    private int maximumDrawHeight;

    
    private Menu popup;

    
    private ChartRenderingInfo info;
    
    
    private Point2D anchor;

    
    private double scaleX;

    
    private double scaleY;

    
    private PlotOrientation orientation = PlotOrientation.VERTICAL;
    
    
    private boolean domainZoomable = false;

    
    private boolean rangeZoomable = false;

    
    private org.eclipse.swt.graphics.Point zoomPoint = null;

    
    private transient Rectangle zoomRectangle = null;

    
    

    
    private int zoomTriggerDistance;
    
    
    private boolean horizontalAxisTrace = false;

    
    private boolean verticalAxisTrace = false;

    
    private transient int verticalTraceLineX;

    
    private transient int horizontalTraceLineY;

    
    private MenuItem zoomInBothMenuItem;

    
    private MenuItem zoomInDomainMenuItem;

    
    private MenuItem zoomInRangeMenuItem;

    
    private MenuItem zoomOutBothMenuItem;

    
    private MenuItem zoomOutDomainMenuItem;

    
    private MenuItem zoomOutRangeMenuItem;

    
    private MenuItem zoomResetBothMenuItem;

    
    private MenuItem zoomResetDomainMenuItem;

    
    private MenuItem zoomResetRangeMenuItem;

    
    private boolean enforceFileExtensions;

    
    private double zoomInFactor = 0.5;
    
    
    private double zoomOutFactor = 2.0;
    
    
    protected static ResourceBundle localizationResources 
        = ResourceBundle.getBundle("org.jfree.chart.LocalizationBundle");

    
    public ChartComposite(Composite comp, int style) {
        this(comp, 
                style,
                null,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_MINIMUM_DRAW_WIDTH,
                DEFAULT_MINIMUM_DRAW_HEIGHT,
                DEFAULT_MAXIMUM_DRAW_WIDTH,
                DEFAULT_MAXIMUM_DRAW_HEIGHT,
                DEFAULT_BUFFER_USED,
                true,  
                true,  
                true,  
                true,  
                true   
        );
    }

    
    public ChartComposite(Composite comp, int style, JFreeChart chart) {
        this( 
                comp, 
                style,
                chart,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_MINIMUM_DRAW_WIDTH,
                DEFAULT_MINIMUM_DRAW_HEIGHT,
                DEFAULT_MAXIMUM_DRAW_WIDTH,
                DEFAULT_MAXIMUM_DRAW_HEIGHT,
                DEFAULT_BUFFER_USED,
                true,  
                true,  
                true,  
                true,  
                true   
        );
    }

    
    public ChartComposite(Composite comp, int style, JFreeChart chart, 
            boolean useBuffer) {
        
        this(comp, style, chart,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_MINIMUM_DRAW_WIDTH,
                DEFAULT_MINIMUM_DRAW_HEIGHT,
                DEFAULT_MAXIMUM_DRAW_WIDTH,
                DEFAULT_MAXIMUM_DRAW_HEIGHT,
                useBuffer,
                true,  
                true,  
                true,  
                true,  
                true   
                );
    }
    
    
    public ChartComposite(
            Composite comp, 
            int style,
            JFreeChart chart,
            boolean properties,
            boolean save,
            boolean print,
            boolean zoom,
            boolean tooltips) {
        this(
                comp,
                style,
                chart,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_MINIMUM_DRAW_WIDTH,
                DEFAULT_MINIMUM_DRAW_HEIGHT,
                DEFAULT_MAXIMUM_DRAW_WIDTH,
                DEFAULT_MAXIMUM_DRAW_HEIGHT,
                DEFAULT_BUFFER_USED,
                properties,
                save,
                print,
                zoom,
                tooltips
                );
    }

    
    public ChartComposite(Composite comp, 
            int style,
            JFreeChart jfreechart,
            int width,
            int height,
            int minimumDrawW,
            int minimumDrawH,
            int maximumDrawW,
            int maximumDrawH,
            boolean usingBuffer,
            boolean properties,
            boolean save,
            boolean print,
            boolean zoom,
            boolean tooltips) {
        super(comp, style);
        this.setChart(jfreechart);
        this.chartMouseListeners = new EventListenerList();
        this.setLayout(new FillLayout());
        this.info = new ChartRenderingInfo();
        this.useBuffer = usingBuffer;
        this.refreshBuffer = false;
        this.minimumDrawWidth = minimumDrawW;
        this.minimumDrawHeight = minimumDrawH;
        this.maximumDrawWidth = maximumDrawW;
        this.maximumDrawHeight = maximumDrawH;
        this.zoomTriggerDistance = DEFAULT_ZOOM_TRIGGER_DISTANCE;
        this.setDisplayToolTips(tooltips);
        canvas = new Canvas(this, SWT.NO_BACKGROUND);
        canvas.addPaintListener(new PaintListener() {
            
            public void paintControl(PaintEvent e) {
        	
        	
        	Rectangle available = getBounds();
        	
                if (chart == null) {
                    canvas.drawBackground(e.gc, available.x, available.y, 
                	    available.width, available.height);
                    return;
                }
                SWTGraphics2D sg2 = new SWTGraphics2D(e.gc);

                
                boolean scale = false;
                int drawWidth = available.width;
                int drawHeight = available.height;
                if ( drawWidth == 0.0 || drawHeight == 0.0 ) return;
                scaleX = 1.0;
                scaleY = 1.0;
                if (drawWidth < minimumDrawWidth) {
                    scaleX = (double) drawWidth / minimumDrawWidth;
                    drawWidth = minimumDrawWidth;
                    scale = true;
                }
                else if (drawWidth > maximumDrawWidth) {
                    scaleX = (double) drawWidth / maximumDrawWidth;
                    drawWidth = maximumDrawWidth;
                    scale = true;
                }
                if (drawHeight < minimumDrawHeight) {
                    scaleY = (double) drawHeight / minimumDrawHeight;
                    drawHeight = minimumDrawHeight;
                    scale = true;
                }
                else if (drawHeight > maximumDrawHeight) {
                    scaleY = (double) drawHeight / maximumDrawHeight;
                    drawHeight = maximumDrawHeight;
                    scale = true;
                }
                
                if (useBuffer) {
                    
                    chartBuffer = (org.eclipse.swt.graphics.Image) 
                            canvas.getData("double-buffer-image");
                    
                    if (chartBuffer == null
                      || chartBufferWidth != available.width
                      || chartBufferHeight != available.height ) {
                        chartBufferWidth = available.width;
                        chartBufferHeight = available.height;
                        if (chartBuffer != null) {
                            chartBuffer.dispose();
                        }
                        chartBuffer = new org.eclipse.swt.graphics.Image( 
                                  getDisplay(), chartBufferWidth, 
                                  chartBufferHeight);
                        refreshBuffer = true;
                    }

                    
                    if (refreshBuffer) {
                        
                        GC gci = new GC(chartBuffer);
                        SWTGraphics2D sg2d = new SWTGraphics2D(gci);
                        if (scale) {
                            sg2d.scale(scaleX, scaleY);
                            chart.draw(sg2d, new Rectangle2D.Double(0, 0, 
                        	    drawWidth, drawHeight), getAnchor(), info);                            
                        } else {
                            chart.draw(sg2d, new Rectangle2D.Double(0, 0, 
                        	    drawWidth, drawHeight), getAnchor(), info);                            
                        }
                        canvas.setData("double-buffer-image", chartBuffer);
                        sg2d.dispose();
                        gci.dispose();
                        refreshBuffer = false;
                    }
                    
                    
                    sg2.drawImage(chartBuffer, 0, 0);
                }
                
                else {
                    chart.draw(sg2, new Rectangle2D.Double(0, 0, 
                        getBounds().width, getBounds().height), getAnchor(), info);
                }
                Rectangle area = getScreenDataArea();
                
                if (verticalAxisTrace && area.x < verticalTraceLineX 
                        && area.x + area.width > verticalTraceLineX) 
                    e.gc.drawLine(verticalTraceLineX, area.y, verticalTraceLineX, area.y + area.height);
                if (horizontalAxisTrace && area.y < horizontalTraceLineY 
                        && area.y + area.height > horizontalTraceLineY) 
                    e.gc.drawLine(area.x, horizontalTraceLineY, area.x + area.width, horizontalTraceLineY);
                verticalTraceLineX = 0;
                horizontalTraceLineY = 0;
                if (zoomRectangle != null) e.gc.drawRectangle(zoomRectangle);
                sg2.dispose();
            }
        } );
        if (chart != null) {
            chart.addChangeListener(this);
            Plot plot = chart.getPlot();
            this.domainZoomable = false;
            this.rangeZoomable = false;
            if (plot instanceof Zoomable) {
                Zoomable z = (Zoomable) plot;
                this.domainZoomable = z.isDomainZoomable();
                this.rangeZoomable = z.isRangeZoomable();
                this.orientation = z.getOrientation();
            }
        }

        
        this.popup = null;
        if (properties || save || print || zoom)
            this.popup = createPopupMenu(properties, save, print, zoom);

        Listener listener = new Listener() {
            public void handleEvent (Event event) {
                switch (event.type) {
                    case SWT.MouseDown:
                        Rectangle scaledDataArea = getScreenDataArea(event.x, event.y);
                        zoomPoint = getPointInRectangle(event.x, event.y, scaledDataArea);
                        Rectangle insets = getClientArea();
                        int x = (int) ((event.x - insets.x) / scaleX);
                        int y = (int) ((event.y - insets.y) / scaleY);

                        anchor = new Point2D.Double(x, y);
                        chart.setNotify(true);  
                        canvas.redraw();
                        
                        Object[] listeners = chartMouseListeners.getListeners(
                                ChartMouseListener.class);
                        if (listeners.length == 0) {
                            return;
                        }

                        ChartEntity entity = null;
                        if (info != null) 
                        {
                            EntityCollection entities 
                                    = info.getEntityCollection();
                            if (entities != null) {
                                entity = entities.getEntity(x, y);
                            }
                        }
                        java.awt.event.MouseEvent mouseEvent = SWTUtils.toAwtMouseEvent(event); 
                        ChartMouseEvent chartEvent = new ChartMouseEvent(getChart(), mouseEvent, entity);
                        for (int i = listeners.length - 1; i >= 0; i -= 1) {
                            ((ChartMouseListener) 
                                    listeners[i]).chartMouseClicked(chartEvent);
                        }
                        break;
                    case SWT.MouseMove:
                        
                        if ( horizontalAxisTrace || verticalAxisTrace ) {
                            horizontalTraceLineY = event.y;
                            verticalTraceLineX = event.x;
                            canvas.redraw();
                        }
                        
                        if (displayToolTips) {                            
                            String s = getToolTipText(new MouseEvent(event));
                            if (s == null && canvas.getToolTipText() != null
                        	    || s!=null && !s.equals(canvas.getToolTipText()))
                                canvas.setToolTipText(s);
                        }
                        
                        if (zoomPoint == null) {
                            return;
                        }
                        scaledDataArea = getScreenDataArea(zoomPoint.x, zoomPoint.y);
                        org.eclipse.swt.graphics.Point movingPoint 
                            = getPointInRectangle(event.x, event.y, scaledDataArea);
                        
                        boolean hZoom = false;
                        boolean vZoom = false;
                        if (orientation == PlotOrientation.HORIZONTAL) {
                            hZoom = rangeZoomable;
                            vZoom = domainZoomable;
                        }
                        else {
                            hZoom = domainZoomable;              
                            vZoom = rangeZoomable;
                        }
                        if (hZoom && vZoom) {
                            
                            zoomRectangle = new Rectangle(zoomPoint.x, zoomPoint.y, 
                                    movingPoint.x - zoomPoint.x, movingPoint.y - zoomPoint.y);                            
                        }
                        else if (hZoom) {
                            zoomRectangle = new Rectangle(zoomPoint.x, scaledDataArea.y,
                                    movingPoint.x - zoomPoint.x, scaledDataArea.height);
                        }
                        else if (vZoom) {
                            zoomRectangle = new Rectangle(
                                    scaledDataArea.x, zoomPoint.y,
                                    scaledDataArea.width, event.y - zoomPoint.y);
                        }
                        canvas.redraw();
                        break;
                    case SWT.MouseUp:
                        if (zoomRectangle == null) {
                            Rectangle screenDataArea = getScreenDataArea(event.x, event.y);
                            if (screenDataArea != null) {
                                zoomPoint = getPointInRectangle(event.x, event.y, screenDataArea);
                            }
                            if (popup != null && event.button == 3) {
                                org.eclipse.swt.graphics.Point pt = canvas.toDisplay(event.x, event.y);
                                displayPopupMenu(pt.x, pt.y);
                            }
                        }
                        else {
                            hZoom = false;
                            vZoom = false;
                            if (orientation == PlotOrientation.HORIZONTAL) {
                                hZoom = rangeZoomable;
                                vZoom = domainZoomable;
                            }
                            else {
                                hZoom = domainZoomable;              
                                vZoom = rangeZoomable;
                            }
                            boolean zoomTrigger1 = hZoom 
                                    && Math.abs(zoomRectangle.width) 
                                    >= zoomTriggerDistance;
                            boolean zoomTrigger2 = vZoom 
                                    && Math.abs(zoomRectangle.height) 
                                    >= zoomTriggerDistance;
                            if (zoomTrigger1 || zoomTrigger2) {
                                
                                if ((hZoom && (zoomRectangle.x + zoomRectangle.width < zoomPoint.x)) 
                                        || (vZoom && (zoomRectangle.y + zoomRectangle.height < zoomPoint.y))) 
                                    restoreAutoBounds();
                                else zoom(zoomRectangle);
                                canvas.redraw();
                            }
                        }
                        zoomPoint = null;
                        zoomRectangle = null;
                        break;
                    default:
                        zoomPoint = null;
                        zoomRectangle = null;
                }
            }
        };
        canvas.addListener(SWT.MouseDown, listener);
        canvas.addListener(SWT.MouseMove, listener);
        canvas.addListener(SWT.MouseUp, listener);
        
        this.enforceFileExtensions = true;
    }
        
    
    public double getScaleX() {
        return this.scaleX;
    }
    
    
    public double getScaleY() {
        return this.scaleY;
    }
    
    
    public Point2D getAnchor() {
        return this.anchor;   
    }
    
    
    protected void setAnchor(Point2D anchor) {
        this.anchor = anchor;   
    }

    
    public JFreeChart getChart() {
        return this.chart;
    }

    
    public void setChart(JFreeChart chart) {
        
        if (this.chart != null) {
            this.chart.removeChangeListener(this);
            this.chart.removeProgressListener(this);
        }

        
        this.chart = chart;
        if (chart != null) {
            this.chart.addChangeListener(this);
            this.chart.addProgressListener(this);
            Plot plot = chart.getPlot();
            this.domainZoomable = false;
            this.rangeZoomable = false;
            if (plot instanceof Zoomable) {
                Zoomable z = (Zoomable) plot;
                this.domainZoomable = z.isDomainZoomable();
                this.rangeZoomable = z.isRangeZoomable();
                this.orientation = z.getOrientation();
            }
        }
        else {
            this.domainZoomable = false;
            this.rangeZoomable = false;
        }
        if (this.useBuffer) {
            this.refreshBuffer = true;
        }
    }

    
    public double getZoomInFactor() {
        return this.zoomInFactor;   
    }
    
    
    public void setZoomInFactor(double factor) {
        this.zoomInFactor = factor;
    }
    
    
    public double getZoomOutFactor() {
        return this.zoomOutFactor;   
    }
    
    
    public void setZoomOutFactor(double factor) {
        this.zoomOutFactor = factor;
    }
    
    
    private void attemptEditChartProperties() {
        SWTChartEditor editor = new SWTChartEditor(canvas.getDisplay(), this.chart);
            
        editor.open();
    }

    
    public boolean isEnforceFileExtensions() {
        return this.enforceFileExtensions;
    }

    
    public void setEnforceFileExtensions(boolean enforce) {
        this.enforceFileExtensions = enforce;
    }

    
    public void doSaveAs() throws IOException {
        FileDialog fileDialog = new FileDialog(canvas.getShell(), SWT.SAVE);
        String[] extensions = { "*.png" };
        fileDialog.setFilterExtensions(extensions);
        String filename = fileDialog.open();
        if (filename != null) {
            if (isEnforceFileExtensions()) {
                if (!filename.endsWith(".png")) {
                    filename = filename + ".png";
                }
            }
            
            ChartUtilities.saveChartAsPNG(new File(filename), this.chart, 
                    canvas.getSize().x, canvas.getSize().y);
        }
    }

    
    private org.eclipse.swt.graphics.Point getPointInRectangle(int x, int y, Rectangle area) {
        x = (int) Math.max(area.x, Math.min(x, area.x + area.width));   
        y = (int) Math.max(area.y, Math.min(y, area.y + area.height));
        return new org.eclipse.swt.graphics.Point(x, y);
    }

    
    public void zoomInBoth(double x, double y) {
        zoomInDomain(x, y);
        zoomInRange(x, y);
    }

    
    public void zoomInDomain(double x, double y) {
        Plot p = this.chart.getPlot();
        if (p instanceof Zoomable) 
        {
            Zoomable plot = (Zoomable) p;
            plot.zoomDomainAxes(this.zoomInFactor, this.info.getPlotInfo(), 
                    translateScreenToJava2D(new Point((int) x, (int) y)));
        }
    }

    
    public void zoomInRange(double x, double y) {
        Plot p = this.chart.getPlot();
        if (p instanceof Zoomable) {
            Zoomable z = (Zoomable) p;
            z.zoomRangeAxes(this.zoomInFactor, this.info.getPlotInfo(), 
                    translateScreenToJava2D(new Point((int) x, (int) y)));
        }
    }

    
    public void zoomOutBoth(double x, double y) {
        zoomOutDomain(x, y);
        zoomOutRange(x, y);
    }

    
    public void zoomOutDomain(double x, double y) {
        Plot p = this.chart.getPlot();
        if (p instanceof Zoomable) {
            Zoomable z = (Zoomable) p;
            z.zoomDomainAxes(this.zoomOutFactor, this.info.getPlotInfo(), 
                    translateScreenToJava2D(new Point((int) x, (int) y)));
        }
    }

    
    public void zoomOutRange(double x, double y) {
        Plot p = this.chart.getPlot();
        if (p instanceof Zoomable) {
            Zoomable z = (Zoomable) p;
            z.zoomRangeAxes(this.zoomOutFactor, this.info.getPlotInfo(), 
                    translateScreenToJava2D(new Point((int) x, (int) y)));
        }
    }

    
    public void zoom(Rectangle selection) {

        
        
        Point2D selectOrigin = translateScreenToJava2D(
                new Point(selection.x, selection.y));
        PlotRenderingInfo plotInfo = this.info.getPlotInfo();
        Rectangle scaledDataArea = getScreenDataArea(
                (int) (selection.x + selection.width)/2, 
                (int) (selection.y + selection.height)/2);
        if ((selection.height > 0) && (selection.width > 0)) {

            double hLower = (selection.x - scaledDataArea.x) 
                / (double) scaledDataArea.width;
            double hUpper = (selection.x + selection.width - scaledDataArea.x) 
                / (double) scaledDataArea.width;
            double vLower = (scaledDataArea.y + scaledDataArea.height - selection.y - selection.height) 
                / (double) scaledDataArea.height;
            double vUpper = (scaledDataArea.y + scaledDataArea.height - selection.y) 
                / (double) scaledDataArea.height;
            Plot p = this.chart.getPlot();
            if (p instanceof Zoomable) {
                Zoomable z = (Zoomable) p;
                if (z.getOrientation() == PlotOrientation.HORIZONTAL) {
                    z.zoomDomainAxes(vLower, vUpper, plotInfo, selectOrigin);
                    z.zoomRangeAxes(hLower, hUpper, plotInfo, selectOrigin);
                }
                else {
                    z.zoomDomainAxes(hLower, hUpper, plotInfo, selectOrigin);
                    z.zoomRangeAxes(vLower, vUpper, plotInfo, selectOrigin);
                }
            }

        }

    }

    
    public void chartChanged(ChartChangeEvent event) {
        this.refreshBuffer = true;
        Plot plot = chart.getPlot();
        if (plot instanceof Zoomable) {
            Zoomable z = (Zoomable) plot;
            this.orientation = z.getOrientation();
        }
        canvas.redraw();
    }

    
    public void forceRedraw() {
        Event ev = new Event();
        ev.gc = new GC(canvas);
        ev.x = 0;
        ev.y = 0;
        ev.width = canvas.getBounds().width;
        ev.height = canvas.getBounds().height;
        ev.count = 0;
        canvas.notifyListeners(SWT.Paint, ev);
        ev.gc.dispose();
    }
    
    
    public void chartProgress(ChartProgressEvent event) {
        
    }

    
    public void restoreAutoBounds() {
        restoreAutoDomainBounds();
        restoreAutoRangeBounds();
    }

    
    public void restoreAutoDomainBounds() {
        Plot p = this.chart.getPlot();
        if (p instanceof Zoomable) 
        {
            Zoomable z = (Zoomable) p;
            z.zoomDomainAxes(0.0, this.info.getPlotInfo(), SWTUtils.toAwtPoint(this.zoomPoint));
        }
    }

    
    public void restoreAutoRangeBounds() {
        Plot p = this.chart.getPlot();
        if (p instanceof ValueAxisPlot) {
            Zoomable z = (Zoomable) p;
            z.zoomRangeAxes(0.0, this.info.getPlotInfo(), SWTUtils.toAwtPoint(this.zoomPoint)); 
        }
    }

    
    public Rectangle scale(Rectangle2D rect) {
        Rectangle insets = this.getClientArea();
        int x = (int) Math.round(rect.getX() * getScaleX()) + insets.x;
        int y = (int) Math.round(rect.getY() * this.getScaleY()) + insets.y;
        int w = (int) Math.round(rect.getWidth() * this.getScaleX());
        int h = (int) Math.round(rect.getHeight() * this.getScaleY());
        return new Rectangle(x, y, w, h);
    }

    
    public Rectangle getScreenDataArea() {
        Rectangle2D dataArea = this.info.getPlotInfo().getDataArea();
        Rectangle clientArea = this.getClientArea();
        int x = (int) (dataArea.getX() * this.scaleX + clientArea.x);
        int y = (int) (dataArea.getY() * this.scaleY + clientArea.y);
        int w = (int) (dataArea.getWidth() * this.scaleX);
        int h = (int) (dataArea.getHeight() * this.scaleY);
        return new Rectangle(x, y, w, h);
    }
    
    
    public Rectangle getScreenDataArea(int x, int y) {
        PlotRenderingInfo plotInfo = this.info.getPlotInfo();
        Rectangle result;
        if (plotInfo.getSubplotCount() == 0)
            result = getScreenDataArea();
        else {
            
            
            Point2D selectOrigin = translateScreenToJava2D(new Point(x, y));
            int subplotIndex = plotInfo.getSubplotIndex(selectOrigin);
            if (subplotIndex == -1) {
                return null;
            }
            result = scale(plotInfo.getSubplotInfo(subplotIndex).getDataArea());
        }
        return result;
    }

    
    public Point translateJava2DToScreen(Point2D java2DPoint) {
        Rectangle insets = this.getClientArea();
        int x = (int) (java2DPoint.getX() * this.scaleX + insets.x);
        int y = (int) (java2DPoint.getY() * this.scaleY + insets.y);
        return new Point(x, y);
    }

    
    public Point translateScreenToJavaSWT(Point screenPoint) {
        Rectangle insets = this.getClientArea();
        int x = (int) ((screenPoint.x - insets.x) / this.scaleX);
        int y = (int) ((screenPoint.y - insets.y) / this.scaleY);
        return new Point(x, y);
    }

    
    public Point2D translateScreenToJava2D(Point screenPoint) {
        Rectangle insets = this.getClientArea();
        int x = (int) ((screenPoint.x - insets.x) / this.scaleX);
        int y = (int) ((screenPoint.y - insets.y) / this.scaleY);
        return new Point2D.Double(x, y);
    }

    
    public boolean getHorizontalAxisTrace() {
        return this.horizontalAxisTrace;    
    }
    
    
    public void setHorizontalAxisTrace(boolean flag) {
        this.horizontalAxisTrace = flag;
    }
    
    
    public boolean getVerticalAxisTrace() {
        return this.verticalAxisTrace;    
    }
    
    
    public void setVerticalAxisTrace(boolean flag) {
        this.verticalAxisTrace = flag;
    }

    
    public void setDisplayToolTips( boolean displayToolTips ) {
        this.displayToolTips = displayToolTips;
    }

    
    public String getToolTipText(org.eclipse.swt.events.MouseEvent e) {
        String result = null;
        if (this.info != null) {
            EntityCollection entities = this.info.getEntityCollection();
            if (entities != null) {
                Rectangle insets = getClientArea();
                ChartEntity entity = entities.getEntity(
                        (int) ((e.x - insets.x) / this.scaleX),
                        (int) ((e.y - insets.y) / this.scaleY));
                if (entity != null) {
                    result = entity.getToolTipText();
                }
            }
        }
        return result;

    }

    
    protected void displayPopupMenu(int x, int y) {
        if (this.popup != null) {
            
            
            Plot plot = this.chart.getPlot();
            boolean isDomainZoomable = false;
            boolean isRangeZoomable = false;
            if (plot instanceof Zoomable) {
                Zoomable z = (Zoomable) plot;
                isDomainZoomable = z.isDomainZoomable();
                isRangeZoomable = z.isRangeZoomable();
            }
            if (this.zoomInDomainMenuItem != null) {
                this.zoomInDomainMenuItem.setEnabled(isDomainZoomable);
            }
            if (this.zoomOutDomainMenuItem != null) {
                this.zoomOutDomainMenuItem.setEnabled(isDomainZoomable);
            } 
            if (this.zoomResetDomainMenuItem != null) {
                this.zoomResetDomainMenuItem.setEnabled(isDomainZoomable);
            }

            if (this.zoomInRangeMenuItem != null) {
                this.zoomInRangeMenuItem.setEnabled(isRangeZoomable);
            }
            if (this.zoomOutRangeMenuItem != null) {
                this.zoomOutRangeMenuItem.setEnabled(isRangeZoomable);
            }

            if (this.zoomResetRangeMenuItem != null) {
                this.zoomResetRangeMenuItem.setEnabled(isRangeZoomable);
            }

            if (this.zoomInBothMenuItem != null) {
                this.zoomInBothMenuItem.setEnabled(
                    isDomainZoomable & isRangeZoomable
                );
            }
            if (this.zoomOutBothMenuItem != null) {
                this.zoomOutBothMenuItem.setEnabled(
                    isDomainZoomable & isRangeZoomable
                );
            }
            if (this.zoomResetBothMenuItem != null) {
                this.zoomResetBothMenuItem.setEnabled(
                    isDomainZoomable & isRangeZoomable
                );
            }

            this.popup.setLocation(x, y);
            this.popup.setVisible(true);
        }

    }

    
    public void createChartPrintJob() {
        
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        PageFormat pf2 = job.pageDialog(pf);
        if (pf2 != pf) {
            job.setPrintable(this, pf2);
            if (job.printDialog()) {
                try {
                    job.print();
                }
                catch (PrinterException e) {
                    MessageBox messageBox = new MessageBox( 
                            canvas.getShell(), SWT.OK | SWT.ICON_ERROR );
                    messageBox.setMessage( e.getMessage() );
                    messageBox.open();
                }
            }
        }
    }

    
    protected Menu createPopupMenu(boolean properties, boolean save, 
            boolean print, boolean zoom) {
        
        Menu result = new Menu(this);
        boolean separator = false;

        if ( properties ) {
            MenuItem propertiesItem = new MenuItem(result, SWT.PUSH);
            propertiesItem.setText(localizationResources.getString(
                    "Properties..."));
            propertiesItem.setData(PROPERTIES_COMMAND);
            propertiesItem.addSelectionListener(this);
            separator = true;
        }
        if (save) 
        {
            if (separator) {
                new MenuItem(result, SWT.SEPARATOR);
                separator = false;
            }
            MenuItem saveItem = new MenuItem(result, SWT.NONE);
            saveItem.setText(localizationResources.getString("Save_as..."));
            saveItem.setData(SAVE_COMMAND);
            saveItem.addSelectionListener(this);
            separator = true;
        }
        if (print) {
            if (separator) {
                new MenuItem(result, SWT.SEPARATOR);
                separator = false;
            }
            MenuItem printItem = new MenuItem(result, SWT.NONE);
            printItem.setText(localizationResources.getString("Print..."));
            printItem.setData(PRINT_COMMAND);
            printItem.addSelectionListener(this);
            separator = true;
        }
        if (zoom) {
            if (separator) {
                new MenuItem(result, SWT.SEPARATOR);
                separator = false;
            }

            Menu zoomInMenu = new Menu(result);
            MenuItem zoomInMenuItem = new MenuItem(result, SWT.CASCADE);
            zoomInMenuItem.setText(localizationResources.getString("Zoom_In"));
            zoomInMenuItem.setMenu(zoomInMenu);

            this.zoomInBothMenuItem = new MenuItem(zoomInMenu, SWT.PUSH);
            this.zoomInBothMenuItem.setText(localizationResources.getString(
                    "All_Axes"));
            this.zoomInBothMenuItem.setData(ZOOM_IN_BOTH_COMMAND);
            this.zoomInBothMenuItem.addSelectionListener(this);

            new MenuItem(zoomInMenu, SWT.SEPARATOR);

            this.zoomInDomainMenuItem = new MenuItem(zoomInMenu, SWT.PUSH);
            this.zoomInDomainMenuItem.setText(localizationResources.getString(
                    "Domain_Axis" ) );
            this.zoomInDomainMenuItem.setData(ZOOM_IN_DOMAIN_COMMAND);
            this.zoomInDomainMenuItem.addSelectionListener(this);

            this.zoomInRangeMenuItem = new MenuItem(zoomInMenu, SWT.PUSH);
            this.zoomInRangeMenuItem.setText(localizationResources.getString(
                    "Range_Axis" ) );
            this.zoomInRangeMenuItem.setData(ZOOM_IN_RANGE_COMMAND);
            this.zoomInRangeMenuItem.addSelectionListener(this);

            Menu zoomOutMenu = new Menu( result );
            MenuItem zoomOutMenuItem = new MenuItem(result, SWT.CASCADE);
            zoomOutMenuItem.setText(localizationResources.getString(
                    "Zoom_Out"));
            zoomOutMenuItem.setMenu(zoomOutMenu);

            this.zoomOutBothMenuItem = new MenuItem(zoomOutMenu, SWT.PUSH);
            this.zoomOutBothMenuItem.setText(localizationResources.getString(
                    "All_Axes"));
            this.zoomOutBothMenuItem.setData(ZOOM_OUT_BOTH_COMMAND);
            this.zoomOutBothMenuItem.addSelectionListener(this);
            
            new MenuItem(zoomOutMenu, SWT.SEPARATOR);

            this.zoomOutDomainMenuItem = new MenuItem(zoomOutMenu, SWT.PUSH);
            this.zoomOutDomainMenuItem.setText(localizationResources.getString(
                    "Domain_Axis"));
            this.zoomOutDomainMenuItem.setData(ZOOM_OUT_DOMAIN_COMMAND);
            this.zoomOutDomainMenuItem.addSelectionListener( this );

            this.zoomOutRangeMenuItem = new MenuItem(zoomOutMenu, SWT.PUSH);
            this.zoomOutRangeMenuItem.setText(
                    localizationResources.getString("Range_Axis"));
            this.zoomOutRangeMenuItem.setData(ZOOM_OUT_RANGE_COMMAND);
            this.zoomOutRangeMenuItem.addSelectionListener(this);

            Menu autoRangeMenu = new Menu(result);
            MenuItem autoRangeMenuItem = new MenuItem(result, SWT.CASCADE);
            autoRangeMenuItem.setText(localizationResources.getString(
                    "Auto_Range"));
            autoRangeMenuItem.setMenu(autoRangeMenu);

            this.zoomResetBothMenuItem = new MenuItem(autoRangeMenu, SWT.PUSH);
            this.zoomResetBothMenuItem.setText(localizationResources.getString(
                    "All_Axes"));
            this.zoomResetBothMenuItem.setData(ZOOM_RESET_BOTH_COMMAND);
            this.zoomResetBothMenuItem.addSelectionListener(this);
            
            new MenuItem(autoRangeMenu, SWT.SEPARATOR);

            this.zoomResetDomainMenuItem = new MenuItem(autoRangeMenu, 
                    SWT.PUSH);
            this.zoomResetDomainMenuItem.setText(
                    localizationResources.getString("Domain_Axis"));
            this.zoomResetDomainMenuItem.setData(ZOOM_RESET_DOMAIN_COMMAND);
            this.zoomResetDomainMenuItem.addSelectionListener(this);
               
            this.zoomResetRangeMenuItem = new MenuItem(autoRangeMenu, SWT.PUSH);
            this.zoomResetRangeMenuItem.setText(
                    localizationResources.getString("Range_Axis"));
            this.zoomResetRangeMenuItem.setData(ZOOM_RESET_RANGE_COMMAND);
            this.zoomResetRangeMenuItem.addSelectionListener(this);
        }
        
        return result;
    }

    
    public void widgetDefaultSelected(SelectionEvent e) {
        
        
    }

    
    public void widgetSelected(SelectionEvent e) {
        String command = (String) ((MenuItem) e.getSource()).getData();
        if (command.equals(PROPERTIES_COMMAND)) {
            attemptEditChartProperties();
        }
        else if (command.equals(SAVE_COMMAND)) {
            try {
                doSaveAs();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (command.equals(PRINT_COMMAND)) {
            createChartPrintJob();
        }
        
        else if (command.equals(ZOOM_IN_BOTH_COMMAND)) {
            zoomInBoth( e.x, e.y );
        }
        else if (command.equals(ZOOM_IN_DOMAIN_COMMAND)) {
            zoomInDomain( e.x, e.y );
        }
        else if (command.equals(ZOOM_IN_RANGE_COMMAND)) {
            zoomInRange( e.x, e.y );
        }
        else if (command.equals(ZOOM_OUT_BOTH_COMMAND)) {
            zoomOutBoth( e.x, e.y );
        }
        else if (command.equals(ZOOM_OUT_DOMAIN_COMMAND)) {
            zoomOutDomain( e.x, e.y );
        }
        else if (command.equals(ZOOM_OUT_RANGE_COMMAND)) {
            zoomOutRange( e.x, e.y );
        }
        else if (command.equals(ZOOM_RESET_BOTH_COMMAND)) {
            restoreAutoBounds();
        }
        else if (command.equals(ZOOM_RESET_DOMAIN_COMMAND)) {
            restoreAutoDomainBounds();
        }
        else if (command.equals(ZOOM_RESET_RANGE_COMMAND)) {
            restoreAutoRangeBounds();
        }
        this.forceRedraw();
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) 
        throws PrinterException {
        if (pageIndex != 0) {
            return NO_SUCH_PAGE;
        }
        
        return PAGE_EXISTS;
    }
    
}
