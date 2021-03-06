namespace ProcessHacker
{
    partial class HandleStatisticsWindow
    {
        private System.ComponentModel.IContainer components = null;
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }
        private void InitializeComponent()
        {
            this.buttonClose = new System.Windows.Forms.Button();
            this.listTypes = new System.Windows.Forms.ListView();
            this.columnType = new System.Windows.Forms.ColumnHeader();
            this.columnNumber = new System.Windows.Forms.ColumnHeader();
            this.SuspendLayout();
            this.buttonClose.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.buttonClose.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.buttonClose.Location = new System.Drawing.Point(264, 268);
            this.buttonClose.Name = "buttonClose";
            this.buttonClose.Size = new System.Drawing.Size(75, 23);
            this.buttonClose.TabIndex = 0;
            this.buttonClose.Text = "Close";
            this.buttonClose.UseVisualStyleBackColor = true;
            this.buttonClose.Click += new System.EventHandler(this.buttonClose_Click);
            this.listTypes.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.listTypes.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnType,
            this.columnNumber});
            this.listTypes.FullRowSelect = true;
            this.listTypes.HideSelection = false;
            this.listTypes.Location = new System.Drawing.Point(12, 12);
            this.listTypes.Name = "listTypes";
            this.listTypes.ShowItemToolTips = true;
            this.listTypes.Size = new System.Drawing.Size(327, 250);
            this.listTypes.Sorting = System.Windows.Forms.SortOrder.Ascending;
            this.listTypes.TabIndex = 1;
            this.listTypes.UseCompatibleStateImageBehavior = false;
            this.listTypes.View = System.Windows.Forms.View.Details;
            this.columnType.Text = "Type";
            this.columnType.Width = 150;
            this.columnNumber.Text = "Number";
            this.columnNumber.Width = 100;
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(351, 303);
            this.Controls.Add(this.listTypes);
            this.Controls.Add(this.buttonClose);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "HandleStatisticsWindow";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Handle Statistics";
            this.ResumeLayout(false);
        }
        private System.Windows.Forms.Button buttonClose;
        private System.Windows.Forms.ListView listTypes;
        private System.Windows.Forms.ColumnHeader columnType;
        private System.Windows.Forms.ColumnHeader columnNumber;
    }
}
