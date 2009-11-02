using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.Remoting.Metadata;
using System.Messaging;
using System.IO;

namespace MsmqGatewayService
{
    [SoapType(XmlNamespace = "http://liwest.at/msmq/outgoing")]
    class MsmqOutGateway : MarshalByRefObject
    {
        Dictionary<string, string> destinations;
        private System.Object lockThis = new System.Object();


        public MsmqOutGateway()
        {
            try
            {
                destinations = new Dictionary<string, string>();
                StringBuilder builder = new StringBuilder();
                foreach (string destination in Properties.Settings.Default.OutgoingDestinations)
                {
                    string[] keyValue = destination.Trim().Split('|');
                    if (keyValue.Length == 2)
                    {
                        if (MessageQueue.Exists(keyValue[1]))
                        {
                            destinations.Add(keyValue[0], keyValue[1]);
                            builder.AppendLine();
                            builder.Append("'").Append(keyValue[0]).Append("' maps to '").Append(keyValue[1]).Append("'");
                        }
                        else
                        {
                            MsmqGatewayService.Instance.EventLog.WriteEntry("Unknown queue " + keyValue[1],
                                System.Diagnostics.EventLogEntryType.Error);
                        }
                    }
                }
                MsmqGatewayService.Instance.EventLog.WriteEntry("MsmqOutGateway initialized: " + builder);
            }
            catch (Exception e)
            {
                MsmqGatewayService.Instance.EventLog.WriteEntry("Failed to initialize MsmqOutGateway " + e.StackTrace,
                    System.Diagnostics.EventLogEntryType.Error);
            }
        }

        [SoapMethod(SoapAction = "routeMessage", XmlNamespace = "http://liwest.at/msmq/outgoing")]
        public bool routeMessage(string destination, MsmqOutMessage message)
        {
            lock (lockThis)
            {
                MessageQueue messageQueue = null;
                MessageQueueTransaction transaction = null;

                try
                {
                    string messageQueueStr = destinations[destination];
                    messageQueue = new MessageQueue(messageQueueStr);
                }
                catch (Exception e)
                {
                    if (messageQueue != null)
                        messageQueue.Close();

                    MsmqGatewayService.Instance.EventLog.WriteEntry("Failed to route message: " + destination + " " + message + " " + e.StackTrace,
                        System.Diagnostics.EventLogEntryType.Error);
                    return false;
                }

                if (messageQueue.Transactional)
                {
                    transaction = new MessageQueueTransaction();
                }

                try
                {
                    Message msg = new Message();

                    if (message.Binary)
                        msg.BodyStream = new MemoryStream(System.Convert.FromBase64String(message.Body));
                    else
                        msg.BodyStream = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(message.Body));
                    if (message.Label != null)
                        msg.Label = message.Label;
                    if (message.CorrelationId != null)
                        msg.CorrelationId = message.CorrelationId;

                    if (transaction != null)
                    {
                        transaction.Begin();
                        messageQueue.Send(msg, transaction);
                        transaction.Commit();
                    }
                    else
                    {
                        messageQueue.Send(msg);
                    }

                    if (Properties.Settings.Default.LogOutgoing)
                    {
                        string msgStr = "routed message: \n" +
                                            "<<destination: " + destination + ">>\n" +
                                            "<<message: " + message + ">>\n" +
                                            "<<messageQueue.hashCode: " + messageQueue.GetHashCode() + ">>\n" +
                                            "<<messageQueue: " + messageQueue.ToString() + ">>";

                        MsmqGatewayService.Instance.EventLog.WriteEntry(msgStr);
                    }

                    return true;
                }
                catch (Exception e)
                {
                    MsmqGatewayService.Instance.EventLog.WriteEntry("Failed to route message: " + destination + " " + message + " " + e.StackTrace,
                        System.Diagnostics.EventLogEntryType.Error);
                }
                finally
                {
                    if (messageQueue != null)
                        messageQueue.Close();

                    if (transaction != null && transaction.Status == MessageQueueTransactionStatus.Pending)
                        transaction.Abort();
                }
                return false;
            }

        }
    }
}
