package de.objectcode.soatools.logstore.gwt.login.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.soatools.logstore.gwt.utils.client.Utils;

public class LoginPanel extends Composite {
	private static UI uiBinder = GWT.create(UI.class);

	interface UI extends UiBinder<Widget, LoginPanel> {
	}

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	@UiField
	TextBox userId;

	@UiField
	PasswordTextBox password;

	@UiField
	Button login;

	public LoginPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiHandler("userId")
	void onUserIdChange(KeyUpEvent event) {
		login.setEnabled(userId.getValue().length() > 0
				&& password.getValue().length() > 0);
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			password.setFocus(true);
		}
	}

	@UiHandler("password")
	void onPasswordChange(KeyUpEvent event) {
		login.setEnabled(userId.getValue().length() > 0
				&& password.getValue().length() > 0);
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			login();
		}
	}

	@UiHandler("login")
	void onLoginClick(ClickEvent event) {
		login();
	}

	private void login() {
		loginService.login(userId.getValue(), password.getValue(),
				new AsyncCallback<Boolean>() {
					public void onSuccess(Boolean result) {
						if (result)
							loginSuccessful();
						else
							loginFailed();
					}

					public void onFailure(Throwable caught) {
					}
				});
	}

	private void loginFailed() {
		Window.alert("Login failed");
	}

	private void loginSuccessful() {
		Utils.redirect("MainUI.html");
	}
}