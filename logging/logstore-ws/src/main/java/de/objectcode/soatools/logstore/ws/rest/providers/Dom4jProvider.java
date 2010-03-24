package de.objectcode.soatools.logstore.ws.rest.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jboss.resteasy.plugins.providers.AbstractEntityProvider;
import org.jboss.resteasy.spi.ReaderException;

@Provider
@Produces( { "text/*+xml", "application/*+xml" })
@Consumes( { "text/*+xml", "application/*+xml" })
public class Dom4jProvider extends AbstractEntityProvider<Document> {
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {

		return Document.class.isAssignableFrom(type);
	}

	public Document readFrom(Class<Document> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {

		try {
			return new SAXReader().read(entityStream);
		} catch (Exception e) {
			throw new ReaderException(e);
		}
	}

	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {

		return Document.class.isAssignableFrom(type);
	}

	public void writeTo(Document document, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		XMLWriter writer = new XMLWriter(entityStream, OutputFormat.createCompactFormat());

		writer.write(document);
		writer.flush();
	}

}
