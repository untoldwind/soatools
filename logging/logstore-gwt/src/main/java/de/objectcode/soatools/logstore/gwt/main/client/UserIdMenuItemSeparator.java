package de.objectcode.soatools.logstore.gwt.main.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class UserIdMenuItemSeparator extends MenuItemSeparator {

	public UserIdMenuItemSeparator(String userId) {
		super();
		
		setStyleName("mainMenu-userId");
		Element div = DOM.getChild(getElement(), 0);

		setStyleName(div, "mainMenu-userId");

		Element span1 = DOM.createSpan();
		DOM.appendChild(div, span1);
		DOM.setInnerText(span1, "UserId:");

		Element span2 = DOM.createSpan();
		DOM.appendChild(div, span2);
		DOM.setInnerText(span2, userId);

		setStyleName(span1, "mainMenu-userId-label");
		setStyleName(span2, "mainMenu-userId-value");
	}

}
