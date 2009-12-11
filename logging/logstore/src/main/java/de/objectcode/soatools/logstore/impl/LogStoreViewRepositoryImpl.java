package de.objectcode.soatools.logstore.impl;

import java.util.HashMap;
import java.util.Map;

import de.objectcode.soatools.logstore.ILogStoreViewRepository;

public class LogStoreViewRepositoryImpl implements ILogStoreViewRepository {

	private Map<String, String> decoratorMap = new HashMap<String, String>();
	
	public String getDecoratorResourcePath(String uri) {
		synchronized (decoratorMap) {
			return decoratorMap.get(uri);
		}
	}

	public void registerDecorator(String serviceCategory, String serviceName,
			String resourcePath) {
		synchronized (decoratorMap) {
			if ( resourcePath.startsWith("/"))
				resourcePath = resourcePath.substring(1);
			decoratorMap.put(serviceCategory + "/" + serviceName, resourcePath);
		}
	}
}
