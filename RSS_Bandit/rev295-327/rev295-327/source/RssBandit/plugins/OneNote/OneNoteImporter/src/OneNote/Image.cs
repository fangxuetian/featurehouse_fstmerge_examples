using System; 
using System.IO; 
using System.Xml; namespace  Microsoft.Office.OneNote {
	
 [Serializable] 
 public class  ImageContent  : OutlineContent {
		
  public  ImageContent(FileInfo file)
  {
   ImageData = new FileData(file);
  }
 
  public  ImageContent(byte[] image)
  {
   ImageData = new BinaryData(image);
  }
 
  public  ImageContent(ImageContent clone)
  {
   ImageData = clone.ImageData;
  }
 
  public override  object Clone()
  {
   return new ImageContent(this);
  }
 
  protected internal override  void SerializeToXml(XmlNode parentNode)
  {
   XmlDocument xmlDocument = parentNode.OwnerDocument;
   XmlElement image = xmlDocument.CreateElement("Image");
   parentNode.AppendChild(image);
   if (alignment != OutlineAlignment.DEFAULT)
   {
    image.SetAttribute("alignment", alignment.ToString());
   }
   ImageData.SerializeToXml(image);
  }
 
  public  Data ImageData
  {
   get
   {
    return (Data) GetChild("ImageData");
   }
   set
   {
    if (value == null)
     throw new ArgumentNullException("ImageData");
    if (!(value is FileData || value is BinaryData))
     throw new ArgumentException("Incorrect data type.");
    Data imageData = ImageData;
    if (imageData != null)
     RemoveChild(imageData);
    AddChild(value, "ImageData");
   }
  }
 
  public  OutlineAlignment Alignment
  {
   get
   {
    return alignment;
   }
   set
   {
    if (alignment == value)
     return;
    alignment = value;
    CommitPending = true;
   }
  }
 
  private  OutlineAlignment alignment = OutlineAlignment.DEFAULT;

	}
	
 [Serializable] 
 public class  ImageObject  : PageObject {
		
  public  ImageObject(FileInfo file)
  {
   Image = new ImageContent(file);
  }
 
  public  ImageObject(byte[] image)
  {
   Image = new ImageContent(image);
  }
 
  public  ImageObject(ImageObject clone)
  {
   Width = clone.Width;
   Height = clone.Height;
   Position = clone.Position;
   Image = clone.Image;
  }
 
  public override  object Clone()
  {
   return new ImageObject(this);
  }
 
  protected internal override  void SerializeObjectToXml(XmlNode parentNode)
  {
   Image.SerializeToXml(parentNode);
   XmlElement imageElement = (XmlElement) parentNode.LastChild;
   if (BackgroundImage)
    imageElement.SetAttribute("backgroundImage", "true");
   if (Width != null)
    Width.SerializeToXml(imageElement);
   if (Height != null)
    Height.SerializeToXml(imageElement);
  }
 
  private  ImageContent Image
  {
   get
   {
    return (ImageContent) GetChild("Image");
   }
   set
   {
    ImageContent image = Image;
    if (value == image)
     return;
    if (image != null)
     RemoveChild(image);
    AddChild(value, "Image");
   }
  }
 
  public  Data ImageData
  {
   get
   {
    return Image.ImageData;
   }
   set
   {
    if (value == null)
     throw new ArgumentNullException("ImageData");
    ImageContent image = Image;
    image.ImageData = value;
   }
  }
 
  public  bool BackgroundImage
  {
   get
   {
    return backgroundImage;
   }
   set
   {
    if (backgroundImage == value)
     return;
    backgroundImage = value;
    CommitPending = true;
   }
  }
 
  private  bool backgroundImage = false;

	}

}
