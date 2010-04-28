package de.objectcode.soatools.util.healthcheck.check;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.mvel2.MVEL;

@XmlType(name = "mbean-data-provider")
public class MBeanDataProvider extends DataProvider {
	String objectNameExpr;
	Serializable objectNameCompiled;
	List<String> attributes;
	MBeanServer server;

	public MBeanDataProvider() {
		if (!MBeanServerFactory.findMBeanServer(null).isEmpty())
			server = MBeanServerFactory.findMBeanServer(null).get(0);
	}

	@XmlElement(name = "object-name")
	public String getObjectNameExpr() {
		return objectNameExpr;
	}

	public void setObjectNameExpr(String objectNameExpr) {
		this.objectNameExpr = objectNameExpr;
		
		objectNameCompiled = MVEL.compileExpression(objectNameExpr);
	}

	@XmlElement(name = "attribute")
	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> retrieveData(Service service) throws Exception {
		ObjectName objectName = new ObjectName(MVEL.executeExpression(objectNameCompiled,
				service, String.class));

		AttributeList attributeList = server.getAttributes(objectName,
				attributes.toArray(new String[attributes.size()]));

		Map<String, Object> values = new HashMap<String, Object>();

		for (Attribute attribute : attributeList.asList()) {
			String name = attribute.getName();
			Object value = attribute.getValue();

			if (value instanceof CompositeData) {
				CompositeData compositeData = (CompositeData) value;
				CompositeType compositeType = compositeData.getCompositeType();

				for (String itemName : compositeType.keySet()) {
					values.put(name + "_" + itemName, compositeData
							.get(itemName));
				}
			} else {
				values.put(name, value);
			}
		}

		return values;
	}
}
