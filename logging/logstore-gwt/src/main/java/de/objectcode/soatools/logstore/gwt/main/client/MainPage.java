package de.objectcode.soatools.logstore.gwt.main.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.log.client.LogModule;
import de.objectcode.soatools.logstore.gwt.login.client.LoginService;
import de.objectcode.soatools.logstore.gwt.login.client.LoginServiceAsync;
import de.objectcode.soatools.logstore.gwt.utils.client.Utils;
import de.objectcode.soatools.logstore.gwt.utils.client.ui.SwitchableLayoutPanel;

public class MainPage implements EntryPoint {

	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	@UiField
	MenuBar mainMenu;

	@UiField
	SwitchableLayoutPanel mainPanel;

	@Override
	public void onModuleLoad() {
		loginService.getAuthenticatedUserId(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				Window.alert("Server error: " + caught);
			}

			public void onSuccess(String result) {
				initialize(result);
			}
		});
	}

	void initialize(String userId) {
		final Widget mainPage = uiBinder.createAndBindUi(MainPage.this);

		mainMenu.addItem("Logstore", LogModule.createMenu(mainPanel));

		mainMenu.addSeparator(new UserIdMenuItemSeparator(userId));

		mainMenu.addItem("Logout", new Command() {
			public void execute() {
				loginService.logout(new AsyncCallback<Void>() {

					public void onSuccess(Void result) {
						Utils.redirect("LoginUI.html");
					}

					public void onFailure(Throwable caught) {
					}
				});
			}
		});

		LogModule.showLogPanel(mainPanel);

		RootLayoutPanel.get().add(mainPage);
	}

}
