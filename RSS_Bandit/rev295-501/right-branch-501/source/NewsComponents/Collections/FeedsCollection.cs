using System;
using System.Collections;
using System.Collections.Generic;
namespace NewsComponents.Collections {
    public sealed class GetList<T> : List<T> {
        private readonly static List<T> _empty = new List<T>(0);
        public static List<T> Empty { get { return _empty; } }
    }
 public sealed class GetArrayList:ArrayList
 {
  private static readonly ArrayList _empty = ReadOnly(new ArrayList(0));
  public static ArrayList Empty { get { return _empty; } }
 }
}
