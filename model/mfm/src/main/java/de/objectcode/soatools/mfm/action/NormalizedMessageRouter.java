package de.objectcode.soatools.mfm.action;

import java.util.Collection;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;

import de.objectcode.soatools.mfm.api.collector.MessageBodyCollector;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

public class NormalizedMessageRouter extends AbstractActionPipelineProcessor {
	final MessagePayloadProxy payloadProxy;
	final String serviceCategory;
	final String serviceName;
	final ServiceInvoker serviceInvoker;

	public NormalizedMessageRouter(ConfigTree config)
			throws ConfigurationException {
		payloadProxy = new MessagePayloadProxy(config);
		serviceCategory = config.getRequiredAttribute("service-category");
		serviceName = config.getRequiredAttribute("service-name");

		try {
			serviceInvoker = new ServiceInvoker(serviceCategory, serviceName);
		} catch (MessageDeliverException e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		try {
			Object data = payloadProxy.getPayload(message);

			if (data instanceof Collection<?>) {
				for (Object element : (Collection<?>) data) {
					route((NormalizedData) element);
				}
			} else {
				route((NormalizedData) data);
			}
		} catch (Exception e) {
			throw new ActionProcessingException(e);
		}

		return null;
	}

	protected void route(NormalizedData data) throws ActionProcessingException {
		Message message = MessageFactory.getInstance().getMessage(
				MessageType.JAVA_SERIALIZED);

		data.marshal(new MessageBodyCollector(message));

		try {
			serviceInvoker.deliverAsync(message);
		} catch (MessageDeliverException e) {
			throw new ActionProcessingException(e);
		}
	}
}
