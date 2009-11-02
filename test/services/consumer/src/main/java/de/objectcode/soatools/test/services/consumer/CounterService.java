package de.objectcode.soatools.test.services.consumer;

public class CounterService implements CounterServiceMBean {

	public long getErrorCounter() {
		return Counter.INSTANCE.errorCounter.get();
	}

	public long getInvokationCounter() {
		return Counter.INSTANCE.invokationCounter.get();
	}

	public void reset() {
		Counter.INSTANCE.reset();
	}

	public void start() throws Exception {
	}

	public void stop() {
	}

}
