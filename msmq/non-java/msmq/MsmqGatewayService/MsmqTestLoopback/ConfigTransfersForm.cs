using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace MsmqTestLoopback
{
    public partial class ConfigTransfersForm : Form
    {
        public ConfigTransfersForm()
        {
            InitializeComponent();
        }

        private void okButton_Click(object sender, EventArgs e)
        {
            StringCollection transfers = new StringCollection();
            foreach (ListViewItem item in transferListView.Items)
            {
                transfers.Add(item.SubItems[0].Text + "|" + item.SubItems[1].Text);
            }
            Properties.Settings.Default.Transfers = transfers;
            Properties.Settings.Default.Save();

            Close();
        }

        private void ConfigTransfersForm_Load(object sender, EventArgs e)
        {
            if ( Properties.Settings.Default.RequestQueues != null )
                foreach (string queue in Properties.Settings.Default.RequestQueues)
                {
                    requestListBox.Items.Add(queue);
                }
            if ( Properties.Settings.Default.ResponseQueues != null ) 
                foreach (string queue in Properties.Settings.Default.ResponseQueues)
                {
                    responseListBox.Items.Add(queue);
                }
            if ( Properties.Settings.Default.Transfers != null )
                foreach (string transfer in Properties.Settings.Default.Transfers)
                {
                    string[] requestResponse = transfer.Trim().Split('|');
                    ListViewItem item = new ListViewItem(requestResponse);
                    transferListView.Items.Add(item);
                }
        }

        private void requestListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            addButton.Enabled = requestListBox.SelectedItem != null && responseListBox.SelectedItem != null;
        }

        private void responseListBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            addButton.Enabled = requestListBox.SelectedItem != null && responseListBox.SelectedItem != null;
        }

        private void transferListView_SelectedIndexChanged(object sender, EventArgs e)
        {
            removeButton.Enabled = transferListView.SelectedItems.Count != 0;
        }

        private void addButton_Click(object sender, EventArgs e)
        {
            ListViewItem item = new ListViewItem(new string[] { (string)requestListBox.SelectedItem, (string)responseListBox.SelectedItem });

            transferListView.Items.Add(item);
        }

        private void removeButton_Click(object sender, EventArgs e)
        {
            foreach (ListViewItem item in transferListView.SelectedItems)
            {
                transferListView.Items.Remove(item);
            }
        }
    }
}
