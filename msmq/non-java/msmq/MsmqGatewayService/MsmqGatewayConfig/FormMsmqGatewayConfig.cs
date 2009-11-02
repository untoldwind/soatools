using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Xml;
using System.Windows.Forms;


namespace MsmqGatewayConfig
{
    public partial class FormMsmqGatewayConfig : Form
    {
        private String configSourcePath = ".\\MsmqGatewayService.exe.config";
        XmlDocument doc;
        char[] trennzeichenInOut = new char[]{ '|' };
        char[] trennzeichenIncomming = new char[] { '.' };

        public FormMsmqGatewayConfig()
        {
            InitializeComponent();
            doc = new XmlDocument();
            doc.Load(this.configSourcePath);
        }

        /**
         * Setzen der stats 
         */
        private void FormMsmqGatewayConfig_Load(object sender, EventArgs e)
        {
            XmlNodeList nodeList = doc.GetElementsByTagName("setting");

            XmlNode node = nodeList.Item(0);
            XmlElement element = (XmlElement)node;
            this.outgoingPort.Text = element.GetElementsByTagName("value")[0].InnerText;

            node = nodeList.Item(1);
            element = (XmlElement)node;
            for (int i = 0; i < element.GetElementsByTagName("string").Count; i++)
            {
                String outgoing = element.GetElementsByTagName("string")[i].InnerText;
                String[] request = outgoing.Split(trennzeichenInOut, 2);
                outgoingDestinations.Items.Add(new ListViewItem(request));                
            }
            
            node = nodeList.Item(2);
            element = (XmlElement)node;
            for (int i = 0; i < element.GetElementsByTagName("string").Count; i++)
            {
                String incomming = element.GetElementsByTagName("string")[i].InnerText;
                String[] response = incomming.Split(trennzeichenInOut, 2);
                String[] responseSplit = response[1].Split(trennzeichenIncomming, 2);
                String[] result = new String[] { response[0], responseSplit[0], responseSplit[1] };
                incommingDestinations.Items.Add(new ListViewItem(result));
            }
            
            node = nodeList.Item(3);
            element = (XmlElement)node;
            String logOut = element.GetElementsByTagName("value")[0].InnerText;
            this.logOutgoing.Checked = (logOut.ToString().Equals("False")) ? false : true;

            node = nodeList.Item(4);
            element = (XmlElement)node;
            String logIn = element.GetElementsByTagName("value")[0].InnerText;
            this.logIncoming.Checked = (logIn.ToString().Equals("False")) ? false : true;

            node = nodeList.Item(5);
            element = (XmlElement)node;
            this.wsService.Text = element.GetElementsByTagName("value")[0].InnerText;

            node = nodeList.Item(6);
            element = (XmlElement)node;
            for (int i = 0; i < element.GetElementsByTagName("string").Count; i++)
            {
                String url = element.GetElementsByTagName("string")[i].InnerText;
                wsServiceList.Items.Add(url);
            }
        }

        private void logOutgoing_CheckedChanged(object sender, EventArgs e)
        {
            setNewValue(this.logOutgoing.Checked.ToString(), 3);         
        }

        private void schliessen_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void speichern_Click(object sender, EventArgs e)
        {
            DialogResult result = MessageBox.Show("Wollen Sie wirklich Speichern?", 
                    "Warnung!", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);
            if (result == System.Windows.Forms.DialogResult.Yes)
            {
                doc.Save(this.configSourcePath);
                Console.WriteLine("Safe");
            }           
        }

        private void logIncoming_CheckedChanged(object sender, EventArgs e)
        {
            setNewValue(this.logIncoming.Checked.ToString(), 4);
        }

        private void setNewValue(String value, int xmlPosition)
        {
            XmlNodeList nodeList = doc.GetElementsByTagName("setting");
            XmlNode node = nodeList.Item(xmlPosition);
            XmlElement element = (XmlElement)node;
            element.GetElementsByTagName("value")[0].InnerText = value;   
        }

        private void wsService_TextChanged(object sender, EventArgs e)
        {
            setNewValue(this.wsService.Text, 5);
        }

        private void outgoingPort_TextChanged(object sender, EventArgs e)
        {
            setNewValue(this.outgoingPort.Text, 0);
        }
        
        private void outgoingDestinations_SelectedIndexChanged(object sender, EventArgs e)
        {
            removeButtonOut.Enabled = outgoingDestinations.SelectedIndices.Count > 0;
        }

        private void incommingDestinations_SelectedIndexChanged(object sender, EventArgs e)
        {
            removeButtonIn.Enabled = incommingDestinations.SelectedIndices.Count > 0;
        }

        private void removeButtonIn_Click(object sender, EventArgs e)
        {
            if (incommingDestinations.SelectedIndices.Count > 0)
            {
                int index = incommingDestinations.SelectedIndices[0];

                incommingDestinations.Items.RemoveAt(index);

                XmlNodeList nodeList = doc.GetElementsByTagName("setting");

                XmlElement element = (XmlElement)nodeList.Item(2);
                XmlNode node = element.GetElementsByTagName("string").Item(index);

                node.ParentNode.RemoveChild(node);
            }
        }

        private void removeButtonOut_Click(object sender, EventArgs e)
        {
            if (outgoingDestinations.SelectedIndices.Count > 0)
            {
                int index = outgoingDestinations.SelectedIndices[0];

                outgoingDestinations.Items.RemoveAt(index);

                XmlNodeList nodeList = doc.GetElementsByTagName("setting");

                XmlElement element = (XmlElement)nodeList.Item(1);
                XmlNode node = element.GetElementsByTagName("string").Item(index);

                node.ParentNode.RemoveChild(node);
            }
        }

        private void addButtonOut_Click(object sender, EventArgs e)
        {
            OutgoingDestinationForm dialog = new OutgoingDestinationForm();

            if (dialog.ShowDialog(this) == DialogResult.OK)
            {
                XmlNodeList nodeList = doc.GetElementsByTagName("setting");

                XmlElement element = (XmlElement)nodeList.Item(1);
                XmlElement arrayElement = (XmlElement)element.GetElementsByTagName("ArrayOfString").Item(0);
                XmlElement stringElement = doc.CreateElement("string");
                arrayElement.AppendChild(stringElement);
                stringElement.AppendChild(doc.CreateTextNode(dialog.DestinationName + "|" + dialog.DestinationQueue));

                String[] request = new String[] { dialog.DestinationName, dialog.DestinationQueue };
                outgoingDestinations.Items.Add(new ListViewItem(request));                
            }
        }

        private void addButtonIn_Click(object sender, EventArgs e)
        {
            IncomingDestinationForm dialog = new IncomingDestinationForm();

            if (dialog.ShowDialog(this) == DialogResult.OK)
            {
                XmlNodeList nodeList = doc.GetElementsByTagName("setting");

                XmlElement element = (XmlElement)nodeList.Item(2);
                XmlElement arrayElement = (XmlElement)element.GetElementsByTagName("ArrayOfString").Item(0);
                XmlElement stringElement = doc.CreateElement("string");
                arrayElement.AppendChild(stringElement);
                stringElement.AppendChild(doc.CreateTextNode(dialog.DestinationQueue + "|" + dialog.DestinationCategory + "." + dialog.DestinationName));

                String[] request = new String[] { dialog.DestinationQueue, dialog.DestinationCategory, dialog.DestinationName  };
                incommingDestinations.Items.Add(new ListViewItem(request));                
            }
        }

        private void wsServiceList_SelectedIndexChanged(object sender, EventArgs e)
        {
            int idx = wsServiceList.SelectedIndex;

            if (idx >= 0 && idx < wsServiceList.Items.Count)
            {
                removeButtonWS.Enabled = true;
                wsService.Text = (string)wsServiceList.Items[idx];
            }
            else
                removeButtonWS.Enabled = false;
        }

        private void addButtonWS_Click(object sender, EventArgs e)
        {
            wsServiceList.Items.Add(wsService.Text);
            XmlNodeList nodeList = doc.GetElementsByTagName("setting");

            XmlElement element = (XmlElement)nodeList.Item(6);
            XmlElement arrayElement = (XmlElement)element.GetElementsByTagName("ArrayOfString").Item(0);
            XmlElement stringElement = doc.CreateElement("string");
            arrayElement.AppendChild(stringElement);
            stringElement.AppendChild(doc.CreateTextNode(wsService.Text));
        }

        private void removeButtonWS_Click(object sender, EventArgs e)
        {
            int idx = wsServiceList.SelectedIndex;

            if (idx >= 0 && idx < wsServiceList.Items.Count)
            {
                wsServiceList.Items.RemoveAt(idx);

                XmlNodeList nodeList = doc.GetElementsByTagName("setting");

                XmlElement element = (XmlElement)nodeList.Item(6);
                XmlNode node = element.GetElementsByTagName("string").Item(idx);

                node.ParentNode.RemoveChild(node);
            }
        }
    }
}