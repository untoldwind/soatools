package de.objectcode.soatools.logstore.ws.rest;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

@Path("/serverinfo")
public class ServerInfoResource {
	private final static Log LOG = LogFactory.getLog(ServerInfoResource.class);

	private final static String[] ATTRIBUTES = { "ActiveThreadCount",
			"ActiveThreadGroupCount", "TotalMemory", "MaxMemory", "FreeMemory" };

	final MBeanServer server;
	final ObjectName serverInfo;

	public ServerInfoResource() {
		try {
			server = (MBeanServer) MBeanServerFactory.findMBeanServer(null)
					.get(0);
			serverInfo = new ObjectName("jboss.system:type=ServerInfo");
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		}
	}

	@GET
	@Produces( { "application/xml", "text/xml" })
	public Document getServerInfo() {
		try {
			AttributeList attributes = server.getAttributes(serverInfo,
					ATTRIBUTES);

			Document document = DocumentFactory.getInstance().createDocument();
			Element serverInfo = document.addElement("server-info");

			for (Object attr : attributes) {
				Attribute attribute = (Attribute) attr;

				serverInfo.addElement(attribute.getName()).addText(
						attribute.getValue().toString());
			}

			return document;
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		}
	}

	@GET
	@Path("/{name}")
	public String getServerInfoAttribute(@PathParam("name") String name) {
		try {
			return server.getAttribute(serverInfo, name).toString();
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		}
	}

}
