

package org.jfree.chart.resources;

import java.util.ListResourceBundle;


public class JFreeChartResources extends ListResourceBundle {

    
    public Object[][] getContents() {
        return CONTENTS;
    }

    
    private static final Object[][] CONTENTS = {
        {"project.name",      "JFreeChart"},
        {"project.version",   "1.0.10"},
        {"project.info",      "http://www.jfree.org/jfreechart/index.html"},
        {"project.copyright",
         "(C)opyright 2000-2008, by Object Refinery Limited and Contributors"}
    };

}
