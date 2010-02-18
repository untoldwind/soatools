package de.objectcode.soatools.util.splitter.actions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.message.format.MessageFactory;

import de.objectcode.soatools.util.splitter.IConstants;
import de.objectcode.soatools.util.splitter.persistent.SplitEntity;

public class Splitter extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(Splitter.class);

	final ServiceInvoker targetService;
	final String serviceCategory;
	final String serviceName;
	final int additionalSplitCount;
	final MessagePayloadProxy payload;
	final SessionFactory sessionFactory;

	public Splitter(ConfigTree config) throws ConfigurationException {
		serviceCategory = config.getParent().getRequiredAttribute(
				"service-category");
		serviceName = config.getParent().getRequiredAttribute("service-name");

		additionalSplitCount = (int) config.getLongAttribute(
				"additional-split-count", 0);
		String targetCategory = config.getRequiredAttribute("target-category");
		String targetName = config.getRequiredAttribute("target-name");

		try {
			targetService = new ServiceInvoker(targetCategory, targetName);
		} catch (MessageDeliverException e) {
			throw new ConfigurationException(e);
		}

		payload = new MessagePayloadProxy(config);

		try {
			final InitialContext ctx = new InitialContext();

			this.sessionFactory = (SessionFactory) ctx
					.lookup(IConstants.SPLITTER_SESSIONFACTORY_JNDI);
		} catch (final NamingException e) {
			throw new ConfigurationException(e);
		}
	}

	public Message process(Message message) throws ActionProcessingException {
		Collection<?> splitMessages;

		try {
			Object data = payload.getPayload(message);

			if (data instanceof Collection<?>)
				splitMessages = (Collection<?>) data;
			else if (data instanceof Object[])
				splitMessages = Arrays.asList((Object[]) data);
			else
				throw new ActionProcessingException(data
						+ " is neither collection or array");
		} catch (MessageDeliverException e) {
			LOG.error("Exception", e);

			throw new ActionProcessingException(e);
		}
		Session session = null;

		try {
			session = sessionFactory.openSession();

			int partCount = additionalSplitCount + splitMessages.size();
			SplitEntity splitEntity = new SplitEntity(serviceCategory,
					serviceName, partCount);

			session.persist(splitEntity);
			session.flush();

			int partIndex = additionalSplitCount;

			for (Object splitMessage : splitMessages) {
				doSend(splitEntity.getId(), partIndex++, partCount,
						splitMessage);
			}
		} catch (final Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return message;
	}

	private void doSend(long splitId, int partIndex, int partCount,
			Object splitMessage) throws MessageDeliverException {
		Message message;

		if (splitMessage instanceof Message) {
			message = (Message) splitMessage;
		} else if (splitMessage instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) splitMessage;

			message = MessageFactory.getInstance().getMessage();

			for (Map.Entry<?, ?> entry : map.entrySet()) {
				message.getBody().add(entry.getKey().toString(),
						entry.getValue());
			}
		} else {
			message = MessageFactory.getInstance().getMessage();

			message.getBody().add(splitMessage);
		}

		message.getProperties().setProperty(IConstants.SPLITTER_ID, splitId);
		message.getProperties().setProperty(IConstants.SPLITTER_PART_INDEX,
				partIndex);
		message.getProperties().setProperty(IConstants.SPLITTER_PART_COUNT,
				partCount);

		targetService.deliverAsync(message);
	}
}
