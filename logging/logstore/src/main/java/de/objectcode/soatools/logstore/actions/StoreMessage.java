package de.objectcode.soatools.logstore.actions;

import static de.objectcode.soatools.logstore.IConstants.LOG_SESSIONFACTORY_JNDI;

import java.util.Date;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.lob.ClobImpl;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.logstore.persistent.LogMessage;

/**
 * Store a wiretaped message or exception in the logstore.
 * 
 * This action is intended for internal use only.
 * 
 * @author junglas
 */
public class StoreMessage extends AbstractActionPipelineProcessor {
	private final static Log LOGGER = LogFactory.getLog(StoreMessage.class);

	SessionFactory sessionFactory;

	public StoreMessage(final ConfigTree config) throws ConfigurationException {
		try {
			final InitialContext ctx = new InitialContext();

			this.sessionFactory = (SessionFactory) ctx
					.lookup(LOG_SESSIONFACTORY_JNDI);
		} catch (final NamingException e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(final Message message)
			throws ActionProcessingException {
		Session session = null;

		try {
			session = this.sessionFactory.openSession();

			final LogMessage logMessage = new LogMessage();

			logMessage.setState((String) message.getBody().get("state"));
			logMessage.setLogEnterTimestamp((Date) message.getBody().get(
					"log-enter-timestamp"));
			logMessage.setLogLeaveTimestamp((Date) message.getBody().get(
					"log-leave-timestamp"));
			logMessage.setServiceCategory((String) message.getBody().get(
					"service-category"));
			logMessage.setServiceName((String) message.getBody().get(
					"service-name"));
			logMessage.setMessageId((String) message.getBody()
					.get("message-id"));
			logMessage.setCorrelationId((String) message.getBody().get(
					"correlation-id"));
			logMessage.setMessageTo((String)message.getBody().get("message-to"));
			logMessage.setMessageFrom((String)message.getBody().get("message-from"));
			logMessage.setMessageReplyTo((String)message.getBody().get("message-replyTo"));
			logMessage.setMessageFaultTo((String)message.getBody().get("message-faultTo"));
			logMessage.setMessageType((String)message.getBody().get("message-type"));
			logMessage.setJbpmProcessInstanceId((Long) message.getBody().get(
					"jbpm-process-instance-id"));
			logMessage.setJbpmTokenId((Long) message.getBody().get(
					"jbpm-token-id"));
			logMessage.setJbpmNodeId((Long) message.getBody().get(
					"jbpm-node-id"));

			logMessage.setFaultReason((String) message.getBody().get(
					"fault-reason"));
			if (message.getBody().get("fault-cause") != null) {
				logMessage.setFaultCause(new ClobImpl((String) message
						.getBody().get("fault-cause")));
			}

			final Map<?, ?> tags = (Map<?, ?>) message.getBody().get("tags");
			if (tags != null) {
				for (final Map.Entry<?, ?> entry : tags.entrySet()) {
					if (entry.getKey() != null && entry.getValue() != null) {
						logMessage.addTag(entry.getKey().toString(), entry
								.getValue().toString());
					}
				}
			}

			final String content = (String) message.getBody().get("content");

			if (content != null) {
				logMessage.setContent(new ClobImpl(content));
			} else {
				LOGGER
						.warn("process  : @memo  No content to store. <<message: "
								+ message + ">>");
			}

			session.persist(logMessage);

			session.flush();
		} catch (final Exception e) {
			LOGGER.error("Failed to store message", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return message;
	}
}
