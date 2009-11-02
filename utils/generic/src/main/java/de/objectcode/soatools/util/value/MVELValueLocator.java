package de.objectcode.soatools.util.value;

import java.util.HashMap;
import java.util.Map;

import org.jboss.soa.esb.actions.ActionProcessingException;
import org.jboss.soa.esb.message.Message;
import org.mvel.MVEL;

/**
 * Locate a value using a MVEL expression.
 * 
 * All body parts will be avalailable as MVEL variables.
 * 
 * @author junglas
 */
public class MVELValueLocator implements IValueLocator {
	final String expression;

	public MVELValueLocator(String expression) {
		super();
		this.expression = expression;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getValue(Message message) throws ActionProcessingException {
		Map<String, Object> variables = new HashMap<String, Object>();

		for (String name : message.getBody().getNames()) {
			variables.put(name, message.getBody().get(name));
		}

		Object result = MVEL.eval(expression, message, variables);

		if (result == null) {
			throw new ActionProcessingException(expression
					+ " evaluates to null");
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(Message message, Object value)
			throws ActionProcessingException {
		Map<String, Object> variables = new HashMap<String, Object>();

		for (String name : message.getBody().getNames()) {
			variables.put(name, message.getBody().get(name));
		}
		variables.put("value", value);

		MVEL.eval(expression, message, variables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "MVELValueLocator(" + expression + ")";
	}

}
