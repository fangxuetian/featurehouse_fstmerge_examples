using System;
namespace ThoughtWorks.CruiseControl.Core.Config
{
    public interface IConfigurationValidation
    {
        void Validate(IConfiguration configuration, object parent, IConfigurationErrorProcesser errorProcesser);
    }
}
