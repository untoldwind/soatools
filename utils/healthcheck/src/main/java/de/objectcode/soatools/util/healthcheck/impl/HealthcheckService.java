package de.objectcode.soatools.util.healthcheck.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.DialectFactory;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.transaction.JBossTransactionManagerLookup;
import org.hibernate.transaction.JTATransactionFactory;

import de.objectcode.soatools.util.healthcheck.IHealhcheckConstants;
import de.objectcode.soatools.util.healthcheck.persistent.HealthcheckEntity;

public class HealthcheckService implements HealthcheckServiceMBean {
	String dataSourceJndiName = "java:/DefaultDS";
	
	SessionFactory sessionFactory;

	UserTransaction userTransaction;

	/**
	 * {@inheritDoc}
	 */
	public void start() throws Exception {
		final InitialContext ctx = new InitialContext();

		final DataSource ds = (DataSource) ctx.lookup(this.dataSourceJndiName);

		userTransaction = (UserTransaction) ctx.lookup("UserTransaction");

		final Connection connection = ds.getConnection();
		final DatabaseMetaData metaData = connection.getMetaData();
		final int majorVersion = metaData.getDatabaseMajorVersion();
		final String vendor = metaData.getDatabaseProductName();
		connection.close();

		Dialect dialect = DialectFactory.determineDialect(vendor,
				majorVersion);

		final AnnotationConfiguration cfg = new AnnotationConfiguration();

		cfg.addAnnotatedClass(HealthcheckEntity.class);
		
		cfg.setProperty(Environment.SESSION_FACTORY_NAME,
				IHealhcheckConstants.SESSIONFACTORY_JNDI_NAME);
		cfg.setProperty(Environment.DATASOURCE, this.dataSourceJndiName);
		cfg.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY,
				JBossTransactionManagerLookup.class.getName());
		cfg.setProperty(Environment.TRANSACTION_STRATEGY,
				JTATransactionFactory.class.getName());
		cfg.setProperty(Environment.DIALECT, dialect.getClass().getName());

		final SchemaUpdate schemaUpdate = new SchemaUpdate(cfg);

		schemaUpdate.execute(true, true);

		sessionFactory = cfg.buildSessionFactory();
	}

	public void stop() {
		sessionFactory = null;
		userTransaction = null;
	}
}
