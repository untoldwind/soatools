package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;

public class NameValuePair implements Serializable
{
  private static final long serialVersionUID = 3377168331467359284L;

  final String name;
  final Object value;
  boolean displayState;
  final boolean toggled;

  public NameValuePair(String name, Object value)
  {
    this.name = name;
    this.value = value;
    
    toggled = value != null && value.toString().length() > 1024;
  }

  public String getName()
  {
    return name;
  }

  public Object getValue()
  {
    return value;
  }

  public boolean isDisplayState()
  {
	return displayState;
  }
  
  public void setDisplayState(boolean displayState)
  {
	this.displayState = displayState;
  }
  
  public boolean isToggled()
  {
	  return toggled;
  }
}
