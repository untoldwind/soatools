package de.objectcode.soatools.util.healthcheck.check;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlType(name="service")
public class Service {
	String name;
	String templateName;
	Map<QName, String> parameters;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "template")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@XmlAnyAttribute
	public Map<QName, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<QName, String> parameters) {
		this.parameters = parameters;
	}

	@XmlTransient
	public Map<String, String > getParams() {
		Map<String, String > params = new HashMap<String, String>();
		
		for ( Map.Entry<QName, String> entry : parameters.entrySet()) 
			params.put(entry.getKey().getLocalPart(), entry.getValue());
		
		return params;
	}
}
