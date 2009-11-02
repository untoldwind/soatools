package de.objectcode.soatools.test.director.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.ws.BindingProvider;

import org.dom4j.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.objectcode.soatools.logstore.test.LogStoreJMXHelper;
import de.objectcode.soatools.test.service.consumer.CounterServiceJMXHelper;
import de.objectcode.soatools.test.webservicein.client.EsbMessage;
import de.objectcode.soatools.test.webservicein.client.KeyValue;
import de.objectcode.soatools.test.webservicein.client.WebserviceIn;
import de.objectcode.soatools.test.webservicein.client.WebserviceInWSService;

public class WebserviceInTest {
	private static CounterServiceJMXHelper counterService;
	private static LogStoreJMXHelper logStoreService;

	private static WebserviceIn webserviceIn;

	@BeforeClass
	public static void init() throws Exception {
		counterService = new CounterServiceJMXHelper();
		logStoreService = new LogStoreJMXHelper();

		WebserviceInWSService service = new WebserviceInWSService();

		webserviceIn = service.getWebserviceInPort();

		final BindingProvider bp = (BindingProvider) webserviceIn;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"http://127.0.0.1:8080/test-webservice-in/WebserviceInWS");
	}

	@Before
	public void setup() throws Exception {
		counterService.reset();
	}

	@Test
	public void testWithoutLog() throws Exception {
		for (int i = 0; i < 100; i++) {
			EsbMessage message = new EsbMessage();

			KeyValue defaultBody = new KeyValue();
			defaultBody.setKey("");
			defaultBody.setValue(String.valueOf(i));
			message.getBody().add(defaultBody);

			KeyValue consumerTagBody = new KeyValue();
			consumerTagBody.setKey("consumerTag");
			consumerTagBody.setValue(String.valueOf(i));
			message.getBody().add(consumerTagBody);

			webserviceIn.routeMessage("SoatoolsTest", "ConsumerWithoutLog",
					message);
		}

		int counter = 0;

		while (counter < 20 && counterService.getInvokationCounter() != 100) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(100, counterService.getInvokationCounter());
	}

	@Test
	public void testWithLog() throws Exception {
		long initialLogPosition = logStoreService.getCurrentPosition();

		for (int i = 0; i < 100; i++) {
			EsbMessage message = new EsbMessage();

			KeyValue defaultBody = new KeyValue();
			defaultBody.setKey("");
			defaultBody.setValue(String.valueOf(i));
			message.getBody().add(defaultBody);

			KeyValue consumerTagBody = new KeyValue();
			consumerTagBody.setKey("consumerTag");
			consumerTagBody.setValue(String.valueOf(i));
			message.getBody().add(consumerTagBody);

			webserviceIn.routeMessage("SoatoolsTest", "ConsumerWithLog",
					message);
		}
		int counter = 0;

		while (counter < 20 && counterService.getInvokationCounter() != 100) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(100, counterService.getInvokationCounter());

		counter = 0;
		while (counter < 20
				&& logStoreService.getCurrentPosition() != initialLogPosition + 100) {
			Thread.sleep(500);
			counter++;
		}

		assertEquals(initialLogPosition + 100, logStoreService
				.getCurrentPosition());

		for (int i = 0; i < 100; i++) {
			Document logMessage = logStoreService.getLogMessagesByTag(
					"consumerTag", String.valueOf(i), initialLogPosition);

			assertNotNull(logMessage);
			assertEquals(1, logMessage.getRootElement().elements().size());
		}
	}
}
