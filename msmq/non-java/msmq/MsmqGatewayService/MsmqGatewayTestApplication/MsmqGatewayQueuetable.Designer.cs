namespace MsmqGatewayTestApplication
{
    partial class MsmqGatewayQueuetable
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
            this.btnClose = new System.Windows.Forms.Button();
            this.grpBDetails = new System.Windows.Forms.GroupBox();
            this.rtxtBody = new System.Windows.Forms.RichTextBox();
            this.txtSize = new System.Windows.Forms.TextBox();
            this.txtLabel = new System.Windows.Forms.TextBox();
            this.lblBody = new System.Windows.Forms.Label();
            this.lblGroesse = new System.Windows.Forms.Label();
            this.lblLabel = new System.Windows.Forms.Label();
            this.grpBQueues = new System.Windows.Forms.GroupBox();
            this.tblQueues = new System.Windows.Forms.ListView();
            this.chQueue = new System.Windows.Forms.ColumnHeader();
            this.chNumber = new System.Windows.Forms.ColumnHeader();
            this.lblNumber = new System.Windows.Forms.Label();
            this.txtNumber = new System.Windows.Forms.TextBox();
            this.grpBDetails.SuspendLayout();
            this.grpBQueues.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnClose
            // 
            this.btnClose.Location = new System.Drawing.Point(563, 414);
            this.btnClose.Name = "btnClose";
            this.btnClose.Size = new System.Drawing.Size(75, 23);
            this.btnClose.TabIndex = 0;
            this.btnClose.Text = "Close";
            this.btnClose.UseVisualStyleBackColor = true;
            this.btnClose.Click += new System.EventHandler(this.btnClose_Click);
            // 
            // grpBDetails
            // 
            this.grpBDetails.Controls.Add(this.txtNumber);
            this.grpBDetails.Controls.Add(this.lblNumber);
            this.grpBDetails.Controls.Add(this.rtxtBody);
            this.grpBDetails.Controls.Add(this.txtSize);
            this.grpBDetails.Controls.Add(this.txtLabel);
            this.grpBDetails.Controls.Add(this.lblBody);
            this.grpBDetails.Controls.Add(this.lblGroesse);
            this.grpBDetails.Controls.Add(this.lblLabel);
            this.grpBDetails.Location = new System.Drawing.Point(217, 12);
            this.grpBDetails.Name = "grpBDetails";
            this.grpBDetails.Size = new System.Drawing.Size(421, 396);
            this.grpBDetails.TabIndex = 1;
            this.grpBDetails.TabStop = false;
            this.grpBDetails.Text = "Details";
            // 
            // rtxtBody
            // 
            this.rtxtBody.Location = new System.Drawing.Point(17, 92);
            this.rtxtBody.Name = "rtxtBody";
            this.rtxtBody.Size = new System.Drawing.Size(398, 298);
            this.rtxtBody.TabIndex = 5;
            this.rtxtBody.Text = "";
            // 
            // txtSize
            // 
            this.txtSize.Location = new System.Drawing.Point(56, 50);
            this.txtSize.Name = "txtSize";
            this.txtSize.Size = new System.Drawing.Size(98, 20);
            this.txtSize.TabIndex = 4;
            // 
            // txtLabel
            // 
            this.txtLabel.Location = new System.Drawing.Point(153, 24);
            this.txtLabel.Name = "txtLabel";
            this.txtLabel.Size = new System.Drawing.Size(193, 20);
            this.txtLabel.TabIndex = 3;
            // 
            // lblBody
            // 
            this.lblBody.AutoSize = true;
            this.lblBody.Location = new System.Drawing.Point(14, 76);
            this.lblBody.Name = "lblBody";
            this.lblBody.Size = new System.Drawing.Size(34, 13);
            this.lblBody.TabIndex = 2;
            this.lblBody.Text = "Body:";
            // 
            // lblGroesse
            // 
            this.lblGroesse.AutoSize = true;
            this.lblGroesse.Location = new System.Drawing.Point(14, 51);
            this.lblGroesse.Name = "lblGroesse";
            this.lblGroesse.Size = new System.Drawing.Size(30, 13);
            this.lblGroesse.TabIndex = 1;
            this.lblGroesse.Text = "Size:";
            // 
            // lblLabel
            // 
            this.lblLabel.AutoSize = true;
            this.lblLabel.Location = new System.Drawing.Point(111, 27);
            this.lblLabel.Name = "lblLabel";
            this.lblLabel.Size = new System.Drawing.Size(36, 13);
            this.lblLabel.TabIndex = 0;
            this.lblLabel.Text = "Label:";
            // 
            // grpBQueues
            // 
            this.grpBQueues.Controls.Add(this.tblQueues);
            this.grpBQueues.Location = new System.Drawing.Point(12, 12);
            this.grpBQueues.Name = "grpBQueues";
            this.grpBQueues.Size = new System.Drawing.Size(199, 396);
            this.grpBQueues.TabIndex = 2;
            this.grpBQueues.TabStop = false;
            this.grpBQueues.Text = "Queues";
            // 
            // tblQueues
            // 
            this.tblQueues.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.chNumber,
            this.chQueue});
            this.tblQueues.FullRowSelect = true;
            this.tblQueues.GridLines = true;
            this.tblQueues.Location = new System.Drawing.Point(6, 19);
            this.tblQueues.Name = "tblQueues";
            this.tblQueues.Size = new System.Drawing.Size(187, 371);
            this.tblQueues.TabIndex = 0;
            this.tblQueues.UseCompatibleStateImageBehavior = false;
            this.tblQueues.View = System.Windows.Forms.View.Details;
            this.tblQueues.MouseClick += new System.Windows.Forms.MouseEventHandler(this.tblQueues_MouseClick);
            // 
            // chQueue
            // 
            this.chQueue.Text = "Queue";
            this.chQueue.Width = 153;
            // 
            // chNumber
            // 
            this.chNumber.Text = "Nr.";
            this.chNumber.Width = 30;
            // 
            // lblNumber
            // 
            this.lblNumber.AutoSize = true;
            this.lblNumber.Location = new System.Drawing.Point(13, 27);
            this.lblNumber.Name = "lblNumber";
            this.lblNumber.Size = new System.Drawing.Size(24, 13);
            this.lblNumber.TabIndex = 6;
            this.lblNumber.Text = "Nr.:";
            // 
            // txtNumber
            // 
            this.txtNumber.Location = new System.Drawing.Point(56, 24);
            this.txtNumber.Name = "txtNumber";
            this.txtNumber.Size = new System.Drawing.Size(33, 20);
            this.txtNumber.TabIndex = 7;
            // 
            // MsmqGatewayQueuetable
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(650, 449);
            this.Controls.Add(this.grpBQueues);
            this.Controls.Add(this.grpBDetails);
            this.Controls.Add(this.btnClose);
            this.Name = "MsmqGatewayQueuetable";
            this.Text = "MsmqGatewayQueuetable";
            this.grpBDetails.ResumeLayout(false);
            this.grpBDetails.PerformLayout();
            this.grpBQueues.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnClose;
        private System.Windows.Forms.GroupBox grpBDetails;
        private System.Windows.Forms.Label lblBody;
        private System.Windows.Forms.Label lblGroesse;
        private System.Windows.Forms.Label lblLabel;
        private System.Windows.Forms.RichTextBox rtxtBody;
        private System.Windows.Forms.TextBox txtSize;
        private System.Windows.Forms.TextBox txtLabel;
        private System.Windows.Forms.GroupBox grpBQueues;
        private System.Windows.Forms.ListView tblQueues;
        private System.Windows.Forms.ColumnHeader chQueue;
        private System.Windows.Forms.ColumnHeader chNumber;
        private System.Windows.Forms.TextBox txtNumber;
        private System.Windows.Forms.Label lblNumber;
    }
}