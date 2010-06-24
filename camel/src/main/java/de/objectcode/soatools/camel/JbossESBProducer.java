package de.objectcode.soatools.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;
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
		Message message = MessageFactory.getInstance().getMessage();
		
		JbossESBMessageAdaptor.exchangeToEsb(message, exchange, false);
		
		getServiceInvoker().deliverAsync(message);
	}

	private void processInOut(Exchange exchange) throws Exception {
		Message request = MessageFactory.getInstance().getMessage();
		
		JbossESBMessageAdaptor.exchangeToEsb(request, exchange, false);
		
		Message reply = getServiceInvoker().deliverSync(request, endpoint.getTimeout());
		
		JbossESBMessageAdaptor.esbToExchange(exchange, reply, true);
	}
	
    private ServiceInvoker getServiceInvoker() throws RegistryException, MessageDeliverException {
        // We lazilly create the invokers...
        if(serviceInvoker == null) {
        	serviceInvoker = new ServiceInvoker(endpoint.getService());
        }

        return serviceInvoker;
    }

}
  