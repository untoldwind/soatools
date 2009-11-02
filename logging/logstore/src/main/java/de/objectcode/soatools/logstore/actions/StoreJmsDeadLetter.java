package de.objectcode.soatools.logstore.actions;

import static de.objectcode.soatools.logstore.IConstants.LOG_SESSIONFACTORY_JNDI;

import java.util.Date;

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

import de.objectcode.soatools.logstore.persistent.LogJmsDeadLetter;

/**
 * Store a JMS dead letter message.
 * 
 * This action in intended for internal use to log messages from the JMS
 * dead-letter queue (not to be mistaken with the ESB dead-letter store).
 * 
 * @author junglas
 */
public class StoreJmsDeadLetter extends AbstractActionPipelineProcessor {
	private final static Log LOGGER = LogFactory
			.getLog(StoreJmsDeadLetter.class);

	SessionFactory sessionFactory;

	public StoreJmsDeadLetter(final ConfigTree config)
			throws ConfigurationException {
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

			final LogJmsDeadLetter logJmsDeadLetter = new LogJmsDeadLetter();

			logJmsDeadLetter.setLogTimestamp(new Date());

			final String content = (String) message.getBody().get();

			if (content != null) {
				logJmsDeadLetter.setContent(new ClobImpl(content));
			}

			session.persist(logJmsDeadLetter);

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