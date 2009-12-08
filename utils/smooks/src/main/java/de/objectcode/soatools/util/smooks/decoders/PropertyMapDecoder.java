package de.objectcode.soatools.util.smooks.decoders;

import java.util.Properties;

import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.milyn.resource.URIResourceLocator;

public class PropertyMapDecoder implements DataDecoder
{
  public static final String PROPERTY_CONFIG_KEY = "propertiesResource";

  Properties properties;

  public void setConfiguration(SmooksResourceConfiguration resourceConfig) throws SmooksConfigurationException
  {
    String propertiesResource = resourceConfig.getStringParameter(PROPERTY_CONFIG_KEY);

    properties = new Properties();
    try {
      properties.load(new URIResourceLocator().getResource(propertiesResource));
    } catch (Exception e) {
      throw new SmooksConfigurationException(e);
    }
  }

  public Object decode(String data) throws DataDecodeException
  {
    if (data == null) {
      return null;
    }

    String value = properties.getProperty(data.toString());

    return value;
  }

}
