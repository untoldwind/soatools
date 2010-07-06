package de.objectcode.soatools.logstore.gwt.log.client.ui.filter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageService;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageServiceAsync;

public class LogMessageServiceFilterPanel extends
		LogMessageFilterComponent<LogMessageFilter.ServiceCriteria> {
	private static UI uiBinder = GWT.create(UI.class);

	private final LogMessageServiceAsync logMessageService = GWT
			.create(LogMessageService.class);

	interface UI extends UiBinder<Widget, LogMessageServiceFilterPanel> {
	}

	@UiField
	ListBox serviceCategoryList;

	@UiField
	ListBox serviceNameList;

	public LogMessageServiceFilterPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		updateCategoryList();

		criteria = new LogMessageFilter.ServiceCriteria(getServiceCategory(),
				getServiceName());
	}

	@Override
	public String getLabel() {
		return "Service:";
	}

	@UiHandler("serviceCategoryList")
	void handleCategorySelection(ChangeEvent event) {
		updateNameList();

		if (!getServiceCategory().equals(criteria.getServiceCategory())
				|| !getServiceName().equals(criteria.getServiceName())) {
			setValue(new LogMessageFilter.ServiceCriteria(getServiceCategory(),
					getServiceName()), true);
		}
	}

	@UiHandler("serviceNameList")
	void handleNameSelection(ChangeEvent event) {
		if (!getServiceCategory().equals(criteria.getServiceCategory())
				|| !getServiceName().equals(criteria.getServiceName())) {
			setValue(new LogMessageFilter.ServiceCriteria(getServiceCategory(),
					getServiceName()), true);
		}
	}

	private String getServiceCategory() {
		int idx = serviceCategoryList.getSelectedIndex();

		if (idx > 0)
			return serviceCategoryList.getValue(idx);

		return "*";
	}

	private String getServiceName() {
		int idx = serviceNameList.getSelectedIndex();

		if (idx > 0)
			return serviceNameList.getValue(idx);

		return "*";
	}

	private void updateCategoryList() {
		logMessageService
				.getServiceCategories(new AsyncCallback<List<String>>() {
					@Override
					public void onSuccess(List<String> serviceCategories) {
						serviceCategoryList.clear();
						serviceCategoryList.addItem("<any>", "");

						for (String serviceCategory : serviceCategories)
							serviceCategoryList.addItem(serviceCategory,
									serviceCategory);

						serviceNameList.clear();
						serviceNameList.addItem("<any>", "");
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Server error: " + caught);
					}
				});
	}

	private void updateNameList() {
		int idx = serviceCategoryList.getSelectedIndex();

		serviceNameList.clear();
		serviceNameList.addItem("<any>", "");

		if (idx > 0) {
			String serviceCategory = serviceCategoryList.getValue(idx);
			logMessageService.getServiceNames(serviceCategory,
					new AsyncCallback<List<String>>() {
						@Override
						public void onSuccess(List<String> serviceNames) {
							serviceNameList.clear();
							serviceNameList.addItem("<any>", "");

							for (String serviceName : serviceNames)
								serviceNameList.addItem(serviceName,
										serviceName);

						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Server error: " + caught);
						}
					});
		}
	}

}
