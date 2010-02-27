package de.objectcode.soatools.logstore.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.objectcode.soatools.logstore.persistent.LogMessage;


public class LogMessageList implements Serializable
{
  private static final long serialVersionUID = -717019075517069164L;

  List<LogMessageBean> logMessages;
  LogMessageDetailBean current;

  public List<LogMessageBean> getLogMessages()
  {
    return logMessages;
  }

  public void setLogMessages(List<LogMessageBean> logMessages)
  {
    this.logMessages = logMessages;
  }

  public boolean isHasCurrent()
  {
    return current != null;
  }

  public LogMessageDetailBean getCurrent()
  {
    return current;
  }

  public void setCurrent(LogMessageDetailBean current)
  {
    this.current = current;
  }

  void fill(List<?> result)
  {
    logMessages = new ArrayList<LogMessageBean>();
    for (Object obj : result) {
      logMessages.add(new LogMessageBean((LogMessage) obj));
    }
  }
}
