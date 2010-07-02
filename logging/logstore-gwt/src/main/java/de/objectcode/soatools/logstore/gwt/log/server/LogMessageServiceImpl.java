package de.objectcode.soatools.logstore.gwt.log.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessagePagingData;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageService;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;
import de.objectcode.soatools.logstore.gwt.utils.server.GwtController;

@Controller
@RequestMapping({ "/MainUI/log.service" })
public class LogMessageServiceImpl extends GwtController implements LogMessageService {

	private static final long serialVersionUID = 1L;

	@Override
	public LogMessagePagingData getLogMessages(LogMessageFilter filter,
			int pageStart, int pageSize){
		List<LogMessageSummary> page = new ArrayList<LogMessageSummary>();

		for (int i = 0; i < pageSize; i++) {
			page.add(new LogMessageSummary(String.valueOf(pageStart + i),
					"TestCat", "TestName"));
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new LogMessagePagingData(pageStart, pageSize, 1000, page);
	}

}
