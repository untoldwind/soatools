package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;

import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;

import de.objectcode.soatools.logstore.persistent.LogMessage;

@Name("logStoreLogDetail")
@Scope(ScopeType.CONVERSATION)
public class LogStoreLogDetailController implements Serializable {
	private static final long serialVersionUID = -11013439034283885L;

	public final static String VIEW_ID = "/secure/logDetail.xhtml";

	@In
	Session logStoreDatabase;

	@In
	LogMessageList logMessageList;

	String selectedDetailTab;
	int currentPage = 1;

	@Transactional
	public String select(LogMessageBean logMessageRow) {
		LogMessage logMessage = (LogMessage)logStoreDatabase.get(LogMessage.class, logMessageRow.getId());
		
		logMessageList.setCurrent(new LogMessageDetailBean(logMessage));

		return VIEW_ID;
	}

	public String getSelectedDetailTab() {
		return selectedDetailTab;
	}

	public void setSelectedDetailTab(String selectedDetailTab) {
		this.selectedDetailTab = selectedDetailTab;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@End
	public String close() {
		return LogStoreSearchController.VIEW_ID;
	}
}
