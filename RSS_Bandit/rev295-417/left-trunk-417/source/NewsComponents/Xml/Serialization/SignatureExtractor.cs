using System;
using System.Text;
using System.Xml.Serialization;
namespace NewsComponents.Xml.Serialization
{
 public sealed class SignatureExtractor
 {
  private SignatureExtractor()
  {
  }
  public static string GetDefaultNamespaceSignature(string defaultNamespace)
  {
   return defaultNamespace;
  }
  public static string GetXmlRootSignature(XmlRootAttribute root)
  {
   StringBuilder sb = new StringBuilder();
   XmlAttributeOverridesThumbprinter.AddXmlRootPrint(root, sb);
   return sb.ToString();
  }
  public static string GetOverridesSignature(XmlAttributeOverrides overrides)
  {
   string thumbPrint = null;
   if (null != overrides)
   {
    thumbPrint = XmlAttributeOverridesThumbprinter.GetThumbprint(overrides);
   }
   return thumbPrint;
  }
  public static string GetTypeArraySignature(Type[] types)
  {
   string thumbPrint = null;
   if (null != types && types.Length > 0)
   {
    StringSorter sorter = new StringSorter();
    foreach (Type t in types)
    {
     sorter.AddString(t.AssemblyQualifiedName);
    }
    thumbPrint = string.Join(":", sorter.GetOrderedArray());
   }
   return thumbPrint;
  }
 }
}
