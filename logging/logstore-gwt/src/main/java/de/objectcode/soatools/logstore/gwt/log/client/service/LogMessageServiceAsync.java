package de.objectcode.soatools.logstore.gwt.log.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LogMessageServiceAsync {

	void getLogMessages(LogMessageFilter filter, int pageStart, int pageSize,
			AsyncCallback<LogMessagePagingData> callback);

}
