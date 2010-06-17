package de.objectcode.soatools.camel;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class JbossESBComponent extends DefaultComponent{

	@Override
	protected Endpoint createEndpoint(String uri, String remaining,
			Map<String, Object> parameters) throws Exception {
		
		String[] split = remaining.split("/");
		
		if ( split.length != 2) {
			throw new IllegalArgumentException("Invalid uri '" + remaining + "' has to be in format 'serviceCategory/serviceName'");
		}
		
		return new JbossESBEndpoint(uri, split[0], split[1], this);
	}

}
