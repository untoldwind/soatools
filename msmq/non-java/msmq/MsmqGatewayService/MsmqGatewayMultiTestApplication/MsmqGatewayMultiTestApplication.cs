using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Messaging;
using System.IO;

namespace MsmqGatewayMultiTestApplication
{
    public partial class MsmqGatewayMultiTestApplication : Form
    {
        private MessageQueue mq;
        private ReceivedMessage rmsg;
        private Dictionary<string, ReceivedMessage> receivedMessages;
        private delegate void AddMessage(System.Messaging.Message message);

        public MsmqGatewayMultiTestApplication()
        {
            InitializeComponent();
            this.mq = null;
            this.rmsg = null;
            this.receivedMessages = new Dictionary<string, ReceivedMessage>();
        }

        private void MsmqGatewayMultiTestApplication_Load(object sender, EventArgs e)
        {
            this.lblQueueFound.Visible = false;
        }

        private void txtQueue_TextChanged(object sender, EventArgs e)
        {
            String queue = this.txtQueue.Text;
            try
            {
                if (MessageQueue.Exists(queue))
                {
                    this.lblQueueFound.ForeColor = System.Drawing.Color.Green;
                    this.lblQueueFound.Text = "queue found";
                    this.lblQueueFound.Visible = true;
                    this.mq = new MessageQueue(queue);
                    this.mq.MessageReadPropertyFilter.ArrivedTime = true;
                    this.mq.MessageReadPropertyFilter.Priority = true;
                    backgroundWorker.RunWorkerAsync();
                }
                else if (queue.Equals(""))
                {
                    if (backgroundWorker.IsBusy)
                        backgroundWorker.CancelAsync();
                    this.lblQueueFound.Visible = false;
                }
                else
                {
                    if (backgroundWorker.IsBusy)
                        backgroundWorker.CancelAsync();
                    this.lblQueueFound.ForeColor = System.Drawing.Color.Red;
                    this.lblQueueFound.Text = "queue not found";
                    this.lblQueueFound.Visible = true;
                    this.mq = null;
                }
            }
            catch (Exception)
            {
                this.lblQueueFound.ForeColor = System.Drawing.Color.Red;
                this.lblQueueFound.Text = "queue not found";
                this.lblQueueFound.Visible = true;
                this.mq = null;
            }
        }

        private void txtQueue_MouseClick(object sender, MouseEventArgs e)
        {
            this.txtQueue.SelectAll();
        }

        private void btnClose_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void txtQuantity_TextChanged(object sender, EventArgs e)
        {
            this.txtQuantity.SelectAll();
        }

        private void addMessage(System.Messaging.Message message)
        {
            lock (receivedMessages)
            {
                receivedMessages.Add(message.Id, new ReceivedMessage(message));
                listViewReceivedMessages.Items.Add(new ListViewItem(new string[] { message.Id, message.Label, message.Priority.ToString(), DateTime.Now.ToString() }));
            }
        }

        private void backgroundWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            if (receivedMessages == null)
                return;

            BackgroundWorker bw = (BackgroundWorker)sender;

            while (!bw.CancellationPending)
            {
                try
                {
                    if (this.mq != null)
                    {
                        System.Messaging.Message message = this.mq.Receive(new TimeSpan(0, 0, 1));

                        if (message != null)
                        {
                            listViewReceivedMessages.BeginInvoke(new AddMessage(addMessage), new object[] { message });
                        }
                    }
                }
                catch (MessageQueueException)
                {
                }
            }
        }

        private void listViewReceivedMessages_SelectedIndexChanged(object sender, EventArgs e)
        {
            try
            {
                if (listViewReceivedMessages.SelectedItems.Count > 0)
                {
                    ListViewItem selected = (ListViewItem)listViewReceivedMessages.SelectedItems[0];
                    string messageId = selected.Text;
                    ReceivedMessage message = receivedMessages[messageId];

                    this.rtxtBody.Text = HexUtils.FullHexFormat(message.Body);
                    this.rmsg = message;
                    //txtReceiveMessageXml.Text = XmlUtils.FormatXml(message.Body);
                }
            }
            catch (Exception)
            {
            }
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            if (this.mq != null)
            {
                if (this.rmsg != null) 
                {
                    if (!this.rtxtBody.Text.Equals(""))
                    {
                        if (!this.txtQuantity.Text.Equals(""))
                        {                            
                            int quantity = Int32.Parse(txtQuantity.Text);

                            for (int i = 0; i < quantity; i++)
                            {  
                                System.Messaging.Message msg = new System.Messaging.Message();
                                msg.Label = this.rmsg.Label;
                                msg.BodyStream = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(this.rtxtBody.Text));
                                msg.Priority = this.rmsg.Priority;
                                mq.Send(msg);
                            }
                            this.lblQueueFound.Visible = false;
                        }
                        else
                        {
                            MessageBox.Show("Bitte geben Sie zuerst den Anzahl ein.",
                                "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        }
                    }
                    else
                    {
                        MessageBox.Show("Bitte geben Sie zuerst den Body ein.",
                           "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                        this.rtxtBody.Select();
                    }
                } 
                else 
                {
                    MessageBox.Show("Sie müssen zuerst eine Message auswählen.",
                        "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }
            }
            else
            {
                MessageBox.Show("Sie müssen zuerst eine Queue auswählen.",
                   "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                this.txtQueue.Select();
            }
        }
    }
}