package de.objectcode.soatools.util.ssh;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.soa.esb.ConfigurationException;
import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.listeners.message.MessageDeliverException;
import org.jboss.soa.esb.message.Message;
import org.jboss.soa.esb.message.MessagePayloadProxy;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import de.objectcode.soatools.util.LimitedByteArrayOutputStream;
import de.objectcode.soatools.util.value.IValueLocator;
import de.objectcode.soatools.util.value.ValueLocatorFactory;

public class SSHExec extends SSHBase {

	private final static Log LOG = LogFactory.getLog(SSHExec.class);

	MessagePayloadProxy payload;
	IValueLocator commandValueLocator;
	List<IValueLocator> parameterValueLocators;

	public SSHExec(ConfigTree config) throws ConfigurationException {
		super(config);

		commandValueLocator = ValueLocatorFactory.INSTANCE
				.createValueLocator(config.getFirstChild("command"));

		parameterValueLocators = new ArrayList<IValueLocator>();

		for (ConfigTree param : config.getChildren("param")) {
			parameterValueLocators.add(ValueLocatorFactory.INSTANCE
					.createValueLocator(param));
		}

		payload = new MessagePayloadProxy(config);
	}

	public Message process(Message message) throws ActionProcessingException {

		StringBuffer command = new StringBuffer(commandValueLocator.getValue(
				message).toString());

		for (IValueLocator parameter : parameterValueLocators) {
			command.append(" ");
			command.append(parameter.getValue(message));
		}

		Session session = null;
		try {
			session = openSession();

			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand(command.toString());

			LimitedByteArrayOutputStream stdout = new LimitedByteArrayOutputStream();
			LimitedByteArrayOutputStream stderr = new LimitedByteArrayOutputStream();

			channelExec.setOutputStream(stdout);
			channelExec.setExtOutputStream(stderr);
			channelExec.connect();

			synchronized (stdout) {
				if (!stdout.isClosed()) {
					stdout.wait(timeout);
				}
			}

			if (channelExec.isConnected()) {
				channelExec.disconnect();
				throw new ActionProcessingException("Connection timed out");
			}

			channelExec.disconnect();

			if (encoding != null) {
				payload.setPayload(message, new String(stdout.toByteArray(),
						encoding));
				if (stderr.getCount() > 0) {
					message.getAttachment().addItem(
							new String(stderr.toByteArray(), encoding));
				}
			} else {
				payload.setPayload(message, stdout.toByteArray());
				if (stderr.getCount() > 0) {
					message.getAttachment().addItem(stderr.toByteArray());
				}
			}

		} catch (JSchException e) {
			LOG.error("Exception", e);
			throw new ActionProcessingException(e);
		} catch (InterruptedException e) {
			LOG.error("Exception", e);
			throw new ActionProcessingException(e);
		} catch (MessageDeliverException e) {
			LOG.error("Exception", e);
			throw new ActionProcessingException(e);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Exception", e);
			throw new ActionProcessingException(e);
		} finally {
			if (session != null && session.isConnected())
				session.disconnect();
		}

		return message;
	}
}
