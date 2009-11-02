using System;
using System.Collections.Generic;
using System.Text;
using System.Messaging;
using System.Threading;
using System.ComponentModel;

namespace MsmqTestLoopback
{
    class MessageListener
    {
        public delegate void InvokeAddMessage(string queueName, System.Messaging.Message message);

        MessageQueue requestQueue;
        string requestQueueName;
        MessageQueue responseQueue;
        ISynchronizeInvoke synchronizedInvoke;
        InvokeAddMessage addMessage;

        volatile bool shouldStop = false;

        Thread runner;

        public MessageListener(string requestQueueName, string responseQueueName,
            ISynchronizeInvoke synchronizedInvoke, InvokeAddMessage addMessage)
        {
            this.synchronizedInvoke = synchronizedInvoke;
            this.addMessage = addMessage;
            this.requestQueueName = requestQueueName;
            this.requestQueue = new MessageQueue(requestQueueName);
            this.responseQueue = new MessageQueue(responseQueueName);

            requestQueue.MessageReadPropertyFilter.ArrivedTime = true;
            requestQueue.MessageReadPropertyFilter.SentTime = true;
            requestQueue.MessageReadPropertyFilter.Priority = true;
            requestQueue.MessageReadPropertyFilter.CorrelationId = true;
            requestQueue.MessageReadPropertyFilter.Id = true;
            requestQueue.MessageReadPropertyFilter.SenderId = true;
        }

        public void Start()
        {
            if ( runner == null || (runner.ThreadState & (ThreadState.Unstarted | ThreadState.Stopped)) != 0 ) {
                runner = new Thread(Run);
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

        void Run()
        {
            MessageQueueTransaction transaction = new MessageQueueTransaction();
            while (!shouldStop)
            {
                try
                {
                    transaction.Begin();
                    Message message = requestQueue.Receive(new TimeSpan(0, 0, 1), transaction);

                    responseQueue.Send(message, transaction);
                    transaction.Commit();

                    synchronizedInvoke.BeginInvoke(addMessage, new object[] { requestQueueName, message });
                }
                catch (MessageQueueException e)
                {
                    if (e.MessageQueueErrorCode != MessageQueueErrorCode.IOTimeout)
                        Console.WriteLine("Receive failure on " + requestQueueName + " " + e.StackTrace,
                            System.Diagnostics.EventLogEntryType.Error);
                }
                finally
                {
                    if (transaction.Status == MessageQueueTransactionStatus.Pending)
                    {
                        transaction.Abort();
                    }
                }
            }
        }

    }
}
