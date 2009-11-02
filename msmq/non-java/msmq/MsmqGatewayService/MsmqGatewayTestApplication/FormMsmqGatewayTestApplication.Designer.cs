namespace MsmqGatewayTestApplication
{
    partial class MsmqGatewayTestApplication
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.txtQueue = new System.Windows.Forms.TextBox();
            this.lblQueue = new System.Windows.Forms.Label();
            this.btnSend = new System.Windows.Forms.Button();
            this.lblMessageLabel = new System.Windows.Forms.Label();
            this.txtLabel = new System.Windows.Forms.TextBox();
            this.lblQueueFound = new System.Windows.Forms.Label();
            this.rtxtBody = new System.Windows.Forms.RichTextBox();
            this.lblBody = new System.Windows.Forms.Label();
            this.btnClose = new System.Windows.Forms.Button();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.btnSimulate = new System.Windows.Forms.Button();
            this.btnSendFile = new System.Windows.Forms.Button();
            this.coboPrioritaet = new System.Windows.Forms.ComboBox();
            this.lblPrioritaet = new System.Windows.Forms.Label();
            this.splitContainer2 = new System.Windows.Forms.SplitContainer();
            this.listViewReceivedMessages = new System.Windows.Forms.ListView();
            this.columnHeader1 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader2 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader3 = new System.Windows.Forms.ColumnHeader();
            this.columnHeader4 = new System.Windows.Forms.ColumnHeader();
            this.lblReceiveQueueFound = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.txtReceiveQueue = new System.Windows.Forms.TextBox();
            this.tabControlReceivedMessage = new System.Windows.Forms.TabControl();
            this.tabPageReceivedMessageByte = new System.Windows.Forms.TabPage();
            this.txtReceivedMessageBytes = new System.Windows.Forms.TextBox();
            this.tabPageReceivedMessageXml = new System.Windows.Forms.TabPage();
            this.txtReceiveMessageXml = new System.Windows.Forms.TextBox();
            this.backgroundWorker = new System.ComponentModel.BackgroundWorker();
            this.label2 = new System.Windows.Forms.Label();
            this.countTextBox = new System.Windows.Forms.TextBox();
            this.btnClear = new System.Windows.Forms.Button();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            this.splitContainer2.Panel1.SuspendLayout();
            this.splitContainer2.Panel2.SuspendLayout();
            this.splitContainer2.SuspendLayout();
            this.tabControlReceivedMessage.SuspendLayout();
            this.tabPageReceivedMessageByte.SuspendLayout();
            this.tabPageReceivedMessageXml.SuspendLayout();
            this.SuspendLayout();
            // 
            // txtQueue
            // 
            this.txtQueue.Location = new System.Drawing.Point(100, 9);
            this.txtQueue.Name = "txtQueue";
            this.txtQueue.Size = new System.Drawing.Size(171, 20);
            this.txtQueue.TabIndex = 0;
            this.txtQueue.TextChanged += new System.EventHandler(this.txtQueue_TextChanged);
            this.txtQueue.MouseClick += new System.Windows.Forms.MouseEventHandler(this.txtQueue_MouseClick);
            this.txtQueue.Enter += new System.EventHandler(this.txtQueue_Enter);
            // 
            // lblQueue
            // 
            this.lblQueue.AutoSize = true;
            this.lblQueue.Location = new System.Drawing.Point(12, 12);
            this.lblQueue.Name = "lblQueue";
            this.lblQueue.Size = new System.Drawing.Size(42, 13);
            this.lblQueue.TabIndex = 7;
            this.lblQueue.Text = "Queue:";
            // 
            // btnSend
            // 
            this.btnSend.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.btnSend.Location = new System.Drawing.Point(11, 507);
            this.btnSend.Name = "btnSend";
            this.btnSend.Size = new System.Drawing.Size(83, 23);
            this.btnSend.TabIndex = 4;
            this.btnSend.Text = "send";
            this.btnSend.UseVisualStyleBackColor = true;
            this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
            // 
            // lblMessageLabel
            // 
            this.lblMessageLabel.AutoSize = true;
            this.lblMessageLabel.Location = new System.Drawing.Point(12, 38);
            this.lblMessageLabel.Name = "lblMessageLabel";
            this.lblMessageLabel.Size = new System.Drawing.Size(82, 13);
            this.lblMessageLabel.TabIndex = 7;
            this.lblMessageLabel.Text = "Message Label:";
            // 
            // txtLabel
            // 
            this.txtLabel.Location = new System.Drawing.Point(100, 35);
            this.txtLabel.Name = "txtLabel";
            this.txtLabel.Size = new System.Drawing.Size(171, 20);
            this.txtLabel.TabIndex = 2;
            // 
            // lblQueueFound
            // 
            this.lblQueueFound.AutoSize = true;
            this.lblQueueFound.ForeColor = System.Drawing.Color.Red;
            this.lblQueueFound.Location = new System.Drawing.Point(277, 12);
            this.lblQueueFound.Name = "lblQueueFound";
            this.lblQueueFound.Size = new System.Drawing.Size(85, 13);
            this.lblQueueFound.TabIndex = 7;
            this.lblQueueFound.Text = "queue not found";
            this.lblQueueFound.Visible = false;
            // 
            // rtxtBody
            // 
            this.rtxtBody.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.rtxtBody.Location = new System.Drawing.Point(15, 119);
            this.rtxtBody.Name = "rtxtBody";
            this.rtxtBody.Size = new System.Drawing.Size(416, 382);
            this.rtxtBody.TabIndex = 3;
            this.rtxtBody.Text = "";
            // 
            // lblBody
            // 
            this.lblBody.AutoSize = true;
            this.lblBody.Location = new System.Drawing.Point(12, 103);
            this.lblBody.Name = "lblBody";
            this.lblBody.Size = new System.Drawing.Size(34, 13);
            this.lblBody.TabIndex = 9;
            this.lblBody.Text = "Body:";
            // 
            // btnClose
            // 
            this.btnClose.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.btnClose.Location = new System.Drawing.Point(356, 507);
            this.btnClose.Name = "btnClose";
            this.btnClose.Size = new System.Drawing.Size(75, 23);
            this.btnClose.TabIndex = 10;
            this.btnClose.Text = "Close";
            this.btnClose.UseVisualStyleBackColor = true;
            this.btnClose.Click += new System.EventHandler(this.btnClose_Click);
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.btnSimulate);
            this.splitContainer1.Panel1.Controls.Add(this.btnSendFile);
            this.splitContainer1.Panel1.Controls.Add(this.coboPrioritaet);
            this.splitContainer1.Panel1.Controls.Add(this.lblPrioritaet);
            this.splitContainer1.Panel1.Controls.Add(this.rtxtBody);
            this.splitContainer1.Panel1.Controls.Add(this.lblQueue);
            this.splitContainer1.Panel1.Controls.Add(this.lblQueueFound);
            this.splitContainer1.Panel1.Controls.Add(this.txtQueue);
            this.splitContainer1.Panel1.Controls.Add(this.lblBody);
            this.splitContainer1.Panel1.Controls.Add(this.txtLabel);
            this.splitContainer1.Panel1.Controls.Add(this.btnClose);
            this.splitContainer1.Panel1.Controls.Add(this.lblMessageLabel);
            this.splitContainer1.Panel1.Controls.Add(this.btnSend);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.splitContainer2);
            this.splitContainer1.Size = new System.Drawing.Size(904, 542);
            this.splitContainer1.SplitterDistance = 434;
            this.splitContainer1.TabIndex = 11;
            // 
            // btnSimulate
            // 
            this.btnSimulate.Location = new System.Drawing.Point(181, 507);
            this.btnSimulate.Name = "btnSimulate";
            this.btnSimulate.Size = new System.Drawing.Size(75, 23);
            this.btnSimulate.TabIndex = 14;
            this.btnSimulate.Text = "Simulate";
            this.btnSimulate.UseVisualStyleBackColor = true;
            this.btnSimulate.Click += new System.EventHandler(this.btnSimulate_Click);
            // 
            // btnSendFile
            // 
            this.btnSendFile.Location = new System.Drawing.Point(100, 507);
            this.btnSendFile.Name = "btnSendFile";
            this.btnSendFile.Size = new System.Drawing.Size(75, 23);
            this.btnSendFile.TabIndex = 13;
            this.btnSendFile.Text = "send file";
            this.btnSendFile.UseVisualStyleBackColor = true;
            this.btnSendFile.Click += new System.EventHandler(this.btnSendFile_Click);
            // 
            // coboPrioritaet
            // 
            this.coboPrioritaet.FormattingEnabled = true;
            this.coboPrioritaet.Items.AddRange(new object[] {
            "Lowest (0)",
            "VeryLow (1)",
            "Low (2)",
            "Normal (3)",
            "AboveNormal (4)",
            "High (5)",
            "VeryHigh (6)",
            "Highest (7)"});
            this.coboPrioritaet.Location = new System.Drawing.Point(100, 64);
            this.coboPrioritaet.Name = "coboPrioritaet";
            this.coboPrioritaet.Size = new System.Drawing.Size(118, 21);
            this.coboPrioritaet.TabIndex = 12;
            // 
            // lblPrioritaet
            // 
            this.lblPrioritaet.AutoSize = true;
            this.lblPrioritaet.Location = new System.Drawing.Point(12, 67);
            this.lblPrioritaet.Name = "lblPrioritaet";
            this.lblPrioritaet.Size = new System.Drawing.Size(45, 13);
            this.lblPrioritaet.TabIndex = 11;
            this.lblPrioritaet.Text = "Priorität:";
            // 
            // splitContainer2
            // 
            this.splitContainer2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer2.Location = new System.Drawing.Point(0, 0);
            this.splitContainer2.Name = "splitContainer2";
            this.splitContainer2.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer2.Panel1
            // 
            this.splitContainer2.Panel1.Controls.Add(this.btnClear);
            this.splitContainer2.Panel1.Controls.Add(this.countTextBox);
            this.splitContainer2.Panel1.Controls.Add(this.label2);
            this.splitContainer2.Panel1.Controls.Add(this.listViewReceivedMessages);
            this.splitContainer2.Panel1.Controls.Add(this.lblReceiveQueueFound);
            this.splitContainer2.Panel1.Controls.Add(this.label1);
            this.splitContainer2.Panel1.Controls.Add(this.txtReceiveQueue);
            // 
            // splitContainer2.Panel2
            // 
            this.splitContainer2.Panel2.Controls.Add(this.tabControlReceivedMessage);
            this.splitContainer2.Size = new System.Drawing.Size(466, 542);
            this.splitContainer2.SplitterDistance = 271;
            this.splitContainer2.TabIndex = 5;
            // 
            // listViewReceivedMessages
            // 
            this.listViewReceivedMessages.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.listViewReceivedMessages.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2,
            this.columnHeader3,
            this.columnHeader4});
            this.listViewReceivedMessages.FullRowSelect = true;
            this.listViewReceivedMessages.Location = new System.Drawing.Point(6, 64);
            this.listViewReceivedMessages.Name = "listViewReceivedMessages";
            this.listViewReceivedMessages.Size = new System.Drawing.Size(448, 204);
            this.listViewReceivedMessages.TabIndex = 3;
            this.listViewReceivedMessages.UseCompatibleStateImageBehavior = false;
            this.listViewReceivedMessages.View = System.Windows.Forms.View.Details;
            this.listViewReceivedMessages.SelectedIndexChanged += new System.EventHandler(this.listViewReceivedMessages_SelectedIndexChanged);
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "Id";
            // 
            // columnHeader2
            // 
            this.columnHeader2.Text = "Label";
            this.columnHeader2.Width = 178;
            // 
            // columnHeader3
            // 
            this.columnHeader3.Text = "Priority";
            // 
            // columnHeader4
            // 
            this.columnHeader4.Text = "Time";
            this.columnHeader4.Width = 135;
            // 
            // lblReceiveQueueFound
            // 
            this.lblReceiveQueueFound.AutoSize = true;
            this.lblReceiveQueueFound.ForeColor = System.Drawing.Color.Red;
            this.lblReceiveQueueFound.Location = new System.Drawing.Point(240, 19);
            this.lblReceiveQueueFound.Name = "lblReceiveQueueFound";
            this.lblReceiveQueueFound.Size = new System.Drawing.Size(85, 13);
            this.lblReceiveQueueFound.TabIndex = 2;
            this.lblReceiveQueueFound.Text = "queue not found";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(3, 19);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(42, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Queue:";
            // 
            // txtReceiveQueue
            // 
            this.txtReceiveQueue.Location = new System.Drawing.Point(45, 16);
            this.txtReceiveQueue.Name = "txtReceiveQueue";
            this.txtReceiveQueue.Size = new System.Drawing.Size(189, 20);
            this.txtReceiveQueue.TabIndex = 1;
            this.txtReceiveQueue.TextChanged += new System.EventHandler(this.txtReceiveQueue_TextChanged);
            // 
            // tabControlReceivedMessage
            // 
            this.tabControlReceivedMessage.Controls.Add(this.tabPageReceivedMessageByte);
            this.tabControlReceivedMessage.Controls.Add(this.tabPageReceivedMessageXml);
            this.tabControlReceivedMessage.Location = new System.Drawing.Point(6, 3);
            this.tabControlReceivedMessage.Name = "tabControlReceivedMessage";
            this.tabControlReceivedMessage.SelectedIndex = 0;
            this.tabControlReceivedMessage.Size = new System.Drawing.Size(448, 252);
            this.tabControlReceivedMessage.TabIndex = 4;
            // 
            // tabPageReceivedMessageByte
            // 
            this.tabPageReceivedMessageByte.Controls.Add(this.txtReceivedMessageBytes);
            this.tabPageReceivedMessageByte.Location = new System.Drawing.Point(4, 22);
            this.tabPageReceivedMessageByte.Name = "tabPageReceivedMessageByte";
            this.tabPageReceivedMessageByte.Padding = new System.Windows.Forms.Padding(3);
            this.tabPageReceivedMessageByte.Size = new System.Drawing.Size(440, 226);
            this.tabPageReceivedMessageByte.TabIndex = 0;
            this.tabPageReceivedMessageByte.Text = "Bytes";
            this.tabPageReceivedMessageByte.UseVisualStyleBackColor = true;
            // 
            // txtReceivedMessageBytes
            // 
            this.txtReceivedMessageBytes.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtReceivedMessageBytes.Location = new System.Drawing.Point(3, 3);
            this.txtReceivedMessageBytes.Multiline = true;
            this.txtReceivedMessageBytes.Name = "txtReceivedMessageBytes";
            this.txtReceivedMessageBytes.Size = new System.Drawing.Size(434, 220);
            this.txtReceivedMessageBytes.TabIndex = 0;
            // 
            // tabPageReceivedMessageXml
            // 
            this.tabPageReceivedMessageXml.Controls.Add(this.txtReceiveMessageXml);
            this.tabPageReceivedMessageXml.Location = new System.Drawing.Point(4, 22);
            this.tabPageReceivedMessageXml.Name = "tabPageReceivedMessageXml";
            this.tabPageReceivedMessageXml.Padding = new System.Windows.Forms.Padding(3);
            this.tabPageReceivedMessageXml.Size = new System.Drawing.Size(440, 226);
            this.tabPageReceivedMessageXml.TabIndex = 1;
            this.tabPageReceivedMessageXml.Text = "Xml";
            this.tabPageReceivedMessageXml.UseVisualStyleBackColor = true;
            // 
            // txtReceiveMessageXml
            // 
            this.txtReceiveMessageXml.Dock = System.Windows.Forms.DockStyle.Fill;
            this.txtReceiveMessageXml.Location = new System.Drawing.Point(3, 3);
            this.txtReceiveMessageXml.Multiline = true;
            this.txtReceiveMessageXml.Name = "txtReceiveMessageXml";
            this.txtReceiveMessageXml.Size = new System.Drawing.Size(434, 220);
            this.txtReceiveMessageXml.TabIndex = 0;
            // 
            // backgroundWorker
            // 
            this.backgroundWorker.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorker_DoWork);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(3, 42);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(35, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Count";
            // 
            // countTextBox
            // 
            this.countTextBox.Location = new System.Drawing.Point(45, 39);
            this.countTextBox.Name = "countTextBox";
            this.countTextBox.ReadOnly = true;
            this.countTextBox.Size = new System.Drawing.Size(75, 20);
            this.countTextBox.TabIndex = 5;
            // 
            // btnClear
            // 
            this.btnClear.Location = new System.Drawing.Point(372, 37);
            this.btnClear.Name = "btnClear";
            this.btnClear.Size = new System.Drawing.Size(75, 23);
            this.btnClear.TabIndex = 6;
            this.btnClear.Text = "Clear";
            this.btnClear.UseVisualStyleBackColor = true;
            this.btnClear.Click += new System.EventHandler(this.btnClear_Click);
            // 
            // MsmqGatewayTestApplication
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(904, 542);
            this.Controls.Add(this.splitContainer1);
            this.Name = "MsmqGatewayTestApplication";
            this.Text = "MsmqGatewayTestApplication";
            this.Load += new System.EventHandler(this.MsmqGatewayTestApplication_Load);
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel1.PerformLayout();
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.ResumeLayout(false);
            this.splitContainer2.Panel1.ResumeLayout(false);
            this.splitContainer2.Panel1.PerformLayout();
            this.splitContainer2.Panel2.ResumeLayout(false);
            this.splitContainer2.ResumeLayout(false);
            this.tabControlReceivedMessage.ResumeLayout(false);
            this.tabPageReceivedMessageByte.ResumeLayout(false);
            this.tabPageReceivedMessageByte.PerformLayout();
            this.tabPageReceivedMessageXml.ResumeLayout(false);
            this.tabPageReceivedMessageXml.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox txtQueue;
        private System.Windows.Forms.Label lblQueue;
        private System.Windows.Forms.Button btnSend;
        private System.Windows.Forms.Label lblMessageLabel;
        private System.Windows.Forms.TextBox txtLabel;
        private System.Windows.Forms.Label lblQueueFound;
        private System.Windows.Forms.RichTextBox rtxtBody;
        private System.Windows.Forms.Label lblBody;
        private System.Windows.Forms.Button btnClose;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.Label lblReceiveQueueFound;
        private System.Windows.Forms.TextBox txtReceiveQueue;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ListView listViewReceivedMessages;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private System.Windows.Forms.ColumnHeader columnHeader3;
        private System.Windows.Forms.ColumnHeader columnHeader4;
        private System.ComponentModel.BackgroundWorker backgroundWorker;
        private System.Windows.Forms.SplitContainer splitContainer2;
        private System.Windows.Forms.TabControl tabControlReceivedMessage;
        private System.Windows.Forms.TabPage tabPageReceivedMessageByte;
        private System.Windows.Forms.TabPage tabPageReceivedMessageXml;
        private System.Windows.Forms.TextBox txtReceivedMessageBytes;
        private System.Windows.Forms.TextBox txtReceiveMessageXml;
        private System.Windows.Forms.ComboBox coboPrioritaet;
        private System.Windows.Forms.Label lblPrioritaet;
        private System.Windows.Forms.Button btnSendFile;
        private System.Windows.Forms.Button btnSimulate;
        private System.Windows.Forms.Button btnClear;
        private System.Windows.Forms.TextBox countTextBox;
        private System.Windows.Forms.Label label2;
    }
}

