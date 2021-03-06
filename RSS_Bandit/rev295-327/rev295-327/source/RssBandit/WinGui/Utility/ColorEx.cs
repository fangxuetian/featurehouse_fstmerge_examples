using System; 
using System.Drawing; namespace  RssBandit.WinGui.Utility {
	
 public class  ColorEx {
		
  public static readonly  Color[] OneNoteColors = new Color[] {
                    Color.FromArgb(138, 168, 228),
                    Color.FromArgb(145, 186, 174),
                    Color.FromArgb(246, 176, 120),
                    Color.FromArgb(213, 164, 187),
                    Color.FromArgb(180, 158, 222),
                    Color.FromArgb(238, 149, 151),
                    Color.FromArgb(183, 201, 151),
                    Color.FromArgb(255, 216, 105),
  };
 
  public  const int HUEMAX = 360; 
  public  const float SATMAX = 1.0f; 
  public  const float BRIGHTMAX = 1.0f; 
  public  const int RGBMAX = 255; 
  private  Color m_clrCurrent = Color.Red;
 
  public  ColorEx() {; }
 
  public  ColorEx(Color initialColor) { this.m_clrCurrent = initialColor; }
 
  public  Color CurrentColor {
   get { return m_clrCurrent; }
   set { m_clrCurrent = value; }
  }
 
  public  byte Red {
   get { return m_clrCurrent.R;}
   set { m_clrCurrent = Color.FromArgb(value, Green, Blue); }
  }
 
  public  byte Green {
   get { return m_clrCurrent.G; }
   set { m_clrCurrent = Color.FromArgb(Red, value, Blue); }
  }
 
  public  byte Blue {
   get { return m_clrCurrent.B; }
   set { m_clrCurrent = Color.FromArgb(Red, Green, value); }
  }
 
  public  int Hue {
   get { return (int)m_clrCurrent.GetHue(); }
   set {
    m_clrCurrent = ColorEx.HSBToRGB(value,
     m_clrCurrent.GetSaturation(),
     m_clrCurrent.GetBrightness());
   }
  }
 
  public  float GetHue() {
   float top = ((float)(2 * Red - Green - Blue)) / (2 * 255);
   float bottom = (float)Math.Sqrt(((Red - Green) * (Red - Green) + (Red - Blue) * (Green - Blue)) / 255);
   return (float)Math.Acos(top / bottom);
  }
 
  public  float GetSaturation() {
   return (255 -
    (((float)(Red + Green + Blue)) / 3) * Math.Min(Red, Math.Min(Green, Blue))) / 255;
  }
 
  public  float GetBrightness() {
   return ((float)(Red + Green + Blue)) / (255.0f * 3.0f);
  }
 
  public  float Saturation {
   get {
    if(0.0f == Brightness)
    {
     return 0.0f;
    }
    else
    {
     float fMax = (float)Math.Max(Red, Math.Max(Green, Blue));
     float fMin = (float)Math.Min(Red, Math.Min(Green, Blue));
     return (fMax - fMin) / fMax;
    }
   }
   set {
    m_clrCurrent = ColorEx.HSBToRGB((int)m_clrCurrent.GetHue(),
     value, m_clrCurrent.GetBrightness());
   }
  }
 
  public  float Brightness {
   get {
    return (float)Math.Max(Red, Math.Max(Green, Blue)) / (255.0f);
   }
   set {
    m_clrCurrent = ColorEx.HSBToRGB((int)m_clrCurrent.GetHue(),
     m_clrCurrent.GetSaturation(),
     value);
   }
  }
 
  public static  Color HSBToRGB(int Hue, float Saturation, float Brightness) {
   int red = 0; int green = 0; int blue = 0;
   if(Saturation == 0.0f)
   {
    red = green = blue = (int)(Brightness * 255);
   }
   else
   {
    float fHexHue = (6.0f / 360.0f) * Hue;
    float fHexSector = (float)Math.Floor((double)fHexHue);
    float fHexSectorPos = fHexHue - fHexSector;
    float fBrightness = Brightness * 255.0f;
    float fSaturation = Saturation;
    byte bWashOut = (byte)(0.5f + fBrightness * (1.0f - fSaturation));
    byte bHueModifierOddSector = (byte)(0.5f + fBrightness * (1.0f - fSaturation * fHexSectorPos));
    byte bHueModifierEvenSector = (byte)(0.5f + fBrightness * (1.0f - fSaturation * (1.0f - fHexSectorPos)));
    switch((int)fHexSector) {
     case 0 :
      red = (int)(Brightness * 255); green = bHueModifierEvenSector; blue = bWashOut;
      break;
     case 1 :
      red = bHueModifierOddSector; green = (int)(Brightness * 255); blue = bWashOut;
      break;
     case 2 :
      red = bWashOut; green = (int)(Brightness * 255); blue = bHueModifierEvenSector;
      break;
     case 3 :
      red = bWashOut; green = bHueModifierOddSector; blue = (int)(Brightness * 255);
      break;
     case 4 :
      red = bHueModifierEvenSector; green = bWashOut; blue = (int)(Brightness * 255);
      break;
     case 5 :
      red = (int)(Brightness * 255); green = bWashOut; blue = bHueModifierOddSector;
      break;
     default :
      red = 0; green = 0; blue = 0;
      break;
    }
   }
   return Color.FromArgb(red, green, blue);
  }
 
  public static  void ColorizeOneNote(System.Windows.Forms.Control control, int index) {
   if (control != null) {
    control.BackColor = OneNoteColors[(index % OneNoteColors.Length)];
   }
  }

	}

}
