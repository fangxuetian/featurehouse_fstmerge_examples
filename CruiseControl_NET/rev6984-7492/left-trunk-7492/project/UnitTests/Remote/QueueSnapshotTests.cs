using System;
using System.Collections.Generic;
using System.Text;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Remote;
namespace ThoughtWorks.CruiseControl.UnitTests.Remote
{
    [TestFixture]
    public class QueueSnapshotTests
    {
        [Test]
        public void QueueNameGetSetTest()
        {
            QueueSnapshot activity = new QueueSnapshot();
            activity.QueueName = "testing";
            Assert.AreEqual("testing", activity.QueueName);
        }
    }
}
