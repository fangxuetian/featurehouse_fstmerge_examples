
package org.jmol.jvxl.readers;

import org.jmol.api.Interface;
import org.jmol.api.SymmetryInterface;
import org.jmol.util.BinaryDocument;
import org.jmol.util.Logger;

class MrcBinaryReader extends VolumeFileReader {

  MrcHeader mrcHeader;
  MrcBinaryReader(SurfaceGenerator sg, String fileName, boolean isBigEndian) {
    super(sg, null);
    binarydoc = new BinaryDocument();
    binarydoc.setStream(sg.getAtomDataServer().getBufferedInputStream(fileName), isBigEndian);
    mrcHeader = new MrcHeader();
    
    params.insideOut = !params.insideOut;
  }
  
  private class MrcHeader {
    
    
    int nx, ny, nz, mode, nxStart, nyStart, nzStart, mx, my, mz;
    float a, b, c, alpha, beta, gamma;
    int mapc, mapr, maps;
    float dmin, dmax, dmean;
    int ispg;
    int nsymbt;
    byte[] extra = new byte[100];
    float originX, originY, originZ;
    byte[] map = new byte[4];
    byte[] machst = new byte[4];
    float rms;
    int nlabel;
    String[] labels = new String[10];
    SymmetryInterface unitCell;
    
    MrcHeader() {
      try {
        nx = binarydoc.readInt();
        ny = binarydoc.readInt();
        nz = binarydoc.readInt();
        
        Logger.info("MRC header: nx,ny,nz: " + nx + "," + ny + "," + nz);

        mode = binarydoc.readInt();
        Logger.info("MRC header: mode: " +mode);

        nxStart = binarydoc.readInt();
        nyStart = binarydoc.readInt();
        nzStart = binarydoc.readInt();
        
        Logger.info("MRC header: nxStart,nyStart,nzStart: " + nxStart + "," + nyStart + "," + nzStart);

        mx = binarydoc.readInt();
        my = binarydoc.readInt();
        mz = binarydoc.readInt();

        Logger.info("MRC header: mx,my,mz: " + mx + "," + my + "," + mz);

        a = binarydoc.readFloat();
        b = binarydoc.readFloat();
        c = binarydoc.readFloat();
        alpha = binarydoc.readFloat();
        beta = binarydoc.readFloat();
        gamma = binarydoc.readFloat();
        
        Logger.info("MRC header: a,b,c,alpha,beta,gamma: " + a + "," + b + "," + c + "," + alpha + "," + beta + "," + gamma);

        unitCell = (SymmetryInterface) Interface.getOptionInterface("symmetry.Symmetry");
        unitCell.setUnitCell(new float[] {a, b, c, alpha, beta, gamma} );

        mapc = binarydoc.readInt();
        mapr = binarydoc.readInt();
        maps = binarydoc.readInt();

        Logger.info("MRC header: mapc,mapr,maps: " + mapc + "," + mapr + "," + maps);
        
        dmin = binarydoc.readFloat();
        dmax = binarydoc.readFloat();
        dmean = binarydoc.readFloat();
        
        Logger.info("MRC header: dmin,dmax,dmean: " + dmin + "," + dmax + "," + dmean);
        
        ispg = binarydoc.readInt();
        nsymbt = binarydoc.readInt();
        
        Logger.info("MRC header: ispg,nsymbt: " + ispg + "," +  nsymbt);

        binarydoc.readByteArray(extra);

        originX = binarydoc.readFloat();
        originY = binarydoc.readFloat();
        originZ = binarydoc.readFloat();
        
        Logger.info("MRC header: originX,Y,Z: " + originX + "," + originY + "," + originZ);

        binarydoc.readByteArray(map);
        binarydoc.readByteArray(machst);
        
        rms = binarydoc.readFloat();
        
        Logger.info("MRC header: rms: " + rms);
        
        nlabel = binarydoc.readInt();
        byte[] temp = new byte[80];
        for (int i = 0; i < 10; i++) {
          binarydoc.readByteArray(temp);
          StringBuffer s = new StringBuffer();
          for (int j = 0; j < 80; j++)
            s.append((char)temp[j]);
          labels[i] = s.toString().trim();
        }
        
        Logger.info("MRC header: bytes read: " + binarydoc.getPosition());
        
        if (params.cutoffAutomatic) {
          params.cutoff = rms * 2 + dmean;
          Logger.info("MRC header: cutoff set to (dmean + 2*rms) = " + params.cutoff);
        }
        
      } catch (Exception e) {
        Logger.error("Error reading " + sg.getParams().fileName + " " + e.getMessage());
      }
    }
    
  }
  
  protected void readTitleLines() throws Exception {
    jvxlFileHeaderBuffer = new StringBuffer();
    jvxlFileHeaderBuffer.append("MRC DATA ").append(mrcHeader.labels[0]).append("\n");
    jvxlFileHeaderBuffer.append("see http://ami.scripps.edu/software/mrctools/mrc_specification.php\n");
    isAngstroms = true;
  }
  
  protected void readAtomCountAndOrigin() {
    VolumeFileReader.checkAtomLine(isXLowToHigh, isAngstroms, "0",
        "0 " + (mrcHeader.originX) + " " + (mrcHeader.originY) + " " +  (mrcHeader.originZ), 
        jvxlFileHeaderBuffer);
    volumetricOrigin.set(mrcHeader.originX, mrcHeader.originY, mrcHeader.originZ);
    if (isAnisotropic)
      setVolumetricOriginAnisotropy();
  }

  protected void readVoxelVector(int voxelVectorIndex) {
    
    
    int i = 0;
    switch (voxelVectorIndex) {
    case 0:
      i = mrcHeader.maps - 1;
      voxelCounts[i] = mrcHeader.nx;
      volumetricVectors[i].set(mrcHeader.a / mrcHeader.mx, 0, 0);
      break;
    case 1:
      i = mrcHeader.mapr - 1;
      voxelCounts[i] = mrcHeader.ny;
      volumetricVectors[i].set(0, mrcHeader.b / mrcHeader.my, 0);
      break;
    case 2:
      i = mrcHeader.mapc - 1;
      voxelCounts[i] = mrcHeader.nz;
      volumetricVectors[i].set(0, 0, mrcHeader.c / mrcHeader.mz);
      break;
    }
    if (isAnisotropic)
      setVectorAnisotropy(volumetricVectors[i]);
  }  
  
  protected float nextVoxel() throws Exception {
    float voxelValue;
    
    switch(mrcHeader.mode) {
    case 0:
      voxelValue = binarydoc.readByte();
      break;
    case 1:
      voxelValue = binarydoc.readShort();
      break;
    case 3:
      
      voxelValue = binarydoc.readShort();
      binarydoc.readShort();
      break;
    case 4:
      
      voxelValue = binarydoc.readFloat();
      binarydoc.readFloat();
      break;
    case 6:
      voxelValue = binarydoc.readUnsignedShort();
      break;
    default:
      voxelValue = binarydoc.readFloat();
    }
    nBytes = binarydoc.getPosition();
    return voxelValue;
  }

  byte[] b2 = new byte[2];
  byte[] b4 = new byte[4];
  protected void skipData(int nPoints) throws Exception {
    for (int i = 0; i < nPoints; i++)
      switch(mrcHeader.mode) {
      case 0:
        binarydoc.readByte();
        break;
      case 1:
      case 6:
        binarydoc.readByteArray(b2);
        break;
      case 4:
        binarydoc.readByteArray(b4);
        binarydoc.readByteArray(b4);
        break;
      default:
        binarydoc.readByteArray(b4);
      }
  }
}
