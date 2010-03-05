package de.objectcode.soatools.logstore.ws.jbm;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.jboss.soa.esb.message.Message;

import de.objectcode.soatools.logstore.persistent.LogJmsDeadLetter;
import de.objectcode.soatools.logstore.ws.NameValuePair;


public class JbmDeadLetterDetailBean extends JbmDeadLetterBean
{
  private static final long serialVersionUID = 7675338065274091610L;

  private final static Log LOG = LogFactory.getLog(JbmDeadLetterDetailBean.class);

  private final String content;
  boolean esbMessage;
  String call;
  Map<String, NameValuePair> properties;
  Map<String, NameValuePair> bodies;
  Map<String, NameValuePair> namedAttachments;
  List<NameValuePair> unnamedAttachments;

  public JbmDeadLetterDetailBean(LogJmsDeadLetter deadLetter)
  {
    super(deadLetter);

    final StringBuffer contentBuffer = new StringBuffer();
    try {
      final char buffer[] = new char[8192];
      int readed;
      final Reader reader = deadLetter.getContent().getCharacterStream();

      while ((readed = reader.read(buffer)) > 0) {
        contentBuffer.append(buffer, 0, readed);
      }
      reader.close();

    } catch (Exception e) {
      LOG.error("Exception", e);
    }

    content = contentBuffer.toString();

    esbMessage = false;
    if (content.indexOf("org.jboss.internal.soa.esb.message.format") > 0) {
      properties = new TreeMap<String, NameValuePair>();
      bodies = new TreeMap<String, NameValuePair>();
      namedAttachments = new TreeMap<String, NameValuePair>();
      unnamedAttachments = new ArrayList<NameValuePair>();

      try {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new StringReader(content));

        Object data = document.selectObject("string(jms-message/object-body)");

        if (data != null) {
          byte[] serData = Base64.decodeBase64(data.toString().getBytes());
          Object ser = SerializationUtils.deserialize(serData);

          if (ser != null && ser instanceof Message) {
            Message message = (Message) ser;

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
            esbMessage = true;
          }
        }
      } catch (Exception e) {
      }
    }
  }

  public String getContent()
  {
    return content;
  }

  public boolean isEsbMessage()
  {
    return esbMessage;
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
