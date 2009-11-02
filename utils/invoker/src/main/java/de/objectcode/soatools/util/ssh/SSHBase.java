package de.objectcode.soatools.util.ssh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.AbstractActionPipelineProcessor;
import org.jboss.soa.esb.helpers.ConfigTree;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public abstract class SSHBase extends AbstractActionPipelineProcessor {

	private final static Log LOG = LogFactory.getLog(SSHBase.class);

	protected String host;
	protected int port;
	protected String knownHostsFile;
	protected String username;
	protected String keyFile;
	protected SSHUserInfo userInfo;
	protected long timeout;
	protected String encoding;

	protected SSHBase(final ConfigTree config) throws ConfigurationException {
		host = config.getRequiredAttribute("host");
		port = (int) config.getLongAttribute("port", 22);
		knownHostsFile = config.getAttribute("known-host-file", System
				.getProperty("user.home")
				+ "/.ssh/known_hosts");
		username = config.getRequiredAttribute("username");
		String password = config.getAttribute("password");
		keyFile = config.getAttribute("key-file");
		String keyPassphrase = config.getAttribute("key-passphrase");
		boolean trustUnknown = config.getBooleanAttribute("trust-unknown",
				false);
		timeout = config.getLongAttribute("timeout", 0);
		encoding = config.getAttribute("encoding");

		userInfo = new SSHUserInfo(password, keyPassphrase, trustUnknown);
	}

	protected Session openSession() throws JSchException {
		JSch jsch = new JSch();
		if (null != keyFile) {
			jsch.addIdentity(keyFile);
		}

		if (knownHostsFile != null) {
			if (LOG.isDebugEnabled())
				LOG.debug("Using known hosts: " + knownHostsFile);
			jsch.setKnownHosts(knownHostsFile);
		}

		Session session = jsch.getSession(username, host, port);
		session.setUserInfo(userInfo);
		if (LOG.isInfoEnabled())
			LOG.info("Connecting to " + host + ":" + port);
		session.setTimeout((int) timeout);
		session.connect();
		return session;

	}
}
