package de.objectcode.soatools.mfm.api.collector;

import java.util.ArrayList;
import java.util.List;

import org.jboss.soa.esb.message.Attachment;
import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.Properties;

import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

/**
 * Implementation of the IDataCollector interface to serialize data to a SOA
 * message.
 * 
 * @author junglas
 */
public class MessageBodyCollector implements IDataCollector {
	Properties properties;
	Body body;
	Attachment attachment;

	public MessageBodyCollector(Message message) {
		body = message.getBody();
		attachment = message.getAttachment();
		properties = message.getProperties();
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataCollector addComponent(String name) {
		if (body.get(name) != null) {
			attachment.put("original-" + name, body.get(name));
		}

		NormalizedData data = new NormalizedData();

		body.add(name, data);

		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void addToArray(String name, int index, Object value) {
		List<Object> array = (List<Object>) body.get(name);

		if (array == null) {
			array = new ArrayList<Object>();

			body.add(name, array);
		}

		array.add(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public IDataCollector addToComponentArray(String name, int index) {
		List<NormalizedData> array = (List<NormalizedData>) body.get(name);

		if (array == null) {
			array = new ArrayList<NormalizedData>();

			body.add(name, array);
		}

		NormalizedData component = new NormalizedData();

		array.add(component);

		return component;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addValue(String name, Object value) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}

		if (body.get(name) != null) {
			attachment.put("original-" + name, body.get(name));
		}

		body.add(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePart(String name) {
		// Don't do this. We want leave the original message as intact as
		// possible
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTypeInformation(String type, int version) {
		properties.setProperty("mfm-type", type);
		properties.setProperty("mfm-version", version);
	}
}
