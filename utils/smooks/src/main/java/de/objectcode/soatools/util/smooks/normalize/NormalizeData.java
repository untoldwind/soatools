package de.objectcode.soatools.util.smooks.normalize;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.jboss.soa.esb.util.XPathNamespaceContext;
import org.milyn.SmooksException;
import org.milyn.cdr.Parameter;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.ContentDeliveryUnit;
import org.milyn.delivery.dom.DOMElementVisitor;
import org.milyn.javabean.repository.BeanRepository;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

public class NormalizeData implements DOMElementVisitor, ContentDeliveryUnit
{
  String beanId;
  boolean addToList;
  boolean onlyNonEmpty;
  String type;
  int version;
  List<IValueBinding> valueBindings;
  XPath xpath;
  XPathNamespaceContext xPathNamespaceContext;

  public void setConfiguration(SmooksResourceConfiguration config) throws SmooksConfigurationException
  {
    try {
      beanId = config.getStringParameter("beanId", "").trim();
      addToList = config.getBoolParameter("addToList", false);
      onlyNonEmpty = config.getBoolParameter("onlyNonEmpty", true);
      type = config.getStringParameter("type");
      version = Integer.parseInt(config.getStringParameter("version", "0").trim());

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

      valueBindings = new ArrayList<IValueBinding>();

      if (bindingsParam != null) {
        Element bindingsParamElement = bindingsParam.getXml();

        if (bindingsParamElement != null) {
          NodeList bindings = bindingsParamElement.getElementsByTagName("binding");

          for (int i = 0; i < bindings.getLength(); i++) {
            Element bindingElement = (Element) bindings.item(i);

            if (bindingElement.hasAttribute("property")) {
              if (bindingElement.hasAttribute("xpath")) {
                valueBindings.add(new XPathValueBinding(xpath, bindingElement.getAttribute("property"), bindingElement
                    .getAttribute("type"), bindingElement.hasAttribute("default") ? bindingElement
                    .getAttribute("default") : null, bindingElement.getAttribute("xpath")));
              } else if (bindingElement.hasAttribute("value")) {
                valueBindings.add(new StaticValueBinding(bindingElement.getAttribute("property"), bindingElement
                    .getAttribute("type"), bindingElement.getAttribute("value")));
              } else if (bindingElement.hasAttribute("ref")) {
                valueBindings.add(new RefValueBinding(bindingElement.getAttribute("property"), bindingElement
                    .getAttribute("ref")));
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
	  System.out.println(">>> Here!");
  }

  public void visitAfter(Element element, ExecutionContext context) throws SmooksException
  {
	  System.out.println(">>> There!");
	  
    NormalizedData normalizedData = new NormalizedData();

    if (type != null) {
      normalizedData.setTypeInformation(type, version);
    }

    for (IValueBinding valueBinding : valueBindings) {
      try {
        valueBinding.applyValue(normalizedData, element, context);
      } catch (Exception e) {
        throw new SmooksException("Cannot evaluate binding", e);
      }
    }

    if (onlyNonEmpty && normalizedData.isEmpty()) {
      return;
    }

    BeanRepository.getInstance(context).addBean(beanId, normalizedData);
  }

}
