package de.objectcode.soatools.util.recordset;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RecordSetBuilder {
	RecordSet recordSet;

	ByteArrayOutputStream rowData;
	DataOutputStream rowDataOut;
	UpdatableRecordRow currentRow;
	int rowCount;

	public RecordSetBuilder(String name, String type) {
		this(new RecordSet(name, type));
	}

	RecordSetBuilder(RecordSet parent, String name, String type) {
		this(new RecordSet(name, type, parent.columnsByType));
	}

	public RecordSetBuilder(RecordSet recordSet) {
		this.recordSet = recordSet;
	}

	public String getName() {
		return recordSet.getName();
	}

	public String getType() {
		return recordSet.getType();
	}

	public void setMeta(String key, String value) {
		recordSet.setMeta(key, value);
	}

	public int getRowCount() {
		return rowCount;
	}

	public Iterator<UpdatableRecordRow> updateRows() {
		if (rowData == null)
			initOutput(false);

		try {
			final DataInputStream in = new DataInputStream(new GZIPInputStream(
					new ByteArrayInputStream(recordSet.rowData)));
			final int size = recordSet.rowCount;

			return new Iterator<UpdatableRecordRow>() {
				int count = 0;

				public boolean hasNext() {
					return count < size;
				}

				public UpdatableRecordRow next() {
					try {
						flush();

						if (count >= size)
							return null;

						count++;

						currentRow = new UpdatableRecordRow(recordSet, in);

						return currentRow;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				public void remove() {
					currentRow = null;
				}
			};
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public UpdatableRecordRow addRow(String id) {
		if (rowData == null)
			initOutput(true);

		flush();

		currentRow = new UpdatableRecordRow(recordSet, id, recordSet.getType());

		return currentRow;
	}

	public UpdatableRecordRow addRowCopy(RecordRow row) {
		if (rowData == null)
			initOutput(true);

		flush();

		currentRow = new UpdatableRecordRow(recordSet, row.getId(), row
				.getRowType());

		for (String column : row.getColumns()) {
			currentRow.setValue(column, row.getValue(column));
		}

		for (RecordSet subSet : row.getSubSets().values()) {
			RecordSetBuilder subSetBuilder = currentRow.addSubSet(subSet
					.getName(), subSet.getType());

			for (RecordRow subSetRow : subSet) {
				subSetBuilder.addRowCopy(subSetRow);
			}
		}

		return currentRow;
	}

	public RecordSet build() {
		flush();

		try {
			if (rowDataOut == null)
				initOutput(false);

			rowDataOut.flush();
			rowDataOut.close();
			recordSet.rowData = rowData.toByteArray();
			recordSet.rowCount = rowCount;

			rowData = null;
			rowDataOut = null;

			return recordSet;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	void initOutput(boolean copyData) {
		try {
			rowData = new ByteArrayOutputStream();
			rowDataOut = new DataOutputStream(new GZIPOutputStream(rowData));
			rowCount = 0;

			if (copyData) {
				rowCount = recordSet.rowCount;

				if (rowCount > 0) {
					GZIPInputStream in = new GZIPInputStream(
							new ByteArrayInputStream(recordSet.rowData));
					byte[] buffer = new byte[2048];
					int readed;

					while ((readed = in.read(buffer)) > 0) {
						rowDataOut.write(buffer, 0, readed);
					}
					in.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	void flush() {
		if (currentRow != null) {
			try {
				currentRow.writeTo(rowDataOut);
				rowCount++;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		currentRow = null;
	}

	public static RecordSet readXml(String name, String type, InputSource source)
			throws ParserConfigurationException, SAXException, IOException {
		RecordSetBuilder builder = new RecordSetBuilder(name, type);
		RecordSetContentHandler handler = new RecordSetContentHandler(builder);

		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

		parser.parse(source, handler);

		return builder.build();
	}
}
