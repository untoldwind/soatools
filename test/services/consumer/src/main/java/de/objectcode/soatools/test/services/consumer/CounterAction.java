package de.objectcode.soatools.test.services.consumer;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class CounterAction extends AbstractActionPipelineProcessor {

	public CounterAction(ConfigTree config) throws ConfigurationException {
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		Counter.INSTANCE.invokationCounter.incrementAndGet();

		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processException(Message message, Throwable th) {
		Counter.INSTANCE.errorCounter.incrementAndGet();
	}
}
