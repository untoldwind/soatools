package de.objectcode.soatools.logstore.persistent;

import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE")
public class EsbMessage
{
  String uuid;
  String type;
  Clob message;
  String delivered;
  String classification;

  @Id
  @Column(name = "uuid", length = 128, nullable = false)
  public String getUuid()
  {
    return uuid;
  }

  public void setUuid(String uuid)
  {
    this.uuid = uuid;
  }

  @Column(name = "type", length = 128, nullable = false)
  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  @Column(name = "message", nullable = false)
  public Clob getMessage()
  {
    return message;
  }

  public void setMessage(Clob message)
  {
    this.message = message;
  }

  @Column(name = "delivered", length = 10, nullable = false)
  public String getDelivered()
  {
    return delivered;
  }

  public void setDelivered(String delivered)
  {
    this.delivered = delivered;
  }

  @Column(name = "classification", length = 10, nullable = true)
  public String getClassification()
  {
    return classification;
  }

  public void setClassification(String classification)
  {
    this.classification = classification;
  }
}
