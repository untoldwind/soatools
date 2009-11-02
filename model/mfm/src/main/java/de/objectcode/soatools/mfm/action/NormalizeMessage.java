package de.objectcode.soatools.mfm.action;

import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.milyn.resource.URIResourceLocator;

import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.ValidationException;
import de.objectcode.soatools.mfm.api.collector.MessageBodyCollector;
import de.objectcode.soatools.mfm.io.XMLIO;
import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

/**
 * Validate and normalize the message data.
 * 
 * <p>
 * Example: <code>
 * &lt;action name="normalize" class="at.liwest.esb.common.mfm.action.NormalizeMessage"&gt;
 *   &lt;property name="message-format-model" value="/at/liwest/esb/services/provisioning/message-format.xml" /&gt;
 *   &lt;property name="message-formats"&gt;
 *     &lt;message-format value="start-provisioning-workflow" version="1" /&gt;
 *   &lt;/property&gt;
 * &lt;/action&gt;
 * </code>
 * </p>
 * <p>
 * Properties:
 * <table>
 * <tr>
 * <td<message-format-model</td>
 * <td>string (optional)</td>
 * <td>Resource containing a message format model. Every message formats an
 * types in that model will be registered in the central registry</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author junglas
 */
public class NormalizeMessage extends AbstractActionPipelineProcessor {

	private final static Log LOG = LogFactory.getLog(NormalizeMessage.class);

	private final IMessageFormatRepository repository;

	private final List<IValueLocator> messageFormatLocators;
	private final List<Integer> messageFormatVersions;

	public NormalizeMessage(ConfigTree config) throws ConfigurationException {
		try {
			InitialContext ctx = new InitialContext();

			repository = (IMessageFormatRepository) ctx
					.lookup(IMessageFormatRepository.JNDI_NAME);
		} catch (NamingException e) {
			throw new ConfigurationException(e);
		}

		String modelResource = config.getAttribute("message-format-model");

		if (modelResource != null) {
			try {
				MessageFormatModel model = XMLIO.INSTANCE
						.read(new URIResourceLocator()
								.getResource(modelResource));

				repository.registerModel(model);
			} catch (Exception e) {
				throw new ConfigurationException("Failed to read: "
						+ modelResource, e);
			}
		}

		ConfigTree[] messageFormatConfigs = config
				.getChildren("message-format");
		messageFormatLocators = new ArrayList<IValueLocator>();
		messageFormatVersions = new ArrayList<Integer>();
		for (ConfigTree messageFormatConfig : messageFormatConfigs) {
			messageFormatLocators.add(ValueLocatorFactory.INSTANCE
					.createValueLocator(messageFormatConfig));
			messageFormatVersions.add((int) messageFormatConfig
					.getLongAttribute("version", 1));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		for (int i = 0; i < messageFormatLocators.size(); i++) {
			Object messageFormatName = messageFormatLocators.get(i).getValue(
					message);

			if (messageFormatName == null) {
				LOG.error("Unable to get message format name: "
						+ messageFormatLocators.get(i));
				throw new ActionProcessingException(
						"Unable to optain message format name: "
								+ messageFormatLocators.get(i));
			}

			MessageFormat messageFormat = repository.getMessageFormat(
					messageFormatName.toString(), messageFormatVersions.get(i));

			if (messageFormat == null) {
				LOG.error("Undefined message format: " + messageFormatName
						+ " " + messageFormatVersions.get(i));
				throw new ActionProcessingException(
						"Undefined message format: " + messageFormatName + " "
								+ messageFormatVersions.get(i));
			}

			MessageBodyCollector bodyCollector = new MessageBodyCollector(
					message);
			try {
				messageFormat.normalizeMessage(message, bodyCollector,
						repository);
			} catch (DowngradeException e) {
				LOG.error("Downgrade error: ", e);
				throw new ActionProcessingException(e);
			} catch (UpgradeException e) {
				LOG.error("Upgrade error: ", e);
				throw new ActionProcessingException(e);
			} catch (ValidationException e) {
				LOG.error("Validation error: ", e);
				throw new ActionProcessingException(e);
			}
		}

		return message;
	}
}
