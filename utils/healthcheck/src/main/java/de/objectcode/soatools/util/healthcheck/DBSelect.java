package de.objectcode.soatools.util.healthcheck;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.util.healthcheck.persistent.HealthcheckEntity;

public class DBSelect extends DBBase {
	private final static Log LOGGER = LogFactory.getLog(DBInsert.class);

	public DBSelect(ConfigTree config) throws ConfigurationException {
		super(config);
	}

	public Message process(Message message) throws ActionProcessingException {
		Session session = null;

		try {
			session = this.sessionFactory.openSession();

			Long id = (Long)message.getProperties().getProperty("healthcheck-id");
			
			HealthcheckEntity entity = null;
			
			if ( id != null )
				entity = (HealthcheckEntity)session.get(HealthcheckEntity.class, id);

			message.getBody().add("db-select", entity != null ? HealthState.OK.toString() : HealthState.WARN.toString());
		} catch (final Exception e) {
			LOGGER.error("Exception", e);
			message.getBody().add("db-select", HealthState.ERROR.toString());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return message;
	}

}
