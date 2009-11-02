package de.objectcode.soatools.mfm.api.collector;

import java.util.Map;

/**
 * Implementation of the IDataCollector interface to serialize the data into an
 * OGNL conform map.
 * 
 * @author junglas
 */
public class MapDataCollector extends OGNLDataCollectorBase {
	Map<Object, Object> map;

	@SuppressWarnings("unchecked")
	public MapDataCollector(Map map) {
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setValue(String key, Object value) {
		map.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void removeValue(String key) {
		map.remove(key);
	}

}
