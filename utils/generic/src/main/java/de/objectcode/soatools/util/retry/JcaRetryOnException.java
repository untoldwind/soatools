package de.objectcode.soatools.util.retry;

import static org.jboss.soa.esb.notification.jms.JMSPropertiesSetter.JMS_REDELIVERED;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.Service;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.services.persistence.MessageStore;

/**
 * Add an automatic retry to a service.
 * 
 * This is only supposed to work in combination with a transaction JCA-JMS
 * listener.
 * 
 * By default JMS will perform an redelivery by itself if the message processor
 * throws an exception (i.e. the transaction failed). Unluckily this redelivery
 * usually is way to fast in certain situations, e.g.: The service performs a
 * synchronous call to a sub-system that is on scheduled downtime.
 * 
 * This action checks if a message was redelivered by the JMS. In this case the
 * message will be routed to the ESB message store with classification RDLVR,
 * i.e. the ESB message store will perform a redelivery some time late (5
 * minutes by default).
 * 
 * By default there will be 20 retries before the message eventually will be
 * send the the ESB DLQ.
 * 
 * @author junglas
 */
public class JcaRetryOnException extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(JcaRetryOnException.class);

	public final static String RETRY_COUNT = "de.objectcode.soatools.retryCount";

	private final Service service;
	private final ServiceInvoker dlqServiceInvoker;
	private final int maxRetries;
	private final boolean throwFault;

	public JcaRetryOnException(ConfigTree config) throws ConfigurationException {
		String serviceCategory = config.getParent().getRequiredAttribute(
				"service-category");
		String serviceName = config.getParent().getRequiredAttribute(
				"service-name");

		service = new Service(serviceCategory, serviceName);
		maxRetries = (int) config.getLongAttribute("max-retries", 20);
		throwFault = config.getBooleanAttribute("throw-fault", true);

		try {
			dlqServiceInvoker = new ServiceInvoker(ServiceInvoker.dlqService);
		} catch (MessageDeliverException e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processException(Message message, Throwable th) {
		// We just log the error at this point. The transaction has failed
		// already, so leave it to the JMS to perform a redelivery in a new
		// transaction
		if (th instanceof RuntimeException) {
			LOG.info("Got RuntimeException that will lead to a retry", th);
		}
		ExceptionCache.getInstance().addException(
				message.getHeader().getCall().getMessageID().toString(), th);
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		final Boolean redelivered = (Boolean) message.getProperties()
				.getProperty(JMS_REDELIVERED);

		if (redelivered) {
			// The message has been redelivered by the JMS, i.e. on the first
			// try there had been some kind of failure leading to a rollback of
			// the transaction.
			int retryCount = (Integer) message.getProperties().getProperty(
					RETRY_COUNT, new Integer(0));

			if (retryCount >= maxRetries) {
				LOG.error("Failed to process message " + retryCount + " times");
				if (throwFault) {
					Throwable cause = ExceptionCache.getInstance()
							.findException(
									message.getHeader().getCall()
											.getMessageID().toString());
					if (cause != null) {
						throw new ActionProcessingException(
								"Max redeliver count reached in service ("
										+ service.getCategory() + ","
										+ service.getName() + ") cause: "
										+ cause.getMessage(), cause);
					} else {
						throw new ActionProcessingException(
								"Max redeliver count reached in service ("
										+ service.getCategory() + ","
										+ service.getName() + ")");
					}
				} else {
					message.getProperties().setProperty(
							MessageStore.CLASSIFICATION,
							MessageStore.CLASSIFICATION_DLQ);
				}
			} else {
				message.getProperties().setProperty(
						MessageStore.CLASSIFICATION,
						MessageStore.CLASSIFICATION_RDLVR);
			}
			message.getProperties().setProperty(ServiceInvoker.DELIVER_TO,
					service);
			message.getProperties().setProperty(RETRY_COUNT,
					new Integer(retryCount + 1));

			try {
				dlqServiceInvoker.deliverAsync(message);

				LOG.info("Send redelivered JMS message to MessageStore for retry");

				return null;
			} catch (MessageDeliverException e) {
				LOG.error("Failed to deliver rety to DLQ", e);
			}
		}

		return message;
	}
}
