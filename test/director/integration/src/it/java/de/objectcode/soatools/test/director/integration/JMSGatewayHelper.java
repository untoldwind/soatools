package de.objectcode.soatools.test.director.integration;

import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

public class JMSGatewayHelper {

	public static final String DEFAULT_LOCATION = "org.jboss.soa.esb.message.defaultEntry";

	private QueueConnectionFactory queueConnectionFactory;
	private Queue queue;

	public JMSGatewayHelper() throws Exception {
		InitialContext ctx = new InitialContext();

		queueConnectionFactory = (QueueConnectionFactory) ctx
				.lookup("ConnectionFactory");
		queue = (Queue) ctx.lookup("queue/soatool_test_jms_gateway");
	}

	public void sendSingle(String serviceCategory, String serviceName,
			Map<String, Object> body) throws Exception {

		QueueConnection connection = null;
		QueueSession session = null;
		try {
			connection = queueConnectionFactory.createQueueConnection();

			connection.start();

			session = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);

			QueueSender sender = session.createSender(queue);

			MapMessage message = session.createMapMessage();

			message.setStringProperty("destServiceCategory", serviceCategory);
			message.setStringProperty("destServiceName", serviceName);

			for (Map.Entry<String, Object> entry : body.entrySet()) {
				message.setObject(entry.getKey(), entry.getValue());
			}

			sender.send(message);

			sender.close();
		} finally {
			if (session != null)
				session.close();
			if (connection != null)
				connection.close();
		}
	}
}
