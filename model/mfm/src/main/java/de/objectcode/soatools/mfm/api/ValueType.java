package de.objectcode.soatools.mfm.api;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "simple-type-base")
public abstract class ValueType extends Type
{
  @Override
  @Transient
  public boolean isValueType()
  {
    return true;
  }

  public abstract void validateValue(Object value) throws ValidationException;

  public abstract Object normalizeValue(Object value) throws ValidationException;

  public abstract Object valueFromString(String value);
}
