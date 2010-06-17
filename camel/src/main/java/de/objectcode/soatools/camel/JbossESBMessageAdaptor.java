package de.objectcode.soatools.camel;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.impl.DefaultMessage;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;

public class JbossESBMessageAdaptor {
	public static Message camelToESB(org.apache.camel.Message camelMessage) {
		Message esbMessage = MessageFactory.getInstance().getMessage();

		Object body = camelMessage.getBody();

		if (body instanceof Map<?, ?>) {
			for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) body)
					.entrySet()) {
				if (entry.getKey() == null)
					esbMessage.getBody().add(entry.getValue());
				else
					esbMessage.getBody().add(entry.getKey().toString(),
							entry.getValue());
			}
		} else {
			esbMessage.getBody().add(body);
		}

		esbMessage.getBody().add(camelMessage.getBody());
		for (Map.Entry<String, Object> header : camelMessage.getHeaders()
				.entrySet()) {
			esbMessage.getContext().setContext(header.getKey(),
					header.getValue());
		}
		for (Map.Entry<String, DataHandler> attachment : camelMessage
				.getAttachments().entrySet()) {
			esbMessage.getAttachment().put(attachment.getKey(),
					attachment.getValue());
		}
		return esbMessage;
	}

	public static org.apache.camel.Message esbToCamel(Message esbMessage) {
		DefaultMessage camelMessage = new DefaultMessage();

		String[] names = esbMessage.getBody().getNames();

		if (names.length == 1)
			camelMessage.setBody(esbMessage.getBody().get(names[0]));
		else {
			Map<String, Object> keyValue = new HashMap<String, Object>();
			
			for (String name: names ) {
				if ( Body.DEFAULT_LOCATION.equals(name))
					keyValue.put(null, esbMessage.getBody().get(name));
				else
					keyValue.put(name, esbMessage.getBody().get(name));
			}
			camelMessage.setBody(keyValue);
		}
		
		for(String key : esbMessage.getContext().getContextKeys()) {
			camelMessage.setHeader(key, esbMessage.getContext().getContext(key));
		}

		return camelMessage;
	}
}
