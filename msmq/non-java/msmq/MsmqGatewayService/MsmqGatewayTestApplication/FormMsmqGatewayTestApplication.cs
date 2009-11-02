using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Messaging;
using System.IO;

namespace MsmqGatewayTestApplication
{
    public partial class MsmqGatewayTestApplication : Form
    {
        private MessageQueue mq;
        private MessageQueue receiveMQ;
        private Dictionary<string, ReceivedMessage> receivedMessages;
        private long receivedMessageCount;
        private delegate void AddMessage(System.Messaging.Message message);

        public MsmqGatewayTestApplication()
        {
            InitializeComponent();
            this.mq = null;
            this.receiveMQ = null;
            this.receivedMessages = new Dictionary<string, ReceivedMessage>();
            this.receivedMessageCount = 0;
        }

        private void MsmqGatewayTestApplication_Load(object sender, EventArgs e)
        {
            this.lblQueueFound.Visible = false;
            this.lblReceiveQueueFound.Visible = false;
            this.coboPrioritaet.SelectedIndex = 3;
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            if (this.mq != null)
            {
                if (!this.rtxtBody.Text.Equals(""))
                {
                    System.Messaging.Message msg = new System.Messaging.Message();
                    msg.Label = this.txtLabel.Text;
                    msg.BodyStream = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(this.rtxtBody.Text));
                    msg.Priority = getPriority(this.coboPrioritaet.SelectedIndex);
                    mq.Send(msg);
                    this.lblQueueFound.Visible = false;
                }
                else
                {
                    MessageBox.Show("Bitte geben Sie zuerst den Body ein.",
                       "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                }                
            }
            else
            {
                MessageBox.Show("Sie müssen zuerst eine Queue auswählen.",
                   "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        private MessagePriority getPriority(int param)
        {
            switch (param)
            {
                case 0: { return MessagePriority.Lowest; }
                case 1: { return MessagePriority.VeryLow; }
                case 2: { return MessagePriority.Low; }
                case 3: { return MessagePriority.Normal; }
                case 4: { return MessagePriority.AboveNormal; }
                case 5: { return MessagePriority.High; }
                case 6: { return MessagePriority.VeryHigh; }
                case 7: { return MessagePriority.Highest; }
                default: { return MessagePriority.Normal; }
            }
        }

        private void txtQueue_Enter(object sender, EventArgs e)
        {
            this.txtQueue.SelectAll();
        }

        private void txtQueue_MouseClick(object sender, MouseEventArgs e)
        {
            this.txtQueue.SelectAll();
        }

        private void btnClose_Click(object sender, EventArgs e)
        {
            Close();
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
                }
                else if (queue.Equals(""))
                {
                    this.lblQueueFound.Visible = false;
                }
                else
                {
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

        private void txtReceiveQueue_TextChanged(object sender, EventArgs e)
        {
            String queue = this.txtReceiveQueue.Text;
            try
            {
                if (MessageQueue.Exists(queue))
                {
                    this.lblReceiveQueueFound.ForeColor = System.Drawing.Color.Green;
                    this.lblReceiveQueueFound.Text = "queue found";
                    this.lblReceiveQueueFound.Visible = true;
                    this.receiveMQ = new MessageQueue(queue);
                    this.receiveMQ.MessageReadPropertyFilter.ArrivedTime = true;
                    this.receiveMQ.MessageReadPropertyFilter.Priority = true;

                    backgroundWorker.RunWorkerAsync();
                }
                else if (queue.Equals(""))
                {
                    if (backgroundWorker.IsBusy)
                        backgroundWorker.CancelAsync();
                    this.lblReceiveQueueFound.Visible = false;
                }
                else
                {
                    if (backgroundWorker.IsBusy)
                        backgroundWorker.CancelAsync();
                    this.lblReceiveQueueFound.ForeColor = System.Drawing.Color.Red;
                    this.lblReceiveQueueFound.Text = "queue not found";
                    this.lblReceiveQueueFound.Visible = true;
                    this.receiveMQ = null;
                }
            }
            catch (Exception)
            {
                this.lblReceiveQueueFound.ForeColor = System.Drawing.Color.Red;
                this.lblReceiveQueueFound.Text = "queue not found";
                this.lblReceiveQueueFound.Visible = true;
                this.mq = null;
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
                    System.Messaging.Message message = receiveMQ.Receive(new TimeSpan(0, 0, 1));

                    if (message != null)
                    {
                        listViewReceivedMessages.BeginInvoke(new AddMessage(addMessage), new object[] { message });
                    }
                }
                catch (MessageQueueException)
                {
                }
            }
        }

        private void addMessage(System.Messaging.Message message) 
        {
            lock (receivedMessages)
            {
                receivedMessages.Add(message.Id, new ReceivedMessage(message));
                listViewReceivedMessages.Items.Add(new ListViewItem(new string[] { message.Id, message.Label, message.Priority.ToString(), DateTime.Now.ToString() }));
                receivedMessageCount++;
                countTextBox.Text = receivedMessageCount.ToString();
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

                    txtReceivedMessageBytes.Text = HexUtils.FullHexFormat(message.Body);
                    txtReceiveMessageXml.Text = XmlUtils.FormatXml(message.Body);
                }
            }
            catch (Exception)
            {
            }
        }

        private void btnSendFile_Click(object sender, EventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog();

            dialog.Filter = "xml (*.xml)|*.xml|All files (*.*)|*.*";
            dialog.FilterIndex = 2;

            if (dialog.ShowDialog() == DialogResult.OK)
            {
                try
                {
                    Stream inStream = dialog.OpenFile();
                    

                    if (inStream != null)
                    {
                        System.Messaging.Message msg = new System.Messaging.Message();
                        msg.Label = this.txtLabel.Text;
                        msg.BodyStream = inStream;
                        msg.Priority = getPriority(this.coboPrioritaet.SelectedIndex);
                        mq.Send(msg);
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error: " + ex.Message);
                }
            }
        }

        private void btnSimulate_Click(object sender, EventArgs e)
        {
            SimulateForm form = new SimulateForm(mq);

            form.Show();
        }

        private void btnClear_Click(object sender, EventArgs e)
        {
            lock (receivedMessages)
            {
                receivedMessages.Clear();
                listViewReceivedMessages.Items.Clear();
            }
        }
    }
}
