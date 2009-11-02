using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using System.Messaging;
using System.IO;
using System.Net;
using MessageListenerConsole.MsmqInGateway;

namespace MessageListenerConsole
{
    class Listener
    {
        bool running = true;
        MessageQueue mq;
        MsmqInGatewayWSService gateway;

        public Listener()
        {
            string queueName = Properties.Settings.Default.MessageQueueName;
            if (MessageQueue.Exists(queueName))
            {
                mq = new MessageQueue(queueName);
            }
            else
            {
                mq = MessageQueue.Create(queueName, true);
            }
            mq.MessageReadPropertyFilter.ArrivedTime = true;
            mq.MessageReadPropertyFilter.SentTime = true;
            mq.MessageReadPropertyFilter.Priority = true;
            mq.MessageReadPropertyFilter.CorrelationId = true;
            mq.MessageReadPropertyFilter.Id = true;
            mq.MessageReadPropertyFilter.SenderId = true;

            gateway = new MsmqInGatewayWSService();
        }

        public void Shutdown()
        {
            running = false;
        }

        public void Run()
        {
            /*MessageQueueTransaction transaction = new MessageQueueTransaction();
            while (running)
            {
                try
                {
                    transaction.Begin();
                    Message message = mq.Receive(new TimeSpan(0, 0, 1), transaction);

                    Console.WriteLine("Message received");

                    msmqInMessage msmqMsg = new msmqInMessage();
                    msmqMsg.id = message.Id;
                    msmqMsg.label = message.Label;
                    msmqMsg.messageType = message.MessageType.ToString();
                    msmqMsg.priority = message.Priority.ToString();
                    msmqMsg.sentTime = message.SentTime;
                    msmqMsg.arrivedTime = message.ArrivedTime;
                    msmqMsg.correlationId = message.CorrelationId;
                    msmqMsg.senderId = message.SenderId;
                    msmqMsg.body = readFully(message.BodyStream);

                    


                    if (gateway.routeMessage("Navision", "IncomingGateway", msmqMsg))
                    {
                        transaction.Commit();
                    }
                    else
                    {
                        Console.WriteLine("Gateway reported error");
                        transaction.Abort();
                        Thread.Sleep(new TimeSpan(0, 0, 10));
                    }
                }
                catch (MessageQueueException e)
                {
                    if ( e.MessageQueueErrorCode != MessageQueueErrorCode.IOTimeout ) 
                        Console.WriteLine("MessageQueue failure: " + e.Message);
                }
                catch (WebException e) 
                {
                    Console.WriteLine("Failed to invoke gateway: " + e.Message);
                    Thread.Sleep(new TimeSpan(0, 0, 10));
                }
                finally
                {
                    if (transaction.Status == MessageQueueTransactionStatus.Pending)
                    {
                        transaction.Abort();
                    }
                }
            }*/
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
    }
}
