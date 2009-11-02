package de.objectcode.soatools.msmq.ws;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DumpServlet extends HttpServlet
{
  private static final long serialVersionUID = -3596075533576535973L;

  private final static Log LOG = LogFactory.getLog(DumpServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    LOG.info("Get: " + req.getPathInfo() + " " + req.getParameterMap());

  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    StringWriter writer = new StringWriter();
    Reader reader = req.getReader();
    char[] buffer = new char[8192];
    int readed;

    while ((readed = reader.read(buffer)) > 0) {
      writer.write(buffer, 0, readed);
    }
    reader.close();
    writer.close();

    LOG.info("Post: " + req.getPathInfo() + " " + writer.toString());
  }
}
