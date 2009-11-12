package de.objectcode.soatools.test.services.error;

public interface ErrorServiceMBean {
	String getExceptionType();

	void setExceptionType(String exceptionType);

	int getFailureCount();

	void setFailureCount(int failureCount);

	void start() throws Exception;

	void stop();
}
