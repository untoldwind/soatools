package de.objectcode.soatools.logstore.test;

import java.io.StringReader;
import java.util.Date;

import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

public class LogStoreJMXHelper {
	ObjectName logStoreName;
	Queue logStoreQueue;

	QueueConnectionFactory queueConnectionFactory;
	RMIAdaptor server;

	public LogStoreJMXHelper() throws Exception {
		SecurityAssociation.setPrincipal(new SimplePrincipal("admin"));
		SecurityAssociation.setCredential("admin");

		final InitialContext ctx = new InitialContext();

		this.server = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
		this.logStoreName = new ObjectName(
				"de.objectcode.soatools.logstore:service=LogStoreService");

		this.queueConnectionFactory = (QueueConnectionFactory) ctx
				.lookup("ConnectionFactory");
		this.logStoreQueue = (Queue) ctx.lookup("queue/log_store_message");
	}

	public long getCurrentPosition() throws Exception {
		final String[] sig = {};
		final Object[] opArgs = {};
		final Object result = this.server.invoke(this.logStoreName,
				"getCurrentPosition", opArgs, sig);

		return (Long) result;
	}

	public Document getLogMessagesByTag(final String tag, final String value,
			final long position) throws Exception {
		final String[] sig = { "java.lang.String", "java.lang.String", "long" };
		final Object[] opArgs = { tag, value, position };
		final Object result = this.server.invoke(this.logStoreName,
				"showMessagesByTagXML", opArgs, sig);

		final SAXReader reader = new SAXReader();
		return reader.read(new StringReader(result.toString()));
	}

	public int countMessages(long position) throws Exception {
		final String[] sig = { "long" };
		final Object[] opArgs = { position };
		final Object result = this.server.invoke(this.logStoreName,
				"countMessages", opArgs, sig);

		return (Integer) result;
	}

	public void waitForCompletion() throws Exception {
		waitForCompletion(Long.MAX_VALUE);
	}

	public void waitForCompletion(final long millis) throws Exception {
		final long startTimeInMillis = new Date().getTime();

		Thread.sleep(1000);

		final QueueConnection connection = this.queueConnectionFactory
				.createQueueConnection();
		final QueueSession session = connection.createQueueSession(false,
				Session.AUTO_ACKNOWLEDGE);

		try {
			connection.start();

			final QueueBrowser browser = session
					.createBrowser(this.logStoreQueue);

			while (browser.getEnumeration().hasMoreElements()) {
				Thread.sleep(500);

				if ((new Date().getTime() - startTimeInMillis) > millis)
					break;

			}
		} finally {
			session.close();
			connection.stop();
			connection.close();
		}
	}
}
