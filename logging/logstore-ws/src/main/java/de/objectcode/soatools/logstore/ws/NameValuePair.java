package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;

public class NameValuePair implements Serializable
{
  private static final long serialVersionUID = 3377168331467359284L;

  final String name;
  final Object value;

  public NameValuePair(String name, Object value)
  {
    this.name = name;
    this.value = value;
  }

  public String getName()
  {
    return name;
  }

  public Object getValue()
  {
    return value;
  }

}
