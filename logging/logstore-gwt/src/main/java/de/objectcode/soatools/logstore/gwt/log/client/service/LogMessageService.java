package de.objectcode.soatools.logstore.gwt.log.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("log.service")
public interface LogMessageService extends RemoteService {
	List<String> getTagNames();
	
	List<String> getServiceCategories();

	List<String> getServiceNames(String serviceCategory);

	LogMessageSummary.Page getLogMessages(LogMessageFilter filter,
			int pageStart, int pageSize);
}
