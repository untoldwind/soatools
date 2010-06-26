package de.objectcode.soatools.logstore.gwt.login.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login.service")
public interface LoginService extends RemoteService {
	boolean login(String userId, String password);

	void logout();
}
