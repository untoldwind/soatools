package de.objectcode.soatools.test.service.mock.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "MockSoapServiceWS", targetNamespace = "http://objectcode.de/test/mock/webservice")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class MockSoapServiceWS {
	public void simpleCallOneWay(
			@WebParam(name = "testCaseName") final String testCaseName,
			@WebParam(name = "testCaseCount") final int testCaseCount,
			@WebParam(name = "data") final String data) {
	}
}
