package de.objectcode.soatools.logstore.ws.soap.types;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "tag-names")
public class TagList {
	List<String> tagNames;

	public TagList() {

	}

	public TagList(List<String> tagNames) {
		this.tagNames = tagNames;
	}

	@XmlElement(name = "tag-name")
	public List<String> getTagNames() {
		return tagNames;
	}

	public void setTagNames(List<String> tagNames) {
		this.tagNames = tagNames;
	}
}
