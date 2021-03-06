using System; 
using System.Collections; 
using System.ComponentModel; 
using System.ComponentModel.Design; 
using System.Windows.Forms; namespace  RssBandit {
	
 internal class  ServiceContainerBase :ApplicationContext , IServiceContainer {
		
  private  SortedList localServices = new SortedList();
 
  private  SortedList localServiceTypes = new SortedList();
 
  private  IServiceContainer parentServiceContainer;
 
  public  ServiceContainerBase():this(null) { }
 
  public  ServiceContainerBase(IServiceContainer parentContainer) {
   this.ServiceParent = parentContainer;
  }
 
  public  IServiceContainer ServiceParent {
   get {
    return parentServiceContainer;
   }
   set {
    parentServiceContainer = value;
    for( int i=0; i<localServices.Count; i++ )
     parentServiceContainer.AddService(
      (Type)localServiceTypes.GetByIndex(i),
      localServices.GetByIndex(i));
    localServices.Clear();
    localServiceTypes.Clear();
   }
  }
 
  public  void RemoveService(Type serviceType, bool promote) {
   if( localServices[serviceType.FullName] != null ) {
    localServices.Remove(serviceType.FullName);
    localServiceTypes.Remove(serviceType.FullName);
   }
   if( promote ) {
    if( parentServiceContainer != null )
     parentServiceContainer.RemoveService(serviceType);
   }
  }
 
  void System.ComponentModel.Design.IServiceContainer.RemoveService(Type serviceType) {
   RemoveService(serviceType, true);
  }
 
  void System.ComponentModel.Design.IServiceContainer.AddService(Type serviceType, System.ComponentModel.Design.ServiceCreatorCallback callback) {
   AddService(serviceType, callback, true);
  }
 
  public  void AddService(Type serviceType, System.ComponentModel.Design.ServiceCreatorCallback callback, bool promote) {
   if( promote && parentServiceContainer != null )
    parentServiceContainer.AddService(serviceType, callback, true);
   else {
    localServiceTypes[serviceType.FullName] = serviceType;
    localServices[serviceType.FullName] = callback;
   }
  }
 
  void System.ComponentModel.Design.IServiceContainer.AddService(Type serviceType, object serviceInstance) {
   if( parentServiceContainer != null )
    parentServiceContainer.AddService(serviceType, serviceInstance, true);
   else {
    localServiceTypes[serviceType.FullName] = serviceType;
    localServices[serviceType.FullName] = serviceInstance;
   }
  }
 
  void System.ComponentModel.Design.IServiceContainer.AddService(Type serviceType, object serviceInstance, bool promote) {
   if( promote && parentServiceContainer != null )
    parentServiceContainer.AddService(serviceType, serviceInstance, true);
   else {
    localServiceTypes[serviceType.FullName] = serviceType;
    localServices[serviceType.FullName] = serviceInstance;
   }
  }
 
  public  object GetService(Type serviceType) {
   if (serviceType == null)
    return null;
   if( parentServiceContainer != null )
    return parentServiceContainer.GetService(serviceType);
   object serviceInstance = localServices[serviceType.FullName];
   if( serviceInstance == null )
    return null;
   else if( serviceInstance.GetType() == typeof(ServiceCreatorCallback) ) {
    return ((ServiceCreatorCallback)serviceInstance)(this, serviceType);
   }
   return serviceInstance;
  }

	}

}
