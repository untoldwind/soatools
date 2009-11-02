package de.objectcode.soatools.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.actions.ActionProcessingException;

public class LimitedStreamReader implements Runnable
{
  private final static Log LOG = LogFactory.getLog(LimitedStreamReader.class);

  final InputStream inputStream;
  final long limit;
  boolean readed = false;
  byte[] result;

  public LimitedStreamReader(final InputStream inputStream, final long limit)
  {
    this.inputStream = inputStream;
    this.limit = limit;
  }

  public byte[] getResult()
  {
    return this.result;
  }

  public void perform() throws ActionProcessingException
  {
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      final byte[] buffer = new byte[8192];
      int readed;
      long count = 0;

      while ((readed = this.inputStream.read(buffer)) > 0) {
        bos.write(buffer, 0, readed);
        count += readed;

        if (this.limit > 0 && count > this.limit) {
          throw new ActionProcessingException("Exceeded read limit: " + this.limit);
        }
      }
      this.inputStream.close();
      bos.close();

      this.result = bos.toByteArray();
    } catch (final IOException e) {
      throw new ActionProcessingException(e);
    }
  }

  public void run()
  {
    try {
      perform();
    } catch (final Exception e) {
      LOG.warn("Exception <<message: " + e.getMessage() + ">>");
    }
  }
}
