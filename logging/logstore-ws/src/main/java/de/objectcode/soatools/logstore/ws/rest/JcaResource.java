package de.objectcode.soatools.logstore.ws.rest;

import java.util.Hashtable;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

@Path("/jca")
public class JcaResource {
	private final static Log LOG = LogFactory.getLog(JcaResource.class);

	private final static String[] ATTRIBUTES = { "PoolJndiName", "MinSize", "MaxSize",
			"ConnectionCount", "MaxConnectionsInUseCount",
			"InUseConnectionCount", "ConnectionCreatedCount",
			"ConnectionDestroyedCount" };

	final MBeanServer server;

	public JcaResource() {
		server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
	}

	@GET
	@Produces( { "application/xml", "text/xml" })
	public Document getPoolList() {
		Set<ObjectName> names = queryAllPoolServices();

		Document document = DocumentFactory.getInstance().createDocument();
		Element queueList = document.addElement("connection-pools");

		for (ObjectName name : names) {
			queueList.addElement("connection-pool").addText(
					name.getKeyProperty("name"));
		}

		return document;
	}

	@GET
	@Path("/{name}")
	@Produces( { "application/xml", "text/xml" })
	public Document getPoolStats(@PathParam("name") String name) {
		Hashtable<String, String> table = new Hashtable<String, String>();

		table.put("name", name);
		table.put("service", "ManagedConnectionPool");

		try {
			ObjectName serviceName = new ObjectName("jboss.jca", table);

			AttributeList attributes = server.getAttributes(serviceName,
					ATTRIBUTES);

			Document document = DocumentFactory.getInstance().createDocument();
			Element poolStats = document.addElement("connection-pool");

			for (Object attr : attributes) {
				Attribute attribute = (Attribute) attr;
				Object value = attribute.getValue();

				if (value != null)
					poolStats.addElement(attribute.getName()).addText(value.toString());
			}

			return document;
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		}

	}

	private Set<ObjectName> queryAllPoolServices() {
		return server.queryNames(null, new QueryExp() {
			private static final long serialVersionUID = 1L;

			public void setMBeanServer(MBeanServer s) {
			}

			public boolean apply(ObjectName name)
					throws BadStringOperationException,
					BadBinaryOpValueExpException,
					BadAttributeValueExpException, InvalidApplicationException {

				String domain = name.getDomain();
				String service = name.getKeyProperty("service");

				return "jboss.jca".equals(domain)
						&& "ManagedConnectionPool".equals(service);
			}
		});
	}

}
