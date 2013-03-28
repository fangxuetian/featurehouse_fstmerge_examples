

package edu.rice.cs.util.docnavigation;


public interface INavigatorItem {
  
  public boolean checkIfClassFileInSync();
  public boolean fileExists();
  
  
  public String getName();
  public boolean isAuxiliaryFile();
  public boolean inProject();
  public boolean isUntitled();
}
