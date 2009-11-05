package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;

import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.objectcode.soatools.test.service.consumer.CounterServiceJMXHelper;
import de.objectcode.soatools.test.service.jbpm.JbpmProcessCounterServiceJMXHelper;

public class JbpmProcessesTest {
	private static ServiceInvoker jbpmProcessesStartService;
	private static CounterServiceJMXHelper counterService;
	private static JbpmProcessCounterServiceJMXHelper jbpmProcessCounterService;

	@BeforeClass
	public static void init() throws Exception {
		jbpmProcessesStartService = new ServiceInvoker("SoatoolsTest",
				"JBPMProcessesStart");

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
			final Message message = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			message.getBody().add("jbpmProcessDefName", "test-process1");
			message.getBody().add("consumerTag", String.valueOf(i));
			message.getBody().add("jbpmProcessKey", String.valueOf(i));

			jbpmProcessesStartService.deliverAsync(message);
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
			final Message message = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			message.getBody().add("jbpmProcessDefName", "test-process2");
			message.getBody().add("consumerTag", String.valueOf(i));
			message.getBody().add("jbpmProcessKey", String.valueOf(i));

			jbpmProcessesStartService.deliverAsync(message);
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
