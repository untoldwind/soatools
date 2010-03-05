package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Serializable;

import javax.management.ObjectName;

public class JbmQueueService implements Serializable
{
  private static final long serialVersionUID = -8779107500438582719L;
  
  private final String queueName;
  private final ObjectName serviceName;

  public JbmQueueService(String queueName, ObjectName serviceName)
  {
    this.queueName = queueName;
    this.serviceName = serviceName;
  }

  public String getQueueName()
  {
    return queueName;
  }

  public ObjectName getServiceName()
  {
    return serviceName;
  }
}
