package de.objectcode.soatools.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.services.registry.RegistryException;

public class JbossESBProducer extends DefaultProducer {
    private final JbossESBEndpoint endpoint;
    private ServiceInvoker serviceInvoker;    

    public JbossESBProducer(JbossESBEndpoint endpoint) {
    	super(endpoint);
    	this.endpoint = endpoint;
    }

	public void process(Exchange exchange) throws Exception {
        if (exchange.getPattern().isOutCapable()) {
            // in out
            processInOut(exchange);
        } else {
            // in only
            processInOnly(exchange);
        }
	}

	private void processInOnly(Exchange exchange) throws Exception {
		Message message = JbossESBMessageAdaptor.camelToESB(exchange.getIn());
		
		getServiceInvoker().deliverAsync(message);
	}

	private void processInOut(Exchange exchange) throws Exception {
		Message request = JbossESBMessageAdaptor.camelToESB(exchange.getIn());
		
		Message reply = getServiceInvoker().deliverSync(request, endpoint.getTimeout());
		
		JbossESBMessageAdaptor.esbToCamel(exchange.getOut(), reply);
	}
	
    private ServiceInvoker getServiceInvoker() throws RegistryException, MessageDeliverException {
        // We lazilly create the invokers...
        if(serviceInvoker == null) {
        	serviceInvoker = new ServiceInvoker(endpoint.getService());
        }

        return serviceInvoker;
    }

}
  