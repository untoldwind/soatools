package de.objectcode.soatools.test.service.jmsgateway;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.gateway.DefaultESBPropertiesSetter;
import org.jboss.soa.esb.listeners.gateway.ESBPropertiesSetter;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.message.format.MessageFactory;

public class PackageMapMessageJmsContents {
	private final static Log LOG = LogFactory
			.getLog(PackageMapMessageJmsContents.class);

	/**
	 * Strategy for setting JMS Properties on the ESB Message object created by
	 * the process method.
	 */
	private ESBPropertiesSetter esbPropertiesStrategy = new DefaultESBPropertiesSetter();
	private MessagePayloadProxy payloadProxy;

	public PackageMapMessageJmsContents(ConfigTree config) {
		this(createPayloadProxy(config));
	}

	public PackageMapMessageJmsContents(MessagePayloadProxy payloadProxy) {
		this.payloadProxy = payloadProxy;
	}

	/**
	 * Will just drop the jms message contents into a esb Message
	 * 
	 * @param obj
	 *            An instance of Message
	 * @return <code>esbMessage</code> A newly created ESB Message object
	 *         populated with the contents obj argument passed in.
	 * @throws JMSException
	 * @throws IOException
	 * @see #setESBMessageBody(javax.jms.Message, Message)
	 */
	public Message process(final Object obj) throws JMSException, IOException,
			MessageDeliverException {
		if (!(obj instanceof javax.jms.Message))
			throw new IllegalArgumentException(
					"Object must be instance of javax.jms.Message");

		final javax.jms.Message jmsMessage = (javax.jms.Message) obj;

		final Message esbMessage = MessageFactory.getInstance().getMessage();

		setESBMessageBody(jmsMessage, esbMessage);

		setPropertiesFromJMSMessage(jmsMessage, esbMessage);

		return esbMessage;
	}

	/**
	 * Set the {@link ESBPropertiesSetter} to be used
	 * 
	 * @param esbPropertiesStrategy
	 *            the strategy to be used
	 */
	public void setEsbPropertiesStrategy(
			ESBPropertiesSetter esbPropertiesStrategy) {
		this.esbPropertiesStrategy = esbPropertiesStrategy;
	}

	/**
	 * Delegates to
	 * {@link DefaultESBPropertiesSetter#setPropertiesFromJMSMessage(javax.jms.Message, Message)}
	 * by default, but this method can be overridden by subclasses that need a
	 * different behaviour. </p> It is also possible to set a different strategy
	 * by setting {@link #setEsbPropertiesStrategy(ESBPropertiesSetter)}
	 */
	protected void setPropertiesFromJMSMessage(javax.jms.Message fromJMS,
			org.jboss.soa.esb.message.Message toESB) throws JMSException {
		esbPropertiesStrategy.setPropertiesFromJMSMessage(fromJMS, toESB);
	}

	/**
	 * This method will set the ESB Message's body to the appropriate type
	 * matching the JMS Message fromJMSMessage argument.
	 * <p>
	 * TextMessage -> Body.add ( String ); BytesMessage -> Body.add ( byte[] );
	 * ObjectMessage -> Body.add ( Object );
	 * </p>
	 * Note: this method will always set the bodies byte array to be backward
	 * compatible.
	 * 
	 * @param fromJMSMessage
	 * @param toESBMessage
	 * @throws JMSException
	 * @throws IOException
	 */
	private void setESBMessageBody(final javax.jms.Message fromJMSMessage,
			final Message toESBMessage) throws JMSException, IOException,
			MessageDeliverException {
		byte[] bodyAsBytes = null;

		if (fromJMSMessage instanceof TextMessage) {
			final String text = ((TextMessage) fromJMSMessage).getText();
			payloadProxy.setPayload(toESBMessage, text);
		} else if (fromJMSMessage instanceof BytesMessage) {
			final BytesMessage jBytes = (BytesMessage) fromJMSMessage;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] ba = new byte[1000];
			int iQread;
			while (-1 != (iQread = jBytes.readBytes(ba))) {
				if (iQread > 0)
					out.write(ba, 0, iQread);
				out.close();
			}
			bodyAsBytes = out.toByteArray();
			payloadProxy.setPayload(toESBMessage, bodyAsBytes);
		} else if (fromJMSMessage instanceof ObjectMessage) {
			final Object object = ((ObjectMessage) fromJMSMessage).getObject();
			payloadProxy.setPayload(toESBMessage, object);
		} else if (fromJMSMessage instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) fromJMSMessage;

			Enumeration<?> it = mapMessage.getMapNames();

			while (it.hasMoreElements()) {
				String name = (String) it.nextElement();

				toESBMessage.getBody().add(name, mapMessage.getObject(name));
			}
		} else {
			LOG.warn("Message type "
					+ fromJMSMessage.getClass().getSimpleName()
					+ " not supported - Message is ignored");
		}
	}

	public static MessagePayloadProxy createPayloadProxy(ConfigTree config) {
		return new MessagePayloadProxy(config);
	}

}
