package de.objectcode.soatools.util.smooks.delivery;

import java.util.List;

import org.milyn.SmooksException;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.container.ExecutionContext;
import org.milyn.delivery.ContentDeliveryUnit;
import org.milyn.delivery.dom.DOMElementVisitor;
import org.milyn.javabean.BeanAccessor;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.milyn.xml.DomUtils;
import org.w3c.dom.Element;

public class ObjectValue implements DOMElementVisitor, ContentDeliveryUnit
{
  String beanId;
  String attributeName;
  String typeAlias;
  DataDecoder decoder;

  public void setConfiguration(SmooksResourceConfiguration config) throws SmooksConfigurationException
  {
    beanId = config.getStringParameter("beanId", "").trim();
    attributeName = config.getStringParameter("attributeName", "").trim();
    typeAlias = config.getStringParameter("type", "String").trim();

    if (beanId.equals("")) {
      throw new SmooksConfigurationException("Invalid Smooks bean configuration.  'beanId' unspecified.");
    }
    if (attributeName.equals("")) {
      attributeName = null;
    }
  }

  public void visitBefore(Element element, ExecutionContext context) throws SmooksException
  {
    String dataString;

    if (attributeName != null) {
      dataString = element.getAttribute(attributeName);
    } else {
      dataString = DomUtils.getAllText(element, false);
    }

    if (decoder == null) {
      decoder = getDecoder(context);
    }

    BeanAccessor.addBean(beanId, decoder.decode(dataString), context, false);
  }

  public void visitAfter(Element element, ExecutionContext context) throws SmooksException
  {
  }

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
