package de.objectcode.soatools.logstore.persistent;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "MLOG_JMS_DLQ")
public class LogJmsDeadLetter
{
  private long id;
  private Date logTimestamp;
  private Clob content;

  @Id
  @GeneratedValue(generator = "SEQ_MLOG_JMS_DLQ")
  @GenericGenerator(name = "SEQ_MLOG_JMS_DLQ", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_MLOG_JMS_DLQ"))
  @Column(name = "ID")
  public long getId()
  {
    return id;
  }

  public void setId(long id)
  {
    this.id = id;
  }

  @Column(name = "LOG_TIMESTAMP")
  public Date getLogTimestamp()
  {
    return logTimestamp;
  }

  public void setLogTimestamp(Date logTimestamp)
  {
    this.logTimestamp = logTimestamp;
  }

  @Column(name = "CONTENT")
  public Clob getContent()
  {
    return content;
  }

  public void setContent(Clob content)
  {
    this.content = content;
  }

}
