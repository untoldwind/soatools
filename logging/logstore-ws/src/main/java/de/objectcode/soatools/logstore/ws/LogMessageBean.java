package de.objectcode.soatools.logstore.ws;

import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jboss.internal.soa.esb.util.Encoding;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.util.Util;

import de.objectcode.soatools.logstore.persistent.LogMessage;
import de.objectcode.soatools.logstore.persistent.LogTag;


public class LogMessageBean implements Serializable
{
  private static final long serialVersionUID = -6079863056193545028L;

  final long id;
  final String state;
  final String serviceCategory;
  final String serviceName;
  final Date logEnterTimestamp;
  final Date logLeaveTimestamp;
  final String messageId;
  final String correlationId;
  final Long jbpmProcessInstanceId;
  final Long jbpmTokenId;
  final Long jbpmNodeId;
  final String faultReason;
  final String faultCause;
  final Map<String, NameValuePair> tags;
  final Map<String, NameValuePair> properties;
  final Map<String, NameValuePair> bodies;
  final Map<String, NameValuePair> namedAttachments;
  final List<NameValuePair> unnamedAttachments;

  public LogMessageBean(LogMessage logMessage)
  {
    id = logMessage.getId();
    state = logMessage.getState();
    serviceCategory = logMessage.getServiceCategory();
    serviceName = logMessage.getServiceName();
    logEnterTimestamp = logMessage.getLogEnterTimestamp();
    logLeaveTimestamp = logMessage.getLogLeaveTimestamp();
    messageId = logMessage.getMessageId();
    correlationId = logMessage.getCorrelationId();
    jbpmProcessInstanceId = logMessage.getJbpmProcessInstanceId();
    jbpmTokenId = logMessage.getJbpmTokenId();
    jbpmNodeId = logMessage.getJbpmNodeId();
    faultReason = logMessage.getFaultReason();
    if (logMessage.getFaultCause() != null) {
      final StringBuffer str = new StringBuffer();
      try {
        final char buffer[] = new char[8192];
        int readed;
        final Reader reader = logMessage.getFaultCause().getCharacterStream();

        while ((readed = reader.read(buffer)) > 0) {
          str.append(buffer, 0, readed);
        }
        reader.close();
      } catch (final Exception e) {
      }
      faultCause = str.toString();
    } else {
      faultCause = null;
    }
    properties = new TreeMap<String, NameValuePair>();
    bodies = new TreeMap<String, NameValuePair>();
    namedAttachments = new TreeMap<String, NameValuePair>();
    unnamedAttachments = new ArrayList<NameValuePair>();
    try {
      final StringBuffer encoded = new StringBuffer();
      final char buffer[] = new char[8192];
      int readed;
      final Reader reader = logMessage.getContent().getCharacterStream();

      while ((readed = reader.read(buffer)) > 0) {
        encoded.append(buffer, 0, readed);
      }
      reader.close();

      final Message message = Util.deserialize(Encoding.decodeToObject(encoded.toString()));

      for (final String name : message.getProperties().getNames()) {
        final Object value = message.getProperties().getProperty(name);

        properties.put(name, new NameValuePair(name, value));
      }
      for (final String name : message.getBody().getNames()) {
        final Object value = message.getBody().get(name);

        bodies.put(name, new NameValuePair(name, value));
      }
      for (final String name : message.getAttachment().getNames()) {
        final Object value = message.getAttachment().get(name);

        namedAttachments.put(name, new NameValuePair(name, value));
      }
      for (int i = 0; i < message.getAttachment().getUnnamedCount(); i++) {
        final Object value = message.getAttachment().itemAt(i);

        unnamedAttachments.add(new NameValuePair("", value));
      }

    } catch (final Exception e) {
    }

    tags = new TreeMap<String, NameValuePair>();
    if (logMessage.getTags() != null) {
      for (Map.Entry<String, LogTag> entry : logMessage.getTags().entrySet()) {
        tags.put(entry.getKey(), new NameValuePair(entry.getKey(), entry.getValue().getTagValue()));
      }
    }
  }

  public long getId()
  {
    return id;
  }

  public String getState()
  {
    return state;
  }

  public String getServiceCategory()
  {
    return serviceCategory;
  }

  public String getServiceName()
  {
    return serviceName;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public String getCorrelationId()
  {
    return correlationId;
  }

  public Long getJbpmProcessInstanceId()
  {
    return jbpmProcessInstanceId;
  }

  public Long getJbpmTokenId()
  {
    return jbpmTokenId;
  }

  public Long getJbpmNodeId()
  {
    return jbpmNodeId;
  }

  public boolean isHasProcessInformation()
  {
    return jbpmProcessInstanceId != null && jbpmNodeId != null;
  }

  public String getFaultReason()
  {
    return faultReason;
  }

  public String getFaultCause()
  {
    return faultCause;
  }

  public Date getLogEnterTimestamp()
  {
    return logEnterTimestamp;
  }

  public Date getLogLeaveTimestamp()
  {
    return logLeaveTimestamp;
  }

  public List<NameValuePair> getTagList()
  {
    return new ArrayList<NameValuePair>(tags.values());
  }

  public List<NameValuePair> getPropertyList()
  {
    return new ArrayList<NameValuePair>(properties.values());
  }

  public Object getProperty(String property)
  {
    NameValuePair value = properties.get(property);

    if (value != null) {
      return value.getValue();
    }

    return null;
  }

  public List<NameValuePair> getBodyList()
  {
    return new ArrayList<NameValuePair>(bodies.values());
  }

  public Object getBody(String body)
  {
    NameValuePair value = bodies.get(body);

    if (value != null) {
      return value.getValue();
    }

    return null;
  }

  public List<NameValuePair> getAttachmentList()
  {
    List<NameValuePair> result = new ArrayList<NameValuePair>();

    result.addAll(namedAttachments.values());
    result.addAll(unnamedAttachments);

    return result;
  }

  public Object getAttachment(String attachment)
  {
    NameValuePair value = namedAttachments.get(attachment);

    if (value != null) {
      return value.getValue();
    }

    return null;
  }
}
