package de.objectcode.soatools.mfm.api.accessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Implementation of the IDataAccessor interface to retrieve the properties of a
 * bean.
 * 
 * All other beans this bean refers to via properties are treated as
 * sub-components. I.e. this implementation can be used to query a bean graph.
 * 
 * @author junglas
 */
public class BeanDataAccessor implements IDataAccessor {
	Object bean;

	public BeanDataAccessor(Object bean) {
		this.bean = bean;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String name) {
		if (name.startsWith("@")) {
			name = name.substring(1);
		}
		try {
			return PropertyUtils.getSimpleProperty(bean, name);
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Object> getArray(String name) {
		Object values = null;
		try {
			values = PropertyUtils.getSimpleProperty(bean, name);
		} catch (Exception e) {
		}

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
		try {
			return DataAccessorFactory.INSTANCE.getAccessor(PropertyUtils
					.getSimpleProperty(bean, name));
		} catch (Exception e) {
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IDataAccessor> getComponentArray(String name) {
		Object values = null;

		try {
			values = PropertyUtils.getSimpleProperty(bean, name);
		} catch (Exception e) {

		}

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

	public String getType() {
		return null;
	}

	public int getVersion() {
		return 0;
	}

}
