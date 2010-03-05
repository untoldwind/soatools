package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Serializable;
import java.util.Date;

public class JbmQueueStat implements Serializable
{
  private static final long serialVersionUID = 7664668388323624170L;
  
  private final JbmQueueService queueService;
  private final int count;
  private final int countDelta;
  private final int messageCount;
  private final int messageCountDelta;
  private final Date lastUpdate;

  public JbmQueueStat(JbmQueueService queueService, int count, int countDelta, int messageCount, int messageCountDelta,
      long lastUpdate)
  {
    this.queueService = queueService;
    this.count = count;
    this.countDelta = countDelta;
    this.messageCount = messageCount;
    this.messageCountDelta = messageCountDelta;
    this.lastUpdate = new Date( lastUpdate);
  }

  public JbmQueueService getQueueService()
  {
    return queueService;
  }

  public int getCount()
  {
    return count;
  }

  public int getCountDelta()
  {
    return countDelta;
  }

  public int getMessageCount()
  {
    return messageCount;
  }

  public int getMessageCountDelta()
  {
    return messageCountDelta;
  }

  public Date getLastUpdate()
  {
    return lastUpdate;
  }

}
