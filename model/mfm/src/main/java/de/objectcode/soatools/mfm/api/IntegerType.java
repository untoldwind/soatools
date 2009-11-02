package de.objectcode.soatools.mfm.api;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "integer")
@XmlType(name = "integer")
@Entity
@Table(name = "MFM_INTEGERTYPES")
public class IntegerType extends ValueType
{
  Integer minValue;
  Integer maxValue;

  @XmlAttribute(name = "min-value")
  @Column(name = "MIN_VALUE", nullable = true)
  public Integer getMinValue()
  {
    return minValue;
  }

  public void setMinValue(Integer minValue)
  {
    this.minValue = minValue;
  }

  @XmlAttribute(name = "max-value")
  @Column(name = "MAX_VALUE", nullable = true)
  public Integer getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue(Integer maxValue)
  {
    this.maxValue = maxValue;
  }

  @Override
  public void validateValue(Object value) throws ValidationException
  {
    int i;

    if (value instanceof Number) {
      i = ((Number) value).intValue();
    } else {
      try {
        i = Integer.parseInt(value.toString());
      } catch (NumberFormatException e) {
        throw new ValidationException(value + " is not an integer value");
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
    int i;

    if (value instanceof Number) {
      i = ((Number) value).intValue();
    } else {
      try {
        i = Integer.parseInt(value.toString());
      } catch (NumberFormatException e) {
        throw new ValidationException(value + " is not an integer value");
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
    return new Integer(value);
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("IntegerType(");
    buffer.append("name=").append(name);
    buffer.append(", minValue=").append(minValue);
    buffer.append(", maxValue=").append(maxValue);
    buffer.append(")");

    return buffer.toString();
  }

}
