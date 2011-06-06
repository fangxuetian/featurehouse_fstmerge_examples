

package gnu.regexp;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;



class CharIndexedReader implements CharIndexed {
    private static final int BUFFER_INCREMENT = 1024;
    private static final int UNKNOWN = Integer.MAX_VALUE; 
    
    private final BufferedReader br;
    
    private int index = -1;

    private int bufsize = BUFFER_INCREMENT;

    private int end = UNKNOWN;

    private char cached = OUT_OF_BOUNDS;

    
    
    
    private char[] lookBehind = new char[] { OUT_OF_BOUNDS, OUT_OF_BOUNDS }; 
  
    CharIndexedReader(Reader reader, int index) {
	if (reader instanceof BufferedReader) {
	    br = (BufferedReader) reader; 
	} else {
	    br = new BufferedReader(reader,BUFFER_INCREMENT);
	}
	next();
	if (index > 0) move(index);
    }
    
    private boolean next() {
	lookBehind[1] = lookBehind[0];
	lookBehind[0] = cached;

	if (end == 1) {
	    cached = OUT_OF_BOUNDS;
	    return false;
	}
	end--; 
	
	try {
	    if (index != -1) {
		br.reset();
	    }
	    int i = br.read();
	    br.mark(bufsize);
	    if (i == -1) {
		end = 1;
		cached = OUT_OF_BOUNDS;
		return false;
	    }

	    
	    cached = (char) i;
	    index = 1;
	} catch (IOException e) { 
	    e.printStackTrace();
	    cached = OUT_OF_BOUNDS;
	    return false; 
	}
	return true;
    }
    
    public char charAt(int index) {
	if (index == 0) {
	    return cached;
	} else if (index >= end) {
	    return OUT_OF_BOUNDS;
	} else if (index >= bufsize) {
	    
	    try {
		while (bufsize <= index) bufsize += BUFFER_INCREMENT;
		br.reset();
		br.mark(bufsize);
		br.skip(index-1);
	    } catch (IOException e) { }
	} else if (this.index != index) {
	    try {
		br.reset();
		br.skip(index-1);
	    } catch (IOException e) { }
	} else if (index == -1) {
	    return lookBehind[0];
	} else if (index == -2) {
	    return lookBehind[1];
	} else if (index < -2) {
	    return OUT_OF_BOUNDS;
	}

	char ch = OUT_OF_BOUNDS;
	
	try {
	    int i = br.read();
	    this.index = index+1; 
	    if (i == -1) {
		
		end = index;
		return ch;
	    }
	    ch = (char) i;
	} catch (IOException ie) { }
	
	return ch;
    }
    
    public boolean move(int index) {
	
	boolean retval = true;
	while (retval && (index-- > 0)) retval = next();
	return retval;
    }
    
    public boolean isValid() {
	return (cached != OUT_OF_BOUNDS);
    }
}
