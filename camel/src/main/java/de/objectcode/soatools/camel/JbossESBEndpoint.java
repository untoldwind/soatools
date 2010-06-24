package de.objectcode.soatools.camel;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.jboss.soa.esb.Service;

public class JbossESBEndpoint extends DefaultEndpoint {
	private final Service service;
	private final long timeout;

	public JbossESBEndpoint(String uri, String serviceCategory,
			String serviceName, long timeout, Component component) {
		super(uri, component);

		this.service = new Service(serviceCategory, serviceName);
		this.timeout = timeout;
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

	public Service getService() {
		return service;
	}

	public long getTimeout() {
		return timeout;
	}

}
