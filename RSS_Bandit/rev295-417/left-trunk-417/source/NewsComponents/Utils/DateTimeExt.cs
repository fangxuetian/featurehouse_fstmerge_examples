using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text.RegularExpressions;
using System.Xml;
using NewsComponents.Resources;
namespace NewsComponents.Utils
{
 public sealed class DateTimeExt
 {
  private static readonly Regex rfc2822 = new Regex(@"\s*(?:(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun)\s*,\s*)?(\d{1,2})\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\s+(\d{2,})\s+(\d{2})\s*:\s*(\d{2})\s*(?::\s*(\d{2}))?\s+([+\-]\d{4}|UT|GMT|EST|EDT|CST|CDT|MST|MDT|PST|PDT|[A-IK-Z])", RegexOptions.Compiled);
  private static readonly List<string> months = new List<string>(new string[]{"ZeroIndex","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" });
  public static DateTime ToDateTime(string dateTime){
   dateTime = (dateTime.EndsWith("Z", StringComparison.Ordinal) ? dateTime.Substring(0, dateTime.Length - 1) : dateTime);
   int timeIndex = dateTime.IndexOf(":", StringComparison.Ordinal);
   if(timeIndex != -1){
    int tzoneIndex = dateTime.IndexOf("-", timeIndex, StringComparison.Ordinal);
    if(tzoneIndex == -1){
     tzoneIndex = dateTime.IndexOf("+", timeIndex, StringComparison.Ordinal);
     if(tzoneIndex != -1){
      return AddOffset("+", dateTime, tzoneIndex);
     }
    }else{
     return AddOffset("-", dateTime, tzoneIndex);
    }
   }
   if (timeIndex == dateTime.LastIndexOf(":", StringComparison.Ordinal))
   {
    dateTime = dateTime + ":00";
   }
   return XmlConvert.ToDateTime(dateTime, XmlDateTimeSerializationMode.Utc);
  }
  public static DateTime Parse(string dateTimeString)
  {
   if (dateTimeString == null)
    return DateTime.Now.ToUniversalTime();
   if (dateTimeString.Trim().Length == 0)
    return DateTime.Now.ToUniversalTime();
   Match m = rfc2822.Match(dateTimeString);
   if (m.Success)
   {
    try
    {
     int dd = Int32.Parse(m.Groups[1].Value);
     int mth = months.IndexOf(m.Groups[2].Value);
     int yy = Int32.Parse(m.Groups[3].Value);
     yy = (yy < 50 ? 2000 + yy: (yy < 1000 ? 1900 + yy: yy));
     int hh = Int32.Parse(m.Groups[4].Value, CultureInfo.InvariantCulture);
     int mm = Int32.Parse(m.Groups[5].Value, CultureInfo.InvariantCulture);
     int ss = Int32.Parse("0" + m.Groups[6].Value, CultureInfo.InvariantCulture);
     string zone = m.Groups[7].Value;
     DateTime xd = new DateTime(yy, mth, dd, hh, mm, ss);
     return xd.AddHours(RFCTimeZoneToGMTBias(zone) * -1);
    }
    catch (Exception e)
    {
     throw new FormatException(ComponentsText.ExceptionRFC2822ParseGroupsMessage(e.GetType().Name), e);
    }
   }
   else
   {
    return DateTime.Parse(dateTimeString, null, DateTimeStyles.AdjustToUniversal);
   }
  }
  private DateTimeExt(){}
  private struct TZB
  {
   public TZB(string z, int b) { Zone = z; Bias = b; }
   public readonly string Zone;
   public readonly int Bias;
  }
  private const int timeZones = 35;
  private static readonly TZB[] ZoneBias = new TZB[timeZones]
    {
     new TZB("GMT", 0), new TZB("UT", 0),
     new TZB("EST", -5*60), new TZB("EDT", -4*60),
     new TZB("CST", -6*60), new TZB("CDT", -5*60),
     new TZB("MST", -7*60), new TZB("MDT", -6*60),
     new TZB("PST", -8*60), new TZB("PDT", -7*60),
     new TZB("Z", 0), new TZB("A", -1*60),
     new TZB("B", -2*60), new TZB("C", -3*60),
     new TZB("D", -4*60), new TZB("E", -5*60),
     new TZB("F", -6*60), new TZB("G", -7*60),
     new TZB("H", -8*60), new TZB("I", -9*60),
     new TZB("K", -10*60), new TZB("L", -11*60),
     new TZB("M", -12*60), new TZB("N", 1*60),
     new TZB("O", 2*60), new TZB("P", 3*60),
     new TZB("Q", 4*60), new TZB("R", 3*60),
     new TZB("S", 6*60), new TZB("T", 3*60),
     new TZB("U", 8*60), new TZB("V", 3*60),
     new TZB("W", 10*60), new TZB("X", 3*60),
     new TZB("Y", 12*60)
    };
  private static double RFCTimeZoneToGMTBias(string zone)
  {
   string s;
   if ( zone.IndexOfAny(new char[]{'+', '-'}) == 0 )
   {
    int fact = (zone.Substring(0,1) == "-"? -1: 1);
    s = zone.Substring(1).TrimEnd();
    double hh = Math.Min(23, Int32.Parse(s.Substring(0, 2), CultureInfo.InvariantCulture));
    double mm = Math.Min(59, Int32.Parse(s.Substring(2, 2), CultureInfo.InvariantCulture)) / 60;
    return fact * (hh+mm);
   }
   else
   {
    s = zone.ToUpper(CultureInfo.InvariantCulture).Trim();
    for (int i = 0; i < timeZones; i++)
     if (ZoneBias[i].Zone.Equals(s))
     {
      return ZoneBias[i].Bias / 60;
     }
   }
   return 0.0;
  }
  private static DateTime AddOffset(string offsetOp, string datetime, int tzoneIndex){
   string[] offset = datetime.Substring(tzoneIndex + 1).Split(new char[]{':'});
   string original = datetime;
   datetime = datetime.Substring(0, tzoneIndex);
   if(datetime.IndexOf(":") == datetime.LastIndexOf(":")){
    datetime = datetime + ":00";
   }
   DateTime toReturn = XmlConvert.ToDateTime(datetime, XmlDateTimeSerializationMode.Unspecified);
   if (offset.Length == 1 && offset[0].Length == 4)
   {
    string[] offset2 = new string[2];
    offset2[0] = offset[0].Substring(0,2);
    offset2[1] = offset[0].Substring(2);
    offset = offset2;
   }
   try{
    switch(offsetOp){
     case "+":
      toReturn = toReturn.Subtract(new TimeSpan(Int32.Parse(offset[0]), Int32.Parse(offset[1]), 0));
      break;
     case "-":
      toReturn = toReturn.Add(new TimeSpan(Int32.Parse(offset[0]), Int32.Parse(offset[1]), 0));
      break;
    }
    return toReturn;
   }catch(IndexOutOfRangeException){
    throw new FormatException(ComponentsText.ExceptionRFC2822InvalidTimezoneFormatMessage(original));
   }
  }
  public static int DateAsInteger(DateTime date) {
   return date.Year *10000 + date.Month * 100 + date.Day;
  }
 }
}
