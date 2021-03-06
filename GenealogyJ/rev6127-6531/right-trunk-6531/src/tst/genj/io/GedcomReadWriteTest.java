
package genj.io;

import genj.gedcom.Context;
import genj.gedcom.Entity;
import genj.gedcom.Gedcom;
import genj.gedcom.GedcomException;
import genj.util.Origin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import junit.framework.TestCase;


public class GedcomReadWriteTest extends TestCase {
  
  
  @SuppressWarnings("deprecation")
  public void testEncryptDecrypt() throws IOException, GedcomException {
    
    
    Logger.getLogger("").setLevel(Level.OFF);

    
    File original = new File("./gedcom/example.ged");
    Gedcom ged = GedcomReaderFactory.createReader(Origin.create(original.toURL()), null).read();
    
    
    ged.setPassword("password");
    for (Entity ent : ged.getEntities()) 
      ent.setPrivate(true, true);
    
    
    File temp = File.createTempFile("test", ".ged");
    FileOutputStream out = new FileOutputStream(temp);
    new GedcomWriter(ged, out).write();
    out.close();
    
    
    Origin o = Origin.create(temp.toURL());
    GedcomReaderFactory.createReader(o, new PasswordContext("wrong")).read();
    ged = GedcomReaderFactory.createReader(o, new PasswordContext(null)).read();
    
    
    temp = File.createTempFile("test", ".ged");
    out = new FileOutputStream(temp);
    new GedcomWriter(ged, out).write();
    out.close();
    
    
    ged = GedcomReaderFactory.createReader(o, new PasswordContext("password")).read();
    
    
    temp = File.createTempFile("test", ".ged");
    out = new FileOutputStream(temp);
    ged.setPassword(null);
    new GedcomWriter(ged, out).write();
    out.close();
    
    
    assertEquals( Collections.EMPTY_LIST, diff(original, temp) );
    
    
    
  }
  
  private class PasswordContext implements GedcomReaderContext {
    String pwd;
    public PasswordContext(String password) {
      pwd = password;
    }
    public String getPassword() {
      String result = pwd;
      pwd = null;
      return result;
    }
    public void handleWarning(int line, String warning, Context context) {
    }
  }
  
  
  public void testStressFile() throws IOException, GedcomException {
    
    
    Logger.getLogger("").setLevel(Level.OFF);

    
    Gedcom ged = GedcomReaderFactory.createReader(getClass().getResourceAsStream("stress.ged"), null).read();
    
    
    File temp = File.createTempFile("test", ".ged");
    OutputStream out = new FileOutputStream(temp);
    new GedcomWriter(ged, out).write();
    out.close();
    
    
    assertEquals( Collections.singletonList("2 _TAG<>"), diff(temp, getClass().getResourceAsStream("stress.ged")) );
  }
  
  
  @SuppressWarnings("deprecation")
  public void testReadWrite() throws IOException, GedcomException {
    
    
    Logger.getLogger("").setLevel(Level.OFF);

    
    File original = new File("./gedcom/example.ged");
    File temp = File.createTempFile("test", ".ged");
    
    
    Gedcom ged = GedcomReaderFactory.createReader(Origin.create(original.toURL()), null).read();
    
    
    FileOutputStream out = new FileOutputStream(temp);
    new GedcomWriter(ged, out).write();
    out.close();
    
    
    assertEquals(Collections.EMPTY_LIST, diff(original, temp));
    
  }
  
  private List<String> diff(File file1, File file2) throws IOException {
    return diff(file1, new FileInputStream(file2));
  }
    
  private List<String> diff(File file1, InputStream file2) throws IOException {
    
    List<String> result = new ArrayList<String>();
    
    BufferedReader left = new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
    BufferedReader right = new BufferedReader(new InputStreamReader(file2));
    
    
    String lineLeft = left.readLine();
    while (true) {
      left.mark(256);
      lineLeft = left.readLine();
      if (lineLeft==null)
        throw new Error();
      if (lineLeft.startsWith("0")) break;
    }
    left.reset();
    String lineRight = right.readLine();
    while (true) {
      right.mark(256);
      lineRight = right.readLine();
      if (lineRight==null)
        throw new Error();
      if (lineRight.startsWith("0")) break;
    }
    right.reset();
    
    
    while (true) {
      
      left.mark(256); right.mark(256);
      lineLeft = left.readLine();
      lineRight = right.readLine();

      
      if (lineLeft==null&&lineRight==null)
        break;
      
      
      if (lineLeft==null||lineRight==null) {
        result.add(lineLeft+"<>"+lineRight);
        break;
      }
        
      
      if (!matches(lineLeft, lineRight)) {
        
        
        left.mark(256);
        if (matches(left.readLine(), lineRight))
          result.add(lineLeft+"<>");
        else
          result.add(lineLeft+"<>"+lineRight);
        left.reset();
        right.reset();
      }
    }
    
    left.close();
    right.close();
    
    
    return result;
  }
  
  private static Pattern COMMASPACE = Pattern.compile(", ");
  private static String COMMA = ",";

  private boolean matches(String left, String right) {
    if (left==null||right==null)
      return false;
    return COMMASPACE.matcher(left).replaceAll(COMMA).equals(COMMASPACE.matcher(right).replaceAll(COMMA));
  }
  
} 
