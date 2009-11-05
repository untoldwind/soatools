package de.objectcode.soatools.test.service.jmsgateway;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;

public class ForwardMessage extends AbstractActionPipelineProcessor {

	public ForwardMessage(ConfigTree config) throws ConfigurationException {
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		String destServiceCategory = (String) message.getProperties()
				.getProperty("destServiceCategory");
		String destServiceName = (String) message.getProperties().getProperty(
				"destServiceName");

		if (destServiceCategory == null || destServiceName == null)
			throw new ActionProcessingException(
					"Destination service not defined!");

		try {
			final ServiceInvoker invoker = new ServiceInvoker(
					destServiceCategory, destServiceName);

			invoker.deliverAsync(message);

			return null;
		} catch (MessageDeliverException e) {
			throw new ActionProcessingException(e);
		}
	}

}
