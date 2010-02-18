package de.objectcode.soatools.util.splitter.actions;

import java.util.List;
import java.util.Map;

import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;

public interface IMessageCombiner {
	Message combine(Message message, List<Map<String, Object>> parts) throws ActionProcessingException;
}
