package de.objectcode.soatools.util.smooks.decoders;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;

public class OrderDecoder implements DataDecoder
{
  /** Logger for this class. */
  private static final Log LOGGER = LogFactory.getLog(OrderDecoder.class);

  private int digits = 7;

  final String DIGITS_CONFIG_KEY = "digits";
  
  final private Random random;

  public OrderDecoder() throws UnknownHostException
  {
    long initLong = this.hashCode() + System.currentTimeMillis() + InetAddress.getLocalHost().getCanonicalHostName().hashCode();
    random = new Random(initLong);
  }

  public Object decode(final String data) throws DataDecodeException
  {
    Object retVal = null;

    try {
      final String currentDate = Long.valueOf(random.nextLong()).toString();
      final String dataAsTransformedString = (data != null && data.trim().length() != 0) ? Integer.valueOf(data)
          .toString() : null;

      if (dataAsTransformedString != null && dataAsTransformedString.length() < this.digits) {

        retVal = currentDate.substring(currentDate.length() - this.digits + dataAsTransformedString.length(),
            currentDate.length())
            + dataAsTransformedString;
      } else if (dataAsTransformedString != null && dataAsTransformedString.length() >= this.digits) {
        retVal = dataAsTransformedString;
      } else {
        retVal = currentDate.substring(currentDate.length() - this.digits, currentDate.length());
      }
    } catch (final NumberFormatException ex) {
      throw new DataDecodeException(ex.getMessage());
    }

    LOGGER.debug("decode: @return retVal = " + retVal);
    return retVal;
  }

  public void setConfiguration(final SmooksResourceConfiguration resourceConfig) throws SmooksConfigurationException
  {
    final String digitsAsString = resourceConfig.getStringParameter(this.DIGITS_CONFIG_KEY, "7");
    this.digits = Integer.valueOf(digitsAsString);
  }
}
