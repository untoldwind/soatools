package de.objectcode.soatools.util.splitter.impl;

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
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.MySQLInnoDBDialect;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.transaction.JBossTransactionManagerLookup;
import org.hibernate.transaction.JTATransactionFactory;

import de.objectcode.soatools.util.splitter.IConstants;
import de.objectcode.soatools.util.splitter.persistent.SplitEntity;
import de.objectcode.soatools.util.splitter.persistent.SplitPartEntity;

public class SplitterService implements SplitterServiceMBean {
	String dataSourceJndiName = "java:/DefaultDS";

	SessionFactory sessionFactory;

	UserTransaction userTransaction;

	/**
	 * {@inheritDoc}
	 */
	public String getDataSourceJndiName() {
		return this.dataSourceJndiName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDataSourceJndiName(final String dataSourceJndiName) {
		this.dataSourceJndiName = dataSourceJndiName;
	}

	public void start() throws Exception {
		final InitialContext ctx = new InitialContext();

		final DataSource ds = (DataSource) ctx.lookup(this.dataSourceJndiName);

		userTransaction = (UserTransaction) ctx.lookup("UserTransaction");

		final Connection connection = ds.getConnection();
		final DatabaseMetaData metaData = connection.getMetaData();
		final int majorVersion = metaData.getDatabaseMajorVersion();
		final String vendor = metaData.getDatabaseProductName();
		connection.close();

		Dialect dialect = DialectFactory.determineDialect(vendor, majorVersion);

		// Fix: Ensure that we always use InnoDB on MySQL
		if (dialect instanceof MySQL5Dialect) {
			dialect = new MySQL5InnoDBDialect();
		} else if (dialect instanceof MySQLDialect) {
			dialect = new MySQLInnoDBDialect();
		}

		final AnnotationConfiguration cfg = new AnnotationConfiguration();

		cfg.addAnnotatedClass(SplitEntity.class);
		cfg.addAnnotatedClass(SplitPartEntity.class);
		cfg.setProperty(Environment.SESSION_FACTORY_NAME,
				IConstants.SPLITTER_SESSIONFACTORY_JNDI);
		cfg.setProperty(Environment.DATASOURCE, this.dataSourceJndiName);
		cfg.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY,
				JBossTransactionManagerLookup.class.getName());
		cfg.setProperty(Environment.TRANSACTION_STRATEGY,
				JTATransactionFactory.class.getName());
		cfg.setProperty(Environment.DIALECT, dialect.getClass().getName());

		final SchemaUpdate schemaUpdate = new SchemaUpdate(cfg);

		schemaUpdate.execute(true, true);

		this.sessionFactory = cfg.buildSessionFactory();
	}

	public void stop() {
		this.sessionFactory = null;
	}

}
