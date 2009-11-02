namespace MsmqTestLoopback
{
    partial class MainForm
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
            this.configQueuesButton = new System.Windows.Forms.Button();
            this.configTransfersButton = new System.Windows.Forms.Button();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.panel1 = new System.Windows.Forms.Panel();
            this.panel2 = new System.Windows.Forms.Panel();
            this.clearButton = new System.Windows.Forms.Button();
            this.closeButton = new System.Windows.Forms.Button();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.idDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.queueNameDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.arrivedDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.labelDataGridViewTextBoxColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.messagesBindingSource = new System.Windows.Forms.BindingSource(this.components);
            this.messageDataSet = new MsmqTestLoopback.MessageDataSet();
            this.bodyTabControl = new System.Windows.Forms.TabControl();
            this.bodyHexTabPage = new System.Windows.Forms.TabPage();
            this.bodyHexTextBox = new System.Windows.Forms.TextBox();
            this.bodyXmlTabPage = new System.Windows.Forms.TabPage();
            this.bodyXmlTextBox = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.arrivedTextBox = new System.Windows.Forms.TextBox();
            this.labelTextBox = new System.Windows.Forms.TextBox();
            this.queueTextBox = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.correlationIdTextBox = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.idTextBox = new System.Windows.Forms.TextBox();
            this.MessageBindingNavigator = new System.Windows.Forms.BindingNavigator(this.components);
            this.bindingNavigatorCountItem = new System.Windows.Forms.ToolStripLabel();
            this.bindingNavigatorMoveFirstItem = new System.Windows.Forms.ToolStripButton();
            this.bindingNavigatorMovePreviousItem = new System.Windows.Forms.ToolStripButton();
            this.bindingNavigatorSeparator = new System.Windows.Forms.ToolStripSeparator();
            this.bindingNavigatorPositionItem = new System.Windows.Forms.ToolStripTextBox();
            this.bindingNavigatorSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.bindingNavigatorMoveNextItem = new System.Windows.Forms.ToolStripButton();
            this.bindingNavigatorMoveLastItem = new System.Windows.Forms.ToolStripButton();
            this.bulkMessagesbutton = new System.Windows.Forms.Button();
            this.tableLayoutPanel1.SuspendLayout();
            this.panel1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.messagesBindingSource)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.messageDataSet)).BeginInit();
            this.bodyTabControl.SuspendLayout();
            this.bodyHexTabPage.SuspendLayout();
            this.bodyXmlTabPage.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.MessageBindingNavigator)).BeginInit();
            this.MessageBindingNavigator.SuspendLayout();
            this.SuspendLayout();
            // 
            // configQueuesButton
            // 
            this.configQueuesButton.Location = new System.Drawing.Point(9, 9);
            this.configQueuesButton.Name = "configQueuesButton";
            this.configQueuesButton.Size = new System.Drawing.Size(118, 23);
            this.configQueuesButton.TabIndex = 0;
            this.configQueuesButton.Text = "Configure Queues";
            this.configQueuesButton.UseVisualStyleBackColor = true;
            this.configQueuesButton.Click += new System.EventHandler(this.configQueuesButton_Click);
            // 
            // configTransfersButton
            // 
            this.configTransfersButton.Location = new System.Drawing.Point(133, 9);
            this.configTransfersButton.Name = "configTransfersButton";
            this.configTransfersButton.Size = new System.Drawing.Size(115, 23);
            this.configTransfersButton.TabIndex = 1;
            this.configTransfersButton.Text = "Config Transfers";
            this.configTransfersButton.UseVisualStyleBackColor = true;
            this.configTransfersButton.Click += new System.EventHandler(this.configTransfersButton_Click);
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 1;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Controls.Add(this.panel1, 0, 0);
            this.tableLayoutPanel1.Controls.Add(this.panel2, 0, 2);
            this.tableLayoutPanel1.Controls.Add(this.splitContainer1, 0, 1);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 3;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 42F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 41F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(739, 702);
            this.tableLayoutPanel1.TabIndex = 2;
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.bulkMessagesbutton);
            this.panel1.Controls.Add(this.configQueuesButton);
            this.panel1.Controls.Add(this.configTransfersButton);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel1.Location = new System.Drawing.Point(3, 3);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(733, 36);
            this.panel1.TabIndex = 0;
            // 
            // panel2
            // 
            this.panel2.Controls.Add(this.clearButton);
            this.panel2.Controls.Add(this.closeButton);
            this.panel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel2.Location = new System.Drawing.Point(3, 664);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(733, 35);
            this.panel2.TabIndex = 2;
            // 
            // clearButton
            // 
            this.clearButton.Location = new System.Drawing.Point(9, 7);
            this.clearButton.Name = "clearButton";
            this.clearButton.Size = new System.Drawing.Size(75, 23);
            this.clearButton.TabIndex = 2;
            this.clearButton.Text = "Clear";
            this.clearButton.UseVisualStyleBackColor = true;
            this.clearButton.Click += new System.EventHandler(this.clearButton_Click);
            // 
            // closeButton
            // 
            this.closeButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.closeButton.Location = new System.Drawing.Point(649, 7);
            this.closeButton.Name = "closeButton";
            this.closeButton.Size = new System.Drawing.Size(75, 23);
            this.closeButton.TabIndex = 1;
            this.closeButton.Text = "Close";
            this.closeButton.UseVisualStyleBackColor = true;
            this.closeButton.Click += new System.EventHandler(this.closeButton_Click);
            // 
            // splitContainer1
            // 
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(3, 45);
            this.splitContainer1.Name = "splitContainer1";
            this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.dataGridView1);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.bodyTabControl);
            this.splitContainer1.Panel2.Controls.Add(this.label5);
            this.splitContainer1.Panel2.Controls.Add(this.label4);
            this.splitContainer1.Panel2.Controls.Add(this.arrivedTextBox);
            this.splitContainer1.Panel2.Controls.Add(this.labelTextBox);
            this.splitContainer1.Panel2.Controls.Add(this.queueTextBox);
            this.splitContainer1.Panel2.Controls.Add(this.label3);
            this.splitContainer1.Panel2.Controls.Add(this.correlationIdTextBox);
            this.splitContainer1.Panel2.Controls.Add(this.label2);
            this.splitContainer1.Panel2.Controls.Add(this.label1);
            this.splitContainer1.Panel2.Controls.Add(this.idTextBox);
            this.splitContainer1.Panel2.Controls.Add(this.MessageBindingNavigator);
            this.splitContainer1.Size = new System.Drawing.Size(733, 613);
            this.splitContainer1.SplitterDistance = 245;
            this.splitContainer1.TabIndex = 3;
            // 
            // dataGridView1
            // 
            this.dataGridView1.AutoGenerateColumns = false;
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.idDataGridViewTextBoxColumn,
            this.queueNameDataGridViewTextBoxColumn,
            this.arrivedDataGridViewTextBoxColumn,
            this.labelDataGridViewTextBoxColumn});
            this.dataGridView1.DataSource = this.messagesBindingSource;
            this.dataGridView1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.dataGridView1.Location = new System.Drawing.Point(0, 0);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.Size = new System.Drawing.Size(733, 245);
            this.dataGridView1.TabIndex = 0;
            // 
            // idDataGridViewTextBoxColumn
            // 
            this.idDataGridViewTextBoxColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.idDataGridViewTextBoxColumn.DataPropertyName = "Id";
            this.idDataGridViewTextBoxColumn.HeaderText = "Id";
            this.idDataGridViewTextBoxColumn.Name = "idDataGridViewTextBoxColumn";
            // 
            // queueNameDataGridViewTextBoxColumn
            // 
            this.queueNameDataGridViewTextBoxColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.queueNameDataGridViewTextBoxColumn.DataPropertyName = "queueName";
            this.queueNameDataGridViewTextBoxColumn.HeaderText = "queueName";
            this.queueNameDataGridViewTextBoxColumn.Name = "queueNameDataGridViewTextBoxColumn";
            // 
            // arrivedDataGridViewTextBoxColumn
            // 
            this.arrivedDataGridViewTextBoxColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.arrivedDataGridViewTextBoxColumn.DataPropertyName = "arrived";
            this.arrivedDataGridViewTextBoxColumn.HeaderText = "arrived";
            this.arrivedDataGridViewTextBoxColumn.Name = "arrivedDataGridViewTextBoxColumn";
            // 
            // labelDataGridViewTextBoxColumn
            // 
            this.labelDataGridViewTextBoxColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.labelDataGridViewTextBoxColumn.DataPropertyName = "label";
            this.labelDataGridViewTextBoxColumn.HeaderText = "label";
            this.labelDataGridViewTextBoxColumn.Name = "labelDataGridViewTextBoxColumn";
            // 
            // messagesBindingSource
            // 
            this.messagesBindingSource.DataMember = "Messages";
            this.messagesBindingSource.DataSource = this.messageDataSet;
            this.messagesBindingSource.Sort = "arrived desc";
            this.messagesBindingSource.CurrentChanged += new System.EventHandler(this.messagesBindingSource_CurrentChanged);
            // 
            // messageDataSet
            // 
            this.messageDataSet.DataSetName = "MessageDataSet";
            this.messageDataSet.SchemaSerializationMode = System.Data.SchemaSerializationMode.IncludeSchema;
            // 
            // bodyTabControl
            // 
            this.bodyTabControl.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.bodyTabControl.Controls.Add(this.bodyHexTabPage);
            this.bodyTabControl.Controls.Add(this.bodyXmlTabPage);
            this.bodyTabControl.Location = new System.Drawing.Point(9, 158);
            this.bodyTabControl.Name = "bodyTabControl";
            this.bodyTabControl.SelectedIndex = 0;
            this.bodyTabControl.Size = new System.Drawing.Size(715, 203);
            this.bodyTabControl.TabIndex = 11;
            // 
            // bodyHexTabPage
            // 
            this.bodyHexTabPage.Controls.Add(this.bodyHexTextBox);
            this.bodyHexTabPage.Location = new System.Drawing.Point(4, 22);
            this.bodyHexTabPage.Name = "bodyHexTabPage";
            this.bodyHexTabPage.Padding = new System.Windows.Forms.Padding(3);
            this.bodyHexTabPage.Size = new System.Drawing.Size(707, 177);
            this.bodyHexTabPage.TabIndex = 0;
            this.bodyHexTabPage.Text = "Hex";
            this.bodyHexTabPage.UseVisualStyleBackColor = true;
            // 
            // bodyHexTextBox
            // 
            this.bodyHexTextBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.bodyHexTextBox.Font = new System.Drawing.Font("Courier New", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.bodyHexTextBox.Location = new System.Drawing.Point(3, 3);
            this.bodyHexTextBox.Multiline = true;
            this.bodyHexTextBox.Name = "bodyHexTextBox";
            this.bodyHexTextBox.ReadOnly = true;
            this.bodyHexTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.bodyHexTextBox.Size = new System.Drawing.Size(701, 171);
            this.bodyHexTextBox.TabIndex = 0;
            // 
            // bodyXmlTabPage
            // 
            this.bodyXmlTabPage.Controls.Add(this.bodyXmlTextBox);
            this.bodyXmlTabPage.Location = new System.Drawing.Point(4, 22);
            this.bodyXmlTabPage.Name = "bodyXmlTabPage";
            this.bodyXmlTabPage.Padding = new System.Windows.Forms.Padding(3);
            this.bodyXmlTabPage.Size = new System.Drawing.Size(707, 177);
            this.bodyXmlTabPage.TabIndex = 1;
            this.bodyXmlTabPage.Text = "Xml";
            this.bodyXmlTabPage.UseVisualStyleBackColor = true;
            // 
            // bodyXmlTextBox
            // 
            this.bodyXmlTextBox.Dock = System.Windows.Forms.DockStyle.Fill;
            this.bodyXmlTextBox.Location = new System.Drawing.Point(3, 3);
            this.bodyXmlTextBox.Multiline = true;
            this.bodyXmlTextBox.Name = "bodyXmlTextBox";
            this.bodyXmlTextBox.ReadOnly = true;
            this.bodyXmlTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.bodyXmlTextBox.Size = new System.Drawing.Size(701, 171);
            this.bodyXmlTextBox.TabIndex = 0;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(9, 135);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(40, 13);
            this.label5.TabIndex = 10;
            this.label5.Text = "Arrived";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(9, 109);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(33, 13);
            this.label4.TabIndex = 9;
            this.label4.Text = "Label";
            // 
            // arrivedTextBox
            // 
            this.arrivedTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.arrivedTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.messagesBindingSource, "arrived", true));
            this.arrivedTextBox.Location = new System.Drawing.Point(91, 132);
            this.arrivedTextBox.Name = "arrivedTextBox";
            this.arrivedTextBox.ReadOnly = true;
            this.arrivedTextBox.Size = new System.Drawing.Size(633, 20);
            this.arrivedTextBox.TabIndex = 8;
            // 
            // labelTextBox
            // 
            this.labelTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.labelTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.messagesBindingSource, "label", true));
            this.labelTextBox.Location = new System.Drawing.Point(91, 106);
            this.labelTextBox.Name = "labelTextBox";
            this.labelTextBox.ReadOnly = true;
            this.labelTextBox.Size = new System.Drawing.Size(633, 20);
            this.labelTextBox.TabIndex = 7;
            // 
            // queueTextBox
            // 
            this.queueTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.queueTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.messagesBindingSource, "queueName", true));
            this.queueTextBox.Location = new System.Drawing.Point(91, 80);
            this.queueTextBox.Name = "queueTextBox";
            this.queueTextBox.ReadOnly = true;
            this.queueTextBox.Size = new System.Drawing.Size(633, 20);
            this.queueTextBox.TabIndex = 6;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(9, 83);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(39, 13);
            this.label3.TabIndex = 5;
            this.label3.Text = "Queue";
            // 
            // correlationIdTextBox
            // 
            this.correlationIdTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.correlationIdTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.messagesBindingSource, "correlationId", true));
            this.correlationIdTextBox.Location = new System.Drawing.Point(91, 54);
            this.correlationIdTextBox.Name = "correlationIdTextBox";
            this.correlationIdTextBox.ReadOnly = true;
            this.correlationIdTextBox.Size = new System.Drawing.Size(633, 20);
            this.correlationIdTextBox.TabIndex = 4;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(9, 57);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(66, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "CorrelationId";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(9, 31);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(16, 13);
            this.label1.TabIndex = 2;
            this.label1.Text = "Id";
            // 
            // idTextBox
            // 
            this.idTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.idTextBox.DataBindings.Add(new System.Windows.Forms.Binding("Text", this.messagesBindingSource, "Id", true));
            this.idTextBox.Location = new System.Drawing.Point(91, 28);
            this.idTextBox.Name = "idTextBox";
            this.idTextBox.ReadOnly = true;
            this.idTextBox.Size = new System.Drawing.Size(633, 20);
            this.idTextBox.TabIndex = 1;
            // 
            // MessageBindingNavigator
            // 
            this.MessageBindingNavigator.AddNewItem = null;
            this.MessageBindingNavigator.BindingSource = this.messagesBindingSource;
            this.MessageBindingNavigator.CountItem = this.bindingNavigatorCountItem;
            this.MessageBindingNavigator.DeleteItem = null;
            this.MessageBindingNavigator.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.bindingNavigatorMoveFirstItem,
            this.bindingNavigatorMovePreviousItem,
            this.bindingNavigatorSeparator,
            this.bindingNavigatorPositionItem,
            this.bindingNavigatorCountItem,
            this.bindingNavigatorSeparator1,
            this.bindingNavigatorMoveNextItem,
            this.bindingNavigatorMoveLastItem});
            this.MessageBindingNavigator.Location = new System.Drawing.Point(0, 0);
            this.MessageBindingNavigator.MoveFirstItem = this.bindingNavigatorMoveFirstItem;
            this.MessageBindingNavigator.MoveLastItem = this.bindingNavigatorMoveLastItem;
            this.MessageBindingNavigator.MoveNextItem = this.bindingNavigatorMoveNextItem;
            this.MessageBindingNavigator.MovePreviousItem = this.bindingNavigatorMovePreviousItem;
            this.MessageBindingNavigator.Name = "MessageBindingNavigator";
            this.MessageBindingNavigator.PositionItem = this.bindingNavigatorPositionItem;
            this.MessageBindingNavigator.Size = new System.Drawing.Size(733, 25);
            this.MessageBindingNavigator.TabIndex = 0;
            this.MessageBindingNavigator.Text = "bindingNavigator1";
            // 
            // bindingNavigatorCountItem
            // 
            this.bindingNavigatorCountItem.Name = "bindingNavigatorCountItem";
            this.bindingNavigatorCountItem.Size = new System.Drawing.Size(44, 22);
            this.bindingNavigatorCountItem.Text = "von {0}";
            this.bindingNavigatorCountItem.ToolTipText = "Die Gesamtanzahl der Elemente.";
            // 
            // bindingNavigatorMoveFirstItem
            // 
            this.bindingNavigatorMoveFirstItem.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.bindingNavigatorMoveFirstItem.Image = ((System.Drawing.Image)(resources.GetObject("bindingNavigatorMoveFirstItem.Image")));
            this.bindingNavigatorMoveFirstItem.Name = "bindingNavigatorMoveFirstItem";
            this.bindingNavigatorMoveFirstItem.RightToLeftAutoMirrorImage = true;
            this.bindingNavigatorMoveFirstItem.Size = new System.Drawing.Size(23, 22);
            this.bindingNavigatorMoveFirstItem.Text = "Erste verschieben";
            // 
            // bindingNavigatorMovePreviousItem
            // 
            this.bindingNavigatorMovePreviousItem.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.bindingNavigatorMovePreviousItem.Image = ((System.Drawing.Image)(resources.GetObject("bindingNavigatorMovePreviousItem.Image")));
            this.bindingNavigatorMovePreviousItem.Name = "bindingNavigatorMovePreviousItem";
            this.bindingNavigatorMovePreviousItem.RightToLeftAutoMirrorImage = true;
            this.bindingNavigatorMovePreviousItem.Size = new System.Drawing.Size(23, 22);
            this.bindingNavigatorMovePreviousItem.Text = "Vorherige verschieben";
            // 
            // bindingNavigatorSeparator
            // 
            this.bindingNavigatorSeparator.Name = "bindingNavigatorSeparator";
            this.bindingNavigatorSeparator.Size = new System.Drawing.Size(6, 25);
            // 
            // bindingNavigatorPositionItem
            // 
            this.bindingNavigatorPositionItem.AccessibleName = "Position";
            this.bindingNavigatorPositionItem.AutoSize = false;
            this.bindingNavigatorPositionItem.Name = "bindingNavigatorPositionItem";
            this.bindingNavigatorPositionItem.Size = new System.Drawing.Size(50, 21);
            this.bindingNavigatorPositionItem.Text = "0";
            this.bindingNavigatorPositionItem.ToolTipText = "Aktuelle Position";
            // 
            // bindingNavigatorSeparator1
            // 
            this.bindingNavigatorSeparator1.Name = "bindingNavigatorSeparator1";
            this.bindingNavigatorSeparator1.Size = new System.Drawing.Size(6, 25);
            // 
            // bindingNavigatorMoveNextItem
            // 
            this.bindingNavigatorMoveNextItem.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.bindingNavigatorMoveNextItem.Image = ((System.Drawing.Image)(resources.GetObject("bindingNavigatorMoveNextItem.Image")));
            this.bindingNavigatorMoveNextItem.Name = "bindingNavigatorMoveNextItem";
            this.bindingNavigatorMoveNextItem.RightToLeftAutoMirrorImage = true;
            this.bindingNavigatorMoveNextItem.Size = new System.Drawing.Size(23, 22);
            this.bindingNavigatorMoveNextItem.Text = "Nächste verschieben";
            // 
            // bindingNavigatorMoveLastItem
            // 
            this.bindingNavigatorMoveLastItem.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.bindingNavigatorMoveLastItem.Image = ((System.Drawing.Image)(resources.GetObject("bindingNavigatorMoveLastItem.Image")));
            this.bindingNavigatorMoveLastItem.Name = "bindingNavigatorMoveLastItem";
            this.bindingNavigatorMoveLastItem.RightToLeftAutoMirrorImage = true;
            this.bindingNavigatorMoveLastItem.Size = new System.Drawing.Size(23, 22);
            this.bindingNavigatorMoveLastItem.Text = "Letzte verschieben";
            // 
            // bulkMessagesbutton
            // 
            this.bulkMessagesbutton.Location = new System.Drawing.Point(619, 9);
            this.bulkMessagesbutton.Name = "bulkMessagesbutton";
            this.bulkMessagesbutton.Size = new System.Drawing.Size(105, 23);
            this.bulkMessagesbutton.TabIndex = 2;
            this.bulkMessagesbutton.Text = "Bulk Messages";
            this.bulkMessagesbutton.UseVisualStyleBackColor = true;
            this.bulkMessagesbutton.Click += new System.EventHandler(this.bulkMessagesbutton_Click);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(739, 702);
            this.Controls.Add(this.tableLayoutPanel1);
            this.Name = "MainForm";
            this.Text = "MsmqTestLoopback";
            this.Load += new System.EventHandler(this.MainForm_Load);
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MainForm_FormClosing);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            this.splitContainer1.Panel2.PerformLayout();
            this.splitContainer1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.messagesBindingSource)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.messageDataSet)).EndInit();
            this.bodyTabControl.ResumeLayout(false);
            this.bodyHexTabPage.ResumeLayout(false);
            this.bodyHexTabPage.PerformLayout();
            this.bodyXmlTabPage.ResumeLayout(false);
            this.bodyXmlTabPage.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.MessageBindingNavigator)).EndInit();
            this.MessageBindingNavigator.ResumeLayout(false);
            this.MessageBindingNavigator.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button configQueuesButton;
        private System.Windows.Forms.Button configTransfersButton;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button closeButton;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Button clearButton;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private System.Windows.Forms.TextBox idTextBox;
        private System.Windows.Forms.BindingNavigator MessageBindingNavigator;
        private System.Windows.Forms.ToolStripLabel bindingNavigatorCountItem;
        private System.Windows.Forms.ToolStripButton bindingNavigatorMoveFirstItem;
        private System.Windows.Forms.ToolStripButton bindingNavigatorMovePreviousItem;
        private System.Windows.Forms.ToolStripSeparator bindingNavigatorSeparator;
        private System.Windows.Forms.ToolStripTextBox bindingNavigatorPositionItem;
        private System.Windows.Forms.ToolStripSeparator bindingNavigatorSeparator1;
        private System.Windows.Forms.ToolStripButton bindingNavigatorMoveNextItem;
        private System.Windows.Forms.ToolStripButton bindingNavigatorMoveLastItem;
        private System.Windows.Forms.DataGridView dataGridView1;
        private System.Windows.Forms.DataGridViewTextBoxColumn idDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn queueNameDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn arrivedDataGridViewTextBoxColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn labelDataGridViewTextBoxColumn;
        private System.Windows.Forms.BindingSource messagesBindingSource;
        private MessageDataSet messageDataSet;
        private System.Windows.Forms.TextBox correlationIdTextBox;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox queueTextBox;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox arrivedTextBox;
        private System.Windows.Forms.TextBox labelTextBox;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TabControl bodyTabControl;
        private System.Windows.Forms.TabPage bodyHexTabPage;
        private System.Windows.Forms.TextBox bodyHexTextBox;
        private System.Windows.Forms.TabPage bodyXmlTabPage;
        private System.Windows.Forms.TextBox bodyXmlTextBox;
        private System.Windows.Forms.Button bulkMessagesbutton;
    }
}

