package de.objectcode.soatools.mfm.api.collector;

/**
 * Collector of arbitrary hierarchical data.
 * 
 * Usually this is a data serializer.
 * 
 * @author junglas
 */
public interface IDataCollector {
	/**
	 * Add a value at the current point in the hierarchy.
	 * 
	 * @param name
	 *            Name of the value
	 * @param value
	 *            The value
	 */
	void addValue(String name, Object value);

	/**
	 * Add an array value at the current point in the hierarchy.
	 * 
	 * @param name
	 *            Name of the value
	 * @param index
	 *            Index of the value
	 * @param value
	 *            The value
	 */
	void addToArray(String name, int index, Object value);

	/**
	 * Add a sub-component at the current point in the hierarchy.
	 * 
	 * I.e. move one step down the hierarchy.
	 * 
	 * @param name
	 *            The name of the component
	 * @return Data collector of the sub-component
	 */
	IDataCollector addComponent(String name);

	/**
	 * Add an array of sub-component at the current point in the hierarchy.
	 * 
	 * @param name
	 *            The of the component
	 * @param index
	 *            The index of the component
	 * @return Data collector of the sub-component
	 */
	IDataCollector addToComponentArray(String name, int index);

	/**
	 * Remove a part from the current point in the hierarchy.
	 * 
	 * This can either be a value or a sub-component.
	 * 
	 * @param name
	 *            Name of the part to remove
	 */
	void removePart(String name);

	/**
	 * Store the message type information of the current point in the hierarchy.
	 * 
	 * @param type
	 *            Message type name
	 * @param version
	 *            Message type version
	 */
	void setTypeInformation(String type, int version);
}
