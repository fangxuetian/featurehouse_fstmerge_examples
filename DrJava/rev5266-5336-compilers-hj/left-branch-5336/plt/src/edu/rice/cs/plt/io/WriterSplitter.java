

package edu.rice.cs.plt.io;

import java.io.Writer;
import java.io.IOException;
import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.plt.object.Composite;
import edu.rice.cs.plt.object.ObjectUtil;


public class WriterSplitter extends DirectWriter implements Composite {
  
  private final Iterable<? extends Writer> _writers;
  
  public WriterSplitter(Writer... writers) { _writers = IterUtil.asIterable(writers); }
  
  public WriterSplitter(Iterable<? extends Writer> writers) { _writers = writers; }
  
  public int compositeHeight() { return ObjectUtil.compositeHeight(_writers) + 1; }
  public int compositeSize() { return ObjectUtil.compositeSize(_writers) + 1; }
  
  @Override public void close() throws IOException {
    for (Writer w : _writers) { w.close(); }
  }
  
  @Override public void flush() throws IOException {
    for (Writer w : _writers) { w.flush(); }
  }
  
  @Override public void write(char[] cbuf) throws IOException {
    for (Writer w : _writers) { w.write(cbuf); }
  }
  
  @Override public void write(char[] cbuf, int off, int len) throws IOException {
    for (Writer w : _writers) { w.write(cbuf, off, len); }
  }
  
  @Override public void write(int c) throws IOException {
    for (Writer w : _writers) { w.write(c); }
  }
  
  @Override public void write(String s) throws IOException {
    for (Writer w : _writers) { w.write(s); }
  }
  
  @Override public void write(String s, int off, int len) throws IOException {
    for (Writer w : _writers) { w.write(s, off, len); }
  }
  
}
