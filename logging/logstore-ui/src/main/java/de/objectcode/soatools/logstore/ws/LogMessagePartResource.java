package de.objectcode.soatools.logstore.ws;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractResource;

@Scope(ScopeType.APPLICATION)
@Name("logMessagePartResource")
@BypassInterceptors
public class LogMessagePartResource extends AbstractResource {

	Transformer ident;

	public LogMessagePartResource() {
		TransformerFactory factory = TransformerFactory.newInstance();

		try {
			ident = factory.newTransformer();
			ident.setOutputProperty(OutputKeys.METHOD, "xml");
			ident.setOutputProperty(OutputKeys.INDENT, "yes");
			ident.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		} catch (TransformerConfigurationException e) {
		}
	}

	@Override
	public void getResource(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		new ContextualHttpServletRequest(request) {
			@Override
			public void process() throws Exception {
				retrieveMessagePart(request, response);
			}

		}.run();
	}

	private void retrieveMessagePart(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			TransformerException {
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		LogMessageList logMessageList = (LogMessageList) Contexts
				.getConversationContext().get("logMessageList");

		if (type == null || name == null || logMessageList == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		LogMessageDetailBean logMessage = logMessageList.getCurrent();

		if (logMessage == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Object value = null;
		if ("body".equals(type)) {
			value = logMessage.getBody(name);
		} else if ("attachment".equals(type)) {
			value = logMessage.getAttachment(name);
		}
		if (value == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		if (value instanceof Source) {
			response.setContentType("text/xml");
			ident.transform((Source) value, new StreamResult(response
					.getOutputStream()));
		} else {
			response.setContentType("text/xml");
			response.getOutputStream().print(value.toString());
		}
	}

	@Override
	public String getResourcePath() {
		return "/logMessagePart";
	}

}
