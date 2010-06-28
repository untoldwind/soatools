package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

import de.objectcode.soatools.logstore.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.soatools.logstore.gwt.utils.client.event.ColumnSortHandler;
import de.objectcode.soatools.logstore.gwt.utils.client.event.HasColumnSortHandlers;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.ContextMenu;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.ExtendedFlexTable;

public class DataTable<RowClass> extends ExtendedFlexTable implements
		HasColumnSortHandlers<RowClass>, HasDoubleClickHandlers,
		HasContextMenuHandlers, IDataViewer<RowClass> {

	protected DataTableColumn<RowClass>[] columns;
	protected List<RowClass> rows = new ArrayList<RowClass>();
	protected int currentSortingIndex;
	protected boolean currentSortingAscending;
	protected DataTableColumn<RowClass> currentSortingColumn;
	protected ContextMenu contextMenu;

	public DataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public DataTable(boolean showHeader, DataTableColumn<RowClass>... columns) {
		this.columns = columns;

		setStyleName("utils-dataTable");

		if (showHeader) {
			super.setHeaders(columns);

			setHeaderStyleName("utils-dataTable-header");
			for (int i = 0; i < columns.length; i++)
				if (columns[i].isSortable())
					setHeaderStyleName(i, "utils-dataTable-header-sortable");
		}
	}

	public void addRow(RowClass row) {
		int rowNum = rows.size();

		rows.add(row);

		for (int i = 0; i < columns.length; i++) {
			DataTableColumn<RowClass> column = columns[i];

			if (column.isWidget()) {
				WidgetDataTableColumn<RowClass> widgetColumn = (WidgetDataTableColumn<RowClass>) column;

				setWidget(rowNum, i, widgetColumn.createCellWidget());
				widgetColumn.updateCellWidget(getWidget(rowNum, i), row);
			} else {
				TextDataTableColumn<RowClass> textColumn = (TextDataTableColumn<RowClass>) column;

				setText(rowNum, i, textColumn.getCellText(row));
			}
		}
		getRowFormatter().setStyleName(rowNum, "utils-dataTable-row");
		getRowFormatter().addStyleName(
				rowNum,
				rowNum % 2 == 0 ? "utils-dataTable-row-even"
						: "utils-dataTable-row-odd");
	}

	public Collection<RowClass> getData() {
		return rows;
	}

	public void setData(Collection<RowClass> rows) {
		removeAllRows();

		for (RowClass row : rows)
			addRow(row);
	}

	public void setFixedRowCount(int rowCount) {
		rows.clear();

		for (int rowNum = 0; rowNum < rowCount; rowNum++) {
			rows.add(null);

			for (int i = 0; i < columns.length; i++) {
				DataTableColumn<RowClass> column = columns[i];

				if (column.isWidget()) {
					WidgetDataTableColumn<RowClass> widgetColumn = (WidgetDataTableColumn<RowClass>) column;

					setWidget(rowNum, i, widgetColumn.createCellWidget());
				} else {
					setText(rowNum, i, "");
				}
			}
			getRowFormatter().setStyleName(rowNum, "utils-dataTable-row-empty");
		}
	}

	public void setRow(int rowNum, RowClass row) {
		rows.set(rowNum, row);

		for (int i = 0; i < columns.length; i++) {
			DataTableColumn<RowClass> column = columns[i];

			if (column.isWidget()) {
				WidgetDataTableColumn<RowClass> widgetColumn = (WidgetDataTableColumn<RowClass>) column;

				widgetColumn.updateCellWidget(getWidget(rowNum, i), row);
			} else {
				if (row != null) {
					TextDataTableColumn<RowClass> textColumn = (TextDataTableColumn<RowClass>) column;

					setText(rowNum, i, textColumn.getCellText(row));
				} else {
					setText(rowNum, i, "");
				}
			}
		}
		getRowFormatter().setStyleName(
				rowNum,
				row != null ? "utils-dataTable-row"
						: "utils-dataTable-row-empty");
		if (row != null) {
			getRowFormatter().addStyleName(
					rowNum,
					rowNum % 2 == 0 ? "utils-dataTable-row-even"
							: "utils-dataTable-row-odd");
		}
	}

	@Override
	public void removeAllRows() {
		super.removeAllRows();
		rows.clear();
	}

	public void setColumnSorting(int columnIndex, boolean ascending,
			boolean fireEvent) {
		currentSortingIndex = columnIndex;
		currentSortingAscending = ascending;

		if (currentSortingIndex >= 0 && currentSortingIndex < columns.length
				&& columns[currentSortingIndex].isSortable()) {
			currentSortingColumn = columns[currentSortingIndex];

			for (int i = 0; i < columns.length; i++) {
				if (columns[i].isSortable()) {
					if (i == currentSortingIndex) {
						setHeaderStyleName(
								i,
								currentSortingAscending ? "utils-dataTable-header-ascending"
										: "utils-dataTable-header-descending");
					} else {
						setHeaderStyleName(i, "utils-dataTable-header-sortable");
					}
				}
			}

			if (fireEvent)
				ColumnSortEvent.<RowClass> fire(this, currentSortingIndex,
						currentSortingAscending, currentSortingColumn);
		}

	}

	public int getCurrentSortingIndex() {
		return currentSortingIndex;
	}

	public boolean isCurrentSortingAscending() {
		return currentSortingAscending;
	}

	public DataTableColumn<RowClass> getCurrentSortingColumn() {
		return currentSortingColumn;
	}

	public void setContextMenu(ContextMenu menu) {
		if (this.contextMenu == null) {
			contextMenu = menu;

			addContextMenuHandler(new ContextMenuHandler() {
				public void onContextMenu(ContextMenuEvent event) {
					event.preventDefault();
					final int x = event.getNativeEvent().getClientX();
					final int y = event.getNativeEvent().getClientY();

					contextMenu.setPopupPosition(x, y);
					contextMenu.show();
				}
			});
		}
	}

	public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
		return addDomHandler(handler, ContextMenuEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	public HandlerRegistration addColumnSortHandler(
			ColumnSortHandler<RowClass> handler) {
		return addHandler(handler, ColumnSortEvent.getType());
	}

	protected void onSort(int columnIndex) {
		setColumnSorting(columnIndex, currentSortingIndex != columnIndex
				|| !currentSortingAscending, true);
	}
}
