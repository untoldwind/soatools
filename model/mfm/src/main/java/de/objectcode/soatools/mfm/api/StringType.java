package de.objectcode.soatools.mfm.api;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "string")
@XmlType(name = "string")
@Entity
@Table(name = "MFM_STRINGTYPES")
public class StringType extends ValueType
{
  Integer minLength;
  Integer maxLength;
  String patternRegex;
  Pattern pattern;

  @XmlAttribute(name = "min-length")
  @Column(name = "MIN_LENGTH", nullable = true)
  public Integer getMinLength()
  {
    return minLength;
  }

  public void setMinLength(Integer minLength)
  {
    this.minLength = minLength;
  }

  @XmlAttribute(name = "max-length")
  @Column(name = "MAX_LENGTH", nullable = true)
  public Integer getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength)
  {
    this.maxLength = maxLength;
  }

  @XmlAttribute(name = "pattern")
  @Column(name = "PATTERN", nullable = true)
  public String getPatternRegex()
  {
    return patternRegex;
  }

  public void setPatternRegex(String patternRegex)
  {
    this.patternRegex = patternRegex;
  }

  @Override
  public void validateValue(Object value) throws ValidationException
  {
    String str = value.toString();

    if (minLength != null && str.length() < minLength) {
      throw new ValidationException(name + " has minimum length " + minLength + " : " + value);
    }
    if (maxLength != null && str.length() > maxLength) {
      throw new ValidationException(name + " has maximum length " + maxLength + " : " + value);
    }
    if (pattern != null && !pattern.matcher(str).matches()) {
      throw new ValidationException(name + " has regex pattern " + patternRegex + " : " + value);
    }
  }

  @Override
  public Object normalizeValue(Object value) throws ValidationException
  {
    String str = value.toString();

    if (minLength != null && str.length() < minLength) {
      throw new ValidationException(name + " has minimum length " + minLength + " : " + value);
    }
    if (maxLength != null && str.length() > maxLength) {
      throw new ValidationException(name + " has maximum length " + maxLength + " : " + value);
    }
    if (pattern != null && !pattern.matcher(str).matches()) {
      throw new ValidationException(name + " has regex pattern " + patternRegex + " : " + value);
    }

    return str;
  }

  @Override
  public Object valueFromString(String value)
  {
    return value;
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("StringType(");
    buffer.append("name=").append(name);
    buffer.append(", minLength=").append(minLength);
    buffer.append(", maxLength=").append(maxLength);
    buffer.append(", patter=").append(patternRegex);
    buffer.append(")");

    return buffer.toString();
  }

}
