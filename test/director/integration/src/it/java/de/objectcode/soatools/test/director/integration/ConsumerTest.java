package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

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
				&& logStoreService.countMessages(initialLogPosition) < IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(IConstants.MESSAGE_COUNT, logStoreService
				.countMessages(initialLogPosition));

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Document logMessage = logStoreService.getLogMessagesByTag(
					"consumerTag", String.valueOf(i), initialLogPosition);

			assertNotNull(logMessage);
			assertEquals(1, logMessage.getRootElement().elements().size());
		}
	}
}
