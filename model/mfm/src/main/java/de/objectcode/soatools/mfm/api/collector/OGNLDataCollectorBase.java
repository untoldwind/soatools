package de.objectcode.soatools.mfm.api.collector;

/**
 * Implementation of the IDataCollector interface to serialize into OGNL conform
 * key-value pairs.
 * 
 * @author junglas
 */
public abstract class OGNLDataCollectorBase implements IDataCollector {

	/**
	 * {@inheritDoc}
	 */
	public IDataCollector addComponent(String name) {
		return new ComponentDataCollector(name + ".");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addToArray(String name, int index, Object value) {
		setValue(name + "[" + index + "]", value);
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataCollector addToComponentArray(String name, int index) {
		return new ComponentDataCollector(name + "[" + index + "].");
	}

	/**
	 * {@inheritDoc}
	 */
	public void addValue(String name, Object value) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}
		setValue(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePart(String name) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}
		removeValue(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTypeInformation(String type, int version) {
	}

	/**
	 * Store an OGNL conform key-value pair.
	 * 
	 * @param key
	 *            The OGNL conform key
	 * @param value
	 *            The value
	 */
	protected abstract void setValue(String key, Object value);

	/**
	 * Remove an OGNL conform key.
	 * 
	 * @param key
	 *            The key to remove
	 */
	protected abstract void removeValue(String key);

	protected class ComponentDataCollector implements IDataCollector {
		String path;

		protected ComponentDataCollector(String path) {
			this.path = path;
		}

		/**
		 * {@inheritDoc}
		 */
		public IDataCollector addComponent(String name) {
			return new ComponentDataCollector(path + name + ".");
		}

		/**
		 * {@inheritDoc}
		 */
		public void addToArray(String name, int index, Object value) {
			setValue(path + name + "[" + index + "]", value);
		}

		/**
		 * {@inheritDoc}
		 */
		public IDataCollector addToComponentArray(String name, int index) {
			return new ComponentDataCollector(path + name + "[" + index + "].");
		}

		/**
		 * {@inheritDoc}
		 */
		public void addValue(String name, Object value) {
			if (name.startsWith("@")) {
				name = name.substring(1);
			}
			setValue(path + name, value);
		}

		/**
		 * {@inheritDoc}
		 */
		public void removePart(String name) {
			if (name.startsWith("@")) {
				name = name.substring(1);
			}
			removeValue(path + name);
		}

		/**
		 * {@inheritDoc}
		 */
		public void setTypeInformation(String type, int version) {
		}
	}
}
