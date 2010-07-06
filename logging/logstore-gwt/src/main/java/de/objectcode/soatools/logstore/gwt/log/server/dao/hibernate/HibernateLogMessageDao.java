package de.objectcode.soatools.logstore.gwt.log.server.dao.hibernate;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;
import de.objectcode.soatools.logstore.gwt.log.server.dao.ILogMessageDao;
import de.objectcode.soatools.logstore.persistent.LogMessage;
import de.objectcode.soatools.logstore.persistent.LogTag;

@Repository("logMessageDao")
@Transactional(propagation = Propagation.MANDATORY)
public class HibernateLogMessageDao implements ILogMessageDao {
	@Resource
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findTagNames() {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LogTag.class);
		criteria.setProjection(Projections.distinct(Projections
				.property("name")));
		criteria.addOrder(Order.asc("name"));

		return criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findServiceCategories() {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LogMessage.class);
		criteria.setProjection(Projections.distinct(Projections
				.property("serviceCategory")));
		criteria.addOrder(Order.asc("serviceCategory"));

		return criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> findServiceNames(String serviceCategory) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(LogMessage.class);
		criteria.add(Restrictions.eq("serviceCategory", serviceCategory));
		criteria.setProjection(Projections.distinct(Projections
				.property("serviceName")));
		criteria.addOrder(Order.asc("serviceName"));

		return criteria.list();
	}

	@Override
	public int countLogMessages(LogMessageFilter filter) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = createCriteria(session, filter);
		criteria.setProjection(Projections.rowCount());

		return (Integer) criteria.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LogMessageSummary> findLogMessageSummaries(
			LogMessageFilter filter, int pageStart, int pageSize) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = createCriteria(session, filter);
		criteria.addOrder(Order.asc("logMessage.id"));
		criteria.setFirstResult(pageStart);
		criteria.setMaxResults(pageSize);

		List<LogMessageSummary> pageData = new ArrayList<LogMessageSummary>();

		for (LogMessage logMessage : (List<LogMessage>) criteria.list()) {
			pageData.add(toSummaryDTO(logMessage));
		}

		return pageData;
	}

	private Criteria createCriteria(Session session, LogMessageFilter filter) {
		Map<LogMessageFilter.CriteriaType, List<LogMessageFilter.Criteria>> filterCriteriaMap = new EnumMap<LogMessageFilter.CriteriaType, List<LogMessageFilter.Criteria>>(
				LogMessageFilter.CriteriaType.class);

		for (LogMessageFilter.CriteriaType criteriaType : LogMessageFilter.CriteriaType
				.values())
			filterCriteriaMap.put(criteriaType,
					new ArrayList<LogMessageFilter.Criteria>());

		System.out.println(">< " + filterCriteriaMap);
		
		if (filter != null && filter.getCriterias() != null)
			for (LogMessageFilter.Criteria filterCriteria : filter
					.getCriterias()) {
				System.out.println(">> " + filterCriteria.getType());
				System.out.println("<< " + filterCriteriaMap.get(filterCriteria.getType()));
				
				filterCriteriaMap.get(filterCriteria.getType()).add(
						filterCriteria);
			}

		Criteria criteria;

		if (!filterCriteriaMap.get(LogMessageFilter.CriteriaType.TAGVALUE)
				.isEmpty()) {
			criteria = session.createCriteria(LogTag.class, "logTag");
			criteria.createAlias("logMessage", "logMessage");
			criteria.setProjection(Projections.property("logMessage"));

			Criterion tagCriterion = null;
			for (LogMessageFilter.Criteria filterCriteria : filterCriteriaMap
					.get(LogMessageFilter.CriteriaType.TAGVALUE)) {
				LogMessageFilter.TagCriteria tagCriteria = (LogMessageFilter.TagCriteria) filterCriteria;

				Criterion nextTagCriterion = Restrictions.and(Restrictions.eq(
						"logTag.name", tagCriteria.getTagName()), Restrictions
						.eq("logTag.tagValue", tagCriteria.getTagValue()));

				if (tagCriterion == null)
					tagCriterion = nextTagCriterion;
				else
					tagCriterion = Restrictions.or(tagCriterion,
							nextTagCriterion);
			}
			if (tagCriterion != null)
				criteria.add(tagCriterion);

		} else {
			criteria = session.createCriteria(LogMessage.class, "logMessage");
		}

		Criterion serviceCriterion = null;
		for (LogMessageFilter.Criteria filterCriteria : filterCriteriaMap
				.get(LogMessageFilter.CriteriaType.SERVICE)) {
			LogMessageFilter.ServiceCriteria serviceCriteria = (LogMessageFilter.ServiceCriteria) filterCriteria;

			Criterion nextServiceCriterion = Restrictions
					.and(Restrictions.eq("logMessage.serviceCategory",
							serviceCriteria.getServiceCategory()), Restrictions
							.eq("logMessage.serviceName",
									serviceCriteria.getServiceName()));

			if (serviceCriterion == null)
				serviceCriterion = nextServiceCriterion;
			else
				serviceCriterion = Restrictions.or(serviceCriterion,
						nextServiceCriterion);
		}
		if (serviceCriterion != null)
			criteria.add(serviceCriterion);

		return criteria;
	}

	static LogMessageSummary toSummaryDTO(LogMessage logMessage) {
		return new LogMessageSummary(logMessage.getId(), logMessage.getState(),
				logMessage.getServiceCategory(), logMessage.getServiceName(),
				logMessage.getLogEnterTimestamp(),
				logMessage.getLogLeaveTimestamp(), logMessage.getMessageId(),
				logMessage.getCorrelationId(), logMessage.getMessageTo(),
				logMessage.getMessageFrom(), logMessage.getMessageReplyTo(),
				logMessage.getMessageFaultTo(), logMessage.getMessageType(),
				logMessage.getJbpmProcessInstanceId(),
				logMessage.getJbpmTokenId(), logMessage.getJbpmNodeId());
	}
}
