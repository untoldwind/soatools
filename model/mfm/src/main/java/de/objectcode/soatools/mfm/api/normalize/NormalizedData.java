package de.objectcode.soatools.mfm.api.normalize;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import de.objectcode.soatools.mfm.api.accessor.IDataAccessor;
import de.objectcode.soatools.mfm.api.collector.IDataCollector;

/**
 * Store arbitrary hierarchical data in normalized from.
 * 
 * This implementation acts like a map and can be used as data accessor and data
 * collector as well.
 * 
 * @author junglas
 */
public class NormalizedData implements Map<String, Object>, IDataAccessor,
		IDataCollector, Serializable {
	private static final long serialVersionUID = -4702644901875528724L;

	String type;
	Map<String, Object> values = new HashMap<String, Object>();
	int version;

	public NormalizedData() {
	}

	public NormalizedData(final String type, final int version) {
		this.type = type;
		this.version = version;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataCollector addComponent(final String name) {
		final NormalizedData component = new NormalizedData();

		this.values.put(name, component);

		return component;
	}

	// DataCollector implementation

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public void addToArray(final String name, final int index,
			final Object value) {
		List<Object> array = (List<Object>) this.values.get(name);

		if (array == null) {
			array = new ArrayList<Object>();

			this.values.put(name, array);
		}

		array.add(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public IDataCollector addToComponentArray(final String name, final int index) {
		List<NormalizedData> array = (List<NormalizedData>) this.values
				.get(name);

		if (array == null) {
			array = new ArrayList<NormalizedData>();

			this.values.put(name, array);
		}

		final NormalizedData component = new NormalizedData();

		array.add(component);

		return component;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addValue(String name, final Object value) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}

		this.values.put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		this.values.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(final Object key) {
		return this.values.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(final Object value) {
		return this.values.containsValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Entry<String, Object>> entrySet() {
		return Collections.unmodifiableSet(this.values.entrySet());
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(final Object key) {
		return this.values.get(key);
	}

	// ComponentAccessor implementation

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getArray(final String name) {
		return (List<Object>) this.values.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataAccessor getComponent(final String name) {
		return (IDataAccessor) this.values.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<IDataAccessor> getComponentArray(final String name) {
		return (List<IDataAccessor>) this.values.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return this.type;
	}

	// Read-only Map implementation

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(final String name) {
		return this.values.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVersion() {
		return this.version;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return this.values.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> keySet() {
		return Collections.unmodifiableSet(this.values.keySet());
	}

	/**
	 * Convenient method to marshal the hierarchical data to a data collector.
	 * 
	 * @param dataCollector
	 *            The data collector retrieving the data
	 */
	public void marshal(final IDataCollector dataCollector) {
		if (this.type != null) {
			dataCollector.setTypeInformation(this.type, this.version);
		}
		for (final Map.Entry<String, Object> entry : this.values.entrySet()) {
			if (entry.getValue() instanceof NormalizedData) {
				final IDataCollector component = dataCollector
						.addComponent(entry.getKey());

				((NormalizedData) entry.getValue()).marshal(component);
			} else if (entry.getValue() instanceof List<?>) {
				final List<?> array = (List<?>) entry.getValue();

				for (int i = 0; i < array.size(); i++) {
					final Object element = array.get(i);

					if (element instanceof NormalizedData) {
						final IDataCollector component = dataCollector
								.addToComponentArray(entry.getKey(), i);

						((NormalizedData) element).marshal(component);
					} else {
						dataCollector.addToArray(entry.getKey(), i, element);
					}
				}
			} else {
				if (entry.getValue() != null) {
					dataCollector.addValue(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(final String key, final Object value) {
		return this.values.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void putAll(final Map<? extends String, ? extends Object> t) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object remove(final Object key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePart(final String name) {
		this.values.remove(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTypeInformation(final String type, final int version) {
		this.type = type;
		this.version = version;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return this.values.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer("NormalizedData(");

		for (final Iterator<String> keyIter = this.values.keySet().iterator(); keyIter
				.hasNext();) {
			final String key = keyIter.next();
			final Object value = this.values.get(key);
			String name = value != null ? value.getClass().getName() : "";

			name = name.substring(name.lastIndexOf(".") + 1, name.length())
					.toLowerCase();

			buffer.append(key + "={");
			buffer.append(name + "=");
			buffer.append(value + "}");
		}

		buffer.append(")");

		return buffer.toString();
	}

	/**
	 * Convenient method to serialize the data to XML.
	 * 
	 * @return An XML string containing the data
	 */
	public String toXML() {
		final Document document = DocumentFactory.getInstance()
				.createDocument();
		Element element;

		if (this.type != null) {
			element = document.addElement(this.type);
		} else {
			element = document.addElement("normalized-data");
		}

		toXML(element);

		try {
			final StringWriter out = new StringWriter();
			final XMLWriter writer = new XMLWriter(out, OutputFormat
					.createPrettyPrint());

			writer.write(document);

			return out.toString();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Convenient method to serialize the data into a DOM tree.
	 * 
	 * @param parent
	 *            The parent element in the DOM tree
	 */
	public void toXML(final Element parent) {
		if (this.type != null) {
			parent.addAttribute("type", this.type);
			parent.addAttribute("version", String.valueOf(this.version));
		}
		for (final Map.Entry<String, Object> entry : this.values.entrySet()) {
			if (entry.getValue() instanceof NormalizedData) {
				final Element componentElement = parent.addElement(entry
						.getKey());

				((NormalizedData) entry.getValue()).toXML(componentElement);
			} else if (entry.getValue() instanceof Collection<?>) {
				final Element arrayElement = parent.addElement(entry.getKey());
				final Collection<?> array = (Collection<?>) entry.getValue();

				for (final Object element : array) {
					final Element objectElement = arrayElement
							.addElement("object");

					if (element instanceof NormalizedData) {
						((NormalizedData) element).toXML(objectElement);
					} else {
						objectElement.setText(element.toString());
					}
				}
			} else if (entry.getKey().startsWith("@")) {
				parent.addAttribute(entry.getKey().substring(1), entry
						.getValue().toString());
			} else {
				final String key = entry.getKey();
				final Element valueElement = parent.addElement(key);
				final Object value = entry.getValue();

				if (value != null) {
					if (value instanceof String[]) {
						if (!key.endsWith("s")) {
							throw new IllegalArgumentException();
						}

						final String[] values = (String[]) value;

						for (int i = 0; i < values.length; i++) {
							final Element valueValuesElement = valueElement
									.addElement(key.substring(0,
											key.length() - 1));
							valueValuesElement.setText(values[i]);
						}
					} else {
						valueElement.setText(value.toString());
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Object> values() {
		return Collections.unmodifiableCollection(this.values.values());
	}
}
