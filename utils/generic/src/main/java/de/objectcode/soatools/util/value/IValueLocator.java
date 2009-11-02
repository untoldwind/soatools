package de.objectcode.soatools.util.value;

import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;

/**
 * Locate a value in a message.
 * 
 * @author junglas
 */
public interface IValueLocator {

	/**
	 * Get the value from the message.
	 * 
	 * @param message
	 *            The ESB message
	 * @return The value or <tt>null</tt> if not present
	 * @throws ActionProcessingException
	 *             on error
	 */
	Object getValue(Message message) throws ActionProcessingException;

	/**
	 * Set the value in the message.
	 * 
	 * @param message
	 *            The ESB message
	 * @param value
	 *            The value of <tt>null</tt> if not present
	 * @throws ActionProcessingException
	 *             on error
	 */
	void setValue(Message message, Object value)
			throws ActionProcessingException;
}
