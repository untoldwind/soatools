package de.objectcode.soatools.util.recordset;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatableRecordRow extends RecordRow {

	List<RecordSetBuilder> subSetBuilders;

	UpdatableRecordRow(RecordSet recordSet, String id, String rowType) {
		super(recordSet, id, rowType);
	}

	UpdatableRecordRow(RecordSet recordSet, DataInputStream in)
			throws IOException {
		super(recordSet, in);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setRowType(String rowType) {
		this.rowType = rowType;
	}

	public void setValue(String column, String value) {
		int idx = recordSet.getColumnIndex(column);

		if (values == null) {
			values = new String[idx + 1];
		} else if (values.length <= idx) {
			int newCapacity = (values.length * 3) / 2 + 1;
			if (newCapacity <= idx)
				newCapacity = idx + 1;

			String[] newValues = new String[newCapacity];
			System.arraycopy(values, 0, newValues, 0, values.length);
			values = newValues;
		}

		values[idx] = value;
		len = len > idx + 1 ? len : idx + 1;
	}

	public RecordSetBuilder addSubSet(String name, String type) {
		if (subSets != null && subSets.containsKey(name))
			throw new RuntimeException("Row " + id + " already has subset "
					+ name);

		RecordSetBuilder subSetBuilder = new RecordSetBuilder(recordSet, name,
				type);

		if (subSetBuilders == null)
			subSetBuilders = new ArrayList<RecordSetBuilder>();

		subSetBuilders.add(subSetBuilder);

		return subSetBuilder;
	}

	public RecordSetBuilder updateSubSet(String name) {
		RecordSet subSet = subSets.get(name);

		RecordSetBuilder subSetBuilder = new RecordSetBuilder(subSet);

		if (subSetBuilders == null)
			subSetBuilders = new ArrayList<RecordSetBuilder>();

		subSetBuilders.add(subSetBuilder);

		return subSetBuilder;

	}

	void writeTo(DataOutputStream out) throws IOException {
		if (subSetBuilders != null) {
			if (subSets == null)
				subSets = new HashMap<String, RecordSet>();

			for (RecordSetBuilder subSetBuilder : subSetBuilders) {
				RecordSet subSet = subSetBuilder.build();

				subSets.put(subSet.getName(), subSet);
			}
			subSetBuilders = null;
		}

		out.writeUTF(id);
		out.writeUTF(rowType);
		out.writeInt(len);
		for (int i = 0; i < len; i++) {
			if (values[i] == null)
				out.writeBoolean(false);
			else {
				out.writeBoolean(true);
				out.writeUTF(values[i]);
			}
		}

		if (subSets == null)
			out.writeInt(0);
		else {
			out.writeInt(subSets.size());
			for (Map.Entry<String, RecordSet> entry : subSets.entrySet()) {
				out.writeUTF(entry.getKey());
				out.writeUTF(entry.getValue().getType());

				entry.getValue().writeTo(out);
			}
		}
	}

}
