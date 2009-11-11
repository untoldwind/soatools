package de.objectcode.soatools.test.service.mock.webservice;

import java.util.ArrayList;
import java.util.List;

public class MockState {
	public final static MockState INSTANCE = new MockState();

	List<Call> calls;

	private MockState() {
		calls = new ArrayList<Call>();
	}

	public void addCall(String methodName, String testCaseName,
			int testCaseCount, String data) {
		synchronized (calls) {
			calls.add(new Call(methodName, testCaseName, testCaseCount, data));
		}
	}

	public List<Call> getCalls() {
		synchronized (calls) {
			return new ArrayList<Call>(calls);
		}
	}

	public void clear() {
		synchronized (calls) {
			calls.clear();
		}
	}
}
