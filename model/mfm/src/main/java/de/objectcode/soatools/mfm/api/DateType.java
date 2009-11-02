package de.objectcode.soatools.mfm.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "date")
@XmlType(name = "date")
@Entity
@Table(name = "MFM_DATETYPES")
public class DateType extends ValueType
{
  private final static SimpleDateFormat FORMAT_UNIVERSAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final static SimpleDateFormat FORMAT_GERMAN = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  private final static SimpleDateFormat[] VALID_DATE_FORMATS = new SimpleDateFormat[] {FORMAT_UNIVERSAL, FORMAT_GERMAN};   

  @Override
  public void validateValue(Object value) throws ValidationException
  {
    if (value instanceof Date) {
      return;
    } else if (value instanceof Long) {
      new Date(((Long) value).longValue());
    } else {
      transformDate(value);
    }
  }

  private Date transformDate(Object value) throws ValidationException
  {
    Date retVal = null;
    
    
    for (int i = 0; i < VALID_DATE_FORMATS.length; i++) {
      try {
        retVal = VALID_DATE_FORMATS[i].parse(value.toString());
      } catch (ParseException e) {
      }
      
      if(retVal != null) break;
    }
    
    if(retVal == null)
      throw new ValidationException(value + " is not an date value");
    
    return retVal;
  }

  @Override
  public Object normalizeValue(Object value) throws ValidationException
  {
    Date date;

    if (value instanceof Date) {
      date = (Date) value;
    } else if (value instanceof Long) {
      date = new Date(((Long) value).longValue());
    } else {
      date = transformDate(value);
    }

    return date;
  }

  @Override
  public Object valueFromString(String value)
  {
    try {
      return transformDate(value);
    } catch (ValidationException e) {
      return null;
    }
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("DateType(");
    buffer.append("name=").append(name);
    buffer.append(")");

    return buffer.toString();
  }
}
