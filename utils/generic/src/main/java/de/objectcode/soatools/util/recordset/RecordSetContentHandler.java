package de.objectcode.soatools.util.recordset;

import java.io.StringWriter;
import java.util.Deque;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RecordSetContentHandler extends DefaultHandler {
	Deque<Context> stack;
	String currentElement;
	StringWriter textContent;

	public RecordSetContentHandler(RecordSetBuilder builder) {
		stack = new LinkedList<Context>();
		stack.push(new Context(builder));
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Context context = stack.peek();

		if (context.getCurrentRow() != null) {
			if ( currentElement == null ) {
				textContent = new StringWriter();
				currentElement = qName;
			} else {
				RecordSetBuilder subBuilder = context.getCurrentRow().addSubSet(currentElement, qName);
				Context subContext = new Context(subBuilder);
				stack.push(subContext);
				subContext.nextRow(attributes.getValue("object-id"));
				currentElement = null;
			}
		} else if (context.getBuilder().getName().equals(qName)) {
			for (int i = 0; i < attributes.getLength(); i++)
				context.getBuilder().setMeta(attributes.getQName(i),
						attributes.getValue(i));
		} else if (context.getBuilder().getType().equals(qName)) {
			context.nextRow(attributes.getValue("object-id"));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		Context context = stack.peek();

		if (context.getCurrentRow() != null && currentElement != null) {
			if (currentElement.equals(qName)) {
				textContent.flush();

				context.getCurrentRow().setValue(currentElement,
						textContent.toString());

				textContent = null;
				currentElement = null;
			}
		} else if (context.getBuilder().getType().equals(qName)) {
			textContent = null;
			context.finishRow();
		} else if ( context.getBuilder().getName().equals(qName)) {
			stack.pop();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (textContent != null)
			textContent.write(ch, start, length);
	}

	static class Context {
		RecordSetBuilder builder;
		UpdatableRecordRow currentRow;

		public Context(RecordSetBuilder builder) {
			this.builder = builder;
		}

		public RecordSetBuilder getBuilder() {
			return builder;
		}

		public UpdatableRecordRow getCurrentRow() {
			return currentRow;
		}

		public void nextRow(String id) {
			currentRow = builder.addRow(id != null ? id : "");
		}

		public void finishRow() {
			currentRow = null;
		}
	}
}
