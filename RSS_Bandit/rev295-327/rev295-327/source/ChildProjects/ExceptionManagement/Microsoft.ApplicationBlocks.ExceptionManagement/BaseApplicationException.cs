using System; 
using System.Runtime.Serialization; 
using System.Reflection; 
using System.Configuration; 
using System.Diagnostics; 
using System.Threading; 
using System.Collections; 
using System.Text; 
using System.Security; 
using System.Security.Principal; 
using System.Security.Permissions; 
using System.Collections.Specialized; 
using System.Resources; namespace  Microsoft.ApplicationBlocks.ExceptionManagement {
	
 [Serializable] 
 public class  BaseApplicationException  : ApplicationException {
		
  public  BaseApplicationException() : base()
  {
   InitializeEnvironmentInformation();
  }
 
  public  BaseApplicationException(string message) : base(message)
  {
   InitializeEnvironmentInformation();
  }
 
  public  BaseApplicationException(string message,Exception inner) : base(message, inner)
  {
   InitializeEnvironmentInformation();
  }
 
  protected  BaseApplicationException(SerializationInfo info, StreamingContext context) : base(info, context)
  {
   machineName = info.GetString("machineName");
   createdDateTime = info.GetDateTime("createdDateTime");
   appDomainName = info.GetString("appDomainName");
   threadIdentity = info.GetString("threadIdentity");
   windowsIdentity = info.GetString("windowsIdentity");
   additionalInformation = (NameValueCollection)info.GetValue("additionalInformation",typeof(NameValueCollection));
  }
 
  private  string machineName;
 
  private  string appDomainName;
 
  private  string threadIdentity;
 
  private  string windowsIdentity;
 
  private  DateTime createdDateTime = DateTime.Now;
 
  private static  ResourceManager resourceManager = new ResourceManager(typeof(ExceptionManager).Namespace + ".ExceptionManagerText",Assembly.GetAssembly(typeof(ExceptionManager)));
 
  private  NameValueCollection additionalInformation = new NameValueCollection();
 
  [SecurityPermission(SecurityAction.Demand, SerializationFormatter = true)] 
  public override  void GetObjectData( SerializationInfo info, StreamingContext context )
  {
   info.AddValue("machineName", machineName, typeof(string));
   info.AddValue("createdDateTime", createdDateTime);
   info.AddValue("appDomainName", appDomainName, typeof(string));
   info.AddValue("threadIdentity", threadIdentity, typeof(string));
   info.AddValue("windowsIdentity", windowsIdentity, typeof(string));
   info.AddValue("additionalInformation", additionalInformation, typeof(NameValueCollection));
   base.GetObjectData(info,context);
  }
 
  public  string MachineName
  {
   get
   {
    return machineName;
   }
  }
 
  public  DateTime CreatedDateTime
  {
   get
   {
    return createdDateTime;
   }
  }
 
  public  string AppDomainName
  {
   get
   {
    return appDomainName;
   }
  }
 
  public  string ThreadIdentityName
  {
   get
   {
    return threadIdentity;
   }
  }
 
  public  string WindowsIdentityName
  {
   get
   {
    return windowsIdentity;
   }
  }
 
  public  NameValueCollection AdditionalInformation
  {
   get
   {
    return additionalInformation;
   }
  }
 
  private  void InitializeEnvironmentInformation()
  {
   try
   {
    machineName = Environment.MachineName;
   }
   catch(SecurityException)
   {
    machineName = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_PERMISSION_DENIED");
   }
   catch
   {
    machineName = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_INFOACCESS_EXCEPTION");
   }
   try
   {
    threadIdentity = Thread.CurrentPrincipal.Identity.Name;
   }
   catch(SecurityException)
   {
    threadIdentity = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_PERMISSION_DENIED");
   }
   catch
   {
    threadIdentity = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_INFOACCESS_EXCEPTION");
   }
   try
   {
    windowsIdentity = WindowsIdentity.GetCurrent().Name;
   }
   catch(SecurityException)
   {
    windowsIdentity = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_PERMISSION_DENIED");
   }
   catch
   {
    windowsIdentity = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_INFOACCESS_EXCEPTION");
   }
   try
   {
    appDomainName = AppDomain.CurrentDomain.FriendlyName;
   }
   catch(SecurityException)
   {
    appDomainName = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_PERMISSION_DENIED");
   }
   catch
   {
    appDomainName = resourceManager.GetString("RES_EXCEPTIONMANAGEMENT_INFOACCESS_EXCEPTION");
   }
  }

	}

}
