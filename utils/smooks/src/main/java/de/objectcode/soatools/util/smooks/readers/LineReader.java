package de.objectcode.soatools.util.smooks.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.milyn.cdr.Parameter;
import org.milyn.cdr.SmooksResourceConfiguration;
import org.milyn.container.ExecutionContext;
import org.milyn.xml.SmooksXMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;

public class LineReader implements SmooksXMLReader
{
  /** Logger for this class. */
  private static final Log LOGGER = LogFactory.getLog(LineReader.class);
  
  private static Attributes EMPTY_ATTRIBS = new AttributesImpl();

  private ContentHandler contentHandler;
  private SmooksResourceConfiguration configuration;
  private ExecutionContext context;

  public void setConfiguration(SmooksResourceConfiguration configuration)
  {
    this.configuration = configuration;
  }

  public void setExecutionContext(ExecutionContext context)
  {
    this.context = context;
  }

  public ContentHandler getContentHandler()
  {
    return contentHandler;
  }

  public DTDHandler getDTDHandler()
  {
    return null;
  }

  public EntityResolver getEntityResolver()
  {
    return null;
  }

  public ErrorHandler getErrorHandler()
  {
    return null;
  }

  public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return false;
  }

  public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException
  {
    return null;
  }

  public void parse(InputSource input) throws IOException, SAXException
  {
    if (contentHandler == null) {
      throw new IllegalStateException("'contentHandler' not set.  Cannot parse string.");
    }
    if (configuration == null) {
      throw new IllegalStateException("CSV to SAX Parser 'configuration' not set.  Cannot parse string.");
    }
    if (context == null) {
      throw new IllegalStateException("Smooks container 'context' not set.  Cannot parse string.");
    }
    
    LOGGER.debug("parse  : @memo contentHandler.getClass() = " + contentHandler.getClass());
    LOGGER.debug("parse  : @memo contentHandler = " + contentHandler);

    Parameter fieldParam = configuration.getParameter("fields");
    List<?> fields = (List<?>) fieldParam.getValue(context.getDeliveryConfig());
    BufferedReader reader = new BufferedReader(input.getCharacterStream());
    String line;
    Iterator<?> it = fields.iterator();
    
    contentHandler.startDocument();
    contentHandler.startElement(null, "fields", "", EMPTY_ATTRIBS);
    while ((line = reader.readLine()) != null && it.hasNext()) {
      String fieldName = it.next().toString();

      LOGGER.debug("parse  : @memo line = " + line);
      LOGGER.debug("parse  : @memo fieldName = " + fieldName);

      contentHandler.startElement(null, fieldName, "", EMPTY_ATTRIBS);
      contentHandler.characters(line.toCharArray(), 0, line.length());
      contentHandler.endElement(null, fieldName, "");
    }
    reader.close();

    contentHandler.endElement(null, "fields", "");
    contentHandler.endDocument();
  }

  public void parse(String systemId) throws IOException, SAXException
  {
    throw new UnsupportedOperationException("Operation not supports by this reader.");
  }

  public void setContentHandler(ContentHandler contentHandler)
  {
    this.contentHandler = contentHandler;
  }

  public void setDTDHandler(DTDHandler handler)
  {
  }

  public void setEntityResolver(EntityResolver resolver)
  {
  }

  public void setErrorHandler(ErrorHandler handler)
  {
  }

  public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException
  {
  }

  public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException
  {
  }

}
