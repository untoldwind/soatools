package de.objectcode.soatools.util.value;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;

/**
 * Locate a value usinging an OGNL like expression.
 * 
 * This is quite similiar to the regular MessagePayloadProxy.
 * 
 * @author junglas
 */
public class ObjectPathValueLocator implements IValueLocator {
	/** Logger for this class. */
	private static final Log LOGGER = LogFactory
			.getLog(ObjectPathValueLocator.class);

	final String objectPath;
	final Object defaultValue;
	final ObjectMapper objectMapper = new ObjectMapper();

	public ObjectPathValueLocator(String objectPath, Object defaultValue) {
		this.objectPath = objectPath;
		this.defaultValue = defaultValue;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(Message message) throws ActionProcessingException {
		Object retVal = null;

		try {
			LOGGER.debug("getValue  : @memo objectPath = " + objectPath);

			Object value = objectMapper.getObjectFromMessage(message,
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
