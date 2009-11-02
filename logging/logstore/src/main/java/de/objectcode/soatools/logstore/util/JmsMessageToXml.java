package de.objectcode.soatools.logstore.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.jms.JMSException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;

/**
 * Create a readable xml representation of an arbitraty JMS message.
 * 
 * @author junglas
 */
public class JmsMessageToXml {
	private final static SimpleDateFormat FORMAT_UNIVERSAL = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public Message process(final Object obj) throws JMSException, IOException,
			MessageDeliverException {
		if (!(obj instanceof javax.jms.Message)) {
			throw new IllegalArgumentException(
					"Object must be instance of javax.jms.Message");
		}

		final Message esbMessage = MessageFactory.getInstance().getMessage();

		esbMessage.getBody().add(transform((javax.jms.Message) obj));

		return esbMessage;
	}

	private String transform(javax.jms.Message jmsMessage) throws JMSException,
			IOException {
		Document document = DocumentFactory.getInstance().createDocument();
		Element root = document.addElement("jms-message");

		transformBasic(jmsMessage, root);

		Element properties = root.addElement("properties");

		transformProperties(jmsMessage, properties);

		if (jmsMessage instanceof javax.jms.TextMessage) {
			formatText((javax.jms.TextMessage) jmsMessage, root);
		} else if (jmsMessage instanceof javax.jms.BytesMessage) {
			formatBytes((javax.jms.BytesMessage) jmsMessage, root);
		} else if (jmsMessage instanceof javax.jms.MapMessage) {
			formatMap((javax.jms.MapMessage) jmsMessage, root);
		} else if (jmsMessage instanceof javax.jms.ObjectMessage) {
			formatObject((javax.jms.ObjectMessage) jmsMessage, root);
		}
		StringWriter out = new StringWriter();
		XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());

		writer.write(document);
		writer.close();

		return out.toString();
	}

	private void transformBasic(javax.jms.Message jmsMessage, Element parent)
			throws JMSException {
		if (jmsMessage.getJMSType() != null) {
			Element type = parent.addElement("type");
			type.addText(jmsMessage.getJMSType());
		}

		Element messageId = parent.addElement("message-id");
		messageId.addText(jmsMessage.getJMSMessageID());

		if (jmsMessage.getJMSCorrelationID() != null) {
			Element correlationId = parent.addElement("correlation-id");
			correlationId.addText(jmsMessage.getJMSCorrelationID());
		}

		Element redelivered = parent.addElement("redelivered");
		redelivered.addText(Boolean.toString(jmsMessage.getJMSRedelivered()));

		if (jmsMessage.getJMSDestination() != null) {
			Element destination = parent.addElement("destination");
			destination.addText(jmsMessage.getJMSDestination().toString());
		}

		if (jmsMessage.getJMSReplyTo() != null) {
			Element replyTo = parent.addElement("reply-to");
			replyTo.addText(jmsMessage.getJMSReplyTo().toString());
		}

		Element timestamp = parent.addElement("timestamp");
		timestamp.addText(FORMAT_UNIVERSAL.format(new Date(jmsMessage
				.getJMSTimestamp())));
	}

	private void transformProperties(javax.jms.Message jmsMessage,
			Element parent) throws JMSException {
		Enumeration<?> namesEnum = jmsMessage.getPropertyNames();

		while (namesEnum.hasMoreElements()) {
			String name = (String) namesEnum.nextElement();
			Object value = jmsMessage.getObjectProperty(name);

			if (value != null) {
				Element property = parent.addElement("property");

				property.addAttribute("name", name);
				property.addText(value.toString());
			}

		}
	}

	private void formatText(javax.jms.TextMessage jmsMessage, Element parent)
			throws JMSException {
		Element textBody = parent.addElement("text-body");

		textBody.addCDATA(jmsMessage.getText());
	}

	private void formatBytes(javax.jms.BytesMessage jmsMessage, Element parent)
			throws JMSException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int readed;

		while ((readed = jmsMessage.readBytes(buffer)) > 0) {
			out.write(buffer, 0, readed);
		}
		out.close();

		byte[] base64 = Base64.encodeBase64(out.toByteArray());

		Element byteBody = parent.addElement("byte-body");
		byteBody.addText(new String(base64));
	}

	private void formatMap(javax.jms.MapMessage jmsMessage, Element parent)
			throws JMSException {
		Element mapBody = parent.addElement("map-body");
		Enumeration<?> namesEnum = jmsMessage.getMapNames();

		while (namesEnum.hasMoreElements()) {
			String name = (String) namesEnum.nextElement();
			Object value = jmsMessage.getObject(name);

			if (value != null) {
				Element property = mapBody.addElement("element");

				property.addAttribute("name", name);
				property.addText(value.toString());
			}
		}
	}

	private void formatObject(javax.jms.ObjectMessage jmsMessage, Element parent)
			throws JMSException {
		Serializable obj = jmsMessage.getObject();
		if (obj != null) {
			byte[] ser = SerializationUtils.serialize(obj);
			byte[] base64 = Base64.encodeBase64(ser);

			Element byteBody = parent.addElement("object-body");
			byteBody.addAttribute("class", obj.getClass().getName());
			byteBody.addText(new String(base64));
		}
	}
}
