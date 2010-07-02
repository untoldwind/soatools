package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogMessagePagingData implements IsSerializable {
	int pageStart;
	int pageSize;
	int dataSize;
	List<LogMessageSummary> logMessages;

	public LogMessagePagingData() {		
	}
	
	
	public LogMessagePagingData(int pageStart, int pageSize, int dataSize,
			List<LogMessageSummary> logMessages) {

		this.pageStart = pageStart;
		this.pageSize = pageSize;
		this.dataSize = dataSize;
		this.logMessages = logMessages;
	}


	public int getPageStart() {
		return pageStart;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public List<LogMessageSummary> getLogMessages() {
		return logMessages;
	}
}
