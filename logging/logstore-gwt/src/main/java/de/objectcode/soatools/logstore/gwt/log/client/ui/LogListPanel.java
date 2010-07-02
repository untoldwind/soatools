package de.objectcode.soatools.logstore.gwt.log.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.PageSizePager;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.PagingListView;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.SelectionModel.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel.SelectionChangeHandler;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;

public class LogListPanel extends Composite {

	public LogListPanel() {
		VerticalPanel container = new VerticalPanel();
		initWidget(container);

		CellTable<LogMessageSummary> logMessageTable = new CellTable<LogMessageSummary>(
				20);
		setColumns(logMessageTable);
		setSelectionModel(logMessageTable);

		SimplePager<LogMessageSummary> pager = createPager(logMessageTable);

		container.add(logMessageTable);
		container.add(pager);

		loadData(0, logMessageTable.getPageSize(), logMessageTable);
	}

	private void setColumns(CellTable<LogMessageSummary> logMessageTable) {
		logMessageTable.addColumn(new TextColumn<LogMessageSummary>() {
			@Override
			public String getValue(LogMessageSummary logMessage) {
				return logMessage.getId();
			}
		}, new TextHeader("Id"));
		logMessageTable.addColumn(new TextColumn<LogMessageSummary>() {
			@Override
			public String getValue(LogMessageSummary logMessage) {
				return logMessage.getServiceCategory() + " : "
						+ logMessage.getServiceName();
			}
		}, new TextHeader("Service"));
	}

	private void setSelectionModel(CellTable<LogMessageSummary> logMessageTable) {
		final SingleSelectionModel<LogMessageSummary> selectionModel = new SingleSelectionModel<LogMessageSummary>();
		SelectionChangeHandler selectionHandler = new SelectionChangeHandler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				LogMessageSummary logMessage = selectionModel
						.getSelectedObject();
				Window.alert(logMessage.getId());
			}
		};
		selectionModel.addSelectionChangeHandler(selectionHandler);
		logMessageTable.setSelectionEnabled(true);
		logMessageTable.setSelectionModel(selectionModel);
	}

	private SimplePager<LogMessageSummary> createPager(
			CellTable<LogMessageSummary> logMessageTable) {
		SimplePager<LogMessageSummary> pager = new SimplePager<LogMessageSummary>(
				logMessageTable, SimplePager.TextLocation.CENTER) {
			public void onRangeOrSizeChanged(
					PagingListView<LogMessageSummary> listView) {
				loadData(listView.getPageStart(), listView.getPageSize(),
						listView);
				super.onRangeOrSizeChanged(listView);
			}
		};
		return pager;
	}

	private void setDataSize(CellTable<LogMessageSummary> logMessageTable) {
		logMessageTable.setDataSize(1000, true);
	}

	private void loadData(int pageStart, int pageSize,
			PagingListView<LogMessageSummary> logMessageTable) {

		List<LogMessageSummary> page = new ArrayList<LogMessageSummary>();

		for (int i = 0; i < pageSize; i++) {
			page.add(new LogMessageSummary(String.valueOf(pageStart + i),
					"TestCat", "TestName"));
		}

		logMessageTable.setDataSize(1000, true);
		logMessageTable.setData(pageStart, pageSize, page);
	}

}
