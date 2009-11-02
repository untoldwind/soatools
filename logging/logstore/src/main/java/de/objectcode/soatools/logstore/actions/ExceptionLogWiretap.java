package de.objectcode.soatools.logstore.actions;

import static de.objectcode.soatools.logstore.IConstants.LOG_SERVICE_CATEGORY;
import static de.objectcode.soatools.logstore.IConstants.LOG_SERVICE_NAME;
import static org.jboss.soa.esb.notification.jms.JMSPropertiesSetter.JMS_REDELIVERED;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.internal.soa.esb.util.Encoding;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.addressing.Call;
import org.jboss.soa.esb.addressing.EPR;
import org.jboss.soa.esb.addressing.PortReference;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.Properties;
import org.jboss.soa.esb.message.format.MessageFactory;
import org.jboss.soa.esb.message.format.MessageType;
import org.jboss.soa.esb.util.Util;

import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

/**
 * Log wiretap to log exception from following actions in the pipeline.
 * 
 * Note that this wiretap does NOT log the message itself, only in case of an
 * exception a log message will be generated. Typically one should use the
 * action at the beginning of an action pipeline to catch and log all processing
 * exception.
 * 
 * Example:
 * 
 * <pre>
 * &gt;action name="logException" class="de.objectcode.soatools.logstore.actions.ExceptionLogWiretap"&lt;
 *           &gt;property name="tags"&lt;
 *             &gt;tag name="tag1" object-path="body.tag1.value"/&lt;
 *             &gt;tag name="contract.no" object-path="body.tag1.value"/&lt;
 *           &gt;/property&lt;
 * &gt;/action&lt;
 * </pre>
 */
public class ExceptionLogWiretap extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(LogWiretap.class);

	ServiceInvoker logService;
	final String serviceCategory;
	final String serviceName;
	final Map<String, List<IValueLocator>> tagValueLocators;

	public ExceptionLogWiretap(ConfigTree config) throws ConfigurationException {
		serviceCategory = config.getParent().getRequiredAttribute(
				"service-category");
		serviceName = config.getParent().getRequiredAttribute("service-name");
		try {
			logService = new ServiceInvoker(LOG_SERVICE_CATEGORY,
					LOG_SERVICE_NAME);
		} catch (MessageDeliverException e) {
			throw new ConfigurationException(e);
		}
		tagValueLocators = new HashMap<String, List<IValueLocator>>();
		ConfigTree[] tagConfigs = config.getChildren("tag");
		for (ConfigTree tagConfig : tagConfigs) {
			List<IValueLocator> locators = tagValueLocators.get(tagConfig
					.getRequiredAttribute("name"));

			if (locators == null) {
				locators = new ArrayList<IValueLocator>();
				tagValueLocators.put(tagConfig.getRequiredAttribute("name"),
						locators);
			}
			locators.add(ValueLocatorFactory.INSTANCE
					.createValueLocator(tagConfig));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processException(Message message, Throwable th) {
		try {
			Map<String, Object> tags = new HashMap<String, Object>();

			for (Map.Entry<String, List<IValueLocator>> entry : tagValueLocators
					.entrySet()) {
				Object value = null;

				for (IValueLocator valueLocator : entry.getValue()) {
					try {
						value = valueLocator.getValue(message);
					} catch (Exception e) {
					}
					if (value != null) {
						break;
					}
				}
				if (value != null) {
					tags.put(entry.getKey(), value);
				}
			}

			Message logMessage = MessageFactory.getInstance().getMessage(
					MessageType.JAVA_SERIALIZED);

			Call call = message.getHeader().getCall();
			findJpbmIds(call.getTo(), logMessage);
			findJpbmIds(call.getFrom(), logMessage);
			findJpbmIds(call.getReplyTo(), logMessage);
			findJpbmIds(call.getFaultTo(), logMessage);
			findJpbmIds(message.getProperties(), logMessage);

			logMessage.getBody().add("message-id",
					call.getMessageID().toString());
			logMessage.getBody().add(
					"correlation-id",
					call.getRelatesTo() != null ? call.getRelatesTo()
							.toString() : "");
			logMessage.getBody().add("service-category", serviceCategory);
			logMessage.getBody().add("service-name", serviceName);
			logMessage.getBody().add("state", "ERROR");
			if (message.getProperties().getProperty("log-enter-timestamp") != null) {
				logMessage.getBody().add(
						"log-enter-timestamp",
						message.getProperties().getProperty(
								"log-enter-timestamp"));
			}
			logMessage.getBody().add("log-leave-timestamp", new Date());
			logMessage.getBody().add("tags", tags);
			logMessage.getBody().add("content",
					Encoding.encodeObject(Util.serialize(message)));
			StringWriter writer = new StringWriter();
			PrintWriter out = new PrintWriter(writer);
			th.printStackTrace(out);
			out.flush();
			out.close();
			logMessage.getBody().add("fault-cause", writer.toString());
			logMessage.getBody().add("fault-reason", th.getMessage());

			logService.deliverAsync(logMessage);
		} catch (Exception e) {
			LOG.warn("Unable to wiretap to logging service", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		message.getProperties().setProperty("log-enter-timestamp", new Date());

		final Boolean redelivered = (Boolean) message.getProperties()
				.getProperty(JMS_REDELIVERED);

		if (redelivered) {
			try {
				Map<String, Object> tags = new HashMap<String, Object>();

				for (Map.Entry<String, List<IValueLocator>> entry : tagValueLocators
						.entrySet()) {
					Object value = null;

					for (IValueLocator valueLocator : entry.getValue()) {
						try {
							value = valueLocator.getValue(message);
						} catch (Exception e) {
						}
						if (value != null) {
							break;
						}
					}
					if (value != null) {
						tags.put(entry.getKey(), value);
					}
				}

				Message logMessage = MessageFactory.getInstance().getMessage(
						MessageType.JAVA_SERIALIZED);

				Call call = message.getHeader().getCall();
				findJpbmIds(call.getTo(), logMessage);
				findJpbmIds(call.getFrom(), logMessage);
				findJpbmIds(call.getReplyTo(), logMessage);
				findJpbmIds(call.getFaultTo(), logMessage);
				findJpbmIds(message.getProperties(), logMessage);

				logMessage.getBody().add("message-id",
						call.getMessageID().toString());
				logMessage.getBody().add(
						"correlation-id",
						call.getRelatesTo() != null ? call.getRelatesTo()
								.toString() : "");
				logMessage.getBody().add("service-category", serviceCategory);
				logMessage.getBody().add("service-name", serviceName);
				logMessage
						.getBody()
						.add(
								"state",
								"RETRY"
										+ (message.getProperties().getProperty(
												"at.liwest.esb.retryCount") != null ? "("
												+ message
														.getProperties()
														.getProperty(
																"at.liwest.esb.retryCount")
												+ ")"
												: ""));
				if (message.getProperties().getProperty("log-enter-timestamp") != null) {
					logMessage.getBody().add(
							"log-enter-timestamp",
							message.getProperties().getProperty(
									"log-enter-timestamp"));
				}
				logMessage.getBody().add("log-leave-timestamp", new Date());
				logMessage.getBody().add("tags", tags);
				logMessage.getBody().add("content",
						Encoding.encodeObject(Util.serialize(message)));

				logService.deliverAsync(logMessage);
			} catch (Exception e) {
				LOG.warn("Unable to wiretap to logging service", e);
			}

		}

		return message;
	}

	private void findJpbmIds(EPR epr, Message logMessage) {
		if (epr != null) {
			PortReference addr = epr.getAddr();
			String instanceId = addr.getExtensionValue("jbpmProcessInstId");
			String tokenId = addr.getExtensionValue("jbpmTokenId");
			String nodeId = addr.getExtensionValue("jbpmNodeId");

			if (instanceId != null) {
				logMessage.getBody().add("jbpm-process-instance-id",
						Long.parseLong(instanceId));
			}
			if (tokenId != null) {
				logMessage.getBody().add("jbpm-token-id",
						Long.parseLong(tokenId));
			}
			if (nodeId != null) {
				logMessage.getBody()
						.add("jbpm-node-id", Long.parseLong(nodeId));
			}
		}
	}

	private void findJpbmIds(Properties properties, Message logMessage) {
		Long processInstanceId = (Long) properties
				.getProperty("jbpm-process-instance-id");
		Long tokenId = (Long) properties.getProperty("jbpm-token-id");

		if (processInstanceId != null) {
			logMessage.getBody().add("jbpm-process-instance-id",
					processInstanceId);
		}
		if (tokenId != null) {
			logMessage.getBody().add("jbpm-token-id", tokenId);
		}

	}
}
