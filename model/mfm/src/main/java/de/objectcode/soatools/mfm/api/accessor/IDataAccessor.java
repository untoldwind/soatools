package de.objectcode.soatools.mfm.api.accessor;

import java.util.List;

/**
 * Data accessor interface.
 * 
 * A data accessor is used to query an arbitrary set of the hierarchical data.
 * 
 * Note that all operations of this interface are supposed to work on a certain
 * level within the hierarchy. I.e. all <tt>name</tt> parameters do NOT accept a
 * dot-notation.
 * 
 * @author junglas
 */
public interface IDataAccessor {
	/**
	 * Get a single value by its name.
	 * 
	 * @param name
	 *            The name of the value
	 * @return value of <tt>name</tt> or <tt>null</tt>
	 */
	Object getValue(String name);

	/**
	 * Get a multi-value by its name.
	 * 
	 * @param name
	 *            The name of the value
	 * @return list of all values of <tt>name</tt> or <tt>null</tt>
	 */
	List<Object> getArray(String name);

	/**
	 * Get the data accessor of a sub-component.
	 * 
	 * I.e. move one level down the hierarchy.
	 * 
	 * @param name
	 *            The name of the sub-component
	 * 
	 * @return data accessor of <tt>name</tt> or <tt>null</tt>
	 */
	IDataAccessor getComponent(String name);

	/**
	 * Get the data accessor of multiple sub-components.
	 * 
	 * @param name
	 *            The name of the sub-components
	 * @return list of data accessors of <tt>name</tt> or <tt>null</tt>
	 */
	List<IDataAccessor> getComponentArray(String name);

	/**
	 * The data may contain information about the message type to use.
	 * 
	 * This is an optional information
	 * 
	 * @return The message type name or <tt>null</tt>
	 */
	String getType();

	/**
	 * The data may contain information about the message type version to use.
	 * 
	 * This is an optional information
	 * 
	 * @return The message type version or <tt>0</tt>
	 */
	int getVersion();

}
