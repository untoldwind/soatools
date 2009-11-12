package de.objectcode.soatools.test.service.error;

import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;

public class ErrorServiceJMXHelper {
	ObjectName errorServiceName;
	RMIAdaptor server;

	public ErrorServiceJMXHelper() throws Exception {
		SecurityAssociation.setPrincipal(new SimplePrincipal("admin"));
		SecurityAssociation.setCredential("admin");

		final InitialContext ctx = new InitialContext();

		this.server = (RMIAdaptor) ctx.lookup("jmx/invoker/RMIAdaptor");
		this.errorServiceName = new ObjectName(
				"de.objectcode.soatools.test:service=ErrorService");

	}

	public int getFailureCount() throws Exception {
		final String[] sig = {};
		final Object[] opArgs = {};
		final Object result = this.server.invoke(this.errorServiceName,
				"getFailureCount", opArgs, sig);

		return (Integer) result;
	}

	public void setFailureCount(int failureCount) throws Exception {
		final String[] sig = { "int" };
		final Object[] opArgs = { failureCount };
		this.server.invoke(this.errorServiceName, "setFailureCount", opArgs,
				sig);
	}

	public String getExceptionType() throws Exception {
		final String[] sig = {};
		final Object[] opArgs = {};
		final Object result = this.server.invoke(this.errorServiceName,
				"getExceptionType", opArgs, sig);

		return (String) result;
	}

	public void setExceptionType(String exceptionType) throws Exception {
		final String[] sig = { "java.lang.String" };
		final Object[] opArgs = { exceptionType };
		this.server.invoke(this.errorServiceName, "setExceptionType", opArgs,
				sig);
	}

}
