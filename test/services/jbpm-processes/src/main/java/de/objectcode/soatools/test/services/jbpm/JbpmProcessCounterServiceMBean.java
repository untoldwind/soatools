package de.objectcode.soatools.test.services.jbpm;

public interface JbpmProcessCounterServiceMBean {
	int countProcessInstances(String processName);

	int countFinishedProcessInstances(String processName);

	void start() throws Exception;

	void stop();
}
