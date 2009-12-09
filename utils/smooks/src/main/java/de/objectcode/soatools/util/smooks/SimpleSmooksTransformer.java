package de.objectcode.soatools.util.smooks;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.javabean.repository.BeanRepository;
import org.milyn.resource.URIResourceLocator;

import com.thoughtworks.xstream.XStream;

import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

public class SimpleSmooksTransformer extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(SimpleSmooksTransformer.class);
	
	private final MessagePayloadProxy payloadProxy;
	private final Smooks smooks;
	private final boolean ensureXmlDecl;
	private final Map<String, IValueLocator> inputLocators;
	private final Map<String, IValueLocator> outputLocators;
	private final boolean attachOriginal;
	private final boolean dropOutput;

	/** Logger for this class. */
	private static final Log LOGGER = LogFactory
			.getLog(SimpleSmooksTransformer.class);

	public SimpleSmooksTransformer(ConfigTree config)
			throws ConfigurationException {
		attachOriginal = config.getBooleanAttribute("attach-original", true);
		dropOutput = config.getBooleanAttribute("drop-output", false);
		payloadProxy = new MessagePayloadProxy(config);
		if (config.getBooleanAttribute("allow-emtpy-body", true)) {
			payloadProxy
					.setNullGetPayloadHandling(MessagePayloadProxy.NullPayloadHandling.NONE);
		}
		String smooksResource = config.getRequiredAttribute("smooks");
		try {
			smooks = new Smooks();
			smooks.addConfigurations("smooks-resource",
					new URIResourceLocator().getResource(smooksResource));
		} catch (Exception e) {
			throw new ConfigurationException("Failed to initialize Smooks", e);
		}
		ensureXmlDecl = config.getBooleanAttribute("ensure-xml-decl", false);
		ConfigTree[] inputConfigs = config.getChildren("input");
		inputLocators = new HashMap<String, IValueLocator>();
		for (ConfigTree contextConfig : inputConfigs) {
			inputLocators.put(contextConfig.getRequiredAttribute("name"),
					ValueLocatorFactory.INSTANCE
							.createValueLocator(contextConfig));
		}
		ConfigTree[] outputConfigs = config.getChildren("output");
		outputLocators = new HashMap<String, IValueLocator>();
		for (ConfigTree contextConfig : outputConfigs) {
			outputLocators.put(contextConfig.getRequiredAttribute("name"),
					ValueLocatorFactory.INSTANCE
							.createValueLocator(contextConfig));
		}
	}

	@SuppressWarnings("deprecation")
	public Message process(Message message) throws ActionProcessingException {
		try {
			Object data = payloadProxy.getPayload(message);
			StreamSource smooksInput;

			if (attachOriginal && data != null) {
				message.getAttachment().put("original-data", data);
			}
			
			if (data == null || data.toString().length() == 0) {
				smooksInput = new StreamSource(new StringReader("<document/>"));
			} else if (data instanceof String) {
				smooksInput = new StreamSource(
						new StringReader(data.toString()));
			} else if (data instanceof byte[]) {
				smooksInput = new StreamSource(new ByteArrayInputStream(
						(byte[]) data));
			} else {
				XStream xstream = new XStream();
				smooksInput = new StreamSource(new StringReader(xstream
						.toXML(data)));
			}

			ExecutionContext context = smooks.createExecutionContext();
			if (inputLocators.isEmpty()) {
				for (String name : message.getBody().getNames()) {
					BeanRepository.getInstance(context).addBean(name, message.getBody().get(name));
				}
			} else {
				for (Map.Entry<String, IValueLocator> contextLocator : inputLocators
						.entrySet()) {
					Object messageValue = contextLocator.getValue().getValue(
							message);

					if (messageValue != null) {
						BeanRepository.getInstance(context).addBean(contextLocator.getKey(), messageValue);
					} else {
						LOGGER.info("process  : no message found for <<key: "
								+ contextLocator.getKey() + ">>");
					}
				}
			}

			StringWriter out = new StringWriter();
			smooks.filter(smooksInput, new StreamResult(out), context);

			if (outputLocators.isEmpty()) {
				for (Map.Entry<String, Object> entry : BeanRepository.getInstance(context).getBeanMap().entrySet()) {
					if (entry.getKey() != null && entry.getValue() != null) {
						message.getBody().add(entry.getKey().toString(),
								entry.getValue());
					}
				}
			} else {
				for (Map.Entry<String, IValueLocator> contextLocator : outputLocators
						.entrySet()) {
					Object value = BeanRepository.getInstance(context).getBean(contextLocator.getKey());
					if (value != null) {
						contextLocator.getValue().setValue(message, value);
					}
				}
			}

			if (dropOutput) {
				payloadProxy.setPayload(message, "");
			} else {
				if (ensureXmlDecl && !out.toString().startsWith("<?xml")) {
					payloadProxy.setPayload(message, "<?xml version=\"1.0\"?>"
							+ out.toString());
				} else {
					payloadProxy.setPayload(message, out.toString());
				}
			}
		} catch (Exception e) {
			LOG.error("Exception", e);
			throw new ActionProcessingException(e);
		}
		return message;
	}
}
