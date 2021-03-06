namespace ThoughtWorks.CruiseControl.UnitTests.Core.Tasks
{
    using System;
    using System.Collections.Generic;
    using System.IO;
    using System.Xml;
    using NUnit.Framework;
    using Rhino.Mocks;
    using ThoughtWorks.CruiseControl.Core;
    using ThoughtWorks.CruiseControl.Core.Tasks;
    using ThoughtWorks.CruiseControl.Core.Util;
    using ThoughtWorks.CruiseControl.Remote;
    using ThoughtWorks.CruiseControl.Remote.Parameters;
    [TestFixture]
    public class TaskBaseTests
    {
        private MockRepository mocks = new MockRepository();
        [Test]
        public void RetrieveDescriptionOrNameRetrievesTheDescriptionWhenDescriptionIsSet()
        {
            var task = new TestTask();
            task.Description = "Test Task";
            var actual = task.RetrieveDescriptionOrName();
            Assert.AreEqual(task.Description, actual);
        }
        [Test]
        public void RetrieveDescriptionOrNameRetrievesTheNameWhenDescriptionIsNotSet()
        {
            var task = new TestTask();
            task.Description = null;
            var actual = task.RetrieveDescriptionOrName();
            Assert.AreEqual(task.GetType().Name, actual);
        }
        [Test]
        public void RunWithTrueResultMarksStatusAsSuccess()
        {
            var task = new TestTask()
            {
                Result = () => true
            };
            var result = mocks.DynamicMock<IIntegrationResult>();
            mocks.ReplayAll();
            task.Run(result);
            mocks.VerifyAll();
            Assert.IsTrue(task.Executed);
            Assert.AreEqual(ItemBuildStatus.CompletedSuccess, task.CurrentStatus.Status);
        }
        [Test]
        public void RunWithFalseResultMarksStatusAsFailed()
        {
            var task = new TestTask()
            {
                Result = () => false
            };
            var result = mocks.DynamicMock<IIntegrationResult>();
            mocks.ReplayAll();
            task.Run(result);
            mocks.VerifyAll();
            Assert.IsTrue(task.Executed);
            Assert.AreEqual(ItemBuildStatus.CompletedFailed, task.CurrentStatus.Status);
        }
        [Test]
        public void RunWithExceptionMarksStatusAsFailed()
        {
            var task = new TestTask()
            {
                Result = () =>
                {
                    throw new Exception();
                }
            };
            var result = mocks.DynamicMock<IIntegrationResult>();
            mocks.ReplayAll();
            try
            {
                task.Run(result);
            }
            catch (Exception) { }
            mocks.VerifyAll();
            Assert.IsTrue(task.Executed);
            Assert.AreEqual(ItemBuildStatus.CompletedFailed, task.CurrentStatus.Status);
        }
        [Test]
        public void PreprocessParametersReturnsOriginalNodeWhenNoDynamicValues()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            Assert.AreEqual(xml, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsDirectValueForValidAttributeDynamicValue()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"$[value|default]\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"default\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<directValue>" +
                        "<parameter>value</parameter>" +
                        "<default>default</default>" +
                        "<property>attrib</property>" +
                    "</directValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsDirectValueForValidNodeDynamicValue()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>$[value|default]</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem>default</subItem>" +
                "<dynamicValues>" +
                    "<directValue>" +
                        "<parameter>value</parameter>" +
                        "<default>default</default>" +
                        "<property>subItem</property>" +
                    "</directValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsDirectValueForValidAttributeDynamicValueWithoutDefault()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"$[value]\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<directValue>" +
                        "<parameter>value</parameter>" +
                        "<default></default>" +
                        "<property>attrib</property>" +
                    "</directValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsDirectValueForValidNodeDynamicValueWithoutDefault()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>$[value]</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<directValue>" +
                        "<parameter>value</parameter>" +
                        "<default></default>" +
                        "<property>subItem</property>" +
                    "</directValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsSingleReplacementValueForValidAttributeDynamicValue()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"($[value1|default])\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>({0})</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>attrib</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsSingleReplacementValueForValidNodeDynamicValue()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>($[value1|default])</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>({0})</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>subItem</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsSingleReplacementValueForValidAttributeDynamicValueWithoutDefault()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"($[value1])\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>({0})</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"\" />" +
                        "</parameters>" +
                        "<property>attrib</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsSingleReplacementValueForValidNodeDynamicValueWithoutDefault()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>($[value1])</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>({0})</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"\" />" +
                        "</parameters>" +
                        "<property>subItem</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsSingleReplacementValueForValidAttributeDynamicValueWithFormat()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"($[value1|default|00])\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>({0:00})</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>attrib</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsSingleReplacementValueForValidNodeDynamicValueWithFormat()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>($[value1|default|00])</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>({0:00})</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>subItem</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsMultipleReplacementValueForValidAttributeDynamicValue()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"$[value1|default] $[value2|default]\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>{0} {1}</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                            "<namedValue name=\"value2\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>attrib</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsMultipleReplacementValueForValidNodeDynamicValue()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>$[value1|default] $[value2|default]</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>{0} {1}</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                            "<namedValue name=\"value2\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>subItem</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsMultipleReplacementValueForValidAttributeDynamicValueWithoutDefault()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"$[value1] $[value2]\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>{0} {1}</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"\" />" +
                            "<namedValue name=\"value2\" value=\"\" />" +
                        "</parameters>" +
                        "<property>attrib</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsMultipleReplacementValueForValidNodeDynamicValueWithoutDefault()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>$[value1] $[value2]</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>{0} {1}</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"\" />" +
                            "<namedValue name=\"value2\" value=\"\" />" +
                        "</parameters>" +
                        "<property>subItem</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsMultipleReplacementValueForValidAttributeDynamicValueWithFormat()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"$[value1|default|00] $[value2|default|00]\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>{0:00} {1:00}</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                            "<namedValue name=\"value2\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>attrib</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsMultipleReplacementValueForValidNodeDynamicValueWithFormat()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"value\"><subItem>$[value1|default|00] $[value2|default|00]</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"value\"><subItem></subItem>" +
                "<dynamicValues>" +
                    "<replacementValue>" +
                        "<format>{0:00} {1:00}</format>" +
                        "<parameters>" +
                            "<namedValue name=\"value1\" value=\"default\" />" +
                            "<namedValue name=\"value2\" value=\"default\" />" +
                        "</parameters>" +
                        "<property>subItem</property>" +
                    "</replacementValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void PreprocessParametersAddsDirectValueForValidAttributeDynamicValueWithEscapedCharacter()
        {
            var document = new XmlDocument();
            var xml = "<item attrib=\"$[first\\|second|first\\|second]\"><subItem>Text</subItem></item>";
            document.LoadXml(xml);
            var task = new TestTask();
            var actual = task.PreprocessParameters(document.DocumentElement);
            var expected = "<item attrib=\"first|second\"><subItem>Text</subItem>" +
                "<dynamicValues>" +
                    "<directValue>" +
                        "<parameter>first|second</parameter>" +
                        "<default>first|second</default>" +
                        "<property>attrib</property>" +
                    "</directValue>" +
                "</dynamicValues></item>";
            Assert.AreEqual(expected, actual.OuterXml);
        }
        [Test]
        public void ApplyParametersHandlesNull()
        {
            var task = new TestTask()
            {
                DynamicValues = null
            };
            var parameters = new Dictionary<string, string>();
            var definitions = new List<ParameterBase>();
            task.ApplyParameters(parameters, definitions);
        }
        [Test]
        public void ApplyParametersHandlesEmpty()
        {
            var task = new TestTask()
            {
                DynamicValues = new IDynamicValue[0]
            };
            var parameters = new Dictionary<string, string>();
            var definitions = new List<ParameterBase>();
            task.ApplyParameters(parameters, definitions);
        }
        [Test]
        public void ApplyParametersHandlesParameter()
        {
            var dynamicValue = mocks.StrictMock<IDynamicValue>();
            var task = new TestTask()
            {
                DynamicValues = new IDynamicValue[]
                {
                    dynamicValue
                }
            };
            var parameters = new Dictionary<string, string>();
            var definitions = new List<ParameterBase>();
            Expect.Call(() =>
            {
                dynamicValue.ApplyTo(task, parameters, definitions);
            });
            mocks.ReplayAll();
            task.ApplyParameters(parameters, definitions);
            mocks.VerifyAll();
        }
        [Test]
        public void AssociateContextValidatesArguments()
        {
            var task = new TestTask();
            var error = Assert.Throws<ArgumentNullException>(() => task.AssociateContext(null));
            Assert.AreEqual("context", error.ParamName);
        }
        [Test]
        public void AssociateContextStoresContext()
        {
            var basePath = Path.GetTempPath();
            var ioSystem = this.mocks.StrictMock<IFileSystem>();
            var context = new TaskContext(ioSystem, basePath);
            var task = new TestTask();
            task.AssociateContext(context);
            Assert.AreSame(context, task.Context);
        }
        private class TestTask
            : TaskBase
        {
            public ThoughtWorks.CruiseControl.CCTrayLib.Presentation.Func<bool> Result { get; set; }
            public bool Executed { get; set; }
            protected override bool Execute(IIntegrationResult result)
            {
                Executed = true;
                return Result();
            }
        }
    }
}
