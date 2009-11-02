package de.objectcode.soatools.logstore.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.ScheduledEventMessageComposer;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.schedule.SchedulingException;

/**
 * Create an ESB message from a schedule provider to be consumed by a
 * PurgeLogStore action.
 * 
 * @author junglas
 */
public class PurgeLogStoreMessageComposer implements
		ScheduledEventMessageComposer {
	/** Logger for this class. */
	private static final Log LOGGER = LogFactory
			.getLog(PurgeLogStoreMessageComposer.class);

	public Message composeMessage() throws SchedulingException {
		LOGGER.debug("composeMessage  : @memo");

		final Message message = MessageFactory.getInstance().getMessage(
				MessageType.JBOSS_XML);

		return message;
	}

	public void initialize(final ConfigTree config)
			throws ConfigurationException {
		LOGGER.debug("initialize  : @memo config = " + config);
	}

	public Message onProcessingComplete(final Message message)
			throws SchedulingException {
		final Message retVal = null;

		LOGGER.debug("onProcessingComplete: @return retVal = " + retVal);

		return retVal;
	}

	public void uninitialize() {
		LOGGER.debug("uninitialize  : @memo");
	}
}
