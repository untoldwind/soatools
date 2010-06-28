package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class SingleSelDataTable<RowClass> extends DataTable<RowClass> implements
		HasSelectionHandlers<RowClass> {

	RowClass currentSelection;
	int currentSelectionRowIndex;

	public SingleSelDataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public SingleSelDataTable(boolean showHeader, DataTableColumn<RowClass>... columns) {
		super(showHeader, columns);
		
		addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cell cell = getCellForEvent(event);
				if (cell != null) {
					int rowNum = cell.getRowIndex();

					if (rowNum == -1)
						onSort(cell.getCellIndex());
					else if (rowNum >= 0 && rowNum < rows.size()) {
						RowClass row = rows.get(rowNum);

						onSelection(row, rowNum, true);
					}
				}
			}
		});
	}

	public void updateSelection() {
		currentSelectionRowIndex = -1;

		for (int i = 0; i < rows.size(); i++) {
			RowClass row = rows.get(i);
			if (row != null && row.equals(currentSelection)) {
				getRowFormatter().setStyleName(i,
						"utils-dataTable-row-selected");
				currentSelectionRowIndex = i;
			}
		}
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<RowClass> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	public RowClass getCurrentSelection() {
		return currentSelection;
	}

	private void onSelection(RowClass row, int rowIndex, boolean fireEvents) {
		if (currentSelection != null && currentSelectionRowIndex >= 0) {
			getRowFormatter().setStyleName(currentSelectionRowIndex,
					"utils-dataTable-row");
			getRowFormatter()
					.addStyleName(
							currentSelectionRowIndex,
							currentSelectionRowIndex % 2 == 0 ? "utils-dataTable-row-even"
									: "utils-dataTable-row-odd");
		}
		currentSelection = row;
		currentSelectionRowIndex = rowIndex;

		if (currentSelection != null) {
			// Select the item and fire the selection event.
			getRowFormatter().setStyleName(rowIndex,
					"utils-dataTable-row-selected");
			if (fireEvents) {
				SelectionEvent.fire(this, currentSelection);
			}
		}
	}
}
