package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.objectcode.soatools.logstore.persistent.LogJmsDeadLetter;


public class JbmDeadLetterList implements Serializable
{
  private static final long serialVersionUID = -717019075517069164L;

  List<JbmDeadLetterBean> messages;
  JbmDeadLetterDetailBean current;

  public List<JbmDeadLetterBean> getMessages()
  {
    return messages;
  }

  public void setMessages(List<JbmDeadLetterBean> messages)
  {
    this.messages = messages;
  }

  public boolean isHasCurrent()
  {
    return current != null;
  }

  public JbmDeadLetterDetailBean getCurrent()
  {
    return current;
  }

  public void setCurrent(JbmDeadLetterDetailBean current)
  {
    this.current = current;
  }

  void fill(List<?> result)
  {
    messages = new ArrayList<JbmDeadLetterBean>();
    for (Object obj : result) {
      messages.add(new JbmDeadLetterBean((LogJmsDeadLetter) obj));
    }
  }

}
