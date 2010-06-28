package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.user.client.ui.Label;

public class LoadingLabel extends Label {
	public LoadingLabel() {
		super("Loading...");
		
		setStyleName("utils-loadingLabel");
	}
}
