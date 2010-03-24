package de.objectcode.soatools.logstore.ws.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.BadAttributeValueExpException;
import javax.management.BadBinaryOpValueExpException;
import javax.management.BadStringOperationException;
import javax.management.InvalidApplicationException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
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

@Path("/esb")
public class EsbResource {
	private final static Log LOG = LogFactory.getLog(EsbResource.class);

	final MBeanServer server;
	final Map<String, ObjectName> cache = Collections
			.synchronizedMap(new HashMap<String, ObjectName>());

	public EsbResource() {
		server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
	}

	@GET
	@Produces( { "application/xml", "text/xml" })
	public Document getMessageCounters() {
		Document document = DocumentFactory.getInstance().createDocument();
		Element esbServices = document.addElement("esb-services");

		for (ObjectName name : queryAllMessageCounterServices()) {
			Element esbService = esbServices.addElement("esb-service");

			esbService.addElement("category").addText(
					name.getKeyProperty("service-category"));
			esbService.addElement("name").addText(
					name.getKeyProperty("service-name"));

			cache.put(name.getKeyProperty("service-category") + "/"
					+ name.getKeyProperty("service-name"), name);
		}

		return document;
	}

	@GET
	@Path("/{category}/{name}")
	@Produces( { "application/xml", "text/xml" })
	public Document getMessageCounter(
			@PathParam("category") String serviceCategory,
			@PathParam("name") String serviceName) {
		Document document = DocumentFactory.getInstance().createDocument();
		Element esbService = document.addElement("esb-service");
		Element overall = esbService.addElement("overall");
		Element counters = esbService.addElement("counters");

		ObjectName counterName = cache.get(serviceCategory + "/" + serviceName);

		if (counterName == null) {
			for (ObjectName name : queryAllMessageCounterServices()) {
				cache.put(name.getKeyProperty("service-category") + "/"
						+ name.getKeyProperty("service-name"), name);
			}
		}

		counterName = cache.get(serviceCategory + "/" + serviceName);

		if (counterName == null)
			throw new RuntimeException("No service: " + serviceCategory + "/"
					+ serviceName);

		try {
			Map<String, Object> attributes = new HashMap<String, Object>();
			MBeanInfo mbeanInfo = server.getMBeanInfo(counterName);

			for (MBeanAttributeInfo attribute : mbeanInfo.getAttributes()) {
				Object value = server.getAttribute(counterName, attribute
						.getName());

				attributes.put(attribute.getName(), value);

				counters.addElement(attribute.getName().replace(' ', '_'))
						.addText(value != null ? value.toString() : "0");
			}

			int messageCount = 0;
			int failureCount = 0;
			long byteCount = 0;
			long processedBytes = 0;
			long processedTime = 0;

			if (attributes.get("overall service message count") != null)
				messageCount = (Integer) attributes
						.get("overall service message count");
			if (attributes.get("overall service count bytes") != null)
				byteCount = (Long) attributes
						.get("overall service count bytes");
			if (attributes.get("overall processedbytes") != null)
				processedBytes = (Long) attributes
						.get("overall processedbytes");
			if (attributes.get("overall service time processed") != null)
				processedTime = (Long) attributes
						.get("overall service time processed");

			for (Map.Entry<String, Object> entry : attributes.entrySet()) {
				if (entry.getValue() != null
						&& entry.getKey().endsWith("messages failed count")) {
					failureCount += (Integer) entry.getValue();
				}
			}

			
			overall.addElement("MessageCount").addText(
					String.valueOf(messageCount));
			overall.addElement("FailureCount").addText(
					String.valueOf(failureCount));
			overall.addElement("ByteCount").addText(
					String.valueOf(byteCount));
			overall.addElement("ProcessedBytes").addText(
					String.valueOf(processedBytes));
			overall.addElement("ProcessedTime").addText(
					String.valueOf(processedTime));
		} catch (Exception e) {
			LOG.error("Exception", e);
		}

		return document;

	}

	private Set<ObjectName> queryAllMessageCounterServices() {
		return server.queryNames(null, new QueryExp() {
			private static final long serialVersionUID = 1L;

			public void setMBeanServer(MBeanServer s) {
			}

			public boolean apply(ObjectName name)
					throws BadStringOperationException,
					BadBinaryOpValueExpException,
					BadAttributeValueExpException, InvalidApplicationException {

				String domain = name.getDomain();
				String category = name.getKeyProperty("category");

				return "jboss.esb".equals(domain)
						&& "MessageCounter".equals(category);
			}
		});
	}

}
