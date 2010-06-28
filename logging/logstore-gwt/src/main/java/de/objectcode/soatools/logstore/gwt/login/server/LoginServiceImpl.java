package de.objectcode.soatools.logstore.gwt.login.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.soatools.logstore.gwt.login.client.LoginService;
import de.objectcode.soatools.logstore.gwt.utils.server.GwtController;

@Controller
@RequestMapping( { "/LoginUI/login.service", "/MainUI/login.service" })
public class LoginServiceImpl extends GwtController implements LoginService {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean login(String userId, String password) {
		System.out.println(">>> Loging in: " + userId);
		return true;
	}

	@Override
	public void logout() {
		System.out.println("Logout");
	}

	@Override
	public String getAuthenticatedUserId() {
		// TODO
		return "admin";
	}

}
