package de.objectcode.soatools.util.smooks.decoders;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;

/**
 * Boolean decoder.
 */
public class BooleanDecoder implements DataDecoder
{
  private static final String FALSE_PATTERN = "falsePattern";
  private static final String IGNORE_CASE = "ignoreCase";
  private static final String TRUE_PATTERN = "truePattern";

  private final Boolean defaultValue = null;
  private boolean ignoreCase = true;
  private String[] truePatterns, falsePatterns;

  private boolean checkData(final String data, final String[] comparePatterns)
  {
    boolean retVal = false;

    for (int i = 0; i < comparePatterns.length && !retVal; i++) {
      final String comparePattern = comparePatterns[i];

      if (this.ignoreCase) {
        retVal = comparePattern.equalsIgnoreCase(data);
      } else {
        retVal = comparePattern.equals(data);
      }
    }

    return retVal;
  }

  private String[] convert(final String truePatternString)
  {
    String[] retVal = null;
    final List<String> stringList = new ArrayList<String>();

    final StringTokenizer stringTokenizer = new StringTokenizer(truePatternString, ",");

    while (stringTokenizer.hasMoreTokens()) {
      final String nextToken = stringTokenizer.nextToken();
      stringList.add(nextToken.trim());
    }

    retVal = stringList.toArray(new String[stringList.size()]);

    return retVal;
  }

  public Object decode(final String data) throws DataDecodeException
  {
    Object retVal = null;

    if (data == null || data.trim().length() == 0) {
      retVal = "";
    } else if (checkData(data, this.truePatterns)) {
      retVal = Boolean.TRUE;
    } else if (checkData(data, this.falsePatterns)) {
      retVal = Boolean.FALSE;
    } else {
      throw new DataDecodeException("Failed to decode Boolean value '" + data + "'.");
    }

    return retVal;
  }

  public void setConfiguration(final SmooksResourceConfiguration resourceConfig) throws SmooksConfigurationException
  {
    final String truePatternString = resourceConfig.getStringParameter(TRUE_PATTERN, "true");
    final String falsePatternString = resourceConfig.getStringParameter(FALSE_PATTERN, "false");

    this.ignoreCase = resourceConfig.getBoolParameter(IGNORE_CASE, true);

    this.truePatterns = convert(truePatternString);
    this.falsePatterns = convert(falsePatternString);
  }
}
