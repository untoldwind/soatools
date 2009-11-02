package de.objectcode.soatools.util.setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;
import org.milyn.resource.URIResourceLocator;

import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

/**
 * Replace some elements of an ESB message by a value from a properties file.
 * 
 * This action can be used for simple message manipulations that are just too
 * small to fire up smooks.
 * 
 * <pre>
 * &lt;action name="replace" class="de.objectcode.soatools.util.setter.PropertiesReplace"&gt;
 *   &lt;property name="properties" value="classpath uri of properties file"&gt;
 *   &lt;property name="puts"&gt;
 *     &lt;put target-path="body.newKey1" object-path="body.value1" /&gt;
 *     &lt;put target-path="body.newKey2" expression="${value2} ${value3}" /&gt;
 *     &lt;put target-path="body.newKey3" xpath="/element3/value" /&gt;
 *     &lt;put target-path="body.newKey4" value="static value 4" /&gt;
 *   &lt;/property&gt;
 * &lt;/action&gt;
 * </pre>
 * 
 * Note that in this example the result of the value locator will be used as
 * property key, i.e. the message body will contain the property values.
 * 
 * @author junglas
 */
public class PropertiesReplace extends AbstractActionPipelineProcessor {
	private final Map<String, IValueLocator> putLocators;
	private final Properties properties;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public PropertiesReplace(ConfigTree config) throws ConfigurationException {
		String propertiesResource = config.getRequiredAttribute("properties");

		properties = new Properties();
		try {
			properties.load(new URIResourceLocator()
					.getResource(propertiesResource));
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}

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
				Object keyValue = putLocator.getValue().getValue(message);
				if (keyValue != null) {
					Object value = properties.get(keyValue.toString());

					objectMapper.setObjectOnMessage(message, putLocator
							.getKey(), value);
				}
			} catch (ObjectMappingException e) {
				throw new ActionProcessingException(e);
			}
		}
		return message;
	}

}
