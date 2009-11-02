using System;
using System.Collections.Generic;
using System.Text;
using System.Messaging;
using System.IO;

namespace MsmqGatewayMultiTestApplication
{
    class ReceivedMessage
    {
        private string messageId;
        private byte[] body;
        private MessagePriority mp;
        private string label;

        public ReceivedMessage(Message message)
        {
            this.messageId = message.Id;
            this.body = readFully(message.BodyStream);
            this.mp = message.Priority;
        }

        public string MessageId
        {
            get { return messageId; }
        }

        public string Label
        {
            get { return label; }
        }

        public byte[] Body
        {
            get { return body; }
        }

        public MessagePriority Priority
        {
            get { return mp; }
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
