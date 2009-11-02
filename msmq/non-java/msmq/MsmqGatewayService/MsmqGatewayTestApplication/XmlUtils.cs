using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.IO;

namespace MsmqGatewayTestApplication
{
    class XmlUtils
    {
        public static string FormatXml(byte[] data)
        {
            try
            {
                XmlDocument document = new XmlDocument();
                document.Load(new MemoryStream(data));
                StringWriter output = new StringWriter();
                XmlTextWriter writer = new XmlTextWriter(output);

                writer.Formatting = Formatting.Indented;

                document.WriteTo(writer);
                writer.Flush();
                output.Flush();

                return output.ToString();
            }
            catch (XmlException)
            {
            }

            return "";
        }
    }
}
