package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.dom4j.Document;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.objectcode.soatools.logstore.test.LogStoreJMXHelper;
import de.objectcode.soatools.test.service.consumer.CounterServiceJMXHelper;

public class ConsumerTest {

	private static ServiceInvoker consumerWithoutLogServiceInvoker;
	private static ServiceInvoker consumerWithLogServiceInvoker;
	private static CounterServiceJMXHelper counterService;
	private static LogStoreJMXHelper logStoreService;

	@BeforeClass
	public static void init() throws Exception {
		consumerWithoutLogServiceInvoker = new ServiceInvoker("SoatoolsTest",
				"ConsumerWithoutLog");
		consumerWithLogServiceInvoker = new ServiceInvoker("SoatoolsTest",
				"ConsumerWithLog");

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
			final Message message = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			message.getBody().add("consumerTag", String.valueOf(i));
			message.getBody().add(i);

			consumerWithoutLogServiceInvoker.deliverAsync(message);
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
			final Message message = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			message.getBody().add("consumerTag", String.valueOf(i));
			message.getBody().add(i);

			consumerWithLogServiceInvoker.deliverAsync(message);
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
			System.setProperty("javax.xml.registry.ConnectionFactoryClass",
					"org.apache.ws.scout.registry.ConnectionFactoryImpl");

			ConsumerTest test = new ConsumerTest();

			test.testWithoutLog();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
