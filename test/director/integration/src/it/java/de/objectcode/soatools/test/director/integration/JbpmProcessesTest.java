package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.objectcode.soatools.test.service.consumer.CounterServiceJMXHelper;
import de.objectcode.soatools.test.service.jbpm.JbpmProcessCounterServiceJMXHelper;

public class JbpmProcessesTest {
	private static JMSGatewayHelper jmsGatewayHelper;
	private static CounterServiceJMXHelper counterService;
	private static JbpmProcessCounterServiceJMXHelper jbpmProcessCounterService;

	@BeforeClass
	public static void init() throws Exception {
		jmsGatewayHelper = new JMSGatewayHelper();

		counterService = new CounterServiceJMXHelper();
		jbpmProcessCounterService = new JbpmProcessCounterServiceJMXHelper();
	}

	@Before
	public void setup() throws Exception {
		counterService.reset();
	}

	@Test
	public void testStartTestProcess1() throws Exception {
		int processCount = jbpmProcessCounterService
				.countProcessInstances("test-process1");

		assertEquals(processCount, jbpmProcessCounterService
				.countFinishedProcessInstances("test-process1"));

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("jbpmProcessDefName", "test-process1");
			body.put("consumerTag", String.valueOf(i));
			body.put("jbpmProcessKey", String.valueOf(i));
			body.put("testCaseName", "testStartTestProcess1");
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
	}

	@Test
	public void testStartTestProcess2() throws Exception {
		int processCount = jbpmProcessCounterService
				.countProcessInstances("test-process2");

		assertEquals(processCount, jbpmProcessCounterService
				.countFinishedProcessInstances("test-process2"));

		for (int i = 0; i < IConstants.MESSAGE_COUNT; i++) {
			Map<String, Object> body = new HashMap<String, Object>();

			body.put("jbpmProcessDefName", "test-process2");
			body.put("consumerTag", String.valueOf(i));
			body.put("jbpmProcessKey", String.valueOf(i));
			body.put("testCaseName", "testStartTestProcess2");
			body.put("testCaseCount", i);

			jmsGatewayHelper.sendSingle("SoatoolsTest", "JBPMProcessesStart",
					body);
		}
		int counter = 0;

		while (counter < IConstants.WAIT_COUNT
				&& counterService.getInvokationCounter() != 200) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(200, counterService.getInvokationCounter());
		assertEquals(processCount + IConstants.MESSAGE_COUNT,
				jbpmProcessCounterService
						.countProcessInstances("test-process2"));
	}
}
