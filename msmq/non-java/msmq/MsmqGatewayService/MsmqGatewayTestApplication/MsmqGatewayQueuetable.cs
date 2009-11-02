using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Messaging;
using System.Windows.Forms;

namespace MsmqGatewayTestApplication
{
    public partial class MsmqGatewayQueuetable : Form
    {

        public MsmqGatewayQueuetable()
        {
            InitializeComponent();
            this.tblQueues.Scrollable = true;
        }

        private void btnClose_Click(object sender, EventArgs e)
        {
            this.Visible = false;
        }

        public void setData(MessageQueue mq)
        {
            this.tblQueues.Items.Clear();

            System.Messaging.Message[] messages = mq.GetAllMessages();

            for (int i = 0; i < messages.Length; i++)
            {
                String[] queueItem = new String[] { i + "", messages[i].Label };
                this.tblQueues.Items.Add(new ListViewItem(queueItem));
            }
        }

        private void tblQueues_MouseClick(object sender, MouseEventArgs e)
        {
            System.Windows.Forms.ListView.SelectedListViewItemCollection sic = this.tblQueues.SelectedItems;

            System.Collections.IEnumerator enColumns = sic.GetEnumerator();

            if (enColumns.MoveNext())
            {
                ListViewItem message = (ListViewItem)enColumns.Current;
                String[] messageTag = (String[]) message.Tag;
                System.Windows.Forms.ListViewItem.ListViewSubItemCollection list = message.SubItems;
                System.Collections.IEnumerator enCells = list.GetEnumerator();
                enCells.MoveNext();
                System.Windows.Forms.ListViewItem.ListViewSubItem item = (System.Windows.Forms.ListViewItem.ListViewSubItem) enCells.Current;
                this.txtNumber.Text = item.Text;
                enCells.MoveNext();
                item = (System.Windows.Forms.ListViewItem.ListViewSubItem) enCells.Current;
                this.txtLabel.Text = item.Text;
            }
        }
    }
}
