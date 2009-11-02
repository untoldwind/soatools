package de.objectcode.soatools.mfm.api.accessor;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * Implementation of the IDataAccessor interface to retrieve data of an XML
 * document.
 * 
 * This XML document is supposed to conform to certain rule (e.g. has been
 * serialized from another normalized message). Arbitrary XML would not do,
 * thats what xslt or smooks was made for.
 * 
 * @author junglas
 */
public class Dom4jDataAccessor implements IDataAccessor {
	Element element;

	public Dom4jDataAccessor(Element element) {
		this.element = element;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(String name) {
		if (name.startsWith("@")) {
			Attribute attribute = element.attribute(name.substring(1));
			if (attribute != null) {
				return attribute.getValue();
			}
			Element valueElement = element.element(name.substring(1));
			if (valueElement != null) {
				return valueElement.getTextTrim();
			}
		} else {
			Element valueElement = element.element(name);

			if (valueElement != null) {
				return valueElement.getTextTrim();
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Object> getArray(String name) {
		Element arrayElement = element.element(name);

		if (arrayElement == null) {
			return null;
		}

		List<Object> result = new ArrayList<Object>();
		List<?> valueElements = arrayElement.elements();

		for (Object valueElement : valueElements) {
			result.add(((Element) valueElement).getTextTrim());
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public IDataAccessor getComponent(String name) {
		Element componentElement = element.element(name);

		if (componentElement != null) {
			return new Dom4jDataAccessor(componentElement);
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IDataAccessor> getComponentArray(String name) {
		Element arrayElement = element.element(name);

		if (arrayElement == null) {
			return null;
		}

		List<IDataAccessor> result = new ArrayList<IDataAccessor>();

		List<?> valueElements = arrayElement.elements();

		for (Object valueElement : valueElements) {
			result.add(new Dom4jDataAccessor((Element) valueElement));
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getType() {
		if (element.attribute("type") != null) {
			return element.attribute("type").getValue();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getVersion() {
		if (element.attribute("version") != null) {
			return Integer.parseInt(element.attribute("version").getValue());
		}
		return 0;
	}

}
