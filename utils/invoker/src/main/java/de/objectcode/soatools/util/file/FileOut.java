package de.objectcode.soatools.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.message.mapping.ObjectMapper;
import org.jboss.soa.esb.message.mapping.ObjectMappingException;

import de.objectcode.soatools.util.value.MVELValueLocator;

public class FileOut extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(FileOut.class);

	private final String context;
	private final Map<String, String> contextSpecificFilePath = new HashMap<String, String>();
	private final File destinationDirectory;
	private final String fileNamePath;
	private final MessagePayloadProxy messagePayloadProxy;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final boolean runtimeOnIOFailure;

	private final String tmpSuffix;

	public FileOut(final ConfigTree config) throws ConfigurationException {
		this.messagePayloadProxy = new MessagePayloadProxy(config);
		this.destinationDirectory = new File(config
				.getRequiredAttribute("destination-directory"));
		this.runtimeOnIOFailure = config.getBooleanAttribute(
				"runtime-on-io-failure", true);
		if (!this.destinationDirectory.exists()) {
			throw new ConfigurationException(
					"FileOut: destinationDirectory does not exist:"
							+ this.destinationDirectory);
		}
		if (!this.destinationDirectory.canWrite()) {
			throw new ConfigurationException(
					"FileOut: destinationDirectory not writeable:"
							+ this.destinationDirectory);
		}

		this.tmpSuffix = config.getAttribute("temporary-suffix", ".tmp");

		this.fileNamePath = config.getAttribute("filename-path");
		final ConfigTree[] prefixesConfigTrees = config
				.getChildren("context-specific-filepath");

		this.context = config.getAttribute("context");

		for (final ConfigTree prefixesConfigTree : prefixesConfigTrees) {
			final String context = prefixesConfigTree.getAttribute("context");
			final String prefix = prefixesConfigTree
					.getAttribute("filename-path");
			this.contextSpecificFilePath.put(context, prefix);
		}

		final boolean fileNamePathExists = (this.fileNamePath != null && this.fileNamePath
				.length() != 0);
		final boolean contextSpecificFilePathExist = (this.contextSpecificFilePath != null && this.contextSpecificFilePath
				.size() != 0);
		final boolean contextExists = (this.context != null && this.context
				.trim().length() != 0);

		if (fileNamePathExists && contextSpecificFilePathExist) {
			final String msg = "Eighter <<filename-path: {0}>> or <<contentSpecificFilePath: {1}>> is allowed, not both.";

			throw new ConfigurationException(MessageFormat.format(msg,
					new Object[] { this.fileNamePath,
							this.contextSpecificFilePath }));
		}

		if ((contextSpecificFilePathExist && !contextExists)
				|| (!contextSpecificFilePathExist && contextExists)) {
			final String msg = "If <<contextSpecificFilePath: {0}>> is set, then <<context: {1}>> can''t be null or empty";

			throw new ConfigurationException(MessageFormat
					.format(msg, new Object[] { this.contextSpecificFilePath,
							this.context }));
		}
	}

	private String getFileName(final Message message)
			throws ObjectMappingException, ActionProcessingException {
		String retVal = null;

		final boolean fileNamePathExists = (this.fileNamePath != null && this.fileNamePath
				.length() != 0);
		final boolean contextSpecificFilePathExist = (this.contextSpecificFilePath != null && this.contextSpecificFilePath
				.size() != 0);
		final boolean contextExists = (this.context != null || this.context
				.trim().length() != 0);

		if (fileNamePathExists) {
			retVal = (String) this.objectMapper.getObjectFromMessage(message,
					this.fileNamePath);

			if (retVal == null) {
				String msg = "FileOut: messsage does not contain filename for <<fileNamePath: {0}>>";
				throw new ActionProcessingException(MessageFormat.format(msg,
						this.fileNamePath));
			}
		}

		if (contextExists && contextSpecificFilePathExist) {
			String contextPattern = (String) new MVELValueLocator(this.context)
					.getValue(message);

			String fileNamePattern = contextSpecificFilePath
					.get(contextPattern);

			if (fileNamePattern == null) {
				String msg = "FileOut: Can''t find any entry in  <<contextSpecificFilePath: {0}>> for <<contextPattern:{1}>>";
				throw new ActionProcessingException(MessageFormat.format(msg,
						this.contextSpecificFilePath, contextPattern));
			}

			retVal = (String) new MVELValueLocator(fileNamePattern)
					.getValue(message);

			if (retVal == null) {
				String msg = "FileOut: messsage does not contain filename for <<fileNamePattern: {0}>>";
				throw new ActionProcessingException(MessageFormat.format(msg,
						fileNamePattern));
			}
		}

		return retVal;
	}

	public Message process(final Message message)
			throws ActionProcessingException {
		File file = null;
		try {
			final Object data = this.messagePayloadProxy.getPayload(message);

			if (data == null) {
				LOG.error("FileOut: data empty.");

				throw new ActionProcessingException("FileOut: data empty.");
			}

			final String fileName = getFileName(message);

			final File targetFile = new File(this.destinationDirectory,
					fileName);

			if (targetFile.exists()) {
				LOG.error("FileOut: destination filename " + fileName
						+ " already exists.");

				throw new ActionProcessingException(
						"FileOut: destination filename " + fileName
								+ " already exists.");
			}
			// TODO Maybe use File.createTempFile file
			file = new File(this.destinationDirectory, fileName
					+ this.tmpSuffix);

			if (LOG.isInfoEnabled()) {
				LOG.info("Writing to: " + file.getAbsoluteFile());
			}

			if (data instanceof byte[]) {
				final FileOutputStream fos = new FileOutputStream(file);
				fos.write((byte[]) data);
				fos.flush();
				fos.close();

				if (LOG.isDebugEnabled()) {
					LOG.debug("Wrote to " + file.getAbsolutePath() + ": "
							+ Arrays.toString((byte[]) data));
				}
			} else {
				final FileWriter fw = new FileWriter(file);
				fw.write(data.toString());
				fw.flush();
				fw.close();

				if (LOG.isDebugEnabled()) {
					LOG.debug("Wrote to " + file.getAbsolutePath() + ": "
							+ data.toString());
				}
			}

			if (!file.renameTo(targetFile)) {
				throw new IOException("Rename of " + file.getAbsolutePath()
						+ " to " + targetFile.getAbsolutePath() + " failed");
			}

			if (LOG.isInfoEnabled()) {
				LOG.info("Renamed " + file.getAbsolutePath() + " "
						+ targetFile.getAbsolutePath());
			}
		} catch (final MessageDeliverException e) {
			LOG.error("FileOut Exception", e);
			throw new ActionProcessingException(e);
		} catch (final ObjectMappingException e) {
			LOG.error("FileOut Exception", e);
			throw new ActionProcessingException(e);
		} catch (final FileNotFoundException e) {
			LOG.error("FileOut Exception", e);
			if (this.runtimeOnIOFailure) {
				throw new RuntimeException("FileNotFound", e);
			} else {
				throw new ActionProcessingException(e);
			}
		} catch (final IOException e) {
			LOG.error("FileOut Exception", e);
			if (this.runtimeOnIOFailure) {
				throw new RuntimeException("IOFailure", e);
			} else {
				throw new ActionProcessingException(e);
			}
		} finally {
			try {
				if (file != null) {
					file = new File(file.getAbsolutePath());

					if (file.exists()) {
						LOG.warn("Purging temporary file:"
								+ file.getAbsolutePath());
						file.delete();
					}
				}
			} catch (final Exception e) {
				// TODO: handle exception
			}
		}

		return message;
	}
}
