package de.objectcode.soatools.util.value;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.util.XPathUtil;
import org.w3c.dom.Document;

/**
 * Assume that the default body part of a message is an xml string and evaluate
 * a XPath expression.
 * 
 * So far this locator can not be used to set values in the message (i.e.
 * manipulate the xml)
 * 
 * @author junglas
 */
public class XPathValueLocator implements IValueLocator {
	final XPathExpression expression;

	public XPathValueLocator(String xpathExpression)
			throws ConfigurationException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		try {
			expression = xpath.compile(xpathExpression);
		} catch (XPathExpressionException e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(Message message) throws ActionProcessingException {
		if (message.getBody().get() != null
				&& message.getBody().get() instanceof String) {
			try {
				Document document = XPathUtil.getDocument((String) message
						.getBody().get());

				return expression.evaluate(document.getDocumentElement());
			} catch (Exception e) {
				throw new ActionProcessingException(e);
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(Message message, Object value)
			throws ActionProcessingException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "XPathValueLocator(" + expression + ")";
	}
}
