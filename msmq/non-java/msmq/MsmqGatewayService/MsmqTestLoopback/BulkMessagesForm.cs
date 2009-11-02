using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Messaging;

namespace MsmqTestLoopback
{
    public partial class BulkMessagesForm : Form
    {
        public BulkMessagesForm()
        {
            InitializeComponent();
        }

        private void BulkMessagesForm_Load(object sender, EventArgs e)
        {
            if (Properties.Settings.Default.ResponseQueues != null)
                foreach (string queue in Properties.Settings.Default.ResponseQueues)
                {
                    queueComboBox.Items.Add(queue);
                }
        }

        private void runButton_Click(object sender, EventArgs e)
        {
            int size = (int)sizeNumericUpDown.Value;
            byte[] data = new byte[size];

            for (int i = 0; i < size; i++)
            {
                data[i] = (byte)(i & 0xff);
            }

            int count = (int)countNumericUpDown.Value;
            string queue = queueComboBox.SelectedItem.ToString();

            MessageQueue mq = new MessageQueue(queue);
            MessageQueueTransaction transaction = new MessageQueueTransaction();

            for (int i = 0; i < count; i++)
            {
                System.Messaging.Message message = new System.Messaging.Message();

                message.Label = "Bulk " + i;
                BulkPayload payload = new BulkPayload();
                payload.Count = i;
                payload.Text = "Text " + i;
                payload.Data = data;
                message.Body = payload;

                try
                {
                    transaction.Begin();
                    mq.Send(message, transaction);
                    transaction.Commit();
                }
                finally
                {
                    if (transaction.Status == MessageQueueTransactionStatus.Pending)
                        transaction.Abort();
                }
            }
        }
    }

    [Serializable]
    public class BulkPayload
    {
        private int count;
        private string text;
        private byte[] data;

        public byte[] Data
        {
            get { return data; }
            set { data = value; }
        }

        public string Text
        {
            get { return text; }
            set { text = value; }
        }

        public int Count
        {
            get { return count; }
            set { count = value; }
        }

    }
}
