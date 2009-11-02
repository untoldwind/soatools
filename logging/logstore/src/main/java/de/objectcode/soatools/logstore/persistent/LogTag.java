package de.objectcode.soatools.logstore.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "MLOG_TAG")
public class LogTag
{
  long id;
  String name;
  String tagValue;
  LogMessage logMessage;

  @Id
  @GeneratedValue(generator = "SEQ_MLOG_MESSAGE_ID")
  @GenericGenerator(name = "SEQ_MLOG_MESSAGE_ID", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MLOG_MESSAGE_ID"))
  @Column(name = "ID")
  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  @Column(name = "NAME")
  @Index(name = "IDX_MLOG_TAG_NAME")
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Column(name = "TAG_VALUE")
  public String getTagValue()
  {
    return tagValue;
  }

  public void setTagValue(String value)
  {
    this.tagValue = value;
  }

  @ManyToOne
  @JoinColumn(name = "MESSAGE_ID", nullable = false)
  public LogMessage getLogMessage()
  {
    return logMessage;
  }

  public void setLogMessage(LogMessage logMessage)
  {
    this.logMessage = logMessage;
  }

}
