using System; 
using System.Diagnostics; 
using System.IO; 
using System.Threading; 
using log4net; 
using Lucene.Net.Analysis; 
using Lucene.Net.Analysis.Standard; 
using Lucene.Net.Documents; 
using Lucene.Net.Index; 
using Lucene.Net.Store; 
using NewsComponents.Collections; 
using NewsComponents.Utils; 
using RssBandit.Common.Logging; 
using Directory=System.IO.Directory; namespace  NewsComponents.Search {
	
 internal enum  IndexOperation :byte  {
  AddSingleDocument = 10,
  AddMultipleDocuments = 11,
  DeleteDocuments = 50,
  DeleteFeed = 2,
  OptimizeIndex = 1,
 } 
 internal class  PendingIndexOperation {
		
  public  IndexOperation Action;
 
  public  object[] Parameters;
 
  private  PendingIndexOperation(){;}
 
  public  PendingIndexOperation(IndexOperation action, object[] parameters){
   this.Action = action;
   this.Parameters = parameters;
  }

	}
	
 internal delegate  void  FinishedIndexOperationEventHandler (object sender, FinishedIndexOperationEventArgs e);
	
 internal class  FinishedIndexOperationEventArgs : EventArgs {
		
  public readonly  PendingIndexOperation Operation;
 
  public  FinishedIndexOperationEventArgs(PendingIndexOperation op) {
   this.Operation = op;
  }

	}
	
 internal class  LuceneIndexModifier : IDisposable {
		
  public  object SyncRoot = new Object();
 
        public  object OpenLock = new Object();
 
  public  event FinishedIndexOperationEventHandler FinishedIndexOperation; 
  private readonly  LuceneSettings settings;
 
  private  Lucene.Net.Store.Directory indexBaseDirectory;
 
  private  bool open, flushInprogress = false, threadRunning = false;
 
  private  Thread IndexModifyingThread;
 
  private readonly  PriorityQueue pendingIndexOperations = new PriorityQueue();
 
  private static readonly  ILog _log = Log.GetLogger(typeof(LuceneIndexModifier));
 
  private static readonly  LuceneInfoWriter _logHelper = new LuceneInfoWriter(_log);
 
  protected internal  IndexWriter indexWriter = null;
 
  protected internal  IndexReader indexReader = null;
 
  private  const int TimeToDelayBeforeRetry = 1000; 
        private  const int MaxSegments = 20; 
        private  const int DocsPerSegment = 50; 
  public  LuceneIndexModifier(LuceneSettings settings)
  {
   this.settings = settings;
   this.indexBaseDirectory = settings.GetIndexDirectory();
   this.Init();
   CreateIndexerThread();
  }
 
  public  LuceneIndexModifier(string baseDirectory) {
   if (!string.IsNullOrEmpty(baseDirectory)) {
    if (!Directory.Exists(baseDirectory))
     Directory.CreateDirectory(baseDirectory);
    this.indexBaseDirectory = Lucene.Net.Store.FSDirectory.GetDirectory(baseDirectory, false);
    this.Init();
   }
   CreateIndexerThread();
  }
 
  public  Lucene.Net.Store.Directory BaseDirectory
  {
   get { return indexBaseDirectory; }
   set { indexBaseDirectory = value; }
  }
 
  public  bool IndexExists {
   get { return IndexReader.IndexExists(this.BaseDirectory); }
  }
 
  public virtual  void Flush()
  {
   FlushPendingOperations(Int32.MaxValue);
   FlushIndex();
  }
 
  public virtual  void Reset() {
   ResetPendingOperations();
   ResetIndex();
  }
 
  public  void StopIndexer()
  {
   this.StopIndexerThread();
   while (this.flushInprogress)
    Thread.Sleep(50);
   this.Flush();
  }
 
  public override  string ToString()
  {
   return this.settings.ToString();
  }
 
  public virtual  void Add(Document doc, string culture) {
   lock(this.pendingIndexOperations.SyncRoot){
    this.pendingIndexOperations.Enqueue((int)IndexOperation.AddSingleDocument,
     new PendingIndexOperation(IndexOperation.AddSingleDocument, new object[]{doc, culture}));
   }
  }
 
  public virtual  void AddRange(Document[] docs, string culture)
  {
   lock (this.pendingIndexOperations.SyncRoot)
   {
    this.pendingIndexOperations.Enqueue((int)IndexOperation.AddMultipleDocuments,
     new PendingIndexOperation(IndexOperation.AddMultipleDocuments, new object[] { docs, culture }));
   }
  }
 
  public virtual  void Delete(Term term)
  {
   lock (this.pendingIndexOperations.SyncRoot)
   {
    this.pendingIndexOperations.Enqueue((int)IndexOperation.DeleteDocuments,
     new PendingIndexOperation(IndexOperation.DeleteDocuments, new object[] { term }));
   }
  }
 
  public virtual  void DeleteFeed(Term term) {
   lock (this.pendingIndexOperations.SyncRoot) {
    this.pendingIndexOperations.Enqueue((int)IndexOperation.DeleteFeed,
     new PendingIndexOperation(IndexOperation.DeleteDocuments, new object[] { term }));
   }
  }
 
  public virtual  void Optimize()
  {
   lock (this.pendingIndexOperations.SyncRoot)
   {
    this.pendingIndexOperations.Enqueue((int)IndexOperation.OptimizeIndex,
     new PendingIndexOperation(IndexOperation.OptimizeIndex, null));
   }
  }
 
  public  void CreateIndex() {
   IndexWriter writer = new IndexWriter(this.settings.GetIndexDirectory(), new StandardAnalyzer(), true);
   writer.Close();
  }
 
  public virtual  int NumberOfDocuments()
  {
   lock (SyncRoot)
   {
    AssureOpen();
    if (indexWriter != null)
    {
     return indexWriter.DocCount();
    }
    else
    {
     return indexReader.NumDocs();
    }
   }
  }
 
  public virtual  void ResetIndex()
  {
            lock (OpenLock) {
                this.Close();
                if (this.BaseDirectory is RAMDirectory) {
                } else if (this.BaseDirectory is FSDirectory &&
                    settings.IndexPath != null) {
                    Directory.Delete(settings.IndexPath, true);
                    Directory.CreateDirectory(settings.IndexPath);
                } else {
                    Debug.Assert(false, "Unhandled BaseDirectory type: " + this.BaseDirectory.GetType().FullName);
                }
            }
                this.BaseDirectory = settings.GetIndexDirectory(true);
                this.Init();
  }
 
  public virtual  void Close()
  {
   lock (this.SyncRoot)
   {
    if (!open) return;
    if (indexWriter != null)
    {
     try { indexWriter.Close(); } catch (Exception) { ;}
     indexWriter = null;
    }
    else if (indexReader != null)
    {
     try { indexReader.Close(); } catch (Exception) { ;}
     indexReader = null;
    }
    open = false;
   }
  }
 
  private  void CreateIndexerThread () {
   IndexModifyingThread = new Thread(this.ThreadRun);
   IndexModifyingThread.Name = "BanditSearchIndexModifyingThread";
   IndexModifyingThread.IsBackground = true;
   this.threadRunning = true;
   IndexModifyingThread.Start();
  }
 
  private  void ThreadRun()
  {
   while(threadRunning) {
    if (false == this.flushInprogress &&
     this.pendingIndexOperations.Count > 0)
    {
                    FlushPendingOperations(Math.Max(200, this.pendingIndexOperations.Count / 10));
                    if (threadRunning)
                        Thread.Sleep(1000 * 5);
    }else{
           Thread.Sleep(1000*30);
       }
   }
  }
 
  private  void StopIndexerThread() {
   threadRunning = false;
  }
 
  private  void PerformOperation(PendingIndexOperation current){
   try {
    switch(current.Action){
     case IndexOperation.AddSingleDocument:
      this.AddSingleDocument((Document)current.Parameters[0], (string)current.Parameters[1]);
      break;
     case IndexOperation.AddMultipleDocuments:
      this.AddMultipleDocuments((Document[])current.Parameters[0], (string)current.Parameters[1]);
      break;
     case IndexOperation.DeleteDocuments:
      this.DeleteTerm((Term)current.Parameters[0]);
      break;
     case IndexOperation.OptimizeIndex:
      this.OptimizeIndex();
      break;
     default:
      Debug.Assert(false, "Unknown index operation: " + current.Action);
      return;
    }
   }catch(FileNotFoundException fe){
    this.ResetIndex();
    _log.Error("Index is corrupted, recreating index:", fe);
   }catch(IndexOutOfRangeException ioore){
    this.ResetIndex();
    _log.Error("Index is corrupted, recreating index:", ioore);
            } catch (UnauthorizedAccessException uae) {
                _log.Error("Index files may be in use, sleeping:", uae);
                Thread.Sleep(TimeToDelayBeforeRetry);
            }
   RaiseFinishedIndexOperationEvent(current);
  }
 
  private  void FlushPendingOperations(int batchedItemsAmount)
  {
   try {
    this.flushInprogress = true;
    do {
     PendingIndexOperation pendingOp = null;
     lock (this.pendingIndexOperations.SyncRoot) {
      if (this.pendingIndexOperations.Count > 0) {
       pendingOp = this.pendingIndexOperations.Dequeue() as PendingIndexOperation;
      }
     }
     if ((pendingOp != null) && (pendingOp.Action != IndexOperation.OptimizeIndex)) {
      this.PerformOperation(pendingOp);
     }
     batchedItemsAmount--;
    } while (this.pendingIndexOperations.Count > 0 && batchedItemsAmount >= 0);
   } finally {
    this.flushInprogress = false;
   }
  }
 
  private  void ResetPendingOperations()
  {
   lock(this.pendingIndexOperations.SyncRoot){
    this.pendingIndexOperations.Clear();
   }
  }
 
  private  void RaiseFinishedIndexOperationEvent(PendingIndexOperation current) {
   if (this.FinishedIndexOperation != null)
    this.FinishedIndexOperation(this, new FinishedIndexOperationEventArgs(current));
  }
 
  private  void AddSingleDocument(Document doc, string culture)
  {
   if (doc == null) return;
   _log.DebugFormat("Adding document {0} to the index", doc.GetField(LuceneSearch.Keyword.ItemLink));
   lock (SyncRoot)
   {
    AssureOpen();
    CreateIndexWriter();
    try{
     if (!string.IsNullOrEmpty(culture))
      indexWriter.AddDocument(doc, LuceneSearch.GetAnalyzer(culture));
     else
      indexWriter.AddDocument(doc);
    }catch(IOException ioe){
     _log.Error("IOException adding document to the index", ioe);
     if(ioe.Message.IndexOf("segments.new") != -1){
      FileHelper.MoveFile(Path.Combine(this.settings.IndexPath, "segments.new"), Path.Combine(this.settings.IndexPath, "segments"), MoveFileFlag.ReplaceExisting);
     }else if(ioe.Message.IndexOf("deleteable.new") != -1){
      FileHelper.MoveFile(Path.Combine(this.settings.IndexPath, "deleteable.new"), Path.Combine(this.settings.IndexPath, "deleteable"), MoveFileFlag.ReplaceExisting);
     }
    }catch(UnauthorizedAccessException uae){
     _log.Error("Access denied error while adding document to the index", uae);
     if(uae.Message.IndexOf("segments.new") != -1){
      FileHelper.MoveFile(Path.Combine(this.settings.IndexPath, "segments.new"), Path.Combine(this.settings.IndexPath, "segments"), MoveFileFlag.ReplaceExisting);
     }else if(uae.Message.IndexOf("deleteable.new") != -1){
      FileHelper.MoveFile(Path.Combine(this.settings.IndexPath, "deleteable.new"), Path.Combine(this.settings.IndexPath, "deleteable"), MoveFileFlag.ReplaceExisting);
     }
    }
   }
  }
 
  private  void AddMultipleDocuments(Document[] docs, string culture)
  {
   if (docs == null || docs.Length == 0) return;
   Analyzer analyzer = null;
   if (!string.IsNullOrEmpty(culture))
    analyzer = LuceneSearch.GetAnalyzer(culture);
   lock (SyncRoot)
   {
    AssureOpen();
    CreateIndexWriter();
    for (int i = 0; i < docs.Length; i++)
     if (analyzer != null)
      indexWriter.AddDocument(docs[i], analyzer);
     else
      indexWriter.AddDocument(docs[i]);
   }
  }
 
  private  int DeleteTerm(Term term)
  {
   _log.DebugFormat("Deleting documents that match '{0}' from the index", term.ToString());
   lock (SyncRoot)
   {
    AssureOpen();
    CreateIndexReader();
    return indexReader.DeleteDocuments(term);
   }
  }
 
  private  void FlushIndex()
  {
   lock (SyncRoot)
   {
    AssureOpen();
    if (indexWriter != null)
    {
     indexWriter.Close();
     indexWriter = null;
     CreateIndexWriter();
    }
    else
    {
     indexReader.Close();
     indexReader = null;
     CreateIndexReader();
    }
   }
  }
 
  private  void OptimizeIndex()
  {
   lock (SyncRoot)
   {
    AssureOpen();
    CreateIndexWriter();
    indexWriter.Optimize();
   }
  }
 
  protected internal  void Init() {
   lock (this.SyncRoot) {
    this.indexWriter =new IndexWriter(this.settings.GetIndexDirectory(),
     LuceneSearch.GetAnalyzer(LuceneSearch.DefaultLanguage), !this.IndexExists);
    open = true;
   }
  }
 
  protected internal virtual  void AssureOpen() {
            lock (OpenLock) {
                if (!open) {
                    throw new InvalidOperationException("Index is closed");
                }
            }
  }
 
  protected internal virtual  void CreateIndexWriter() {
   if (this.indexWriter == null) {
    if (this.indexReader != null) {
     try { this.indexReader.Close(); }
     catch (IOException) { ;}
     catch(UnauthorizedAccessException uae){
      Thread.Sleep(TimeToDelayBeforeRetry);
      _log.Debug("Error closing index reader:", uae);
      try { this.indexReader.Close(); } catch(Exception e){_log.Debug("Error closing index writer after sleeping:", e);}
     }
     this.indexReader = null;
    }
    this.indexWriter = new IndexWriter(this.BaseDirectory,
     LuceneSearch.GetAnalyzer(LuceneSearch.DefaultLanguage), false);
    this.indexWriter.SetInfoStream( _logHelper);
                this.indexWriter.SetMergeFactor(MaxSegments);
                this.indexWriter.SetMaxBufferedDocs(DocsPerSegment);
   }
  }
 
  protected internal virtual  void CreateIndexReader() {
   if (this.indexReader == null) {
    if (this.indexWriter != null) {
                    try {
                        this.indexWriter.Close();
                    } catch (Exception e) {
                        _log.Error("Error closing index writer:", e);
                    }
                    this.indexWriter = null;
                }
    this.indexReader = IndexReader.Open(this.BaseDirectory);
   }
  }
 
  public  void Dispose() {
   Close();
  }

	}
	
 internal class  LuceneInfoWriter : TextWriter {
		
  private readonly  ILog logger = null;
 
  private  LuceneInfoWriter(){;}
 
  public override  System.Text.Encoding Encoding {
   get {
    return null;
   }
  }
 
  internal  LuceneInfoWriter(ILog logger){
   this.logger = logger;
  }
 
  public override  void Write(string value) {
   logger.Debug(value);
  }
 
  public override  void Write(string format, params object[] args) {
   logger.DebugFormat(format, args);
  }

	}

}
