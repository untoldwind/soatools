using System;
using System.Data;
using System.Configuration;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;

namespace MsmqOutGatewayWS
{
    public class MsmqOutMessage
    {
        private byte[] bodyField;
        private string correlationIdField;
        private string labelField;
        private string priorityField;

        public byte[] Body
        {
            get { return bodyField; }
            set { bodyField = value; }
        }

        public string CorrelationId
        {
            get { return correlationIdField; }
            set { correlationIdField = value; }
        }

        public string Label
        {
            get { return labelField; }
            set { labelField = value; }
        }

        public string Priority
        {
            get { return priorityField; }
            set { priorityField = value; }
        }

    }
}
