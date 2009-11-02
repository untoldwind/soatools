package de.objectcode.soatools.logstore.ws.esb;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.internal.soa.esb.util.Encoding;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.util.Util;

import de.objectcode.soatools.logstore.persistent.EsbMessage;
import de.objectcode.soatools.logstore.ws.NameValuePair;


public class EsbDeadLetterDetailBean extends EsbDeadLetterBean
{
  private static final long serialVersionUID = -4012515044163392835L;

  private final static Log LOG = LogFactory.getLog(EsbDeadLetterDetailBean.class);

  String call;
  Map<String, NameValuePair> properties;
  Map<String, NameValuePair> bodies;
  Map<String, NameValuePair> namedAttachments;
  List<NameValuePair> unnamedAttachments;

  public EsbDeadLetterDetailBean(EsbMessage deadLetter)
  {
    super(deadLetter);

    try {
      final StringBuffer contentBuffer = new StringBuffer();
      final char buffer[] = new char[8192];
      int readed;
      final Reader reader = deadLetter.getMessage().getCharacterStream();

      while ((readed = reader.read(buffer)) > 0) {
        contentBuffer.append(buffer, 0, readed);
      }
      reader.close();

      Message message = Util.deserialize(Encoding.decodeToObject(contentBuffer.toString()));

      properties = new TreeMap<String, NameValuePair>();
      bodies = new TreeMap<String, NameValuePair>();
      namedAttachments = new TreeMap<String, NameValuePair>();
      unnamedAttachments = new ArrayList<NameValuePair>();

      if (message.getHeader().getCall() != null) {
        call = message.getHeader().getCall().toString();
      }
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
    } catch (Exception e) {
      LOG.error("Exception", e);
    }
  }

  public String getCall()
  {
    return call;
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
