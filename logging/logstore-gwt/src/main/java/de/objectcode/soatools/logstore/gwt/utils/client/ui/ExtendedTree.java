package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasContextMenuHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class ExtendedTree extends Tree implements HasWidgets,
		HasAnimation, HasAllKeyHandlers,
		HasAllFocusHandlers, HasSelectionHandlers<TreeItem>,
		HasOpenHandlers<TreeItem>, HasCloseHandlers<TreeItem>,
		 HasAllMouseHandlers, HasDoubleClickHandlers,
		HasContextMenuHandlers {

	private ContextMenu contextMenu;
	
	public ExtendedTree() {
		super();
	}

	
	public ExtendedTree(Resources resources, boolean useLeafImages) {
		super(resources, useLeafImages);
	}

	public ExtendedTree(Resources resources) {
		super(resources);
	}

	public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
		return addDomHandler(handler, ContextMenuEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}
	public void setContextMenu(ContextMenu menu) {
		if ( this.contextMenu == null ) {
			contextMenu = menu;
			
			addContextMenuHandler(new ContextMenuHandler() {			
				public void onContextMenu(ContextMenuEvent event) {
					event.preventDefault();
					final int x = event.getNativeEvent().getClientX();
					final int y = event.getNativeEvent().getClientY();

					contextMenu.setPopupPosition(x, y);
					contextMenu.show();
				}
			});
		}
	}

}
