package de.objectcode.soatools.logstore.gwt.log.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("log.service")
public interface LogMessageService extends RemoteService {
	LogMessageSummary.Page getLogMessages(LogMessageFilter filter, int pageStart, int pageSize);
}
