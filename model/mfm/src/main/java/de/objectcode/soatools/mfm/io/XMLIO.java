package de.objectcode.soatools.mfm.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.soatools.mfm.api.BooleanType;
import de.objectcode.soatools.mfm.api.Component;
import de.objectcode.soatools.mfm.api.ComponentType;
import de.objectcode.soatools.mfm.api.DateType;
import de.objectcode.soatools.mfm.api.IntegerType;
import de.objectcode.soatools.mfm.api.LongType;
import de.objectcode.soatools.mfm.api.MessageFormat;
import de.objectcode.soatools.mfm.api.MessageFormatModel;
import de.objectcode.soatools.mfm.api.StringType;
import de.objectcode.soatools.mfm.api.TypeRef;

/**
 * Read and write a message format model from/to XML.
 * 
 * @author junglas
 */
public class XMLIO {
	private static final Log LOG = LogFactory.getLog(XMLIO.class);

	public static final XMLIO INSTANCE = new XMLIO();

	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	public XMLIO() {
		try {
			JAXBContext context = JAXBContext.newInstance(ComponentType.class,
					StringType.class, IntegerType.class, LongType.class,
					DateType.class, BooleanType.class, TypeRef.class,
					MessageFormat.class, MessageFormatModel.class,
					Component.class);

			marshaller = context.createMarshaller();
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			LOG.error("Exception", e);
		}
	}

	/**
	 * Read a message format model from a byte-array containing XML.
	 * 
	 * @param in
	 *            The byte array
	 * @return The message format model
	 * @throws IOException
	 *             on error
	 */
	public MessageFormatModel read(byte[] in) throws IOException {
		try {
			return (MessageFormatModel) unmarshaller
					.unmarshal(new ByteArrayInputStream(in));
		} catch (JAXBException e) {
			LOG.error("Exception", e);
			throw new IOException(e.toString());
		}
	}

	/**
	 * Read a message format model from an input stream.
	 * 
	 * @param in
	 *            The input stream
	 * @return The message format model
	 * @throws IOException
	 *             on error
	 */
	public MessageFormatModel read(InputStream in) throws IOException {
		try {
			return (MessageFormatModel) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			LOG.error("Exception", e);
			throw new IOException(e.toString());
		}
	}

	/**
	 * Write a message format model to an output stream.
	 * 
	 * @param messageFormatModel
	 *            The message format model
	 * @param out
	 *            The output stream
	 * @throws IOException
	 *             on error
	 */
	public void write(MessageFormatModel messageFormatModel, OutputStream out)
			throws IOException {
		try {
			marshaller.marshal(messageFormatModel, out);
		} catch (JAXBException e) {
			LOG.error("Exception", e);
			throw new IOException(e.toString());
		}
	}

	/**
	 * Write a message format model to a writer.
	 * 
	 * @param messageFormatModel
	 *            The message format model
	 * @param writer
	 *            The writer
	 * @throws IOException
	 *             on error
	 */
	public void write(MessageFormatModel messageFormatModel, Writer writer)
			throws IOException {
		try {
			marshaller.marshal(messageFormatModel, writer);
		} catch (JAXBException e) {
			LOG.error("Exception", e);
			throw new IOException(e.toString());
		}
	}

	/**
	 * Write a message format model to a xml stream writer.
	 * 
	 * @param messageFormatModel
	 *            The message format model
	 * @param writer
	 *            The xml stream writer
	 * @throws IOException
	 *             on error
	 */
	public void write(MessageFormatModel messageFormatModel,
			XMLStreamWriter writer) throws IOException {
		try {
			marshaller.marshal(messageFormatModel, writer);
		} catch (JAXBException e) {
			LOG.error("Exception", e);
			throw new IOException(e.toString());
		}
	}

}
