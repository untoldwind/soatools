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
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageService;
import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageServiceAsync;

public class LogMessageTagFilterPanel extends
		LogMessageFilterComponent<LogMessageFilter.TagCriteria> {
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

		criteria = new LogMessageFilter.TagCriteria(getTagName(), getTagValue());
	}

	@Override
	public String getLabel() {
		return "Tag:";
	}

	@UiHandler("tagNameList")
	void handleTagNameChange(ChangeEvent event) {
		if (!getTagName().equals(criteria.getTagName())
				|| !getTagValue().equals(criteria.getTagValue())) {
			setValue(new LogMessageFilter.TagCriteria(getTagName(),
					getTagValue()), true);
		}
	}

	private String getTagName() {
		int idx = tagNameList.getSelectedIndex();

		if (idx > 0)
			return tagNameList.getValue(idx);

		return null;
	}

	private String getTagValue() {
		String tagValue = tagValueText.getText();

		if (tagValue != null && tagValue.length() > 0)
			return tagValue;

		return "*";
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
