using System.Globalization;
using System.ComponentModel;
using System.Threading;
using System.Windows.Forms;
using System;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.InteropServices;
using WorldWind;
using WorldWind.Renderable;
using WorldWind.PluginEngine;
using System.Net;
using System.Net.Sockets;
using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
using System.Drawing;
using System.Xml;
using System.Data;
using System.Collections;
namespace GpsTrackerPlugin
{
 public class GpsTrackerPlugin : WorldWind.PluginEngine.Plugin
 {
  public string m_sVersion = "V04R00";
        public static string m_sPluginDirectory;
  private GpsTracker gpsTracker=null;
  public GPSTrackerOverlay gpsOverlay;
  public bool m_fGpsTrackerRunning=false;
  WindowsControlMenuButton m_MenuButton;
  public override void Load()
  {
            if (PluginDirectory.ToLower().EndsWith("gpstracker"))
                m_sPluginDirectory = PluginDirectory;
            else
                m_sPluginDirectory = PluginDirectory + "\\plugins\\gpstracker";
   gpsOverlay=null;
   pluginAddOverlay();
   gpsTracker = new GpsTracker(this);
            m_MenuButton = new WindowsControlMenuButton("Gps Tracker", m_sPluginDirectory + "\\gpstracker.png", this.gpsTracker);
   Application.WorldWindow.MenuBar.AddToolsMenuButton( m_MenuButton );
   base.Load();
  }
  public override void Unload()
  {
   gpsTracker.Close();
   Application.WorldWindow.MenuBar.RemoveToolsMenuButton(m_MenuButton);
   pluginRemoveOverlay();
   m_MenuButton=null;
   gpsTracker=null;
   gpsOverlay=null;
   base.Unload ();
  }
  public void pluginAddOverlay()
  {
   gpsOverlay = new GPSTrackerOverlay(this);
   gpsOverlay.Initialize(Application.WorldWindow.DrawArgs);
   Application.WorldWindow.CurrentWorld.RenderableObjects.Add(gpsOverlay);
   gpsOverlay.Update(Application.WorldWindow.DrawArgs);
  }
  public void pluginRemoveOverlay()
  {
   if (gpsOverlay!=null)
   {
    Application.WorldWindow.CurrentWorld.RenderableObjects.Remove(gpsOverlay.Name);
    gpsOverlay=null;
   }
  }
  public void pluginWorldWindowGotoLatLonHeading(double fLat, double fLon, float fHeading, int iAltitud)
  {
   if (fHeading==-1F || gpsTracker.m_bTrackHeading==false || gpsOverlay==null)
   {
    if (iAltitud==0)
     ParentApplication.WorldWindow.GotoLatLon(fLat,fLon);
    else
     ParentApplication.WorldWindow.GotoLatLonAltitude(fLat,fLon,(double)iAltitud*(double)1000);
   }
   else
   {
    if (gpsOverlay!=null && gpsTracker.m_bTrackHeading)
    {
     if (iAltitud==0)
      ParentApplication.WorldWindow.GotoLatLonHeadingAltitude(fLat, fLon, fHeading, gpsOverlay.drawArgs.WorldCamera.Altitude);
     else
      ParentApplication.WorldWindow.GotoLatLonHeadingAltitude(fLat,fLon,fHeading,(double)iAltitud*(double)1000);
    }
   }
  }
  public void pluginWorldWindowFocus()
  {
   ParentApplication.WorldWindow.Focus();
  }
  public void pluginWorldWindowInvalidate()
  {
   ParentApplication.WorldWindow.Invalidate();
  }
  public void pluginLocked(bool fLocked)
  {
  }
  public void pluginShowOverlay(GPSRenderInformation renderInformation)
  {
   renderInformation.iActiveTrack=gpsTracker.GetActiveTrack();
   gpsOverlay.ShowOverlay(renderInformation);
  }
  public void pluginShowFixInfo(string sText)
  {
   gpsOverlay.ShowFixInfo(sText);
  }
  public void pluginRemoveAllOverlay()
  {
   gpsOverlay.RemoveAllOverlay();
  }
  public void pluginSetActiveTrack(int iIndex, bool bTrack)
  {
   gpsTracker.SetActiveTrack(iIndex,bTrack);
  }
  public void pluginToggleTrackHeading()
  {
   if (gpsTracker.m_bTrackHeading==true)
    gpsTracker.SetTrackHeading(false);
   else
    gpsTracker.SetTrackHeading(true);
  }
  public void pluginToggleTrackLine()
  {
   if (gpsTracker.m_bTrackLine==true)
    gpsTracker.SetTrackLine(false);
   else
    gpsTracker.SetTrackLine(true);
  }
  public void pluginAddPOI(string sPOIName, float fLat, float fLon)
  {
   gpsTracker.AddPOI(sPOIName, fLat, fLon);
  }
 }
 public class GPSTrackerOverlay : RenderableObjectList
 {
  public static MainApplication ParentApplication;
  public GpsTrackerPlugin Plugin;
  public DrawArgs drawArgs;
  GPSTrackerFixInfo gpsTrackerInfo;
  private Object thisLock = new Object();
  public GPSIcon [] m_gpsIcons;
  public GPSIcon [] m_gpsPOI;
  public GPSTrackLine [] m_gpsTrack;
        private uint m_uIconResize;
        private uint m_uPOIResize;
        private uint m_uTrackResize;
  public int m_iGpsPOIIndex;
  public int m_iGpsIconIndex;
  public string m_sPOIName;
  [DllImport("user32")] public static extern int GetKeyboardState(byte [] pbKeyState);
  static int VK_LSHIFT = 0xA0;
  public GPSTrackerOverlay(GpsTrackerPlugin plugin) : base("GPSTracker")
  {
   this.Plugin = plugin;
            ParentApplication = plugin.ParentApplication;
            m_uIconResize=1;
            m_uPOIResize=1;
            m_uTrackResize=1;
            m_gpsIcons = new GPSIcon[m_uIconResize];
            m_gpsPOI = new GPSIcon[m_uTrackResize];
            m_gpsTrack = new GPSTrackLine[m_uTrackResize];
   gpsTrackerInfo=null;
   m_iGpsPOIIndex=0;
   m_iGpsIconIndex=0;
   IsOn = false;
  }
  public override void Initialize(DrawArgs drawArgs)
  {
   Dispose();
   this.drawArgs = ParentApplication.WorldWindow.DrawArgs;
   isInitialized = true;
  }
  public override void Render(DrawArgs drawArgs)
  {
   if(!IsOn)
    return;
   if(!isInitialized)
    return;
   base.Render(drawArgs);
  }
  public override bool PerformSelectionAction(DrawArgs drawArgs)
  {
   foreach(RenderableObject ro in ChildObjects)
   {
    if(!ro.IsOn || !ro.isSelectable)
     continue;
    if (ro.PerformSelectionAction(drawArgs))
     return true;
   }
   byte [] pbKeyState = new byte[256];
   GetKeyboardState(pbKeyState);
   if ((pbKeyState[VK_LSHIFT] & 0x80)==0x80)
   {
    Angle StartLatitude;
    Angle StartLongitude;
    drawArgs.WorldCamera.PickingRayIntersection(
     DrawArgs.LastMousePosition.X,
     DrawArgs.LastMousePosition.Y,
     out StartLatitude,
     out StartLongitude);
    if (!double.IsNaN(StartLatitude.Degrees) && !double.IsNaN(StartLongitude.Degrees))
    {
     POIName poiName = new POIName(this);
     m_sPOIName="";
     poiName.ShowDialog();
     poiName.Dispose();
     Plugin.pluginAddPOI(m_sPOIName, (float)StartLatitude.Degrees, (float)StartLongitude.Degrees);
    }
   }
   return false;
  }
  public override void Dispose()
  {
   isInitialized = false;
   RemoveAllOverlay();
   base.Dispose();
  }
  public bool GetPOI(int iIndex, out double fLat, out double fLon, out string sName, out string sIconFile)
  {
   bool bRet=false;
   fLat=0F;
   fLon=0F;
   sName="";
   sIconFile="";
   if (m_gpsPOI[iIndex]!=null)
   {
    fLat=m_gpsPOI[iIndex].m_RenderInfo.fLat;
    fLon=m_gpsPOI[iIndex].m_RenderInfo.fLon;
    sName=m_gpsPOI[iIndex].m_RenderInfo.sDescription;
    sIconFile=m_gpsPOI[iIndex].m_textureFileName;
    bRet=true;
   }
   return bRet;
  }
  public void ShowOverlay(GPSRenderInformation renderInformation)
  {
   lock (thisLock)
   {
    if (renderInformation.gpsTrack!=null)
    {
                    if (renderInformation.iIndex >= m_uTrackResize)
                    {
                        m_uTrackResize = (uint)renderInformation.iIndex+1;
                        Array.Resize(ref m_gpsTrack, (int)m_uTrackResize);
                    }
     if (m_gpsTrack[renderInformation.iIndex]==null)
                        m_gpsTrack[renderInformation.iIndex] = new GPSTrackLine(ParentApplication.WorldWindow.CurrentWorld, renderInformation.sDescription);
                    m_gpsTrack[renderInformation.iIndex].Initialize(this.drawArgs);
                    m_gpsTrack[renderInformation.iIndex].SetTrack(this, renderInformation.gpsTrack, renderInformation.sDescription, renderInformation.colorTrack, renderInformation.sIcon, renderInformation.bShowInfo);
     Add(m_gpsTrack[renderInformation.iIndex]);
    }
    else
    {
     if (renderInformation.bPOI==false)
     {
      int iIndex;
      for (iIndex=0; iIndex<m_iGpsIconIndex; iIndex++)
       if (renderInformation.sDescription==m_gpsIcons[iIndex].m_RenderInfo.sDescription)
        break;
      if (iIndex==m_iGpsIconIndex)
      {
                            if (m_iGpsIconIndex>=m_uIconResize)
                            {
                                m_uIconResize+=5;
                                Array.Resize(ref m_gpsIcons, (int)m_uIconResize);
                            }
       m_gpsIcons[iIndex] = new GPSIcon(this, renderInformation, ParentApplication.WorldWindow.CurrentWorld);
       m_gpsIcons[iIndex].Initialize(this.drawArgs);
       Add(m_gpsIcons[iIndex]);
       m_iGpsIconIndex++;
      }
      if (m_gpsIcons[iIndex].m_bTrack)
       Plugin.pluginWorldWindowGotoLatLonHeading(renderInformation.fLat,renderInformation.fLon,renderInformation.fHeading,renderInformation.iStartAltitud);
      m_gpsIcons[iIndex].SetGpsData(drawArgs, renderInformation);
     }
     else
     {
                        if (m_iGpsPOIIndex >= m_uPOIResize)
                        {
                            m_uPOIResize += 5;
                            Array.Resize(ref m_gpsPOI, (int)m_uPOIResize);
                        }
      if (m_gpsPOI[m_iGpsPOIIndex]==null)
      {
       m_gpsPOI[m_iGpsPOIIndex] = new GPSIcon(this, renderInformation.iIndex,renderInformation, ParentApplication.WorldWindow.CurrentWorld);
       m_gpsPOI[m_iGpsPOIIndex].Initialize(this.drawArgs);
       Add(m_gpsPOI[m_iGpsPOIIndex]);
      }
      m_iGpsPOIIndex++;
     }
    }
   }
  }
  public void RemoveAllOverlay()
  {
            for (int i = 0; i < m_iGpsPOIIndex; i++)
            {
                if (m_gpsPOI[i] != null)
                {
                    Remove(m_gpsPOI[i]);
                    m_gpsPOI[i].Dispose();
                    m_gpsPOI[i] = null;
                }
            }
            for (int i = 0; i < m_iGpsIconIndex; i++)
            {
                if (m_gpsIcons[i] != null)
                {
                    Remove(m_gpsIcons[i]);
                    m_gpsIcons[i].Dispose();
                    m_gpsIcons[i] = null;
                }
            }
            for (int i = 0; i < m_uTrackResize; i++)
   {
    if (m_gpsTrack[i]!=null)
    {
     Remove(m_gpsTrack[i]);
     m_gpsTrack[i].Dispose();
     m_gpsTrack[i] = null;
    }
   }
   m_iGpsPOIIndex=0;
   m_iGpsIconIndex=0;
   if (gpsTrackerInfo!=null)
   {
    Remove(gpsTrackerInfo);
    gpsTrackerInfo.Dispose();
    gpsTrackerInfo = null;
   }
  }
  public void ShowFixInfo(string sText)
  {
   if (gpsTrackerInfo==null)
   {
    gpsTrackerInfo = new GPSTrackerFixInfo();
    gpsTrackerInfo.Initialize(this.drawArgs);
    Add(gpsTrackerInfo);
   }
   gpsTrackerInfo.ShowFixInfo(sText);
   Update(this.drawArgs);
  }
  public void SetActiveTrack(int iIndex, bool bTrack)
  {
   Plugin.pluginSetActiveTrack(iIndex,bTrack);
  }
  public void ToggleTrackHeading()
  {
   Plugin.pluginToggleTrackHeading();
  }
  public void ToggleTrackLine()
  {
   Plugin.pluginToggleTrackLine();
  }
 }
 public class GPSTrackerFixInfo : RenderableObject
 {
  public string m_sText;
  public GPSTrackerFixInfo() : base("Fix Status", Vector3.Empty, Quaternion.Identity)
  {
   m_sText="GPSTracker: Active";
   this.RenderPriority = RenderPriority.Icons;
   this.IsOn = true;
  }
  public void ShowFixInfo(string sText)
  {
   if (sText=="" || m_sText=="" ||
    !(m_sText!="GPSTracker: Active" &&
    m_sText!="GPSTracker: Fix" &&
    m_sText!="GPSTracker: No Fix" &&
    (
     sText=="GPSTracker: Active" ||
     sText=="GPSTracker: Fix" ||
     sText=="GPSTracker: No Fix"
    )))
    m_sText=sText;
  }
  public override void Render(DrawArgs drawArgs)
  {
   String sInfo=m_sText;
   Rectangle bounds = drawArgs.toolbarFont.MeasureString(null, sInfo, DrawTextFormat.None, 0);
   int color = Color.Black.ToArgb();
   drawArgs.toolbarFont.DrawText(null, sInfo,drawArgs.screenWidth-bounds.Width-5+1, drawArgs.screenHeight-bounds.Height-5,color );
   drawArgs.toolbarFont.DrawText(null, sInfo,drawArgs.screenWidth-bounds.Width-5+1, drawArgs.screenHeight-bounds.Height-5+1,color );
   color = Color.Yellow.ToArgb();
   drawArgs.toolbarFont.DrawText(null, sInfo,drawArgs.screenWidth-bounds.Width-5, drawArgs.screenHeight-bounds.Height-5,color );
  }
  public override void Initialize(DrawArgs drawArgs)
  {
  }
  public override void Update(DrawArgs drawArgs)
  {
  }
  public override void Dispose()
  {
  }
  public override bool PerformSelectionAction(DrawArgs drawArgs)
  {
   return false;
  }
 }
 public class GPSTrackLine : RenderableObject
 {
        DrawArgs m_drawArgs;
  GPSTrack m_gpsTrack;
  World m_parentWorld;
  uint m_uVerticesCount;
  float m_fTotalDistance=0F;
        double m_fStartAlt;
        double m_fStartLatitud;
        double m_fStartLongitude;
        double m_fLatitudFrom;
        double m_fLatitudTo;
        double m_fLongitudeFrom;
        double m_fLongitudeTo;
        string m_sDescription;
        string m_sIconFileName;
  private CustomVertex.PositionColored[][] vertices = new CustomVertex.PositionColored[1000][];
  Vector3 v;
  Color m_colorTrack;
        int m_iTextureWidth;
        int m_iTextureHeight;
        int m_iIconWidth;
        int m_iIconHeight;
        int m_iIconWidthHalf;
        int m_iIconHeightHalf;
        Vector3 xyzPosition;
        float m_fHeightAboveSurface;
        Texture texture;
        Sprite sprite;
        Rectangle spriteSize;
        bool m_bShowInfo;
        [DllImport("user32")]
        public static extern int GetKeyboardState(byte[] pbKeyState);
        static int VK_LCONTROL = 0xA2;
        public GPSTrackLine(World parentWorld, string sDescription)
   : base(sDescription, parentWorld.Position, Quaternion.RotationYawPitchRoll(0,0,0))
  {
   m_parentWorld=parentWorld;
  }
  public void SetTrack(GPSTrackerOverlay gpsTrackerOverlay,GPSTrack gpsTrack,string sDescription,Color colorTrack, string sIcon, bool bShowInfo)
  {
   m_gpsTrack=gpsTrack;
            m_bShowInfo = bShowInfo;
   m_uVerticesCount=(gpsTrack.m_uPointCount/50000)+1;
   uint uCount=0;
   uint uLength=0;
   m_fTotalDistance=0;
   m_colorTrack=colorTrack;
            m_fHeightAboveSurface = 0F;
            m_fStartAlt = gpsTrack.m_fAlt[0];
            m_fStartLatitud = gpsTrack.m_fLat[0];
            m_fStartLongitude = gpsTrack.m_fLon[0];
            m_sDescription = sDescription;
            m_sIconFileName = sIcon;
   for (int i=0; i<m_uVerticesCount; i++)
   {
    if (((i+1)*50000)>gpsTrack.m_uPointCount)
     uLength=gpsTrack.m_uPointCount-uCount;
    else
     uLength=50000;
    this.vertices[i] = new CustomVertex.PositionColored[uLength];
    for(uint ii = 0; ii < uLength; ii++)
    {
     if (gpsTrack.m_fAlt[uCount]!=-1000000F && World.Settings.VerticalExaggeration>=1F)
     {
      float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(gpsTrack.m_fLat[uCount], gpsTrack.m_fLon[uCount], 100F / gpsTrackerOverlay.drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
      v=MathEngine.SphericalToCartesian(gpsTrack.m_fLat[uCount], gpsTrack.m_fLon[uCount], this.m_parentWorld.EquatorialRadius + ((gpsTrack.m_fAlt[uCount]* World.Settings.VerticalExaggeration)-elevation));
     }
     else
      v=MathEngine.SphericalToCartesian(gpsTrack.m_fLat[uCount], gpsTrack.m_fLon[uCount], this.m_parentWorld.EquatorialRadius);
     this.vertices[i][ii].X = v.X;
     this.vertices[i][ii].Y = v.Y;
     this.vertices[i][ii].Z = v.Z;
     this.vertices[i][ii].Color =m_colorTrack.ToArgb();
     v=gpsTrackerOverlay.drawArgs.WorldCamera.Project(v);
     if (uCount==0)
     {
                        m_fLatitudFrom = gpsTrack.m_fLat[uCount];
                        m_fLongitudeFrom = gpsTrack.m_fLon[uCount];
     }
     else
     {
                        m_fLatitudTo = gpsTrack.m_fLat[uCount];
                        m_fLongitudeTo = gpsTrack.m_fLon[uCount];
                        double dDistance, dLatTo, dLatFrom, dLonFrom, dLonTo, deltaLon, deltaLat;
                        dLatTo = m_fLatitudTo;
                        dLatFrom = m_fLatitudFrom;
                        dLonTo = m_fLongitudeTo;
                        dLonFrom = m_fLongitudeFrom;
                        dLatFrom = dLatFrom * (Math.PI / 180);
                        dLatTo = dLatTo * (Math.PI / 180);
                        dLonFrom = dLonFrom * (Math.PI / 180);
                        dLonTo = dLonTo * (Math.PI / 180);
                        deltaLon = dLonTo - dLonFrom;
                        deltaLat = dLatTo - dLatFrom;
                        if (deltaLon == 0 && deltaLat == 0)
                            dDistance = (double)0;
                        else
                            dDistance = Math.Acos(Math.Sin(dLatFrom) * Math.Sin(dLatTo) + Math.Cos(dLatFrom) * Math.Cos(dLatTo) * Math.Cos(deltaLon)) * 6371.0;
                        if (double.IsNaN(dDistance) == false)
                            m_fTotalDistance = m_fTotalDistance + (float)dDistance;
                        m_fLatitudFrom = m_fLatitudTo;
                        m_fLongitudeFrom = m_fLongitudeTo;
     }
     uCount++;
    }
   }
            try
            {
                this.texture = TextureLoader.FromFile(m_drawArgs.device, this.m_sIconFileName, 0, 0, 1, 0, Format.Unknown, Pool.Managed, Filter.Box, Filter.Box, 0);
            }
            catch (Microsoft.DirectX.Direct3D.InvalidDataException)
            {
                this.texture = TextureLoader.FromFile(m_drawArgs.device, GpsTrackerPlugin.m_sPluginDirectory + "\\gpsx.png", 0, 0, 1, 0, Format.Unknown, Pool.Managed, Filter.Box, Filter.Box, 0);
            }
            using (Surface s = this.texture.GetSurfaceLevel(0))
            {
                SurfaceDescription desc = s.Description;
                this.m_iTextureWidth = desc.Width;
                this.m_iTextureHeight = desc.Height;
                this.m_iIconWidth = desc.Width;
                this.m_iIconHeight = desc.Height;
                this.m_iIconWidthHalf = desc.Width / 2;
                this.m_iIconHeightHalf = desc.Height / 2;
                this.spriteSize = new Rectangle(0, 0, desc.Width, desc.Height);
            }
            this.isSelectable = true;
            this.sprite = new Sprite(m_drawArgs.device);
   this.isInitialized = true;
   this.RenderPriority = RenderPriority.Icons;
  }
        public override void Initialize(DrawArgs drawArgs)
        {
            this.isInitialized = true;
            m_drawArgs = drawArgs;
        }
  public override void Update(DrawArgs drawArgs)
  {
   if(!this.isInitialized)
    this.Initialize(drawArgs);
  }
  public override void Dispose()
  {
   this.isInitialized = false;
  }
        public override bool PerformSelectionAction(DrawArgs drawArgs)
        {
            bool bRet = false;
            if (m_fStartAlt != -1000000F && World.Settings.VerticalExaggeration >= 1F)
            {
                float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(m_fStartLatitud, m_fStartLongitude, 100F / drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
                xyzPosition = MathEngine.SphericalToCartesian(m_fStartLatitud, m_fStartLongitude, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius + ((m_fStartLatitud * World.Settings.VerticalExaggeration) - elevation));
            }
            else
                xyzPosition = MathEngine.SphericalToCartesian(m_fStartLatitud, m_fStartLongitude, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius);
            if (!drawArgs.WorldCamera.ViewFrustum.ContainsPoint(xyzPosition))
                return false;
            Vector3 translationVector = new Vector3(
                (float)(xyzPosition.X - drawArgs.WorldCamera.ReferenceCenter.X),
                (float)(xyzPosition.Y - drawArgs.WorldCamera.ReferenceCenter.Y),
                (float)(xyzPosition.Z - drawArgs.WorldCamera.ReferenceCenter.Z));
            Vector3 projectedPoint = drawArgs.WorldCamera.Project(translationVector);
   try
            {
                if (Math.Abs(DrawArgs.LastMousePosition.X - projectedPoint.X) < m_iIconWidthHalf &&
                    Math.Abs(DrawArgs.LastMousePosition.Y - projectedPoint.Y) < m_iIconHeightHalf)
                {
                    byte[] pbKeyState = new byte[256];
                    GetKeyboardState(pbKeyState);
                    if ((pbKeyState[VK_LCONTROL] & 0x80) == 0x80)
                    {
                        if (m_bShowInfo)
                            m_bShowInfo = false;
                        else
                            m_bShowInfo = true;
                    }
                    bRet = true;
                    Update(drawArgs);
                }
            }
   catch
   {
   }
            return bRet;
        }
  public override void Render(DrawArgs drawArgs)
  {
   if(this.isInitialized)
   {
    drawArgs.device.RenderState.ZBufferEnable = true;
    drawArgs.device.VertexFormat = CustomVertex.PositionColored.Format;
    drawArgs.device.TextureState[0].ColorOperation = TextureOperation.Disable;
                drawArgs.device.Transform.World = Matrix.Translation(
                    (float)-drawArgs.WorldCamera.ReferenceCenter.X,
                    (float)-drawArgs.WorldCamera.ReferenceCenter.Y,
                    (float)-drawArgs.WorldCamera.ReferenceCenter.Z
                    );
    for (uint i=0; i<m_uVerticesCount; i++)
     drawArgs.device.DrawUserPrimitives(PrimitiveType.LineStrip, this.vertices[i].Length - 1, this.vertices[i]);
                if (m_fStartAlt != -1000000F && World.Settings.VerticalExaggeration >= 1F)
                {
                    float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(m_fStartLatitud, m_fStartLongitude, 100F / m_drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
                    xyzPosition = MathEngine.SphericalToCartesian(m_fStartLatitud, m_fStartLongitude, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius + ((m_fStartLatitud * World.Settings.VerticalExaggeration) - elevation));
                }
                else
                    xyzPosition = MathEngine.SphericalToCartesian(m_fStartLatitud, m_fStartLongitude, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius);
       if(!drawArgs.WorldCamera.ViewFrustum.ContainsPoint(xyzPosition))
        return;
                Vector3 translationVector = new Vector3(
                    (float)(xyzPosition.X - drawArgs.WorldCamera.ReferenceCenter.X),
                    (float)(xyzPosition.Y - drawArgs.WorldCamera.ReferenceCenter.Y),
                    (float)(xyzPosition.Z - drawArgs.WorldCamera.ReferenceCenter.Z));
                Vector3 projectedPoint = drawArgs.WorldCamera.Project(translationVector);
       this.sprite.Begin(SpriteFlags.AlphaBlend);
       this.sprite.Transform = Matrix.Transformation2D(
        new Vector2(0.0f, 0.0f),
        0.0f,
        new Vector2((float)1,(float)1),
        new Vector2(0,0),
        0.0f,
        new Vector2(projectedPoint.X + (m_iIconWidthHalf), projectedPoint.Y + (m_iIconHeightHalf)));
       this.sprite.Draw(this.texture, this.spriteSize,
        new Vector3(this.m_iIconWidth,this.m_iIconHeight,0),
        new Vector3(0,0,0),
        Color.FromArgb(180,255,255,255).ToArgb());
       this.sprite.End();
                bool sShowInfo = false;
                if (Math.Abs(DrawArgs.LastMousePosition.X - projectedPoint.X) < m_iIconWidthHalf &&
                    Math.Abs(DrawArgs.LastMousePosition.Y - projectedPoint.Y) < m_iIconHeightHalf)
                {
                    DrawArgs.MouseCursor = CursorType.Hand;
                    sShowInfo = true;
                }
                if (m_bShowInfo || sShowInfo)
                {
                    string sInfo = "Track: " + m_sDescription + "\n";
                    string sNS;
                    if (m_fStartLatitud >= (float)0)
                        sNS = "N";
                    else
                        sNS = "S";
                    double dLat = Math.Abs(m_fStartLatitud);
                    double dWhole = Math.Floor(dLat);
                    double dFraction = dLat - dWhole;
                    double dMin = dFraction * (double)60;
                    double dMinWhole = Math.Floor(dMin);
                    double dSeconds = (dMin - dMinWhole) * (double)60;
                    int iDegrees = Convert.ToInt32(dWhole);
                    int iMinutes = Convert.ToInt32(dMinWhole);
                    float fSeconds = Convert.ToSingle(dSeconds);
                    sInfo += "Lat: " + Convert.ToString(iDegrees) + "" + Convert.ToString(iMinutes) + "'" + Convert.ToString(fSeconds) + "\" " + sNS + "\n";
                    string sEW;
                    if (m_fStartLongitude >= (float)0)
                        sEW = "E";
                    else
                        sEW = "W";
                    double dLon = Math.Abs(m_fStartLongitude);
                    dWhole = Math.Floor(dLon);
                    dFraction = dLon - dWhole;
                    dMin = dFraction * (double)60;
                    dMinWhole = Math.Floor(dMin);
                    dSeconds = (dMin - dMinWhole) * (double)60;
                    iDegrees = Convert.ToInt32(dWhole);
                    iMinutes = Convert.ToInt32(dMinWhole);
                    fSeconds = Convert.ToSingle(dSeconds);
                    sInfo += "Lon: " + Convert.ToString(iDegrees) + "" + Convert.ToString(iMinutes) + "'" + Convert.ToString(fSeconds) + "\" " + sEW + "\n";
                    sInfo += "Length: " + m_fTotalDistance + "km.";
                    int color = Color.Black.ToArgb();
                    drawArgs.toolbarFont.DrawText(null, sInfo, (int)projectedPoint.X + (m_iIconWidthHalf) + 11, (int)(projectedPoint.Y), color);
                    drawArgs.toolbarFont.DrawText(null, sInfo, (int)projectedPoint.X + (m_iIconWidthHalf) + 11, (int)(projectedPoint.Y) + 1, color);
                    color = Color.Yellow.ToArgb();
                    drawArgs.toolbarFont.DrawText(null, sInfo, (int)projectedPoint.X + (m_iIconWidthHalf) + 10, (int)(projectedPoint.Y), color);
                }
   }
  }
 }
 public class GPSIcon : RenderableObject
 {
  GPSTrackerOverlay m_gpsTrackerOverlay;
  public GPSRenderInformation m_RenderInfo;
  public string m_sDescriptionFrom;
  private bool m_bSignalDistance;
  public double m_fLatitudeFrom;
  public double m_fLongitudeFrom;
  bool m_bShowInfo;
  bool m_bTrackLine;
  float m_fLastAlt;
  float m_fLastRoll;
  float m_fLastDepth;
  float m_fLastPitch;
  float m_fLastSpeed;
  float m_fLastESpeed;
  float m_fLastNSpeed;
  float m_fLastVSpeed;
  float m_fLastHeading;
  int m_iLastHour;
  int m_iLastMin;
  float m_fLastSec;
  int m_iLastDay;
  int m_iLastMonth;
  int m_iLastYear;
  float m_fHeightAboveSurface;
  World m_parentWorld;
  public string m_textureFileName;
  int m_iTextureWidth;
  int m_iTextureHeight;
  int m_iIconWidth;
  int m_iIconHeight;
  int m_iIconWidthHalf;
  int m_iIconHeightHalf;
  Vector3 xyzPosition;
  static int hotColor = System.Drawing.Color.White.ToArgb();
  static int normalColor = Color.FromArgb(180,255,255,255).ToArgb();
  uint m_uVerticesCount=0;
  uint m_uPointCount=0;
  float m_fTotalDistance=0;
  double m_fLatFrom;
  double m_fLonFrom;
  uint m_uTotalPointCount=0;
  private CustomVertex.PositionColored[][] vertices = new CustomVertex.PositionColored[20][];
  public bool m_bTrack;
  Texture texture;
  Sprite sprite;
  Rectangle spriteSize;
        [DllImport("user32")]
        public static extern int GetKeyboardState(byte[] pbKeyState);
        static int VK_LCONTROL = 0xA2;
        static int VK_RCONTROL = 0xA3;
        static int VK_LALT = 0xA4;
        static int VK_RALT = 0xA5;
  public GPSIcon(
   GPSTrackerOverlay gpsTrackerOverlay,
   int iIndex,
   GPSRenderInformation renderInformation,
   World parentWorld)
   : base(renderInformation.sDescription, parentWorld.Position, Quaternion.RotationYawPitchRoll(0,0,0))
  {
   m_RenderInfo=renderInformation;
   m_RenderInfo.bPOI=true;
   this.m_gpsTrackerOverlay=gpsTrackerOverlay;
   m_RenderInfo.iIndex=iIndex;
   this.m_fHeightAboveSurface = 0F;
   this.m_parentWorld = parentWorld;
   this.m_textureFileName = m_RenderInfo.sIcon;
   this.m_iTextureWidth = 32;
   this.m_iTextureHeight = 32;
   this.m_iIconWidth = 32;
   this.m_iIconHeight = 32;
   this.m_iIconWidthHalf = m_iIconWidth/2;
   this.m_iIconHeightHalf = m_iIconHeight/2;
   this.m_bTrack=renderInformation.fTrack;
   m_bShowInfo = m_RenderInfo.bShowInfo;
   m_fLatitudeFrom=1000F;
   m_fLongitudeFrom=1000F;
   m_sDescriptionFrom="";
   m_bSignalDistance=false;
   m_uVerticesCount=0;
   m_uPointCount=0;
   m_fTotalDistance=0;
   m_uTotalPointCount=0;
   this.RenderPriority = RenderPriority.Icons;
  }
  public GPSIcon(
   GPSTrackerOverlay gpsTrackerOverlay,
   GPSRenderInformation renderInformation,
   World parentWorld)
   : base(renderInformation.sDescription, parentWorld.Position, Quaternion.RotationYawPitchRoll(0,0,0))
  {
   m_RenderInfo=renderInformation;
   m_RenderInfo.bPOI=false;
   this.m_gpsTrackerOverlay=gpsTrackerOverlay;
   this.m_fHeightAboveSurface = 0F;
   this.m_parentWorld = parentWorld;
   this.m_textureFileName = m_RenderInfo.sIcon;
   this.m_iTextureWidth = 32;
   this.m_iTextureHeight = 32;
   this.m_iIconWidth = 32;
   this.m_iIconHeight = 32;
   this.m_iIconWidthHalf = m_iIconWidth/2;
   this.m_iIconHeightHalf = m_iIconHeight/2;
   m_bSignalDistance=false;
   this.m_bTrack=renderInformation.fTrack;
   m_bShowInfo = m_RenderInfo.bShowInfo;
   m_fLastAlt=-1000000F;
   m_fLastRoll=-1000F;
   m_fLastDepth=-1000000F;
   m_fLastPitch=-1000F;
   m_fLastSpeed=-1F;
   m_fLastESpeed=-1000000F;
   m_fLastNSpeed=-1000000F;
   m_fLastVSpeed=-1000000F;
   m_fLastHeading=-1F;
   m_iLastHour=-1;
   m_iLastMin=-1;
   m_fLastSec=(float)-1;
   m_iLastDay=-1;
   m_iLastMonth=-1;
   m_iLastYear=-1;
   m_fLatitudeFrom=1000F;
   m_fLongitudeFrom=1000F;
   m_sDescriptionFrom="";
   m_uVerticesCount=0;
   m_uPointCount=0;
   m_fTotalDistance=0;
   m_uTotalPointCount=0;
   for (int i=0; i<m_gpsTrackerOverlay.m_iGpsIconIndex; i++)
    if (m_gpsTrackerOverlay.m_gpsIcons[i].m_RenderInfo.iIndex==m_RenderInfo.iIndex &&
     m_gpsTrackerOverlay.m_gpsIcons[i].m_bTrack &&
     m_bTrack)
     m_bTrack=false;
   this.RenderPriority = RenderPriority.Icons;
  }
  public override void Initialize(DrawArgs drawArgs)
  {
   this.isInitialized = true;
   try
   {
    this.texture = TextureLoader.FromFile(drawArgs.device, this.m_textureFileName, 0, 0, 1, 0, Format.Unknown, Pool.Managed, Filter.Box, Filter.Box, 0);
   }
   catch(Microsoft.DirectX.Direct3D.InvalidDataException)
   {
    this.texture = TextureLoader.FromFile(drawArgs.device, GpsTrackerPlugin.m_sPluginDirectory + "\\gpsx.png", 0, 0, 1, 0, Format.Unknown, Pool.Managed, Filter.Box, Filter.Box, 0);
   }
   using(Surface s = this.texture.GetSurfaceLevel(0))
   {
    SurfaceDescription desc = s.Description;
    this.m_iTextureWidth = desc.Width;
    this.m_iTextureHeight = desc.Height;
    this.m_iIconWidth = desc.Width;
    this.m_iIconHeight = desc.Height;
    this.m_iIconWidthHalf = desc.Width/2;
    this.m_iIconHeightHalf = desc.Height/2;
    this.spriteSize = new Rectangle(0,0, desc.Width, desc.Height);
   }
   this.isSelectable = true;
   this.sprite = new Sprite(drawArgs.device);
   if (m_RenderInfo.fAlt!=-1000000F && World.Settings.VerticalExaggeration>=1F)
   {
    float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(m_RenderInfo.fLat, m_RenderInfo.fLon, 100F / drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
    xyzPosition=MathEngine.SphericalToCartesian(m_RenderInfo.fLat, m_RenderInfo.fLon, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius + ((m_RenderInfo.fAlt* World.Settings.VerticalExaggeration)-elevation));
   }
   else
    xyzPosition=MathEngine.SphericalToCartesian(m_RenderInfo.fLat, m_RenderInfo.fLon, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius);
  }
  public override void Update(DrawArgs drawArgs)
  {
   if(!this.isInitialized)
    this.Initialize(drawArgs);
  }
  public override void Dispose()
  {
   this.isInitialized = false;
   if(this.texture != null)
    this.texture.Dispose();
   if(this.sprite != null)
    this.sprite.Dispose();
  }
  public override bool PerformSelectionAction(DrawArgs drawArgs)
  {
   bool bRet=false;
   if (m_RenderInfo.fAlt!=-1000000F && World.Settings.VerticalExaggeration>=1F)
   {
    float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(m_RenderInfo.fLat, m_RenderInfo.fLon, 100F / drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
    xyzPosition=MathEngine.SphericalToCartesian(m_RenderInfo.fLat, m_RenderInfo.fLon, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius + ((m_RenderInfo.fAlt* World.Settings.VerticalExaggeration)-elevation));
   }
   else
    xyzPosition=MathEngine.SphericalToCartesian(m_RenderInfo.fLat, m_RenderInfo.fLon, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius);
   if(!drawArgs.WorldCamera.ViewFrustum.ContainsPoint(xyzPosition))
    return false;
            Vector3 translationVector = new Vector3(
                (float)(xyzPosition.X - drawArgs.WorldCamera.ReferenceCenter.X),
                (float)(xyzPosition.Y - drawArgs.WorldCamera.ReferenceCenter.Y),
                (float)(xyzPosition.Z - drawArgs.WorldCamera.ReferenceCenter.Z));
            Vector3 projectedPoint = drawArgs.WorldCamera.Project(translationVector);
   try
   {
    if(Math.Abs(DrawArgs.LastMousePosition.X-projectedPoint.X)<m_iIconWidthHalf &&
     Math.Abs(DrawArgs.LastMousePosition.Y-projectedPoint.Y)<m_iIconHeightHalf)
    {
     byte [] pbKeyState = new byte[256];
     GetKeyboardState(pbKeyState);
     if ((pbKeyState[VK_LCONTROL] & 0x80)==0x80)
     {
      if (m_bShowInfo)
       m_bShowInfo=false;
      else
       m_bShowInfo=true;
     }
     else
      if ((pbKeyState[VK_RCONTROL] & 0x80)==0x80 && m_RenderInfo.bPOI==false)
     {
      m_gpsTrackerOverlay.ToggleTrackHeading();
     }
     else
      if ((pbKeyState[VK_LALT] & 0x80)==0x80 && m_RenderInfo.bPOI==false)
     {
      if (m_bTrackLine)
       m_bTrackLine=false;
      else
       m_bTrackLine=true;
      m_gpsTrackerOverlay.ToggleTrackLine();
     }
     else
      if ((pbKeyState[VK_RALT] & 0x80)==0x80 && m_RenderInfo.bPOI==false)
     {
      if (m_bSignalDistance)
      {
       m_bSignalDistance=false;
       for (int i=0; i<m_gpsTrackerOverlay.m_iGpsPOIIndex; i++)
       {
        if (m_gpsTrackerOverlay.m_gpsPOI[i]!=null)
        {
         m_gpsTrackerOverlay.m_gpsPOI[i].m_fLatitudeFrom=1000F;
         m_gpsTrackerOverlay.m_gpsPOI[i].m_fLongitudeFrom=1000F;
         m_gpsTrackerOverlay.m_gpsPOI[i].m_sDescriptionFrom="";
        }
       }
      }
      else
      {
       for (int i=0; i<m_gpsTrackerOverlay.m_iGpsIconIndex; i++)
        if (m_gpsTrackerOverlay.m_gpsIcons[i]!=null)
         m_gpsTrackerOverlay.m_gpsIcons[i].m_bSignalDistance=false;
       for (int i=0; i<m_gpsTrackerOverlay.m_iGpsPOIIndex; i++)
       {
        if (m_gpsTrackerOverlay.m_gpsPOI[i]!=null)
        {
         m_bSignalDistance=true;
         m_gpsTrackerOverlay.m_gpsPOI[i].m_fLatitudeFrom=m_RenderInfo.fLat;
         m_gpsTrackerOverlay.m_gpsPOI[i].m_fLongitudeFrom=m_RenderInfo.fLon;
         m_gpsTrackerOverlay.m_gpsPOI[i].m_sDescriptionFrom=m_RenderInfo.sDescription;
        }
       }
      }
     }
     else
      if ((pbKeyState[VK_RALT] & 0x80)==0x80 && m_RenderInfo.bPOI==true)
     {
      if (m_bSignalDistance)
      {
       m_bSignalDistance=false;
       for (int i=0; i<m_gpsTrackerOverlay.m_iGpsIconIndex; i++)
       {
        if (m_gpsTrackerOverlay.m_gpsIcons[i]!=null)
        {
         m_gpsTrackerOverlay.m_gpsIcons[i].m_fLatitudeFrom=1000F;
         m_gpsTrackerOverlay.m_gpsIcons[i].m_fLongitudeFrom=1000F;
         m_gpsTrackerOverlay.m_gpsIcons[i].m_sDescriptionFrom="";
        }
       }
      }
      else
      {
       for (int i=0; i<m_gpsTrackerOverlay.m_iGpsPOIIndex; i++)
        if (m_gpsTrackerOverlay.m_gpsPOI[i]!=null)
         m_gpsTrackerOverlay.m_gpsPOI[i].m_bSignalDistance=false;
       for (int i=0; i<m_gpsTrackerOverlay.m_iGpsIconIndex; i++)
       {
        if (m_gpsTrackerOverlay.m_gpsIcons[i]!=null)
        {
         m_bSignalDistance=true;
         m_gpsTrackerOverlay.m_gpsIcons[i].m_fLatitudeFrom=m_RenderInfo.fLat;
         m_gpsTrackerOverlay.m_gpsIcons[i].m_fLongitudeFrom=m_RenderInfo.fLon;
         m_gpsTrackerOverlay.m_gpsIcons[i].m_sDescriptionFrom=m_RenderInfo.sDescription;
        }
       }
      }
     }
     else
     {
      bool bOn=true;
      if (m_bTrack)
       bOn=false;
      for (int i=0; i<m_gpsTrackerOverlay.m_iGpsIconIndex; i++)
       m_gpsTrackerOverlay.m_gpsIcons[i].m_bTrack=false;
      for (int i=0; i<m_gpsTrackerOverlay.m_iGpsPOIIndex; i++)
       m_gpsTrackerOverlay.m_gpsPOI[i].m_bTrack=false;
      m_bTrack=bOn;
      m_gpsTrackerOverlay.SetActiveTrack(m_RenderInfo.iIndex,bOn);
     }
     bRet=true;
    }
   }
   catch
   {
   }
   return bRet;
  }
  public override void Render(DrawArgs drawArgs)
  {
   if(!this.isInitialized || this.texture == null)
    return;
   int color = normalColor;
   float fDistance=-1F;
   if (m_RenderInfo.bPOI)
   {
    if (m_bTrack)
     color = hotColor;
                xyzPosition = MathEngine.SphericalToCartesian(this.m_RenderInfo.fLat, this.m_RenderInfo.fLon, this.m_parentWorld.EquatorialRadius);
   }
   else
   {
    if(m_bTrackLine && m_uPointCount>1)
    {
     drawArgs.device.RenderState.ZBufferEnable = false;
     drawArgs.device.TextureState[0].ColorOperation = TextureOperation.Disable;
     drawArgs.device.VertexFormat = CustomVertex.PositionColored.Format;
                    drawArgs.device.Transform.World = Matrix.Translation(
                        (float)-drawArgs.WorldCamera.ReferenceCenter.X,
                        (float)-drawArgs.WorldCamera.ReferenceCenter.Y,
                        (float)-drawArgs.WorldCamera.ReferenceCenter.Z
                        );
     for (uint i=0; i<=m_uVerticesCount; i++)
     {
      if (m_uPointCount<50000)
       drawArgs.device.DrawUserPrimitives(PrimitiveType.LineStrip, (int)m_uPointCount - 1, this.vertices[i]);
      else
       drawArgs.device.DrawUserPrimitives(PrimitiveType.LineStrip, this.vertices[i].Length - 1, this.vertices[i]);
     }
    }
    if (m_bTrack)
     color = hotColor;
    if (m_RenderInfo.fAlt!=-1000000F && World.Settings.VerticalExaggeration>=1F)
    {
     float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(m_RenderInfo.fLat,m_RenderInfo.fLon, 100F / drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
     xyzPosition = MathEngine.SphericalToCartesian(this.m_RenderInfo.fLat, this.m_RenderInfo.fLon, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius + ((m_RenderInfo.fAlt*World.Settings.VerticalExaggeration)-elevation));
    }
    else
     xyzPosition = MathEngine.SphericalToCartesian(this.m_RenderInfo.fLat, this.m_RenderInfo.fLon, this.m_fHeightAboveSurface + this.m_parentWorld.EquatorialRadius);
   }
   if(!drawArgs.WorldCamera.ViewFrustum.ContainsPoint(xyzPosition))
    return;
            Vector3 translationVector = new Vector3(
                (float)(xyzPosition.X - drawArgs.WorldCamera.ReferenceCenter.X),
                (float)(xyzPosition.Y - drawArgs.WorldCamera.ReferenceCenter.Y),
                (float)(xyzPosition.Z - drawArgs.WorldCamera.ReferenceCenter.Z));
            Vector3 projectedPoint = drawArgs.WorldCamera.Project(translationVector);
   if(Math.Abs(DrawArgs.LastMousePosition.X-projectedPoint.X)<m_iIconWidthHalf &&
    Math.Abs(DrawArgs.LastMousePosition.Y-projectedPoint.Y)<m_iIconHeightHalf)
   {
    if (color==normalColor)
     color = hotColor;
    else
     color=normalColor;
    DrawArgs.MouseCursor = CursorType.Hand;
    ShowInfo(drawArgs, projectedPoint, fDistance );
   }
   if (m_bShowInfo)
    ShowInfo(drawArgs, projectedPoint , fDistance);
   this.sprite.Begin(SpriteFlags.AlphaBlend);
   this.sprite.Transform = Matrix.Transformation2D(
    new Vector2(0.0f, 0.0f),
    0.0f,
    new Vector2((float)1,(float)1),
    new Vector2(0,0),
    0.0f,
    new Vector2(projectedPoint.X + (m_iIconWidthHalf), projectedPoint.Y + (m_iIconHeightHalf)));
   this.sprite.Draw(this.texture, this.spriteSize,
    new Vector3(this.m_iIconWidth,this.m_iIconHeight,0),
    new Vector3(0,0,0),
    color);
   this.sprite.End();
  }
  void ShowInfo(DrawArgs drawArgs, Vector3 projectedPoint, float fDistance )
  {
   int color = Color.Black.ToArgb();
   string sInfo = m_RenderInfo.sDescription + "\n";
   string sNS;
   if (m_RenderInfo.fLat>=(float)0)
    sNS="N";
   else
    sNS="S";
   double dLat=Math.Abs(m_RenderInfo.fLat);
   double dWhole = Math.Floor(dLat);
   double dFraction = dLat- dWhole;
   double dMin = dFraction * (double)60;
   double dMinWhole = Math.Floor(dMin);
   double dSeconds = (dMin - dMinWhole) * (double)60;
   int iDegrees = Convert.ToInt32(dWhole);
   int iMinutes = Convert.ToInt32(dMinWhole);
   float fSeconds = Convert.ToSingle(dSeconds);
   sInfo += "Lat: " + Convert.ToString(iDegrees) + "" + Convert.ToString(iMinutes) + "'" + Convert.ToString(fSeconds) + "\" " + sNS + "\n";
   string sEW;
   if (m_RenderInfo.fLon>=(float)0)
    sEW="E";
   else
    sEW="W";
   double dLon=Math.Abs(m_RenderInfo.fLon);
   dWhole = Math.Floor(dLon);
   dFraction = dLon- dWhole;
   dMin = dFraction * (double)60;
   dMinWhole = Math.Floor(dMin);
   dSeconds = (dMin - dMinWhole) * (double)60;
   iDegrees = Convert.ToInt32(dWhole);
   iMinutes = Convert.ToInt32(dMinWhole);
   fSeconds = Convert.ToSingle(dSeconds);
   sInfo += "Lon: " + Convert.ToString(iDegrees) + "" + Convert.ToString(iMinutes) + "'" + Convert.ToString(fSeconds) + "\" " + sEW + "\n";
   if (m_RenderInfo.bPOI==true && m_sDescriptionFrom!="")
   {
                double dDistance = CalculateDistance(m_fLatitudeFrom, m_fLongitudeFrom, m_RenderInfo.fLat, m_RenderInfo.fLon);
                if (double.IsNaN(dDistance) == false)
                {
                    string sUnit = (dDistance >= 1F) ? "km" : "m";
                    dDistance = (dDistance < 1F) ? (dDistance * 1000F) : dDistance;
                    sInfo += "Distance From " + m_sDescriptionFrom + ": " + Convert.ToString(decimal.Round(Convert.ToDecimal(dDistance), 3)) +
                        sUnit +
                        "\n";
                }
   }
   else
   if (m_RenderInfo.bPOI==false)
   {
    if (m_RenderInfo.fAlt==-1000000F)
    {
     if (m_fLastAlt!=-1000000F)
      sInfo += "Altitud: " + Convert.ToString(m_fLastAlt) + " " + m_RenderInfo.sAltUnit + " (Last Known)\n";
    }
    else
    {
     sInfo += "Altitud: " + Convert.ToString(m_RenderInfo.fAlt) + " " + m_RenderInfo.sAltUnit + "\n";
     m_fLastAlt=m_RenderInfo.fAlt;
    }
    if (m_RenderInfo.fDepth==-1000000F)
    {
     if (m_fLastDepth!=-1000000F)
      sInfo += "Depth: " + Convert.ToString(m_fLastDepth) + " meters (Last Known)\n";
    }
    else
    {
     sInfo += "Depth: " + Convert.ToString(m_RenderInfo.fDepth) + " meters\n";
     m_fLastDepth=m_RenderInfo.fDepth;
    }
    if (m_RenderInfo.fRoll==-1000F)
    {
     if (m_fLastRoll!=-1000F)
      sInfo += "Roll: " + Convert.ToString(m_fLastRoll) + " (Last Known)\n";
    }
    else
    {
     sInfo += "Roll: " + Convert.ToString(m_RenderInfo.fRoll) + "\n";
     m_fLastRoll=m_RenderInfo.fRoll;
    }
    if (m_RenderInfo.fPitch==-1000F)
    {
     if (m_fLastPitch!=-1000F)
      sInfo += "Pitch: " + Convert.ToString(m_fLastPitch) + " (Last Known)\n";
    }
    else
    {
     sInfo += "Pitch: " + Convert.ToString(m_RenderInfo.fPitch) + "\n";
     m_fLastPitch=m_RenderInfo.fPitch;
    }
    if (m_RenderInfo.fESpeed==-1000000F)
    {
     if (m_fLastESpeed!=-1000000F)
      sInfo += "Speed: E " + Convert.ToString(m_fLastESpeed) + "m/s, N " + Convert.ToString(m_fLastNSpeed)+ "m/s, V " +Convert.ToString(m_fLastVSpeed) +"m/s\n";
    }
    else
    {
     sInfo += "Speed: E " + Convert.ToString(m_RenderInfo.fESpeed) + "m/s, N " + Convert.ToString(m_RenderInfo.fNSpeed)+ "m/s, V " +Convert.ToString(m_RenderInfo.fVSpeed) +"m/s\n";
     m_fLastESpeed=m_RenderInfo.fESpeed;
     m_fLastNSpeed=m_RenderInfo.fNSpeed;
     m_fLastVSpeed=m_RenderInfo.fVSpeed;
    }
    if (m_RenderInfo.fESpeed==-1000000F && m_fLastESpeed==-1000000F)
    {
     if (m_RenderInfo.fSpeed==-1F)
     {
      if (m_fLastSpeed!=-1F)
       sInfo += "Speed: " + Convert.ToString(m_fLastSpeed) + " " + m_RenderInfo.sSpeedUnit + " (Last Known)\n";
     }
     else
     {
      sInfo += "Speed: " + Convert.ToString(m_RenderInfo.fSpeed) + " " + m_RenderInfo.sSpeedUnit + "\n";
      m_fLastSpeed=m_RenderInfo.fSpeed;
     }
    }
    if (m_RenderInfo.fHeading==-1F)
    {
     if (m_fLastHeading!=-1F)
      sInfo += "Heading: " + Convert.ToString(m_fLastHeading) + " (Last Known)\n";
    }
    else
    {
     sInfo += "Heading: " + Convert.ToString(m_RenderInfo.fHeading) + "\n";
     m_fLastHeading=m_RenderInfo.fHeading;
    }
    if (m_RenderInfo.iHour==-1 || m_RenderInfo.iMin==-1 || m_RenderInfo.fSec==-1F)
    {
     if (m_iLastHour!=-1)
      sInfo += "UTC: " + String.Format("{0:00}",m_iLastHour) + ":" +
       String.Format("{0:00}",m_iLastMin) + ":" +
       String.Format("{0:00.000}",m_fLastSec) +
       " (Last Known)\n";
    }
    else
    {
     sInfo += "UTC: " + String.Format("{0:00}",m_RenderInfo.iHour) + ":" +
      String.Format("{0:00}",m_RenderInfo.iMin) + ":" +
      String.Format("{0:00.000}",m_RenderInfo.fSec) +
      "\n";
     m_iLastHour=m_RenderInfo.iHour;
     m_iLastMin=m_RenderInfo.iMin;
     m_fLastSec=m_RenderInfo.fSec;
    }
    if (m_RenderInfo.iDay==-1 || m_RenderInfo.iMonth==-1 || m_RenderInfo.iYear==-1)
    {
     if (m_iLastDay!=-1)
      sInfo += "Date: " + String.Format("{0:00}",m_iLastDay) + "/" +
       String.Format("{0:00}",m_iLastMonth) + "/" +
       String.Format("{0:00}",m_iLastYear) +
       " (Last Known)\n";
    }
    else
    {
     sInfo += "Date: " + String.Format("{0:00}",m_RenderInfo.iDay) + "/" +
      String.Format("{0:00}",m_RenderInfo.iMonth) + "/" +
      String.Format("{0:00}",m_RenderInfo.iYear) +
      "\n";
     m_iLastDay=m_RenderInfo.iDay;
     m_iLastMonth=m_RenderInfo.iMonth;
     m_iLastYear=m_RenderInfo.iYear;
    }
    if (m_fTotalDistance>=0F)
    {
     string sUnit=(m_fTotalDistance>=1F)?"km":"m";
                    fDistance = (m_fTotalDistance < 1F) ? (m_fTotalDistance * 1000F) : m_fTotalDistance;
     sInfo += "Track Distance: " + Convert.ToString(decimal.Round(Convert.ToDecimal(fDistance),3)) +
      sUnit +
      "\n";
    }
    if (m_sDescriptionFrom!="")
    {
                    double dDistance = CalculateDistance(m_fLatitudeFrom, m_fLongitudeFrom, m_RenderInfo.fLat, m_RenderInfo.fLon);
                    if (double.IsNaN(dDistance) == false)
                    {
                        string sUnit = (dDistance >= 1F) ? "km" : "m";
                        dDistance = (dDistance < 1F) ? (dDistance * 1000F) : dDistance;
                        sInfo += "Distance To " + m_sDescriptionFrom + ": " + Convert.ToString(decimal.Round(Convert.ToDecimal(dDistance), 3)) +
                            sUnit +
                            "\n";
                    }
     double lat1, lat2, lon1, lon2, deltaLon, dBearing;
     lat1= Convert.ToDouble(m_RenderInfo.fLat)*(Math.PI/180);
     lat2= Convert.ToDouble(m_fLatitudeFrom)*(Math.PI/180);
     lon1= Convert.ToDouble(m_RenderInfo.fLon)*(Math.PI/180);
     lon2= Convert.ToDouble(m_fLongitudeFrom)*(Math.PI/180);
     deltaLon=lon2-lon1;
     dBearing = Math.Atan2(Math.Sin(deltaLon)*Math.Cos(lat2),Math.Cos(lat1)*Math.Sin(lat2)-Math.Sin(lat1)*Math.Cos(lat2)*Math.Cos(deltaLon));
     dBearing = dBearing * (180 / Math.PI);
     if (dBearing<0)
      dBearing=(180+dBearing)+180;
     sInfo += "Bearing To " + m_sDescriptionFrom + ": " + Convert.ToString(decimal.Round(Convert.ToDecimal(dBearing),3)) + "\n";
    }
    if (m_RenderInfo.sComment!="")
     sInfo += "Comment: " + m_RenderInfo.sComment + "\n";
    sInfo += m_RenderInfo.sPortInfo;
   }
   drawArgs.toolbarFont.DrawText(null, sInfo, (int)projectedPoint.X + (m_iIconWidthHalf) +11, (int)(projectedPoint.Y),color );
   drawArgs.toolbarFont.DrawText(null, sInfo, (int)projectedPoint.X + (m_iIconWidthHalf) +11, (int)(projectedPoint.Y)+1,color );
   color = Color.Yellow.ToArgb();
   drawArgs.toolbarFont.DrawText(null, sInfo, (int)projectedPoint.X + (m_iIconWidthHalf) +10, (int)(projectedPoint.Y),color );
  }
  public void SetGpsData(DrawArgs drawArgs, GPSRenderInformation renderInformation)
  {
   this.m_RenderInfo=renderInformation;
   if (m_bSignalDistance)
   {
    for (int i=0; i<m_gpsTrackerOverlay.m_iGpsPOIIndex; i++)
    {
     m_gpsTrackerOverlay.m_gpsPOI[i].m_fLatitudeFrom=m_RenderInfo.fLat;
     m_gpsTrackerOverlay.m_gpsPOI[i].m_fLongitudeFrom=m_RenderInfo.fLon;
     m_gpsTrackerOverlay.m_gpsPOI[i].m_sDescriptionFrom=m_RenderInfo.sDescription;
    }
   }
   if (renderInformation.bRestartTrack)
   {
    m_uVerticesCount=0;
    m_uPointCount=0;
    m_uTotalPointCount=0;
    m_fTotalDistance=0;
   }
   else
   if (m_uPointCount>=50000)
   {
    m_uVerticesCount++;
    m_uPointCount=0;
   }
   if (this.vertices[m_uVerticesCount]==null)
    this.vertices[m_uVerticesCount] = new CustomVertex.PositionColored[50000];
   Vector3 v;
   if (renderInformation.fAlt!=-1000000F && World.Settings.VerticalExaggeration>=1F)
   {
    float elevation = (float)(m_parentWorld.TerrainAccessor.GetElevationAt(renderInformation.fLat, renderInformation.fLon, 100F / m_gpsTrackerOverlay.drawArgs.WorldCamera.ViewRange.Degrees) * World.Settings.VerticalExaggeration);
    v=MathEngine.SphericalToCartesian(renderInformation.fLat, renderInformation.fLon, this.m_parentWorld.EquatorialRadius + ((renderInformation.fAlt* World.Settings.VerticalExaggeration)-elevation));
   }
   else
    v=MathEngine.SphericalToCartesian(renderInformation.fLat, renderInformation.fLon, this.m_parentWorld.EquatorialRadius);
            this.vertices[m_uVerticesCount][m_uPointCount].X = v.X;
            this.vertices[m_uVerticesCount][m_uPointCount].Y = v.Y;
            this.vertices[m_uVerticesCount][m_uPointCount].Z = v.Z;
   this.vertices[m_uVerticesCount][m_uPointCount].Color =m_RenderInfo.colorTrack.ToArgb();
   m_uPointCount++;
   if (m_uTotalPointCount>0)
   {
                double dDistance = CalculateDistance(m_fLatFrom, m_fLonFrom, renderInformation.fLat, renderInformation.fLon);
    if (double.IsNaN(dDistance)==false)
    {
     m_fTotalDistance=m_fTotalDistance+(float)dDistance;
    }
   }
   m_fLatFrom=renderInformation.fLat;
   m_fLonFrom=renderInformation.fLon;
   m_uTotalPointCount++;
  }
        private double CalculateDistance(double dLatFrom, double dLonFrom, double dLatTo, double dLonTo)
        {
            double deltaLon, deltaLat, dDistance;
            dLatFrom = dLatFrom * (Math.PI / 180);
            dLatTo = dLatTo * (Math.PI / 180);
            dLonFrom = dLonFrom * (Math.PI / 180);
            dLonTo = dLonTo * (Math.PI / 180);
            deltaLon = dLonTo - dLonFrom;
            deltaLat = dLatTo - dLatFrom;
            if (deltaLon == 0 && deltaLat == 0)
                dDistance = (double)0;
            else
                dDistance = Math.Acos(Math.Sin(dLatFrom) * Math.Sin(dLatTo) + Math.Cos(dLatFrom) * Math.Cos(dLatTo) * Math.Cos(deltaLon)) * 6371.0;
            return dDistance;
        }
 }
 [Serializable]
 public class GPSTrack
 {
  public double [] m_fLat;
  public double [] m_fLon;
  public float [] m_fAlt;
  public uint m_uPointCount;
  public GPSTrack()
  {
   m_uPointCount=0;
  }
  public void Resize(uint uDesiredSize)
  {
            double[] m_fLatResize = new double[uDesiredSize];
            Array.Copy(m_fLat, m_fLatResize, Math.Min(m_fLat.Length,uDesiredSize));
   m_fLat=m_fLatResize;
            double[] m_fLonResize = new double[uDesiredSize];
            Array.Copy(m_fLon, m_fLonResize, Math.Min(m_fLon.Length, uDesiredSize));
   m_fLon=m_fLonResize;
            float[] m_fAltResize = new float[uDesiredSize];
            Array.Copy(m_fAlt, m_fAltResize, Math.Min(m_fAlt.Length, uDesiredSize));
   m_fAlt=m_fAltResize;
  }
        public void SetSize(uint uDesiredSize)
        {
            m_fLat = new double[uDesiredSize];
            m_fLon = new double[uDesiredSize];
            m_fAlt = new float[uDesiredSize];
        }
  public void AddPoint(double fLat, double fLon, float fAlt)
  {
   bool bAdd=false;
   uint iCount;
   if (fAlt==-1000000F)
    fAlt=0F;
   if (m_uPointCount==1000000)
   {
    for (iCount=0; iCount<m_uPointCount; iCount++)
    {
     m_fLat[iCount-1]=m_fLat[iCount];
     m_fLon[iCount-1]=m_fLon[iCount];
     m_fAlt[iCount-1]=m_fAlt[iCount];
    }
    m_uPointCount--;
    bAdd=true;
   }
   else
   if (m_uPointCount>0)
   {
    if (m_fLat[m_uPointCount-1]!=fLat ||
     m_fLon[m_uPointCount-1]!=fLon ||
     m_fAlt[m_uPointCount-1]!=fAlt)
     bAdd=true;
   }
   else
    bAdd=true;
   if (bAdd==true )
   {
    m_fLat[m_uPointCount]=fLat;
    m_fLon[m_uPointCount]=fLon;
    m_fAlt[m_uPointCount]=fAlt;
    m_uPointCount++;
   }
  }
 }
}
