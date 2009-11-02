using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.Messaging;
using System.IO;
using System.Net;
using MessageListenerConsole.MsmqInGateway;
using System.Collections;

namespace MessageListenerConsole
{
    public partial class MessageMediator : Form
    {
        Hashtable messageQueues;
        MsmqInGatewayWSService gateway;

        public MessageMediator()
        {
            InitializeComponent();
            label.Enabled = false;
            body.Enabled = false;
            messageQueues = new Hashtable();
        }

        public void testbutton_Click(object sender, EventArgs e) 
        {
            MessageQueue mq;
            string queueName = Properties.Settings.Default.MessageQueueName;
            if (MessageQueue.Exists(queueName))
            {
                mq = new MessageQueue(queueName);
            }
            else
            {
                mq = MessageQueue.Create(queueName, true);
            }

            gateway = new MsmqInGatewayWSService();

            MessageQueueTransaction transaction = new MessageQueueTransaction();
            //while (running)
           // {
                try
                {
                    transaction.Begin();
                    System.Messaging.Message message = mq.Receive(new TimeSpan(0, 0, 1), transaction);

                    Console.WriteLine("Message received");

                    msmqInMessage msmqMsg = new msmqInMessage();
                    msmqMsg.label = message.Label;
                    Console.WriteLine("Message received333");
                    msmqMsg.id = message.Id;
                    Console.WriteLine("Message received222");
                    msmqMsg.body = readFully(message.BodyStream);
                                     
                    Console.WriteLine("Commit");
                    transaction.Commit();
                   
                    messageList.Items.Add(msmqMsg.label);
                    messageQueues.Add(msmqMsg.id, msmqMsg);
                }
                catch (MessageQueueException mqe)
                {
                    if (mqe.MessageQueueErrorCode != MessageQueueErrorCode.IOTimeout)
                        Console.WriteLine("MessageQueue failure: " + mqe.Message);
                }                
                finally
                {
                    if (transaction.Status == MessageQueueTransactionStatus.Pending)
                    {
                        transaction.Abort();
                    }
                }
           // }
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

        private void messageList_SelectedValueChanged(object sender, EventArgs e)
        {
            label.Text = (String)messageList.SelectedItem;
            //body.Text = 
        }
    }
}
