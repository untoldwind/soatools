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
import de.objectcode.soatools.test.service.jbpm.JbpmProcessCounterServiceJMXHelper;

public class JbpmProcessesTest {
	private static JMSGatewayHelper jmsGatewayHelper;
	private static CounterServiceJMXHelper counterService;
	private static JbpmProcessCounterServiceJMXHelper jbpmProcessCounterService;
	private static LogStoreJMXHelper logStoreService;

	@BeforeClass
	public static void init() throws Exception {
		jmsGatewayHelper = new JMSGatewayHelper();

		counterService = new CounterServiceJMXHelper();
		jbpmProcessCounterService = new JbpmProcessCounterServiceJMXHelper();
		logStoreService = new LogStoreJMXHelper();
	}

	@Before
	public void setup() throws Exception {
		counterService.reset();
	}

	@Test
	public void testStartTestProcess1() throws Exception {
		long initialLogPosition = logStoreService.getCurrentPosition();

		int processCount = jbpmProcessCounterService
				.countProcessInstances("test-process1");

		assertEquals(processCount, jbpmProcessCounterService
				.countFinishedProcessInstances("test-process1"));

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("jbpmProcessDefName", "test-process1");
			body.put("consumerTag", String.valueOf(i));
			body.put("jbpmProcessKey", String.valueOf(i));
			body.put("testCaseName", "JbpmProcessesTest.testStartTestProcess1");
			body.put("testCaseCount", i);

			jmsGatewayHelper.sendSingle("SoatoolsTest", "JBPMProcessesStart",
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
		assertEquals(processCount + IConstants.MESSAGE_COUNT,
				jbpmProcessCounterService
						.countProcessInstances("test-process1"));

		counter = 0;
		while (counter < IConstants.WAIT_COUNT
				&& logStoreService.countMessages(initialLogPosition) < IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		Document allLogMessages = logStoreService.getLogMessagesByTag(
				"testCaseName", "JbpmProcessesTest.testStartTestProcess1",
				initialLogPosition);
		assertNotNull(allLogMessages);
		assertEquals(IConstants.MESSAGE_COUNT, allLogMessages.getRootElement()
				.elements().size());
	}

	@Test
	public void testStartTestProcess2() throws Exception {
		long initialLogPosition = logStoreService.getCurrentPosition();

		int processCount = jbpmProcessCounterService
				.countProcessInstances("test-process2");

		assertEquals(processCount, jbpmProcessCounterService
				.countFinishedProcessInstances("test-process2"));

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("jbpmProcessDefName", "test-process2");
			body.put("consumerTag", String.valueOf(i));
			body.put("jbpmProcessKey", String.valueOf(i));
			body.put("testCaseName", "JbpmProcessesTest.testStartTestProcess2");
			body.put("testCaseCount", i);

			jmsGatewayHelper.sendSingle("SoatoolsTest", "JBPMProcessesStart",
					body);
		}
		int counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() != 2 * IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(2 * IConstants.MESSAGE_COUNT, counterService
				.getInvokationCounter());
		assertEquals(processCount + IConstants.MESSAGE_COUNT,
				jbpmProcessCounterService
						.countProcessInstances("test-process2"));

		Document allLogMessages = logStoreService.getLogMessagesByTag(
				"testCaseName", "JbpmProcessesTest.testStartTestProcess2",
				initialLogPosition);
		assertNotNull(allLogMessages);
		assertEquals(2 * IConstants.MESSAGE_COUNT, allLogMessages
				.getRootElement().elements().size());
	}

	@Test
	public void testStartTestProcess3() throws Exception {
		long initialLogPosition = logStoreService.getCurrentPosition();

		int processCount = jbpmProcessCounterService
				.countProcessInstances("test-process3");

		assertEquals(processCount, jbpmProcessCounterService
				.countFinishedProcessInstances("test-process3"));

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("jbpmProcessDefName", "test-process3");
			body.put("consumerTag", String.valueOf(i));
			body.put("jbpmProcessKey", String.valueOf(i));
			body.put("testCaseName", "JbpmProcessesTest.testStartTestProcess3");
			body.put("testCaseCount", i);

			jmsGatewayHelper.sendSingle("SoatoolsTest", "JBPMProcessesStart",
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
		assertEquals(processCount + IConstants.MESSAGE_COUNT,
				jbpmProcessCounterService
						.countProcessInstances("test-process3"));

		counter = 0;
		while (counter < IConstants.WAIT_COUNT
				&& logStoreService.countMessages(initialLogPosition) < IConstants.MESSAGE_COUNT) {
			Thread.sleep(500);
			counter++;
		}

		Document allLogMessages = logStoreService.getLogMessagesByTag(
				"testCaseName", "JbpmProcessesTest.testStartTestProcess3",
				initialLogPosition);
		assertNotNull(allLogMessages);
		assertEquals(IConstants.MESSAGE_COUNT, allLogMessages.getRootElement()
				.elements().size());
	}
}
