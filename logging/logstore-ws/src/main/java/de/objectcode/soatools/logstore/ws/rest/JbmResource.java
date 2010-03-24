package de.objectcode.soatools.logstore.ws.rest;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

@Path("/jbm")
public class JbmResource {
	private final static Log LOG = LogFactory.getLog(JbmResource.class);

	private final static String[] ATTRIBUTES = { "Name", "MessageCount",
			"DeliveringCount", "ConsumerCount", "MessageCounter",
			"MessageStatistics" };

	final MBeanServer server;
	final Map<String, ObjectName> cache = Collections
			.synchronizedMap(new HashMap<String, ObjectName>());

	public JbmResource() {
		server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
	}

	@GET
	@Produces( { "application/xml", "text/xml" })
	public Document getQueueList() {
		Set<ObjectName> names = queryAllQueueServices();

		Document document = DocumentFactory.getInstance().createDocument();
		Element queueList = document.addElement("queue-list");

		for (ObjectName name : names) {
			queueList.addElement("queue").addText(name.getKeyProperty("name"));

			cache.put(name.getKeyProperty("name"), name);
		}

		return document;
	}

	@GET
	@Path("/{name}")
	@Produces( { "application/xml", "text/xml" })
	public Document getQueueStats(@PathParam("name") String name) {
		ObjectName serviceName = cache.get(name);

		if (serviceName == null) {
			Set<ObjectName> names = queryAllQueueServices();

			for (ObjectName objName : names) {
				cache.put(objName.getKeyProperty("name"), objName);
			}
		}

		serviceName = cache.get(name);

		if (serviceName == null) {
			throw new RuntimeException("Not found: " + name);
		}

		try {
			AttributeList attributes = server.getAttributes(serviceName,
					ATTRIBUTES);

			Document document = DocumentFactory.getInstance().createDocument();
			Element queueStats = document.addElement("queue-stats");

			for (Object attr : attributes) {
				Attribute attribute = (Attribute) attr;
				Object value = attribute.getValue();

				if ( value != null) {
					if ( value.getClass().getName().startsWith("java.lang") ) {
						queueStats.addElement(attribute.getName()).addText(
								value.toString());						
					} else {
						Element valueElement = queueStats.addElement(attribute.getName());
						
						for ( PropertyDescriptor property : PropertyUtils.getPropertyDescriptors(value) ) {
							Object propertyValue = PropertyUtils.getProperty(value, property.getName());
							
							if ( propertyValue != null ) {
								valueElement.addElement(property.getName()).addText(propertyValue.toString());
							}
						}
					}
				}
			}

			return document;
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		}
	}

	private Set<ObjectName> queryAllQueueServices() {
		return server.queryNames(null, new QueryExp() {
			private static final long serialVersionUID = 1L;

			MBeanServer current;

			public void setMBeanServer(MBeanServer s) {
				current = s;
			}

			public boolean apply(ObjectName name)
					throws BadStringOperationException,
					BadBinaryOpValueExpException,
					BadAttributeValueExpException, InvalidApplicationException {

				String serviceName = name.getKeyProperty("service");

				if (serviceName != null && "Queue".equals(serviceName)) {
					try {
						MBeanInfo info = current.getMBeanInfo(name);

						for (MBeanAttributeInfo attribute : info
								.getAttributes()) {
							if ("MessageCounter".equals(attribute.getName()))
								return true;
						}
					} catch (Exception e) {
					}
				}
				return false;
			}
		});
	}
}
