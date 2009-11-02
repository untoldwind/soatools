package de.objectcode.soatools.mfm.action;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jbpm.JbpmConfiguration;
import org.jbpm.command.CommandService;
import org.jbpm.command.SignalCommand;
import org.jbpm.command.impl.CommandServiceImpl;

public class SignalJbpmToken extends AbstractActionPipelineProcessor {
	private final CommandService commandService;

	public SignalJbpmToken(ConfigTree config) throws ConfigurationException {
		JbpmConfiguration configuration = JbpmConfiguration.getInstance();

		commandService = new CommandServiceImpl(configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		Long tokenId = (Long) message.getProperties().getProperty(
				"jbpm-token-id");

		if (tokenId == null) {
			throw new ActionProcessingException("Token id not found");
		}

		SignalCommand signal = new SignalCommand(tokenId, null);

		commandService.execute(signal);

		return message;
	}
}
