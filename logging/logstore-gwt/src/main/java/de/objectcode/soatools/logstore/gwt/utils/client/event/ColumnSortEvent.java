package de.objectcode.soatools.logstore.gwt.utils.client.event;

import com.google.gwt.event.shared.GwtEvent;

import de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable.DataTableColumn;

public class ColumnSortEvent<RowClass> extends
		GwtEvent<ColumnSortHandler<RowClass>> {
	private static Type<ColumnSortHandler<?>> TYPE;

	private final int columnIndex;	
	private final boolean ascending;
	private final DataTableColumn<RowClass> sortColumn;

	protected ColumnSortEvent(int columnIndex, boolean ascending, DataTableColumn<RowClass> sortColumn) {
		this.columnIndex = columnIndex;
		this.ascending = ascending;
		this.sortColumn = sortColumn;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}

	public boolean isAscending() {
		return ascending;
	}

	public DataTableColumn<RowClass> getSortColumn() {
		return sortColumn;
	}

	public static <RowClass> void fire(HasColumnSortHandlers<RowClass> source, int columnIndex, boolean ascending, DataTableColumn<RowClass> sortColumn) {
		if (TYPE != null) {
			ColumnSortEvent<RowClass> event = new ColumnSortEvent<RowClass>(columnIndex, ascending, sortColumn);
			source.fireEvent(event);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Type<ColumnSortHandler<RowClass>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ColumnSortHandler<RowClass> handler) {
		handler.onColumnSort(this);
	}

	public static Type<ColumnSortHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<ColumnSortHandler<?>>();
		}
		return TYPE;
	}

}
