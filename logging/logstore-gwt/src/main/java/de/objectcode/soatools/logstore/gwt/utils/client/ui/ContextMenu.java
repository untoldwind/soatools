package de.objectcode.soatools.logstore.gwt.utils.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;

public class ContextMenu extends DecoratedPopupPanel {
	private MenuBar menuBar;

	public ContextMenu() {
		super(true, false);

		menuBar = new MenuBar(true);
		setWidget(menuBar);
		setStyleName("utils-MenuBarPopup");
	}

	/**
	 * Adds a menu item to the bar.
	 * 
	 * @param item
	 *            the item to be added
	 * @return the {@link MenuItem} object
	 */
	private MenuItem addItem(ContextMenuItem item) {
		return menuBar.addItem(item);
	}

	/**
	 * Adds a menu item to the bar, that will fire the given command when it is
	 * selected.
	 * 
	 * @param text
	 *            the item's text
	 * @param asHTML
	 *            <code>true</code> to treat the specified text as html
	 * @param cmd
	 *            the command to be fired
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, boolean asHTML, Command cmd) {
		return addItem(new ContextMenuItem(text, asHTML, cmd));
	}

	/**
	 * Adds a menu item to the bar, that will fire the given command when it is
	 * selected.
	 * 
	 * @param text
	 *            the item's text
	 * @param cmd
	 *            the command to be fired
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, Command cmd) {
		return addItem(new ContextMenuItem(text, cmd));
	}

	/**
	 * Adds a thin line to the {@link MenuBar} to separate sections of
	 * {@link MenuItem}s.
	 * 
	 * @return the {@link MenuItemSeparator} object created
	 */
	public MenuItemSeparator addSeparator() {
		return addSeparator(new MenuItemSeparator());
	}

	/**
	 * Adds a thin line to the {@link MenuBar} to separate sections of
	 * {@link MenuItem}s.
	 * 
	 * @param separator
	 *            the {@link MenuItemSeparator} to be added
	 * @return the {@link MenuItemSeparator} object
	 */
	public MenuItemSeparator addSeparator(MenuItemSeparator separator) {
		return menuBar.addSeparator(separator);
	}

	private class ContextMenuItem extends MenuItem implements Command {
		private Command delegatedCommand;

		public ContextMenuItem(String text, boolean asHTML, Command cmd) {
			super(text, asHTML, cmd);

			delegatedCommand = cmd;
			setCommand(this);
		}

		public ContextMenuItem(String text, Command cmd) {
			super(text, cmd);

			delegatedCommand = cmd;
			setCommand(this);
		}

		public void execute() {
			delegatedCommand.execute();

			hide();
		}
	}
}
