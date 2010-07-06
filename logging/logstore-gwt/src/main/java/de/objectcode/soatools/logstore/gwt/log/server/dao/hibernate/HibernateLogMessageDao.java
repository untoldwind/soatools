package de.objectcode.soatools.logstore.gwt.log.server.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;
import de.objectcode.soatools.logstore.gwt.log.server.dao.ILogMessageDao;
import de.objectcode.soatools.logstore.persistent.LogMessage;

@Repository("logMessageDao")
@Transactional(propagation = Propagation.MANDATORY)
public class HibernateLogMessageDao implements
		ILogMessageDao {
	@Resource
	private SessionFactory sessionFactory;

	@Override
	public int countLogMessages(LogMessageFilter filter) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(LogMessage.class);
		criteria.setProjection(Projections.count("id"));
		
		return (Integer)criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<LogMessageSummary> findLogMessageSummaries(LogMessageFilter filter, int pageStart,
			int pageSize) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(LogMessage.class);
		criteria.addOrder(Order.asc("id"));
		criteria.setFirstResult(pageStart);
		criteria.setMaxResults(pageSize);
		
		List<LogMessageSummary> pageData = new ArrayList<LogMessageSummary>();
		
		for (LogMessage logMessage : (List<LogMessage>)criteria.list() ) {
			pageData.add(toSummaryDTO(logMessage));
		}
		
		return pageData;
	}
	
	static LogMessageSummary toSummaryDTO(LogMessage logMessage) {
		return new LogMessageSummary(
				logMessage.getId(),
				logMessage.getState(),
				logMessage.getServiceCategory(),
				logMessage.getServiceName(),
				logMessage.getLogEnterTimestamp(),
				logMessage.getLogLeaveTimestamp(),
				logMessage.getMessageId(),
				logMessage.getCorrelationId(),
				logMessage.getMessageTo(),
				logMessage.getMessageFrom(),
				logMessage.getMessageReplyTo(),
				logMessage.getMessageFaultTo(),
				logMessage.getMessageType(),
				logMessage.getJbpmProcessInstanceId(),
				logMessage.getJbpmTokenId(),
				logMessage.getJbpmNodeId());
	}
}
