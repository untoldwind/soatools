namespace MessageListenerConsole
{
    partial class MessageMediator
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
            this.body = new System.Windows.Forms.RichTextBox();
            this.bodyLabel = new System.Windows.Forms.Label();
            this.labelLabel = new System.Windows.Forms.Label();
            this.label = new System.Windows.Forms.TextBox();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.messageList = new System.Windows.Forms.ListBox();
            this.testbutton = new System.Windows.Forms.Button();
            this.groupBox2.SuspendLayout();
            this.SuspendLayout();
            // 
            // body
            // 
            this.body.Location = new System.Drawing.Point(6, 135);
            this.body.Name = "body";
            this.body.Size = new System.Drawing.Size(239, 211);
            this.body.TabIndex = 0;
            this.body.Text = "";
            // 
            // bodyLabel
            // 
            this.bodyLabel.AutoSize = true;
            this.bodyLabel.Location = new System.Drawing.Point(6, 119);
            this.bodyLabel.Name = "bodyLabel";
            this.bodyLabel.Size = new System.Drawing.Size(78, 13);
            this.bodyLabel.TabIndex = 1;
            this.bodyLabel.Text = "Beschreibung: ";
            // 
            // labelLabel
            // 
            this.labelLabel.AutoSize = true;
            this.labelLabel.Location = new System.Drawing.Point(6, 42);
            this.labelLabel.Name = "labelLabel";
            this.labelLabel.Size = new System.Drawing.Size(38, 13);
            this.labelLabel.TabIndex = 2;
            this.labelLabel.Text = "Name:";
            // 
            // label
            // 
            this.label.Location = new System.Drawing.Point(6, 58);
            this.label.Name = "label";
            this.label.Size = new System.Drawing.Size(200, 20);
            this.label.TabIndex = 3;
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.label);
            this.groupBox2.Controls.Add(this.bodyLabel);
            this.groupBox2.Controls.Add(this.labelLabel);
            this.groupBox2.Controls.Add(this.body);
            this.groupBox2.Location = new System.Drawing.Point(322, 30);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(251, 352);
            this.groupBox2.TabIndex = 6;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "selected Message";
            // 
            // messageList
            // 
            this.messageList.FormattingEnabled = true;
            this.messageList.Location = new System.Drawing.Point(24, 34);
            this.messageList.Name = "messageList";
            this.messageList.Size = new System.Drawing.Size(196, 342);
            this.messageList.TabIndex = 7;
            this.messageList.SelectedValueChanged += new System.EventHandler(this.messageList_SelectedValueChanged);
            // 
            // testbutton
            // 
            this.testbutton.Location = new System.Drawing.Point(232, 422);
            this.testbutton.Name = "testbutton";
            this.testbutton.Size = new System.Drawing.Size(79, 25);
            this.testbutton.TabIndex = 8;
            this.testbutton.Text = "Testbutton";
            this.testbutton.UseVisualStyleBackColor = true;
            this.testbutton.Click += new System.EventHandler(this.testbutton_Click);
            // 
            // MessageMediator
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(596, 501);
            this.Controls.Add(this.testbutton);
            this.Controls.Add(this.messageList);
            this.Controls.Add(this.groupBox2);
            this.Name = "MessageMediator";
            this.Text = "MessageMediator";
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.RichTextBox body;
        private System.Windows.Forms.Label bodyLabel;
        private System.Windows.Forms.Label labelLabel;
        private System.Windows.Forms.TextBox label;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.ListBox messageList;
        private System.Windows.Forms.Button testbutton;
    }
}