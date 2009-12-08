package de.objectcode.soatools.util.smooks.decoders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;

public class UTCDateDecoder implements DataDecoder {
	  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	  public static final String FORMAT_CONFIG_KEY = "format";

	  private SimpleDateFormat decoder = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	  private String format = DEFAULT_DATE_FORMAT;

	  public Object decode(final String data) throws DataDecodeException
	  {
	    if (data == null || data.trim().length() == 0)
	      return "";

	    try {
	      // Must be sync'd - DateFormat is not synchronized.
	      synchronized (this.decoder) {
	    	  decoder.setTimeZone(TimeZone.getTimeZone("UTC"));
	        return this.decoder.parse(data.trim());
	      }
	    } catch (final ParseException e) {
	      throw new DataDecodeException("Error decoding Date data value '" + data + "' with decode format '" + this.format
	          + "'.", e);
	    }
	  }

	  public void setConfiguration(final SmooksResourceConfiguration resourceConfig) throws SmooksConfigurationException
	  {
	    this.format = resourceConfig.getStringParameter(FORMAT_CONFIG_KEY, DEFAULT_DATE_FORMAT);
	    this.decoder = new SimpleDateFormat(this.format.trim());
	  }

}
