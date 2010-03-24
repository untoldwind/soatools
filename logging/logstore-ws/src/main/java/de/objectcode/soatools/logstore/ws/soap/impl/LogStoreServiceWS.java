package de.objectcode.soatools.logstore.ws.soap.impl;

import static de.objectcode.soatools.logstore.IConstants.LOG_SESSIONFACTORY_JNDI;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.BindingType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import de.objectcode.soatools.logstore.persistent.LogTag;
import de.objectcode.soatools.logstore.ws.soap.LogStoreService;
import de.objectcode.soatools.logstore.ws.soap.types.LogMessage;
import de.objectcode.soatools.logstore.ws.soap.types.LogMessageDetail;
import de.objectcode.soatools.logstore.ws.soap.types.LogMessageList;
import de.objectcode.soatools.logstore.ws.soap.types.Query;
import de.objectcode.soatools.logstore.ws.soap.types.TagCondition;
import de.objectcode.soatools.logstore.ws.soap.types.TagList;

@WebService(name = "LogStoreService", targetNamespace = "http://objectcode.de/soatools/logstore", endpointInterface = "de.objectcode.soatools.logstore.ws.soap.LogStoreService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@BindingType(value = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true")
public class LogStoreServiceWS implements LogStoreService {
	private final static Log LOGGER = LogFactory
			.getLog(LogStoreServiceWS.class);

	SessionFactory sessionFactory;

	public LogStoreServiceWS() {
		try {
			final InitialContext ctx = new InitialContext();

			this.sessionFactory = (SessionFactory) ctx
					.lookup(LOG_SESSIONFACTORY_JNDI);
		} catch (final NamingException e) {
			LOGGER.error("Exception", e);
		}
	}

	@SuppressWarnings("unchecked")
	public TagList getTagNames() {
		Session session = null;

		try {
			session = sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogTag.class);
			criteria.setProjection(Projections.distinct(Projections
					.property("name")));

			return new TagList((List<String>) criteria.list());
		} finally {
			if (session != null)
				session.close();
		}
	}

	public LogMessageList queryLogMessages(Query query) {
		Session session = null;

		try {
			session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(LogTag.class);

			criteria.setProjection(Projections.property("logMessage"));
			if (query.getConditions() != null) {
				for (TagCondition tagCondition : query.getConditions())
					criteria.add(Restrictions.and(Restrictions.eq("name",
							tagCondition.getTagName()), Restrictions.eq(
							"tagValue", tagCondition.getValue())));

			}
			Criteria logMessageCriteria = criteria.createCriteria("logMessage");
			if (query.getFrom() != null) {
				logMessageCriteria.add(Restrictions.ge("logEnterTimestamp",
						query.getFrom()));
			}
			if (query.getUntil() != null) {
				logMessageCriteria.add(Restrictions.le("logEnterTimestamp",
						query.getUntil()));
			}
			criteria.addOrder(Order.asc("logMessage.id"));

			return transform(criteria.list());
		} finally {
			if (session != null)
				session.close();
		}
	}

	public LogMessageDetail logMessageDetail(long logId) {
		// TODO Auto-generated method stub
		return null;
	}

	LogMessageList transform(List<?> result) {
		LogMessageList logMessageList = new LogMessageList();
		logMessageList.setMessages(new ArrayList<LogMessage>());

		for (Object obj : result) {
			logMessageList
					.getMessages()
					.add(
							transform((de.objectcode.soatools.logstore.persistent.LogMessage) obj));
		}
		return logMessageList;
	}

	LogMessage transform(
			de.objectcode.soatools.logstore.persistent.LogMessage entity) {

		LogMessage logMessage = new LogMessage();

		logMessage.setId(entity.getId());
		logMessage.setState(entity.getState());
		logMessage.setLogEnterTimestamp(entity.getLogEnterTimestamp());
		logMessage.setLogLeaveTimestamp(entity.getLogLeaveTimestamp());
		logMessage.setServiceCategory(entity.getServiceCategory());
		logMessage.setServiceName(entity.getServiceName());
		logMessage.setMessageId(entity.getMessageId());
		logMessage.setCorrelationId(entity.getCorrelationId());
		logMessage.setMessageTo(entity.getMessageTo());
		logMessage.setMessageFrom(entity.getMessageFrom());
		logMessage.setMessageReplyTo(entity.getMessageReplyTo());
		logMessage.setMessageFaultTo(entity.getMessageFaultTo());
		logMessage.setMessageType(entity.getMessageType());
		logMessage.setJbpmProcessInstanceId(entity.getJbpmProcessInstanceId());
		logMessage.setJbpmTokenId(entity.getJbpmTokenId());
		logMessage.setJbpmNodeId(entity.getJbpmNodeId());

		return logMessage;
	}
}
