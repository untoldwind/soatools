package de.objectcode.soatools.logstore.gwt.log.client.ui.filter;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;


public class LogMessageServiceFilterPanel extends LogMessageFilterComponent {
	ListBox serviceCategoryList;
	ListBox serviceNameList;
	public LogMessageServiceFilterPanel() {
		HorizontalPanel panel = new HorizontalPanel();

		serviceCategoryList = new ListBox();
		serviceCategoryList.addItem("<any>", "");

		serviceNameList = new ListBox();
		serviceNameList.addItem("<any>", "");

		panel.add(new Label("Category:"));
		panel.add(serviceCategoryList);
		panel.add(new Label("Name:"));
		panel.add(serviceNameList);
		
		initWidget(panel);
	}
	
	@Override
	public String getLabel() {
		return "Service:";
	}

}
