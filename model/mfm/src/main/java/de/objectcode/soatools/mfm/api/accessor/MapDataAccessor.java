package de.objectcode.soatools.mfm.api.accessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the IDataAccessor interface to query a hierarchical set of
 * maps.
 * 
 * This implementation also works with OGNL encoded key.
 * 
 * @author junglas
 */
public class MapDataAccessor implements IDataAccessor {
	Map<?, ?> map;
	String path;

	public MapDataAccessor(Map<?, ?> map) {
		this.map = map;
		this.path = null;
	}

	public MapDataAccessor(Map<?, ?> map, String path) {
		this.map = map;
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataAccessor getComponent(String name) {
		String key;

		if (path != null) {
			key = path + "." + name;
		} else {
			key = name;
		}

		Object value = map.get(key);

		if (value == null) {
			for (Object mapKey : map.keySet()) {
				if (mapKey.toString().startsWith(key)) {
					return new MapDataAccessor(map, name);
				}
			}

			return null;
		} else {
			return DataAccessorFactory.INSTANCE.getAccessor(value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IDataAccessor> getComponentArray(String name) {
		String key;

		if (path != null) {
			key = path + "." + name;
		} else {
			key = name;
		}

		Object values = map.get(key);

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
			} else {
				return null;
			}
		} else {
			List<IDataAccessor> result = new ArrayList<IDataAccessor>();
			String base = key + "[";
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String entryKey = entry.getKey().toString();

				if (entryKey.startsWith(base)) {
					int keyIdx = entryKey.indexOf(']', base.length());

					if (keyIdx >= 0) {
						try {
							int idx = Integer.parseInt(entryKey.substring(base
									.length(), keyIdx));

							if (idx < result.size()) {
								result.set(idx, new MapDataAccessor(map, base
										+ "[" + idx + "]"));
							} else {
								result.add(idx, new MapDataAccessor(map, base
										+ "[" + idx + "]"));
							}
						} catch (NumberFormatException e) {
						}
					}
				}
			}
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Object> getArray(String name) {
		String key;

		if (path != null) {
			key = path + "." + name;
		} else {
			key = name;
		}

		Object values = map.get(key);

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
			} else {
				return null;
			}
		} else {
			List<Object> result = new ArrayList<Object>();
			String base = key + "[";
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				String entryKey = entry.getKey().toString();

				if (entryKey.startsWith(base)) {
					int keyIdx = entryKey.indexOf(']', base.length());

					if (keyIdx >= 0) {
						try {
							int idx = Integer.parseInt(entryKey.substring(base
									.length(), keyIdx));

							if (idx < result.size()) {
								result
										.set(idx, map.get(base + "[" + idx
												+ "]"));
							} else {
								result
										.add(idx, map.get(base + "[" + idx
												+ "]"));
							}
						} catch (NumberFormatException e) {
						}
					}
				}
			}
			return result;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String name) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}
		String key;

		if (path != null) {
			key = path + "." + name;
		} else {
			key = name;
		}

		return map.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVersion() {
		return 0;
	}

}
