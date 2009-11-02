package de.objectcode.soatools.test.service.jbpm;

import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

public class JbpmProcessCounterServiceJMXHelper {
	ObjectName serviceName;
	RMIAdaptor server;

	public JbpmProcessCounterServiceJMXHelper() throws Exception {
		SecurityAssociation.setPrincipal(new SimplePrincipal("admin"));
		SecurityAssociation.setCredential("admin");

		final InitialContext ctx = new InitialContext();

		this.server = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
		this.serviceName = new ObjectName(
				"de.objectcode.soatools.test:service=JbpmProcessCounterService");

	}

	public int countProcessInstances(String processName) throws Exception {
		final String[] sig = { "java.lang.String" };
		final Object[] opArgs = { processName };
		final Object result = this.server.invoke(this.serviceName,
				"countProcessInstances", opArgs, sig);

		return (Integer) result;
	}

	public int countFinishedProcessInstances(String processName)
			throws Exception {
		final String[] sig = { "java.lang.String" };
		final Object[] opArgs = { processName };
		final Object result = this.server.invoke(this.serviceName,
				"countFinishedProcessInstances", opArgs, sig);

		return (Integer) result;
	}

}
