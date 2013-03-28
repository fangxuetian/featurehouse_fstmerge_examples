using System;
using System.Diagnostics;
using System.Collections;
using System.IO;
using System.Net;
using Microsoft.DirectX;
using Microsoft.DirectX.Direct3D;
using WorldWind.Camera;
using WorldWind;
using WorldWind.Net;
namespace WorldWind
{
 public class DrawArgs : IDisposable
 {
  public Device device;
  public System.Windows.Forms.Control parentControl;
  public static System.Windows.Forms.Control ParentControl = null;
  public int numBoundaryPointsTotal;
  public int numBoundaryPointsRendered;
  public int numBoundariesDrawn;
  public Font defaultDrawingFont;
  public System.Drawing.Font defaultSubTitleFont;
  public Font defaultSubTitleDrawingFont;
  public Font toolbarFont;
  public int screenWidth;
  public int screenHeight;
  public static System.Drawing.Point LastMousePosition;
  public int numberTilesDrawn;
  public System.Drawing.Point CurrentMousePosition;
  public string UpperLeftCornerText = "";
  CameraBase m_WorldCamera;
  public World m_CurrentWorld = null;
  public static bool IsLeftMouseButtonDown = false;
  public static bool IsRightMouseButtonDown = false;
  public static DownloadQueue DownloadQueue = new DownloadQueue();
  public static WorldWind.Widgets.RootWidget RootWidget = null;
  public int TexturesLoadedThisFrame = 0;
  private static System.Drawing.Bitmap bitmap;
  public static System.Drawing.Graphics Graphics = null;
  public bool RenderWireFrame = false;
  public static CameraBase Camera = null;
  public CameraBase WorldCamera
  {
   get
   {
    return m_WorldCamera;
   }
   set
   {
    m_WorldCamera = value;
    Camera = value;
   }
  }
  public World CurrentWorld
  {
   get
   {
    return m_CurrentWorld;
   }
   set
   {
    m_CurrentWorld = value;
   }
  }
  public static long CurrentFrameStartTicks;
  public static float LastFrameSecondsElapsed;
  static CursorType mouseCursor;
  static CursorType lastCursor;
  bool repaint = true;
  bool isPainting;
  Hashtable fontList = new Hashtable();
  public static Device Device = null;
  System.Windows.Forms.Cursor measureCursor;
  public DrawArgs( Device device, System.Windows.Forms.Control parentForm )
  {
   this.parentControl = parentForm;
   DrawArgs.ParentControl = parentForm;
   DrawArgs.Device = device;
   this.device = device;
   defaultDrawingFont = CreateFont( World.Settings.defaultFontName, World.Settings.defaultFontSize );
   if(defaultDrawingFont==null)
    defaultDrawingFont = CreateFont( "", 10 );
   defaultSubTitleFont = new System.Drawing.Font("Ariel", 8.0f);
   defaultSubTitleDrawingFont = new Font(device, defaultSubTitleFont);
   if(defaultSubTitleDrawingFont==null)
    defaultSubTitleDrawingFont = CreateFont( "", 8 );
   toolbarFont = CreateFont( World.Settings.ToolbarFontName, World.Settings.ToolbarFontSize, World.Settings.ToolbarFontStyle );
   bitmap = new System.Drawing.Bitmap(256, 256, System.Drawing.Imaging.PixelFormat.Format32bppArgb);
   DrawArgs.Graphics = System.Drawing.Graphics.FromImage(bitmap);
  }
  System.Windows.Forms.Control m_ReferenceForm;
  private void InitializeReference()
  {
   PresentParameters presentParameters = new PresentParameters();
   presentParameters.Windowed = true;
   presentParameters.SwapEffect = SwapEffect.Discard;
   presentParameters.AutoDepthStencilFormat = DepthFormat.D16;
   presentParameters.EnableAutoDepthStencil = true;
   m_ReferenceForm = new System.Windows.Forms.Control("Reference", 0,0,1,1);
   m_ReferenceForm.Visible = false;
   int adapterOrdinal = 0;
   try
   {
    adapterOrdinal = Manager.Adapters.Default.Adapter;
   }
   catch
   {
    throw new NotAvailableException();
   }
   CreateFlags flags = CreateFlags.SoftwareVertexProcessing;
   flags |= CreateFlags.MultiThreaded | CreateFlags.FpuPreserve;
  }
  private void OnDeviceReset(object sender, EventArgs e)
  {
   if( m_Device3dReference.DeviceCaps.TextureFilterCaps.SupportsMinifyAnisotropic)
   {
    m_Device3dReference.SamplerState[0].MinFilter = TextureFilter.Anisotropic;
   }
   else if( m_Device3dReference.DeviceCaps.TextureFilterCaps.SupportsMinifyLinear)
   {
    m_Device3dReference.SamplerState[0].MinFilter = TextureFilter.Linear;
   }
   if( m_Device3dReference.DeviceCaps.TextureFilterCaps.SupportsMagnifyAnisotropic )
   {
    m_Device3dReference.SamplerState[0].MagFilter = TextureFilter.Anisotropic;
   }
   else if( m_Device3dReference.DeviceCaps.TextureFilterCaps.SupportsMagnifyLinear )
   {
    m_Device3dReference.SamplerState[0].MagFilter = TextureFilter.Linear;
   }
   m_Device3dReference.SamplerState[0].AddressU = TextureAddress.Clamp;
   m_Device3dReference.SamplerState[0].AddressV = TextureAddress.Clamp;
   m_Device3dReference.RenderState.Clipping = true;
   m_Device3dReference.RenderState.CullMode = Cull.Clockwise;
   m_Device3dReference.RenderState.Lighting = false;
   m_Device3dReference.RenderState.Ambient = System.Drawing.Color.FromArgb(0x40, 0x40, 0x40);
   m_Device3dReference.RenderState.ZBufferEnable = true;
   m_Device3dReference.RenderState.AlphaBlendEnable = true;
   m_Device3dReference.RenderState.SourceBlend = Blend.SourceAlpha;
   m_Device3dReference.RenderState.DestinationBlend = Blend.InvSourceAlpha;
  }
  Device m_Device3dReference = null;
  public void BeginRender()
  {
   this.numberTilesDrawn = 0;
   this.TexturesLoadedThisFrame = 0;
   this.UpperLeftCornerText = "";
   this.numBoundaryPointsRendered = 0;
   this.numBoundaryPointsTotal = 0;
   this.numBoundariesDrawn = 0;
   this.isPainting = true;
  }
  public void EndRender()
  {
   Debug.Assert(isPainting);
   this.isPainting = false;
  }
  public void Present()
  {
   long previousFrameStartTicks = CurrentFrameStartTicks;
   PerformanceTimer.QueryPerformanceCounter(ref CurrentFrameStartTicks);
   LastFrameSecondsElapsed = (CurrentFrameStartTicks - previousFrameStartTicks) /
    (float)PerformanceTimer.TicksPerSecond;
   device.Present();
  }
  public Font CreateFont( string familyName, float emSize )
  {
   return CreateFont( familyName, emSize, System.Drawing.FontStyle.Regular );
  }
  public Font CreateFont( string familyName, float emSize, System.Drawing.FontStyle style )
  {
   try
   {
    FontDescription description = new FontDescription();
    description.FaceName = familyName;
    description.Height = (int)(1.9*emSize);
    if(style == System.Drawing.FontStyle.Regular)
     return CreateFont( description );
    if((style & System.Drawing.FontStyle.Italic) != 0)
     description.IsItalic = true;
    if((style & System.Drawing.FontStyle.Bold) != 0)
     description.Weight = FontWeight.Heavy;
    return CreateFont( description );
   }
   catch
   {
    Utility.Log.Write("FONT", string.Format("Unable to load '{0}' {2} ({1}em)",
     familyName, emSize, style ) );
    return defaultDrawingFont;
   }
  }
  public Font CreateFont( FontDescription description )
  {
   try
   {
    if (World.Settings.AntiAliasedText)
     description.Quality = FontQuality.ClearTypeNatural;
    else
     description.Quality = FontQuality.Default;
    string hash = description.ToString();
    Font font = (Font)fontList[ hash ];
    if(font!=null)
     return font;
    font = new Font( this.device, description );
    fontList.Add( hash, font );
    return font;
   }
   catch
   {
    Utility.Log.Write("FONT", string.Format("Unable to load '{0}' (Height: {1})", description.FaceName, description.Height) );
    return defaultDrawingFont;
   }
  }
  public static CursorType MouseCursor
  {
   get
   {
    return mouseCursor;
   }
   set
   {
    mouseCursor = value;
   }
  }
  public void UpdateMouseCursor(System.Windows.Forms.Control parent)
  {
   if(lastCursor == mouseCursor)
    return;
   switch( mouseCursor )
   {
    case CursorType.Hand:
     parent.Cursor = System.Windows.Forms.Cursors.Hand;
     break;
    case CursorType.Cross:
     parent.Cursor = System.Windows.Forms.Cursors.Cross;
     break;
    case CursorType.Measure:
     if(measureCursor == null)
      measureCursor = ImageHelper.LoadCursor("measure.cur");
     parent.Cursor = measureCursor;
     break;
    case CursorType.SizeWE:
     parent.Cursor = System.Windows.Forms.Cursors.SizeWE;
     break;
    case CursorType.SizeNS:
     parent.Cursor = System.Windows.Forms.Cursors.SizeNS;
     break;
    case CursorType.SizeNESW:
     parent.Cursor = System.Windows.Forms.Cursors.SizeNESW;
     break;
    case CursorType.SizeNWSE:
     parent.Cursor = System.Windows.Forms.Cursors.SizeNWSE;
     break;
    default:
     parent.Cursor = System.Windows.Forms.Cursors.Arrow;
     break;
   }
   lastCursor = mouseCursor;
  }
  public static float SecondsSinceLastFrame
  {
   get
   {
    long curTicks = 0;
    PerformanceTimer.QueryPerformanceCounter( ref curTicks );
    float elapsedSeconds = (curTicks - CurrentFrameStartTicks)/(float)PerformanceTimer.TicksPerSecond;
    return elapsedSeconds;
   }
  }
  public bool IsPainting
  {
   get
   {
    return this.isPainting;
   }
  }
  public bool Repaint
  {
   get
   {
    return this.repaint;
   }
   set
   {
    this.repaint = value;
   }
  }
  public void Dispose()
  {
   foreach(IDisposable font in fontList.Values)
   {
    if(font!=null)
    {
     font.Dispose();
    }
   }
   fontList.Clear();
   if(measureCursor!=null)
   {
    measureCursor.Dispose();
    measureCursor = null;
   }
   if(DownloadQueue != null)
   {
    DownloadQueue.Dispose();
    DownloadQueue = null;
   }
   GC.SuppressFinalize(this);
  }
 }
 public enum CursorType
 {
  Arrow = 0,
  Hand,
  Cross,
  Measure,
  SizeWE,
  SizeNS,
  SizeNESW,
  SizeNWSE
 }
}
