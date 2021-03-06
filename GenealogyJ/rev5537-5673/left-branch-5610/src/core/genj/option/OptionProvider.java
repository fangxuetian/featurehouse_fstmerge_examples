
package genj.option;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;


public abstract class OptionProvider {

  
  private static List<Option> options;
  
  
  public abstract List<? extends Option> getOptions();

  
  public static void persistAll() {
    
    
    for (Option option : getAllOptions()) try {
      option.persist();
    } catch (Throwable t) {
    }
    
    
    
  }
  
  
  public static List<Option> getAllOptions() {  

    
    if (options!=null)
      return options;    
  
    
    options = new ArrayList<Option>(32);
  
    
    Iterator<OptionProvider> providers = lookupProviders();
    while (providers.hasNext()) {
      
      
      OptionProvider provider = providers.next();
      
      
      options.addAll(provider.getOptions());
      
      
      for (Option option : options) {
        try {
          option.restore();
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
      
    }
    
    
    return options;
  }
  
  
  private static Iterator<OptionProvider> lookupProviders() {
    return ServiceRegistry.lookupProviders(OptionProvider.class);
  }

} 
