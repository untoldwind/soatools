package de.objectcode.soatools.camel;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.jboss.soa.esb.Service;

public class JbossESBEndpoint extends DefaultEndpoint {
	private final Service service;
	
    public JbossESBEndpoint(String uri, String serviceCategory, String serviceName, Component component) {
        super(uri, component);
        
        service = new Service(serviceCategory, serviceName);
    }
    
	public Consumer createConsumer(Processor processor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Producer createProducer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSingleton() {
		return true;
	}

}
