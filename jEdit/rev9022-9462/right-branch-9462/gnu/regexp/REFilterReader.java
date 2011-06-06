

package gnu.regexp;
import java.io.FilterReader;
import java.io.Reader;



public class REFilterReader extends FilterReader {

  private RE expr;
  private String replace;
  private String buffer;
  private int bufpos;
  private int offset;
  private CharIndexedReader stream;

  
  public REFilterReader(Reader stream, RE expr, String replace) {
    super(stream);
    this.stream = new CharIndexedReader(stream,0);
    this.expr = expr;
    this.replace = replace;
  }

  
  public int read() {
    
    if ((buffer != null) && (bufpos < buffer.length())) {
      return (int) buffer.charAt(bufpos++);
    }

    
    if (!stream.isValid()) return -1;

    REMatch mymatch = new REMatch(expr.getNumSubs(),offset,0);
    if (expr.match(stream,mymatch)) {
      mymatch.end[0] = mymatch.index;
      mymatch.finish(stream);
      stream.move(mymatch.toString().length());
      offset += mymatch.toString().length();
      buffer = mymatch.substituteInto(replace);
      bufpos = 1;

      if (buffer.length() > 0) {
	  return buffer.charAt(0);
      }
    }
    char ch = stream.charAt(0);
    if (ch == CharIndexed.OUT_OF_BOUNDS) return -1;
    stream.move(1);
    offset++;
    return ch;
  }

  
  public boolean markSupported() {
    return false;
  }

  
  public int read(char[] b, int off, int len) {
    int i;
    int ok = 0;
    while (len-- > 0) {
      i = read();
      if (i == -1) return (ok == 0) ? -1 : ok;
      b[off++] = (char) i;
      ok++;
    }
    return ok;
  }

  
  public int read(char[] b) {
    return read(b,0,b.length);
  }
}
