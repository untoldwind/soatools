package de.objectcode.soatools.logstore.gwt.log.client.ui.filter;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageService;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageServiceAsync;

public class LogMessageTagFilterPanel extends LogMessageFilterComponent {
	private static UI uiBinder = GWT.create(UI.class);

	private final LogMessageServiceAsync logMessageService = GWT
			.create(LogMessageService.class);

	interface UI extends UiBinder<Widget, LogMessageTagFilterPanel> {
	}

	@UiField
	ListBox tagNameList;
	
	@UiField
	SuggestBox tagValueText;

	public LogMessageTagFilterPanel() {
		initWidget(uiBinder.createAndBindUi(this));

		updateTagNameList();
	}

	@Override
	public String getLabel() {
		return "Tag:";
	}

	private void updateTagNameList() {
		logMessageService.getTagNames(new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> tagNames) {
				tagNameList.clear();

				for (String tagName : tagNames)
					tagNameList.addItem(tagName, tagName);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server error: " + caught);
			}
		});
	}

}
