using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using Novell.iFolderCom;
using Novell.iFolder.Web;
using System.IO;
namespace Novell.Wizard
{
    public partial class ExportKeyPage : Novell.Wizard.InteriorPageTemplate
    {
        private SimiasWebService simiasWebService = null;
        private iFolderWebService ifWebService = null;
        private DomainItem selectedDomain = null;
        private KeyRecoveryWizard wizard;
        private System.ComponentModel.Container components = null;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        public System.Windows.Forms.TextBox filePath;
        private System.Windows.Forms.Button BrowseButton;
        private System.Windows.Forms.Label recoveryAgentLabel;
        private Label label1;
        private System.Windows.Forms.TextBox recoveryAgent;
        private string domainID;
        private string emailAddress;
        public ExportKeyPage(iFolderWebService ifws, SimiasWebService simws)
        {
            InitializeComponent();
            this.simiasWebService = simws;
            this.ifWebService = ifws;
        }
         private string GetDefaultPath(string domainName)
        {
            string appdata = System.Environment.GetEnvironmentVariable("APPDATA");
            int i = appdata.LastIndexOf("\\");
            appdata = appdata.Substring(0, i);
            appdata = appdata + "\\" + domainName + ".xml";
            return appdata;
        }
        public string EmailAddress
        {
            get { return emailAddress; }
            set { emailAddress = value; }
        }
        public string ExportPath
        {
            get { return filePath.Text; }
            set { filePath.Text = value; }
        }
        internal override void ActivatePage(int previousIndex)
        {
            base.ActivatePage(previousIndex);
            wizard = (KeyRecoveryWizard)this.Parent;
            selectedDomain = wizard.DomainSelectionPage.SelectedDomain;
            this.accountBox.Text = selectedDomain.Name;
            DisplayRAName(selectedDomain);
            UpdateSensitivity();
        }
        internal override int DeactivatePage()
        {
            return base.DeactivatePage();
        }
        internal override int ValidatePage(int currentIndex)
        {
            bool result = false;
            result = Export_func();
            if (result == false)
            {
                return currentIndex;
            }
            return base.ValidatePage(currentIndex);
        }
        private bool Export_func()
        {
            bool result = true;
            try
            {
                this.simiasWebService.ExportiFoldersCryptoKeys(selectedDomain.ID, this.filePath.Text);
            }
            catch (Exception ex)
            {
                MyMessageBox mmb =
                    new MyMessageBox(TrayApp.Properties.Resources.unableToExportMesg,
                    TrayApp.Properties.Resources.wizardText,
                    null,MyMessageBoxButtons.OK,
                    MyMessageBoxIcon.Error);
                    mmb.ShowDialog();
                    result = false;
            }
            return result;
        }
        private void UpdateSensitivity()
        {
            if (
               this.filePath.Text.Length > 0 && this.filePath.Text.EndsWith(".xml")
               )
            {
                ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Next | KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel;
            }
            else
            {
                ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel;
            }
        }
        private void DisplayRAName(DomainItem selectedDomain)
        {
            try
            {
                string RAName = this.ifWebService.GetRAName(selectedDomain.ID);
                if (RAName == null || RAName == "")
                {
                    this.recoveryAgent.Text = "";
                    return;
                }
                else
                {
                    this.recoveryAgent.Text = RAName;
                    char[] EmailParser = { '=' };
                    string[] ParsedString = RAName.Split(EmailParser);
                    string emailID = "";
                    if (ParsedString.Length > 1)
                    {
                        for (int x = 0; x < ParsedString.Length; x++)
                        {
                            char[] FinalEmailParser = { '@' };
                            string[] FinalParsedString = ParsedString[x].Split(FinalEmailParser);
                            if (FinalParsedString.Length > 1)
                            {
                                emailID = ParsedString[x];
                                emailAddress = emailID;
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
            }
        }
        private void BrowseButton_Click(object sender, EventArgs e)
        {
            SaveFileDialog fileDlg = new SaveFileDialog();
            fileDlg.Filter = "XML Files|*.xml";
            if (fileDlg.ShowDialog() == DialogResult.OK)
            {
               this.filePath.Text = fileDlg.FileName;
            }
        }
        private void filePath_TextChanged(object sender, EventArgs e)
        {
           UpdateSensitivity();
        }
    }
}
