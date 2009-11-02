package de.objectcode.soatools.util.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.gateway.LocalFileMessageComposer;
import org.jboss.soa.esb.message.Message;

/**
 * Used in combination with a FileGatewayListener to log the files being read.
 * 
 * The debug log also contains the file's content.
 * 
 * @author junglas
 */
public class FileInLog extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(FileInLog.class);

	public FileInLog(final ConfigTree config) throws ConfigurationException {
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(final Message message)
			throws ActionProcessingException {
		String path = (String) message.getProperties().getProperty(
				LocalFileMessageComposer.PROP_FILE_PATH, null);

		if (path == null) {
			LOG.warn("No filename in message");
		} else if (LOG.isInfoEnabled()) {
			LOG.info("Readed file: " + path);
		}

		if (LOG.isDebugEnabled()) {
			Object content = message.getBody().get();

			if (content instanceof String) {
				LOG.debug("Content of " + path + ": " + content);
			} else if (content instanceof byte[]) {
				String string = new String((byte[]) content);
				LOG.debug("Content of " + path + ": " + string);
			} else {
				LOG.debug("Content is " + content.getClass());
				LOG.debug("Content of " + path + ": " + content);
			}
		}

		return message;
	}
}
