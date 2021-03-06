using System; 
using System.Collections; 
using System.IO; 
using System.Net; 
using System.Runtime.InteropServices; 
using System.Text; 
using NewsComponents.Resources; 
using NewsComponents.Utils; namespace  NewsComponents.News {
	
 [ComVisible(false)] 
 public class  NntpWebRequest : WebRequest, IWebRequestCreate, IDisposable {
		
  public static  string NntpUriScheme = "nntp";
 
  public static  string NntpsUriScheme = "nntps";
 
  public static  string NewsUriScheme = "news";
 
  public static  int NntpDefaultServerPort = 119;
 
  private  Hashtable delegateTable = new Hashtable();
 
  private  int timeout = 0;
 
  private  string method = "NEWNEWS";
 
  private  int downloadCount = 500;
 
  private  ICredentials credentials = null;
 
  private  Uri requestUri = null;
 
  private  MemoryStream requestStream = new MemoryStream();
 
  private  DateTime ifModifiedSince = DateTime.Now - new TimeSpan(365, 0, 0 ,0);
 
  private  NntpWebRequest(){;}
 
  public  NntpWebRequest(Uri requestUri){
   this.requestUri = requestUri;
  }
 
  private delegate  Stream  GetRequestStreamDelegate ();
		
  private delegate  WebResponse  GetResponseDelegate (bool asyncRequest);
		
  public override  string Method {
   get {
    return method;
   }
   set {
    method = value.ToUpper();
   }
  }
 
  public  DateTime IfModifiedSince{
   get { return ifModifiedSince; }
   set { ifModifiedSince = value; }
  }
 
  public  int DownloadCount {
   get {
    return downloadCount;
   }
   set {
    if (value<=0) {
     throw new ArgumentOutOfRangeException("value");
    }
    downloadCount = value;
   }
  }
 
  public override  ICredentials Credentials {
   get {
    return credentials;
   }
   set {
    credentials = value;
   }
  }
 
  public override  Uri RequestUri {
   get {
    return this.requestUri;
   }
  }
 
  public override  int Timeout {
   get {
    return timeout;
   }
   set {
    if (value< 0) {
     throw new ArgumentOutOfRangeException("value");
    }
    timeout = value;
   }
  }
 
  private  bool CanGetRequestStream {
   get {
    if(this.method.Equals("POST")){
     return true;
    }
    return false;
   }
  }
 
         private  bool CanGetResponseStream {
             get {
     if(this.method.Equals("POST")){
      return false;
     }
     return true;
             }
         }
 
  private  NntpClient Connect(){
   NntpClient client = new NntpClient(requestUri.Host, requestUri.Port);
   if((this.credentials != null)){
    NetworkCredential nc = this.credentials as NetworkCredential;
    if(nc == null){
     throw new NntpWebException("Credentials property not an instance of NetworkCredential");
    }
    bool authOK = false;
    if(string.IsNullOrEmpty(nc.Domain)){
     authOK = client.AuthInfo(nc.UserName, nc.Password);
    }else{
     authOK = client.AuthInfo(nc.Domain + "\\" + nc.UserName, nc.Password);
    }
    if (!authOK)
     throw new NntpWebException(ComponentsText.ExceptionNntpServerAuthenticationFailed(requestUri.Host));
   }
   return client;
  }
 
  public  void Dispose(){
   try{
    requestStream.Close();
   }catch(Exception){}
   requestStream = null;
  }
 
  public override  IAsyncResult BeginGetResponse(AsyncCallback callback, object state) {
   GetResponseDelegate getResponse = new GetResponseDelegate(GetResponse);
   IAsyncResult asyncResult = getResponse.BeginInvoke(true, callback, state);
   this.delegateTable[asyncResult] = getResponse;
   return asyncResult;
  }
 
  public override  WebResponse EndGetResponse(IAsyncResult asyncResult) {
   GetResponseDelegate getResponse =
    (GetResponseDelegate) this.delegateTable[asyncResult];
   if(getResponse == null){
    throw new NntpWebException("GetRequestStreamDelegate for "
     + this.requestUri +
     "not found in delegates table of NntpWebRequest");
   }
   this.delegateTable.Remove(asyncResult);
   return getResponse.EndInvoke(asyncResult);
  }
 
  public override  IAsyncResult BeginGetRequestStream(AsyncCallback callback, object state) {
   GetRequestStreamDelegate getRequestStream = new GetRequestStreamDelegate(GetRequestStream);
   IAsyncResult asyncResult = getRequestStream.BeginInvoke(callback, state);
   this.delegateTable[asyncResult] = getRequestStream;
   return asyncResult;
  }
 
  public override  Stream EndGetRequestStream(IAsyncResult asyncResult) {
   GetRequestStreamDelegate getRequestStream =
     (GetRequestStreamDelegate) this.delegateTable[asyncResult];
   if(getRequestStream == null){
    throw new NntpWebException("GetRequestStreamDelegate for "
          + this.requestUri +
          "not found in delegates table of NntpWebRequest");
   }
   this.delegateTable.Remove(asyncResult);
   return getRequestStream.EndInvoke(asyncResult);
  }
 
  public new  WebRequest Create(Uri uri){
   if((!uri.Scheme.Equals(NntpUriScheme)) && (!uri.Scheme.Equals(NewsUriScheme))){
    throw new NotSupportedException();
   }
   return new NntpWebRequest(uri);
  }
 
  public new  WebRequest Create(string uri){
   return this.Create(new Uri(uri));
  }
 
  public override  Stream GetRequestStream(){
   if (!CanGetRequestStream){
    throw new ProtocolViolationException("Attempt to upload message when the method is not POST");
   }
             return this.requestStream;
  }
 
  public override  WebResponse GetResponse() {
   return this.GetResponse(false);
  }
 
  private  WebResponse GetResponse(bool asyncRequest) {
   NntpWebResponse response;
   MemoryStream newsgroupListStream;
   StreamWriter sw;
   using(NntpClient client = this.Connect()){
    if(!asyncRequest){
     client.Timeout = this.Timeout;
    }
    try{
     switch(this.method){
      case "POST":
       requestStream.Position = 0;
       client.Post(new UTF8Encoding().GetString(this.requestStream.ToArray()));
       response = new NntpWebResponse(NntpStatusCode.OK);
       break;
      case "LIST":
       newsgroupListStream = new MemoryStream();
       sw = new StreamWriter(newsgroupListStream);
       client.Groups(sw);
       sw.Flush();
       newsgroupListStream.Position = 0;
       response = new NntpWebResponse(NntpStatusCode.OK, newsgroupListStream);
       break;
      case "NEWNEWS":
       client.SelectGroup(requestUri.PathAndQuery.Substring(1));
       newsgroupListStream = new MemoryStream();
       sw = new StreamWriter(newsgroupListStream);
       sw.Flush();
       client.GetNntpMessages(ifModifiedSince, downloadCount, sw);
       newsgroupListStream.Position = 0;
       response = new NntpWebResponse(NntpStatusCode.OK, newsgroupListStream);
       break;
      default:
       throw new NotSupportedException(method);
     }
    }catch(Exception){
     client.Dispose();
     throw;
    }
   }
   return response;
  }
 
  public override  void Abort()
  {
  }
 
        static  void MyStreamCreator(string fileName, out Stream stream)
        {
            if( File.Exists(fileName))
            {
                Console.WriteLine("{0} already exists.", fileName);
                stream = null;
            }
            else
            {
                Console.WriteLine("Downloading {0}", fileName);
                stream = File.Create(fileName);
            }
        }

	}

}
