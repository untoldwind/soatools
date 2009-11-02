package de.objectcode.soatools.msmq.ws;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.Properties;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;

@WebService(name = "MsmqInGateway", targetNamespace = "http://objectcode.de/msmq/incoming")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class MsmqInGatewayWS {
	private static final Log LOGGER = LogFactory.getLog(MsmqInGatewayWS.class);

	@WebResult(name = "success")
	public boolean routeMessage(
			@WebParam(name = "serviceCategory") final String serviceCategory,
			@WebParam(name = "serviceName") final String serviceName,
			@WebParam(name = "message") final MsmqInMessage msmqMessage) {
		try {
			final Message message = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			final Properties properties = message.getProperties();

			if (msmqMessage.getId() != null) {
				properties.setProperty("msmq-id", msmqMessage.getId());
			}
			if (msmqMessage.getLabel() != null) {
				properties.setProperty("msmq-label", msmqMessage.getLabel());
			}
			if (msmqMessage.getPriority() != null) {
				properties.setProperty("msmq-priority", msmqMessage
						.getPriority());
			}
			if (msmqMessage.getMessageType() != null) {
				properties.setProperty("msmq-messageType", msmqMessage
						.getMessageType());
			}
			if (msmqMessage.getSenderId() != null) {
				properties.setProperty("msmq-senderId", msmqMessage
						.getSenderId());
			}
			if (msmqMessage.getArrivedTime() != null) {
				properties.setProperty("msmq-arrivedTime", msmqMessage
						.getArrivedTime());
			}
			if (msmqMessage.getCorrelationId() != null) {
				properties.setProperty("msmq-correlationId", msmqMessage
						.getCorrelationId());
			}

			Date sentTime = msmqMessage.getSentTime();
			LOGGER.debug("routeMessage  : @memo sentTime = " + sentTime);

			if (sentTime != null) {
				properties.setProperty("msmq-sentTime", sentTime);
			}

			byte[] body = msmqMessage.getBody();

			if (LOGGER.isDebugEnabled()) {
				try {
					String bodyAsString = new String(body, "UTF-8");
					LOGGER.debug("routeMessage  : @memo bodyAsString = "
							+ bodyAsString);
				} catch (final RuntimeException ex) {
					LOGGER.error("routeMessage: ex = ", ex);
				}
			}

			message.getBody().add(body);

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
