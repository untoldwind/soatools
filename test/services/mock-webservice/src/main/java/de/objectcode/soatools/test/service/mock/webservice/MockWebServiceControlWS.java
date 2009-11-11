package de.objectcode.soatools.test.service.mock.webservice;

import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "MockSoapServiceWS", targetNamespace = "http://objectcode.de/test/mock/webservice")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class MockWebServiceControlWS {
	@WebResult(name = "calls")
	public CallList getCalls() {
		return new CallList(MockState.INSTANCE.getCalls());
	}

	public void clearCalls() {
		MockState.INSTANCE.clear();
	}
}
