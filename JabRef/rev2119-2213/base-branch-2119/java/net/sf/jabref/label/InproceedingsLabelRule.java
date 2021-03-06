
package net.sf.jabref.label;

import net.sf.jabref.*;
import java.util.StringTokenizer ;

public class InproceedingsLabelRule extends DefaultLabelRule {

    
    
    public String applyRule(BibtexEntry oldEntry){
        String oldLabel = (String) (oldEntry.getField(BibtexFields.KEY_FIELD)) ;
        String newLabel = "" ;


        StringTokenizer authorTokens = null ;
        
        try{
            authorTokens= new StringTokenizer((String) oldEntry.getField("author"),",") ;
            newLabel += authorTokens.nextToken().toLowerCase().replaceAll(" ","").replaceAll("\\.","")   ;
        }catch(Throwable t){
                        System.out.println("error getting author: "+t) ;
        }

        
        try{
            if( oldEntry.getField("year")!= null){
                newLabel += String.valueOf( oldEntry.getField("year")) ;
            }
        }catch(Throwable t){
                        System.out.println("error getting year: "+t) ;
        }

        
        
        try{

          if(oldEntry.getField("booktitle") != null) {
            authorTokens = new StringTokenizer( ((String) oldEntry.getField("booktitle")).replaceAll(","," ").replaceAll("/"," ")) ;
            String tempString = authorTokens.nextToken() ;
            tempString = tempString.replaceAll(",","") ;
            boolean done = false ;
            while(tempString!=null && !done ){
                tempString = tempString.replaceAll(",","").trim() ;
                if(tempString.trim().length() > 3 && !KeyWord.isKeyWord(tempString))  {
                    done = true ;
                }
                else{

                    if(authorTokens.hasMoreTokens()){
                        tempString = authorTokens.nextToken() ;
                    }else{
                        done = true ;
                    }
                }
            }

            if(tempString!=null && (tempString.indexOf("null")<0) ){
                newLabel += String.valueOf( tempString.toLowerCase()) ;
            }
          }
        }
        catch(Throwable t){  System.err.println(t) ; }

        
        return newLabel;
    }



}



