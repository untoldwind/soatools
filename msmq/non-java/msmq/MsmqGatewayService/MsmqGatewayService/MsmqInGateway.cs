using System;
using System.Collections.Generic;
using System.Text;
using System.Messaging;
using System.IO;
using System.Net;
using System.Threading;
using MsmqGatewayService.MsmqInGatewayService;

namespace MsmqGatewayService
{
    class MsmqInGateway
    {
        MessageQueue messageQueue;
        string queueName;
        string serviceCategory;
        string serviceName;
        MsmqInGatewayWSService gateway;
        volatile bool shouldStop = false;
        int currentUrl;

        Thread runner;

        public MsmqInGateway(string queueName, string serviceCategory, string serviceName)
        {
            this.queueName = queueName;
            this.serviceCategory = serviceCategory;
            this.serviceName = serviceName;
            
            messageQueue = new MessageQueue(queueName);
            messageQueue.MessageReadPropertyFilter.ArrivedTime = true;
            messageQueue.MessageReadPropertyFilter.SentTime = true;
            messageQueue.MessageReadPropertyFilter.Priority = true;
            messageQueue.MessageReadPropertyFilter.CorrelationId = true;
            messageQueue.MessageReadPropertyFilter.Id = true;
            messageQueue.MessageReadPropertyFilter.SenderId = true;

            gateway = new MsmqInGatewayWSService();
            currentUrl = 0;
            gateway.Url = Properties.Settings.Default.MsmqInGatewayService_List[currentUrl];
        }

        public void Start()
        {
            if ( runner == null || (runner.ThreadState & (ThreadState.Unstarted | ThreadState.Stopped)) != 0 ) {
                if (messageQueue.Transactional)
                    runner = new Thread(RunTransactional);
                else
                    runner = new Thread(RunNonTransactional);
                runner.Start();
            }
        }

        public void Stop()
        {
            shouldStop = true;

            if ( runner != null && runner.IsAlive && 
                (runner.ThreadState & ( ThreadState.Suspended | ThreadState.SuspendRequested )) == 0 ) {
                runner.Interrupt();
                if (!runner.Join(2000))
                    runner.Abort();
            }
        }

        void RunTransactional()
        {
            MessageQueueTransaction transaction = new MessageQueueTransaction();
            while (!shouldStop)
            {
                try
                {
                    try
                    {
                        transaction.Begin();
                        Message message = messageQueue.Receive(new TimeSpan(0, 0, 1), transaction);

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

                        if (Properties.Settings.Default.LogIncoming)
                        {
                            MsmqGatewayService.Instance.EventLog.WriteEntry("Received message from " + queueName + " " + message.Id);
                        }

                        if (gateway.routeMessage("Navision", "IncomingGateway", msmqMsg))
                        {
                            transaction.Commit();
                        }
                        else
                        {
                            MsmqGatewayService.Instance.EventLog.WriteEntry("Gateway reported error",
                                System.Diagnostics.EventLogEntryType.Warning);
                            transaction.Abort();
                            Thread.Sleep(new TimeSpan(0, 0, 10));
                        }
                    }
                    catch (MessageQueueException e)
                    {
                        if (e.MessageQueueErrorCode != MessageQueueErrorCode.IOTimeout)
                            MsmqGatewayService.Instance.EventLog.WriteEntry("Receive failure on " + queueName + "\r\n ErrorCode:" + e.ErrorCode + "\r\n ErrorMessage: " + e.Message + "\r\n Trace: "  + e.StackTrace,
                                System.Diagnostics.EventLogEntryType.Error);
                    }
                    catch (WebException e)
                    {
                        currentUrl = (currentUrl + 1) % Properties.Settings.Default.MsmqInGatewayService_List.Count;
                        gateway.Url = Properties.Settings.Default.MsmqInGatewayService_List[currentUrl];
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Failed to invoke gateway (retry using " + gateway.Url + "):"  + "\r\n ErrorMessage: " + e.Message + "\r\n Trace: "  + e.StackTrace,
                            System.Diagnostics.EventLogEntryType.Warning);
                        if (e.InnerException != null)
                            MsmqGatewayService.Instance.EventLog.WriteEntry("Caused by:" + "\r\n ErrorMessage: " + e.InnerException.Message + "\r\n Trace: " + e.InnerException.StackTrace,
                                System.Diagnostics.EventLogEntryType.Warning);
                        Thread.Sleep(new TimeSpan(0, 0, 10));
                    }
                    finally
                    {
                        if (transaction.Status == MessageQueueTransactionStatus.Pending)
                        {
                            transaction.Abort();
                        }
                    }
                }
                catch (Exception e)
                {
                    MsmqGatewayService.Instance.EventLog.WriteEntry("Unexpected exception: " + "\r\n ErrorMessage: " + e.Message + "\r\n Trace: " + e.StackTrace,
                        System.Diagnostics.EventLogEntryType.Error);
                    if (e.InnerException != null)
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Caused by:" + "\r\n ErrorMessage: " + e.InnerException.Message + "\r\n Trace: " + e.InnerException.StackTrace,
                            System.Diagnostics.EventLogEntryType.Warning);
                }
            }
        }

        void RunNonTransactional()
        {
            while (!shouldStop)
            {
                try
                {
                    Message message = messageQueue.Peek(new TimeSpan(0, 0, 1));

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

                    if (Properties.Settings.Default.LogIncoming)
                    {
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Received message (non-transactional) from " + queueName + " " + message.Id);
                    }

                    if (gateway.routeMessage("Navision", "IncomingGateway", msmqMsg))
                    {
                        messageQueue.ReceiveById(message.Id);
                    }
                    else
                    {
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Gateway reported error",
                            System.Diagnostics.EventLogEntryType.Warning);
                        Thread.Sleep(new TimeSpan(0, 0, 10));
                    }
                }
                catch (MessageQueueException e)
                {
                    if (e.MessageQueueErrorCode != MessageQueueErrorCode.IOTimeout)
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Receive failure on " + queueName + "\r\n ErrorCode:" + e.ErrorCode + "\r\n ErrorMessage: " + e.Message + "\r\n Trace: " + e.StackTrace,
                            System.Diagnostics.EventLogEntryType.Error);
                }
                catch (WebException e)
                {
                    currentUrl = (currentUrl + 1) % Properties.Settings.Default.MsmqInGatewayService_List.Count;
                    gateway.Url = Properties.Settings.Default.MsmqInGatewayService_List[currentUrl];
                    MsmqGatewayService.Instance.EventLog.WriteEntry("Failed to invoke gateway (retry using " + gateway.Url + "):" + "\r\n ErrorMessage: " + e.Message + "\r\n Trace: " + e.StackTrace,
                        System.Diagnostics.EventLogEntryType.Warning);
                    if ( e.InnerException != null )
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Caused by:" + "\r\n ErrorMessage: " + e.InnerException.Message + "\r\n Trace: " + e.InnerException.StackTrace,
                            System.Diagnostics.EventLogEntryType.Warning);
                    Thread.Sleep(new TimeSpan(0, 0, 10));
                }
                catch (Exception e)
                {
                    MsmqGatewayService.Instance.EventLog.WriteEntry("Unexpected exception: " + "\r\n ErrorMessage: " + e.Message + "\r\n Trace: " + e.StackTrace,
                        System.Diagnostics.EventLogEntryType.Error);
                    if (e.InnerException != null)
                        MsmqGatewayService.Instance.EventLog.WriteEntry("Caused by:" + "\r\n ErrorMessage: " + e.InnerException.Message + "\r\n Trace: " + e.InnerException.StackTrace,
                            System.Diagnostics.EventLogEntryType.Warning);
                }
            }
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
