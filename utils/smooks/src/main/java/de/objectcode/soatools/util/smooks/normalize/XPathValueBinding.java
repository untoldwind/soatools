package de.objectcode.soatools.util.smooks.normalize;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Element;

class XPathValueBinding extends AbstractValueBinding
{
  String defaultValue;
  XPathExpression expression;

  public XPathValueBinding(XPath xpath, String property, String typeAlias, String defaultValue, String xpathExpression)
      throws Exception
  {
    super(property, typeAlias);

    this.defaultValue = defaultValue;
    expression = xpath.compile(xpathExpression);
  }

  @Override
  String getValue(Element element) throws Exception
  {
    String result = (String) expression.evaluate(element, XPathConstants.STRING);

    if (result == null) {
      return defaultValue;
    }

    return result;
  }
}
