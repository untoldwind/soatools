package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

import de.objectcode.soatools.logstore.gwt.utils.client.event.ColumnSortEvent;
import de.objectcode.soatools.logstore.gwt.utils.client.event.ColumnSortHandler;
import de.objectcode.soatools.logstore.gwt.utils.client.event.DataPageEvent;
import de.objectcode.soatools.logstore.gwt.utils.client.event.DataPageHandler;
import de.objectcode.soatools.logstore.gwt.utils.client.event.HasColumnSortHandlers;
import de.objectcode.soatools.logstore.gwt.utils.client.event.HasDataPageHandlers;

public abstract class PagedDataTable<RowClass> extends Composite implements
		HasDataPageHandlers, HasColumnSortHandlers<RowClass>,
		IPagedDataViewer<RowClass> {

	protected DataPager dataPager;
	protected int pageSize;
	protected IPagedDataProvider<RowClass> dataProvider;
	protected HandlerRegistration dataPagerHandler = null;
	protected HandlerRegistration sortingHandler = null;

	public PagedDataTable(int pageSize, DataTableColumn<RowClass>... columns) {
		this.pageSize = pageSize;

		dataPager = new DataPager();

	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return dataPager.getCurrentPage();
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);

		getDataTable().setWidth(width);
	}

	public int getCurrentSortingIndex() {
		return getDataTable().getCurrentSortingIndex();
	}

	public boolean isCurrentSortingAscending() {
		return getDataTable().isCurrentSortingAscending();
	}

	public DataTableColumn<RowClass> getCurrentSortingColumn() {
		return getDataTable().getCurrentSortingColumn();
	}

	public void setColumnSorting(int columnIndex, boolean ascending,
			boolean fireEvent) {
		getDataTable().setColumnSorting(columnIndex, ascending, fireEvent);
	}
	
	public IPagedDataProvider<RowClass> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(IPagedDataProvider<RowClass> provider) {
		if (dataPagerHandler != null) {
			dataPagerHandler.removeHandler();
			dataPagerHandler = null;
		}
		if (sortingHandler != null ) {
			sortingHandler.removeHandler();
			sortingHandler = null;
		}

		this.dataProvider = provider;

		if (this.dataProvider != null) {
			dataPagerHandler = dataPager
					.addDataPageHandler(new DataPageHandler() {
						public void onDataPage(DataPageEvent event) {
							if (dataProvider != null)
								dataProvider.updateDataPage(event
										.getPageNumber(), PagedDataTable.this);
						}
					});
			sortingHandler = getDataTable().addColumnSortHandler(new ColumnSortHandler<RowClass>() {
				public void onColumnSort(ColumnSortEvent<RowClass> event) {
					if (dataProvider != null)
						dataProvider.updateDataPage(dataPager.getCurrentPage(), PagedDataTable.this);
				}
			});
			this.dataProvider.updateDataPage(dataPager.getCurrentPage(), this);
		}
	}

	public HandlerRegistration addDataPageHandler(DataPageHandler handler) {
		return dataPager.addDataPageHandler(handler);
	}

	public HandlerRegistration addColumnSortHandler(
			ColumnSortHandler<RowClass> handler) {
		return getDataTable().addColumnSortHandler(handler);
	}

	protected abstract DataTable<RowClass> getDataTable();
}
