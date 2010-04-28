package de.objectcode.soatools.util.healthcheck;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class ESBCheck extends AbstractActionPipelineProcessor {
	public ESBCheck(ConfigTree config) throws ConfigurationException {
	}

	public Message process(Message message) throws ActionProcessingException {
		message.getBody().add("esb-check", HealthState.OK.toString());
		
		return message;
	}
}
