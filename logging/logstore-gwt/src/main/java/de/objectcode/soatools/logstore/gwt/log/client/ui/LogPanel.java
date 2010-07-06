package de.objectcode.soatools.logstore.gwt.log.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class LogPanel extends ResizeComposite {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, LogPanel> {
	}

	public LogPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
