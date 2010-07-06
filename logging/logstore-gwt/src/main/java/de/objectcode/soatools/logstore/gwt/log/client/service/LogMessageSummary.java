package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.soatools.logstore.gwt.utils.client.service.BaseDataPage;

public class LogMessageSummary implements IsSerializable {

	private long id;
	private String state;
	private String serviceCategory;
	private String serviceName;
	private Date logEnterTimestamp;
	private Date logLeaveTimestamp;
	private String messageId;
	private String correlationId;
	private String messageTo;
	private String messageFrom;
	private String messageReplyTo;
	private String messageFaultTo;
	private String messageType;
	private Long jbpmProcessInstanceId;
	private Long jbpmTokenId;
	private Long jbpmNodeId;

	public LogMessageSummary() {
	}

	public LogMessageSummary(long id, String state, String serviceCategory,
			String serviceName, Date logEnterTimestamp, Date logLeaveTimestamp,
			String messageId, String correlationId, String messageTo,
			String messageFrom, String messageReplyTo, String messageFaultTo,
			String messageType, Long jbpmProcessInstanceId, Long jbpmTokenId,
			Long jbpmNodeId) {

		this.id = id;
		this.state = state;
		this.serviceCategory = serviceCategory;
		this.serviceName = serviceName;
		this.logEnterTimestamp = logEnterTimestamp;
		this.logLeaveTimestamp = logLeaveTimestamp;
		this.messageId = messageId;
		this.correlationId = correlationId;
		this.messageTo = messageTo;
		this.messageFrom = messageFrom;
		this.messageReplyTo = messageReplyTo;
		this.messageFaultTo = messageFaultTo;
		this.messageType = messageType;
		this.jbpmProcessInstanceId = jbpmProcessInstanceId;
		this.jbpmTokenId = jbpmTokenId;
		this.jbpmNodeId = jbpmNodeId;
	}

	public long getId() {
		return id;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getState() {
		return state;
	}

	public Date getLogEnterTimestamp() {
		return logEnterTimestamp;
	}

	public Date getLogLeaveTimestamp() {
		return logLeaveTimestamp;
	}

	public String getMessageId() {
		return messageId;
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

	public String getCorrelationId() {
		return correlationId;
	}

	public Long getJbpmTokenId() {
		return jbpmTokenId;
	}

	public Long getJbpmNodeId() {
		return jbpmNodeId;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogMessageSummary other = (LogMessageSummary) obj;

		return id == other.id;
	}

	public static class Page extends BaseDataPage<LogMessageSummary> {

		public Page() {
		}

		public Page(int pageStart, int pageSize, int totalNumber,
				List<LogMessageSummary> pageData) {
			super(pageStart, pageSize, totalNumber);

			this.pageData = pageData;
		}

		private List<LogMessageSummary> pageData;

		public List<LogMessageSummary> getPageData() {
			return pageData;
		}
	}
}
