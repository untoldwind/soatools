package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class MultiSelDataTable<RowClass> extends DataTable<RowClass> implements
		HasSelectionHandlers<Set<RowClass>> {

	Set<RowClass> currentSelection = new HashSet<RowClass>();

	public MultiSelDataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public MultiSelDataTable(boolean showHeader,
			DataTableColumn<RowClass>... columns) {
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
		for (int i = 0; i < rows.size(); i++) {
			RowClass row = rows.get(i);
			if (row != null && currentSelection.contains(row)) {
				getRowFormatter().setStyleName(i,
						"utils-dataTable-row-selected");
			}
		}
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Set<RowClass>> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	public Set<RowClass> getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(Collection<RowClass> selection, boolean fireEvents) {
		currentSelection.clear();
		currentSelection.addAll(selection);
		
		for (int i = 0; i < rows.size(); i++) {
			RowClass row = rows.get(i);

			if (!currentSelection.contains(row)) {
				getRowFormatter().setStyleName(i, "utils-dataTable-row");
				getRowFormatter().addStyleName(
						i,
						i % 2 == 0 ? "utils-dataTable-row-even"
								: "utils-dataTable-row-odd");
			} else {
				getRowFormatter().setStyleName(i,
						"utils-dataTable-row-selected");
			}
		}

		if (fireEvents) {
			SelectionEvent.fire(this, currentSelection);
		}
	}

	private void onSelection(RowClass row, int rowIndex, boolean fireEvents) {
		if (currentSelection.contains(row)) {
			currentSelection.remove(row);

			getRowFormatter().setStyleName(rowIndex, "utils-dataTable-row");
			getRowFormatter().addStyleName(
					rowIndex,
					rowIndex % 2 == 0 ? "utils-dataTable-row-even"
							: "utils-dataTable-row-odd");
		} else {
			currentSelection.add(row);

			getRowFormatter().setStyleName(rowIndex,
					"utils-dataTable-row-selected");
		}

		if (fireEvents) {
			SelectionEvent.fire(this, currentSelection);
		}
	}
}
