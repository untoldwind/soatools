package de.objectcode.soatools.logstore.gwt.log.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.ui.filter.LogMessageFilterComponent;
import de.objectcode.soatools.logstore.gwt.log.client.ui.filter.LogMessageServiceFilterPanel;
import de.objectcode.soatools.logstore.gwt.log.client.ui.filter.LogMessageTagFilterPanel;

public class LogFilterPanel extends Composite {
	FlexTable criteriaTable;
	ListBox newCriteriaList;
	Button addButton;

	LogMessageFilter logMessageFilter;
	List<LogMessageFilterComponent> filterComponents;

	public LogFilterPanel() {
		logMessageFilter = new LogMessageFilter();
		filterComponents = new ArrayList<LogMessageFilterComponent>();

		criteriaTable = new FlexTable();

		newCriteriaList = new ListBox();

		for (LogMessageFilter.CriteriaType criteriaType : LogMessageFilter.CriteriaType
				.values())
			newCriteriaList.addItem(criteriaType.name());

		addButton = new Button();
		addButton.setText("+");
		addButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addCriteria();
			}
		});

		updateCriteriaTable();

		initWidget(criteriaTable);
	}

	private void addCriteria() {
		LogMessageFilter.CriteriaType criteriaType = LogMessageFilter.CriteriaType
				.values()[(newCriteriaList.getSelectedIndex())];

		switch (criteriaType) {
		case SERVICE:
			filterComponents.add(new LogMessageServiceFilterPanel());
			break;
		case TAGVALUE:
			filterComponents.add(new LogMessageTagFilterPanel());
			break;
		}

		updateCriteriaTable();
	}

	private void removeCriteria(LogMessageFilterComponent filterComponent) {
		filterComponents.remove(filterComponent);

		updateCriteriaTable();
	}

	private void updateCriteriaTable() {
		int row = 0;

		for (final LogMessageFilterComponent filterComponent : filterComponents) {
			criteriaTable.setText(row, 0, filterComponent.getLabel());
			criteriaTable.setWidget(row, 1, filterComponent);

			Button removeButton = new Button();
			removeButton.setText("-");
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					removeCriteria(filterComponent);
				}
			});

			criteriaTable.setWidget(row, 2, removeButton);

			row++;
		}

		criteriaTable.setText(row, 0, "Add filter:");
		criteriaTable.setWidget(row, 1, newCriteriaList);
		criteriaTable.setWidget(row, 2, addButton);
	}
}
