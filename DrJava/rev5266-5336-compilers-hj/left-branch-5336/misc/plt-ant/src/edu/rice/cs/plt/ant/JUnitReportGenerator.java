

package edu.rice.cs.plt.ant;

import java.io.*;
import java.util.*;
import java.text.*;
import org.jdom.*;
import org.jdom.input.*;

public class JUnitReportGenerator {
  
  private static TreeMap times = new TreeMap();
  private static ArrayList revs = new ArrayList();
  
  public static void main(String[] args) throws Exception {
    
    File[] revDirs = new File( "benchmarkResults" ).listFiles( new FileFilter() {
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    });
    Arrays.sort(revDirs);
    
    for (int i=0; i<revDirs.length;i++)
      parseRevisionResults( revDirs[i].getName(), revDirs[i].getPath() );
    
    createHTMLFile("report.html");
    
  }
  
  public static void parseRevisionResults(String name,final String loc) throws Exception {
    revs.add(name);
    File[] files = new File( loc ).listFiles( new FilenameFilter() {
      public boolean accept(File dir,String filename) {
        return filename.matches("^TEST-.*\\.xml$");
      }
    });
    
    for (int i=0; i<files.length;i++) {
      parseTestCaseResult( name, files[i] );
    }
  }
  
  public static void parseTestCaseResult(String revName,File file) throws Exception {
    Document doc = new SAXBuilder().build( file );
    Element testSuiteElt = doc.getRootElement();
    String testName = testSuiteElt.getAttribute("name").getValue();
    testName = testName.substring(testName.lastIndexOf(".")+1);
    if (!times.containsKey(testName)) times.put(testName,new HashMap());
    ((HashMap)times.get(testName)).put(revName, testSuiteElt.getAttribute("time").getValue());
  }
  
  public static void createHTMLFile(String filename) throws Exception {
    DecimalFormat output = new DecimalFormat("0.000");
    PrintWriter fout = new PrintWriter( new FileWriter(filename) );
    fout.println("<font size=5><b>Results of Benchmark</b></font><br><br>");
    fout.println("<table border=0 cellpadding=10><tr><td valign=top>");
    fout.println("<table border=1 cellpadding=3 cellspacing=0><tr><td><b>Test Case</b></td>");
    Iterator itr = revs.iterator();
    while (itr.hasNext()) {
      String rev = (String)itr.next();
      fout.print("<td><b>"+rev+"</b></td>");
    }
    fout.println("</tr>");
    itr = times.keySet().iterator();
    while (itr.hasNext()) {
      String test = (String)itr.next();
      fout.println("<tr><td>"+test+"</td>");
      HashMap results = (HashMap)times.get(test);
      double last = -1;
      Iterator itr2 = revs.iterator();
      while (itr2.hasNext()) {
        String rev = (String)itr2.next();
        String resD = (String)results.get(rev);
        if (resD!=null) {
          double res = Double.parseDouble(resD);
          if (last!=-1)
            fout.print("<td align=right bgcolor="+pctToColor(res/last)+"><font face=monospace>"+output.format(res)+"</td>");
          else
            fout.print("<td align=right><font face=monospace>"+output.format(res)+"</td>");
          last = res;
        } 
        else
          fout.print("<td bgcolor=black align=center><font face=monospace color=white>No Result</font></td>");
      }
      fout.println("</tr>");
    }
    
    fout.println("</table></td><td valign=top><table border=1 cellpadding=3 cellspacing=0><tr><td align=center width=200><b>Legend</b></td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(1.3)+">>30% increase</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(1.2)+">20-30% increase</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(1.1)+">10-20% increase</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(1.05)+">5-10% increase</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(.95)+">Within 5%</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(.9)+">5-10% decrease</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(.8)+">10-20% decrease</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(.7)+">20-30% decrease</td></tr>");
    fout.println("<tr><td align=center bgcolor="+pctToColor(0)+">>30% decrease</td></tr>");
    fout.println("</table></td></tr></table>");
    fout.close();
  }
  
  public static String pctToColor(double pct) {
    if (pct>=1.3) return "#FF6F6F";  
    if (pct>=1.2) return "#FF9F7F";  
    if (pct>=1.1) return "#FFD07F";  
    if (pct>=1.05) return "#FFFF7F"; 
    if (pct>=.95) return "#FFFFFF";  
    if (pct>=.9) return "#99FF6F";   
    if (pct>=.8) return "#6FD399";   
    if (pct>=.7) return "#88AAFF";   
    return "#BB88FF";                
  }
  
}