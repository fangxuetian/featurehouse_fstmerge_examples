using System.Threading;
using System.Collections;
using NewsComponents.Collections;
namespace NewsComponents.Threading {
 public class PriorityThread {
  private static int _defaultMaxWorkerThreads = 1;
  private PriorityQueue _waitingCallbacks;
  private Semaphore _workerThreadNeeded;
  private ArrayList _workerThreads;
  private int _inUseThreads;
  public PriorityThread():this(_defaultMaxWorkerThreads, ThreadPriority.Normal) {}
  public PriorityThread(int workerThreadCount, ThreadPriority priority) {
   if (workerThreadCount < _defaultMaxWorkerThreads)
    workerThreadCount = _defaultMaxWorkerThreads;
   _waitingCallbacks = new PriorityQueue();
   _workerThreads = new ArrayList(workerThreadCount);
   _inUseThreads = 0;
   _workerThreadNeeded = new Semaphore(0);
   for(int i=0; i<workerThreadCount; i++) {
    Thread newThread = new Thread(new ThreadStart(ProcessQueuedItems));
    newThread.Name = "PriorityThread #" + i.ToString();
    newThread.Priority = priority;
    _workerThreads.Add(newThread);
    newThread.IsBackground = true;
    newThread.TrySetApartmentState(ApartmentState.MTA);
    newThread.Start();
   }
  }
  public void QueueUserWorkItem(WaitCallback callback, int priority) {
   QueueUserWorkItem(callback, null, priority);
  }
  public void QueueUserWorkItem(WaitCallback callback, object state, int priority) {
   PriorityThreadPool.WaitingCallback waiting = new PriorityThreadPool.WaitingCallback(callback, state);
   lock(_waitingCallbacks.SyncRoot) { _waitingCallbacks.Enqueue(priority, waiting); }
   _workerThreadNeeded.AddOne();
  }
  public void EmptyQueue() {
   lock(_waitingCallbacks.SyncRoot) {
    try {
     foreach(object obj in _waitingCallbacks) {
      ((PriorityThreadPool.WaitingCallback)obj).Dispose();
     }
    }
    catch {
    }
    _waitingCallbacks.Clear();
    _workerThreadNeeded.Reset(0);
   }
  }
  public int ActiveThreads { get { return _inUseThreads; } }
  public int WaitingCallbacks { get { lock(_waitingCallbacks.SyncRoot) { return _waitingCallbacks.Count; } } }
  private void ProcessQueuedItems() {
   while(true) {
    PriorityThreadPool.WaitingCallback callback = null;
    while (callback == null) {
     lock(_waitingCallbacks.SyncRoot) {
      if (_waitingCallbacks.Count > 0) {
       callback = (PriorityThreadPool.WaitingCallback)_waitingCallbacks.Dequeue();
      }
     }
     if (callback == null) _workerThreadNeeded.WaitOne();
    }
    try {
     Interlocked.Increment(ref _inUseThreads);
     callback.Callback(callback.State);
    }
    catch (System.Exception e){ e.ToString();
    }
    finally {
     Interlocked.Decrement(ref _inUseThreads);
    }
   }
  }
 }
}
