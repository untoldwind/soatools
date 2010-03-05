package de.objectcode.soatools.logstore.ws.soap.types;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "log-message")
public class LogMessage {
	long id;
	String state;
	Date logEnterTimestamp;
	Date logLeaveTimestamp;
	String serviceCategory;
	String serviceName;
	String messageId;
	String correlationId;
	String messageTo;
	String messageFrom;
	String messageReplyTo;
	String messageFaultTo;
	String messageType;
	Long jbpmProcessInstanceId;
	Long jbpmTokenId;
	Long jbpmNodeId;

	@XmlAttribute(name = "log-id", required = true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlAttribute(name = "state", required = true)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@XmlAttribute(name = "enter-timestamp", required = true)
	public Date getLogEnterTimestamp() {
		return logEnterTimestamp;
	}

	public void setLogEnterTimestamp(Date logEnterTimestamp) {
		this.logEnterTimestamp = logEnterTimestamp;
	}

	@XmlAttribute(name = "leave-timestamp", required = true)
	public Date getLogLeaveTimestamp() {
		return logLeaveTimestamp;
	}

	public void setLogLeaveTimestamp(Date logLeaveTimestamp) {
		this.logLeaveTimestamp = logLeaveTimestamp;
	}

	@XmlAttribute(name = "service-category", required = true)
	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	@XmlAttribute(name = "service-name", required = true)
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@XmlElement(name="message-id")
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@XmlElement(name="correlation-id")
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@XmlElement(name="message-to")
	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	@XmlElement(name="message-from")
	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	@XmlElement(name="message-reply-to")
	public String getMessageReplyTo() {
		return messageReplyTo;
	}

	public void setMessageReplyTo(String messageReplyTo) {
		this.messageReplyTo = messageReplyTo;
	}

	@XmlElement(name="message-fault-to")
	public String getMessageFaultTo() {
		return messageFaultTo;
	}

	public void setMessageFaultTo(String messageFaultTo) {
		this.messageFaultTo = messageFaultTo;
	}

	@XmlElement(name="message-type")
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@XmlElement(name="jbpm-process-instance-id")
	public Long getJbpmProcessInstanceId() {
		return jbpmProcessInstanceId;
	}

	public void setJbpmProcessInstanceId(Long jbpmProcessInstanceId) {
		this.jbpmProcessInstanceId = jbpmProcessInstanceId;
	}

	@XmlElement(name="jbpm-token-id")
	public Long getJbpmTokenId() {
		return jbpmTokenId;
	}

	public void setJbpmTokenId(Long jbpmTokenId) {
		this.jbpmTokenId = jbpmTokenId;
	}

	@XmlElement(name="jbpm-node-id")
	public Long getJbpmNodeId() {
		return jbpmNodeId;
	}

	public void setJbpmNodeId(Long jbpmNodeId) {
		this.jbpmNodeId = jbpmNodeId;
	}
}
