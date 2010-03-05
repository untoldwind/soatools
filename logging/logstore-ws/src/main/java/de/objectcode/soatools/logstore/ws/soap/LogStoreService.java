package de.objectcode.soatools.logstore.ws.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

import de.objectcode.soatools.logstore.ws.soap.types.LogMessageList;
import de.objectcode.soatools.logstore.ws.soap.types.Query;
import de.objectcode.soatools.logstore.ws.soap.types.TagList;

@WebService(name="LogStoreService", targetNamespace="http://objectcode.de/soatools/logstore")
public interface LogStoreService {
	@WebMethod
	TagList getTagNames();

	@WebMethod
	LogMessageList queryLogMessages(Query query);
}
