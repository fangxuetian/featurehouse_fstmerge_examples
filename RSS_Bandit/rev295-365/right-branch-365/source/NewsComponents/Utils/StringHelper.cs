using System;
using System.Globalization;
using System.Text;
using System.Text.RegularExpressions;
namespace NewsComponents.Utils
{
 public static class StringHelper
 {
  private static readonly Regex regexEMail = new Regex(@"(?<prefix>mailto:)?(?<address>(?:[\w\!\#\$\%\&\'\*\+\-\/\=\?\^\`\{\|\}\~]+\.)*[\w\!\#\$\%\&\'\*\+\-\/\=\?\^\`\{\|\}\~]+@(?:(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9\-](?!\.)){0,61}[a-zA-Z0-9]?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9\-](?!$)){0,61}[a-zA-Z0-9]?)|(?:\[(?:(?:[01]?\d{1,2}|2[0-4]\d|25[0-5])\.){3}(?:[01]?\d{1,2}|2[0-4]\d|25[0-5])\]))$)", RegexOptions.Singleline | RegexOptions.CultureInvariant| RegexOptions.Compiled);
  private static readonly Regex regexWords = new Regex(@"\S+", RegexOptions.Multiline | RegexOptions.CultureInvariant| RegexOptions.Compiled);
  public static bool EmptyTrimOrNull(string text) {
   return (text == null || text.Trim().Length == 0);
  }
  public static int SizeOfStr(string s) {
   return LengthOfStr(s) * 2;
  }
  public static int LengthOfStr(string s) {
   if (string.IsNullOrEmpty(s))
    return 0;
   return s.Length;
  }
  public static string ShortenByEllipsis(string text, int allowedLength) {
   if (text == null) return String.Empty;
   if (text.Length > allowedLength + 3) {
    int nlPos = text.IndexOfAny(new char[]{'\n','\r'});
    if (nlPos >= 0 && nlPos < allowedLength)
     return text.Substring(0, nlPos) + "...";
    else
     return text.Substring(0, allowedLength) + "...";
   }
   else
    return text;
  }
  public static string GetFirstWords(string text, int wordCount)
  {
   if (text == null) return String.Empty;
   MatchCollection words = regexWords.Matches(text);
   StringBuilder sb = new StringBuilder();
   for (int i = 0; i < Math.Min(words.Count, wordCount); i++)
   {
    sb.Append(words[i].Value);
    sb.Append(" ");
   }
   return sb.ToString().TrimEnd();
  }
  public static bool IsEMailAddress(string text)
  {
   if (string.IsNullOrEmpty(text))
    return false;
   return regexEMail.IsMatch(text.Trim());
  }
  public static string GetEMailAddress(string text)
  {
   if (string.IsNullOrEmpty(text))
    return String.Empty;
   Match m = regexEMail.Match(text);
   if (m.Success)
   {
    return m.Groups["address"].Value;
   }
   else
   {
    return text;
   }
  }
  public static bool AreEqualCaseInsensitive(string original, string comparand)
  {
   return string.Compare(original, comparand, true, CultureInfo.InvariantCulture) == 0;
  }
 }
}
