package de.objectcode.soatools.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.direct.DirectComponent;
import org.apache.camel.impl.DefaultEndpoint;

public class JbossESBEndpoint extends DefaultEndpoint {

    public JbossESBEndpoint(String uri, DirectComponent component) {
        super(uri, component);
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
