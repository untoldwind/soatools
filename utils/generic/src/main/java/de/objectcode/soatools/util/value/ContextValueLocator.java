package de.objectcode.soatools.util.value;

import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;

public class ContextValueLocator implements IValueLocator {
	final String contextName;
	final Object defaultValue;

	public ContextValueLocator(String contextName, Object defaultValue) {
		this.contextName = contextName;
		this.defaultValue = defaultValue;
	}

	public Object getValue(Message message) throws ActionProcessingException {
		Object value = message.getContext().getContext(contextName);

		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public void setValue(Message message, Object value)
			throws ActionProcessingException {
		
		message.getContext().setContext(contextName, value);
	}

}
