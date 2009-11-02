package de.objectcode.soatools.mfm.api.accessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.soa.esb.message.Body;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.Properties;

/**
 * Implementation of the IDataAccessor interface to query the data of a SOA
 * message.
 * 
 * @author junglas
 */
public class MessageBodyDataAccessor implements IDataAccessor {
	Properties properties;
	Body body;

	public MessageBodyDataAccessor(Message message) {
		body = message.getBody();
		properties = message.getProperties();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Object> getArray(String name) {
		Object values = body.get(name);

		if (values != null) {
			if (values instanceof Collection<?>) {
				List<Object> result = new ArrayList<Object>();

				result.addAll((Collection<?>) values);

				return result;
			} else if (values instanceof Object[]) {
				List<Object> result = new ArrayList<Object>();

				for (Object value : (Object[]) values) {
					result.add(value);
				}
				return result;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataAccessor getComponent(String name) {
		Object value = body.get(name);

		if (value != null) {
			try {
				return DataAccessorFactory.INSTANCE.getAccessor(value);
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IDataAccessor> getComponentArray(String name) {
		Object values = body.get(name);

		if (values != null) {
			if (values instanceof Collection<?>) {
				List<IDataAccessor> result = new ArrayList<IDataAccessor>();

				for (Object value : (Collection<?>) values) {
					result.add(DataAccessorFactory.INSTANCE.getAccessor(value));
				}

				return result;
			} else if (values instanceof Object[]) {
				List<IDataAccessor> result = new ArrayList<IDataAccessor>();

				for (Object value : (Object[]) values) {
					result.add(DataAccessorFactory.INSTANCE.getAccessor(value));
				}
				return result;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String name) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}
		return body.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return (String) properties.getProperty("mfm-type");
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVersion() {
		if (properties.getProperty("mfm-version") != null) {
			return (Integer) properties.getProperty("mfm-version");
		}

		return 0;
	}

}
