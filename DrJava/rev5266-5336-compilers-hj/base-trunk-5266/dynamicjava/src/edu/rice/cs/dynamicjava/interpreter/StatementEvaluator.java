package edu.rice.cs.dynamicjava.interpreter;

import java.util.Iterator;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.plt.iter.ReadOnlyIterator;
import edu.rice.cs.plt.lambda.WrappedException;
import edu.rice.cs.plt.tuple.Option;

import koala.dynamicjava.tree.*;
import koala.dynamicjava.tree.visitor.*;

import edu.rice.cs.dynamicjava.Options;
import edu.rice.cs.dynamicjava.symbol.LocalVariable;
import edu.rice.cs.dynamicjava.symbol.TypeSystem;
import edu.rice.cs.dynamicjava.symbol.SymbolUtil;

import static koala.dynamicjava.interpreter.NodeProperties.*;


public class StatementEvaluator extends AbstractVisitor<StatementEvaluator.Result> {
  
  
  public static class Result {
    private Option<Object> _val;
    private RuntimeBindings _bindings;
    
    public Result(Object val, RuntimeBindings b) {
      _val = Option.some(val);
      _bindings = b;
    }
    public Result(RuntimeBindings b) {
      _val = Option.none();
      _bindings = b;
    }
    public Option<Object> value() { return _val; }
    public RuntimeBindings bindings() { return _bindings; }
  }
  

  private final RuntimeBindings _bindings;
  private final Options _opt;

  public StatementEvaluator(RuntimeBindings bindings, Options opt) {
    _bindings = bindings;
    _opt = opt;
  }
  
  
  public Result evaluateSequence(Iterable<? extends Node> nodes) {
    Result result = new Result(_bindings);
    for (Node n : nodes) {
      result = n.acceptVisitor(new StatementEvaluator(result.bindings(), _opt));
    }
    return result;
  }
  
  
  

  @Override public Result visit(PackageDeclaration node) { return new Result(_bindings); }
  @Override public Result visit(ImportDeclaration node) { return new Result(_bindings); }
  @Override public Result visit(ClassDeclaration node) { return new Result(_bindings); }
  @Override public Result visit(InterfaceDeclaration node) { return new Result(_bindings); }
  @Override public Result visit(ConstructorDeclaration node) { return new Result(_bindings); }
  @Override public Result visit(MethodDeclaration node) { return new Result(_bindings); }

  @Override public Result visit(VariableDeclaration node) {
    
    
    Object init = SymbolUtil.initialValue(getErasedType(node).value());
    RuntimeBindings newB = new RuntimeBindings(_bindings, getVariable(node), init);
    if (node.getInitializer() != null) {
      newB.set(getVariable(node), new ExpressionEvaluator(newB, _opt).value(node.getInitializer()));
    }
    return new Result(newB);
  }
  
  
  
  
  @Override public Result visit(EmptyStatement node) { return new Result(_bindings); }
  
  @Override public Result visit(ExpressionStatement node) {
    if (hasStatementTranslation(node)) {
      return getStatementTranslation(node).acceptVisitor(this);
    }
    else {
      Object val = new ExpressionEvaluator(_bindings, _opt).value(node.getExpression());
      if (node.getHasSemicolon() || getType(node.getExpression()).equals(TypeSystem.VOID)) {
        return new Result(_bindings);
      }
      else { return new Result(val, _bindings); }
    }
  }

  @Override public Result visit(WhileStatement node) {
    ExpressionEvaluator eval = new ExpressionEvaluator(_bindings, _opt);
    try {
      while ((Boolean) eval.value(node.getCondition())) {
        try { node.getBody().acceptVisitor(this); }
        catch (ContinueException e) {
          if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
        }
      }
    }
    catch (BreakException e) {
      if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
    }
    return new Result(_bindings);
  }

  @Override public Result visit(final ForEachStatement node) {
    
    
    
    LocalVariable param = getVariable(node.getParameter());
    RuntimeBindings newB = new RuntimeBindings(_bindings, param, null);
    final Object iterable = new ExpressionEvaluator(newB, _opt).value(node.getCollection());
    if (iterable == null) { throw new WrappedException(new EvaluatorException(new NullPointerException())); }
    Iterator<?> iter;
    if (iterable.getClass().isArray()) {
      final int length = Array.getLength(iterable);
      iter = new ReadOnlyIterator<Object>() {
        int i = 0;
        public boolean hasNext() { return i < length; }
        public Object next() {
          try { return Array.get(iterable, i++); }
          catch (ArrayIndexOutOfBoundsException e) { throw new WrappedException(new EvaluatorException(e)); }
        }
      };
    }
    else {
      try {
        Method getIterator = iterable.getClass().getMethod("iterator");
        try { getIterator.setAccessible(true); }
        catch (SecurityException e) {  }
        iter = (Iterator<?>) getIterator.invoke(iterable);
      }
      catch (NoSuchMethodException e) { throw new RuntimeException(e); }
      catch (IllegalAccessException e) { throw new RuntimeException(e); }
      catch (InvocationTargetException e) { throw new WrappedException(new EvaluatorException(e.getCause())); }
    }
    
    StatementEvaluator seval = new StatementEvaluator(newB, _opt);
    try {
      while (true) {
        Object elt;
        try { 
          if (!iter.hasNext()) { break; }
          elt = iter.next();
        }
        catch (Throwable t) { throw new WrappedException(new EvaluatorException(t)); }
        
        try {
          newB.set(param, elt);
          node.getBody().acceptVisitor(seval);
        }
        catch (ContinueException e) {
          if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
        }
      }
    }
    catch (BreakException e) {
      if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
    }
    return new Result(_bindings);
  }
  
  @Override public Result visit(ForStatement node) {
    RuntimeBindings newB = _bindings;
    if (node.getInitialization() != null) {
      newB = evaluateSequence(node.getInitialization()).bindings();
    }
    Expression cond = node.getCondition();
    Iterable<Node> update = node.getUpdate();
    ExpressionEvaluator eval = new ExpressionEvaluator(newB, _opt);
    StatementEvaluator seval = new StatementEvaluator(newB, _opt);
    try {
      while (cond == null || (Boolean) eval.value(cond)) {
        try { node.getBody().acceptVisitor(seval); }
        catch (ContinueException e) {
          if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
        }
        if (update != null) { seval.evaluateSequence(update); }
      }
    }
    catch (BreakException e) {
      if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
    }
    return new Result(_bindings);
  }

  @Override public Result visit(DoStatement node) {
    ExpressionEvaluator eval = new ExpressionEvaluator(_bindings, _opt);
    try {
      do {
        try { node.getBody().acceptVisitor(this); } 
        catch (ContinueException e) {
          if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
        }
      } while ((Boolean) eval.value(node.getCondition()));
    } catch (BreakException e) {
      if (e.hasLabel() && !node.hasLabel(e.label())) { throw e; }
    }
    return new Result(_bindings);
  }

  @Override public Result visit(SwitchStatement node) {
    ExpressionEvaluator eval = new ExpressionEvaluator(_bindings, _opt);
    Object sel = eval.value(node.getSelector());
    Iterator<SwitchBlock> body = node.getBindings().iterator();
    
    
    for (SwitchBlock block : node.getBindings()) {
      if (block.getExpression() != null && eval.value(block.getExpression()).equals(sel)) {
        break;
      }
      else { body.next();  }
    }
    
    
    if (!body.hasNext()) {
      body = node.getBindings().iterator();
      for (SwitchBlock block : node.getBindings()) {
        if (block.getExpression() == null) { break; }
        else { body.next();  }
      }
    }
    
    Iterable<Node> toEvaluate = IterUtil.empty();
    while (body.hasNext()) { toEvaluate = IterUtil.compose(toEvaluate, body.next().getStatements()); }
    try { evaluateSequence(toEvaluate); }
    catch (BreakException e) {
      if (e.hasLabel()) { throw e; }
    }
    return new Result(_bindings);
  }
  
  @Override public Result visit(LabeledStatement node) {
    try { node.getStatement().acceptVisitor(this); }
    catch (BreakException e) {
      if (!e.hasLabel() || !e.label().equals(node.getLabel())) { throw e; }
    }
    return new Result(_bindings);
  }

  @Override public Result visit(SynchronizedStatement node) {
    synchronized (new ExpressionEvaluator(_bindings, _opt).value(node.getLock())) {
      node.getBody().acceptVisitor(this);
    }
    return new Result(_bindings);
  }

  @Override public Result visit(BreakStatement node) {
    if (node.getLabel() == null) { throw new BreakException(); }
    else { throw new BreakException(node.getLabel()); }
  }

  @Override public Result visit(ContinueStatement node) {
    if (node.getLabel() == null) { throw new ContinueException(); }
    else { throw new ContinueException(node.getLabel()); }
  }

  @Override public Result visit(TryStatement node) {
    try { node.getTryBlock().acceptVisitor(this); }
    catch (WrappedException e) {
      if (e.getCause() instanceof EvaluatorException) {
        Throwable t = e.getCause().getCause();
        boolean handled = false;
        for (CatchStatement cs : node.getCatchStatements()) {
          if (getErasedType(cs).value().isInstance(t)) {
            handled = true;
            RuntimeBindings newB = new RuntimeBindings(_bindings, getVariable(cs.getException()), t);
            cs.getBlock().acceptVisitor(new StatementEvaluator(newB, _opt));
            break;
          }
        }
        if (!handled) { throw e; }
      }
      else { throw e; }
    }
    finally {
      if (node.getFinallyBlock() != null) { node.getFinallyBlock().acceptVisitor(this); }
    }
    return new Result(_bindings);
  }
  
  @Override public Result visit(ThrowStatement node) {
    Throwable t = (Throwable) new ExpressionEvaluator(_bindings, _opt).value(node.getExpression());
    
    if (t == null) {
        
        
        
        
        t = new NullPointerException();
        t.setStackTrace(new StackTraceElement[0]);
    }
    throw new WrappedException(new EvaluatorException(t));
  }

  @Override public Result visit(ReturnStatement node) {
    if (node.getExpression() == null) { throw new ReturnException(); }
    else {
      Object result = new ExpressionEvaluator(_bindings, _opt).value(node.getExpression());
      throw new ReturnException(result);
    }
  }

  @Override public Result visit(IfThenStatement node) {
    if ((Boolean) new ExpressionEvaluator(_bindings, _opt).value(node.getCondition())) {
      node.getThenStatement().acceptVisitor(this);
    }
    return new Result(_bindings);
  }

  @Override public Result visit(IfThenElseStatement node) {
    if ((Boolean) new ExpressionEvaluator(_bindings, _opt).value(node.getCondition())) {
      node.getThenStatement().acceptVisitor(this);
    }
    else {
      node.getElseStatement().acceptVisitor(this);
    }
    return new Result(_bindings);
  }

  @Override public Result visit(AssertStatement node) {
    
    ExpressionEvaluator eval = new ExpressionEvaluator(_bindings, _opt);
    if ((Boolean) eval.value(node.getCondition())) {
      return new Result(_bindings);
    }
    else {
      if (node.getFailString() == null) {
        throw new WrappedException(new EvaluatorException(new AssertionError()));
      }
      else {
        Object messageObj = eval.value(node.getFailString());
        String message;
        try { message = messageObj.toString(); }
        catch (Throwable t) { throw new WrappedException(new EvaluatorException(t)); }
        throw new WrappedException(new EvaluatorException(new AssertionError(message)));
      }
    }
  }
  
  @Override public Result visit(BlockStatement node) {
    return evaluateSequence(node.getStatements());
  }
  
  
  public static class ControlFlowException extends RuntimeException {}

  public static class LabelControlException extends ControlFlowException {
    private final String _label;
    public LabelControlException() { _label = null; }
    public LabelControlException(String label) { _label = label; }
    public boolean hasLabel() { return _label != null; }
    public String label() { return _label; }
  }

  public static class ContinueException extends LabelControlException {
    public ContinueException() { super(); }
    public ContinueException(String label) { super(label); }
  }

  public static class BreakException extends LabelControlException {
    public BreakException() { super(); }
    public BreakException(String label) { super(label); }
  }
  
  public static class ReturnException extends ControlFlowException {
    private final Option<Object> _value;
    public ReturnException() { _value = Option.none(); }
    public ReturnException(Object value) { _value = Option.some(value); }
    public Option<Object> value() { return _value; }
  }
  
  
}
