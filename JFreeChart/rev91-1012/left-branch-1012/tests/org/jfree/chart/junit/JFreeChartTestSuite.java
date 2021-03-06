

package org.jfree.chart.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jfree.chart.annotations.junit.AnnotationsPackageTests;
import org.jfree.chart.axis.junit.AxisPackageTests;
import org.jfree.chart.block.junit.BlockPackageTests;
import org.jfree.chart.entity.junit.EntityPackageTests;
import org.jfree.chart.labels.junit.LabelsPackageTests;
import org.jfree.chart.needle.junit.NeedlePackageTests;
import org.jfree.chart.plot.dial.junit.DialPackageTests;
import org.jfree.chart.plot.junit.PlotPackageTests;
import org.jfree.chart.renderer.category.junit.RendererCategoryPackageTests;
import org.jfree.chart.renderer.junit.RendererPackageTests;
import org.jfree.chart.renderer.xy.junit.RendererXYPackageTests;
import org.jfree.chart.title.junit.TitlePackageTests;
import org.jfree.chart.urls.junit.UrlsPackageTests;
import org.jfree.data.category.junit.DataCategoryPackageTests;
import org.jfree.data.gantt.junit.DataGanttPackageTests;
import org.jfree.data.junit.DataPackageTests;
import org.jfree.data.statistics.junit.DataStatisticsPackageTests;
import org.jfree.data.time.junit.DataTimePackageTests;
import org.jfree.data.time.ohlc.junit.OHLCPackageTests;
import org.jfree.data.xy.junit.DataXYPackageTests;


public class JFreeChartTestSuite extends TestCase {

    
    public static Test suite() {
        TestSuite suite = new TestSuite("JFreeChart");
        suite.addTest(ChartPackageTests.suite());
        suite.addTest(AnnotationsPackageTests.suite());
        suite.addTest(AxisPackageTests.suite());
        suite.addTest(BlockPackageTests.suite());
        suite.addTest(EntityPackageTests.suite());
        suite.addTest(LabelsPackageTests.suite());
        suite.addTest(NeedlePackageTests.suite());
        suite.addTest(PlotPackageTests.suite());
        suite.addTest(DialPackageTests.suite());
        suite.addTest(RendererPackageTests.suite());
        suite.addTest(RendererCategoryPackageTests.suite());
        suite.addTest(RendererXYPackageTests.suite());
        suite.addTest(TitlePackageTests.suite());
        suite.addTest(UrlsPackageTests.suite());
        suite.addTest(DataPackageTests.suite());
        suite.addTest(DataCategoryPackageTests.suite());
        suite.addTest(DataStatisticsPackageTests.suite());
        suite.addTest(DataTimePackageTests.suite());
        suite.addTest(OHLCPackageTests.suite());
        suite.addTest(DataXYPackageTests.suite());
        suite.addTest(DataGanttPackageTests.suite());
        return suite;
    }
    
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

}
