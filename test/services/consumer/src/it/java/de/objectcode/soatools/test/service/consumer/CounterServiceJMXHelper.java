package de.objectcode.soatools.test.service.consumer;

import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

public class CounterServiceJMXHelper {
	ObjectName counterServiceName;
	RMIAdaptor server;

	public CounterServiceJMXHelper() throws Exception {
		SecurityAssociation.setPrincipal(new SimplePrincipal("admin"));
		SecurityAssociation.setCredential("admin");

		final InitialContext ctx = new InitialContext();

		this.server = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
		this.counterServiceName = new ObjectName(
				"de.objectcode.soatools.test:service=ConsumerCounterService");

	}

	public long getErrorCounter() throws Exception {
		final String[] sig = {};
		final Object[] opArgs = {};
		final Object result = this.server.invoke(this.counterServiceName,
				"getErrorCounter", opArgs, sig);

		return (Long) result;
	}

	public long getInvokationCounter() throws Exception {
		final String[] sig = {};
		final Object[] opArgs = {};
		final Object result = this.server.invoke(this.counterServiceName,
				"getInvokationCounter", opArgs, sig);

		return (Long) result;
	}

	public void reset() throws Exception {
		final String[] sig = {};
		final Object[] opArgs = {};
		this.server.invoke(this.counterServiceName, "reset", opArgs, sig);
	}
}
