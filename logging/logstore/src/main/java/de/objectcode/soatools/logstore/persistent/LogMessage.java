package de.objectcode.soatools.logstore.persistent;

import java.sql.Clob;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "MLOG_MESSAGE")
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
	Clob content;
	Long jbpmProcessInstanceId;
	Long jbpmTokenId;
	Long jbpmNodeId;
	String faultReason;
	Clob faultCause;
	Map<String, LogTag> tags;

	@Id
	@GeneratedValue(generator = "SEQ_MLOG_MESSAGE_ID")
	@GenericGenerator(name = "SEQ_MLOG_MESSAGE_ID", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MLOG_MESSAGE_ID"))
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "MESSAGE_ID")
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Column(name = "CORRELATION_ID")
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	@Column(name = "MESSAGE_TO")
	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	@Column(name = "MESSAGE_FROM")
	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

	@Column(name = "MESSAGE_REPLYTO")
	public String getMessageReplyTo() {
		return messageReplyTo;
	}

	public void setMessageReplyTo(String messageReplyTo) {
		this.messageReplyTo = messageReplyTo;
	}

	@Column(name = "MESSAGE_FAULTTO")
	public String getMessageFaultTo() {
		return messageFaultTo;
	}

	public void setMessageFaultTo(String messageFaultTo) {
		this.messageFaultTo = messageFaultTo;
	}

	@Column(name = "MESSAGE_TYPE")
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Column(name = "SERVICE_CATEGORY")
	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	@Column(name = "SERVICE_NAME")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Column(name = "CONTENT")
	public Clob getContent() {
		return content;
	}

	public void setContent(Clob content) {
		this.content = content;
	}

	@Column(name = "JBPM_PROCESSINSTANCE_ID")
	public Long getJbpmProcessInstanceId() {
		return jbpmProcessInstanceId;
	}

	public void setJbpmProcessInstanceId(Long jbpmProcessInstanceId) {
		this.jbpmProcessInstanceId = jbpmProcessInstanceId;
	}

	@Column(name = "JBPM_TOKEN_ID")
	public Long getJbpmTokenId() {
		return jbpmTokenId;
	}

	public void setJbpmTokenId(Long jbpmTokenId) {
		this.jbpmTokenId = jbpmTokenId;
	}

	@Column(name = "JBPM_NODE_ID")
	public Long getJbpmNodeId() {
		return jbpmNodeId;
	}

	public void setJbpmNodeId(Long jbpmNodeId) {
		this.jbpmNodeId = jbpmNodeId;
	}

	public void addTag(String name, String tagValue) {
		if (tags == null) {
			tags = new HashMap<String, LogTag>();
		}
		LogTag tag = new LogTag();
		tag.setName(name);
		tag.setTagValue(tagValue);
		tag.setLogMessage(this);
		tags.put(name, tag);
	}

	@OneToMany(mappedBy = "logMessage", cascade = CascadeType.ALL)
	@MapKey(name = "name")
	public Map<String, LogTag> getTags() {
		return tags;
	}

	public void setTags(Map<String, LogTag> tags) {
		this.tags = tags;
	}

	@Column(name = "FAULT_REASON")
	public String getFaultReason() {
		return faultReason;
	}

	public void setFaultReason(String faultReason) {
		this.faultReason = faultReason;
	}

	@Column(name = "FAULT_CAUSE")
	public Clob getFaultCause() {
		return faultCause;
	}

	public void setFaultCause(Clob fault) {
		this.faultCause = fault;
	}

	@Column(name = "LOG_ENTER")
	public Date getLogEnterTimestamp() {
		return logEnterTimestamp;
	}

	public void setLogEnterTimestamp(Date logEnterTimestamp) {
		this.logEnterTimestamp = logEnterTimestamp;
	}

	@Column(name = "LOG_LEAVE")
	public Date getLogLeaveTimestamp() {
		return logLeaveTimestamp;
	}

	public void setLogLeaveTimestamp(Date logLeaveTimestamp) {
		this.logLeaveTimestamp = logLeaveTimestamp;
	}

}
