package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;

import de.objectcode.soatools.logstore.persistent.LogMessage;

@Name("logStoreLogDetail")
@Scope(ScopeType.CONVERSATION)
public class LogStoreLogDetailController implements Serializable {
	private final static Log LOG = LogFactory
	.getLog(LogStoreLogDetailController.class);
	
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

	@Transactional
	public String refresh() {
		logMessageList.refresh();

		return VIEW_ID;
	}
	
	public String resend() {
		LogMessageDetailBean logMessageBean = logMessageList.getCurrent();
		
		if ( logMessageBean != null ) {
			Message resendMessage = MessageFactory.getInstance().getMessage();
			
			for ( NameValuePair entry : logMessageBean.getBodyList()) {
				resendMessage.getBody().add(entry.getName(), entry.getValue());
			}
			for (NameValuePair entry :logMessageBean.getPropertyList()) {
				resendMessage.getProperties().setProperty(entry.getName(), entry.getValue());
			}
			for(NameValuePair entry : logMessageBean.getContextList()) {
				resendMessage.getContext().setContext(entry.getName(), entry.getValue());
			}
			
			try {
				ServiceInvoker invoker = new ServiceInvoker(logMessageBean.getServiceCategory(), logMessageBean.getServiceName());
				
				invoker.deliverAsync(resendMessage);
			}
			catch ( Exception e ) {
				LOG.error("Exception", e);
			}
		}
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
