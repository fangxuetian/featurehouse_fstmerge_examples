

package edu.rice.cs.plt.io;


public class VoidOutputStream extends DirectOutputStream {
  public static final VoidOutputStream INSTANCE = new VoidOutputStream();
  protected VoidOutputStream() {} 
  @Override public void close() {}
  @Override public void flush() {}
  @Override public void write(byte[] bbuf) {}
  @Override public void write(byte[] bbuf, int offset, int len) {}
  @Override public void write(int b) {}
}
