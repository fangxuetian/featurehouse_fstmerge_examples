

package org.jmol.shapesurface;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;

import org.jmol.g3d.Graphics3D;
import org.jmol.util.ArrayUtil;
import org.jmol.util.Logger;
import org.jmol.util.Measure;
import org.jmol.viewer.Viewer;
import org.jmol.jvxl.data.JvxlData;
import org.jmol.jvxl.readers.JvxlReader;

import org.jmol.jvxl.calc.MarchingSquares;
import org.jmol.shape.Mesh;

public class IsosurfaceMesh extends Mesh {
  JvxlData jvxlData = new JvxlData();
  int vertexIncrement = 1;
  int firstRealVertex = -1;
  int dataType;
  boolean hasGridPoints;
  Object calculatedArea;
  Object calculatedVolume;
  public boolean isSolvent;
  
  public float[] vertexValues;  
  public short[] vertexColixes;
  
  IsosurfaceMesh(String thisID, Graphics3D g3d, short colix, int index) {
    super(thisID, g3d, colix, index);
    haveCheckByte = true;
    jvxlData.version = Viewer.getJmolVersion();
  }

  void clear(String meshType, boolean iAddGridPoints) {
    super.clear(meshType);  
    nSets = 0;
    thisSet = -1;
    firstRealVertex = -1;
    hasGridPoints = iAddGridPoints;
    showPoints = iAddGridPoints;
    jvxlData.jvxlSurfaceData = "";
    jvxlData.jvxlEdgeData = "";
    jvxlData.jvxlColorData = "";
    isColorSolid = true;
    vertexColixes = null;
    vertexValues = null;
    polygonColixes = null;
    assocGridPointMap = null;
    assocGridPointNormals = null;
    vertexSets = null;
    centers = null;
    jvxlData.vContours = null;
    surfaceSet = null;
  }  

  void allocVertexColixes() {
    if (vertexColixes == null) {
      vertexColixes = new short[vertexCount];
      for (int i = vertexCount; --i >= 0; )
        vertexColixes[i] = colix;
    }
    isColorSolid = false;
  }

  Hashtable assocGridPointMap ;
  Hashtable assocGridPointNormals;

  int addVertexCopy(Point3f vertex, float value, int assocVertex, boolean associateNormals) {
    int vPt = addVertexCopy(vertex, value);
    switch (assocVertex) {
    case MarchingSquares.CONTOUR_POINT:
      if (firstRealVertex < 0)
        firstRealVertex = vPt;
      break;
    case MarchingSquares.VERTEX_POINT:
      hasGridPoints = true;
      break;
    case MarchingSquares.EDGE_POINT:
      vertexIncrement = 3;
      break;
    default:
      if (firstRealVertex < 0)
        firstRealVertex = vPt;
      if (associateNormals) {
        if (assocGridPointMap == null) {
          assocGridPointMap = new Hashtable();
          assocGridPointNormals = new Hashtable();
        }
        Integer key = new Integer(assocVertex);
        assocGridPointMap.put(new Integer(vPt), key);
        if (!assocGridPointNormals.containsKey(key))
          assocGridPointNormals.put(key, new Vector3f(0, 0, 0));
      }
    }
    return vPt;
  }

  int addVertexCopy(Point3f vertex, float value) {
    if (vertexCount == 0)
      vertexValues = new float[SEED_COUNT];
    else if (vertexCount >= vertexValues.length)
      vertexValues = (float[]) ArrayUtil.doubleLength(vertexValues);
    vertexValues[vertexCount] = value;
    return addVertexCopy(vertex);
  }

  public void setTranslucent(boolean isTranslucent, float iLevel) {
    super.setTranslucent(isTranslucent, iLevel);
    if (vertexColixes != null)
      for (int i = vertexCount; --i >= 0; )
        vertexColixes[i] =
          Graphics3D.getColixTranslucent(vertexColixes[i], isTranslucent, iLevel);
  }

  public short[] polygonColixes;
  private int lastColor;
  private short lastColix;
  
  void addTriangleCheck(int vertexA, int vertexB, int vertexC, int check,
                        int color) {
    if (vertices == null
        || vertexValues != null
        && (Float.isNaN(vertexValues[vertexA])
            || Float.isNaN(vertexValues[vertexB]) || Float
            .isNaN(vertexValues[vertexC]))
        || Float.isNaN(vertices[vertexA].x) 
        || Float.isNaN(vertices[vertexB].x)
        || Float.isNaN(vertices[vertexC].x))
      return;
    if (polygonCount == 0)
      polygonIndexes = new int[SEED_COUNT][];
    else if (polygonCount == polygonIndexes.length)
      polygonIndexes = (int[][]) ArrayUtil.doubleLength(polygonIndexes);
    if (color != 0) {
      if (polygonColixes == null) {
        polygonColixes = new short[SEED_COUNT];
        lastColor = 0;
      } else if (polygonCount == polygonColixes.length) {
        polygonColixes = (short[]) ArrayUtil.doubleLength(polygonColixes);
      }
      polygonColixes[polygonCount] = (color == lastColor ? lastColix
          : (lastColix = Graphics3D.getColix(lastColor = color)));
    }
    polygonIndexes[polygonCount++] = new int[] { vertexA, vertexB, vertexC,
        check };
  }
  
  void invalidateTriangles() {
    for (int i = polygonCount; --i >= 0;)
      if (!setABC(i))
        polygonIndexes[i] = null;
  }
  
  private int iA, iB, iC;
  
  private boolean setABC(int i) {
    int[] vertexIndexes = polygonIndexes[i];
    return vertexIndexes != null
          && !(Float.isNaN(vertexValues[iA = vertexIndexes[0]])
            || Float.isNaN(vertexValues[iB = vertexIndexes[1]]) 
            || Float.isNaN(vertexValues[iC = vertexIndexes[2]]));
  }
  
  Object calculateArea() {
    if (calculatedArea != null)
      return calculatedArea;
    boolean justOne = (nSets == 0 || thisSet >= 0); 
    int n = (justOne ? 1 : nSets);
    double[] v = new double[n];
    for (int i = polygonCount; --i >= 0;) {
      if (!setABC(i)) 
        continue;
      int iSet = (nSets == 0 ? 0 : vertexSets[iA]);
      if (thisSet >= 0 && iSet != thisSet)
        continue;
      vAB.sub(vertices[iB], vertices[iA]);
      vAC.sub(vertices[iC], vertices[iA]);
      vTemp.cross(vAB, vAC);
      v[justOne ? 0 : iSet] += vTemp.length();
    }
    for (int i = 0; i < n; i++)
      v[i] /= 2;
    if (justOne)
      return calculatedArea = new Float(v[0]);
    return calculatedArea = v;
  }

  Object calculateVolume() {
    if (calculatedVolume != null)
      return calculatedVolume;
    boolean justOne = (nSets == 0 || thisSet >= 0); 
    int n = (justOne ? 1 : nSets);
    double[] v = new double[n];
    for (int i = polygonCount; --i >= 0;) {
      if (!setABC(i))
        continue;
      int iSet = (nSets == 0 ? 0 : vertexSets[iA]);
      if (thisSet >= 0 && iSet != thisSet)
        continue;
      vAB.set(vertices[iB]);
      vAC.set(vertices[iC]);
      vTemp.cross(vAB, vAC);
      vAC.set(vertices[iA]);
      v[justOne ? 0 : iSet] += vAC.dot(vTemp);
    }
    for (int i = 0; i < n; i++)
      v[i] /= 6;
    if (justOne)
      return calculatedVolume = new Float(v[0]);
    return calculatedVolume = v;
  }

  BitSet[] surfaceSet;
  int[] vertexSets;
  int nSets;
  int thisSet = -1;
  
  public void sumVertexNormals(Vector3f[] vectorSums) {
    super.sumVertexNormals(vectorSums);
    
    if (assocGridPointMap != null) {
      Enumeration e = assocGridPointMap.keys();
      while (e.hasMoreElements()) {
        Integer I = (Integer) e.nextElement();
        ((Vector3f) assocGridPointNormals.get(assocGridPointMap.get(I)))
            .add(vectorSums[I.intValue()]);
      }
      e = assocGridPointMap.keys();
      while (e.hasMoreElements()) {
        Integer I = (Integer) e.nextElement();
        vectorSums[I.intValue()] = ((Vector3f) assocGridPointNormals
            .get(assocGridPointMap.get(I)));
      }
    }
  }
  
  Point3f[] centers;
  Point3f[] getCenters() {
    if (centers != null)
      return centers;
    centers = new Point3f[polygonCount];
    for (int i = 0; i < polygonCount; i++) {
      Point3f pt = centers[i] = new Point3f();
      pt.add(vertices[polygonIndexes[i][0]]);
      pt.add(vertices[polygonIndexes[i][1]]);
      pt.add(vertices[polygonIndexes[i][2]]);
      pt.scale(1/3f);
    }
    return centers;
  }
  
  Point4f getFacePlane(int i, Vector3f vNorm) {
    return Measure.getPlaneThroughPoints(vertices[polygonIndexes[i][0]], 
        vertices[polygonIndexes[i][1]], vertices[polygonIndexes[i][2]], 
        vNorm, vAB, vAC);
  }
  
  public static final int CONTOUR_NPOLYGONS = 0;
  public static final int CONTOUR_BITSET = 1;
  public static final int CONTOUR_VALUE = 2;
  public static final int CONTOUR_COLOR = 3;
  public static final int CONTOUR_FDATA = 4;
  public static final int CONTOUR_POINTS = 5;

  
  Vector[] getContours() {
    int n = jvxlData.nContours;
    if (n == 0 || polygonIndexes == null)
      return null;
    if ((havePlanarContours = (jvxlData.jvxlPlane != null)) == true)
      return null; 
    if (n < 0)
      n = -1 - n;
    Vector[] vContours = jvxlData.vContours;
    if (vContours != null) {
      for (int i = 0; i < n; i++) {
        if (vContours[i].size() > CONTOUR_POINTS)
          return jvxlData.vContours;
        JvxlReader.set3dContourVector(vContours[i], polygonIndexes, vertices);
      }
      dumpData();
      return jvxlData.vContours;
    }
    dumpData();
    vContours = new Vector[n];
    for (int i = 0; i < n; i++)
      vContours[i] = new Vector();
    float dv = (jvxlData.valueMappedToBlue - jvxlData.valueMappedToRed)
        / (n + 1);
    
    for (int i = 0; i < n; i++) {
      float value = jvxlData.valueMappedToRed + (i + 1) * dv;
      
      get3dContour(vContours[i], value, jvxlData.contourColors[i]);
    }
    Logger.info(n + " contour lines; separation = " + dv);
    return jvxlData.vContours = vContours;
  }
  
  public static void setContourVector(Vector v, int nPolygons,
                                      BitSet bsContour, float value, int color,
                                      StringBuffer fData) {
    v.add(new Integer(nPolygons));
    v.add(bsContour);
    v.add(new Float(value));
    v.add(new int[] { color });
    v.add(fData);
  }

  private void get3dContour(Vector v, float value, int color) {
    BitSet bsContour = new BitSet(polygonCount);
    StringBuffer fData = new StringBuffer();
    setContourVector(v, polygonCount, bsContour, value, color, fData);
    for (int i = 0; i < polygonCount; i++) {
      if (!setABC(i))
        continue;
      int type = 0;
      float f1, f2;
      f1 = checkPt(iA, iB, value);
      if (!Float.isNaN(f1)) {
        type |= 1;
        v.add(getContourPoint(vertices, iA, iB, f1));
      }
      f2 = checkPt(iB, iC, value);
        if (!Float.isNaN(f2)) {
          if (type == 0)
            f1 = f2;
          type |= 2;
          v.add(getContourPoint(vertices, iB, iC, f2));
        }
      switch(type){
      case 0:
        continue;
      case 3:
        break;
      default:
        f2 = checkPt(iC, iA, value);
        type |= 4;
        v.add(getContourPoint(vertices, iC, iA, f2));
      }
      bsContour.set(i);
      fData.append(type);
      fData.append(JvxlReader.jvxlFractionAsCharacter(f1));
      fData.append(JvxlReader.jvxlFractionAsCharacter(f2));
      
      
    }
    v.add(new Point3f(Float.NaN, Float.NaN, Float.NaN));
  }

  private float checkPt(int i, int j, float f) {
    float f1, f2;
    return (((f1 = vertexValues[i]) <= f) == (f < (f2 = vertexValues[j]))
        ? (f - f1) / (f2 - f1) : Float.NaN);
  }

  public static Point3f getContourPoint(Point3f[] vertices, int i, int j, float f) {
    Point3f pt = new Point3f();
    pt.set(vertices[j]);
    pt.sub(vertices[i]);
    pt.scale(f);
    pt.add(vertices[i]);
    
    return pt;
  }
   
  private void dumpData() {
    
    
    
  }
}
