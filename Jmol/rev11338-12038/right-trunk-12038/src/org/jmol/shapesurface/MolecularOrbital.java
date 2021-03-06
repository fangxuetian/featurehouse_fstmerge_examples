

package org.jmol.shapesurface;

import java.util.BitSet;
import java.util.Vector;
import java.util.Hashtable;

import javax.vecmath.Point4f;

import org.jmol.util.ArrayUtil;
import org.jmol.util.Escape;
import org.jmol.util.Logger;
import org.jmol.script.Token;
import org.jmol.jvxl.data.JvxlCoder;
import org.jmol.jvxl.readers.Parameters;

public class MolecularOrbital extends Isosurface {

  public void initShape() {
    super.initShape();
    myType = "molecularOrbital";
    super.setProperty("thisID", "mo", null);
  }

  

  private String moTranslucency = null;
  private Float moTranslucentLevel = null;
  private Point4f moPlane = null;
  private Float moCutoff = null;
  private Float moResolution = null;
  private Float moScale = null;
  private Integer moColorPos = null;
  private Integer moColorNeg = null;
  private boolean moIsPositiveOnly = false;
  private int moFill = Token.nofill;
  private int moMesh = Token.mesh;
  private int moDots = Token.nodots;
  private int moFrontOnly = Token.frontonly;
  private String moTitleFormat = null;
  private boolean moDebug;
  private int myColorPt;
  private String strID;
  private int moNumber;
  private Hashtable htModels;
  private Hashtable thisModel;

  public void setProperty(String propertyName, Object value, BitSet bs) {

    if (Logger.debugging) {
      Logger
          .debug("MolecularOrbital.setProperty " + propertyName + " " + value);
    }

    
    

    if ("init" == propertyName) {
      myColorPt = 0;
      moDebug = false;
      int modelIndex = ((Integer) value).intValue();
      strID = getId(modelIndex);
      Logger.info("MO init " + strID);
      
      super.setProperty("init", null, null);
      super.setProperty("modelIndex", new Integer(modelIndex), null);
      if (htModels == null)
        htModels = new Hashtable();
      if (!htModels.containsKey(strID))
        htModels.put(strID, new Hashtable());
      thisModel = (Hashtable) htModels.get(strID);
      moNumber = (!thisModel.containsKey("moNumber") ? 0 : ((Integer) thisModel
          .get("moNumber")).intValue());
      return;
    }

    if ("cutoff" == propertyName) {
      thisModel.put("moCutoff", value);
      thisModel.put("moIsPositiveOnly", Boolean.FALSE);
      return;
    }

    if ("scale" == propertyName) {
      thisModel.put("moScale", value);
      return;
    }

    if ("cutoffPositive" == propertyName) {
      thisModel.put("moCutoff", value);
      thisModel.put("moIsPositiveOnly", Boolean.TRUE);
      return;
    }

    if ("resolution" == propertyName) {
      thisModel.put("moResolution", value);
      return;
    }

    if ("titleFormat" == propertyName) {
      moTitleFormat = (String) value;
      return;
    }

    if ("color" == propertyName) {
      if (!(value instanceof Integer))
        return;
      thisModel.remove("moTranslucency");
      super.setProperty("color", value, bs);
      propertyName = "colorRGB";
      myColorPt = 0;
      
    }

    if ("colorRGB" == propertyName) {
      moColorPos = (Integer) value;
      if (myColorPt++ == 0)
        moColorNeg = moColorPos;
      thisModel.put("moColorNeg", moColorNeg);
      thisModel.put("moColorPos", moColorPos);
      return;
    }

    if ("plane" == propertyName) {
      if (value == null)
        thisModel.remove("moPlane");
      else
        thisModel.put("moPlane", value);
      return;
    }

    if ("molecularOrbital" == propertyName) {
      moNumber = ((Integer) value).intValue();
      thisModel.put("moNumber", value);
      setOrbital(moNumber);
      return;
    }

    if ("translucentLevel" == propertyName) {
      if (thisModel == null) {
        if (currentMesh == null)
          return;
        thisModel = (Hashtable) htModels.get(currentMesh.thisID);
      }
      thisModel.put("moTranslucentLevel", value);
      
    }

    if ("delete" == propertyName) {
      htModels.remove(strID);
      moNumber = 0;
      
    }

    if ("token" == propertyName) {
      int tok = ((Integer) value).intValue();
      switch (tok) {
      case Token.dots:
      case Token.nodots:
        moDots = tok;
        break;
      case Token.fill:
      case Token.nofill:
        moFill = tok;
        break;
      case Token.mesh:
      case Token.nomesh:
        moMesh = tok;
        break;
      case Token.frontonly:
      case Token.notfrontonly:
        moFrontOnly = tok;
        break;
      }
      
    }

    if ("translucency" == propertyName) {
      if (thisModel == null) {
        if (currentMesh == null)
          return;
        thisModel = (Hashtable) htModels.get(currentMesh.thisID);
      }
      thisModel.put("moTranslucency", value);
      
    }

    if (propertyName == "deleteModelAtoms") {
      int modelIndex = ((int[]) ((Object[]) value)[2])[0];
      Hashtable htModelsNew = new Hashtable();
      for (int i = meshCount; --i >= 0;) {
        if (meshes[i] == null)
          continue;
        if (meshes[i].modelIndex == modelIndex) {
          meshCount--;
          if (meshes[i] == currentMesh) {
            currentMesh = null;
            thisModel = null;
          }
          meshes = (IsosurfaceMesh[]) ArrayUtil.deleteElements(meshes, i, 1);
          continue;
        }
        Hashtable htModel = (Hashtable) htModels.get(meshes[i].thisID);
        if (meshes[i].modelIndex > modelIndex) {
          meshes[i].modelIndex--;
          meshes[i].thisID = getId(meshes[i].modelIndex);
        }
        htModelsNew.put(meshes[i].thisID, htModel);
      }
      htModels = htModelsNew;
      return;
    }
    super.setProperty(propertyName, value, bs);
  }

  private String getId(int modelIndex) {
    return "mo_model" + viewer.getModelNumber(modelIndex);
  }

  public Object getProperty(String propertyName, int param) {
    if (propertyName == "list") {
      String s = (String) super.getProperty("list", param);
      if (s.length() > 1)
        s += "cutoff = " + super.getProperty("cutoff", 0) + "\n";
      return viewer.getMoInfo(-1) + "\n" + s;
    }
    if (propertyName == "moNumber")
      return new Integer(moNumber);
    if (propertyName == "showMO") {
      StringBuffer str = new StringBuffer();
      Vector mos = (Vector) (sg.getMoData().get("mos"));
      int nOrb = (mos == null ? 0 : mos.size());
      int thisMO = param;
      int currentMO = moNumber;
      boolean isShowCurrent = (thisMO == Integer.MIN_VALUE);
      if (thisMO == Integer.MAX_VALUE) {
        thisMO = currentMO;
      }
      if (nOrb == 0 || isShowCurrent && currentMO == 0)
        return "";
      boolean doOneMo = (thisMO != 0);
      if (currentMO == 0)
        thisMO = 0;
      boolean haveHeader = false;
      int nTotal = (thisMO > 0 ? 1 : nOrb);
      int i0 = (nTotal == 1 && currentMO > 0 ? currentMO : 1);
      for (int i = i0; i <= nOrb; i++)
        if (thisMO == 0 || thisMO == i || !doOneMo && i == currentMO) {
          if (!doOneMo) {
            Parameters params = sg.getParams();
            super.setProperty("init", params, null);
            setOrbital(i);
          }
          if (!haveHeader) {
            str.append(JvxlCoder.jvxlGetFile(jvxlData, null, null,
                "HEADERONLY", true, nTotal, null, null));
            haveHeader = true;
          }
          str.append(JvxlCoder.jvxlGetFile(jvxlData, null, jvxlData.title,
              null, false, 1, thisMesh.getState(myType),
              (thisMesh.scriptCommand == null ? "" : thisMesh.scriptCommand)));
          if (!doOneMo)
            super.setProperty("delete", "mo_show", null);
          if (nTotal == 1)
            break;
        }
      str.append(JvxlCoder.jvxlGetFile(jvxlData, null, null, "TRAILERONLY", true,
          0, null, null));
      return str.toString();
    }
    return super.getProperty(propertyName, param);
  }

  protected void clearSg() {
    
  }

  private boolean getSettings(String strID) {
    thisModel = (Hashtable) htModels.get(strID);
    if (thisModel == null || thisModel.get("moNumber") == null)
      return false;
    moTranslucency = (String) thisModel.get("moTranslucency");
    moTranslucentLevel = (Float) thisModel.get("moTranslucentLevel");
    moPlane = (Point4f) thisModel.get("moPlane");
    moCutoff = (Float) thisModel.get("moCutoff");
    if (moCutoff == null)
      moCutoff = (Float) sg.getMoData().get("defaultCutoff");
    if (moCutoff == null) {
      moCutoff = new Float(Parameters.defaultQMOrbitalCutoff);
    }
    thisModel.put("moCutoff", new Float(moCutoff.floatValue()));
    moResolution = (Float) thisModel.get("moResolution");
    moScale = (Float) thisModel.get("moScale");
    moColorPos = (Integer) thisModel.get("moColorPos");
    moColorNeg = (Integer) thisModel.get("moColorNeg");
    moNumber = ((Integer) thisModel.get("moNumber")).intValue();
    Object b = thisModel.get("moIsPositiveOnly");
    moIsPositiveOnly = (b != null && ((Boolean) (b)).booleanValue());
    return true;
  }

  private void setOrbital(int moNumber) {
    super.setProperty("reset", strID, null);
    if (moDebug)
      super.setProperty("debug", Boolean.TRUE, null);
    getSettings(strID);
    if (moScale != null)
      super.setProperty("scale", moScale, null);
    if (moResolution != null)
      super.setProperty("resolution", moResolution, null);
    if (moPlane != null) {
      super.setProperty("plane", moPlane, null);
      if (moCutoff != null) {
        super.setProperty("red", new Float(-moCutoff.floatValue()), null);
        super.setProperty("blue", moCutoff, null);
      }
    } else {
      if (moCutoff != null)
        super.setProperty((moIsPositiveOnly ? "cutoffPositive" : "cutoff"),
            moCutoff, null);
      if (moColorNeg != null)
        super.setProperty("colorRGB", moColorNeg, null);
      if (moColorPos != null)
        super.setProperty("colorRGB", moColorPos, null);
    }
    super.setProperty("title", moTitleFormat, null);
    super.setProperty("fileName", viewer.getFileName(), null);
    super.setProperty("molecularOrbital", new Integer(moNumber), null);
    if (moPlane != null && moColorNeg != null)
      super.setProperty("colorRGB", moColorNeg, null);
    if (moPlane != null && moColorPos != null)
      super.setProperty("colorRGB", moColorPos, null);
     currentMesh.isColorSolid = false;
    if (moTranslucentLevel != null)
      super.setProperty("translucenctLevel", moTranslucentLevel, null);
    if (moTranslucency != null)
      super.setProperty("translucency", moTranslucency, null);
    super.setProperty("token", new Integer(moFill), null);
    super.setProperty("token",  new Integer(moMesh), null);
    super.setProperty("token",  new Integer(moDots), null);
    super.setProperty("token",  new Integer(moFrontOnly), null);
    thisModel.put("mesh", currentMesh);
    return;
  }

  public String getShapeState() {
    if (htModels == null)
      return "";
    StringBuffer s = new StringBuffer();
    for (int i = 0; i < modelCount; i++)
      s.append(getMoState(i));
    return s.toString();
  }

  private String getMoState(int modelIndex) {
    strID = getId(modelIndex);
    if (!getSettings(strID))
      return "";
    StringBuffer s = new StringBuffer();
    if (modelCount > 1)
      appendCmd(s, "frame " + viewer.getModelNumber(modelIndex));
    if (moCutoff != null)
      appendCmd(s, "mo cutoff " + (sg.getIsPositiveOnly() ? "+" : "")
          + moCutoff);
    if (moScale != null)
      appendCmd(s, "mo scale " + moScale);
    if (moResolution != null)
      appendCmd(s, "mo resolution " + moResolution);
    if (moPlane != null)
      appendCmd(s, "mo plane {" + moPlane.x + " " + moPlane.y + " " + moPlane.z
          + " " + moPlane.w + "}");
    if (moTitleFormat != null)
      appendCmd(s, "mo titleFormat " + Escape.escape(moTitleFormat));
    
    if (moColorNeg != null)
      appendCmd(s, "mo color "
          + Escape.escapeColor(moColorNeg.intValue())
          + (moColorNeg.equals(moColorPos) ? "" : " "
              + Escape.escapeColor(moColorPos.intValue())));
    appendCmd(s, "mo " + moNumber);
    if (moTranslucency != null)
      appendCmd(s, "mo translucent " + moTranslucentLevel);
    appendCmd(s, ((IsosurfaceMesh) thisModel.get("mesh")).getState(myType));
    return s.toString();
  }
}
