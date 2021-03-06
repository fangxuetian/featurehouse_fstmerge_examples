using System;
using System.Collections;
using System.Collections.Specialized;
using NewsComponents.Feed;
using System.Collections.Generic;
namespace NewsComponents.Collections {
 public interface IcategoryCollection {
  int Count { get; }
  bool IsSynchronized { get; }
  object SyncRoot { get; }
  void CopyTo(category[] array, int arrayIndex);
  IcategoryEnumerator GetEnumerator();
 }
 public interface
  IcategoryList: IcategoryCollection {
  bool IsFixedSize { get; }
  bool IsReadOnly { get; }
  category this[int index] { get; set; }
  int Add(category value);
  void Clear();
  bool Contains(category value);
  int IndexOf(category value);
  void Insert(int index, category value);
  void Remove(category value);
  void RemoveAt(int index);
 }
 public interface IcategoryEnumerator {
  category Current { get; }
  bool MoveNext();
  void Reset();
 }
 public interface IStringcategoryCollection {
  int Count { get; }
  bool IsSynchronized { get; }
  object SyncRoot { get; }
  void CopyTo(CategoryEntry[] array, int arrayIndex);
  IStringcategoryEnumerator GetEnumerator();
 }
 public interface
  IStringcategoryDictionary: IStringcategoryCollection {
  bool IsFixedSize { get; }
  bool IsReadOnly { get; }
  category this[String key] { get; set; }
  ICollection<string> Keys { get; }
  IcategoryCollection Values { get; }
  void Add(String key, category value);
  void Clear();
  bool Contains(String key);
  void Remove(String key);
 }
 public interface
  IStringcategoryList: IStringcategoryCollection {
  bool IsFixedSize { get; }
  bool IsReadOnly { get; }
  CategoryEntry this[int index] { get; set; }
  int Add(CategoryEntry entry);
  void Clear();
  bool Contains(CategoryEntry entry);
  int IndexOf(CategoryEntry entry);
  void Insert(int index, CategoryEntry entry);
  void Remove(CategoryEntry entry);
  void RemoveAt(int index);
 }
 public interface IStringcategoryEnumerator {
  CategoryEntry Current { get; }
  CategoryEntry Entry { get; }
  String Key { get; }
  category Value { get; }
  bool MoveNext();
  void Reset();
 }
 [Serializable]
 public struct CategoryEntry {
  private String _key;
  private category _value;
  public CategoryEntry(String key, category value) {
   if ((object) key == null)
    throw new ArgumentNullException("key");
   this._key = key;
   this._value = value;
  }
  public String Key {
   get { return this._key; }
   set {
    if ((object) value == null)
     throw new ArgumentNullException("value");
    this._key = value;
   }
  }
  public category Value {
   get { return this._value; }
   set { this._value = value; }
  }
  public static implicit operator CategoryEntry(DictionaryEntry entry) {
   CategoryEntry pair = new CategoryEntry();
   if (entry.Key != null) pair.Key = (String) entry.Key;
   if (entry.Value != null) pair.Value = (category) entry.Value;
   return pair;
  }
  public static implicit operator DictionaryEntry(CategoryEntry pair) {
   DictionaryEntry entry = new DictionaryEntry();
   if (pair.Key != null) entry.Key = pair.Key;
   entry.Value = pair.Value;
   return entry;
  }
 }
 [Serializable]
 public class CategoriesCollection:
  IStringcategoryList, IList, ICloneable {
  private const int _defaultCapacity = 16;
  private String[] _keys;
  private category[] _values;
  private int _count;
  [NonSerialized]
  private int _version;
  private KeyList _keyList;
  private ValueList _valueList;
  private enum Tag { Default }
  private CategoriesCollection(Tag tag) { }
  public CategoriesCollection() {
   this._keys = new String[_defaultCapacity];
   this._values = new category[_defaultCapacity];
  }
  public CategoriesCollection(int capacity) {
   if (capacity < 0)
    throw new ArgumentOutOfRangeException("capacity",
     capacity, "Argument cannot be negative.");
   this._keys = new String[capacity];
   this._values = new category[capacity];
  }
  public CategoriesCollection(CategoriesCollection collection) {
   if (collection == null)
    throw new ArgumentNullException("collection");
   this._keys = new String[collection.Count];
   this._values = new category[collection.Count];
   AddRange(collection);
  }
  public CategoriesCollection(CategoryEntry[] array) {
   if (array == null)
    throw new ArgumentNullException("array");
   this._keys = new String[array.Length];
   this._values = new category[array.Length];
   AddRange(array);
  }
  protected virtual String[] InnerKeys {
   get { return this._keys; }
  }
  protected virtual category[] InnerValues {
   get { return this._values; }
  }
  public virtual int Capacity {
   get { return this._keys.Length; }
   set {
    if (value == this._keys.Length) return;
    if (value < this._count)
     throw new ArgumentOutOfRangeException("Capacity",
      value, "Value cannot be less than Count.");
    if (value == 0) {
     this._keys = new String[_defaultCapacity];
     this._values = new category[_defaultCapacity];
     return;
    }
    String[] newKeys = new String[value];
    category[] newValues = new category[value];
    Array.Copy(this._keys, 0, newKeys, 0, this._count);
    Array.Copy(this._values, 0, newValues, 0, this._count);
    this._keys = newKeys;
    this._values = newValues;
   }
  }
  public virtual int Count {
   get { return this._count; }
  }
  public virtual bool IsFixedSize {
   get { return false; }
  }
  public virtual bool IsReadOnly {
   get { return false; }
  }
  public virtual bool IsSynchronized {
   get { return false; }
  }
  public virtual CategoryEntry this[int index] {
   get {
    ValidateIndex(index);
    return new CategoryEntry(this._keys[index], this._values[index]);
   }
   set {
    ValidateIndex(index);
    ++this._version;
    this._keys[index] = value.Key;
    this._values[index] = value.Value;
   }
  }
  object IList.this[int index] {
   get { return (DictionaryEntry) this[index]; }
   set { this[index] = (CategoryEntry) (DictionaryEntry) value; }
  }
  public virtual ICollection<string> Keys {
   get { return GetKeyList(); }
  }
  public virtual object SyncRoot {
   get { return this; }
  }
  public virtual IcategoryCollection Values {
   get { return GetValueList(); }
  }
  private static category CreateCategory(string categoryName) {
   category c = new category();
   c.Value = categoryName;
   return c;
  }
  public virtual int Add(CategoryEntry entry) {
   if (this._count == this._keys.Length)
    EnsureCapacity(this._count + 1);
   ++this._version;
   this._keys[this._count] = entry.Key;
   this._values[this._count] = entry.Value;
   return this._count++;
  }
  public int Add(String key, category value) {
   if ((object) key == null)
    throw new ArgumentNullException("key");
   value.parent = this.GetParentCategory(key);
   foreach(category c in this.GetChildCategories(key)){
    c.parent = value;
   }
   return Add(new CategoryEntry(key, value));
  }
  public int Add(String key) {
   if ((object) key == null)
    throw new ArgumentNullException("key");
   if(this.ContainsKey(key)){
    return this.IndexOfKey(key);
   }
   StringCollection ancestors = this.GetAncestors(key);
   for(int i = ancestors.Count; i-->0;){
      category c = this.GetByKey(ancestors[i]);
    if(c == null){
     this.Add(new CategoryEntry(ancestors[i], CreateCategory(ancestors[i])));
    }
   }
   category newCategory = CreateCategory(key);
   newCategory.parent = ( ancestors.Count == 0 ? null : this.GetByKey(ancestors[ancestors.Count - 1]) );
   return Add(new CategoryEntry(key, newCategory));
  }
  public int Add(category value) {
   if ((object) value == null)
    throw new ArgumentNullException("value");
   if(this.ContainsKey(value.Value)){
    return this.IndexOfKey(value.Value);
   }
   return Add(new CategoryEntry(value.Value, value));
  }
  int IList.Add(object entry) {
   return Add((CategoryEntry) entry);
  }
  public virtual void AddRange(CategoriesCollection collection) {
   if (collection == null)
    throw new ArgumentNullException("collection");
   if (collection.Count == 0) return;
   if (this._count + collection.Count > this._keys.Length)
    EnsureCapacity(this._count + collection.Count);
   ++this._version;
   Array.Copy(collection.InnerKeys, 0, this._keys, this._count, collection.Count);
   Array.Copy(collection.InnerValues, 0, this._values, this._count, collection.Count);
   this._count += collection.Count;
  }
  public virtual void AddRange(CategoryEntry[] array) {
   if (array == null)
    throw new ArgumentNullException("array");
   if (array.Length == 0) return;
   if (this._count + array.Length > this._keys.Length)
    EnsureCapacity(this._count + array.Length);
   ++this._version;
   for (int i = 0; i < array.Length; ++i, ++this._count) {
    this._keys[this._count] = array[i].Key;
    this._values[this._count] = array[i].Value;
   }
  }
  public virtual void Clear() {
   if (this._count == 0) return;
   ++this._version;
   Array.Clear(this._keys, 0, this._count);
   Array.Clear(this._values, 0, this._count);
   this._count = 0;
  }
  public virtual object Clone() {
   CategoriesCollection collection = new CategoriesCollection(this._count);
   Array.Copy(this._keys, 0, collection._keys, 0, this._count);
   Array.Copy(this._values, 0, collection._values, 0, this._count);
   collection._count = this._count;
   collection._version = this._version;
   return collection;
  }
  public virtual bool Contains(CategoryEntry entry) {
   return (IndexOf(entry) >= 0);
  }
  bool IList.Contains(object entry) {
   return Contains((CategoryEntry) entry);
  }
  public virtual bool ContainsKey(String key) {
   return (IndexOfKey(key) >= 0);
  }
  public virtual bool ContainsValue(category value) {
   return (IndexOfValue(value) >= 0);
  }
  public virtual void CopyTo(CategoryEntry[] array, int arrayIndex) {
   CheckTargetArray(array, arrayIndex);
   for (int i = 0; i < this._count; i++) {
    CategoryEntry entry =
     new CategoryEntry(this._keys[i], this._values[i]);
    array.SetValue(entry, arrayIndex + i);
   }
  }
  void ICollection.CopyTo(Array array, int arrayIndex) {
   CopyTo((CategoryEntry[]) array, arrayIndex);
  }
  public virtual bool Equals(CategoriesCollection collection) {
   if (collection == null || this._count != collection.Count)
    return false;
   for (int i = 0; i < this._count; i++)
    if (this._keys[i] != collection.InnerKeys[i] ||
     this._values[i] != collection.InnerValues[i])
     return false;
   return true;
  }
  public virtual category GetByIndex(int index) {
   ValidateIndex(index);
   return this._values[index];
  }
  public virtual category GetByKey(String key) {
   int index = IndexOfKey(key);
   if (index >= 0) return this._values[index];
   return null;
  }
  public virtual IStringcategoryEnumerator GetEnumerator() {
   return new Enumerator(this);
  }
  IEnumerator IEnumerable.GetEnumerator() {
   return (IEnumerator) GetEnumerator();
  }
  public virtual String GetKey(int index) {
   ValidateIndex(index);
   return this._keys[index];
  }
  public virtual ICollection<string> GetKeyList() {
   if (this._keyList == null)
    this._keyList = new KeyList(this);
   return this._keyList;
  }
  public virtual IcategoryList GetValueList() {
   if (this._valueList == null)
    this._valueList = new ValueList(this);
   return this._valueList;
  }
  public virtual int IndexOf(CategoryEntry entry) {
   for (int i = 0; i < this._count; ++i)
    if (entry.Key == this._keys[i] &&
     entry.Value == this._values[i])
     return i;
   return -1;
  }
  int IList.IndexOf(object entry) {
   return IndexOf((CategoryEntry) entry);
  }
  public virtual int IndexOfKey(String key) {
   if ((object) key == null)
    throw new ArgumentNullException("key");
   return Array.IndexOf(this._keys, key, 0, this._count);
  }
  public virtual int IndexOfValue(category value) {
   return Array.IndexOf(this._values, value, 0, this._count);
  }
  public virtual void Insert(int index, CategoryEntry entry) {
   if (index < 0)
    throw new ArgumentOutOfRangeException("index",
     index, "Argument cannot be negative.");
   if (index > this._count)
    throw new ArgumentOutOfRangeException("index",
     index, "Argument cannot exceed Count.");
   if (this._count == this._keys.Length)
    EnsureCapacity(this._count + 1);
   ++this._version;
   if (index < this._count) {
    Array.Copy(this._keys, index,
     this._keys, index + 1, this._count - index);
    Array.Copy(this._values, index,
     this._values, index + 1, this._count - index);
   }
   this._keys[index] = entry.Key;
   this._values[index] = entry.Value;
   ++this._count;
  }
  void IList.Insert(int index, object entry) {
   Insert(index, (CategoryEntry) entry);
  }
  public virtual void Remove(string key) {
   int index = IndexOfKey(key);
   if (index >= 0) RemoveAt(index);
  }
  public virtual void Remove(CategoryEntry entry) {
   int index = IndexOf(entry);
   if (index >= 0) RemoveAt(index);
  }
  void IList.Remove(object entry) {
   Remove((CategoryEntry) entry);
  }
  public virtual void RemoveAt(int index) {
   ValidateIndex(index);
   ++this._version;
   if (index < --this._count) {
    Array.Copy(this._keys, index + 1,
     this._keys, index, this._count - index);
    Array.Copy(this._values, index + 1,
     this._values, index, this._count - index);
   }
   this._keys[this._count] = null;
   this._values[this._count] = null;
  }
  public virtual void SetByIndex(int index, category value) {
   ValidateIndex(index);
   ++this._version;
   this._values[index] = value;
  }
  public virtual int SetByKey(String key, category value) {
   int index = IndexOfKey(key);
   if (index >= 0) {
    this._values[index] = value;
    return index;
   }
   return Add(key, value);
  }
  public static CategoriesCollection Synchronized(CategoriesCollection collection) {
   if (collection == null)
    throw new ArgumentNullException("collection");
   return new SyncList(collection);
  }
  public virtual CategoryEntry[] ToArray() {
   CategoryEntry[] array = new CategoryEntry[this._count];
   CopyTo(array, 0);
   return array;
  }
  public virtual void TrimToSize() {
   Capacity = this._count;
  }
  private category GetParentCategory(string key){
   int index = key.LastIndexOf(NewsHandler.CategorySeparator);
   if(index != -1){
    string parentName = key.Substring(0, index);
    return this.GetByKey(parentName);
   }else{
    return null;
   }
  }
  private ArrayList GetChildCategories(string key){
   ArrayList list = new ArrayList();
   foreach(category c in this.Values){
    if(c.Value.StartsWith(key)){
     list.Add(c);
    }
   }
   return list;
  }
  private StringCollection GetAncestors(string key){
   StringCollection list = new StringCollection();
   string current = String.Empty;
   string[] s = key.Split(NewsHandler.CategorySeparator.ToCharArray());
   if(s.Length != 1){
    for(int i = 0; i < (s.Length -1) ; i++){
     current += (i == 0 ? s[i] : NewsHandler.CategorySeparator + s[i]);
     list.Add(current);
    }
   }
   return list;
  }
  private void CheckEnumIndex(int index) {
   if (index < 0 || index >= this._count)
    throw new InvalidOperationException(
     "Enumerator is not on a collection element.");
  }
  private void CheckEnumVersion(int version) {
   if (version != this._version)
    throw new InvalidOperationException(
     "Enumerator invalidated by modification to collection.");
  }
  private void CheckTargetArray(Array array, int arrayIndex) {
   if (array == null)
    throw new ArgumentNullException("array");
   if (array.Rank > 1)
    throw new ArgumentException(
     "Argument cannot be multidimensional.", "array");
   if (arrayIndex < 0)
    throw new ArgumentOutOfRangeException("arrayIndex",
     arrayIndex, "Argument cannot be negative.");
   if (arrayIndex >= array.Length)
    throw new ArgumentException(
     "Argument must be less than array length.", "arrayIndex");
   if (this._count > array.Length - arrayIndex)
    throw new ArgumentException(
     "Argument section must be large enough for collection.", "array");
  }
  private void EnsureCapacity(int minimum) {
   int newCapacity = (this._keys.Length == 0 ?
   _defaultCapacity : this._keys.Length * 2);
   if (newCapacity < minimum) newCapacity = minimum;
   Capacity = newCapacity;
  }
  private void ValidateIndex(int index) {
   if (index < 0)
    throw new ArgumentOutOfRangeException("index",
     index, "Argument cannot be negative.");
   if (index >= this._count)
    throw new ArgumentOutOfRangeException("index",
     index, "Argument must be less than Count.");
  }
  [Serializable]
   private sealed class Enumerator:
   IStringcategoryEnumerator, IDictionaryEnumerator {
   private readonly CategoriesCollection _collection;
   private readonly int _version;
   private int _index;
   internal Enumerator(CategoriesCollection collection) {
    this._collection = collection;
    this._version = collection._version;
    this._index = -1;
   }
   public CategoryEntry Current {
    get { return Entry; }
   }
   object IEnumerator.Current {
    get { return (DictionaryEntry) Entry; }
   }
   public CategoryEntry Entry {
    get {
     this._collection.CheckEnumIndex(this._index);
     this._collection.CheckEnumVersion(this._version);
     return new CategoryEntry(
      this._collection._keys[this._index],
      this._collection._values[this._index]);
    }
   }
   DictionaryEntry IDictionaryEnumerator.Entry {
    get { return Entry; }
   }
   public String Key {
    get {
     this._collection.CheckEnumIndex(this._index);
     this._collection.CheckEnumVersion(this._version);
     return this._collection._keys[this._index];
    }
   }
   object IDictionaryEnumerator.Key {
    get { return Key; }
   }
   public category Value {
    get {
     this._collection.CheckEnumIndex(this._index);
     this._collection.CheckEnumVersion(this._version);
     return this._collection._values[this._index];
    }
   }
   object IDictionaryEnumerator.Value {
    get { return Value; }
   }
   public bool MoveNext() {
    this._collection.CheckEnumVersion(this._version);
    return (++this._index < this._collection.Count);
   }
   public void Reset() {
    this._collection.CheckEnumVersion(this._version);
    this._index = -1;
   }
  }
  [Serializable]
   private sealed class KeyList: IList<string> {
   private CategoriesCollection _collection;
   internal KeyList(CategoriesCollection collection) {
    this._collection = collection;
   }
   public int Count {
    get { return this._collection.Count; }
   }
   public bool IsReadOnly {
    get { return true; }
   }
   public bool IsFixedSize {
    get { return true; }
   }
   public bool IsSynchronized {
    get { return this._collection.IsSynchronized; }
   }
   public String this[int index] {
    get { return this._collection.GetKey(index); }
    set { throw new NotSupportedException(
        "Read-only collections cannot be modified."); }
   }
   public object SyncRoot {
    get { return this._collection.SyncRoot; }
   }
   public void Add(String key) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public void Clear() {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public bool Contains(String key) {
    return this._collection.ContainsKey(key);
   }
   public void CopyTo(String[] array, int arrayIndex) {
    this._collection.CheckTargetArray(array, arrayIndex);
    Array.Copy(this._collection._keys, 0,
     array, arrayIndex, this._collection.Count);
   }
   public IEnumerator<string> GetEnumerator() {
                return new List<string>(this._collection._keys).GetEnumerator();
   }
   IEnumerator IEnumerable.GetEnumerator() {
    return (IEnumerator) GetEnumerator();
   }
   public int IndexOf(String key) {
    return this._collection.IndexOfKey(key);
   }
   public void Insert(int index, String key) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public bool Remove(String key) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public void RemoveAt(int index) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
  }
  [Serializable]
   private sealed class ValueList: IcategoryList, IList {
   private CategoriesCollection _collection;
   internal ValueList(CategoriesCollection collection) {
    this._collection = collection;
   }
   public int Count {
    get { return this._collection.Count; }
   }
   public bool IsReadOnly {
    get { return true; }
   }
   public bool IsFixedSize {
    get { return true; }
   }
   public bool IsSynchronized {
    get { return this._collection.IsSynchronized; }
   }
   public category this[int index] {
    get { return this._collection.GetByIndex(index); }
    set { throw new NotSupportedException(
        "Read-only collections cannot be modified."); }
   }
   object IList.this[int index] {
    get { return this[index]; }
    set { throw new NotSupportedException(
        "Read-only collections cannot be modified."); }
   }
   public object SyncRoot {
    get { return this._collection.SyncRoot; }
   }
   public int Add(category value) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   int IList.Add(object value) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public void Clear() {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public bool Contains(category value) {
    return this._collection.ContainsValue(value);
   }
   bool IList.Contains(object value) {
    return Contains((category) value);
   }
   public void CopyTo(category[] array, int arrayIndex) {
    this._collection.CheckTargetArray(array, arrayIndex);
    Array.Copy(this._collection._values, 0,
     array, arrayIndex, this._collection.Count);
   }
   void ICollection.CopyTo(Array array, int arrayIndex) {
    this._collection.CheckTargetArray(array, arrayIndex);
    CopyTo((category[]) array, arrayIndex);
   }
   public IcategoryEnumerator GetEnumerator() {
    return new ValueEnumerator(this._collection);
   }
   IEnumerator IEnumerable.GetEnumerator() {
    return (IEnumerator) GetEnumerator();
   }
   public int IndexOf(category value) {
    return this._collection.IndexOfValue(value);
   }
   int IList.IndexOf(object value) {
    return IndexOf((category) value);
   }
   public void Insert(int index, category value) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   void IList.Insert(int index, object value) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public void Remove(category value) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   void IList.Remove(object value) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
   public void RemoveAt(int index) {
    throw new NotSupportedException(
     "Read-only collections cannot be modified.");
   }
  }
  [Serializable]
   private sealed class ValueEnumerator:
   IcategoryEnumerator, IEnumerator {
   private readonly CategoriesCollection _collection;
   private readonly int _version;
   private int _index;
   internal ValueEnumerator(CategoriesCollection collection) {
    this._collection = collection;
    this._version = collection._version;
    this._index = -1;
   }
   public category Current {
    get {
     this._collection.CheckEnumIndex(this._index);
     this._collection.CheckEnumVersion(this._version);
     return this._collection._values[this._index];
    }
   }
   object IEnumerator.Current {
    get { return Current; }
   }
   public bool MoveNext() {
    this._collection.CheckEnumVersion(this._version);
    return (++this._index < this._collection.Count);
   }
   public void Reset() {
    this._collection.CheckEnumVersion(this._version);
    this._index = -1;
   }
  }
  [Serializable]
   private sealed class SyncList: CategoriesCollection {
   private CategoriesCollection _collection;
   private object _root;
   internal SyncList(CategoriesCollection collection):
    base(Tag.Default) {
    this._collection = collection;
    this._root = collection.SyncRoot;
   }
   protected override String[] InnerKeys {
    get { lock (this._root) return this._collection.InnerKeys; }
   }
   protected override category[] InnerValues {
    get { lock (this._root) return this._collection.InnerValues; }
   }
   public override int Capacity {
    get { lock (this._root) return this._collection.Capacity; }
   }
   public override int Count {
    get { lock (this._root) return this._collection.Count; }
   }
   public override bool IsFixedSize {
    get { return this._collection.IsFixedSize; }
   }
   public override bool IsReadOnly {
    get { return this._collection.IsReadOnly; }
   }
   public override bool IsSynchronized {
    get { return true; }
   }
   public override CategoryEntry this[int index] {
    get { lock (this._root) return this._collection[index]; }
    set { lock (this._root) this._collection[index] = value; }
   }
   public override ICollection<string> Keys {
    get { lock (this._root) return this._collection.Keys; }
   }
   public override object SyncRoot {
    get { return this._root; }
   }
   public override IcategoryCollection Values {
    get { lock (this._root) return this._collection.Values; }
   }
   public override int Add(CategoryEntry entry) {
    lock (this._root) return this._collection.Add(entry);
   }
   public override void AddRange(CategoriesCollection collection) {
    lock (this._root) this._collection.AddRange(collection);
   }
   public override void AddRange(CategoryEntry[] array) {
    lock (this._root) this._collection.AddRange(array);
   }
   public override void Clear() {
    lock (this._root) this._collection.Clear();
   }
   public override object Clone() {
    lock (this._root) return this._collection.Clone();
   }
   public override bool Contains(CategoryEntry entry) {
    lock (this._root) return this._collection.Contains(entry);
   }
   public override bool ContainsKey(String key) {
    lock (this._root) return this._collection.ContainsKey(key);
   }
   public override bool ContainsValue(category value) {
    lock (this._root) return this._collection.ContainsValue(value);
   }
   public override void CopyTo(CategoryEntry[] array, int index) {
    lock (this._root) this._collection.CopyTo(array, index);
   }
   public override bool Equals(CategoriesCollection collection) {
    lock (this._root) return this._collection.Equals(collection);
   }
   public override category GetByIndex(int index) {
    lock (this._root) return this._collection.GetByIndex(index);
   }
   public override category GetByKey(String key) {
    lock (this._root) return this._collection.GetByKey(key);
   }
   public override IStringcategoryEnumerator GetEnumerator() {
    lock (this._root) return this._collection.GetEnumerator();
   }
   public override String GetKey(int index) {
    lock (this._root) return this._collection.GetKey(index);
   }
   public override ICollection<string> GetKeyList() {
    lock (this._root) return this._collection.GetKeyList();
   }
   public override IcategoryList GetValueList() {
    lock (this._root) return this._collection.GetValueList();
   }
   public override int IndexOf(CategoryEntry entry) {
    lock (this._root) return this._collection.IndexOf(entry);
   }
   public override int IndexOfKey(String key) {
    lock (this._root) return this._collection.IndexOfKey(key);
   }
   public override int IndexOfValue(category value) {
    lock (this._root) return this._collection.IndexOfValue(value);
   }
   public override void Insert(int index, CategoryEntry entry) {
    lock (this._root) this._collection.Insert(index, entry);
   }
   public override void Remove(CategoryEntry entry) {
    lock (this._root) this._collection.Remove(entry);
   }
   public override void RemoveAt(int index) {
    lock (this._root) this._collection.RemoveAt(index);
   }
   public override void SetByIndex(int index, category value) {
    lock (this._root) this._collection.SetByIndex(index, value);
   }
   public override int SetByKey(String key, category value) {
    lock (this._root) return this._collection.SetByKey(key, value);
   }
   public override CategoryEntry[] ToArray() {
    lock (this._root) return this._collection.ToArray();
   }
   public override void TrimToSize() {
    lock (this._root) this._collection.TrimToSize();
   }
  }
 }
}
