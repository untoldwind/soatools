package de.objectcode.soatools.camel;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;

public class CamelSpringAction extends AbstractCamelSpringAction {

	protected CamelSpringAction(ConfigTree config)
			throws ConfigurationException {
		super(config);
		
		if ( getCamelContext() == null ) {
			throw new ConfigurationException("Camel context not found: " + camelContextRef);
		}
	}

}
