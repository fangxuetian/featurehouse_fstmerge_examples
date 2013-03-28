

package edu.rice.cs.plt.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;


public class ExpandingByteBuffer extends ExpandingBuffer<byte[]> {

  
  private boolean _eof;
  
  public ExpandingByteBuffer() {
    super();
    _eof = false;
  }
  
  
  public synchronized void end() { _eof = true; notifyAll(); }
  
  public synchronized boolean isEnded() { return _eof; }
  
  protected byte[] allocateBuffer(int size) { return new byte[size]; }
  
  
  public DirectOutputStream outputStream() {
    return new DirectOutputStream() {
      public void close() {}
      
      public void flush() {}
      
      @Override public void write(int b) throws IOException {
        synchronized (ExpandingByteBuffer.this) {
          if (_eof) { throw new IOException("Buffer has been ended"); }
          allocate();
          lastBuffer()[lastIndex()] = (byte) b;
          recordWrite(1);
          ExpandingByteBuffer.this.notifyAll();
        }
      }
      
      @Override public void write(byte[] bbuf) throws IOException { write(bbuf, 0, bbuf.length); }
      
      @Override public void write(byte[] bbuf, int off, int bytes) throws IOException {
        synchronized (ExpandingByteBuffer.this) {
          if (_eof) { throw new IOException("Buffer has been ended"); }
          while (bytes > 0) {
            int space = allocate();
            int toWrite = (space > bytes) ? bytes : space;
            System.arraycopy(bbuf, off, lastBuffer(), lastIndex(), toWrite);
            recordWrite(toWrite);
            bytes -= toWrite;
          }
          ExpandingByteBuffer.this.notifyAll();
        }
      }
      
      @Override public int write(InputStream in, int bytes) throws IOException {
        if (bytes == 0) { return 0; }
        synchronized (ExpandingByteBuffer.this) {
          if (_eof) { throw new IOException("Buffer has been ended"); }
          int bytesRead = 0;
          int totalRead = 0;
          while (bytes > 0 && bytesRead >= 0) {
            int space = allocate();
            bytesRead = in.read(lastBuffer(), lastIndex(), space);
            if (bytesRead >= 0) {
              recordWrite(bytesRead);
              bytes -= bytesRead;
              totalRead += bytesRead;
            }
          }
          ExpandingByteBuffer.this.notifyAll();
          if (totalRead == 0) { return -1; }
          else { return totalRead; }
        }
      }
      
      @Override public int write(InputStream in, int bytes, int bufferSize) throws IOException {
        return write(in, bytes);
      }
      
      @Override public int write(InputStream in, int bytes, byte[] buffer) throws IOException {
        return write(in, bytes);
      }
      
      @Override public int writeAll(InputStream in) throws IOException {
        synchronized (ExpandingByteBuffer.this) {
          int bytesRead;
          long totalRead = 0;
          do {
            int space = allocate();
            bytesRead = in.read(lastBuffer(), lastIndex(), space);
            if (bytesRead >= 0) {
              recordWrite(bytesRead);
              totalRead += bytesRead;
            }
          } while (bytesRead >= 0);
          ExpandingByteBuffer.this.notifyAll();
          
          if (totalRead == 0) { return -1; }
          else if (totalRead > Integer.MAX_VALUE) { return Integer.MAX_VALUE; }
          else { return (int) totalRead; }
        }
      }
      
      @Override public int writeAll(InputStream in, int bufferSize) throws IOException {
        return writeAll(in);
      }
      
      @Override public int writeAll(InputStream in, byte[] buffer) throws IOException {
        return writeAll(in);
      }
      
    };
  }
  
  
  
  public DirectInputStream inputStream() {
    return new DirectInputStream() {
      @Override public void close() {}
      
      @Override public int available() {
        long result = size();
        return result < Integer.MAX_VALUE ? (int) result : Integer.MAX_VALUE;
      }

      @Override public int read() throws IOException {
        synchronized (ExpandingByteBuffer.this) {
          waitForInput();
          if (isEmpty()) { return -1; }
          else {
            byte result = firstBuffer()[firstIndex()];
            recordRead(1);
            deallocate();
            return result;
          }
        }
      }
      
      @Override public int read(byte[] bbuf) throws IOException { return read(bbuf, 0, bbuf.length); }
      
      @Override public int read(byte[] bbuf, int offset, int bytes) throws IOException {
        if (bytes <= 0) { return 0; }
        synchronized (ExpandingByteBuffer.this) {
          waitForInput();
          if (isEmpty()) { return -1; }
          else {
            int totalRead = 0;
            while (bytes > 0 && !isEmpty()) {
              int inFirstBuffer = elementsInFirstBuffer();
              int toRead = (inFirstBuffer > bytes) ? bytes : inFirstBuffer;
              System.arraycopy(firstBuffer(), firstIndex(), bbuf, offset, toRead);
              recordRead(toRead);
              bytes -= toRead;
              totalRead += toRead;
              deallocate();
            }
            return totalRead;
          }
        }
      }
      
      @Override public int read(OutputStream out, int bytes) throws IOException {
        if (bytes <= 0) { return 0; }
        synchronized (ExpandingByteBuffer.this) {
          waitForInput();
          if (isEmpty()) { return -1; }
          else {
            int totalRead = 0;
            while (bytes > 0 && !isEmpty()) {
              int inFirstBuffer = elementsInFirstBuffer();
              int toRead = (inFirstBuffer > bytes) ? bytes : inFirstBuffer;
              out.write(firstBuffer(), firstIndex(), toRead);
              recordRead(toRead);
              bytes -= toRead;
              totalRead += toRead;
              deallocate();
            }
            return totalRead;
          }
        }
      }
      
      @Override public int read(OutputStream out, int bytes, int bufferSize) throws IOException {
        return read(out, bytes);
      }
      
      @Override public int read(OutputStream out, int bytes, byte[] buffer) throws IOException {
        return read(out, bytes);
      }
      
      @Override public int readAll(OutputStream out) throws IOException {
        synchronized (ExpandingByteBuffer.this) {
          long totalRead = 0;
          do {
            waitForInput();
            while (!isEmpty()) {
              int toRead = elementsInFirstBuffer();
              out.write(firstBuffer(), firstIndex(), toRead);
              recordRead(toRead);
              totalRead += toRead;
              deallocate();
            }
          } while (!_eof);
          
          if (totalRead == 0) { return -1; }
          else if (totalRead > Integer.MAX_VALUE) { return Integer.MAX_VALUE; }
          else { return (int) totalRead; }
        }
      }
      
      @Override public int readAll(OutputStream out, int bufferSize) throws IOException {
        return readAll(out);
      }
      
      @Override public int readAll(OutputStream out, byte[] buffer) throws IOException {
        return readAll(out);
      }

      @Override public long skip(long bytes) throws IOException {
        if (bytes <= 0) { return 0; }
        synchronized (ExpandingByteBuffer.this) {
          waitForInput();
          long size = size();
          if (bytes > size) { bytes = size; }
          recordRead(bytes);
          while (deallocate()) {}
          return bytes;
        }
      }
      
      
      private void waitForInput() throws InterruptedIOException {
        while (!_eof && isEmpty()) {
          try { ExpandingByteBuffer.this.wait(); }
          catch (InterruptedException e) { throw new InterruptedIOException(); }
        }
      }

    };
  }

}
