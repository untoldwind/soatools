package de.objectcode.soatools.test.services.consumer;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {
	public static Counter INSTANCE = new Counter();

	AtomicLong errorCounter = new AtomicLong();

	AtomicLong invokationCounter = new AtomicLong();

	private Counter() {
	}

	public synchronized void reset() {
		errorCounter.set(0);
		invokationCounter.set(0);
	}
}
