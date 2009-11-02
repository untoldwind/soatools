package de.objectcode.soatools.mfm.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "long")
@XmlType(name = "long")
@Entity
@Table(name = "MFM_LONGTYPES")
public class LongType extends ValueType
{
  Long minValue;
  Long maxValue;

  @XmlAttribute(name = "min-value")
  @Column(name = "MIN_VALUE", nullable = true)
  public Long getMinValue()
  {
    return minValue;
  }

  public void setMinValue(Long minValue)
  {
    this.minValue = minValue;
  }

  @XmlAttribute(name = "max-value")
  @Column(name = "MAX_VALUE", nullable = true)
  public Long getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue(Long maxValue)
  {
    this.maxValue = maxValue;
  }

  @Override
  public void validateValue(Object value) throws ValidationException
  {
    long i;

    if (value instanceof Number) {
      i = ((Number) value).longValue();
    } else {
      try {
        i = Long.parseLong(value.toString());
      } catch (NumberFormatException e) {
        throw new ValidationException(value + " is not an long value");
      }
    }

    if (minValue != null && i < minValue) {
      throw new ValidationException(name + " has minimum value " + minValue + " : " + minValue);
    }
    if (maxValue != null && i > maxValue) {
      throw new ValidationException(name + " has maximum value " + maxValue + " : " + maxValue);
    }
  }

  @Override
  public Object normalizeValue(Object value) throws ValidationException
  {
    long i;

    if (value instanceof Number) {
      i = ((Number) value).longValue();
    } else {
      try {
        i = Long.parseLong(value.toString());
      } catch (NumberFormatException e) {
        throw new ValidationException(value + " is not an long value");
      }
    }

    if (minValue != null && i < minValue) {
      throw new ValidationException(name + " has minimum value " + minValue + " : " + minValue);
    }
    if (maxValue != null && i > maxValue) {
      throw new ValidationException(name + " has maximum value " + maxValue + " : " + maxValue);
    }

    return i;
  }

  @Override
  public Object valueFromString(String value)
  {
    return new Long(value);
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("LongType(");
    buffer.append("name=").append(name);
    buffer.append(", minValue=").append(minValue);
    buffer.append(", maxValue=").append(maxValue);
    buffer.append(")");

    return buffer.toString();
  }

}
