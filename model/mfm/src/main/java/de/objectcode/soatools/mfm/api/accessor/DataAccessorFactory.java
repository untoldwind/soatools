package de.objectcode.soatools.mfm.api.accessor;

import java.io.StringReader;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * Factory to find a suitable data accessor.
 * 
 * @author junglas
 */
public class DataAccessorFactory {
	public final static DataAccessorFactory INSTANCE = new DataAccessorFactory();

	/**
	 * Get a suitable data accessor for an arbitrary object.
	 * 
	 * In case of a Map the MapDataAccessor will be used, in case of an XML
	 * string the Dom4jDataAccessor will be used, in any other case the object
	 * will be treated as java bean.
	 * 
	 * @param value
	 *            The object
	 * @return data accessor to access <tt>value</tt>
	 */
	public IDataAccessor getAccessor(Object value) {
		if (value instanceof Map<?, ?>) {
			return new MapDataAccessor((Map<?, ?>) value);
		} else if (value instanceof String
				&& ((String) value).startsWith("<?xml")) {
			try {
				SAXReader reader = new SAXReader();
				Document document = reader.read(new StringReader(value
						.toString()));

				return new Dom4jDataAccessor(document.getRootElement());
			} catch (DocumentException e) {
				return null;
			}
		} else {
			return new BeanDataAccessor(value);
		}
	}
}
