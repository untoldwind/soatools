package de.objectcode.soatools.mfm.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.command.CommandService;
import org.jbpm.command.GetProcessDefinitionCommand;
import org.jbpm.command.NewProcessInstanceCommand;
import org.jbpm.command.StartProcessInstanceCommand;
import org.jbpm.command.impl.CommandServiceImpl;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;

import de.objectcode.soatools.mfm.api.DowngradeException;
import de.objectcode.soatools.mfm.api.IMessageFormatRepository;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.NameVersionPair;
import de.objectcode.soatools.mfm.api.UpgradeException;
import de.objectcode.soatools.mfm.api.ValidationException;
import de.objectcode.soatools.mfm.api.normalize.NormalizedData;
import de.objectcode.soatools.mfm.io.XMLIO;
import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

/**
 * Create and optionally starts a new JPBM process instance.
 * <p>
 * This actions has an extended functionality compared the the standard action.
 * The name of the process can be extract dynamically from the message data and
 * the process definition can contain an additional message format that also
 * defines the variables of the new process instance.
 * </p>
 * <p>
 * Example: <code>
 *  &lt;action name="startProcessInstance" class="at.liwest.esb.common.mfm.action.NewJbpmProcessInstance"&gt;
 *    &lt;property name="start-process" value="true" /&gt;
 *    &lt;property name="parameters"&gt;
 *      &lt;process-definition object-path="body.jbpmProcessDefName" /&gt;
 *      &lt;key object-path="body.order.no" /&gt;
 *    &lt;/property&gt;
 *  &lt;/action&gt;
 * </code>
 * </p>
 * <p>
 * Properties:
 * <table>
 * <tr>
 * <td>startProcess</td>
 * <td>boolean (default: false)</td>
 * <td>Wether to start the new process instance of not</td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * Value locators:
 * <table>
 * <tr>
 * <td>process-definition</td>
 * <td>string (required)</td>
 * <td>The name of the process definition</td>
 * </tr>
 * <tr>
 * <td>key</td>
 * <td>string (optional)</td>
 * <td>The key of the new process instance</td>
 * </tr>
 * </table>
 * 
 * @author junglas
 */
public class NewJbpmProcessInstance extends AbstractActionPipelineProcessor {

	private final static Log LOGGER = LogFactory
			.getLog(NewJbpmProcessInstance.class);
	private final CommandService commandService;

	private final IValueLocator keyLocator;
	private final IValueLocator processDefinitionLocator;
	private final Map<NameVersionPair, List<MessageFormat>> registeredProcesses;
	private final IMessageFormatRepository repository;

	private final boolean startProcess;

	private final Map<String, IValueLocator> variableLocators;

	public NewJbpmProcessInstance(final ConfigTree config)
			throws ConfigurationException {
		try {
			final InitialContext ctx = new InitialContext();
			this.repository = (IMessageFormatRepository) ctx
					.lookup(IMessageFormatRepository.JNDI_NAME);

			final JbpmConfiguration configuration = JbpmConfiguration
					.getInstance();

			this.commandService = new CommandServiceImpl(configuration);

			this.startProcess = config.getBooleanAttribute("start-process",
					false);
			final ConfigTree[] processDefinitionConfig = config
					.getChildren("process-definition");

			if (processDefinitionConfig.length != 1) {
				throw new ConfigurationException(
						"Parameters require exactly one <process-definition> tag");
			}
			this.processDefinitionLocator = ValueLocatorFactory.INSTANCE
					.createValueLocator(processDefinitionConfig[0]);

			final ConfigTree[] keyConfig = config.getChildren("key");
			if (keyConfig.length > 0) {
				this.keyLocator = ValueLocatorFactory.INSTANCE
						.createValueLocator(keyConfig[0]);
			} else {
				this.keyLocator = null;
			}

			this.variableLocators = new HashMap<String, IValueLocator>();
			final ConfigTree[] variableConfigs = config.getChildren("variable");
			for (final ConfigTree variableConfig : variableConfigs) {
				this.variableLocators.put(variableConfig
						.getRequiredAttribute("name"),
						ValueLocatorFactory.INSTANCE
								.createValueLocator(variableConfig));
			}

			this.registeredProcesses = new HashMap<NameVersionPair, List<MessageFormat>>();
		} catch (final Exception ex) {
			throw new ConfigurationException(ex);
		}
	}

	private List<MessageFormat> getMessageFormats(
			final ExtendedGetProcessDefinitionCommand getProcessDefinition,
			final ProcessDefinition processDefinition) {
		List<MessageFormat> messageFormats;
		synchronized (this.registeredProcesses) {
			final NameVersionPair processDefinitionVersion = new NameVersionPair(
					processDefinition.getName(), processDefinition.getVersion());
			if (getProcessDefinition.getMessageFormatModel() != null) {
				final MessageFormatModel messageFormatModel = getProcessDefinition
						.getMessageFormatModel();

				messageFormats = new ArrayList<MessageFormat>();
				this.repository.registerModel(messageFormatModel);

				for (final MessageFormat messageFormat : messageFormatModel
						.getMessageFormats()) {
					messageFormats.add(this.repository
							.getMessageFormat(messageFormat.getName(),
									messageFormat.getVersion()));
				}

				messageFormats = Collections.unmodifiableList(messageFormats);

				if (LOGGER.isDebugEnabled()
						&& (messageFormats == null || messageFormats.size() == 0)) {
					LOGGER
							.debug("getMessageFormats: @param getProcessDefinition, processDefinition = "
									+ new Object[] { getProcessDefinition,
											processDefinition });
					LOGGER
							.debug("getMessageFormats  : @memo if branch processDefinitionVersion = "
									+ processDefinitionVersion);
					LOGGER
							.debug("getMessageFormats  : @memo getProcessDefinition.getMessageFormatModel() = "
									+ getProcessDefinition
											.getMessageFormatModel());
					LOGGER
							.debug("getMessageFormats  : @memo messageFormatModel.getMessageFormats().size() = "
									+ messageFormatModel.getMessageFormats()
											.size());
					LOGGER
							.debug("getMessageFormats  : @memo put processDefinitionVersion = "
									+ processDefinitionVersion);
					LOGGER
							.debug("getMessageFormats  : @memo put messageFormats = "
									+ messageFormats);
					LOGGER.debug("getMessageFormats  : @memo Put empty in: "
							+ messageFormats);
				}

				this.registeredProcesses.put(processDefinitionVersion,
						messageFormats);
			} else {
				messageFormats = this.registeredProcesses
						.get(processDefinitionVersion);

				if (LOGGER.isDebugEnabled()
						&& (messageFormats == null || messageFormats.size() == 0)) {
					LOGGER
							.debug("getMessageFormats: @param getProcessDefinition, processDefinition = "
									+ new Object[] { getProcessDefinition,
											processDefinition });
					LOGGER
							.debug("getMessageFormats  : @memo if branch processDefinitionVersion = "
									+ processDefinitionVersion);
					LOGGER
							.debug("getMessageFormats  : @memo getProcessDefinition.getMessageFormatModel() = "
									+ getProcessDefinition
											.getMessageFormatModel());
					LOGGER
							.debug("getMessageFormats  : @memo put processDefinitionVersion = "
									+ processDefinitionVersion);
					LOGGER
							.debug("getMessageFormats  : @memo put messageFormats = "
									+ messageFormats);
					LOGGER
							.debug("getMessageFormats  : @memo else branch processDefinitionVersion = "
									+ processDefinitionVersion);
					LOGGER.debug("getMessageFormats  : @memo Get empty out: "
							+ messageFormats);
				}
			}
		}

		return messageFormats;
	}

	private Map<String, Object> getVariableMap(final Message message,
			final List<MessageFormat> messageFormats)
			throws ActionProcessingException {
		final Map<String, Object> variableMap = new HashMap<String, Object>();

		final NormalizedData normalizedData = new NormalizedData();
		for (final MessageFormat messageFormat : messageFormats) {
			try {
				messageFormat.normalizeMessage(message, normalizedData,
						this.repository);
			} catch (final DowngradeException e) {
				LOGGER.error("Downgrade error: ", e);
				throw new ActionProcessingException(e);
			} catch (final UpgradeException e) {
				LOGGER.error("Upgrade error: ", e);
				throw new ActionProcessingException(e);
			} catch (final ValidationException e) {
				LOGGER.debug("getVariableMap  : @memo messageFormat = "
						+ messageFormat);
				LOGGER.debug("getVariableMap  : @memo message = " + message);

				throw new ActionProcessingException(e);
			}
		}
		for (final Map.Entry<String, Object> entry : normalizedData.entrySet()) {
			variableMap.put(entry.getKey(), entry.getValue());
		}

		/*
		 * MapDataCollector mapCollector = new MapDataCollector(variableMap);
		 * 
		 * for (MessageFormat messageFormat : messageFormats) { try {
		 * messageFormat.normalizeMessage(message, mapCollector); } catch
		 * (ValidationException e) { throw new ActionProcessingException(e); } }
		 */
		for (final Map.Entry<String, IValueLocator> entry : this.variableLocators
				.entrySet()) {
			variableMap.put(entry.getKey(), entry.getValue().getValue(message));
		}

		return variableMap;
	}

	private NewProcessInstanceCommand newProcessInstance(
			final ProcessDefinition processDefinition, final Message message) {
		NewProcessInstanceCommand newProcessInstance;
		if (this.startProcess) {
			newProcessInstance = new StartProcessInstanceCommand();
		} else {
			newProcessInstance = new NewProcessInstanceCommand();
		}

		newProcessInstance.setCreateStartTask(false);
		newProcessInstance.setProcessId(processDefinition.getId());

		if (this.keyLocator != null) {
			try {
				final String processInstanceName = (String) this.keyLocator
						.getValue(message);
				LOGGER.debug("process  : @memo processInstanceName = "
						+ processInstanceName);

				newProcessInstance.setKey(processInstanceName);
			} catch (final Exception e) {
				LOGGER.warn("Unable to set key: " + this.keyLocator);
			}
		}

		return newProcessInstance;
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(final Message message)
			throws ActionProcessingException {
		final String processDefinitionName = (String) this.processDefinitionLocator
				.getValue(message);

		if (processDefinitionName == null) {
			throw new ActionProcessingException(
					"Unable to resolve process definition name");
		}

		final ExtendedGetProcessDefinitionCommand getProcessDefinition = new ExtendedGetProcessDefinitionCommand(
				processDefinitionName);

		final ProcessDefinition processDefinition = (ProcessDefinition) this.commandService
				.execute(getProcessDefinition);

		if (processDefinition == null) {
			throw new ActionProcessingException(
					"Cannot find process definition: " + processDefinitionName);
		}

		final List<MessageFormat> messageFormats = getMessageFormats(
				getProcessDefinition, processDefinition);

		message.getProperties().setProperty("jbpm-process-definition-name",
				processDefinition.getName());
		message.getProperties().setProperty("jbpm-process-definition-version",
				processDefinition.getVersion());
		message.getProperties().setProperty("jbpm-process-definition-id",
				processDefinition.getId());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("process  : @memo processDefinition.getName() = "
					+ processDefinition.getName());
			LOGGER.debug("process  : @memo processDefinition.getVersion() = "
					+ processDefinition.getVersion());
			LOGGER.debug("process  : @memo processDefinition.getId() = "
					+ processDefinition.getId());
		}

		final NewProcessInstanceCommand newProcessInstance = newProcessInstance(
				processDefinition, message);

		final Map<String, Object> variableMap = getVariableMap(message,
				messageFormats);

		newProcessInstance.setVariables(variableMap);

		final ProcessInstance processInstance = (ProcessInstance) this.commandService
				.execute(newProcessInstance);

		if (processInstance == null) {
			throw new ActionProcessingException(
					"Failed to create process instance: "
							+ processDefinitionName);
		}

		final Token rootToken = processInstance.getRootToken();
		LOGGER.debug("process  : @memo rootToken = " + rootToken);

		message.getProperties().setProperty("jbpm-process-instance-id",
				processInstance.getId());
		message.getProperties().setProperty("jbpm-token-id", rootToken.getId());

		return message;
	}

	/**
	 * Extension of the standard GetProcessDefinitionCommand that allows us to
	 * retrieve the process definition together with the message format model
	 * inside.
	 * 
	 * @author junglas
	 */
	private class ExtendedGetProcessDefinitionCommand extends
			GetProcessDefinitionCommand {

		private static final long serialVersionUID = 1L;

		MessageFormatModel messageFormatModel;

		public ExtendedGetProcessDefinitionCommand(final String name) {
			super(name);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object execute(final JbpmContext jbpmContext) throws Exception {
			ProcessDefinition processDefinition;

			try {
				processDefinition = (ProcessDefinition) super
						.execute(jbpmContext);
			} catch (final NullPointerException ex) {
				throw new ActionProcessingException(
						"Problems getting process definition for <<process:"
								+ getName() + ">>", ex);
			}

			if (processDefinition != null) {
				final NameVersionPair processDefinitionVersion = new NameVersionPair(
						processDefinition.getName(), processDefinition
								.getVersion());

				synchronized (NewJbpmProcessInstance.this.registeredProcesses) {
					if (!NewJbpmProcessInstance.this.registeredProcesses
							.containsKey(processDefinitionVersion)) {
						if (processDefinition.getFileDefinition().hasFile(
								"message-format.xml")) {
							try {
								this.messageFormatModel = XMLIO.INSTANCE
										.read(processDefinition
												.getFileDefinition().getBytes(
														"message-format.xml"));
							} catch (final IOException e) {
								throw new ActionProcessingException(
										"Invalid message-format.xml in process definition",
										e);
							}
						}
					}
				}
			}
			return processDefinition;
		}

		public MessageFormatModel getMessageFormatModel() {
			return this.messageFormatModel;
		}

	}
}
