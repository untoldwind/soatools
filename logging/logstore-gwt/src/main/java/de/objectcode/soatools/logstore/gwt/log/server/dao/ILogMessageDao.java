package de.objectcode.soatools.logstore.gwt.log.server.dao;

import java.util.List;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;

public interface ILogMessageDao {

	List<String> findTagNames();

	List<String> findServiceCategories();

	List<String> findServiceNames(String serviceCategory);

	int countLogMessages(LogMessageFilter filter);

	List<LogMessageSummary> findLogMessageSummaries(LogMessageFilter filter,
			int pageStart, int pageSize);
}
