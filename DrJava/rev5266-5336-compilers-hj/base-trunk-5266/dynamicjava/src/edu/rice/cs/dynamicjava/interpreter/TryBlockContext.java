package edu.rice.cs.dynamicjava.interpreter;

import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.dynamicjava.symbol.type.Type;


public class TryBlockContext extends DelegatingContext {
  
  private final Iterable<Type> _caughtTypes;
  
  public TryBlockContext(TypeContext next, Iterable<Type> caughtTypes) {
    super(next);
    _caughtTypes = caughtTypes;
  }
  
  protected TryBlockContext duplicate(TypeContext next) {
    return new TryBlockContext(next, _caughtTypes);
  }
  
  
  @Override public Iterable<Type> getDeclaredThrownTypes() {
    return IterUtil.compose(_caughtTypes, super.getDeclaredThrownTypes());
  }

}
