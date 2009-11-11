package de.objectcode.soatools.logstore.impl;

import static de.objectcode.soatools.logstore.IConstants.LOG_SESSIONFACTORY_JNDI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.DialectFactory;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.transaction.JBossTransactionManagerLookup;
import org.hibernate.transaction.JTATransactionFactory;
import org.jboss.internal.soa.esb.util.Encoding;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.util.Util;

import de.objectcode.soatools.logstore.persistent.EsbMessage;
import de.objectcode.soatools.logstore.persistent.LogJmsDeadLetter;
import de.objectcode.soatools.logstore.persistent.LogMessage;
import de.objectcode.soatools.logstore.persistent.LogTag;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

public class LogStoreService implements LogStoreServiceMBean {
	private final static Log LOG = LogFactory.getLog(LogStoreService.class);

	String dataSourceJndiName = "java:/DefaultDS";

	SessionFactory sessionFactory;

	UserTransaction userTransaction;

	private Transformer transformer;

	private void dumpLogMessage(final Element parent,
			final LogMessage logMessage) {
		final Element logMessageElement = parent.addElement("log-message");
		logMessageElement.addAttribute("service-category", logMessage
				.getServiceCategory());
		logMessageElement.addAttribute("service-name", logMessage
				.getServiceName());
		logMessageElement.addAttribute("state", logMessage.getState());
		logMessageElement.addAttribute("message-id", logMessage.getMessageId());
		logMessageElement.addAttribute("correlation-id", logMessage
				.getCorrelationId());
		if (logMessage.getJbpmProcessInstanceId() != null) {
			logMessageElement.addAttribute("jbpm-process-instance-id", String
					.valueOf(logMessage.getJbpmProcessInstanceId()));
		}
		if (logMessage.getJbpmNodeId() != null) {
			logMessageElement.addAttribute("jbpm-node-id", String
					.valueOf(logMessage.getJbpmNodeId()));
		}
		if (logMessage.getJbpmTokenId() != null) {
			logMessageElement.addAttribute("jbpm-token-id", String
					.valueOf(logMessage.getJbpmTokenId()));
		}
		if (logMessage.getFaultReason() != null) {
			final Element faultElement = logMessageElement.addElement("fault");
			faultElement.addElement("reason").addText(
					logMessage.getFaultReason());

			if (logMessage.getFaultCause() != null) {
				final StringBuffer faultCause = new StringBuffer();
				try {
					final char buffer[] = new char[8192];
					int readed;
					final Reader reader = logMessage.getFaultCause()
							.getCharacterStream();

					while ((readed = reader.read(buffer)) > 0) {
						faultCause.append(buffer, 0, readed);
					}
					reader.close();
				} catch (final Exception e) {
				}

				faultElement.addElement("cause")
						.addCDATA(faultCause.toString());
			}
		}
		try {
			final StringBuffer encoded = new StringBuffer();
			final char buffer[] = new char[8192];
			int readed;
			final Reader reader = logMessage.getContent().getCharacterStream();

			while ((readed = reader.read(buffer)) > 0) {
				encoded.append(buffer, 0, readed);
			}
			reader.close();

			final Message message = Util.deserialize(Encoding
					.decodeToObject(encoded.toString()));
			final Element messageElement = logMessageElement
					.addElement("message");

			final Element propertiesElement = messageElement
					.addElement("properties");
			for (final String name : message.getProperties().getNames()) {
				final Object value = message.getProperties().getProperty(name);
				final Element propertyElement = propertiesElement
						.addElement("property");
				propertyElement.addAttribute("name", name);
				if (value != null) {
					propertyElement.addAttribute("value", value.toString());
				}
			}

			final Element bodiesElement = messageElement.addElement("bodies");
			for (final String name : message.getBody().getNames()) {
				final Object value = message.getBody().get(name);
				final Element bodyElement = bodiesElement.addElement("body");
				bodyElement.addAttribute("name", name);
				if (value != null) {
					if (value instanceof NormalizedData) {
						final Element dataElement = bodyElement
								.addElement("normalized-data");

						((NormalizedData) value).toXML(dataElement);
					} else if (value instanceof String
							&& ((String) value).startsWith("<?xml")) {
						try {
							final SAXReader saxReader = new SAXReader();
							final Document dataDocument = saxReader
									.read(new StringReader((String) value));

							bodyElement.add(dataDocument.getRootElement()
									.createCopy());
						} catch (final Exception e) {
							bodyElement.addCDATA(value.toString());
						}
					} else if (value instanceof byte[]) {
						bodyElement
								.addCDATA(new String((byte[]) value, "UTF-8"));
					} else {
						bodyElement.addCDATA(value.toString());
					}
				}
			}

			final Element attachmentsElement = messageElement
					.addElement("attachments");
			for (final String name : message.getAttachment().getNames()) {
				final Object value = message.getAttachment().get(name);
				final Element attachmentElement = attachmentsElement
						.addElement("attachment");
				attachmentElement.addAttribute("name", name);
				if (value != null) {
					if (value instanceof byte[]) {
						attachmentElement.addCDATA(new String((byte[]) value,
								"UTF-8"));
					} else {
						attachmentElement.addCDATA(value.toString());
					}
				}
			}
			for (int i = 0; i < message.getAttachment().getUnnamedCount(); i++) {
				final Object value = message.getAttachment().itemAt(i);
				final Element attachmentElement = attachmentsElement
						.addElement("attachment");
				if (value != null) {
					if (value instanceof byte[]) {
						attachmentElement.addCDATA(new String((byte[]) value,
								"UTF-8"));
					} else {
						attachmentElement.addCDATA(value.toString());
					}
				}
			}
		} catch (final Exception e) {
		}
	}

	private void dumpLogMessage(final PrintWriter out,
			final LogMessage logMessage) {
		out.println("-------");
		out.print("Service category: ");
		out.println(logMessage.getServiceCategory());
		out.print("Service name: ");
		out.println(logMessage.getServiceName());
		out.print("Message-Id: ");
		out.println(logMessage.getMessageId());
		out.print("Corrlation-Id: ");
		out.println(logMessage.getCorrelationId());
		out.print("State: ");
		out.println(logMessage.getState());
		out.print("JBPM ProcessInstance Id: ");
		out.println(logMessage.getJbpmProcessInstanceId());
		out.print("JBPM Token Id: ");
		out.println(logMessage.getJbpmTokenId());
		out.print("JBPM Node Id: ");
		out.println(logMessage.getJbpmNodeId());
		if (logMessage.getTags() != null) {
			out.println("Tags:");
			for (final Map.Entry<String, LogTag> entry : logMessage.getTags()
					.entrySet()) {
				out.print("  ");
				out.print(entry.getKey());
				out.print("=");
				out.println(entry.getValue().getTagValue());
			}
		}
		if (logMessage.getFaultReason() != null) {
			out.print("Fault reason: ");
			out.println(logMessage.getFaultReason());
		}
		if (logMessage.getFaultCause() != null) {
			try {
				final StringBuffer faultCause = new StringBuffer();
				final char buffer[] = new char[8192];
				int readed;
				final Reader reader = logMessage.getFaultCause()
						.getCharacterStream();

				while ((readed = reader.read(buffer)) > 0) {
					faultCause.append(buffer, 0, readed);
				}
				reader.close();

				out.println("Fault cause:");
				out.println(faultCause);
			} catch (final Exception e) {
			}
		}
		out.println("-------");
		try {
			final StringBuffer encoded = new StringBuffer();
			final char buffer[] = new char[8192];
			int readed;
			final Reader reader = logMessage.getContent().getCharacterStream();

			while ((readed = reader.read(buffer)) > 0) {
				encoded.append(buffer, 0, readed);
			}
			reader.close();

			final Message message = Util.deserialize(Encoding
					.decodeToObject(encoded.toString()));

			out.println("Properties:");
			for (final String name : message.getProperties().getNames()) {
				out.print("  ");
				out.print(name);
				out.print("=");
				out.println(message.getProperties().getProperty(name));
			}
			out.println("Body:");
			for (final String name : message.getBody().getNames()) {
				out.print("  ");
				out.print(name);
				out.print("=");
				out.println(message.getBody().get(name));
			}
			out.println("Attachment:");
			for (final String name : message.getAttachment().getNames()) {
				out.print("  ");
				out.print(name);
				out.print("=");
				out.println(message.getAttachment().get(name));
			}
			out.println("Fault:");
			out.print("  Reason:");
			out.println(message.getFault().getReason());
			if (message.getFault().getCause() != null) {
				message.getFault().getCause().printStackTrace(out);
			}
		} catch (final Exception e) {
		}
		out.println("-------");
		out.println();
	}

	public long getCurrentPosition() {
		Session session = null;

		try {
			userTransaction.begin();
			session = this.sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogMessage.class);
			criteria.setProjection(Projections.max("id"));

			final Object result = criteria.uniqueResult();

			if (result != null && result instanceof Long) {
				return (Long) result;
			}
		} catch (final Exception e) {
			LOG.error("Failed to store message", e);
		} finally {
			if (session != null) {
				session.close();
			}
			try {
				if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
					userTransaction.rollback();
			} catch (Exception e) {
			}
		}
		return 0;
	}

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

	/**
	 * {@inheritDoc}
	 */
	public String showMessagesByProcessInstanceId(final long processInstanceId) {
		final EncodedStringWriter writer = new EncodedStringWriter();
		final PrintWriter out = new PrintWriter(writer);
		Session session = null;

		try {
			userTransaction.begin();
			session = this.sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogMessage.class);
			criteria.add(Restrictions.eq("jbpmProcessInstanceId",
					processInstanceId));
			criteria.addOrder(Order.asc("id"));

			final ScrollableResults result = criteria
					.scroll(ScrollMode.FORWARD_ONLY);

			int count = 0;
			while (result.next()) {
				final LogMessage logMessage = (LogMessage) result.get(0);

				dumpLogMessage(out, logMessage);

				if (++count % 20 == 0) {
					session.clear();
				}
			}
			session.clear();
			out.flush();
		} catch (final Exception e) {
			LOG.error("Failed to store message", e);
		} finally {
			if (session != null) {
				session.close();
			}
			try {
				if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
					userTransaction.rollback();
			} catch (Exception e) {
			}
		}
		return writer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String showMessagesByProcessInstanceIdAsHtml(
			final long processInstanceId) {
		String retVal = null;

		try {
			final String logMessagesXml = showMessagesByProcessInstanceIdAsXml(processInstanceId);

			final Document logMessages = DocumentHelper
					.parseText(logMessagesXml);

			final ByteArrayOutputStream bos = new ByteArrayOutputStream();

			this.transformer.transform(new DocumentSource(logMessages),
					new StreamResult(bos));

			retVal = new String(bos.toByteArray(), "UTF-8");
		} catch (final Exception ex) {
			LOG.error("showMessagesByProcessInstanceIdAsHtml: ex = ", ex);
		}

		return retVal;

	}

	/**
	 * {@inheritDoc}
	 */
	public String showMessagesByProcessInstanceIdAsXml(
			final long processInstanceId) {
		final StringWriter writer = new StringWriter();
		Session session = null;

		try {
			userTransaction.begin();
			final Document document = DocumentFactory.getInstance()
					.createDocument();
			final Element root = document.addElement("log-messages");

			session = this.sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogMessage.class);
			criteria.add(Restrictions.eq("jbpmProcessInstanceId",
					processInstanceId));
			criteria.addOrder(Order.asc("id"));

			final ScrollableResults result = criteria
					.scroll(ScrollMode.FORWARD_ONLY);

			int count = 0;
			while (result.next()) {
				final LogMessage logMessage = (LogMessage) result.get(0);

				dumpLogMessage(root, logMessage);

				if (++count % 20 == 0) {
					session.clear();
				}
			}
			session.clear();
			final OutputFormat outformat = OutputFormat.createPrettyPrint();
			final XMLWriter xmlWriter = new XMLWriter(writer, outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (final Exception e) {
			LOG.error("Failed to store message", e);
		} finally {
			if (session != null) {
				session.close();
			}
			try {
				if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
					userTransaction.rollback();
			} catch (Exception e) {
			}
		}
		return writer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String showMessagesByTag(final String tag, final String value,
			final long position) {
		final EncodedStringWriter writer = new EncodedStringWriter();
		final PrintWriter out = new PrintWriter(writer);
		Session session = null;

		try {
			userTransaction.begin();
			session = this.sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogTag.class);
			criteria.add(Restrictions.and(Restrictions.and(Restrictions.eq(
					"name", tag), Restrictions.eq("tagValue", value)),
					Restrictions.gt("logMessage.id", position)));
			criteria.addOrder(Order.asc("logMessage.id"));

			final ScrollableResults result = criteria
					.scroll(ScrollMode.FORWARD_ONLY);

			int count = 0;
			while (result.next()) {
				final LogTag logTag = (LogTag) result.get(0);
				final LogMessage logMessage = logTag.getLogMessage();

				dumpLogMessage(out, logMessage);

				if (++count % 20 == 0) {
					session.clear();
				}
			}
			session.clear();
			out.flush();
		} catch (final Exception e) {
			LOG.error("Failed to store message", e);
		} finally {
			if (session != null) {
				session.close();
			}
			try {
				if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
					userTransaction.rollback();
			} catch (Exception e) {
			}
		}
		return writer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public String showMessagesByTagXML(final String tag, final String value,
			final long position) {
		final StringWriter writer = new StringWriter();
		Session session = null;

		try {
			userTransaction.begin();
			final Document document = DocumentFactory.getInstance()
					.createDocument();
			final Element root = document.addElement("log-messages");

			session = this.sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogTag.class);
			criteria.add(Restrictions.and(Restrictions.and(Restrictions.eq(
					"name", tag), Restrictions.eq("tagValue", value)),
					Restrictions.gt("logMessage.id", position)));
			criteria.addOrder(Order.asc("logMessage.id"));

			final ScrollableResults result = criteria
					.scroll(ScrollMode.FORWARD_ONLY);

			int count = 0;
			while (result.next()) {
				final LogTag logTag = (LogTag) result.get(0);
				final LogMessage logMessage = logTag.getLogMessage();

				dumpLogMessage(root, logMessage);

				if (++count % 20 == 0) {
					session.clear();
				}
			}
			session.clear();
			final OutputFormat outformat = OutputFormat.createPrettyPrint();
			final XMLWriter xmlWriter = new XMLWriter(writer, outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (final Exception e) {
			LOG.error("Failed to store message", e);
		} finally {
			if (session != null) {
				session.close();
			}
			try {
				if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
					userTransaction.rollback();
			} catch (Exception e) {
			}
		}
		return writer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public int countMessages(long position) {
		Session session = null;

		try {
			userTransaction.begin();
			session = this.sessionFactory.openSession();

			final Criteria criteria = session.createCriteria(LogMessage.class);
			criteria.add(Restrictions.gt("id", position));
			criteria.setProjection(Projections.rowCount());

			return (Integer) criteria.uniqueResult();
		} catch (final Exception e) {
			LOG.error("Failed to store message", e);
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				session.close();
			}
			try {
				if (userTransaction.getStatus() == Status.STATUS_ACTIVE)
					userTransaction.rollback();
			} catch (Exception e) {
			}
		}
	}

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

		final Dialect dialect = DialectFactory.determineDialect(vendor,
				majorVersion);

		final AnnotationConfiguration cfg = new AnnotationConfiguration();

		cfg.addAnnotatedClass(LogTag.class);
		cfg.addAnnotatedClass(LogMessage.class);
		cfg.addAnnotatedClass(LogJmsDeadLetter.class);
		cfg.addAnnotatedClass(EsbMessage.class);
		cfg.setProperty(Environment.SESSION_FACTORY_NAME,
				LOG_SESSIONFACTORY_JNDI);
		cfg.setProperty(Environment.DATASOURCE, this.dataSourceJndiName);
		cfg.setProperty(Environment.TRANSACTION_MANAGER_STRATEGY,
				JBossTransactionManagerLookup.class.getName());
		cfg.setProperty(Environment.TRANSACTION_STRATEGY,
				JTATransactionFactory.class.getName());
		cfg.setProperty(Environment.DIALECT, dialect.getClass().getName());

		final SchemaUpdate schemaUpdate = new SchemaUpdate(cfg);

		schemaUpdate.execute(true, true);

		this.sessionFactory = cfg.buildSessionFactory();

		final TransformerFactory factory = TransformerFactory.newInstance();
		this.transformer = factory.newTransformer(new StreamSource(
				LogStoreService.class
						.getResourceAsStream("logstore-report.xsl")));

	}

	/**
	 * {@inheritDoc}
	 */
	public void stop() {
		this.sessionFactory = null;
	}

	private final static class EncodedStringWriter extends Writer {
		StringBuffer buffer = new StringBuffer();

		@Override
		public void close() throws IOException {
		}

		@Override
		public void flush() throws IOException {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.buffer.toString();
		}

		@Override
		public void write(final char[] cbuf, final int off, final int len)
				throws IOException {
			for (int i = 0; i < len; i++) {
				final char c = cbuf[i + off];

				switch (c) {
				case '&':
					this.buffer.append("&amp;");
					break;
				case '<':
					this.buffer.append("&lt;");
					break;
				case '>':
					this.buffer.append("&gt;");
					break;
				case '\"':
					this.buffer.append("&quot;");
					break;
				default:
					this.buffer.append(c);
				}
			}
		}
	}

}
