package de.objectcode.soatools.msmq.ws;

import java.io.Serializable;
import java.util.Date;

public class MsmqInMessage implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String id;
  private String label;
  private String priority;
  private String messageType;
  private Date sentTime;
  private Date arrivedTime;
  private byte[] body;
  private byte[] senderId;
  private String correlationId;

  public Date getArrivedTime()
  {
    return arrivedTime;
  }

  public byte[] getBody()
  {
    return body;
  }

  public String getCorrelationId()
  {
    return correlationId;
  }

  public String getId()
  {
    return id;
  }

  public String getLabel()
  {
    return label;
  }

  public String getMessageType()
  {
    return messageType;
  }

  public String getPriority()
  {
    return priority;
  }

  public byte[] getSenderId()
  {
    return senderId;
  }

  public Date getSentTime()
  {
    return sentTime;
  }

  public void setArrivedTime(Date arrivedTime)
  {
    this.arrivedTime = arrivedTime;
  }

  public void setBody(byte[] body)
  {
    this.body = body;
  }

  public void setCorrelationId(String correlationId)
  {
    this.correlationId = correlationId;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public void setMessageType(String messageType)
  {
    this.messageType = messageType;
  }

  public void setPriority(String priority)
  {
    this.priority = priority;
  }

  public void setSenderId(byte[] senderId)
  {
    this.senderId = senderId;
  }

  public void setSentTime(Date sentTime)
  {
    this.sentTime = sentTime;
  }
}
