package de.objectcode.soatools.test.service.webservicein;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;

@WebService(name = "WebserviceIn", targetNamespace = "http://objectcode.de/test/webservicein")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WebserviceInWS {
	private static final Log LOGGER = LogFactory.getLog(WebserviceInWS.class);

	@WebResult(name = "success")
	public boolean routeMessage(
			@WebParam(name = "serviceCategory") final String serviceCategory,
			@WebParam(name = "serviceName") final String serviceName,
			@WebParam(name = "message") final ESBMessage esbMessage) {
		try {
			final Message message = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			if (esbMessage.getBody() != null) {
				for (KeyValue keyValue : esbMessage.getBody()) {
					if (keyValue.getKey() == null
							|| keyValue.getKey().length() == 0) {
						message.getBody().add(keyValue.getValue());
					} else {
						message.getBody().add(keyValue.getKey(),
								keyValue.getValue());
					}
				}
			}
			if (esbMessage.getProperties() != null) {
				for (KeyValue keyValue : esbMessage.getBody()) {
					message.getProperties().setProperty(keyValue.getKey(),
							keyValue.getValue());
				}
			}

			final ServiceInvoker invoker = new ServiceInvoker(serviceCategory,
					serviceName);

			invoker.deliverAsync(message);

			return true;

		} catch (final Exception e) {
			LOGGER.error("Exception", e);
		}
		return false;
	}

}
