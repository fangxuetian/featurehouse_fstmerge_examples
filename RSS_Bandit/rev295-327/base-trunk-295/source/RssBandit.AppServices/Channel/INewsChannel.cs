using System;
namespace NewsComponents
{
 public interface INewsChannel
 {
  string ChannelName { get ;}
  int ChannelPriority { get ;}
  ChannelProcessingType ChannelProcessingType { get; }
 }
}
