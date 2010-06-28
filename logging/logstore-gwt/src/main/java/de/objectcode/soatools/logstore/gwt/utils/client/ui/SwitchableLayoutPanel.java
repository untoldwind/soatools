package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class SwitchableLayoutPanel extends LayoutPanel {

	private Label loadingLabel;

	public SwitchableLayoutPanel() {
		setStyleName("utils-switchableLayoutPanel");

		loadingLabel = new Label();
		loadingLabel.setStyleName("utils-loadingLabel");
	}

	public void prepareSwitch() {
		clear();
		
		add(loadingLabel);
		setWidgetLeftRight(loadingLabel, 0, Unit.PX, 0, Unit.PX);
		setWidgetTopBottom(loadingLabel, 0, Unit.PX, 0, Unit.PX);
	}

	public void switchWidget(Widget widget) {
		clear();
		
		add(widget);
		setWidgetLeftRight(widget, 0, Unit.PX, 0, Unit.PX);
		setWidgetTopBottom(widget, 0, Unit.PX, 0, Unit.PX);
	}
}
