package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.utils.client.service.IProjection;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.IFormatter;


public class WidgetDataTableColumn<RowClass> extends DataTableColumn<RowClass> {


	public WidgetDataTableColumn(String header, String width,
			IProjection<RowClass> projection, IFormatter formatter) {
		super(header, width, projection, formatter);
	}

	@Override
	public boolean isWidget() {
		return true;
	}

	public Widget createCellWidget() {
		return new Label();
	}

	public void updateCellWidget(Widget widget, RowClass row) {
		Label label = (Label) widget;

		if (row != null)
			label.setText(row.toString());
		else
			label.setText("");
	}

}
