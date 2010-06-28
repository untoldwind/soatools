package de.objectcode.soatools.logstore.gwt.utils.client.ui.datatable;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.utils.client.UtilsClientBundle;
import de.objectcode.soatools.logstore.gwt.utils.client.service.IProjection;

public class BooleanDataTableColumn<RowClass> extends
		WidgetDataTableColumn<RowClass> {

	public BooleanDataTableColumn(String header, String width,
			IProjection<RowClass> projection) {
		super(header, width, projection, null);
	}

	@Override
	public Widget createCellWidget() {
		FlowPanel flow = new FlowPanel();

		flow.add(new Image(UtilsClientBundle.INSTANCE.active()));
		flow.add(new Image(UtilsClientBundle.INSTANCE.inactive()));
		flow.getWidget(0).setVisible(false);
		flow.getWidget(1).setVisible(false);
		return flow;
	}

	@Override
	public void updateCellWidget(Widget widget, RowClass row) {
		FlowPanel flow = (FlowPanel) widget;
		Object value = row != null && projection != null ? projection.project(row) : row;

		if (value == null) {
			flow.getWidget(0).setVisible(false);
			flow.getWidget(1).setVisible(false);
		} else {
			flow.getWidget(0).setVisible((Boolean)value);
			flow.getWidget(1).setVisible(!(Boolean)value);
		}
	}

}
