package de.objectcode.soatools.util.splitter.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.Service;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.addressing.EPR;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.RegistryUtil;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Context;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;
import org.jboss.soa.esb.services.registry.RegistryException;
import org.jboss.soa.esb.services.registry.ServiceNotFoundException;

import de.objectcode.soatools.util.splitter.IConstants;
import de.objectcode.soatools.util.splitter.persistent.SplitEntity;

public class Splitter extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(Splitter.class);

	final String serviceCategory;
	final String serviceName;
	final MessagePayloadProxy payload;
	final SessionFactory sessionFactory;
	final IMessageSplitter messageSplitter;
	final Service aggregatorService;
	final boolean copyContext;
	final boolean hasAggregator;

	private Map<Service, ServiceInvoker> invokers = new LinkedHashMap<Service, ServiceInvoker>();

	public Splitter(ConfigTree config) throws ConfigurationException {
		serviceCategory = config.getParent().getRequiredAttribute(
				"service-category");
		serviceName = config.getParent().getRequiredAttribute("service-name");

		copyContext = config.getBooleanAttribute("copy-context", true);
		hasAggregator = config.getBooleanAttribute("has-aggregator", true);

		String messageSplitterClass = config
				.getAttribute("message-splitter-class");

		if (messageSplitterClass != null) {
			try {
				messageSplitter = (IMessageSplitter) Class.forName(
						messageSplitterClass).newInstance();
			} catch (Exception e) {
				throw new ConfigurationException(e);
			}
		} else
			messageSplitter = null;

		if (config.getAttribute("aggregator-category") != null
				&& config.getAttribute("aggregator-name") != null) {
			aggregatorService = new Service(config
					.getAttribute("aggregator-category"), config
					.getAttribute("aggregator-name"));
		} else
			aggregatorService = null;

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
		List<?> splitMessages;

		if (messageSplitter != null)
			splitMessages = messageSplitter.split(message);
		else {
			try {
				Object data = payload.getPayload(message);

				if (data instanceof Collection<?>)
					splitMessages = new ArrayList<Object>((Collection<?>) data);
				else if (data instanceof Object[])
					splitMessages = Arrays.asList((Object[]) data);
				else
					throw new ActionProcessingException(data
							+ " is neither collection or array");
			} catch (MessageDeliverException e) {
				LOG.error("Exception", e);

				throw new ActionProcessingException(e);
			}
		}

		Session session = null;

		if (hasAggregator) {
			try {
				EPR aggregatorEPR = findAggregatorEPR();

				session = sessionFactory.openSession();

				int partCount = splitMessages.size();
				SplitEntity splitEntity = new SplitEntity(serviceCategory,
						serviceName, partCount);

				session.persist(splitEntity);
				session.flush();

				int partIndex = 0;

				for (Object element : splitMessages) {
					doSend(message.getContext(), splitEntity.getId(),
							partIndex++, partCount, (SplitMessage) element,
							aggregatorEPR);
				}
			} catch (final Exception e) {
				LOG.error("Exception", e);
				throw new RuntimeException(e);
			} finally {
				if (session != null) {
					session.close();
				}
			}
		} else {
			int partCount = splitMessages.size();
			int partIndex = 0;

			try {
				for (Object element : splitMessages) {
					doSend(message.getContext(), null, partIndex++, partCount,
							(SplitMessage) element, null);
				}
			} catch (final Exception e) {
				LOG.error("Exception", e);
				throw new RuntimeException(e);
			}
		}

		return message;
	}

	private EPR findAggregatorEPR() throws RegistryException,
			ServiceNotFoundException {
		if (aggregatorService != null) {
			List<EPR> eprs = RegistryUtil.getEprs(aggregatorService
					.getCategory(), aggregatorService.getName());

			if (eprs.isEmpty())
				throw new RuntimeException("No epr found for service: "
						+ aggregatorService);

			return eprs.get(0);
		}
		return null;

	}

	private void doSend(Context context, Long splitId, int partIndex,
			int partCount, SplitMessage splitMessage, EPR aggregatorEPR)
			throws RegistryException, MessageDeliverException {
		Message message = splitMessage.getMessage();

		if (copyContext) {
			for (String name : context.getContextKeys())
				message.getContext().setContext(name, context.getContext(name));
		}

		if (splitId != null)
			message.getProperties()
					.setProperty(IConstants.SPLITTER_ID, splitId);
		
		message.getProperties().setProperty(IConstants.SPLITTER_PART_INDEX,
				partIndex);
		message.getProperties().setProperty(IConstants.SPLITTER_PART_COUNT,
				partCount);

		if (aggregatorEPR != null) {
			message.getHeader().getCall().setReplyTo(aggregatorEPR);
			message.getHeader().getCall().setFaultTo(aggregatorEPR);
		}

		ServiceInvoker invoker = getInvoker(splitMessage.getTargetService());

		invoker.deliverAsync(message);
	}

	private ServiceInvoker getInvoker(Service recipient)
			throws RegistryException, MessageDeliverException {
		ServiceInvoker invoker = invokers.get(recipient);

		// We lazilly create the invokers...
		if (invoker == null) {
			invoker = new ServiceInvoker(recipient);
			invokers.put(recipient, invoker);
		}

		return invoker;
	}
}
