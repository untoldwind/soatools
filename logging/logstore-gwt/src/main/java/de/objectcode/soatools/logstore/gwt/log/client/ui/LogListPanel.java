package de.objectcode.soatools.logstore.gwt.log.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.ListView;
import com.google.gwt.view.client.ListView.Delegate;
import com.google.gwt.view.client.SelectionModel.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel.SelectionChangeHandler;
import com.google.gwt.view.client.SingleSelectionModel;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessagePagingData;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageService;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageServiceAsync;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageSummary;

public class LogListPanel extends Composite {
	private final LogMessageServiceAsync logMessageService = GWT
			.create(LogMessageService.class);

	public LogListPanel() {
		DockLayoutPanel container = new DockLayoutPanel(Unit.EM);
		initWidget(container);

		CellTable<LogMessageSummary> logMessageTable = new CellTable<LogMessageSummary>(
				20);
		setColumns(logMessageTable);
		setSelectionModel(logMessageTable);
		logMessageTable.setDelegate(new Delegate<LogMessageSummary>() {
			@Override
			public void onRangeChanged(ListView<LogMessageSummary> view) {
				loadData(view.getRange().getStart(), view.getRange()
						.getLength(), view);
			}
		});

		container.addSouth(new SimplePager<LogMessageSummary>(logMessageTable,
				SimplePager.TextLocation.CENTER), 2);
		container.add(new ScrollPanel(logMessageTable));

		logMessageTable.refresh();
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

	private void loadData(int pageStart, int pageSize,
			final ListView<LogMessageSummary> logMessageTable) {

		logMessageService.getLogMessages(null, pageStart, pageSize,
				new AsyncCallback<LogMessagePagingData>() {
					@Override
					public void onSuccess(
							LogMessagePagingData logMessagePagingData) {
						logMessageTable.setDataSize(
								logMessagePagingData.getDataSize(), true);
						logMessageTable.setData(
								logMessagePagingData.getPageStart(),
								logMessagePagingData.getPageStart(),
								logMessagePagingData.getLogMessages());
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);

					}
				});
	}

}
