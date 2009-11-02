using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Messaging;

namespace MsmqTestLoopback
{
    public partial class ConfigQueuesForm : Form
    {
        public ConfigQueuesForm()
        {
            InitializeComponent();
        }

        private void okButton_Click(object sender, EventArgs e)
        {
            StringCollection requestQueues = new StringCollection();
            StringCollection responseQueues = new StringCollection();

            foreach (string requestQueue in requestListBox.CheckedItems)
            {
                requestQueues.Add(requestQueue);
            }
            foreach (string responseQueue in responseListBox.CheckedItems)
            {
                responseQueues.Add(responseQueue);
            }
            Properties.Settings.Default.RequestQueues = requestQueues;
            Properties.Settings.Default.ResponseQueues = responseQueues;
            Properties.Settings.Default.Save();

            Close();
        }

        private void ConfigQueuesForm_Load(object sender, EventArgs e)
        {
            MessageQueue[] privateQueues = MessageQueue.GetPrivateQueuesByMachine(".");
            StringCollection requestQueues = Properties.Settings.Default.RequestQueues;
            StringCollection responseQueues = Properties.Settings.Default.ResponseQueues;
            foreach (MessageQueue queue in privateQueues ) 
            {
                string path = queue.Path;
                requestListBox.Items.Add(path, requestQueues != null && requestQueues.Contains(path));
                responseListBox.Items.Add(path, responseQueues != null && responseQueues.Contains(path));
            }
        }
    }
}
