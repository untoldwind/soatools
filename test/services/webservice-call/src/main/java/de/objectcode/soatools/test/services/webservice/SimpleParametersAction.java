package de.objectcode.soatools.test.services.webservice;

import java.util.HashMap;
import java.util.Map;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public class SimpleParametersAction extends AbstractActionPipelineProcessor {

	public SimpleParametersAction(ConfigTree config)
			throws ConfigurationException {
	}

	/**
	 * {@inheritDoc}
	 */
	public Message process(Message message) throws ActionProcessingException {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("simpleCallOneWay.testCaseName", message.getBody().get(
				"testCaseName"));
		parameters.put("simpleCallOneWay.testCaseCount", message.getBody().get(
				"testCaseCount"));
		parameters.put("simpleCallOneWay.data", "Some data");

		message.getBody().add(parameters);

		return message;
	}

}