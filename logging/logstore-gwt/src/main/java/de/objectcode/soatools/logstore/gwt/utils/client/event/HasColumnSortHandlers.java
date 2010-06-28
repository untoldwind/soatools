package de.objectcode.soatools.logstore.gwt.utils.client.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasColumnSortHandlers<RowClass> extends HasHandlers{
	HandlerRegistration addColumnSortHandler(ColumnSortHandler<RowClass> handler);

}
