package de.objectcode.soatools.test.services.consumer;

public interface CounterServiceMBean {
	long getErrorCounter();

	long getInvokationCounter();

	void reset();

	void start() throws Exception;

	void stop();
}
