package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LogMessageServiceAsync {

	void getTagNames(AsyncCallback<List<String>> callback);

	void getServiceCategories(AsyncCallback<List<String>> callback);

	void getServiceNames(String serviceCategory,
			AsyncCallback<List<String>> callback);

	void getLogMessages(LogMessageFilter filter, int pageStart, int pageSize,
			AsyncCallback<LogMessageSummary.Page> callback);

}
