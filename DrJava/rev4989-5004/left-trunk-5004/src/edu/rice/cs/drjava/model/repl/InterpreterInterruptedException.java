

package edu.rice.cs.drjava.model.repl;


public class InterpreterInterruptedException extends RuntimeException {
  public final int startLine;
  public final int startColumn;
  public final int endLine;
  public final int endColumn;

  public InterpreterInterruptedException(int sl, int sc, int el, int ec) {
    super("Interrupted between (" + sl + "," + sc + ") and " + 
          "(" + el + ", " + ec + ")");
    startLine = sl;
    startColumn = sc;
    endLine = el;
    endColumn = ec;
  }
}
