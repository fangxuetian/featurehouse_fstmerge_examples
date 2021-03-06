using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using Novell.iFolder.Web;
using Novell.FormsTrayApp;
using Novell.iFolderCom;
using Simias.Client;
using Simias.Client.Authentication;
using Simias.Client.Event;
using TrayApp.Properties;
namespace Novell.Wizard
{
    public partial class ImportKeyPage : Novell.Wizard.InteriorPageTemplate
    {
        private iFolderWebService ifWebService = null;
        private SimiasWebService simiasWebService = null;
        private KeyRecoveryWizard wizard;
        private DomainItem selectedDomain = null;
        private System.ComponentModel.IContainer components = null;
        public ImportKeyPage(iFolderWebService ifws,SimiasWebService simws)
        {
            InitializeComponent();
            this.ifWebService = ifws;
            this.simiasWebService = simws;
         }
        internal override void ActivatePage(int previousIndex)
        {
            base.ActivatePage(previousIndex);
            wizard = (KeyRecoveryWizard)this.Parent;
            selectedDomain = wizard.DomainSelectionPage.SelectedDomain;
            this.accountBox.Text = selectedDomain.Name;
            this.oneTimePassphrase.Enabled = false;
           ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel;
           this.LocationEntry.Focus();
        }
        internal override int DeactivatePage()
        {
            return base.DeactivatePage();
        }
        internal override int ValidatePage(int currentIndex)
        {
            bool result = true;
            result = Import_func();
            if (result == false)
            {
                return currentIndex;
            }
            return base.ValidatePage(currentIndex);
        }
        private void BrowseButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog fileDlg = new OpenFileDialog();
            fileDlg.Filter = "XML Files|*.xml";
            if (fileDlg.ShowDialog() == DialogResult.OK)
            {
                this.LocationEntry.Text = fileDlg.FileName;
            }
        }
        private void UpdateSensitivity()
        {
            if( this.passphrase.Text.Length >0 && this.reTypePassphrase.Text.Length > 0 && this.reTypePassphrase.Text == this.passphrase.Text && this.LocationEntry.Text.Length > 0 && this.LocationEntry.Text.EndsWith(".xml") )
            {
                if ( this.isEncrypted.Checked == true)
                {
                    if( this.oneTimePassphrase.Text.Length >0)
                    ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Next | KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel;
                    else
                    ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel;
                }
                else
                {
                    ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel|KeyRecoveryWizardButtons.Next;
                }
             }
            else
                 ((KeyRecoveryWizard)this.Parent).WizardButtons = KeyRecoveryWizardButtons.Back | KeyRecoveryWizardButtons.Cancel;
        }
        private void LocationEntry_TextChanged(object sender, EventArgs e)
        {
            UpdateSensitivity();
        }
        private void oneTimePassphrase_TextChanged(object sender, EventArgs e)
        {
            UpdateSensitivity();
        }
        private void passphrase_TextChanged(object sender, EventArgs e)
        {
            UpdateSensitivity();
        }
        private void reTypePassphrase_TextChanged(object sender, EventArgs e)
        {
            UpdateSensitivity();
        }
        private bool Import_func()
        {
            try
            {
                string onetimepp;
                if (this.oneTimePassphrase != null)
                    onetimepp = this.oneTimePassphrase.Text;
                else
                    onetimepp = null;
                this.simiasWebService.ImportiFoldersCryptoKeys(selectedDomain.ID, this.passphrase.Text, onetimepp, this.LocationEntry.Text);
                bool rememberOption = this.simiasWebService.GetRememberOption(selectedDomain.ID);
                this.simiasWebService.StorePassPhrase(selectedDomain.ID, "", CredentialType.None, false);
                this.simiasWebService.StorePassPhrase(selectedDomain.ID, this.passphrase.Text, CredentialType.Basic, rememberOption);
            }
            catch (Exception ex)
            {
                MyMessageBox mmb = new MyMessageBox(TrayApp.Properties.Resources.importErrorMesg, TrayApp.Properties.Resources.wizardText, null,MyMessageBoxButtons.OK, MyMessageBoxIcon.Error);
                mmb.ShowDialog();
                return false;
            }
            return true;
        }
        private void isEncrypted_CheckedChanged(object sender, EventArgs e)
        {
            if (isEncrypted.Checked == true)
                this.oneTimePassphrase.Enabled = true;
            else
                this.oneTimePassphrase.Enabled = false;
            UpdateSensitivity();
        }
    }
}
