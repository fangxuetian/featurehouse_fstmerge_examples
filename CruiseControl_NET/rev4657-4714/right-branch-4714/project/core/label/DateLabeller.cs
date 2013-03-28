using System;
using Exortech.NetReflector;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Label
{
 [ReflectorType("dateLabeller")]
 public class DateLabeller : ILabeller
 {
  private readonly DateTimeProvider dateTimeProvider;
  public DateLabeller() : this(new DateTimeProvider())
  {}
  public DateLabeller(DateTimeProvider dateTimeProvider)
  {
   this.dateTimeProvider = dateTimeProvider;
  }
  public string Generate(IIntegrationResult integrationResult)
  {
   DateTime now = dateTimeProvider.Now;
   Version version = ParseVersion(now, integrationResult.LastIntegration);
   int revision = version.Revision;
   if (now.Year == version.Major && now.Month == version.Minor && now.Day == version.Build)
   {
    revision += 1;
   }
   else
   {
    revision = 1;
   }
            return string.Format("{0}.{1}.{2}.{3}",
                   now.Year.ToString("0000"), now.Month.ToString("00"), now.Day.ToString("00"), revision.ToString("000"));
  }
  private Version ParseVersion(DateTime date, IntegrationSummary lastIntegrationSummary)
  {
   try
   {
    return new Version(lastIntegrationSummary.LastSuccessfulIntegrationLabel);
   }
   catch (SystemException)
   {
    return new Version(date.Year, date.Month, date.Day, 0);
   }
  }
  public void Run(IIntegrationResult result)
  {
   result.Label = Generate(result);
  }
 }
}
