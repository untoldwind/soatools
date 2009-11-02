package de.objectcode.soatools.util.ssh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SSHUserInfo implements UserInfo, UIKeyboardInteractive {

	private final static Log LOG = LogFactory.getLog(SSHUserInfo.class);

	private String password;
	private String passphrase;
	private boolean trustUnknown;

	public SSHUserInfo(String password, String passphrase, boolean trustUnknown) {
		this.password = password;
		this.passphrase = passphrase;
		this.trustUnknown = trustUnknown;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public String getPassword() {
		return password;
	}

	public boolean promptPassphrase(String message) {
		return true;
	}

	public boolean promptPassword(String message) {
		return true;
	}

	public boolean promptYesNo(String message) {
		return trustUnknown;
	}

	public void showMessage(String message) {
		LOG.debug(message);
	}

	public String[] promptKeyboardInteractive(String destination, String name,
			String instruction, String[] prompt, boolean[] echo) {
		if (prompt.length != 1 || echo[0] || this.password == null) {
			return null;
		}
		String[] response = new String[1];
		response[0] = this.password;
		return response;
	}

}
