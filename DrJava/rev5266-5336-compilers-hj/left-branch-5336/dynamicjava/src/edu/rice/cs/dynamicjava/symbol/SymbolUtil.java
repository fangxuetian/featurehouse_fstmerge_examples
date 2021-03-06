package edu.rice.cs.dynamicjava.symbol;

import java.lang.reflect.Array;
import edu.rice.cs.plt.lambda.Lambda;
import edu.rice.cs.plt.lambda.Thunk;
import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.plt.reflect.JavaVersion;
import edu.rice.cs.plt.reflect.ReflectUtil;
import edu.rice.cs.plt.reflect.ReflectException;

import edu.rice.cs.dynamicjava.symbol.type.*;

import static edu.rice.cs.plt.debug.DebugUtil.debug;

public class SymbolUtil { 
  
  
  public static Iterable<DJClass> outerClassChain(DJClass c) {
    Iterable<DJClass> result = IterUtil.singleton(c);
    DJClass outer = c.declaringClass();
    while (outer != null) { result = IterUtil.compose(outer, result); outer = outer.declaringClass(); }
    return result;
  }
  
  
  public static DJClass outermostClass(DJClass c) {
    DJClass result = c;
    DJClass outer = c.declaringClass();
    while (outer != null) { result = outer; outer = outer.declaringClass(); }
    return result;
  }
  
  
  public static boolean dynamicallyEncloses(DJClass outer, DJClass inner) {
    DJClass c = inner;
    boolean nextIsStatic = false;
    while (c != null) {
      if (c.equals(outer)) { return !nextIsStatic; }
      nextIsStatic = c.isStatic();
      c = c.declaringClass();
    }
    return false;
  }
  
  
  public static DJClass dynamicOuterClass(DJClass c) {
    DJClass inner = c;
    while (inner != null && inner.isStatic()) { inner = inner.declaringClass(); }
    return (inner == null) ? null : inner.declaringClass();
  }
  
  
  public static ClassType dynamicOuterClassType(ClassType t) {
    final DJClass outer = dynamicOuterClass(t.ofClass());
    if (outer == null) { return null; }
    else {
      return t.apply(new TypeAbstractVisitor<ClassType>() {
        public ClassType defaultCase(Type t) { throw new RuntimeException(); }
        @Override public ClassType forSimpleClassType(SimpleClassType t) {
          return new SimpleClassType(outer);
        }
        @Override public ClassType forRawClassType(RawClassType t) {
          Iterable<VariableType> outerParams = allTypeParameters(outer);
          if (IterUtil.isEmpty(outerParams)) { return new SimpleClassType(outer); }
          else { return new RawClassType(outer); }
        }
        @Override public ClassType forParameterizedClassType(ParameterizedClassType t) {
          Iterable<VariableType> outerParams = allTypeParameters(outer);
          if (IterUtil.isEmpty(outerParams)) { return new SimpleClassType(outer); }
          else {
            Iterable<Type> targs = IterUtil.truncate(t.typeArguments(), IterUtil.sizeOf(outerParams));
            return new ParameterizedClassType(outer, targs);
          }
        }
      });
    }
  }
  
  
  public static Iterable<VariableType> allTypeParameters(DJClass c) {
    Iterable<VariableType> result = IterUtil.empty();
    Iterable<VariableType> enclosing = IterUtil.empty();
    for (DJClass cl : outerClassChain(c)) {
      if (!cl.isStatic()) { result = IterUtil.compose(result, enclosing); }
      
      enclosing = cl.declaredTypeParameters();
    }
    result = IterUtil.compose(result, enclosing); 
    return result;
  }
  
  
  public static String shortName(DJClass c) {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (DJClass outer : outerClassChain(c)) {
      if (!first) { result.append('.'); }
      first = false;
      if (outer.isAnonymous()) {
        result.append("(anonymous ");
        Iterable<Type> supers = outer.declaredSupertypes();
        try {
          ClassType superClassT = (ClassType) IterUtil.first(supers);
          result.append(shortName(superClassT.ofClass()));
        }
        catch (RuntimeException e) { result.append("Object"); }
        result.append(')');
      }
      else { result.append(outer.declaredName()); }
    }
    return result.toString();
  }
  
  
  
  public static ClassType thisType(DJClass c) {
    Iterable<VariableType> vars = allTypeParameters(c);
    if (IterUtil.isEmpty(vars)) { return new SimpleClassType(c); }
    else { return new ParameterizedClassType(c, vars); }
  }
  
  
  public static DJClass wrapClass(Class<?> c) {
    if (JavaVersion.CURRENT.supports(JavaVersion.JAVA_5)) {
      try {
        return (DJClass) ReflectUtil.loadObject(DJClass.class.getClassLoader(),
                                                "edu.rice.cs.dynamicjava.symbol.Java5Class", c);
      }
      catch (ReflectException e) { throw new RuntimeException("Unable to create a Java5Class"); }
    }
    else { return new JavaClass(c); }
  }
  
  
  public static Library classLibrary(ClassLoader loader) {
    if (JavaVersion.CURRENT.supports(JavaVersion.JAVA_5)) {
      try {
        return (Library) ReflectUtil.loadObject(Library.class.getClassLoader(), Java5Library.class.getName(),
                                                new Class<?>[]{ ClassLoader.class }, loader);
      }
      catch (ReflectException e) {
        debug.log(e);
        throw new RuntimeException("Unable to create a Java5Library");
      }
    }
    else { return new JavaLibrary(loader); }
  }
  
  
  public static Type typeOfPrimitiveClass(Class<?> c) {
    if (c.equals(boolean.class)) { return TypeSystem.BOOLEAN; }
    else if (c.equals(int.class)) { return TypeSystem.INT; }
    else if (c.equals(double.class)) { return TypeSystem.DOUBLE; }
    else if (c.equals(char.class)) { return TypeSystem.CHAR; }
    else if (c.equals(void.class)) { return TypeSystem.VOID; }
    else if (c.equals(long.class)) { return TypeSystem.LONG; }
    else if (c.equals(byte.class)) { return TypeSystem.BYTE; }
    else if (c.equals(short.class)) { return TypeSystem.SHORT; }
    else if (c.equals(float.class)) { return TypeSystem.FLOAT; }
    else { throw new IllegalArgumentException("Unrecognized primitive: " + c); }
  }
  
  
  public static Type typeOfGeneralClass(Class<?> c, TypeSystem ts) {
    if (c.isPrimitive()) { return typeOfPrimitiveClass(c); }
    else if (c.isArray()) { return new SimpleArrayType(typeOfGeneralClass(c.getComponentType(), ts)); }
    else { return ts.makeClassType(wrapClass(c)); }
  }
  
  
  public static Thunk<Class<?>> arrayClassThunk(final Thunk<Class<?>> element) {
    return new Thunk<Class<?>>() {
      public Class<?> value() { return Array.newInstance(element.value(), 0).getClass(); }
    };
  }
  
  
  public static Object initialValue(Class<?> c) {
    if (c.isPrimitive()) {
      if (c.equals(boolean.class)) { return Boolean.FALSE; }
      else if (c.equals(int.class)) { return Integer.valueOf(0); }
      else if (c.equals(double.class)) { return Double.valueOf(0.0); }
      else if (c.equals(char.class)) { return Character.valueOf('\u'); }
      else if (c.equals(void.class)) { return null; }
      else if (c.equals(long.class)) { return Long.valueOf(0l); }
      else if (c.equals(byte.class)) { return Byte.valueOf((byte) 0); }
      else if (c.equals(short.class)) { return Short.valueOf((short) 0); }
      else if (c.equals(float.class)) { return Float.valueOf(0.0f); }
      else { throw new IllegalArgumentException("Unrecognized primitive: " + c); }
    }
    else { return null; }
  }
  
  public static Iterable<Type> parameterTypes(Function f) {
    return IterUtil.map(f.parameters(), TYPE_OF_VARIABLE);
  }
  
  private static final Lambda<Variable, Type> TYPE_OF_VARIABLE = new Lambda<Variable, Type>() {
    public Type value(Variable v) { return v.type(); }
  };
  
  
  public static boolean isVararg(Function f) {
    Iterable<LocalVariable> params = f.parameters();
    return !IterUtil.isEmpty(params) && (IterUtil.last(params).type() instanceof VarargArrayType); 
  }

}
