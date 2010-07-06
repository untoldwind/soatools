package de.objectcode.soatools.logstore.gwt.log.server;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageService;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;
import de.objectcode.soatools.logstore.gwt.log.server.dao.ILogMessageDao;
import de.objectcode.soatools.logstore.gwt.utils.server.GwtController;

@Controller
@RequestMapping({ "/MainUI/log.service" })
public class LogMessageServiceImpl extends GwtController implements LogMessageService {

	private static final long serialVersionUID = 1L;

	@Resource
	private ILogMessageDao logMessageDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<String> getTagNames() {
		return logMessageDao.findTagNames();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<String> getServiceCategories() {
		return logMessageDao.findServiceCategories();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<String> getServiceNames(String serviceCategory) {
		return logMessageDao.findServiceNames(serviceCategory);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public LogMessageSummary.Page getLogMessages(LogMessageFilter filter,
			int pageStart, int pageSize){

		int totalNumber = logMessageDao.countLogMessages(filter);
		List<LogMessageSummary> pageData = logMessageDao.findLogMessageSummaries(filter, pageStart, pageSize);
		
		return new LogMessageSummary.Page(pageStart, pageSize, totalNumber, pageData);
	}

}
