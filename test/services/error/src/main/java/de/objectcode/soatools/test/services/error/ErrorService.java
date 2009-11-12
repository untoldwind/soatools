package de.objectcode.soatools.test.services.error;

public class ErrorService implements ErrorServiceMBean {
	public String getExceptionType() {
		return ErrorState.INSTANCE.getExceptionType().toString();
	}

	public void setExceptionType(String exceptionType) {
		ErrorState.INSTANCE.setExceptionType(ErrorState.ExceptionType
				.valueOf(exceptionType));
	}

	public int getFailureCount() {
		return ErrorState.INSTANCE.getFailureCount();
	}

	public void setFailureCount(int failureCount) {
		ErrorState.INSTANCE.setFailureCount(failureCount);
	}

	public void start() throws Exception {
	}

	public void stop() {
	}

}
