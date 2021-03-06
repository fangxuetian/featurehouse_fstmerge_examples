

package org.jfree.experimental.chart.demo;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.util.ApplicationFrame;
import org.jfree.chart.util.RefineryUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.axis.LogAxis;


public class LogAxisDemo1 extends ApplicationFrame {

    
    public LogAxisDemo1(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    private static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Log Axis Demo 1",
            "X", 
            "Y", 
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        LogAxis xAxis = new LogAxis("X");
        LogAxis yAxis = new LogAxis("Y");
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
        return chart;
    }
    
    
    private static XYDataset createDataset() {
        XYSeries series = new XYSeries("Random Data");
        series.add(1.0, 500.2);
        series.add(5.0, 694.1);
        series.add(4.0, 100.0);
        series.add(12.5, 734.4);
        series.add(17.3, 453.2);
        series.add(21.2, 500.2);
        series.add(21.9, 9005.5);
        series.add(25.6, 734.4);
        series.add(3000.0, 453.2);
        return new XYSeriesCollection(series);       
    }

    
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
    
    
    public static void main(String[] args) {

        LogAxisDemo1 demo = new LogAxisDemo1("Log Axis Demo 1");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
