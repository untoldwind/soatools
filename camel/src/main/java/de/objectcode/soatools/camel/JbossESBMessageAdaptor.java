package de.objectcode.soatools.camel;

import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;

public class JbossESBMessageAdaptor {
	@SuppressWarnings("unchecked")
	public static void camelToESB(Message esbMessage,
			org.apache.camel.Message camelMessage) {
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
	}

	public static void exchangeToEsb(Message esbMessage, Exchange exchange,  boolean useOut) {
		if (useOut && exchange.hasOut()) {
			camelToESB(esbMessage, exchange.getOut());
		} else {
			camelToESB(esbMessage, exchange.getIn());
		}

		for (String name : esbMessage.getProperties().getNames())
			exchange.setProperty(name, esbMessage.getProperties().getProperty(
					name));
	}

	public static void esbToCamel(org.apache.camel.Message camelMessage,
			Message esbMessage) {
		String[] names = esbMessage.getBody().getNames();

		if (names.length == 1)
			camelMessage.setBody(esbMessage.getBody().get(names[0]));
		else {
			Map<String, Object> keyValue = new HashMap<String, Object>();

			for (String name : names) {
				if (Body.DEFAULT_LOCATION.equals(name))
					keyValue.put(null, esbMessage.getBody().get(name));
				else
					keyValue.put(name, esbMessage.getBody().get(name));
			}
			camelMessage.setBody(keyValue);
		}

		for (String key : esbMessage.getContext().getContextKeys()) {
			camelMessage
					.setHeader(key, esbMessage.getContext().getContext(key));
		}
	}

	public static void esbToExchange(Exchange exchange, Message esbMessage, boolean useOut) {
		if (useOut && exchange.hasOut()) {
			esbToCamel(exchange.getOut(), esbMessage);
		} else {
			esbToCamel(exchange.getIn(), esbMessage);
		}

		for (String name : esbMessage.getProperties().getNames())
			exchange.setProperty(name, esbMessage.getProperties().getProperty(
					name));
	}
}
