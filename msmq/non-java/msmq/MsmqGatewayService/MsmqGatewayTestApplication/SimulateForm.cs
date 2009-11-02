using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Messaging;
using System.Windows.Forms;
using System.IO;

namespace MsmqGatewayTestApplication
{
    public partial class SimulateForm : Form
    {
        MessageQueue mq;

        public SimulateForm(MessageQueue mq)
        {
            this.mq = mq;

            InitializeComponent();
        }

        private void updatePreview()
        {
            previewTextBox.Text = createMessage(100000L);
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            updatePreview();
        }

        private string createMessage(long start)
        {
            int count = (int)groupNumeric.Value;
            StringBuilder sb = new StringBuilder();

            sb.AppendLine("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
            sb.AppendLine("<message_type name=\"order\">");

            switch (comboBox1.SelectedIndex)
            {
                case 0:
                    for (int i = 0; i < count; i++)
                    {
                        sb.AppendLine("  <service name=\"digitv\">");
                        sb.AppendLine("    <action name=\"activate\">");
                        sb.AppendLine("      <data>");
                        sb.AppendLine("        <country>AT</country>");
                        sb.AppendLine("        <order_no>" + (start + i) + "</order_no>");
                        sb.AppendLine("        <product_id>101</product_id>");
                        sb.AppendLine("        <subscription_start>01.08.2008 00:00:00</subscription_start>");
                        sb.AppendLine("        <subscription_end>31.08.2008 23:59:59</subscription_end>");
                        sb.AppendLine("        <priority>EMM</priority>");
                        sb.AppendLine("        <nofsmartcards>1</nofsmartcards>");
                        sb.AppendLine("        <smartcard_list>01359012477</smartcard_list>");
                        sb.AppendLine("      </data>");
                        sb.AppendLine("    </action>");
                        sb.AppendLine("  </service>");
                    }
                    break;
            }

            sb.AppendLine("</message_type>");

            return sb.ToString();
        }

        private void countNumeric_ValueChanged(object sender, EventArgs e)
        {
            updatePreview();
        }

        private void groupNumeric_ValueChanged(object sender, EventArgs e)
        {
            updatePreview();
        }

        private void btnSend_Click(object sender, EventArgs e)
        {
            if (this.mq != null)
            {
                long count = (int)countNumeric.Value + 100000L;
                int groupCount = (int)groupNumeric.Value;

                for (long start = 100000L; start < count; start += groupCount)
                {
                    System.Messaging.Message msg = new System.Messaging.Message();
                    msg.Label = "Navision MSMQ";
                    msg.BodyStream = new MemoryStream(System.Text.Encoding.UTF8.GetBytes(createMessage(start)));
                    mq.Send(msg);
                }
            }
            else
            {
                MessageBox.Show("Sie müssen zuerst eine Queue auswählen.",
                   "Warnung!", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }

            Close();
        }
    }
}
