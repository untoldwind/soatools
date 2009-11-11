package de.objectcode.soatools.test.mock.webservice.client;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.1-b03- Generated
 * source version: 2.0
 * 
 */
@WebServiceClient(name = "MockWebServiceControlWSService", targetNamespace = "http://objectcode.de/test/mock/webservice", wsdlLocation = "http://localhost:8080/test-mock/MockWebServiceControlWS?wsdl")
public class MockWebServiceControlWSService extends Service {

	private final static URL MOCKWEBSERVICECONTROLWSSERVICE_WSDL_LOCATION;

	static {
		MOCKWEBSERVICECONTROLWSSERVICE_WSDL_LOCATION = MockWebServiceControlWSService.class
				.getClassLoader().getResource("mock-webservice-control.wsdl");
	}

	public MockWebServiceControlWSService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public MockWebServiceControlWSService() {
		super(MOCKWEBSERVICECONTROLWSSERVICE_WSDL_LOCATION, new QName(
				"http://objectcode.de/test/mock/webservice",
				"MockWebServiceControlWSService"));
	}

	/**
	 * 
	 * @return returns MockWebServiceControlWS
	 */
	@WebEndpoint(name = "MockWebServiceControlWSPort")
	public MockWebServiceControlWS getMockWebServiceControlWSPort() {
		return (MockWebServiceControlWS) super.getPort(new QName(
				"http://objectcode.de/test/mock/webservice",
				"MockWebServiceControlWSPort"), MockWebServiceControlWS.class);
	}

}
