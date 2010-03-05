package de.objectcode.soatools.logstore.ws.soap.types;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "log-messages")
public class LogMessageList {
	List<LogMessage> messages;

	@XmlElement(name = "log-message")
	public List<LogMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<LogMessage> messages) {
		this.messages = messages;
	}

}
