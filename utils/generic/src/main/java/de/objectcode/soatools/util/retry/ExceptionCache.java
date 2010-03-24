package de.objectcode.soatools.util.retry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ExceptionCache {
	private static ExceptionCache INSTANCE = new ExceptionCache();

	private final static int EXCEPTION_CACHE_SIZE = 1000;

	private final Map<String, Throwable> exceptionCache = new LinkedHashMap<String, Throwable>(
			EXCEPTION_CACHE_SIZE) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Entry<String, Throwable> eldest) {
			return size() >= EXCEPTION_CACHE_SIZE;
		}
	};

	public synchronized void addException(String messageId, Throwable th) {
		exceptionCache.put(messageId, th);
	}

	public synchronized Throwable findException(String messageId) {
		return exceptionCache.get(messageId);
	}

	public static ExceptionCache getInstance() {
		return INSTANCE;
	}
}
