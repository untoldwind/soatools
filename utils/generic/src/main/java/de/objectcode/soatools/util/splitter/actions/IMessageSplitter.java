package de.objectcode.soatools.util.splitter.actions;

import java.util.List;

import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;

public interface IMessageSplitter {
	List<SplitMessage> split(Message message) throws ActionProcessingException;
}
