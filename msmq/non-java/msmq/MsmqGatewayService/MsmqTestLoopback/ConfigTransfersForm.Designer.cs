namespace MsmqTestLoopback
{
    partial class ConfigTransfersForm
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
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.okButton = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.removeButton = new System.Windows.Forms.Button();
            this.addButton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.requestListBox = new System.Windows.Forms.ListBox();
            this.responseListBox = new System.Windows.Forms.ListBox();
            this.transferListView = new System.Windows.Forms.ListView();
            this.requestColumn = new System.Windows.Forms.ColumnHeader();
            this.responseColumn = new System.Windows.Forms.ColumnHeader();
            this.tableLayoutPanel1.SuspendLayout();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.Controls.Add(this.okButton, 1, 4);
            this.tableLayoutPanel1.Controls.Add(this.panel1, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.label2, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.requestListBox, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.responseListBox, 1, 1);
            this.tableLayoutPanel1.Controls.Add(this.transferListView, 0, 3);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 5;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 20F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 35F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableLayoutPanel1.Size = new System.Drawing.Size(592, 466);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // okButton
            // 
            this.okButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.okButton.Location = new System.Drawing.Point(514, 440);
            this.okButton.Name = "okButton";
            this.okButton.Size = new System.Drawing.Size(75, 23);
            this.okButton.TabIndex = 0;
            this.okButton.Text = "Ok";
            this.okButton.UseVisualStyleBackColor = true;
            this.okButton.Click += new System.EventHandler(this.okButton_Click);
            // 
            // panel1
            // 
            this.tableLayoutPanel1.SetColumnSpan(this.panel1, 2);
            this.panel1.Controls.Add(this.removeButton);
            this.panel1.Controls.Add(this.addButton);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel1.Location = new System.Drawing.Point(3, 214);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(586, 29);
            this.panel1.TabIndex = 1;
            // 
            // removeButton
            // 
            this.removeButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.removeButton.Enabled = false;
            this.removeButton.Location = new System.Drawing.Point(495, 3);
            this.removeButton.Name = "removeButton";
            this.removeButton.Size = new System.Drawing.Size(82, 23);
            this.removeButton.TabIndex = 1;
            this.removeButton.Text = "Remove";
            this.removeButton.UseVisualStyleBackColor = true;
            this.removeButton.Click += new System.EventHandler(this.removeButton_Click);
            // 
            // addButton
            // 
            this.addButton.Enabled = false;
            this.addButton.Location = new System.Drawing.Point(9, 3);
            this.addButton.Name = "addButton";
            this.addButton.Size = new System.Drawing.Size(75, 23);
            this.addButton.TabIndex = 0;
            this.addButton.Text = "Add";
            this.addButton.UseVisualStyleBackColor = true;
            this.addButton.Click += new System.EventHandler(this.addButton_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Dock = System.Windows.Forms.DockStyle.Left;
            this.label1.Location = new System.Drawing.Point(3, 0);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(47, 20);
            this.label1.TabIndex = 2;
            this.label1.Text = "Request";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Dock = System.Windows.Forms.DockStyle.Left;
            this.label2.Location = new System.Drawing.Point(299, 0);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(55, 20);
            this.label2.TabIndex = 3;
            this.label2.Text = "Response";
            // 
            // requestListBox
            // 
            this.requestListBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.requestListBox.FormattingEnabled = true;
            this.requestListBox.Location = new System.Drawing.Point(3, 23);
            this.requestListBox.Name = "requestListBox";
            this.requestListBox.Size = new System.Drawing.Size(290, 173);
            this.requestListBox.TabIndex = 4;
            this.requestListBox.SelectedIndexChanged += new System.EventHandler(this.requestListBox_SelectedIndexChanged);
            // 
            // responseListBox
            // 
            this.responseListBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.responseListBox.FormattingEnabled = true;
            this.responseListBox.Location = new System.Drawing.Point(299, 23);
            this.responseListBox.Name = "responseListBox";
            this.responseListBox.Size = new System.Drawing.Size(290, 173);
            this.responseListBox.TabIndex = 5;
            this.responseListBox.SelectedIndexChanged += new System.EventHandler(this.responseListBox_SelectedIndexChanged);
            // 
            // transferListView
            // 
            this.transferListView.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.requestColumn,
            this.responseColumn});
            this.tableLayoutPanel1.SetColumnSpan(this.transferListView, 2);
            this.transferListView.Dock = System.Windows.Forms.DockStyle.Fill;
            this.transferListView.FullRowSelect = true;
            this.transferListView.GridLines = true;
            this.transferListView.Location = new System.Drawing.Point(3, 249);
            this.transferListView.MultiSelect = false;
            this.transferListView.Name = "transferListView";
            this.transferListView.Size = new System.Drawing.Size(586, 185);
            this.transferListView.TabIndex = 6;
            this.transferListView.UseCompatibleStateImageBehavior = false;
            this.transferListView.View = System.Windows.Forms.View.Details;
            this.transferListView.SelectedIndexChanged += new System.EventHandler(this.transferListView_SelectedIndexChanged);
            // 
            // requestColumn
            // 
            this.requestColumn.Text = "Request";
            this.requestColumn.Width = 292;
            // 
            // responseColumn
            // 
            this.responseColumn.Text = "Response";
            this.responseColumn.Width = 284;
            // 
            // ConfigTransfersForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(592, 466);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Name = "ConfigTransfersForm";
            this.Text = "ConfigTransfersForm";
            this.Load += new System.EventHandler(this.ConfigTransfersForm_Load);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.panel1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Button okButton;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button removeButton;
        private System.Windows.Forms.Button addButton;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.ListBox requestListBox;
        private System.Windows.Forms.ListBox responseListBox;
        private System.Windows.Forms.ListView transferListView;
        private System.Windows.Forms.ColumnHeader requestColumn;
        private System.Windows.Forms.ColumnHeader responseColumn;

    }
}