package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

public interface IPagedDataProvider<RowClass> {
	void updateDataPage(int pageNumber, IPagedDataViewer<RowClass> viewer);
}
