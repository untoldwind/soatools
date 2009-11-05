package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.objectcode.soatools.logstore.test.LogStoreJMXHelper;
import de.objectcode.soatools.test.service.consumer.CounterServiceJMXHelper;

public class ConsumerTest {

	private static JMSGatewayHelper jmsGatewayHelper;
	private static CounterServiceJMXHelper counterService;
	private static LogStoreJMXHelper logStoreService;

	@BeforeClass
	public static void init() throws Exception {
		jmsGatewayHelper = new JMSGatewayHelper();

		counterService = new CounterServiceJMXHelper();
		logStoreService = new LogStoreJMXHelper();
	}

	@Before
	public void setup() throws Exception {
		counterService.reset();
	}

	@Test
	public void testWithoutLog() throws Exception {
		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("consumerTag", String.valueOf(i));
			body.put(JMSGatewayHelper.DEFAULT_LOCATION, i);

			jmsGatewayHelper.sendSingle("SoatoolsTest", "ConsumerWithoutLog",
					body);
		}

		int counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() != IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(IConstants.MESSAGE_COUNT, counterService
				.getInvokationCounter());
	}

	@Test
	public void testWithLog() throws Exception {
		long initialLogPosition = logStoreService.getCurrentPosition();

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("consumerTag", String.valueOf(i));
			body.put(JMSGatewayHelper.DEFAULT_LOCATION, i);

			jmsGatewayHelper
					.sendSingle("SoatoolsTest", "ConsumerWithLog", body);
		}
		int counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() != IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(IConstants.MESSAGE_COUNT, counterService
				.getInvokationCounter());

		counter = 0;
		while (counter < IConstants.WAIT_COUNT
				&& logStoreService.getCurrentPosition() != initialLogPosition
						+ IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(initialLogPosition + IConstants.MESSAGE_COUNT,
				logStoreService.getCurrentPosition());

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Document logMessage = logStoreService.getLogMessagesByTag(
					"consumerTag", String.valueOf(i), initialLogPosition);

			assertNotNull(logMessage);
			assertEquals(1, logMessage.getRootElement().elements().size());
		}
	}

	public static void main(String[] args) {
		try {
			InitialContext ctx = new InitialContext();

			QueueConnectionFactory qcf = (QueueConnectionFactory) ctx
					.lookup("ConnectionFactory");
			Queue queue = (Queue) ctx.lookup("queue/soatool_test_jms_gateway");

			QueueConnection connection = qcf.createQueueConnection();

			connection.start();

			QueueSession session = connection.createQueueSession(false,
					Session.AUTO_ACKNOWLEDGE);

			QueueSender sender = session.createSender(queue);

			MapMessage message = session.createMapMessage();
			message.setString("bla", "blib");
			message.setStringProperty("destServiceCategory", "SoatoolsTest");
			message.setStringProperty("destServiceName", "ConsumerWithoutLog");

			sender.send(message);

			session.close();
			connection.close();

			System.out.println(">>" + queue);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
