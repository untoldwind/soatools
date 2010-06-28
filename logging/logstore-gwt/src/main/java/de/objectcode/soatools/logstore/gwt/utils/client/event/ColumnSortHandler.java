package de.objectcode.soatools.logstore.gwt.utils.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ColumnSortHandler<RowClass> extends EventHandler {
	void onColumnSort(ColumnSortEvent<RowClass> event);
}
