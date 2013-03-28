

package edu.rice.cs.drjava.model.definitions.reducedmodel;


public class HighlightStatus {
  public static final int NORMAL = 0;
  public static final int COMMENTED = 1;
  public static final int SINGLE_QUOTED = 2;
  public static final int DOUBLE_QUOTED = 3;
  public static final int KEYWORD = 4;
  public static final int NUMBER = 5;
  public static final int TYPE = 6;
  private int _state;
  private int _location;
  private int _length;

  
  public HighlightStatus(int location, int length, int state) {
    _location = location;
    _length = length;
    _state = state;
  }

  
  public int getState() {
    return  _state;
  }

  
  public int getLocation() {
    return  _location;
  }

  
  public int getLength() {
    return  _length;
  }
}



