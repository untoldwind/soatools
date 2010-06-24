package de.objectcode.soatools.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

public abstract class AbstractCamelAction extends
		AbstractActionPipelineProcessor {

	protected boolean inOut;
	protected String endpointUri;
	private Endpoint endpoint;
	private ProducerTemplate producerTemplate;

	protected AbstractCamelAction(ConfigTree config)
			throws ConfigurationException {
		this.endpointUri = config.getRequiredAttribute("endpoint");
		this.inOut = config.getBooleanAttribute("in-out", false);
	}

	protected AbstractCamelAction(ConfigTree config, String endpointUri)
			throws ConfigurationException {
		this.endpointUri = endpointUri;
		this.inOut = config.getBooleanAttribute("in-out", false);
	}

	@Override
	public Message process(Message message) throws ActionProcessingException {
		if (inOut)
			return processInOut(message);
		else
			return processInOnly(message);
	}

	protected Message processInOnly(Message message) {
		Exchange exchange = getEndpoint()
				.createExchange(ExchangePattern.InOnly);

		JbossESBMessageAdaptor.esbToExchange(exchange, message, false);

		getProducerTemplate().send(exchange);

		return message;
	}

	protected Message processInOut(Message message) {
		Exchange exchange = getEndpoint().createExchange(ExchangePattern.InOut);

		JbossESBMessageAdaptor.esbToExchange(exchange, message, false);

		exchange = getProducerTemplate().send(exchange);

		JbossESBMessageAdaptor.exchangeToEsb(message, exchange, true);

		return message;
	}

	protected Endpoint getEndpoint() {
		if (endpoint == null)
			endpoint = getCamelContext().getEndpoint(endpointUri);
		return endpoint;
	}

	protected ProducerTemplate getProducerTemplate() {
		if (producerTemplate == null)
			producerTemplate = new DefaultProducerTemplate(getCamelContext(),
					getEndpoint());
		return producerTemplate;
	}

	protected abstract CamelContext getCamelContext();
}
