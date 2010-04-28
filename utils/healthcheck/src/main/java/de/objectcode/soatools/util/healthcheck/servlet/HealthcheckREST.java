package de.objectcode.soatools.util.healthcheck.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jboss.soa.esb.client.ServiceInvoker;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.format.MessageFactory;

public class HealthcheckREST extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Log LOGGER = LogFactory.getLog(HealthcheckREST.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		long timeoutMillis = 5000;

		if (req.getParameter("timeout") != null) {
			try {
				timeoutMillis = Long.parseLong(req.getParameter("timeout"));
			} catch (Exception e) {
			}
		}

		Document response = DocumentFactory.getInstance().createDocument();
		Element heathcheck = response.addElement("healthcheck");

		try {
			ServiceInvoker invoker = new ServiceInvoker("Healthcheck",
					"Healthcheck");

			Message request = MessageFactory.getInstance().getMessage();

			Message reply = invoker.deliverSync(request, timeoutMillis);

			String[] names = reply.getBody().getNames();

			Arrays.sort(names);

			for (String name : names) {
				String value = reply.getBody().get(name).toString();

				if (value.startsWith("<?xml")) {
					Document subDocument = new SAXReader()
							.read(new StringReader(value));
					heathcheck.add(subDocument.getRootElement());
				} else {
					heathcheck.addElement(name).addText(value);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception", e);

			heathcheck.addElement("error").addText(e.toString());
		}

		resp.setContentType("application/xml");
		ServletOutputStream out = resp.getOutputStream();
		XMLWriter writer = new XMLWriter(out, OutputFormat
				.createCompactFormat());

		writer.write(response);
		writer.flush();

		out.close();
	}
}
