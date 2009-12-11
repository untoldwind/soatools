package de.objectcode.soatools.logstore;

public interface ILogStoreViewRepository {
	String JNDI_NAME = "logStoreViewRepository";

	void registerDecorator(String serviceCategory, String serviceName, String resourcePath);
	
	String getDecoratorResourcePath(String uri);
}
