using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
namespace ThoughtWorks.CruiseControl.Core.Util
{
 public class StringUtil
 {
  private static readonly Regex NullStringRegex = new Regex("\0");
  public const string DEFAULT_DELIMITER = ",";
  public static bool EqualsIgnoreCase(string a, string b)
  {
   return CaseInsensitiveComparer.Default.Compare(a, b) == 0;
  }
  public static int GenerateHashCode(params string[] values)
  {
   int hashcode = 0;
   foreach (string value in values)
   {
    if (value != null)
     hashcode += value.GetHashCode();
   }
   return hashcode;
  }
  public static string LastWord(string input)
  {
   return LastWord(input, " .,;!?:");
  }
  public static string LastWord(string input, string separators)
  {
   if (input == null)
    return null;
   string[] tokens = input.Split(separators.ToCharArray());
   for (int i = tokens.Length - 1; i >= 0; i--)
   {
    if (IsWhitespace(tokens[i]) == false)
     return tokens[i].Trim();
   }
   return String.Empty;
  }
  public static bool IsBlank(string input)
  {
   return string.IsNullOrEmpty(input);
  }
  public static bool IsWhitespace(string input)
  {
   return (input == null || input.Trim().Length == 0);
  }
  public static string Strip(string input, params string[] removals)
  {
   string revised = input;
   foreach (string removal in removals)
   {
    int i;
    while ((i = revised.IndexOf(removal)) > -1)
     revised = revised.Remove(i, removal.Length);
   }
   return revised;
  }
  public static string Join(string separator, params string[] strings)
  {
   StringBuilder sb = new StringBuilder();
   foreach (string s in strings)
   {
    if (string.IsNullOrEmpty(s))
     continue;
    if (sb.Length > 0)
     sb.Append(separator);
    sb.Append(s);
   }
   return sb.ToString();
  }
  public static string RemoveNulls(string s)
  {
   return NullStringRegex.Replace(s, string.Empty).TrimStart();
  }
  public static string StripQuotes(string filename)
  {
   return filename == null ? null : filename.Trim('"');
  }
  public static string RemoveInvalidCharactersFromFileName(string fileName)
  {
   return Strip(fileName, "\\", "/", ":", "*", "?", "\"", "<", ">", "|");
  }
  public static string AutoDoubleQuoteString(string value)
  {
   if (!string.IsNullOrEmpty(value) && (value.IndexOf(' ') > -1) && (value.IndexOf("\"") == -1))
    return string.Format("\"{0}\"", value);
   return value;
  }
  public static string RemoveTrailingPathDelimeter(string directory)
  {
   return string.IsNullOrEmpty(directory) ? "" : directory.TrimEnd(new char[] {Path.DirectorySeparatorChar});
  }
  public static string IntegrationPropertyToString(object value)
  {
   return IntegrationPropertyToString(value, DEFAULT_DELIMITER);
  }
  public static string IntegrationPropertyToString(object value, string delimiter)
  {
   if (value == null)
    return null;
   if ((value is string) || (value is int) || (value is Enum))
    return value.ToString();
   if (value is ArrayList)
   {
    string[] tmp = (string[]) ((ArrayList) value).ToArray(typeof (string));
    if (tmp.Length <= 1)
     return string.Join(string.Empty, tmp);
    return string.Format("\"{0}\"", string.Join(delimiter, tmp));
   }
   throw new ArgumentException(
    string.Format("The IntegrationProperty type {0} is not supported yet", value.GetType()));
  }
  public static string MakeBuildResult(string input, string msgLevel)
  {
   StringBuilder sb = new StringBuilder();
   Regex linePattern = new Regex(@"([^\r\n]+)");
   MatchCollection lines = linePattern.Matches(input);
   if (lines.Count > 0)
   {
    sb.Append(Environment.NewLine);
    sb.Append("<buildresults>");
    sb.Append(Environment.NewLine);
    foreach (Match line in lines)
    {
     sb.Append("  <message");
     if (msgLevel != "")
      sb.AppendFormat(" level=\"{0}\"", msgLevel);
     sb.Append(">");
     sb.Append(XmlUtil.EncodePCDATA(line.ToString()));
     sb.Append("</message>");
     sb.Append(Environment.NewLine);
    }
    sb.Append("</buildresults>");
    sb.Append(Environment.NewLine);
   }
   else
    sb.Append(input);
   return sb.ToString();
  }
  public static string ArrayToNewLineSeparatedString(string[] input)
  {
   StringBuilder sb = new StringBuilder();
   foreach (string file in input)
   {
    if (sb.Length > 0)
     sb.Append(Environment.NewLine);
    sb.Append(file);
   }
   return sb.ToString();
  }
  public static string[] NewLineSeparatedStringToArray(string input)
  {
   if (IsBlank(input))
    return new string[0];
   List<string> targets = new List<string>();
   using (StringReader reader = new StringReader(input))
   {
    while (reader.Peek() >= 0)
    {
     targets.Add(reader.ReadLine());
    }
   }
   return targets.ToArray();
  }
 }
}
