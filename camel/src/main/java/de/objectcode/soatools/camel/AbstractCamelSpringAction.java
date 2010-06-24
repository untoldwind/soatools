package de.objectcode.soatools.camel;

import org.apache.camel.CamelContext;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractCamelSpringAction extends AbstractCamelAction {
	protected String[] springContextUrls;
	protected String camelContextRef;
	private ApplicationContext applicationContext;
	private CamelContext camelContext;

	protected AbstractCamelSpringAction(ConfigTree config)
			throws ConfigurationException {
		super(config);
	}

	protected AbstractCamelSpringAction(ConfigTree config,
			String[] springContextUrls, String camelContextRef,
			String endpointUri) throws ConfigurationException {
		super(config, endpointUri);

		this.springContextUrls = springContextUrls;
		this.camelContextRef = camelContextRef;
	}

	@Override
	protected CamelContext getCamelContext() {
		if (camelContext == null) {
			camelContext = (CamelContext) getApplicationContext().getBean(
					camelContextRef);
		}

		return camelContext;
	}

	protected ApplicationContext getApplicationContext() {
		if (applicationContext == null)
			applicationContext = new ClassPathXmlApplicationContext(
					springContextUrls);
		return applicationContext;
	}
}
