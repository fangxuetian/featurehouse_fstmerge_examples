using System;
namespace ThoughtWorks.CruiseControl.Core.Util
{
 public class DateUtil
 {
  public const string DateOutputFormat = "yyyy-MM-dd HH:mm:ss";
  public static string FormatDate(DateTime date)
  {
   return date.ToString(DateOutputFormat);
  }
  public static string FormatDate(DateTime date, IFormatProvider formatter)
  {
   return date.ToString(DateOutputFormat, formatter);
  }
  public static DateTime MaxDate(DateTime a, DateTime b)
  {
   return (a > b) ? a : b;
  }
 }
}
