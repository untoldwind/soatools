package de.objectcode.soatools.logstore.ws.soap.types;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="log-message-detail")
public class LogMessageDetail extends LogMessage {
	List<NamedValue> propertiesContents;
	List<NamedValue> contextContents;
	List<ContentPart> bodyContents;
	List<ContentPart> attachmentContents;
	
	@XmlElement(name="properties-content")
	public List<NamedValue> getPropertiesContents() {
		return propertiesContents;
	}

	public void setPropertiesContents(List<NamedValue> propertiesContents) {
		this.propertiesContents = propertiesContents;
	}

	@XmlElement(name="context-content")
	public List<NamedValue> getContextContents() {
		return contextContents;
	}

	public void setContextContents(List<NamedValue> contextContents) {
		this.contextContents = contextContents;
	}

	@XmlElement(name="attachment-content")
	public List<ContentPart> getAttachmentContents() {
		return attachmentContents;
	}

	public void setAttachmentContents(List<ContentPart> attachmentContents) {
		this.attachmentContents = attachmentContents;
	}

	@XmlElement(name="body-content")
	public List<ContentPart> getBodyContents() {
		return bodyContents;
	}

	public void setBodyContents(List<ContentPart> bodyContents) {
		this.bodyContents = bodyContents;
	}
	
}
