package de.objectcode.soatools.util.recordset;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class RecordSet extends SAXSource implements Iterable<RecordRow>,
		Serializable {
	private static final long serialVersionUID = 1L;

	String name;
	String type;
	Map<String, String> meta;
	Map<String, Integer> columns;
	Map<String, Map<String, Integer>> columnsByType;

	byte[] rowData;
	int rowCount;

	public RecordSet(String name, String type) {
		this(name, type, new HashMap<String, Map<String, Integer>>());
	}

	RecordSet(RecordSet parent, String name, String type, DataInputStream in)
			throws IOException {
		this.name = name;
		this.type = type;
		columnsByType = parent.columnsByType;
		columns = columnsByType.get(type);

		if (columns == null) {
			columns = new HashMap<String, Integer>();
			columnsByType.put(type, columns);
		}

		rowCount = in.readInt();

		rowData = new byte[in.readInt()];
		in.readFully(rowData);
	}

	protected RecordSet(String name, String type,
			Map<String, Map<String, Integer>> columnsByType) {

		this.name = name;
		this.type = type;
		this.columnsByType = columnsByType;
		this.columns = columnsByType.get(type);

		if (columns == null) {
			columns = new HashMap<String, Integer>();
			columnsByType.put(type, columns);
		}
	}

	public Set<String> getColumns() {
		return new TreeSet<String>(columns.keySet());
	}

	public Iterator<RecordRow> iterator() {
		try {
			final DataInputStream in = new DataInputStream(new GZIPInputStream(
					new ByteArrayInputStream(rowData)));
			final int size = rowCount;

			return new Iterator<RecordRow>() {
				int count = 0;

				public boolean hasNext() {
					return count < size;
				}

				public RecordRow next() {
					if (count >= size)
						return null;

					count++;
					try {
						return new RecordRow(RecordSet.this, in);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				public void remove() {
					throw new RuntimeException("Not available");
				}
			};
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int size() {
		return rowCount;
	}

	protected int getColumnIndex(String column) {
		Integer idx = columns.get(column);

		if (idx == null) {
			idx = columns.size();
			columns.put(column, idx);
		}

		return idx;
	}

	public void setMeta(String key, String value) {
		if (meta == null)
			meta = new HashMap<String, String>();

		if (value != null)
			meta.put(key, value);
		else
			meta.remove(key);
	}

	public String getMeta(String key) {
		return meta.get(key);
	}

	public Map<String, String> getMetaValues() {
		return meta != null ? meta : Collections.<String, String> emptyMap();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	@Override
	public InputSource getInputSource() {
		InputSource inputSource = new InputSource();

		inputSource.setSystemId(name);

		return inputSource;
	}

	@Override
	public XMLReader getXMLReader() {
		return new RecordSetXMLReader(this);
	}

	void writeTo(DataOutputStream out) throws IOException {
		out.writeInt(rowCount);

		out.writeInt(rowData.length);
		out.write(rowData);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecordSet [meta=");
		builder.append(meta);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
}
