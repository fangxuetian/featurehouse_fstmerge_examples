using System.Windows.Forms;
namespace WikiFunctions.Controls
{
    public partial class LoginDlg : Form
    {
        public LoginDlg()
        {
            InitializeComponent();
        }
        public string UserName
        {
            get { return txtName.Text; }
            set { txtName.Text = value; }
        }
        public string Password
        {
            get { return txtPassword.Text; }
            set { txtPassword.Text = value; }
        }
    }
}
