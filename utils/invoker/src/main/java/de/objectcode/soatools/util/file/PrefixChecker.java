package de.objectcode.soatools.util.file;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.Properties;

/**
 * Use in combination with a FileGatewayListener to add additional check of a
 * prefix of the file name.
 * 
 * @author junglas
 */
public class PrefixChecker extends AbstractActionPipelineProcessor {
	final static private String KEY_FILE_NAME = "org.jboss.soa.esb.gateway.original.file.name";

	/** Logger for this class. */
	private static final Log LOGGER = LogFactory.getLog(PrefixChecker.class);
	private final List<String> preFixes = new ArrayList<String>();

	public PrefixChecker(final ConfigTree config) throws ConfigurationException {
		final ConfigTree[] prefixesConfigTrees = config.getChildren("prefix");

		for (final ConfigTree prefixesConfigTree : prefixesConfigTrees) {
			final String value = prefixesConfigTree.getAttribute("value");
			this.preFixes.add(value);
		}
	}

	public Message process(final Message message)
			throws ActionProcessingException {
		Message retVal = null;

		final Properties properties = message.getProperties();

		final String originFileName = (String) properties
				.getProperty(KEY_FILE_NAME);

		for (final String prefix : this.preFixes) {
			if (originFileName.startsWith(prefix)) {
				retVal = message;
				break;
			}
		}

		if (retVal == null) {
			final String msg = "Set message to null and break action pipeline because <<file: {0}>>did''t match one of the following <<prefixes: {1}>>";
			LOGGER.info(MessageFormat.format(msg, new Object[] {
					originFileName, this.preFixes }));
		}

		return retVal;
	}

}
