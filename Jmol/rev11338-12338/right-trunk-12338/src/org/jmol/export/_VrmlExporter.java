

package org.jmol.export;

import java.util.BitSet;
import java.util.Hashtable;
import java.util.Vector;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import org.jmol.g3d.Font3D;
import org.jmol.g3d.Graphics3D;
import org.jmol.util.Escape;
import org.jmol.util.Quaternion;
import org.jmol.viewer.Viewer;

public class _VrmlExporter extends __CartesianExporter {

  

  public _VrmlExporter() {
    useTable = new UseTable("USE ");
  }
  
  protected AxisAngle4f viewpoint = new AxisAngle4f();
  
  protected void output(Tuple3f pt) {
    output(round(pt));
  }
  
  protected UseTable useTable;
  
  protected void outputHeader() {
    output("#VRML V2.0 utf8 Generated by Jmol " + Viewer.getJmolVersion()
        + "\n");
    output("WorldInfo { \n");
    output(" title " + Escape.escape(viewer.getModelSetName()) + "\n"); 
    output(" info [ \"Generated by Jmol " + Viewer.getJmolVersion() + " \", \n");
    output("  \"http://www.jmol.org \", \n");
    output("  \"Creation date: " + getExportDate() + " \" ]\n");
    output("} \n");

    output("NavigationInfo { type \"EXAMINE\" } \n");
    
    output("Background { skyColor ["
        + rgbFractionalFromColix(backgroundColix, ' ') + "] } \n");
    
    getViewpointPosition(tempP1);
    adjustViewpointPosition(tempP1);
    float angle = getFieldOfView();
    viewer.getAxisAngle(viewpoint);
    output("Viewpoint{fieldOfView " + angle 
        + " position " + tempP1.x + " " + tempP1.y + " " + tempP1.z 
        + " orientation " + viewpoint.x + " " + viewpoint.y + " " + (viewpoint.angle == 0 ? 1 : viewpoint.z) + " " + -viewpoint.angle);
    output("\n jump TRUE description \"v1\"\n}\n\n");
    outputJmolPerspective();
    output("\nTransform{children Transform{translation ");
    tempP1.set(center);
    tempP1.scale(-1);
    output(tempP1);
    output("\nchildren [\n");
  }

  protected void outputFooter() {
    useTable = null;
    output("\n]\n");
    output("}}\n");
  }

  protected void outputAppearance(short colix, boolean isText) {
    String def = useTable.getDef((isText ? "T" : "") + colix);
    output(" appearance ");
    if (def.charAt(0) == '_') {
      String color = rgbFractionalFromColix(colix, ' ');
      output(" DEF " + def + " Appearance{material Material{diffuseColor ");
      if (isText)
        output(" 0 0 0 specularColor 0 0 0 ambientIntensity 0.0 shininess 0.0 emissiveColor " 
            + color + " }}");
      else
        output(color + " transparency " + translucencyFractionalFromColix(colix) + "}}");
      return;
    }
    output(def);
  }
  
  protected void outputCircle(Point3f pt1, Point3f pt2, float radius, short colix,
                            boolean doFill) {
    if (doFill) {
      

      output("Transform{translation ");
      tempV1.set(pt1);
      tempV1.add(pt2);
      tempV1.scale(0.5f);
      output(tempV1);
      output(" children Billboard{axisOfRotation 0 0 0 children Transform{rotation 1 0 0 1.5708");
      outputCylinderChild(pt1, pt2, colix, Graphics3D.ENDCAPS_FLAT,
          (int) (radius * 2000));
      output("}}}\n");
      return;
    }

    

    String child = useTable.getDef("C" + colix + "_" + radius);
    outputTransRot(pt1, pt2, 0, 0, 1);
    tempP3.set(1, 1, 1);
    tempP3.scale(radius);
    output(" scale ");
    output(tempP3);
    output(" children ");
    if (child.charAt(0) == '_') {
      output("DEF " + child);
      output(" Billboard{axisOfRotation 0 0 0 children Transform{children");
      output(" Shape{geometry Extrusion{beginCap FALSE convex FALSE endCap FALSE creaseAngle 1.57");
      output(" crossSection [");
      float rpd = 3.1415926f / 180;
      float scale = 0.02f / radius;
      for (int i = 0; i <= 360; i += 10) {
        output(round(Math.cos(i * rpd) * scale) + " ");
        output(round(Math.sin(i * rpd) * scale) + " ");
      }
      output("] spine [");
      for (int i = 0; i <= 360; i += 10) {
        output(round(Math.cos(i * rpd)) + " ");
        output(round(Math.sin(i * rpd)) + " 0 ");
      }
      output("]}");
      outputAppearance(colix, false);
      output("}}}");
    } else {
      output(child);
    }
    output("}\n");
  }

  protected void outputComment(String comment) {
    output("# " + comment + "\n");
  }
  
  protected void outputCone(Point3f ptBase, Point3f ptTip, float radius,
                            short colix) {
    float height = tempP1.distance(tempP2);
    outputTransRot(tempP1, tempP2, 0, 1, 0);
    output(" children ");
    String cone = "o" + (int) (height * 100) + "_" + (int) (radius * 100);
    String child = useTable.getDef("c" + cone + "_" + colix);
    if (child.charAt(0) == '_') {
      output("DEF " + child + " Shape{geometry ");
      cone = useTable.getDef(cone);
      if (cone.charAt(0) == '_') {
        output("DEF " + cone + " Cone{height " + round(height)
            + " bottomRadius " + round(radius) + "}");
      } else {
        output(cone);
      }
      outputAppearance(colix, false);
      output("}");
    } else {
      output(child);
    }
    output("}\n");
  }

  protected void outputCylinder(Point3f pt1, Point3f pt2, short colix,
                             byte endcaps, float radius) {
    outputTransRot(pt1, pt2, 0, 1, 0);
    outputCylinderChild(pt1, pt2, colix, endcaps, radius);
    output("}\n");
    if (endcaps == Graphics3D.ENDCAPS_SPHERICAL) {
      outputSphere(pt1, radius*1.01f, colix);
      outputSphere(pt2, radius*1.01f, colix);
    }
  }

  private void outputCylinderChild(Point3f pt1, Point3f pt2, short colix,
                                   byte endcaps, float radius) {
    output(" children ");    
    float length = pt1.distance(pt2);
    String child = useTable.getDef("C" + colix + "_" + (int) (length * 100) + "_" + radius
        + "_" + endcaps);
    if (child.charAt(0) == '_') {
      output("DEF " + child);
      output(" Shape{geometry ");
      String cyl = useTable.getDef("c" + round(length) + "_" + endcaps + "_" + radius);
      if (cyl.charAt(0) == '_') {
        output("DEF " + cyl + " Cylinder{height " 
            + round(length) + " radius " + radius 
            + (endcaps == Graphics3D.ENDCAPS_FLAT ? "" : " top FALSE bottom FALSE") + "}");
      } else {
        output(cyl);
      }
      outputAppearance(colix, false);
      output("}");
    } else {
      output(child);
    }
  }

  protected void outputEllipsoid(Point3f center, Point3f[] points, short colix) {
    output("Transform{translation ");
    output(center);

    
    
    
    

    AxisAngle4f a = Quaternion.getQuaternionFrame(center, points[1], points[3])
        .toAxisAngle4f();
    if (!Float.isNaN(a.x))
      output(" rotation " + a.x + " " + a.y + " " + a.z + " " + a.angle);
    tempP3.set(0, 0, 0);
    float sx = points[1].distance(center);
    float sy = points[3].distance(center);
    float sz = points[5].distance(center);
    output(" scale " + sx + " " + sy + " " + sz + " children ");
    outputSphere(tempP3, 1.0f, colix);
    output("}\n");
  }

  protected void outputSurface(Point3f[] vertices, Vector3f[] normals,
                                  short[] colixes, int[][] indices,
                                  short[] polygonColixes,
                                  int nVertices, int nPolygons, int nFaces, BitSet bsFaces,
                                  int faceVertexMax, short colix, Vector colorList, Hashtable htColixes, Point3f offset) {
    output("Shape {\n");
    outputAppearance(colix, false);
    output(" geometry IndexedFaceSet {\n");

    if (polygonColixes != null)
      output(" colorPerVertex FALSE\n");

    

    output("coord Coordinate {\n   point [\n");
    outputVertices(vertices, nVertices, offset);
    output("   ]\n");
    output("  }\n");
    output("  coordIndex [\n");
    int[] map = new int[nVertices];
    getCoordinateMap(vertices, map);
    outputIndices(indices, map, nPolygons, bsFaces, faceVertexMax);
    output("  ]\n");

    

    if (normals != null) {
      Vector vNormals = new Vector();
      map = getNormalMap(normals, nVertices, vNormals);
      output("  solid FALSE\n  normalPerVertex TRUE\n   normal Normal {\n  vector [\n");
      outputNormals(vNormals);
      output("   ]\n");
      output("  }\n");
      output("  normalIndex [\n");
      outputIndices(indices, map, nPolygons, bsFaces, faceVertexMax);
      output("  ]\n");
    }

    map = null;
    
    

    if (colorList != null) {
      output("  color Color { color [\n");
      outputColors(colorList);
      output("  ] } \n");
      output("  colorIndex [\n");
      outputColorIndices(indices, nPolygons, bsFaces, faceVertexMax, htColixes, colixes, polygonColixes);
      output("  ]\n");
    }

    output(" }\n");
    output("}\n");
  }

  protected void outputFace(int[] face, int[] map, int faceVertexMax) {
    output(map[face[0]] + " " + map[face[1]] + " " + map[face[2]] + " -1\n");
    if (faceVertexMax == 4 && face.length == 4)
      output(map[face[0]] + " " + map[face[2]] + " " + map[face[3]] + " -1\n");
  }

  protected void outputNormals(Vector vNormals) {
    int n = vNormals.size();
    for (int i = 0; i < n; i++)
      output((String) vNormals.get(i));
  }

  protected void outputColors(Vector colorList) {
    int nColors = colorList.size();
    for (int i = 0; i < nColors; i++) {
      String color = rgbFractionalFromColix(((Short) colorList.get(i)).shortValue(),
          ' ');
      output(" ");
      output(color);
      output("\n");
    }
  }

  protected void outputColorIndices(int[][] indices, int nPolygons, BitSet bsFaces,
                                  int faceVertexMax, Hashtable htColixes,
                                  short[] colixes, short[] polygonColixes) {
    boolean isAll = (bsFaces == null);
    int i0 = (isAll ? nPolygons - 1 : bsFaces.nextSetBit(0));
    for (int i = i0; i >= 0; i = (isAll ? i - 1 : bsFaces.nextSetBit(i + 1))) {
      if (polygonColixes == null) {
        output(htColixes.get("" + colixes[indices[i][0]]) + " "
            + htColixes.get("" + colixes[indices[i][1]]) + " "
            + htColixes.get("" + colixes[indices[i][2]]) + " -1\n");
        if (faceVertexMax == 4 && indices[i].length == 4)
          output(htColixes.get("" + colixes[indices[i][0]]) + " "
              + htColixes.get("" + colixes[indices[i][2]]) + " "
              + htColixes.get("" + colixes[indices[i][3]]) + " -1\n");
      } else {
        output(htColixes.get("" + polygonColixes[i]) + "\n");
      }
    }
  }

  Hashtable htSpheresRendered = new Hashtable();
  protected void outputSphere(Point3f center, float radius, short colix) {
    String child = useTable.getDef("S" + colix + "_" + (int) (radius * 100));
    String check = child + " " + center;
    if (htSpheresRendered.get(check) != null)
      return;
    htSpheresRendered.put(check, Boolean.TRUE);
    output("Transform{translation ");
    output(center);
    output(" children ");
    if (child.charAt(0) == '_') {
      output("DEF " + child);
      output(" Shape{geometry Sphere{radius " + radius + "}");
      outputAppearance(colix, false);
      output("}");
    } else {
      output(child);
    }
    output("}\n");
  }
  
  protected void outputTextPixel(Point3f pt, int argb) {
    String color = rgbFractionalFromArgb(argb, ' ');
    output("Transform{translation ");
    output(pt);
    output(" children ");
    String child = useTable.getDef("p" + argb);
    if (child.charAt(0) == '_') {
      output("DEF " + child + " Shape{geometry Sphere{radius 0.01}");
      output(" appearance Appearance{material Material{diffuseColor 0 0 0 specularColor 0 0 0 ambientIntensity 0.0 shininess 0.0 emissiveColor "
          + color + " }}}");
    } else {
      output(child);
    }
    output("}\n");
  }

  private void outputTransRot(Point3f pt1, Point3f pt2, int x, int y, int z) {    
    output("Transform{");
    outputTransRot(pt1, pt2, x, y, z, " ", "");
  }
  
  protected void outputTransRot(Point3f pt1, Point3f pt2, int x, int y, int z,
                                String pre, String post) {
    tempV1.set(pt2);
    tempV1.add(pt1);
    tempV1.scale(0.5f);
    output("translation");
    output(pre);
    output(tempV1);
    output(post);
    tempV1.sub(pt1);
    tempV1.normalize();
    tempV2.set(x, y, z);
    tempV2.add(tempV1);
    tempA.set(tempV2.x, tempV2.y, tempV2.z, 3.14159f);
    output(" rotation");
    output(pre);
    output(round(tempA.x) + " " + round(tempA.y) + " " + round(tempA.z) + " "
        + round(tempA.angle));
    output(post);
  }

  protected void outputTriangle(Point3f pt1, Point3f pt2, Point3f pt3, short colix) {
    
    
    output("Shape{geometry IndexedFaceSet{solid FALSE coord Coordinate{point[");
    output(pt1);
    output(" ");
    output(pt2);
    output(" ");
    output(pt3);
    output("]}coordIndex[ 0 1 2 -1 ]}");
    outputAppearance(colix, false);
    output("}\n");
  }

  void plotText(int x, int y, int z, short colix, String text, Font3D font3d) {
    if (z < 3)
      z = viewer.getFrontPlane();
    String useFontStyle = font3d.fontStyle.toUpperCase();
    String preFontFace = font3d.fontFace.toUpperCase();
    String useFontFace = (preFontFace.equals("MONOSPACED") ? "TYPEWRITER"
        : preFontFace.equals("SERIF") ? "SERIF" : "SANS");
    output("Transform{translation ");
    tempP3.set(x, y, z);
    viewer.unTransformPoint(tempP3, tempP1);
    output(tempP1);
    
    
    output(" children ");
    String child = useTable.getDef("T" + colix + useFontFace + useFontStyle + "_" + text);
    if (child.charAt(0) == '_') {
      output("DEF " + child + " Billboard{axisOfRotation 0 0 0 children Transform{children Shape{");
      outputAppearance(colix, true);
      output(" geometry Text{fontStyle ");
      String fontstyle = useTable.getDef("F" + useFontFace + useFontStyle);
      if (fontstyle.charAt(0) == '_') {
        output("DEF " + fontstyle + " FontStyle{size 0.4 family \"" + useFontFace
            + "\" style \"" + useFontStyle + "\"}");      
      } else {
        output(fontstyle);
      }
      output(" string " + Escape.escape(text) + "}}}}");
    } else {
      output(child);
    }
    output("}\n");
  }

  

}


