using System;
namespace NewsComponents
{
 public class NewsItemChannelBase: INewsChannel {
  protected string p_channelName;
  protected int p_channelPriority;
  public NewsItemChannelBase():
   this("http://www.rssbandit.org/channels/newsitemchannel", 50){
   }
  public NewsItemChannelBase(string channelName, int channelPriority)
  {
   p_channelName = channelName;
   p_channelPriority = channelPriority;
  }
  public virtual string ChannelName {
   get { return p_channelName; }
  }
  public virtual int ChannelPriority {
   get {return p_channelPriority; }
  }
  public NewsComponents.ChannelProcessingType ChannelProcessingType {
   get { return ChannelProcessingType.NewsItem;}
  }
  public virtual INewsItem Process(INewsItem item)
  {
   return item;
  }
 }
}
