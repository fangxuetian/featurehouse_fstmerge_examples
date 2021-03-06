using NUnit.Framework;
using System.Collections.Generic;
using System.Configuration;
using ThoughtWorks.CruiseControl.Core.Config;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.UnitTests.Core.Config
{
    [TestFixture]
    public class ServerConfigurationHandlerTest : CustomAssertion
    {
        [Test]
        public void GetConfig()
        {
            ServerConfiguration config = ConfigurationManager.GetSection("cruiseServer") as ServerConfiguration;
            Assert.IsNotNull(config);
            Assert.AreEqual(1, config.Extensions.Count);
            Assert.AreEqual("ThoughtWorks.CruiseControl.UnitTests.Remote.ServerExtensionStub,ThoughtWorks.CruiseControl.UnitTests", config.Extensions[0].Type);
        }
    }
}
