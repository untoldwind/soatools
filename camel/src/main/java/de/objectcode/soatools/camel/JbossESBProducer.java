package de.objectcode.soatools.camel;

import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.impl.DefaultProducer;

public class JbossESBProducer extends DefaultProducer {
    private final JmsEndpoint endpoint;

    public JbossESBProducer(JmsEndpoint endpoint) {
    	super(endpoint);
    	this.endpoint = endpoint;
    }

	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
  