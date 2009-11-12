package de.objectcode.soatools.test.services.error;

import org.jboss.soa.esb.actions.ActionProcessingException;

public class ErrorState {
	public final static ErrorState INSTANCE = new ErrorState();

	public enum ExceptionType {
		ACTIONPROCESSING, RUNTIME, ERROR, INTERNAL_ERROR
	}

	private ExceptionType exceptionType;
	private int failureCount;

	private ErrorState() {
		exceptionType = ExceptionType.ACTIONPROCESSING;
		failureCount = 2;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public void throwError(int retryCount) throws ActionProcessingException {
		if (retryCount >= failureCount)
			return;

		switch (exceptionType) {
		case ACTIONPROCESSING:
			throw new ActionProcessingException(
					"Test action processing exception");
		case RUNTIME:
			throw new RuntimeException("Test runtime exception");
		case ERROR:
			throw new Error("Test internal error");
		case INTERNAL_ERROR:
			throw new InternalError("Test internal error");
		}
	}
}
