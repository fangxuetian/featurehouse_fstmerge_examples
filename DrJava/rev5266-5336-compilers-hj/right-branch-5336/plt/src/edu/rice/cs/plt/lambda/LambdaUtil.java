

package edu.rice.cs.plt.lambda;

import java.io.Serializable;
import edu.rice.cs.plt.iter.IterUtil;
import edu.rice.cs.plt.recur.RecurUtil;
import edu.rice.cs.plt.tuple.*;


public final class LambdaUtil {
  
  
  private LambdaUtil() {}
  
  
  public static interface GeneralRunnable
    extends Runnable, Runnable1<Object>, Runnable2<Object, Object>, Runnable3<Object, Object, Object>, 
            Runnable4<Object, Object, Object, Object> {}
  
  
  public static interface GeneralLambda<R>
    extends Thunk<R>, Lambda<Object, R>, Lambda2<Object, Object, R>, Lambda3<Object, Object, Object, R>, 
            Lambda4<Object, Object, Object, Object, R> {}
  
  
  public static interface GeneralPredicate
    extends Condition, Predicate<Object>, Predicate2<Object, Object>, Predicate3<Object, Object, Object>, 
            Predicate4<Object, Object, Object, Object> {}
  
  
  public static final GeneralRunnable NO_OP = new NoOp();
  
  private static final class NoOp implements GeneralRunnable, Serializable {
    private NoOp() {}
    public void run() {}
    public void run(Object o) {}
    public void run(Object o1, Object o2) {}
    public void run(Object o1, Object o2, Object o3) {}
    public void run(Object o1, Object o2, Object o3, Object o4) {}
  }
  
  
  public static final GeneralPredicate TRUE = new True();
  
  private static final class True implements GeneralPredicate, Serializable {
    private True() {}
    public boolean isTrue() { return true; }
    public boolean contains(Object o) { return true; }
    public boolean contains(Object o1, Object o2) { return true; }
    public boolean contains(Object o1, Object o2, Object o3) { return true; }
    public boolean contains(Object o1, Object o2, Object o3, Object o4) { return true; }
  }
  
  
  public static final GeneralPredicate FALSE = new False();
  
  private static final class False implements GeneralPredicate, Serializable {
    private False() {}
    public boolean isTrue() { return true; }
    public boolean contains(Object o) { return false; }
    public boolean contains(Object o1, Object o2) { return false; }
    public boolean contains(Object o1, Object o2, Object o3) { return false; }
    public boolean contains(Object o1, Object o2, Object o3, Object o4) { return false; }
  }
  
  
  public static final Predicate<Object> IS_NULL = new IsNullPredicate();
  
  private static final class IsNullPredicate implements Predicate<Object>, Serializable {
    private IsNullPredicate() {}
    public boolean contains(Object arg) { return arg == null; }
  }
  
  
  public static final Predicate<Object> NOT_NULL = new NotNullPredicate();

  
  private static final class NotNullPredicate implements Predicate<Object>, Serializable {
    private NotNullPredicate() {}
    public boolean contains(Object arg) { return arg != null; }
  }
  
  
  public static final Predicate2<Object, Object> EQUAL = new EqualPredicate();
    
  private static final class EqualPredicate implements Predicate2<Object, Object>, Serializable {
    private EqualPredicate() {}
    public boolean contains(Object arg1, Object arg2) { return RecurUtil.safeEquals(arg1, arg2); }
  }
  
  
  public static final Predicate2<Object, Object> NOT_EQUAL = negate(EQUAL);
  
  
  public static final Predicate2<Object, Object> IDENTICAL = new IdenticalPredicate();
  
  private static final class IdenticalPredicate implements Predicate2<Object, Object>, Serializable {
    private IdenticalPredicate() {} 
    public boolean contains(Object arg1, Object arg2) { return arg1 == arg2; }
  }
  
  
  public static final Predicate2<Object, Object> NOT_IDENTICAL = new NotIdenticalPredicate();

  
  private static final class NotIdenticalPredicate implements Predicate2<Object, Object>, Serializable {
    private NotIdenticalPredicate() {} 
    public boolean contains(Object arg1, Object arg2) { return arg1 != arg2; }
  }

  
  
  public static final Predicate2<Object, Class<?>> INSTANCE_OF = new InstanceOfPredicate();

  private static final class InstanceOfPredicate implements Predicate2<Object, Class<?>>, Serializable {
    private InstanceOfPredicate() {} 
    public boolean contains(Object val, Class<?> c) { return c.isInstance(val); }
  }

  
  
  public static final Lambda<Object, String> TO_STRING = new ToStringLambda();
  
  private static final class ToStringLambda implements Lambda<Object, String>, Serializable {
    private ToStringLambda() {}
    public String value(Object obj) { return RecurUtil.safeToString(obj); }
  }
  
  
  public static final Lambda2<Object, Object, String> STRING_CONCAT = new StringConcatLambda();
  
  private static final class StringConcatLambda implements Lambda2<Object, Object, String>, Serializable {
    private StringConcatLambda() {}
    public String value(Object o1, Object o2) { return RecurUtil.safeToString(o1) + RecurUtil.safeToString(o2); }
  }
  
  
  public static final Lambda<Object, Integer> HASH_CODE = new HashCodeLambda();
  
  private static final class HashCodeLambda implements Lambda<Object, Integer>, Serializable {
    private HashCodeLambda() {}
    public Integer value(Object obj) { return RecurUtil.safeHashCode(obj); }
  }
  
  
  
  public static final Lambda<Integer, Integer> INCREMENT_INT = new IncrementIntLambda();
  
  private static final class IncrementIntLambda implements Lambda<Integer, Integer>, Serializable {
    private IncrementIntLambda() {}
    public Integer value(Integer i) { return i+1; }
  }
  
  
  public static final Lambda<Integer, Integer> DECREMENT_INT = new DecrementIntLambda();
  
  private static final class DecrementIntLambda implements Lambda<Integer, Integer>, Serializable {
    private DecrementIntLambda() {}
    public Integer value(Integer i) { return i-1; }
  }
  
  
  
  public static final Lambda2<Integer, Integer, Integer> ADD_INT = new AddIntLambda();
  
  private static final class AddIntLambda implements Lambda2<Integer, Integer, Integer>, Serializable {
    private AddIntLambda() {}
    public Integer value(Integer x, Integer y) { return x+y; }
  }
  
  
  public static final Lambda2<Integer, Integer, Integer> SUBTRACT_INT = new SubtractIntLambda();
  
  private static final class SubtractIntLambda implements Lambda2<Integer, Integer, Integer>, Serializable {
    private SubtractIntLambda() {}
    public Integer value(Integer x, Integer y) { return x-y; }
  }
  
  
  public static final Lambda2<Integer, Integer, Integer> MULTIPLY_INT = new MultiplyIntLambda();
  
  private static final class MultiplyIntLambda implements Lambda2<Integer, Integer, Integer>, Serializable {
    private MultiplyIntLambda() {}
    public Integer value(Integer x, Integer y) { return x*y; }
  }
  
  
  public static final Lambda2<Integer, Integer, Integer> DIVIDE_INT = new DivideIntLambda();
  
  private static final class DivideIntLambda implements Lambda2<Integer, Integer, Integer>, Serializable {
    private DivideIntLambda() {}
    public Integer value(Integer x, Integer y) { return x/y; }
  }
  
  
  
  @SuppressWarnings("unchecked") public static <T> Lambda<T, T> identity() {
    return (Lambda<T, T>) IdentityLambda.INSTANCE;
  }
  
  private static final class IdentityLambda implements Lambda<Object, Object>, Serializable {
    private static final IdentityLambda INSTANCE = new IdentityLambda();
    private IdentityLambda() {}
    public Object value(Object arg) { return arg; }
  }
  
  
  
  @SuppressWarnings("unchecked") public static <T> GeneralLambda<T> nullLambda() {
    return (GeneralLambda<T>) NullLambda.INSTANCE;
  }

  private static final class NullLambda implements GeneralLambda<Void>, Serializable {
    private static final NullLambda INSTANCE = new NullLambda();
    private NullLambda() {}
    public Void value() { return null; }
    public Void value(Object arg) { return null; }
    public Void value(Object arg1, Object arg2) { return null; }
    public Void value(Object arg1, Object arg2, Object arg3) { return null; }
    public Void value(Object arg1, Object arg2, Object arg3, Object arg4) { return null; }
  }
  
  
  
  public static <T> GeneralLambda<T> exceptionLambda(RuntimeException e) { return new ExceptionLambda<T>(e); }

  private static final class ExceptionLambda<R> implements GeneralLambda<R>, Serializable {
    private final RuntimeException _e;
    public ExceptionLambda(RuntimeException e) { _e = e; }
    public R value() { _e.fillInStackTrace(); throw _e; }
    public R value(Object arg) { _e.fillInStackTrace(); throw _e; }
    public R value(Object arg1, Object arg2) { _e.fillInStackTrace(); throw _e; }
    public R value(Object arg1, Object arg2, Object arg3) { _e.fillInStackTrace(); throw _e; }
    public R value(Object arg1, Object arg2, Object arg3, Object arg4) { _e.fillInStackTrace(); throw _e; }
  }
  
  
  
  public static <T> GeneralLambda<T> valueLambda(T val) { return new ValueLambda<T>(val); }
  
  private static final class ValueLambda<T> implements GeneralLambda<T>, Serializable {
    private final T _val;
    public ValueLambda(T val) { _val = val; }
    public T value() { return _val; }
    public T value(Object arg) { return _val; }
    public T value(Object arg1, Object arg2) { return _val; }
    public T value(Object arg1, Object arg2, Object arg3) { return _val; }
    public T value(Object arg1, Object arg2, Object arg3, Object arg4) { return _val; }
  }
  
  
  
  @SuppressWarnings("unchecked")
  public static <R> Lambda<Thunk<? extends R>, R> thunkValueLambda() {
    return (Lambda<Thunk<? extends R>, R>) (Lambda<?, ?>) ThunkValueLambda.INSTANCE;
  }
  
  private static final class ThunkValueLambda<R> implements Lambda<Thunk<? extends R>, R>, Serializable {
    public static final ThunkValueLambda<Void> INSTANCE = new ThunkValueLambda<Void>();
    private ThunkValueLambda() {}
    public R value(Thunk<? extends R> t) { return t.value(); }
  }
    
  
  @SuppressWarnings("unchecked")
  public static <T, R> Lambda2<Lambda<? super T, ? extends R>, T, R> applicationLambda() {
    return (Lambda2<Lambda<? super T, ? extends R>, T, R>) (Lambda2<?, ?, ?>) ApplicationLambda.INSTANCE;
  }
  
  private static final class ApplicationLambda<T, R>
    implements Lambda2<Lambda<? super T, ? extends R>, T, R>, Serializable {
    private static final ApplicationLambda<Object, Void> INSTANCE = new ApplicationLambda<Object, Void>();
    private ApplicationLambda() {}
    public R value(Lambda<? super T, ? extends R> lambda, T arg) { return lambda.value(arg); }
  }

  
  @SuppressWarnings("unchecked")
  public static <T1, T2, R> Lambda3<Lambda2<? super T1, ? super T2, ? extends R>, T1, T2, R> binaryApplicationLambda() {
    return (Lambda3<Lambda2<? super T1, ? super T2, ? extends R>, T1, T2, R>) (Lambda3<?, ?, ?, ?>)
           BinaryApplicationLambda.INSTANCE;
  }
  
  private static final class BinaryApplicationLambda<T1, T2, R>
    implements Lambda3<Lambda2<? super T1, ? super T2, ? extends R>, T1, T2, R>, Serializable {
    private static final BinaryApplicationLambda<Object, Object, Void> INSTANCE = 
      new BinaryApplicationLambda<Object, Object, Void>();
    private BinaryApplicationLambda() {}
    public R value(Lambda2<? super T1, ? super T2, ? extends R> lambda, T1 a1, T2 a2) { return lambda.value(a1, a2); }
  }

  
  @SuppressWarnings("unchecked")
  public static <T1, T2, T3, R> 
    Lambda4<Lambda3<? super T1, ? super T2, ? super T3, ? extends R>, T1, T2, T3, R> ternaryApplicationLambda() {
    return (Lambda4<Lambda3<? super T1, ? super T2, ? super T3, ? extends R>, T1, T2, T3, R>) (Lambda4<?, ?, ?, ?, ?>)
      TernaryApplicationLambda.INSTANCE;
  }
  
  private static final class TernaryApplicationLambda<T1, T2, T3, R>
    implements Lambda4<Lambda3<? super T1, ? super T2, ? super T3, ? extends R>, T1, T2, T3, R>, Serializable {
    private static final TernaryApplicationLambda<Object, Object, Object, Void> INSTANCE = 
      new TernaryApplicationLambda<Object, Object, Object, Void>();
    private TernaryApplicationLambda() {}
    public R value(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T1 a1, T2 a2, T3 a3) {
      return lambda.value(a1, a2, a3);
    }
  }


  
  public static GeneralRunnable promote(Runnable r) { return new PromotedGeneralRunnable(r); }
  
  private static final class PromotedGeneralRunnable implements GeneralRunnable, Serializable {
    private final Runnable _r;
    public PromotedGeneralRunnable(Runnable r) { _r = r; }
    public void run() { _r.run(); }
    public void run(Object arg) { _r.run(); }
    public void run(Object arg1, Object arg2) { _r.run(); }
    public void run(Object arg1, Object arg2, Object arg3) { _r.run(); }
    public void run(Object arg1, Object arg2, Object arg3, Object arg4) { _r.run(); }
  }
  
  
  public static <T> Runnable2<T, Object> promote(Runnable1<? super T> r) { return new PromotedRunnable2<T>(r); }
  
  private static final class PromotedRunnable2<T> implements Runnable2<T, Object>, Serializable {
    private final Runnable1<? super T> _r;
    public PromotedRunnable2(Runnable1<? super T> r) { _r = r; }
    public void run(T arg1, Object arg2) { _r.run(arg1); }
  }
  
  
  
  public static <T1, T2> Runnable3<T1, T2, Object> promote(Runnable2<? super T1, ? super T2> r) {
    return new PromotedRunnable3<T1, T2>(r);
  }
  
  private static final class PromotedRunnable3<T1, T2> implements Runnable3<T1, T2, Object>, Serializable {
    private final Runnable2<? super T1, ? super T2> _r;
    public PromotedRunnable3(Runnable2<? super T1, ? super T2> r) { _r = r; }
    public void run(T1 arg1, T2 arg2, Object arg3) { _r.run(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3> Runnable4<T1, T2, T3, Object> promote(Runnable3<? super T1, ? super T2, ? super T3> r) {
    return new PromotedRunnable4<T1, T2, T3>(r);
  }
  
  private static final class PromotedRunnable4<T1, T2, T3> implements Runnable4<T1, T2, T3, Object>, Serializable {
    private final Runnable3<? super T1, ? super T2, ? super T3> _r;
    public PromotedRunnable4(Runnable3<? super T1, ? super T2, ? super T3> r) { _r = r; }
    public void run(T1 arg1, T2 arg2, T3 arg3, Object arg4) { _r.run(arg1, arg2, arg3); }
  }
  
  
  
  public static <R> GeneralLambda<R> promote(Thunk<? extends R> thunk) {
    return new PromotedGeneralLambda<R>(thunk);
  }

  private static final class PromotedGeneralLambda<R> implements GeneralLambda<R>, Serializable {
    private final Thunk<? extends R> _l;
    public PromotedGeneralLambda(Thunk<? extends R> l) { _l = l; }
    public R value() { return _l.value(); }
    public R value(Object arg) { return _l.value(); }
    public R value(Object arg1, Object arg2) { return _l.value(); }
    public R value(Object arg, Object arg2, Object arg3) { return _l.value(); }
    public R value(Object arg, Object arg2, Object arg3, Object arg4) { return _l.value(); }
  }
  
  
  public static <T, R> Lambda2<T, Object, R> promote(Lambda<? super T, ? extends R> lambda) {
    return new PromotedLambda2<T, R>(lambda);
  }
  
  private static final class PromotedLambda2<T, R> implements Lambda2<T, Object, R>, Serializable {
    private final Lambda<? super T, ? extends R> _l;
    public PromotedLambda2(Lambda<? super T, ? extends R> l) { _l = l; }
    public R value(T arg1, Object arg2) { return _l.value(arg1); }
  }
  
  
  public static <T1, T2, R> Lambda3<T1, T2, Object, R> promote(Lambda2<? super T1, ? super T2, ? extends R> lambda) {
    return new PromotedLambda3<T1, T2, R>(lambda);
  }
  
  private static final class PromotedLambda3<T1, T2, R> implements Lambda3<T1, T2, Object, R>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _l;
    public PromotedLambda3(Lambda2<? super T1, ? super T2, ? extends R> l) { _l = l; }
    public R value(T1 arg1, T2 arg2, Object arg3) { return _l.value(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3, R> 
    Lambda4<T1, T2, T3, Object, R> promote(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) {
    return new PromotedLambda4<T1, T2, T3, R>(lambda);
  }
  
  private static final class PromotedLambda4<T1, T2, T3, R> implements Lambda4<T1, T2, T3, Object, R>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _l;
    public PromotedLambda4(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> l) { _l = l; }
    public R value(T1 arg1, T2 arg2, T3 arg3, Object arg4) { return _l.value(arg1, arg2, arg3); }
  }
  
  
  
  public static GeneralPredicate promote(Condition cond) {
    return new PromotedGeneralPredicate(cond);
  }

  private static final class PromotedGeneralPredicate implements GeneralPredicate, Serializable {
    private final Condition _c;
    public PromotedGeneralPredicate(Condition c) { _c = c; }
    public boolean isTrue() { return _c.isTrue(); }
    public boolean contains(Object arg) { return _c.isTrue(); }
    public boolean contains(Object arg1, Object arg2) { return _c.isTrue(); }
    public boolean contains(Object arg1, Object arg2, Object arg3) { return _c.isTrue(); }
    public boolean contains(Object arg1, Object arg2, Object arg3, Object arg4) { return _c.isTrue(); }
  }
  
  
  public static <T> Predicate2<T, Object> promote(Predicate<? super T> pred) {
    return new PromotedPredicate2<T>(pred);
  }
  
  private static final class PromotedPredicate2<T> implements Predicate2<T, Object>, Serializable {
    private final Predicate<? super T> _p;
    public PromotedPredicate2(Predicate<? super T> p) { _p = p; }
    public boolean contains(T arg1, Object arg2) { return _p.contains(arg1); }
  }
  
  
  public static <T1, T2> Predicate3<T1, T2, Object> promote(Predicate2<? super T1, ? super T2> pred) {
    return new PromotedPredicate3<T1, T2>(pred);
  }
  
  private static final class PromotedPredicate3<T1, T2> implements Predicate3<T1, T2, Object>, Serializable {
    private final Predicate2<? super T1, ? super T2> _p;
    public PromotedPredicate3(Predicate2<? super T1, ? super T2> p) { _p = p; }
    public boolean contains(T1 arg1, T2 arg2, Object arg3) { return _p.contains(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3> 
    Predicate4<T1, T2, T3, Object> promote(Predicate3<? super T1, ? super T2, ? super T3> pred) {
    return new PromotedPredicate4<T1, T2, T3>(pred);
  }
  
  private static final class PromotedPredicate4<T1, T2, T3> implements Predicate4<T1, T2, T3, Object>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _p;
    public PromotedPredicate4(Predicate3<? super T1, ? super T2, ? super T3> p) { _p = p; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3, Object arg4) { return _p.contains(arg1, arg2, arg3); }
  }
  
  
  
  public static <T, U> Thunk<U> compose(Thunk<? extends T> thunk, Lambda<? super T, ? extends U> lambda) {
    return new ComposedThunk<T, U>(thunk, lambda);
  }
  
  private static final class ComposedThunk<T, U> implements Thunk<U>, Serializable {
    private final Thunk<? extends T> _l1;
    private final Lambda<? super T, ? extends U> _l2;
    public ComposedThunk(Thunk<? extends T> l1, Lambda<? super T, ? extends U> l2) { _l1 = l1; _l2 = l2; }
    public U value() { return _l2.value(_l1.value()); }
  }
  
  
  public static <S, T, U> Lambda<S, U> compose(Lambda<? super S, ? extends T> l1, 
                                               Lambda<? super T, ? extends U> l2) {
    return new ComposedLambda<S, T, U>(l1, l2);
  }
  
  private static final class ComposedLambda<S, T, U> implements Lambda<S, U>, Serializable {
    private final Lambda<? super S, ? extends T> _l1;
    private final Lambda<? super T, ? extends U> _l2;
    public ComposedLambda(Lambda<? super S, ? extends T> l1, Lambda<? super T, ? extends U> l2) {
      _l1 = l1;
      _l2 = l2;
    }
    public U value(S arg) { return _l2.value(_l1.value(arg)); }
  }
  
  
  public static <S1, S2, T, U> Lambda2<S1, S2, U> compose(Lambda2<? super S1, ? super S2, ? extends T> l1, 
                                                          Lambda<? super T, ? extends U> l2) {
    return new ComposedLambda2<S1, S2, T, U>(l1, l2);
  }
  
  private static final class ComposedLambda2<S1, S2, T, U> implements Lambda2<S1, S2, U>, Serializable {
    private final Lambda2<? super S1, ? super S2, ? extends T> _l1;
    private final Lambda<? super T, ? extends U> _l2;
    public ComposedLambda2(Lambda2<? super S1, ? super S2, ? extends T> l1, Lambda<? super T, ? extends U> l2) {
      _l1 = l1;
      _l2 = l2;
    }
    public U value(S1 arg1, S2 arg2) { return _l2.value(_l1.value(arg1, arg2)); }
  }
  
  
  public static <S1, S2, S3, T, U> 
    Lambda3<S1, S2, S3, U> compose(Lambda3<? super S1, ? super S2, ? super S3, ? extends T> l1, 
                                   Lambda<? super T, ? extends U> l2) {
    return new ComposedLambda3<S1, S2, S3, T, U>(l1, l2);
  }
  
  private static final class ComposedLambda3<S1, S2, S3, T, U> implements Lambda3<S1, S2, S3, U>, Serializable {
    private final Lambda3<? super S1, ? super S2, ? super S3, ? extends T> _l1;
    private final Lambda<? super T, ? extends U> _l2;
    public ComposedLambda3(Lambda3<? super S1, ? super S2, ? super S3, ? extends T> l1, 
                           Lambda<? super T, ? extends U> l2) {
      _l1 = l1;
      _l2 = l2;
    }
    public U value(S1 arg1, S2 arg2, S3 arg3) { return _l2.value(_l1.value(arg1, arg2, arg3)); }
  }
  
  
  public static <S1, S2, S3, S4, T, U> Lambda4<S1, S2, S3, S4, U> 
    compose(final Lambda4<? super S1, ? super S2, ? super S3, ? super S4, ? extends T> l1, 
            final Lambda<? super T, ? extends U> l2) {
    return new ComposedLambda4<S1, S2, S3, S4, T, U>(l1, l2);
  }
  
  private static final class ComposedLambda4<S1, S2, S3, S4, T, U> 
    implements Lambda4<S1, S2, S3, S4, U>, Serializable {
    private final Lambda4<? super S1, ? super S2, ? super S3, ? super S4, ? extends T> _l1;
    private final Lambda<? super T, ? extends U> _l2;
    public ComposedLambda4(Lambda4<? super S1, ? super S2, ? super S3, ? super S4, ? extends T> l1, 
                           Lambda<? super T, ? extends U> l2) {
      _l1 = l1;
      _l2 = l2;
    }
    public U value(S1 arg1, S2 arg2, S3 arg3, S4 arg4) { return _l2.value(_l1.value(arg1, arg2, arg3, arg4)); }
  }
  
  
  
  public static Runnable compose(Runnable... runnables) {
    return new ComposedRunnable(IterUtil.asIterable(runnables));
  }
  
  
  public static Runnable compose(Iterable<? extends Runnable> runnables) {
    return new ComposedRunnable(runnables);
  }
  
  private static final class ComposedRunnable implements Runnable, Serializable {
    private final Iterable<? extends Runnable> _runnables;
    public ComposedRunnable(Iterable<? extends Runnable> runnables) { _runnables = runnables; }
    public void run() {
      for (Runnable r : _runnables) { r.run(); }
    }
  }
  
  
  public static <T> Runnable1<T> compose(Runnable1<? super T> r1, Runnable1<? super T> r2) {
    
    return new ComposedRunnable1<T>(IterUtil.<Runnable1<? super T>>make(r1, r2));
  }
  
  
  public static <T> Runnable1<T> compose(Runnable1<? super T> r1, Runnable1<? super T> r2, Runnable1<? super T> r3) {
    return new ComposedRunnable1<T>(IterUtil.<Runnable1<? super T>>make(r1, r2, r3));
  }
  
  
  public static <T> Runnable1<T> compose1(Iterable<? extends Runnable1<? super T>> runnables) {
    return new ComposedRunnable1<T>(runnables);
  }
  
  private static final class ComposedRunnable1<T> implements Runnable1<T>, Serializable {
    private final Iterable<? extends Runnable1<? super T>> _runnables;
    public ComposedRunnable1(Iterable<? extends Runnable1<? super T>> runnables) { _runnables = runnables; }
    public void run(T arg) {
      for (Runnable1<? super T> r : _runnables) { r.run(arg); }
    }
  }
  
  
  public static <T1, T2> Runnable2<T1, T2> compose(Runnable2<? super T1, ? super T2> r1, 
                                                   Runnable2<? super T1, ? super T2> r2) { 
    return new ComposedRunnable2<T1, T2>(IterUtil.<Runnable2<? super T1, ? super T2>>make(r1, r2));
  }
  
  
  public static <T1, T2> Runnable2<T1, T2> compose(Runnable2<? super T1, ? super T2> r1, 
                                                   Runnable2<? super T1, ? super T2> r2, 
                                                   Runnable2<? super T1, ? super T2> r3) { 
    return new ComposedRunnable2<T1, T2>(IterUtil.<Runnable2<? super T1, ? super T2>>make(r1, r2, r3));
  }
  
  
  public static <T1, T2> Runnable2<T1, T2> compose2(Iterable<? extends Runnable2<? super T1, ? super T2>> runnables) {
    return new ComposedRunnable2<T1, T2>(runnables);
  }
  
  private static final class ComposedRunnable2<T1, T2> implements Runnable2<T1, T2>, Serializable {
    private final Iterable<? extends Runnable2<? super T1, ? super T2>> _runnables;
    public ComposedRunnable2(Iterable<? extends Runnable2<? super T1, ? super T2>> runnables) {
      _runnables = runnables;
    }
    public void run(T1 arg1, T2 arg2) {
      for (Runnable2<? super T1, ? super T2> r : _runnables) { r.run(arg1, arg2); }
    }
  }
  
  
  public static <T1, T2, T3> Runnable3<T1, T2, T3> compose(Runnable3<? super T1, ? super T2, ? super T3> r1, 
                                                           Runnable3<? super T1, ? super T2, ? super T3> r2) {
    return new ComposedRunnable3<T1, T2, T3>(IterUtil.<Runnable3<? super T1, ? super T2, ? super T3>>
                                             make(r1, r2));
  }
  
  
  public static <T1, T2, T3> Runnable3<T1, T2, T3> compose(Runnable3<? super T1, ? super T2, ? super T3> r1, 
                                                           Runnable3<? super T1, ? super T2, ? super T3> r2,
                                                           Runnable3<? super T1, ? super T2, ? super T3> r3) {
    return new ComposedRunnable3<T1, T2, T3>(IterUtil.<Runnable3<? super T1, ? super T2, ? super T3>>
                                             make(r1, r2, r3));
  }
  
  
  public static <T1, T2, T3> 
    Runnable3<T1, T2, T3> compose3(Iterable<? extends Runnable3<? super T1, ? super T2, ? super T3>> runnables) {
    return new ComposedRunnable3<T1, T2, T3>(runnables);
  }
  
  private static final class ComposedRunnable3<T1, T2, T3> implements Runnable3<T1, T2, T3>, Serializable {
    private final Iterable<? extends Runnable3<? super T1, ? super T2, ? super T3>> _runnables;
    public ComposedRunnable3(Iterable<? extends Runnable3<? super T1, ? super T2, ? super T3>> runnables) {
      _runnables = runnables;
    }
    public void run(T1 arg1, T2 arg2, T3 arg3) {
      for (Runnable3<? super T1, ? super T2, ? super T3> r : _runnables) { r.run(arg1, arg2, arg3); }
    }
  }
    
  
  public static <T1, T2, T3, T4> 
    Runnable4<T1, T2, T3, T4> compose(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r1, 
                                      Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r2) {
    return new ComposedRunnable4<T1, T2, T3, T4>(IterUtil.<Runnable4<? super T1, ? super T2, ? super T3, ? super T4>>
                                                 make(r1, r2));
  }
  
  
  public static <T1, T2, T3, T4> 
    Runnable4<T1, T2, T3, T4> compose(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r1, 
                                      Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r2,
                                      Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r3) {
    return new ComposedRunnable4<T1, T2, T3, T4>(IterUtil.<Runnable4<? super T1, ? super T2, ? super T3, ? super T4>>
                                                 make(r1, r2, r3));
  }
  
  
  public static <T1, T2, T3, T4> Runnable4<T1, T2, T3, T4> 
    compose4(Iterable<? extends Runnable4<? super T1, ? super T2, ? super T3, ? super T4>> runnables) {
    return new ComposedRunnable4<T1, T2, T3, T4>(runnables);
  }
  
  private static final class ComposedRunnable4<T1, T2, T3, T4> implements Runnable4<T1, T2, T3, T4>, Serializable {
    private final Iterable<? extends Runnable4<? super T1, ? super T2, ? super T3, ? super T4>> _runnables;
    public 
      ComposedRunnable4(Iterable<? extends Runnable4<? super T1, ? super T2, ? super T3, ? super T4>> runnables) {
      _runnables = runnables;
    }
    public void run(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      for (Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r : _runnables) {
        r.run(arg1, arg2, arg3, arg4);
      }
    }
  }
  
  
  public static <T, R> Thunk<R> bindFirst(Lambda<? super T, ? extends R> lambda, T arg) {
    return new BindFirstThunk<T, R>(lambda, arg);
  }
  
  private static final class BindFirstThunk<T, R> implements Thunk<R>, Serializable {
    private final Lambda<? super T, ? extends R> _lambda;
    private final T _arg;
    public BindFirstThunk(Lambda<? super T, ? extends R> lambda, T arg) { _lambda = lambda; _arg = arg; }
    public R value() { return _lambda.value(_arg); }
  }
  
  
  public static <T1, T2, R> Lambda<T2, R> bindFirst(Lambda2<? super T1, ? super T2, ? extends R> lambda, T1 arg1) {
    return new BindFirstLambda<T1, T2, R>(lambda, arg1);
  }
  
  private static final class BindFirstLambda<T1, T2, R> implements Lambda<T2, R>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    private final T1 _arg1;
    public BindFirstLambda(Lambda2<? super T1, ? super T2, ? extends R> lambda, T1 arg1) {
      _lambda = lambda; _arg1 = arg1;
    }
    public R value(T2 arg2) { return _lambda.value(_arg1, arg2); }
  }
  
  
  public static <T1, T2, R> Lambda<T1, R> bindSecond(Lambda2<? super T1, ? super T2, ? extends R> lambda, T2 arg2) {
    return new BindSecondLambda<T1, T2, R>(lambda, arg2);
  }
  
  private static final class BindSecondLambda<T1, T2, R> implements Lambda<T1, R>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    private final T2 _arg2;
    public BindSecondLambda(Lambda2<? super T1, ? super T2, ? extends R> lambda, T2 arg2) {
      _lambda = lambda; _arg2 = arg2;
    }
    public R value(T1 arg1) { return _lambda.value(arg1, _arg2); }
  }
  
  
  public static <T1, T2, T3, R>
    Lambda2<T2, T3, R> bindFirst(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T1 arg1) {
    return new BindFirstLambda2<T1, T2, T3, R>(lambda, arg1);
  }
  
  private static final class BindFirstLambda2<T1, T2, T3, R> implements Lambda2<T2, T3, R>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    private final T1 _arg1;
    public BindFirstLambda2(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T1 arg1) {
      _lambda = lambda; _arg1 = arg1;
    }
    public R value(T2 arg2, T3 arg3) { return _lambda.value(_arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3, R>
    Lambda2<T1, T3, R> bindSecond(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T2 arg2) {
    return new BindSecondLambda2<T1, T2, T3, R>(lambda, arg2);
  }
  
  private static final class BindSecondLambda2<T1, T2, T3, R> implements Lambda2<T1, T3, R>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    private final T2 _arg2;
    public BindSecondLambda2(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T2 arg2) {
      _lambda = lambda; _arg2 = arg2;
    }
    public R value(T1 arg1, T3 arg3) { return _lambda.value(arg1, _arg2, arg3); }
  }
  
  
  public static <T1, T2, T3, R>
    Lambda2<T1, T2, R> bindThird(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T3 arg3) {
    return new BindThirdLambda2<T1, T2, T3, R>(lambda, arg3);
  }
  
  private static final class BindThirdLambda2<T1, T2, T3, R> implements Lambda2<T1, T2, R>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    private final T3 _arg3;
    public BindThirdLambda2(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, T3 arg3) {
      _lambda = lambda; _arg3 = arg3;
    }
    public R value(T1 arg1, T2 arg2) { return _lambda.value(arg1, arg2, _arg3); }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda3<T2, T3, T4, R> 
    bindFirst(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T1 arg1) {
    return new BindFirstLambda3<T1, T2, T3, T4, R>(lambda, arg1);
  }
  
  private static final class BindFirstLambda3<T1, T2, T3, T4, R> implements Lambda3<T2, T3, T4, R>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    private final T1 _arg1;
    public BindFirstLambda3(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T1 arg1) {
      _lambda = lambda; _arg1 = arg1;
    }
    public R value(T2 arg2, T3 arg3, T4 arg4) { return _lambda.value(_arg1, arg2, arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda3<T1, T3, T4, R> 
    bindSecond(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T2 arg2) {
    return new BindSecondLambda3<T1, T2, T3, T4, R>(lambda, arg2);
  }
  
  private static final class BindSecondLambda3<T1, T2, T3, T4, R> implements Lambda3<T1, T3, T4, R>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    private final T2 _arg2;
    public BindSecondLambda3(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T2 arg2) {
      _lambda = lambda; _arg2 = arg2;
    }
    public R value(T1 arg1, T3 arg3, T4 arg4) { return _lambda.value(arg1, _arg2, arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda3<T1, T2, T4, R> 
    bindThird(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T3 arg3) {
    return new BindThirdLambda3<T1, T2, T3, T4, R>(lambda, arg3);
  }
  
  private static final class BindThirdLambda3<T1, T2, T3, T4, R> implements Lambda3<T1, T2, T4, R>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    private final T3 _arg3;
    public BindThirdLambda3(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T3 arg3) {
      _lambda = lambda; _arg3 = arg3;
    }
    public R value(T1 arg1, T2 arg2, T4 arg4) { return _lambda.value(arg1, arg2, _arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda3<T1, T2, T3, R> 
    bindFourth(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T4 arg4) {
    return new BindFourthLambda3<T1, T2, T3, T4, R>(lambda, arg4);
  }
  
  private static final class BindFourthLambda3<T1, T2, T3, T4, R> implements Lambda3<T1, T2, T3, R>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    private final T4 _arg4;
    public BindFourthLambda3(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, T4 arg4) {
      _lambda = lambda; _arg4 = arg4;
    }
    public R value(T1 arg1, T2 arg2, T3 arg3) { return _lambda.value(arg1, arg2, arg3, _arg4); }
  }
  
  
  
  public static <T> Condition bindFirst(Predicate<? super T> pred, T arg) {
    return new BindFirstCondition<T>(pred, arg);
  }
  
  private static final class BindFirstCondition<T> implements Condition, Serializable {
    private final Predicate<? super T> _pred;
    private final T _arg;
    public BindFirstCondition(Predicate<? super T> pred, T arg) { _pred = pred; _arg = arg; }
    public boolean isTrue() { return _pred.contains(_arg); }
  }
  
  
  public static <T1, T2> Predicate<T2> bindFirst(Predicate2<? super T1, ? super T2> pred, T1 arg1) {
    return new BindFirstPredicate<T1, T2>(pred, arg1);
  }
  
  private static final class BindFirstPredicate<T1, T2> implements Predicate<T2>, Serializable {
    private final Predicate2<? super T1, ? super T2> _pred;
    private final T1 _arg1;
    public BindFirstPredicate(Predicate2<? super T1, ? super T2> pred, T1 arg1) {
      _pred = pred; _arg1 = arg1;
    }
    public boolean contains(T2 arg2) { return _pred.contains(_arg1, arg2); }
  }
  
  
  public static <T1, T2> Predicate<T1> bindSecond(Predicate2<? super T1, ? super T2> pred, T2 arg2) {
    return new BindSecondPredicate<T1, T2>(pred, arg2);
  }
  
  private static final class BindSecondPredicate<T1, T2> implements Predicate<T1>, Serializable {
    private final Predicate2<? super T1, ? super T2> _pred;
    private final T2 _arg2;
    public BindSecondPredicate(Predicate2<? super T1, ? super T2> pred, T2 arg2) {
      _pred = pred; _arg2 = arg2;
    }
    public boolean contains(T1 arg1) { return _pred.contains(arg1, _arg2); }
  }
  
  
  public static <T1, T2, T3>
    Predicate2<T2, T3> bindFirst(Predicate3<? super T1, ? super T2, ? super T3> pred, T1 arg1) {
    return new BindFirstPredicate2<T1, T2, T3>(pred, arg1);
  }
  
  private static final class BindFirstPredicate2<T1, T2, T3> implements Predicate2<T2, T3>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _pred;
    private final T1 _arg1;
    public BindFirstPredicate2(Predicate3<? super T1, ? super T2, ? super T3> pred, T1 arg1) {
      _pred = pred; _arg1 = arg1;
    }
    public boolean contains(T2 arg2, T3 arg3) { return _pred.contains(_arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3>
    Predicate2<T1, T3> bindSecond(Predicate3<? super T1, ? super T2, ? super T3> pred, T2 arg2) {
    return new BindSecondPredicate2<T1, T2, T3>(pred, arg2);
  }
  
  private static final class BindSecondPredicate2<T1, T2, T3> implements Predicate2<T1, T3>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _pred;
    private final T2 _arg2;
    public BindSecondPredicate2(Predicate3<? super T1, ? super T2, ? super T3> pred, T2 arg2) {
      _pred = pred; _arg2 = arg2;
    }
    public boolean contains(T1 arg1, T3 arg3) { return _pred.contains(arg1, _arg2, arg3); }
  }
  
  
  public static <T1, T2, T3>
    Predicate2<T1, T2> bindThird(Predicate3<? super T1, ? super T2, ? super T3> pred, T3 arg3) {
    return new BindThirdPredicate2<T1, T2, T3>(pred, arg3);
  }
  
  private static final class BindThirdPredicate2<T1, T2, T3> implements Predicate2<T1, T2>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _pred;
    private final T3 _arg3;
    public BindThirdPredicate2(Predicate3<? super T1, ? super T2, ? super T3> pred, T3 arg3) {
      _pred = pred; _arg3 = arg3;
    }
    public boolean contains(T1 arg1, T2 arg2) { return _pred.contains(arg1, arg2, _arg3); }
  }
  
  
  public static <T1, T2, T3, T4> Predicate3<T2, T3, T4> 
    bindFirst(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T1 arg1) {
    return new BindFirstPredicate3<T1, T2, T3, T4>(pred, arg1);
  }
  
  private static final class BindFirstPredicate3<T1, T2, T3, T4> implements Predicate3<T2, T3, T4>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _pred;
    private final T1 _arg1;
    public BindFirstPredicate3(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T1 arg1) {
      _pred = pred; _arg1 = arg1;
    }
    public boolean contains(T2 arg2, T3 arg3, T4 arg4) { return _pred.contains(_arg1, arg2, arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4> Predicate3<T1, T3, T4> 
    bindSecond(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T2 arg2) {
    return new BindSecondPredicate3<T1, T2, T3, T4>(pred, arg2);
  }
  
  private static final class BindSecondPredicate3<T1, T2, T3, T4> implements Predicate3<T1, T3, T4>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _pred;
    private final T2 _arg2;
    public BindSecondPredicate3(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T2 arg2) {
      _pred = pred; _arg2 = arg2;
    }
    public boolean contains(T1 arg1, T3 arg3, T4 arg4) { return _pred.contains(arg1, _arg2, arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4> Predicate3<T1, T2, T4> 
    bindThird(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T3 arg3) {
    return new BindThirdPredicate3<T1, T2, T3, T4>(pred, arg3);
  }
  
  private static final class BindThirdPredicate3<T1, T2, T3, T4> implements Predicate3<T1, T2, T4>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _pred;
    private final T3 _arg3;
    public BindThirdPredicate3(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T3 arg3) {
      _pred = pred; _arg3 = arg3;
    }
    public boolean contains(T1 arg1, T2 arg2, T4 arg4) { return _pred.contains(arg1, arg2, _arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4> Predicate3<T1, T2, T3> 
    bindFourth(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T4 arg4) {
    return new BindFourthPredicate3<T1, T2, T3, T4>(pred, arg4);
  }
  
  private static final class BindFourthPredicate3<T1, T2, T3, T4> implements Predicate3<T1, T2, T3>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _pred;
    private final T4 _arg4;
    public BindFourthPredicate3(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred, T4 arg4) {
      _pred = pred; _arg4 = arg4;
    }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3) { return _pred.contains(arg1, arg2, arg3, _arg4); }
  }
  
  
  
  public static <T> Runnable bindFirst(Runnable1<? super T> runnable, T arg) {
    return new BindFirstRunnable<T>(runnable, arg);
  }
  
  private static final class BindFirstRunnable<T> implements Runnable, Serializable {
    private final Runnable1<? super T> _runnable;
    private final T _arg;
    public BindFirstRunnable(Runnable1<? super T> runnable, T arg) { _runnable = runnable; _arg = arg; }
    public void run() { _runnable.run(_arg); }
  }
  
  
  public static <T1, T2> Runnable1<T2> bindFirst(Runnable2<? super T1, ? super T2> runnable, T1 arg1) {
    return new BindFirstRunnable1<T1, T2>(runnable, arg1);
  }
  
  private static final class BindFirstRunnable1<T1, T2> implements Runnable1<T2>, Serializable {
    private final Runnable2<? super T1, ? super T2> _runnable;
    private final T1 _arg1;
    public BindFirstRunnable1(Runnable2<? super T1, ? super T2> runnable, T1 arg1) {
      _runnable = runnable; _arg1 = arg1;
    }
    public void run(T2 arg2) { _runnable.run(_arg1, arg2); }
  }
  
  
  public static <T1, T2> Runnable1<T1> bindSecond(Runnable2<? super T1, ? super T2> runnable, T2 arg2) {
    return new BindSecondRunnable1<T1, T2>(runnable, arg2);
  }
  
  private static final class BindSecondRunnable1<T1, T2> implements Runnable1<T1>, Serializable {
    private final Runnable2<? super T1, ? super T2> _runnable;
    private final T2 _arg2;
    public BindSecondRunnable1(Runnable2<? super T1, ? super T2> runnable, T2 arg2) {
      _runnable = runnable; _arg2 = arg2;
    }
    public void run(T1 arg1) { _runnable.run(arg1, _arg2); }
  }
  
  
  public static <T1, T2, T3>
    Runnable2<T2, T3> bindFirst(Runnable3<? super T1, ? super T2, ? super T3> runnable, T1 arg1) {
    return new BindFirstRunnable2<T1, T2, T3>(runnable, arg1);
  }
  
  private static final class BindFirstRunnable2<T1, T2, T3> implements Runnable2<T2, T3>, Serializable {
    private final Runnable3<? super T1, ? super T2, ? super T3> _runnable;
    private final T1 _arg1;
    public BindFirstRunnable2(Runnable3<? super T1, ? super T2, ? super T3> runnable, T1 arg1) {
      _runnable = runnable; _arg1 = arg1;
    }
    public void run(T2 arg2, T3 arg3) { _runnable.run(_arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3>
    Runnable2<T1, T3> bindSecond(Runnable3<? super T1, ? super T2, ? super T3> runnable, T2 arg2) {
    return new BindSecondRunnable2<T1, T2, T3>(runnable, arg2);
  }
  
  private static final class BindSecondRunnable2<T1, T2, T3> implements Runnable2<T1, T3>, Serializable {
    private final Runnable3<? super T1, ? super T2, ? super T3> _runnable;
    private final T2 _arg2;
    public BindSecondRunnable2(Runnable3<? super T1, ? super T2, ? super T3> runnable, T2 arg2) {
      _runnable = runnable; _arg2 = arg2;
    }
    public void run(T1 arg1, T3 arg3) { _runnable.run(arg1, _arg2, arg3); }
  }
  
  
  public static <T1, T2, T3>
    Runnable2<T1, T2> bindThird(Runnable3<? super T1, ? super T2, ? super T3> runnable, T3 arg3) {
    return new BindThirdRunnable2<T1, T2, T3>(runnable, arg3);
  }
  
  private static final class BindThirdRunnable2<T1, T2, T3> implements Runnable2<T1, T2>, Serializable {
    private final Runnable3<? super T1, ? super T2, ? super T3> _runnable;
    private final T3 _arg3;
    public BindThirdRunnable2(Runnable3<? super T1, ? super T2, ? super T3> runnable, T3 arg3) {
      _runnable = runnable; _arg3 = arg3;
    }
    public void run(T1 arg1, T2 arg2) { _runnable.run(arg1, arg2, _arg3); }
  }
  
  
  public static <T1, T2, T3, T4> Runnable3<T2, T3, T4> 
    bindFirst(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T1 arg1) {
    return new BindFirstRunnable3<T1, T2, T3, T4>(runnable, arg1);
  }
  
  private static final class BindFirstRunnable3<T1, T2, T3, T4> implements Runnable3<T2, T3, T4>, Serializable {
    private final Runnable4<? super T1, ? super T2, ? super T3, ? super T4> _runnable;
    private final T1 _arg1;
    public BindFirstRunnable3(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T1 arg1) {
      _runnable = runnable; _arg1 = arg1;
    }
    public void run(T2 arg2, T3 arg3, T4 arg4) { _runnable.run(_arg1, arg2, arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4> Runnable3<T1, T3, T4> 
    bindSecond(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T2 arg2) {
    return new BindSecondRunnable3<T1, T2, T3, T4>(runnable, arg2);
  }
  
  private static final class BindSecondRunnable3<T1, T2, T3, T4> implements Runnable3<T1, T3, T4>, Serializable {
    private final Runnable4<? super T1, ? super T2, ? super T3, ? super T4> _runnable;
    private final T2 _arg2;
    public BindSecondRunnable3(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T2 arg2) {
      _runnable = runnable; _arg2 = arg2;
    }
    public void run(T1 arg1, T3 arg3, T4 arg4) { _runnable.run(arg1, _arg2, arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4> Runnable3<T1, T2, T4> 
    bindThird(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T3 arg3) {
    return new BindThirdRunnable3<T1, T2, T3, T4>(runnable, arg3);
  }
  
  private static final class BindThirdRunnable3<T1, T2, T3, T4> implements Runnable3<T1, T2, T4>, Serializable {
    private final Runnable4<? super T1, ? super T2, ? super T3, ? super T4> _runnable;
    private final T3 _arg3;
    public BindThirdRunnable3(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T3 arg3) {
      _runnable = runnable; _arg3 = arg3;
    }
    public void run(T1 arg1, T2 arg2, T4 arg4) { _runnable.run(arg1, arg2, _arg3, arg4); }
  }
  
  
  public static <T1, T2, T3, T4> Runnable3<T1, T2, T3> 
    bindFourth(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T4 arg4) {
    return new BindFourthRunnable3<T1, T2, T3, T4>(runnable, arg4);
  }
  
  private static final class BindFourthRunnable3<T1, T2, T3, T4> implements Runnable3<T1, T2, T3>, Serializable {
    private final Runnable4<? super T1, ? super T2, ? super T3, ? super T4> _runnable;
    private final T4 _arg4;
    public BindFourthRunnable3(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable, T4 arg4) {
      _runnable = runnable; _arg4 = arg4;
    }
    public void run(T1 arg1, T2 arg2, T3 arg3) { _runnable.run(arg1, arg2, arg3, _arg4); }
  }

  
  
  public static <T1, T2, R> Lambda<T1, Lambda<T2, R>> curry(Lambda2<? super T1, ? super T2, ? extends R> lambda) {
    return new CurriedLambda2<T1, T2, R>(lambda);
  }
  
  private static final class CurriedLambda2<T1, T2, R> implements Lambda<T1, Lambda<T2, R>>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    public CurriedLambda2(Lambda2<? super T1, ? super T2, ? extends R> lambda) { _lambda = lambda; }
    public Lambda<T2, R> value(T1 arg) { return new BindFirstLambda<T1, T2, R>(_lambda, arg); }
  }
  
  
  public static <T1, T2, T3, R>
    Lambda<T1, Lambda<T2, Lambda<T3, R>>> curry(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) {
    return new CurriedLambda3<T1, T2, T3, R>(lambda);
  }
  
  private static final class CurriedLambda3<T1, T2, T3, R>
    implements Lambda<T1, Lambda<T2, Lambda<T3, R>>>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    public CurriedLambda3(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) { _lambda = lambda; }
    public Lambda<T2, Lambda<T3, R>> value(T1 arg) {
      return new CurriedLambda2<T2, T3, R>(new BindFirstLambda2<T1, T2, T3, R>(_lambda, arg));
    }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda<T1, Lambda<T2, Lambda<T3, Lambda<T4, R>>>>
    curry(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda) {
    return new CurriedLambda4<T1, T2, T3, T4, R>(lambda);
  }
  
  private static final class CurriedLambda4<T1, T2, T3, T4, R>
    implements Lambda<T1, Lambda<T2, Lambda<T3, Lambda<T4, R>>>>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    public CurriedLambda4(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda) {
      _lambda = lambda;
    }
    public Lambda<T2, Lambda<T3, Lambda<T4, R>>> value(T1 arg) {
      return new CurriedLambda3<T2, T3, T4, R>(new BindFirstLambda3<T1, T2, T3, T4, R>(_lambda, arg));
    }
  }
  
  
  public static <T1, T2, R> Lambda2<T2, T1, R> transpose(Lambda2<? super T1, ? super T2, ? extends R> lambda) {
    return new TransposedLambda2<T1, T2, R>(lambda);
  }
  
  private static final class TransposedLambda2<T1, T2, R> implements Lambda2<T2, T1, R>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    public TransposedLambda2(Lambda2<? super T1, ? super T2, ? extends R> lambda) { _lambda = lambda; }
    public R value(T2 arg2, T1 arg1) { return _lambda.value(arg1, arg2); }
  }
  
  
  public static <T1, T2> Predicate2<T2, T1> transpose(Predicate2<? super T1, ? super T2> pred) {
    return new TransposedPredicate2<T1, T2>(pred);
  }
  
  private static final class TransposedPredicate2<T1, T2> implements Predicate2<T2, T1>, Serializable {
    private final Predicate2<? super T1, ? super T2> _pred;
    public TransposedPredicate2(Predicate2<? super T1, ? super T2> pred) { _pred = pred; }
    public boolean contains(T2 arg2, T1 arg1) { return _pred.contains(arg1, arg2); }
  }
  
  
  public static <T1, T2> Runnable2<T2, T1> transpose(Runnable2<? super T1, ? super T2> r) {
    return new TransposedRunnable2<T1, T2>(r);
  }
  
  private static final class TransposedRunnable2<T1, T2> implements Runnable2<T2, T1>, Serializable {
    private final Runnable2<? super T1, ? super T2> _r;
    public TransposedRunnable2(Runnable2<? super T1, ? super T2> r) { _r = r; }
    public void run(T2 arg2, T1 arg1) { _r.run(arg1, arg2); }
  }
  
  
  public static <T, R> Thunk<R> flatten0(Lambda<? super Null<T>, ? extends R> lambda) {
    return new BindFirstThunk<Null<T>, R>(lambda, Null.<T>make());
  }
  
  
  public static <T1, T2, R> Lambda2<T1, T2, R> flatten2(Lambda<? super Pair<T1, T2>, ? extends R> lambda) {
    return new FlattenedLambda2<T1, T2, R>(lambda);
  }
  
  private static final class FlattenedLambda2<T1, T2, R> implements Lambda2<T1, T2, R>, Serializable {
    private final Lambda<? super Pair<T1, T2>, ? extends R> _lambda;
    public FlattenedLambda2(Lambda<? super Pair<T1, T2>, ? extends R> lambda) { _lambda = lambda; }
    public R value(T1 arg1, T2 arg2) { return _lambda.value(new Pair<T1, T2>(arg1, arg2)); }
  }
  
  
  public static <T1, T2, T3, R>
    Lambda3<T1, T2, T3, R> flatten3(Lambda<? super Triple<T1, T2, T3>, ? extends R> lambda) {
    return new FlattenedLambda3<T1, T2, T3, R>(lambda);
  }
  
  private static final class FlattenedLambda3<T1, T2, T3, R> implements Lambda3<T1, T2, T3, R>, Serializable {
    private final Lambda<? super Triple<T1, T2, T3>, ? extends R> _lambda;
    public FlattenedLambda3(Lambda<? super Triple<T1, T2, T3>, ? extends R> lambda) { _lambda = lambda; }
    public R value(T1 arg1, T2 arg2, T3 arg3) {
      return _lambda.value(new Triple<T1, T2, T3>(arg1, arg2, arg3));
    }
  }
  
  
  public static <T1, T2, T3, T4, R>
    Lambda4<T1, T2, T3, T4, R> flatten4(Lambda<? super Quad<T1, T2, T3, T4>, ? extends R> lambda) {
    return new FlattenedLambda4<T1, T2, T3, T4, R>(lambda);
  }

  private static final class FlattenedLambda4<T1, T2, T3, T4, R>
    implements Lambda4<T1, T2, T3, T4, R>, Serializable {
    private final Lambda<? super Quad<T1, T2, T3, T4>, ? extends R> _lambda;
    public FlattenedLambda4(Lambda<? super Quad<T1, T2, T3, T4>, ? extends R> lambda) { _lambda = lambda; }
    public R value(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      return _lambda.value(new Quad<T1, T2, T3, T4>(arg1, arg2, arg3, arg4));
    }
  }
  
  
  public static <T> Condition flatten0(Predicate<? super Null<T>> pred) {
    return new BindFirstCondition<Null<T>>(pred, Null.<T>make());
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> flatten2(Predicate<? super Pair<T1, T2>> pred) {
    return new FlattenedPredicate2<T1, T2>(pred);
  }
  
  private static final class FlattenedPredicate2<T1, T2> implements Predicate2<T1, T2>, Serializable {
    private final Predicate<? super Pair<T1, T2>> _pred;
    public FlattenedPredicate2(Predicate<? super Pair<T1, T2>> pred) { _pred = pred; }
    public boolean contains(T1 arg1, T2 arg2) { return _pred.contains(new Pair<T1, T2>(arg1, arg2)); }
  }
  
  
  public static <T1, T2, T3>
    Predicate3<T1, T2, T3> flatten3(Predicate<? super Triple<T1, T2, T3>> pred) {
    return new FlattenedPredicate3<T1, T2, T3>(pred);
  }
  
  private static final class FlattenedPredicate3<T1, T2, T3> implements Predicate3<T1, T2, T3>, Serializable {
    private final Predicate<? super Triple<T1, T2, T3>> _pred;
    public FlattenedPredicate3(Predicate<? super Triple<T1, T2, T3>> pred) { _pred = pred; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3) {
      return _pred.contains(new Triple<T1, T2, T3>(arg1, arg2, arg3));
    }
  }
  
  
  public static <T1, T2, T3, T4>
    Predicate4<T1, T2, T3, T4> flatten4(Predicate<? super Quad<T1, T2, T3, T4>> pred) {
    return new FlattenedPredicate4<T1, T2, T3, T4>(pred);
  }

  private static final class FlattenedPredicate4<T1, T2, T3, T4>
    implements Predicate4<T1, T2, T3, T4>, Serializable {
    private final Predicate<? super Quad<T1, T2, T3, T4>> _pred;
    public FlattenedPredicate4(Predicate<? super Quad<T1, T2, T3, T4>> pred) { _pred = pred; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      return _pred.contains(new Quad<T1, T2, T3, T4>(arg1, arg2, arg3, arg4));
    }
  }

  
  public static <T> Runnable flatten0(Runnable1<? super Null<T>> runnable) {
    return new BindFirstRunnable<Null<T>>(runnable, Null.<T>make());
  }
  
  
  public static <T1, T2> Runnable2<T1, T2> flatten2(Runnable1<? super Pair<T1, T2>> runnable) {
    return new FlattenedRunnable2<T1, T2>(runnable);
  }
  
  private static final class FlattenedRunnable2<T1, T2> implements Runnable2<T1, T2>, Serializable {
    private final Runnable1<? super Pair<T1, T2>> _runnable;
    public FlattenedRunnable2(Runnable1<? super Pair<T1, T2>> runnable) { _runnable = runnable; }
    public void run(T1 arg1, T2 arg2) { _runnable.run(new Pair<T1, T2>(arg1, arg2)); }
  }
  
  
  public static <T1, T2, T3>
    Runnable3<T1, T2, T3> flatten3(Runnable1<? super Triple<T1, T2, T3>> runnable) {
    return new FlattenedRunnable3<T1, T2, T3>(runnable);
  }
  
  private static final class FlattenedRunnable3<T1, T2, T3> implements Runnable3<T1, T2, T3>, Serializable {
    private final Runnable1<? super Triple<T1, T2, T3>> _runnable;
    public FlattenedRunnable3(Runnable1<? super Triple<T1, T2, T3>> runnable) { _runnable = runnable; }
    public void run(T1 arg1, T2 arg2, T3 arg3) {
      _runnable.run(new Triple<T1, T2, T3>(arg1, arg2, arg3));
    }
  }
  
  
  public static <T1, T2, T3, T4>
    Runnable4<T1, T2, T3, T4> flatten4(Runnable1<? super Quad<T1, T2, T3, T4>> runnable) {
    return new FlattenedRunnable4<T1, T2, T3, T4>(runnable);
  }

  private static final class FlattenedRunnable4<T1, T2, T3, T4>
    implements Runnable4<T1, T2, T3, T4>, Serializable {
    private final Runnable1<? super Quad<T1, T2, T3, T4>> _runnable;
    public FlattenedRunnable4(Runnable1<? super Quad<T1, T2, T3, T4>> runnable) { _runnable = runnable; }
    public void run(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      _runnable.run(new Quad<T1, T2, T3, T4>(arg1, arg2, arg3, arg4));
    }
  }
  
  
  public static <R> Lambda<Null<?>, R> unary(Thunk<? extends R> thunk) {
    return new UnaryThunk<R>(thunk);
  }
  
  private static final class UnaryThunk<R> implements Lambda<Null<?>, R>, Serializable {
    private final Thunk<? extends R> _thunk;
    public UnaryThunk(Thunk<? extends R> thunk) { _thunk = thunk; }
    public R value(Null<?> arg) { return _thunk.value(); }
  }
  
  
  public static <T1, T2, R> Lambda<Pair<T1, T2>, R>
    unary(Lambda2<? super T1, ? super T2, ? extends R> lambda) {
    return new UnaryLambda2<T1, T2, R>(lambda);
  }
  
  private static final class UnaryLambda2<T1, T2, R> implements Lambda<Pair<T1, T2>, R>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    public UnaryLambda2(Lambda2<? super T1, ? super T2, ? extends R> lambda) { _lambda = lambda; }
    public R value(Pair<T1, T2> arg) { return _lambda.value(arg.first(), arg.second()); }
  }
  
  
  public static <T1, T2, T3, R> Lambda<Triple<T1, T2, T3>, R>
    unary(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) {
    return new UnaryLambda3<T1, T2, T3, R>(lambda);
  }
  
  private static final class UnaryLambda3<T1, T2, T3, R>
    implements Lambda<Triple<T1, T2, T3>, R>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    public UnaryLambda3(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) {
      _lambda = lambda;
    }
    public R value(Triple<T1, T2, T3> arg) {
      return _lambda.value(arg.first(), arg.second(), arg.third());
    }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda<Quad<T1, T2, T3, T4>, R>
    unary(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda) {
    return new UnaryLambda4<T1, T2, T3, T4, R>(lambda);
  }
  
  private static final class UnaryLambda4<T1, T2, T3, T4, R>
    implements Lambda<Quad<T1, T2, T3, T4>, R>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    public UnaryLambda4(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda) {
      _lambda = lambda;
    }
    public R value(Quad<T1, T2, T3, T4> arg) {
      return _lambda.value(arg.first(), arg.second(), arg.third(), arg.fourth());
    }
  }
  
  
  public static Predicate<Null<?>> unary(Condition cond) {
    return new UnaryCondition(cond);
  }
  
  private static final class UnaryCondition implements Predicate<Null<?>>, Serializable {
    private final Condition _cond;
    public UnaryCondition(Condition cond) { _cond = cond; }
    public boolean contains(Null<?> arg) { return _cond.isTrue(); }
  }
  
  
  public static <T1, T2> Predicate<Pair<T1, T2>>
    unary(Predicate2<? super T1, ? super T2> pred) {
    return new UnaryPredicate2<T1, T2>(pred);
  }
  
  private static final class UnaryPredicate2<T1, T2> implements Predicate<Pair<T1, T2>>, Serializable {
    private final Predicate2<? super T1, ? super T2> _pred;
    public UnaryPredicate2(Predicate2<? super T1, ? super T2> pred) { _pred = pred; }
    public boolean contains(Pair<T1, T2> arg) { return _pred.contains(arg.first(), arg.second()); }
  }
  
  
  public static <T1, T2, T3> Predicate<Triple<T1, T2, T3>>
    unary(Predicate3<? super T1, ? super T2, ? super T3> pred) {
    return new UnaryPredicate3<T1, T2, T3>(pred);
  }
  
  private static final class UnaryPredicate3<T1, T2, T3>
    implements Predicate<Triple<T1, T2, T3>>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _pred;
    public UnaryPredicate3(Predicate3<? super T1, ? super T2, ? super T3> pred) {
      _pred = pred;
    }
    public boolean contains(Triple<T1, T2, T3> arg) {
      return _pred.contains(arg.first(), arg.second(), arg.third());
    }
  }
  
  
  public static <T1, T2, T3, T4> Predicate<Quad<T1, T2, T3, T4>>
    unary(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred) {
    return new UnaryPredicate4<T1, T2, T3, T4>(pred);
  }
  
  private static final class UnaryPredicate4<T1, T2, T3, T4>
    implements Predicate<Quad<T1, T2, T3, T4>>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _pred;
    public UnaryPredicate4(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred) {
      _pred = pred;
    }
    public boolean contains(Quad<T1, T2, T3, T4> arg) {
      return _pred.contains(arg.first(), arg.second(), arg.third(), arg.fourth());
    }
  }
  
  
  public static Runnable1<Null<?>> unary(Runnable runnable) {
    return new UnaryRunnable(runnable);
  }
  
  private static final class UnaryRunnable implements Runnable1<Null<?>>, Serializable {
    private final Runnable _runnable;
    public UnaryRunnable(Runnable runnable) { _runnable = runnable; }
    public void run(Null<?> arg) { _runnable.run(); }
  }
  
  
  public static <T1, T2> Runnable1<Pair<T1, T2>> unary(Runnable2<? super T1, ? super T2> runnable) {
    return new UnaryRunnable2<T1, T2>(runnable);
  }
  
  private static final class UnaryRunnable2<T1, T2> implements Runnable1<Pair<T1, T2>>, Serializable {
    private final Runnable2<? super T1, ? super T2> _runnable;
    public UnaryRunnable2(Runnable2<? super T1, ? super T2> runnable) { _runnable = runnable; }
    public void run(Pair<T1, T2> arg) { _runnable.run(arg.first(), arg.second()); }
  }
  
  
  public static <T1, T2, T3> Runnable1<Triple<T1, T2, T3>>
    unary(Runnable3<? super T1, ? super T2, ? super T3> runnable) {
    return new UnaryRunnable3<T1, T2, T3>(runnable);
  }
  
  private static final class UnaryRunnable3<T1, T2, T3>
    implements Runnable1<Triple<T1, T2, T3>>, Serializable {
    private final Runnable3<? super T1, ? super T2, ? super T3> _runnable;
    public UnaryRunnable3(Runnable3<? super T1, ? super T2, ? super T3> runnable) {
      _runnable = runnable;
    }
    public void run(Triple<T1, T2, T3> arg) {
      _runnable.run(arg.first(), arg.second(), arg.third());
    }
  }
  
  
  public static <T1, T2, T3, T4> Runnable1<Quad<T1, T2, T3, T4>>
    unary(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable) {
    return new UnaryRunnable4<T1, T2, T3, T4>(runnable);
  }
  
  private static final class UnaryRunnable4<T1, T2, T3, T4>
    implements Runnable1<Quad<T1, T2, T3, T4>>, Serializable {
    private final Runnable4<? super T1, ? super T2, ? super T3, ? super T4> _runnable;
    public UnaryRunnable4(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> runnable) {
      _runnable = runnable;
    }
    public void run(Quad<T1, T2, T3, T4> arg) {
      _runnable.run(arg.first(), arg.second(), arg.third(), arg.fourth());
    }
  }
  

  
  public static <R> Thunk<Option<R>> wrapPartial(Thunk<? extends R> thunk, boolean filterNull,
                                                 Predicate<? super RuntimeException> filterException) {
    return new PartialThunk<R>(thunk, filterNull, filterException);
  }
  
  private static final class PartialThunk<R> implements Thunk<Option<R>>, Serializable {
    private final Thunk<? extends R> _thunk;
    private final boolean _filterNull;
    private final Predicate<? super RuntimeException> _filterException;
    public PartialThunk(Thunk<? extends R> thunk, boolean filterNull,
                        Predicate<? super RuntimeException> filterException) {
      _thunk = thunk; _filterNull = filterNull; _filterException = filterException;
    }
    public Option<R> value() {
      try {
        if (_filterNull) { return Option.<R>wrap(_thunk.value()); }
        else { return Option.<R>some(_thunk.value()); }
      }
      catch (RuntimeException e) {
        if (_filterException.contains(e)) { return Option.none(); }
        else { throw e; }
      }
    }
  }
  
  
  public static <T, R> Lambda<T, Option<R>> wrapPartial(Lambda<? super T, ? extends R> lambda, boolean filterNull,
                                                        Predicate<? super RuntimeException> filterException) {
    return new PartialLambda<T, R>(lambda, filterNull, filterException);
  }
  
  private static final class PartialLambda<T, R> implements Lambda<T, Option<R>>, Serializable {
    private final Lambda<? super T, ? extends R> _lambda;
    private final boolean _filterNull;
    private final Predicate<? super RuntimeException> _filterException;
    public PartialLambda(Lambda<? super T, ? extends R> lambda, boolean filterNull,
                         Predicate<? super RuntimeException> filterException) {
      _lambda = lambda; _filterNull = filterNull; _filterException = filterException;
    }
    public Option<R> value(T arg) {
      try {
        if (_filterNull) { return Option.<R>wrap(_lambda.value(arg)); }
        else { return Option.<R>some(_lambda.value(arg)); }
      }
      catch (RuntimeException e) {
        if (_filterException.contains(e)) { return Option.none(); }
        else { throw e; }
      }
    }
  }
  
  
  public static <T1, T2, R>
    Lambda2<T1, T2, Option<R>> wrapPartial(Lambda2<? super T1, ? super T2, ? extends R> lambda, boolean filterNull,
                                           Predicate<? super RuntimeException> filterException) {
    return new PartialLambda2<T1, T2, R>(lambda, filterNull, filterException);
  }
  
  private static final class PartialLambda2<T1, T2, R> implements Lambda2<T1, T2, Option<R>>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    private final boolean _filterNull;
    private final Predicate<? super RuntimeException> _filterException;
    public PartialLambda2(Lambda2<? super T1, ? super T2, ? extends R> lambda, boolean filterNull,
                          Predicate<? super RuntimeException> filterException) {
      _lambda = lambda; _filterNull = filterNull; _filterException = filterException;
    }
    public Option<R> value(T1 arg1, T2 arg2) {
      try {
        if (_filterNull) { return Option.<R>wrap(_lambda.value(arg1, arg2)); }
        else { return Option.<R>some(_lambda.value(arg1, arg2)); }
      }
      catch (RuntimeException e) {
        if (_filterException.contains(e)) { return Option.none(); }
        else { throw e; }
      }
    }
  }
  
  
  public static <T1, T2, T3, R> Lambda3<T1, T2, T3, Option<R>>
    wrapPartial(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, boolean filterNull,
                Predicate<? super RuntimeException> filterException) {
    return new PartialLambda3<T1, T2, T3, R>(lambda, filterNull, filterException);
  }
  
  private static final class PartialLambda3<T1, T2, T3, R> implements Lambda3<T1, T2, T3, Option<R>>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    private final boolean _filterNull;
    private final Predicate<? super RuntimeException> _filterException;
    public PartialLambda3(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda, boolean filterNull,
                          Predicate<? super RuntimeException> filterException) {
      _lambda = lambda; _filterNull = filterNull; _filterException = filterException;
    }
    public Option<R> value(T1 arg1, T2 arg2, T3 arg3) {
      try {
        if (_filterNull) { return Option.<R>wrap(_lambda.value(arg1, arg2, arg3)); }
        else { return Option.<R>some(_lambda.value(arg1, arg2, arg3)); }
      }
      catch (RuntimeException e) {
        if (_filterException.contains(e)) { return Option.none(); }
        else { throw e; }
      }
    }
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda4<T1, T2, T3, T4, Option<R>>
    wrapPartial(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda, boolean filterNull,
                Predicate<? super RuntimeException> filterException) {
    return new PartialLambda4<T1, T2, T3, T4, R>(lambda, filterNull, filterException);
  }
  
  private static final class PartialLambda4<T1, T2, T3, T4, R>
    implements Lambda4<T1, T2, T3, T4, Option<R>>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    private final boolean _filterNull;
    private final Predicate<? super RuntimeException> _filterException;
    public PartialLambda4(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda,
                          boolean filterNull, Predicate<? super RuntimeException> filterException) {
      _lambda = lambda; _filterNull = filterNull; _filterException = filterException;
    }
    public Option<R> value(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      try {
        if (_filterNull) { return Option.<R>wrap(_lambda.value(arg1, arg2, arg3, arg4)); }
        else { return Option.<R>some(_lambda.value(arg1, arg2, arg3, arg4)); }
      }
      catch (RuntimeException e) {
        if (_filterException.contains(e)) { return Option.none(); }
        else { throw e; }
      }
    }
  }
  
  
  public interface LiftedLambda<T, R> extends Lambda<Option<? extends T>, Option<R>> {}
  
  
  public static <T, R> LiftedLambda<T, R> lift(Lambda<? super T, ? extends R> lambda) {
    return new WrappedLiftedLambda<T, R>(lambda);
  }
  
  private static final class WrappedLiftedLambda<T, R> implements LiftedLambda<T, R>, Serializable {
    private final Lambda<? super T, ? extends R> _lambda;
    public WrappedLiftedLambda(Lambda<? super T, ? extends R> lambda) { _lambda = lambda; }
    public Option<R> value(Option<? extends T> arg) {
      if (arg.isSome()) { return Option.<R>some(_lambda.value(arg.unwrap())); }
      else { return Option.none(); }
    }
  }
  
  
  public interface LiftedLambda2<T1, T2, R>
    extends Lambda2<Option<? extends T1>, Option<? extends T2>, Option<R>> {}
  
  
  public static <T1, T2, R> LiftedLambda2<T1, T2, R> lift(Lambda2<? super T1, ? super T2, ? extends R> lambda) {
    return new WrappedLiftedLambda2<T1, T2, R>(lambda);
  }
  
  private static final class WrappedLiftedLambda2<T1, T2, R> implements LiftedLambda2<T1, T2, R>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends R> _lambda;
    public WrappedLiftedLambda2(Lambda2<? super T1, ? super T2, ? extends R> lambda) { _lambda = lambda; }
    public Option<R> value(Option<? extends T1> arg1, Option<? extends T2> arg2) {
      if (arg1.isSome() && arg2.isSome()) { return Option.<R>some(_lambda.value(arg1.unwrap(), arg2.unwrap())); }
      else { return Option.none(); }
    }
  }
  
  
  public interface LiftedLambda3<T1, T2, T3, R>
    extends Lambda3<Option<? extends T1>, Option<? extends T2>, Option<? extends T3>, Option<R>> {}
  
  
  public static <T1, T2, T3, R> LiftedLambda3<T1, T2, T3, R>
    lift(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) {
    return new WrappedLiftedLambda3<T1, T2, T3, R>(lambda);
  }
  
  private static final class WrappedLiftedLambda3<T1, T2, T3, R> implements LiftedLambda3<T1, T2, T3, R>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends R> _lambda;
    public WrappedLiftedLambda3(Lambda3<? super T1, ? super T2, ? super T3, ? extends R> lambda) { _lambda = lambda; }
    public Option<R> value(Option<? extends T1> arg1, Option<? extends T2> arg2, Option<? extends T3> arg3) {
      if (arg1.isSome() && arg2.isSome() && arg3.isSome()) {
        return Option.<R>some(_lambda.value(arg1.unwrap(), arg2.unwrap(), arg3.unwrap()));
      }
      else { return Option.none(); }
    }
  }
  
  
  public interface LiftedLambda4<T1, T2, T3, T4, R>
    extends Lambda4<Option<? extends T1>, Option<? extends T2>, Option<? extends T3>, Option<? extends T4>, Option<R>> {}
  
  
  public static <T1, T2, T3, T4, R> LiftedLambda4<T1, T2, T3, T4, R>
    lift(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda) {
    return new WrappedLiftedLambda4<T1, T2, T3, T4, R>(lambda);
  }
  
  private static final class WrappedLiftedLambda4<T1, T2, T3, T4, R>
      implements LiftedLambda4<T1, T2, T3, T4, R>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> _lambda;
    public WrappedLiftedLambda4(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> lambda) {
      _lambda = lambda;
    }
    public Option<R> value(Option<? extends T1> arg1, Option<? extends T2> arg2, Option<? extends T3> arg3,
                           Option<? extends T4> arg4) {
      if (arg1.isSome() && arg2.isSome() && arg3.isSome() && arg4.isSome()) {
        return Option.<R>some(_lambda.value(arg1.unwrap(), arg2.unwrap(), arg3.unwrap(), arg4.unwrap()));
      }
      else { return Option.none(); }
    }
  }
  
  
  
  public static Condition negate(Condition cond) {
    return new NegationCondition(cond);
  }
  
  private static final class NegationCondition implements Condition, Serializable {
    private final Condition _c; 
    public NegationCondition(Condition c) { _c = c; }
    public boolean isTrue() { return !_c.isTrue(); }
  }
  
  
  public static <T> Predicate<T> negate(Predicate<? super T> pred) {
    return new NegationPredicate<T>(pred);
  }
  
  private static final class NegationPredicate<T> implements Predicate<T>, Serializable {
    private final Predicate<? super T> _p;
    public NegationPredicate(Predicate<? super T> p) { _p = p; }
    public boolean contains(T arg) { return !_p.contains(arg); }
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> negate(Predicate2<? super T1, ? super T2> pred) {
    return new NegationPredicate2<T1, T2>(pred);
  }
  
  private static final class NegationPredicate2<T1, T2> implements Predicate2<T1, T2>, Serializable {
    private final Predicate2<? super T1, ? super T2> _p;
    public NegationPredicate2(Predicate2<? super T1, ? super T2> p) { _p = p; }
    public boolean contains(T1 arg1, T2 arg2) { return !_p.contains(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3> Predicate3<T1, T2, T3> negate(Predicate3<? super T1, ? super T2, ? super T3> pred) {
    return new NegationPredicate3<T1, T2, T3>(pred);
  }
  
  private static final class NegationPredicate3<T1, T2, T3> implements Predicate3<T1, T2, T3>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _p;
    public NegationPredicate3(Predicate3<? super T1, ? super T2, ? super T3> p) { _p = p; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3) { return !_p.contains(arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3, T4> 
    Predicate4<T1, T2, T3, T4> negate(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> pred) {
    return new NegationPredicate4<T1, T2, T3, T4>(pred);
  }
  
  private static final class NegationPredicate4<T1, T2, T3, T4> implements Predicate4<T1, T2, T3, T4>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _p;
    public NegationPredicate4(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p) { _p = p; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3, T4 arg4) { return !_p.contains(arg1, arg2, arg3, arg4); }
  }
  
  
  
  public static Condition and(Condition c1, Condition c2) {
    return new AndCondition(IterUtil.make(c1, c2));
  }
  
  
  public static Condition and(Condition c1, Condition c2, Condition c3) {
    return new AndCondition(IterUtil.make(c1, c2, c3));
  }
  
  
  public static Condition and0(Iterable<? extends Condition> conds) {
    return new AndCondition(conds);
  }
  
  private static final class AndCondition implements Condition, Serializable {
    private final Iterable<? extends Condition> _conds;
    public AndCondition(Iterable<? extends Condition> conds) { _conds = conds; }
    public boolean isTrue() {
      for (Condition c : _conds) { if (!c.isTrue()) { return false; } }
      return true;
    }
  }
  
  
  public static <T> Predicate<T> and(Predicate<? super T> p1, Predicate<? super T> p2) {
    
    return new AndPredicate<T>(IterUtil.<Predicate<? super T>>make(p1, p2));
  }
  
  
  public static <T> Predicate<T> and(Predicate<? super T> p1, Predicate<? super T> p2, Predicate<? super T> p3) {
    return new AndPredicate<T>(IterUtil.<Predicate<? super T>>make(p1, p2, p3));
  }
  
  
  public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> preds) {
    return new AndPredicate<T>(preds);
  }
  
  private static final class AndPredicate<T> implements Predicate<T>, Serializable {
    private final Iterable<? extends Predicate<? super T>> _preds;
    public AndPredicate(Iterable<? extends Predicate<? super T>> preds) { _preds = preds; }
    public boolean contains(T arg) {
      for (Predicate<? super T> p : _preds) { if (!p.contains(arg)) { return false; } }
      return true;
    }
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> and(Predicate2<? super T1, ? super T2> p1, 
                                                Predicate2<? super T1, ? super T2> p2) {
    return new AndPredicate2<T1, T2>(IterUtil.<Predicate2<? super T1, ? super T2>>make(p1, p2));
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> and(Predicate2<? super T1, ? super T2> p1, 
                                                Predicate2<? super T1, ? super T2> p2,
                                                Predicate2<? super T1, ? super T2> p3) {
    return new AndPredicate2<T1, T2>(IterUtil.<Predicate2<? super T1, ? super T2>>make(p1, p2, p3));
  }
  
  
  public static <T1, T2> 
    Predicate2<T1, T2> and2(final Iterable<? extends Predicate2<? super T1, ? super T2>> preds) {
    return new AndPredicate2<T1, T2>(preds);
  }
  
  private static final class AndPredicate2<T1, T2> implements Predicate2<T1, T2>, Serializable {
    private final Iterable<? extends Predicate2<? super T1, ? super T2>> _preds;
    public AndPredicate2(Iterable<? extends Predicate2<? super T1, ? super T2>> preds) { _preds = preds; }
    public boolean contains(T1 arg1, T2 arg2) {
      for (Predicate2<? super T1, ? super T2> p : _preds) { if (!p.contains(arg1, arg2)) { return false; } }
      return true;
    }
  }

  
  public static <T1, T2, T3> 
    Predicate3<T1, T2, T3> and(Predicate3<? super T1, ? super T2, ? super T3> p1, 
                               Predicate3<? super T1, ? super T2, ? super T3> p2) {
    return new AndPredicate3<T1, T2, T3>(IterUtil.<Predicate3<? super T1, ? super T2, ? super T3>>
                                         make(p1, p2));
  }
  
  
  public static <T1, T2, T3> 
    Predicate3<T1, T2, T3> and(Predicate3<? super T1, ? super T2, ? super T3> p1, 
                               Predicate3<? super T1, ? super T2, ? super T3> p2,
                               Predicate3<? super T1, ? super T2, ? super T3> p3) {
    return new AndPredicate3<T1, T2, T3>(IterUtil.<Predicate3<? super T1, ? super T2, ? super T3>>
                                         make(p1, p2, p3));
  }
  
  
  public static <T1, T2, T3> 
    Predicate3<T1, T2, T3> and3(Iterable<? extends Predicate3<? super T1, ? super T2, ? super T3>> preds) {
    return new AndPredicate3<T1, T2, T3>(preds);
  }
  
  private static final class AndPredicate3<T1, T2, T3> implements Predicate3<T1, T2, T3>, Serializable {
    private final Iterable<? extends Predicate3<? super T1, ? super T2, ? super T3>> _preds;
    public AndPredicate3(Iterable<? extends Predicate3<? super T1, ? super T2, ? super T3>> preds) {
      _preds = preds;
    }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3) {
      for (Predicate3<? super T1, ? super T2, ? super T3> p : _preds) {
        if (!p.contains(arg1, arg2, arg3)) { return false; }
      }
      return true;
    }
  }

  
  public static <T1, T2, T3, T4> 
    Predicate4<T1, T2, T3, T4> and(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p1,
                                   Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p2) {
    return new AndPredicate4<T1, T2, T3, T4>(IterUtil.<Predicate4<? super T1, ? super T2, ? super T3, ? super T4>>
                                             make(p1, p2));
  }
  
  
  public static <T1, T2, T3, T4> 
    Predicate4<T1, T2, T3, T4> and(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p1,
                                   Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p2,
                                   Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p3) {
    return new AndPredicate4<T1, T2, T3, T4>(IterUtil.<Predicate4<? super T1, ? super T2, ? super T3, ? super T4>>
                                             make(p1, p2, p3));
  }
  
  
  public static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> 
    and4(Iterable<? extends Predicate4<? super T1, ? super T2, ? super T3, ? super T4>> preds) {
    return new AndPredicate4<T1, T2, T3, T4>(preds);
  }
  
  private static final class AndPredicate4<T1, T2, T3, T4> implements Predicate4<T1, T2, T3, T4>, Serializable {
    private final Iterable<? extends Predicate4<? super T1, ? super T2, ? super T3, ? super T4>> _preds;
    public AndPredicate4(Iterable<? extends Predicate4<? super T1, ? super T2, ? super T3, ? super T4>> preds) {
      _preds = preds;
    }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      for (Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p : _preds) {
        if (!p.contains(arg1, arg2, arg3, arg4)) { return false; }
      }
      return true;
    }
  }


  
  public static Condition or(Condition c1, Condition c2) {
    return new OrCondition(IterUtil.make(c1, c2));
  }
  
  
  public static Condition or(Condition c1, Condition c2, Condition c3) {
    return new OrCondition(IterUtil.make(c1, c2, c3));
  }
  
  
  public static Condition or0(Iterable<? extends Condition> conds) {
    return new OrCondition(conds);
  }
  
  private static final class OrCondition implements Condition, Serializable {
    private final Iterable<? extends Condition> _conds;
    public OrCondition(Iterable<? extends Condition> conds) { _conds = conds; }
    public boolean isTrue() {
      for (Condition c : _conds) { if (c.isTrue()) { return true; } }
      return false;
    }
  }
  
  
  public static <T> Predicate<T> or(Predicate<? super T> p1, Predicate<? super T> p2) {
    
    return new OrPredicate<T>(IterUtil.<Predicate<? super T>>make(p1, p2));
  }
  
  
  public static <T> Predicate<T> or(Predicate<? super T> p1, Predicate<? super T> p2, Predicate<? super T> p3) {
    return new OrPredicate<T>(IterUtil.<Predicate<? super T>>make(p1, p2, p3));
  }
  
  
  public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> preds) {
    return new OrPredicate<T>(preds);
  }
  
  private static final class OrPredicate<T> implements Predicate<T>, Serializable {
    private final Iterable<? extends Predicate<? super T>> _preds;
    public OrPredicate(Iterable<? extends Predicate<? super T>> preds) { _preds = preds; }
    public boolean contains(T arg) {
      for (Predicate<? super T> p : _preds) { if (p.contains(arg)) { return true; } }
      return false;
    }
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> or(Predicate2<? super T1, ? super T2> p1, 
                                               Predicate2<? super T1, ? super T2> p2) {
    return new OrPredicate2<T1, T2>(IterUtil.<Predicate2<? super T1, ? super T2>>make(p1, p2));
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> or(Predicate2<? super T1, ? super T2> p1, 
                                               Predicate2<? super T1, ? super T2> p2,
                                               Predicate2<? super T1, ? super T2> p3) {
    return new OrPredicate2<T1, T2>(IterUtil.<Predicate2<? super T1, ? super T2>>make(p1, p2, p3));
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> or2(Iterable<? extends Predicate2<? super T1, ? super T2>> preds) {
    return new OrPredicate2<T1, T2>(preds);
  }
  
  private static final class OrPredicate2<T1, T2> implements Predicate2<T1, T2>, Serializable {
    private final Iterable<? extends Predicate2<? super T1, ? super T2>> _preds;
    public OrPredicate2(Iterable<? extends Predicate2<? super T1, ? super T2>> preds) { _preds = preds; }
    public boolean contains(T1 arg1, T2 arg2) {
      for (Predicate2<? super T1, ? super T2> p : _preds) { if (p.contains(arg1, arg2)) { return true; } }
      return false;
    }
  }

  
  public static <T1, T2, T3> Predicate3<T1, T2, T3> or(Predicate3<? super T1, ? super T2, ? super T3> p1, 
                                                       Predicate3<? super T1, ? super T2, ? super T3> p2) {
    return new OrPredicate3<T1, T2, T3>(IterUtil.<Predicate3<? super T1, ? super T2, ? super T3>>
                                        make(p1, p2));
  }
  
  
  public static <T1, T2, T3> Predicate3<T1, T2, T3> or(Predicate3<? super T1, ? super T2, ? super T3> p1, 
                                                       Predicate3<? super T1, ? super T2, ? super T3> p2,
                                                       Predicate3<? super T1, ? super T2, ? super T3> p3) {
    return new OrPredicate3<T1, T2, T3>(IterUtil.<Predicate3<? super T1, ? super T2, ? super T3>>
                                        make(p1, p2, p3));
  }
  
  
  public static <T1, T2, T3> 
    Predicate3<T1, T2, T3> or3(Iterable<? extends Predicate3<? super T1, ? super T2, ? super T3>> preds) {
    return new OrPredicate3<T1, T2, T3>(preds);
  }
  
  private static final class OrPredicate3<T1, T2, T3> implements Predicate3<T1, T2, T3>, Serializable {
    private final Iterable<? extends Predicate3<? super T1, ? super T2, ? super T3>> _preds;
    public OrPredicate3(Iterable<? extends Predicate3<? super T1, ? super T2, ? super T3>> preds) {
      _preds = preds;
    }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3) {
      for (Predicate3<? super T1, ? super T2, ? super T3> p : _preds) {
        if (p.contains(arg1, arg2, arg3)) { return true; }
      }
      return false;
    }
  }

  
  public static <T1, T2, T3, T4> 
    Predicate4<T1, T2, T3, T4> or(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p1,
                                  Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p2) {
    return new OrPredicate4<T1, T2, T3, T4>(IterUtil.<Predicate4<? super T1, ? super T2, ? super T3, ? super T4>>
                                            make(p1, p2));
  }
  
  
  public static <T1, T2, T3, T4> 
    Predicate4<T1, T2, T3, T4> or(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p1,
                                  Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p2,
                                  Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p3) {
    return new OrPredicate4<T1, T2, T3, T4>(IterUtil.<Predicate4<? super T1, ? super T2, ? super T3, ? super T4>>
                                            make(p1, p2, p3));
  }
  
  
  public static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> 
    or4(Iterable<? extends Predicate4<? super T1, ? super T2, ? super T3, ? super T4>> preds) {
    return new OrPredicate4<T1, T2, T3, T4>(preds);
  }
  
  private static final class OrPredicate4<T1, T2, T3, T4> implements Predicate4<T1, T2, T3, T4>, Serializable {
    private final Iterable<? extends Predicate4<? super T1, ? super T2, ? super T3, ? super T4>> _preds;
    public OrPredicate4(Iterable<? extends Predicate4<? super T1, ? super T2, ? super T3, ? super T4>> preds) {
      _preds = preds;
    }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      for (Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p : _preds) {
        if (p.contains(arg1, arg2, arg3, arg4)) { return true; }
      }
      return false;
    }
  }

  
  public static Runnable asRunnable(Thunk<?> thunk) { return new ThunkRunnable(thunk); }
  
  private static final class ThunkRunnable implements Runnable, Serializable {
    private final Thunk<?> _t;
    public ThunkRunnable(Thunk<?> t) { _t = t; }
    public void run() { _t.value(); }
  }
  
  
  public static <T> Runnable1<T> asRunnable(Lambda<? super T, ?> lambda) {
    return new LambdaRunnable1<T>(lambda);
  }
  
  private static final class LambdaRunnable1<T> implements Runnable1<T>, Serializable {
    private final Lambda<? super T, ?> _l;
    public LambdaRunnable1(Lambda<? super T, ?> l) { _l = l; }
    public void run(T arg) { _l.value(arg); }
  }
  
  
  public static <T1, T2> Runnable2<T1, T2> asRunnable(Lambda2<? super T1, ? super T2, ?> lambda) {
    return new LambdaRunnable2<T1, T2>(lambda);
  }
  
  private static final class LambdaRunnable2<T1, T2> implements Runnable2<T1, T2>, Serializable {
    private final Lambda2<? super T1, ? super T2, ?> _l;
    public LambdaRunnable2(Lambda2<? super T1, ? super T2, ?> l) { _l = l; }
    public void run(T1 arg1, T2 arg2) { _l.value(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3> 
    Runnable3<T1, T2, T3> asRunnable(Lambda3<? super T1, ? super T2, ? super T3, ?> lambda) {
    return new LambdaRunnable3<T1, T2, T3>(lambda);
  }
  
  private static final class LambdaRunnable3<T1, T2, T3> implements Runnable3<T1, T2, T3>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ?> _l;
    public LambdaRunnable3(Lambda3<? super T1, ? super T2, ? super T3, ?> l) { _l = l; }
    public void run(T1 arg1, T2 arg2, T3 arg3) { _l.value(arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3, T4> 
    Runnable4<T1, T2, T3, T4> asRunnable(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ?> lambda) {
    return new LambdaRunnable4<T1, T2, T3, T4>(lambda);
  }
  
  private static final class LambdaRunnable4<T1, T2, T3, T4> implements Runnable4<T1, T2, T3, T4>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ?> _l;
    public LambdaRunnable4(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ?> l) { _l = l; }
    public void run(T1 arg1, T2 arg2, T3 arg3, T4 arg4) { _l.value(arg1, arg2, arg3, arg4); }
  }
  
  
  public static Thunk<Void> asThunk(Runnable r) { return new RunnableThunk<Void>(r, null); }

  
  public static <R> Thunk<R> asThunk(Runnable r, R result) { return new RunnableThunk<R>(r, result); }
  
  private static final class RunnableThunk<R> implements Thunk<R>, Serializable {
    private final Runnable _r;
    private final R _result;
    public RunnableThunk(Runnable r, R result) { _r = r; _result = result; }
    public R value() { _r.run(); return _result; }
  }
  
  
  public static <T> Lambda<T, Void> asLambda(Runnable1<? super T> r) {
    return new RunnableLambda<T, Void>(r, null);
  }
  
  
  public static <T, R> Lambda<T, R> asLambda(Runnable1<? super T> r, R result) {
    return new RunnableLambda<T, R>(r, result);
  }
  
  private static final class RunnableLambda<T, R> implements Lambda<T, R>, Serializable {
    private final Runnable1<? super T> _r;
    private final R _result;
    public RunnableLambda(Runnable1<? super T> r, R result) { _r = r; _result = result; }
    public R value(T arg) { _r.run(arg); return _result; }
  }
  
  
  public static <T1, T2> Lambda2<T1, T2, Void> asLambda(Runnable2<? super T1, ? super T2> r) {
    return new RunnableLambda2<T1, T2, Void>(r, null);
  }
  
  
  public static <T1, T2, R> Lambda2<T1, T2, R> asLambda(Runnable2<? super T1, ? super T2> r, R result) {
    return new RunnableLambda2<T1, T2, R>(r, result);
  }
  
  private static final class RunnableLambda2<T1, T2, R> implements Lambda2<T1, T2, R>, Serializable {
    private final Runnable2<? super T1, ? super T2> _r;
    private final R _result;
    public RunnableLambda2(Runnable2<? super T1, ? super T2> r, R result) { _r = r; _result = result; }
    public R value(T1 arg1, T2 arg2) { _r.run(arg1, arg2); return _result; }
  }
  
  
  public static <T1, T2, T3> 
    Lambda3<T1, T2, T3, Void> asLambda(Runnable3<? super T1, ? super T2, ? super T3> r) {
    return new RunnableLambda3<T1, T2, T3, Void>(r, null);
  }
  
  
  public static <T1, T2, T3, R> 
    Lambda3<T1, T2, T3, R> asLambda(Runnable3<? super T1, ? super T2, ? super T3> r, R result) {
    return new RunnableLambda3<T1, T2, T3, R>(r, result);
  }
  
  private static final class RunnableLambda3<T1, T2, T3, R> implements Lambda3<T1, T2, T3, R>, Serializable {
    private final Runnable3<? super T1, ? super T2, ? super T3> _r;
    private final R _result;
    public RunnableLambda3(Runnable3<? super T1, ? super T2, ? super T3> r, R result) { _r = r; _result = result; }
    public R value(T1 arg1, T2 arg2, T3 arg3) { _r.run(arg1, arg2, arg3); return _result; }
  }
  
  
  public static <T1, T2, T3, T4> Lambda4<T1, T2, T3, T4, Void> 
    asLambda(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r) {
    return new RunnableLambda4<T1, T2, T3, T4, Void>(r, null);
  }
  
  
  public static <T1, T2, T3, T4, R> Lambda4<T1, T2, T3, T4, R> 
    asLambda(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r, R result) {
    return new RunnableLambda4<T1, T2, T3, T4, R>(r, result);
  }
  
  private static final class RunnableLambda4<T1, T2, T3, T4, R> 
    implements Lambda4<T1, T2, T3, T4, R>, Serializable {
    private final Runnable4<? super T1, ? super T2, ? super T3, ? super T4> _r;
    private final R _result;
    public RunnableLambda4(Runnable4<? super T1, ? super T2, ? super T3, ? super T4> r, R result) { 
      _r = r;
      _result = result;
    }
    public R value(T1 arg1, T2 arg2, T3 arg3, T4 arg4) { _r.run(arg1, arg2, arg3, arg4); return _result; }
  }
  
  
  public static Condition asCondition(Thunk<? extends Boolean> thunk) {
    return new ThunkCondition(thunk);
  }
  
  private static final class ThunkCondition implements Condition, Serializable {
    private final Thunk<? extends Boolean> _thunk;
    public ThunkCondition(Thunk<? extends Boolean> thunk) { _thunk = thunk; }
    public boolean isTrue() { return _thunk.value(); }
  }
  
  
  public static <T> Predicate<T> asPredicate(Lambda<? super T, ? extends Boolean> lambda) {
    return new LambdaPredicate<T>(lambda);
  }
  
  private static final class LambdaPredicate<T> implements Predicate<T>, Serializable {
    private final Lambda<? super T, ? extends Boolean> _l;
    public LambdaPredicate(Lambda<? super T, ? extends Boolean> l) { _l = l; }
    public boolean contains(T arg) { return _l.value(arg); }
  }
  
  
  public static <T1, T2> Predicate2<T1, T2> asPredicate(Lambda2<? super T1, ? super T2, ? extends Boolean> lambda) {
    return new LambdaPredicate2<T1, T2>(lambda);
  }
  
  private static final class LambdaPredicate2<T1, T2> implements Predicate2<T1, T2>, Serializable {
    private final Lambda2<? super T1, ? super T2, ? extends Boolean> _l;
    public LambdaPredicate2(Lambda2<? super T1, ? super T2, ? extends Boolean> l) { _l = l; }
    public boolean contains(T1 arg1, T2 arg2) { return _l.value(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3> 
    Predicate3<T1, T2, T3> asPredicate(Lambda3<? super T1, ? super T2, ? super T3, ? extends Boolean> lambda) {
    return new LambdaPredicate3<T1, T2, T3>(lambda);
  }
  
  private static final class LambdaPredicate3<T1, T2, T3> implements Predicate3<T1, T2, T3>, Serializable {
    private final Lambda3<? super T1, ? super T2, ? super T3, ? extends Boolean> _l;
    public LambdaPredicate3(Lambda3<? super T1, ? super T2, ? super T3, ? extends Boolean> l) { _l = l; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3) { return _l.value(arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3, T4> Predicate4<T1, T2, T3, T4> 
    asPredicate(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends Boolean> lambda) {
    return new LambdaPredicate4<T1, T2, T3, T4>(lambda);
  }
 
  private static final class LambdaPredicate4<T1, T2, T3, T4> implements Predicate4<T1, T2, T3, T4>, Serializable {
    private final Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends Boolean> _l;
    public LambdaPredicate4(Lambda4<? super T1, ? super T2, ? super T3, ? super T4, ? extends Boolean> l) { _l = l; }
    public boolean contains(T1 arg1, T2 arg2, T3 arg3, T4 arg4) { return _l.value(arg1, arg2, arg3, arg4); }
  }
  
  
  
  public static Thunk<Boolean> asThunk(Condition cond) {
    return new ConditionThunk(cond);
  }
  
  private static final class ConditionThunk implements Thunk<Boolean>, Serializable {
    private final Condition _c;
    public ConditionThunk(Condition c) { _c = c; }
    public Boolean value() { return _c.isTrue(); }
  }
  
  
  public static <T> Lambda<T, Boolean> asLambda(Predicate<? super T> predicate) {
    return new PredicateLambda<T>(predicate);
  }
  
  private static final class PredicateLambda<T> implements Lambda<T, Boolean>, Serializable {
    private final Predicate<? super T> _p;
    public PredicateLambda(Predicate<? super T> p) { _p = p; }
    public Boolean value(T arg) { return _p.contains(arg); }
  }
  
  
  public static <T1, T2> Lambda2<T1, T2, Boolean> asLambda(Predicate2<? super T1, ? super T2> predicate) {
    return new PredicateLambda2<T1, T2>(predicate);
  }
  
  private static final class PredicateLambda2<T1, T2> implements Lambda2<T1, T2, Boolean>, Serializable {
    private final Predicate2<? super T1, ? super T2> _p;
    public PredicateLambda2(Predicate2<? super T1, ? super T2> p) { _p = p; }
    public Boolean value(T1 arg1, T2 arg2) { return _p.contains(arg1, arg2); }
  }
  
  
  public static <T1, T2, T3> 
    Lambda3<T1, T2, T3, Boolean> asLambda(Predicate3<? super T1, ? super T2, ? super T3> predicate) {
    return new PredicateLambda3<T1, T2, T3>(predicate);
  }
  
  private static final class PredicateLambda3<T1, T2, T3> implements Lambda3<T1, T2, T3, Boolean>, Serializable {
    private final Predicate3<? super T1, ? super T2, ? super T3> _p;
    public PredicateLambda3(Predicate3<? super T1, ? super T2, ? super T3> p) { _p = p; }
    public Boolean value(T1 arg1, T2 arg2, T3 arg3) { return _p.contains(arg1, arg2, arg3); }
  }
  
  
  public static <T1, T2, T3, T4> Lambda4<T1, T2, T3, T4, Boolean> 
    asLambda(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> predicate) {
    return new PredicateLambda4<T1, T2, T3, T4>(predicate);
  }
 
  private static final class PredicateLambda4<T1, T2, T3, T4> implements Lambda4<T1, T2, T3, T4, Boolean>, Serializable {
    private final Predicate4<? super T1, ? super T2, ? super T3, ? super T4> _p;
    public PredicateLambda4(Predicate4<? super T1, ? super T2, ? super T3, ? super T4> p) { _p = p; }
    public Boolean value(T1 arg1, T2 arg2, T3 arg3, T4 arg4) { return _p.contains(arg1, arg2, arg3, arg4); }
  }
  
}
