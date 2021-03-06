using System; namespace  Microsoft.Office.OneNote {
	
 [Serializable] 
 public class  ImportException  : Exception {
		
  public  ImportException()
  {
  }
 
  public  ImportException(int errorCode)
  {
   HResult = errorCode;
   switch ((uint) errorCode)
   {
    case 0x80041000:
     message = "Error: Attempting to import malformed XML!";
     break;
    case 0x80041001:
     message = "Error: Attempting to import invalid XML!";
     break;
    case 0x80041002:
     message = "Unable to create the specified section.";
     break;
    case 0x80041003:
     message = "Unable to open the specified section.";
     break;
    case 0x80041004:
     message = "The specified section does not exist.";
     break;
    case 0x80041005:
     message = "The specified page does not exist.";
     break;
    case 0x80041006:
     message = "Unable to access the referenced file.";
     break;
    case 0x80041007:
     message = "Unrecognized image data.";
     break;
    case 0x80041008:
     message = "Unrecognized ink data.";
     break;
    case 0x80041009:
     message = "Error import html content.";
     break;
    case 0x8004100a:
     message = "Unable to navigate to the specified page.";
     break;
   }
  }
 
  public  ImportException(string message)
  {
   this.message = message;
  }
 
  public override  string Message
  {
   get
   {
    return message;
   }
  }
 
  private  string message;

	}

}
