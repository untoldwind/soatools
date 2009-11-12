package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.objectcode.soatools.logstore.test.LogStoreJMXHelper;
import de.objectcode.soatools.test.service.consumer.CounterServiceJMXHelper;
import de.objectcode.soatools.test.service.error.ErrorServiceJMXHelper;

public class ErrorTest {
	private static JMSGatewayHelper jmsGatewayHelper;
	private static CounterServiceJMXHelper counterService;
	private static ErrorServiceJMXHelper errorService;
	private static LogStoreJMXHelper logStoreService;

	@BeforeClass
	public static void init() throws Exception {
		jmsGatewayHelper = new JMSGatewayHelper();

		counterService = new CounterServiceJMXHelper();
		errorService = new ErrorServiceJMXHelper();
		logStoreService = new LogStoreJMXHelper();
	}

	@Before
	public void setup() throws Exception {
		counterService.reset();
	}

	@Test
	public void testRuntimeRetryWithoutLog() throws Exception {
		errorService.setExceptionType("RUNTIME");
		errorService.setFailureCount(2);

		Map<String, Object> body = new HashMap<String, Object>();

		body.put("consumerTag", "runtime-error");
		body.put(JMSGatewayHelper.DEFAULT_LOCATION, "runtime-error");
		body.put("testCaseName", "ErrorTest.testRuntimeRetry");
		body.put("testCaseCount", 0);

		jmsGatewayHelper.sendSingle("SoatoolsTest", "ErrorWithoutLog", body);

		int counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() < 1) {
			Thread.sleep(500);
			counter++;
		}
		Thread.sleep(1000L);

		assertEquals(1, counterService.getInvokationCounter());
		assertEquals(1, counterService.getErrorCounter());

		body = new HashMap<String, Object>();

		jmsGatewayHelper.sendSingle("SoatoolsTest", "RedeliverTrigger", body);

		counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() < 2) {
			Thread.sleep(500);
			counter++;
		}
		Thread.sleep(1000L);

		assertEquals(2, counterService.getInvokationCounter());
		assertEquals(2, counterService.getErrorCounter());

		jmsGatewayHelper.sendSingle("SoatoolsTest", "RedeliverTrigger", body);

		counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() < 3) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(3, counterService.getInvokationCounter());
		assertEquals(2, counterService.getErrorCounter());
	}
}
