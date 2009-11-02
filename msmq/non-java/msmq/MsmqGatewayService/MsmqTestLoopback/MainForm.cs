using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Messaging;
using System.IO;
using System.Threading;

namespace MsmqTestLoopback
{
    public partial class MainForm : Form
    {
        ConfigQueuesForm configQueuesForm;
        ConfigTransfersForm configTransfersForm;
        BulkMessagesForm bulkMessagesForm;

        List<MessageListener> listeners = new List<MessageListener>();

        public MainForm()
        {
            InitializeComponent();
        }

        private void configQueuesButton_Click(object sender, EventArgs e)
        {
            if (configQueuesForm != null && !configQueuesForm.IsDisposed)
            {
                configQueuesForm.BringToFront();
                return;
            }

            configQueuesForm = new ConfigQueuesForm();
            configQueuesForm.Show();
        }

        private void configTransfersButton_Click(object sender, EventArgs e)
        {
            if (configTransfersForm != null && !configTransfersForm.IsDisposed)
            {
                configTransfersForm.BringToFront();
                return;
            }

            configTransfersForm = new ConfigTransfersForm();
            configTransfersForm.Show();
        }

        private void closeButton_Click(object sender, EventArgs e)
        {
            StopListeners();
            Close();
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            StartListeners();
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            StopListeners();
        }

        private void clearButton_Click(object sender, EventArgs e)
        {
            messageDataSet.Messages.Clear();
        }

        private void AddMessage(string queueName, System.Messaging.Message message)
        {
            messageDataSet.Messages.AddMessagesRow(message.Id, queueName, 
                message.CorrelationId, message.ArrivedTime, message.Label, 
                readFully(message.BodyStream));
        }

        private void messagesBindingSource_CurrentChanged(object sender, EventArgs e)
        {
            DataRowView rowView = (DataRowView)messagesBindingSource.Current;
            if (rowView != null)
            {
                MessageDataSet.MessagesRow row = (MessageDataSet.MessagesRow)rowView.Row;

                bodyHexTextBox.Text = HexUtils.FullHexFormat(row.body);
                bodyXmlTextBox.Text = XmlUtils.FormatXml(row.body);
            }
            else
            {
                bodyHexTextBox.Text = "";
                bodyXmlTextBox.Text = "";
            }
        }

        private void StartListeners()
        {
            if ( Properties.Settings.Default.Transfers != null )
                foreach (string transfer in Properties.Settings.Default.Transfers)
                {
                    string[] requestResponse = transfer.Trim().Split('|');

                    if (requestResponse.Length == 2)
                    {
                        MessageListener listener = new MessageListener(requestResponse[0], requestResponse[1], this, new MessageListener.InvokeAddMessage(AddMessage));
                            
                        listeners.Add(listener);
                        listener.Start();
                    }
                }
        }

        private void StopListeners()
        {
            foreach(MessageListener listener in listeners ) 
            {
                listener.Stop();
            }
            listeners.Clear();
        }

        byte[] readFully(Stream stream)
        {
            byte[] buffer = new byte[8192];
            using (MemoryStream ms = new MemoryStream())
            {
                while (true)
                {
                    int read = stream.Read(buffer, 0, buffer.Length);
                    if (read <= 0)
                        return ms.ToArray();
                    ms.Write(buffer, 0, read);
                }
            }
        }

        private void bulkMessagesbutton_Click(object sender, EventArgs e)
        {
            if (bulkMessagesForm != null && !bulkMessagesForm.IsDisposed)
            {
                bulkMessagesForm.BringToFront();
                return;
            }
            bulkMessagesForm = new BulkMessagesForm();
            bulkMessagesForm.Show();
        }

    }
}
