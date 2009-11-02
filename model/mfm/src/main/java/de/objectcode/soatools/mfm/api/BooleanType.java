package de.objectcode.soatools.mfm.api;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "boolean")
@XmlType(name = "boolean")
@Entity
@Table(name = "MFM_BOOLEANTYPES")
public class BooleanType extends ValueType
{

  @Override
  public void validateValue(Object value) throws ValidationException
  {
    if (value instanceof Boolean) {
      return;
    } else {
      if (!"true".equalsIgnoreCase(value.toString()) && !"false".equalsIgnoreCase(value.toString())) {
        throw new ValidationException(value + " is not a boolean value");
      }
    }
  }

  @Override
  public Object normalizeValue(Object value) throws ValidationException
  {
    if (value instanceof Boolean) {
      return value;
    } else {
      if (!"true".equalsIgnoreCase(value.toString()) && !"false".equalsIgnoreCase(value.toString())) {
        throw new ValidationException(value + " is not a boolean value");
      }

      return "true".equalsIgnoreCase(value.toString());
    }
  }

  @Override
  public Object valueFromString(String value)
  {
    return new Boolean(value);
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("BooleanType(");
    buffer.append("name=").append(name);
    buffer.append(")");

    return buffer.toString();
  }
}