namespace MsmqGatewayTestApplication
{
    partial class SimulateForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.previewTextBox = new System.Windows.Forms.TextBox();
            this.btnSend = new System.Windows.Forms.Button();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.countNumeric = new System.Windows.Forms.NumericUpDown();
            this.groupNumeric = new System.Windows.Forms.NumericUpDown();
            this.label2 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.countNumeric)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.groupNumeric)).BeginInit();
            this.SuspendLayout();
            // 
            // previewTextBox
            // 
            this.previewTextBox.Location = new System.Drawing.Point(12, 96);
            this.previewTextBox.Multiline = true;
            this.previewTextBox.Name = "previewTextBox";
            this.previewTextBox.ReadOnly = true;
            this.previewTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.previewTextBox.Size = new System.Drawing.Size(339, 157);
            this.previewTextBox.TabIndex = 0;
            // 
            // btnSend
            // 
            this.btnSend.Location = new System.Drawing.Point(276, 259);
            this.btnSend.Name = "btnSend";
            this.btnSend.Size = new System.Drawing.Size(75, 23);
            this.btnSend.TabIndex = 1;
            this.btnSend.Text = "Send";
            this.btnSend.UseVisualStyleBackColor = true;
            this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
            // 
            // comboBox1
            // 
            this.comboBox1.FormattingEnabled = true;
            this.comboBox1.Items.AddRange(new object[] {
            "digitv-activate"});
            this.comboBox1.Location = new System.Drawing.Point(12, 12);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(339, 21);
            this.comboBox1.TabIndex = 2;
            this.comboBox1.SelectedIndexChanged += new System.EventHandler(this.comboBox1_SelectedIndexChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 46);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(35, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Count";
            // 
            // countNumeric
            // 
            this.countNumeric.Increment = new decimal(new int[] {
            100,
            0,
            0,
            0});
            this.countNumeric.Location = new System.Drawing.Point(53, 44);
            this.countNumeric.Maximum = new decimal(new int[] {
            100000,
            0,
            0,
            0});
            this.countNumeric.Name = "countNumeric";
            this.countNumeric.Size = new System.Drawing.Size(120, 20);
            this.countNumeric.TabIndex = 4;
            this.countNumeric.ValueChanged += new System.EventHandler(this.countNumeric_ValueChanged);
            // 
            // groupNumeric
            // 
            this.groupNumeric.Increment = new decimal(new int[] {
            10,
            0,
            0,
            0});
            this.groupNumeric.Location = new System.Drawing.Point(53, 70);
            this.groupNumeric.Maximum = new decimal(new int[] {
            500,
            0,
            0,
            0});
            this.groupNumeric.Name = "groupNumeric";
            this.groupNumeric.Size = new System.Drawing.Size(120, 20);
            this.groupNumeric.TabIndex = 5;
            this.groupNumeric.ValueChanged += new System.EventHandler(this.groupNumeric_ValueChanged);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 72);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(36, 13);
            this.label2.TabIndex = 6;
            this.label2.Text = "Group";
            // 
            // SimulateForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(363, 294);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.groupNumeric);
            this.Controls.Add(this.countNumeric);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.comboBox1);
            this.Controls.Add(this.btnSend);
            this.Controls.Add(this.previewTextBox);
            this.Name = "SimulateForm";
            this.Text = "Simulate";
            ((System.ComponentModel.ISupportInitialize)(this.countNumeric)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.groupNumeric)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox previewTextBox;
        private System.Windows.Forms.Button btnSend;
        private System.Windows.Forms.ComboBox comboBox1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.NumericUpDown countNumeric;
        private System.Windows.Forms.NumericUpDown groupNumeric;
        private System.Windows.Forms.Label label2;
    }
}