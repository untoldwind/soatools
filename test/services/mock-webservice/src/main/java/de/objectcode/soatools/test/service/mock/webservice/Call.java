package de.objectcode.soatools.test.service.mock.webservice;

import java.io.Serializable;

public class Call implements Serializable {
	private static final long serialVersionUID = 1L;

	private String methodName;
	private String testCaseName;
	private int testCaseCount;
	private String data;

	public Call() {

	}

	public Call(String methodName, String testCaseName, int testCaseCount,
			String data) {
		this.methodName = methodName;
		this.testCaseName = testCaseName;
		this.testCaseCount = testCaseCount;
		this.data = data;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	public int getTestCaseCount() {
		return testCaseCount;
	}

	public void setTestCaseCount(int testCaseCount) {
		this.testCaseCount = testCaseCount;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
