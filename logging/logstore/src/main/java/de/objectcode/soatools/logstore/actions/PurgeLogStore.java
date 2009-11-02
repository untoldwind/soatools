package de.objectcode.soatools.logstore.actions;

import static de.objectcode.soatools.logstore.IConstants.LOG_SESSIONFACTORY_JNDI;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.LockAcquisitionException;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.logstore.persistent.LogMessage;

/**
 * Purges obsolete messages from the log store.
 * 
 * This action is supposed for internal use only in conjunction with a schedule
 * provider.
 * 
 * @author junglas
 */
public class PurgeLogStore extends AbstractActionPipelineProcessor {
	private final static Log LOGGER = LogFactory.getLog(PurgeLogStore.class);

	private final long periodToDeleteOld;
	SessionFactory sessionFactory;

	public PurgeLogStore(final ConfigTree config) throws ConfigurationException {
		try {
			final InitialContext ctx = new InitialContext();

			this.sessionFactory = (SessionFactory) ctx
					.lookup(LOG_SESSIONFACTORY_JNDI);
		} catch (final NamingException e) {
			throw new ConfigurationException(e);
		}

		this.periodToDeleteOld = config.getLongAttribute("period-to-delete",
				1000 * 60 * 60 * 24 * 7); // 7 days ago
	}

	private void deleteLogMessage(final long logMessageId) {
		final Date startDate;

		if (LOGGER.isInfoEnabled()) {
			startDate = new Date();
		} else {
			startDate = null;
		}

		Session session = null;
		Transaction trx = null;

		// Select candidates
		try {
			session = this.sessionFactory.openSession();

			trx = session.beginTransaction();

			final LogMessage logMessage = (LogMessage) session.get(
					LogMessage.class, logMessageId, LockMode.UPGRADE);

			if (logMessage == null) {
				if (LOGGER.isInfoEnabled()) {
					final String msg = "No instance found for <<id: {0}>>. Maybe delete by other cluster instance";
					LOGGER.info(MessageFormat.format(msg, logMessageId));
				}

				return;
			}

			session.delete(logMessage);

			if (LOGGER.isInfoEnabled()) {
				final String msg = "Delete message with <<id: {0}>> takes <<ms: {1}>>";
				LOGGER.info(MessageFormat.format(msg, logMessageId, new Date()
						.getTime()
						- startDate.getTime()));
			}

			session.flush();
			trx.commit();
		} catch (final LockAcquisitionException ex) {
			LOGGER.info("cannot get lock for <<id: " + logMessageId
					+ ">>. Maybe lock by other cluster instance. ");
		} catch (final Exception e) {
			LOGGER.error("Failed to delete logmessage " + logMessageId, e);
		} finally {
			if (trx != null && trx.isActive()) {
				trx.rollback();
			}
			if (session != null) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Message process(final Message message)
			throws ActionProcessingException {
		Session session = null;
		Transaction trx = null;
		List<Long> logMessageIds = null;
		int increment = 1000;

		// Select candidates
		try {
			session = this.sessionFactory.openSession();

			trx = session.beginTransaction();

			final Date currentDate = new Date();
			final Date oldEndDate = new Date(currentDate.getTime()
					- this.periodToDeleteOld);

			final Criteria criteria = session.createCriteria(LogMessage.class);
			criteria.setProjection(Projections.id());
			criteria.add(Restrictions.isNotNull("logLeaveTimestamp"));
			criteria.add(Restrictions.le("logLeaveTimestamp", oldEndDate));

			criteria.setMaxResults(increment);

			logMessageIds = criteria.list();

			trx.rollback();
		} catch (final Exception e) {
			LOGGER.error("Failed to purge message store", e);
		} finally {
			if (trx != null && trx.isActive()) {
				trx.rollback();
			}
			if (session != null) {
				session.close();
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("LogMessage to delete: " + logMessageIds);
		}

		if (logMessageIds != null) {
			for (long logMessageId : logMessageIds) {
				deleteLogMessage(logMessageId);
			}
		}

		return message;
	}
}
