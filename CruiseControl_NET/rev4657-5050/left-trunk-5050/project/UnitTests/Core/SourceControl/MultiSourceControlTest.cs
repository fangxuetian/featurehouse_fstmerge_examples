using System;
using System.Collections;
using Exortech.NetReflector;
using NMock;
using NUnit.Framework;
using ThoughtWorks.CruiseControl.Core;
using ThoughtWorks.CruiseControl.Core.Sourcecontrol;
namespace ThoughtWorks.CruiseControl.UnitTests.Core.Sourcecontrol
{
 [TestFixture]
 public class MultiSourceControlTest : CustomAssertion
 {
  public static string SourceControlXml = @"<sourcecontrol type=""multi"">
 <sourceControls>
  <mocksourcecontrol>
   <anOptionalProperty>foo</anOptionalProperty>
  </mocksourcecontrol>
  <mocksourcecontrol>
   <anOptionalProperty>bar</anOptionalProperty>
  </mocksourcecontrol>
 </sourceControls>
</sourcecontrol>
";
  [Test]
  public void ValuePopulation()
  {
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   NetReflector.Read(SourceControlXml, multiSourceControl);
   Assert.IsTrue(multiSourceControl.SourceControls.Length == 2);
   string optionalProp0 = ((SourceControlMock) multiSourceControl.SourceControls[0]).AnOptionalProperty;
   string optionalProp1 = ((SourceControlMock) multiSourceControl.SourceControls[1]).AnOptionalProperty;
   bool fooFound = optionalProp0 == "foo" || optionalProp1 == "foo";
   bool barFound = optionalProp0 == "bar" || optionalProp1 == "bar";
   Assert.IsTrue(fooFound && barFound);
  }
  [Test]
  public void PassesThroughLabelSourceControl()
  {
   IntegrationResult result = new IntegrationResult();
   DynamicMock mockSC1 = new DynamicMock(typeof (ISourceControl));
   mockSC1.Expect("LabelSourceControl", result);
   DynamicMock mockSC2 = new DynamicMock(typeof (ISourceControl));
   mockSC2.Expect("LabelSourceControl", result);
   ISourceControl[] sourceControls = new ISourceControl[] {(ISourceControl) mockSC1.MockInstance, (ISourceControl) mockSC2.MockInstance};
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.SourceControls = sourceControls;
   multiSourceControl.LabelSourceControl(result);
   mockSC1.Verify();
   mockSC2.Verify();
  }
  [Test]
  public void PassesThroughGetSourceControlAndCombinesResults()
  {
   IntegrationResult from_ = IntegrationResultMother.CreateSuccessful(DateTime.Now);
   IntegrationResult to = IntegrationResultMother.CreateSuccessful(DateTime.Now.AddDays(10));
   Modification mod1 = new Modification();
   mod1.Comment = "Testing Multi";
   Modification mod2 = new Modification();
   mod2.Comment = "More Multi";
   Modification mod3 = new Modification();
   mod3.Comment = "Yet More Multi";
   ArrayList mocks = new ArrayList();
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod1, mod2}, from_, to));
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod3}, from_, to));
   mocks.Add(CreateModificationsSourceControlMock(new Modification[0], from_, to));
   mocks.Add(CreateModificationsSourceControlMock(null, from_, to));
   ArrayList scList = new ArrayList();
   foreach (DynamicMock mock in mocks)
   {
    scList.Add(mock.MockInstance);
   }
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.SourceControls = (ISourceControl[]) scList.ToArray(typeof (ISourceControl));
   ArrayList returnedMods = new ArrayList(multiSourceControl.GetModifications(from_, to));
   foreach (DynamicMock mock in mocks)
   {
    mock.Verify();
   }
   Assert.IsTrue(returnedMods.Contains(mod1));
   Assert.IsTrue(returnedMods.Contains(mod2));
   Assert.IsTrue(returnedMods.Contains(mod3));
  }
  [Test]
  public void ShouldInstructAggregatedSourceControlsToGetSource()
  {
   IntegrationResult result = new IntegrationResult();
   IMock mockSC1 = new DynamicMock(typeof (ISourceControl));
   IMock mockSC2 = new DynamicMock(typeof (ISourceControl));
   mockSC1.Expect("GetSource", result);
   mockSC2.Expect("GetSource", result);
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.SourceControls = new ISourceControl[] {(ISourceControl) mockSC1.MockInstance, (ISourceControl) mockSC2.MockInstance};
   multiSourceControl.GetSource(result);
   mockSC1.Verify();
   mockSC2.Verify();
  }
  private DynamicMock CreateModificationsSourceControlMock(Modification[] mods, IntegrationResult dt1, IntegrationResult dt2)
  {
   DynamicMock mock = new DynamicMock(typeof (ISourceControl));
   mock.ExpectAndReturn("GetModifications", mods, dt1, dt2);
   return mock;
  }
  [Test]
  public void IfRequireChangesFromAllTrueAndAllSourceControlHasModificationsThenReturnMods()
  {
   IntegrationResult from_ = IntegrationResultMother.CreateSuccessful(DateTime.Now);
   IntegrationResult to = IntegrationResultMother.CreateSuccessful(DateTime.Now.AddDays(10));
   Modification mod1 = new Modification();
   mod1.Comment = "Testing Multi";
   Modification mod2 = new Modification();
   mod2.Comment = "Testing Multi";
   ArrayList mocks = new ArrayList();
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod1}, from_, to));
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod2}, from_, to));
   ArrayList scList = new ArrayList();
   foreach (DynamicMock mock in mocks)
   {
    scList.Add(mock.MockInstance);
   }
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.SourceControls = (ISourceControl[]) scList.ToArray(typeof (ISourceControl));
   multiSourceControl.RequireChangesFromAll = true;
   ArrayList returnedMods = new ArrayList(multiSourceControl.GetModifications(from_, to));
   Assert.AreEqual(2, returnedMods.Count);
   foreach (DynamicMock mock in mocks)
   {
    mock.Verify();
   }
  }
  [Test]
  public void IfRequireChangesFromAllTrueAndSecondSourceControlHasEmptyChangesThenReturnEmpty()
  {
   IntegrationResult from_ = IntegrationResultMother.CreateSuccessful(DateTime.Now);
   IntegrationResult to = IntegrationResultMother.CreateSuccessful(DateTime.Now.AddDays(10));
   Modification mod1 = new Modification();
   mod1.Comment = "Testing Multi";
   ArrayList mocks = new ArrayList();
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod1}, from_, to));
   mocks.Add(CreateModificationsSourceControlMock(new Modification[0], from_, to));
   ArrayList scList = new ArrayList();
   foreach (DynamicMock mock in mocks)
   {
    scList.Add(mock.MockInstance);
   }
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.SourceControls = (ISourceControl[]) scList.ToArray(typeof (ISourceControl));
   multiSourceControl.RequireChangesFromAll = true;
   ArrayList returnedMods = new ArrayList(multiSourceControl.GetModifications(from_, to));
   foreach (DynamicMock mock in mocks)
   {
    mock.Verify();
   }
   Assert.AreEqual(0, returnedMods.Count);
  }
  [Test]
  public void IfRequireChangesFromAllTrueAndFirstSourceControlHasEmptyChangesThenReturnEmpty()
  {
   IntegrationResult from_ = IntegrationResultMother.CreateSuccessful(DateTime.Now);
   IntegrationResult to = IntegrationResultMother.CreateSuccessful(DateTime.Now.AddDays(10));
   Modification mod1 = new Modification();
   mod1.Comment = "Testing Multi";
   ArrayList mocks = new ArrayList();
   mocks.Add(CreateModificationsSourceControlMock(new Modification[0], from_, to));
   DynamicMock nonCalledMock = new DynamicMock(typeof (ISourceControl));
   nonCalledMock.ExpectNoCall("GetModifications", typeof(IIntegrationResult), typeof(IIntegrationResult));
   mocks.Add(nonCalledMock);
   ArrayList scList = new ArrayList();
   foreach (DynamicMock mock in mocks)
   {
    scList.Add(mock.MockInstance);
   }
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.SourceControls = (ISourceControl[]) scList.ToArray(typeof (ISourceControl));
   multiSourceControl.RequireChangesFromAll = true;
   ArrayList returnedMods = new ArrayList(multiSourceControl.GetModifications(from_, to));
   foreach (DynamicMock mock in mocks)
   {
    mock.Verify();
   }
   Assert.AreEqual(0, returnedMods.Count);
  }
  [Test]
  public void IfRequireChangesFromAllTrueAndNoSourceControlHasEmptyChangesThenReturnChanges()
  {
   IntegrationResult from_ = IntegrationResultMother.CreateSuccessful(DateTime.Now);
   IntegrationResult to = IntegrationResultMother.CreateSuccessful(DateTime.Now.AddDays(10));
   Modification mod1 = new Modification();
   mod1.Comment = "Testing Multi";
   Modification mod2 = new Modification();
   mod2.Comment = "More Multi";
   Modification mod3 = new Modification();
   mod3.Comment = "Yet More Multi";
   ArrayList mocks = new ArrayList();
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod1, mod2}, from_, to));
   mocks.Add(CreateModificationsSourceControlMock(new Modification[] {mod3}, from_, to));
   ArrayList scList = new ArrayList();
   foreach (DynamicMock mock in mocks)
   {
    scList.Add(mock.MockInstance);
   }
   MultiSourceControl multiSourceControl = new MultiSourceControl();
   multiSourceControl.RequireChangesFromAll = true;
   multiSourceControl.SourceControls = (ISourceControl[]) scList.ToArray(typeof (ISourceControl));
   ArrayList returnedMods = new ArrayList(multiSourceControl.GetModifications(from_, to));
   foreach (DynamicMock mock in mocks)
   {
    mock.Verify();
   }
   Assert.IsTrue(returnedMods.Contains(mod1));
   Assert.IsTrue(returnedMods.Contains(mod2));
   Assert.IsTrue(returnedMods.Contains(mod3));
  }
 }
}
