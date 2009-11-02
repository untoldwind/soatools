package de.objectcode.soatools.mfm.api.collector;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Implementation of the IDataCollector to serialize hierarchical data into XML.
 * 
 * @author junglas
 */
public class Dom4jDataCollector implements IDataCollector {
	Element element;

	public Dom4jDataCollector(Element element) {
		this.element = element;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataCollector addComponent(String name) {
		Element componentElement = element.addElement(name);

		return new Dom4jDataCollector(componentElement);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addToArray(String name, int index, Object value) {
		Element valueElement = element.addElement(name);

		if (value != null) {
			valueElement.setText(value.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataCollector addToComponentArray(String name, int index) {
		Element componentElement = element.addElement(name);

		return new Dom4jDataCollector(componentElement);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addValue(String name, Object value) {
		if (name.startsWith("@")) {
			if (value != null) {
				element.addAttribute(name.substring(1), value.toString());
			}
		} else {
			Element valueElement = element.addElement(name);

			if (value != null) {
				valueElement.setText(value.toString());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePart(String name) {
		if (name.startsWith("@")) {
			Attribute attribute = element.attribute(name.substring(1));
			if (attribute != null) {
				element.remove(attribute);
			}
			List<?> valueElements = element.elements(name.substring(1));
			for (Object valueElement : valueElements) {
				element.remove((Element) valueElement);
			}
		} else {
			List<?> valueElements = element.elements(name);
			for (Object valueElement : valueElements) {
				element.remove((Element) valueElement);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTypeInformation(String type, int version) {
		element.addAttribute("type", type);
		element.addAttribute("version", String.valueOf(version));
	}
}
