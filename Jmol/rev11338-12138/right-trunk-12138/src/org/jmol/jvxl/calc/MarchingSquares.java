
package org.jmol.jvxl.calc;

import java.util.BitSet;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;

import org.jmol.util.*;
import org.jmol.jvxl.api.VertexDataServer;
import org.jmol.jvxl.data.VolumeData;


public class MarchingSquares {

  

  public final static int CONTOUR_POINT = -1;
  public final static int VERTEX_POINT = -2;
  public final static int EDGE_POINT = -3;


  private boolean logMessages = false;
  
  private VertexDataServer surfaceReader;
  private VolumeData volumeData;

  private final static int nContourMax = 100;
  public final static int defaultContourCount = 9; 
  private int nContourSegments;
  private int nContoursSpecified;
  private int contourType;
  private Point4f thePlane;
  private boolean is3DContour;
  private int thisContour = 0;
  private float valueMin, valueMax;
  private int nVertices;

  private final Vector3f pointVector = new Vector3f();
  private final Point3f pointA = new Point3f();
  private final Point3f pointB = new Point3f();
  private final Vector3f edgeVector = new Vector3f();

  private final Point3f planarOrigin = new Point3f();
  private final Vector3f[] planarVectors = new Vector3f[3];
  private final Vector3f[] unitPlanarVectors = new Vector3f[3];
  private final float[] planarVectorLengths = new float[2];
  private final Matrix3f matXyzToPlane = new Matrix3f();
  {
    planarVectors[0] = new Vector3f();
    planarVectors[1] = new Vector3f();
    planarVectors[2] = new Vector3f();
    unitPlanarVectors[0] = new Vector3f();
    unitPlanarVectors[1] = new Vector3f();
    unitPlanarVectors[2] = new Vector3f();
  }

  private boolean contourFromZero = true;
  private float[] contoursDiscrete;
   
  public MarchingSquares(VertexDataServer surfaceReader, VolumeData volumeData,
      Point4f thePlane, float[] contoursDiscrete, int nContours, int thisContour, boolean contourFromZero) {
    this.surfaceReader = surfaceReader;
    this.volumeData = volumeData;
    this.thePlane = thePlane;
    this.thisContour = thisContour;
    is3DContour = (thePlane == null);
    this.contoursDiscrete = contoursDiscrete;
    nContoursSpecified = nContours;
    this.contourFromZero = contourFromZero; 
    if (contoursDiscrete == null) {
      int i = (true ? 0 : contourFromZero ? 1  : is3DContour ? 1 : 2);
      nContourSegments = (nContours == 0 ? defaultContourCount : nContours) + i;
      if (nContourSegments > nContourMax)
        nContourSegments = nContourMax;
    } else {
      nContours = contoursDiscrete.length;
      nContourSegments = nContours; 
      this.contourFromZero = false;
    }
    setContourType();
  }

  public int getContourType() {
    return contourType;
  }
  
  public void setMinMax(float valueMin, float valueMax) {
    this.valueMin = valueMin;
    this.valueMax = valueMax;
  }
    
  
  
  

  private void setContourType() {
    if (is3DContour) {
      planarVectors[0].set(volumeData.volumetricVectors[0]);
      planarVectors[1].set(volumeData.volumetricVectors[1]);
      pixelCounts[0] = volumeData.voxelCounts[0];
      pixelCounts[1] = volumeData.voxelCounts[1];
      contourType = 2;
      return;
    }
    contourType = getContourType(thePlane, volumeData.volumetricVectors);
  }
  
  private static int getContourType(Point4f plane, Vector3f[] volumetricVectors) {
    Vector3f norm = new Vector3f(plane.x, plane.y, plane.z);
    float dotX = norm.dot(volumetricVectors[0]);
    float dotY = norm.dot(volumetricVectors[1]);
    float dotZ = norm.dot(volumetricVectors[2]);
    dotX *= dotX;
    dotY *= dotY;
    dotZ *= dotZ;
    float max = Math.max(dotX, dotY);
    int iType = (max < dotZ ? 2 : max == dotY ? 1 : 0);
    return iType;
  }

  private final static Point3i[] squareVertexOffsets = { new Point3i(0, 0, 0),
    new Point3i(1, 0, 0), new Point3i(1, 1, 0), new Point3i(0, 1, 0) };

  private final static Vector3f[] squareVertexVectors = { 
    new Vector3f(0, 0, 0),
    new Vector3f(1, 0, 0), 
    new Vector3f(1, 1, 0), 
    new Vector3f(0, 1, 0) };

  private final static byte edgeVertexes2d[] = { 0, 1, 1, 2, 2, 3, 3, 0 };

  private final static byte insideMaskTable2d[] = { 0, 9, 3, 10, 6, 15, 5, 12, 12, 5,
    15, 6, 10, 3, 9, 0 };

  
  
  
  

  public int generateContourData(boolean haveData) {

    

    Logger.info("generateContours: " + nContourSegments + " segments");

    if (!is3DContour)
      getPlanarVectors();
    setPlanarTransform();
    getPlanarOrigin();
    setupMatrix(planarMatrix, planarVectors);

    calcPixelVertexVectors();
    if (!is3DContour)
      getPixelCounts();
    createPlanarSquares();
    loadPixelData(haveData);
    nVertices = 0;
    if (logMessages) {
      int n = pixelCounts[0] / 2;
      Logger.info(ArrayUtil.dumpArray("generateContourData", pixelData, n - 4, n + 4,
          n - 4, n + 4));
    }
    boolean centerIsLow = createContours(valueMin, valueMax);
    triangulateContours(centerIsLow);
    Logger.info("generateContours: " + nVertices + " vertices");

    return contourVertexCount;
  }

  

  private void getPlanarVectors() {
    

    planarVectors[2].set(0, 0, 0);

    Vector3f thePlaneNormal = new Vector3f(thePlane.x, thePlane.y, thePlane.z);

    Vector3f[] volumetricVectors = volumeData.volumetricVectors;
    Vector3f vZ = volumetricVectors[contourType];
    float vZdotNorm = vZ.dot(thePlaneNormal);
    switch (contourType) {
    case 0: 
      planarVectors[0].scaleAdd(-volumetricVectors[1].dot(thePlaneNormal)
          / vZdotNorm, vZ, volumetricVectors[1]);
      planarVectors[1].scaleAdd(-volumetricVectors[2].dot(thePlaneNormal)
          / vZdotNorm, vZ, volumetricVectors[2]);
      break;
    case 1: 
      planarVectors[0].scaleAdd(-volumetricVectors[2].dot(thePlaneNormal)
          / vZdotNorm, vZ, volumetricVectors[2]);
      planarVectors[1].scaleAdd(-volumetricVectors[0].dot(thePlaneNormal)
          / vZdotNorm, vZ, volumetricVectors[0]);
      break;
    case 2: 
      planarVectors[0].scaleAdd(-volumetricVectors[0].dot(thePlaneNormal)
          / vZdotNorm, vZ, volumetricVectors[0]);
      planarVectors[1].scaleAdd(-volumetricVectors[1].dot(thePlaneNormal)
          / vZdotNorm, vZ, volumetricVectors[1]);
    }
  }

  private void setPlanarTransform() {
    planarVectorLengths[0] = planarVectors[0].length();
    planarVectorLengths[1] = planarVectors[1].length();
    unitPlanarVectors[0].normalize(planarVectors[0]);
    unitPlanarVectors[1].normalize(planarVectors[1]);
    unitPlanarVectors[2].cross(unitPlanarVectors[0], unitPlanarVectors[1]);
    setupMatrix(matXyzToPlane, unitPlanarVectors);
    matXyzToPlane.invert();

    float alpha = planarVectors[0].angle(planarVectors[1]);
    Logger.info("planar axes type " + contourType + " axis angle = "
        + (alpha / Math.PI * 180) + " normal=" + unitPlanarVectors[2]);
    for (int i = 0; i < 2; i++)
      Logger.info("planar vectors / lengths:" + planarVectors[i] + " / "
          + planarVectorLengths[i]);
    for (int i = 0; i < 3; i++)
      Logger.info("unit orthogonal plane vectors:" + unitPlanarVectors[i]);
  }

  private void getPlanarOrigin() {
    
    if (contourVertexCount == 0) {
      planarOrigin.set(0, 0, 0);
      return;
    }

    float minX = Float.MAX_VALUE;
    float minY = Float.MAX_VALUE;
    planarOrigin.set(contourVertexes[0]);
    for (int i = 0; i < contourVertexCount; i++) {
      pointVector.set(contourVertexes[i]);
      xyzToPixelVector(pointVector);
      if (pointVector.x < minX)
        minX = pointVector.x;
      if (pointVector.y < minY)
        minY = pointVector.y;
    }
    planarOrigin.set(pixelPtToXYZ((int) (minX * 1.0001f),
        (int) (minY * 1.0001f)));
    
      
  }

  

  private int contourVertexCount;
  protected ContourVertex[] contourVertexes;

  private static class ContourVertex extends Point3f {
    Point3i voxelLocation;
    int[] pixelLocation = new int[2];
    float value;
    int vertexIndex;

    ContourVertex(int x, int y, int z, Point3f vertexXYZ, int vPt) {
      set(vertexXYZ);
      voxelLocation = new Point3i(x, y, z);
      vertexIndex = vPt;
    }

    void setValue(float value, VolumeData volumeData) {
      this.value =  value;
      if (volumeData != null && volumeData.voxelData != null)
        volumeData.voxelData[voxelLocation.x][voxelLocation.y][voxelLocation.z] = value;
    }

    void setPixelLocation(Point3i pt) {
      pixelLocation[0] = pt.x;
      pixelLocation[1] = pt.y;
    }
  }

  public int addContourVertex(int x, int y, int z, Point3i offsets, Point3f vertexXYZ,
                     float value) {
    if (contourVertexes == null)
      contourVertexes = new ContourVertex[256];
    if (contourVertexCount == contourVertexes.length)
      contourVertexes = (ContourVertex[]) ArrayUtil
          .doubleLength(contourVertexes);
    x += offsets.x;
    y += offsets.y;
    z += offsets.z;
    int vPt = surfaceReader.addVertexCopy(vertexXYZ, value, VERTEX_POINT);
    contourVertexes[contourVertexCount++] = new ContourVertex(x, y, z,
        vertexXYZ, vPt);
    return vPt;
  }

  public void setContourData(int i, float value) {
    contourVertexes[i].setValue(value, volumeData);  
  }
  
  private void loadPixelData(boolean haveData) {
    pixelData = new float[pixelCounts[0]][pixelCounts[1]];
    int x, y;
    contourPlaneMinimumValue = Float.MAX_VALUE;
    contourPlaneMaximumValue = -Float.MAX_VALUE;
    for (int i = 0; i < contourVertexCount; i++) {
      ContourVertex c = contourVertexes[i];
      Point3i pt = locatePixel(c);
      c.setPixelLocation(pt);
      float value;
      if (haveData) {
        value = c.value;
      } else {
        value = volumeData.lookupInterpolatedVoxelValue(c);
        c.setValue(value, null);
      }
      if (value < contourPlaneMinimumValue)
        contourPlaneMinimumValue = value;
      if (value > contourPlaneMaximumValue)
        contourPlaneMaximumValue = value;
      
        
      if ((x = pt.x) >= 0 && x < pixelCounts[0] && (y = pt.y) >= 0
          && y < pixelCounts[1]) {
        pixelData[x][y] = value;
        int nSquares = 0;
        if (x != squareCountX && y != squareCountY) {
          nSquares++;
        }
        if (x != 0 && y != squareCountY) {
          nSquares++;
        }
        if (y != 0 && x != squareCountX) {
          nSquares++;
        }
        if (y != 0 && x != 0) {
          nSquares++;
        }
        if (nSquares == 0)
          continue;
        int vi = (nSquares < 2 ? -1 : c.vertexIndex);
        if (x != squareCountX && y != squareCountY) {
          planarSquares[x * squareCountY + y].setVertex(0, vi, i);
        }
        if (x != 0 && y != squareCountY) {
          planarSquares[(x - 1) * squareCountY + y].setVertex(1, vi, i);
        }
        if (y != 0 && x != squareCountX) {
          planarSquares[x * squareCountY + y - 1].setVertex(3, vi, i);
        }
        if (y != 0 && x != 0) {
          planarSquares[(x - 1) * squareCountY + y - 1].setVertex(2, vi, i);
        }
      } else {
        Logger.error("loadPixelData out of bounds: " + pt.x + " " + pt.y + "?");
      }
    }
    for (int i = 0; i < nSquares; i++) {
      planarSquares[i].checkVertices();
    }
  }

  public float getInterpolatedPixelValue(Point3f ptXYZ) {
    pointVector.set(ptXYZ);
    xyzToPixelVector(pointVector);
    float x = pointVector.x;
    float y = pointVector.y;
    if (Float.isNaN(x))
      return Float.NaN;
    int xDown = (x >= pixelCounts[0] ? pixelCounts[0] - 1 : x < 0 ? 0 : (int) x);
    int yDown = (y >= pixelCounts[1] ? pixelCounts[1] - 1 : y < 0 ? 0 : (int) y);
    int xUp = xDown + (xDown == pixelCounts[0] - 1 ? 0 : 1);
    int yUp = yDown + (yDown == pixelCounts[1] - 1 ? 0 : 1);
    float value = VolumeData.getFractional2DValue(x - xDown, y - yDown,
        pixelData[xDown][yDown], pixelData[xUp][yDown], pixelData[xDown][yUp],
        pixelData[xUp][yUp]);
    return value;
  }

  

  private final int[] pixelCounts = new int[2];
  private final Matrix3f planarMatrix = new Matrix3f();
  private float[][] pixelData;

  private final float[] vertexValues2d = new float[4];
  private final Point3f[] contourPoints = new Point3f[4];
  {
    for (int i = 4; --i >= 0;)
      contourPoints[i] = new Point3f();
  }
  private int squareCountX, squareCountY;

  private PlanarSquare[] planarSquares;
  private int nSquares;

  private static class PlanarSquare {
    int[] edgeMask12; 
    int edgeMask12All;
    int nInside;
    int nOutside;
    int nThrough;
    int index;
    
    
    final int[] vertexes = new int[] {-1, -1, -1, -1 };
    final int[] contourIndexes = new int[4];
    final float[] values = new float[4];
    float[][] fractions;
    int[][] intersectionPoints;
    int iOption;

    PlanarSquare(int index, int nContourSegments) {
      this.index = index;
      edgeMask12 = new int[nContourSegments];
      intersectionPoints = new int[nContourSegments][4];
      fractions = new float[nContourSegments][4];
      edgeMask12All = 0;
      
      
      
    }

    void setIntersectionPoints(int contourIndex, int[] pts, float[] f) {
      for (int i = 0; i < 4; i++) {
        intersectionPoints[contourIndex][i] = pts[i];
        fractions[contourIndex][i] = f[i];
      }
    }

    void setVertex(int iV, int pt, int contourIndex) {
      if (vertexes[iV] != -1 && vertexes[iV] != pt)
        Logger
            .error("iV IS NOT -1 or pt:" + iV + " " + vertexes[iV] + "!=" + pt);
      vertexes[iV] = pt;
      contourIndexes[iV] = contourIndex;
    }

    void checkVertices() {
      
    }
    
    void addEdgeMask(int contourIndex, int edgeMask4, int insideMask) {
      
      edgeMask12[contourIndex] = (((edgeMask4 << 4) + edgeMask4) << 4)
          + insideMask;
      edgeMask12All |= edgeMask12[contourIndex];
      if (insideMask == 0)
        ++nOutside;
      else if (insideMask == 0xF)
        ++nInside;
      else
        ++nThrough;
    }
  }

  private void getPixelCounts() {
    int max = 1;
    for (int i = 0; i < 3; i++) {
      if (i != contourType)
        max = Math.max(max, volumeData.voxelCounts[i]);
    }
    pixelCounts[0] = pixelCounts[1] = max;
    
    
    

    
    
  }

  private void createPlanarSquares() {
    squareCountX = pixelCounts[0] - 1;
    squareCountY = pixelCounts[1] - 1;

    planarSquares = new PlanarSquare[squareCountX * squareCountY];
    nSquares = 0;
    int i;
    for (int x = 0; x < squareCountX; x++)
      for (int y = 0; y < squareCountY; y++)
        planarSquares[i = nSquares++] = new PlanarSquare(i, nContourSegments);
    Logger.info("nSquares = " + nSquares);
  }

  private float contourPlaneMinimumValue;
  private float contourPlaneMaximumValue;

  private int contourIndex;
  private float[] contourValuesUsed;
  public float[] getContourValues() {
    return contourValuesUsed;
  }
  
  public boolean createContours(float min, float max) {
    float diff = max - min;
    boolean centerIsLow = true; 
    int lastInside = -1;
    Logger.info("generateContourData min=" + min + " max=" + max
        + " nContours=" + nContourSegments + " (" + nContoursSpecified + " specified) contourFromZero=" + contourFromZero);
    contourValuesUsed = new float[nContourSegments];
    for (int i = 0; i < nContourSegments; i++) {
      contourIndex = i;
      float cutoff = (
        contoursDiscrete != null ? contoursDiscrete[i] 
            : contourFromZero ? min + (i * 1f / nContourSegments) * diff 
            : i == 0 ? -Float.MAX_VALUE 
            : i == nContourSegments - 1 ? Float.MAX_VALUE 
            : min + ((i - 1) * 1f / (nContourSegments-1)) * diff);        
      
      if (contoursDiscrete == null && Math.abs(cutoff) < 0.0001)
        cutoff = (cutoff < 0 ? -0.0001f : 0.0001f);
      contourValuesUsed[i] = cutoff;
      int insideCount = generateContourData(i, cutoff);
      Logger.debug("contour " + (i + 1) + " cutoff=" + cutoff + " insideCount=" + insideCount + " centerIsLow=" + centerIsLow);
      if (lastInside < 0)
        lastInside = insideCount;
      else if (lastInside > insideCount) {
        centerIsLow = false;
        lastInside = 0;
      }
    }
    return centerIsLow;
  }

  private int generateContourData(int iContour, float contourCutoff) {

    

    int[][] isoPointIndexes2d = new int[squareCountY][4];
    float[][] squareFractions2d = new float[squareCountY][4];

    for (int i = squareCountY; --i >= 0;)
      isoPointIndexes2d[i][0] = isoPointIndexes2d[i][1] = isoPointIndexes2d[i][2] = isoPointIndexes2d[i][3] = -1;

    int insideCount = 0, contourCount = 0;
    for (int x = squareCountX; --x >= 0;) {
      for (int y = squareCountY; --y >= 0;) {
        int squareIndex = x * squareCountY + y;
        PlanarSquare ps = planarSquares[squareIndex];
        int[] pixelPointIndexes = propagateNeighborPointIndexes2d(x, y,
            isoPointIndexes2d, squareFractions2d);
        int insideMask = 0;
        for (int i = 4; --i >= 0;) {
          Point3i offset = squareVertexOffsets[i];
          vertexValues2d[i] = pixelData[x + offset.x][y + offset.y];
        }
        for (int i = 4; --i >= 0;) {
          if (ps.vertexes[i] == -1) {
            vertexValues2d[i] = Float.NaN;
          }
          ps.values[i] = vertexValues2d[i];
          if (isInside2d(vertexValues2d[i], contourCutoff)) {
            insideMask |= 1 << i;
            ++insideCount;
          }
        }
        if (insideMask == 0x0F) {
          ps.addEdgeMask(contourIndex, 0, 0x0F);
          continue;
        }
        ++contourCount;
        processOneQuadrilateral(ps, insideMask, contourCutoff, pixelPointIndexes,
            x, y);

        
        
        
        
        
        
      }
    }

    return insideCount;
  }

  private boolean isInside2d(float voxelValue, float max) {
    return contourFromZero ? 
        (max > 0 && voxelValue >= max) || (max <= 0 && voxelValue <= max)
        : voxelValue < max;
  }

  private float[] squareFractions;
  
  private int[] propagateNeighborPointIndexes2d(int x, int y, int[][] isoPointIndexes2d, float[][]squareFractions2d) {

    

    int[] pixelPointIndexes = isoPointIndexes2d[y];
    squareFractions = squareFractions2d[y];

    boolean noXNeighbor = (x == squareCountX - 1);
    
    if (noXNeighbor) {
      pixelPointIndexes[0] = -1;
      pixelPointIndexes[1] = -1;
      pixelPointIndexes[2] = -1;
      pixelPointIndexes[3] = -1;
    } else {
      pixelPointIndexes[1] = pixelPointIndexes[3];
      squareFractions[1] = 1f - squareFractions[3];
    }

    
    boolean noYNeighbor = (y == squareCountY - 1);
    if (noYNeighbor) {
      pixelPointIndexes[2] = -1;    
    } else {
      pixelPointIndexes[2] = isoPointIndexes2d[y + 1][0];
      squareFractions[2] = 1f - squareFractions2d[y + 1][0];
    }

    
    pixelPointIndexes[0] = -1;
    pixelPointIndexes[3] = -1;
    return pixelPointIndexes;
  }

  private void processOneQuadrilateral(PlanarSquare ps, int insideMask, float cutoff,
                               int[] pixelPointIndexes, int x, int y) {
    int edgeMask = insideMaskTable2d[insideMask];
    ps.addEdgeMask(contourIndex, edgeMask,
        insideMask);
    for (int iEdge = 4; --iEdge >= 0;) {
      if ((edgeMask & (1 << iEdge)) == 0) {
        continue;
      }
      if (pixelPointIndexes[iEdge] >= 0)
        continue; 
      int vertexA = edgeVertexes2d[2 * iEdge];
      int vertexB = edgeVertexes2d[2 * iEdge + 1];
      float valueA = vertexValues2d[vertexA];
      float valueB = vertexValues2d[vertexB];
      if (is3DContour) 
        calcVertexPoints3d(x, y, vertexA, vertexB);
      else
        calcVertexPoints2d(x, y, vertexA, vertexB);
      squareFractions[iEdge] = calcContourPoint(cutoff, valueA, valueB, contourPoints[iEdge]);
      pixelPointIndexes[iEdge] = surfaceReader.addVertexCopy(contourPoints[iEdge], cutoff, CONTOUR_POINT);
      nVertices++;
      
      
    }
    
    ps.setIntersectionPoints(contourIndex,
        pixelPointIndexes, squareFractions);
  }

  private final Point3f pixelOrigin = new Point3f();

  private void calcVertexPoints2d(int x, int y, int vertexA, int vertexB) {
    pixelOrigin.scaleAdd(x, planarVectors[0], planarOrigin);
    pixelOrigin.scaleAdd(y, planarVectors[1], pixelOrigin);
    pointA.add(pixelOrigin, pixelVertexVectors[vertexA]);
    pointB.add(pixelOrigin, pixelVertexVectors[vertexB]);
  }

  private void calcVertexPoints3d(int x, int y, int vertexA, int vertexB) {
    contourLocateXYZ(x + squareVertexOffsets[vertexA].x, y
        + squareVertexOffsets[vertexA].y, pointA);
    contourLocateXYZ(x + squareVertexOffsets[vertexB].x, y
        + squareVertexOffsets[vertexB].y, pointB);
  }

  private void contourLocateXYZ(int ix, int iy, Point3f pt) {
    int i = findContourVertex(ix, iy);
    if (i < 0) {
      pt.x = Float.NaN;
      return;
    }
    ContourVertex c = contourVertexes[i];
    pt.set(c);    
  }

  private int findContourVertex(int ix, int iy) {
    for (int i = 0; i < contourVertexCount; i++) {
      if (contourVertexes[i].pixelLocation[0] == ix
          && contourVertexes[i].pixelLocation[1] == iy)
        return i;
    }
    return -1;
  }

  private float calcContourPoint(float cutoff, float valueA, float valueB,
                         Point3f contourPoint) {
    
    float fraction = (false && Float.isNaN(valueA) ? 1 
        : false && Float.isNaN(valueB) ? 0 
        : (cutoff - valueA) / (valueB - valueA));
    edgeVector.sub(pointB, pointA);
    contourPoint.scaleAdd(fraction, edgeVector, pointA);
    return fraction;
  }

    private Vector3f[] pixelVertexVectors = new Vector3f[4];
  
    private void calcPixelVertexVectors() {
    for (int i = 4; --i >= 0;) {
      pixelVertexVectors[i] = new Vector3f();
      planarMatrix.transform(squareVertexVectors[i],pixelVertexVectors[i]);
    }
  }

  private void triangulateContours(boolean centerIsLow) {

    
    int offset = (centerIsLow ? -1 : 1);
    for (int contourIndex = 0; contourIndex < nContourSegments; contourIndex++) {
      if (thisContour <= 0 || thisContour == contourIndex + 1) {
        for (int squareIndex = 0; squareIndex < nSquares; squareIndex++) {




          

          PlanarSquare square = planarSquares[squareIndex];
          int edgeMask0 = square.edgeMask12[contourIndex];
          edgeMask0 &= 0xFF;

          boolean isTerminal = (contourIndex + offset < 0 || contourIndex + offset == nContourSegments);
          
          if (edgeMask0 == 0 && !isTerminal && square.edgeMask12[contourIndex +offset] == 0)
            continue;
          
          if (edgeMask0 == 0xF && !isTerminal && square.edgeMask12[contourIndex +offset] == 0xF)
            continue;
          boolean lowerFirst = false;
          int edgeMask = edgeMask0;  
          if (!isTerminal) {
            edgeMask0 = square.edgeMask12[contourIndex + offset];
            if (edgeMask0 != 0) {
              int andMask = (edgeMask & edgeMask0 & 0xF0) >> 4;
              if (andMask != 0) { 
                for (int i = 0; i < 4; i++)
                  if ((andMask & (1 << i)) != 0) {
                    lowerFirst = (square.fractions[contourIndex][i] > square.fractions[contourIndex + offset][i]);
                    break;
                  }
              }
              
              edgeMask ^= edgeMask0 & 0x0F0F;
              
            }
          }

          if (edgeMask != 0) {
            
              
            
            fillSquare(square, squareIndex, contourIndex, edgeMask, lowerFirst, offset);
          }
        }
      }
    }
  }

  private final int[] triangleVertexList = new int[20];

  private final BitSet bsMesh0 = new BitSet();
  private final BitSet bsMesh1 = new BitSet();
  private final BitSet bsMesh2 = new BitSet();
  private void fillSquare(PlanarSquare square, int squareIndex, int contourIndex, int edgeMask,
                  boolean lowerFirst, int offset) {
    int nIntersect = 0;
    
    int vPt = 0;
    bsMesh0.clear();
    bsMesh1.clear();
    bsMesh2.clear();
    int nValid = 4;
    int pt0 = 0;
    for (int i = 0; i < 4; i++) {
      if (square.vertexes[i] < 0)
        nValid--;
    }
    if (nValid < 3)
      return;
    int maskN = (edgeMask >> 4) & 0xF;
    int maskN0 = (edgeMask >> 8) & 0xF;
    int maskV = edgeMask & 0xF;
    
    
    
    boolean newVertex = ((maskV & 1) != 0);
    int iOption = 0;
    switch (maskN) {
    case 0xF:
      if (square.iOption < 0) {
        iOption = (newVertex ? -2 : -1);
      } else {
        iOption = (newVertex ? 1 : 2);
      }
      square.iOption = iOption;
      break;
    case 0x0:
      if (maskN0 == 0xF) {
        switch (square.iOption) {
        case -1:
          iOption = -2;
          break;
        case 1:
          iOption = 2;
          break;
        case -2:
          iOption = -1;
          break;
        case 2:
          iOption = 1;
          break;          
        }
      }
      square.iOption = iOption;
      break;
    case 9: 
    case 6: 
      
      square.iOption = -1;
      break;
    case 12: 
    case 3: 
      
      square.iOption = 1;
      break;
    default:
      square.iOption = 0;
    }
    
    for (int i = 0; i < 4; i++) {
      newVertex = ((maskV & (1 << i)) != 0);
      boolean thisIntersect = ((maskN & (1 << i)) != 0);
      boolean lowerIntersect = ((maskN0 & (1 << i)) != 0);
      
      
      
      if (newVertex) {
        triangleVertexList[vPt++] = square.vertexes[i];
      }

      
      if (lowerFirst && lowerIntersect) {
        bsMesh2.set(vPt);
        triangleVertexList[vPt++] = square.intersectionPoints[contourIndex + offset][i];
        nIntersect++;
      }
      
      
      
      if (thisIntersect) {
        bsMesh1.set(vPt);
        triangleVertexList[vPt++] = square.intersectionPoints[contourIndex][i];
        nIntersect++;
      }
      
      

      if (!lowerFirst && lowerIntersect) {
        bsMesh2.set(vPt);
        triangleVertexList[vPt++] = square.intersectionPoints[contourIndex + offset][i];
        nIntersect++;
      }
      
      if (lowerIntersect && thisIntersect) {
        lowerFirst = !lowerFirst;
      }
      
      if (i == 0 && iOption == -2) {
        BitSetUtil.copy(bsMesh1, bsMesh0);
        pt0 = vPt;
      }
      
      if (i == 2 && iOption == -2) {
        createTriangleSet(pt0, vPt, nValid, contourIndex, contourIndex + offset);
        vPt = pt0;
        pt0 = 0;
        BitSetUtil.copy(bsMesh0, bsMesh1);
      }
      
      if (i == 1 && iOption == 2) {
        createTriangleSet(pt0, vPt, nValid, contourIndex, contourIndex + offset);
        vPt = 0;
        bsMesh1.clear();
      }
      

    }

     
     
     
     
     
     

    if (vPt > 2)
      createTriangleSet(pt0, vPt, nValid, contourIndex, contourIndex + offset);
  }

  private void createTriangleSet(int pt0, int nVertex, int nValid, int contourIndex, int contourIndex2) {
    
    
    

    int i1 = pt0;
    int i2 = pt0 + 1;
    int i0 = pt0 + 2;
    if (nValid == 3) {
      if (triangleVertexList[pt0] < 0) {
        
        i1++;
        i2 = i0++;
      } else if (triangleVertexList[i2] < 0) {
        
        i2 = i0++;
      }
    }
    for (int i3 = i0; i3 < nVertex; i3++) {
      int iA = triangleVertexList[i1];
      int iB = triangleVertexList[i2];
      int iC = triangleVertexList[i3];
      if (iA >= 0 && iB >= 0 && iC >= 0) {
        int check = (bsMesh1.get(i1) && bsMesh1.get(i2) ? 1 : bsMesh1.get(i2)
            && bsMesh1.get(i3) ? 2 : bsMesh1.get(i3) && bsMesh1.get(i1) ? 4 : 0);
        int check2 = (check > 0 ? 0 : bsMesh2.get(i1) && bsMesh2.get(i2) ? 1 : bsMesh2.get(i2)
            && bsMesh2.get(i3) ? 2 : bsMesh2.get(i3) && bsMesh2.get(i1) ? 4 : 0);
         surfaceReader.addTriangleCheck(iA, iB, iC, Math.max(check, check2), (check > 0 ? contourIndex : contourIndex2), false, 0);
      }
      if (iC >= 0)
        i2 = i3;
    }
  }
  
  private static void setupMatrix(Matrix3f mat, Vector3f[] cols) {
    for (int i = 0; i < 3; i++)
      mat.setColumn(i, cols[i]);
  }
  
  private void xyzToPixelVector(Vector3f vector) {
    
    vector.sub(vector, planarOrigin);
    matXyzToPlane.transform(vector);
    vector.x /= planarVectorLengths[0];
    vector.y /= planarVectorLengths[1];
  }

  private Point3f pixelPtToXYZ(int x, int y) {
    Point3f ptXyz = new Point3f();
    ptXyz.scaleAdd(x, planarVectors[0], planarOrigin);
    ptXyz.scaleAdd(y, planarVectors[1], ptXyz);
    return ptXyz;
  }
  
  private final Point3i ptiTemp = new Point3i();

  private Point3i locatePixel(Point3f ptXyz) {
    pointVector.set(ptXyz);
    xyzToPixelVector(pointVector);
    ptiTemp.x = (int) (pointVector.x + 0.5f);
    
    ptiTemp.y = (int) (pointVector.y + 0.5f);
    return ptiTemp;
  }
}
