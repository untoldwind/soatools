namespace MsmqGatewayConfig
{
    partial class FormMsmqGatewayConfig
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
            this.tabs = new System.Windows.Forms.TabControl();
            this.tabIngoing = new System.Windows.Forms.TabPage();
            this.removeButtonIn = new System.Windows.Forms.Button();
            this.addButtonIn = new System.Windows.Forms.Button();
            this.incommingDestinations = new System.Windows.Forms.ListView();
            this.incommingQueue = new System.Windows.Forms.ColumnHeader();
            this.incomingServiceCategory = new System.Windows.Forms.ColumnHeader();
            this.incommingServiceName = new System.Windows.Forms.ColumnHeader();
            this.wsService = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.logIncoming = new System.Windows.Forms.CheckBox();
            this.label1 = new System.Windows.Forms.Label();
            this.tabOutgoing = new System.Windows.Forms.TabPage();
            this.removeButtonOut = new System.Windows.Forms.Button();
            this.addButtonOut = new System.Windows.Forms.Button();
            this.outgoingDestinations = new System.Windows.Forms.ListView();
            this.outgoingName = new System.Windows.Forms.ColumnHeader();
            this.outgoingQueue = new System.Windows.Forms.ColumnHeader();
            this.outgoingPort = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.logOutgoing = new System.Windows.Forms.CheckBox();
            this.speichern = new System.Windows.Forms.Button();
            this.schliessen = new System.Windows.Forms.Button();
            this.wsServiceList = new System.Windows.Forms.ListBox();
            this.addButtonWS = new System.Windows.Forms.Button();
            this.removeButtonWS = new System.Windows.Forms.Button();
            this.tabs.SuspendLayout();
            this.tabIngoing.SuspendLayout();
            this.tabOutgoing.SuspendLayout();
            this.SuspendLayout();
            // 
            // tabs
            // 
            this.tabs.AccessibleName = "";
            this.tabs.Controls.Add(this.tabIngoing);
            this.tabs.Controls.Add(this.tabOutgoing);
            this.tabs.Location = new System.Drawing.Point(12, 12);
            this.tabs.Name = "tabs";
            this.tabs.SelectedIndex = 0;
            this.tabs.Size = new System.Drawing.Size(545, 373);
            this.tabs.TabIndex = 0;
            this.tabs.Tag = "";
            // 
            // tabIngoing
            // 
            this.tabIngoing.Controls.Add(this.removeButtonWS);
            this.tabIngoing.Controls.Add(this.addButtonWS);
            this.tabIngoing.Controls.Add(this.wsServiceList);
            this.tabIngoing.Controls.Add(this.removeButtonIn);
            this.tabIngoing.Controls.Add(this.addButtonIn);
            this.tabIngoing.Controls.Add(this.incommingDestinations);
            this.tabIngoing.Controls.Add(this.wsService);
            this.tabIngoing.Controls.Add(this.label4);
            this.tabIngoing.Controls.Add(this.logIncoming);
            this.tabIngoing.Controls.Add(this.label1);
            this.tabIngoing.Location = new System.Drawing.Point(4, 22);
            this.tabIngoing.Name = "tabIngoing";
            this.tabIngoing.Padding = new System.Windows.Forms.Padding(3);
            this.tabIngoing.Size = new System.Drawing.Size(537, 347);
            this.tabIngoing.TabIndex = 0;
            this.tabIngoing.Text = "Ingoing";
            this.tabIngoing.UseVisualStyleBackColor = true;
            // 
            // removeButtonIn
            // 
            this.removeButtonIn.Enabled = false;
            this.removeButtonIn.Location = new System.Drawing.Point(456, 161);
            this.removeButtonIn.Name = "removeButtonIn";
            this.removeButtonIn.Size = new System.Drawing.Size(75, 23);
            this.removeButtonIn.TabIndex = 7;
            this.removeButtonIn.Text = "Remove";
            this.removeButtonIn.UseVisualStyleBackColor = true;
            this.removeButtonIn.Click += new System.EventHandler(this.removeButtonIn_Click);
            // 
            // addButtonIn
            // 
            this.addButtonIn.Location = new System.Drawing.Point(456, 132);
            this.addButtonIn.Name = "addButtonIn";
            this.addButtonIn.Size = new System.Drawing.Size(75, 23);
            this.addButtonIn.TabIndex = 6;
            this.addButtonIn.Text = "Add";
            this.addButtonIn.UseVisualStyleBackColor = true;
            this.addButtonIn.Click += new System.EventHandler(this.addButtonIn_Click);
            // 
            // incommingDestinations
            // 
            this.incommingDestinations.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.incommingQueue,
            this.incomingServiceCategory,
            this.incommingServiceName});
            this.incommingDestinations.FullRowSelect = true;
            this.incommingDestinations.GridLines = true;
            this.incommingDestinations.Location = new System.Drawing.Point(21, 132);
            this.incommingDestinations.Name = "incommingDestinations";
            this.incommingDestinations.Size = new System.Drawing.Size(429, 209);
            this.incommingDestinations.TabIndex = 5;
            this.incommingDestinations.UseCompatibleStateImageBehavior = false;
            this.incommingDestinations.View = System.Windows.Forms.View.Details;
            this.incommingDestinations.SelectedIndexChanged += new System.EventHandler(this.incommingDestinations_SelectedIndexChanged);
            // 
            // incommingQueue
            // 
            this.incommingQueue.Text = "Queue";
            this.incommingQueue.Width = 141;
            // 
            // incomingServiceCategory
            // 
            this.incomingServiceCategory.Text = "Service Category";
            this.incomingServiceCategory.Width = 137;
            // 
            // incommingServiceName
            // 
            this.incommingServiceName.Text = "Service Name";
            this.incommingServiceName.Width = 142;
            // 
            // wsService
            // 
            this.wsService.Location = new System.Drawing.Point(97, 42);
            this.wsService.Name = "wsService";
            this.wsService.Size = new System.Drawing.Size(353, 20);
            this.wsService.TabIndex = 3;
            this.wsService.TextChanged += new System.EventHandler(this.wsService_TextChanged);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(18, 47);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(67, 13);
            this.label4.TabIndex = 2;
            this.label4.Text = "WS Service:";
            // 
            // logIncoming
            // 
            this.logIncoming.AutoSize = true;
            this.logIncoming.Location = new System.Drawing.Point(97, 22);
            this.logIncoming.Name = "logIncoming";
            this.logIncoming.Size = new System.Drawing.Size(15, 14);
            this.logIncoming.TabIndex = 1;
            this.logIncoming.UseVisualStyleBackColor = true;
            this.logIncoming.CheckedChanged += new System.EventHandler(this.logIncoming_CheckedChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(16, 22);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(74, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Log Incoming:";
            // 
            // tabOutgoing
            // 
            this.tabOutgoing.Controls.Add(this.removeButtonOut);
            this.tabOutgoing.Controls.Add(this.addButtonOut);
            this.tabOutgoing.Controls.Add(this.outgoingDestinations);
            this.tabOutgoing.Controls.Add(this.outgoingPort);
            this.tabOutgoing.Controls.Add(this.label3);
            this.tabOutgoing.Controls.Add(this.label2);
            this.tabOutgoing.Controls.Add(this.logOutgoing);
            this.tabOutgoing.Location = new System.Drawing.Point(4, 22);
            this.tabOutgoing.Name = "tabOutgoing";
            this.tabOutgoing.Padding = new System.Windows.Forms.Padding(3);
            this.tabOutgoing.Size = new System.Drawing.Size(537, 347);
            this.tabOutgoing.TabIndex = 1;
            this.tabOutgoing.Text = "Outgoing";
            this.tabOutgoing.UseVisualStyleBackColor = true;
            // 
            // removeButtonOut
            // 
            this.removeButtonOut.Enabled = false;
            this.removeButtonOut.Location = new System.Drawing.Point(272, 104);
            this.removeButtonOut.Name = "removeButtonOut";
            this.removeButtonOut.Size = new System.Drawing.Size(75, 23);
            this.removeButtonOut.TabIndex = 7;
            this.removeButtonOut.Text = "Remove";
            this.removeButtonOut.UseVisualStyleBackColor = true;
            this.removeButtonOut.Click += new System.EventHandler(this.removeButtonOut_Click);
            // 
            // addButtonOut
            // 
            this.addButtonOut.Location = new System.Drawing.Point(272, 75);
            this.addButtonOut.Name = "addButtonOut";
            this.addButtonOut.Size = new System.Drawing.Size(75, 23);
            this.addButtonOut.TabIndex = 6;
            this.addButtonOut.Text = "Add";
            this.addButtonOut.UseVisualStyleBackColor = true;
            this.addButtonOut.Click += new System.EventHandler(this.addButtonOut_Click);
            // 
            // outgoingDestinations
            // 
            this.outgoingDestinations.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.outgoingName,
            this.outgoingQueue});
            this.outgoingDestinations.FullRowSelect = true;
            this.outgoingDestinations.GridLines = true;
            this.outgoingDestinations.Location = new System.Drawing.Point(21, 75);
            this.outgoingDestinations.Name = "outgoingDestinations";
            this.outgoingDestinations.Size = new System.Drawing.Size(244, 266);
            this.outgoingDestinations.TabIndex = 5;
            this.outgoingDestinations.UseCompatibleStateImageBehavior = false;
            this.outgoingDestinations.View = System.Windows.Forms.View.Details;
            this.outgoingDestinations.SelectedIndexChanged += new System.EventHandler(this.outgoingDestinations_SelectedIndexChanged);
            // 
            // outgoingName
            // 
            this.outgoingName.Text = "Name";
            this.outgoingName.Width = 120;
            // 
            // outgoingQueue
            // 
            this.outgoingQueue.Text = "Queue";
            this.outgoingQueue.Width = 120;
            // 
            // outgoingPort
            // 
            this.outgoingPort.Location = new System.Drawing.Point(53, 49);
            this.outgoingPort.Name = "outgoingPort";
            this.outgoingPort.Size = new System.Drawing.Size(60, 20);
            this.outgoingPort.TabIndex = 3;
            this.outgoingPort.TextChanged += new System.EventHandler(this.outgoingPort_TextChanged);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(18, 52);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(29, 13);
            this.label3.TabIndex = 2;
            this.label3.Text = "Port:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(18, 24);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(74, 13);
            this.label2.TabIndex = 1;
            this.label2.Text = "Log Outgoing:";
            // 
            // logOutgoing
            // 
            this.logOutgoing.AutoSize = true;
            this.logOutgoing.Location = new System.Drawing.Point(98, 24);
            this.logOutgoing.Name = "logOutgoing";
            this.logOutgoing.Size = new System.Drawing.Size(15, 14);
            this.logOutgoing.TabIndex = 0;
            this.logOutgoing.UseVisualStyleBackColor = true;
            this.logOutgoing.CheckedChanged += new System.EventHandler(this.logOutgoing_CheckedChanged);
            // 
            // speichern
            // 
            this.speichern.Location = new System.Drawing.Point(397, 391);
            this.speichern.Name = "speichern";
            this.speichern.Size = new System.Drawing.Size(75, 23);
            this.speichern.TabIndex = 1;
            this.speichern.Text = "Speichern";
            this.speichern.UseVisualStyleBackColor = true;
            this.speichern.Click += new System.EventHandler(this.speichern_Click);
            // 
            // schliessen
            // 
            this.schliessen.Location = new System.Drawing.Point(478, 391);
            this.schliessen.Name = "schliessen";
            this.schliessen.Size = new System.Drawing.Size(75, 23);
            this.schliessen.TabIndex = 2;
            this.schliessen.Text = "Schließen";
            this.schliessen.UseVisualStyleBackColor = true;
            this.schliessen.Click += new System.EventHandler(this.schliessen_Click);
            // 
            // wsServiceList
            // 
            this.wsServiceList.FormattingEnabled = true;
            this.wsServiceList.Location = new System.Drawing.Point(97, 68);
            this.wsServiceList.Name = "wsServiceList";
            this.wsServiceList.Size = new System.Drawing.Size(353, 56);
            this.wsServiceList.TabIndex = 8;
            this.wsServiceList.SelectedIndexChanged += new System.EventHandler(this.wsServiceList_SelectedIndexChanged);
            // 
            // addButtonWS
            // 
            this.addButtonWS.Location = new System.Drawing.Point(456, 42);
            this.addButtonWS.Name = "addButtonWS";
            this.addButtonWS.Size = new System.Drawing.Size(75, 23);
            this.addButtonWS.TabIndex = 9;
            this.addButtonWS.Text = "Add";
            this.addButtonWS.UseVisualStyleBackColor = true;
            this.addButtonWS.Click += new System.EventHandler(this.addButtonWS_Click);
            // 
            // removeButtonWS
            // 
            this.removeButtonWS.Enabled = false;
            this.removeButtonWS.Location = new System.Drawing.Point(456, 71);
            this.removeButtonWS.Name = "removeButtonWS";
            this.removeButtonWS.Size = new System.Drawing.Size(75, 23);
            this.removeButtonWS.TabIndex = 10;
            this.removeButtonWS.Text = "Remove";
            this.removeButtonWS.UseVisualStyleBackColor = true;
            this.removeButtonWS.Click += new System.EventHandler(this.removeButtonWS_Click);
            // 
            // FormMsmqGatewayConfig
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(567, 424);
            this.Controls.Add(this.schliessen);
            this.Controls.Add(this.speichern);
            this.Controls.Add(this.tabs);
            this.Name = "FormMsmqGatewayConfig";
            this.Text = "MsmqGatewayConfig";
            this.Load += new System.EventHandler(this.FormMsmqGatewayConfig_Load);
            this.tabs.ResumeLayout(false);
            this.tabIngoing.ResumeLayout(false);
            this.tabIngoing.PerformLayout();
            this.tabOutgoing.ResumeLayout(false);
            this.tabOutgoing.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tabs;
        private System.Windows.Forms.TabPage tabIngoing;
        private System.Windows.Forms.TabPage tabOutgoing;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.CheckBox logIncoming;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox outgoingPort;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.CheckBox logOutgoing;
        private System.Windows.Forms.TextBox wsService;
        private System.Windows.Forms.ListView outgoingDestinations;
        private System.Windows.Forms.ColumnHeader outgoingName;
        private System.Windows.Forms.ColumnHeader outgoingQueue;
        private System.Windows.Forms.Button speichern;
        private System.Windows.Forms.Button schliessen;
        private System.Windows.Forms.ListView incommingDestinations;
        private System.Windows.Forms.ColumnHeader incommingQueue;
        private System.Windows.Forms.ColumnHeader incomingServiceCategory;
        private System.Windows.Forms.ColumnHeader incommingServiceName;
        private System.Windows.Forms.Button removeButtonIn;
        private System.Windows.Forms.Button addButtonIn;
        private System.Windows.Forms.Button removeButtonOut;
        private System.Windows.Forms.Button addButtonOut;
        private System.Windows.Forms.Button removeButtonWS;
        private System.Windows.Forms.Button addButtonWS;
        private System.Windows.Forms.ListBox wsServiceList;
    }
}

