namespace ProcessHacker.Components
{
    partial class SectionProperties
    {
        private System.ComponentModel.IContainer components = null;
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            _sectionHandle.Dereference(disposing);
            base.Dispose(disposing);
        }
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.labelSize = new System.Windows.Forms.Label();
            this.labelAttributes = new System.Windows.Forms.Label();
            this.SuspendLayout();
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(6, 3);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(54, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Attributes:";
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(6, 25);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(30, 13);
            this.label2.TabIndex = 0;
            this.label2.Text = "Size:";
            this.labelSize.AutoSize = true;
            this.labelSize.Location = new System.Drawing.Point(75, 25);
            this.labelSize.Name = "labelSize";
            this.labelSize.Size = new System.Drawing.Size(23, 13);
            this.labelSize.TabIndex = 0;
            this.labelSize.Text = "0 B";
            this.labelAttributes.AutoSize = true;
            this.labelAttributes.Location = new System.Drawing.Point(75, 3);
            this.labelAttributes.Name = "labelAttributes";
            this.labelAttributes.Size = new System.Drawing.Size(36, 13);
            this.labelAttributes.TabIndex = 0;
            this.labelAttributes.Text = "Image";
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.labelAttributes);
            this.Controls.Add(this.labelSize);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Name = "SectionProperties";
            this.Padding = new System.Windows.Forms.Padding(3);
            this.Size = new System.Drawing.Size(185, 50);
            this.ResumeLayout(false);
            this.PerformLayout();
        }
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label labelSize;
        private System.Windows.Forms.Label labelAttributes;
    }
}
