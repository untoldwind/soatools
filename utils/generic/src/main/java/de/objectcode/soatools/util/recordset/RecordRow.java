package de.objectcode.soatools.util.recordset;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordRow {
	RecordSet recordSet;
	int len = 0;
	String id;
	String rowType;
	String[] values = null;
	Map<String, RecordSet> subSets;

	RecordRow(RecordSet recordSet, DataInputStream in) throws IOException {
		this.recordSet = recordSet;

		id = in.readUTF();
		rowType = in.readUTF();
		len = in.readInt();
		values = new String[len];
		for (int i = 0; i < len; i++) {
			if (in.readBoolean())
				values[i] = in.readUTF();
		}

		int subSetCount = in.readInt();

		if (subSetCount > 0) {
			subSets = new HashMap<String, RecordSet>();

			for (int i = 0; i < subSetCount; i++) {
				String subName = in.readUTF();
				String subType = in.readUTF();

				subSets.put(subName, new RecordSet(recordSet, subName, subType,
						in));
			}
		}
	}

	protected RecordRow(RecordSet recordSet, String id, String rowType) {
		this.recordSet = recordSet;
		this.id = id;
		this.rowType = rowType;
	}

	public String getId() {
		return id;
	}
	
	public String getRowType() {
		return rowType;
	}

	public Set<String> getColumns() {
		return recordSet.getColumns();
	}

	public String getValue(String column) {
		int idx = recordSet.getColumnIndex(column);

		if (values == null || idx >= values.length)
			return null;

		return values[idx];
	}

	public RecordSet getSubSet(String subName) {
		if (subSets != null)
			return subSets.get(subName);

		return null;
	}

	public Map<String, RecordSet> getSubSets() {
		return subSets != null ? subSets : Collections
				.<String, RecordSet> emptyMap();
	}
}
