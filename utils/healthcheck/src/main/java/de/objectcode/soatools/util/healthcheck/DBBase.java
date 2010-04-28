package de.objectcode.soatools.util.healthcheck;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.SessionFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.helpers.ConfigTree;

public abstract class DBBase extends AbstractActionPipelineProcessor {
	SessionFactory sessionFactory;

	public DBBase(final ConfigTree config)
			throws ConfigurationException {
		try {
			final InitialContext ctx = new InitialContext();

			this.sessionFactory = (SessionFactory) ctx
					.lookup(IHealhcheckConstants.SESSIONFACTORY_JNDI_NAME);
		} catch (final NamingException e) {
			throw new ConfigurationException(e);
		}
	}

}
