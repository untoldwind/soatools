using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Messaging;

namespace MsmqGatewayConfig
{
    public partial class OutgoingDestinationForm : Form
    {
        public OutgoingDestinationForm()
        {
            InitializeComponent();

            MessageQueue[] privateQueues = MessageQueue.GetPrivateQueuesByMachine(".");
            foreach (MessageQueue queue in privateQueues)
            {
                queueComboBox.Items.Add(queue.Path);
            }
        }

        public string DestinationName
        {
            get { return nameTextBox.Text; }
        }

        public string DestinationQueue
        {
            get { return queueComboBox.Text; }
        }
    }
}
