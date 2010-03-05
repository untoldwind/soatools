package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;
import java.util.Date;

import de.objectcode.soatools.logstore.persistent.LogMessage;

public class LogMessageBean implements Serializable {
	private static final long serialVersionUID = -6079863056193545028L;

	final long id;
	final String state;
	final String serviceCategory;
	final String serviceName;
	final Date logEnterTimestamp;
	final Date logLeaveTimestamp;
	final String messageId;
	final String correlationId;
	final String messageTo;
	final String messageFrom;
	final String messageReplyTo;
	final String messageFaultTo;
	final String messageType;
	final Long jbpmProcessInstanceId;
	final Long jbpmTokenId;
	final Long jbpmNodeId;

	public LogMessageBean(LogMessage logMessage) {
		id = logMessage.getId();
		state = logMessage.getState();
		serviceCategory = logMessage.getServiceCategory();
		serviceName = logMessage.getServiceName();
		logEnterTimestamp = logMessage.getLogEnterTimestamp();
		logLeaveTimestamp = logMessage.getLogLeaveTimestamp();
		messageId = logMessage.getMessageId();
		correlationId = logMessage.getCorrelationId();
		messageTo = logMessage.getMessageTo();
		messageFrom = logMessage.getMessageFrom();
		messageReplyTo = logMessage.getMessageReplyTo();
		messageFaultTo = logMessage.getMessageFaultTo();
		messageType = logMessage.getMessageType();
		jbpmProcessInstanceId = logMessage.getJbpmProcessInstanceId();
		jbpmTokenId = logMessage.getJbpmTokenId();
		jbpmNodeId = logMessage.getJbpmNodeId();
	}

	public long getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getMessageId() {
		return messageId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public String getMessageTo() {
		return messageTo;
	}

	public String getMessageFrom() {
		return messageFrom;
	}

	public String getMessageReplyTo() {
		return messageReplyTo;
	}

	public String getMessageFaultTo() {
		return messageFaultTo;
	}

	public String getMessageType() {
		return messageType;
	}

	public Long getJbpmProcessInstanceId() {
		return jbpmProcessInstanceId;
	}

	public Long getJbpmTokenId() {
		return jbpmTokenId;
	}

	public Long getJbpmNodeId() {
		return jbpmNodeId;
	}

	public boolean isHasProcessInformation() {
		return jbpmProcessInstanceId != null && jbpmNodeId != null;
	}

	public Date getLogEnterTimestamp() {
		return logEnterTimestamp;
	}

	public Date getLogLeaveTimestamp() {
		return logLeaveTimestamp;
	}

}
