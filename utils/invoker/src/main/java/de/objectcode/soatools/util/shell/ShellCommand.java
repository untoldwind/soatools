package de.objectcode.soatools.util.shell;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.javabean.BeanAccessor;
import org.milyn.resource.URIResourceLocator;

import de.objectcode.soatools.util.LimitedStreamReader;
import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

public class ShellCommand extends AbstractActionPipelineProcessor {
	private final static Log LOGGER = LogFactory.getLog(ShellCommand.class);

	private final boolean attachRawData;
	private final String encoding;
	private final Map<String, IValueLocator> environmentLocators;
	private final String errorDescriptionLocation;
	private final String errorStatusLocation;
	private final boolean execFromPayload;
	private final String exitCodeLocation;
	private final long exitCodeTimeoutFailure;
	private final List<IValueLocator> parameterLocators;
	private final MessagePayloadProxy payloadProxy;
	private final long responseLimit;
	private final Smooks responseSmooks;
	private final boolean runtimeOnIOFailure;
	private final boolean runtimeOnTimeout;
	private final String shellCommand;
	private final String[] shellCommandOptions;
	private final long timeout;
	private final File workingDirectory;

	public ShellCommand(final ConfigTree config) throws ConfigurationException {
		this.payloadProxy = new MessagePayloadProxy(config);
		this.shellCommand = config.getAttribute("shell-command");
		this.execFromPayload = config.getBooleanAttribute("exec-from-payload",
				true);
		this.exitCodeLocation = config.getAttribute("exit-code-location",
				"exitCode");
		this.errorStatusLocation = config.getAttribute("exit-status-location",
				"status");
		this.errorDescriptionLocation = config.getAttribute(
				"exit-description-location", "errorDescription");
		this.workingDirectory = new File(config
				.getRequiredAttribute("working-directory"));
		this.timeout = config.getLongAttribute("timeout", 5000);
		this.runtimeOnTimeout = config.getBooleanAttribute(
				"runtime-on-timeout-failure", false);
		this.exitCodeTimeoutFailure = config.getLongAttribute(
				"exit-code-timeout-failure", -1);
		this.responseLimit = config.getLongAttribute("response-limit",
				10 * 1024L * 1024L);
		this.encoding = config.getAttribute("encoding", "UTF-8");
		this.attachRawData = config
				.getBooleanAttribute("attach-raw-data", true);
		this.runtimeOnIOFailure = config.getBooleanAttribute(
				"runtime-on-io-failure", true);

		final String shellCommandOptionsStr = config
				.getAttribute("shell-command-options");

		if (shellCommandOptionsStr != null
				&& shellCommandOptionsStr.trim().length() != 0) {
			final StringTokenizer stringTokenizer = new StringTokenizer(
					shellCommandOptionsStr, ",");

			final List<String> tokens = new ArrayList<String>();

			while (stringTokenizer.hasMoreTokens()) {
				tokens.add(stringTokenizer.nextToken());
			}

			this.shellCommandOptions = tokens.toArray(new String[] {});
		} else {
			this.shellCommandOptions = new String[] {};
		}

		if (!this.workingDirectory.exists()
				|| !this.workingDirectory.isDirectory()) {
			throw new ConfigurationException(this.workingDirectory
					+ " is not a directory");
		}

		if (this.runtimeOnTimeout && this.exitCodeTimeoutFailure != -1) {
			final String msg = "Whether <<runtime-on-timeout-failure: {0}>> or <<error-code-timeout-failure: {1}>> maybe set. Check your settings.";
			throw new ConfigurationException(MessageFormat.format(msg,
					new Object[] { this.runtimeOnTimeout,
							this.exitCodeTimeoutFailure }));
		}

		final String responseSmooksResource = config
				.getAttribute("response-smooks");
		if (responseSmooksResource != null) {
			try {
				this.responseSmooks = new Smooks();
				this.responseSmooks.addConfigurations("smooks-resource",
						new URIResourceLocator()
								.getResource(responseSmooksResource));
				this.responseSmooks.addConfigurations("cdu-creators",
						new URIResourceLocator()
								.getResource("/META-INF/smooks-creators.xml"));
			} catch (final Exception e) {
				throw new ConfigurationException("Failed to initialize Smooks",
						e);
			}
		} else {
			this.responseSmooks = null;
		}
		final ConfigTree[] paramterConfigs = config.getChildren("parameter");
		this.parameterLocators = new ArrayList<IValueLocator>();
		for (final ConfigTree parameterConfig : paramterConfigs) {
			this.parameterLocators.add(ValueLocatorFactory.INSTANCE
					.createValueLocator(parameterConfig));
		}
		final ConfigTree[] environmentConfigs = config
				.getChildren("environment");
		this.environmentLocators = new HashMap<String, IValueLocator>();
		for (final ConfigTree environmentConfig : environmentConfigs) {
			final String name = environmentConfig.getRequiredAttribute("name");

			this.environmentLocators.put(name, ValueLocatorFactory.INSTANCE
					.createValueLocator(environmentConfig));
		}
	}

	@SuppressWarnings("deprecation")
	private void invokeShellCommand(final Message message, final String cmd,
			final List<String> parameters,
			final Map<String, String> environmentVars)
			throws ActionProcessingException {
		int i;
		final Runtime runtime = Runtime.getRuntime();
		final ObjectMapper objectMapper = new ObjectMapper();
		final String[] command = new String[parameters.size() + 1
				+ this.shellCommandOptions.length]; // +2 f�r DOS

		command[0] = this.shellCommand;

		for (i = 0; i < this.shellCommandOptions.length; i++) {
			command[i + 1] = this.shellCommandOptions[i];
		}

		for (i = 0; i < parameters.size(); i++) {
			command[i + 1 + this.shellCommandOptions.length] = parameters
					.get(i);
		}

		final String[] environment = new String[environmentVars.size()];
		i = 0;
		for (final Map.Entry<String, String> entry : environmentVars.entrySet()) {
			environment[i++] = entry.getKey() + "=" + entry.getValue();
		}

		Thread stdoutThread = null;
		Thread stderrThread = null;

		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Invoking: " + Arrays.toString(command)
						+ " with environemt " + Arrays.toString(environment)
						+ " in directory " + this.workingDirectory);
			}

			if (this.attachRawData) {
				final StringBuffer commandLine = new StringBuffer(
						this.shellCommand);

				for (final String parameter : parameters) {
					commandLine.append(" \"");
					commandLine.append(parameter);
					commandLine.append("\"");
				}
				message.getAttachment().put("command-line",
						commandLine.toString());

				final StringBuffer environmentStr = new StringBuffer();
				for (final Map.Entry<String, String> entry : environmentVars
						.entrySet()) {
					environmentStr.append(entry.getKey()).append("=").append(
							entry.getValue()).append("\n");
				}

				message.getAttachment().put("command-environment",
						environmentStr.toString());
			}

			final Process process;
			if (environment == null || environment.length == 0) {
				process = runtime.exec(command, null, this.workingDirectory);
			} else {
				process = runtime.exec(command, environment,
						this.workingDirectory);
			}

			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					process.destroy();
				}
			}, this.timeout);
			final LimitedStreamReader stdoutReader = new LimitedStreamReader(
					process.getInputStream(), this.responseLimit);
			final LimitedStreamReader stderrReader = new LimitedStreamReader(
					process.getErrorStream(), this.responseLimit);
			stdoutThread = new Thread(stdoutReader);
			stderrThread = new Thread(stderrReader);
			stdoutThread.start();
			stderrThread.start();

			long exitCode = process.waitFor();
			timer.cancel();

			stdoutThread.join(1000);
			if (stdoutThread.isAlive()) {
				if (this.runtimeOnTimeout) {
					LOGGER
							.debug("invokeShellCommand stdout  : @memo runtimeOnTimeout = "
									+ runtimeOnTimeout);
					throw new RuntimeException(
							"Timeout execute command. Stdout reader still alive");
				} else if (this.exitCodeTimeoutFailure != -1) {
					LOGGER
							.debug("invokeShellCommand stdout  : @memo exitCodeTimeoutFailure = "
									+ exitCodeTimeoutFailure);
					objectMapper.setObjectOnMessage(message,
							this.errorStatusLocation, "NOK");
					objectMapper.setObjectOnMessage(message,
							this.errorDescriptionLocation,
							"Timeout shell invoking.");
					exitCode = this.exitCodeTimeoutFailure;
				} else {
					throw new ActionProcessingException(
							"Stdout reader still alive");
				}
			}
			stderrThread.join(1000);
			if (stderrThread.isAlive()) {
				if (this.runtimeOnTimeout) {
					LOGGER
							.debug("invokeShellCommand stderr  : @memo runtimeOnTimeout = "
									+ runtimeOnTimeout);
					throw new RuntimeException(
							"Timeout execute command. Stderr reader still alive");
				} else if (this.exitCodeTimeoutFailure != -1) {
					LOGGER
							.debug("invokeShellCommand stderr  : @memo exitCodeTimeoutFailure = "
									+ exitCodeTimeoutFailure);
					objectMapper.setObjectOnMessage(message,
							this.errorStatusLocation, "NOK");
					objectMapper.setObjectOnMessage(message,
							this.errorDescriptionLocation,
							"Timeout shell invoking.");
					exitCode = this.exitCodeTimeoutFailure;
				} else {
					throw new ActionProcessingException(
							"Stderr reader still alive");
				}
			}

			objectMapper.setObjectOnMessage(message, this.exitCodeLocation,
					exitCode);

			byte[] stdoutResult = stdoutReader.getResult();
			byte[] stderrResult = stderrReader.getResult();

			if (stdoutResult == null) {
				LOGGER.warn("Failure reading stdout");
			}
			if (stderrResult == null) {
				LOGGER.warn("Failure reading stderr");
			} else if (stderrResult.length > 0) {
				LOGGER.warn("Shell command " + this.shellCommand
						+ " wrote to stderr: "
						+ new String(stderrResult, this.encoding));
			}

			if (this.attachRawData) {
				if (stdoutResult != null)
					message.getAttachment().put("command-stdout", stdoutResult);
				if (stderrResult != null)
					message.getAttachment().put("command-stderr", stderrResult);
			}

			if (this.responseSmooks != null && stdoutResult != null) {
				final ExecutionContext executionContext = this.responseSmooks
						.createExecutionContext();
				final StringWriter result = new StringWriter();

				this.responseSmooks.filter(new StreamSource(new StringReader(
						new String(stdoutResult, this.encoding))),
						new StreamResult(result), executionContext);

				final Map<String, Object> beanMap = BeanAccessor
						.getBeans(executionContext);
				if (beanMap != null) {
					final Iterator<Map.Entry<String, Object>> beans = beanMap
							.entrySet().iterator();
					while (beans.hasNext()) {
						final Map.Entry<String, Object> entry = beans.next();
						final String key = entry.getKey();

						if (LOGGER.isDebugEnabled()
								&& message.getBody().get(key) != null) {
							LOGGER.debug("Outputting Java object to '" + key
									+ "'.  Overwritting existing value.");
						}
						message.getBody().add(key, entry.getValue());
					}
				}

				this.payloadProxy.setPayload(message, result.toString());
			} else if (stdoutResult != null) {
				this.payloadProxy.setPayload(message, stdoutResult);
			}
		} catch (final IOException e) {
			if (this.runtimeOnIOFailure) {
				throw new RuntimeException("IOFailure", e);
			} else {
				throw new ActionProcessingException(e);
			}
		} catch (final InterruptedException e) {
			throw new ActionProcessingException(e);
		} catch (final ObjectMappingException e) {
			throw new ActionProcessingException(e);
		} catch (final MessageDeliverException e) {
			throw new ActionProcessingException(e);
		} finally {
			if (stdoutThread != null && stdoutThread.isAlive()) {
				stdoutThread.interrupt();
			}
			if (stderrThread != null && stderrThread.isAlive()) {
				stderrThread.interrupt();
			}
		}
	}

	public Message process(final Message message)
			throws ActionProcessingException {
		final List<String> parameters = new ArrayList<String>();
		final Map<String, String> environmentVars = new HashMap<String, String>();

		if (this.execFromPayload) {
			Object execXml = null;

			try {
				execXml = this.payloadProxy.getPayload(message);

				final SAXReader reader = new SAXReader();

				final Document exec = reader.read(new StringReader(execXml
						.toString()));
				final Node commandElement = exec
						.selectSingleNode("exec/command");

				String cmd = this.shellCommand;
				if (commandElement != null) {
					if (cmd != null) {
						parameters.add(commandElement.getText());
					} else {
						cmd = commandElement.getText();
					}
				}

				final List<?> parameterElements = exec
						.selectNodes("exec/parameter");

				for (final Object parameter : parameterElements) {
					parameters.add(((Node) parameter).getText());
				}
			} catch (final MessageDeliverException e) {
				throw new ActionProcessingException(e);
			} catch (final DocumentException e) {
				LOGGER.info("process  : @memo execXml = " + execXml);
				throw new ActionProcessingException(e);
			}
		} else {
			for (final IValueLocator parameterLocator : this.parameterLocators) {
				final Object value = parameterLocator.getValue(message);
				if (value != null) {
					parameters.add(value.toString());
				}
			}

			for (final Map.Entry<String, IValueLocator> entry : this.environmentLocators
					.entrySet()) {
				final Object value = entry.getValue().getValue(message);
				if (value != null) {
					environmentVars.put(entry.getKey(), value.toString());
				}
			}
		}

		// TODO: Make this an error (exception)
		if (this.shellCommand != null && !new File(this.shellCommand).exists()) {
			LOGGER.error(this.shellCommand + " does not exists! Not invoking: "
					+ this.shellCommand + " with parameters " + parameters
					+ " with environemt " + environmentVars + " in directory "
					+ this.workingDirectory);

			if (this.attachRawData) {
				final StringBuffer commandLine = new StringBuffer(
						this.shellCommand);

				for (final String parameter : parameters) {
					commandLine.append(" \"");
					commandLine.append(parameter);
					commandLine.append("\"");
				}
				message.getAttachment().put("command-line",
						commandLine.toString());

				final StringBuffer environmentStr = new StringBuffer();
				for (final Map.Entry<String, String> entry : environmentVars
						.entrySet()) {
					environmentStr.append(entry.getKey()).append("=").append(
							entry.getValue()).append("\n");
				}

				message.getAttachment().put("command-environment",
						environmentStr.toString());
			}
			return message;
		}

		invokeShellCommand(message, this.shellCommand, parameters,
				environmentVars);

		return message;
	}
}
