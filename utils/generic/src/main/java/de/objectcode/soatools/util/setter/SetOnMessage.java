package de.objectcode.soatools.util.setter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;

import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

/**
 * Replace some elements of an ESB message by value of a value locator.
 * 
 * This action can be used for simple message manipulations that are just too
 * small to fire up smooks.
 * 
 * <pre>
 * &lt;action name="set" class="de.objectcode.soatools.util.setter.SetOnMessage"&gt;
 *   &lt;property name="puts"&gt;
 *     &lt;put target-path="body.newKey1" object-path="body.value1" default-value="nothing" /&gt;
 *     &lt;put target-path="body.newKey2" expression="${value2} ${value3}" /&gt;
 *     &lt;put target-path="body.newKey3" xpath="/element3/value" /&gt;
 *     &lt;put target-path="body.newKey4" value="static value 4" /&gt;
 *   &lt;/property&gt;
 * &lt;/action&gt;
 * </pre>
 * 
 * @author junglas
 */
public class SetOnMessage extends AbstractActionPipelineProcessor {
	/** Logger for this class. */
	private static final Log LOGGER = LogFactory.getLog(SetOnMessage.class);

	private final Map<String, IValueLocator> putLocators;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public SetOnMessage(ConfigTree config) throws ConfigurationException {
		ConfigTree[] putConfigs = config.getChildren("put");
		putLocators = new HashMap<String, IValueLocator>();
		for (ConfigTree putConfig : putConfigs) {
			putLocators.put(putConfig.getRequiredAttribute("target-path"),
					ValueLocatorFactory.INSTANCE.createValueLocator(putConfig));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		for (Map.Entry<String, IValueLocator> putLocator : putLocators
				.entrySet()) {
			try {
				final String key = putLocator.getKey();
				final Object value = putLocator.getValue().getValue(message);

				LOGGER.debug("process  : @memo key = " + key);
				LOGGER.debug("process  : @memo value = " + value);

				if (key != null || value != null) {
					objectMapper.setObjectOnMessage(message, key, value);
				} else {
					LOGGER.warn("Can't fill null values in map: <<key: " + key
							+ ">><<value: " + value + ">>");
					LOGGER.debug("process  : @memo message = " + message);
				}
			} catch (ObjectMappingException e) {
				throw new ActionProcessingException(e);
			}
		}
		return message;
	}

}
