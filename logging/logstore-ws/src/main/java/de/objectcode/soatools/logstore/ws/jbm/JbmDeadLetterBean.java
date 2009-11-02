package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Serializable;
import java.util.Date;

import de.objectcode.soatools.logstore.persistent.LogJmsDeadLetter;


public class JbmDeadLetterBean implements Serializable
{
  private static final long serialVersionUID = -7213822913602214650L;

  private final long id;
  private final Date timestamp;

  public JbmDeadLetterBean(LogJmsDeadLetter deadLetter)
  {
    id = deadLetter.getId();
    timestamp = deadLetter.getLogTimestamp();
  }

  public long getId()
  {
    return id;
  }

  public Date getTimestamp()
  {
    return timestamp;
  }

}
