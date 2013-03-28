using System;
using System.Collections;
using System.Drawing.Imaging;
using System.Text.RegularExpressions;
using System.Xml;
using System.Xml.XPath;
using ThoughtWorks.CruiseControl.Core.Util;
namespace ThoughtWorks.CruiseControl.Core.Publishers.Statistics
{
    public class StatisticsChartGenerator
    {
        private string[] relevantStats;
        private IPlotter plotter;
        public StatisticsChartGenerator()
        {
        }
        public StatisticsChartGenerator(IPlotter plotter)
        {
            this.plotter = plotter;
        }
        public string[] RelevantStats
        {
            get { return relevantStats; }
            set { relevantStats = value; }
        }
        private IPlotter GetPlotter(string savePath)
        {
            if (plotter == null)
            {
                plotter = new Plotter(savePath, "png", ImageFormat.Png);
            }
            return plotter;
        }
        public void Process(XmlDocument xmlDocument, string savePath)
        {
            foreach (string relevantStat in relevantStats)
            {
                if (!AvailableStatistics(xmlDocument).Contains(relevantStat))
                {
                    throw new UnavailableStatisticsException(relevantStat);
                }
                XPathNavigator navigator = xmlDocument.CreateNavigator();
                string xpath = "/statistics/integration";
                XPathNodeIterator dataList = navigator.Select(xpath);
                ArrayList ordinateData = new ArrayList();
                IList abscissaData = new ArrayList();
                while (dataList.MoveNext())
                {
                    XPathNavigator integration = dataList.Current;
                    XPathNavigator statistic =
                        integration.SelectSingleNode(string.Format("statistic[@name='{0}']", relevantStat));
                    string value = statistic.Value;
                    Log.Debug(string.Format("Relevant Stat: {0}, Raw Value: {1}", relevantStat, value));
                    ordinateData.Add(GetPlottableValue(relevantStat, value));
                    abscissaData.Add(integration.GetAttribute("build-label", string.Empty));
                }
                try
                {
                    GetPlotter(savePath).DrawGraph(ordinateData, abscissaData, relevantStat);
                }
                catch (Exception)
                {
                    throw new Exception(string.Format("Cannot handle value for the statistic {0}", relevantStat));
                }
            }
        }
        private static string GetPlottableValue(string relevantStat, string value)
        {
            if (relevantStat == "Duration" && Regex.IsMatch(value, "[0-9]+:[0-9]+:[0-9]+"))
            {
                string[] parts = value.Split(':');
                value = Convert.ToInt32(parts[0]) * 3600 + Convert.ToInt32(parts[1]) * 60 + parts[2];
            }
            return value;
        }
        private static ArrayList AvailableStatistics(XmlDocument xmlDocument)
        {
            XPathNavigator xPathNavigator = xmlDocument.CreateNavigator();
            XPathExpression statsNames = xPathNavigator.Compile("/statistics/integration[1]/statistic/@name");
            XPathNodeIterator nodeIterator = xPathNavigator.Select(statsNames);
            ArrayList stats = new ArrayList();
            while (nodeIterator.MoveNext())
            {
                stats.Add(nodeIterator.Current.Value);
            }
            return stats;
        }
    }
}
