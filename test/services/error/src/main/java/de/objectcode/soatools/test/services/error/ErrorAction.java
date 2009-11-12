package de.objectcode.soatools.test.services.error;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class ErrorAction extends AbstractActionPipelineProcessor {
	public final static String RETRY_COUNT = "de.objectcode.soatools.retryCount";

	public ErrorAction(ConfigTree config) throws ConfigurationException {
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		int retryCount = (Integer) message.getProperties().getProperty(
				RETRY_COUNT, new Integer(0));

		ErrorState.INSTANCE.throwError(retryCount);

		return message;
	}
}