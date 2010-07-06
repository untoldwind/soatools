package de.objectcode.soatools.logstore.gwt.log.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter.Criteria;
import de.objectcode.soatools.logstore.gwt.log.client.ui.filter.LogMessageFilterComponent;
import de.objectcode.soatools.logstore.gwt.log.client.ui.filter.LogMessageServiceFilterPanel;
import de.objectcode.soatools.logstore.gwt.log.client.ui.filter.LogMessageTagFilterPanel;

public class LogFilterPanel extends Composite implements
		HasValue<LogMessageFilter>,
		ValueChangeHandler<LogMessageFilter.Criteria> {
	FlexTable criteriaTable;
	ListBox newCriteriaList;
	Button addButton;

	LogMessageFilter logMessageFilter;
	List<LogMessageFilterComponent<?>> filterComponents;

	public LogFilterPanel() {
		logMessageFilter = new LogMessageFilter();
		filterComponents = new ArrayList<LogMessageFilterComponent<?>>();

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

	@Override
	public LogMessageFilter getValue() {
		return logMessageFilter;
	}

	@Override
	public void setValue(LogMessageFilter logMessageFilter, boolean fireEvent) {
		setValue(logMessageFilter);

		if (fireEvent)
			ValueChangeEvent.fire(this, logMessageFilter);
	}

	@Override
	public void setValue(LogMessageFilter logMessageFilter) {
		this.logMessageFilter = logMessageFilter;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<LogMessageFilter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void onValueChange(ValueChangeEvent<Criteria> event) {
		List<LogMessageFilter.Criteria> critierias = new ArrayList<LogMessageFilter.Criteria>();

		for (LogMessageFilterComponent<?> filterComponent : filterComponents) {
			if (filterComponent.getValue() != null)
				critierias.add(filterComponent.getValue());
		}

		setValue(new LogMessageFilter(critierias), true);
	}

	private void addCriteria() {
		LogMessageFilter.CriteriaType criteriaType = LogMessageFilter.CriteriaType
				.values()[(newCriteriaList.getSelectedIndex())];

		LogMessageFilterComponent<?> newFilterComponent = null;
		switch (criteriaType) {
		case SERVICE:
			newFilterComponent = new LogMessageServiceFilterPanel();
			break;
		case TAGVALUE:
			newFilterComponent = new LogMessageTagFilterPanel();
			break;
		}

		if (newFilterComponent != null) {
			filterComponents.add(newFilterComponent);
			newFilterComponent.addValueChangeHandler(this);

			updateCriteriaTable();
		}
	}

	private void removeCriteria(LogMessageFilterComponent<?> toRemove) {
		filterComponents.remove(toRemove);

		updateCriteriaTable();

		List<LogMessageFilter.Criteria> critierias = new ArrayList<LogMessageFilter.Criteria>();

		for (LogMessageFilterComponent<?> filterComponent : filterComponents) {
			if (filterComponent.getValue() != null)
				critierias.add(filterComponent.getValue());
		}

		setValue(new LogMessageFilter(critierias), true);
	}

	private void updateCriteriaTable() {
		int row = 0;

		for (final LogMessageFilterComponent<?> filterComponent : filterComponents) {
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
