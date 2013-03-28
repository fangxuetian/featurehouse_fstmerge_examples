namespace CCNet.CSharpDemos.Plugin.SourceControl
{
    using System;
    using System.IO;
    using Exortech.NetReflector;
    using ThoughtWorks.CruiseControl.Core;
    [ReflectorType("indexFileSource")]
    public class IndexFileSourceControl
        : ISourceControl
    {
        [ReflectorProperty("file")]
        public string FileName { get; set; }
        public Modification[] GetModifications(IIntegrationResult from_, IIntegrationResult to)
        {
            var path = to.BaseFromWorkingDirectory(this.FileName);
            using (var reader = new StreamReader(path))
            {
                var parser = new IndexFileHistoryParser();
                return parser.Parse(reader, from_.StartTime, to.StartTime);
            }
        }
        public void LabelSourceControl(IIntegrationResult result)
        {
            var fileName = Path.Combine(
                Path.GetDirectoryName(this.FileName),
                DateTime.Now.ToString("yyyyMMddHHmmss") + ".label");
            File.WriteAllText(fileName, result.Label);
        }
        public void GetSource(IIntegrationResult result)
        {
            foreach (var modification in result.Modifications)
            {
                var source = Path.Combine(
                    modification.FolderName,
                    modification.FileName);
                var destination = result.BaseFromWorkingDirectory(
                    modification.FileName);
                if (File.Exists(source))
                {
                    File.Copy(source, destination, true);
                }
                else
                {
                    File.Delete(destination);
                }
            }
        }
        public void Initialize(IProject project)
        {
        }
        public void Purge(IProject project)
        {
        }
    }
}
