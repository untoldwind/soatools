package de.objectcode.soatools.test.service.mock.webservice;

import java.io.Serializable;
import java.util.List;

public class CallList implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Call> calls;

	public CallList() {

	}

	public CallList(List<Call> calls) {
		this.calls = calls;
	}

	public List<Call> getCalls() {
		return calls;
	}

	public void setCalls(List<Call> calls) {
		this.calls = calls;
	}

}
