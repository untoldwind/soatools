package de.objectcode.soatools.util.recordset;

import java.io.IOException;
import java.util.Map;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

public class RecordSetXMLReader implements XMLReader {
	protected RecordSet recordSet;	
	protected ContentHandler contentHandler;
	protected DTDHandler dtdHandler;
	protected EntityResolver entityResolver;
	protected ErrorHandler errorHandler;
	
	public RecordSetXMLReader(RecordSet recordSet) {
		this.recordSet = recordSet;
	}

	public RecordSetXMLReader(RecordSet recordSet, ContentHandler contentHandler) {
		this.recordSet = recordSet;
		this.contentHandler = contentHandler;
	}

	public ContentHandler getContentHandler() {
		return contentHandler;
	}

	public DTDHandler getDTDHandler() {
		return dtdHandler;
	}

	public EntityResolver getEntityResolver() {
		return entityResolver;
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public boolean getFeature(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		return false;
	}

	public Object getProperty(String name) throws SAXNotRecognizedException,
			SAXNotSupportedException {
		return null;
	}

	public void setContentHandler(ContentHandler handler) {
		contentHandler = handler;
	}

	public void setDTDHandler(DTDHandler handler) {
		dtdHandler = handler;
	}

	public void setEntityResolver(EntityResolver resolver) {
		entityResolver = resolver;
	}

	public void setErrorHandler(ErrorHandler handler) {
		errorHandler = handler;
	}

	public void setFeature(String name, boolean value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
	}

	public void setProperty(String name, Object value)
			throws SAXNotRecognizedException, SAXNotSupportedException {
	}
	
	public void parse(InputSource input) throws IOException, SAXException {
		writeDocument();
	}

	public void parse(String systemId) throws IOException, SAXException {
		writeDocument();
	}

	protected void writeDocument() throws IOException, SAXException {
		contentHandler.startDocument();

		writeRows(recordSet);
		
		contentHandler.endDocument();
	}
	
	protected void writeRows(RecordSet set) throws IOException, SAXException {
		AttributesImpl attrs = new AttributesImpl();
		for ( Map.Entry<String, String> meta : set.getMetaValues().entrySet() ) {
			attrs.addAttribute(null, meta.getKey(), meta.getKey(), "CDATA", meta.getValue());
		}
		contentHandler.startElement(null, set.getName(), set.getName(), attrs);
		
		for ( RecordRow row : set ) {
			AttributesImpl rowAttrs = new AttributesImpl();
			rowAttrs.addAttribute(null, "row-id", "row-id", "CDATA", row.getId());
			contentHandler.startElement(null, row.getRowType(), row.getRowType(), rowAttrs);
			
			for ( String column : set.getColumns() ) {
				String value = row.getValue(column);
				if ( value != null ) {
					contentHandler.startElement(null, column, column, new AttributesImpl());
					contentHandler.characters(value.toCharArray(), 0, value.length());
					contentHandler.endElement(null, column, column);
				}
			}
			for ( RecordSet subSet : row.getSubSets().values() ) {
				writeRows(subSet);
			}

			contentHandler.endElement(null, row.getRowType(), row.getRowType());
		}
		
		contentHandler.endElement(null, set.getName(), set.getName());
	}
}
