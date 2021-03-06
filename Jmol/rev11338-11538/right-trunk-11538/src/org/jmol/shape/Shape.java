

package org.jmol.shape;

import org.jmol.util.Escape;
import org.jmol.util.Logger;
import org.jmol.util.Point3fi;
import org.jmol.viewer.JmolConstants;
import org.jmol.viewer.StateManager;
import org.jmol.viewer.Viewer;
import org.jmol.g3d.*;
import org.jmol.modelset.Atom;
import org.jmol.modelset.Bond;
import org.jmol.modelset.Group;
import org.jmol.modelset.ModelSet;

import javax.vecmath.Point3f;
import javax.vecmath.Point3i;

import java.util.BitSet;
import java.util.Vector;
import java.util.Hashtable;


public abstract class Shape {

  
  
  
  
  
  
  
  
  public Viewer viewer; 
  public ModelSet modelSet;
  public Graphics3D g3d;
  public int shapeID;
  public int myVisibilityFlag;
  protected float translucentLevel;
  protected boolean translucentAllowed = true;
  public boolean isBioShape;
  
  public Viewer getViewer() {
    return viewer;
  }
  
  final public void initializeShape(Viewer viewer, Graphics3D g3d, ModelSet modelSet,
                               int shapeID) {
    this.viewer = viewer;
    this.g3d = g3d;
    this.shapeID = shapeID;
    this.myVisibilityFlag = JmolConstants.getShapeVisibilityFlag(shapeID);
    setModelSet(modelSet);
    initShape();
    

  }

  public void setModelSet(ModelSet modelSet) {
    this.modelSet = modelSet;
    initModelSet();
  }
  
  protected void initModelSet() {
  }

  public void initShape() {
  }

  public void setSize(int size, BitSet bsSelected) {
  }

  public void setSize(int flags, float size, BitSet bsSelected) {
    
  }

  public void setProperty(String propertyName, Object value, BitSet bsSelected) {
    if (propertyName == "translucentLevel") {
      translucentLevel = ((Float) value).floatValue();
      return;
    }

    if (propertyName == "refreshTrajectories") {
      return;
    }

    if (propertyName == "deleteModelAtoms") {
      
      
    }
    
    Logger.warn("unassigned " + JmolConstants.shapeClassBases[shapeID] + " + shape setProperty:" + propertyName + ":" + value);
  }

  public boolean getProperty(String property, Object[] data) {
    return false;
  }

  public Object getProperty(String property, int index) {
    return null;
  }

  public int getIndexFromName(String thisID) {
    return -1;
  }

  public boolean wasClicked(int x, int y) {
    return false;
  }

  public void findNearestAtomIndex(int xMouse, int yMouse, Atom[] closest) {
  }

  public void checkBoundsMinMax(Point3f pointMin, Point3f pointMax) {
  }

  public void setModelClickability() {
  }

  public Point3fi checkObjectClicked(int x, int y, int modifiers, BitSet bsVisible) {
    return null;
  }

  public boolean checkObjectHovered(int x, int y, BitSet bsVisible) {
    return false;
  }

  public boolean checkObjectDragged(int prevX, int prevY, int x, int y,
                             int modifiers, BitSet bsVisible) {
    return false;
  }

  protected int coordinateInRange(int x, int y, Point3f vertex, int dmin2, Point3i ptXY) {
    viewer.transformPoint(vertex, ptXY);
    int d2 = (x - ptXY.x) * (x - ptXY.x) + (y - ptXY.y) * (y - ptXY.y);
    return (d2 < dmin2 ? d2 : -1);
  }
  
  public short setColix(short colix, byte paletteID, int atomIndex) {
    return setColix(colix, paletteID, modelSet.getAtomAt(atomIndex));
  }

  protected short setColix(short colix, byte paletteID, Atom atom) {
    return (colix == Graphics3D.USE_PALETTE ? viewer.getColixAtomPalette(atom,
        paletteID) : colix);
  }

  protected short setColix(short colix, byte paletteID, Bond bond) {
    return (colix == Graphics3D.USE_PALETTE ? viewer.getColixBondPalette(bond,
        paletteID) : colix);
  }

  protected void remapColors() {
    
  }
  
  public Vector getShapeDetail() {
    return null;
  }

  public String getShapeState() {
    return null;
  }

  public void setVisibilityFlags(BitSet bs) {
  }

  static public void setStateInfo(Hashtable ht, int i, String key) {
    setStateInfo(ht, i, i, key);
  }

  static public void setStateInfo(Hashtable ht, int i1, int i2, String key) {
    StateManager.setStateInfo(ht, i1, i2, key);
  }

  static public String getShapeCommands(Hashtable htDefine, Hashtable htMore,
                                 int atomCount) {
    return StateManager.getCommands(htDefine, htMore, atomCount);
  }

  static public String getShapeCommands(Hashtable htDefine, Hashtable htMore,
                                 int count, String selectCmd) {
    return StateManager.getCommands(htDefine, htMore, count, selectCmd);
  }

  static public void appendCmd(StringBuffer s, String cmd) {
    StateManager.appendCmd(s, cmd);
  }

  static public String getFontCommand(String type, Font3D font) {
    if (font == null)
      return "";
    return "font " + type + " " + font.fontSizeNominal + " " + font.fontFace + " "
        + font.fontStyle;
  }

  public String getColorCommand(String type, short colix) {
    return getColorCommand(type, JmolConstants.PALETTE_UNKNOWN, colix);
  }

  public String getColorCommand(String type, byte pid, short colix) {
    if (pid == JmolConstants.PALETTE_UNKNOWN && colix == Graphics3D.INHERIT_ALL)
      return "";
    return "color " + type + " " + encodeTransColor(pid, colix, translucentAllowed);
  }

  private String encodeTransColor(byte pid, short colix,
                                  boolean translucentAllowed) {
    if (pid == JmolConstants.PALETTE_UNKNOWN && colix == Graphics3D.INHERIT_ALL)
      return "";
    
    return (translucentAllowed ? getTranslucentLabel(colix) + " " : "")
        + (pid != JmolConstants.PALETTE_UNKNOWN 
        && !JmolConstants.isPaletteVariable(pid) 
        ? JmolConstants.getPaletteName(pid) : encodeColor(colix));
  }

  String encodeColor(short colix) {
    
    return (Graphics3D.isColixColorInherited(colix) ? "none" : Escape
        .escapeColor(g3d.getColixArgb(colix)));
  }

  private static String getTranslucentLabel(short colix) {
    return (Graphics3D.isColixTranslucent(colix) ? "translucent "
        + Graphics3D.getColixTranslucencyLevel(colix): "opaque");
  }

  public static short getColix(short[] colixes, int i, Atom atom) {
    return Graphics3D.getColixInherited(
        (colixes == null || i >= colixes.length ? Graphics3D.INHERIT_ALL
            : colixes[i]), atom.getColix());
  }

  public int getSize(int atomIndex) {
    return 0;
  }

  public int getSize(Group group) {
    return 0;
  }

}
