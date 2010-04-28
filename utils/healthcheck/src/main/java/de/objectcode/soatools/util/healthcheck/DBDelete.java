package de.objectcode.soatools.util.healthcheck;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.util.healthcheck.persistent.HealthcheckEntity;

public class DBDelete extends DBBase {
	private final static Log LOGGER = LogFactory.getLog(DBInsert.class);

	public DBDelete(ConfigTree config) throws ConfigurationException {
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

			if ( entity != null ) {
				session.delete(entity);
				
				session.flush();
				
				message.getBody().add("db-delete",HealthState.OK.toString());				
			} else {
				message.getBody().add("db-delete",HealthState.WARN.toString());
			}
		} catch (final Exception e) {
			LOGGER.error("Exception", e);
			message.getBody().add("db-delete", HealthState.ERROR.toString());
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return message;
	}

}