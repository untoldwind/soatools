package de.objectcode.soatools.util.splitter.actions;

import java.io.Serializable;

import org.jboss.soa.esb.Service;
import org.jboss.soa.esb.message.Message;

public class SplitMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final Service targetService;
	private final Message message;

	public SplitMessage(Service targetService, Message message) {
		this.targetService = targetService;
		this.message = message;
	}

	public Service getTargetService() {
		return targetService;
	}

	public Message getMessage() {
		return message;
	}

}
