package de.objectcode.soatools.util.value;

import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;

/**
 * Used to define static values in the configuration.
 * 
 * This locator always returns the same value for all messages. It can not be
 * used to set values in the message.
 * 
 * @author junglas
 */
public class StaticValueLocator implements IValueLocator {
	final String value;

	public StaticValueLocator(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(Message message) throws ActionProcessingException {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(Message message, Object value)
			throws ActionProcessingException {
		throw new ActionProcessingException("Trying to set a static value");
	}

}
