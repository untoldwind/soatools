package de.objectcode.soatools.logstore.ws.soap.types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "tag-condition")
public class TagCondition {
	String tagName;
	String value;

	@XmlAttribute(name="tag-name", required=true)
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@XmlAttribute(name="value", required=true)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
