package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class ToManyDataTable<RowClass> extends Composite implements
		IDataViewer<RowClass> {
	private static UI uiBinder = GWT.create(UI.class);

	@SuppressWarnings("unchecked")
	interface UI extends UiBinder<Widget, ToManyDataTable> {
	}

	SingleSelDataTable<RowClass> dataTable;

	MultiSelPagedDataTable<RowClass> candidatesTable;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	PushButton addButton;

	@UiField
	PushButton removeButton;

	public ToManyDataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public ToManyDataTable(boolean showHeader,
			DataTableColumn<RowClass>... columns) {
		initWidget(uiBinder.createAndBindUi(this));

		dataTable = new SingleSelDataTable<RowClass>(showHeader, columns);
		dataTable.setWidth("100%");

		candidatesTable = new MultiSelPagedDataTable<RowClass>(10, columns);

		scrollPanel.add(dataTable);
	}

	public void setData(Collection<RowClass> data) {
		dataTable.setData(data);
	}

	public void setCandidatesDataProvider(IPagedDataProvider<RowClass> provider){
		candidatesTable.setDataProvider(provider);
	}
	
	@UiHandler("addButton")
	protected void onAddClick(ClickEvent event) {
		DialogBox addDialog = createAddDialog(candidatesTable);

		candidatesTable.setCurrentSelection(dataTable.getData(), false);
		
		addDialog.center();
		addDialog.show();
	}

	@UiHandler("removeButton")
	protected void onRemoveClick(ClickEvent event) {

	}

	protected abstract void updateData(Collection<RowClass> data);

	protected DialogBox createAddDialog(
			final MultiSelPagedDataTable<RowClass> candidatesTable) {
		final DialogBox addDialog = new DialogBox(true, true);
		addDialog.setGlassEnabled(true);
		addDialog.setAnimationEnabled(true);

		VerticalPanel dialogPanel = new VerticalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		dialogPanel.add(candidatesTable);
		dialogPanel.add(buttonPanel);

		final PushButton okButton = new PushButton("Ok");

		buttonPanel.add(okButton);
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				updateData(candidatesTable.getCurrentSelection());
				addDialog.hide();
			}

		});

		PushButton cancelButton = new PushButton("Cancel");

		buttonPanel.add(cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				addDialog.hide();
			}
		});

		addDialog.setWidget(dialogPanel);

		return addDialog;
	}
}
