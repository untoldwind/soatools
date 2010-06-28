package de.objectcode.soatools.logstore.gwt.log.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;

import de.objectcode.soatools.logstore.gwt.log.client.ui.LogPanel;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.IModuleCallback;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.SwitchableLayoutPanel;

public class LogModule {
	private static LogModule instance = null;

	private LogPanel logPanel = new LogPanel();

	public LogPanel getLogPanel() {
		return logPanel;
	}

	public static void createAsync(
			final IModuleCallback<LogModule> callback) {
		GWT.runAsync(LogModule.class, new RunAsyncCallback() {
			public void onFailure(Throwable err) {
				Window.alert("Client error" + err);
			}

			public void onSuccess() {
				if (instance == null) {
					instance = new LogModule();
				}
				callback.onSuccess(instance);
			}
		});
	}

	public static MenuBar createMenu(final SwitchableLayoutPanel mainPanel) {
		MenuBar webClientMenu = new MenuBar(true);
		webClientMenu.addItem("Web client", new Command() {

			public void execute() {
				showLogPanel(mainPanel);
			}
		});

		return webClientMenu;
	}

	public static void showLogPanel(final SwitchableLayoutPanel mainPanel) {
		mainPanel.prepareSwitch();

		createAsync(new IModuleCallback<LogModule>() {
			public void onSuccess(final LogModule instance) {
				mainPanel.switchWidget(instance.getLogPanel());
			}
		});
	}
}
