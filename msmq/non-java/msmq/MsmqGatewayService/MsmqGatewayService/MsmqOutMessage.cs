using System;
using System.Collections.Generic;
using System.Text;
using System.Runtime.Remoting.Metadata;

namespace MsmqGatewayService
{
    [Serializable]
    [SoapType(XmlNamespace="http://liwest.at/msmq/outgoing")]
    class MsmqOutMessage
    {
        private string body;
        private bool binary;
        private string label;
        private string correlationId;

        public string Body
        {
            get { return body; }
            set { body = value; }
        }

        public bool Binary
        {
            get { return binary; }
            set { binary = value; }
        }

        public string Label
        {
            get { return label; }
            set { label = value; }
        }

        public string CorrelationId
        {
            get { return correlationId; }
            set { correlationId = value; }
        }

        public override string ToString()
        {
            return "MsmqOutMessage(body=" + body + ", binary=" + binary + 
                ", label=" + label + ", correlationId=" + correlationId + ")";
        }
    }
}
