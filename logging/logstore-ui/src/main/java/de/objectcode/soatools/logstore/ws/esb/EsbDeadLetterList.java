package de.objectcode.soatools.logstore.ws.esb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.objectcode.soatools.logstore.persistent.EsbMessage;


public class EsbDeadLetterList implements Serializable
{
  private static final long serialVersionUID = -717019075517069164L;

  List<EsbDeadLetterBean> messages;
  EsbDeadLetterDetailBean current;

  public List<EsbDeadLetterBean> getMessages()
  {
    return messages;
  }

  public void setMessages(List<EsbDeadLetterBean> messages)
  {
    this.messages = messages;
  }

  public boolean isHasCurrent()
  {
    return current != null;
  }

  public EsbDeadLetterDetailBean getCurrent()
  {
    return current;
  }

  public void setCurrent(EsbDeadLetterDetailBean current)
  {
    this.current = current;
  }

  void fill(List<?> result)
  {
    messages = new ArrayList<EsbDeadLetterBean>();
    for (Object obj : result) {
      messages.add(new EsbDeadLetterBean((EsbMessage) obj));
    }
  }

}
