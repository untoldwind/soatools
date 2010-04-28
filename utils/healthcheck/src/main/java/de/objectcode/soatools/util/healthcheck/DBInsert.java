package de.objectcode.soatools.util.healthcheck;

import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.util.healthcheck.persistent.HealthcheckEntity;

public class DBInsert extends DBBase {
	private final static Log LOGGER = LogFactory.getLog(DBInsert.class);

	public DBInsert(ConfigTree config) throws ConfigurationException {
		super(config);
	}

	public Message process(Message message) throws ActionProcessingException {
		Session session = null;

		try {
			session = this.sessionFactory.openSession();

			HealthcheckEntity entity = new HealthcheckEntity(DateFormat
					.getDateTimeInstance().format(new Date()));

			session.persist(entity);

			session.flush();

			message.getProperties().setProperty("healthcheck-id",
					entity.getId());
			message.getBody().add("db-insert", HealthState.OK.toString());
		} catch (final Exception e) {
			LOGGER.error("Exception", e);
			message.getBody().add("db-insert", HealthState.ERROR.toString());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return message;
	}

}
