package de.objectcode.soatools.util.splitter.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.lob.BlobImpl;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;

import de.objectcode.soatools.util.splitter.IConstants;
import de.objectcode.soatools.util.splitter.persistent.SplitEntity;
import de.objectcode.soatools.util.splitter.persistent.SplitPartEntity;
import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

public class Aggregator extends AbstractActionPipelineProcessor {
	private final static Log LOG = LogFactory.getLog(Splitter.class);

	final SessionFactory sessionFactory;
	final MessagePayloadProxy payload;
	final Map<String, IValueLocator> valueLocators;
	final IMessageCombiner messageCombiner;

	public Aggregator(ConfigTree config) throws ConfigurationException {
		payload = new MessagePayloadProxy(config);

		String messageCombinerClass = config
				.getAttribute("message-combiner-class");

		if (messageCombinerClass != null) {
			try {
				messageCombiner = (IMessageCombiner) Class.forName(
						messageCombinerClass).newInstance();
			} catch (Exception e) {
				throw new ConfigurationException(e);
			}
		} else
			messageCombiner = null;

		valueLocators = new HashMap<String, IValueLocator>();
		ConfigTree[] valueConfigs = config.getChildren("value");
		for (ConfigTree valueConfig : valueConfigs) {
			valueLocators.put(valueConfig.getRequiredAttribute("name"),
					ValueLocatorFactory.INSTANCE
							.createValueLocator(valueConfig));
		}

		try {
			final InitialContext ctx = new InitialContext();

			this.sessionFactory = (SessionFactory) ctx
					.lookup(IConstants.SPLITTER_SESSIONFACTORY_JNDI);
		} catch (final NamingException e) {
			throw new ConfigurationException(e);
		}
	}

	public Message process(Message message) throws ActionProcessingException {
		Long splitId = (Long) message.getProperties().getProperty(
				IConstants.SPLITTER_ID);

		if (splitId == null)
			throw new ActionProcessingException(IConstants.SPLITTER_ID
					+ " not found");

		Integer partIndex = (Integer) message.getProperties().getProperty(
				IConstants.SPLITTER_PART_INDEX);

		if (partIndex == null)
			throw new ActionProcessingException(IConstants.SPLITTER_PART_INDEX
					+ " not found");

		Session session = null;

		try {
			session = sessionFactory.openSession();

			SplitEntity splitEntity = (SplitEntity) session.get(
					SplitEntity.class, splitId, LockMode.UPGRADE);

			if (splitEntity == null)
				throw new ActionProcessingException("Split " + splitId
						+ " not found in database");

			Map<Integer, SplitPartEntity> parts = splitEntity.getParts();

			if (partIndex < 0 || partIndex >= splitEntity.getPartCount())
				throw new ActionProcessingException("Part index " + partIndex
						+ " out of bounds: " + splitEntity.getPartCount());

			if (parts.containsKey(partIndex))
				throw new ActionProcessingException("Part " + partIndex
						+ "in split " + splitId + " already received");

			if (parts.size() == splitEntity.getPartCount() - 1) {
				List<Map<String, Object>> partContents = new ArrayList<Map<String, Object>>(
						Collections.<Map<String, Object>> nCopies(splitEntity
								.getPartCount(), null));

				partContents.set(partIndex, getContents(message));

				for (Map.Entry<Integer, SplitPartEntity> entry : parts
						.entrySet()) {
					partContents.set(entry.getKey(), deserializeContent(entry
							.getValue().getContent()));
				}

				session.delete(splitEntity);
				session.flush();

				if (messageCombiner != null)
					return messageCombiner.combine(message, partContents);
				else {
					payload.setPayload(message, partContents);

					return message;
				}
			} else {
				SplitPartEntity part = new SplitPartEntity(partIndex,
						splitEntity, serializeContent(message));

				session.persist(part);
				session.flush();

				return null;
			}
		} catch (final ActionProcessingException e) {
			throw e;
		} catch (final Exception e) {
			LOG.error("Exception", e);
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private Blob serializeContent(Message message) throws IOException,
			ActionProcessingException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(getContents(message));
		oos.flush();
		oos.close();

		return new BlobImpl(bos.toByteArray());
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> deserializeContent(Blob content)
			throws IOException, SQLException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(content.getBinaryStream());

		Map<String, Object> result = (Map<String, Object>) ois.readObject();

		ois.close();

		return result;
	}

	private Map<String, Object> getContents(Message message)
			throws ActionProcessingException {
		Map<String, Object> contents = new HashMap<String, Object>();

		for (Map.Entry<String, IValueLocator> entry : valueLocators.entrySet()) {
			contents.put(entry.getKey(), entry.getValue().getValue(message));
		}

		return contents;
	}

}
