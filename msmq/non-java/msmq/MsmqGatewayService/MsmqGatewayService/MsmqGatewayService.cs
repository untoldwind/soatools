using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.ServiceProcess;
using System.Text;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Channels;
using System.Runtime.Remoting.Channels.Http;
using System.Messaging;

namespace MsmqGatewayService
{
    public partial class MsmqGatewayService : ServiceBase
    {
        private static MsmqGatewayService instance = new MsmqGatewayService();

        private HttpChannel channel;
        private List<MsmqInGateway> incomingListeners;

        public MsmqGatewayService()
        {
            InitializeComponent();
        }

        public static MsmqGatewayService Instance
        {
            get
            {
                return instance;
            }
        }

        protected override void OnStart(string[] args)
        {
            IDictionary properties = new Hashtable();
            channel = new HttpChannel(Properties.Settings.Default.Port);
            ChannelServices.RegisterChannel(channel, false);

            RemotingConfiguration.CustomErrorsMode = CustomErrorsModes.Off;
            RemotingConfiguration.RegisterWellKnownServiceType(typeof(MsmqOutGateway), "gateway", WellKnownObjectMode.Singleton);
            SoapServices.RegisterInteropXmlType("MsmqOutMessage", "http://liwest.at/msmq/outgoing", typeof(MsmqOutMessage));

            incomingListeners = new List<MsmqInGateway>();
            StringBuilder builder = new StringBuilder();
            foreach (string destination in Properties.Settings.Default.IncomingDestinations)
            {
                string[] keyValue = destination.Trim().Split('|');
                if (keyValue.Length == 2)
                {
                    string[] serviceSpec = keyValue[1].Split('.');

                    if (serviceSpec.Length != 2)
                        continue;

                    if (MessageQueue.Exists(keyValue[0]))
                    {
                        MsmqInGateway incomingListener = new MsmqInGateway(keyValue[0], serviceSpec[0], serviceSpec[1]);
                        incomingListener.Start();
                        incomingListeners.Add(incomingListener);
                        builder.AppendLine();
                        builder.Append("'").Append(keyValue[0]).Append("' maps to '").Append(serviceSpec[0]).Append(" ").Append(serviceSpec[1]).Append("'");
                    }
                    else
                    {
                        EventLog.WriteEntry("Unknown queue " + keyValue[0],
                            System.Diagnostics.EventLogEntryType.Error);
                    }
                }
            }
            MsmqGatewayService.Instance.EventLog.WriteEntry("MsmqInGateways initialized: " + builder);
        }

        protected override void OnStop()
        {
            RequestAdditionalTime(2000 * incomingListeners.Count);

            foreach (MsmqInGateway incomingListener in incomingListeners)
            {
                incomingListener.Stop();
            }

            if (channel != null)
                ChannelServices.UnregisterChannel(channel);
        }
    }
}
