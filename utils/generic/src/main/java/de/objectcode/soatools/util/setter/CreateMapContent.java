package de.objectcode.soatools.util.setter;

import java.util.HashMap;
import java.util.Map;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;

import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

/**
 * Create map from value locators and add it as message body part.
 * 
 * This action can be used for simple message manipulations that are just too
 * small to fire up smooks.
 * 
 * <pre>
 * &lt;action name="createMap" class="de.objectcode.soatools.util.setter.CreateMapContent"&gt;
 *   &lt;property name="set-payload-location" value="body.newMap"/&gt;
 *   &lt;property name="puts"&gt;
 *     &lt;put key="key1" object-path="body.value1" default-value="nothing" /&gt;
 *     &lt;put key="key2" expression="${value2} ${value3}" /&gt;
 *     &lt;put key="key3" xpath="/element3/value" /&gt;
 *     &lt;put key="key4" value="static value 4" /&gt;
 *   &lt;/property&gt;
 * &lt;/action&gt;
 * </pre>
 * 
 * @author junglas
 */
public class CreateMapContent extends AbstractActionPipelineProcessor {
	private final Map<String, IValueLocator> putLocators;
	private final MessagePayloadProxy payloadProxy;

	public CreateMapContent(ConfigTree config) throws ConfigurationException {
		this.payloadProxy = new MessagePayloadProxy(config);
		ConfigTree[] putConfigs = config.getChildren("put");
		putLocators = new HashMap<String, IValueLocator>();
		for (ConfigTree putConfig : putConfigs) {
			putLocators.put(putConfig.getRequiredAttribute("key"),
					ValueLocatorFactory.INSTANCE.createValueLocator(putConfig));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		Map<String, Object> content = new HashMap<String, Object>();
		for (Map.Entry<String, IValueLocator> putLocator : putLocators
				.entrySet()) {
			content.put(putLocator.getKey(), putLocator.getValue().getValue(
					message));
		}
		try {
			payloadProxy.setPayload(message, content);
		} catch (MessageDeliverException e) {
			throw new ActionProcessingException(e);
		}
		return message;
	}
}
