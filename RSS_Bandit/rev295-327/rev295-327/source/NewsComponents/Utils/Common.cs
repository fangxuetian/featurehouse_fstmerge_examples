using System; 
using System.Runtime.InteropServices; namespace  NewsComponents.Utils {
	
 public sealed class  Common {
		
  public static readonly  Version ClrVersion;
 
  static  Common() {
   ClrVersion = GetFrameworkVersion();
  }
 
  private static  Version GetFrameworkVersion() {
   Version fv = new Version(1,0);
   try {
    fv = new Version(RuntimeEnvironment.GetSystemVersion().Replace("v", String.Empty));
   } catch {}
   return fv;
  }
 
  private  Common(){}

	}

}
