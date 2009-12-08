package de.objectcode.soatools.util.smooks.decoders;

import java.util.StringTokenizer;

import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;

public class StringArrayDecoder implements DataDecoder
{
  public static final String DELIM_CONFIG_KEY = "delim";

  private String delim;

  public Object decode(String data) throws DataDecodeException
  {
    StringTokenizer t = new StringTokenizer(data, delim);
    String result[] = new String[t.countTokens()];

    for (int i = 0; t.hasMoreTokens(); i++) {
      result[i] = t.nextToken();
    }

    return result;
  }

  public void setConfiguration(SmooksResourceConfiguration resourceConfig) throws SmooksConfigurationException
  {
    delim = resourceConfig.getStringParameter(DELIM_CONFIG_KEY, ",;");
  }

}
