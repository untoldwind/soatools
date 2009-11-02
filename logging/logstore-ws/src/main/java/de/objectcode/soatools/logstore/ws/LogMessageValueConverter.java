package de.objectcode.soatools.logstore.ws;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.DocumentSource;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import de.objectcode.soatools.mfm.api.normalize.NormalizedData;

@Name("logMessageValueConverter")
@Converter(id = "logMessageValueConverter")
@BypassInterceptors
public class LogMessageValueConverter implements javax.faces.convert.Converter {
	private final static Log LOG = LogFactory
			.getLog(LogMessageValueConverter.class);

	Transformer prettyPrint;

	public LogMessageValueConverter() {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			prettyPrint = factory.newTransformer(new StreamSource(getClass()
					.getResourceAsStream("pretty-print.xsl")));
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Exception", e);
		}
	}

	public Object getAsObject(FacesContext context, UIComponent component,
			String str) {
		throw new RuntimeException("Not implemented");
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (component instanceof HtmlOutputText) {
			((HtmlOutputText) component).setEscape(true);
		}

		if (value == null) {
			return "";
		} else if (value instanceof String
				&& ((String) value).startsWith("<?xml")) {
			if (component instanceof HtmlOutputText) {
				((HtmlOutputText) component).setEscape(false);
			}

			return prettyPrint((String) value);
		} else if (value instanceof byte[]) {
			if (component instanceof HtmlOutputText) {
				((HtmlOutputText) component).setEscape(false);
			}

			return hexDump((byte[]) value);
		} else if (value instanceof NormalizedData) {
			Document document = DocumentFactory.getInstance().createDocument();
			Element root = document.addElement("normalized-data");

			((NormalizedData) value).toXML(root);

			if (component instanceof HtmlOutputText) {
				((HtmlOutputText) component).setEscape(false);
			}

			return prettyPrint(document);
		} else {
			return value.toString();
		}
	}

	String prettyPrint(String value) {
		try {
			StringWriter out = new StringWriter();
			out.append("<pre>");
			prettyPrint.transform(new StreamSource(new StringReader(value)),
					new StreamResult(out));
			out.append("</pre>");
			return out.toString();

		} catch (Exception e) {
			LOG.error("Exception", e);
		}
		return value;
	}

	String prettyPrint(Document document) {
		try {
			StringWriter out = new StringWriter();
			out.append("<pre>");
			prettyPrint.transform(new DocumentSource(document),
					new StreamResult(out));
			out.append("</pre>");

			return out.toString();
		} catch (Exception e) {
			LOG.error("Exception", e);
		}
		return "";
	}

	String hexDump(byte[] data) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);

		out.println("<pre>");
		for (int i = 0; i < data.length; i += 16) {
			out.print(Character.forDigit((i >> 20) & 0xf, 16));
			out.print(Character.forDigit((i >> 16) & 0xf, 16));
			out.print(Character.forDigit((i >> 12) & 0xf, 16));
			out.print(Character.forDigit((i >> 8) & 0xf, 16));
			out.print(Character.forDigit((i >> 4) & 0xf, 16));
			out.print(Character.forDigit(i & 0xf, 16));
			out.print(" :");
			for (int j = 0; j < 16; j++) {
				if (i + j < data.length) {
					out.print(" ");
					out.print(Character.forDigit((data[i + j] >> 4) & 0xf, 16));
					out.print(Character.forDigit(data[i + j] & 0xf, 16));
				} else {
					out.print("   ");
				}
			}
			out.print("  ");
			for (int j = 0; j < 16; j++) {
				if (i + j < data.length) {
					if (data[i + j] >= 32 && data[i + j] < 128) {
						char c = (char) data[i + j];
						if (c == '<') {
							out.print("&lt;");
						} else if (c == '>') {
							out.print("&gt;");
						} else if (c == '\"') {
							out.print("&quot;");
						} else if (c == '&') {
							out.print("&amp;");
						} else {
							out.print(c);
						}
					} else {
						out.print('.');
					}
				}
			}
			out.println();
		}
		out.println("</pre>");

		return writer.toString();
	}

	public static void main(String[] args) {
		try {
			LogMessageValueConverter converter = new LogMessageValueConverter();

			byte[] data = new byte[300];

			Random rand = new Random();
			rand.nextBytes(data);

			System.out.println(converter.hexDump(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
