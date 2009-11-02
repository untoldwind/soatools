package de.objectcode.soatools.util.value;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;

/**
 * Special version of the ObjectPathValueLocator that expectes the value to be a
 * String.
 * 
 * If the message body contains anything else but a string the default value is
 * returned. This may be useful in some situations.
 * 
 * @author junglas
 */
public class StringPathValueLocator implements IValueLocator {
	/** Logger for this class. */
	private static final Log LOGGER = LogFactory
			.getLog(StringPathValueLocator.class);

	final String objectPath;
	final String defaultValue;
	final ObjectMapper objectMapper = new ObjectMapper();

	public StringPathValueLocator(String objectPath, String defaultValue) {
		this.objectPath = objectPath;
		this.defaultValue = defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getValue(Message message) throws ActionProcessingException {
		String retVal = null;

		try {
			LOGGER.debug("getValue  : @memo objectPath = " + objectPath);

			String value = (String) objectMapper.getObjectFromMessage(message,
					objectPath);

			if (value == null) {
				retVal = defaultValue;
			} else {
				retVal = value;
			}
		} catch (ObjectMappingException e) {
			throw new ActionProcessingException(e);
		}

		return retVal;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(Message message, Object value)
			throws ActionProcessingException {
		try {
			objectMapper.setObjectOnMessage(message, objectPath, value);
		} catch (ObjectMappingException e) {
			throw new ActionProcessingException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "ObjectPathValueLocator(" + objectPath + ")";
	}

}
