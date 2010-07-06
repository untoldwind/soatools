package de.objectcode.soatools.logstore.gwt.log.client.ui.filter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;

import de.objectcode.soatools.logstore.gwt.log.client.service.LogMessageFilter;

public abstract class LogMessageFilterComponent<T extends LogMessageFilter.Criteria>
		extends Composite implements HasValue<LogMessageFilter.Criteria> {
	
	protected T criteria;
	
	public abstract String getLabel();

	@Override
	public T getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(LogMessageFilter.Criteria criteria, boolean fireEvents) {
		setValue(criteria);
		
		if ( fireEvents )
			ValueChangeEvent.fire(this, criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(LogMessageFilter.Criteria criteria) {
		this.criteria = (T)criteria;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<LogMessageFilter.Criteria> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
