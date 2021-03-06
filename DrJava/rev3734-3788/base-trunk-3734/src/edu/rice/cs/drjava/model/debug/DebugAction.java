

package edu.rice.cs.drjava.model.debug;

import com.sun.jdi.request.*;
import java.util.Vector;


public abstract class DebugAction<T extends EventRequest> {
  public static final int ANY_LINE = -1;

  protected JPDADebugger _manager;

  

  
  protected Vector<T> _requests;
  protected int _suspendPolicy = EventRequest.SUSPEND_NONE;
  protected boolean _enabled = true;
  protected int _countFilter = -1;
  protected int _lineNumber = ANY_LINE;

  
  public DebugAction(JPDADebugger manager) {
    _manager = manager;
    _requests = new Vector<T>();
  }

  
  public Vector<T> getRequests() {
    return _requests;
  }

  
  public int getLineNumber() {
    return _lineNumber;
  }

  
  

  public boolean createRequests() throws DebugException {
    _createRequests();
    if (_requests.size() > 0) {
      _prepareRequests(_requests);
      return true;
    }
    else {
      return false;
    }
  }

  
  protected void _initializeRequests() throws DebugException {
    createRequests();
    if (_requests.size() == 0) {
      throw new DebugException("Could not create EventRequests for this action!");
    }
  }

  
  protected void _createRequests() throws DebugException { }

  
  protected void _prepareRequests(Vector<T> requests) {
    for (int i=0; i < requests.size(); i++) {
      _prepareRequest(requests.get(i));
    }
  }

  
  protected void _prepareRequest(T request) {
    
    request.setEnabled(false);

    if (_countFilter != -1) {
      request.addCountFilter(_countFilter);
    }
    request.setSuspendPolicy(_suspendPolicy);
    request.setEnabled(_enabled);

    
    request.putProperty("debugAction", this);
  }
  
  
  public boolean isEnabled() { return _enabled; }
  
  
  
  public void setEnabled(boolean enabled) {
    _enabled = enabled;
  }
}
