package de.objectcode.soatools.util.smooks.delivery;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jboss.soa.esb.util.XPathNamespaceContext;
import org.milyn.SmooksException;
import org.milyn.cdr.Parameter;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.ContentDeliveryUnit;
import org.milyn.delivery.dom.DOMElementVisitor;
import org.milyn.javabean.BeanAccessor;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.milyn.javabean.repository.BeanRepository;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

public class NormalizeData implements DOMElementVisitor, ContentDeliveryUnit
{
  String beanId;
  List<AbstractValueBinding> valueBindings;
  XPath xpath;
  XPathNamespaceContext xPathNamespaceContext;

  public void setConfiguration(SmooksResourceConfiguration config) throws SmooksConfigurationException
  {
    try {
      beanId = config.getStringParameter("beanId", "").trim();

      XPathFactory factory = XPathFactory.newInstance();
      xPathNamespaceContext = new XPathNamespaceContext();
      xpath = factory.newXPath();
      xpath.setNamespaceContext(xPathNamespaceContext);

      Parameter namespacesParam = config.getParameter("namespaces");

      if (namespacesParam != null) {
        Element namespacesParamElement = namespacesParam.getXml();

        if (namespacesParamElement != null) {
          NodeList namespaces = namespacesParamElement.getElementsByTagName("namespace");

          for (int i = 0; i < namespaces.getLength(); i++) {
            Element namespaceElement = (Element) namespaces.item(i);

            if (namespaceElement.hasAttribute("prefix") && namespaceElement.hasAttribute("uri")) {
              xPathNamespaceContext.setMapping(namespaceElement.getAttribute("prefix"), namespaceElement
                  .getAttribute("uri"));
            }
          }
        }
      }

      Parameter bindingsParam = config.getParameter("bindings");

      valueBindings = new ArrayList<AbstractValueBinding>();

      if (bindingsParam != null) {
        Element bindingsParamElement = bindingsParam.getXml();

        if (bindingsParamElement != null) {
          NodeList bindings = bindingsParamElement.getElementsByTagName("binding");

          for (int i = 0; i < bindings.getLength(); i++) {
            Element bindingElement = (Element) bindings.item(i);

            if (bindingElement.hasAttribute("property") && bindingElement.hasAttribute("type")) {
              if (bindingElement.hasAttribute("xpath")) {
                valueBindings
                    .add(new XPathValueBinding(bindingElement.getAttribute("property"), bindingElement
                        .getAttribute("type"), bindingElement.getAttribute("default"), bindingElement
                        .getAttribute("xpath")));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      throw new SmooksConfigurationException(e);
    }
  }

  public void visitBefore(Element element, ExecutionContext context) throws SmooksException
  {
  }

  public void visitAfter(Element element, ExecutionContext context) throws SmooksException
  {
    NormalizedData normalizedData = new NormalizedData();
    BeanRepository.getInstance(context).addBean(beanId, normalizedData);

    for (AbstractValueBinding valueBinding : valueBindings) {
      try {
        valueBinding.applyValue(normalizedData, element, context);
      } catch (Exception e) {
        e.printStackTrace();
        throw new SmooksException("Cannot evaluate binding", e);
      }
    }
  }

  abstract class AbstractValueBinding
  {
    String[] propertyPath;
    DataDecoder decoder;
    String typeAlias;
    String defaultValue;

    AbstractValueBinding(String property, String typeAlias, String defaultValue)
    {
      this.propertyPath = property.split("\\.");
      this.typeAlias = typeAlias;
      this.defaultValue = defaultValue;
    }

    void applyValue(NormalizedData normalizedData, Element element, ExecutionContext context) throws Exception
    {
      if (propertyPath.length > 1) {
        for (int i = 0; i < propertyPath.length - 1; i++) {
          NormalizedData component = (NormalizedData) normalizedData.getComponent(propertyPath[i]);

          if (component == null) {
            component = (NormalizedData) normalizedData.addComponent(propertyPath[i]);
          }

          normalizedData = component;
        }
      }

      if (decoder == null) {
        decoder = getDecoder(context);
      }
      normalizedData.addValue(propertyPath[propertyPath.length - 1], decoder.decode(getValue(element)));
    }

    abstract String getValue(Element element) throws Exception;

    private DataDecoder getDecoder(ExecutionContext executionContext) throws DataDecodeException
    {
      List<?> decoders = executionContext.getDeliveryConfig().getObjects("decoder:" + typeAlias);

      if (decoders == null || decoders.isEmpty()) {
        decoder = DataDecoder.Factory.create(typeAlias);
      } else if (!(decoders.get(0) instanceof DataDecoder)) {
        throw new DataDecodeException("Configured decoder '" + typeAlias + ":" + decoders.get(0).getClass().getName()
            + "' is not an instance of " + DataDecoder.class.getName());
      } else {
        decoder = (DataDecoder) decoders.get(0);
      }

      return decoder;
    }
  }

  class XPathValueBinding extends AbstractValueBinding
  {
    XPathExpression expression;

    public XPathValueBinding(String property, String typeAlias, String defaultValue, String xpathExpression)
        throws Exception
    {
      super(property, typeAlias, defaultValue);

      expression = xpath.compile(xpathExpression);
    }

    @Override
    String getValue(Element element) throws Exception
    {
      return (String) expression.evaluate(element, XPathConstants.STRING);
    }
  }

}
