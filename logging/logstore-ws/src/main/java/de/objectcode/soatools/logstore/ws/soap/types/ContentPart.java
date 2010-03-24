package de.objectcode.soatools.logstore.ws.soap.types;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Source;

@XmlType(name = "contant-part")
public class ContentPart {
	String name;
	Source xmlContent;
	DataHandler binaryContent;

	@XmlAttribute(name = "name", required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "xml-content", required = false)
	@XmlMimeType("text/xml")
	public Source getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(Source xmlContent) {
		this.xmlContent = xmlContent;
	}

	@XmlElement(name = "binary-content", required = false)
	@XmlMimeType("text/plain")
	public DataHandler getBinaryContent() {
		return binaryContent;
	}

	public void setBinaryContent(DataHandler binaryContent) {
		this.binaryContent = binaryContent;
	}

}
