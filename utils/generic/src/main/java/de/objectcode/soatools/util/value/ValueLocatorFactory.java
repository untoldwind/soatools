package de.objectcode.soatools.util.value;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.helpers.ConfigTree;

/**
 * Factory for value locators.
 * 
 * This is the most flexible variant to create value locators from a config
 * tree. Typical use might be like this:
 * 
 * Service configuration:
 * 
 * <pre>
 * &lt;property name="something"&gt;
 *   &lt;something name="key1" object-path="body.value1" default-value="nothing" /&gt;
 *   &lt;something name="key2" expression="${value2} ${value3}" /&gt;
 *   &lt;something name="key3" xpath="/element3/value" /&gt;
 *   &lt;something name="key4" value="static value 4" /&gt;
 * &lt;/property&gt;
 * </pre>
 * 
 * Inside service one might decide to store all values in a map:
 * 
 * Constructor part:
 * 
 * <pre>
 * valueLocators = new HashMap&lt;String, IValueLocator&gt;();
 * ConfigTree[] somethingConfigs = config.getChildren(&quot;something&quot;);
 * for (ConfigTree somethingConfig : somethingConfigs) {
 * 	valueLocators(somethingConfig.getRequiredAttribute(&quot;name&quot;),
 * 			ValueLocatorFactory.INSTANCE.createValueLocator(somethingConfig));
 * }
 * </pre>
 * 
 * Message processing part:
 * 
 * <pre>
 * Map&lt;String, Object&gt; concreteValues = new HashMap&lt;String, Object&gt;();
 * for (Map.Entry&lt;String, IValueLocator&gt; valueLocator : valueLocators.entrySet()) {
 * 	concreteValues.put(valueLocator.getKey(), valueLocator.getValue().getValue(
 * 			message));
 * }
 * </pre>
 * 
 * In this example the <tt>concreteValues</tt> map vill contain the following
 * entries:
 * <ul>
 * <li>key1 will be taken from body part <tt>value1</tt> or has the value
 * "nothing" if there is no such body part</li>
 * <li>key2 will be the result of a MVEL expression</li>
 * <li>key3 will be the result of a XPath expression (whereas its assumed that
 * the default body part contains a xml string)</li>
 * <li>key4 will be a fixed value "static value 4"</li>
 * </ul>
 * 
 * @author junglas
 */
public class ValueLocatorFactory {
	public final static ValueLocatorFactory INSTANCE = new ValueLocatorFactory();

	private ValueLocatorFactory() {
	}

	/**
	 * Create a suitable value locator from a configuration tree element.
	 * 
	 * @param config
	 *            The configuration tree element.
	 * @return A suitable value locator
	 * @throws ConfigurationException
	 *             on error
	 */
	public IValueLocator createValueLocator(ConfigTree config)
			throws ConfigurationException {

		String expression = config.getAttribute("expression");
		String objectPath = config.getAttribute("object-path");
		String contextValue = config.getAttribute("context-value");
		String xpath = config.getAttribute("xpath");
		String defaultValue = config.getAttribute("default-value");
		String value = config.getAttribute("value");

		if ((objectPath == null && expression == null && value == null && xpath == null && contextValue == null )
				|| (objectPath != null && value != null)) {
			throw new ConfigurationException(
					"parameter needs either object-path, expression, xpath, value or context-value");
		}

		if (objectPath != null) {
			return new ObjectPathValueLocator(objectPath, defaultValue);
		} else if (expression != null) {
			return new MVELValueLocator(expression);
		} else if (xpath != null) {
			return new XPathValueLocator(xpath);
		} else if ( contextValue != null ) {
			return new ContextValueLocator(contextValue, defaultValue);
		} else {
			return new StaticValueLocator(value);
		}
	}
}
