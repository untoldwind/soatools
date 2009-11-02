package de.objectcode.soatools.mfm.impl;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.hibernate.SessionFactory;
import org.hibernate.cache.HashtableCacheProvider;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.DialectFactory;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.transaction.JBossTransactionManagerLookup;
import org.hibernate.transaction.JTATransactionFactory;
import org.jboss.util.naming.NonSerializableFactory;

import de.objectcode.soatools.mfm.api.BooleanType;
import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.DateType;
import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.IntegerType;
import de.objectcode.soatools.mfm.api.LongType;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.StringType;
import de.objectcode.soatools.mfm.api.Type;
import de.objectcode.soatools.mfm.api.ValueType;
import de.objectcode.soatools.mfm.api.strategy.DropStrategy;
import de.objectcode.soatools.mfm.api.strategy.KeepStrategy;
import de.objectcode.soatools.mfm.api.strategy.RenameStrategy;
import de.objectcode.soatools.mfm.api.strategy.ScriptExpressionStrategy;
import de.objectcode.soatools.mfm.api.strategy.SetValueStrategy;
import de.objectcode.soatools.mfm.api.strategy.StrategyBase;
import de.objectcode.soatools.mfm.io.XMLIO;


public class MessageFormatRepositoryService implements
		MessageFormatRepositoryServiceMBean {
	String SESSIONFACTORY_JNDI = "java:/hibernate/MFMSessionFactory";

	String dataSourceJndiName = "java:/DefaultDS";
	String dialect;

	IMessageFormatRepository repository;
	SessionFactory sessionFactory;
	String messageFormatResources;

	public void start() throws Exception {
		InitialContext ctx = new InitialContext();

		if (dialect == null || dialect.length() == 0) {
			DataSource ds = (DataSource) ctx.lookup(dataSourceJndiName);

			Connection connection = ds.getConnection();
			DatabaseMetaData metaData = connection.getMetaData();
			int majorVersion = metaData.getDatabaseMajorVersion();
			String vendor = metaData.getDatabaseProductName();
			connection.close();

			if ("H2".equalsIgnoreCase(vendor)) {
				dialect = H2Dialect.class.getName();
			} else {
				Dialect autoDialect = DialectFactory.determineDialect(vendor,
						majorVersion);
				dialect = autoDialect.getClass().getName();
			}
		}

		AnnotationConfiguration cfg = new AnnotationConfiguration();

		cfg.addAnnotatedClass(Type.class);
		cfg.addAnnotatedClass(Component.class);
		cfg.addAnnotatedClass(ValueType.class);
		cfg.addAnnotatedClass(ComponentType.class);
		cfg.addAnnotatedClass(MessageFormat.class);
		cfg.addAnnotatedClass(BooleanType.class);
		cfg.addAnnotatedClass(StringType.class);
		cfg.addAnnotatedClass(IntegerType.class);
		cfg.addAnnotatedClass(LongType.class);
		cfg.addAnnotatedClass(DateType.class);
		cfg.addAnnotatedClass(StrategyBase.class);
		cfg.addAnnotatedClass(KeepStrategy.class);
		cfg.addAnnotatedClass(RenameStrategy.class);
		cfg.addAnnotatedClass(ScriptExpressionStrategy.class);
		cfg.addAnnotatedClass(SetValueStrategy.class);
		cfg.addAnnotatedClass(DropStrategy.class);
		cfg.setProperty(Environment.SESSION_FACTORY_NAME, SESSIONFACTORY_JNDI);
		cfg.setProperty(Environment.DATASOURCE, dataSourceJndiName);
		cfg.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY,
				JBossTransactionManagerLookup.class.getName());
		cfg.setProperty(Environment.TRANSACTION_STRATEGY,
				JTATransactionFactory.class.getName());
		cfg.setProperty(Environment.DIALECT, dialect);
		cfg.setProperty(Environment.CACHE_PROVIDER,
				HashtableCacheProvider.class.getName());

		SchemaUpdate schemaUpdate = new SchemaUpdate(cfg);

		schemaUpdate.execute(true, true);

		sessionFactory = cfg.buildSessionFactory();

		repository = new MessageFormatRepositoryImpl(sessionFactory);

		if (messageFormatResources != null) {
			for (String messageFormatResource : messageFormatResources
					.split(",")) {
				MessageFormatModel model = XMLIO.INSTANCE
						.read(MessageFormatRepositoryService.class
								.getResourceAsStream(messageFormatResource
										.trim()));

				repository.registerModel(model);
			}
		}

		NonSerializableFactory.rebind(ctx, IMessageFormatRepository.JNDI_NAME,
				repository);
	}

	public void stop() {
		try {
			NonSerializableFactory.unbind(IMessageFormatRepository.JNDI_NAME);
		} catch (NameNotFoundException e) {
		}
		sessionFactory = null;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getDataSourceJndiName() {
		return dataSourceJndiName;
	}

	public void setDataSourceJndiName(String dataSourceJndiName) {
		this.dataSourceJndiName = dataSourceJndiName;
	}

	public String showMessageFormats() throws Exception {
		EncodedStringWriter writer = new EncodedStringWriter();

		MessageFormatModel messageFormatModel = new MessageFormatModel();

		messageFormatModel.setMessageFormats(new ArrayList<MessageFormat>(
				repository.getMessageFormats()));
		messageFormatModel.setTypes(new ArrayList<ComponentType>());

		XMLStreamWriter streamWriter = XMLOutputFactory.newInstance()
				.createXMLStreamWriter(writer);

		XMLIO.INSTANCE.write(messageFormatModel, streamWriter);

		return writer.toString();
	}

	public String getMessageFormatResources() {
		return messageFormatResources;
	}

	public void setMessageFormatResources(String messageFormatResources) {
		this.messageFormatResources = messageFormatResources;
	}

	private final static class EncodedStringWriter extends Writer {
		StringBuffer buffer = new StringBuffer();

		@Override
		public void close() throws IOException {
		}

		@Override
		public void flush() throws IOException {
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			for (int i = 0; i < len; i++) {
				char c = cbuf[i + off];

				switch (c) {
				case '&':
					buffer.append("&amp;");
					break;
				case '<':
					buffer.append("&lt;");
					break;
				case '>':
					buffer.append("&gt;");
					break;
				case '\"':
					buffer.append("&quot;");
					break;
				default:
					buffer.append(c);
				}
			}
		}

		@Override
		public String toString() {
			return buffer.toString();
		}
	}
}
