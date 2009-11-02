using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.Web.Services.Description;
using System.Messaging;
using System.IO;

namespace MsmqOutGatewayWS
{
    [WebService(Namespace = "http://liwest.at/msmq/outgoing")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [ToolboxItem(false)]
    [SoapRpcService(Use=SoapBindingUse.Literal)]
    public class MsmqOutGateway : System.Web.Services.WebService
    {
        Dictionary<string, MessageQueue> destinations;

        public MsmqOutGateway()
        {
            destinations = new Dictionary<string, MessageQueue>();

            destinations.Add("test", new MessageQueue(".\\private$\\MyQueue"));
        }

        [WebMethod]
        public bool routeMessage(string destination, MsmqOutMessage message)
        {
            MessageQueueTransaction transaction = new MessageQueueTransaction();
            try
            {
                MessageQueue mq = destinations[destination];
                System.Messaging.Message msg = new System.Messaging.Message();

                if (message.Label != null)
                    msg.Label = message.Label;
                if (message.Priority != null)
                    msg.Priority = (MessagePriority)Enum.Parse(typeof(MessagePriority), message.Priority, true);
                if (message.CorrelationId != null)
                    msg.CorrelationId = message.CorrelationId;
                if (message.Body != null)
                    msg.BodyStream = new MemoryStream(message.Body);

                transaction.Begin();
                mq.Send(msg, transaction);
                transaction.Commit();

                return true;
            }
            catch (KeyNotFoundException)
            {
                return false;
            }
            finally
            {
                if (transaction.Status == MessageQueueTransactionStatus.Pending)
                    transaction.Abort();
            }
        }
    }
}
