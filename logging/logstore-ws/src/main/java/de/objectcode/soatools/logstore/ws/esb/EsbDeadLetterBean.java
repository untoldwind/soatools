package de.objectcode.soatools.logstore.ws.esb;

import java.io.Serializable;

import de.objectcode.soatools.logstore.persistent.EsbMessage;


public class EsbDeadLetterBean implements Serializable
{
  private static final long serialVersionUID = -7213822913602214650L;

  private final String id;

  public EsbDeadLetterBean(EsbMessage deadLetter)
  {
    id = deadLetter.getUuid();
  }

  public String getId()
  {
    return id;
  }
}