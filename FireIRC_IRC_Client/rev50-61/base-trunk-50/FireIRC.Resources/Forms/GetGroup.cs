using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using OVT.Melissa.PluginSupport;
using OVT.FireIRC.Resources.Resources;
namespace OVT.FireIRC.Resources
{
    public partial class GetGroup : Form
    {
        public string group;
        public GetGroup()
        {
            InitializeComponent();
            foreach (Server i in FireIRCCore.Settings.Servers)
            {
                if (listBox1.Items.Contains(i.GroupName) != true)
                {
                    listBox1.Items.Add(i.GroupName);
                }
            }
            ResourceManagement.ConvertToManagedResource(this);
        }
        private void GetGroup_Load(object sender, EventArgs e)
        {
        }
        private void button1_Click(object sender, EventArgs e)
        {
            group = (string)listBox1.SelectedItem;
            DialogResult = DialogResult.OK;
            this.Close();
        }
    }
}
